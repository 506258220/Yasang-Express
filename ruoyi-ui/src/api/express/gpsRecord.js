import request from '@/utils/request'

// 查询GPS定位记录列表
export function listRecord(query) {
  return request({
    url: '/express/gps/record/list',
    method: 'get',
    params: query
  })
}

// 查询GPS定位记录详细
export function getRecord(id) {
  return request({
    url: '/express/gps/record/' + id,
    method: 'get'
  })
}

// 新增GPS定位记录
export function addRecord(data) {
  return request({
    url: '/express/gps/record',
    method: 'post',
    data: data
  })
}

// 修改GPS定位记录
export function updateRecord(data) {
  return request({
    url: '/express/gps/record',
    method: 'put',
    data: data
  })
}

// 删除GPS定位记录
export function delRecord(id) {
  return request({
    url: '/express/gps/record/' + id,
    method: 'delete'
  })
}
