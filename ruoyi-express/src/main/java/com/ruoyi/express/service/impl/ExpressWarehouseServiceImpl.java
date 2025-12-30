package com.ruoyi.express.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.express.mapper.ExpressWarehouseMapper;
import com.ruoyi.express.domain.ExpressWarehouse;
import com.ruoyi.express.service.IExpressWarehouseService;

/**
 * 仓库信息Service业务层处理
 * 
 * @author apisflorea
 * @date 2025-12-29
 */
@Service
public class ExpressWarehouseServiceImpl implements IExpressWarehouseService 
{
    @Autowired
    private ExpressWarehouseMapper expressWarehouseMapper;

    /**
     * 查询仓库信息
     * 
     * @param id 仓库信息主键
     * @return 仓库信息
     */
    @Override
    public ExpressWarehouse selectExpressWarehouseById(Long id)
    {
        return expressWarehouseMapper.selectExpressWarehouseById(id);
    }

    /**
     * 查询仓库信息列表
     * 
     * @param expressWarehouse 仓库信息
     * @return 仓库信息
     */
    @Override
    public List<ExpressWarehouse> selectExpressWarehouseList(ExpressWarehouse expressWarehouse)
    {
        return expressWarehouseMapper.selectExpressWarehouseList(expressWarehouse);
    }

    /**
     * 新增仓库信息
     * 
     * @param expressWarehouse 仓库信息
     * @return 结果
     */
    @Override
    public int insertExpressWarehouse(ExpressWarehouse expressWarehouse)
    {
        expressWarehouse.setCreateTime(DateUtils.getNowDate());
        return expressWarehouseMapper.insertExpressWarehouse(expressWarehouse);
    }

    /**
     * 修改仓库信息
     * 
     * @param expressWarehouse 仓库信息
     * @return 结果
     */
    @Override
    public int updateExpressWarehouse(ExpressWarehouse expressWarehouse)
    {
        expressWarehouse.setUpdateTime(DateUtils.getNowDate());
        return expressWarehouseMapper.updateExpressWarehouse(expressWarehouse);
    }

    /**
     * 批量删除仓库信息
     * 
     * @param ids 需要删除的仓库信息主键
     * @return 结果
     */
    @Override
    public int deleteExpressWarehouseByIds(Long[] ids)
    {
        return expressWarehouseMapper.deleteExpressWarehouseByIds(ids);
    }

    /**
     * 删除仓库信息信息
     * 
     * @param id 仓库信息主键
     * @return 结果
     */
    @Override
    public int deleteExpressWarehouseById(Long id)
    {
        return expressWarehouseMapper.deleteExpressWarehouseById(id);
    }
}
