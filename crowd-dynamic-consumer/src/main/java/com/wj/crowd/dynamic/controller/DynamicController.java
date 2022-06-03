package com.wj.crowd.dynamic.controller;

import com.wj.crowd.api.mysql.MysqlRemoteService;
import com.wj.crowd.api.oss.OssRemoteService;
import com.wj.crowd.common.constant.CrowdConstant;
import com.wj.crowd.common.result.ResultCodeEnum;
import com.wj.crowd.common.result.ResultEntity;
import com.wj.crowd.common.utils.JwtHelper;
import com.wj.crowd.entity.Do.Dynamic;
import com.wj.crowd.entity.Do.Picture;
import com.wj.crowd.entity.Do.User;
import com.wj.crowd.entity.Vo.comment.CommentFormVo;
import com.wj.crowd.entity.Vo.dynamic.DynamicVo;
import com.wj.crowd.entity.Vo.picture.PictureVo;
import com.wj.crowd.entity.Vo.user.SimpleUserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @descript
 * @date 2022/5/24 - 17:27
 */
@RestController
@RequestMapping("/dynamic")
@Api("动态模块")
@CrossOrigin
public class DynamicController {

    @Autowired
    private OssRemoteService ossRemoteService;

    @Autowired
    private MysqlRemoteService mysqlRemoteService;

    @PostMapping(value = "/upload/picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation("上传动态图片")
    public ResultEntity<String> uploadPicture(HttpServletRequest request) {

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        //file是form-data中二进制字段对应的name
        MultipartFile file = multipartRequest.getFile("file");

        ResultEntity<String> stringResultEntity = ossRemoteService.fileUpload(file);
        if (!stringResultEntity.isSuccess()) {
            return ResultEntity.build(null, ResultCodeEnum.NETWORK_ERROR);
        }
        String pictureUrl = stringResultEntity.getData();
        if (null == pictureUrl) {
            return ResultEntity.build(null, ResultCodeEnum.NETWORK_ERROR);
        }

        return ResultEntity.success(pictureUrl);
    }

    @ApiOperation("发布动态")
    @PostMapping("/save/dynamic")
    public ResultEntity<String> saveDynamic(@RequestBody DynamicVo dynamicVo, HttpServletRequest request) {

        String token = request.getHeader("token");
        String userId = JwtHelper.getUserId(token);

        Dynamic dynamic = new Dynamic();
        BeanUtils.copyProperties(dynamicVo, dynamic);

        List<Picture> pictureList = new ArrayList<>();
        for (PictureVo pictureVo : dynamicVo.getPictureList()) {
            Picture picture = new Picture();
            picture.setPicturePath(pictureVo.getPicturePath());
            pictureList.add(picture);
        }
        dynamic.setPictureList(pictureList);
        dynamic.setUid(userId);

        ResultEntity<String> stringResultEntity = mysqlRemoteService.saveDynamic(dynamic);
        if (!stringResultEntity.isSuccess()) {
            return ResultEntity.build(null, ResultCodeEnum.NETWORK_ERROR);
        }
        return ResultEntity.success();
    }

    // 发布动态评论
    @PostMapping("/add/comment/to/dynamic")
    @ApiOperation("发布动态评论")
    public ResultEntity<String> addCommentToDynamic(@RequestBody CommentFormVo commentFormVo, HttpServletRequest request) {
        String token = request.getHeader("token");
        String userId = JwtHelper.getUserId(token);
        commentFormVo.setSourceType(CrowdConstant.COMMENT_TYPE_DYNAMIC);
        ResultEntity<String> stringResultEntity = mysqlRemoteService.addComment(commentFormVo);
        if (!stringResultEntity.isSuccess()) {
            return ResultEntity.fail(stringResultEntity.getMessage());
        }
        return ResultEntity.success();
    }

    @GetMapping("/get/comments/by/{dynamicId}")
    @ApiOperation("获取动态评论")
    public ResultEntity<Map<String, Object>> getCommentsByDynamicId(@PathVariable String dynamicId, HttpServletRequest request) {
        String token = request.getHeader("token");
        String userId = JwtHelper.getUserId(token);
        ResultEntity<Map<String, Object>> stringResultEntity = mysqlRemoteService.getCommentList(dynamicId, CrowdConstant.COMMENT_TYPE_DYNAMIC);
        if (!stringResultEntity.isSuccess()) {
            return ResultEntity.fail();
        }
        return ResultEntity.success(stringResultEntity.getData());
    }

    @ApiOperation("查看所有动态")
    @GetMapping("/get/all/dynamic")
    public ResultEntity<List<DynamicVo>> getAllDynamic() {
        ResultEntity<List<Dynamic>> allDynamic = mysqlRemoteService.getAllDynamic();

        if (!allDynamic.isSuccess()) {
            return ResultEntity.fail();
        }

        List<Dynamic> dynamicList = allDynamic.getData();
        if (dynamicList != null && dynamicList.size() > 0) {

            List<DynamicVo> dynamicVos = new ArrayList<>();
            dynamicList.forEach(dynamic -> {
                DynamicVo dynamicVo = new DynamicVo();
                BeanUtils.copyProperties(dynamic, dynamicVo);
                // 封装图片Vo对象
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
                // 封装发起人Vo对象
                SimpleUserVo simpleUserVo = new SimpleUserVo();
                User user = dynamic.getUser();
                BeanUtils.copyProperties(user,simpleUserVo);
                dynamicVo.setUser(simpleUserVo);
                dynamicVos.add(dynamicVo);
            });

            return ResultEntity.success(dynamicVos);

        }
        return ResultEntity.success();

    }
}
