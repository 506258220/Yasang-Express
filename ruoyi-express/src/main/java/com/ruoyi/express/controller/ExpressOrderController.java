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
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.express.domain.ExpressOrder;
import com.ruoyi.express.service.IExpressOrderService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import java.util.Arrays;

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

    /**
     * 导入订单管理
     */
    @PreAuthorize("@ss.hasPermi('express:order:import')")
    @Log(title = "订单管理", businessType = BusinessType.IMPORT)
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<ExpressOrder> util = new ExcelUtil<ExpressOrder>(ExpressOrder.class);
        List<ExpressOrder> expressOrderList = util.importExcel(file.getInputStream(), 2); // 第3行是表头（索引2），第4行开始是数据
        String message = importUser(expressOrderList, updateSupport);
        return AjaxResult.success(message);
    }

    /**
     * 导入订单数据处理
     */
    private String importUser(List<ExpressOrder> expressOrderList, boolean updateSupport)
    {
        if (StringUtils.isNull(expressOrderList) || expressOrderList.size() == 0)
        {
            throw new ServiceException("导入订单数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (ExpressOrder expressOrder : expressOrderList)
        {
            try
            {
                // 检查订单编号是否已存在
                ExpressOrder existingOrder = expressOrderService.selectExpressOrderByOrderNo(expressOrder.getOrderNo());
                if (existingOrder != null)
                {
                    failureNum++;
                    failureMsg.append("导入订单 " + expressOrder.getOrderNo() + " 失败：订单号已存在;\n");
                    continue;
                }
                
                expressOrder.setCreateBy(getUsername());
                expressOrder.setUpdateBy(getUsername());
                expressOrder.setDelFlag("0");
                expressOrderService.batchInsertExpressOrder(Arrays.asList(expressOrder));
                successNum++;
                successMsg.append("、" + expressOrder.getOrderNo());
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "导入订单 " + expressOrder.getOrderNo() + " 失败：";
                failureMsg.append(msg + e.getMessage() + ";\n");
                logger.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "导入失败 " + failureNum + " 条订单数据，失败信息如下：\n");
            throw new ServiceException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "导入成功 " + successNum + " 条订单数据，订单编号如下：");
        }
        return successMsg.toString();
    }
}
