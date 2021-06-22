package com.leyou.item.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leyou.item.entity.Sku;
import org.apache.ibatis.annotations.Update;

import java.util.Map;


/**
 * @author Huang Mingwang
 * @create 2021-05-28 9:35 下午
 */
public interface SkuMapper extends BaseMapper<Sku> {
    @Update({"update tb_sku set sold = sold + #{num}, stock = stock - #{num} where id = #{id}"})
    int deductStock(Map<Long, Integer> map);

    @Update("UPDATE tb_sku SET stock = stock + #{num}, sold = sold - #{num} WHERE id = #{id}")
    int addStock(Map<String,Object> sku);
}
