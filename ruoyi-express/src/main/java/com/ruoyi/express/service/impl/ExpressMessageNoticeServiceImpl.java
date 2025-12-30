package com.ruoyi.express.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.express.mapper.ExpressMessageNoticeMapper;
import com.ruoyi.express.domain.ExpressMessageNotice;
import com.ruoyi.express.service.IExpressMessageNoticeService;

/**
 * 消息通知记录Service业务层处理
 * 
 * @author apisflorea
 * @date 2025-12-29
 */
@Service
public class ExpressMessageNoticeServiceImpl implements IExpressMessageNoticeService 
{
    @Autowired
    private ExpressMessageNoticeMapper expressMessageNoticeMapper;

    /**
     * 查询消息通知记录
     * 
     * @param id 消息通知记录主键
     * @return 消息通知记录
     */
    @Override
    public ExpressMessageNotice selectExpressMessageNoticeById(Long id)
    {
        return expressMessageNoticeMapper.selectExpressMessageNoticeById(id);
    }

    /**
     * 查询消息通知记录列表
     * 
     * @param expressMessageNotice 消息通知记录
     * @return 消息通知记录
     */
    @Override
    public List<ExpressMessageNotice> selectExpressMessageNoticeList(ExpressMessageNotice expressMessageNotice)
    {
        return expressMessageNoticeMapper.selectExpressMessageNoticeList(expressMessageNotice);
    }

    /**
     * 新增消息通知记录
     * 
     * @param expressMessageNotice 消息通知记录
     * @return 结果
     */
    @Override
    public int insertExpressMessageNotice(ExpressMessageNotice expressMessageNotice)
    {
        expressMessageNotice.setCreateTime(DateUtils.getNowDate());
        return expressMessageNoticeMapper.insertExpressMessageNotice(expressMessageNotice);
    }

    /**
     * 修改消息通知记录
     * 
     * @param expressMessageNotice 消息通知记录
     * @return 结果
     */
    @Override
    public int updateExpressMessageNotice(ExpressMessageNotice expressMessageNotice)
    {
        expressMessageNotice.setUpdateTime(DateUtils.getNowDate());
        return expressMessageNoticeMapper.updateExpressMessageNotice(expressMessageNotice);
    }

    /**
     * 批量删除消息通知记录
     * 
     * @param ids 需要删除的消息通知记录主键
     * @return 结果
     */
    @Override
    public int deleteExpressMessageNoticeByIds(Long[] ids)
    {
        return expressMessageNoticeMapper.deleteExpressMessageNoticeByIds(ids);
    }

    /**
     * 删除消息通知记录信息
     * 
     * @param id 消息通知记录主键
     * @return 结果
     */
    @Override
    public int deleteExpressMessageNoticeById(Long id)
    {
        return expressMessageNoticeMapper.deleteExpressMessageNoticeById(id);
    }
}
