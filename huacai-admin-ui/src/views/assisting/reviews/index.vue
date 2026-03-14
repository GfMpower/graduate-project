<template>
  <div class="app-container">
    <!-- 顶部搜索 -->
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="产品名称" prop="productName">
        <el-input v-model="queryParams.productName" placeholder="请输入产品名称" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="用户昵称" prop="userName">
        <el-input v-model="queryParams.userName" placeholder="请输入用户昵称" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option label="显示" :value="1" />
          <el-option label="隐藏" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">搜索</el-button>
        <el-button @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 顶部按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="danger" plain :disabled="multiple" @click="handleDelete" v-hasPermi="['assisting:reviews:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain @click="handleExport" v-hasPermi="['assisting:reviews:export']">导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 表格 -->
    <el-table v-loading="loading" :data="reviewsList" @selection-change="handleSelectionChange" border>
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" type="index" width="50" align="center" />
      <el-table-column label="产品名称" align="center" prop="productName" min-width="120" show-overflow-tooltip />
      <el-table-column label="用户昵称" align="center" prop="userName" width="100" />
      <el-table-column label="评分" align="center" width="140">
        <template #default="scope">
          <el-rate v-model="scope.row.rating" disabled show-score text-color="#ff9900" />
        </template>
      </el-table-column>
      <el-table-column label="评论内容" align="center" prop="content" min-width="200" show-overflow-tooltip />
      <el-table-column label="评论时间" align="center" prop="createTime" width="160" />
      <el-table-column label="状态" align="center" width="80">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
            {{ scope.row.status === 1 ? '显示' : '隐藏' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200" fixed="right">
        <template #default="scope">
          <el-button link type="primary" @click="handleReply(scope.row)" v-hasPermi="['assisting:reviews:edit']">回复</el-button>
          <el-button link type="primary" @click="handleUpdateStatus(scope.row)" v-hasPermi="['assisting:reviews:edit']">
            {{ scope.row.status === 1 ? '隐藏' : '显示' }}
          </el-button>
          <el-button link type="danger" @click="handleDelete(scope.row)" v-hasPermi="['assisting:reviews:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination v-show="total>0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <!-- 回复对话框 -->
    <el-dialog title="回复评论" v-model="replyOpen" width="600px" append-to-body>
      <el-form ref="replyRef" :model="replyForm" label-width="80px">
        <el-form-item label="评论内容">
          <div class="review-content">{{ replyForm.content }}</div>
        </el-form-item>
        <el-form-item label="回复内容" prop="replyContent">
          <el-input v-model="replyForm.replyContent" type="textarea" :rows="4" placeholder="请输入回复内容..." />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="replyOpen = false">取 消</el-button>
        <el-button type="primary" @click="submitReply">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Reviews">
import { ref, reactive, toRefs, getCurrentInstance, onMounted } from 'vue'
import { listReviews, delReviews, updateReviews } from "@/api/assisting/reviews"
import { ElMessage, ElMessageBox } from 'element-plus'

const { proxy } = getCurrentInstance()

const reviewsList = ref([])
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const replyOpen = ref(false)

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    productName: null,
    userName: null,
    status: null
  },
  replyForm: {}
})

const { queryParams, replyForm } = toRefs(data)

/** 查询评论列表 */
const getList = () => {
  loading.value = true
  listReviews(queryParams.value).then(response => {
    reviewsList.value = response.rows
    total.value = response.total
    loading.value = false
  }).catch(() => {
    loading.value = false
  })
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  proxy.resetForm("queryRef")
  handleQuery()
}

/** 多选框选中数据 */
const handleSelectionChange = (selection) => {
  ids.value = selection.map(item => item.reviewId)
  single.value = selection.length !== 1
  multiple.value = !selection.length
}

/** 回复按钮操作 */
const handleReply = (row) => {
  replyForm.value = {
    reviewId: row.reviewId,
    content: row.content,
    replyContent: row.replyContent || ''
  }
  replyOpen.value = true
}

/** 提交回复 */
const submitReply = () => {
  if (!replyForm.value.replyContent.trim()) {
    ElMessage.warning('请输入回复内容')
    return
  }
  updateReviews({
    reviewId: replyForm.value.reviewId,
    replyContent: replyForm.value.replyContent
  }).then(() => {
    ElMessage.success('回复成功')
    replyOpen.value = false
    getList()
  })
}

/** 修改状态 */
const handleUpdateStatus = (row) => {
  const newStatus = row.status === 1 ? 0 : 1
  const text = newStatus === 1 ? '显示' : '隐藏'
  ElMessageBox.confirm(`确认要${text}该评论吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    return updateReviews({
      reviewId: row.reviewId,
      status: newStatus
    })
  }).then(() => {
    ElMessage.success('操作成功')
    getList()
  }).catch(() => {})
}

/** 删除按钮操作 */
const handleDelete = (row) => {
  const _reviewIds = row.reviewId || ids.value
  ElMessageBox.confirm('是否确认删除该评论？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    return delReviews(_reviewIds)
  }).then(() => {
    getList()
    ElMessage.success('删除成功')
  }).catch(() => {})
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy.download('assisting/reviews/export', {
    ...queryParams.value
  }, `reviews_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList()
})
</script>

<style scoped>
.review-content {
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
  color: #606266;
}
</style>
