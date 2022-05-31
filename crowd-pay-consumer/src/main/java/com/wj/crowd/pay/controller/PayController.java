package com.wj.crowd.pay.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.wj.crowd.api.mysql.MysqlRemoteService;
import com.wj.crowd.common.constant.CrowdConstant;
import com.wj.crowd.common.exception.CrowdException;
import com.wj.crowd.common.result.ResultCodeEnum;
import com.wj.crowd.common.result.ResultEntity;
import com.wj.crowd.entity.Do.PayOrder;
import com.wj.crowd.entity.Do.Project;
import com.wj.crowd.entity.Vo.project.UpdateProjectVo;
import com.wj.crowd.pay.config.PayProperties;
import com.wj.crowd.pay.util.WebSocket;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * @author wj
 * @descript
 * @date 2022/5/31 - 14:32
 */
@ApiOperation("支付模块")
@RestController
@RequestMapping("/pay")
@Slf4j
public class PayController {
    @Autowired
    private WebSocket webSocket;    // 导入刚刚写好的 WebSocket 工具类

    @Autowired
    private PayProperties payProperties;

    @Autowired
    private MysqlRemoteService mysqlRemoteService;

    @ApiOperation(value = "支付宝支付 沙箱环境")
    @PostMapping("/send/request/to/pay/{order_id}")
    public ResultEntity<String> sendRequestToAliPay(@PathVariable("order_id") String orderId) throws AlipayApiException {
        //调用远程接口获订单信息
        ResultEntity<PayOrder> orderInfoDetailRemote = mysqlRemoteService.getOrderInfoDetailRemote(orderId);

        if(!orderInfoDetailRemote.isSuccess()){
            throw new CrowdException(ResultCodeEnum.DATA_ERROR);
        }

        PayOrder order = orderInfoDetailRemote.getData();

        //获得初始化的 AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(
                payProperties.getGatewayUrl(),
                payProperties.getAppId(),
                payProperties.getMerchantPrivateKey(),
                "json",
                payProperties.getCharset(),
                payProperties.getAlipayPublicKey(),
                payProperties.getSignType());

        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
        request.setBizModel(model);
        model.setOutTradeNo(UUID.randomUUID()+"");
        model.setTotalAmount("88.88");
        model.setSubject("Iphone6 16G");
        AlipayTradePrecreateResponse response = alipayClient.execute(request);
        log.info(response.toString());
        // 返回支付宝支付网址，用于生成二维码
        return ResultEntity.success(response.getQrCode());
    }

    @ApiOperation(value = "支付宝支付 异步通知")
    @PostMapping("/notify")
    public void call(HttpServletRequest request) throws IOException, AlipayApiException {
        //获取支付宝 POST 过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        boolean signVerified = AlipaySignature.rsaCheckV1(
                params, payProperties.getAlipayPublicKey(), payProperties.getCharset(), payProperties.getSignType()); //调用 SDK 验证签名

//        if(signVerified) {
            //验证成功
            log.info("验证成功");
            //商户订单号
            String outTradeNo =
                    new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
            //支付宝交易号
            String tradeNo =
                    new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
            //交易状态
            String tradeStatus =
                    new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
            log.info("out_trade_no="+outTradeNo);
            log.info("trade_no="+tradeNo);
            log.info("trade_status="+tradeStatus);



            if (("TRADE_SUCCESS").equals(tradeStatus)){
                // 向前端发送一条支付成功的通知
                webSocket.sendMessage("true");
                // 更新订单状态
                PayOrder payOrder = new PayOrder();
                payOrder.setId(outTradeNo);
                payOrder.setPayNum(tradeNo);
                payOrder.setOrderStatus(CrowdConstant.ORDER_STATUS_PENDING_SHIPMENT);
                ResultEntity<String> stringResultEntity = mysqlRemoteService.modifyOrderInfoDetailRemote(payOrder);
                if(!stringResultEntity.isSuccess()){
                    throw new CrowdException(ResultCodeEnum.NETWORK_ERROR);
                }
                // 更新项目情况
                String projectId = payOrder.getProjectId();
                ResultEntity<Project> projectByProjectIdRemote = mysqlRemoteService.getProjectByProjectIdRemote(projectId);
                if(!projectByProjectIdRemote.isSuccess()){
                    throw new CrowdException(ResultCodeEnum.NETWORK_ERROR);
                }

                Project project = projectByProjectIdRemote.getData();
                Long supporterNumber = project.getSupporterNumber();
                project.setSupporterNumber(supporterNumber+1);
                Double supportMoney = project.getSupportMoney();
                project.setSupportMoney(supportMoney+payOrder.getOrderAmount());

                ResultEntity<String> projectStringResultEntity = mysqlRemoteService.modifyProjectSupporter(payOrder.getUid(), project);
                if(!projectStringResultEntity.isSuccess()){
                    throw new CrowdException(ResultCodeEnum.NETWORK_ERROR);
                }
            }
//        }else {
//            //验证失败
//            //调试用，写文本函数记录程序运行情况是否正常
//            //String sWord = AlipaySignature.getSignCheckContentV1(params);
//            //AlipayConfig.logResult(sWord);
//            log.info("验证失败");
//        }
    }

}
