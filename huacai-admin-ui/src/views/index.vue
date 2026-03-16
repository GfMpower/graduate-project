<template>
    <div class="home-service-container">
        <!-- 主横幅 -->
        <div class="main-banner">
            <el-card shadow="never" class="transparent-card">
                <div class="banner-content">
                    <div class="banner-text">
                        <h1>豫农优品电商后台管理</h1>
                        <p class="sub-title">助力乡村振兴 · 共同富裕</p>
                        <el-divider></el-divider>
                        <p class="banner-description">
                            通过数字化手段提升农业扶贫工作的效率和管理水平，
                            连接农户与消费者，打造透明、高效的助农平台。
                        </p>
                    </div>
                </div>
            </el-card>
        </div>

        <!-- 平台优势 -->
        <div class="advantage-section">
            <h2 class="section-title">
                <el-icon>
                    <Medal/>
                </el-icon>
                平台优势
            </h2>
            <el-row :gutter="20">
                <el-col :xs="12" :sm="8" :md="8" :lg="8">
                    <div class="advantage-card">
                        <div class="advantage-icon">
                            <el-icon>
                                <Finished/>
                            </el-icon>
                        </div>
                        <h4>精准帮扶</h4>
                        <p>通过数据分析实现精准扶贫</p>
                    </div>
                </el-col>
                <el-col :xs="12" :sm="8" :md="8" :lg="8">
                    <div class="advantage-card">
                        <div class="advantage-icon">
                            <el-icon>
                                <Connection/>
                            </el-icon>
                        </div>
                        <h4>信息透明</h4>
                        <p>全流程信息公开透明</p>
                    </div>
                </el-col>
                <el-col :xs="12" :sm="8" :md="8" :lg="8">
                    <div class="advantage-card">
                        <div class="advantage-icon">
                            <el-icon>
                                <DataLine/>
                            </el-icon>
                        </div>
                        <h4>高效管理</h4>
                        <p>数字化管理提升工作效率</p>
                    </div>
                </el-col>
            </el-row>
        </div>

        <!-- 平台介绍 -->
        <div class="intro-section">
            <el-row :gutter="30">
                <el-col :xs="24" :md="12">
                    <div class="intro-card">
                        <h3>平台简介</h3>
                        <p>
                            助农扶贫系统是一个专门为农业扶贫开发的数字化管理平台。
                            系统集成了农户信息管理、农产品展销、扶贫项目跟踪、数据统计分析等功能，
                            旨在通过信息化手段提升扶贫工作的精准度和效率。
                        </p>
                    </div>
                </el-col>
                <el-col :xs="24" :md="12">
                    <div class="intro-card">
                        <h3>愿景使命</h3>
                        <p>
                            我们的愿景是通过科技手段助力乡村振兴，实现共同富裕。
                            使命是连接农户与市场，打通扶贫最后一公里，让每一份爱心都能精准传递，
                            让每一次帮扶都能产生实效。
                        </p>
                    </div>
                </el-col>
            </el-row>
        </div>

        <!-- 销售数据统计 -->
        <div class="sales-statistics-section" v-if="isFarmer || isAdmin">
            <h2 class="section-title">
                <el-icon>
                    <TrendCharts/>
                </el-icon>
                销售数据统计
            </h2>

            <!-- 时间范围选择 -->
            <div class="time-range-selector">
                <el-radio-group v-model="timeRange" @change="loadSalesData">
                    <el-radio-button label="day">近7天</el-radio-button>
                    <el-radio-button label="week">近4周</el-radio-button>
                    <el-radio-button label="month">近6个月</el-radio-button>
                    <el-radio-button label="year">近2年</el-radio-button>
                </el-radio-group>
            </div>

            <!-- 统计卡片 -->
            <el-row :gutter="20" class="statistics-cards">
                <el-col :xs="12" :sm="6">
                    <div class="stat-card">
                        <div class="stat-icon" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);">
                            <el-icon><ShoppingCart/></el-icon>
                        </div>
                        <div class="stat-content">
                            <div class="stat-value">{{ statistics.totalOrders || 0 }}</div>
                            <div class="stat-label">总订单数</div>
                        </div>
                    </div>
                </el-col>
                <el-col :xs="12" :sm="6">
                    <div class="stat-card">
                        <div class="stat-icon" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
                            <el-icon><Money/></el-icon>
                        </div>
                        <div class="stat-content">
                            <div class="stat-value">¥{{ statistics.totalAmount || 0 }}</div>
                            <div class="stat-label">总销售额</div>
                        </div>
                    </div>
                </el-col>
                <el-col :xs="12" :sm="6">
                    <div class="stat-card">
                        <div class="stat-icon" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
                            <el-icon><Box/></el-icon>
                        </div>
                        <div class="stat-content">
                            <div class="stat-value">{{ statistics.totalProducts || 0 }}</div>
                            <div class="stat-label">产品数量</div>
                        </div>
                    </div>
                </el-col>
                <el-col :xs="12" :sm="6">
                    <div class="stat-card">
                        <div class="stat-icon" style="background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);">
                            <el-icon><User/></el-icon>
                        </div>
                        <div class="stat-content">
                            <div class="stat-value">{{ statistics.customers || 0 }}</div>
                            <div class="stat-label">客户数量</div>
                        </div>
                    </div>
                </el-col>
            </el-row>

            <!-- 图表区域 -->
            <el-row :gutter="20">
                <el-col :xs="24" :lg="16">
                    <el-card shadow="hover" class="chart-card">
                        <template #header>
                            <div class="card-header">
                                <span>销售趋势</span>
                            </div>
                        </template>
                        <div ref="salesTrendChart" style="height: 350px;"></div>
                    </el-card>
                </el-col>
                <el-col :xs="24" :lg="8">
                    <el-card shadow="hover" class="chart-card">
                        <template #header>
                            <div class="card-header">
                                <span>订单状态分布</span>
                            </div>
                        </template>
                        <div ref="statusDistributionChart" style="height: 350px;"></div>
                    </el-card>
                </el-col>
            </el-row>

            <el-row :gutter="20">
                <el-col :xs="24">
                    <el-card shadow="hover" class="chart-card">
                        <template #header>
                            <div class="card-header">
                                <span>产品销售排行</span>
                            </div>
                        </template>
                        <div ref="productRankingChart" style="height: 350px;"></div>
                    </el-card>
                </el-col>
            </el-row>
        </div>
    </div>
</template>


<script setup>
import {Connection, DataLine, Finished, Medal, TrendCharts, ShoppingCart, Money, Box, User} from "@element-plus/icons-vue";
import {ref, onMounted, computed, onBeforeUnmount} from 'vue';
import {getSalesStatistics} from "@/api/assisting/sales.js";
import * as echarts from 'echarts';
import useUserStore from "@/store/modules/user";

// 获取用户信息
const userStore = useUserStore();
// 计算是否为农户角色
const isFarmer = computed(() => {
    return userStore?.roles && Array.isArray(userStore.roles) && userStore.roles.includes('farmers');
});

// 计算是否为admin角色
const isAdmin = computed(() => {
    return userStore?.roles && Array.isArray(userStore.roles) && userStore.roles.includes('admin');
});

// 时间范围选择
const timeRange = ref('day');
// 统计数据
const statistics = ref({
    totalOrders: 0,      // 总订单数
    totalAmount: 0,      // 总销售额
    totalProducts: 0,    // 产品数量
    customers: 0         // 客户数量
});

// 图表引用
const salesTrendChart = ref(null);        // 销售趋势图
const statusDistributionChart = ref(null); // 订单状态分布图
const productRankingChart = ref(null);     // 产品销售排行图

// 图表实例
let salesTrendChartInstance = null;
let statusDistributionChartInstance = null;
let productRankingChartInstance = null;

/**
 * 加载销售数据
 */
const loadSalesData = () => {
    if (!userStore) {
        console.warn('用户信息不完整，无法加载销售数据');
        return;
    }
    
    // admin角色传递null，获取所有数据
    const userId = isAdmin.value ? null : userStore.id;
    getSalesStatistics(userId, timeRange.value).then(res => {
        if (!res || !res.data) {
            console.warn('销售数据加载失败');
            return;
        }
        
        const data = res.data;
        
        // 计算统计数据
        statistics.value = {
            totalOrders: data.salesTrend?.reduce((sum, item) => sum + item.orderCount, 0) || 0,
            totalAmount: data.salesTrend?.reduce((sum, item) => sum + item.totalAmount, 0)?.toFixed(2) || 0,
            totalProducts: data.productRanking?.length || 0,
            customers: data.salesTrend?.reduce((sum, item) => sum + item.orderCount, 0) || 0
        };

        // 初始化图表
        initSalesTrendChart(data.salesTrend || []);
        initStatusDistributionChart(data.statusDistribution || []);
        initProductRankingChart(data.productRanking || []);
    }).catch(error => {
        console.error('加载销售数据时出错:', error);
    });
};

/**
 * 初始化销售趋势图
 * @param {Array} data - 销售趋势数据
 */
const initSalesTrendChart = (data) => {
    if (!salesTrendChart.value) return;
    
    try {
        // 销毁现有实例
        if (salesTrendChartInstance) {
            salesTrendChartInstance.dispose();
        }

        // 创建新实例
        salesTrendChartInstance = echarts.init(salesTrendChart.value);
        
        // 处理数据
        const dates = Array.isArray(data) ? data.map(item => item.date || '').filter(Boolean) : [];
        const amounts = Array.isArray(data) ? data.map(item => item.totalAmount || 0) : [];
        const orders = Array.isArray(data) ? data.map(item => item.orderCount || 0) : [];

        // 图表配置
        const option = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross'
                }
            },
            legend: {
                data: ['销售额', '订单数']
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: {
                type: 'category',
                boundaryGap: false,
                data: dates
            },
            yAxis: [
                {
                    type: 'value',
                    name: '销售额',
                    position: 'left'
                },
                {
                    type: 'value',
                    name: '订单数',
                    position: 'right'
                }
            ],
            series: [
                {
                    name: '销售额',
                    type: 'line',
                    smooth: true,
                    data: amounts,
                    itemStyle: {
                        color: '#5B86E5'
                    },
                    areaStyle: {
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                            { offset: 0, color: 'rgba(91, 134, 229, 0.3)' },
                            { offset: 1, color: 'rgba(91, 134, 229, 0.05)' }
                        ])
                    }
                },
                {
                    name: '订单数',
                    type: 'line',
                    smooth: true,
                    yAxisIndex: 1,
                    data: orders,
                    itemStyle: {
                        color: '#36D1DC'
                    }
                }
            ]
        };

        // 设置图表配置
        salesTrendChartInstance.setOption(option);
    } catch (error) {
        console.error('初始化销售趋势图失败:', error);
    }
};

/**
 * 初始化订单状态分布图
 * @param {Array} data - 订单状态数据
 */
const initStatusDistributionChart = (data) => {
    if (!statusDistributionChart.value) return;
    
    try {
        // 销毁现有实例
        if (statusDistributionChartInstance) {
            statusDistributionChartInstance.dispose();
        }

        // 创建新实例
        statusDistributionChartInstance = echarts.init(statusDistributionChart.value);
        
        // 订单状态映射
        const statusMap = {
            '待发货': { color: '#E6A23C', value: 0 },
            '待收货': { color: '#409EFF', value: 0 },
            '已完成': { color: '#67C23A', value: 0 },
            '已取消': { color: '#F56C6C', value: 0 }
        };

        // 填充数据
        if (Array.isArray(data)) {
            data.forEach(item => {
                if (item && statusMap[item.status]) {
                    statusMap[item.status].value = item.count || 0;
                }
            });
        }

        // 转换为图表数据
        const chartData = Object.entries(statusMap).map(([key, value]) => ({
            name: key,
            value: value.value,
            itemStyle: { color: value.color }
        }));

        // 图表配置
        const option = {
            tooltip: {
                trigger: 'item',
                formatter: '{a} <br/>{b}: {c} ({d}%)'
            },
            legend: {
                orient: 'vertical',
                left: 'left'
            },
            series: [
                {
                    name: '订单状态',
                    type: 'pie',
                    radius: ['40%', '70%'],
                    avoidLabelOverlap: false,
                    itemStyle: {
                        borderRadius: 10,
                        borderColor: '#fff',
                        borderWidth: 2
                    },
                    label: {
                        show: false,
                        position: 'center'
                    },
                    emphasis: {
                        label: {
                            show: true,
                            fontSize: 20,
                            fontWeight: 'bold'
                        }
                    },
                    labelLine: {
                        show: false
                    },
                    data: chartData
                }
            ]
        };

        // 设置图表配置
        statusDistributionChartInstance.setOption(option);
    } catch (error) {
        console.error('初始化订单状态分布图失败:', error);
    }
};

/**
 * 初始化产品销售排行图
 * @param {Array} data - 产品销售数据
 */
const initProductRankingChart = (data) => {
    if (!productRankingChart.value) return;
    
    try {
        // 销毁现有实例
        if (productRankingChartInstance) {
            productRankingChartInstance.dispose();
        }

        // 创建新实例
        productRankingChartInstance = echarts.init(productRankingChart.value);
        
        // 处理数据
        const products = Array.isArray(data) ? data.map(item => item.name || '未知产品').filter(Boolean) : [];
        const amounts = Array.isArray(data) ? data.map(item => item.totalAmount || 0) : [];

        // 图表配置
        const option = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'shadow'
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: {
                type: 'value',
                name: '销售额'
            },
            yAxis: {
                type: 'category',
                data: products
            },
            series: [
                {
                    name: '销售额',
                    type: 'bar',
                    data: amounts,
                    itemStyle: {
                        color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [
                            { offset: 0, color: '#5B86E5' },
                            { offset: 1, color: '#36D1DC' }
                        ]),
                        borderRadius: [0, 4, 4, 0]
                    },
                    label: {
                        show: true,
                        position: 'right',
                        formatter: '¥{c}'
                    }
                }
            ]
        };

        // 设置图表配置
        productRankingChartInstance.setOption(option);
    } catch (error) {
        console.error('初始化产品销售排行图失败:', error);
    }
};

/**
 * 处理窗口大小变化
 */
const handleResize = () => {
    if (salesTrendChartInstance) salesTrendChartInstance.resize();
    if (statusDistributionChartInstance) statusDistributionChartInstance.resize();
    if (productRankingChartInstance) productRankingChartInstance.resize();
};

// 组件挂载时初始化
onMounted(() => {
    if (isFarmer.value || isAdmin.value) {
        loadSalesData();
        window.addEventListener('resize', handleResize);
    }
});

// 组件卸载前清理
onBeforeUnmount(() => {
    if (salesTrendChartInstance) salesTrendChartInstance.dispose();
    if (statusDistributionChartInstance) statusDistributionChartInstance.dispose();
    if (productRankingChartInstance) productRankingChartInstance.dispose();
    window.removeEventListener('resize', handleResize);
});
</script>

<style lang="scss" scoped>
.home-service-container {
    padding: 20px;
    background-color: #f5f7fa;

    .transparent-card {
        background-color: transparent;
        border: none;
    }

    .main-banner {
        margin-bottom: 40px;

        .banner-content {
            display: flex;
            align-items: center;
            padding: 30px;
            background: linear-gradient(135deg, #5B86E5 0%, #36D1DC 100%);
            border-radius: 8px;
            color: white;

            .banner-text {
                flex: 1;
                padding-right: 40px;

                h1 {
                    font-size: 36px;
                    color: white;
                    margin-bottom: 15px;
                }

                .sub-title {
                    font-size: 18px;
                    color: rgba(255, 255, 255, 0.8);
                    margin-bottom: 20px;
                }

                .banner-description {
                    font-size: 16px;
                    line-height: 1.8;
                    color: rgba(255, 255, 255, 0.8);
                    margin-bottom: 30px;
                }

                .banner-actions {
                    display: flex;
                    gap: 20px;
                    margin-top: 30px;
                }
            }

            .banner-image {
                flex: 1;
                text-align: center;

                img {
                    max-width: 100%;
                    border-radius: 4px;
                    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
                }
            }
        }
    }

    .section-title {
        text-align: center;
        font-size: 28px;
        color: #303133;
    }

    .service-section {
        margin-bottom: 50px;

        .service-card {
            background: white;
            padding: 30px 20px;
            border-radius: 8px;
            text-align: center;
            box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
            height: 100%;
            transition: transform 0.3s;
            display: flex;
            flex-direction: column;
            align-items: center;

            &:hover {
                transform: translateY(-5px);
                box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
            }

            .service-icon {
                font-size: 48px;
                margin-bottom: 20px;
            }

            h3 {
                font-size: 20px;
                margin-bottom: 15px;
                color: #303133;
            }

            p {
                color: #606266;
                line-height: 1.6;
                margin-bottom: 20px;
            }
        }
    }

    .advantage-section {
        margin: 50px 0;

        .advantage-card {
            background: white;
            padding: 25px 15px;
            border-radius: 8px;
            text-align: center;
            box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
            height: 100%;
            margin-bottom: 20px;

            .advantage-icon {
                font-size: 36px;
                margin-bottom: 15px;
                color: #5B86E5;
            }

            h4 {
                font-size: 18px;
                margin-bottom: 10px;
                color: #303133;
            }

            p {
                color: #909399;
                font-size: 14px;
            }
        }
    }

    .intro-section {
        margin: 50px 0;

        .intro-card {
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);

            h3 {
                font-size: 22px;
                margin-bottom: 20px;
                color: #303133;
                position: relative;
                padding-bottom: 10px;

                &::after {
                    content: '';
                    position: absolute;
                    bottom: 0;
                    left: 0;
                    width: 50px;
                    height: 3px;
                    background: #409EFF;
                    border-radius: 2px;
                }
            }

            p {
                color: #606266;
                line-height: 1.8;
                font-size: 16px;
            }
        }
    }
}

/* 销售统计区域样式 */
.sales-statistics-section {
    margin: 50px 0;
}

.time-range-selector {
    text-align: center;
    margin-bottom: 30px;
}

.statistics-cards {
    margin-bottom: 30px;
}

.stat-card {
    background: white;
    border-radius: 8px;
    padding: 20px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
    transition: transform 0.3s;
    margin-bottom: 20px;

    &:hover {
        transform: translateY(-5px);
        box-shadow: 0 5px 20px rgba(0, 0, 0, 0.1);
    }

    .stat-icon {
        width: 60px;
        height: 60px;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 20px;

        .el-icon {
            font-size: 28px;
            color: white;
        }
    }

    .stat-content {
        flex: 1;

        .stat-value {
            font-size: 24px;
            font-weight: bold;
            color: #303133;
            margin-bottom: 5px;
        }

        .stat-label {
            font-size: 14px;
            color: #909399;
        }
    }
}

.chart-card {
    margin-bottom: 20px;

    .card-header {
        font-size: 16px;
        font-weight: bold;
        color: #303133;
    }
}

@media (max-width: 768px) {
    .home-service-container {
        .main-banner {
            .banner-content {
                flex-direction: column;

                .banner-text {
                    padding-right: 0;
                    margin-bottom: 30px;
                }
            }
        }

        .service-section,
        .advantage-section {
            .el-col {
                margin-bottom: 20px;
            }
        }
    }
}
</style>
