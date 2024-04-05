// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** getPayInfoList GET /api/payInfo/list */
export async function getPayInfoListUsingGET(options?: { [key: string]: any }) {
  return request<API.BaseResponseListPayInfoVO_>('/api/payInfo/list', {
    method: 'GET',
    ...(options || {}),
  });
}

/** （管理员）分页获取订单列表 POST /api/payInfo/list/byPage */
export async function listPayInfoByPageUsingPOST(
  body: API.AlipayInfoQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageAlipayInfo_>('/api/payInfo/list/byPage', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 获取个人支付订单 POST /api/payInfo/list/my/page */
export async function listMyPayInfoByPageUsingPOST(
  body: API.AlipayInfoQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageAlipayInfo_>('/api/payInfo/list/my/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
