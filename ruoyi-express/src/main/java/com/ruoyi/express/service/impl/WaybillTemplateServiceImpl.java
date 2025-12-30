
// 实现类
package com.ruoyi.express.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.express.mapper.WaybillTemplateMapper;
import com.ruoyi.express.domain.WaybillTemplate;
import com.ruoyi.express.service.IWaybillTemplateService;
import com.ruoyi.express.domain.ExpressOrder;
import com.ruoyi.express.service.IExpressOrderService;

/**
 * 面单模板Service业务层处理
 *
 * @author 全栈开发者
 * @date 2025-01-01
 */
@Service
public class WaybillTemplateServiceImpl implements IWaybillTemplateService
{
    @Autowired
    private WaybillTemplateMapper waybillTemplateMapper;

    @Autowired
    private IExpressOrderService expressOrderService;

    /**
     * 查询面单模板
     */
    @Override
    public WaybillTemplate selectWaybillTemplateById(Long id)
    {
        return waybillTemplateMapper.selectWaybillTemplateById(id);
    }

    /**
     * 查询面单模板列表
     */
    @Override
    public List<WaybillTemplate> selectWaybillTemplateList(WaybillTemplate waybillTemplate)
    {
        return waybillTemplateMapper.selectWaybillTemplateList(waybillTemplate);
    }

    /**
     * 新增面单模板
     */
    @Override
    public int insertWaybillTemplate(WaybillTemplate waybillTemplate)
    {
        return waybillTemplateMapper.insertWaybillTemplate(waybillTemplate);
    }

    /**
     * 修改面单模板
     */
    @Override
    public int updateWaybillTemplate(WaybillTemplate waybillTemplate)
    {
        return waybillTemplateMapper.updateWaybillTemplate(waybillTemplate);
    }

    /**
     * 批量删除面单模板
     */
    @Override
    public int deleteWaybillTemplateByIds(Long[] ids)
    {
        return waybillTemplateMapper.deleteWaybillTemplateByIds(ids);
    }

    /**
     * 删除面单模板信息
     */
    @Override
    public int deleteWaybillTemplateById(Long id)
    {
        return waybillTemplateMapper.deleteWaybillTemplateById(id);
    }

    /**
     * 预览面单模板：解析JSON生成完整HTML预览
     */
    @Override
    public WaybillTemplate previewWaybillTemplate(Long id) {
        WaybillTemplate template = selectWaybillTemplateById(id);
        if (template == null || StringUtils.isEmpty(template.getTemplateJson())) {
            return null;
        }

        // 解析纸张尺寸
        String[] paperSize = template.getPaperSize().split("\\*");
        String width = paperSize[0].replace("mm", "");
        String height = paperSize.length > 1 ? paperSize[1].replace("mm", "") : "130";

        // 构建完整的HTML预览，包含必要的CSS和JavaScript库
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html lang='zh-CN'>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<title>面单模板预览</title>");
        html.append("<style>");
        html.append("body { margin: 0; padding: 20px; font-family: Arial, sans-serif; background: #f5f7fa; }");
        html.append(".preview-container { width: 100%; max-width: 800px; margin: 0 auto; }");
        html.append(".waybill-preview { width: " + width + "mm; height: " + height + "mm; margin: 0 auto; background: #fff; box-shadow: 0 0 15px rgba(0, 0, 0, 0.1); position: relative; overflow: hidden; border: 1px solid #000; }");
        html.append(".canvas-element { position: absolute; border: 1px dashed transparent; cursor: move; z-index: 10; }");

        // 文本元素样式
        html.append(".elem-text { display: flex; align-items: center; }");
        html.append(".elem-text .content { outline: none; }");

        // 时间元素样式
        html.append(".elem-time { display: flex; align-items: center; font-size: 14px; color: #000; }");

        // 条形码元素样式
        html.append(".elem-barcode { display: flex; justify-content: center; align-items: center; position: relative; }");
        html.append(".elem-barcode canvas { max-width: 100%; max-height: 100%; }");

        // 二维码元素样式
        html.append(".elem-qrcode { display: flex; justify-content: center; align-items: center; }");
        html.append(".elem-qrcode canvas { max-width: 100%; max-height: 100%; }");

        // 水平线和垂直线样式
        html.append(".elem-hline { overflow: hidden; }");
        html.append(".elem-hline-inner { background: #000; height: 1px; }");
        html.append(".elem-vline { overflow: hidden; }");
        html.append(".elem-vline-inner { background: #000; width: 1px; height: 100%; }");

        // 图片元素样式
        html.append(".elem-image { display: flex; justify-content: center; align-items: center; }");
        html.append(".elem-image img { max-width: 100%; max-height: 100%; }");

        // 图标元素样式
        html.append(".elem-icon { display: flex; justify-content: center; align-items: center; font-size: 24px; }");

        // 形状元素样式
        html.append(".elem-shape { border: 1px solid #000; background: #f0f0f0; }");

        // 斜线元素样式
        html.append(".elem-diagonal { position: relative; overflow: hidden; }");
        html.append(".elem-diagonal::after { content: ''; position: absolute; width: 100%; height: 100%; border-bottom: 1px solid #000; transform: rotate(45deg); transform-origin: center center; }");

        // 画布尺寸显示
        html.append(".canvas-size-info { position: absolute; top: 0; right: 0; background: rgba(0, 0, 0, 0.7); color: #fff; padding: 2px 6px; font-size: 10px; border-radius: 0 0 0 4px; z-index: 20; }");

        html.append("</style>");

        // 引入必要的JavaScript库
        html.append("<script src='https://cdn.jsdelivr.net/npm/jsbarcode@3.11.5/dist/JsBarcode.all.min.js'></script>");
        html.append("<script src='https://cdn.jsdelivr.net/npm/qrcode@1.5.3/build/qrcode.min.js'></script>");

        html.append("</head>");
        html.append("<body>");
        html.append("<div class='preview-container'>");
        html.append("<div class='waybill-preview'>");
        html.append("<div class='canvas-size-info'>" + width + "mm × " + height + "mm</div>");

        // 渲染LOGO
        if (StringUtils.isNotEmpty(template.getLogoUrl())) {
            html.append("<img src='").append(template.getLogoUrl()).append("' style='position:absolute;top:5px;right:5px;width:30mm;height:15mm;'/> ");
        }

        try {
            // 构建测试数据映射，用于替换模板中的占位符
            Map<String, String> orderDataMap = buildOrderDataMap();

            // 尝试解析为数组格式
            JSONArray elements = JSON.parseArray(template.getTemplateJson());
            if (elements != null && elements.size() > 0) {
                // 渲染每个元素
                for (int i = 0; i < elements.size(); i++) {
                    JSONObject elem = elements.getJSONObject(i);
                    String type = elem.getString("type");
                    if (type == null) continue;

                    double x = elem.getDoubleValue("x");
                    double y = elem.getDoubleValue("y");
                    double elemWidth = elem.getDoubleValue("width");
                    double elemHeight = elem.getDoubleValue("height");
                    String content = elem.getString("content");
                    int zIndex = elem.getIntValue("zIndex");

                    // 替换占位符内容
                    if (content != null) {
                        content = replacePlaceholders(content, orderDataMap);
                    }

                    // 开始构建元素HTML
                    html.append("<div class='canvas-element elem-").append(type).append("' ");
                    html.append("style='position:absolute;");
                    html.append("left:").append(x).append("mm;");
                    html.append("top:").append(y).append("mm;");
                    html.append("width:").append(elemWidth).append("mm;");
                    html.append("height:").append(elemHeight).append("mm;");
                    html.append("z-index:").append(zIndex).append(";");
                    html.append("'>");

                    // 根据元素类型渲染不同内容
                    switch (type) {
                        case "text":
                            html.append("<div class='content' style='width: 100%; height: 100%; display: flex; align-items: center;'>");
                            html.append(content != null ? content : "");
                            html.append("</div>");
                            break;
                        case "time":
                            // 时间元素，显示当前时间作为示例
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            html.append("<div style='width: 100%; height: 100%; display: flex; align-items: center;'>");
                            html.append(sdf.format(new Date()));
                            html.append("</div>");
                            break;
                        case "barcode":
                            // 条形码元素
                            html.append("<canvas id='barcode_").append(i).append("' style='width: 100%; height: 100%;'></canvas>");
                            // 添加初始化脚本
                            html.append("<script>");
                            html.append("window.addEventListener('load', function() {");
                            html.append("    JsBarcode('#barcode_").append(i).append("', '").append(content != null ? content : "1234567890").append("', {");
                            html.append("        format: 'CODE128',");
                            html.append("        width: 2,");
                            html.append("        height: 30,");
                            html.append("        displayValue: true,");
                            html.append("        fontSize: 12");
                            html.append("    });");
                            html.append("});");
                            html.append("</script>");
                            break;
                        case "qrcode":
                            // 二维码元素
                            html.append("<canvas id='qrcode_").append(i).append("' style='width: 100%; height: 100%;'></canvas>");
                            // 添加初始化脚本
                            html.append("<script>");
                            html.append("window.addEventListener('load', function() {");
                            html.append("    QRCode.toCanvas(document.getElementById('qrcode_").append(i).append("'), '").append(content != null ? content : "https://example.com").append("', {");
                            html.append("        width: 128,");
                            html.append("        margin: 1");
                            html.append("    }, function(error) {");
                            html.append("        if (error) console.error(error);");
                            html.append("    });");
                            html.append("});");
                            html.append("</script>");
                            break;
                        case "hline":
                            // 水平线元素
                            html.append("<div class='elem-hline-inner' style='width: 100%;'></div>");
                            break;
                        case "vline":
                            // 垂直线元素
                            html.append("<div class='elem-vline-inner' style='height: 100%;'></div>");
                            break;
                        case "image":
                            // 图片元素，使用示例图片
                            html.append("<img src='").append(content != null ? content : "https://via.placeholder.com/100").append("' style='width: 100%; height: 100%; object-fit: contain;' />");
                            break;
                        case "icon":
                            // 图标元素
                            html.append("<div style='font-size: ").append(Math.min(elemWidth, elemHeight) * 0.8).append("mm;'>📦</div>");
                            break;
                        case "shape":
                            // 形状元素
                            html.append("<div style='width: 100%; height: 100%; border: 1px solid #000; background: #f5f5f5;'></div>");
                            break;
                        case "diagonal":
                            // 斜线元素，已通过CSS实现
                            break;
                        default:
                            // 未知元素类型
                            html.append("<div style='color: #999; font-size: 12px;'>未知元素</div>");
                            break;
                    }

                    html.append("</div>");
                }
            } else {
                // 尝试解析为旧格式（JSON对象）
                JSONObject templateJson = JSON.parseObject(template.getTemplateJson());
                if (templateJson != null) {
                    // 旧格式处理（如果需要）
                    html.append("<div style='color: #999; padding: 20px;'>旧格式模板，暂不支持预览</div>");
                }
            }
        } catch (Exception e) {
            html.append("<div style='color: #f56c6c; padding: 20px;'>预览生成失败：").append(e.getMessage()).append("</div>");
        }

        html.append("</div>");
        html.append("</div>");
        html.append("</body>");
        html.append("</html>");

        return template;
    }

    /**
     * 构建订单数据映射，用于替换模板中的占位符
     */
    private Map<String, String> buildOrderDataMap() {
        Map<String, String> dataMap = new HashMap<>();

        // 基础订单信息
        dataMap.put("{orderNo}", "EXP1234567890");
        dataMap.put("{senderName}", "张三");
        dataMap.put("{senderPhone}", "13800138000");
        dataMap.put("{senderAddress}", "北京市朝阳区建国路88号");
        dataMap.put("{receiverName}", "李四");
        dataMap.put("{receiverPhone}", "13900139000");
        dataMap.put("{receiverAddress}", "上海市浦东新区陆家嘴环路166号");
        dataMap.put("{goodsName}", "电子产品");
        dataMap.put("{goodsWeight}", "2.5kg");
        dataMap.put("{goodsQuantity}", "1");
        dataMap.put("{orderTime}", "2025-01-15 14:30:00");
        dataMap.put("{expressCompany}", "极速快递");

        // 扩展字段
        dataMap.put("{orderType}", "普通订单");
        dataMap.put("{payStatus}", "已支付");
        dataMap.put("{orderStatus}", "已发货");
        dataMap.put("{trackingNo}", "TRK7894561230");

        return dataMap;
    }

    /**
     * 替换文本中的占位符
     */
    private String replacePlaceholders(String text, Map<String, String> dataMap) {
        if (StringUtils.isEmpty(text) || dataMap == null || dataMap.isEmpty()) {
            return text;
        }

        String result = text;
        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null) {
                result = result.replace(entry.getKey(), entry.getValue());
            }
        }

        return result;
    }

    /**
     * 根据订单和模板生成打印数据
     */
    @Override
    public Map<String, Object> generatePrintHtml(Long orderId, Long templateId) {
        // 获取订单信息
        ExpressOrder order = expressOrderService.selectExpressOrderById(orderId);
        if (order == null) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "订单不存在");
            return result;
        }

        // 获取模板信息
        WaybillTemplate template = selectWaybillTemplateById(templateId);
        if (template == null || StringUtils.isEmpty(template.getTemplateJson())) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "模板数据为空");
            return result;
        }

        // 解析纸张尺寸
        String[] paperSize = template.getPaperSize().split("\\*");
        String width = paperSize[0].replace("mm", "");
        String height = paperSize.length > 1 ? paperSize[1].replace("mm", "") : "130";

        // 构建完整的HTML预览，包含必要的CSS和JavaScript库
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html lang='zh-CN'>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<title>面单打印</title>");
        html.append("<style>");
        html.append("body { margin: 0; padding: 0; font-family: Arial, sans-serif; background: #fff; }");
        html.append(".waybill-print { width: " + width + "mm; height: " + height + "mm; margin: 0 auto; background: #fff; position: relative; overflow: hidden; border: 1px solid #000; page-break-after: always; }");
        html.append(".canvas-element { position: absolute; z-index: 10; }");

        // 文本元素样式
        html.append(".elem-text { display: flex; align-items: center; }");
        html.append(".elem-text .content { outline: none; }");

        // 时间元素样式
        html.append(".elem-time { display: flex; align-items: center; font-size: 14px; color: #000; }");

        // 条形码元素样式
        html.append(".elem-barcode { display: flex; justify-content: center; align-items: center; position: relative; }");
        html.append(".elem-barcode canvas { max-width: 100%; max-height: 100%; }");

        // 二维码元素样式
        html.append(".elem-qrcode { display: flex; justify-content: center; align-items: center; }");
        html.append(".elem-qrcode canvas { max-width: 100%; max-height: 100%; }");

        // 水平线和垂直线样式
        html.append(".elem-hline { overflow: hidden; }");
        html.append(".elem-hline-inner { background: #000; height: 1px; }");
        html.append(".elem-vline { overflow: hidden; }");
        html.append(".elem-vline-inner { background: #000; width: 1px; height: 100%; }");

        // 图片元素样式
        html.append(".elem-image { display: flex; justify-content: center; align-items: center; }");
        html.append(".elem-image img { max-width: 100%; max-height: 100%; }");

        // 图标元素样式
        html.append(".elem-icon { display: flex; justify-content: center; align-items: center; font-size: 24px; }");

        // 形状元素样式
        html.append(".elem-shape { border: 1px solid #000; background: #f0f0f0; }");

        // 斜线元素样式
        html.append(".elem-diagonal { position: relative; overflow: hidden; }");
        html.append(".elem-diagonal::after { content: ''; position: absolute; width: 100%; height: 100%; border-bottom: 1px solid #000; transform: rotate(45deg); transform-origin: center center; }");

        html.append("</style>");

        // 引入必要的JavaScript库
        html.append("<script src='https://cdn.jsdelivr.net/npm/jsbarcode@3.11.5/dist/JsBarcode.all.min.js'></script>");
        html.append("<script src='https://cdn.jsdelivr.net/npm/qrcode@1.5.3/build/qrcode.min.js'></script>");

        html.append("</head>");
        html.append("<body>");
        html.append("<div class='waybill-print'>");

        // 渲染LOGO
        if (StringUtils.isNotEmpty(template.getLogoUrl())) {
            html.append("<img src='").append(template.getLogoUrl()).append("' style='position:absolute;top:5px;right:5px;width:30mm;height:15mm;'/> ");
        }

        try {
            // 构建订单数据映射，用于替换模板中的占位符
            Map<String, String> orderDataMap = buildOrderDataMap(order);

            // 尝试解析为数组格式
            JSONArray elements = JSON.parseArray(template.getTemplateJson());
            if (elements != null && elements.size() > 0) {
                // 渲染每个元素
                for (int i = 0; i < elements.size(); i++) {
                    JSONObject elem = elements.getJSONObject(i);
                    String type = elem.getString("type");
                    if (type == null) continue;

                    double x = elem.getDoubleValue("x");
                    double y = elem.getDoubleValue("y");
                    double elemWidth = elem.getDoubleValue("width");
                    double elemHeight = elem.getDoubleValue("height");
                    String content = elem.getString("content");
                    int zIndex = elem.getIntValue("zIndex");

                    // 替换占位符内容
                    if (content != null) {
                        content = replacePlaceholders(content, orderDataMap);
                    }

                    // 开始构建元素HTML
                    html.append("<div class='canvas-element elem-").append(type).append("' ");
                    html.append("style='position:absolute;");
                    html.append("left:").append(x).append("mm;");
                    html.append("top:").append(y).append("mm;");
                    html.append("width:").append(elemWidth).append("mm;");
                    html.append("height:").append(elemHeight).append("mm;");
                    html.append("z-index:").append(zIndex).append(";");
                    html.append("'>");

                    // 根据元素类型渲染不同内容
                    switch (type) {
                        case "text":
                            html.append("<div class='content' style='width: 100%; height: 100%; display: flex; align-items: center;'>");
                            html.append(content != null ? content : "");
                            html.append("</div>");
                            break;
                        case "time":
                            // 时间元素，显示当前时间
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            html.append("<div style='width: 100%; height: 100%; display: flex; align-items: center;'>");
                            html.append(sdf.format(new Date()));
                            html.append("</div>");
                            break;
                        case "barcode":
                            // 条形码元素
                            html.append("<canvas id='barcode_").append(orderId).append("_").append(i).append("' style='width: 100%; height: 100%;'></canvas>");
                            // 添加初始化脚本
                            html.append("<script>");
                            html.append("window.addEventListener('load', function() {");
                            html.append("    JsBarcode('#barcode_").append(orderId).append("_").append(i).append("', '").append(content != null ? content : order.getOrderNo()).append("', {");
                            html.append("        format: 'CODE128',");
                            html.append("        width: 2,");
                            html.append("        height: 30,");
                            html.append("        displayValue: true,");
                            html.append("        fontSize: 12");
                            html.append("    });");
                            html.append("});");
                            html.append("</script>");
                            break;
                        case "qrcode":
                            // 二维码元素
                            html.append("<canvas id='qrcode_").append(orderId).append("_").append(i).append("' style='width: 100%; height: 100%;'></canvas>");
                            // 添加初始化脚本
                            html.append("<script>");
                            html.append("window.addEventListener('load', function() {");
                            html.append("    QRCode.toCanvas(document.getElementById('qrcode_").append(orderId).append("_").append(i).append("'), '").append(content != null ? content : "https://example.com/order/" + order.getOrderNo()).append("', {");
                            html.append("        width: 128,");
                            html.append("        margin: 1");
                            html.append("    }, function(error) {");
                            html.append("        if (error) console.error(error);");
                            html.append("    });");
                            html.append("});");
                            html.append("</script>");
                            break;
                        case "hline":
                            // 水平线元素
                            html.append("<div class='elem-hline-inner' style='width: 100%;'></div>");
                            break;
                        case "vline":
                            // 垂直线元素
                            html.append("<div class='elem-vline-inner' style='height: 100%;'></div>");
                            break;
                        case "image":
                            // 图片元素
                            html.append("<img src='").append(content != null ? content : "https://via.placeholder.com/100").append("' style='width: 100%; height: 100%; object-fit: contain;' />");
                            break;
                        case "icon":
                            // 图标元素
                            html.append("<div style='font-size: ").append(Math.min(elemWidth, elemHeight) * 0.8).append("mm;'>📦</div>");
                            break;
                        case "shape":
                            // 形状元素
                            html.append("<div style='width: 100%; height: 100%; border: 1px solid #000; background: #f5f5f5;'></div>");
                            break;
                        case "diagonal":
                            // 斜线元素，已通过CSS实现
                            break;
                        default:
                            // 未知元素类型
                            html.append("<div style='color: #999; font-size: 12px;'>未知元素</div>");
                            break;
                    }

                    html.append("</div>");
                }
            } else {
                // 尝试解析为旧格式（JSON对象）
                html.append("<div style='color: #999; padding: 20px;'>旧格式模板，暂不支持打印</div>");
            }
        } catch (Exception e) {
            html.append("<div style='color: #f56c6c; padding: 20px;'>打印内容生成失败：").append(e.getMessage()).append("</div>");
        }

        html.append("</div>");
        html.append("</body>");
        html.append("</html>");

        // 构建订单数据映射，用于替换模板中的占位符
        Map<String, String> orderDataMap = buildOrderDataMap(order);
        
        // 返回订单和模板数据，由前端负责渲染
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("order", order);
        result.put("template", template);
        result.put("orderDataMap", orderDataMap);
        
        return result;
    }

    /**
     * 构建订单数据映射，用于替换模板中的占位符
     */
    private Map<String, String> buildOrderDataMap(ExpressOrder order) {
        Map<String, String> dataMap = new HashMap<>();
        if (order == null) {
            return dataMap;
        }

        // 基础订单信息
        dataMap.put("{orderNo}", order.getOrderNo() != null ? order.getOrderNo() : "");
        dataMap.put("{senderName}", order.getSenderName() != null ? order.getSenderName() : "");
        dataMap.put("{senderPhone}", order.getSenderPhone() != null ? order.getSenderPhone() : "");
        dataMap.put("{senderAddress}", order.getSenderAddress() != null ? order.getSenderAddress() : "");
        dataMap.put("{receiverName}", order.getReceiverName() != null ? order.getReceiverName() : "");
        dataMap.put("{receiverPhone}", order.getReceiverPhone() != null ? order.getReceiverPhone() : "");
        dataMap.put("{receiverAddress}", order.getReceiverAddress() != null ? order.getReceiverAddress() : "");

        // 快递信息
        dataMap.put("{expressType}", order.getExpressType() != null ? order.getExpressType() : "");
        dataMap.put("{weight}", order.getWeight() != null ? order.getWeight().toString() + "kg" : "");
        dataMap.put("{freight}", order.getFreight() != null ? "¥" + order.getFreight().toString() : "");

        // 状态信息
        dataMap.put("{payStatus}", getPayStatusText(order.getPayStatus()));
        dataMap.put("{orderStatus}", getOrderStatusText(order.getOrderStatus()));

        // 时间信息
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dataMap.put("{createTime}", order.getCreateTime() != null ? sdf.format(order.getCreateTime()) : "");
        dataMap.put("{payTime}", order.getPayTime() != null ? sdf.format(order.getPayTime()) : "");

        // 扩展字段
        dataMap.put("{expressCompany}", "极速快递");
        dataMap.put("{trackingNo}", order.getOrderNo() != null ? order.getOrderNo() : "");

        return dataMap;
    }

    /**
     * 获取支付状态文本
     */
    private String getPayStatusText(String payStatus) {
        switch (payStatus) {
            case "0":
                return "未支付";
            case "1":
                return "已支付";
            case "2":
                return "已退款";
            default:
                return "未知";
        }
    }

    /**
     * 获取订单状态文本
     */
    private String getOrderStatusText(String orderStatus) {
        switch (orderStatus) {
            case "01":
                return "待揽收";
            case "02":
                return "运输中";
            case "03":
                return "派送中";
            case "04":
                return "已签收";
            case "05":
                return "已退回";
            case "06":
                return "异常";
            default:
                return "未知";
        }
    }
}
