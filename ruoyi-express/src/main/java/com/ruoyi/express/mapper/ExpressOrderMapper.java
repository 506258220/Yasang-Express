package com.ruoyi.express.mapper;

import java.util.List;
import com.ruoyi.express.domain.ExpressOrder;

/**
 * 快递订单Mapper接口
 * 
 * @author apisflorea
 * @date 2025-12-29
 */
public interface ExpressOrderMapper 
{
    /**
     * 查询快递订单
     * 
     * @param id 快递订单主键
     * @return 快递订单
     */
    public ExpressOrder selectExpressOrderById(Long id);

    /**
     * 查询快递订单列表
     * 
     * @param expressOrder 快递订单
     * @return 快递订单集合
     */
    public List<ExpressOrder> selectExpressOrderList(ExpressOrder expressOrder);

    /**
     * 新增快递订单
     * 
     * @param expressOrder 快递订单
     * @return 结果
     */
    public int insertExpressOrder(ExpressOrder expressOrder);

    /**
     * 修改快递订单
     * 
     * @param expressOrder 快递订单
     * @return 结果
     */
    public int updateExpressOrder(ExpressOrder expressOrder);

    /**
     * 删除快递订单
     * 
     * @param id 快递订单主键
     * @return 结果
     */
    public int deleteExpressOrderById(Long id);

    /**
     * 批量删除快递订单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteExpressOrderByIds(Long[] ids);
}
