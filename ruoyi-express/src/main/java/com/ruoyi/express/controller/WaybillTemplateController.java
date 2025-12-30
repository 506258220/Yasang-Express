package com.ruoyi.express.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.utils.DictUtils;
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
import com.ruoyi.express.domain.WaybillTemplate;
import com.ruoyi.express.service.IWaybillTemplateService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 面单模板Controller
 *
 * @author 全栈开发者
 * @date 2025-01-01
 */
@RestController
@RequestMapping("/express/template")
public class WaybillTemplateController extends BaseController {
    @Autowired
    private IWaybillTemplateService waybillTemplateService;

    /**
     * 查询面单模板列表
     */
    @PreAuthorize("@ss.hasPermi('express:template:list')")
    @GetMapping("/list")
    public TableDataInfo list(WaybillTemplate waybillTemplate) {
        startPage(); // 若依内置分页
        List<WaybillTemplate> list = waybillTemplateService.selectWaybillTemplateList(waybillTemplate);
        // 字典回显（若依字典工具类）
        list.forEach(template -> {
            template.setScopeTypeLabel(DictUtils.getDictLabel("waybill_scope_type", template.getScopeType(), ""));
            template.setStatusLabel(DictUtils.getDictLabel("sys_normal_disable", template.getStatus(), ""));
        });
        return getDataTable(list); // 若依统一分页返回
    }

    /**
     * 导出面单模板列表
     */
    @PreAuthorize("@ss.hasPermi('express:template:export')")
    @Log(title = "面单模板", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WaybillTemplate waybillTemplate) {
        List<WaybillTemplate> list = waybillTemplateService.selectWaybillTemplateList(waybillTemplate);
        ExcelUtil<WaybillTemplate> util = new ExcelUtil<WaybillTemplate>(WaybillTemplate.class);
        util.exportExcel(response, list, "面单模板数据");
    }

    /**
     * 获取面单模板详细信息
     */
    @PreAuthorize("@ss.hasPermi('express:template:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(waybillTemplateService.selectWaybillTemplateById(id));
    }

    /**
     * 新增面单模板
     */
    @PreAuthorize("@ss.hasPermi('express:template:add')")
    @Log(title = "面单模板", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WaybillTemplate waybillTemplate) {
        return toAjax(waybillTemplateService.insertWaybillTemplate(waybillTemplate));
    }

    /**
     * 修改面单模板
     */
    @PreAuthorize("@ss.hasPermi('express:template:edit')")
    @Log(title = "面单模板", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WaybillTemplate waybillTemplate) {
        return toAjax(waybillTemplateService.updateWaybillTemplate(waybillTemplate));
    }

    /**
     * 删除面单模板
     */
    @PreAuthorize("@ss.hasPermi('express:template:remove')")
    @Log(title = "面单模板", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(waybillTemplateService.deleteWaybillTemplateByIds(ids));
    }

    /**
     * 预览面单模板
     */
    @PreAuthorize("@ss.hasPermi('express:template:preview')")
    @GetMapping("/preview/{id}")
    public AjaxResult preview(@PathVariable("id") Long id) {
        WaybillTemplate template = waybillTemplateService.previewWaybillTemplate(id);
        return AjaxResult.success("操作成功", template);
    }

    /**
     * 根据订单和模板生成打印HTML
     */
    @PreAuthorize("@ss.hasPermi('express:order:print')")
    @PostMapping("/print")
    public AjaxResult generatePrintHtml(@RequestBody java.util.Map<String, Long> params) {
        Long orderId = params.get("orderId");
        Long templateId = params.get("templateId");
        if (orderId == null || templateId == null) {
            return error("订单ID和模板ID不能为空");
        }
        java.util.Map<String, Object> result = waybillTemplateService.generatePrintHtml(orderId, templateId);
        return AjaxResult.success("操作成功", result);
    }
}
