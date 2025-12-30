import request from '@/utils/request'

// 查询消息通知记录列表
export function listNotice(query) {
  return request({
    url: '/express/notice/list',
    method: 'get',
    params: query
  })
}

// 查询消息通知记录详细
export function getNotice(id) {
  return request({
    url: '/express/notice/' + id,
    method: 'get'
  })
}

// 新增消息通知记录
export function addNotice(data) {
  return request({
    url: '/express/notice',
    method: 'post',
    data: data
  })
}

// 修改消息通知记录
export function updateNotice(data) {
  return request({
    url: '/express/notice',
    method: 'put',
    data: data
  })
}

// 删除消息通知记录
export function delNotice(id) {
  return request({
    url: '/express/notice/' + id,
    method: 'delete'
  })
}
