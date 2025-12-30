package com.ruoyi.express.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 面单模板对象 express_waybill_template
 *
 * @author 全栈开发者
 * @date 2025-01-01
 */
public class WaybillTemplate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 模板名称 */
    @NotBlank(message = "模板名称不能为空")
    @Size(max = 64, message = "模板名称长度不能超过64个字符")
    private String templateName;

    /** 模板编码 */
    @NotBlank(message = "模板编码不能为空")
    @Size(max = 32, message = "模板编码长度不能超过32个字符")
    private String templateCode;

    /** LOGO路径 */
    private String logoUrl;

    /** 模板布局JSON */
    @NotBlank(message = "模板布局不能为空")
    private String templateJson;

    /** 纸张尺寸 */
    @NotBlank(message = "纸张尺寸不能为空")
    @Size(max = 16, message = "纸张尺寸长度不能超过16个字符")
    private String paperSize;

    /** 适用范围（1-全平台 2-指定区域 3-指定司机） */
    @NotBlank(message = "适用范围不能为空")
    private String scopeType;

    /** 状态（0-禁用 1-启用） */
    private String status;

    /** 逻辑删除（0-正常 1-删除）*/
    private String delFlag;

    // 扩展字段：若依字典回显用（前端展示文字）
    private String scopeTypeLabel;
    private String statusLabel;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("templateName", getTemplateName())
            .append("templateCode", getTemplateCode())
            .append("logoUrl", getLogoUrl())
            .append("templateJson", getTemplateJson())
            .append("paperSize", getPaperSize())
            .append("scopeType", getScopeType())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getTemplateJson() {
        return templateJson;
    }

    public void setTemplateJson(String templateJson) {
        this.templateJson = templateJson;
    }

    public String getPaperSize() {
        return paperSize;
    }

    public void setPaperSize(String paperSize) {
        this.paperSize = paperSize;
    }

    public String getScopeType() {
        return scopeType;
    }

    public void setScopeType(String scopeType) {
        this.scopeType = scopeType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getScopeTypeLabel() {
        return scopeTypeLabel;
    }

    public void setScopeTypeLabel(String scopeTypeLabel) {
        this.scopeTypeLabel = scopeTypeLabel;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
}
