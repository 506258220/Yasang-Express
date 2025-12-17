package com.ruoyi.express.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.express.mapper.ExpressWaybillMapper;
import com.ruoyi.express.domain.ExpressWaybill;
import com.ruoyi.express.service.IExpressWaybillService;

/**
 * 快递单Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-17
 */
@Service
public class ExpressWaybillServiceImpl implements IExpressWaybillService 
{
    @Autowired
    private ExpressWaybillMapper expressWaybillMapper;

    /**
     * 查询快递单
     * 
     * @param waybillId 快递单主键
     * @return 快递单
     */
    @Override
    public ExpressWaybill selectExpressWaybillByWaybillId(Long waybillId)
    {
        return expressWaybillMapper.selectExpressWaybillByWaybillId(waybillId);
    }

    /**
     * 查询快递单列表
     * 
     * @param expressWaybill 快递单
     * @return 快递单
     */
    @Override
    public List<ExpressWaybill> selectExpressWaybillList(ExpressWaybill expressWaybill)
    {
        return expressWaybillMapper.selectExpressWaybillList(expressWaybill);
    }

    /**
     * 新增快递单
     * 
     * @param expressWaybill 快递单
     * @return 结果
     */
    @Override
    public int insertExpressWaybill(ExpressWaybill expressWaybill)
    {
        expressWaybill.setCreateTime(DateUtils.getNowDate());
        return expressWaybillMapper.insertExpressWaybill(expressWaybill);
    }

    /**
     * 修改快递单
     * 
     * @param expressWaybill 快递单
     * @return 结果
     */
    @Override
    public int updateExpressWaybill(ExpressWaybill expressWaybill)
    {
        expressWaybill.setUpdateTime(DateUtils.getNowDate());
        return expressWaybillMapper.updateExpressWaybill(expressWaybill);
    }

    /**
     * 批量删除快递单
     * 
     * @param waybillIds 需要删除的快递单主键
     * @return 结果
     */
    @Override
    public int deleteExpressWaybillByWaybillIds(Long[] waybillIds)
    {
        return expressWaybillMapper.deleteExpressWaybillByWaybillIds(waybillIds);
    }

    /**
     * 删除快递单信息
     * 
     * @param waybillId 快递单主键
     * @return 结果
     */
    @Override
    public int deleteExpressWaybillByWaybillId(Long waybillId)
    {
        return expressWaybillMapper.deleteExpressWaybillByWaybillId(waybillId);
    }
}
