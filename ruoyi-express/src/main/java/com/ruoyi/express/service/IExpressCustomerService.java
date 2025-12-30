package com.ruoyi.express.service;

import java.util.List;
import com.ruoyi.express.domain.ExpressCustomer;

/**
 * 客户信息Service接口
 * 
 * @author apisflorea
 * @date 2025-12-29
 */
public interface IExpressCustomerService 
{
    /**
     * 查询客户信息
     * 
     * @param id 客户信息主键
     * @return 客户信息
     */
    public ExpressCustomer selectExpressCustomerById(Long id);

    /**
     * 查询客户信息列表
     * 
     * @param expressCustomer 客户信息
     * @return 客户信息集合
     */
    public List<ExpressCustomer> selectExpressCustomerList(ExpressCustomer expressCustomer);

    /**
     * 新增客户信息
     * 
     * @param expressCustomer 客户信息
     * @return 结果
     */
    public int insertExpressCustomer(ExpressCustomer expressCustomer);

    /**
     * 修改客户信息
     * 
     * @param expressCustomer 客户信息
     * @return 结果
     */
    public int updateExpressCustomer(ExpressCustomer expressCustomer);

    /**
     * 批量删除客户信息
     * 
     * @param ids 需要删除的客户信息主键集合
     * @return 结果
     */
    public int deleteExpressCustomerByIds(Long[] ids);

    /**
     * 删除客户信息信息
     * 
     * @param id 客户信息主键
     * @return 结果
     */
    public int deleteExpressCustomerById(Long id);
}
