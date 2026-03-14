import request from '@/utils/request'

// 查询产品评论列表
export function listReviews(query) {
  return request({
    url: '/assisting/reviews/list',
    method: 'get',
    params: query
  })
}

// 查询产品评论详细
export function getReviews(reviewId) {
  return request({
    url: '/assisting/reviews/' + reviewId,
    method: 'get'
  })
}

// 新增产品评论
export function addReviews(data) {
  return request({
    url: '/assisting/reviews',
    method: 'post',
    data: data
  })
}

// 修改产品评论
export function updateReviews(data) {
  return request({
    url: '/assisting/reviews',
    method: 'put',
    data: data
  })
}

// 删除产品评论
export function delReviews(reviewId) {
  return request({
    url: '/assisting/reviews/' + reviewId,
    method: 'delete'
  })
}
