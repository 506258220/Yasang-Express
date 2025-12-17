package com.ruoyi.common.utils.express;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.core.redis.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 快递单号生成工具类
 *
 * @author ruoyi
 */
@Component
public class ExpressNoGenerator
{
    private static final Lock lock = new ReentrantLock();

    @Autowired
    private RedisCache redisCache;

    /**
     * 生成快递单号
     * 规则：前缀 + 8位日期 + 6位随机数
     *
     * @param prefix 快递公司前缀（如SF、ZT等）
     * @return 唯一的快递单号
     */
    public String generateExpressNo(String prefix)
    {
        // 加本地锁，防止同一实例下并发生成重复单号
        lock.lock();
        try
        {
            String dateStr = DateUtils.dateTimeNow("yyyyMMdd");
            String redisKey = "express:no:" + prefix + ":" + dateStr;

            // 使用Redis实现分布式锁和自增，确保每天每个前缀下的序号唯一
            // 获取当前自增值，不存在则返回1
            Long sequence = redisCache.incre(redisKey);

            // 设置过期时间为24小时，确保每天重新计数
            redisCache.expire(redisKey, 24, TimeUnit.HOURS);

            // 生成6位随机数，使用自增值确保唯一性
            String sequenceStr = StringUtils.leftPad(String.valueOf(sequence), 6, "0");

            // 组合成完整的快递单号
            return prefix + dateStr + sequenceStr;
        }
        finally
        {
            lock.unlock();
        }
    }

    /**
     * 生成多个快递单号
     *
     * @param prefix 快递公司前缀
     * @param count 需要生成的单号数量
     * @return 快递单号数组
     */
    public String[] generateExpressNos(String prefix, int count)
    {
        String[] expressNos = new String[count];
        for (int i = 0; i < count; i++)
        {
            expressNos[i] = generateExpressNo(prefix);
        }
        return expressNos;
    }

    /**
     * 校验快递单号格式是否正确
     *
     * @param expressNo 快递单号
     * @return 校验结果
     */
    public boolean validateExpressNo(String expressNo)
    {
        if (StringUtils.isEmpty(expressNo))
        {
            return false;
        }
        // 规则：2-4位字母前缀 + 8位数字日期 + 6位数字序号
        String regex = "^[A-Z]{2,4}\\d{14}$";
        return expressNo.matches(regex);
    }
}
