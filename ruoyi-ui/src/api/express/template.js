import request from '@/utils/request'

// 查询快递模板配置列表
export function listTemplate(query) {
  return request({
    url: '/express/template/list',
    method: 'get',
    params: query
  })
}

// 查询快递模板配置详细
export function getTemplate(templateId) {
  return request({
    url: '/express/template/' + templateId,
    method: 'get'
  })
}

// 新增快递模板配置
export function addTemplate(data) {
  return request({
    url: '/express/template',
    method: 'post',
    data: data
  })
}

// 修改快递模板配置
export function updateTemplate(data) {
  return request({
    url: '/express/template',
    method: 'put',
    data: data
  })
}

// 删除快递模板配置
export function delTemplate(templateId) {
  return request({
    url: '/express/template/' + templateId,
    method: 'delete'
  })
}
