package com.ruoyi.express.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 虚拟号管理对象 express_virtual_number
 * 
 * @author apisflorea
 * @date 2025-12-29
 */
public class ExpressVirtualNumber extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 虚拟手机号 */
    @Excel(name = "虚拟手机号")
    private String virtualPhone;

    /** 绑定的真实手机号 */
    @Excel(name = "绑定的真实手机号")
    private String realPhone;

    /** 关联快递单号，可空（号池未分配） */
    @Excel(name = "关联快递单号，可空", readConverterExp = "号=池未分配")
    private String orderNo;

    /** 虚拟号服务商（如云片/容联） */
    @Excel(name = "虚拟号服务商", readConverterExp = "如=云片/容联")
    private String provider;

    /** 绑定时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "绑定时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date bindTime;

    /** 有效期截止时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "有效期截止时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expireTime;

    /** 状态（0-未使用 1-已绑定 2-已过期 3-已释放） */
    @Excel(name = "状态", readConverterExp = "0=-未使用,1=-已绑定,2=-已过期,3=-已释放")
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

    public void setVirtualPhone(String virtualPhone) 
    {
        this.virtualPhone = virtualPhone;
    }

    public String getVirtualPhone() 
    {
        return virtualPhone;
    }

    public void setRealPhone(String realPhone) 
    {
        this.realPhone = realPhone;
    }

    public String getRealPhone() 
    {
        return realPhone;
    }

    public void setOrderNo(String orderNo) 
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo() 
    {
        return orderNo;
    }

    public void setProvider(String provider) 
    {
        this.provider = provider;
    }

    public String getProvider() 
    {
        return provider;
    }

    public void setBindTime(Date bindTime) 
    {
        this.bindTime = bindTime;
    }

    public Date getBindTime() 
    {
        return bindTime;
    }

    public void setExpireTime(Date expireTime) 
    {
        this.expireTime = expireTime;
    }

    public Date getExpireTime() 
    {
        return expireTime;
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
            .append("virtualPhone", getVirtualPhone())
            .append("realPhone", getRealPhone())
            .append("orderNo", getOrderNo())
            .append("provider", getProvider())
            .append("bindTime", getBindTime())
            .append("expireTime", getExpireTime())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
