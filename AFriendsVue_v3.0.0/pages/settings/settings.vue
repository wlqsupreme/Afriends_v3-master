<template>
	<view class="settings-page">
		<!-- 状态栏 -->
		<!-- <view class="status-bar">
			<text class="time">12:00</text>
			<view class="status-icons">
				<view class="signal"></view>
				<view class="wifi"></view>
				<view class="battery"></view>
			</view>
		</view> -->
		
		<!-- 导航栏 -->
		<!-- <view class="header">
			<view class="back-button" @click="goBack">
				<view class="back-arrow"></view>
			</view>
			<text class="title">设置</text>
			<view class="placeholder"></view>
		</view> -->
		
		<!-- 设置内容 -->
		<view class="settings-content">
			<!-- 我的账户 -->
			<view class="settings-section">
				<view class="section-header">
					<text class="section-title">我的账户</text>
				</view>
				<view class="section-items">
					<view class="setting-item">
						<text class="item-text">暗夜模式</text>
						<view class="toggle-switch" :class="{ active: darkMode }" @click="toggleDarkMode">
							<view class="toggle-circle"></view>
						</view>
					</view>
					<view class="setting-item" @click="goToSwitchAccount">
						<text class="item-text">切换账号</text>
						<view class="arrow-right"></view>
					</view>
					<view class="setting-item" @click="goToAccountSecurity">
						<text class="item-text">账号与安全</text>
						<view class="arrow-right"></view>
					</view>
					<view class="setting-item" @click="goToModeSelection">
						<text class="item-text">模式选择</text>
						<view class="arrow-right"></view>
					</view>
				</view>
			</view>
			
			<!-- 账户设置 -->
			<view class="settings-section">
				<view class="section-header">
					<text class="section-title">账户设置</text>
				</view>
				<view class="section-items">
					<view class="setting-item" @click="goToNotifications">
						<text class="item-text">通知</text>
						<view class="arrow-right"></view>
					</view>
					<view class="setting-item" @click="goToPrivacySettings">
						<text class="item-text">隐私设置</text>
						<view class="arrow-right"></view>
					</view>
					<view class="setting-item" @click="goToLanguage">
						<text class="item-text">语言</text>
						<view class="arrow-right"></view>
					</view>
					<view class="setting-item" @click="goToBlockedUsers">
						<text class="item-text">拉黑用户</text>
						<view class="arrow-right"></view>
					</view>
					<!-- <view class="setting-item" @click="goToChatSettings">
						<text class="item-text">聊天设置</text>
						<view class="arrow-right"></view>
					</view> -->
				</view>
			</view>
			
			<!-- 帮助 -->
			<view class="settings-section">
				<view class="section-header">
					<text class="section-title">帮助</text>
				</view>
				<view class="section-items">
					<view class="setting-item" @click="goToTermsOfService">
						<text class="item-text">服务条款</text>
						<view class="arrow-right"></view>
					</view>
					<view class="setting-item" @click="goToCustomerService">
						<text class="item-text">客服与支持</text>
						<view class="arrow-right"></view>
					</view>
					<!-- <view class="setting-item" @click="goToUsageTutorial">
						<text class="item-text">使用教程</text>
						<view class="arrow-right"></view>
					</view> -->
				</view>
			</view>
			
			<!-- 账户操作 -->
			<view class="account-actions">
				<view class="action-button logout-account" @click="logoutAccount">
					<text class="action-text">注销账户</text>
				</view>
				<view class="action-button sign-out" @click="signOut">
					<text class="action-text">退出登录</text>
				</view>
			</view>
		</view>
		
		<!-- 底部导航栏 -->
		<!-- <view class="bottom-navigation">
			<view class="nav-item" @click="goToHome">
				<text class="nav-text">首页</text>
			</view>
			<view class="nav-item" @click="goToChatList">
				<text class="nav-text">消息</text>
			</view>
			<view class="nav-item" @click="goToAIChat">
				<view class="ai-tab">
					<text class="ai-text">AI</text>
				</view>
			</view>
			<view class="nav-item" @click="goToFriendList">
				<text class="nav-text">好友</text>
			</view>
			<view class="nav-avatar" @click="goToProfile">
				<image class="avatar-small" src="" mode="aspectFill"></image>
			</view>
		</view> -->
	</view>
</template>

<script>
	export default {
		name: 'SettingsPage',
		data() {
			return {
				darkMode: false
			}
		},
		methods: {
			goBack() {
				uni.navigateBack();
			},
			goToHome() {
				uni.navigateTo({
					url: '/pages/feed/content-feed'
				});
			},
			goToChatList() {
				uni.navigateTo({
					url: '/pages/chat/chat-list'
				});
			},
			goToAIChat() {
				uni.navigateTo({
					url: '/pages/chat/chat'
				});
			},
			goToFriendList() {
				uni.navigateTo({
					url: '/pages/chat/friend-list'
				});
			},
			goToProfile() {
				uni.navigateTo({
					url: '/pages/feed/user-profile'
				});
			},
			toggleDarkMode() {
				this.darkMode = !this.darkMode;
			},
			goToSwitchAccount() {
				uni.navigateTo({
					url: '/pages/settings/switch-account'
				});
			},
			goToAccountSecurity() {
				uni.navigateTo({
					url: '/pages/settings/account-security'
				});
			},
			goToModeSelection() {
				uni.navigateTo({
					url: '/pages/settings/mode-selection'
				});
			},
			goToNotifications() {
				uni.navigateTo({
					url: '/pages/settings/notification-settings'
				});
			},
			goToPrivacySettings() {
				uni.navigateTo({
					url: '/pages/settings/privacy-settings'
				});
			},
			goToLanguage() {
				uni.navigateTo({
					url: '/pages/settings/language'
				});
			},
			goToBlockedUsers() {
				uni.navigateTo({
					url: '/pages/settings/blocked-users'
				});
			},
			goToChatSettings() {
				uni.navigateTo({
					url: '/pages/settings/chat-settings'
				});
			},
			goToTermsOfService() {
				uni.navigateTo({
					url: '/pages/settings/terms-of-service'
				});
			},
			goToCustomerService() {
				uni.navigateTo({
					url: '/pages/chat/chat?friendName=客服'
				});
			},
			goToUsageTutorial() {
				uni.navigateTo({
					url: '/pages/settings/usage-tutorial'
				});
			},
			logoutAccount() {
				uni.showModal({
					title: '确认注销',
					content: '确定要注销账户吗？此操作不可逆。',
					success: (res) => {
						if (res.confirm) {
							uni.showToast({
								title: '账户已注销',
								icon: 'none',
								duration: 1500
							});
							
							// 延迟跳转到登录页面
							setTimeout(() => {
								uni.redirectTo({
									url: '/pages/login/login-replica'
								});
							}, 1500);
						}
					}
				});
			},
			signOut() {
				uni.showModal({
					title: '确认退出',
					content: '确定要退出登录吗？',
					success: (res) => {
						if (res.confirm) {
							uni.showToast({
								title: '已退出登录',
								icon: 'none',
								duration: 1500
							});
							
							// 延迟跳转到登录页面
							setTimeout(() => {
								uni.redirectTo({
									url: '/pages/login/login-replica'
								});
							}, 1500);
						}
					}
				});
			}
		}
	}
</script>

<style>
	.settings-page {
		min-height: 100vh;
		background-color: #FFFFFF;
		display: flex;
		flex-direction: column;
	}
	
	/* 状态栏 */
	.status-bar {
		height: 44rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 0 32rpx;
		background-color: #FFFFFF;
	}
	
	.time {
		font-size: 28rpx;
		color: #000000;
		font-weight: 600;
	}
	
	.status-icons {
		display: flex;
		align-items: center;
		gap: 8rpx;
	}
	
	.signal, .wifi, .battery {
		width: 24rpx;
		height: 24rpx;
		background-color: #000000;
		border-radius: 4rpx;
	}
	
	/* 导航栏 */
	.header {
		height: 88rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 32rpx;
		border-bottom: 1rpx solid #F0F0F0;
	}
	
	.back-button {
		width: 48rpx;
		height: 48rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.back-arrow {
		width: 0;
		height: 0;
		border-right: 12rpx solid #000000;
		border-top: 8rpx solid transparent;
		border-bottom: 8rpx solid transparent;
	}
	
	.title {
		font-size: 32rpx;
		color: #333333;
		font-weight: 600;
	}
	
	.placeholder {
		width: 48rpx;
	}
	
	/* 设置内容 */
	.settings-content {
		flex: 1;
		padding: 24rpx 0;
	}
	
	.settings-section {
		margin-bottom: 32rpx;
	}
	
	.section-header {
		padding: 16rpx 32rpx;
		background-color: #F8F8F8;
	}
	
	.section-title {
		font-size: 26rpx;
		color: #666666;
		font-weight: 500;
	}
	
	.section-items {
		background-color: #FFFFFF;
	}
	
	.setting-item {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 24rpx 32rpx;
		border-bottom: 1rpx solid #F0F0F0;
	}
	
	.setting-item:last-child {
		border-bottom: none;
	}
	
	.item-text {
		font-size: 28rpx;
		color: #333333;
		font-weight: 400;
	}
	
	/* 开关样式 */
	.toggle-switch {
		width: 80rpx;
		height: 44rpx;
		background-color: #E0E0E0;
		border-radius: 22rpx;
		position: relative;
		transition: all 0.3s ease;
	}
	
	.toggle-switch.active {
		background-color: #007AFF;
	}
	
	.toggle-circle {
		width: 40rpx;
		height: 40rpx;
		background-color: #FFFFFF;
		border-radius: 50%;
		position: absolute;
		top: 2rpx;
		left: 2rpx;
		transition: all 0.3s ease;
		box-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.1);
	}
	
	.toggle-switch.active .toggle-circle {
		left: 38rpx;
	}
	
	/* 右箭头 */
	.arrow-right {
		width: 0;
		height: 0;
		border-left: 8rpx solid #CCCCCC;
		border-top: 6rpx solid transparent;
		border-bottom: 6rpx solid transparent;
	}
	
	/* 账户操作 */
	.account-actions {
		padding: 32rpx;
		display: flex;
		flex-direction: column;
		gap: 16rpx;
	}
	
	.action-button {
		height: 88rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		border-radius: 16rpx;
		cursor: pointer;
	}
	
	.logout-account {
		background-color: #FF3B30;
	}
	
	.sign-out {
		background-color: #FF3B30;
	}
	
	.action-text {
		font-size: 28rpx;
		color: #FFFFFF;
		font-weight: 500;
	}
	
	/* 底部导航栏样式 */
	.bottom-navigation {
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		height: 120rpx;
		background-color: #FFFFFF;
		border-top: 1rpx solid #F0F0F0;
		display: flex;
		align-items: center;
		justify-content: space-around;
		padding: 0 32rpx;
		z-index: 100;
	}
	
	.nav-item {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 8rpx;
		cursor: pointer;
		padding: 16rpx;
		border-radius: 12rpx;
		transition: all 0.2s ease;
	}
	
	.nav-item:active {
		background-color: #F5F5F5;
		transform: scale(0.95);
	}
	
	.nav-text {
		font-size: 24rpx;
		color: #666666;
		font-weight: 500;
	}
	
	.nav-item.active .nav-text {
		color: #FF69B4;
		font-weight: 600;
	}
	
	.ai-tab {
		width: 48rpx;
		height: 48rpx;
		background: linear-gradient(135deg, #FF69B4, #FF8E53);
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.ai-text {
		font-size: 20rpx;
		color: #FFFFFF;
		font-weight: 600;
	}
	
	.nav-avatar {
		width: 48rpx;
		height: 48rpx;
	}
	
	.avatar-small {
		width: 100%;
		height: 100%;
		border-radius: 50%;
		background: linear-gradient(135deg, #FFA500, #FF8C00);
	}
	
	.cat-avatar::before {
		content: '';
		position: absolute;
		top: 8rpx;
		left: 8rpx;
		width: 8rpx;
		height: 8rpx;
		background-color: #000000;
		border-radius: 50%;
	}
	
	.cat-avatar::after {
		content: '';
		position: absolute;
		top: 8rpx;
		right: 8rpx;
		width: 8rpx;
		height: 8rpx;
		background-color: #000000;
		border-radius: 50%;
	}
</style>


