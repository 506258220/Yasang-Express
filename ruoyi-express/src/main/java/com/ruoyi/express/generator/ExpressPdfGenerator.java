package com.ruoyi.express.generator;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.ruoyi.express.domain.ExpressWaybill;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 76×130mm一联快递单PDF生成工具（高利用率布局，充分填充页面）
 */
public class ExpressPdfGenerator {
    // 面单核心尺寸（76mm×130mm 换算为pt：1mm=2.835pt）
    private static final float PAGE_WIDTH = 76 * 2.835f;   // ≈215.46pt
    private static final float PAGE_HEIGHT = 130 * 2.835f; // ≈368.55pt
    // 品牌主色（可替换为对应快递色：顺丰红/申通红/圆通蓝等）
    private static final BaseColor MAIN_COLOR = new BaseColor(208, 2, 27);
    private static final BaseColor TEXT_COLOR = BaseColor.BLACK;
    // 中文字体（解决乱码，优先使用系统宋体）
    private static final String FONT_NAME = "STSongStd-Light";
    private static final String FONT_ENCODING = "UniGB-UCS2-H";
    // 全局极小边距（最大化利用页面）
    private static final float GLOBAL_MARGIN = 2f;

    public byte[] createExpressPdf(ExpressWaybillPrint waybill,
                                           Map<String, Boolean> fieldsConfig,
                                           Map<String, Object> printConfig) throws DocumentException, IOException {
        // 1. 初始化PDF文档（76×130mm + 极小边距）
        Rectangle pageSize = new Rectangle(PAGE_WIDTH, PAGE_HEIGHT);
        Document document = new Document(pageSize, GLOBAL_MARGIN, GLOBAL_MARGIN, GLOBAL_MARGIN, GLOBAL_MARGIN);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();

        // 2. 初始化字体（适配小尺寸+高利用率）
        BaseFont baseFont = BaseFont.createFont(FONT_NAME, FONT_ENCODING, BaseFont.EMBEDDED);
        Font tinyFont = new Font(baseFont, 6.5f, Font.NORMAL, TEXT_COLOR);    // 超小字体（备注/打印源）
        Font smallFont = new Font(baseFont, 7.5f, Font.NORMAL, TEXT_COLOR);   // 小字体（地址/辅助信息）
        Font normalFont = new Font(baseFont, 8.5f, Font.NORMAL, TEXT_COLOR);  // 常规字体（姓名/电话）
        Font boldFont = new Font(baseFont, 9f, Font.BOLD, MAIN_COLOR);        // 加粗标题（品牌/字段名）
        Font barcodeFont = new Font(baseFont, 10f, Font.BOLD, TEXT_COLOR);    // 单号字体

        // ===================== 3. 顶部通栏（占15%高度，无空白） =====================
        PdfPTable topBar = new PdfPTable(3);
        topBar.setWidths(new float[]{30, 40, 30}); // 左30%:品牌 中40%:面单类型 右30%:时间
        topBar.setWidthPercentage(100);
        topBar.setSpacingAfter(1f); // 极小间距

        // 3.1 左侧：品牌标识
        if (fieldsConfig.getOrDefault("brand", true)) {
            PdfPCell brandCell = new PdfPCell(new Paragraph("速运快递", boldFont));
            brandCell.setBorder(Rectangle.NO_BORDER);
            brandCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            brandCell.setPadding(0.5f);
            topBar.addCell(brandCell);
        } else {
            topBar.addCell(emptyCell());
        }

        // 3.2 中间：面单类型
        if (fieldsConfig.getOrDefault("billType", true)) {
            PdfPCell typeCell = new PdfPCell(new Paragraph("电子面单 | 一联单", normalFont));
            typeCell.setBorder(Rectangle.NO_BORDER);
            typeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            typeCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            typeCell.setPadding(0.5f);
            topBar.addCell(typeCell);
        } else {
            topBar.addCell(emptyCell());
        }

        // 3.3 右侧：打印时间（自动填充当前时间）
        if (fieldsConfig.getOrDefault("printTime", true)) {
            String printTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
            PdfPCell timeCell = new PdfPCell(new Paragraph("打印时间：" + printTime, tinyFont));
            timeCell.setBorder(Rectangle.NO_BORDER);
            timeCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            timeCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            timeCell.setPadding(0.5f);
            topBar.addCell(timeCell);
        } else {
            topBar.addCell(emptyCell());
        }
        document.add(topBar);

        // ===================== 4. 条码核心区（占20%高度，填充整宽） =====================
        PdfPTable barcodeArea = new PdfPTable(1);
        barcodeArea.setWidthPercentage(100);
        barcodeArea.setSpacingAfter(1.5f);

        if (fieldsConfig.getOrDefault("waybillNo", true)) {
            String waybillNo = waybill.getWaybillNo() == null ? "" : waybill.getWaybillNo();
            // 4.1 条形码（最大化宽度，高度适配）
            Barcode128 barcode = new Barcode128();
            barcode.setCode(waybillNo);
            barcode.setSize(8);       // 条码字体适配
            barcode.setBaseline(6);   // 基线调整
            barcode.setBarHeight(28); // 条码高度（占满区域）
            barcode.setX(1.2f);       // 条码宽度密度
            Image barcodeImg = barcode.createImageWithBarcode(writer.getDirectContent(), TEXT_COLOR, TEXT_COLOR);
            barcodeImg.setAlignment(Element.ALIGN_CENTER);
            PdfPCell barcodeCell = new PdfPCell(barcodeImg);
            barcodeCell.setBorder(Rectangle.NO_BORDER);
            barcodeCell.setPadding(1f);
            barcodeArea.addCell(barcodeCell);

            // 4.2 快递单号（条码下方居中，大号字体）
            PdfPCell noCell = new PdfPCell(new Paragraph("快递单号：" + waybillNo, barcodeFont));
            noCell.setBorder(Rectangle.NO_BORDER);
            noCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            noCell.setPadding(0.5f);
            barcodeArea.addCell(noCell);
        }
        document.add(barcodeArea);

        // ===================== 5. 寄收件核心区（占45%高度，左右分栏填充） =====================
        PdfPTable contactArea = new PdfPTable(2);
        contactArea.setWidths(new float[]{50, 50}); // 左右等分，无间隙
        contactArea.setWidthPercentage(100);
        contactArea.setSpacingAfter(1.5f);

        // 5.1 左侧：寄件人信息（填充整列，无空白）
        if (fieldsConfig.getOrDefault("sender", true)) {
            PdfPTable senderTable = createContactTable("寄件人信息",
                    waybill.getSenderName(), waybill.getSenderPhone(), waybill.getSenderPostcode(), waybill.getSenderAddress(),
                    boldFont, normalFont, smallFont);
            PdfPCell senderCell = new PdfPCell(senderTable);
            senderCell.setBorderColor(MAIN_COLOR);
            senderCell.setBorderWidth(0.5f); // 细边框区分
            senderCell.setPadding(1f);
            contactArea.addCell(senderCell);
        } else {
            contactArea.addCell(emptyCell());
        }

        // 5.2 右侧：收件人信息（填充整列，无空白）
        if (fieldsConfig.getOrDefault("consignee", true)) {
            PdfPTable consigneeTable = createContactTable("收件人信息",
                    waybill.getConsignee(), waybill.getConsigneePhone(), waybill.getConsigneePostcode(), waybill.getConsigneeAddress(),
                    boldFont, normalFont, smallFont);
            PdfPCell consigneeCell = new PdfPCell(consigneeTable);
            consigneeCell.setBorderColor(MAIN_COLOR);
            consigneeCell.setBorderWidth(0.5f); // 细边框区分
            consigneeCell.setPadding(1f);
            contactArea.addCell(consigneeCell);
        } else {
            contactArea.addCell(emptyCell());
        }
        document.add(contactArea);

        // ===================== 6. 辅助信息区（占15%高度，4列填充） =====================
        PdfPTable assistArea = new PdfPTable(4);
        assistArea.setWidths(new float[]{25, 25, 25, 25}); // 4列等分
        assistArea.setWidthPercentage(100);
        assistArea.setSpacingAfter(1f);

        // 6.1 物品类型
        if (fieldsConfig.getOrDefault("goodsInfo", true)) {
            String goods = waybill.getGoodsType() == null ? "未知" : waybill.getGoodsType();
            assistArea.addCell(createAssistCell("物品：", goods, tinyFont));
        } else {
            assistArea.addCell(emptyCell());
        }

        // 6.2 重量
        if (fieldsConfig.getOrDefault("weight", true)) {
            String weight = waybill.getWeight() == null ? "0kg" : waybill.getWeight() + "kg";
            assistArea.addCell(createAssistCell("重量：", weight, tinyFont));
        } else {
            assistArea.addCell(emptyCell());
        }

        // 6.3 运费
        if (fieldsConfig.getOrDefault("freight", true)) {
            String freight = waybill.getFreight() == null ? "0元" : waybill.getFreight() + "元";
            assistArea.addCell(createAssistCell("运费：", freight, tinyFont));
        } else {
            assistArea.addCell(emptyCell());
        }

        // 6.4 订单号
        if (fieldsConfig.getOrDefault("orderNo", true)) {
            String orderNo = waybill.getOrderNo() == null ? "" : waybill.getOrderNo();
            assistArea.addCell(createAssistCell("订单号：", orderNo, tinyFont));
        } else {
            assistArea.addCell(emptyCell());
        }
        document.add(assistArea);

        // ===================== 7. 底部通栏（占5%高度，填充整宽） =====================
        PdfPTable bottomBar = new PdfPTable(3);
        bottomBar.setWidths(new float[]{25, 50, 25}); // 左25%:验视 中50%:备注 右25%:打印源
        bottomBar.setWidthPercentage(100);

        // 7.1 左侧：已验视
        if (fieldsConfig.getOrDefault("checked", true)) {
            PdfPCell checkCell = new PdfPCell(new Paragraph("✓ 已验视", tinyFont));
            checkCell.setBorder(Rectangle.NO_BORDER);
            checkCell.setPadding(0.5f);
            bottomBar.addCell(checkCell);
        } else {
            bottomBar.addCell(emptyCell());
        }

        // 7.2 中间：备注
        if (fieldsConfig.getOrDefault("remark", true)) {
            String remark = waybill.getRemark() == null ? "无备注" : waybill.getRemark();
            PdfPCell remarkCell = new PdfPCell(new Paragraph("备注：" + remark, tinyFont));
            remarkCell.setBorder(Rectangle.NO_BORDER);
            remarkCell.setPadding(0.5f);
            bottomBar.addCell(remarkCell);
        } else {
            bottomBar.addCell(emptyCell());
        }

        // 7.3 右侧：打印源
        if (fieldsConfig.getOrDefault("printSource", true)) {
            PdfPCell sourceCell = new PdfPCell(new Paragraph("打印源：系统打印", tinyFont));
            sourceCell.setBorder(Rectangle.NO_BORDER);
            sourceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            sourceCell.setPadding(0.5f);
            bottomBar.addCell(sourceCell);
        } else {
            bottomBar.addCell(emptyCell());
        }
        document.add(bottomBar);

        // 8. 资源释放
        document.close();
        writer.close();
        baos.close();
        return baos.toByteArray();
    }

    /**
     * 构建寄/收件人信息子表格（填充整列，无空白）
     */
    private PdfPTable createContactTable(String title, String name, String phone, String postcode, String address,
                                         Font titleFont, Font normalFont, Font smallFont) {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);

        // 标题行
        PdfPCell titleCell = new PdfPCell(new Paragraph(title, titleFont));
        titleCell.setBorder(Rectangle.BOTTOM);
        titleCell.setBorderWidth(0.5f);
        titleCell.setPadding(0.5f);
        table.addCell(titleCell);

        // 姓名+电话+邮编（一行紧凑显示）
        String namePhone = (name == null ? "" : "姓名：" + name) + " | " + (phone == null ? "" : "电话：" + phone);
        if (postcode != null && !postcode.isEmpty()) {
            namePhone += " | 邮编：" + postcode;
        }
        PdfPCell nameCell = new PdfPCell(new Paragraph(namePhone, normalFont));
        nameCell.setBorder(Rectangle.NO_BORDER);
        nameCell.setPadding(0.5f);
        table.addCell(nameCell);

        // 地址（自动换行，填充剩余空间）
        String addrText = address == null ? "" : "地址：" + address;
        Paragraph addrPara = new Paragraph(addrText, smallFont);
        addrPara.setLeading(9f); // 行间距适配
        PdfPCell addrCell = new PdfPCell(addrPara);
        addrCell.setBorder(Rectangle.NO_BORDER);
        addrCell.setPadding(0.5f);
        table.addCell(addrCell);

        return table;
    }

    /**
     * 构建辅助信息单元格（字段名+内容，紧凑显示）
     */
    private PdfPCell createAssistCell(String label, String content, Font font) {
        Paragraph para = new Paragraph(label + content, font);
        PdfPCell cell = new PdfPCell(para);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(0.5f);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    /**
     * 空单元格（占位，无样式）
     */
    private PdfPCell emptyCell() {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(0);
        return cell;
    }
}
