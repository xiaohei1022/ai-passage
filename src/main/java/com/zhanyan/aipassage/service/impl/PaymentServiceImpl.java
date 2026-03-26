package com.zhanyan.aipassage.service.impl;

import cn.hutool.core.util.XmlUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.zhanyan.aipassage.exception.BusinessException;
import com.zhanyan.aipassage.exception.ErrorCode;
import com.zhanyan.aipassage.mapper.PaymentRecordMapper;
import com.zhanyan.aipassage.mapper.UserMapper;
import com.zhanyan.aipassage.model.entity.PaymentRecord;
import com.zhanyan.aipassage.model.entity.User;
import com.zhanyan.aipassage.model.vo.PaymentResultVO;
import com.zhanyan.aipassage.service.PaymentService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Resource
    private PaymentRecordMapper paymentRecordMapper;

    @Resource
    private UserMapper userMapper;

    private static final String VIP_PRODUCT_ID = "VIP_PERMANENT";
    private static final int VIP_PRICE_CENTS = 19900;

    @Override
    @Transactional
    public PaymentResultVO createWeChatPayOrder(Long userId, String productId) {
        if (!VIP_PRODUCT_ID.equals(productId)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无效的产品ID");
        }

        String orderNo = generateOrderNo();
        String codeUrl = "weixin://wxpay/bizpayurl?pr=" + orderNo;

        PaymentRecord paymentRecord = new PaymentRecord();
        paymentRecord.setUserId(userId);
        paymentRecord.setOrderNo(orderNo);
        paymentRecord.setAmount(VIP_PRICE_CENTS);
        paymentRecord.setCurrency("CNY");
        paymentRecord.setStatus("PENDING");
        paymentRecord.setProductType(VIP_PRODUCT_ID);
        paymentRecord.setDescription("AI Passage 永久会员");
        paymentRecord.setCodeUrl(codeUrl);
        paymentRecord.setExpireTime(LocalDateTime.now().plusMinutes(15));

        paymentRecordMapper.insert(paymentRecord);

        PaymentResultVO result = new PaymentResultVO();
        result.setOrderNo(orderNo);
        result.setCodeUrl(codeUrl);
        result.setExpireTime(paymentRecord.getExpireTime());
        result.setStatus("PENDING");

        return result;
    }

    @Override
    public String getPaymentStatus(String orderNo) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_no", orderNo);
        PaymentRecord record = paymentRecordMapper.selectOneByQuery(queryWrapper);

        if (record == null) {
            return "NOT_FOUND";
        }

        return record.getStatus();
    }

    @Override
    @Transactional
    public boolean handleWeChatPayNotify(String xmlData) {
        log.info("收到微信支付回调: {}", xmlData);

        try {
            Document document = XmlUtil.parseXml(xmlData);
            Element root = document.getDocumentElement();
            String returnCode = getElementText(root, "return_code");
            String resultCode = getElementText(root, "result_code");
            String outTradeNo = getElementText(root, "out_trade_no");
            String transactionId = getElementText(root, "transaction_id");

            if ("SUCCESS".equals(returnCode) && "SUCCESS".equals(resultCode)) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("order_no", outTradeNo);
                PaymentRecord record = paymentRecordMapper.selectOneByQuery(queryWrapper);

                if (record != null && "PENDING".equals(record.getStatus())) {
                    record.setStatus("SUCCESS");
                    record.setPayTime(LocalDateTime.now());
                    record.setTransactionId(transactionId);
                    paymentRecordMapper.updateByQuery(record, new QueryWrapper().eq("id", record.getId()));

                    activateVip(record.getUserId());

                    log.info("支付成功，已开通VIP, orderNo: {}", outTradeNo);
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("处理微信支付回调失败", e);
        }

        return false;
    }

    private String getElementText(Element root, String tagName) {
        var elements = root.getElementsByTagName(tagName);
        if (elements.getLength() > 0) {
            return elements.item(0).getTextContent();
        }
        return null;
    }

    private void activateVip(Long userId) {
        User user = new User();
        user.setId(userId);
        user.setVipTime(LocalDateTime.now());
        QueryWrapper updateWrapper = new QueryWrapper();
        updateWrapper.eq("id", userId);
        userMapper.updateByQuery(user, updateWrapper);
        log.info("用户 {} 已开通VIP", userId);
    }

    private String generateOrderNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        return "VIP" + timestamp + sb.toString();
    }
}