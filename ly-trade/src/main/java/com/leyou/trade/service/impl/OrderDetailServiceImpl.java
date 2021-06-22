package com.leyou.trade.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leyou.trade.entity.OrderDetail;
import com.leyou.trade.mapper.OrderDetailMapper;
import com.leyou.trade.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @author Huang Mingwang
 * @create 2021-06-09 2:17 下午
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
