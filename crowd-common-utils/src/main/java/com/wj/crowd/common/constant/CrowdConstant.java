package com.wj.crowd.common.constant;

/**
 * @author wj
 * @descript
 * @date 2022/5/9 - 15:57
 */
public class CrowdConstant {

    public static final String REGISTER_AND_LOGIN_CODE = "REGISTER_AND_LOGIN_CODE";
    public static final String MODIFY_CODE = "MODIFY_CODE";
    public static final String PASSWORD_ERROR = "密码错误";
    public static final String SUCCESS = "SUCCESS";
    public static final String PICTURE_TYPE_DYNAMIC = "dynamic";
    public static final String TEMPORARY_PROJECT = "TEMPORARY_PROJECT";
    public static final String PICTURE_TYPE_REWARD = "reward";
    public static final String PICTURE_TYPE_SUPPORTING = "supporting";
    // 订单状态

    public static final Integer ORDER_STATUS_UNPAID =1;
    public static final Integer ORDER_STATUS_CANCEL =0;
    public static final Integer ORDER_STATUS_PENDING_SHIPMENT=2;
    public static final String COMMENT_TYPE_PROJECT = "project";
    // 项目查询条件
    public static final String PROJECT_SORT_METHOD_TIME = "最近上线";
    public static final String PROJECT_ORDER_BY_TIME= "start_time";

    public static final String PROJECT_SORT_METHOD_MONEY = "金额最多";
    public static final String PROJECT_ORDER_BY_MONEY = "support_money";
    public static final String PROJECT_SORT_METHOD_COMMENT = "评论最多";
    public static final String PROJECT_ORDER_BY_COMMENT = "comment_number";

    public static final String PROJECT_TYPE_ALL = "全部";

    public static final String PROJECT_STATUS_ING = "众筹中";
    public static final String PROJECT_STATUS_ING_NUMBER = "1";

    public static final String PROJECT_STATUS_SUCCESS = "众筹成功";
    public static final String PROJECT_STATUS_SUCCESS_NUMBER = "2";
}
