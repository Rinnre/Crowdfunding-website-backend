package com.wj.crowd.management.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wj.crowd.api.mysql.MysqlRemoteService;
import com.wj.crowd.common.result.ResultEntity;
import com.wj.crowd.entity.Vo.order.PayOrderVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author wj
 * @descript
 * @date 2022/6/4 - 13:33
 */
@RestController
@RequestMapping("/management/order")
@CrossOrigin
public class OrderController {

    @Autowired
    private MysqlRemoteService mysqlRemoteService;


    @ApiOperation("获取所有订单信息")
    @GetMapping("/get/all/order/pages/{page}/{size}")
    public ResultEntity<Page<PayOrderVo>> getAllOrderPages(@PathVariable Long page,
                                                           @PathVariable Long size,
                                                           @RequestParam(required = false) Map<String, String> params
    ) {

        String keyWords = params.get("keyWords");
        String orderStatus = params.get("orderStatus");
        return mysqlRemoteService.getAllOrderPages(page, size, keyWords, orderStatus);

    }
}
