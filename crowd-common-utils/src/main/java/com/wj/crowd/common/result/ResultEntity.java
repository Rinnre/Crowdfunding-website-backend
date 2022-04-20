package com.wj.crowd.common.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 全局统一返回结果类
 */
@Data
@ApiModel(value = "全局统一返回结果")
public class ResultEntity<T> {

    @ApiModelProperty(value = "返回码")
    private Integer code;

    @ApiModelProperty(value = "返回消息")
    private String message;

    @ApiModelProperty(value = "返回数据")
    private T data;

    public ResultEntity(){}

    protected static <T> ResultEntity<T> build(T data) {
        ResultEntity<T> resultEntity = new ResultEntity<T>();
        if (data != null)
            resultEntity.setData(data);
        return resultEntity;
    }

    public static <T> ResultEntity<T> build(T body, ResultCodeEnum resultCodeEnum) {
        ResultEntity<T> resultEntity = build(body);
        resultEntity.setCode(resultCodeEnum.getCode());
        resultEntity.setMessage(resultCodeEnum.getMessage());
        return resultEntity;
    }

    public static <T> ResultEntity<T> build(Integer code, String message) {
        ResultEntity<T> resultEntity = build(null);
        resultEntity.setCode(code);
        resultEntity.setMessage(message);
        return resultEntity;
    }

    public static<T> ResultEntity<T> success(){
        return ResultEntity.success(null);
    }

    /**
     * 操作成功
     * @param data
     * @param <T>
     * @return
     */
    public static<T> ResultEntity<T> success(T data){
        ResultEntity<T> resultEntity = build(data);
        return build(data, ResultCodeEnum.SUCCESS);
    }

    public static<T> ResultEntity<T> fail(){
        return ResultEntity.fail(null);
    }

    /**
     * 操作失败
     * @param data
     * @param <T>
     * @return
     */
    public static<T> ResultEntity<T> fail(T data){
        ResultEntity<T> resultEntity = build(data);
        return build(data, ResultCodeEnum.FAIL);
    }

    public ResultEntity<T> message(String msg){
        this.setMessage(msg);
        return this;
    }

    public ResultEntity<T> code(Integer code){
        this.setCode(code);
        return this;
    }

    public boolean isSuccess() {
        if(this.getCode().intValue() == ResultCodeEnum.SUCCESS.getCode().intValue()) {
            return true;
        }
        return false;
    }
}
