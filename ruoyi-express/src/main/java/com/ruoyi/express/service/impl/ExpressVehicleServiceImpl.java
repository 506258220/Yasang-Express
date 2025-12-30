package com.ruoyi.express.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.express.mapper.ExpressVehicleMapper;
import com.ruoyi.express.domain.ExpressVehicle;
import com.ruoyi.express.service.IExpressVehicleService;

/**
 * 车辆信息Service业务层处理
 * 
 * @author apisflorea
 * @date 2025-12-29
 */
@Service
public class ExpressVehicleServiceImpl implements IExpressVehicleService 
{
    @Autowired
    private ExpressVehicleMapper expressVehicleMapper;

    /**
     * 查询车辆信息
     * 
     * @param id 车辆信息主键
     * @return 车辆信息
     */
    @Override
    public ExpressVehicle selectExpressVehicleById(Long id)
    {
        return expressVehicleMapper.selectExpressVehicleById(id);
    }

    /**
     * 查询车辆信息列表
     * 
     * @param expressVehicle 车辆信息
     * @return 车辆信息
     */
    @Override
    public List<ExpressVehicle> selectExpressVehicleList(ExpressVehicle expressVehicle)
    {
        return expressVehicleMapper.selectExpressVehicleList(expressVehicle);
    }

    /**
     * 新增车辆信息
     * 
     * @param expressVehicle 车辆信息
     * @return 结果
     */
    @Override
    public int insertExpressVehicle(ExpressVehicle expressVehicle)
    {
        expressVehicle.setCreateTime(DateUtils.getNowDate());
        return expressVehicleMapper.insertExpressVehicle(expressVehicle);
    }

    /**
     * 修改车辆信息
     * 
     * @param expressVehicle 车辆信息
     * @return 结果
     */
    @Override
    public int updateExpressVehicle(ExpressVehicle expressVehicle)
    {
        expressVehicle.setUpdateTime(DateUtils.getNowDate());
        return expressVehicleMapper.updateExpressVehicle(expressVehicle);
    }

    /**
     * 批量删除车辆信息
     * 
     * @param ids 需要删除的车辆信息主键
     * @return 结果
     */
    @Override
    public int deleteExpressVehicleByIds(Long[] ids)
    {
        return expressVehicleMapper.deleteExpressVehicleByIds(ids);
    }

    /**
     * 删除车辆信息信息
     * 
     * @param id 车辆信息主键
     * @return 结果
     */
    @Override
    public int deleteExpressVehicleById(Long id)
    {
        return expressVehicleMapper.deleteExpressVehicleById(id);
    }
}
