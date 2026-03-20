<template>
  <div class="recommend-section">
    <div class="section-header">
      <h2 class="section-title">为您推荐</h2>
    </div>
    
    <div class="recommend-list">
      <el-row :gutter="20">
        <el-col :span="6" v-for="item in currentPageItems" :key="item.productsId">
          <div class="product-card" @click="goToProductDetail(item.productsId)">
            <div class="product-image">
              <img :src="baseUrl + item.image" alt="">
              <div class="recommend-tag">推荐</div>
            </div>
            <div class="product-info">
              <h3 class="product-name">{{ item.name }}</h3>
              <p class="product-origin">产地: {{ item.origin }}</p>
              <div class="product-meta">
                <div class="product-price">
                  <span class="current-price">¥{{ item.price }}</span>
                </div>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 分页组件：当推荐商品数量大于每页显示数量时显示 -->
    <div class="pagination-container" v-if="recommendList.length > pageSize">
      <el-pagination
        v-model:current-page="currentPage"
        :page-size="pageSize"
        :total="recommendList.length"
        layout="prev, pager, next"
        @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { getRecommendProducts } from '@/api/assisting/recommend.js'
import useUserStore from '@/store/modules/user.js'

const router = useRouter()
const userStore = useUserStore()
const baseUrl = import.meta.env.VITE_APP_BASE_API
const recommendList = ref([])
const currentPage = ref(1)
// 每页显示8个商品（2排，一排4个）
const pageSize = ref(8)

const currentPageItems = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return recommendList.value.slice(start, end)
})

const goToProductDetail = (productsId) => {
  router.push(`/index/productDetail/${productsId}`)
}

const handlePageChange = (page) => {
  currentPage.value = page
}

const getRecommendations = async () => {
  try {
    const userId = userStore.id || 'guest'
    const res = await getRecommendProducts(userId)
    recommendList.value = res.data
  } catch (error) {
    console.error('获取推荐商品失败:', error)
  }
}

onMounted(() => {
  getRecommendations()
})
</script>

<style scoped>
.recommend-section {
  margin: 40px 0;
  padding: 20px;
  background: #f9f9f9;
  border-radius: 8px;
}

.section-header {
  text-align: center;
  margin-bottom: 30px;
}

.section-title {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
}

.section-subtitle {
  font-size: 14px;
  color: #666;
}

.recommend-list {
  margin-top: 20px;
}

.product-card {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  cursor: pointer;
  margin-bottom: 20px;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.15);
}

.product-image {
  position: relative;
  height: 200px;
  overflow: hidden;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.product-card:hover .product-image img {
  transform: scale(1.05);
}

.recommend-tag {
  position: absolute;
  top: 10px;
  right: 10px;
  background: #ff6b6b;
  color: #fff;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: bold;
}

.product-info {
  padding: 15px;
}

.product-name {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 8px;
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.product-origin {
  font-size: 14px;
  color: #666;
  margin-bottom: 10px;
}

.product-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.product-price {
  font-size: 18px;
  font-weight: bold;
  color: #ff6b6b;
}

.current-price {
  font-size: 18px;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 30px;
  padding: 20px 0;
}

@media screen and (max-width: 768px) {
  .el-col {
    width: 50% !important;
    margin-bottom: 20px;
  }
  
  .product-image {
    height: 150px;
  }
  
  .product-name {
    font-size: 14px;
  }
  
  .product-price {
    font-size: 16px;
  }
}
</style>