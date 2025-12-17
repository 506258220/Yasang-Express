import request from '@/utils/request'

// 查询物流轨迹列表
export function listTrack(query) {
  return request({
    url: '/express/track/list',
    method: 'get',
    params: query
  })
}

// 查询物流轨迹详细
export function getTrack(trackId) {
  return request({
    url: '/express/track/' + trackId,
    method: 'get'
  })
}

// 新增物流轨迹
export function addTrack(data) {
  return request({
    url: '/express/track',
    method: 'post',
    data: data
  })
}

// 修改物流轨迹
export function updateTrack(data) {
  return request({
    url: '/express/track',
    method: 'put',
    data: data
  })
}

// 删除物流轨迹
export function delTrack(trackId) {
  return request({
    url: '/express/track/' + trackId,
    method: 'delete'
  })
}
