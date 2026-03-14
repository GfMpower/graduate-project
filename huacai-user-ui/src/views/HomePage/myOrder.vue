<template>
  <!-- 订单容器 -->
    <div class="order-container">
        <!-- 订单状态筛选区域 -->
        <div class="order-filter">
            <el-tabs v-model="activeStatus" @tab-change="fetchOrders">
                <!-- 遍历合并后的订单状态选项生成标签页 -->
                <el-tab-pane
                        v-for="dict in combinedOrderStatus"
                        :key="dict.name"
                        :label="dict.label"
                        :name="dict.name">
                </el-tab-pane>
            </el-tabs>
        </div>

        <!-- 订单列表区域 -->
        <div class="order-list" v-loading="loading">
            <!-- 空订单状态显示 -->
            <div v-if="total === 0" class="empty-order">
                <el-empty description="暂无订单">
                    <el-button type="primary" @click="router.push('/index/products')">去逛逛</el-button>
                </el-empty>
            </div>

            <!-- 订单项循环 -->
            <div class="order-item" v-for="order in ordersList" :key="order.ordersId">
                <!-- 订单头部信息 -->
                <div class="order-header">
                    <div class="order-info">
                        <!-- 订单编号 -->
                        <span class="order-no">订单号: {{ order.ordersId }}</span>
                        <!-- 下单时间 -->
                        <span class="order-time">下单时间: {{ order.createTime }}</span>
                    </div>
                    <!-- 订单状态提示 -->
                    <div class="order-status">
                        <dict-tag :options="order_status" :value="order.status"/>
                    </div>
                </div>

                <!-- 订单产品信息 -->
                <div class="order-products">
                    <!-- 遍历订单中的产品 -->
                    <div class="product-item" v-for="item in order.ordersProductsList" :key="item.opId">
                        <!-- 产品图片 -->
                        <img :src="baseUrl + item.image" alt="" class="product-image">
                        <!-- 产品信息 -->
                        <div class="product-info">
                            <div class="product-name">{{ item.productsName }}</div>
                            <div class="product-farmersName">来自农户: {{ order.farmersName }}</div>
                            <div class="product-specs">
                                <span>规格: {{ item.specs }}</span>
                            </div>
                        </div>
                        <!-- 产品单价 -->
                        <div class="product-price">¥{{ item.price.toFixed(2) }}</div>
                        <!-- 产品数量 -->
                        <div class="product-quantity">x{{ item.quantity }}</div>
                        <!-- 产品小计 -->
                        <div class="product-subtotal">¥{{ (item.price * item.quantity).toFixed(2) }}</div>
                    </div>
                </div>

                <!-- 订单汇总信息 -->
                <div class="order-summary">
                    <div class="summary-info">
                        <!-- 产品总数和总金额 -->
                        共{{ order.ordersProductsList.length }}件产品 合计:
                        <span class="total-amount">¥{{ order.totalPrice.toFixed(2) }}</span>
                    </div>
                    <!-- 订单操作区域 -->
                    <div class="order-actions">
                        <!-- 查看订单详情按钮 -->
                        <el-button size="small" @click="viewOrderDetail(order.ordersId)">订单详情</el-button>
                        <!-- 待付款状态下的操作按钮 -->
                        <template v-if="order.status === '待付款'">
                            <el-button size="small" @click="cancelOrder(order)">取消订单</el-button>
                            <el-button type="primary" size="small" @click="openPaymentDialog(order.ordersId)">去支付
                            </el-button>
                        </template>
                        <!-- 待收货状态下的操作按钮 -->
                        <template v-else-if="order.status === '待收货'">
                            <el-button type="primary" size="small" @click="receipt(order)">确认收货</el-button>
                        </template>
                        <!-- 已完成状态下的评论按钮 -->
                        <template v-else-if="order.status === '已完成'">
                            <el-button 
                                type="success" 
                                size="small" 
                                @click="openReviewDialog(order)"
                                v-if="!order.isReviewed"
                            >
                                发表评论
                            </el-button>
                            <el-button 
                                type="info" 
                                size="small" 
                                disabled
                                v-else
                            >
                                已评价
                            </el-button>
                        </template>
                    </div>
                </div>
            </div>

            <!-- 分页组件 -->
            <div class="pagination">
                <pagination
                        v-show="total>0"
                        :total="total"
                        v-model:page="queryParams.pageNum"
                        v-model:limit="queryParams.pageSize"
                        @pagination="getList"
                />
            </div>
        </div>

        <!-- 订单详情对话框 -->
        <vxe-modal title="订单详情" v-model="open" width="700px" show-maximize showFooter resize>
            <div class="order-detail" v-if="currentOrder">
                <!-- 订单基本信息区域 -->
                <div class="detail-section">
                    <h3>订单信息</h3>
                    <div class="detail-row">
                        <div class="detail-col">
                            <span class="detail-label">订单号:</span>
                            <span class="detail-value">{{ currentOrder.ordersId }}</span>
                        </div>
                        <div class="detail-col">
                            <span class="detail-label">下单时间:</span>
                            <span class="detail-value">{{ currentOrder.createTime }}</span>
                        </div>
                        <div class="detail-col">
                            <span class="detail-label">订单状态:</span>
                            <dict-tag :options="order_status" :value="currentOrder.status"/>
                        </div>
                    </div>
                    <!-- 发货时间 (如果有) -->
                    <div class="detail-row" v-if="currentOrder.shippingTime">
                        <div class="detail-col">
                            <span class="detail-label">发货时间:</span>
                            <span class="detail-value">{{ currentOrder.shippingTime }}</span>
                        </div>
                    </div>
                    <!-- 完成时间 (如果有) -->
                    <div class="detail-row" v-if="currentOrder.shippingTime">
                        <div class="detail-col">
                            <span class="detail-label">完成时间:</span>
                            <span class="detail-value">{{ currentOrder.completeTime }}</span>
                        </div>
                    </div>
                </div>

                <!-- 收货信息区域 -->
                <div class="detail-section">
                    <h3>收货信息</h3>
                    <div class="detail-row">
                        <div class="detail-col">
                            <span class="detail-label">收货人:</span>
                            <span class="detail-value">{{ currentOrder.name }}</span>
                        </div>
                        <div class="detail-col">
                            <span class="detail-label">联系电话:</span>
                            <span class="detail-value">{{ currentOrder.phone }}</span>
                        </div>
                    </div>
                    <div class="detail-row">
                        <div class="detail-col">
                            <span class="detail-label">收货地址:</span>
                            <span class="detail-value">{{ currentOrder.address }}</span>
                        </div>
                    </div>
                </div>

                <!-- 产品信息区域 -->
                <div class="detail-section">
                    <h3>产品信息</h3>
                    <div class="detail-products">
                        <!-- 遍历产品信息 -->
                        <div class="product-item" v-for="item in currentOrder.ordersProductsList" :key="item.opId">
                            <img :src="baseUrl + item.image" alt="" class="product-image">
                            <!-- 产品信息 -->
                            <div class="product-info">
                                <div class="product-name">{{ item.productsName }}</div>
                                <div class="product-specs">
                                    <span>规格: {{ item.specs }}</span>
                                </div>
                            </div>
                            <!-- 产品单价 -->
                            <div class="product-price">¥{{ item.price.toFixed(2) }}</div>
                            <!-- 产品数量 -->
                            <div class="product-quantity">x{{ item.quantity }}</div>
                            <!-- 产品小计 -->
                            <div class="product-subtotal">¥{{ (item.price * item.quantity).toFixed(2) }}</div>
                        </div>
                    </div>
                </div>

                <!-- 订单金额区域 -->
                <div class="detail-section">
                    <h3>订单金额</h3>
                    <div class="detail-amount">
                        <div class="amount-row">
                            <span>产品总价: </span>
                            <span class="amount-value">¥{{ currentOrder.totalPrice.toFixed(2) }}</span>
                        </div>
                    </div>
                </div>

            </div>
        </vxe-modal>

    </div>

    <!-- 支付方式选择对话框 -->
    <el-dialog
        v-model="paymentDialogVisible"
        title="选择支付方式"
        width="500px"
        :close-on-click-modal="false"
        center
    >
        <div class="payment-methods">
            <h3 class="payment-title">选择支付方式</h3>
            <div class="method-list">
                <!-- 微信支付 -->
                <div
                    class="method-item"
                    :class="{'active': selectedMethod === 'wechat'}"
                    @click="selectedMethod = 'wechat'"
                >
                    <div class="method-icon">
                        <SvgIcon style="font-size: 40px" icon-class="微信"/>
                    </div>
                    <div class="method-name">微信支付</div>
                    <div class="method-check">
                        <el-icon v-if="selectedMethod === 'wechat'"><Select/></el-icon>
                    </div>
                </div>

                <!-- 支付宝支付 -->
                <div
                    class="method-item"
                    :class="{'active': selectedMethod === 'alipay'}"
                    @click="selectedMethod = 'alipay'"
                >
                    <div class="method-icon">
                        <SvgIcon style="font-size: 40px" icon-class="支付宝"/>
                    </div>
                    <div class="method-name">支付宝支付</div>
                    <div class="method-check">
                        <el-icon v-if="selectedMethod === 'alipay'"><Select/></el-icon>
                    </div>
                </div>

                <!-- 银行卡支付 -->
                <div
                    class="method-item"
                    :class="{'active': selectedMethod === 'bank'}"
                    @click="selectedMethod = 'bank'"
                >
                    <div class="method-icon">
                        <SvgIcon style="font-size: 40px" icon-class="银行卡"/>
                    </div>
                    <div class="method-name">银行卡支付</div>
                    <div class="method-check">
                        <el-icon v-if="selectedMethod === 'bank'"><Select/></el-icon>
                    </div>
                </div>
            </div>
        </div>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="cancelPayment">取消</el-button>
                <el-button type="primary" @click="confirmPaymentMethod">确认支付</el-button>
            </span>
        </template>
    </el-dialog>

    <!-- 评论对话框 -->
    <el-dialog
        v-model="reviewDialogVisible"
        title="发表评价"
        width="500px"
        :close-on-click-modal="false"
        center
    >
        <div class="review-form">
            <div class="review-product" v-if="currentReviewOrder && currentReviewOrder.ordersProductsList">
                <img :src="baseUrl + currentReviewOrder.ordersProductsList[0].image" class="review-product-img" />
                <div class="review-product-info">
                    <div class="review-product-name">{{ currentReviewOrder.ordersProductsList[0].productsName }}</div>
                    <div class="review-product-specs">规格: {{ currentReviewOrder.ordersProductsList[0].specs }}</div>
                </div>
            </div>
            <div class="review-rating">
                <span class="review-label">商品评分</span>
                <el-rate v-model="reviewForm.rating" show-score />
            </div>
            <div class="review-content">
                <span class="review-label">评价内容</span>
                <el-input 
                    v-model="reviewForm.content" 
                    type="textarea" 
                    :rows="4" 
                    placeholder="请输入您的评价..."
                    maxlength="500"
                    show-word-limit
                />
            </div>
        </div>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="reviewDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="submitReview" :loading="submitting">提交评价</el-button>
            </span>
        </template>
    </el-dialog>
</template>

<script setup>
import {ref, computed, getCurrentInstance} from "vue";
import useUserStore from "@/store/modules/user.js";
import {listOrders, payment, updateOrders} from "@/api/assisting/orders.js";
import {addReviews} from "@/api/assisting/reviews.js";
import {useRouter} from "vue-router";
import {ElLoading, ElMessage, ElMessageBox} from "element-plus";
import {Select} from "@element-plus/icons-vue";
import SvgIcon from "@/components/SvgIcon/index.vue";

const router = useRouter()
const {proxy} = getCurrentInstance()
const {order_status} = proxy.useDict('order_status')
const baseUrl = import.meta.env.VITE_APP_BASE_API

//是否打开对话框
const open = ref(false)

//当前选中的订单详情
const currentOrder = ref(null)

//查看订单详情的方法
const viewOrderDetail = (ordersId) => {
    //从订单列表中查找对应的订单
    currentOrder.value = ordersList.value.find(order => order.ordersId === ordersId)
    //打开对话框
    open.value = true
}

//订单状态筛选参数
const activeStatus = ref('全部订单')

// 计算属性：合并字典数据，添加"全部订单"选项
const combinedOrderStatus = computed(() => [
    {label: "全部订单", name: "全部订单"}, // 手动添加
    ...(order_status.value || []).map(item => ({
        label: item.label,
        value: item.value,
        name: item.value
    }))
])

//根据订单状态查询列表数据
const fetchOrders = () => {
    // 设置查询参数中的状态
    queryParams.value.status = activeStatus.value
    //获取列表
    getList()
}

//确认收货方法
const receipt = (order) => {
    ElMessageBox.confirm(
        '确认收到产品了吗?',
        '提示',
        {confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning',}
    )
        .then(() => {
            //构建更新数据
            const item = {
                ordersId: order.ordersId,
                status: '已完成',
                completeTime: new Date().getTime()
            }
            //调用api更新订单状态
            updateOrders(item).then(res => {
                //刷新列表
                getList()
                //显示成功提示
                ElMessage({type: 'success', message: '确认收货成功~',})
            })
        })
        .catch(() => {
            //用户取消操作
        })
}

//支付方式
const selectedMethod = ref('wechat')
//支付对话框可见性
const paymentDialogVisible = ref(false)
//当前要支付的订单ID
const currentPaymentOrderId = ref('')

//打开支付对话框
const openPaymentDialog = (ordersId) => {
    currentPaymentOrderId.value = ordersId
    paymentDialogVisible.value = true
}

//确认支付
const confirmPaymentMethod = () => {
    paymentDialogVisible.value = false
    
    ElMessageBox.confirm(
        `确认使用${selectedMethod.value === 'wechat' ? '微信' : selectedMethod.value === 'alipay' ? '支付宝' : '银行卡'}支付该订单吗`,
        '提示',
        {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then(() => {
            //打开加载状态
            const loading = ElLoading.service({
                lock: true,
                text: '支付中...',
                background: 'rgba(0, 0, 0, 0.7)',
            })
            //调用支付api进行支付
            payment(currentPaymentOrderId.value).then(res => {
                //提示支付成功
                ElMessage({type: 'success', message: '支付成功~',})
                //刷新列表
                getList()
                //关闭加载状态
                loading.close()
            })
        })
        .catch(() => {
            ElMessage({type: 'info', message: '取消支付',})
        })
}

//取消支付
const cancelPayment = () => {
    paymentDialogVisible.value = false
    currentPaymentOrderId.value = ''
    selectedMethod.value = 'wechat'
}

// 评论相关数据
const reviewDialogVisible = ref(false)
const submitting = ref(false)
const reviewForm = ref({
    productsId: '',
    orderId: '',
    farmersUserId: '',
    rating: 5,
    content: ''
})
const currentReviewOrder = ref(null)

// 打开评论对话框
const openReviewDialog = (order) => {
    currentReviewOrder.value = order
    // 获取订单中的第一个产品进行评价（简化处理）
    const product = order.ordersProductsList[0]
    reviewForm.value = {
        productsId: product.productsId,
        orderId: order.ordersId,
        farmersUserId: order.productsUserId,
        rating: 5,
        content: ''
    }
    reviewDialogVisible.value = true
}

// 提交评论
const submitReview = () => {
    if (!reviewForm.value.content.trim()) {
        ElMessage.warning('请输入评价内容')
        return
    }
    
    submitting.value = true
    addReviews(reviewForm.value).then(() => {
        ElMessage.success('评价成功')
        reviewDialogVisible.value = false
        // 标记订单已评价
        currentReviewOrder.value.isReviewed = true
        submitting.value = false
    }).catch(() => {
        submitting.value = false
    })
}

//取消订单方法
const cancelOrder = (order) => {
    ElMessageBox.confirm(
        '确定要取消该订单吗?',
        '提示',
        {confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning',}
    )
        .then(() => {
            //构建更新数据
            const item = {
                ordersId: order.ordersId,
                status: '已取消',
            }
            //调用api更新订单状态
            updateOrders(item).then(res => {
                //刷新列表
                getList()
                //显示成功提示
                ElMessage({type: 'success', message: '已取消订单',})
            })
        })
        .catch(() => {
            //用户取消操作
        })
}

//加载状态
const loading = ref(false)

//当前用户的信息
const loginUser = useUserStore()

//查询参数
const queryParams = ref({
    pageNum: 1,
    pageSize: 10,
    ordersId: null,
    status: '全部订单',
    userId: loginUser.id
})

//列表数据总数
const total = ref(0)

//订单列表数据
const ordersList = ref([])

// 获取我的订单列表
const getList = () => {
    //打开加载状态
    loading.value = true
    //如果状态是全部订单, 则设置null查询所有状态
    if (queryParams.value.status === '全部订单') {
        queryParams.value.status = null
    }
    //调用api获取订单列表
    listOrders(queryParams.value).then(res => {
        //设置总数
        total.value = res.total
        //设置订单列表数据
        ordersList.value = res.rows
        //关闭加载状态
        loading.value = false
    })
}

//组件挂载时调用
onMounted(() => {
    getList()
})
</script>

<style scoped>
/* 订单容器样式 */
.order-container {
    max-width: 1200px; /* 最大宽度 */
    margin: 0 auto; /* 水平居中 */
    padding: 20px; /* 内边距 */
}

/* 订单筛选区域样式 */
.order-filter {
    background-color: #fff; /* 背景色 */
    padding: 15px 20px; /* 内边距 */
    margin-bottom: 20px; /* 下边距 */
    border-radius: 8px; /* 圆角 */
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1); /* 阴影效果 */
}

/* 订单列表区域样式 */
.order-list {
    background-color: #fff; /* 背景色 */
    padding: 20px; /* 内边距 */
    border-radius: 8px; /* 圆角 */
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1); /* 阴影效果 */
}

/* 空订单状态样式 */
.empty-order {
    padding: 50px 0; /* 上下内边距 */
}

/* 单个订单项样式 */
.order-item {
    margin-bottom: 20px; /* 下边距 */
    border: 1px solid #eee; /* 边框 */
    border-radius: 4px; /* 圆角 */
    overflow: hidden; /* 溢出隐藏 */
}

/* 最后一个订单项取消下边距 */
.order-item:last-child {
    margin-bottom: 0;
}

/* 订单头部样式 */
.order-header {
    display: flex; /* 弹性布局 */
    justify-content: space-between; /* 两端对齐 */
    align-items: center; /* 垂直居中 */
    padding: 15px; /* 内边距 */
    background-color: #f9f9f9; /* 背景色 */
    border-bottom: 1px solid #eee; /* 底部边框 */
}

/* 订单信息区域样式 */
.order-info {
    display: flex; /* 弹性布局 */
    align-items: center; /* 垂直居中 */
}

/* 订单编号样式 */
.order-no {
    margin-right: 20px; /* 右边距 */
    font-size: 14px; /* 字体大小 */
    color: #333; /* 字体颜色 */
}

/* 订单时间样式 */
.order-time {
    font-size: 13px; /* 字体大小 */
    color: #999; /* 字体颜色 */
}

/* 订单状态标签样式 */
.order-status .el-tag {
    font-size: 13px; /* 字体大小 */
}

/* 订单商品区域样式 */
.order-products {
    padding: 15px; /* 内边距 */
}

/* 单个商品项样式 */
.product-item {
    display: flex; /* 弹性布局 */
    align-items: center; /* 垂直居中 */
    padding: 10px 0; /* 上下内边距 */
}

/* 商品项之间添加分隔线 */
.product-item:not(:last-child) {
    border-bottom: 1px dashed #eee; /* 底部虚线边框 */
}

/* 商品图片样式 */
.product-image {
    width: 80px; /* 宽度 */
    height: 80px; /* 高度 */
    object-fit: cover; /* 图片填充方式 */
    margin-right: 15px; /* 右边距 */
    border-radius: 4px; /* 圆角 */
}

/* 商品信息区域样式 */
.product-info {
    flex: 1; /* 弹性扩展 */
    margin-right: 15px; /* 右边距 */
}

/* 商品名称样式 */
.product-name {
    font-size: 14px; /* 字体大小 */
    color: #333; /* 字体颜色 */
    margin-bottom: 5px; /* 下边距 */
}

/* 农户名称样式 */
.product-farmersName {
    font-size: 12px; /* 字体大小 */
    color: #999; /* 字体颜色 */
    margin-bottom: 5px; /* 下边距 */
}

/* 商品规格样式 */
.product-specs {
    font-size: 12px; /* 字体大小 */
    color: #999; /* 字体颜色 */
    margin-bottom: 5px; /* 下边距 */
}

/* 规格项间距 */
.product-specs span {
    margin-right: 8px; /* 右边距 */
}

/* 商品价格、数量和总价样式 */
.product-price,
.product-quantity,
.product-subtotal {
    width: 100px; /* 固定宽度 */
    text-align: center; /* 文本居中 */
    font-size: 14px; /* 字体大小 */
    color: #333; /* 字体颜色 */
}

/* 商品总价样式 */
.product-subtotal {
    color: #f56c6c; /* 红色字体 */
    font-weight: bold; /* 粗体 */
}

/* 订单汇总区域样式 */
.order-summary {
    display: flex; /* 弹性布局 */
    justify-content: space-between; /* 两端对齐 */
    align-items: center; /* 垂直居中 */
    padding: 15px; /* 内边距 */
    background-color: #f9f9f9; /* 背景色 */
    border-top: 1px solid #eee; /* 顶部边框 */
}

/* 汇总信息样式 */
.summary-info {
    font-size: 14px; /* 字体大小 */
    color: #666; /* 字体颜色 */
}

/* 总金额样式 */
.total-amount {
    color: #f56c6c; /* 红色字体 */
    font-weight: bold; /* 粗体 */
    font-size: 16px; /* 字体大小 */
}

/* 订单操作按钮区域样式 */
.order-actions {
    display: flex; /* 弹性布局 */
    gap: 10px; /* 按钮间距 */
}

/* 分页样式 */
.pagination {
    margin-top: 30px; /* 上边距 */
    text-align: center; /* 文本居中 */
}

/* 订单详情区域样式 */
.order-detail {
    padding: 10px; /* 内边距 */
}

/* 详情区域样式 */
.detail-section {
    margin-bottom: 25px; /* 下边距 */
}

/* 详情区域标题样式 */
.detail-section h3 {
    margin: 0 0 15px 0; /* 外边距 */
    font-size: 16px; /* 字体大小 */
    color: #333; /* 字体颜色 */
    padding-bottom: 8px; /* 底部内边距 */
    border-bottom: 1px solid #eee; /* 底部边框 */
}

/* 详情行样式 */
.detail-row {
    display: flex; /* 弹性布局 */
    margin-bottom: 10px; /* 下边距 */
}

/* 详情列样式 */
.detail-col {
    flex: 1; /* 弹性扩展 */
    display: flex; /* 弹性布局 */
}

/* 详情标签样式 */
.detail-label {
    width: 80px; /* 固定宽度 */
    color: #999; /* 字体颜色 */
    font-size: 14px; /* 字体大小 */
}

/* 详情值样式 */
.detail-value {
    flex: 1; /* 弹性扩展 */
    font-size: 14px; /* 字体大小 */
    color: #333; /* 字体颜色 */
}

/* 详情商品区域样式 */
.detail-products {
    border: 1px solid #eee; /* 边框 */
    border-radius: 4px; /* 圆角 */
}

/* 详情金额区域样式 */
.detail-amount {
    width: 300px; /* 固定宽度 */
    margin-left: auto; /* 左外边距自动，实现右对齐 */
}

/* 金额行样式 */
.amount-row {
    display: flex; /* 弹性布局 */
    justify-content: space-between; /* 两端对齐 */
    margin-bottom: 10px; /* 下边距 */
    font-size: 14px; /* 字体大小 */
    color: #666; /* 字体颜色 */
}

/* 总金额行样式 */
.amount-row.total {
    margin-top: 15px; /* 上边距 */
    padding-top: 10px; /* 顶部内边距 */
    border-top: 1px solid #eee; /* 顶部边框 */
    font-size: 15px; /* 字体大小 */
    color: #333; /* 字体颜色 */
    font-weight: bold; /* 粗体 */
}

/* 金额值样式 */
.amount-value {
    color: #f56c6c; /* 红色字体 */
}

/* 响应式设计 - 移动端适配 */
@media (max-width: 768px) {
    /* 移动端订单头部改为垂直布局 */
    .order-header {
        flex-direction: column; /* 垂直方向 */
        align-items: flex-start; /* 左对齐 */
    }

    /* 移动端订单状态调整 */
    .order-status {
        margin-top: 10px; /* 上边距 */
    }

    /* 移动端商品项换行 */
    .product-item {
        flex-wrap: wrap; /* 允许换行 */
    }

    /* 移动端商品信息全宽 */
    .product-info {
        width: 100%; /* 全宽 */
        margin-bottom: 10px; /* 下边距 */
    }

    /* 移动端价格数量等调整 */
    .product-price,
    .product-quantity,
    .product-subtotal {
        width: auto; /* 自动宽度 */
        margin-right: 15px; /* 右边距 */
    }

    /* 移动端订单汇总垂直布局 */
    .order-summary {
        flex-direction: column; /* 垂直方向 */
        align-items: flex-end; /* 右对齐 */
        gap: 15px; /* 元素间距 */
    }

    /* 移动端详情行垂直布局 */
    .detail-row {
        flex-direction: column; /* 垂直方向 */
        margin-bottom: 15px; /* 下边距 */
    }

    /* 移动端详情列调整 */
    .detail-col {
        margin-bottom: 8px; /* 下边距 */
    }

    /* 移动端金额区域全宽 */
    .detail-amount {
        width: 100%; /* 全宽 */
    }
}

/* 支付方式样式 */
.payment-methods {
    margin-top: 30px; /* 顶部外边距 */
    padding: 20px; /* 内边距 */
    background-color: #fff; /* 背景色 */
    border-radius: 8px; /* 圆角 */
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1); /* 阴影效果 */
}

.payment-title {
    margin: 0 0 20px 0; /* 外边距 */
    font-size: 16px; /* 字体大小 */
    color: #333; /* 字体颜色 */
}

.method-list {
    display: flex; /* 弹性布局 */
    flex-direction: column; /* 垂直方向排列 */
    gap: 15px; /* 项间距 */
    align-items: center; /* 水平居中 */
}

.method-item {
    display: flex; /* 弹性布局 */
    align-items: center; /* 垂直居中 */
    padding: 15px 30px; /* 内边距 */
    border: 1px solid #eee; /* 边框 */
    border-radius: 4px; /* 圆角 */
    cursor: pointer; /* 鼠标指针样式 */
    transition: all 0.3s; /* 过渡效果 */
    width: 100%; /* 宽度100% */
    max-width: 400px; /* 最大宽度 */
}

.method-item:hover {
    border-color: #3AAE6E; /* 悬停边框颜色 */
}

.method-item.active {
    border-color: #3AAE6E; /* 激活状态边框颜色 */
    background-color: #f0f9eb; /* 激活状态背景色 */
}

.method-icon {
    width: 40px; /* 宽度 */
    height: 40px; /* 高度 */
    margin-right: 15px; /* 右边距 */
}

.method-name {
    flex: 1; /* 弹性扩展 */
    font-size: 14px; /* 字体大小 */
    color: #333; /* 字体颜色 */
}

.method-check {
    width: 20px; /* 宽度 */
    color: #3AAE6E; /* 颜色 */
}

/* 评论表单样式 */
.review-form {
    padding: 20px;
}

.review-product {
    display: flex;
    align-items: center;
    padding: 15px;
    background-color: #f9f9f9;
    border-radius: 8px;
    margin-bottom: 20px;
}

.review-product-img {
    width: 80px;
    height: 80px;
    object-fit: cover;
    border-radius: 4px;
    margin-right: 15px;
}

.review-product-info {
    flex: 1;
}

.review-product-name {
    font-size: 14px;
    color: #333;
    margin-bottom: 5px;
}

.review-product-specs {
    font-size: 12px;
    color: #999;
}

.review-rating {
    display: flex;
    align-items: center;
    margin-bottom: 20px;
}

.review-label {
    font-size: 14px;
    color: #333;
    margin-right: 15px;
    min-width: 70px;
}

.review-content {
    display: flex;
    flex-direction: column;
}
</style>
