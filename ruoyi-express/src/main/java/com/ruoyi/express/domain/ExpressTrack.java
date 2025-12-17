package com.ruoyi.express.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 物流轨迹对象 express_track
 * 
 * @author ruoyi
 * @date 2025-12-17
 */
public class ExpressTrack extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 轨迹ID */
    private Long trackId;

    /** 关联快递单号 */
    @Excel(name = "关联快递单号")
    private String waybillNo;

    /** 轨迹状态（揽收/运输中/派送中/已签收/已取消） */
    @Excel(name = "轨迹状态", readConverterExp = "揽=收/运输中/派送中/已签收/已取消")
    private String trackStatus;

    /** 轨迹描述（如：【北京市】快递已揽收） */
    @Excel(name = "轨迹描述", readConverterExp = "如=：【北京市】快递已揽收")
    private String trackDesc;

    /** 轨迹地点（省+市+区） */
    @Excel(name = "轨迹地点", readConverterExp = "省=+市+区")
    private String trackLocation;

    /** 轨迹时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "轨迹时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date trackTime;

    /** 排序（升序） */
    private Long sort;

    /** 删除标志（0存在 2删除） */
    private String delFlag;

    public void setTrackId(Long trackId) 
    {
        this.trackId = trackId;
    }

    public Long getTrackId() 
    {
        return trackId;
    }

    public void setWaybillNo(String waybillNo) 
    {
        this.waybillNo = waybillNo;
    }

    public String getWaybillNo() 
    {
        return waybillNo;
    }

    public void setTrackStatus(String trackStatus) 
    {
        this.trackStatus = trackStatus;
    }

    public String getTrackStatus() 
    {
        return trackStatus;
    }

    public void setTrackDesc(String trackDesc) 
    {
        this.trackDesc = trackDesc;
    }

    public String getTrackDesc() 
    {
        return trackDesc;
    }

    public void setTrackLocation(String trackLocation) 
    {
        this.trackLocation = trackLocation;
    }

    public String getTrackLocation() 
    {
        return trackLocation;
    }

    public void setTrackTime(Date trackTime) 
    {
        this.trackTime = trackTime;
    }

    public Date getTrackTime() 
    {
        return trackTime;
    }

    public void setSort(Long sort) 
    {
        this.sort = sort;
    }

    public Long getSort() 
    {
        return sort;
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
            .append("trackId", getTrackId())
            .append("waybillNo", getWaybillNo())
            .append("trackStatus", getTrackStatus())
            .append("trackDesc", getTrackDesc())
            .append("trackLocation", getTrackLocation())
            .append("trackTime", getTrackTime())
            .append("sort", getSort())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
