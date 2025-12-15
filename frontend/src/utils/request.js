import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({
    // 🔥 修改点：改为 /api，让 vite.config.js 里的代理去处理转发
    baseURL: '/api',
    timeout: 10000 // 延长一点超时时间，防止冷启动超时
})

// 请求拦截器
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';

    // 修正：从 localStorage 获取 token (登录时存的是 'token')
    const token = localStorage.getItem("token");
    if (token) {
        config.headers['token'] = token; // 这里的 key 要看后端定义，通常是 token 或 Authorization
    }
    return config
}, error => {
    return Promise.reject(error)
});

// 响应拦截器
request.interceptors.response.use(
    response => {
        let res = response.data;
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        return res;
    },
    error => {
        console.error('请求出错:', error) // 在控制台打印详细错误
        ElMessage({
            message: error.message || '连接服务器失败',
            type: 'error',
            duration: 5 * 1000
        })
        return Promise.reject(error)
    }
)

export default request