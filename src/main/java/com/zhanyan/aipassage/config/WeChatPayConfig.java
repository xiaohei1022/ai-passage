package com.zhanyan.aipassage.config;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "wechat.pay")
public class WeChatPayConfig {

    private String appId;
    private String mchId;
    private String apiKey;
    private String serialNo;
    private String privateKey;
    private String notifyUrl;
    private boolean useSandbox;

    public Config createConfig() {
        return new RSAAutoCertificateConfig.Builder()
                .merchantId(mchId)
                .privateKey(privateKey)
                .merchantSerialNumber(serialNo)
                .apiV3Key(apiKey)
                .build();
    }
}