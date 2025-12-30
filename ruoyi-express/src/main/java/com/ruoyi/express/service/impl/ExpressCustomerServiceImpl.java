package com.ruoyi.express.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.express.mapper.ExpressCustomerMapper;
import com.ruoyi.express.domain.ExpressCustomer;
import com.ruoyi.express.service.IExpressCustomerService;

/**
 * 客户信息Service业务层处理
 * 
 * @author apisflorea
 * @date 2025-12-29
 */
@Service
public class ExpressCustomerServiceImpl implements IExpressCustomerService 
{
    @Autowired
    private ExpressCustomerMapper expressCustomerMapper;

    /**
     * 查询客户信息
     * 
     * @param id 客户信息主键
     * @return 客户信息
     */
    @Override
    public ExpressCustomer selectExpressCustomerById(Long id)
    {
        return expressCustomerMapper.selectExpressCustomerById(id);
    }

    /**
     * 查询客户信息列表
     * 
     * @param expressCustomer 客户信息
     * @return 客户信息
     */
    @Override
    public List<ExpressCustomer> selectExpressCustomerList(ExpressCustomer expressCustomer)
    {
        return expressCustomerMapper.selectExpressCustomerList(expressCustomer);
    }

    /**
     * 新增客户信息
     * 
     * @param expressCustomer 客户信息
     * @return 结果
     */
    @Override
    public int insertExpressCustomer(ExpressCustomer expressCustomer)
    {
        expressCustomer.setCreateTime(DateUtils.getNowDate());
        return expressCustomerMapper.insertExpressCustomer(expressCustomer);
    }

    /**
     * 修改客户信息
     * 
     * @param expressCustomer 客户信息
     * @return 结果
     */
    @Override
    public int updateExpressCustomer(ExpressCustomer expressCustomer)
    {
        expressCustomer.setUpdateTime(DateUtils.getNowDate());
        return expressCustomerMapper.updateExpressCustomer(expressCustomer);
    }

    /**
     * 批量删除客户信息
     * 
     * @param ids 需要删除的客户信息主键
     * @return 结果
     */
    @Override
    public int deleteExpressCustomerByIds(Long[] ids)
    {
        return expressCustomerMapper.deleteExpressCustomerByIds(ids);
    }

    /**
     * 删除客户信息信息
     * 
     * @param id 客户信息主键
     * @return 结果
     */
    @Override
    public int deleteExpressCustomerById(Long id)
    {
        return expressCustomerMapper.deleteExpressCustomerById(id);
    }
}
