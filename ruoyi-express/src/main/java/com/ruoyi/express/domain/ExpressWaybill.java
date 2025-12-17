package com.ruoyi.express.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 快递单对象 express_waybill
 * 
 * @author ruoyi
 * @date 2025-12-17
 */
public class ExpressWaybill extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 快递单ID */
    private Long waybillId;

    /** 快递单号（KD+年月日+6位随机数） */
    @Excel(name = "快递单号", readConverterExp = "K=D+年月日+6位随机数")
    private String waybillNo;

    /** 关联订单ID */
    @Excel(name = "关联订单ID")
    private Long orderId;

    /** 关联订单编号 */
    @Excel(name = "关联订单编号")
    private String orderNo;

    /** 关联模板ID */
    @Excel(name = "关联模板ID")
    private Long templateId;

    /** 快递类型（standard/express/economy/fresh） */
    @Excel(name = "快递类型", readConverterExp = "s=tandard/express/economy/fresh")
    private String expressType;

    /** 寄件日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "寄件日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date sendDate;

    /** 物品类型 */
    @Excel(name = "物品类型")
    private String goodsType;

    /** 收货人姓名 */
    @Excel(name = "收货人姓名")
    private String consignee;

    /** 收货电话 */
    @Excel(name = "收货电话")
    private String consigneePhone;

    /** 收货地址 */
    @Excel(name = "收货地址")
    private String consigneeAddress;

    /** 重量(kg) */
    @Excel(name = "重量(kg)")
    private BigDecimal weight;

    /** 物品数量 */
    @Excel(name = "物品数量")
    private Long quantity;

    /** 备注信息 */
    @Excel(name = "备注信息")
    private String remarks;

    /** 快递状态（0草稿 1已打印 2运输中 3已签收 4已取消） */
    @Excel(name = "快递状态", readConverterExp = "0=草稿,1=已打印,2=运输中,3=已签收,4=已取消")
    private String status;

    /** 打印人 */
    @Excel(name = "打印人")
    private String printBy;

    /** 打印时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "打印时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date printTime;

    /** 打印次数 */
    @Excel(name = "打印次数")
    private Long printCount;

    /** 删除标志（0存在 2删除） */
    private String delFlag;

    public void setWaybillId(Long waybillId) 
    {
        this.waybillId = waybillId;
    }

    public Long getWaybillId() 
    {
        return waybillId;
    }

    public void setWaybillNo(String waybillNo) 
    {
        this.waybillNo = waybillNo;
    }

    public String getWaybillNo() 
    {
        return waybillNo;
    }

    public void setOrderId(Long orderId) 
    {
        this.orderId = orderId;
    }

    public Long getOrderId() 
    {
        return orderId;
    }

    public void setOrderNo(String orderNo) 
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo() 
    {
        return orderNo;
    }

    public void setTemplateId(Long templateId) 
    {
        this.templateId = templateId;
    }

    public Long getTemplateId() 
    {
        return templateId;
    }

    public void setExpressType(String expressType) 
    {
        this.expressType = expressType;
    }

    public String getExpressType() 
    {
        return expressType;
    }

    public void setSendDate(Date sendDate) 
    {
        this.sendDate = sendDate;
    }

    public Date getSendDate() 
    {
        return sendDate;
    }

    public void setGoodsType(String goodsType) 
    {
        this.goodsType = goodsType;
    }

    public String getGoodsType() 
    {
        return goodsType;
    }

    public void setConsignee(String consignee) 
    {
        this.consignee = consignee;
    }

    public String getConsignee() 
    {
        return consignee;
    }

    public void setConsigneePhone(String consigneePhone) 
    {
        this.consigneePhone = consigneePhone;
    }

    public String getConsigneePhone() 
    {
        return consigneePhone;
    }

    public void setConsigneeAddress(String consigneeAddress) 
    {
        this.consigneeAddress = consigneeAddress;
    }

    public String getConsigneeAddress() 
    {
        return consigneeAddress;
    }

    public void setWeight(BigDecimal weight) 
    {
        this.weight = weight;
    }

    public BigDecimal getWeight() 
    {
        return weight;
    }

    public void setQuantity(Long quantity) 
    {
        this.quantity = quantity;
    }

    public Long getQuantity() 
    {
        return quantity;
    }

    public void setRemarks(String remarks) 
    {
        this.remarks = remarks;
    }

    public String getRemarks() 
    {
        return remarks;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setPrintBy(String printBy) 
    {
        this.printBy = printBy;
    }

    public String getPrintBy() 
    {
        return printBy;
    }

    public void setPrintTime(Date printTime) 
    {
        this.printTime = printTime;
    }

    public Date getPrintTime() 
    {
        return printTime;
    }

    public void setPrintCount(Long printCount) 
    {
        this.printCount = printCount;
    }

    public Long getPrintCount() 
    {
        return printCount;
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
            .append("waybillId", getWaybillId())
            .append("waybillNo", getWaybillNo())
            .append("orderId", getOrderId())
            .append("orderNo", getOrderNo())
            .append("templateId", getTemplateId())
            .append("expressType", getExpressType())
            .append("sendDate", getSendDate())
            .append("goodsType", getGoodsType())
            .append("consignee", getConsignee())
            .append("consigneePhone", getConsigneePhone())
            .append("consigneeAddress", getConsigneeAddress())
            .append("weight", getWeight())
            .append("quantity", getQuantity())
            .append("remarks", getRemarks())
            .append("status", getStatus())
            .append("printBy", getPrintBy())
            .append("printTime", getPrintTime())
            .append("printCount", getPrintCount())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
