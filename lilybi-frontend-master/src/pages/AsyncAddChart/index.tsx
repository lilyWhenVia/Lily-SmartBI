import {
  genChartByAiAsyncMqUsingPOST,
  genChartByAiAsyncUsingPOST,
  genChartByAiUsingPOST
} from '@/services/lilyBI/ChartController';
import {Button, Card, Col, Divider, Form, message, Row, Select, Space, Spin, Upload} from 'antd';
import React, {useEffect, useState} from 'react';

import {UploadOutlined} from '@ant-design/icons';
import TextArea from 'antd/es/input/TextArea';
import {ProForm} from "@ant-design/pro-form";
import useForm = ProForm.useForm;
import {history} from "@@/core/history";
import {CHART_TYPE_SELECT} from "@/constants";
import {globalData} from "@/global";

const AsyncAddChart: React.FC = () => {
  const [form] = useForm();
  const [submitting, setSubmitting] = useState<boolean>(false);

  // 设置全局变量到本地
  const [isGlobalEnabled, setIsGlobalEnabled] = useState(globalData.isGlobalEnabled);

  // 设置全局变量到本地
  useEffect(() => {
    setIsGlobalEnabled(globalData.isGlobalEnabled);
  }, []);

  /**
   * 提交表单
   * @param values
   */
  const onFinish = async (values: any) => {
    // 避免重复提交
    if (submitting) {
      return;
    }
    // 开始提交
    setSubmitting(true);
    const param = {
      ...values,
      file: undefined,
    };
    try {
      const res = await genChartByAiAsyncMqUsingPOST(param, {}, values.file.file.originFileObj);
      if (!res?.data) {
        message.error('分析失败' );
      } else {
        message.success('图表分析成功，请稍后到 我的图表 界面查看结果');
        form.resetFields();
      }
    } catch (e: any) {
      message.error('分析失败,' + e.message);
    }
    // 提交完成
    setSubmitting(false);
  };

  // 上传图表分析，并开启自动刷新
  const openAutoFlush = () => {
    globalData.isGlobalEnabled = true;
    setIsGlobalEnabled(globalData.isGlobalEnabled);
    console.log('全局刷新变量已修改为：', globalData.isGlobalEnabled);
  };
  return (
    <div className="add-chart-async">
      <Card>
        <Divider style={{fontWeight: 'bold', color: 'blue'}}>智能分析</Divider>
        <Form
          form={form}
          name="addChart"
          labelAlign="left"
          labelCol={{span: 4}}
          wrapperCol={{span: 16}}
          onFinish={onFinish}
          initialValues={{}}
        >
          <Form.Item
            name="goal"
            label="分析目标"
            rules={[{required: true, message: '请输入分析目标'}]}
          >
            <TextArea placeholder="请输入你的分析需求，比如分析网站用户的增长情况"/>
          </Form.Item>

          <Form.Item
            name="chartName"
            label="图表名称"
            rules={[{required: true, message: '请输入图表名称！'}]}
          >
            <TextArea placeholder="请输入图表名称"/>
          </Form.Item>

          <Form.Item
            name="chartType"
            label="图表类型"
            rules={[{required: true, message: '请选择图表类型！'}]}
          >
            <Select
              options={CHART_TYPE_SELECT}
            ></Select>
          </Form.Item>

          <Form.Item name="file" label="原始数据">
            <Upload name="file" maxCount={1} accept=".csv,.xls,.xlsx,.json,.txt,.xml,.sql">
              <Button icon={<UploadOutlined/>}>上传 CSV 文件(Excel)</Button>
            </Upload>
          </Form.Item>
          <Form.Item wrapperCol={{span: 16, offset: 4}}>
            <Space>
              {/* 在上传的同时开启图表管理自动刷新 openAutoFlush */}
              <Button
                type="primary"
                htmlType="submit"
                loading={submitting}
                disabled={submitting}
                onClick={openAutoFlush}
              >
                确定上传
              </Button>
              <Button htmlType="reset">重置内容</Button>
              <a href="https://www.yuque.com/kcslily/zpovmy/lab4hgt8rf9d6sia?singleDoc# 《AIGC数据可视化系统系统测试数据下载》"
                 target='_blank'>
                <Button
                  block
                  shape='round'
                >
                  获取测试数据
                </Button>
              </a>
            </Space>
          </Form.Item>
        </Form>
      </Card>
    </div>
  );
};
export default AsyncAddChart;
