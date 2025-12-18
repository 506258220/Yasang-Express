package com.ruoyi.express.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.express.domain.ExpressWaybill;
import com.ruoyi.express.service.IExpressWaybillService;
import com.ruoyi.express.service.impl.PdfWaybillGeneratorService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.HttpServletResponse;

/**
 * 快递单Controller
 *
 * @author ruoyi
 * @date 2025-12-17
 */
@RestController
@RequestMapping("/express/waybill")
public class ExpressWaybillController extends BaseController
{
    @Autowired
    private IExpressWaybillService expressWaybillService;

    @Autowired
    private PdfWaybillGeneratorService pdfWaybillGeneratorService;

    /**
     * 查询快递单列表
     */
    @PreAuthorize("@ss.hasPermi('express:waybill:list')")
    @GetMapping("/list")
    public TableDataInfo list(ExpressWaybill expressWaybill)
    {
        startPage();
        List<ExpressWaybill> list = expressWaybillService.selectExpressWaybillList(expressWaybill);
        return getDataTable(list);
    }

    /**
     * 导出快递单列表
     */
    @PreAuthorize("@ss.hasPermi('express:waybill:export')")
    @Log(title = "快递单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExpressWaybill expressWaybill)
    {
        List<ExpressWaybill> list = expressWaybillService.selectExpressWaybillList(expressWaybill);
        ExcelUtil<ExpressWaybill> util = new ExcelUtil<ExpressWaybill>(ExpressWaybill.class);
        util.exportExcel(response, list, "快递单数据");
    }

    /**
     * 获取快递单详细信息
     */
    @PreAuthorize("@ss.hasPermi('express:waybill:query')")
    @GetMapping(value = "/{waybillId}")
    public AjaxResult getInfo(@PathVariable("waybillId") Long waybillId)
    {
        return success(expressWaybillService.selectExpressWaybillByWaybillId(waybillId));
    }

    /**
     * 新增快递单
     */
    @PreAuthorize("@ss.hasPermi('express:waybill:add')")
    @Log(title = "快递单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ExpressWaybill expressWaybill)
    {
        return toAjax(expressWaybillService.insertExpressWaybill(expressWaybill));
    }

    /**
     * 修改快递单
     */
    @PreAuthorize("@ss.hasPermi('express:waybill:edit')")
    @Log(title = "快递单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ExpressWaybill expressWaybill)
    {
        return toAjax(expressWaybillService.updateExpressWaybill(expressWaybill));
    }

    /**
     * 删除快递单
     */
    @PreAuthorize("@ss.hasPermi('express:waybill:remove')")
    @Log(title = "快递单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{waybillIds}")
    public AjaxResult remove(@PathVariable Long[] waybillIds)
    {
        return toAjax(expressWaybillService.deleteExpressWaybillByWaybillIds(waybillIds));
    }

    /**
     * 生成快递单PDF
     */
    @PreAuthorize("@ss.hasPermi('express:waybill:print')")
    @PostMapping("/generatePdf/{waybillId}/{templateId}")
    public void generatePdf(@PathVariable Long waybillId, @PathVariable Long templateId, HttpServletResponse response)
    {
        try {
            // 生成PDF字节数组
            byte[] pdfBytes = pdfWaybillGeneratorService.generateWaybillPdf(waybillId, templateId);

            // 设置响应头
            response.setContentType("application/pdf");
            response.setContentLength(pdfBytes.length);
            response.setHeader("Content-Disposition", "attachment; filename=waybill_" + waybillId + ".pdf");

            // 写入响应流
            response.getOutputStream().write(pdfBytes);
            response.getOutputStream().flush();
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
