package com.ruoyi.express.service;

import java.util.List;
import com.ruoyi.express.domain.ExpressMessageNotice;

/**
 * 消息通知记录Service接口
 * 
 * @author apisflorea
 * @date 2025-12-29
 */
public interface IExpressMessageNoticeService 
{
    /**
     * 查询消息通知记录
     * 
     * @param id 消息通知记录主键
     * @return 消息通知记录
     */
    public ExpressMessageNotice selectExpressMessageNoticeById(Long id);

    /**
     * 查询消息通知记录列表
     * 
     * @param expressMessageNotice 消息通知记录
     * @return 消息通知记录集合
     */
    public List<ExpressMessageNotice> selectExpressMessageNoticeList(ExpressMessageNotice expressMessageNotice);

    /**
     * 新增消息通知记录
     * 
     * @param expressMessageNotice 消息通知记录
     * @return 结果
     */
    public int insertExpressMessageNotice(ExpressMessageNotice expressMessageNotice);

    /**
     * 修改消息通知记录
     * 
     * @param expressMessageNotice 消息通知记录
     * @return 结果
     */
    public int updateExpressMessageNotice(ExpressMessageNotice expressMessageNotice);

    /**
     * 批量删除消息通知记录
     * 
     * @param ids 需要删除的消息通知记录主键集合
     * @return 结果
     */
    public int deleteExpressMessageNoticeByIds(Long[] ids);

    /**
     * 删除消息通知记录信息
     * 
     * @param id 消息通知记录主键
     * @return 结果
     */
    public int deleteExpressMessageNoticeById(Long id);
}
