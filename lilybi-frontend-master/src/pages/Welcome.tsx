import { IMAGES } from '@/constants';
import { createFromIconfontCN } from '@ant-design/icons';
import { PageContainer } from '@ant-design/pro-components';
import { Alert, Card, Carousel, Divider, Image, Typography } from 'antd';
import React, { useEffect, useState } from 'react';

/**
 * 每个单独的卡片，为了复用样式抽成了组件
 */
const { Title, Paragraph } = Typography;

const MyIcon = createFromIconfontCN({
  scriptUrl: '//at.alicdn.com/t/c/font_4064432_4snwg02llsf.js', // 在 iconfont.cn 上生成
});

const BIIntroduce = `区别于传统的BI，数据分析者只需要导入最原始的数据集，输入想要进行分析的目标，就能利用AI自动生成一个符合要求的图表以及分析结论。此外，还会有图表管理、异步生成、AI对话等功能。只需输入分析目标、原始数据和原始问题，利用AI就能一键生成可视化图表、分析结论和问题解答，大幅降低人工数据分析成本。
`;
const Welcome: React.FC = () => {
  // 加载图片
  const [loadedImages, setLoadedImages] = useState([]);
  useEffect(() => {
    Promise.all(IMAGES)
      // @ts-ignore
      .then((images) => setLoadedImages(images))
      .catch((error) => console.error(error));
  }, []);

  return (
    <PageContainer>
      <Card>
        <Alert
          message={'欢迎使用Lily同学的智能BI平台！'}
          type="success"
          showIcon
          banner
          style={{
            margin: -12,
            marginBottom: 48,
          }}
        />
        <Typography.Title
          level={2}
          style={{
            textAlign: 'center',
          }}
        >
          <MyIcon type={'icon-maomaochushou'} /> lily 智能 BI 分析平台{' '}
          <MyIcon type={'icon-Artboard'} />
        </Typography.Title>
        <Paragraph>
          我们的智能BI平台是一个革命性的数据分析工具，为用户提供了轻松、快速且智能化的数据分析体验。
        </Paragraph>
        <Title level={3}>智能BI介绍</Title>
        <Paragraph>
          <pre style={{ backgroundColor: '#d2d2d2', color: 'black' }}>{BIIntroduce}</pre>
        </Paragraph>
        <Title level={3}>智能BI特点</Title>
        <Paragraph>
          1.
          自动化数据分析：项目采用AI技术，能够自动化地从原始数据中生成符合要求的图表和分析结论。用户只需要输入分析目标和原始数据，无需具备专业的数据分析知识，即可完成数据分析的过程。
          <br />
          2.
          节约人力成本：传统的数据分析通常需要数据分析师具备一定的技能和经验，并花费大量的时间进行数据清洗、转化和分析。这个项目的优势在于能够大幅度降低人工数据分析成本，让不具备数据分析能力的用户也能迅速完成数据分析任务，节约了人力资源。
          <br />
          3.
          简化操作流程：该项目提供了图表管理和异步生成的功能，使用户能够更加方便地管理和保存生成的图表，同时异步生成功能也能提升用户的操作效率。
          <br />
          4.
          可视化结果：通过使用AI接口生成分析结果，该项目能够实现将数据以可视化的方式展示出来，使用户更容易理解和解读数据，从而做出更明智的决策。
          <br />
          5.
          适用范围广：由于项目能够从最原始的数据集中进行分析，因此适用于各类行业和领域。用户只需要提供自己的数据和分析目标，即可快速得到相应的分析结果。
        </Paragraph>
        <br />
        <Divider style={{ color: 'darkred' }}>lily的logo</Divider>
        <div style={{ marginLeft: '25px' }}>
          <Carousel autoplay autoplaySpeed={10000}>
            {loadedImages.map((image, index) => (
              <div key={index}>
                <Image src={image.default} />
              </div>
            ))}
          </Carousel>
        </div>
      </Card>
    </PageContainer>
  );
};

export default Welcome;
