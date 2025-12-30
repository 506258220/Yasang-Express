import request from '@/utils/request'

// 查询车辆信息列表
export function listVehicle(query) {
  return request({
    url: '/express/vehicle/list',
    method: 'get',
    params: query
  })
}

// 查询车辆信息详细
export function getVehicle(id) {
  return request({
    url: '/express/vehicle/' + id,
    method: 'get'
  })
}

// 新增车辆信息
export function addVehicle(data) {
  return request({
    url: '/express/vehicle',
    method: 'post',
    data: data
  })
}

// 修改车辆信息
export function updateVehicle(data) {
  return request({
    url: '/express/vehicle',
    method: 'put',
    data: data
  })
}

// 删除车辆信息
export function delVehicle(id) {
  return request({
    url: '/express/vehicle/' + id,
    method: 'delete'
  })
}
