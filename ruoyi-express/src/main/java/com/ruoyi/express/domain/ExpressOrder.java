package com.ruoyi.express.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 订单管理对象 express_order
 *
 * @author apisflorea
 * @date 2025-12-17
 */
public class ExpressOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 订单ID */
    private Long orderId;

    /** 订单号（唯一） */
    @Excel(name = "订单号")
    private String orderNo;

    /** 支付方式（0=微信支付,1=余额支付） */
    @Excel(name = "支付方式",readConverterExp = "0=微信支付,1=余额支付")
    private String payType;

    /** 支付渠道（0=小程序,1=余额） */
    @Excel(name = "支付渠道",readConverterExp = "0=小程序,1=余额")
    private String payChannel;

    /** 支付状态（0待支付 1已支付 2已取消 3已完成） */
    @Excel(name = "支付状态", readConverterExp = "0=待支付,1=已支付,2=已取消,3=已完成")
    private String orderStatus;

    /** 退款状态（0未退款 1退款中 2已退款） */
    @Excel(name = "退款状态", readConverterExp = "0=未退款,1=退款中,2=已退款")
    private String refundStatus;

    /** 商品信息（JSON格式：[{"name":"商品1","num":1,"price":10.00}]） */
    @Excel(name = "商品信息")
    private String goodsInfo;

    /** 收货人 */
    @Excel(name = "收货人")
    private String consignee;

    /** 收货电话 */
    @Excel(name = "收货电话")
    private String consigneePhone;

    /** 收货地址（省+市+区+详细地址） */
    @Excel(name = "收货地址")
    private String consigneeAddress;

    /** 用户备注 */
    @Excel(name = "用户备注")
    private String userRemark;

    /** 商户备注 */
    @Excel(name = "商户备注")
    private String merchantRemark;

    /** 实际支付金额 */
    @Excel(name = "实际支付金额")
    private BigDecimal orderAmount;

    /** 支付时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "支付时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date payTime;

    /** 退款时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "退款时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date refundTime;

    /** 删除标志（0存在 2删除） */
    private String delFlag;

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

    public void setPayType(String payType)
    {
        this.payType = payType;
    }

    public String getPayType()
    {
        return payType;
    }

    public void setPayChannel(String payChannel)
    {
        this.payChannel = payChannel;
    }

    public String getPayChannel()
    {
        return payChannel;
    }

    public void setOrderStatus(String orderStatus)
    {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus()
    {
        return orderStatus;
    }

    public void setRefundStatus(String refundStatus)
    {
        this.refundStatus = refundStatus;
    }

    public String getRefundStatus()
    {
        return refundStatus;
    }

    public void setGoodsInfo(String goodsInfo)
    {
        this.goodsInfo = goodsInfo;
    }

    public String getGoodsInfo()
    {
        return goodsInfo;
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

    public void setUserRemark(String userRemark)
    {
        this.userRemark = userRemark;
    }

    public String getUserRemark()
    {
        return userRemark;
    }

    public void setMerchantRemark(String merchantRemark)
    {
        this.merchantRemark = merchantRemark;
    }

    public String getMerchantRemark()
    {
        return merchantRemark;
    }

    public void setOrderAmount(BigDecimal orderAmount)
    {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getOrderAmount()
    {
        return orderAmount;
    }

    public void setPayTime(Date payTime)
    {
        this.payTime = payTime;
    }

    public Date getPayTime()
    {
        return payTime;
    }

    public void setRefundTime(Date refundTime)
    {
        this.refundTime = refundTime;
    }

    public Date getRefundTime()
    {
        return refundTime;
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
            .append("orderId", getOrderId())
            .append("orderNo", getOrderNo())
            .append("payType", getPayType())
            .append("payChannel", getPayChannel())
            .append("orderStatus", getOrderStatus())
            .append("refundStatus", getRefundStatus())
            .append("goodsInfo", getGoodsInfo())
            .append("consignee", getConsignee())
            .append("consigneePhone", getConsigneePhone())
            .append("consigneeAddress", getConsigneeAddress())
            .append("userRemark", getUserRemark())
            .append("merchantRemark", getMerchantRemark())
            .append("orderAmount", getOrderAmount())
            .append("payTime", getPayTime())
            .append("refundTime", getRefundTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
