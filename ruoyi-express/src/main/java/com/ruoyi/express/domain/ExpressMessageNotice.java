package com.ruoyi.express.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 消息通知记录对象 express_message_notice
 * 
 * @author apisflorea
 * @date 2025-12-29
 */
public class ExpressMessageNotice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 接收用户ID（用户/司机） */
    @Excel(name = "接收用户ID", readConverterExp = "用=户/司机")
    private Long userId;

    /** 关联快递单号 */
    @Excel(name = "关联快递单号")
    private String orderNo;

    /** 通知类型（订单创建/揽收/派送/签收） */
    @Excel(name = "通知类型", readConverterExp = "订=单创建/揽收/派送/签收")
    private String noticeType;

    /** 通知内容 */
    @Excel(name = "通知内容")
    private String noticeContent;

    /** 通知渠道（1-微信服务通知 2-站内信） */
    @Excel(name = "通知渠道", readConverterExp = "1=-微信服务通知,2=-站内信")
    private String noticeChannel;

    /** 发送时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发送时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date sendTime;

    /** 阅读状态（0-未读 1-已读） */
    @Excel(name = "阅读状态", readConverterExp = "0=-未读,1=-已读")
    private String readFlag;

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

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setOrderNo(String orderNo) 
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo() 
    {
        return orderNo;
    }

    public void setNoticeType(String noticeType) 
    {
        this.noticeType = noticeType;
    }

    public String getNoticeType() 
    {
        return noticeType;
    }

    public void setNoticeContent(String noticeContent) 
    {
        this.noticeContent = noticeContent;
    }

    public String getNoticeContent() 
    {
        return noticeContent;
    }

    public void setNoticeChannel(String noticeChannel) 
    {
        this.noticeChannel = noticeChannel;
    }

    public String getNoticeChannel() 
    {
        return noticeChannel;
    }

    public void setSendTime(Date sendTime) 
    {
        this.sendTime = sendTime;
    }

    public Date getSendTime() 
    {
        return sendTime;
    }

    public void setReadFlag(String readFlag) 
    {
        this.readFlag = readFlag;
    }

    public String getReadFlag() 
    {
        return readFlag;
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
            .append("userId", getUserId())
            .append("orderNo", getOrderNo())
            .append("noticeType", getNoticeType())
            .append("noticeContent", getNoticeContent())
            .append("noticeChannel", getNoticeChannel())
            .append("sendTime", getSendTime())
            .append("readFlag", getReadFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
