import request from '@/utils/request'

// 查询第三方商品订单（用于导入后批量打印快递单）列表
export function listOrder(query) {
  return request({
    url: '/express/third/order/list',
    method: 'get',
    params: query
  })
}

// 查询第三方商品订单（用于导入后批量打印快递单）详细
export function getOrder(orderId) {
  return request({
    url: '/express/third/order/' + orderId,
    method: 'get'
  })
}

// 新增第三方商品订单（用于导入后批量打印快递单）
export function addOrder(data) {
  return request({
    url: '/express/third/order',
    method: 'post',
    data: data
  })
}

// 修改第三方商品订单（用于导入后批量打印快递单）
export function updateOrder(data) {
  return request({
    url: '/express/third/order',
    method: 'put',
    data: data
  })
}

// 删除第三方商品订单（用于导入后批量打印快递单）
export function delOrder(orderId) {
  return request({
    url: '/express/third/order/' + orderId,
    method: 'delete'
  })
}

// 批量生成快递订单
export function batchGenerateExpressOrders(orderIds, config) {
  return request({
    url: '/express/third/order/generateExpressOrders',
    method: 'post',
    data: {
      orderIds: orderIds,
      config: config
    }
  })
}

// 导入第三方商品订单
export function importOrder(data) {
  return request({
    url: '/express/third/order/importData',
    method: 'post',
    data: data
  })
}

// 导出第三方商品订单模板
export function importTemplate() {
  return request({
    url: '/express/third/order/importTemplate',
    method: 'post',
    responseType: 'blob'
  })
}
