package com.ruoyi.express.mapper;

import java.util.List;
import com.ruoyi.express.domain.ExpressTemplate;

/**
 * 快递模板配置Mapper接口
 * 
 * @author ruoyi
 * @date 2025-12-17
 */
public interface ExpressTemplateMapper 
{
    /**
     * 查询快递模板配置
     * 
     * @param templateId 快递模板配置主键
     * @return 快递模板配置
     */
    public ExpressTemplate selectExpressTemplateByTemplateId(Long templateId);

    /**
     * 查询快递模板配置列表
     * 
     * @param expressTemplate 快递模板配置
     * @return 快递模板配置集合
     */
    public List<ExpressTemplate> selectExpressTemplateList(ExpressTemplate expressTemplate);

    /**
     * 新增快递模板配置
     * 
     * @param expressTemplate 快递模板配置
     * @return 结果
     */
    public int insertExpressTemplate(ExpressTemplate expressTemplate);

    /**
     * 修改快递模板配置
     * 
     * @param expressTemplate 快递模板配置
     * @return 结果
     */
    public int updateExpressTemplate(ExpressTemplate expressTemplate);

    /**
     * 删除快递模板配置
     * 
     * @param templateId 快递模板配置主键
     * @return 结果
     */
    public int deleteExpressTemplateByTemplateId(Long templateId);

    /**
     * 批量删除快递模板配置
     * 
     * @param templateIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteExpressTemplateByTemplateIds(Long[] templateIds);
}
