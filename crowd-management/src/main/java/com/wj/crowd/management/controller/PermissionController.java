package com.wj.crowd.management.controller;


import com.wj.crowd.common.result.ResultEntity;
import com.wj.crowd.management.entity.Vo.PermissionVo;
import com.wj.crowd.management.service.api.PermissionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author w
 * @since 2022-04-20
 */
@RestController
@RequestMapping("/management/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;





}

