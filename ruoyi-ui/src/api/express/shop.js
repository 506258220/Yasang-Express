import request from '@/utils/request'

// 查询店铺列表
export function listShop(query) {
  return request({
    url: '/express/shop/list',
    method: 'get',
    params: query
  })
}

// 查询店铺详细
export function getShop(shopId) {
  return request({
    url: '/express/shop/' + shopId,
    method: 'get'
  })
}

// 新增店铺
export function addShop(data) {
  return request({
    url: '/express/shop',
    method: 'post',
    data: data
  })
}

// 修改店铺
export function updateShop(data) {
  return request({
    url: '/express/shop',
    method: 'put',
    data: data
  })
}

// 删除店铺
export function delShop(shopId) {
  return request({
    url: '/express/shop/' + shopId,
    method: 'delete'
  })
}

// 修改店铺状态
export function changeStatus(data) {
  return request({
    url: '/express/shop/changeStatus',
    method: 'put',
    data: data
  })
}