import request from '@/utils/request'

// 查询订单异常记录列表
export function listException(query) {
  return request({
    url: '/express/exception/list',
    method: 'get',
    params: query
  })
}

// 查询订单异常记录详细
export function getException(id) {
  return request({
    url: '/express/exception/' + id,
    method: 'get'
  })
}

// 新增订单异常记录
export function addException(data) {
  return request({
    url: '/express/exception',
    method: 'post',
    data: data
  })
}

// 修改订单异常记录
export function updateException(data) {
  return request({
    url: '/express/exception',
    method: 'put',
    data: data
  })
}

// 删除订单异常记录
export function delException(id) {
  return request({
    url: '/express/exception/' + id,
    method: 'delete'
  })
}
