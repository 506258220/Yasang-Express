package com.ruoyi.express.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 快递模板配置对象 express_template
 *
 * @author ruoyi
 * @date 2025-12-17
 */
public class ExpressTemplate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 模板ID */
    private Long templateId;

    /** 模板名称（如：标准快递、生鲜快递） */
    @Excel(name = "模板名称", readConverterExp = "如=：标准快递、生鲜快递")
    private String templateName;

    /** 模板编码（唯一标识） */
    @Excel(name = "模板编码", readConverterExp = "唯=一标识")
    private String templateCode;

    /** 面单尺寸（mm） */
    @Excel(name = "面单尺寸", readConverterExp = "m=m")
    private String templateSize;

    /** 模板样式（standard/简约simple） */
    @Excel(name = "模板样式", readConverterExp = "s=tandard/简约simple")
    private String templateStyle;

    /** 字段配置（JSON格式：{"waybillNo":true,"consignee":true,"phone":true,"address":true,"goodsInfo":true}） */
    @Excel(name = "字段配置", readConverterExp = "J=SON格式：{\"waybillNo\":true,\"consignee\":true,\"phone\":true,\"address\":true,\"goodsInfo\":true}")
    private String fieldsConfig;

    /** 打印配置（JSON：{"fontSize":12,"marginTop":5,"marginLeft":5}） */
    @Excel(name = "打印配置", readConverterExp = "J=SON：{\"fontSize\":12,\"marginTop\":5,\"marginLeft\":5}")
    private String printConfig;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0存在 2删除） */
    private String delFlag;

    public void setTemplateId(Long templateId)
    {
        this.templateId = templateId;
    }

    public Long getTemplateId()
    {
        return templateId;
    }

    public void setTemplateName(String templateName)
    {
        this.templateName = templateName;
    }

    public String getTemplateName()
    {
        return templateName;
    }

    public void setTemplateCode(String templateCode)
    {
        this.templateCode = templateCode;
    }

    public String getTemplateCode()
    {
        return templateCode;
    }

    public void setTemplateSize(String templateSize)
    {
        this.templateSize = templateSize;
    }

    public String getTemplateSize()
    {
        return templateSize;
    }

    public void setTemplateStyle(String templateStyle)
    {
        this.templateStyle = templateStyle;
    }

    public String getTemplateStyle()
    {
        return templateStyle;
    }

    public void setFieldsConfig(String fieldsConfig)
    {
        this.fieldsConfig = fieldsConfig;
    }

    public String getFieldsConfig()
    {
        return fieldsConfig;
    }

    public void setPrintConfig(String printConfig)
    {
        this.printConfig = printConfig;
    }

    public String getPrintConfig()
    {
        return printConfig;
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
            .append("templateId", getTemplateId())
            .append("templateName", getTemplateName())
            .append("templateCode", getTemplateCode())
            .append("templateSize", getTemplateSize())
            .append("templateStyle", getTemplateStyle())
            .append("fieldsConfig", getFieldsConfig())
            .append("printConfig", getPrintConfig())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
