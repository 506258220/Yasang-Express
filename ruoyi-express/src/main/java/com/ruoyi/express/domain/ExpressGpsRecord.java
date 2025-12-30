package com.ruoyi.express.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * GPS定位记录对象 express_gps_record
 * 
 * @author apisflorea
 * @date 2025-12-29
 */
public class ExpressGpsRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 关联车辆ID */
    @Excel(name = "关联车辆ID")
    private Long vehicleId;

    /** 关联司机ID */
    @Excel(name = "关联司机ID")
    private Long driverId;

    /** 关联快递单号（运输中订单） */
    @Excel(name = "关联快递单号", readConverterExp = "运=输中订单")
    private String orderNo;

    /** 经度（GCJ02坐标系） */
    @Excel(name = "经度", readConverterExp = "G=CJ02坐标系")
    private BigDecimal longitude;

    /** 纬度（GCJ02坐标系） */
    @Excel(name = "纬度", readConverterExp = "G=CJ02坐标系")
    private BigDecimal latitude;

    /** 定位时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "定位时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date locationTime;

    /** 行驶速度（km/h） */
    @Excel(name = "行驶速度", readConverterExp = "k=m/h")
    private BigDecimal speed;

    /** 定位地址解析 */
    @Excel(name = "定位地址解析")
    private String address;

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

    public void setVehicleId(Long vehicleId) 
    {
        this.vehicleId = vehicleId;
    }

    public Long getVehicleId() 
    {
        return vehicleId;
    }

    public void setDriverId(Long driverId) 
    {
        this.driverId = driverId;
    }

    public Long getDriverId() 
    {
        return driverId;
    }

    public void setOrderNo(String orderNo) 
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo() 
    {
        return orderNo;
    }

    public void setLongitude(BigDecimal longitude) 
    {
        this.longitude = longitude;
    }

    public BigDecimal getLongitude() 
    {
        return longitude;
    }

    public void setLatitude(BigDecimal latitude) 
    {
        this.latitude = latitude;
    }

    public BigDecimal getLatitude() 
    {
        return latitude;
    }

    public void setLocationTime(Date locationTime) 
    {
        this.locationTime = locationTime;
    }

    public Date getLocationTime() 
    {
        return locationTime;
    }

    public void setSpeed(BigDecimal speed) 
    {
        this.speed = speed;
    }

    public BigDecimal getSpeed() 
    {
        return speed;
    }

    public void setAddress(String address) 
    {
        this.address = address;
    }

    public String getAddress() 
    {
        return address;
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
            .append("vehicleId", getVehicleId())
            .append("driverId", getDriverId())
            .append("orderNo", getOrderNo())
            .append("longitude", getLongitude())
            .append("latitude", getLatitude())
            .append("locationTime", getLocationTime())
            .append("speed", getSpeed())
            .append("address", getAddress())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
