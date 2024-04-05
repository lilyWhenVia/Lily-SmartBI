package com.lilyVia.springbootinit.manager;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * Created by lily via on 2024/3/21 0:18
 */
@SpringBootTest
class AIManagerTest {

    @Resource
    public AIManager aiManager;

    String originData = "你是一名优秀的数据分析师，根据分析目标:" + "网站访问情况" + ",以及以下数据帮我生成一个" + "折线图" + "类型的图表。"+"日期，用户数\n" +
            "1号,10 \n" +
            "2号,20\n" +
            "3号,30";

    String json = "{\n" +
            "    \"title\": {\n" +
            "        \"text\": \"网站访问情况\",\n" +
            "        \"subtext\": \"日期 vs 用户数\"\n" +
            "    },\n" +
            "    \"tooltip\": {\n" +
            "        \"trigger\": \"axis\"\n" +
            "    },\n" +
            "    \"legend\": {\n" +
            "        \"data\": [\"用户数\"]\n" +
            "    },\n" +
            "    \"xAxis\": {\n" +
            "        \"type\": \"category\",\n" +
            "        \"boundaryGap\": false,\n" +
            "        \"data\": [\"1号\", \"2号\", \"3号\"]\n" +
            "    },\n" +
            "    \"yAxis\": {\n" +
            "        \"type\": \"value\"\n" +
            "    },\n" +
            "    \"series\": [{\n" +
            "        \"name\": \"用户数\",\n" +
            "        \"type\": \"line\",\n" +
            "        \"data\": [10, 20, 30]\n" +
            "    }]\n" +
            "}\n";

//    @Test
    public void chatTest() {
        JSON parse = JSONUtil.parse(json);
        System.out.println(parse);

    }

//    @Test
    public void xingHuoChatTest() {
        String s = aiManager.sendMesToAIUseXingHuo(originData);
        System.out.println(s);
    }

}