package com.ruoyi.express.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.express.mapper.ExpressTrackMapper;
import com.ruoyi.express.domain.ExpressTrack;
import com.ruoyi.express.service.IExpressTrackService;

/**
 * 物流轨迹Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-17
 */
@Service
public class ExpressTrackServiceImpl implements IExpressTrackService 
{
    @Autowired
    private ExpressTrackMapper expressTrackMapper;

    /**
     * 查询物流轨迹
     * 
     * @param trackId 物流轨迹主键
     * @return 物流轨迹
     */
    @Override
    public ExpressTrack selectExpressTrackByTrackId(Long trackId)
    {
        return expressTrackMapper.selectExpressTrackByTrackId(trackId);
    }

    /**
     * 查询物流轨迹列表
     * 
     * @param expressTrack 物流轨迹
     * @return 物流轨迹
     */
    @Override
    public List<ExpressTrack> selectExpressTrackList(ExpressTrack expressTrack)
    {
        return expressTrackMapper.selectExpressTrackList(expressTrack);
    }

    /**
     * 新增物流轨迹
     * 
     * @param expressTrack 物流轨迹
     * @return 结果
     */
    @Override
    public int insertExpressTrack(ExpressTrack expressTrack)
    {
        expressTrack.setCreateTime(DateUtils.getNowDate());
        return expressTrackMapper.insertExpressTrack(expressTrack);
    }

    /**
     * 修改物流轨迹
     * 
     * @param expressTrack 物流轨迹
     * @return 结果
     */
    @Override
    public int updateExpressTrack(ExpressTrack expressTrack)
    {
        return expressTrackMapper.updateExpressTrack(expressTrack);
    }

    /**
     * 批量删除物流轨迹
     * 
     * @param trackIds 需要删除的物流轨迹主键
     * @return 结果
     */
    @Override
    public int deleteExpressTrackByTrackIds(Long[] trackIds)
    {
        return expressTrackMapper.deleteExpressTrackByTrackIds(trackIds);
    }

    /**
     * 删除物流轨迹信息
     * 
     * @param trackId 物流轨迹主键
     * @return 结果
     */
    @Override
    public int deleteExpressTrackByTrackId(Long trackId)
    {
        return expressTrackMapper.deleteExpressTrackByTrackId(trackId);
    }
}
