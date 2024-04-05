import { selectAvatarUrl, selectGender } from '@/constants';
import { getAiFrequencyUsingGET } from '@/services/lilyBI/aiFrequencyController';
import { addOrderUsingPOST } from '@/services/lilyBI/aiFrequencyOrderController';
import {
  getLoginUserUsingGET,
  getUserVOByIdUsingGET,
  updateByProfileUserUsingPOST,
  updateMyInfoUsingPOST,
} from '@/services/lilyBI/UserController';
import { useModel } from '@@/exports';
import { LoadingOutlined, PlusOutlined } from '@ant-design/icons';
import { ModalForm, ProForm, ProFormText } from '@ant-design/pro-components';
import { ProFormSelect } from '@ant-design/pro-form';
import {
  Button,
  Card,
  Col,
  Descriptions,
  Divider,
  InputNumber,
  message,
  Row,
  Upload,
  UploadFile,
  UploadProps,
} from 'antd';
import Modal from 'antd/es/modal/Modal';
import { RcFile, UploadChangeParam } from 'antd/es/upload';
import React, { useEffect, useState } from 'react';

const waitTime = (time: number = 100) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve(true);
    }, time);
  });
};

const avatarStyle: React.CSSProperties = {
  width: '100%',
  textAlign: 'center',
};
const buttonStyle: React.CSSProperties = {
  marginLeft: '30px',
};

/**
 * 上传前校验
 * @param file 文件
 */
const beforeUpload = (file: RcFile) => {
  const isJpgOrPng =
    file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/jpg';
  if (!isJpgOrPng) {
    message.error('仅允许上传 JPG/PNG 格式的文件!');
  }
  const isLt2M = file.size / 1024 / 1024 < 2;
  if (!isLt2M) {
    message.error('文件最大上传大小为 2MB!');
  }
  return isJpgOrPng && isLt2M;
};

const UserInfo: React.FC = () => {
  const [myUser, setMyUser] = useState({
    userName: '',
    userAccount: '',
    userAvatar: '',
    userPassword: '',
    userRole: 'user',
    gender: '',
    phone: '',
    email: '',
    userStatus: '0',
    userCode: '',
    createTime: '',
    updateTime: '',
  });
  useEffect(() => {
    async function fetchData() {
      try {
        const res = await getLoginUserUsingGET(); // 使用 getLoginUserUsingGET 发送请求
        // @ts-ignore
        setMyUser(res.data);
      } catch (error) {
        // 处理请求错误
        message.error('请求参数错误！');
      }
    }

    fetchData();
  }, []);

  //加...是创建一个新的对象把值赋给新对象，不会造成对象污染
  const [userId, setUserId] = useState<number>();
  const [frequency, setFrequency] = useState<API.AiFrequencyVO>();
  const [number, setNumber] = useState<number>();
  const [data, setData] = useState<API.UserVO>({});
  const [loading, setLoading] = useState(false);
  const [imageUrl, setImageUrl] = useState<string>();
  const { initialState, setInitialState } = useModel('@@initialState');
  const [isModalOpen, setIsModalOpen] = useState(false);

  const loadData = async () => {
    try {
      const res = await getAiFrequencyUsingGET();
      // console.log('用户次数', res.data);
      if (res.data) {
        setFrequency(res.data);
      }
    } catch (e: any) {
      message.error('获取数据失败' + e.error);
    }
  };

  // 用户充值次数
  const onChange = async (value: number) => {
    console.log('changed', value);
    setNumber(value);
  };

  const showModal = () => {
    setIsModalOpen(true);
  };

  const handleOk = async () => {
    const res = await addOrderUsingPOST({ total: number || 1 });
    if (res.data) {
      message.success('生成订单成功，请在 个人订单 中查看订单信息');
    } else {
      message.error('生成订单失败');
    }
    loadData();
    setIsModalOpen(false);
  };
  // 取消支付
  const handleCancel = () => {
    setIsModalOpen(false);
  };

  // 获取用户信息
  const getUserInfo = async (id: any) => {
    return getUserVOByIdUsingGET({ id }).then((res: any) => {
      console.log('编号', res.data);
      if (res.data) {
        setInitialState((s: any) => ({ ...s, loginUser: res.data }));
        setData(res.data);
        setImageUrl(res.data.userAvatar);
        setUserId(res.data.id);
      }
    });
  };

  useEffect(() => {
    try {
      loadData();
      getUserInfo(initialState?.currentUser?.id).then((r) => {});
      // console.log('用户信息', initialState?.currentUser);
    } catch (e: any) {
      console.log(e);
    }
  }, []);

  // 更新用户头像
  const updateUserAvatar = async (id: any, userAvatar: any) => {
    // 更新用户头像
    const res = await updateByProfileUserUsingPOST({
      id: id,
      userAvatar: userAvatar,
    });
    console.log('头像状态' + res.code);
    if (res.code !== 0) {
      message.error(`更新用户头像失败`);
    } else {
      getUserInfo(id);
    }
  };

  /**
   * 上传图片
   * @param info
   */
  const handleChange: UploadProps['onChange'] = (info: UploadChangeParam<UploadFile>) => {
    if (info.file.status === 'uploading') {
      setLoading(true);
      return;
    }
    console.log('上传状态', info.file.status);
    if (info.file.status === 'done') {
      if (info.file.response.code === 0) {
        message.success(`上传成功`);
        const id = initialState?.currentUser?.id as number;
        const userAvatar = info.file.response.data;
        console.log('头像url', info.file);
        setLoading(false);
        setImageUrl(userAvatar);
        updateUserAvatar(id, userAvatar);
      }
    }
  };

  const uploadButton = (
    <div>
      {loading ? <LoadingOutlined /> : <PlusOutlined />}
      <div style={{ marginTop: 8 }}>Upload</div>
    </div>
  );

  return (
    <>
      <Divider style={{ fontWeight: 'bold', color: 'blue' }}>用户头像</Divider>
      <Descriptions style={{ margin: '50px' }}>
        <Row>
          <Col style={avatarStyle}>
            <Upload
              name="file"
              listType="picture-circle"
              showUploadList={false}
              // action="http://127.0.0.1:8103/api/oss/upload"
              action="http://bi.konglily.top/api/oss/upload"
              beforeUpload={beforeUpload}
              onChange={handleChange}
            >
              {imageUrl ? (
                <img
                  src={data?.userAvatar}
                  alt="userAvatar"
                  style={{ width: '200%', borderRadius: '50%', height: '200%' }}
                />
              ) : (
                uploadButton
              )}
            </Upload>
          </Col>
        </Row>
      </Descriptions>
      <Divider style={{ fontWeight: 'bold', color: 'blue' }}>AI智能分析次数</Divider>
      <Descriptions bordered size={'default'} contentStyle={{ fontWeight: 'bold', color: 'blue' }}>
        <Card style={{ textAlign: 'center' }} type="inner">
          <Row gutter={16}>
            <Col span={10} style={{ fontSize: 16, color: 'black' }}>
              {'已使用：' + frequency?.totalFrequency + ' 次'}
            </Col>
            <Col span={4} style={{ fontSize: 16, color: 'black' }}>
              {'剩余：' + frequency?.remainFrequency + ' 次'}
            </Col>
            <Col span={10}>
              <Button style={{ margin: '5px' }} type="primary" onClick={showModal}>
                充值
              </Button>
              <Modal
                title="请输入充值次数"
                open={isModalOpen}
                onOk={handleOk}
                onCancel={handleCancel}
              >
                <InputNumber min={1} max={1000} defaultValue={1} onChange={onChange} />
              </Modal>
            </Col>
          </Row>
        </Card>
      </Descriptions>
      <Divider style={{ fontWeight: 'bold', color: 'blue' }}>用户信息</Divider>
      <Descriptions
        bordered
        column={1}
        size={'default'}
        contentStyle={{ fontWeight: 'bold', color: 'blue', maxWidth: '200px' }}
      >
        <Descriptions.Item style={{ textAlign: 'center' }} label="用户名：">
          {myUser.userName}
        </Descriptions.Item>
        <Descriptions.Item style={{ textAlign: 'center' }} label="性别：">
          {myUser.gender}
        </Descriptions.Item>
        <Descriptions.Item style={{ textAlign: 'center' }} label="用户编号：">
          {userId}
        </Descriptions.Item>
        <Descriptions.Item style={{ textAlign: 'center' }} label="用户账户：">
          {myUser.userAccount}
        </Descriptions.Item>
        <Descriptions.Item style={{ textAlign: 'center' }} label="我的身份：">
          {myUser.userRole === 'user' ? '普通用户' : '管理员'}
        </Descriptions.Item>
        <Descriptions.Item style={{ textAlign: 'center' }} label="手机号码：">
          {myUser.phone === null ? '尚未填写手机号码！' : myUser.phone}
        </Descriptions.Item>
        <Descriptions.Item style={{ textAlign: 'center' }} label="我的邮箱：">
          {myUser.email === null ? '尚未填写邮箱！' : myUser.email}
        </Descriptions.Item>
        <Descriptions.Item style={{ textAlign: 'center', color: '#1bf113' }} label="我的状态：">
          {/*@ts-ignore*/}
          {myUser.userStatus === 0 ? '正常在线' : '账号异常'}
        </Descriptions.Item>
        <Descriptions.Item style={{ textAlign: 'center' }} label="用户创建时间：">
          {myUser.createTime}
        </Descriptions.Item>
        <Descriptions.Item style={{ textAlign: 'center' }} label="用户更新时间：">
          {myUser.updateTime}
        </Descriptions.Item>
      </Descriptions>

      <ModalForm<API.UserUpdateMyRequest>
        title="修改我的信息"
        trigger={
          <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
            <Button
              type="primary"
              shape="round"
              size="large"
              block
              style={{ margin: '50px', width: '250px' }}
            >
              修改个人信息
            </Button>
          </div>
        }
        autoFocusFirstInput
        modalProps={{
          destroyOnClose: true,
          onCancel: () => console.log('run'),
        }}
        submitTimeout={2000}
        onFinish={async (values) => {
          await waitTime(1000);
          //点击发起请求
          const isModify = await updateMyInfoUsingPOST(values);
          if (isModify) {
            message.success('修改成功！');
            // 刷新用户信息表单
            location.reload();
            return true;
          } else {
            message.error('修改失败！');
          }
          return false;
        }}
      >
        <ProForm.Group>
          <ProFormText
            width="md"
            name="userName"
            label="用户名"
            placeholder="请输入用户名"
            initialValue={myUser.userName}
            rules={[
              {
                required: true,
                message: '请输入选择用户头像!',
              },
            ]}
          />
          <ProFormText
            width="md"
            name="userAccount"
            label="账号名称"
            placeholder="你想修改的账号名称"
            initialValue={myUser.userAccount}
            rules={[
              {
                required: true,
                message: '请输入选择用户头像!',
              },
            ]}
          />
          <ProFormText
            width="md"
            name="id"
            label="用户编号"
            placeholder="修改修改后的密码"
            initialValue={userId}
            disabled
          />
          <ProFormText
            width="md"
            name="userPassword"
            label="修改密码"
            placeholder="修改修改后的密码"
            initialValue={myUser.userPassword}
            rules={[
              {
                required: true,
                message: '请输入选择用户头像!',
              },
            ]}
          />
          <ProFormText
            width="md"
            name="phone"
            label="修改手机号码"
            placeholder="修改手机号码密码"
            initialValue={myUser.phone}
          />
          <ProFormSelect
            width="md"
            name="gender"
            label="修改性别"
            placeholder="修改我的性别"
            options={selectGender}
            initialValue={myUser.gender}
          />
          <ProFormText
            width="md"
            name="email"
            label="修改邮箱"
            placeholder="修改修改后的邮箱"
            initialValue={myUser.email}
          />
          <ProFormText
            width="md"
            name="userCode"
            label="修改我的编码"
            placeholder="输入修改后的编码"
            initialValue={myUser.userCode}
          />
          <ProFormSelect
            name="userAvatar"
            fieldProps={{
              size: 'large',
            }}
            label="修改头像"
            options={selectAvatarUrl}
            placeholder={'请选择用户头像 '}
            initialValue={myUser.userAvatar}
            rules={[
              {
                required: true,
                message: '请输入选择用户头像!',
              },
            ]}
          />
        </ProForm.Group>
      </ModalForm>
    </>
  );
};

export default UserInfo;
