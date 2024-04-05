/**
 * @author lily
 * CreateTime 2023/5/21 9:04
 */

import { extend } from 'dayjs';

const request = extend({
// @ts-ignore
  credentials: 'include', // 默认请求是否带上cookie
  prefix: process.env.NODE_ENV === 'production' ? 'http://bi.konglily.top' : 'http://localhost:8103',
  /* process.env.NODE_ENV这个变量就是记录当前是什么环境，然后这句话就是说，如果当前是production生产环境，就去请求地址 http://xxxx，这个地址就是后端生产环境的地址  */
});
export default request;
