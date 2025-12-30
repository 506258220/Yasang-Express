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
import com.ruoyi.express.domain.ExpressDriverPerformance;
import com.ruoyi.express.service.IExpressDriverPerformanceService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 司机业绩统计Controller
 * 
 * @author apisflorea
 * @date 2025-12-29
 */
@RestController
@RequestMapping("/express/performance")
public class ExpressDriverPerformanceController extends BaseController
{
    @Autowired
    private IExpressDriverPerformanceService expressDriverPerformanceService;

    /**
     * 查询司机业绩统计列表
     */
    @PreAuthorize("@ss.hasPermi('express:performance:list')")
    @GetMapping("/list")
    public TableDataInfo list(ExpressDriverPerformance expressDriverPerformance)
    {
        startPage();
        List<ExpressDriverPerformance> list = expressDriverPerformanceService.selectExpressDriverPerformanceList(expressDriverPerformance);
        return getDataTable(list);
    }

    /**
     * 导出司机业绩统计列表
     */
    @PreAuthorize("@ss.hasPermi('express:performance:export')")
    @Log(title = "司机业绩统计", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExpressDriverPerformance expressDriverPerformance)
    {
        List<ExpressDriverPerformance> list = expressDriverPerformanceService.selectExpressDriverPerformanceList(expressDriverPerformance);
        ExcelUtil<ExpressDriverPerformance> util = new ExcelUtil<ExpressDriverPerformance>(ExpressDriverPerformance.class);
        util.exportExcel(response, list, "司机业绩统计数据");
    }

    /**
     * 获取司机业绩统计详细信息
     */
    @PreAuthorize("@ss.hasPermi('express:performance:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(expressDriverPerformanceService.selectExpressDriverPerformanceById(id));
    }

    /**
     * 新增司机业绩统计
     */
    @PreAuthorize("@ss.hasPermi('express:performance:add')")
    @Log(title = "司机业绩统计", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ExpressDriverPerformance expressDriverPerformance)
    {
        return toAjax(expressDriverPerformanceService.insertExpressDriverPerformance(expressDriverPerformance));
    }

    /**
     * 修改司机业绩统计
     */
    @PreAuthorize("@ss.hasPermi('express:performance:edit')")
    @Log(title = "司机业绩统计", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ExpressDriverPerformance expressDriverPerformance)
    {
        return toAjax(expressDriverPerformanceService.updateExpressDriverPerformance(expressDriverPerformance));
    }

    /**
     * 删除司机业绩统计
     */
    @PreAuthorize("@ss.hasPermi('express:performance:remove')")
    @Log(title = "司机业绩统计", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(expressDriverPerformanceService.deleteExpressDriverPerformanceByIds(ids));
    }
}
