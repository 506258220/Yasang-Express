import request from '@/utils/request'

// 查询仓库出入库记录列表
export function listRecord(query) {
  return request({
    url: '/express/warehouse/record/list',
    method: 'get',
    params: query
  })
}

// 查询仓库出入库记录详细
export function getRecord(id) {
  return request({
    url: '/express/warehouse/record/' + id,
    method: 'get'
  })
}

// 新增仓库出入库记录
export function addRecord(data) {
  return request({
    url: '/express/warehouse/record',
    method: 'post',
    data: data
  })
}

// 修改仓库出入库记录
export function updateRecord(data) {
  return request({
    url: '/express/warehouse/record',
    method: 'put',
    data: data
  })
}

// 删除仓库出入库记录
export function delRecord(id) {
  return request({
    url: '/express/warehouse/record/' + id,
    method: 'delete'
  })
}
