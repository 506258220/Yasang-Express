package com.ruoyi.express.mapper;

import java.util.List;
import com.ruoyi.express.domain.ExpressMessageNotice;

/**
 * 消息通知记录Mapper接口
 * 
 * @author apisflorea
 * @date 2025-12-29
 */
public interface ExpressMessageNoticeMapper 
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
     * 删除消息通知记录
     * 
     * @param id 消息通知记录主键
     * @return 结果
     */
    public int deleteExpressMessageNoticeById(Long id);

    /**
     * 批量删除消息通知记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteExpressMessageNoticeByIds(Long[] ids);
}
