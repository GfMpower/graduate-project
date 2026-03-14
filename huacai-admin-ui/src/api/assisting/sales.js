import request from '@/utils/request'

/**
 * 获取农户销售统计数据
 * @param {number} userId - 农户用户ID
 * @param {string} timeRange - 时间范围：day(近7天)、week(近4周)、month(近6个月)、year(近2年)
 * @returns {Promise} - 返回销售统计数据
 */
export function getSalesStatistics(userId, timeRange) {
  return request({
    url: '/assisting/orders/salesStatistics',
    method: 'get',
    params: {
      userId,
      timeRange
    }
  })
}