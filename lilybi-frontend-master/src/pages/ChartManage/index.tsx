import { globalData } from '@/global';
import {
  deleteChartUsingPOST,
  listMyChartByPageUsingPOST,
} from '@/services/lilyBI/ChartController';
import { Link, useModel } from '@@/exports';
import { ExclamationCircleOutlined } from '@ant-design/icons';
import { Avatar, Button, Card, Col, Divider, List, message, Modal, Result, Row } from 'antd';
import Search from 'antd/es/input/Search';
import ReactECharts from 'echarts-for-react';
import React, { useEffect, useRef, useState } from 'react';

const MyChartPage: React.FC = () => {
  /**
   * 初始值
   */
  const initSearchParams = {
    current: 1,
    pageSize: 4,
    sortField: 'createTime',
    sortOrder: 'desc',
  };

  //设置全局变量
  const [isGlobalEnabled, setIsGlobalEnabled] = useState(globalData.isGlobalEnabled);
  //设置全局变量
  useEffect(() => {
    setIsGlobalEnabled(globalData.isGlobalEnabled);
  }, []);

  // 使用 useRef 创建定时器的引用
  const refreshTimer = useRef(null);

  /**
   * 查询参数
   */
  const [searchParams, setSearchParams] = useState<API.ChartQueryRequest>({
    ...initSearchParams,
  });

  /**
   * 分页获取图表
   */
  const [chartList, setChartList] = useState<API.Chart[]>();
  const [chartTotal, setChartTotal] = useState<number>(0);
  const [loading, setLoading] = useState<boolean>(true);

  /**
   * 获取当前用户
   */
  const { initialState } = useModel('@@initialState');
  const { currentUser } = initialState || {};

  /**
   * 加载图表数据
   */
  const loadData = async () => {
    setLoading(loading);
    try {
      let res = await listMyChartByPageUsingPOST(searchParams);
      console.log('列表：', res?.data?.records);
      if (res.data) {
        setChartList(res.data.records ?? []);
        setChartTotal(res.data.total ?? 0);
        // 隐藏title
        if (res.data.records) {
          // 标志变量，表示是否需要继续刷新
          let shouldRefresh = false;
          res.data.records.forEach((data) => {
            if (data.chartStatus === 1) {
              const chartOption = JSON.parse(data.genChart ?? '{}');
              // 取出title并且设置为 undefined
              chartOption.title = undefined;
              data.genChart = JSON.stringify(chartOption);
            } else {
              console.log('继续定时刷新图表');
              // 如果存在不是 "succeed" 的记录，则设置标志变量为 true
              shouldRefresh = true;
            }
          });

          // 当前页的图表状态都是 succeed 则取消
          if (!shouldRefresh) {
            console.log('定时器已取消');
            // 取消定时刷新
            // @ts-ignore
            clearInterval(refreshTimer.current);
          }
        }
      } else {
        message.error('获取图表失败');
      }
    } catch (e: any) {
      message.error('获取图表失败' + e.message);
    }
    setLoading(false);
  };

  /**
   * 变化时执行此处，从后端加载数据
   */
  useEffect(() => {
    loadData();
  }, [searchParams]);

  // 每 10 秒钟自动刷新一次
  useEffect(() => {
    // @ts-ignore
    refreshTimer.current = setInterval(() => {
      // 在定时器的回调函数中执行你需要定时刷新的操作
      if (isGlobalEnabled) {
        loadData();
      }
    }, 10000);
    return () => {
      // @ts-ignore
      clearInterval(refreshTimer.current);
    };
  }, [isGlobalEnabled]);

  /**
   * 删除图表
   * @param chartId
   */
  const handleDelete = (chartId: any) => {
    Modal.confirm({
      title: '确认删除',
      icon: <ExclamationCircleOutlined />,
      content: '确定要删除这个图表吗？',
      okText: '确认',
      cancelText: '取消',
      onOk: async () => {
        try {
          const res = await deleteChartUsingPOST({ id: chartId });
          console.log('res:', res.data);
          if (res.data) {
            message.success('删除成功');
            // 删除成功后重新加载图表数据
            loadData();
          } else {
            message.error('删除失败');
          }
        } catch (e: any) {
          message.error('删除失败' + e.message);
        }
      },
    });
  };

  return (
    <div className="my-chart-page">
      <div className="margin-20">
        <Search
          placeholder="请输入搜索内容"
          loading={loading}
          enterButton
          onSearch={(value) => {
            setSearchParams({
              ...initSearchParams,
              chartName: value,
            });
          }}
        />
      </div>

      <List
        grid={{
          gutter: 16,
          xs: 1,
          sm: 1,
          md: 1,
          lg: 2,
          xl: 2,
          xxl: 2,
        }}
        pagination={{
          // 设置分页
          showTotal: () => `共 ${chartTotal} 条记录`,
          showSizeChanger: true,
          showQuickJumper: true,
          pageSizeOptions: ['10', '20', '30'],
          onChange: (page, pageSize) => {
            setSearchParams({
              ...searchParams,
              current: page,
              pageSize,
            });
          },
          current: searchParams.current,
          pageSize: searchParams.pageSize,
          total: chartTotal,
        }}
        loading={loading}
        dataSource={chartList}
        renderItem={(item) => (
          <List.Item key={item.id}>
            <Card style={{ width: '100%' }}>
              <List.Item.Meta
                avatar={<Avatar src={currentUser?.userAvatar} />}
                title={currentUser?.userName}
              />
              <>
                {item.chartStatus == 0 && (
                  <>
                    <Result
                      status="warning"
                      title="排队中...."
                      subTitle={item.executorMessage ?? '系统繁忙，请稍后重试'}
                    />
                    <Row>
                      <Col push={17}>
                        <Button danger onClick={() => handleDelete(item.id)}>
                          删除
                        </Button>
                      </Col>
                    </Row>
                  </>
                )}
                {item.chartStatus == 3 && (
                  <>
                    <Result status="info" title="图表生成中...." subTitle={item.executorMessage} />
                  </>
                )}
                {item.chartStatus == 1 && (
                  <>
                    <p
                      style={{
                        textAlign: 'center',
                        fontWeight: 'bold',
                        color: 'black',
                        fontSize: '16px',
                      }}
                    >
                      {'图表标题：' + item.goal}
                    </p>

                    <List.Item.Meta
                      style={{ textAlign: 'left', fontWeight: 'bold' }}
                      description={item.chartType ? '图表类型：' + item.chartType : undefined}
                    />
                    <ReactECharts option={item.genChart && JSON.parse(item.genChart)} />
                    <p
                      style={{
                        textAlign: 'center',
                        fontWeight: 'bold',
                        color: '#e617ff',
                        fontSize: '16px',
                      }}
                    >
                      {'图表名称：' + item.chartName}
                    </p>
                    <Divider style={{ fontWeight: 'bold', color: 'blue', fontSize: '16px' }}>
                      智能分析结果
                    </Divider>
                    <div style={{ whiteSpace: 'pre-wrap', overflow: 'auto' }}>
                      <p style={{ fontWeight: 'bold', color: '#0b93a1', textAlign: 'left' }}>
                        {item.genResult}
                      </p>
                    </div>
                    <Row>
                      <Col style={{ color: 'black', fontWeight: 'bold' }}>
                        {'图表生成时间：' + new Date(item.createTime).toLocaleString()}
                      </Col>
                      <Col push={7}>
                        <Link to={`/ViewChartData/${item.id}`}>
                          <Button>查看图表数据</Button>
                        </Link>
                      </Col>
                      <Col push={8}>
                        <Button danger onClick={() => handleDelete(item.id)}>
                          删除
                        </Button>
                      </Col>
                    </Row>
                  </>
                )}
                {item.chartStatus == 2 && (
                  <>
                    <Result status="error" title="图表生成失败" subTitle={item.executorMessage} />
                    <Row justify="end">
                      <Col style={{ paddingRight: '10px' }}>
                        <Button type="primary" onClick={() => message.warning('敬请期待')}>
                          重试
                        </Button>
                      </Col>
                      <Col>
                        <Button danger onClick={() => handleDelete(item.id)}>
                          删除
                        </Button>
                      </Col>
                    </Row>
                  </>
                )}
              </>
            </Card>
          </List.Item>
        )}
      />
    </div>
  );
};
export default MyChartPage;
