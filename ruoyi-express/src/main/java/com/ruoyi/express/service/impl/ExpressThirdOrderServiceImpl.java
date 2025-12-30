package com.ruoyi.express.service.impl;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.math.BigDecimal;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.express.ExpressNoGenerator;
import com.ruoyi.express.domain.ExpressThirdOrder;
import com.ruoyi.express.domain.ExpressOrder;
import com.ruoyi.express.mapper.ExpressThirdOrderMapper;
import com.ruoyi.express.service.IExpressThirdOrderService;
import com.ruoyi.express.service.IExpressOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 第三方商品订单（用于导入后批量打印快递单）Service业务层处理
 *
 * @author apisflorea
 * @date 2025-12-29
 */
@Service
public class ExpressThirdOrderServiceImpl implements IExpressThirdOrderService
{
    @Autowired
    private ExpressThirdOrderMapper expressThirdOrderMapper;

    @Autowired
    private IExpressOrderService expressOrderService;

    @Autowired
    private ExpressNoGenerator expressNoGenerator;

    /**
     * 查询第三方商品订单（用于导入后批量打印快递单）
     *
     * @param orderId 第三方商品订单（用于导入后批量打印快递单）主键
     * @return 第三方商品订单（用于导入后批量打印快递单）
     */
    @Override
    public ExpressThirdOrder selectExpressThirdOrderByOrderId(Long orderId)
    {
        return expressThirdOrderMapper.selectExpressThirdOrderByOrderId(orderId);
    }

    /**
     * 查询第三方商品订单（用于导入后批量打印快递单）列表
     *
     * @param expressThirdOrder 第三方商品订单（用于导入后批量打印快递单）
     * @return 第三方商品订单（用于导入后批量打印快递单）
     */
    @Override
    public List<ExpressThirdOrder> selectExpressThirdOrderList(ExpressThirdOrder expressThirdOrder)
    {
        return expressThirdOrderMapper.selectExpressThirdOrderList(expressThirdOrder);
    }

    /**
     * 新增第三方商品订单（用于导入后批量打印快递单）
     *
     * @param expressThirdOrder 第三方商品订单（用于导入后批量打印快递单）
     * @return 结果
     */
    @Override
    public int insertExpressThirdOrder(ExpressThirdOrder expressThirdOrder)
    {
        expressThirdOrder.setCreateTime(DateUtils.getNowDate());
        // 设置orderCreateTime默认值为当前时间，避免SQL错误
        if (expressThirdOrder.getOrderCreateTime() == null) {
            expressThirdOrder.setOrderCreateTime(DateUtils.getNowDate());
        }
        return expressThirdOrderMapper.insertExpressThirdOrder(expressThirdOrder);
    }

    /**
     * 修改第三方商品订单（用于导入后批量打印快递单）
     *
     * @param expressThirdOrder 第三方商品订单（用于导入后批量打印快递单）
     * @return 结果
     */
    @Override
    public int updateExpressThirdOrder(ExpressThirdOrder expressThirdOrder)
    {
        expressThirdOrder.setUpdateTime(DateUtils.getNowDate());
        // 设置orderCreateTime默认值为当前时间，避免SQL错误
        if (expressThirdOrder.getOrderCreateTime() == null) {
            expressThirdOrder.setOrderCreateTime(DateUtils.getNowDate());
        }
        return expressThirdOrderMapper.updateExpressThirdOrder(expressThirdOrder);
    }

    /**
     * 批量删除第三方商品订单（用于导入后批量打印快递单）
     *
     * @param orderIds 需要删除的第三方商品订单（用于导入后批量打印快递单）主键
     * @return 结果
     */
    @Override
    public int deleteExpressThirdOrderByOrderIds(Long[] orderIds)
    {
        return expressThirdOrderMapper.deleteExpressThirdOrderByOrderIds(orderIds);
    }

    /**
     * 删除第三方商品订单（用于导入后批量打印快递单）信息
     *
     * @param orderId 第三方商品订单（用于导入后批量打印快递单）主键
     * @return 结果
     */
    @Override
    public int deleteExpressThirdOrderByOrderId(Long orderId)
    {
        return expressThirdOrderMapper.deleteExpressThirdOrderByOrderId(orderId);
    }

    /**
     * 批量生成快递订单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> batchGenerateExpressOrders(Long[] orderIds, Map<String, Object> config)
    {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> successList = new ArrayList<>();
        List<Map<String, Object>> failList = new ArrayList<>();
        int successCount = 0;
        int failCount = 0;

        // 获取配置信息
        String senderName = (String) config.getOrDefault("senderName", "");
        String senderPhone = (String) config.getOrDefault("senderPhone", "");
        String senderAddress = (String) config.getOrDefault("senderAddress", "");
        Long customerId = config.get("customerId") != null ? Long.valueOf(config.get("customerId").toString()) : null;
        String expressType = (String) config.getOrDefault("expressType", "普通件");
        BigDecimal defaultWeight = config.get("defaultWeight") != null ? new BigDecimal(config.get("defaultWeight").toString()) : new BigDecimal("1.0");
        BigDecimal defaultFreight = config.get("defaultFreight") != null ? new BigDecimal(config.get("defaultFreight").toString()) : new BigDecimal("0.0");
        Long warehouseId = config.get("warehouseId") != null ? Long.valueOf(config.get("warehouseId").toString()) : null;
        Long waybillTemplateId = config.get("waybillTemplateId") != null ? Long.valueOf(config.get("waybillTemplateId").toString()) : null;
        String orderNoPrefix = (String) config.getOrDefault("orderNoPrefix", "YS");

        // 遍历第三方订单，转换为快递订单
        for (Long orderId : orderIds)
        {
            try
            {
                // 查询第三方订单
                ExpressThirdOrder thirdOrder = expressThirdOrderMapper.selectExpressThirdOrderByOrderId(orderId);
                if (thirdOrder == null)
                {
                    failList.add(createFailItem(orderId, "第三方订单不存在"));
                    failCount++;
                    continue;
                }

                // 检查必要字段
                if (StringUtils.isEmpty(thirdOrder.getReceiverName()) || 
                    StringUtils.isEmpty(thirdOrder.getReceiverPhone()) || 
                    StringUtils.isEmpty(thirdOrder.getReceiverAddress()))
                {
                    failList.add(createFailItem(orderId, thirdOrder.getOrderNo(), "收货人信息不完整"));
                    failCount++;
                    continue;
                }

                // 检查是否已生成过快递订单（根据第三方订单号查找）- 可以后续扩展
                // ExpressOrder existOrder = new ExpressOrder();
                // List<ExpressOrder> existOrders = expressOrderService.selectExpressOrderList(existOrder);

                // 创建快递订单
                ExpressOrder expressOrder = new ExpressOrder();
                
                // 生成快递单号
                String expressOrderNo = expressNoGenerator.generateExpressNo(orderNoPrefix);
                expressOrder.setOrderNo(expressOrderNo);

                // 寄件人信息（从配置获取）
                expressOrder.setCustomerId(customerId);
                expressOrder.setSenderName(senderName);
                expressOrder.setSenderPhone(senderPhone);
                expressOrder.setSenderAddress(senderAddress);

                // 收件人信息（从第三方订单获取）
                expressOrder.setReceiverName(thirdOrder.getReceiverName());
                expressOrder.setReceiverPhone(thirdOrder.getReceiverPhone());
                expressOrder.setReceiverAddress(thirdOrder.getReceiverAddress());

                // 其他信息
                expressOrder.setExpressType(expressType);
                expressOrder.setWeight(defaultWeight);
                
                // 运费：如果第三方订单有实际支付金额，可以使用，否则使用默认值
                if (thirdOrder.getActualPayAmount() != null && thirdOrder.getActualPayAmount().compareTo(BigDecimal.ZERO) > 0)
                {
                    expressOrder.setFreight(thirdOrder.getActualPayAmount());
                }
                else
                {
                    expressOrder.setFreight(defaultFreight);
                }

                // 支付状态：根据第三方订单的支付状态转换
                if (thirdOrder.getPayStatus() != null && thirdOrder.getPayStatus() == 1)
                {
                    expressOrder.setPayStatus("1"); // 已支付
                    if (thirdOrder.getOrderCreateTime() != null)
                    {
                        expressOrder.setPayTime(thirdOrder.getOrderCreateTime());
                    }
                }
                else
                {
                    expressOrder.setPayStatus("0"); // 未支付
                }

                // 订单状态：默认待揽收
                expressOrder.setOrderStatus("01");

                // 其他关联信息
                expressOrder.setWarehouseId(warehouseId);
                expressOrder.setWaybillTemplateId(waybillTemplateId);

                // 备注：记录第三方订单号
                String remark = "来源：第三方订单[" + thirdOrder.getOrderNo() + "]";
                if (StringUtils.isNotEmpty(thirdOrder.getUserRemark()))
                {
                    remark += "，用户备注：" + thirdOrder.getUserRemark();
                }
                if (StringUtils.isNotEmpty(thirdOrder.getMerchantRemark()))
                {
                    remark += "，商户备注：" + thirdOrder.getMerchantRemark();
                }
                expressOrder.setRemark(remark);

                // 保存快递订单
                int insertResult = expressOrderService.insertExpressOrder(expressOrder);
                if (insertResult > 0)
                {
                    Map<String, Object> successItem = new HashMap<>();
                    successItem.put("thirdOrderId", orderId);
                    successItem.put("thirdOrderNo", thirdOrder.getOrderNo());
                    successItem.put("expressOrderId", expressOrder.getId());
                    successItem.put("expressOrderNo", expressOrderNo);
                    successItem.put("receiverName", expressOrder.getReceiverName());
                    successList.add(successItem);
                    successCount++;
                }
                else
                {
                    failList.add(createFailItem(orderId, thirdOrder.getOrderNo(), "保存失败"));
                    failCount++;
                }
            }
            catch (Exception e)
            {
                failList.add(createFailItem(orderId, null, "处理异常：" + e.getMessage()));
                failCount++;
            }
        }

        // 构建返回结果
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("totalCount", orderIds.length);
        result.put("successList", successList);
        result.put("failList", failList);

        return result;
    }

    /**
     * 创建失败项
     */
    private Map<String, Object> createFailItem(Long orderId, String reason)
    {
        return createFailItem(orderId, null, reason);
    }

    /**
     * 创建失败项
     */
    private Map<String, Object> createFailItem(Long orderId, String orderNo, String reason)
    {
        Map<String, Object> item = new HashMap<>();
        item.put("thirdOrderId", orderId);
        item.put("thirdOrderNo", orderNo);
        item.put("reason", reason);
        return item;
    }

    /**
     * 导入第三方商品订单数据
     *
     * @param thirdOrderList 第三方商品订单数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importThirdOrder(List<ExpressThirdOrder> thirdOrderList, boolean isUpdateSupport)
    {
        if (StringUtils.isNull(thirdOrderList) || thirdOrderList.isEmpty())
        {
            throw new ServiceException("导入数据不能为空");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        // 遍历导入的订单列表
        for (ExpressThirdOrder thirdOrder : thirdOrderList)
        {
            try
            {
                // 检查订单是否已存在（根据订单号）
                ExpressThirdOrder existingOrder = new ExpressThirdOrder();
                existingOrder.setOrderNo(thirdOrder.getOrderNo());
                List<ExpressThirdOrder> existingOrders = expressThirdOrderMapper.selectExpressThirdOrderList(existingOrder);

                if (existingOrders == null || existingOrders.isEmpty())
                {
                    // 订单不存在，执行新增
                    this.insertExpressThirdOrder(thirdOrder);
                    successNum++;
                }
                else if (isUpdateSupport)
                {
                    // 订单已存在且支持更新，执行更新
                    thirdOrder.setOrderId(existingOrders.get(0).getOrderId());
                    this.updateExpressThirdOrder(thirdOrder);
                    successNum++;
                }
                else
                {
                    // 订单已存在但不支持更新，跳过
                    failureNum++;
                    failureMsg.append("、" + thirdOrder.getOrderNo());
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "“" + thirdOrder.getOrderNo() + "”导入失败：" + e.getMessage();
                failureMsg.append("、" + msg);
            }
        }

        // 构建返回消息
        if (failureNum > 0)
        {
            failureMsg.insert(0, "导入失败：");
            failureMsg.append("。");
        }
        else
        {
            failureMsg.append("未发现导入失败数据");
        }
        successMsg.append("成功导入数据");
        successMsg.append("：" + successNum + " 条");
        if (failureNum > 0)
        {
            successMsg.append("，失败 " + failureNum + " 条");
        }

        return successMsg.append("。" + failureMsg).toString();
    }
}
