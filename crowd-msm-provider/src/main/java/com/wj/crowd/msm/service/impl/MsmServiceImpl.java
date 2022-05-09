package com.wj.crowd.msm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.wj.crowd.common.constant.CrowdConstant;
import com.wj.crowd.common.exception.CrowdException;
import com.wj.crowd.common.result.ResultCodeEnum;
import com.wj.crowd.common.utils.RandomUtil;
import com.wj.crowd.msm.config.MsmProperties;
import com.wj.crowd.msm.service.api.MsmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author wj
 * @descript
 * @date 2022/5/8 - 19:55
 */
@Service
public class MsmServiceImpl implements MsmService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private MsmProperties msmProperties;

    /**
     * 发送短信验证码
     *
     * @param phone 手机号
     */
    @Override
    public void sendCode(String phone, String type) {
        // 判断redis中是否已经存在过验证码
        ValueOperations<String, String> stringStringValueOperations = redisTemplate.opsForValue();
        String code = stringStringValueOperations.get(phone + type);
        if (null != code) {
            // 获取过期时间
            Long expireTime = stringStringValueOperations.getOperations().getExpire(phone);
            // 过期时间大于1分钟、直接发送原code
            if (expireTime != null && expireTime < 60) {
                // 过期时间小于1分钟、重新生成code发送
                code = RandomUtil.getSixBitRandom();
            }
        } else {
            code = RandomUtil.getSixBitRandom();
        }

        // 发送验证码
        Boolean sendResult = this.send(phone, code);
        if (!sendResult) {
            throw new CrowdException(ResultCodeEnum.SEND_CODE_ERROR);
        }

        // 将验证码存入redis中 设置过期时间5分钟
        stringStringValueOperations.set(phone + type, code, 5, TimeUnit.MINUTES);

    }


    /**
     * @param phone
     * @param code
     */
    private Boolean send(String phone, String code) {
        //整合阿里云短信服务
        //设置相关参数
        DefaultProfile profile = DefaultProfile.
                getProfile(msmProperties.getRegionId(),
                        msmProperties.getAccessKeyId(),
                        msmProperties.getSecret());
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        //手机号
        request.putQueryParameter("PhoneNumbers", phone);
        //签名名称
        request.putQueryParameter("SignName", "阿里云短信测试");
        //模板code
        request.putQueryParameter("TemplateCode", "SMS_154950909");
        //验证码  使用json格式   {"code":"123456"}
        Map<String, Object> param = new HashMap<>();
        param.put("code", code);
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param));

        //调用方法进行短信发送
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            return response.getHttpResponse().isSuccess();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }
}
