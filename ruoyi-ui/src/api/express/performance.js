import request from '@/utils/request'

// 查询司机业绩统计列表
export function listPerformance(query) {
  return request({
    url: '/express/performance/list',
    method: 'get',
    params: query
  })
}

// 查询司机业绩统计详细
export function getPerformance(id) {
  return request({
    url: '/express/performance/' + id,
    method: 'get'
  })
}

// 新增司机业绩统计
export function addPerformance(data) {
  return request({
    url: '/express/performance',
    method: 'post',
    data: data
  })
}

// 修改司机业绩统计
export function updatePerformance(data) {
  return request({
    url: '/express/performance',
    method: 'put',
    data: data
  })
}

// 删除司机业绩统计
export function delPerformance(id) {
  return request({
    url: '/express/performance/' + id,
    method: 'delete'
  })
}
