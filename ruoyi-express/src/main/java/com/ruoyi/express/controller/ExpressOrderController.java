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
 * 订单管理Controller
 * 
 * @author apisflorea
 * @date 2025-12-17
 */
@RestController
@RequestMapping("/express/order")
public class ExpressOrderController extends BaseController
{
    @Autowired
    private IExpressOrderService expressOrderService;

    /**
     * 查询订单管理列表
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
     * 导出订单管理列表
     */
    @PreAuthorize("@ss.hasPermi('express:order:export')")
    @Log(title = "订单管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExpressOrder expressOrder)
    {
        List<ExpressOrder> list = expressOrderService.selectExpressOrderList(expressOrder);
        ExcelUtil<ExpressOrder> util = new ExcelUtil<ExpressOrder>(ExpressOrder.class);
        util.exportExcel(response, list, "订单管理数据");
    }

    /**
     * 获取订单管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('express:order:query')")
    @GetMapping(value = "/{orderId}")
    public AjaxResult getInfo(@PathVariable("orderId") Long orderId)
    {
        return success(expressOrderService.selectExpressOrderByOrderId(orderId));
    }

    /**
     * 新增订单管理
     */
    @PreAuthorize("@ss.hasPermi('express:order:add')")
    @Log(title = "订单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ExpressOrder expressOrder)
    {
        return toAjax(expressOrderService.insertExpressOrder(expressOrder));
    }

    /**
     * 修改订单管理
     */
    @PreAuthorize("@ss.hasPermi('express:order:edit')")
    @Log(title = "订单管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ExpressOrder expressOrder)
    {
        return toAjax(expressOrderService.updateExpressOrder(expressOrder));
    }

    /**
     * 删除订单管理
     */
    @PreAuthorize("@ss.hasPermi('express:order:remove')")
    @Log(title = "订单管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{orderIds}")
    public AjaxResult remove(@PathVariable Long[] orderIds)
    {
        return toAjax(expressOrderService.deleteExpressOrderByOrderIds(orderIds));
    }
}
