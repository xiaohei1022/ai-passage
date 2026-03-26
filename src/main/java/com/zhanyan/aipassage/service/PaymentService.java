package com.zhanyan.aipassage.service;

import com.zhanyan.aipassage.model.entity.PaymentRecord;
import com.zhanyan.aipassage.model.vo.PaymentResultVO;

public interface PaymentService {

    /**
     * 创建微信支付订单（Native扫码支付）
     *
     * @param userId    用户ID
     * @param productId 产品ID
     * @return 支付结果（包含二维码链接）
     */
    PaymentResultVO createWeChatPayOrder(Long userId, String productId);

    /**
     * 查询支付状态
     *
     * @param orderNo 订单号
     * @return 支付状态
     */
    String getPaymentStatus(String orderNo);

    /**
     * 处理微信支付回调
     *
     * @param xmlData 微信回调的XML数据
     * @return 是否处理成功
     */
    boolean handleWeChatPayNotify(String xmlData);
}