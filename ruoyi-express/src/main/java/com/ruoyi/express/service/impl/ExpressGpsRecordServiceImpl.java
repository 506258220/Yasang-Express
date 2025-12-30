package com.ruoyi.express.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.express.mapper.ExpressGpsRecordMapper;
import com.ruoyi.express.domain.ExpressGpsRecord;
import com.ruoyi.express.service.IExpressGpsRecordService;

/**
 * GPS定位记录Service业务层处理
 * 
 * @author apisflorea
 * @date 2025-12-29
 */
@Service
public class ExpressGpsRecordServiceImpl implements IExpressGpsRecordService 
{
    @Autowired
    private ExpressGpsRecordMapper expressGpsRecordMapper;

    /**
     * 查询GPS定位记录
     * 
     * @param id GPS定位记录主键
     * @return GPS定位记录
     */
    @Override
    public ExpressGpsRecord selectExpressGpsRecordById(Long id)
    {
        return expressGpsRecordMapper.selectExpressGpsRecordById(id);
    }

    /**
     * 查询GPS定位记录列表
     * 
     * @param expressGpsRecord GPS定位记录
     * @return GPS定位记录
     */
    @Override
    public List<ExpressGpsRecord> selectExpressGpsRecordList(ExpressGpsRecord expressGpsRecord)
    {
        return expressGpsRecordMapper.selectExpressGpsRecordList(expressGpsRecord);
    }

    /**
     * 新增GPS定位记录
     * 
     * @param expressGpsRecord GPS定位记录
     * @return 结果
     */
    @Override
    public int insertExpressGpsRecord(ExpressGpsRecord expressGpsRecord)
    {
        expressGpsRecord.setCreateTime(DateUtils.getNowDate());
        return expressGpsRecordMapper.insertExpressGpsRecord(expressGpsRecord);
    }

    /**
     * 修改GPS定位记录
     * 
     * @param expressGpsRecord GPS定位记录
     * @return 结果
     */
    @Override
    public int updateExpressGpsRecord(ExpressGpsRecord expressGpsRecord)
    {
        expressGpsRecord.setUpdateTime(DateUtils.getNowDate());
        return expressGpsRecordMapper.updateExpressGpsRecord(expressGpsRecord);
    }

    /**
     * 批量删除GPS定位记录
     * 
     * @param ids 需要删除的GPS定位记录主键
     * @return 结果
     */
    @Override
    public int deleteExpressGpsRecordByIds(Long[] ids)
    {
        return expressGpsRecordMapper.deleteExpressGpsRecordByIds(ids);
    }

    /**
     * 删除GPS定位记录信息
     * 
     * @param id GPS定位记录主键
     * @return 结果
     */
    @Override
    public int deleteExpressGpsRecordById(Long id)
    {
        return expressGpsRecordMapper.deleteExpressGpsRecordById(id);
    }
}
