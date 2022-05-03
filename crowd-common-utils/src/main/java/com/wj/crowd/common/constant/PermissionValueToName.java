package com.wj.crowd.common.constant;


import java.util.ArrayList;
import java.util.List;

/**
 * @author wj
 * @descript
 * @date 2022/4/30 - 16:26
 */

public class PermissionValueToName {
    public static final String GET ="查询";
    public static final String ADD ="添加";
    public static final String UPDATE ="修改";
    public static final String DELETE ="删除";

    public static final String SELECT = "get";
    public static final String SAVE = "add";
    public static final String MODIFY = "update";
    public static final String REMOVE = "delete";



    public static String getName(String value){
        if("GET".equals(value)){
            return GET;
        }if("ADD".equals(value)){
            return ADD;
        }if("UPDATE".equals(value)){
            return UPDATE;
        }if("DELETE".equals(value)){
            return DELETE;
        }
        return null;
    }

    public static List<String> getSufPermission(){
        List<String> sufPermissions = new ArrayList<>();
        sufPermissions.add(PermissionValueToName.SELECT);
        sufPermissions.add(PermissionValueToName.SAVE);
        sufPermissions.add(PermissionValueToName.MODIFY);
        sufPermissions.add(PermissionValueToName.REMOVE);
        return sufPermissions;
    }
}
