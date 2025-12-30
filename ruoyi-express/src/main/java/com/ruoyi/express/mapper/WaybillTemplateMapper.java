package com.ruoyi.express.mapper;

import java.util.List;
import com.ruoyi.express.domain.WaybillTemplate;
import org.apache.ibatis.annotations.Mapper;

/**
 * 面单模板Mapper接口
 *
 * @author 全栈开发者
 * @date 2025-01-01
 */
@Mapper
public interface WaybillTemplateMapper
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
     * 删除面单模板
     *
     * @param id 面单模板主键
     * @return 结果
     */
    public int deleteWaybillTemplateById(Long id);

    /**
     * 批量删除面单模板
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWaybillTemplateByIds(Long[] ids);
}
