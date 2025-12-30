package com.ruoyi.express.service;

import java.util.List;
import com.ruoyi.express.domain.ExpressDriverPerformance;

/**
 * 司机业绩统计Service接口
 * 
 * @author apisflorea
 * @date 2025-12-29
 */
public interface IExpressDriverPerformanceService 
{
    /**
     * 查询司机业绩统计
     * 
     * @param id 司机业绩统计主键
     * @return 司机业绩统计
     */
    public ExpressDriverPerformance selectExpressDriverPerformanceById(Long id);

    /**
     * 查询司机业绩统计列表
     * 
     * @param expressDriverPerformance 司机业绩统计
     * @return 司机业绩统计集合
     */
    public List<ExpressDriverPerformance> selectExpressDriverPerformanceList(ExpressDriverPerformance expressDriverPerformance);

    /**
     * 新增司机业绩统计
     * 
     * @param expressDriverPerformance 司机业绩统计
     * @return 结果
     */
    public int insertExpressDriverPerformance(ExpressDriverPerformance expressDriverPerformance);

    /**
     * 修改司机业绩统计
     * 
     * @param expressDriverPerformance 司机业绩统计
     * @return 结果
     */
    public int updateExpressDriverPerformance(ExpressDriverPerformance expressDriverPerformance);

    /**
     * 批量删除司机业绩统计
     * 
     * @param ids 需要删除的司机业绩统计主键集合
     * @return 结果
     */
    public int deleteExpressDriverPerformanceByIds(Long[] ids);

    /**
     * 删除司机业绩统计信息
     * 
     * @param id 司机业绩统计主键
     * @return 结果
     */
    public int deleteExpressDriverPerformanceById(Long id);
}
