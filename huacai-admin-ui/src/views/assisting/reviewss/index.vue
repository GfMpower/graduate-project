<template>
  <div class="app-container">
    <!-- 顶部搜索 -->
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="产品ID" prop="productsId">
        <el-input
          v-model="queryParams.productsId"
          placeholder="请输入产品ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="农户用户ID" prop="farmersUserId">
        <el-input
          v-model="queryParams.farmersUserId"
          placeholder="请输入农户用户ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 顶部按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['assisting:reviewss:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['assisting:reviewss:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['assisting:reviewss:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['assisting:reviewss:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="primary"
            icon="Upload"
            size="mini"
            @click="handleImport"
        >导入</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 表格 -->
    <el-table @row-click="clickRow" ref="table" highlight-current-row
              border v-loading="loading" :data="reviewssList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" type="index" :index="indexMethod" />
      <el-table-column label="评论ID" align="center" prop="reviewId" />
      <el-table-column label="产品ID" align="center" prop="productsId" />
      <el-table-column label="用户ID" align="center" prop="userId" />
      <el-table-column label="评论内容" align="center" prop="content" />
      <el-table-column label="评价等级" align="center" prop="rating" />
      <el-table-column label="订单ID" align="center" prop="orderId" />
      <el-table-column label="农户用户ID" align="center" prop="farmersUserId" />
      <el-table-column label="商家回复内容" align="center" prop="replyContent" />
      <el-table-column label="商家回复时间" align="center" prop="replyTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.replyTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态：0-隐藏，1-显示" align="center" prop="status" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['assisting:reviewss:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['assisting:reviewss:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 导入对话框 -->
    <vxe-modal :title="upload.title" v-model="upload.open" width="400px" showFooter show-zoom resize>
      <el-upload
          ref="uploadRef"
          :limit="1"
          accept=".xlsx, .xls"
          :headers="upload.headers"
          :action="upload.url"
          :data="{ updateSupport: upload.updateSupport }"
          :disabled="upload.isUploading"
          :on-progress="handleFileUploadProgress"
          :on-success="handleFileSuccess"
          :auto-upload="false"
          drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </vxe-modal>

    <!-- 添加或修改产品评论对话框 -->
    <vxe-modal :title="title" v-model="open" width="500px" show-maximize showFooter resize>
      <el-form ref="reviewssRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="产品ID" prop="productsId">
          <el-input v-model="form.productsId" placeholder="请输入产品ID" />
        </el-form-item>
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户ID" />
        </el-form-item>
        <el-form-item label="评论内容">
          <editor v-model="form.content" :min-height="192"/>
        </el-form-item>
        <el-form-item label="评价等级" prop="rating">
          <el-input v-model="form.rating" placeholder="请输入评价等级" />
        </el-form-item>
        <el-form-item label="订单ID" prop="orderId">
          <el-input v-model="form.orderId" placeholder="请输入订单ID" />
        </el-form-item>
        <el-form-item label="农户用户ID" prop="farmersUserId">
          <el-input v-model="form.farmersUserId" placeholder="请输入农户用户ID" />
        </el-form-item>
        <el-form-item label="商家回复内容">
          <editor v-model="form.replyContent" :min-height="192"/>
        </el-form-item>
        <el-form-item label="商家回复时间" prop="replyTime">
          <el-date-picker clearable
            v-model="form.replyTime"
            type="date"
            value-format="YYYY-MM-DD"
            placeholder="请选择商家回复时间">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </vxe-modal>
  </div>
</template>

<script setup name="Reviewss">
import { listReviewss, getReviewss, delReviewss, addReviewss, updateReviewss } from "@/api/assisting/reviewss"
import {getToken} from "@/utils/auth.js";
const baseURL = import.meta.env.VITE_APP_BASE_API

const { proxy } = getCurrentInstance()

const reviewssList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const selectedRow = ref(null)

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    productsId: null,
    userId: null,
    farmersUserId: null,
  },
  rules: {
    productsId: [
      { required: true, message: "产品ID不能为空", trigger: "blur" }
    ],
    userId: [
      { required: true, message: "用户ID不能为空", trigger: "blur" }
    ],
    content: [
      { required: true, message: "评论内容不能为空", trigger: "blur" }
    ],
    rating: [
      { required: true, message: "评价等级不能为空", trigger: "blur" }
    ],
    createTime: [
      { required: true, message: "创建时间不能为空", trigger: "blur" }
    ],
  },
  // 导入参数
  upload: {
    // 是否显示弹出层（导入）
    open: false,
    // 弹出层标题（导入）
    title: "",
    // 是否禁用上传
    isUploading: false,
    // 设置上传的请求头部
    headers: { Authorization: "Bearer " + getToken() },
    // 上传的地址
    url: baseURL + "/assisting/reviewss/importData"
  }
})

const { queryParams, form, rules, upload } = toRefs(data)

//点击行 获取行
const clickRow = (row) => {
  selectedRow.value = row; // 更新选中的行
  const table = proxy.$refs.table;
  // 清除所有已选中的行
  table.clearSelection();
  // 选中当前点击的行
  table.toggleRowSelection(row, true);
}

/** 自定义序号 */
const indexMethod = (index) => {
  let pageNum = queryParams.value.pageNum - 1;
  if((pageNum!==-1 && pageNum!==0)){
    return (index + 1) + (pageNum  * queryParams.value.pageSize);
  }else{
    return (index + 1)
  }
}

/** 查询产品评论列表 */
const getList = () => {
  loading.value = true
  listReviewss(queryParams.value).then(response => {
    reviewssList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

// 取消按钮
const cancel = () => {
  open.value = false
  reset()
}

// 表单重置
const reset = () => {
  form.value = {
    reviewId: null,
    productsId: null,
    userId: null,
    content: null,
    rating: null,
    createTime: null,
    orderId: null,
    farmersUserId: null,
    replyContent: null,
    replyTime: null,
    status: null
  }
  proxy.resetForm("reviewssRef")
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

// 多选框选中数据
const handleSelectionChange = (selection) => {
  ids.value = selection.map(item => item.reviewId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset()
  open.value = true
  title.value = "添加产品评论"
}

/** 修改按钮操作 */
const handleUpdate = (row) => {
  reset()
  const _reviewId = row.reviewId || ids.value
  getReviewss(_reviewId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改产品评论"
  })
}

/** 提交按钮 */
const submitForm = () => {
  proxy.$refs["reviewssRef"].validate(valid => {
    if (valid) {
      if (form.value.reviewId != null) {
        updateReviewss(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addReviewss(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 删除按钮操作 */
const handleDelete = (row) => {
  const _reviewIds = row.reviewId || ids.value
  proxy.$modal.confirm('是否确认删除该项数据？').then(function() {
    return delReviewss(_reviewIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy.download('assisting/reviewss/export', {
    ...queryParams.value
  }, `reviewss_${new Date().getTime()}.xlsx`)
}

/** 下载模板操作 */
const importTemplate = () => {
  proxy.download('assisting/reviewss/importTemplate', {
  }, `template_${new Date().getTime()}.xlsx`)
}

/** 导入按钮操作 */
const handleImport = () => {
  upload.value.title = "产品评论导入";
  upload.value.open = true;
}

// 文件上传中处理
const handleFileUploadProgress = (event, file, fileList) => {
  upload.value.isUploading = true;
}

// 文件上传成功处理
const handleFileSuccess = (response, file, fileList) => {
  upload.value.open = false;
  upload.value.isUploading = false;
  proxy.$refs.uploadRef.clearFiles();
  proxy.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
  getList();
}

// 提交上传文件
const submitFileForm = () => {
  proxy.$refs.uploadRef.submit();
}

getList()
</script>
