package com.zhanyan.aipassage.model.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class PaymentResultVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String orderNo;

    private String codeUrl;

    private LocalDateTime expireTime;

    private String status;
}