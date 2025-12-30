package com.ruoyi.express.mapper;

import java.util.List;
import com.ruoyi.express.domain.ExpressGpsRecord;

/**
 * GPS定位记录Mapper接口
 * 
 * @author apisflorea
 * @date 2025-12-29
 */
public interface ExpressGpsRecordMapper 
{
    /**
     * 查询GPS定位记录
     * 
     * @param id GPS定位记录主键
     * @return GPS定位记录
     */
    public ExpressGpsRecord selectExpressGpsRecordById(Long id);

    /**
     * 查询GPS定位记录列表
     * 
     * @param expressGpsRecord GPS定位记录
     * @return GPS定位记录集合
     */
    public List<ExpressGpsRecord> selectExpressGpsRecordList(ExpressGpsRecord expressGpsRecord);

    /**
     * 新增GPS定位记录
     * 
     * @param expressGpsRecord GPS定位记录
     * @return 结果
     */
    public int insertExpressGpsRecord(ExpressGpsRecord expressGpsRecord);

    /**
     * 修改GPS定位记录
     * 
     * @param expressGpsRecord GPS定位记录
     * @return 结果
     */
    public int updateExpressGpsRecord(ExpressGpsRecord expressGpsRecord);

    /**
     * 删除GPS定位记录
     * 
     * @param id GPS定位记录主键
     * @return 结果
     */
    public int deleteExpressGpsRecordById(Long id);

    /**
     * 批量删除GPS定位记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteExpressGpsRecordByIds(Long[] ids);
}
