package com.ruoyi.express.mapper;

import java.util.List;
import com.ruoyi.express.domain.ExpressWaybill;

/**
 * 快递单Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-17
 */
public interface ExpressWaybillMapper 
{
    /**
     * 查询快递单
     * 
     * @param waybillId 快递单主键
     * @return 快递单
     */
    public ExpressWaybill selectExpressWaybillByWaybillId(Long waybillId);

    /**
     * 查询快递单列表
     * 
     * @param expressWaybill 快递单
     * @return 快递单集合
     */
    public List<ExpressWaybill> selectExpressWaybillList(ExpressWaybill expressWaybill);

    /**
     * 新增快递单
     * 
     * @param expressWaybill 快递单
     * @return 结果
     */
    public int insertExpressWaybill(ExpressWaybill expressWaybill);

    /**
     * 修改快递单
     * 
     * @param expressWaybill 快递单
     * @return 结果
     */
    public int updateExpressWaybill(ExpressWaybill expressWaybill);

    /**
     * 删除快递单
     * 
     * @param waybillId 快递单主键
     * @return 结果
     */
    public int deleteExpressWaybillByWaybillId(Long waybillId);

    /**
     * 批量删除快递单
     * 
     * @param waybillIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteExpressWaybillByWaybillIds(Long[] waybillIds);
}
