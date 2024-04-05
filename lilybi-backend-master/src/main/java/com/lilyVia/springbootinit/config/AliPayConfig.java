package com.lilyVia.springbootinit.config;

import org.springframework.context.annotation.Configuration;

/**
 * Created by lily via on 2024/4/5 23:27
 */
//@Configuration
public class AliPayConfig {

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "9021000135693019";

    // 商户私钥
    public static String alipay_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCE1pK67InBBYWjN5AyVOXRM3FPjtdOBW7yRIUyIyr4Z/uEMIv8TK+Z5QEf0/V2eucc5K8s90Qs/f7gLaw3TcZo/ZwdEk1u/74mIyYBzCtUjhH/zSQ6aeQ+KBvOL911WoTuefWVND/g+N0Tkj+UYXv7Zg276Od05Z49ghdRMeQaNoheyzV+5y7ALtjyGUulNSkB2lgov+tXLspvi+aGk81qNmvBa3ZVr5pVOACz2Wsfm5f8u0wPuPfqS8A4bF9c72UimVXAQ4k/hrCFjeoTRmig4BisUUiTyEyMZASowru5YJ0EBfYSG/QSQDPqbSvyqkO6h4/J6GvvIATENdyXFYLrAgMBAAECggEAVl7+ALQTzYE9pyZVh6RT2XZNUqFZtM0rmjKbOEJNij5x9z1ph56E/tc7p+o1S9gVtV+r1tzfYzGzKygHNd5IB7P+cZiY6Hc4t6ta5whNlCjb4ysLRz/au99wVNwibk9w5249E8ppTmoBtNv+owfPiWKNw4RoGkXCYdPYPV7nJXMnl31lFeZEziFh430K+vKwENwWR0gRpA3cv/735dw9mxB67c3imqLlkpo5TilRCZcOs52yXKcwl/8xxXkEDfg7o/m/+TwO6xgrsKZLCOnU0/wnU1cHQG8b/lpYAtLc6+Fs9nD2JwSvn0ziCI79ONdQ1ByzcDgJus9RT/iI9hByIQKBgQDBrO2L07Fu0sbdktRjFtDfYcQiq/ZS9NK2pXzXxxUHwka4DoLu2vPlSRF3+mpxYEAss6Am/o3EIgpgmzbjlQE9PxIzn5WOuXzFT9+mup1GXKx93JFEXaXKHrYDSUslXbPc2/5rS+gjWgO/ehr9gCy+05iGMzBQtNBKGP75GRzBpwKBgQCvldcNBC0Comq+zy/d3NF3JHKKWA3oxDdOLEY7oe++eZeqSgJP0iwZFCFFmqJh2v3wA/24HxLBZowljTxY3s5OKyfBYsOa+3VqcqKJtpeCQi1WOw6zo7N17HO9kzZSPQV9wpV9B1taqA92EG6ZYqBzK7mGCebh8f8a6pi2XuQ1HQKBgQC2T0zwsZ41Pw3ZpwxsFh8aF5KkPJFLt4/eRFsHVyyRuCwPuAFCTDMH40chVET8YmkmaxPXgQj71jtEgvchNbML7yH4HEGRL0OnDK/v53yCrlvkwkqwOyMD5vLYlxa4nsLfpOqSH8rt9B7REGT1ucDlBJNvyCGz5vCmv27MFvp9wwKBgGcGbpaOmrJvBl9LRnTRun5D/ok6PX8Ukrblt5gJ3phO1DTxFAYt9f4JF7S1lQ4B5vpL/TVp1iFwwE7QXlZu9XJcVAleoBNx9GUvOju/zYp6tgtgv7GcHhLVWNwlYrxCXdR6tigUhv/cmKXX4m1Rt3+calFBwh1VVzA98rkfWJvpAoGAX260LYxTxRXgKjYS4TFJLe+wqBTIBwbMIi1uhaO/qug3EM6xBcRhz1hOHgop2uNI6Yf72t7wDfzXrVcH0twqPO8o309+QFJdMYLNwN3dq/IW0mgyakdwkmRq6iYDME8usEMBi2Q7UoOOvN0n0soYFZLISe5sR6d1cQFPykCTNCM=";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6jsb3lPydq36cOl3jNWacQFC29NnaqAV956//iitoq/eM8JizeYSzGiYgxUBNBJkI0wlQW1As1iXYV+H8jGF6S4hjYlfmTvZRsezOx40R1ZEIGbSoyNQVK1b9KKy7dVp+wrdpEEqMuVpnrgMxJOPk8tNDyWWzDSy7zB8lc+Mt2syb/tJBZEMP+K+ETh7fHQrsJw9XqFhn4WUL8+AmdoBi48rnSS5DllZxQBMezANpV6rToQoa+b1mIaCfyAOrHj0A2Wt+DwMnF8g/FtEI/s3Ddv4eIeE3tp2RXYoj8FF/cOhnCCN3zxfyWjG6O85msWN0hURwPkDQFAzz1n5I1nuYQIDAQAB";
    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://工程公网访问地址/alipay.trade.page.pay-JAVA-UTF-8/notify_url.jsp";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://工程公网访问地址/alipay.trade.page.pay-JAVA-UTF-8/return_url.jsp";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "C:\\";

}
