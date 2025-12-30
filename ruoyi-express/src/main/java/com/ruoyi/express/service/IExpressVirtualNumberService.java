package com.ruoyi.express.service;

import java.util.List;
import com.ruoyi.express.domain.ExpressVirtualNumber;

/**
 * 虚拟号管理Service接口
 * 
 * @author apisflorea
 * @date 2025-12-29
 */
public interface IExpressVirtualNumberService 
{
    /**
     * 查询虚拟号管理
     * 
     * @param id 虚拟号管理主键
     * @return 虚拟号管理
     */
    public ExpressVirtualNumber selectExpressVirtualNumberById(Long id);

    /**
     * 查询虚拟号管理列表
     * 
     * @param expressVirtualNumber 虚拟号管理
     * @return 虚拟号管理集合
     */
    public List<ExpressVirtualNumber> selectExpressVirtualNumberList(ExpressVirtualNumber expressVirtualNumber);

    /**
     * 新增虚拟号管理
     * 
     * @param expressVirtualNumber 虚拟号管理
     * @return 结果
     */
    public int insertExpressVirtualNumber(ExpressVirtualNumber expressVirtualNumber);

    /**
     * 修改虚拟号管理
     * 
     * @param expressVirtualNumber 虚拟号管理
     * @return 结果
     */
    public int updateExpressVirtualNumber(ExpressVirtualNumber expressVirtualNumber);

    /**
     * 批量删除虚拟号管理
     * 
     * @param ids 需要删除的虚拟号管理主键集合
     * @return 结果
     */
    public int deleteExpressVirtualNumberByIds(Long[] ids);

    /**
     * 删除虚拟号管理信息
     * 
     * @param id 虚拟号管理主键
     * @return 结果
     */
    public int deleteExpressVirtualNumberById(Long id);
}
