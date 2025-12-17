import request from '@/utils/request'

// 查询快递单列表
export function listWaybill(query) {
  return request({
    url: '/express/waybill/list',
    method: 'get',
    params: query
  })
}

// 查询快递单详细
export function getWaybill(waybillId) {
  return request({
    url: '/express/waybill/' + waybillId,
    method: 'get'
  })
}

// 新增快递单
export function addWaybill(data) {
  return request({
    url: '/express/waybill',
    method: 'post',
    data: data
  })
}

// 修改快递单
export function updateWaybill(data) {
  return request({
    url: '/express/waybill',
    method: 'put',
    data: data
  })
}

// 删除快递单
export function delWaybill(waybillId) {
  return request({
    url: '/express/waybill/' + waybillId,
    method: 'delete'
  })
}
