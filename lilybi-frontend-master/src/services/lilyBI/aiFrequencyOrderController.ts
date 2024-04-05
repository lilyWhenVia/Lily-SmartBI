// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** addOrder POST /api/order/add */
export async function addOrderUsingPOST(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.addOrderUsingPOSTParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/order/add', {
    method: 'POST',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 取消订单 POST /api/order/cancel */
export async function cancelOrderUsingPOST(
  body: API.AiFrequencyOrderCancelRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/order/cancel', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除订单 POST /api/order/delete */
export async function deleteOrderUsingPOST(
  body: API.DeleteRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/order/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** getOrderList GET /api/order/list */
export async function getOrderListUsingGET(options?: { [key: string]: any }) {
  return request<API.BaseResponseListAiFrequencyOrderVO_>('/api/order/list', {
    method: 'GET',
    ...(options || {}),
  });
}

/** （管理员）分页获取订单列表 POST /api/order/list/byPage */
export async function listOrderByPageUsingPOST(
  body: API.AiFrequencyOrderQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageAiFrequencyOrder_>('/api/order/list/byPage', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 获取个人订单 POST /api/order/my/list/page */
export async function listMyOrderByPageUsingPOST(
  body: API.AiFrequencyOrderQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageAiFrequencyOrder_>('/api/order/my/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 修改订单信息 POST /api/order/update */
export async function updateOrderUsingPOST(
  body: API.AiFrequencyOrderUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseBoolean_>('/api/order/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
