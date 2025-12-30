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
import com.ruoyi.express.domain.ExpressWarehouseRecord;
import com.ruoyi.express.service.IExpressWarehouseRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 仓库出入库记录Controller
 *
 * @author apisflorea
 * @date 2025-12-29
 */
@RestController
@RequestMapping("/express/warehouse/record")
public class ExpressWarehouseRecordController extends BaseController
{
    @Autowired
    private IExpressWarehouseRecordService expressWarehouseRecordService;

    /**
     * 查询仓库出入库记录列表
     */
    @PreAuthorize("@ss.hasPermi('express:warehouse:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(ExpressWarehouseRecord expressWarehouseRecord)
    {
        startPage();
        List<ExpressWarehouseRecord> list = expressWarehouseRecordService.selectExpressWarehouseRecordList(expressWarehouseRecord);
        return getDataTable(list);
    }

    /**
     * 导出仓库出入库记录列表
     */
    @PreAuthorize("@ss.hasPermi('express:warehouse:record:export')")
    @Log(title = "仓库出入库记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExpressWarehouseRecord expressWarehouseRecord)
    {
        List<ExpressWarehouseRecord> list = expressWarehouseRecordService.selectExpressWarehouseRecordList(expressWarehouseRecord);
        ExcelUtil<ExpressWarehouseRecord> util = new ExcelUtil<ExpressWarehouseRecord>(ExpressWarehouseRecord.class);
        util.exportExcel(response, list, "仓库出入库记录数据");
    }

    /**
     * 获取仓库出入库记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('express:warehouse:record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(expressWarehouseRecordService.selectExpressWarehouseRecordById(id));
    }

    /**
     * 新增仓库出入库记录
     */
    @PreAuthorize("@ss.hasPermi('express:warehouse:record:add')")
    @Log(title = "仓库出入库记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ExpressWarehouseRecord expressWarehouseRecord)
    {
        return toAjax(expressWarehouseRecordService.insertExpressWarehouseRecord(expressWarehouseRecord));
    }

    /**
     * 修改仓库出入库记录
     */
    @PreAuthorize("@ss.hasPermi('express:warehouse:record:edit')")
    @Log(title = "仓库出入库记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ExpressWarehouseRecord expressWarehouseRecord)
    {
        return toAjax(expressWarehouseRecordService.updateExpressWarehouseRecord(expressWarehouseRecord));
    }

    /**
     * 删除仓库出入库记录
     */
    @PreAuthorize("@ss.hasPermi('express:warehouse:record:remove')")
    @Log(title = "仓库出入库记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(expressWarehouseRecordService.deleteExpressWarehouseRecordByIds(ids));
    }
}
