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
import com.ruoyi.express.domain.ExpressOrder;
import com.ruoyi.express.service.IExpressOrderService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 快递订单Controller
 * 
 * @author apisflorea
 * @date 2025-12-29
 */
@RestController
@RequestMapping("/express/order")
public class ExpressOrderController extends BaseController
{
    @Autowired
    private IExpressOrderService expressOrderService;

    /**
     * 查询快递订单列表
     */
    @PreAuthorize("@ss.hasPermi('express:order:list')")
    @GetMapping("/list")
    public TableDataInfo list(ExpressOrder expressOrder)
    {
        startPage();
        List<ExpressOrder> list = expressOrderService.selectExpressOrderList(expressOrder);
        return getDataTable(list);
    }

    /**
     * 导出快递订单列表
     */
    @PreAuthorize("@ss.hasPermi('express:order:export')")
    @Log(title = "快递订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExpressOrder expressOrder)
    {
        List<ExpressOrder> list = expressOrderService.selectExpressOrderList(expressOrder);
        ExcelUtil<ExpressOrder> util = new ExcelUtil<ExpressOrder>(ExpressOrder.class);
        util.exportExcel(response, list, "快递订单数据");
    }

    /**
     * 获取快递订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('express:order:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(expressOrderService.selectExpressOrderById(id));
    }

    /**
     * 新增快递订单
     */
    @PreAuthorize("@ss.hasPermi('express:order:add')")
    @Log(title = "快递订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ExpressOrder expressOrder)
    {
        return toAjax(expressOrderService.insertExpressOrder(expressOrder));
    }

    /**
     * 修改快递订单
     */
    @PreAuthorize("@ss.hasPermi('express:order:edit')")
    @Log(title = "快递订单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ExpressOrder expressOrder)
    {
        return toAjax(expressOrderService.updateExpressOrder(expressOrder));
    }

    /**
     * 删除快递订单
     */
    @PreAuthorize("@ss.hasPermi('express:order:remove')")
    @Log(title = "快递订单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(expressOrderService.deleteExpressOrderByIds(ids));
    }
}
