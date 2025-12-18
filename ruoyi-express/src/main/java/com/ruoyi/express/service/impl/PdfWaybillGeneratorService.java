package com.ruoyi.express.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.ruoyi.common.utils.JsonUtils;
import com.ruoyi.express.domain.ExpressTemplate;
import com.ruoyi.express.domain.ExpressWaybill;
import com.ruoyi.express.generator.ExpressPdfGenerator;
import com.ruoyi.express.generator.ExpressWaybillPrint;
import com.ruoyi.express.service.IExpressTemplateService;
import com.ruoyi.express.service.IExpressWaybillService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 * PDF快递单生成服务类
 *
 * @author ruoyi
 * @date 2025-12-17
 */
@Service
public class PdfWaybillGeneratorService {

    @Autowired
    private IExpressTemplateService expressTemplateService;

    @Autowired
    private IExpressWaybillService expressWaybillService;

    /**
     * 生成快递单PDF
     *
     * @param waybillId 快递单ID
     * @return PDF字节数组
     * @throws Exception 生成异常
     */
    public byte[] generateWaybillPdf(Long waybillId, Long templateId) throws Exception {
        // 获取快递单信息
        ExpressWaybill waybill = expressWaybillService.selectExpressWaybillByWaybillId(waybillId);
        if (waybill == null) {
            throw new IllegalArgumentException("快递单不存在");
        }

        // 获取模板信息
        ExpressTemplate template = expressTemplateService.selectExpressTemplateByTemplateId(templateId);
        if (template == null) {
            throw new IllegalArgumentException("模板不存在");
        }

        // 解析模板配置
        Map<String, Boolean> fieldsConfig = JsonUtils.parseObject(template.getFieldsConfig(), Map.class);
        Map<String, Object> printConfig = JsonUtils.parseObject(template.getPrintConfig(), Map.class);

        ExpressPdfGenerator expressPdfGenerator = new ExpressPdfGenerator();
        ExpressWaybillPrint expressWaybillPrint = new ExpressWaybillPrint();
        BeanUtils.copyProperties(waybill, expressWaybillPrint);
        return expressPdfGenerator.createExpressPdf(expressWaybillPrint, fieldsConfig, printConfig);
        // 生成PDF
//        return createPdf(waybill, template, fieldsConfig, printConfig);
    }

    /**
     * 创建PDF文档
     *
     * @param waybill      快递单信息
     * @param template     模板信息
     * @param fieldsConfig 字段配置
     * @param printConfig  打印配置
     * @return PDF字节数组
     * @throws Exception 生成异常
     */
    private byte[] createPdf(ExpressWaybill waybill, ExpressTemplate template,
                           Map<String, Boolean> fieldsConfig, Map<String, Object> printConfig) throws Exception {
        // 创建文档对象
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, baos);

        // 打开文档
        document.open();

        // 设置字体
        BaseFont baseFont = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font font = new Font(baseFont, 12);
        Font boldFont = new Font(baseFont, 14, Font.BOLD);

        // 添加标题
        Paragraph title = new Paragraph("快递单", boldFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        // 生成条形码
        if (fieldsConfig.getOrDefault("waybillNo", true)) {
            Barcode128 barcode = new Barcode128();
            barcode.setCode(waybill.getWaybillNo());
            barcode.setSize(12);
            barcode.setBaseline(10);
            barcode.setBarHeight(60);
            barcode.setX(2);
            Image barcodeImage = barcode.createImageWithBarcode(writer.getDirectContent(), null, null);
            barcodeImage.setAlignment(Element.ALIGN_CENTER);
            document.add(barcodeImage);
            document.add(Chunk.NEWLINE);
        }

        // 添加快递单号
        if (fieldsConfig.getOrDefault("waybillNo", true)) {
            Paragraph waybillNo = new Paragraph("快递单号: " + waybill.getWaybillNo(), font);
            waybillNo.setAlignment(Element.ALIGN_CENTER);
            document.add(waybillNo);
            document.add(Chunk.NEWLINE);
        }

        // 添加收货人信息
        if (fieldsConfig.getOrDefault("consignee", true)) {
            Paragraph consignee = new Paragraph("收货人: " + waybill.getConsignee(), font);
            document.add(consignee);
        }

        if (fieldsConfig.getOrDefault("phone", true)) {
            Paragraph phone = new Paragraph("联系电话: " + waybill.getConsigneePhone(), font);
            document.add(phone);
        }

        if (fieldsConfig.getOrDefault("address", true)) {
            Paragraph address = new Paragraph("收货地址: " + waybill.getConsigneeAddress(), font);
            document.add(address);
        }

        // 添加物品信息
        if (fieldsConfig.getOrDefault("goodsInfo", true)) {
            Paragraph goodsInfo = new Paragraph("物品类型: " + waybill.getGoodsType(), font);
            document.add(goodsInfo);
        }

        // 添加重量信息
        if (fieldsConfig.getOrDefault("weight", true)) {
            Paragraph weight = new Paragraph("重量: " + waybill.getWeight() + " kg", font);
            document.add(weight);
        }

        // 添加订单号
        if (fieldsConfig.getOrDefault("orderNo", true)) {
            Paragraph orderNo = new Paragraph("订单编号: " + waybill.getOrderNo(), font);
            document.add(orderNo);
        }

        // 关闭文档
        document.close();
        writer.close();

        return baos.toByteArray();
    }
}
