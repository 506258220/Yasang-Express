package com.ruoyi.express.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.express.domain.ExpressThirdOrder;
import com.ruoyi.express.service.IExpressThirdOrderService;
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
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 第三方商品订单（用于导入后批量打印快递单）Controller
 *
 * @author apisflorea
 * @date 2025-12-29
 */
@RestController
@RequestMapping("/express/third/order")
public class ExpressThirdOrderController extends BaseController
{
    @Autowired
    private IExpressThirdOrderService expressThirdOrderService;

    /**
     * 查询第三方商品订单（用于导入后批量打印快递单）列表
     */
    @PreAuthorize("@ss.hasPermi('express:third:order:list')")
    @GetMapping("/list")
    public TableDataInfo list(ExpressThirdOrder expressThirdOrder)
    {
        startPage();
        List<ExpressThirdOrder> list = expressThirdOrderService.selectExpressThirdOrderList(expressThirdOrder);
        return getDataTable(list);
    }

    /**
     * 导出第三方商品订单（用于导入后批量打印快递单）列表
     */
    @PreAuthorize("@ss.hasPermi('express:third:order:export')")
    @Log(title = "第三方商品订单（用于导入后批量打印快递单）", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExpressThirdOrder expressThirdOrder)
    {
        List<ExpressThirdOrder> list = expressThirdOrderService.selectExpressThirdOrderList(expressThirdOrder);
        ExcelUtil<ExpressThirdOrder> util = new ExcelUtil<ExpressThirdOrder>(ExpressThirdOrder.class);
        util.exportExcel(response, list, "第三方商品订单（用于导入后批量打印快递单）数据");
    }

    /**
     * 获取第三方商品订单（用于导入后批量打印快递单）详细信息
     */
    @PreAuthorize("@ss.hasPermi('express:third:order:query')")
    @GetMapping(value = "/{orderId}")
    public AjaxResult getInfo(@PathVariable("orderId") Long orderId)
    {
        return success(expressThirdOrderService.selectExpressThirdOrderByOrderId(orderId));
    }

    /**
     * 新增第三方商品订单（用于导入后批量打印快递单）
     */
    @PreAuthorize("@ss.hasPermi('express:third:order:add')")
    @Log(title = "第三方商品订单（用于导入后批量打印快递单）", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ExpressThirdOrder expressThirdOrder)
    {
        return toAjax(expressThirdOrderService.insertExpressThirdOrder(expressThirdOrder));
    }

    /**
     * 修改第三方商品订单（用于导入后批量打印快递单）
     */
    @PreAuthorize("@ss.hasPermi('express:third:order:edit')")
    @Log(title = "第三方商品订单（用于导入后批量打印快递单）", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ExpressThirdOrder expressThirdOrder)
    {
        return toAjax(expressThirdOrderService.updateExpressThirdOrder(expressThirdOrder));
    }

    /**
     * 删除第三方商品订单（用于导入后批量打印快递单）
     */
    @PreAuthorize("@ss.hasPermi('express:third:order:remove')")
    @Log(title = "第三方商品订单（用于导入后批量打印快递单）", businessType = BusinessType.DELETE)
	@DeleteMapping("/{orderIds}")
    public AjaxResult remove(@PathVariable Long[] orderIds)
    {
        return toAjax(expressThirdOrderService.deleteExpressThirdOrderByOrderIds(orderIds));
    }

    /**
     * 批量生成快递订单
     */
    @PreAuthorize("@ss.hasPermi('express:third:order:generate')")
    @Log(title = "批量生成快递订单", businessType = BusinessType.INSERT)
    @PostMapping("/generateExpressOrders")
    public AjaxResult batchGenerateExpressOrders(@RequestBody java.util.Map<String, Object> params)
    {
        Object orderIdsObj = params.get("orderIds");
        if (orderIdsObj == null)
        {
            return error("订单ID列表不能为空");
        }

        // 转换订单ID数组
        java.util.List<?> orderIdsList = (java.util.List<?>) orderIdsObj;
        Long[] orderIds = orderIdsList.stream()
                .map(id -> Long.valueOf(id.toString()))
                .toArray(Long[]::new);

        // 获取配置信息
        @SuppressWarnings("unchecked")
        java.util.Map<String, Object> config = (java.util.Map<String, Object>) params.get("config");
        if (config == null)
        {
            config = new java.util.HashMap<>();
        }

        // 批量生成快递订单
        java.util.Map<String, Object> result = expressThirdOrderService.batchGenerateExpressOrders(orderIds, config);
        return success(result);
    }

    /**
     * 导入第三方商品订单（用于导入后批量打印快递单）
     */
    @PreAuthorize("@ss.hasPermi('express:third:order:import')")
    @Log(title = "第三方商品订单导入", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<ExpressThirdOrder> util = new ExcelUtil<ExpressThirdOrder>(ExpressThirdOrder.class);
        List<ExpressThirdOrder> thirdOrderList = util.importExcel(file.getInputStream(), 2);
        String message = expressThirdOrderService.importThirdOrder(thirdOrderList, updateSupport);
        return success(message);
    }

    /**
     * 导出第三方商品订单（用于导入后批量打印快递单）模板
     */
    @PreAuthorize("@ss.hasPermi('express:third:order:export')")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<ExpressThirdOrder> util = new ExcelUtil<ExpressThirdOrder>(ExpressThirdOrder.class);
        util.importTemplateExcel(response, "第三方商品订单模板");
    }
}
