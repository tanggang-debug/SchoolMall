export const ORDER_STATUS_MAP = {
  0: { label: '待支付', type: 'warning' },
  1: { label: '已支付', type: 'success' },
  2: { label: '待发货', type: 'info' },
  3: { label: '已发货', type: 'primary' },
  4: { label: '已完成', type: 'success' },
  5: { label: '已取消', type: 'danger' },
  6: { label: '退款中', type: 'warning' },
  7: { label: '已退款', type: 'info' }
}

export const PRODUCT_STATUS_MAP = {
  0: { label: '待审核', type: 'warning' },
  1: { label: '已上架', type: 'success' },
  2: { label: '已下架', type: 'info' },
  3: { label: '已驳回', type: 'danger' }
}

export const ROLE_MAP = {
  1: '学生',
  2: '商户',
  3: '管理员'
}

export const USER_STATUS_MAP = {
  0: { label: '正常', type: 'success' },
  1: { label: '已冻结', type: 'danger' }
}

export function formatCurrency(value) {
  const amount = Number(value ?? 0)
  return Number.isNaN(amount) ? '0.00' : amount.toFixed(2)
}

export function parseImageList(images) {
  if (!images) return []
  if (Array.isArray(images)) return images.filter(Boolean)
  if (typeof images === 'string') {
    try {
      const parsed = JSON.parse(images)
      return Array.isArray(parsed) ? parsed.filter(Boolean) : [images]
    } catch {
      return images.split(',').map(item => item.trim()).filter(Boolean)
    }
  }
  return []
}

export function getFirstImage(images, fallback = 'https://picsum.photos/400/400') {
  return parseImageList(images)[0] || fallback
}

export function getOrderStatusMeta(status, label) {
  return ORDER_STATUS_MAP[status] || { label: label || '未知状态', type: 'info' }
}

export function getProductStatusMeta(status, label) {
  return PRODUCT_STATUS_MAP[status] || { label: label || '未知状态', type: 'info' }
}

export function getRoleLabel(role) {
  return ROLE_MAP[role] || '未知角色'
}

export function getUserStatusMeta(status) {
  return USER_STATUS_MAP[status] || { label: '未知状态', type: 'info' }
}

export function formatAddress(address) {
  if (!address) return ''
  if (typeof address === 'string') return address
  return [
    address.receiverName,
    address.phone,
    [address.province, address.city, address.district, address.detail].filter(Boolean).join('')
  ].filter(Boolean).join(' ')
}
