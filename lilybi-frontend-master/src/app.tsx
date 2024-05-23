import { SYSTEM_LOGO } from '@/constants';
import { getLoginUserUsingGET } from '@/services/lilyBI/UserController';

import { SettingDrawer } from '@ant-design/pro-components';
import type { RunTimeLayoutConfig } from '@umijs/max';
import { history, Link } from '@umijs/max';
import { AvatarDropdown, AvatarName } from './components/RightContent/AvatarDropdown';
import { errorConfig } from './requestConfig';

const isDev = process.env.NODE_ENV === 'development';
const loginPath = '/user/login';

/**
 * @see  https://umijs.org/zh-CN/plugins/plugin-initial-state
 * */
export async function getInitialState(): Promise<{
  currentUser?: API.LoginUserVO;
}> {
  const fetchUserInfo = async () => {
    console.log(
      'NO BUG\n' +
        '       _                        \n' +
        '       \\`*-.                    \n' +
        '        )  _`-.                 \n' +
        '       .  : `. .                \n' +
        "       : _   '  \\               \n" +
        '       ; *` _.   `*-._          \n' +
        "       `-.-'          `-.       \n" +
        '         ;       `       `.     \n' +
        '         :.       .        \\    \n' +
        "         . \\  .   :   .-'   .   \n" +
        "         '  `+.;  ;  '      :   \n" +
        "         :  '  |    ;       ;-. \n" +
        "         ; '   : :`-:     _.`* ;\n" +
        "[bug] .*' /  .*' ; .*`- +'  `*' \n" +
        "      `*-*   `*-*  `*-*'",
    );
    try {
      // 获取登录用户信息
      const msg = await getLoginUserUsingGET();
      return msg.data;
    } catch (error) {
      history.push(loginPath);
    }
    return undefined;
  };
  // 如果不是登录页面，执行
  const { location } = history;
  if (location.pathname !== loginPath) {
    const currentUser = await fetchUserInfo();
    return {
      currentUser,
    };
  }
  return {};
}

// ProLayout 支持的api https://procomponents.ant.design/components/layout
export const layout: RunTimeLayoutConfig = ({ initialState, setInitialState }) => {
  return {
    title: 'lily智能 BI',
    logo: SYSTEM_LOGO,
    iconfontUrl: '//at.alicdn.com/t/c/font_4064432_y5wwdt2q2be.js',
    // 显示头像
    avatarProps: {
      src: initialState?.currentUser?.userAvatar,
      title: <AvatarName />,
      render: (_, avatarChildren) => {
        return <AvatarDropdown>{avatarChildren}</AvatarDropdown>;
      },
    },
    // 水印
    waterMarkProps: {
      content: initialState?.currentUser?.userName,
    },

    links: isDev
      ? [
          <Link key="openapi" to="/umi/plugin/openapi" target="_blank">
            <span>OpenAPI 接口文档</span>
          </Link>,
        ]
      : [],
    menuHeaderRender: undefined,
    // 自定义 403 页面
    // unAccessible: <div>unAccessible</div>,
    // 增加一个 loading 的状态
    childrenRender: (children) => {
      // if (initialState?.loading) return <PageLoading />;
      return (
        <>
          {children}
          <SettingDrawer
            disableUrlParams
            enableDarkTheme
            onSettingChange={(settings) => {
              setInitialState((preInitialState: any) => ({
                ...preInitialState,
                settings,
              }));
            }}
          />
        </>
      );
    },
  };
};

/**
 * @name request 配置，可以配置错误处理
 * 它基于 axios 和 ahooks 的 useRequest 提供了一套统一的网络请求和错误处理方案。
 * @doc https://umijs.org/docs/max/request#配置
 */


export const request = {
// dev? : 'http://bi.konglily.top'
  // 请求后端地址
  // baseURL: 'http://127.0.0.1:8101',
  baseURL:'http://127.0.0.1:8101',
  // 获取cookie
  withCredentials: true,
  ...errorConfig,
};
