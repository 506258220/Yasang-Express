package com.ruoyi.express.service;

import java.util.List;
import com.ruoyi.express.domain.ExpressOrderException;

/**
 * 订单异常记录Service接口
 * 
 * @author apisflorea
 * @date 2025-12-29
 */
public interface IExpressOrderExceptionService 
{
    /**
     * 查询订单异常记录
     * 
     * @param id 订单异常记录主键
     * @return 订单异常记录
     */
    public ExpressOrderException selectExpressOrderExceptionById(Long id);

    /**
     * 查询订单异常记录列表
     * 
     * @param expressOrderException 订单异常记录
     * @return 订单异常记录集合
     */
    public List<ExpressOrderException> selectExpressOrderExceptionList(ExpressOrderException expressOrderException);

    /**
     * 新增订单异常记录
     * 
     * @param expressOrderException 订单异常记录
     * @return 结果
     */
    public int insertExpressOrderException(ExpressOrderException expressOrderException);

    /**
     * 修改订单异常记录
     * 
     * @param expressOrderException 订单异常记录
     * @return 结果
     */
    public int updateExpressOrderException(ExpressOrderException expressOrderException);

    /**
     * 批量删除订单异常记录
     * 
     * @param ids 需要删除的订单异常记录主键集合
     * @return 结果
     */
    public int deleteExpressOrderExceptionByIds(Long[] ids);

    /**
     * 删除订单异常记录信息
     * 
     * @param id 订单异常记录主键
     * @return 结果
     */
    public int deleteExpressOrderExceptionById(Long id);
}
