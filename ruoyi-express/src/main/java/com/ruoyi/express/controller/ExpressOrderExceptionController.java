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
import com.ruoyi.express.domain.ExpressOrderException;
import com.ruoyi.express.service.IExpressOrderExceptionService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 订单异常记录Controller
 * 
 * @author apisflorea
 * @date 2025-12-29
 */
@RestController
@RequestMapping("/express/exception")
public class ExpressOrderExceptionController extends BaseController
{
    @Autowired
    private IExpressOrderExceptionService expressOrderExceptionService;

    /**
     * 查询订单异常记录列表
     */
    @PreAuthorize("@ss.hasPermi('express:exception:list')")
    @GetMapping("/list")
    public TableDataInfo list(ExpressOrderException expressOrderException)
    {
        startPage();
        List<ExpressOrderException> list = expressOrderExceptionService.selectExpressOrderExceptionList(expressOrderException);
        return getDataTable(list);
    }

    /**
     * 导出订单异常记录列表
     */
    @PreAuthorize("@ss.hasPermi('express:exception:export')")
    @Log(title = "订单异常记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExpressOrderException expressOrderException)
    {
        List<ExpressOrderException> list = expressOrderExceptionService.selectExpressOrderExceptionList(expressOrderException);
        ExcelUtil<ExpressOrderException> util = new ExcelUtil<ExpressOrderException>(ExpressOrderException.class);
        util.exportExcel(response, list, "订单异常记录数据");
    }

    /**
     * 获取订单异常记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('express:exception:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(expressOrderExceptionService.selectExpressOrderExceptionById(id));
    }

    /**
     * 新增订单异常记录
     */
    @PreAuthorize("@ss.hasPermi('express:exception:add')")
    @Log(title = "订单异常记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ExpressOrderException expressOrderException)
    {
        return toAjax(expressOrderExceptionService.insertExpressOrderException(expressOrderException));
    }

    /**
     * 修改订单异常记录
     */
    @PreAuthorize("@ss.hasPermi('express:exception:edit')")
    @Log(title = "订单异常记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ExpressOrderException expressOrderException)
    {
        return toAjax(expressOrderExceptionService.updateExpressOrderException(expressOrderException));
    }

    /**
     * 删除订单异常记录
     */
    @PreAuthorize("@ss.hasPermi('express:exception:remove')")
    @Log(title = "订单异常记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(expressOrderExceptionService.deleteExpressOrderExceptionByIds(ids));
    }
}
