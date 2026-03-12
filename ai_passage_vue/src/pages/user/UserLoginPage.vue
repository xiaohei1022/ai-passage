<template>
  <div id="userLoginPage">
    <!-- 动态背景 -->
    <div class="bg-gradient"></div>
    <div class="bg-shapes">
      <div class="shape shape-1"></div>
      <div class="shape shape-2"></div>
      <div class="shape shape-3"></div>
    </div>

    <!-- 登录卡片 -->
    <div class="login-container">
      <div class="login-card">
        <!-- Logo区域 -->
        <div class="card-header">
          <div class="logo">
            <RobotOutlined />
          </div>
          <h1 class="title">AI Passage</h1>
          <p class="subtitle">智能文章创作平台</p>
        </div>

        <!-- 欢迎文字 -->
        <div class="welcome-section">
          <h2>欢迎回来 👋</h2>
          <p>登录您的账号继续创作</p>
        </div>

        <!-- 登录表单 -->
        <a-form :model="formState" @finish="handleSubmit" class="login-form">
          <a-form-item 
            name="userAccount" 
            :rules="[{ required: true, message: '请输入账号' }]"
          >
            <a-input 
              v-model:value="formState.userAccount" 
              placeholder="请输入账号" 
              size="large"
              class="custom-input"
            >
              <template #prefix>
                <UserOutlined class="input-icon"/>
              </template>
            </a-input>
          </a-form-item>

          <a-form-item
            name="userPassword"
            :rules="[
              { required: true, message: '请输入密码' },
              { min: 8, message: '密码长度不能小于 8 位' },
            ]"
          >
            <a-input-password 
              v-model:value="formState.userPassword" 
              placeholder="请输入密码" 
              size="large"
              class="custom-input"
            >
              <template #prefix>
                <LockOutlined class="input-icon"/>
              </template>
            </a-input-password>
          </a-form-item>

          <div class="form-options">
            <a-checkbox>记住密码</a-checkbox>
          </div>

          <a-form-item>
            <a-button 
              type="primary" 
              html-type="submit" 
              size="large" 
              block 
              class="submit-btn"
              :loading="loading"
            >
              登 录
            </a-button>
          </a-form-item>
        </a-form>

        <!-- 底部 -->
        <div class="card-footer">
          <span>还没有账号？</span>
          <RouterLink to="/user/register" class="register-link">立即注册</RouterLink>
        </div>
      </div>

      <!-- 版权信息 -->
      <div class="copyright">
        © 2026 AI Passage. All Rights Reserved.
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>

import {reactive, ref} from "vue";
import {useRouter} from "vue-router";
import {useLoginUserStore} from "@/stores/loginUser.ts";
import {userLogin} from "@/api/userController.ts";
import {message} from "ant-design-vue";
import { UserOutlined, LockOutlined, RobotOutlined, GithubOutlined, GoogleOutlined } from '@ant-design/icons-vue';

const formState = reactive<API.UserLoginRequest>({
  userAccount: '',
  userPassword: '',
})

const router = useRouter()
const loginUserStore = useLoginUserStore()
const loading = ref(false)

const handleSubmit = async (values: any) => {
  loading.value = true
  try {
    const res = await userLogin(values)
    if (res.data.code === 0 && res.data.data) {
      await loginUserStore.fetchLoginUser()
      message.success('登录成功')
      router.push({
        path: '/',
        replace: true,
      })
    } else {
      message.error('登录失败，' + res.data.message)
    }
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
/* ===== 背景样式 ===== */
#userLoginPage {
  min-height: 100vh;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
  overflow-x: hidden;
  background: #0f0f23;
  box-sizing: border-box;
  padding: 20px;
}

.bg-gradient {
  position: absolute;
  inset: 0;
  background: 
    radial-gradient(ellipse at 20% 20%, rgba(99, 102, 241, 0.15) 0%, transparent 50%),
    radial-gradient(ellipse at 80% 80%, rgba(139, 92, 246, 0.15) 0%, transparent 50%),
    radial-gradient(ellipse at 50% 50%, rgba(59, 130, 246, 0.1) 0%, transparent 70%),
    linear-gradient(180deg, #0f0f23 0%, #1a1a3e 100%);
  z-index: 0;
}

.bg-shapes {
  position: absolute;
  inset: 0;
  overflow: hidden;
  z-index: 1;
}

.shape {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.5;
  animation: float 20s ease-in-out infinite;
}

.shape-1 {
  width: 400px;
  height: 400px;
  background: linear-gradient(135deg, #6366f1, #8b5cf6);
  top: -100px;
  left: -100px;
  animation-delay: 0s;
}

.shape-2 {
  width: 300px;
  height: 300px;
  background: linear-gradient(135deg, #06b6d4, #3b82f6);
  bottom: -50px;
  right: -50px;
  animation-delay: -7s;
}

.shape-3 {
  width: 200px;
  height: 200px;
  background: linear-gradient(135deg, #8b5cf6, #ec4899);
  top: 50%;
  right: 20%;
  animation-delay: -14s;
}

@keyframes float {
  0%, 100% {
    transform: translate(0, 0) rotate(0deg);
  }
  25% {
    transform: translate(30px, -30px) rotate(5deg);
  }
  50% {
    transform: translate(-20px, 20px) rotate(-5deg);
  }
  75% {
    transform: translate(20px, 30px) rotate(3deg);
  }
}

/* ===== 登录容器 ===== */
.login-container {
  position: relative;
  z-index: 10;
  width: 100%;
  max-width: 420px;
}

/* ===== 登录卡片 ===== */
.login-card {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 24px;
  padding: 32px 28px;
  box-shadow: 
    0 25px 50px -12px rgba(0, 0, 0, 0.5),
    inset 0 1px 0 rgba(255, 255, 255, 0.1);
}

/* ===== 头部 ===== */
.card-header {
  text-align: center;
  margin-bottom: 20px;
}

.logo {
  width: 60px;
  height: 60px;
  margin: 0 auto 12px;
  font-size: 28px;
  background: linear-gradient(135deg, #6366f1, #8b5cf6);
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 36px;
  color: white;
  box-shadow: 0 10px 40px rgba(99, 102, 241, 0.4);
}

.title {
  font-size: 28px;
  font-weight: 700;
  color: #fff;
  margin: 0 0 8px 0;
  background: linear-gradient(135deg, #fff, #a5b4fc);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.subtitle {
  color: rgba(255, 255, 255, 0.5);
  font-size: 13px;
  margin: 0;
}

/* ===== 欢迎区域 ===== */
.welcome-section {
  text-align: center;
  margin-bottom: 20px;
}

.welcome-section h2 {
  font-size: 20px;
  font-weight: 600;
  color: #fff;
  margin: 0 0 8px 0;
}

.welcome-section p {
  color: rgba(255, 255, 255, 0.5);
  font-size: 14px;
  margin: 0;
}

/* ===== 表单样式 ===== */
.login-form :deep(.ant-form-item) {
  margin-bottom: 16px;
}

/* 输入框容器 */
.custom-input :deep(.ant-input-affix-wrapper) {
  background: linear-gradient(135deg, #ffffff, #f8f9fa) !important;
  border: 1px solid rgba(139, 92, 246, 0.3) !important;
  border-radius: 14px !important;
  padding: 4px 14px !important;
  height: 48px !important;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 
    0 4px 20px rgba(0, 0, 0, 0.1),
    inset 0 1px 0 rgba(255, 255, 255, 0.8);
}

.custom-input :deep(.ant-input-affix-wrapper:hover) {
  border-color: rgba(139, 92, 246, 0.6) !important;
  transform: translateY(-2px);
  box-shadow: 
    0 8px 25px rgba(139, 92, 246, 0.2),
    inset 0 1px 0 rgba(255, 255, 255, 0.1);
}

.custom-input :deep(.ant-input-affix-wrapper-focused) {
  border-color: #8b5cf6 !important;
  box-shadow: 
    0 0 0 4px rgba(139, 92, 246, 0.25),
    0 8px 30px rgba(139, 92, 246, 0.3) !important;
  transform: translateY(-2px);
}

/* 输入框本身 */
.custom-input :deep(.ant-input) {
  background: transparent !important;
  border: none !important;
  color: #1a1a2e !important;
  font-size: 15px !important;
  padding: 0 !important;
  height: 38px !important;
  line-height: 38px !important;
  box-shadow: none !important;
}

.custom-input :deep(.ant-input::placeholder) {
  color: rgba(26, 26, 46, 0.5) !important;
}

/* 密码输入框 */
.custom-input :deep(.ant-input-password input) {
  color: #1a1a2e !important;
  background: transparent !important;
  font-size: 15px !important;
  height: 38px !important;
  line-height: 38px !important;
}

/* 图标 */
.custom-input :deep(.ant-input-prefix) {
  margin-right: 12px !important;
}

.input-icon {
  color: #8b5cf6;
  font-size: 20px;
  filter: drop-shadow(0 0 8px rgba(139, 92, 246, 0.5));
}

/* 密码可见性图标 */
.custom-input :deep(.ant-input-password-icon) {
  color: rgba(255, 255, 255, 0.5) !important;
  font-size: 18px !important;
  transition: all 0.3s;
}

.custom-input :deep(.ant-input-password-icon:hover) {
  color: #8b5cf6 !important;
}

/* ===== 表单验证提示 ===== */
.login-form :deep(.ant-form-item-explain-error) {
  color: #f87171;
  font-size: 13px;
  margin-top: 4px;
  padding-left: 4px;
}

/* ===== 表单选项 ===== */
.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.form-options :deep(.ant-checkbox-wrapper) {
  color: rgba(255, 255, 255, 0.6);
}

.form-options :deep(.ant-checkbox-inner) {
  background: rgba(255, 255, 255, 0.1);
  border-color: rgba(255, 255, 255, 0.2);
}

.forgot-link {
  color: #8b5cf6;
  font-size: 14px;
  transition: color 0.3s;
}

.forgot-link:hover {
  color: #a78bfa;
}

/* ===== 提交按钮 ===== */
.submit-btn {
  height: 44px;
  border-radius: 12px;
  font-size: 15px;
  font-weight: 600;
  border: none;
  background: linear-gradient(135deg, #6366f1, #8b5cf6);
  transition: all 0.3s ease;
  box-shadow: 0 4px 20px rgba(99, 102, 241, 0.4);
}

.submit-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 30px rgba(99, 102, 241, 0.5);
}

.submit-btn:active {
  transform: translateY(0);
}

/* ===== 分割线 ===== */
.divider {
  display: flex;
  align-items: center;
  margin: 24px 0;
  color: rgba(255, 255, 255, 0.3);
  font-size: 13px;
}

.divider::before,
.divider::after {
  content: '';
  flex: 1;
  height: 1px;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.1), transparent);
}

.divider span {
  padding: 0 16px;
}

/* ===== 社交登录 ===== */
.social-login {
  display: flex;
  justify-content: center;
  gap: 16px;
}

.social-btn {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  color: rgba(255, 255, 255, 0.6);
  font-size: 20px;
  transition: all 0.3s ease;
}

.social-btn:hover {
  background: rgba(255, 255, 255, 0.1);
  border-color: rgba(255, 255, 255, 0.2);
  color: #fff;
  transform: translateY(-2px);
}

/* ===== 底部 ===== */
.card-footer {
  text-align: center;
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid rgba(255, 255, 255, 0.08);
  color: rgba(255, 255, 255, 0.5);
  font-size: 13px;
}

.register-link {
  color: #8b5cf6;
  margin-left: 6px;
  font-weight: 500;
  transition: color 0.3s;
}

.register-link:hover {
  color: #a78bfa;
}

/* ===== 版权 ===== */
.copyright {
  text-align: center;
  margin-top: 12px;
  color: rgba(255, 255, 255, 0.25);
  font-size: 12px;
}

/* ===== 响应式 ===== */
@media (max-width: 480px) {
  .login-card {
    padding: 28px 20px;
  }
  
  .logo {
    width: 60px;
    height: 60px;
    font-size: 28px;
  }
  
  .title {
    font-size: 24px;
  }
}
</style>
