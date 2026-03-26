// @ts-ignore
/* eslint-disable */
import request from "@/request";

export interface PaymentResultVO {
  orderNo?: string;
  codeUrl?: string;
  expireTime?: string;
  status?: string;
}

/** 创建微信支付订单 POST /payment/create-wechat */
export async function createWeChatPayOrder(options?: { [key: string]: any }) {
  return request<API.BaseResponsePaymentResultVO>("/payment/create-wechat", {
    method: "POST",
    ...(options || {}),
  });
}

/** 查询支付状态 GET /payment/status/{orderNo} */
export async function getPaymentStatus(
  orderNo: string,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseString>(`/payment/status/${orderNo}`, {
    method: "GET",
    ...(options || {}),
  });
}