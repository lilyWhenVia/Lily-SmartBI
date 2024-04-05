// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** pay GET /api/alipay/pay */
export async function payUsingGET(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.payUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<any>('/api/alipay/pay', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** payCode POST /api/alipay/payCode */
export async function payCodeUsingPOST(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.payCodeUsingPOSTParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseAlipayInfoVO_>('/api/alipay/payCode', {
    method: 'POST',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** queryPayResultFromAlipay POST /api/alipay/query/payNo */
export async function queryPayResultFromAlipayUsingPOST(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.queryPayResultFromAlipayUsingPOSTParams,
  options?: { [key: string]: any },
) {
  return request<any>('/api/alipay/query/payNo', {
    method: 'POST',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** tradeQuery POST /api/alipay/tradeQuery */
export async function tradeQueryUsingPOST(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.tradeQueryUsingPOSTParams,
  options?: { [key: string]: any },
) {
  return request<any>('/api/alipay/tradeQuery', {
    method: 'POST',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}
