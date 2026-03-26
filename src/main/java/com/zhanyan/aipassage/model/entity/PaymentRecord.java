package com.zhanyan.aipassage.model.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "payment_record", camelToUnderline = false)
public class PaymentRecord implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private Long id;

    private Long userId;

    private String orderNo;

    private String transactionId;

    private Integer amount;

    private String currency;

    private String status;

    private String productType;

    private String description;

    private String codeUrl;

    private LocalDateTime expireTime;

    private LocalDateTime payTime;

    private LocalDateTime refundTime;

    private String refundReason;

    @Column(isLogicDelete = true)
    private Integer isDelete;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}