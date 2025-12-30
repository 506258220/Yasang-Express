package com.ruoyi.express.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.express.mapper.ExpressOrderMapper;
import com.ruoyi.express.domain.ExpressOrder;
import com.ruoyi.express.service.IExpressOrderService;

/**
 * 快递订单Service业务层处理
 * 
 * @author apisflorea
 * @date 2025-12-29
 */
@Service
public class ExpressOrderServiceImpl implements IExpressOrderService 
{
    @Autowired
    private ExpressOrderMapper expressOrderMapper;

    /**
     * 查询快递订单
     * 
     * @param id 快递订单主键
     * @return 快递订单
     */
    @Override
    public ExpressOrder selectExpressOrderById(Long id)
    {
        return expressOrderMapper.selectExpressOrderById(id);
    }

    /**
     * 查询快递订单列表
     * 
     * @param expressOrder 快递订单
     * @return 快递订单
     */
    @Override
    public List<ExpressOrder> selectExpressOrderList(ExpressOrder expressOrder)
    {
        return expressOrderMapper.selectExpressOrderList(expressOrder);
    }

    /**
     * 新增快递订单
     * 
     * @param expressOrder 快递订单
     * @return 结果
     */
    @Override
    public int insertExpressOrder(ExpressOrder expressOrder)
    {
        expressOrder.setCreateTime(DateUtils.getNowDate());
        return expressOrderMapper.insertExpressOrder(expressOrder);
    }

    /**
     * 修改快递订单
     * 
     * @param expressOrder 快递订单
     * @return 结果
     */
    @Override
    public int updateExpressOrder(ExpressOrder expressOrder)
    {
        expressOrder.setUpdateTime(DateUtils.getNowDate());
        return expressOrderMapper.updateExpressOrder(expressOrder);
    }

    /**
     * 批量删除快递订单
     * 
     * @param ids 需要删除的快递订单主键
     * @return 结果
     */
    @Override
    public int deleteExpressOrderByIds(Long[] ids)
    {
        return expressOrderMapper.deleteExpressOrderByIds(ids);
    }

    /**
     * 删除快递订单信息
     * 
     * @param id 快递订单主键
     * @return 结果
     */
    @Override
    public int deleteExpressOrderById(Long id)
    {
        return expressOrderMapper.deleteExpressOrderById(id);
    }
}
