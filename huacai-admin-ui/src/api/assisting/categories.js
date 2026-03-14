import request from '@/utils/request'

// 获取分类列表
export function listCategories() {
  return request({
    url: '/assisting/categories/list',
    method: 'get'
  }).then(response => response.rows)
}

// 获取分类详情
export function getCategories(categoryId) {
  return request({
    url: `/assisting/categories/${categoryId}`,
    method: 'get'
  })
}

// 新增分类
export function addCategories(data) {
  return request({
    url: '/assisting/categories',
    method: 'post',
    data: data
  })
}

// 修改分类
export function updateCategories(data) {
  return request({
    url: '/assisting/categories',
    method: 'put',
    data: data
  })
}

// 删除分类
export function delCategories(categoryId) {
  return request({
    url: `/assisting/categories/${categoryId}`,
    method: 'delete'
  })
}
