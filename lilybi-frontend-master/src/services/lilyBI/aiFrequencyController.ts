// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** AiFrequencyRecharge POST /api/aiFrequency/frequency */
export async function AiFrequencyRechargeUsingPOST(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.AiFrequencyRechargeUsingPOSTParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLong_>('/api/aiFrequency/frequency', {
    method: 'POST',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** getAiFrequency GET /api/aiFrequency/get */
export async function getAiFrequencyUsingGET(options?: { [key: string]: any }) {
  return request<API.BaseResponseAiFrequencyVO_>('/api/aiFrequency/get', {
    method: 'GET',
    ...(options || {}),
  });
}
