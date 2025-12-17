package com.ruoyi.express.service;

import java.util.List;
import com.ruoyi.express.domain.ExpressOrder;

/**
 * 订单管理Service接口
 * 
 * @author apisflorea
 * @date 2025-12-17
 */
public interface IExpressOrderService 
{
    /**
     * 查询订单管理
     * 
     * @param orderId 订单管理主键
     * @return 订单管理
     */
    public ExpressOrder selectExpressOrderByOrderId(Long orderId);

    /**
     * 查询订单管理列表
     * 
     * @param expressOrder 订单管理
     * @return 订单管理集合
     */
    public List<ExpressOrder> selectExpressOrderList(ExpressOrder expressOrder);

    /**
     * 新增订单管理
     * 
     * @param expressOrder 订单管理
     * @return 结果
     */
    public int insertExpressOrder(ExpressOrder expressOrder);

    /**
     * 修改订单管理
     * 
     * @param expressOrder 订单管理
     * @return 结果
     */
    public int updateExpressOrder(ExpressOrder expressOrder);

    /**
     * 批量删除订单管理
     * 
     * @param orderIds 需要删除的订单管理主键集合
     * @return 结果
     */
    public int deleteExpressOrderByOrderIds(Long[] orderIds);

    /**
     * 删除订单管理信息
     * 
     * @param orderId 订单管理主键
     * @return 结果
     */
    public int deleteExpressOrderByOrderId(Long orderId);
}
