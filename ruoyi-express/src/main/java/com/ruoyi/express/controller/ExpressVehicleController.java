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
import com.ruoyi.express.domain.ExpressVehicle;
import com.ruoyi.express.service.IExpressVehicleService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 车辆信息Controller
 * 
 * @author apisflorea
 * @date 2025-12-29
 */
@RestController
@RequestMapping("/express/vehicle")
public class ExpressVehicleController extends BaseController
{
    @Autowired
    private IExpressVehicleService expressVehicleService;

    /**
     * 查询车辆信息列表
     */
    @PreAuthorize("@ss.hasPermi('express:vehicle:list')")
    @GetMapping("/list")
    public TableDataInfo list(ExpressVehicle expressVehicle)
    {
        startPage();
        List<ExpressVehicle> list = expressVehicleService.selectExpressVehicleList(expressVehicle);
        return getDataTable(list);
    }

    /**
     * 导出车辆信息列表
     */
    @PreAuthorize("@ss.hasPermi('express:vehicle:export')")
    @Log(title = "车辆信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExpressVehicle expressVehicle)
    {
        List<ExpressVehicle> list = expressVehicleService.selectExpressVehicleList(expressVehicle);
        ExcelUtil<ExpressVehicle> util = new ExcelUtil<ExpressVehicle>(ExpressVehicle.class);
        util.exportExcel(response, list, "车辆信息数据");
    }

    /**
     * 获取车辆信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('express:vehicle:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(expressVehicleService.selectExpressVehicleById(id));
    }

    /**
     * 新增车辆信息
     */
    @PreAuthorize("@ss.hasPermi('express:vehicle:add')")
    @Log(title = "车辆信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ExpressVehicle expressVehicle)
    {
        return toAjax(expressVehicleService.insertExpressVehicle(expressVehicle));
    }

    /**
     * 修改车辆信息
     */
    @PreAuthorize("@ss.hasPermi('express:vehicle:edit')")
    @Log(title = "车辆信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ExpressVehicle expressVehicle)
    {
        return toAjax(expressVehicleService.updateExpressVehicle(expressVehicle));
    }

    /**
     * 删除车辆信息
     */
    @PreAuthorize("@ss.hasPermi('express:vehicle:remove')")
    @Log(title = "车辆信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(expressVehicleService.deleteExpressVehicleByIds(ids));
    }
}
