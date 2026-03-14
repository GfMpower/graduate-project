import request from '@/utils/request'

export function listCategories(query) {
  return request({
    url: '/assisting/categories/list',
    method: 'get',
    params: query
  })
}

export function getCategories(categoryId) {
  return request({
    url: '/assisting/categories/' + categoryId,
    method: 'get'
  })
}

export function addCategories(data) {
  return request({
    url: '/assisting/categories',
    method: 'post',
    data: data
  })
}

export function updateCategories(data) {
  return request({
    url: '/assisting/categories',
    method: 'put',
    data: data
  })
}

export function delCategories(categoryId) {
  return request({
    url: '/assisting/categories/' + categoryId,
    method: 'delete'
  })
}