import request from '@/utils/request'

// 查询虚拟号管理列表
export function listNumber(query) {
  return request({
    url: '/express/number/list',
    method: 'get',
    params: query
  })
}

// 查询虚拟号管理详细
export function getNumber(id) {
  return request({
    url: '/express/number/' + id,
    method: 'get'
  })
}

// 新增虚拟号管理
export function addNumber(data) {
  return request({
    url: '/express/number',
    method: 'post',
    data: data
  })
}

// 修改虚拟号管理
export function updateNumber(data) {
  return request({
    url: '/express/number',
    method: 'put',
    data: data
  })
}

// 删除虚拟号管理
export function delNumber(id) {
  return request({
    url: '/express/number/' + id,
    method: 'delete'
  })
}
