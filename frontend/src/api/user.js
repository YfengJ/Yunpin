import request from '@/utils/request'

// 登录接口
export function login(data) {
  return request({
    url: '/users/login',
    method: 'post',
    data
  })
}

// 根据用户名查询用户（用于登录验证）
export function getUserByUsername(username) {
  return request({
    url: `/users/username/${username}`,
    method: 'get'
  })
}



