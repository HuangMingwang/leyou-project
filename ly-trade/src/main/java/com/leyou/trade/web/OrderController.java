package com.leyou.trade.web;

import com.leyou.trade.dto.OrderDTO;
import com.leyou.trade.dto.OrderFormDTO;
import com.leyou.trade.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Huang Mingwang
 * @create 2021-06-11 2:26 下午
 */
@RestController
@RequestMapping("order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * 新增订单
     * @param orderFormDTO 订单数据
     * @return 订单编号
     */
    @PostMapping
    public ResponseEntity<Long> createOrder(@RequestBody OrderFormDTO orderFormDTO){
        // 创建订单
        Long orderId = orderService.createOrder(orderFormDTO);
        // 返回id
        return ResponseEntity.status(HttpStatus.CREATED).body(orderId);
    }

    /**
     * 根据id查询订单
     * @param id 订单id
     * @return 订单对象
     */
    @GetMapping("{id}")
    public ResponseEntity<OrderDTO> queryOrderById(@PathVariable("id") Long id){
        return ResponseEntity.ok(new OrderDTO(orderService.getById(id)));
    }

    /**
     * 查询订单支付状态
     * @param orderId 订单id
     * @return 状态值
     */
    @GetMapping("/state/{id}")
    public ResponseEntity<Integer> queryOrderState(@PathVariable("id") Long orderId){
        return ResponseEntity.ok(orderService.queryOrderState(orderId));
    }

}
