package com.ruoyi.express.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 第三方商品订单（用于导入后批量打印快递单）对象 express_third_order
 *
 * @author apisflorea
 * @date 2025-12-29
 */
public class ExpressThirdOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 订单ID（主键） */
    private Long orderId;

    /** 订单类型：1-实物订单，2-虚拟订单，3-服务订单 */
    @Excel(name = "订单类型",readConverterExp = "1=实物订单,2=虚拟订单,3=服务订单")
    private Integer orderType;

    /** 订单号（第三方唯一标识） */
    @Excel(name = "订单号")
    private String orderNo;

    /** 商户名称 */
    @Excel(name = "商户名称")
    private String merchantName;

    /** 用户昵称 */
    @Excel(name = "用户昵称")
    private String userNickname;

    /** 实际支付金额（元） */
    @Excel(name = "实际支付金额")
    private BigDecimal actualPayAmount;

    /** 支付状态：0-未支付，1-已支付，2-支付中，3-支付失败 */
    @Excel(name = "支付状态", readConverterExp = "0=未支付,1=已支付,2=支付中,3=支付失败")
    private Integer payStatus;

    /** 支付方式：0-未支付，1-微信支付，2-支付宝，3-银行卡，4-现金 */
    @Excel(name = "支付方式", readConverterExp = "0=未支付,1=微信支付,2=支付宝,3=银行卡,4=现金")
    private Integer payType;

    /** 支付渠道（如：微信公众号、支付宝APP、银联） */
    @Excel(name = "支付渠道", readConverterExp = "0=微信公众号,1=支付宝APP,2=银联")
    private String payChannel;

    /** 订单状态：0-待付款，1-待发货，2-待收货，3-已完成，4-已取消，5-已关闭 */
    @Excel(name = "订单状态",readConverterExp = "0=待付款,1=待发货,2=待收货,3=已完成,4=已取消,5=已关闭")
    private Integer orderStatus;

    /** 退款状态：0-未退款，1-退款中，2-已退款，3-退款失败，4-部分退款 */
    @Excel(name = "退款状态",readConverterExp = "0=未退款,1=退款中,2=已退款,3=退款失败,4=部分退款")
    private Integer refundStatus;

    /** 订单创建时间（第三方订单创建时间） */
    @Excel(name = "订单创建时间")
    private Date orderCreateTime;

    /** 商品信息（JSON格式存储，如：[{"商品名称":"华为手机","数量":1,"单价":6999}]） */
    @Excel(name = "商品信息")
    private String productInfo;

    /** 收货人 */
    @Excel(name = "收货人")
    private String receiverName;

    /** 收货电话 */
    @Excel(name = "收货电话")
    private String receiverPhone;

    /** 收货地址（省+市+区+详细地址） */
    @Excel(name = "收货地址")
    private String receiverAddress;

    /** 用户备注（下单时填写） */
    @Excel(name = "用户备注")
    private String userRemark;

    /** 商户备注（平台录入） */
    @Excel(name = "商户备注")
    private String merchantRemark;

    /** 逻辑删除标志：0-正常，1-删除（RuoYi框架默认） */
    private Integer delFlag;

    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
    }

    public Long getOrderId()
    {
        return orderId;
    }

    public void setOrderType(Integer orderType)
    {
        this.orderType = orderType;
    }

    public Integer getOrderType()
    {
        return orderType;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setMerchantName(String merchantName)
    {
        this.merchantName = merchantName;
    }

    public String getMerchantName()
    {
        return merchantName;
    }

    public void setUserNickname(String userNickname)
    {
        this.userNickname = userNickname;
    }

    public String getUserNickname()
    {
        return userNickname;
    }

    public void setActualPayAmount(BigDecimal actualPayAmount)
    {
        this.actualPayAmount = actualPayAmount;
    }

    public BigDecimal getActualPayAmount()
    {
        return actualPayAmount;
    }

    public void setPayStatus(Integer payStatus)
    {
        this.payStatus = payStatus;
    }

    public Integer getPayStatus()
    {
        return payStatus;
    }

    public void setPayType(Integer payType)
    {
        this.payType = payType;
    }

    public Integer getPayType()
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

    public void setOrderStatus(Integer orderStatus)
    {
        this.orderStatus = orderStatus;
    }

    public Integer getOrderStatus()
    {
        return orderStatus;
    }

    public void setRefundStatus(Integer refundStatus)
    {
        this.refundStatus = refundStatus;
    }

    public Integer getRefundStatus()
    {
        return refundStatus;
    }

    public void setOrderCreateTime(Date orderCreateTime)
    {
        this.orderCreateTime = orderCreateTime;
    }

    public Date getOrderCreateTime()
    {
        return orderCreateTime;
    }

    public void setProductInfo(String productInfo)
    {
        this.productInfo = productInfo;
    }

    public String getProductInfo()
    {
        return productInfo;
    }

    public void setReceiverName(String receiverName)
    {
        this.receiverName = receiverName;
    }

    public String getReceiverName()
    {
        return receiverName;
    }

    public void setReceiverPhone(String receiverPhone)
    {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverPhone()
    {
        return receiverPhone;
    }

    public void setReceiverAddress(String receiverAddress)
    {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverAddress()
    {
        return receiverAddress;
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

    public void setDelFlag(Integer delFlag)
    {
        this.delFlag = delFlag;
    }

    public Integer getDelFlag()
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("orderId", getOrderId())
            .append("orderType", getOrderType())
            .append("orderNo", getOrderNo())
            .append("merchantName", getMerchantName())
            .append("userNickname", getUserNickname())
            .append("actualPayAmount", getActualPayAmount())
            .append("payStatus", getPayStatus())
            .append("payType", getPayType())
            .append("payChannel", getPayChannel())
            .append("orderStatus", getOrderStatus())
            .append("refundStatus", getRefundStatus())
            .append("orderCreateTime", getOrderCreateTime())
            .append("productInfo", getProductInfo())
            .append("receiverName", getReceiverName())
            .append("receiverPhone", getReceiverPhone())
            .append("receiverAddress", getReceiverAddress())
            .append("userRemark", getUserRemark())
            .append("merchantRemark", getMerchantRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
