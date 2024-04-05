import { HeartTwoTone, SmileTwoTone } from '@ant-design/icons';
import { PageContainer } from '@ant-design/pro-components';
import '@umijs/max';
import { Alert, Card, Typography } from 'antd';
import React from 'react';

const { Title, Paragraph } = Typography;

/**
 *
 * @constructor
 */
const Admin: React.FC = () => {
  return (
    <PageContainer content={' 管理员首页介绍 '}>
      <Card>
        <Typography.Title
          level={2}
          style={{
            textAlign: 'center',
          }}
        >
          <SmileTwoTone /> 智能 BI 管理界面
          <HeartTwoTone twoToneColor="#eb2f96" />
        </Typography.Title>

        <Alert
          message={'全力打造智能BI系统'}
          style={{
            margin: -12,
            marginBottom: 48,
            textAlign:"center"
          }}
        />
        <Title level={1} style={{color:"#0015ff",textAlign:"left"}}>
          管理员特权
        </Title>
        <Title level={4}  style={{color:"rgb(0,42,255)"}}>
          1. 增删改查用户
        </Title>
        <Title level={4} style={{color:"rgba(0,21,255,0.9)"}}>
          2. 调用平台所有接口服务
        </Title>
        <Title level={4} style={{color:"rgba(0,34,255,0.87)"}}>
          3. 更好进行图表管理
        </Title>
        <Title level={4} style={{color:"#0015ff"}}>
          4. 可以查看所有用户生成的图表
        </Title>
        <Title level={4} style={{color:"#003cff"}}>
          5. 管理用户的订单
        </Title>
      </Card>
    </PageContainer>
  );
};
export default Admin;
