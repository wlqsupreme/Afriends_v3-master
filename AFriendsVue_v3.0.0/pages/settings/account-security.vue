<template>
	<view class="account-security-page">
		<!-- 状态栏 -->
		<!-- <view class="status-bar">
			<text class="time">9:41</text>
			<view class="status-icons">
				<view class="signal"></view>
				<view class="wifi"></view>
				<view class="battery"></view>
			</view>
		</view> -->
		
		<!-- 头部导航 -->
		<!-- <view class="header">
			<view class="back-button" @click="goBack">
				<view class="back-arrow"></view>
			</view>
			<text class="page-title">账号与安全</text>
			<view class="placeholder"></view>
		</view> -->
		
		<!-- 安全设置列表 -->
		<view class="security-list">
			<!-- 第一组：基础信息 -->
			<view class="list-group">
				<view class="list-item" @click="goToPhoneNumber">
					<text class="item-label">手机号</text>
					<view class="item-content">
						<text class="item-value">+86133****8358</text>
						<view class="arrow-icon">></view>
					</view>
				</view>
				<view class="divider"></view>
				<view class="list-item" @click="goToPassword">
					<text class="item-label">登录密码</text>
					<view class="item-content">
						<text class="item-value">未设置</text>
						<view class="arrow-icon">></view>
					</view>
				</view>
			</view>
			
			<!-- 第二组：第三方账号绑定 -->
			<view class="list-group">
				<view class="list-item" @click="goToThirdPartyBinding">
					<text class="item-label">第三方账号绑定</text>
					<view class="item-content">
						<view class="arrow-icon">></view>
					</view>
				</view>
			</view>
			
			<!-- 第三组：认证信息 -->
			<view class="list-group">
				<view class="list-item" @click="goToRealNameVerification">
					<text class="item-label">实名认证</text>
					<view class="item-content">
						<view class="arrow-icon">></view>
					</view>
				</view>
				<view class="divider"></view>
				<view class="list-item" @click="goToOfficialCertification">
					<text class="item-label">官方认证</text>
					<view class="item-content">
						<view class="arrow-icon">></view>
					</view>
				</view>
			</view>
			
			<!-- 第四组：账户管理 -->
			<view class="list-group">
				<view class="list-item" @click="goToDeviceManagement">
					<text class="item-label">登录设备管理</text>
					<view class="item-content">
						<view class="arrow-icon">></view>
					</view>
				</view>
				<view class="divider"></view>
				<!-- <view class="list-item" @click="goToAccountManagement">
					<text class="item-label">账号管理</text>
					<view class="item-content">
						<view class="arrow-icon">></view>
					</view>
				</view> -->
				<view class="divider"></view>
				<view class="list-item" @click="goToAccountDeletion">
					<text class="item-label">注销账号</text>
					<view class="item-content">
						<view class="arrow-icon">></view>
					</view>
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
		
		<!-- 底部指示器 -->
		<!-- <view class="home-indicator"></view> -->
	</view>
</template>

<script>
	export default {
		name: 'AccountSecurityPage',
		data() {
			return {
				
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
			goToPhoneNumber() {
				uni.showToast({
					title: '手机号设置',
					icon: 'none'
				});
			},
			goToPassword() {
				uni.showToast({
					title: '密码设置',
					icon: 'none'
				});
			},
			goToThirdPartyBinding() {
				uni.navigateTo({
					url: '/pages/settings/third-party-binding'
				});
			},
			goToRealNameVerification() {
				uni.navigateTo({
					url: '/pages/verification/real-name-verification'
				});
			},
			goToOfficialCertification() {
				uni.navigateTo({
					url: '/pages/verification/official-certification'
				});
			},
			goToDeviceManagement() {
				uni.navigateTo({
					url: '/pages/settings/device-management'
				});
			},
			goToAccountManagement() {
				uni.navigateTo({
					url: '/pages/settings/switch-account'
				});
			},
			goToAccountDeletion() {
				uni.showToast({
					title: '注销账号',
					icon: 'none'
				});
			}
		}
	}
</script>

<style>
	.account-security-page {
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
	
	/* 头部导航 */
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
		border-right: 12rpx solid #666666;
		border-top: 8rpx solid transparent;
		border-bottom: 8rpx solid transparent;
	}
	
	.page-title {
		font-size: 32rpx;
		color: #333333;
		font-weight: 600;
	}
	
	.placeholder {
		width: 48rpx;
	}
	
	/* 安全设置列表 */
	.security-list {
		flex: 1;
		padding: 0 32rpx;
	}
	
	.list-group {
		margin-bottom: 24rpx;
		background-color: #FFFFFF;
		border-radius: 16rpx;
		overflow: hidden;
		box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
	}
	
	.list-item {
		height: 120rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 24rpx;
		background-color: #FFFFFF;
		cursor: pointer;
	}
	
	.list-item:active {
		background-color: #F8F8F8;
	}
	
	.item-label {
		font-size: 28rpx;
		color: #333333;
		font-weight: 500;
	}
	
	.item-content {
		display: flex;
		align-items: center;
		gap: 16rpx;
	}
	
	.item-value {
		font-size: 26rpx;
		color: #999999;
	}
	
	.arrow-icon {
		font-size: 28rpx;
		color: #CCCCCC;
		font-weight: 300;
	}
	
	.divider {
		height: 1rpx;
		background-color: #F0F0F0;
		margin: 0 24rpx;
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
	
	/* 底部手势条 */
	.home-indicator {
		position: fixed;
		bottom: 0;
		left: 50%;
		transform: translateX(-50%);
		width: 134rpx;
		height: 8rpx;
		background-color: rgba(255, 255, 255, 0.3);
		border-radius: 4rpx;
		z-index: 101;
	}
</style>
