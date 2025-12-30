package com.ruoyi.express.service;

import com.ruoyi.express.domain.ExpressThirdOrder;

import java.util.List;

/**
 * 第三方商品订单（用于导入后批量打印快递单）Service接口
 *
 * @author apisflorea
 * @date 2025-12-29
 */
public interface IExpressThirdOrderService
{
    /**
     * 查询第三方商品订单（用于导入后批量打印快递单）
     *
     * @param orderId 第三方商品订单（用于导入后批量打印快递单）主键
     * @return 第三方商品订单（用于导入后批量打印快递单）
     */
    public ExpressThirdOrder selectExpressThirdOrderByOrderId(Long orderId);

    /**
     * 查询第三方商品订单（用于导入后批量打印快递单）列表
     *
     * @param expressThirdOrder 第三方商品订单（用于导入后批量打印快递单）
     * @return 第三方商品订单（用于导入后批量打印快递单）集合
     */
    public List<ExpressThirdOrder> selectExpressThirdOrderList(ExpressThirdOrder expressThirdOrder);

    /**
     * 新增第三方商品订单（用于导入后批量打印快递单）
     *
     * @param expressThirdOrder 第三方商品订单（用于导入后批量打印快递单）
     * @return 结果
     */
    public int insertExpressThirdOrder(ExpressThirdOrder expressThirdOrder);

    /**
     * 修改第三方商品订单（用于导入后批量打印快递单）
     *
     * @param expressThirdOrder 第三方商品订单（用于导入后批量打印快递单）
     * @return 结果
     */
    public int updateExpressThirdOrder(ExpressThirdOrder expressThirdOrder);

    /**
     * 批量删除第三方商品订单（用于导入后批量打印快递单）
     *
     * @param orderIds 需要删除的第三方商品订单（用于导入后批量打印快递单）主键集合
     * @return 结果
     */
    public int deleteExpressThirdOrderByOrderIds(Long[] orderIds);

    /**
     * 删除第三方商品订单（用于导入后批量打印快递单）信息
     *
     * @param orderId 第三方商品订单（用于导入后批量打印快递单）主键
     * @return 结果
     */
    public int deleteExpressThirdOrderByOrderId(Long orderId);

    /**
     * 批量生成快递订单
     *
     * @param orderIds 第三方订单ID数组
     * @param config 生成配置（寄件人信息、默认值等）
     * @return 生成结果（成功数量、失败数量、详细信息）
     */
    public java.util.Map<String, Object> batchGenerateExpressOrders(Long[] orderIds, java.util.Map<String, Object> config);

    /**
     * 导入第三方商品订单数据
     *
     * @param thirdOrderList 第三方商品订单数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @return 结果
     */
    public String importThirdOrder(List<ExpressThirdOrder> thirdOrderList, boolean isUpdateSupport);
}
