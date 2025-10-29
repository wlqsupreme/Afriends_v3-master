<template>
  <view class="login-container">
    <!-- 主内容区域 -->
    <view class="main-content">
      <!-- 应用Logo -->
      <view class="app-header">
        <image class="app-logo" src="/static/afriends-logo.png" mode="aspectFit"></image>
      </view>
      
      <!-- 问候语和欢迎文字 -->
      <view class="greeting-section">
        <text class="greeting-text">修改密码</text>
        <text class="welcome-text">请您修改当前账号密码，在修改后需要重新登录</text>
      </view>
      
      <!-- 登录表单 -->
      <view class="login-form">
                 <!-- 新密码输入框 -->
         <view class="input-group">
           <text class="input-label">新密码</text>
           <view class="input-wrapper">
                           <input 
                class="form-input" 
                type="password" 
                v-model="username"
              />
             <view class="input-underline"></view>
           </view>
         </view>
        
                 <!-- 确认密码输入框 -->
         <view class="input-group">
           <text class="input-label">请再次输入新密码</text>
           <view class="input-wrapper">
                           <input 
                class="form-input" 
                type="password" 
                v-model="password"
              />
             <view class="input-underline"></view>
           </view>
         </view>
        

        
        <!-- 用户协议 -->
        <view class="terms-section">
          <text class="terms-link" @click="goToTermsOfService">服务条款</text>
        </view>
        
        <!-- 确认按钮 -->
        <button class="login-btn" @click="handleConfirm" :disabled="!isFormValid">
          <text class="btn-text">修改密码</text>
        </button>
        
        <!-- 返回登录链接 -->
        <view class="register-section">
          <text class="register-text">没有账号？</text>
          <text class="register-link" @click="goToLogin">注册账号</text>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      username: '',
      password: ''
    }
  },
  computed: {
    isFormValid() {
      // 检查两个密码是否都已输入且一致
      return this.username && this.password && this.username === this.password;
    }
  },
  methods: {
    handleConfirm() {
      if (!this.username || !this.password) {
        uni.showToast({
          title: '请输入新密码和确认密码',
          icon: 'none'
        })
        return
      }
      
      if (this.username !== this.password) {
        uni.showToast({
          title: '两次输入的密码不一致',
          icon: 'none'
        })
        return
      }
      
      // 这里添加密码修改逻辑
      console.log('密码修改信息:', {
        newPassword: this.username
      })
      
      uni.showToast({
        title: '密码修改成功',
        icon: 'success'
      })
      
      // 密码修改成功后跳转到账号密码登录界面
      setTimeout(() => {
        uni.navigateTo({
          url: '/pages/login/login-replica'
        })
      }, 1500)
    },
    

    goToTermsOfService() {
      uni.navigateTo({
        url: '/pages/settings/terms-of-service'
      })
    }

  }
}
</script>

<style scoped>
/* 全局样式已内联 */

.login-container {
  width: 375px;
  height: 812px;
  background-color: #FFFFFF;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  margin: 0 auto;
}



.main-content {
  flex: 1;
  padding: 0px 32px 20px 32px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
}

.app-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 0px;
  margin-top: 50px;
}

.app-logo {
  width: 240px;
  height: 100px;
  margin: 0;
  padding: 0;
  display: block;
  box-sizing: border-box;
  object-fit: contain;
}

.greeting-section {
  width: 100%;
  max-width: 320px;
  margin-bottom: 25px;
  text-align: left;
  padding-left: 0px;
}

.greeting-text {
  font-size: 36px;
  font-weight: bold;
  color: #333333;
  display: block;
  margin-bottom: 8px;
}

.welcome-text {
  font-size: 12px;
  color: #666666;
  display: block;
  line-height: 1.5;
}

.login-form {
  width: 100%;
  max-width: 320px;
  padding-left: 16px;
  padding-right: 16px;
}

.input-group {
  margin-bottom: 5px;
}

.input-label {
  font-size: 12px;
  color: #666666;
  display: block;
  margin-bottom: 0px;
  font-weight: 500;
}

.input-wrapper {
  position: relative;
}

.form-input {
  width: 100%;
  height: 38px;
  padding: 0 16px;
  font-size: 16px;
  color: #333333;
  background-color: transparent;
  border: none;
  outline: none;
}

.placeholder {
  color: #999999;
}

.input-underline {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 1px;
  background-color: #EF0056;
}

.form-input:focus + .input-underline {
  background-color: #EF0056;
}



.terms-section {
  text-align: center;
  margin-top: 240px;
  margin-bottom: 5px;
  line-height: 1.5;
  padding: 0 16px;
}

.terms-text {
  font-size: 12px;
  color: #999999;
}

.terms-link {
  font-size: 12px;
  color: #1286DD;
  cursor: pointer;
}

.login-btn {
  width: 100%;
  height: 44px;
  background-color: #EF0056;
  border: none;
  border-radius: 10px;
  margin-bottom: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.login-btn:disabled {
  background-color: #CCCCCC;
  cursor: not-allowed;
}

.login-btn:not(:disabled):active {
  background-color: #D1004A;
}

.btn-text {
  font-size: 16px;
  font-weight: 600;
  color: #FFFFFF;
}

.register-section {
  text-align: center;
  padding: 0 16px;
}

.register-text {
  font-size: 14px;
  color: #999999;
}

.register-link {
  font-size: 14px;
  color: #1286DD;
  cursor: pointer;
  margin-left: 4px;
}

/* 响应式设计 */
@media (max-width: 375px) {
  .main-content {
    padding: 0px 24px 20px 24px;
  }
  
  .app-logo {
    width: 200px;
    height: 80px;
    box-sizing: border-box;
    object-fit: contain;
  }
  
  .greeting-section {
    max-width: 280px;
  }
  
  .login-form {
    max-width: 280px;
  }
}

@media (min-width: 414px) {
  .main-content {
    padding: 0px 40px 30px 40px;
  }
  
  .app-logo {
    width: 140px;
    height: 50px;
    box-sizing: border-box;
    object-fit: contain;
  }
  
  .greeting-section {
    max-width: 360px;
  }
  
  .login-form {
    max-width: 360px;
  }
}
</style> 