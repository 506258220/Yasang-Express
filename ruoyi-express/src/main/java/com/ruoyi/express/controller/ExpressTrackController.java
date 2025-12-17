package com.ruoyi.express.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.express.domain.ExpressTrack;
import com.ruoyi.express.service.IExpressTrackService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 物流轨迹Controller
 * 
 * @author ruoyi
 * @date 2025-12-17
 */
@RestController
@RequestMapping("/express/track")
public class ExpressTrackController extends BaseController
{
    @Autowired
    private IExpressTrackService expressTrackService;

    /**
     * 查询物流轨迹列表
     */
    @PreAuthorize("@ss.hasPermi('express:track:list')")
    @GetMapping("/list")
    public TableDataInfo list(ExpressTrack expressTrack)
    {
        startPage();
        List<ExpressTrack> list = expressTrackService.selectExpressTrackList(expressTrack);
        return getDataTable(list);
    }

    /**
     * 导出物流轨迹列表
     */
    @PreAuthorize("@ss.hasPermi('express:track:export')")
    @Log(title = "物流轨迹", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExpressTrack expressTrack)
    {
        List<ExpressTrack> list = expressTrackService.selectExpressTrackList(expressTrack);
        ExcelUtil<ExpressTrack> util = new ExcelUtil<ExpressTrack>(ExpressTrack.class);
        util.exportExcel(response, list, "物流轨迹数据");
    }

    /**
     * 获取物流轨迹详细信息
     */
    @PreAuthorize("@ss.hasPermi('express:track:query')")
    @GetMapping(value = "/{trackId}")
    public AjaxResult getInfo(@PathVariable("trackId") Long trackId)
    {
        return success(expressTrackService.selectExpressTrackByTrackId(trackId));
    }

    /**
     * 新增物流轨迹
     */
    @PreAuthorize("@ss.hasPermi('express:track:add')")
    @Log(title = "物流轨迹", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ExpressTrack expressTrack)
    {
        return toAjax(expressTrackService.insertExpressTrack(expressTrack));
    }

    /**
     * 修改物流轨迹
     */
    @PreAuthorize("@ss.hasPermi('express:track:edit')")
    @Log(title = "物流轨迹", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ExpressTrack expressTrack)
    {
        return toAjax(expressTrackService.updateExpressTrack(expressTrack));
    }

    /**
     * 删除物流轨迹
     */
    @PreAuthorize("@ss.hasPermi('express:track:remove')")
    @Log(title = "物流轨迹", businessType = BusinessType.DELETE)
	@DeleteMapping("/{trackIds}")
    public AjaxResult remove(@PathVariable Long[] trackIds)
    {
        return toAjax(expressTrackService.deleteExpressTrackByTrackIds(trackIds));
    }
}
