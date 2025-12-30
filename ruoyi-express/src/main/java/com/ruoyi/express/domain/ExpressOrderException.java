package com.ruoyi.express.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 订单异常记录对象 express_order_exception
 * 
 * @author apisflorea
 * @date 2025-12-29
 */
public class ExpressOrderException extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 关联快递单号 */
    @Excel(name = "关联快递单号")
    private String orderNo;

    /** 上报司机ID */
    @Excel(name = "上报司机ID")
    private Long driverId;

    /** 异常类型（丢件/破损/拒收/超区） */
    @Excel(name = "异常类型", readConverterExp = "丢=件/破损/拒收/超区")
    private String exceptionType;

    /** 异常描述 */
    @Excel(name = "异常描述")
    private String exceptionDesc;

    /** 异常凭证图片（多个逗号分隔） */
    @Excel(name = "异常凭证图片", readConverterExp = "多=个逗号分隔")
    private String exceptionImg;

    /** 上报时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "上报时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date reportTime;

    /** 处理人ID（管理员） */
    @Excel(name = "处理人ID", readConverterExp = "管=理员")
    private Long handleUserId;

    /** 处理结果 */
    @Excel(name = "处理结果")
    private String handleResult;

    /** 处理时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "处理时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date handleTime;

    /** 处理状态（0-未处理 1-处理中 2-已处理） */
    @Excel(name = "处理状态", readConverterExp = "0=-未处理,1=-处理中,2=-已处理")
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

    public void setOrderNo(String orderNo) 
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo() 
    {
        return orderNo;
    }

    public void setDriverId(Long driverId) 
    {
        this.driverId = driverId;
    }

    public Long getDriverId() 
    {
        return driverId;
    }

    public void setExceptionType(String exceptionType) 
    {
        this.exceptionType = exceptionType;
    }

    public String getExceptionType() 
    {
        return exceptionType;
    }

    public void setExceptionDesc(String exceptionDesc) 
    {
        this.exceptionDesc = exceptionDesc;
    }

    public String getExceptionDesc() 
    {
        return exceptionDesc;
    }

    public void setExceptionImg(String exceptionImg) 
    {
        this.exceptionImg = exceptionImg;
    }

    public String getExceptionImg() 
    {
        return exceptionImg;
    }

    public void setReportTime(Date reportTime) 
    {
        this.reportTime = reportTime;
    }

    public Date getReportTime() 
    {
        return reportTime;
    }

    public void setHandleUserId(Long handleUserId) 
    {
        this.handleUserId = handleUserId;
    }

    public Long getHandleUserId() 
    {
        return handleUserId;
    }

    public void setHandleResult(String handleResult) 
    {
        this.handleResult = handleResult;
    }

    public String getHandleResult() 
    {
        return handleResult;
    }

    public void setHandleTime(Date handleTime) 
    {
        this.handleTime = handleTime;
    }

    public Date getHandleTime() 
    {
        return handleTime;
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
            .append("orderNo", getOrderNo())
            .append("driverId", getDriverId())
            .append("exceptionType", getExceptionType())
            .append("exceptionDesc", getExceptionDesc())
            .append("exceptionImg", getExceptionImg())
            .append("reportTime", getReportTime())
            .append("handleUserId", getHandleUserId())
            .append("handleResult", getHandleResult())
            .append("handleTime", getHandleTime())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
