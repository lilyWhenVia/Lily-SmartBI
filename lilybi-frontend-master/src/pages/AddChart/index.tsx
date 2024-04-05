import { genChartByAiUsingPOST } from '@/services/lilyBI/ChartController';
import { Button, Card, Col, Divider, Form, message, Row, Select, Space, Spin, Upload } from 'antd';
import React, { useState } from 'react';

import { UploadOutlined } from '@ant-design/icons';
import TextArea from 'antd/es/input/TextArea';
import ReactECharts from 'echarts-for-react';
import {Link} from "@@/exports";
import {CHART_TYPE_SELECT} from "@/constants";

const AddChart: React.FC = () => {
  const [chart, setChart] = useState<API.BiResponse>();
  const [option, setOption] = useState<any>();
  const [submitting, setSubmitting] = useState<boolean>(false);

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
    setChart(undefined);
    setOption(undefined);
    const param = {
      ...values,
      file: undefined,
    };
    try {
      const res = await genChartByAiUsingPOST(param, {}, values.file.file.originFileObj);
      if (!res?.data) {
        message.error('分析失败');
      } else {
        message.success('分析成功');
        const chartOption = JSON.parse(res.data.genChart ?? '');
        if (!chartOption) {
          throw new Error('图表代码解析错误');
        } else {
          setChart(res.data);
          setOption(chartOption);
        }
      }
    } catch (e: any) {
      message.error('分析失败,' + e.message);
    }
    // 提交完成
    setSubmitting(false);
  };

  return (
    <div className="add-chart">
      <Row gutter={24}>
        <Col span={12}>
          <Card>
            <Divider style={{ fontWeight: 'bold', color: 'blue' }}>智能分析</Divider>
            <Form
              name="addChart"
              labelAlign="left"
              labelCol={{ span: 4 }}
              wrapperCol={{ span: 16 }}
              onFinish={onFinish}
              initialValues={{}}
            >
              <Form.Item
                name="goal"
                label="分析目标"
                rules={[{ required: true, message: '请输入分析目标' }]}
              >
                <TextArea placeholder="请输入你的分析需求，比如分析网站用户的增长情况" />
              </Form.Item>

              <Form.Item
                name="chartName"
                label="图表名称"
                rules={[{ required: true, message: '请输入图表名称！' }]}
              >
                <TextArea placeholder="请输入图表名称" />
              </Form.Item>

              <Form.Item
                name="chartType"
                label="图表类型"
                rules={[{ required: true, message: '请选择图表类型！' }]}
              >
                <Select
                  options={CHART_TYPE_SELECT}
                ></Select>
              </Form.Item>

              <Form.Item name="file" label="原始数据">
                <Upload name="file" maxCount={1} accept=".csv,.xls,.xlsx,.json,.txt,.xml,.sql">
                  <Button icon={<UploadOutlined />}>上传 CSV 文件(Excel)</Button>
                </Upload>
              </Form.Item>

              <Form.Item wrapperCol={{ span: 16, offset: 4 }}>
                <Space>
                  <Button
                    type="primary"
                    htmlType="submit"
                    loading={submitting}
                    disabled={submitting}
                  >
                    确定上传
                  </Button>
                  <Button htmlType="reset">重置内容</Button>

                  <a href="https://www.yuque.com/kcslily/zpovmy/lab4hgt8rf9d6sia?singleDoc# 《智能BI系统测试数据下载》" target='_blank'>
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
        </Col>

        <Col span={12}>
          <Card>
            <Divider style={{ fontWeight: 'bold', color: 'blue' }}>可视化图表</Divider>
            <Card>
              {option ? (
                <ReactECharts option={option} />
              ) : (
                <div
                  style={{
                    color: 'red',
                    textAlign: 'center',
                    fontWeight: 'bold',
                    fontSize: '16px',
                  }}
                >
                  请在左侧进行提交分析数据！
                </div>
              )}
              <Spin spinning={submitting} size="large" />
            </Card>

            <Divider style={{ fontWeight: 'bold', color: 'blue' }}>由上图得出结论</Divider>
            <Card>
              {chart?.genResult ?? (
                <div
                  style={{
                    color: 'red',
                    textAlign: 'center',
                    fontWeight: 'bold',
                    fontSize: '16px',
                  }}
                >
                  无可视化图表，无法得出结论！
                </div>
              )}
              <Spin spinning={submitting} size="large" />
            </Card>
          </Card>
        </Col>
      </Row>
    </div>
  );
};
export default AddChart;
