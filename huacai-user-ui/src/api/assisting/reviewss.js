import request from '@/utils/request'

// 查询产品评论列表
export function listReviewss(query) {
  return request({
    url: '/assisting/reviewss/list',
    method: 'get',
    params: query
  })
}

// 查询产品评论详细
export function getReviewss(reviewId) {
  return request({
    url: '/assisting/reviewss/' + reviewId,
    method: 'get'
  })
}

// 新增产品评论
export function addReviewss(data) {
  return request({
    url: '/assisting/reviewss',
    method: 'post',
    data: data
  })
}

// 修改产品评论
export function updateReviewss(data) {
  return request({
    url: '/assisting/reviewss',
    method: 'put',
    data: data
  })
}

// 删除产品评论
export function delReviewss(reviewId) {
  return request({
    url: '/assisting/reviewss/' + reviewId,
    method: 'delete'
  })
}
