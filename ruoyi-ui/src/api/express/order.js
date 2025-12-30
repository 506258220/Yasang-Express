import request from '@/utils/request'

// 查询快递订单列表
export function listOrder(query) {
  return request({
    url: '/express/order/list',
    method: 'get',
    params: query
  })
}

// 查询快递订单详细
export function getOrder(id) {
  return request({
    url: '/express/order/' + id,
    method: 'get'
  })
}

// 新增快递订单
export function addOrder(data) {
  return request({
    url: '/express/order',
    method: 'post',
    data: data
  })
}

// 修改快递订单
export function updateOrder(data) {
  return request({
    url: '/express/order',
    method: 'put',
    data: data
  })
}

// 删除快递订单
export function delOrder(id) {
  return request({
    url: '/express/order/' + id,
    method: 'delete'
  })
}

// 批量查询订单（根据ID数组）
export function getOrdersByIds(ids) {
  return request({
    url: '/express/order/ids',
    method: 'post',
    data: ids
  })
}