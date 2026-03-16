<template>
  <!-- 评论管理页面主容器 -->
  <div class="app-container">
    <!-- ==================== 顶部搜索区域 ==================== -->
    <!-- 搜索表单：用于根据产品名称、用户昵称、状态等条件筛选评论 -->
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="80px">
      <!-- 产品名称搜索项 -->
      <el-form-item label="产品名称" prop="productName">
        <el-input v-model="queryParams.productName" placeholder="请输入产品名称" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <!-- 用户昵称搜索项 -->
      <el-form-item label="用户昵称" prop="userName">
        <el-input v-model="queryParams.userName" placeholder="请输入用户昵称" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <!-- 状态筛选项：显示/隐藏 -->
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="显示" :value="1" />
          <el-option label="隐藏" :value="0" />
        </el-select>
      </el-form-item>
      <!-- 搜索操作按钮 -->
      <el-form-item>
        <el-button type="primary" @click="handleQuery">搜索</el-button>
        <el-button @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- ==================== 顶部操作按钮区域 ==================== -->
    <!-- 包含批量删除、导出、搜索栏显示/隐藏切换等功能按钮 -->
    <el-row :gutter="10" class="mb8">
      <!-- 批量删除按钮：需要选中至少一条数据 -->
      <el-col :span="1.5">
        <el-button type="danger" plain :disabled="multiple" @click="handleDelete" v-hasPermi="['assisting:reviews:remove']">删除</el-button>
      </el-col>
      <!-- 导出按钮：导出当前筛选条件下的评论数据 -->
      <el-col :span="1.5">
        <el-button type="warning" plain @click="handleExport" v-hasPermi="['assisting:reviews:export']">导出</el-button>
      </el-col>
      <!-- 右侧工具栏：包含搜索栏显示/隐藏切换、刷新表格等功能 -->
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- ==================== 评论数据表格区域 ==================== -->
    <!-- 展示评论列表，支持多选、排序、分页等功能 -->
    <el-table v-loading="loading" :data="reviewsList" @selection-change="handleSelectionChange" border>
      <!-- 多选列：用于批量操作 -->
      <el-table-column type="selection" width="55" align="center" />
      <!-- 序号列：自动生成的行号 -->
      <el-table-column label="序号" type="index" width="50" align="center" />
      <!-- 产品名称列：显示评论关联的产品名称 -->
      <el-table-column label="产品名称" align="center" prop="productName" min-width="120" show-overflow-tooltip />
      <!-- 用户昵称列：显示发表评论的用户昵称 -->
      <el-table-column label="用户昵称" align="center" prop="userName" width="100" />
      <!-- 评分列：使用星级评分组件展示 -->
      <el-table-column label="评分" align="center" width="140">
        <template #default="scope">
          <el-rate v-model="scope.row.rating" disabled show-score text-color="#ff9900" />
        </template>
      </el-table-column>
      <!-- 评论内容列：显示用户评论的详细内容 -->
      <el-table-column label="评论内容" align="center" prop="content" min-width="200" show-overflow-tooltip />
      <!-- 评论时间列：显示评论提交的时间 -->
      <el-table-column label="评论时间" align="center" prop="createTime" width="160" />
      <!-- 状态列：显示评论的显示/隐藏状态 -->
      <el-table-column label="状态" align="center" width="80">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
            {{ scope.row.status === 1 ? '显示' : '隐藏' }}
          </el-tag>
        </template>
      </el-table-column>
      <!-- 操作列：包含回复、修改状态、删除等操作按钮 -->
      <el-table-column label="操作" align="center" width="200" fixed="right">
        <template #default="scope">
          <!-- 回复按钮：打开回复对话框 -->
          <el-button link type="primary" @click="handleReply(scope.row)" v-hasPermi="['assisting:reviews:edit']">回复</el-button>
          <!-- 状态切换按钮：切换评论的显示/隐藏状态 -->
          <el-button link type="primary" @click="handleUpdateStatus(scope.row)" v-hasPermi="['assisting:reviews:edit']">
            {{ scope.row.status === 1 ? '隐藏' : '显示' }}
          </el-button>
          <!-- 删除按钮：删除当前评论 -->
          <el-button link type="danger" @click="handleDelete(scope.row)" v-hasPermi="['assisting:reviews:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- ==================== 分页组件区域 ==================== -->
    <!-- 当数据总数大于0时显示分页组件 -->
    <pagination v-show="total>0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <!-- ==================== 回复评论对话框 ==================== -->
    <!-- 用于管理员回复用户评论的弹窗 -->
    <el-dialog title="回复评论" v-model="replyOpen" width="600px" append-to-body>
      <el-form ref="replyRef" :model="replyForm" label-width="80px">
        <!-- 显示原始评论内容（只读） -->
        <el-form-item label="评论内容">
          <div class="review-content">{{ replyForm.content }}</div>
        </el-form-item>
        <!-- 回复内容输入框 -->
        <el-form-item label="回复内容" prop="replyContent">
          <el-input v-model="replyForm.replyContent" type="textarea" :rows="4" placeholder="请输入回复内容..." />
        </el-form-item>
      </el-form>
      <!-- 对话框底部按钮 -->
      <template #footer>
        <el-button @click="replyOpen = false">取 消</el-button>
        <el-button type="primary" @click="submitReply">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Reviews">
/**
 * 评论管理页面
 * 功能：展示、搜索、回复、删除用户评论，支持评论状态管理
 * 权限：需要具备 assisting:reviews:list 权限才能查看列表
 *       需要具备 assisting:reviews:edit 权限才能回复和修改状态
 *       需要具备 assisting:reviews:remove 权限才能删除评论
 */

// ==================== Vue 核心 API 导入 ====================
import { ref, reactive, toRefs, getCurrentInstance, onMounted } from 'vue'

// ==================== API 接口导入 ====================
import { listReviews, delReviews, updateReviews } from "@/api/assisting/reviews"

// ==================== Element Plus 组件导入 ====================
import { ElMessage, ElMessageBox } from 'element-plus'

// ==================== 全局实例获取 ====================
const { proxy } = getCurrentInstance()

// 打印调试信息
console.log('评论管理组件加载');
console.log('API路径:', listReviews);

// ==================== 数据状态定义 ====================

/**
 * 评论列表数据
 * 存储从后端获取的评论数据数组
 */
const reviewsList = ref([])

/**
 * 加载状态标识
 * 控制表格的加载动画显示/隐藏
 */
const loading = ref(true)

/**
 * 搜索栏显示状态
 * 控制搜索表单的显示/隐藏
 */
const showSearch = ref(true)

/**
 * 选中的评论ID数组
 * 用于批量删除操作
 */
const ids = ref([])

/**
 * 单选状态标识
 * 控制单条数据操作按钮的禁用状态
 * true: 禁用（未选中或选中多条）
 * false: 启用（选中一条）
 */
const single = ref(true)

/**
 * 多选状态标识
 * 控制批量操作按钮的禁用状态
 * true: 禁用（未选中任何数据）
 * false: 启用（选中至少一条）
 */
const multiple = ref(true)

/**
 * 数据总数
 * 用于分页组件显示总记录数
 */
const total = ref(0)

/**
 * 回复对话框显示状态
 * 控制回复评论弹窗的显示/隐藏
 */
const replyOpen = ref(false)

// ==================== 响应式数据对象 ====================

/**
 * 查询参数对象
 * 包含分页参数和搜索条件
 * @property {number} pageNum - 当前页码，默认第1页
 * @property {number} pageSize - 每页显示条数，默认10条
 * @property {string} productName - 产品名称搜索关键字
 * @property {string} userName - 用户昵称搜索关键字
 * @property {number} status - 评论状态筛选（1:显示, 0:隐藏）
 */
const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    productName: null,
    userName: null,
    status: null
  },
  /**
   * 回复表单数据
   * @property {string} reviewId - 评论ID
   * @property {string} content - 原始评论内容
   * @property {string} replyContent - 回复内容
   */
  replyForm: {}
})

// 使用 toRefs 解构响应式数据，方便在模板中直接使用
const { queryParams, replyForm } = toRefs(data)

// ==================== 核心方法定义 ====================

/**
 * 查询评论列表
 * 根据当前查询参数从后端获取评论数据
 * 包含加载状态控制、错误处理、数据赋值等逻辑
 */
const getList = () => {
  // 显示加载动画
  loading.value = true
  // 打印查询参数，便于调试
  console.log('查询评论列表，参数：', queryParams.value)

  // 调用API获取数据
  listReviews(queryParams.value).then(response => {
    // 打印响应数据，便于调试
    console.log('查询评论列表，响应：', response)
    // 将获取的数据赋值给表格
    reviewsList.value = response.rows
    // 设置数据总数，用于分页
    total.value = response.total
    // 关闭加载动画
    loading.value = false
  }).catch(error => {
    // 打印错误信息
    console.error('查询评论列表失败：', error)
    // 关闭加载动画
    loading.value = false
  })
}

/**
 * 搜索按钮操作
 * 重置页码为第1页，然后重新查询数据
 */
const handleQuery = () => {
  queryParams.value.pageNum = 1
  getList()
}

/**
 * 重置按钮操作
 * 清空搜索条件，重置页码，重新查询数据
 */
const resetQuery = () => {
  // 使用全局重置表单方法清空搜索条件
  proxy.resetForm("queryRef")
  // 重新查询数据
  handleQuery()
}

/**
 * 多选框选中数据变化事件
 * 处理表格多选框的选中状态变化
 * @param {Array} selection - 当前选中的数据数组
 */
const handleSelectionChange = (selection) => {
  // 提取选中数据的评论ID
  ids.value = selection.map(item => item.reviewId)
  // 更新单选状态：选中且仅选中一条时启用
  single.value = selection.length !== 1
  // 更新多选状态：选中至少一条时启用
  multiple.value = !selection.length
}

/**
 * 回复按钮操作
 * 打开回复对话框，准备回复评论
 * @param {Object} row - 当前行的评论数据
 */
const handleReply = (row) => {
  // 设置回复表单数据
  replyForm.value = {
    reviewId: row.reviewId,           // 评论ID
    content: row.content,             // 原始评论内容
    replyContent: row.replyContent || ''  // 已有的回复内容（如果有）
  }
  // 显示回复对话框
  replyOpen.value = true
}

/**
 * 提交回复
 * 将管理员的回复内容提交到后端保存
 */
const submitReply = () => {
  // 验证回复内容是否为空
  if (!replyForm.value.replyContent.trim()) {
    ElMessage.warning('请输入回复内容')
    return
  }
  // 调用API提交回复
  updateReviews({
    reviewId: replyForm.value.reviewId,
    replyContent: replyForm.value.replyContent
  }).then(() => {
    // 显示成功提示
    ElMessage.success('回复成功')
    // 关闭对话框
    replyOpen.value = false
    // 刷新评论列表
    getList()
  })
}

/**
 * 修改评论状态
 * 切换评论的显示/隐藏状态
 * @param {Object} row - 当前行的评论数据
 */
const handleUpdateStatus = (row) => {
  // 计算新状态：当前为显示则改为隐藏，反之亦然
  const newStatus = row.status === 1 ? 0 : 1
  // 根据新状态确定操作文本
  const text = newStatus === 1 ? '显示' : '隐藏'
  // 显示确认对话框
  ElMessageBox.confirm(`确认要${text}该评论吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 调用API更新状态
    return updateReviews({
      reviewId: row.reviewId,
      status: newStatus
    })
  }).then(() => {
    // 显示成功提示
    ElMessage.success('操作成功')
    // 刷新评论列表
    getList()
  }).catch(() => {
    // 用户取消操作，不做处理
  })
}

/**
 * 删除按钮操作
 * 删除单条或多条评论数据
 * @param {Object} row - 当前行的评论数据（可选，不传则使用选中的ids）
 */
const handleDelete = (row) => {
  // 确定要删除的评论ID：优先使用传入的row，否则使用选中的ids
  const _reviewIds = row.reviewId || ids.value
  // 显示确认对话框
  proxy.$modal.confirm('是否确认删除该评论？').then(function () {
    return delReviews(_reviewIds)
  }).then(() => {
    // 刷新评论列表
    getList()
    // 显示成功提示
    proxy.$modal.msgSuccess('删除成功')
  }).catch(() => {
    // 用户取消操作，不做处理
  })
}

/**
 * 导出按钮操作
 * 导出当前筛选条件下的评论数据为Excel文件
 */
const handleExport = () => {
  // 使用全局下载方法导出数据
  proxy.download('assisting/reviews/export', {
    ...queryParams.value
  }, `reviews_${new Date().getTime()}.xlsx`)
}

// ==================== 生命周期钩子 ====================

/**
 * 组件挂载完成钩子
 * 页面加载完成后自动查询评论列表
 */
onMounted(() => {
  getList()
})
</script>

<style scoped>
/**
 * 评论内容展示样式
 * 用于回复对话框中显示原始评论内容的样式
 */
.review-content {
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
  color: #606266;
}
</style>
