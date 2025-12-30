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
import com.ruoyi.express.domain.ExpressVirtualNumber;
import com.ruoyi.express.service.IExpressVirtualNumberService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 虚拟号管理Controller
 * 
 * @author apisflorea
 * @date 2025-12-29
 */
@RestController
@RequestMapping("/express/number")
public class ExpressVirtualNumberController extends BaseController
{
    @Autowired
    private IExpressVirtualNumberService expressVirtualNumberService;

    /**
     * 查询虚拟号管理列表
     */
    @PreAuthorize("@ss.hasPermi('express:number:list')")
    @GetMapping("/list")
    public TableDataInfo list(ExpressVirtualNumber expressVirtualNumber)
    {
        startPage();
        List<ExpressVirtualNumber> list = expressVirtualNumberService.selectExpressVirtualNumberList(expressVirtualNumber);
        return getDataTable(list);
    }

    /**
     * 导出虚拟号管理列表
     */
    @PreAuthorize("@ss.hasPermi('express:number:export')")
    @Log(title = "虚拟号管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExpressVirtualNumber expressVirtualNumber)
    {
        List<ExpressVirtualNumber> list = expressVirtualNumberService.selectExpressVirtualNumberList(expressVirtualNumber);
        ExcelUtil<ExpressVirtualNumber> util = new ExcelUtil<ExpressVirtualNumber>(ExpressVirtualNumber.class);
        util.exportExcel(response, list, "虚拟号管理数据");
    }

    /**
     * 获取虚拟号管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('express:number:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(expressVirtualNumberService.selectExpressVirtualNumberById(id));
    }

    /**
     * 新增虚拟号管理
     */
    @PreAuthorize("@ss.hasPermi('express:number:add')")
    @Log(title = "虚拟号管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ExpressVirtualNumber expressVirtualNumber)
    {
        return toAjax(expressVirtualNumberService.insertExpressVirtualNumber(expressVirtualNumber));
    }

    /**
     * 修改虚拟号管理
     */
    @PreAuthorize("@ss.hasPermi('express:number:edit')")
    @Log(title = "虚拟号管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ExpressVirtualNumber expressVirtualNumber)
    {
        return toAjax(expressVirtualNumberService.updateExpressVirtualNumber(expressVirtualNumber));
    }

    /**
     * 删除虚拟号管理
     */
    @PreAuthorize("@ss.hasPermi('express:number:remove')")
    @Log(title = "虚拟号管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(expressVirtualNumberService.deleteExpressVirtualNumberByIds(ids));
    }
}
