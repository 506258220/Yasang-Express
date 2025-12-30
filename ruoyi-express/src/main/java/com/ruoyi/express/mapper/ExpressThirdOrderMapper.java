package com.ruoyi.express.mapper;

import com.ruoyi.express.domain.ExpressThirdOrder;

import java.util.List;

/**
 * 第三方商品订单（用于导入后批量打印快递单）Mapper接口
 *
 * @author apisflorea
 * @date 2025-12-29
 */
public interface ExpressThirdOrderMapper
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
     * 删除第三方商品订单（用于导入后批量打印快递单）
     *
     * @param orderId 第三方商品订单（用于导入后批量打印快递单）主键
     * @return 结果
     */
    public int deleteExpressThirdOrderByOrderId(Long orderId);

    /**
     * 批量删除第三方商品订单（用于导入后批量打印快递单）
     *
     * @param orderIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteExpressThirdOrderByOrderIds(Long[] orderIds);
}
