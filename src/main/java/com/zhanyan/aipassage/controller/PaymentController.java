package com.zhanyan.aipassage.controller;

import com.zhanyan.aipassage.common.BaseResponse;
import com.zhanyan.aipassage.common.ResultUtils;
import com.zhanyan.aipassage.exception.BusinessException;
import com.zhanyan.aipassage.exception.ErrorCode;
import com.zhanyan.aipassage.model.entity.User;
import com.zhanyan.aipassage.model.vo.PaymentResultVO;
import com.zhanyan.aipassage.service.PaymentService;
import com.zhanyan.aipassage.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/payment")
@Tag(name = "支付管理")
@Slf4j
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    @Resource
    private UserService userService;

    private static final String VIP_PRODUCT_ID = "VIP_PERMANENT";

    @PostMapping("/create-wechat")
    @Operation(summary = "创建微信支付订单")
    public BaseResponse<PaymentResultVO> createWeChatPayOrder(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }

        if (loginUser.getVipTime() != null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "您已经是会员");
        }

        PaymentResultVO result = paymentService.createWeChatPayOrder(loginUser.getId(), VIP_PRODUCT_ID);
        return ResultUtils.success(result);
    }

    @GetMapping("/status/{orderNo}")
    @Operation(summary = "查询支付状态")
    public BaseResponse<String> getPaymentStatus(@PathVariable String orderNo) {
        String status = paymentService.getPaymentStatus(orderNo);
        return ResultUtils.success(status);
    }

    @PostMapping("/notify")
    @Operation(summary = "微信支付回调")
    public String handleWeChatPayNotify(@RequestBody String xmlData) {
        log.info("收到微信支付回调: {}", xmlData);
        boolean success = paymentService.handleWeChatPayNotify(xmlData);
        if (success) {
            return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
        }
        return "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[FAIL]]></return_msg></xml>";
    }
}