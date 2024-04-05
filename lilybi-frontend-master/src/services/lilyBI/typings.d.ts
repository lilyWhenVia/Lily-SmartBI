declare namespace API {
  type addOrderUsingPOSTParams = {
    /** total */
    total?: number;
  };

  type addUsingGETParams = {
    /** name */
    name?: string;
  };

  type AiAssistant = {
    createTime?: string;
    executorMessage?: string;
    id?: number;
    isDelete?: number;
    questionGoal?: string;
    questionName?: string;
    questionResult?: string;
    questionStatus?: string;
    questionType?: string;
    updateTime?: string;
    userId?: number;
  };

  type AiAssistantAddRequest = {
    questionGoal?: string;
    questionName?: string;
    questionResult?: string;
    questionType?: string;
  };

  type AiAssistantEditRequest = {
    executorMessage?: string;
    id?: number;
    questionGoal?: string;
    questionName?: string;
    questionResult?: string;
    questionStatus?: string;
    questionType?: string;
  };

  type AiAssistantQueryRequest = {
    current?: number;
    executorMessage?: string;
    id?: number;
    pageSize?: number;
    questionGoal?: string;
    questionName?: string;
    questionResult?: string;
    questionStatus?: string;
    questionType?: string;
    sortField?: string;
    sortOrder?: string;
    userId?: number;
  };

  type AiAssistantUpdateRequest = {
    executorMessage?: string;
    id?: number;
    questionGoal?: string;
    questionName?: string;
    questionResult?: string;
    questionStatus?: string;
    questionType?: string;
    userId?: number;
  };

  type AiFrequencyOrder = {
    createTime?: string;
    id?: number;
    isDelete?: number;
    orderStatus?: number;
    price?: number;
    purchaseQuantity?: number;
    totalAmount?: number;
    updateTime?: string;
    userId?: number;
  };

  type AiFrequencyOrderCancelRequest = {
    createTime?: string;
    id?: number;
    orderStatus?: number;
    price?: number;
    purchaseQuantity?: number;
    totalAmount?: number;
    updateTime?: string;
    userId?: number;
  };

  type AiFrequencyOrderQueryRequest = {
    createTime?: string;
    current?: number;
    id?: number;
    orderStatus?: number;
    pageSize?: number;
    price?: number;
    purchaseQuantity?: number;
    sortField?: string;
    sortOrder?: string;
    totalAmount?: number;
    updateTime?: string;
    userId?: number;
  };

  type AiFrequencyOrderUpdateRequest = {
    createTime?: string;
    id?: number;
    orderStatus?: number;
    price?: number;
    purchaseQuantity?: number;
    totalAmount?: number;
    updateTime?: string;
    userId?: number;
  };

  type AiFrequencyOrderVO = {
    createTime?: string;
    id?: number;
    orderStatus?: number;
    price?: number;
    purchaseQuantity?: number;
    totalAmount?: number;
    updateTime?: string;
    userId?: number;
  };

  type AiFrequencyRechargeUsingPOSTParams = {
    frequency?: number;
  };

  /** ai对话剩余次数 **/
  type AiFrequencyVO = {
    id?: number;
    remainFrequency?: number;
    totalFrequency?: number;
    userId?: number;
  };

  type AlipayInfo = {
    alipayAccountNo?: number;
    alipayId?: string;
    createTime?: string;
    id?: number;
    isDelete?: number;
    orderId?: number;
    payStatus?: number;
    totalAmount?: number;
    updateTime?: string;
    userId?: number;
  };

  type AlipayInfoQueryRequest = {
    alipayAccountNo?: number;
    alipayId?: string;
    createTime?: string;
    current?: number;
    id?: number;
    orderId?: number;
    pageSize?: number;
    payStatus?: number;
    sortField?: string;
    sortOrder?: string;
    totalAmount?: number;
    updateTime?: string;
    userId?: number;
  };

  type AlipayInfoVO = {
    alipayAccountNo?: string;
    alipayId?: string;
    orderId?: number;
    payStatus?: number;
    qrCode?: string;
    totalAmount?: number;
    userId?: number;
  };

  type BaseResponseAiAssistant_ = {
    code?: number;
    data?: AiAssistant;
    message?: string;
  };

  type BaseResponseAiFrequencyVO_ = {
    code?: number;
    data?: AiFrequencyVO;
    message?: string;
  };

  type BaseResponseAlipayInfoVO_ = {
    code?: number;
    data?: AlipayInfoVO;
    message?: string;
  };

  type BaseResponseBiResponse_ = {
    code?: number;
    data?: BiResponse;
    message?: string;
  };

  type BaseResponseBoolean_ = {
    code?: number;
    data?: boolean;
    message?: string;
  };

  type BaseResponseChart_ = {
    code?: number;
    data?: Chart;
    message?: string;
  };

  type BaseResponseListAiFrequencyOrderVO_ = {
    code?: number;
    data?: AiFrequencyOrderVO[];
    message?: string;
  };

  type BaseResponseListPayInfoVO_ = {
    code?: number;
    data?: PayInfoVO[];
    message?: string;
  };

  type BaseResponseLoginUserVO_ = {
    code?: number;
    data?: LoginUserVO;
    message?: string;
  };

  type BaseResponseLong_ = {
    code?: number;
    data?: number;
    message?: string;
  };

  type BaseResponseObject_ = {
    code?: number;
    data?: Record<string, any>;
    message?: string;
  };

  type BaseResponsePageAiAssistant_ = {
    code?: number;
    data?: PageAiAssistant_;
    message?: string;
  };

  type BaseResponsePageAiFrequencyOrder_ = {
    code?: number;
    data?: PageAiFrequencyOrder_;
    message?: string;
  };

  type BaseResponsePageAlipayInfo_ = {
    code?: number;
    data?: PageAlipayInfo_;
    message?: string;
  };

  type BaseResponsePageChart_ = {
    code?: number;
    data?: PageChart_;
    message?: string;
  };

  type BaseResponsePageUser_ = {
    code?: number;
    data?: PageUser_;
    message?: string;
  };

  type BaseResponsePageUserVO_ = {
    code?: number;
    data?: PageUserVO_;
    message?: string;
  };

  type BaseResponseString_ = {
    code?: number;
    data?: string;
    message?: string;
  };

  type BaseResponseUser_ = {
    code?: number;
    data?: User;
    message?: string;
  };

  type BaseResponseUserCode_ = {
    code?: number;
    data?: UserCode;
    message?: string;
  };

  type BaseResponseUserCodeVO_ = {
    code?: number;
    data?: UserCodeVO;
    message?: string;
  };

  type BiResponse = {
    chartData?: string;
    chartId?: number;
    chartName?: string;
    chartType?: string;
    createTime?: string;
    genChart?: string;
    genResult?: string;
    goal?: string;
  };

  type Chart = {
    chartData?: string;
    chartName?: string;
    chartStatus?: number;
    chartType?: string;
    createTime?: string;
    executorMessage?: string;
    genChart?: string;
    genResult?: string;
    goal?: string;
    id?: number;
    isDelete?: number;
    updateTime?: string;
    userId?: number;
  };

  type ChartAddRequest = {
    chartData?: string;
    chartName?: string;
    chartType?: string;
    goal?: string;
  };

  type ChartEditRequest = {
    chartData?: string;
    chartName?: string;
    chartType?: string;
    goal?: string;
    id?: number;
  };

  type ChartQueryRequest = {
    chartName?: string;
    chartType?: string;
    current?: number;
    goal?: string;
    id?: number;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
    userId?: number;
  };

  type ChartUpdateRequest = {
    chartData?: string;
    chartName?: string;
    chartType?: string;
    createTime?: string;
    genChart?: string;
    genResult?: string;
    goal?: string;
    id?: number;
    isDelete?: number;
    updateTime?: string;
    userId?: number;
  };

  type DeleteRequest = {
    id?: number;
  };

  type genChartByAiAsyncMqUsingPOSTParams = {
    chartName?: string;
    chartType?: string;
    goal?: string;
  };

  type genChartByAiAsyncUsingPOSTParams = {
    chartName?: string;
    chartType?: string;
    goal?: string;
  };

  type genChartByAiUsingPOSTParams = {
    chartName?: string;
    chartType?: string;
    goal?: string;
  };

  type GenChatByAiRequest = {
    questionGoal?: string;
    questionName?: string;
    questionType?: string;
  };

  type getAiAssistantByIdUsingGETParams = {
    /** id */
    id?: number;
  };

  type getChartByIdUsingGETParams = {
    /** id */
    id?: number;
  };

  type getUserByIdUsingGETParams = {
    /** id */
    id?: number;
  };

  type getUserCodeByIdUsingGETParams = {
    /** userId */
    userId?: number;
  };

  type getUserVOByIdUsingGETParams = {
    /** id */
    id?: number;
  };

  type LoginUserVO = {
    createTime?: string;
    email?: string;
    gender?: string;
    id?: number;
    phone?: string;
    updateTime?: string;
    userAccount?: string;
    userAvatar?: string;
    userName?: string;
    userPassword?: string;
    userRole?: string;
    userStatus?: number;
  };

  type OrderItem = {
    asc?: boolean;
    column?: string;
  };

  type PageAiAssistant_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: AiAssistant[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type PageAiFrequencyOrder_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: AiFrequencyOrder[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type PageAlipayInfo_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: AlipayInfo[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type PageChart_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: Chart[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type PageUser_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: User[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type PageUserVO_ = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: UserVO[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type payCodeUsingPOSTParams = {
    /** orderId */
    orderId?: number;
  };

  type PayInfoVO = {
    alipayAccountNo?: number;
    alipayId?: string;
    createTime?: string;
    id?: number;
    orderId?: number;
    payStatus?: number;
    totalAmount?: number;
    updateTime?: string;
    userId?: number;
  };

  type payUsingGETParams = {
    /** alipayAccountNo */
    alipayAccountNo?: string;
  };

  type queryPayResultFromAlipayUsingPOSTParams = {
    /** alipayAccountNo */
    alipayAccountNo?: string;
  };

  type tradeQueryUsingPOSTParams = {
    /** alipayAccountNo */
    alipayAccountNo?: string;
  };

  type User = {
    createTime?: string;
    email?: string;
    gender?: string;
    id?: number;
    isDelete?: number;
    phone?: string;
    updateTime?: string;
    userAccount?: string;
    userAvatar?: string;
    userName?: string;
    userPassword?: string;
    userRole?: string;
    userStatus?: number;
  };

  type UserAddRequest = {
    email?: string;
    gender?: string;
    phone?: string;
    userAccount?: string;
    userAvatar?: string;
    userName?: string;
    userPassword?: string;
    userRole?: string;
    userStatus?: number;
  };

  type UserCode = {
    createTime?: string;
    id?: number;
    isDelete?: number;
    updateTime?: string;
    userId?: number;
  };

  type UserCodeVO = {
    createTime?: string;
    email?: string;
    gender?: string;
    id?: number;
    phone?: string;
    userAccount?: string;
    userAvatar?: string;
    userName?: string;
    userRole?: string;
    userStatus?: number;
  };

  type UserLoginRequest = {
    userAccount?: string;
    userPassword?: string;
  };

  type UserQueryRequest = {
    current?: number;
    email?: string;
    gender?: string;
    id?: number;
    pageSize?: number;
    phone?: string;
    sortField?: string;
    sortOrder?: string;
    userName?: string;
    userRole?: string;
    userStatus?: number;
  };

  type UserRegisterRequest = {
    checkPassword?: string;
    userAccount?: string;
    userPassword?: string;
  };

  type UserUpdateMyRequest = {
    email?: string;
    gender?: string;
    phone?: string;
    userAccount?: string;
    userAvatar?: string;
    userName?: string;
    userPassword?: string;
    userStatus?: number;
  };

  type UserUpdateRequest = {
    email?: string;
    gender?: string;
    id?: number;
    phone?: string;
    userAccount?: string;
    userAvatar?: string;
    userName?: string;
    userPassword?: string;
    userRole?: string;
    userStatus?: number;
  };

  type UserVO = {
    createTime?: string;
    email?: string;
    gender?: string;
    id?: number;
    phone?: string;
    userAccount?: string;
    userAvatar?: string;
    userName?: string;
    userRole?: string;
    userStatus?: number;
  };
}
