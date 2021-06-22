package com.leyou.item.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leyou.common.exception.LyException;
import com.leyou.item.entity.Sku;
import com.leyou.item.mapper.SkuMapper;
import com.leyou.item.service.SkuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Huang Mingwang
 * @create 2021-05-28 9:43 下午
 */
@Service
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements SkuService {
    private static final String DEDUCT_STOCK_STATEMENT = "com.leyou.item.mapper.SkuMapper.deductStock";
    private static final String ADD_STOCK_STATEMENT = "com.leyou.item.mapper.SkuMapper.addStock";
    @Override
    @Transactional
    public void deductStock(Map<Long, Integer> cartMap) {
        /**
         * 批量减库存
         */
        try {

            executeBatch(sqlSession -> {
                for (Map.Entry<Long, Integer> entry : cartMap.entrySet()) {
                    Integer num = entry.getValue();
                    Long id = entry.getKey();
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("id", id);
                    map.put("num", num);
                    sqlSession.update(DEDUCT_STOCK_STATEMENT, map);
                }
                sqlSession.flushStatements();
            });
        } catch (Exception e) {
            throw new LyException(400, "手速慢啦");
        }


    }

    @Override
    @Transactional
    public void addStock(Map<Long, Integer> skuMap) {
        try {
            // 批处理
            updateStock(skuMap, ADD_STOCK_STATEMENT);
        } catch (Exception e) {
            throw new LyException(500, "库存恢复失败！");
        }
    }

    private void updateStock(Map<Long, Integer> skuMap, String statement) {
        // 批处理
        executeBatch(sqlSession -> {
            for (Map.Entry<Long, Integer> entry : skuMap.entrySet()) {
                // 如果充足，扣减库存 update tb_sku set stock = stock - 1, sold = sold + 1  where id = 1
                Map<String, Object> param = new HashMap<>(2);
                param.put("id", entry.getKey());
                param.put("num", entry.getValue());
                // 执行update两个参数：statementId（接口全路径名.方法名），sql需要的参数
                sqlSession.update(statement, param);
            }
            sqlSession.flushStatements();
        });
    }
}
