<template>
  <view class="privacy-settings">
    <!-- 顶部导航栏 -->
    <!-- <view class="nav-bar">
      <view class="nav-left" @click="goBack">
        <text class="back-arrow">‹</text>
      </view>
      <view class="nav-title">隐私设置</view>
      <view class="nav-right"></view>
    </view> -->

    <!-- 主要内容区域 -->
    <scroll-view class="content" scroll-y>
      <!-- 互动设置模块 -->
      <view class="section">
        <view class="section-header">
          <text class="section-title">互动</text>
        </view>
        <view class="section-content">
          <view class="setting-item">
            <view class="setting-info">
              <text class="setting-label">一键防护</text>
              <text class="setting-desc">开启后,7天内将不接收陌生人的好友申请/评论/分享</text>
            </view>
            <view class="toggle-switch" @click="toggleSetting('oneClickProtection')">
              <view class="toggle-track" :class="{ active: settings.oneClickProtection }">
                <view class="toggle-thumb" :class="{ active: settings.oneClickProtection }"></view>
              </view>
            </view>
          </view>
          
          <!-- <view class="setting-item">
            <view class="setting-info">
              <text class="setting-label">展示我的状态</text>
            </view>
            <view class="toggle-switch" @click="toggleSetting('showMyStatus')">
              <view class="toggle-track" :class="{ active: settings.showMyStatus }">
                <view class="toggle-thumb" :class="{ active: settings.showMyStatus }"></view>
              </view>
            </view>
          </view> -->
          
          <view class="setting-item">
            <view class="setting-info">
              <text class="setting-label">只允许我的好友评论我</text>
            </view>
            <view class="toggle-switch" @click="toggleSetting('allowFriendsComment')">
              <view class="toggle-track" :class="{ active: settings.allowFriendsComment }">
                <view class="toggle-thumb" :class="{ active: settings.allowFriendsComment }"></view>
              </view>
            </view>
          </view>
          
          <view class="setting-item">
            <view class="setting-info">
              <text class="setting-label">只允许我的好友@我</text>
            </view>
            <view class="toggle-switch" @click="toggleSetting('allowFriendsAt')">
              <view class="toggle-track" :class="{ active: settings.allowFriendsAt }">
                <view class="toggle-thumb" :class="{ active: settings.allowFriendsAt }"></view>
              </view>
            </view>
          </view>
          
          <view class="setting-item">
            <view class="setting-info">
              <text class="setting-label">公开我的收藏</text>
            </view>
            <view class="toggle-switch" @click="toggleSetting('publicCollections')">
              <view class="toggle-track" :class="{ active: settings.publicCollections }">
                <view class="toggle-thumb" :class="{ active: settings.publicCollections }"></view>
              </view>
            </view>
          </view>
        </view>
      </view>

      <!-- 朋友设置模块 -->
      <view class="section">
        <view class="section-header">
          <text class="section-title">朋友</text>
        </view>
        <view class="section-content">
          <view class="setting-item">
            <view class="setting-info">
              <text class="setting-label">加我为好友时需要验证</text>
            </view>
            <view class="toggle-switch" @click="toggleSetting('friendVerification')">
              <view class="toggle-track" :class="{ active: settings.friendVerification }">
                <view class="toggle-thumb" :class="{ active: settings.friendVerification }"></view>
              </view>
            </view>
          </view>
          
          <view class="setting-item" @click="goToAddWays">
            <view class="setting-info">
              <text class="setting-label">添加我的方式</text>
            </view>
            <view class="arrow-right">
              <text class="arrow-icon">›</text>
            </view>
          </view>
          
          <view class="setting-item">
            <view class="setting-info">
              <text class="setting-label">给我推荐可能认识的人</text>
            </view>
            <view class="toggle-switch" @click="toggleSetting('recommendFriends')">
              <view class="toggle-track" :class="{ active: settings.recommendFriends }">
                <view class="toggle-thumb" :class="{ active: settings.recommendFriends }"></view>
              </view>
            </view>
          </view>
          
          <view class="setting-item" @click="goToBlacklist">
            <view class="setting-info">
              <text class="setting-label">黑名单用户</text>
            </view>
            <view class="arrow-right">
              <text class="arrow-icon">›</text>
            </view>
          </view>
        </view>
      </view>

      <!-- 权限设置模块 -->
      <view class="section">
        <view class="section-header">
          <text class="section-title">权限</text>
        </view>
        <view class="section-content">
          <view class="setting-item" @click="goToSystemPermissions">
            <view class="setting-info">
              <text class="setting-label">系统权限管理</text>
              <text class="setting-desc">APP内使用的所有系统权限</text>
            </view>
            <view class="arrow-right">
              <text class="arrow-icon">›</text>
            </view>
          </view>
        </view>
      </view>
    </scroll-view>
  </view>
</template>

<script>
export default {
  name: 'PrivacySettings',
  data() {
    return {
      settings: {
        oneClickProtection: false,
        showMyStatus: false,
        allowFriendsComment: false,
        allowFriendsAt: false,
        publicCollections: false,
        friendVerification: false,
        recommendFriends: false
      }
    }
  },
  methods: {
    goBack() {
      uni.navigateBack()
    },
    

    
    toggleSetting(key) {
      this.settings[key] = !this.settings[key]
      // 这里可以添加API调用
      uni.showToast({
        title: '设置已保存',
        icon: 'success'
      })
    },
    
    goToSystemPermissions() {
      // 跳转到系统权限管理页面
      uni.navigateTo({
        url: '/pages/settings/system-permissions'
      })
    },
    
    goToAddWays() {
      // 跳转到添加我的方式页面
      uni.navigateTo({
        url: '/pages/settings/add-ways'
      })
    },
    
    goToBlacklist() {
      // 跳转到黑名单用户页面
      uni.navigateTo({
        url: '/pages/settings/blocked-users'
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.privacy-settings {
  min-height: 100vh;
  background-color: #FAFAFA;
}

.nav-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 88rpx;
  padding: 0 32rpx;
  background-color: #ffffff;
  border-bottom: 1rpx solid #efefef;
  position: sticky;
  top: 0;
  z-index: 100;
}

.nav-left {
  width: 60rpx;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.back-arrow {
  font-size: 48rpx;
  color: #000000;
  font-weight: 300;
  line-height: 1;
}

.nav-title {
  font-size: 36rpx;
  font-weight: 600;
  color: #000000;
}

.nav-right {
  width: 60rpx;
}

.content {
  flex: 1;
  padding: 0;
}

.section {
  background-color: #FFFFFF;
  border-radius: 0;
  margin-bottom: 0;
  overflow: hidden;
  border-bottom: 1rpx solid #efefef;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  padding: 16rpx 32rpx;
  background-color: #FAFAFA;
}

.section-title {
  font-size: 20rpx; /* 分类标题 10pt */
  font-weight: 400;
  color: #666666;
  text-transform: uppercase;
}

.arrow-right {
  width: 40rpx;
  height: 40rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.arrow-icon {
  font-size: 32rpx;
  color: #000000;
  font-weight: 300;
}

.section-content {
  padding: 0;
}

.setting-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 4rpx;
}

.setting-desc {
  font-size: 28rpx; /* 解释文字 14pt */
  color: #666666;
  line-height: 1.4;
  font-weight: 400;
}

.setting-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16rpx 32rpx;
  border-bottom: 1rpx solid #efefef;
  background-color: #FFFFFF;
}

.setting-item:last-child {
  border-bottom: none;
}

.setting-label {
  font-size: 32rpx; /* 功能文字 16pt */
  color: #000000;
  font-weight: 400;
}

.toggle-switch {
  width: 100rpx;
  height: 60rpx;
  display: flex;
  align-items: center;
  justify-content: center;
}

.toggle-track {
  width: 100rpx;
  height: 56rpx;
  background-color: #e5e5e5;
  border-radius: 28rpx;
  position: relative;
  transition: background-color 0.3s ease;
}

.toggle-track.active {
  background-color: #007aff;
}

.toggle-thumb {
  width: 52rpx;
  height: 52rpx;
  background-color: #ffffff;
  border-radius: 26rpx;
  position: absolute;
  left: 2rpx;
  top: 2rpx;
  transition: transform 0.3s ease;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
}

.toggle-thumb.active {
  transform: translateX(44rpx);
}

/* 响应式设计 */
@media (max-width: 750rpx) {
  .content {
    padding: 16rpx;
  }
  
  .section {
    margin-bottom: 16rpx;
  }
  
  .section-header {
    padding: 24rpx 24rpx 16rpx;
  }
  
  .section-content {
    padding: 16rpx 24rpx 24rpx;
  }
  
  .setting-item {
    padding: 20rpx 0;
  }
}
</style> 