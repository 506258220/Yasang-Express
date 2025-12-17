package com.ruoyi.express.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.express.mapper.ExpressTemplateMapper;
import com.ruoyi.express.domain.ExpressTemplate;
import com.ruoyi.express.service.IExpressTemplateService;

/**
 * 快递模板配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-12-17
 */
@Service
public class ExpressTemplateServiceImpl implements IExpressTemplateService 
{
    @Autowired
    private ExpressTemplateMapper expressTemplateMapper;

    /**
     * 查询快递模板配置
     * 
     * @param templateId 快递模板配置主键
     * @return 快递模板配置
     */
    @Override
    public ExpressTemplate selectExpressTemplateByTemplateId(Long templateId)
    {
        return expressTemplateMapper.selectExpressTemplateByTemplateId(templateId);
    }

    /**
     * 查询快递模板配置列表
     * 
     * @param expressTemplate 快递模板配置
     * @return 快递模板配置
     */
    @Override
    public List<ExpressTemplate> selectExpressTemplateList(ExpressTemplate expressTemplate)
    {
        return expressTemplateMapper.selectExpressTemplateList(expressTemplate);
    }

    /**
     * 新增快递模板配置
     * 
     * @param expressTemplate 快递模板配置
     * @return 结果
     */
    @Override
    public int insertExpressTemplate(ExpressTemplate expressTemplate)
    {
        expressTemplate.setCreateTime(DateUtils.getNowDate());
        return expressTemplateMapper.insertExpressTemplate(expressTemplate);
    }

    /**
     * 修改快递模板配置
     * 
     * @param expressTemplate 快递模板配置
     * @return 结果
     */
    @Override
    public int updateExpressTemplate(ExpressTemplate expressTemplate)
    {
        expressTemplate.setUpdateTime(DateUtils.getNowDate());
        return expressTemplateMapper.updateExpressTemplate(expressTemplate);
    }

    /**
     * 批量删除快递模板配置
     * 
     * @param templateIds 需要删除的快递模板配置主键
     * @return 结果
     */
    @Override
    public int deleteExpressTemplateByTemplateIds(Long[] templateIds)
    {
        return expressTemplateMapper.deleteExpressTemplateByTemplateIds(templateIds);
    }

    /**
     * 删除快递模板配置信息
     * 
     * @param templateId 快递模板配置主键
     * @return 结果
     */
    @Override
    public int deleteExpressTemplateByTemplateId(Long templateId)
    {
        return expressTemplateMapper.deleteExpressTemplateByTemplateId(templateId);
    }
}
