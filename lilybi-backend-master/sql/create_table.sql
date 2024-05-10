# 数据库初始化

-- 创建库
DROP DATABASE IF EXISTS easyBI;
create database if not exists easyBI;

-- 切换库
use easyBI;

create table chart
(
    id              bigint auto_increment comment 'id'
        primary key,
    chartStatus     tinyint  default 0                 not null comment '图表处理状态',
    executorMessage varchar(256)                       null comment '图表处理状态信息',
    goal            text                               null comment '分析目标',
    chartData       text                               null comment '目标图表数据',
    chartType       varchar(128)                       null comment '生成图表的类型',
    genChart        text                               null comment '生成的图表',
    userId          bigint                             not null comment '创建用户 id',
    createTime      datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete        tinyint  default 0                 not null comment '是否删除',
    name            varchar(128)                       null comment '图表名称',
    genResult       text                               null comment '分析结论'
)
    comment '图表信息表' collate = utf8mb4_unicode_ci;

create table chart_core_data
(
    id        bigint auto_increment comment 'id'
        primary key,
    chartId   bigint not null comment '图表信息表的id',
    genChart  text   null comment '生成的图表',
    chartData text   null comment '目标图表数据',
    createTime      datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint chart_core_data_ibfk_1
        foreign key (chartId) references chart (id)
)
    comment '图表核心数据表' collate = utf8mb4_unicode_ci;

create index chart_id
    on chart_core_data (chartId);

create table question
(
    id              bigint auto_increment comment 'id'
        primary key,
    createTime      datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    executorMessage varchar(256)                       null comment 'ai问答处理状态信息',
    isDelete        tinyint  default 0                 not null comment '是否删除',
    questionGoal    text                               not null comment '分析目标',
    questionName    varchar(128)                       null comment 'ai问答名称',
    questionResult  text                               null comment '目标ai问答数据',
    questionStatus  tinyint  default 0                 not null comment 'ai问答处理状态',
    questionType    varchar(128)                       null comment '生成ai问答的类型',
    updateTime      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    userId          bigint                             not null comment '创建用户 id'
)
    comment 'ai问答信息表' collate = utf8mb4_unicode_ci;

create table user
(
    id              bigint auto_increment comment 'id'
        primary key,
    userAccount     varchar(256)                           not null comment '账号',
    userPassword    varchar(512)                           not null comment '密码',
    unionId         varchar(256)                           null comment '微信开放平台id',
    mpOpenId        varchar(256)                           null comment '公众号openId',
    userName        varchar(256)                           null comment '用户昵称',
    userAvatar      varchar(256)                           null comment '用户头像',
    userRole        varchar(256) default 'user'            not null comment '用户角色：user/admin',
    createTime      datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime      datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete        tinyint      default 0                 not null comment '是否删除',
    remainFrequency int          default 10                not null comment '剩余ai使用次数',
    totalFrequency  int          default 0                 not null comment '已使用ai使用次数'
)
    comment '用户' collate = utf8mb4_unicode_ci;

create index idx_unionId
    on user (unionId);

