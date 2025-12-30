package com.ruoyi.express.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.express.mapper.ExpressVirtualNumberMapper;
import com.ruoyi.express.domain.ExpressVirtualNumber;
import com.ruoyi.express.service.IExpressVirtualNumberService;

/**
 * 虚拟号管理Service业务层处理
 * 
 * @author apisflorea
 * @date 2025-12-29
 */
@Service
public class ExpressVirtualNumberServiceImpl implements IExpressVirtualNumberService 
{
    @Autowired
    private ExpressVirtualNumberMapper expressVirtualNumberMapper;

    /**
     * 查询虚拟号管理
     * 
     * @param id 虚拟号管理主键
     * @return 虚拟号管理
     */
    @Override
    public ExpressVirtualNumber selectExpressVirtualNumberById(Long id)
    {
        return expressVirtualNumberMapper.selectExpressVirtualNumberById(id);
    }

    /**
     * 查询虚拟号管理列表
     * 
     * @param expressVirtualNumber 虚拟号管理
     * @return 虚拟号管理
     */
    @Override
    public List<ExpressVirtualNumber> selectExpressVirtualNumberList(ExpressVirtualNumber expressVirtualNumber)
    {
        return expressVirtualNumberMapper.selectExpressVirtualNumberList(expressVirtualNumber);
    }

    /**
     * 新增虚拟号管理
     * 
     * @param expressVirtualNumber 虚拟号管理
     * @return 结果
     */
    @Override
    public int insertExpressVirtualNumber(ExpressVirtualNumber expressVirtualNumber)
    {
        expressVirtualNumber.setCreateTime(DateUtils.getNowDate());
        return expressVirtualNumberMapper.insertExpressVirtualNumber(expressVirtualNumber);
    }

    /**
     * 修改虚拟号管理
     * 
     * @param expressVirtualNumber 虚拟号管理
     * @return 结果
     */
    @Override
    public int updateExpressVirtualNumber(ExpressVirtualNumber expressVirtualNumber)
    {
        expressVirtualNumber.setUpdateTime(DateUtils.getNowDate());
        return expressVirtualNumberMapper.updateExpressVirtualNumber(expressVirtualNumber);
    }

    /**
     * 批量删除虚拟号管理
     * 
     * @param ids 需要删除的虚拟号管理主键
     * @return 结果
     */
    @Override
    public int deleteExpressVirtualNumberByIds(Long[] ids)
    {
        return expressVirtualNumberMapper.deleteExpressVirtualNumberByIds(ids);
    }

    /**
     * 删除虚拟号管理信息
     * 
     * @param id 虚拟号管理主键
     * @return 结果
     */
    @Override
    public int deleteExpressVirtualNumberById(Long id)
    {
        return expressVirtualNumberMapper.deleteExpressVirtualNumberById(id);
    }
}
