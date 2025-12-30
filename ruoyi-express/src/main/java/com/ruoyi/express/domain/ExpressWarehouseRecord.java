package com.ruoyi.express.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 仓库出入库记录对象 express_warehouse_record
 * 
 * @author apisflorea
 * @date 2025-12-29
 */
public class ExpressWarehouseRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 关联快递单号 */
    @Excel(name = "关联快递单号")
    private String orderNo;

    /** 仓库ID */
    @Excel(name = "仓库ID")
    private Long warehouseId;

    /** 记录类型（1-入库 2-出库 3-盘点） */
    @Excel(name = "记录类型", readConverterExp = "1=-入库,2=-出库,3=-盘点")
    private String recordType;

    /** 操作人ID */
    @Excel(name = "操作人ID")
    private Long operateUserId;

    /** 操作时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date operateTime;

    /** 操作前库存数量 */
    @Excel(name = "操作前库存数量")
    private Long stockBefore;

    /** 操作后库存数量 */
    @Excel(name = "操作后库存数量")
    private Long stockAfter;

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

    public void setWarehouseId(Long warehouseId) 
    {
        this.warehouseId = warehouseId;
    }

    public Long getWarehouseId() 
    {
        return warehouseId;
    }

    public void setRecordType(String recordType) 
    {
        this.recordType = recordType;
    }

    public String getRecordType() 
    {
        return recordType;
    }

    public void setOperateUserId(Long operateUserId) 
    {
        this.operateUserId = operateUserId;
    }

    public Long getOperateUserId() 
    {
        return operateUserId;
    }

    public void setOperateTime(Date operateTime) 
    {
        this.operateTime = operateTime;
    }

    public Date getOperateTime() 
    {
        return operateTime;
    }

    public void setStockBefore(Long stockBefore) 
    {
        this.stockBefore = stockBefore;
    }

    public Long getStockBefore() 
    {
        return stockBefore;
    }

    public void setStockAfter(Long stockAfter) 
    {
        this.stockAfter = stockAfter;
    }

    public Long getStockAfter() 
    {
        return stockAfter;
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
            .append("warehouseId", getWarehouseId())
            .append("recordType", getRecordType())
            .append("operateUserId", getOperateUserId())
            .append("operateTime", getOperateTime())
            .append("stockBefore", getStockBefore())
            .append("stockAfter", getStockAfter())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
