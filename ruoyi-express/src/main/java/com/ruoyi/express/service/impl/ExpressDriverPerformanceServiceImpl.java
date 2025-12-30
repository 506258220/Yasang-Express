package com.ruoyi.express.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.express.mapper.ExpressDriverPerformanceMapper;
import com.ruoyi.express.domain.ExpressDriverPerformance;
import com.ruoyi.express.service.IExpressDriverPerformanceService;

/**
 * 司机业绩统计Service业务层处理
 * 
 * @author apisflorea
 * @date 2025-12-29
 */
@Service
public class ExpressDriverPerformanceServiceImpl implements IExpressDriverPerformanceService 
{
    @Autowired
    private ExpressDriverPerformanceMapper expressDriverPerformanceMapper;

    /**
     * 查询司机业绩统计
     * 
     * @param id 司机业绩统计主键
     * @return 司机业绩统计
     */
    @Override
    public ExpressDriverPerformance selectExpressDriverPerformanceById(Long id)
    {
        return expressDriverPerformanceMapper.selectExpressDriverPerformanceById(id);
    }

    /**
     * 查询司机业绩统计列表
     * 
     * @param expressDriverPerformance 司机业绩统计
     * @return 司机业绩统计
     */
    @Override
    public List<ExpressDriverPerformance> selectExpressDriverPerformanceList(ExpressDriverPerformance expressDriverPerformance)
    {
        return expressDriverPerformanceMapper.selectExpressDriverPerformanceList(expressDriverPerformance);
    }

    /**
     * 新增司机业绩统计
     * 
     * @param expressDriverPerformance 司机业绩统计
     * @return 结果
     */
    @Override
    public int insertExpressDriverPerformance(ExpressDriverPerformance expressDriverPerformance)
    {
        expressDriverPerformance.setCreateTime(DateUtils.getNowDate());
        return expressDriverPerformanceMapper.insertExpressDriverPerformance(expressDriverPerformance);
    }

    /**
     * 修改司机业绩统计
     * 
     * @param expressDriverPerformance 司机业绩统计
     * @return 结果
     */
    @Override
    public int updateExpressDriverPerformance(ExpressDriverPerformance expressDriverPerformance)
    {
        expressDriverPerformance.setUpdateTime(DateUtils.getNowDate());
        return expressDriverPerformanceMapper.updateExpressDriverPerformance(expressDriverPerformance);
    }

    /**
     * 批量删除司机业绩统计
     * 
     * @param ids 需要删除的司机业绩统计主键
     * @return 结果
     */
    @Override
    public int deleteExpressDriverPerformanceByIds(Long[] ids)
    {
        return expressDriverPerformanceMapper.deleteExpressDriverPerformanceByIds(ids);
    }

    /**
     * 删除司机业绩统计信息
     * 
     * @param id 司机业绩统计主键
     * @return 结果
     */
    @Override
    public int deleteExpressDriverPerformanceById(Long id)
    {
        return expressDriverPerformanceMapper.deleteExpressDriverPerformanceById(id);
    }
}
