// 接口
package com.ruoyi.express.service;

import java.util.List;
import java.util.Map;

import com.ruoyi.express.domain.WaybillTemplate;

/**
 * 面单模板Service接口
 *
 * @author 全栈开发者
 * @date 2025-01-01
 */
public interface IWaybillTemplateService
{
    /**
     * 查询面单模板
     *
     * @param id 面单模板主键
     * @return 面单模板
     */
    public WaybillTemplate selectWaybillTemplateById(Long id);

    /**
     * 查询面单模板列表
     *
     * @param waybillTemplate 面单模板
     * @return 面单模板集合
     */
    public List<WaybillTemplate> selectWaybillTemplateList(WaybillTemplate waybillTemplate);

    /**
     * 新增面单模板
     *
     * @param waybillTemplate 面单模板
     * @return 结果
     */
    public int insertWaybillTemplate(WaybillTemplate waybillTemplate);

    /**
     * 修改面单模板
     *
     * @param waybillTemplate 面单模板
     * @return 结果
     */
    public int updateWaybillTemplate(WaybillTemplate waybillTemplate);

    /**
     * 批量删除面单模板
     *
     * @param ids 需要删除的面单模板主键集合
     * @return 结果
     */
    public int deleteWaybillTemplateByIds(Long[] ids);

    /**
     * 删除面单模板信息
     *
     * @param id 面单模板主键
     * @return 结果
     */
    public int deleteWaybillTemplateById(Long id);

    /**
     * 预览面单模板（返回模板数据，由前端负责渲染）
     * @param id 模板ID
     * @return 面单模板对象
     */
    public WaybillTemplate previewWaybillTemplate(Long id);

    /**
     * 根据订单和模板生成打印数据（返回订单和模板数据，由前端负责渲染）
     * @param orderId 订单ID
     * @param templateId 模板ID
     * @return 包含订单和模板数据的Map
     */
    public Map<String, Object> generatePrintHtml(Long orderId, Long templateId);
}

