package com.wj.crowd.mysql.controller;


import com.wj.crowd.common.result.ResultEntity;
import com.wj.crowd.entity.Do.ShippingAddress;
import com.wj.crowd.mysql.service.api.ShippingAddressService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author w
 * @since 2022-05-26
 */
@RestController
@RequestMapping("/mysql/shipping-address")
public class ShippingAddressController {

    @Autowired
    private ShippingAddressService shippingAddressService;

    @GetMapping("/get/shipping/address/{uid}")
    @ApiOperation("获取用户所有地址信息")
    public ResultEntity<List<ShippingAddress>> getShippingAddress(@PathVariable String uid){
        try {
            List<ShippingAddress> shippingAddressList =shippingAddressService.getShippingAddress(uid);
            return ResultEntity.success(shippingAddressList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }
    }

    @PutMapping("/modify/shipping/address")
    @ApiOperation("修改用户地址信息")
    public ResultEntity<String> modifyShippingAddress(@RequestBody ShippingAddress shippingAddress){
        try {
            shippingAddressService.modifyShippingAddress(shippingAddress);
            return ResultEntity.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }


    @PostMapping("/save/shipping/address")
    @ApiOperation("新建收货地址")
    public ResultEntity<String> saveShippingAddress(@RequestBody ShippingAddress shippingAddress){
        try {
            shippingAddressService.save(shippingAddress);
            return ResultEntity.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }


    @DeleteMapping("/remove/shipping/address/{addressId}")
    @ApiOperation("删除收货地址")
    public ResultEntity<String> removeShippingAddress(@PathVariable String addressId){
        try {
            shippingAddressService.removeById(addressId);
            return ResultEntity.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }

}

