<template>
  <!-- 外层容器，确保整个页面是白底黑字 -->
  <view class="system-permissions-page">
    <!-- 顶部导航栏区域 -->
    <!-- <view class="header"> -->
      <!-- 返回箭头，使用 SVG 图标 -->
      <!-- <view @click="goBack" class="back-button">
        <svg fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg" class="back-icon">
          <path fill-rule="evenodd" d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z" clip-rule="evenodd"></path>
        </svg>
      </view> -->
      <!-- 页面标题 -->
      <!-- <text class="page-title">系统权限</text> -->
    <!-- </view> -->

    <!-- 权限列表区域 -->
    <view class="permissions-list">
      <!-- 通讯录权限 -->
      <view class="permission-item" @click="goToSystemSettings('通讯录权限')">
        <text class="permission-text">通讯录权限</text>
        <text class="permission-arrow">></text>
      </view>
      <!-- 相册权限 -->
      <view class="permission-item" @click="goToAlbumPermissions">
        <text class="permission-text">相册权限</text>
        <view class="permission-action">
          <text class="action-text">去设置</text>
          <text class="permission-arrow">></text>
        </view>
      </view>
      <!-- 位置权限 -->
      <view class="permission-item" @click="showPermissionDialog('位置权限', 'location')">
        <text class="permission-text">位置权限</text>
        <view class="permission-action">
          <text class="action-text">去设置</text>
          <text class="permission-arrow">></text>
        </view>
      </view>
      <!-- 相机权限 -->
      <view class="permission-item" @click="showPermissionDialog('相机权限', 'camera')">
        <text class="permission-text">相机权限</text>
        <view class="permission-action">
          <text class="action-text">去设置</text>
          <text class="permission-arrow">></text>
        </view>
      </view>
      <!-- 麦克风权限 -->
      <view class="permission-item" @click="showPermissionDialog('麦克风权限', 'microphone')">
        <text class="permission-text">麦克风权限</text>
        <view class="permission-action">
          <text class="action-text">去设置</text>
          <text class="permission-arrow">></text>
        </view>
      </view>
      <!-- 其他权限 -->
      <view class="permission-item" @click="goToSystemSettings('其他权限')">
        <text class="permission-text">其他权限</text>
        <view class="permission-action">
          <text class="action-text">去设置</text>
          <text class="permission-arrow">></text>
        </view>
      </view>

      <!-- 剪切板读取权限区域 -->
      <view class="clipboard-section">
        <view class="clipboard-header">
          <text class="clipboard-title">剪切板读取权限</text>
          <!-- uni-app的switch组件 -->
          <switch :checked="clipboardEnabled" @change="toggleClipboard" color="#007aff" class="clipboard-switch" />
        </view>
        <text class="clipboard-description">
          关闭后，将无法自动识别你复制的口令以展示相应内容
        </text>
      </view>
    </view>

    <!-- 权限弹窗 -->
    <view class="permission-overlay" v-if="showDialog" @click="hidePermissionDialog">
      <view class="permission-dialog" @click.stop>
        <!-- 关闭按钮 -->
        <view class="close-button" @click="hidePermissionDialog">
          <text class="close-icon">×</text>
        </view>
        
        <!-- 权限图标 -->
        <view class="permission-icon">
          <view class="icon-container" :class="currentPermissionType">
            <text class="icon-text">{{ getPermissionIcon(currentPermissionType) }}</text>
          </view>
        </view>
        
        <!-- 权限标题 -->
        <text class="dialog-title">{{ currentPermissionTitle }}</text>
        
        <!-- 权限描述 -->
        <text class="dialog-description">{{ getPermissionDescription(currentPermissionType) }}</text>
        
        <!-- 操作按钮 -->
        <view class="dialog-buttons">
          <view class="button button-secondary" @click="hidePermissionDialog">
            <text class="button-text">再想想</text>
          </view>
          <view class="button button-primary" @click="goToSystemSettings(currentPermissionTitle)">
            <text class="button-text">去设置</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  // 定义组件的数据
  data() {
    return {
      clipboardEnabled: true, // 剪切板权限开关的初始状态
      showDialog: false, // 是否显示权限弹窗
      currentPermissionTitle: '', // 当前权限标题
      currentPermissionType: '' // 当前权限类型
    };
  },
  // 定义组件的方法
  methods: {
    /**
     * @description 返回上一页
     * 在uni-app中，通常使用 uni.navigateBack()
     */
    goBack() {
      console.log('返回上一页');
      uni.navigateBack();
    },
    /**
     * @description 切换剪切板权限开关状态
     * @param {Object} e - 事件对象，e.detail.value 包含开关的当前值
     */
    toggleClipboard(e) {
      this.clipboardEnabled = e.detail.value;
      console.log('剪切板权限已切换为:', this.clipboardEnabled);
      // 这里可以添加调用本地API来实际修改权限的逻辑
    },
    /**
     * @description 跳转到相册权限页面
     */
    goToAlbumPermissions() {
      uni.navigateTo({
        url: '/pages/settings/album-permissions'
      });
    },
    /**
     * @description 显示权限弹窗
     * @param {String} permissionTitle - 权限标题
     * @param {String} permissionType - 权限类型
     */
    showPermissionDialog(permissionTitle, permissionType) {
      this.currentPermissionTitle = permissionTitle;
      this.currentPermissionType = permissionType;
      this.showDialog = true;
    },
    /**
     * @description 隐藏权限弹窗
     */
    hidePermissionDialog() {
      this.showDialog = false;
    },
    /**
     * @description 获取权限图标
     * @param {String} permissionType - 权限类型
     * @returns {String} 图标文本
     */
    getPermissionIcon(permissionType) {
      const icons = {
        'location': '📍',
        'camera': '📷',
        'microphone': '🎤'
      };
      return icons[permissionType] || '⚙️';
    },
    /**
     * @description 获取权限描述
     * @param {String} permissionType - 权限类型
     * @returns {String} 描述文本
     */
    getPermissionDescription(permissionType) {
      const descriptions = {
        'location': '关闭后,将无法为你推荐位置相关的服务。',
        'camera': '关闭后,将无法使用抖音拍摄作品。',
        'microphone': '关闭后,将无法使用抖音拍摄作品。'
      };
      return descriptions[permissionType] || '关闭后,将无法使用相关功能。';
    },
    /**
     * @description 跳转到系统设置
     * @param {String} permissionType - 权限类型
     */
    goToSystemSettings(permissionType) {
      console.log('前往系统设置:', permissionType);
      // 在uni-app中，这里可以调用系统设置API
      // 例如：uni.openAppAuthorizeSetting() 或 uni.openSetting()
      uni.showModal({
        title: '跳转系统设置',
        content: `即将跳转到系统设置页面，请在"${permissionType}"中开启相应权限。`,
        showCancel: false,
        success: () => {
          // 这里可以调用系统设置API
          // uni.openAppAuthorizeSetting();
          uni.showToast({
            title: '系统设置功能开发中',
            icon: 'none'
          });
        }
      });
    }
  }
};
</script>

<style>
.system-permissions-page {
  min-height: 100vh;
  background-color: #FFFFFF;
  display: flex;
  flex-direction: column;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

/* 顶部导航栏 */
.header {
  height: 88rpx;
  display: flex;
  align-items: center;
  padding: 0 32rpx;
  background-color: #FFFFFF;
  border-bottom: 1rpx solid #F0F0F0;
  position: relative;
}

.back-button {
  width: 48rpx;
  height: 48rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 24rpx;
  background: rgba(0, 0, 0, 0.05);
  transition: all 0.2s ease;
}

.back-button:active {
  background: rgba(0, 0, 0, 0.1);
  transform: scale(0.95);
}

.back-icon {
  width: 24rpx;
  height: 24rpx;
  color: #000000;
}

.page-title {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  font-size: 32rpx;
  color: #000000;
  font-weight: 600;
}

/* 权限列表 */
.permissions-list {
  flex: 1;
  padding: 0 32rpx;
}

.permission-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 32rpx 0;
  border-bottom: 1rpx solid #F0F0F0;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.permission-item:active {
  background-color: rgba(0, 0, 0, 0.05);
}

.permission-text {
  font-size: 28rpx;
  color: #000000;
  font-weight: 500;
}

.permission-action {
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.action-text {
  font-size: 24rpx;
  color: #666666;
}

.permission-arrow {
  font-size: 32rpx;
  color: #CCCCCC;
  font-weight: 300;
}

/* 剪切板权限区域 */
.clipboard-section {
  margin: 48rpx 0;
  padding: 32rpx;
  background-color: #F8F8F8;
  border-radius: 16rpx;
}

.clipboard-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16rpx;
}

.clipboard-title {
  font-size: 28rpx;
  color: #000000;
  font-weight: 600;
}

.clipboard-switch {
  transform: scale(0.9);
}

.clipboard-description {
  font-size: 24rpx;
  color: #666666;
  line-height: 1.5;
}

/* 权限弹窗 */
.permission-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.permission-dialog {
  width: 600rpx;
  background-color: #FFFFFF;
  border-radius: 24rpx;
  padding: 48rpx;
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
}

/* 关闭按钮 */
.close-button {
  position: absolute;
  top: 24rpx;
  right: 24rpx;
  width: 48rpx;
  height: 48rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.close-icon {
  font-size: 32rpx;
  color: #000000;
  font-weight: 300;
}

/* 权限图标 */
.permission-icon {
  margin-bottom: 32rpx;
}

.icon-container {
  width: 120rpx;
  height: 120rpx;
  border: 4rpx solid #000000;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: transparent;
}

.icon-text {
  font-size: 48rpx;
}

/* 弹窗标题 */
.dialog-title {
  font-size: 32rpx;
  color: #000000;
  font-weight: 600;
  margin-bottom: 24rpx;
  text-align: center;
}

/* 弹窗描述 */
.dialog-description {
  font-size: 26rpx;
  color: #666666;
  line-height: 1.5;
  text-align: center;
  margin-bottom: 48rpx;
}

/* 弹窗按钮 */
.dialog-buttons {
  display: flex;
  gap: 24rpx;
  width: 100%;
}

.button {
  flex: 1;
  height: 88rpx;
  border-radius: 44rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s ease;
}

.button-secondary {
  background-color: #FFFFFF;
  border: 2rpx solid #E0E0E0;
}

.button-primary {
  background-color: #FF3B30;
}

.button:active {
  transform: scale(0.98);
}

.button-text {
  font-size: 28rpx;
  font-weight: 500;
}

.button-secondary .button-text {
  color: #000000;
}

.button-primary .button-text {
  color: #FFFFFF;
}
</style>
