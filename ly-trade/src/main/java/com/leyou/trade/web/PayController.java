package com.leyou.trade.web;

import com.leyou.trade.dto.PayResultDTO;
import com.leyou.trade.service.OrderService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Huang Mingwang
 * @create 2021-06-12 2:26 下午
 */
@RestController
@RequestMapping("pay")
public class PayController {

    private final OrderService orderService;

    public PayController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/url/{id}")
    public ResponseEntity<String> getPayUrl(@PathVariable("id") Long id){
        // 查询支付链接
        return ResponseEntity.ok(orderService.getPayUrl(id));
    }

    /**
     * 处理微信的异步通知
     * @param data 通知内容
     * @return 处理结果
     */
    @PostMapping(value = "/wx/notify", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<PayResultDTO> handleWxNotify(@RequestBody Map<String,String> data){
        orderService.handleNotify(data);
        return ResponseEntity.ok(new PayResultDTO());
    }

}

