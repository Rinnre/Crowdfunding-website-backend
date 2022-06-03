package com.wj.crowd.api.mysql;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wj.crowd.common.result.ResultEntity;
import com.wj.crowd.entity.Do.*;
import com.wj.crowd.entity.Vo.comment.CommentFormVo;
import com.wj.crowd.entity.Vo.order.PayOrderVo;
import com.wj.crowd.entity.Vo.project.SearchProjectVo;
import com.wj.crowd.entity.Vo.project.UpdateProjectVo;
import com.wj.crowd.entity.Vo.user.UserAuthInfoVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @descript
 * @date 2022/5/9 - 14:42
 */
@Service
@FeignClient("crowd-mysql")
public interface MysqlRemoteService {
    // 用户注册登录远程接口
    @GetMapping("/mysql/user/get/user_info/remote/{phone}")
    @ApiOperation("用户手机登录接口")
    ResultEntity<User> getUserByPhoneRemote(@PathVariable String phone);

    @PostMapping("/mysql/user/save/user/remote")
    @ApiOperation("用户注册")
    ResultEntity<String> saveUserRemote(@RequestBody User user);

    @GetMapping("/mysql/user/get/user_info_detail/remote/{uid}")
    @ApiOperation("查询用户详细信息")
    ResultEntity<User> getUserByUidRemote(@PathVariable String uid);

    @PutMapping("/mysql/user/modify/user_info/remote")
    @ApiOperation("更新用户信息")
    ResultEntity<String> modifyUserRemote(@RequestBody User user);

    @PutMapping("/mysql/user/modify/auth_info/remote")
    @ApiOperation("更新认证信息")
    ResultEntity<String> modifyAuthInfoRemote(UserAuthInfoVo userAuthInfoVo);

    @ApiOperation("查询用户动态")
    @GetMapping("/mysql/dynamic/get/dynamic/by/{userId}")
    ResultEntity<List<Dynamic>> getDynamicByUserId(@PathVariable String userId);

    // 项目远程接口

    @PostMapping("/mysql/project/save/project/remote")
    @ApiOperation("新建项目")
    ResultEntity<String> saveProjectRemote(@RequestBody Project project);

    @PostMapping("/mysql/project/get/project/pages/remote/{page}/{size}")
    @ApiOperation("分页带条件查询所有项目")
    ResultEntity<Page<SimpleProject>> getProjectPagesRemote(@PathVariable Long page,
                                                            @PathVariable Long size);

    @PostMapping("/mysql/project/get/project/pages/remote/{page}/{size}")
    @ApiOperation("分页带条件查询所有项目")
    ResultEntity<Page<SimpleProject>> getProjectPagesRemote(@PathVariable Long page,
                                                            @PathVariable Long size,
                                                            @RequestBody(required = false) SearchProjectVo searchProjectVo);

    @GetMapping("/mysql/project/get/project/detail/remote/{project_id}")
    @ApiOperation("根据id查询项目详细信息")
    ResultEntity<Project> getProjectByProjectIdRemote(@PathVariable("project_id") String projectId);

    @PutMapping("/mysql/project/modify/project/remote")
    @ApiOperation("根据id更新项目详细信息")
    ResultEntity<String> modifyProjectRemote(@RequestBody UpdateProjectVo updateProjectVo);

    @ApiOperation("更新项目支持者信息")
    @PostMapping("/mysql/project/modify/project/supporter/Info/{supportId}")
    public ResultEntity<String> modifyProjectSupporter(@PathVariable String supportId,
                                                       @RequestBody Project project);

    // 用户动态远程接口
    @PostMapping("/mysql/dynamic/save/dynamic")
    @ApiOperation("发布动态")
    ResultEntity<String> saveDynamic(@RequestBody Dynamic dynamic);

    @ApiOperation("用户删除动态")
    @DeleteMapping("/mysql/dynamic/remove/dynamic/{uid}/{dynamicId}")
    ResultEntity<String> removeDynamic(@PathVariable String uid, @PathVariable String dynamicId);

    // 动态
    @ApiOperation("查看所有动态")
    @GetMapping("/mysql/dynamic/get/all/dynamic")
    public ResultEntity<List<Dynamic>> getAllDynamic();

    // 用户收货地址管理
    @GetMapping("/mysql/shipping-address/get/shipping/address/{uid}")
    @ApiOperation("获取用户所有地址信息")
    ResultEntity<List<ShippingAddress>> getShippingAddress(@PathVariable String uid);

    @PutMapping("/mysql/shipping-address/modify/shipping/address")
    @ApiOperation("修改用户地址信息")
    ResultEntity<String> modifyShippingAddress(@RequestBody ShippingAddress shippingAddress);

    @PostMapping("/mysql/shipping-address/save/shipping/address")
    @ApiOperation("新建收货地址")
    ResultEntity<String> saveShippingAddress(@RequestBody ShippingAddress shippingAddress);

    @DeleteMapping("/mysql/shipping-address/remove/shipping/address/{addressId}")
    @ApiOperation("删除收货地址")
    ResultEntity<String> removeShippingAddress(@PathVariable String addressId);

    // 用户项目管理
    @ApiOperation("获取用户发起的项目")
    @GetMapping("/mysql/project/get/project/by/userId/remote/{uid}")
    public ResultEntity<List<Project>> getProjectByUserId(@PathVariable String uid);

    @DeleteMapping("/mysql/project/remove/project/{projectId}")
    @ApiOperation("用户项目删除操作")
    public ResultEntity<String> removeProjectById(@PathVariable String projectId);

    @ApiOperation("获取用户项目简要信息")
    @GetMapping("/mysql/project/get/user/simple/project/info/{uid}")
    public ResultEntity<Map<String, Object>> getUserSimpleProjectInfo(@PathVariable String uid);

    // 用户订单信息查看
    @ApiOperation("查询用户所有订单信息")
    @GetMapping("/mysql/pay_order/get/user/order/info/{uid}")
    public ResultEntity<List<PayOrderVo>> getUserOrderInfo(@PathVariable String uid,@RequestParam(required = false) String orderStatus);

    // 订单信息
    @ApiOperation("获取订单回报信息")
    @GetMapping("/mysql/pay_order/get/reward/remote/by/{rewardId}")
    public ResultEntity<Reward> getRewardByIdRemote(@PathVariable String rewardId);

    @ApiOperation("新建订单")
    @PostMapping("/mysql/pay_order/save/order/info")
    public ResultEntity<String> saveOrderInfo(@RequestBody PayOrder payOrder);

    @ApiOperation("获取订单详情")
    @GetMapping("/mysql/pay_order/get/order/info/detail/remote/{orderId}")
    public ResultEntity<PayOrder> getOrderInfoDetailRemote(@PathVariable String orderId);

    @ApiOperation("修改订单信息")
    @PostMapping("/mysql/pay_order/modify/order")
    public ResultEntity<String> modifyOrderInfoDetailRemote(@RequestBody PayOrder payOrder);

    @PostMapping("/mysql/pay_order/modify/reward/info/remote")
    @ApiOperation("更新回报数量")
    public ResultEntity<String> modifyReward(@RequestBody Reward reward);

    // 评论管理
    @ApiOperation(value = "根据查询对象获取评论列表")
    @GetMapping("/mysql/comments/getCommentList/{sourceId}/{sourceType}")
    public ResultEntity<Map<String, Object>> getCommentList(@PathVariable String sourceId,
                                                            @PathVariable String sourceType);


    @ApiOperation(value = "添加评论")
    @PostMapping("/mysql/comments/addComment")
    public ResultEntity<String> addComment(@RequestBody CommentFormVo commentFormVo);

    @ApiOperation(value = "根据评论编号删除评论信息")
    @DeleteMapping("/mysql/comments/removeComment/{commentId}/{uid}")
    public ResultEntity<String> removeComment(@PathVariable String commentId,
                                              @PathVariable String uid);

}
