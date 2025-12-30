package com.ruoyi.express.service;

import java.util.List;
import com.ruoyi.express.domain.ExpressVehicle;

/**
 * 车辆信息Service接口
 * 
 * @author apisflorea
 * @date 2025-12-29
 */
public interface IExpressVehicleService 
{
    /**
     * 查询车辆信息
     * 
     * @param id 车辆信息主键
     * @return 车辆信息
     */
    public ExpressVehicle selectExpressVehicleById(Long id);

    /**
     * 查询车辆信息列表
     * 
     * @param expressVehicle 车辆信息
     * @return 车辆信息集合
     */
    public List<ExpressVehicle> selectExpressVehicleList(ExpressVehicle expressVehicle);

    /**
     * 新增车辆信息
     * 
     * @param expressVehicle 车辆信息
     * @return 结果
     */
    public int insertExpressVehicle(ExpressVehicle expressVehicle);

    /**
     * 修改车辆信息
     * 
     * @param expressVehicle 车辆信息
     * @return 结果
     */
    public int updateExpressVehicle(ExpressVehicle expressVehicle);

    /**
     * 批量删除车辆信息
     * 
     * @param ids 需要删除的车辆信息主键集合
     * @return 结果
     */
    public int deleteExpressVehicleByIds(Long[] ids);

    /**
     * 删除车辆信息信息
     * 
     * @param id 车辆信息主键
     * @return 结果
     */
    public int deleteExpressVehicleById(Long id);
}
