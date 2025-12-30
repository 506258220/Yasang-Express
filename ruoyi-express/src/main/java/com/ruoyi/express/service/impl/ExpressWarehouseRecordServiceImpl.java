package com.ruoyi.express.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.express.mapper.ExpressWarehouseRecordMapper;
import com.ruoyi.express.domain.ExpressWarehouseRecord;
import com.ruoyi.express.service.IExpressWarehouseRecordService;

/**
 * 仓库出入库记录Service业务层处理
 * 
 * @author apisflorea
 * @date 2025-12-29
 */
@Service
public class ExpressWarehouseRecordServiceImpl implements IExpressWarehouseRecordService 
{
    @Autowired
    private ExpressWarehouseRecordMapper expressWarehouseRecordMapper;

    /**
     * 查询仓库出入库记录
     * 
     * @param id 仓库出入库记录主键
     * @return 仓库出入库记录
     */
    @Override
    public ExpressWarehouseRecord selectExpressWarehouseRecordById(Long id)
    {
        return expressWarehouseRecordMapper.selectExpressWarehouseRecordById(id);
    }

    /**
     * 查询仓库出入库记录列表
     * 
     * @param expressWarehouseRecord 仓库出入库记录
     * @return 仓库出入库记录
     */
    @Override
    public List<ExpressWarehouseRecord> selectExpressWarehouseRecordList(ExpressWarehouseRecord expressWarehouseRecord)
    {
        return expressWarehouseRecordMapper.selectExpressWarehouseRecordList(expressWarehouseRecord);
    }

    /**
     * 新增仓库出入库记录
     * 
     * @param expressWarehouseRecord 仓库出入库记录
     * @return 结果
     */
    @Override
    public int insertExpressWarehouseRecord(ExpressWarehouseRecord expressWarehouseRecord)
    {
        expressWarehouseRecord.setCreateTime(DateUtils.getNowDate());
        return expressWarehouseRecordMapper.insertExpressWarehouseRecord(expressWarehouseRecord);
    }

    /**
     * 修改仓库出入库记录
     * 
     * @param expressWarehouseRecord 仓库出入库记录
     * @return 结果
     */
    @Override
    public int updateExpressWarehouseRecord(ExpressWarehouseRecord expressWarehouseRecord)
    {
        expressWarehouseRecord.setUpdateTime(DateUtils.getNowDate());
        return expressWarehouseRecordMapper.updateExpressWarehouseRecord(expressWarehouseRecord);
    }

    /**
     * 批量删除仓库出入库记录
     * 
     * @param ids 需要删除的仓库出入库记录主键
     * @return 结果
     */
    @Override
    public int deleteExpressWarehouseRecordByIds(Long[] ids)
    {
        return expressWarehouseRecordMapper.deleteExpressWarehouseRecordByIds(ids);
    }

    /**
     * 删除仓库出入库记录信息
     * 
     * @param id 仓库出入库记录主键
     * @return 结果
     */
    @Override
    public int deleteExpressWarehouseRecordById(Long id)
    {
        return expressWarehouseRecordMapper.deleteExpressWarehouseRecordById(id);
    }
}
