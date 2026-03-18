import request from '@/utils/request'

// 推荐相关API

/**
 * 获取用户个性化推荐
 * @param {string} userId - 用户ID
 * @returns {Promise}
 */
export function getRecommendProducts(userId) {
  return request({
    url: `/api/recommend/user/${userId}`,
    method: 'get'
  })
}

/**
 * 获取相似商品推荐
 * @param {string} productId - 商品ID
 * @returns {Promise}
 */
export function getSimilarProducts(productId) {
  return request({
    url: `/api/recommend/similar/${productId}`,
    method: 'get'
  })
}

/**
 * 获取关联商品推荐
 * @param {Array} productIds - 商品ID列表
 * @returns {Promise}
 */
export function getRelatedProducts(productIds) {
  return request({
    url: '/api/recommend/related',
    method: 'get',
    params: {
      productIds: productIds.join(',')
    }
  })
}
