import request from '@/utils/request'

// 查询面单模板列表
export function listWaybillTemplate(query) {
  return request({
    url: '/express/template/list',
    method: 'get',
    params: query
  })
}

// 查询面单模板详细
export function getWaybillTemplate(id) {
  return request({
    url: '/express/template/' + id,
    method: 'get'
  })
}

// 新增面单模板
export function addWaybillTemplate(data) {
  return request({
    url: '/express/template',
    method: 'post',
    data: data
  })
}

// 修改面单模板
export function updateWaybillTemplate(data) {
  return request({
    url: '/express/template',
    method: 'put',
    data: data
  })
}

// 删除面单模板
export function delWaybillTemplate(ids) {
  return request({
    url: '/express/template/' + ids,
    method: 'delete'
  })
}

// 预览面单模板
export function previewWaybillTemplate(id) {
  return request({
    url: '/express/template/preview/' + id,
    method: 'get'
  })
}

// 根据订单和模板生成打印HTML
export function generatePrintHtml(orderId, templateId) {
  return request({
    url: '/express/template/print',
    method: 'post',
    data: {
      orderId: orderId,
      templateId: templateId
    }
  })
}
