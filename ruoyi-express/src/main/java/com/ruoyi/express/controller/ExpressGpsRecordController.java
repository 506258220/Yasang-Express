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
import com.ruoyi.express.domain.ExpressGpsRecord;
import com.ruoyi.express.service.IExpressGpsRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * GPS定位记录Controller
 *
 * @author apisflorea
 * @date 2025-12-29
 */
@RestController
@RequestMapping("/express/gps/record")
public class ExpressGpsRecordController extends BaseController
{
    @Autowired
    private IExpressGpsRecordService expressGpsRecordService;

    /**
     * 查询GPS定位记录列表
     */
    @PreAuthorize("@ss.hasPermi('express:gps:list')")
    @GetMapping("/list")
    public TableDataInfo list(ExpressGpsRecord expressGpsRecord)
    {
        startPage();
        List<ExpressGpsRecord> list = expressGpsRecordService.selectExpressGpsRecordList(expressGpsRecord);
        return getDataTable(list);
    }

    /**
     * 导出GPS定位记录列表
     */
    @PreAuthorize("@ss.hasPermi('express:gps:export')")
    @Log(title = "GPS定位记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExpressGpsRecord expressGpsRecord)
    {
        List<ExpressGpsRecord> list = expressGpsRecordService.selectExpressGpsRecordList(expressGpsRecord);
        ExcelUtil<ExpressGpsRecord> util = new ExcelUtil<ExpressGpsRecord>(ExpressGpsRecord.class);
        util.exportExcel(response, list, "GPS定位记录数据");
    }

    /**
     * 获取GPS定位记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('express:gps:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(expressGpsRecordService.selectExpressGpsRecordById(id));
    }

    /**
     * 新增GPS定位记录
     */
    @PreAuthorize("@ss.hasPermi('express:gps:add')")
    @Log(title = "GPS定位记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ExpressGpsRecord expressGpsRecord)
    {
        return toAjax(expressGpsRecordService.insertExpressGpsRecord(expressGpsRecord));
    }

    /**
     * 修改GPS定位记录
     */
    @PreAuthorize("@ss.hasPermi('express:gps:edit')")
    @Log(title = "GPS定位记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ExpressGpsRecord expressGpsRecord)
    {
        return toAjax(expressGpsRecordService.updateExpressGpsRecord(expressGpsRecord));
    }

    /**
     * 删除GPS定位记录
     */
    @PreAuthorize("@ss.hasPermi('express:gps:remove')")
    @Log(title = "GPS定位记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(expressGpsRecordService.deleteExpressGpsRecordByIds(ids));
    }
}
