package com.ruoyi.express.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.express.mapper.ExpressOrderMapper;
import com.ruoyi.express.domain.ExpressOrder;
import com.ruoyi.express.service.IExpressOrderService;

/**
 * 订单管理Service业务层处理
 * 
 * @author apisflorea
 * @date 2025-12-17
 */
@Service
public class ExpressOrderServiceImpl implements IExpressOrderService 
{
    @Autowired
    private ExpressOrderMapper expressOrderMapper;

    /**
     * 查询订单管理
     * 
     * @param orderId 订单管理主键
     * @return 订单管理
     */
    @Override
    public ExpressOrder selectExpressOrderByOrderId(Long orderId)
    {
        return expressOrderMapper.selectExpressOrderByOrderId(orderId);
    }

    /**
     * 查询订单管理列表
     * 
     * @param expressOrder 订单管理
     * @return 订单管理
     */
    @Override
    public List<ExpressOrder> selectExpressOrderList(ExpressOrder expressOrder)
    {
        return expressOrderMapper.selectExpressOrderList(expressOrder);
    }

    /**
     * 新增订单管理
     * 
     * @param expressOrder 订单管理
     * @return 结果
     */
    @Override
    public int insertExpressOrder(ExpressOrder expressOrder)
    {
        expressOrder.setCreateTime(DateUtils.getNowDate());
        return expressOrderMapper.insertExpressOrder(expressOrder);
    }

    /**
     * 修改订单管理
     * 
     * @param expressOrder 订单管理
     * @return 结果
     */
    @Override
    public int updateExpressOrder(ExpressOrder expressOrder)
    {
        expressOrder.setUpdateTime(DateUtils.getNowDate());
        return expressOrderMapper.updateExpressOrder(expressOrder);
    }

    /**
     * 批量删除订单管理
     * 
     * @param orderIds 需要删除的订单管理主键
     * @return 结果
     */
    @Override
    public int deleteExpressOrderByOrderIds(Long[] orderIds)
    {
        return expressOrderMapper.deleteExpressOrderByOrderIds(orderIds);
    }

    /**
     * 删除订单管理信息
     * 
     * @param orderId 订单管理主键
     * @return 结果
     */
    @Override
    public int deleteExpressOrderByOrderId(Long orderId)
    {
        return expressOrderMapper.deleteExpressOrderByOrderId(orderId);
    }
}
