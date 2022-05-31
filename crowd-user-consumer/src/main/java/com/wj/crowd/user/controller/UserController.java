package com.wj.crowd.user.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.wj.crowd.api.msm.MsmRemoteService;
import com.wj.crowd.api.mysql.MysqlRemoteService;
import com.wj.crowd.api.oss.OssRemoteService;
import com.wj.crowd.api.redis.CommonRedisRemoteService;
import com.wj.crowd.common.constant.CrowdConstant;
import com.wj.crowd.common.exception.CrowdException;
import com.wj.crowd.common.result.ResultCodeEnum;
import com.wj.crowd.common.result.ResultEntity;
import com.wj.crowd.common.utils.JwtHelper;
import com.wj.crowd.entity.Do.*;
import com.wj.crowd.entity.Vo.address.ShippingAddressVo;
import com.wj.crowd.entity.Vo.dynamic.DynamicVo;
import com.wj.crowd.entity.Vo.picture.PictureVo;
import com.wj.crowd.entity.Vo.project.ProjectDetailVo;
import com.wj.crowd.entity.Vo.project.ProjectVo;
import com.wj.crowd.entity.Vo.project.RewardVo;
import com.wj.crowd.entity.Vo.user.LoginAndRegisteredUserVo;
import com.wj.crowd.entity.Vo.user.SimpleUserVo;
import com.wj.crowd.entity.Vo.user.UserAuthInfoVo;
import com.wj.crowd.entity.Vo.user.UserVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author wj
 * @descript
 * @date 2022/5/8 - 16:47
 */
@RestController
@ApiOperation("用户模块")
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private MysqlRemoteService mysqlRemoteService;

    @Autowired
    private CommonRedisRemoteService commonRedisRemoteService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MsmRemoteService msmRemoteService;

    @Autowired
    private OssRemoteService ossRemoteService;


    @PostMapping("/do/login")
    @ApiOperation("密码登录登录")
    public ResultEntity<SimpleUserVo> doLogin(@RequestBody LoginAndRegisteredUserVo loginAndRegisteredUserVo) {
        // 前端数据验证
        String phone = loginAndRegisteredUserVo.getPhone();
        String password = loginAndRegisteredUserVo.getPassword();

        if (null == password || null == phone) {
            return ResultEntity.build(null, ResultCodeEnum.LOGIN_PHONE_ERROR);
        }

        // 查询用户信息
        ResultEntity<User> userByPhoneRemote = mysqlRemoteService.getUserByPhoneRemote(phone);
        if (!userByPhoneRemote.isSuccess()) {
            return ResultEntity.build(null, ResultCodeEnum.LOGIN_PHONE_OR_PASSWORD_ERROR);
        }

        User user = userByPhoneRemote.getData();
        if (null == user) {
            return ResultEntity.build(null, ResultCodeEnum.LOGIN_PHONE_OR_PASSWORD_ERROR);
        }

        // 进行密码校验
        String mysqlPassword = user.getPassword();

        if (!bCryptPasswordEncoder.matches(password, mysqlPassword)) {
            return ResultEntity.build(null, ResultCodeEnum.LOGIN_PHONE_OR_PASSWORD_ERROR);
        }

        // 返回登录账号基本信息和token
        SimpleUserVo simpleUserVo = new SimpleUserVo();
        BeanUtils.copyProperties(user, simpleUserVo);

        String token = JwtHelper.createToken(user.getUid(), user.getNickName());

        simpleUserVo.setToken(token);

        return ResultEntity.success(simpleUserVo);
    }

    @PostMapping("/do/login_or_register")
    @ApiOperation("手机登录注册")
    public ResultEntity<SimpleUserVo> doRegistered(@RequestBody LoginAndRegisteredUserVo loginAndRegisteredUserVo
    ) {
        // 数据验证
        String phone = loginAndRegisteredUserVo.getPhone();
        String code = loginAndRegisteredUserVo.getCode();
        if (null == phone || null == code) {
            return ResultEntity.build(null, ResultCodeEnum.LOGIN_DATA_ERROR);
        }

        // 校验验证码
        ResultEntity<String> redisCodeResult = commonRedisRemoteService.getRedisStringValueRemoteByKey(phone + CrowdConstant.REGISTER_AND_LOGIN_CODE);
        boolean success = redisCodeResult.isSuccess();
        if (!success) {
            return ResultEntity.build(null, ResultCodeEnum.NETWORK_ERROR);
        }
        String redisCode = redisCodeResult.getData();

        if (!Objects.equals(code, redisCode)) {
            return ResultEntity.build(null, ResultCodeEnum.CODE_ERROR);
        }

        // 判断是否需要进行注册操作
        ResultEntity<User> userByPhoneRemoteResult = mysqlRemoteService.getUserByPhoneRemote(phone);

        if (!userByPhoneRemoteResult.isSuccess()) {
            return ResultEntity.build(null, ResultCodeEnum.NETWORK_ERROR);
        }

        User user = userByPhoneRemoteResult.getData();

        // 第一次登录进行注册操作
        if (null == user) {
            user = new User();
            user.setPhone(phone);
            ResultEntity<String> stringResultEntity = mysqlRemoteService.saveUserRemote(user);
            if (!stringResultEntity.isSuccess()) {
                return ResultEntity.build(null, ResultCodeEnum.NETWORK_ERROR);
            }
            user = mysqlRemoteService.getUserByPhoneRemote(phone).getData();
        }


        // 返回用户信息和JWT生成的token
        SimpleUserVo simpleUserVo = new SimpleUserVo();
        BeanUtils.copyProperties(user, simpleUserVo);

        String token = JwtHelper.createToken(user.getUid(), user.getNickName());

        simpleUserVo.setToken(token);

        // 删除使用过的验证码
        ResultEntity<String> stringResultEntity = commonRedisRemoteService.removeRedisKeyRemote(phone + CrowdConstant.REGISTER_AND_LOGIN_CODE);
        if (!stringResultEntity.isSuccess()) {
            throw new CrowdException(ResultCodeEnum.NETWORK_ERROR);
        }
        return ResultEntity.success(simpleUserVo);

    }

    @GetMapping("/send/code/{phone}/{type}")
    @ApiOperation("发送验证码")
    public ResultEntity<String> sendCode(@PathVariable String phone, @PathVariable String type) {

        if (Objects.equals(type, "login")) {
            ResultEntity<String> stringResultEntity = msmRemoteService.sendCodeRemote(phone, CrowdConstant.REGISTER_AND_LOGIN_CODE);
            if (!stringResultEntity.isSuccess()) {
                return ResultEntity.build(null, ResultCodeEnum.NETWORK_ERROR);
            }
        }
        if (Objects.equals(type, "modify")) {
            ResultEntity<String> stringResultEntity = msmRemoteService.sendCodeRemote(phone, CrowdConstant.MODIFY_CODE);
            if (!stringResultEntity.isSuccess()) {
                return ResultEntity.build(null, ResultCodeEnum.NETWORK_ERROR);
            }
        }

        return ResultEntity.success();
    }

    @PutMapping("/modify/password")
    @ApiOperation("修改密码")
    public ResultEntity<String> modifyPassword(HttpServletRequest request,
                                               @RequestBody Map<String,String> params) {

        // 从token中获取userId
        String token = request.getHeader("token");
        String userId = JwtHelper.getUserId(token);

        // 查询用户信息
        ResultEntity<User> userByUidRemote = mysqlRemoteService.getUserByUidRemote(userId);
        if (!userByUidRemote.isSuccess()) {
            return ResultEntity.build(null, ResultCodeEnum.NETWORK_ERROR);
        }

        User user = userByUidRemote.getData();
        if (null == user) {
            return ResultEntity.build(null, ResultCodeEnum.NETWORK_ERROR);
        }
        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");

        // 进行密码校验
        String password = user.getPassword();
        if (!bCryptPasswordEncoder.matches(oldPassword, password)) {
            return ResultEntity.fail(CrowdConstant.PASSWORD_ERROR);
        }

        // 重置密码
        newPassword = bCryptPasswordEncoder.encode(newPassword);
        user.setPassword(newPassword);
        ResultEntity<String> stringResultEntity = mysqlRemoteService.modifyUserRemote(user);
        if (!stringResultEntity.isSuccess()) {
            return ResultEntity.build(null, ResultCodeEnum.NETWORK_ERROR);
        }
        return ResultEntity.success();

    }

    @PostMapping("/confirm/modify_code")
    @ApiOperation("验证修改操作验证码")
    public ResultEntity<String> confirmModifyCode(HttpServletRequest request,
                                                  @RequestBody Map<String,String> params) {

        // 从token中获取userId
        String token = request.getHeader("token");
        String userId = JwtHelper.getUserId(token);

        String phone = params.get("phone");
        String code = params.get("code");

        // 获取redis中存储的验证码
        ResultEntity<String> redisStringValueRemoteByKey = commonRedisRemoteService.getRedisStringValueRemoteByKey(phone + CrowdConstant.MODIFY_CODE);
        if (!redisStringValueRemoteByKey.isSuccess()) {
            return ResultEntity.build(null, ResultCodeEnum.NETWORK_ERROR);
        }
        String modifyCode = redisStringValueRemoteByKey.getData();
        if (null == modifyCode) {
            return ResultEntity.build(null, ResultCodeEnum.CODE_ERROR);
        }

        // 进行验证码比对
        if (!Objects.equals(code, modifyCode)) {
            return ResultEntity.build(null, ResultCodeEnum.CODE_ERROR);
        }

        // 更新验证码比对结果并移除验证码
        ResultEntity<String> stringResultEntity = commonRedisRemoteService.setRedisKeyValueRemote(userId + CrowdConstant.MODIFY_CODE, CrowdConstant.SUCCESS);
        commonRedisRemoteService.removeRedisKeyRemote(phone + CrowdConstant.MODIFY_CODE);

        if (!stringResultEntity.isSuccess()) {
            return ResultEntity.build(null, ResultCodeEnum.NETWORK_ERROR);
        }

        return ResultEntity.success();

    }

    @PostMapping("/modify/password")
    @ApiOperation("通过手机验证码修改密码")
    public ResultEntity<String> modifyPasswordByPhone(HttpServletRequest request,
                                               @RequestBody Map<String,String> params) {
        // 从token中获取userId
        String token = request.getHeader("token");
        String userId = JwtHelper.getUserId(token);

        String newPassword = params.get("newPassword");
        // 判断验证码验证是否通过
        ResultEntity<String> redisStringValueRemoteByKey = commonRedisRemoteService.getRedisStringValueRemoteByKey(userId + CrowdConstant.MODIFY_CODE);
        if (!redisStringValueRemoteByKey.isSuccess()) {
            return ResultEntity.build(null, ResultCodeEnum.NETWORK_ERROR);
        }

        String confirmResult = redisStringValueRemoteByKey.getData();
        if (!CrowdConstant.SUCCESS.equals(confirmResult)) {
            return ResultEntity.build(null, ResultCodeEnum.DATA_ERROR);
        }

        // 更新密码
        ResultEntity<User> userByUidRemote = mysqlRemoteService.getUserByUidRemote(userId);
        if (!userByUidRemote.isSuccess()) {
            return ResultEntity.build(null, ResultCodeEnum.NETWORK_ERROR);
        }
        User user = userByUidRemote.getData();
        if (null == user) {
            return ResultEntity.build(null, ResultCodeEnum.DATA_ERROR);
        }
        // 密码加密
        newPassword = bCryptPasswordEncoder.encode(newPassword);
        user.setPassword(newPassword);

        ResultEntity<String> stringResultEntity = mysqlRemoteService.modifyUserRemote(user);
        if (!stringResultEntity.isSuccess()) {
            return ResultEntity.build(null, ResultCodeEnum.NETWORK_ERROR);
        }
        return ResultEntity.success();
    }


    @GetMapping("/get/dynamic/by/user_id")
    @ApiOperation("查询用户动态")
    public ResultEntity<List<DynamicVo>> getDynamicByUserId(HttpServletRequest request) {

        // 从token中获取userId
        String token = request.getHeader("token");
        String userId = JwtHelper.getUserId(token);

        // 远程调用查询用户动态接口
        ResultEntity<List<Dynamic>> dynamicResult = mysqlRemoteService.getDynamicByUserId(userId);
        if (!dynamicResult.isSuccess()) {
            return ResultEntity.build(null, ResultCodeEnum.NETWORK_ERROR);
        }

        // 数据转换
        List<Dynamic> dynamicList = dynamicResult.getData();

        if (dynamicList != null && dynamicList.size() > 0) {

            List<DynamicVo> dynamicVos = new ArrayList<>();
            dynamicList.forEach(dynamic -> {
                DynamicVo dynamicVo = new DynamicVo();
                BeanUtils.copyProperties(dynamic, dynamicVo);
                List<Picture> pictureList = dynamic.getPictureList();
                if (null != pictureList && pictureList.size() > 0) {
                    List<PictureVo> pictureVos = new ArrayList<>();
                    pictureList.forEach(picture -> {
                        PictureVo pictureVo = new PictureVo();
                        BeanUtils.copyProperties(picture, pictureVo);
                        pictureVos.add(pictureVo);
                    });
                    dynamicVo.setPictureList(pictureVos);
                }
                dynamicVos.add(dynamicVo);
            });
            return ResultEntity.success(dynamicVos);
        }
        return ResultEntity.success();
    }

    @ApiOperation("删除动态")
    @DeleteMapping("/remove/dynamic/{dynamic_id}")
    public ResultEntity<String> removeDynamic(@PathVariable("dynamic_id") String dynamicId,
                                              HttpServletRequest request) {
        // 从token中获取userId
        String token = request.getHeader("token");
        String userId = JwtHelper.getUserId(token);

        // 调用远程接口
        ResultEntity<String> stringResultEntity = mysqlRemoteService.removeDynamic(userId, dynamicId);

        if (!stringResultEntity.isSuccess()) {
            return ResultEntity.build(null, ResultCodeEnum.NETWORK_ERROR);
        }
        return ResultEntity.success();
    }

    // 用户认证
    @ApiOperation("用户认证")
    @PostMapping("/do/user/auth")
    public ResultEntity<String> userAuth(@RequestBody UserAuthInfoVo userAuthInfoVo,
                                         HttpServletRequest request) {
        // 数据校验
        if (null == userAuthInfoVo) {
            return ResultEntity.build(null, ResultCodeEnum.DATA_ERROR);
        }

        // 从token中获取userId
        String token = request.getHeader("token");
        String userId = JwtHelper.getUserId(token);

        // 更新用户认证信息
        userAuthInfoVo.setUid(userId);
        // 调用远程接口更新认证信息
        ResultEntity<String> stringResultEntity = mysqlRemoteService.modifyAuthInfoRemote(userAuthInfoVo);
        if (!stringResultEntity.isSuccess()) {
            return ResultEntity.build(stringResultEntity.getMessage(), ResultCodeEnum.NETWORK_ERROR);
        }
        return ResultEntity.success();
    }

    // 上传用户认证照片
    @PostMapping(value = "/upload/picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation("上传用户认证照片")
    public ResultEntity<String> uploadPicture(MultipartFile file) {


        // 调用远程接口上传图片
        ResultEntity<String> stringResultEntity = ossRemoteService.fileUpload(file);
        // 结果处理
        if (!stringResultEntity.isSuccess()) {
            return ResultEntity.build(stringResultEntity.getMessage(), ResultCodeEnum.NETWORK_ERROR);
        }
        String pictureUrl = stringResultEntity.getData();
        if (null == pictureUrl) {
            return ResultEntity.build(null, ResultCodeEnum.NETWORK_ERROR);
        }

        return ResultEntity.success(pictureUrl);
    }

    // 查询用户认证信息
    @GetMapping("/get/user/auth/info")
    @ApiOperation("查询用户认证信息")
    public ResultEntity<UserAuthInfoVo> getUserAuthInfo(HttpServletRequest request) {
        // 从token中获取userId
        String token = request.getHeader("token");
        String userId = JwtHelper.getUserId(token);

        // 调用远程接口查询查询认证信息
        ResultEntity<User> userByUidRemote = mysqlRemoteService.getUserByUidRemote(userId);

        // 结果处理
        if (!userByUidRemote.isSuccess()) {
            return ResultEntity.fail();
        }
        User user = userByUidRemote.getData();

        if (null != user) {
            UserAuthInfoVo userAuthInfoVo = new UserAuthInfoVo();
            BeanUtils.copyProperties(user, userAuthInfoVo);
            // 认证信息模糊处理
            String realName = userAuthInfoVo.getRealName();
            String idNumber = userAuthInfoVo.getIdNumber();
            String realNameFirst = realName.substring(0, 1);
            String realNameLast = realName.substring(1);
            realNameLast = realNameLast.replaceAll("[一-龟]", "*");

            String idNumberFirst = idNumber.substring(0, 3);
            String idNumberLast = idNumber.substring(3);
            idNumberLast = idNumber.replaceAll("\\d", "*");

            userAuthInfoVo.setFrontIdPicture(null);
            userAuthInfoVo.setRealName(realNameFirst + realNameLast);
            userAuthInfoVo.setIdNumber(idNumberFirst + idNumberLast);
            return ResultEntity.success(userAuthInfoVo);
        }

        return ResultEntity.fail();
    }

    // 查询用户信息
    @ApiOperation("查询用户信息")
    @GetMapping("/get/user/info")
    public ResultEntity<UserVo> getUserInfo(HttpServletRequest request) {
        // 从token中获取userId
        String token = request.getHeader("token");
        String userId = JwtHelper.getUserId(token);

        ResultEntity<User> userByUidRemote = mysqlRemoteService.getUserByUidRemote(userId);

        // 结果处理
        if (!userByUidRemote.isSuccess()) {
            return ResultEntity.fail();
        }
        User user = userByUidRemote.getData();

        if(null==user){
            return ResultEntity.fail();
        }

        // vo对象封装以及擦除不必要的信息

        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user,userVo);

        userVo.setPassword("");
        return ResultEntity.success(userVo);
    }

    // 修改用户信息
    @PutMapping("/modify/use/info")
    @ApiOperation("修改用户信息")
    public ResultEntity<String> modifyUserInfo(@RequestBody UserVo userVo,
                                               HttpServletRequest request){
        // 从token中获取userId
        String token = request.getHeader("token");
        String userId = JwtHelper.getUserId(token);

        User user = new User();
        BeanUtils.copyProperties(userVo,user);
        user.setUid(userId);

        // 结果处理
        ResultEntity<String> stringResultEntity = mysqlRemoteService.modifyUserRemote(user);
        if(!stringResultEntity.isSuccess()){
            return ResultEntity.build(stringResultEntity.getMessage(),ResultCodeEnum.NETWORK_ERROR);
        }
        return ResultEntity.success(stringResultEntity.getMessage());

    }

    // 用户地址管理
    @GetMapping("/get/shipping/address")
    @ApiOperation("获取用户所有地址信息")
    public ResultEntity<List<ShippingAddressVo>> getShippingAddress(HttpServletRequest request){
        // 从token中获取userId
        String token = request.getHeader("token");
        String userId = JwtHelper.getUserId(token);
        ResultEntity<List<ShippingAddress>> shippingAddressResult = mysqlRemoteService.getShippingAddress(userId);
        if(!shippingAddressResult.isSuccess()){
            return ResultEntity.fail();
        }
        List<ShippingAddress> shippingAddressList = shippingAddressResult.getData();

        // 数据转换
        List<ShippingAddressVo> shippingAddressVos = new ArrayList<>();
        if(null!=shippingAddressList&&shippingAddressList.size()!=0){
            shippingAddressList.forEach(shippingAddress -> {
                ShippingAddressVo shippingAddressVo = new ShippingAddressVo();
                BeanUtils.copyProperties(shippingAddress,shippingAddressVo);
                shippingAddressVos.add(shippingAddressVo);
            });
        }
        return ResultEntity.success(shippingAddressVos);
    }

    @PutMapping("/modify/shipping/address")
    @ApiOperation("修改用户地址信息")
    public ResultEntity<String> modifyShippingAddress(@RequestBody ShippingAddressVo shippingAddressVo){
        if(null==shippingAddressVo){
            return ResultEntity.build(null,ResultCodeEnum.DATA_ERROR);
        }

        // 数据转换
        ShippingAddress shippingAddress = new ShippingAddress();
        BeanUtils.copyProperties(shippingAddressVo,shippingAddress);

        // 调用远程接口

        ResultEntity<String> stringResultEntity = mysqlRemoteService.modifyShippingAddress(shippingAddress);
        if(!stringResultEntity.isSuccess()){
            return ResultEntity.build(stringResultEntity.getMessage(),ResultCodeEnum.NETWORK_ERROR);
        }

        return ResultEntity.success();
    }
    @PostMapping("/save/shipping/address")
    @ApiOperation("新建收货地址")
    public ResultEntity<String> saveShippingAddress(HttpServletRequest request,
                                                    @RequestBody ShippingAddressVo shippingAddressVo){
        if(null==shippingAddressVo){
            return ResultEntity.build(null,ResultCodeEnum.DATA_ERROR);
        }

        // 从token中获取userId
        String token = request.getHeader("token");
        String userId = JwtHelper.getUserId(token);

        // 数据转换
        ShippingAddress shippingAddress = new ShippingAddress();
        BeanUtils.copyProperties(shippingAddressVo,shippingAddress);

        shippingAddress.setUid(userId);
        // 调用远程接口
        ResultEntity<String> stringResultEntity = mysqlRemoteService.saveShippingAddress(shippingAddress);
        if(!stringResultEntity.isSuccess()){
            return ResultEntity.build(stringResultEntity.getMessage(),ResultCodeEnum.NETWORK_ERROR);
        }

        return ResultEntity.success();
    }

    @DeleteMapping("/remove/shipping/address/{addressId}")
    @ApiOperation("删除收货地址")
    public ResultEntity<String> removeShippingAddress(@PathVariable String addressId){
        // 调用远程接口
        ResultEntity<String> stringResultEntity = mysqlRemoteService.removeShippingAddress(addressId);
        if(!stringResultEntity.isSuccess()){
            return ResultEntity.build(stringResultEntity.getMessage(),ResultCodeEnum.NETWORK_ERROR);
        }

        return ResultEntity.success();
    }

    @GetMapping("/get/user/project")
    @ApiOperation("获取用户发起的项目信息")
    public ResultEntity<List<ProjectVo>> getUserProject(HttpServletRequest request){
        String token = request.getHeader("token");
        String userId = JwtHelper.getUserId(token);

        // 获取用户未完成的项目
        ResultEntity<List<Object>> redisAllHashByKeyRemote = commonRedisRemoteService.getRedisAllHashByKeyRemote(userId + CrowdConstant.TEMPORARY_PROJECT);
        if(!redisAllHashByKeyRemote.isSuccess()){
            return ResultEntity.fail();
        }
        List<Object> projectData = redisAllHashByKeyRemote.getData();
        List<ProjectVo> projectVos = new ArrayList<>();
        if(projectData!=null){

            projectData.forEach(data->{
                Project project = JSON.parseObject(JSON.toJSONString(data), new TypeReference<Project>() {
                });
                ProjectVo projectVo = projectToProjectVo(project);
                projectVos.add(projectVo);
            });
        }
        // 从数据库中获取已经发布的和正在审核的项目
        ResultEntity<List<Project>> projectByUserId = mysqlRemoteService.getProjectByUserId(userId);
        if(!projectByUserId.isSuccess()){
            return ResultEntity.fail();
        }
        List<Project> projectList = projectByUserId.getData();
        if(null!=projectList&&projectList.size()!=0){
            projectList.forEach(project -> {
                ProjectVo projectVo = projectToProjectVo(project);
                projectVos.add(projectVo);
            });
        }
        return ResultEntity.success(projectVos);
    }

    @DeleteMapping("remove/project/{projectId}")
    @ApiOperation("用户项目删除操作")
    public ResultEntity<String> removeProjectById(@PathVariable String projectId){
        return mysqlRemoteService.removeProjectById(projectId);
    }


    private ProjectVo projectToProjectVo(Project project) {
        ProjectVo projectVo = new ProjectVo();
        BeanUtils.copyProperties(project, projectVo);
        List<Reward> rewards = project.getRewards();
        // 项目回报
        if (null != rewards && rewards.size() != 0) {
            List<RewardVo> rewardVos = new ArrayList<>();
            rewards.forEach(reward -> {
                RewardVo rewardVo = new RewardVo();
                BeanUtils.copyProperties(reward, rewardVo);

                List<Picture> pictures = reward.getPicture();

                if (null != pictures && pictures.size() != 0) {
                    List<PictureVo> pictureVos = new ArrayList<>();

                    pictures.forEach(picture -> {
                        PictureVo pictureVo = new PictureVo();
                        BeanUtils.copyProperties(picture, pictureVo);
                        pictureVos.add(pictureVo);
                    });
                    rewardVo.setPictureVos(pictureVos);
                }
                rewardVos.add(rewardVo);
            });
            projectVo.setRewardVos(rewardVos);
            // 项目详情
            List<ProjectDetail> projectDetails = project.getProjectDetails();
            if(null!=projectDetails&&projectDetails.size()!=0){
                List<ProjectDetailVo> projectDetailVoList = new ArrayList<>();
                projectDetails.forEach(projectDetail -> {
                    ProjectDetailVo projectDetailVo = new ProjectDetailVo();
                    BeanUtils.copyProperties(projectDetail,projectDetailVo);
                    projectDetailVoList.add(projectDetailVo);
                });
                projectVo.setProjectDetailVos(projectDetailVoList);
            }

            // 项目辅助资料
            List<Picture> projectSupportingList = project.getProjectSupportingList();
            if(null!=projectSupportingList&&projectSupportingList.size()!=0){
                List<PictureVo> projectSupportingVoList = new ArrayList<>();
                projectSupportingList.forEach(projectSupporting->{
                    PictureVo projectSupportingVo = new PictureVo();
                    BeanUtils.copyProperties(projectSupporting,projectSupportingVo);
                    projectSupportingVoList.add(projectSupportingVo);
                });
                projectVo.setProjectSupportingVoList(projectSupportingVoList);
            }
        }
        return projectVo;
    }

}
