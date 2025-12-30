package com.ruoyi.express.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 快递订单对象 express_order
 * 
 * @author apisflorea
 * @date 2025-12-29
 */
public class ExpressOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 快递单号（唯一） */
    @Excel(name = "快递单号", readConverterExp = "唯=一")
    private String orderNo;

    /** 寄件人客户ID，关联express_customer */
    @Excel(name = "寄件人客户ID，关联express_customer")
    private Long customerId;

    /** 寄件人姓名 */
    @Excel(name = "寄件人姓名")
    private String senderName;

    /** 寄件人真实手机号 */
    @Excel(name = "寄件人真实手机号")
    private String senderPhone;

    /** 寄件人虚拟号，关联express_virtual_number */
    @Excel(name = "寄件人虚拟号，关联express_virtual_number")
    private String senderVirtualPhone;

    /** 寄件人详细地址 */
    @Excel(name = "寄件人详细地址")
    private String senderAddress;

    /** 收件人姓名 */
    @Excel(name = "收件人姓名")
    private String receiverName;

    /** 收件人真实手机号 */
    @Excel(name = "收件人真实手机号")
    private String receiverPhone;

    /** 收件人虚拟号，关联express_virtual_number */
    @Excel(name = "收件人虚拟号，关联express_virtual_number")
    private String receiverVirtualPhone;

    /** 收件人详细地址 */
    @Excel(name = "收件人详细地址")
    private String receiverAddress;

    /** 快递类型（普通件/生鲜件/大件） */
    @Excel(name = "快递类型", readConverterExp = "普=通件/生鲜件/大件")
    private String expressType;

    /** 快递重量（kg） */
    @Excel(name = "快递重量", readConverterExp = "k=g")
    private BigDecimal weight;

    /** 运费金额 */
    @Excel(name = "运费金额")
    private BigDecimal freight;

    /** 支付状态（0-未支付 1-已支付 2-已退款） */
    @Excel(name = "支付状态", readConverterExp = "0=-未支付,1=-已支付,2=-已退款")
    private String payStatus;

    /** 支付时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "支付时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date payTime;

    /** 订单状态（01-待揽收 02-运输中 03-派送中 04-已签收 05-已退回 06-异常） */
    @Excel(name = "订单状态", readConverterExp = "0=1-待揽收,0=2-运输中,0=3-派送中,0=4-已签收,0=5-已退回,0=6-异常")
    private String orderStatus;

    /** 始发仓库ID，关联express_warehouse */
    @Excel(name = "始发仓库ID，关联express_warehouse")
    private Long warehouseId;

    /** 运输车辆ID，关联express_vehicle */
    @Excel(name = "运输车辆ID，关联express_vehicle")
    private Long vehicleId;

    /** 派送司机ID，关联sys_user（司机角色） */
    @Excel(name = "派送司机ID，关联sys_user", readConverterExp = "司=机角色")
    private Long driverId;

    /** 面单模板ID，关联express_waybill_template */
    @Excel(name = "面单模板ID，关联express_waybill_template")
    private Long waybillTemplateId;

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

    public void setCustomerId(Long customerId) 
    {
        this.customerId = customerId;
    }

    public Long getCustomerId() 
    {
        return customerId;
    }

    public void setSenderName(String senderName) 
    {
        this.senderName = senderName;
    }

    public String getSenderName() 
    {
        return senderName;
    }

    public void setSenderPhone(String senderPhone) 
    {
        this.senderPhone = senderPhone;
    }

    public String getSenderPhone() 
    {
        return senderPhone;
    }

    public void setSenderVirtualPhone(String senderVirtualPhone) 
    {
        this.senderVirtualPhone = senderVirtualPhone;
    }

    public String getSenderVirtualPhone() 
    {
        return senderVirtualPhone;
    }

    public void setSenderAddress(String senderAddress) 
    {
        this.senderAddress = senderAddress;
    }

    public String getSenderAddress() 
    {
        return senderAddress;
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

    public void setReceiverVirtualPhone(String receiverVirtualPhone) 
    {
        this.receiverVirtualPhone = receiverVirtualPhone;
    }

    public String getReceiverVirtualPhone() 
    {
        return receiverVirtualPhone;
    }

    public void setReceiverAddress(String receiverAddress) 
    {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverAddress() 
    {
        return receiverAddress;
    }

    public void setExpressType(String expressType) 
    {
        this.expressType = expressType;
    }

    public String getExpressType() 
    {
        return expressType;
    }

    public void setWeight(BigDecimal weight) 
    {
        this.weight = weight;
    }

    public BigDecimal getWeight() 
    {
        return weight;
    }

    public void setFreight(BigDecimal freight) 
    {
        this.freight = freight;
    }

    public BigDecimal getFreight() 
    {
        return freight;
    }

    public void setPayStatus(String payStatus) 
    {
        this.payStatus = payStatus;
    }

    public String getPayStatus() 
    {
        return payStatus;
    }

    public void setPayTime(Date payTime) 
    {
        this.payTime = payTime;
    }

    public Date getPayTime() 
    {
        return payTime;
    }

    public void setOrderStatus(String orderStatus) 
    {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() 
    {
        return orderStatus;
    }

    public void setWarehouseId(Long warehouseId) 
    {
        this.warehouseId = warehouseId;
    }

    public Long getWarehouseId() 
    {
        return warehouseId;
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

    public void setWaybillTemplateId(Long waybillTemplateId) 
    {
        this.waybillTemplateId = waybillTemplateId;
    }

    public Long getWaybillTemplateId() 
    {
        return waybillTemplateId;
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
            .append("customerId", getCustomerId())
            .append("senderName", getSenderName())
            .append("senderPhone", getSenderPhone())
            .append("senderVirtualPhone", getSenderVirtualPhone())
            .append("senderAddress", getSenderAddress())
            .append("receiverName", getReceiverName())
            .append("receiverPhone", getReceiverPhone())
            .append("receiverVirtualPhone", getReceiverVirtualPhone())
            .append("receiverAddress", getReceiverAddress())
            .append("expressType", getExpressType())
            .append("weight", getWeight())
            .append("freight", getFreight())
            .append("payStatus", getPayStatus())
            .append("payTime", getPayTime())
            .append("orderStatus", getOrderStatus())
            .append("warehouseId", getWarehouseId())
            .append("vehicleId", getVehicleId())
            .append("driverId", getDriverId())
            .append("waybillTemplateId", getWaybillTemplateId())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
