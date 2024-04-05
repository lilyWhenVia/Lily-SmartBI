package com.lilyVia.springbootinit.constant;

/**
 * Created by lily via on 2024/3/21 1:45
 */
public interface QuestionConstant {

    /**
     * 任务等待处理中
     */
    int WAITING = 0;
    /**
     * 任务处理成功
     */
    int SUCCEED = 1;
    /**
     * 任务处理失败
     */
    int FAILED = 2;
    /**
     * 任务正在执行
     */
    int RUNNING = 3;

    /**
     * 更新状态失败
     */
    int UPDATE_STATUE_FILED = 4;

}
