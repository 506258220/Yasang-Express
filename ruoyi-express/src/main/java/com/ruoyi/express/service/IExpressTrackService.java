package com.ruoyi.express.service;

import java.util.List;
import com.ruoyi.express.domain.ExpressTrack;

/**
 * 物流轨迹Service接口
 * 
 * @author ruoyi
 * @date 2025-12-17
 */
public interface IExpressTrackService 
{
    /**
     * 查询物流轨迹
     * 
     * @param trackId 物流轨迹主键
     * @return 物流轨迹
     */
    public ExpressTrack selectExpressTrackByTrackId(Long trackId);

    /**
     * 查询物流轨迹列表
     * 
     * @param expressTrack 物流轨迹
     * @return 物流轨迹集合
     */
    public List<ExpressTrack> selectExpressTrackList(ExpressTrack expressTrack);

    /**
     * 新增物流轨迹
     * 
     * @param expressTrack 物流轨迹
     * @return 结果
     */
    public int insertExpressTrack(ExpressTrack expressTrack);

    /**
     * 修改物流轨迹
     * 
     * @param expressTrack 物流轨迹
     * @return 结果
     */
    public int updateExpressTrack(ExpressTrack expressTrack);

    /**
     * 批量删除物流轨迹
     * 
     * @param trackIds 需要删除的物流轨迹主键集合
     * @return 结果
     */
    public int deleteExpressTrackByTrackIds(Long[] trackIds);

    /**
     * 删除物流轨迹信息
     * 
     * @param trackId 物流轨迹主键
     * @return 结果
     */
    public int deleteExpressTrackByTrackId(Long trackId);
}
