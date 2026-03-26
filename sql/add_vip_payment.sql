-- 添加会员和支付功能
-- @author xiaoh

use ai_passage;

-- 1. 扩展 user 表，添加会员相关字段
ALTER TABLE user
ADD COLUMN vipTime DATETIME NULL COMMENT '成为会员时间';

-- 2. 创建支付记录表（微信支付版本）
CREATE TABLE IF NOT EXISTS payment_record (
      id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
      userId BIGINT NOT NULL COMMENT '用户ID',
      orderNo VARCHAR(64) NOT NULL COMMENT '商户订单号',
    transactionId VARCHAR(64) COMMENT '微信支付订单号',
    amount INT NOT NULL COMMENT '金额（分）',
    currency VARCHAR(8) DEFAULT 'CNY' COMMENT '货币',
    status VARCHAR(32) NOT NULL COMMENT '状态：PENDING/SUCCESS/FAILED/REFUNDED/CLOSED',
    productType VARCHAR(32) NOT NULL COMMENT '产品类型：VIP_PERMANENT',
    description VARCHAR(256) COMMENT '描述',
    codeUrl VARCHAR(512) COMMENT '支付二维码链接',
    expireTime DATETIME COMMENT '二维码过期时间',
    payTime DATETIME NULL COMMENT '支付成功时间',
    refundTime DATETIME NULL COMMENT '退款时间',
    refundReason VARCHAR(512) NULL COMMENT '退款原因',
    createTime DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updateTime DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    isDelete        tinyint     default 0              not null comment '是否删除',

    INDEX idx_userId (userId),
    INDEX idx_orderNo (orderNo),
    INDEX idx_transactionId (transactionId),
    INDEX idx_status (status),
    INDEX idx_createTime (createTime)
    ) COMMENT '支付记录表' COLLATE = utf8mb4_unicode_ci;
