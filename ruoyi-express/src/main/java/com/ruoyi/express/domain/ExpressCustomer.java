package com.ruoyi.express.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 客户信息对象 express_customer
 * 
 * @author apisflorea
 * @date 2025-12-29
 */
public class ExpressCustomer extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 客户姓名 */
    @Excel(name = "客户姓名")
    private String customerName;

    /** 客户真实手机号 */
    @Excel(name = "客户真实手机号")
    private String customerPhone;

    /** 客户类型（1-个人 2-企业） */
    @Excel(name = "客户类型", readConverterExp = "1=-个人,2=-企业")
    private String customerType;

    /** 企业名称（企业客户必填） */
    @Excel(name = "企业名称", readConverterExp = "企=业客户必填")
    private String companyName;

    /** 默认地址 */
    @Excel(name = "默认地址")
    private String defaultAddress;

    /** 累计下单次数 */
    @Excel(name = "累计下单次数")
    private Long totalOrder;

    /** 累计消费金额 */
    @Excel(name = "累计消费金额")
    private BigDecimal totalAmount;

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

    public void setCustomerName(String customerName) 
    {
        this.customerName = customerName;
    }

    public String getCustomerName() 
    {
        return customerName;
    }

    public void setCustomerPhone(String customerPhone) 
    {
        this.customerPhone = customerPhone;
    }

    public String getCustomerPhone() 
    {
        return customerPhone;
    }

    public void setCustomerType(String customerType) 
    {
        this.customerType = customerType;
    }

    public String getCustomerType() 
    {
        return customerType;
    }

    public void setCompanyName(String companyName) 
    {
        this.companyName = companyName;
    }

    public String getCompanyName() 
    {
        return companyName;
    }

    public void setDefaultAddress(String defaultAddress) 
    {
        this.defaultAddress = defaultAddress;
    }

    public String getDefaultAddress() 
    {
        return defaultAddress;
    }

    public void setTotalOrder(Long totalOrder) 
    {
        this.totalOrder = totalOrder;
    }

    public Long getTotalOrder() 
    {
        return totalOrder;
    }

    public void setTotalAmount(BigDecimal totalAmount) 
    {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmount() 
    {
        return totalAmount;
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
            .append("customerName", getCustomerName())
            .append("customerPhone", getCustomerPhone())
            .append("customerType", getCustomerType())
            .append("companyName", getCompanyName())
            .append("defaultAddress", getDefaultAddress())
            .append("totalOrder", getTotalOrder())
            .append("totalAmount", getTotalAmount())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
