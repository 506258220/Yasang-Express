package com.ruoyi.express.mapper;

import java.util.List;
import com.ruoyi.express.domain.ExpressWarehouse;

/**
 * 仓库信息Mapper接口
 * 
 * @author apisflorea
 * @date 2025-12-29
 */
public interface ExpressWarehouseMapper 
{
    /**
     * 查询仓库信息
     * 
     * @param id 仓库信息主键
     * @return 仓库信息
     */
    public ExpressWarehouse selectExpressWarehouseById(Long id);

    /**
     * 查询仓库信息列表
     * 
     * @param expressWarehouse 仓库信息
     * @return 仓库信息集合
     */
    public List<ExpressWarehouse> selectExpressWarehouseList(ExpressWarehouse expressWarehouse);

    /**
     * 新增仓库信息
     * 
     * @param expressWarehouse 仓库信息
     * @return 结果
     */
    public int insertExpressWarehouse(ExpressWarehouse expressWarehouse);

    /**
     * 修改仓库信息
     * 
     * @param expressWarehouse 仓库信息
     * @return 结果
     */
    public int updateExpressWarehouse(ExpressWarehouse expressWarehouse);

    /**
     * 删除仓库信息
     * 
     * @param id 仓库信息主键
     * @return 结果
     */
    public int deleteExpressWarehouseById(Long id);

    /**
     * 批量删除仓库信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteExpressWarehouseByIds(Long[] ids);
}
