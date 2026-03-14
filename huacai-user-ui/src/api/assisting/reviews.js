import request from '@/utils/request'

export function listReviews(query) {
  return request({
    url: '/assisting/reviews/list',
    method: 'get',
    params: query
  })
}

export function getReviews(reviewId) {
  return request({
    url: '/assisting/reviews/' + reviewId,
    method: 'get'
  })
}

export function addReviews(data) {
  return request({
    url: '/assisting/reviews',
    method: 'post',
    data: data
  })
}

export function updateReviews(data) {
  return request({
    url: '/assisting/reviews',
    method: 'put',
    data: data
  })
}

export function delReviews(reviewId) {
  return request({
    url: '/assisting/reviews/' + reviewId,
    method: 'delete'
  })
}