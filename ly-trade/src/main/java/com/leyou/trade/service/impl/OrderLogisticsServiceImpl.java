package com.leyou.trade.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leyou.trade.entity.OrderLogistics;
import com.leyou.trade.mapper.OrderLogisticsMapper;
import com.leyou.trade.service.OrderLogisticsService;
import org.springframework.stereotype.Service;

/**
 * @author Huang Mingwang
 * @create 2021-06-09 2:18 下午
 */
@Service
public class OrderLogisticsServiceImpl extends ServiceImpl<OrderLogisticsMapper, OrderLogistics>
        implements OrderLogisticsService {
}
