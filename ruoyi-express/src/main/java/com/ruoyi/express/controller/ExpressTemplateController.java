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
import com.ruoyi.express.domain.ExpressTemplate;
import com.ruoyi.express.service.IExpressTemplateService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 快递模板配置Controller
 * 
 * @author ruoyi
 * @date 2025-12-17
 */
@RestController
@RequestMapping("/express/template")
public class ExpressTemplateController extends BaseController
{
    @Autowired
    private IExpressTemplateService expressTemplateService;

    /**
     * 查询快递模板配置列表
     */
    @PreAuthorize("@ss.hasPermi('express:template:list')")
    @GetMapping("/list")
    public TableDataInfo list(ExpressTemplate expressTemplate)
    {
        startPage();
        List<ExpressTemplate> list = expressTemplateService.selectExpressTemplateList(expressTemplate);
        return getDataTable(list);
    }

    /**
     * 导出快递模板配置列表
     */
    @PreAuthorize("@ss.hasPermi('express:template:export')")
    @Log(title = "快递模板配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExpressTemplate expressTemplate)
    {
        List<ExpressTemplate> list = expressTemplateService.selectExpressTemplateList(expressTemplate);
        ExcelUtil<ExpressTemplate> util = new ExcelUtil<ExpressTemplate>(ExpressTemplate.class);
        util.exportExcel(response, list, "快递模板配置数据");
    }

    /**
     * 获取快递模板配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('express:template:query')")
    @GetMapping(value = "/{templateId}")
    public AjaxResult getInfo(@PathVariable("templateId") Long templateId)
    {
        return success(expressTemplateService.selectExpressTemplateByTemplateId(templateId));
    }

    /**
     * 新增快递模板配置
     */
    @PreAuthorize("@ss.hasPermi('express:template:add')")
    @Log(title = "快递模板配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ExpressTemplate expressTemplate)
    {
        return toAjax(expressTemplateService.insertExpressTemplate(expressTemplate));
    }

    /**
     * 修改快递模板配置
     */
    @PreAuthorize("@ss.hasPermi('express:template:edit')")
    @Log(title = "快递模板配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ExpressTemplate expressTemplate)
    {
        return toAjax(expressTemplateService.updateExpressTemplate(expressTemplate));
    }

    /**
     * 删除快递模板配置
     */
    @PreAuthorize("@ss.hasPermi('express:template:remove')")
    @Log(title = "快递模板配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{templateIds}")
    public AjaxResult remove(@PathVariable Long[] templateIds)
    {
        return toAjax(expressTemplateService.deleteExpressTemplateByTemplateIds(templateIds));
    }
}
