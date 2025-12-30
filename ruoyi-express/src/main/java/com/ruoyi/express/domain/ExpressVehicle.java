package com.ruoyi.express.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 车辆信息对象 express_vehicle
 * 
 * @author apisflorea
 * @date 2025-12-29
 */
public class ExpressVehicle extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 车牌号 */
    @Excel(name = "车牌号")
    private String vehicleNo;

    /** 车型（如厢式货车/面包车） */
    @Excel(name = "车型", readConverterExp = "如=厢式货车/面包车")
    private String vehicleType;

    /** 载重（吨） */
    @Excel(name = "载重", readConverterExp = "吨=")
    private BigDecimal loadWeight;

    /** 容积（m³） */
    @Excel(name = "容积", readConverterExp = "m=³")
    private BigDecimal loadVolume;

    /** GPS设备编号 */
    @Excel(name = "GPS设备编号")
    private String gpsDeviceNo;

    /** 绑定司机ID，关联sys_user */
    @Excel(name = "绑定司机ID，关联sys_user")
    private Long driverId;

    /** 状态（0-停运 1-在途 2-闲置） */
    @Excel(name = "状态", readConverterExp = "0=-停运,1=-在途,2=-闲置")
    private String status;

    /** 逻辑删除（0-正常 1-删除） */
    private String delFlag;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setVehicleNo(String vehicleNo) 
    {
        this.vehicleNo = vehicleNo;
    }

    public String getVehicleNo() 
    {
        return vehicleNo;
    }

    public void setVehicleType(String vehicleType) 
    {
        this.vehicleType = vehicleType;
    }

    public String getVehicleType() 
    {
        return vehicleType;
    }

    public void setLoadWeight(BigDecimal loadWeight) 
    {
        this.loadWeight = loadWeight;
    }

    public BigDecimal getLoadWeight() 
    {
        return loadWeight;
    }

    public void setLoadVolume(BigDecimal loadVolume) 
    {
        this.loadVolume = loadVolume;
    }

    public BigDecimal getLoadVolume() 
    {
        return loadVolume;
    }

    public void setGpsDeviceNo(String gpsDeviceNo) 
    {
        this.gpsDeviceNo = gpsDeviceNo;
    }

    public String getGpsDeviceNo() 
    {
        return gpsDeviceNo;
    }

    public void setDriverId(Long driverId) 
    {
        this.driverId = driverId;
    }

    public Long getDriverId() 
    {
        return driverId;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("vehicleNo", getVehicleNo())
            .append("vehicleType", getVehicleType())
            .append("loadWeight", getLoadWeight())
            .append("loadVolume", getLoadVolume())
            .append("gpsDeviceNo", getGpsDeviceNo())
            .append("driverId", getDriverId())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
