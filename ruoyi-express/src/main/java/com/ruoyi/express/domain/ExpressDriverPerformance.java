package com.ruoyi.express.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 司机业绩统计对象 express_driver_performance
 * 
 * @author apisflorea
 * @date 2025-12-29
 */
public class ExpressDriverPerformance extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 司机ID */
    @Excel(name = "司机ID")
    private Long driverId;

    /** 统计日期（按天统计） */
    @Excel(name = "统计日期", readConverterExp = "按=天统计")
    private Date statisticDate;

    /** 揽收订单数 */
    @Excel(name = "揽收订单数")
    private Long receiveCount;

    /** 派送订单数 */
    @Excel(name = "派送订单数")
    private Long deliverCount;

    /** 异常订单数 */
    @Excel(name = "异常订单数")
    private Long exceptionCount;

    /** 业绩提成金额 */
    @Excel(name = "业绩提成金额")
    private BigDecimal performanceAmount;

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

    public void setDriverId(Long driverId) 
    {
        this.driverId = driverId;
    }

    public Long getDriverId() 
    {
        return driverId;
    }

    public void setStatisticDate(Date statisticDate) 
    {
        this.statisticDate = statisticDate;
    }

    public Date getStatisticDate() 
    {
        return statisticDate;
    }

    public void setReceiveCount(Long receiveCount) 
    {
        this.receiveCount = receiveCount;
    }

    public Long getReceiveCount() 
    {
        return receiveCount;
    }

    public void setDeliverCount(Long deliverCount) 
    {
        this.deliverCount = deliverCount;
    }

    public Long getDeliverCount() 
    {
        return deliverCount;
    }

    public void setExceptionCount(Long exceptionCount) 
    {
        this.exceptionCount = exceptionCount;
    }

    public Long getExceptionCount() 
    {
        return exceptionCount;
    }

    public void setPerformanceAmount(BigDecimal performanceAmount) 
    {
        this.performanceAmount = performanceAmount;
    }

    public BigDecimal getPerformanceAmount() 
    {
        return performanceAmount;
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
            .append("driverId", getDriverId())
            .append("statisticDate", getStatisticDate())
            .append("receiveCount", getReceiveCount())
            .append("deliverCount", getDeliverCount())
            .append("exceptionCount", getExceptionCount())
            .append("performanceAmount", getPerformanceAmount())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
