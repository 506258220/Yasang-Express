import request from '@/utils/request'

// 查询物流轨迹列表
export function listLogistics(query) {
  return request({
    url: '/express/logistics/list',
    method: 'get',
    params: query
  })
}

// 查询物流轨迹详细
export function getLogistics(trajectoryId) {
  return request({
    url: '/express/logistics/' + trajectoryId,
    method: 'get'
  })
}

// 查询订单物流轨迹
export function getOrderLogistics(orderId) {
  return request({
    url: '/express/logistics/order/' + orderId,
    method: 'get'
  })
}

// 新增物流轨迹
export function addLogistics(data) {
  return request({
    url: '/express/logistics',
    method: 'post',
    data: data
  })
}

// 修改物流轨迹
export function updateLogistics(data) {
  return request({
    url: '/express/logistics',
    method: 'put',
    data: data
  })
}

// 删除物流轨迹
export function delLogistics(trajectoryId) {
  return request({
    url: '/express/logistics/' + trajectoryId,
    method: 'delete'
  })
}

// 同步物流轨迹
export function syncLogistics(data) {
  return request({
    url: '/express/logistics/sync',
    method: 'post',
    data: data
  })
}

// 导出物流轨迹
export function exportLogistics(query) {
  return request({
    url: '/express/logistics/export',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}