package com.ruoyi.express.service;

import java.util.List;
import com.ruoyi.express.domain.ExpressWarehouseRecord;

/**
 * 仓库出入库记录Service接口
 * 
 * @author apisflorea
 * @date 2025-12-29
 */
public interface IExpressWarehouseRecordService 
{
    /**
     * 查询仓库出入库记录
     * 
     * @param id 仓库出入库记录主键
     * @return 仓库出入库记录
     */
    public ExpressWarehouseRecord selectExpressWarehouseRecordById(Long id);

    /**
     * 查询仓库出入库记录列表
     * 
     * @param expressWarehouseRecord 仓库出入库记录
     * @return 仓库出入库记录集合
     */
    public List<ExpressWarehouseRecord> selectExpressWarehouseRecordList(ExpressWarehouseRecord expressWarehouseRecord);

    /**
     * 新增仓库出入库记录
     * 
     * @param expressWarehouseRecord 仓库出入库记录
     * @return 结果
     */
    public int insertExpressWarehouseRecord(ExpressWarehouseRecord expressWarehouseRecord);

    /**
     * 修改仓库出入库记录
     * 
     * @param expressWarehouseRecord 仓库出入库记录
     * @return 结果
     */
    public int updateExpressWarehouseRecord(ExpressWarehouseRecord expressWarehouseRecord);

    /**
     * 批量删除仓库出入库记录
     * 
     * @param ids 需要删除的仓库出入库记录主键集合
     * @return 结果
     */
    public int deleteExpressWarehouseRecordByIds(Long[] ids);

    /**
     * 删除仓库出入库记录信息
     * 
     * @param id 仓库出入库记录主键
     * @return 结果
     */
    public int deleteExpressWarehouseRecordById(Long id);
}
