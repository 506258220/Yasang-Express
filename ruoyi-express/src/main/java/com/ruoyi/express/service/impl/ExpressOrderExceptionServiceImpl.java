package com.ruoyi.express.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.express.mapper.ExpressOrderExceptionMapper;
import com.ruoyi.express.domain.ExpressOrderException;
import com.ruoyi.express.service.IExpressOrderExceptionService;

/**
 * 订单异常记录Service业务层处理
 * 
 * @author apisflorea
 * @date 2025-12-29
 */
@Service
public class ExpressOrderExceptionServiceImpl implements IExpressOrderExceptionService 
{
    @Autowired
    private ExpressOrderExceptionMapper expressOrderExceptionMapper;

    /**
     * 查询订单异常记录
     * 
     * @param id 订单异常记录主键
     * @return 订单异常记录
     */
    @Override
    public ExpressOrderException selectExpressOrderExceptionById(Long id)
    {
        return expressOrderExceptionMapper.selectExpressOrderExceptionById(id);
    }

    /**
     * 查询订单异常记录列表
     * 
     * @param expressOrderException 订单异常记录
     * @return 订单异常记录
     */
    @Override
    public List<ExpressOrderException> selectExpressOrderExceptionList(ExpressOrderException expressOrderException)
    {
        return expressOrderExceptionMapper.selectExpressOrderExceptionList(expressOrderException);
    }

    /**
     * 新增订单异常记录
     * 
     * @param expressOrderException 订单异常记录
     * @return 结果
     */
    @Override
    public int insertExpressOrderException(ExpressOrderException expressOrderException)
    {
        expressOrderException.setCreateTime(DateUtils.getNowDate());
        return expressOrderExceptionMapper.insertExpressOrderException(expressOrderException);
    }

    /**
     * 修改订单异常记录
     * 
     * @param expressOrderException 订单异常记录
     * @return 结果
     */
    @Override
    public int updateExpressOrderException(ExpressOrderException expressOrderException)
    {
        expressOrderException.setUpdateTime(DateUtils.getNowDate());
        return expressOrderExceptionMapper.updateExpressOrderException(expressOrderException);
    }

    /**
     * 批量删除订单异常记录
     * 
     * @param ids 需要删除的订单异常记录主键
     * @return 结果
     */
    @Override
    public int deleteExpressOrderExceptionByIds(Long[] ids)
    {
        return expressOrderExceptionMapper.deleteExpressOrderExceptionByIds(ids);
    }

    /**
     * 删除订单异常记录信息
     * 
     * @param id 订单异常记录主键
     * @return 结果
     */
    @Override
    public int deleteExpressOrderExceptionById(Long id)
    {
        return expressOrderExceptionMapper.deleteExpressOrderExceptionById(id);
    }
}
