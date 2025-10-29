<template>
	<view class="container">
		<!-- 状态栏 -->
		<!-- <view class="status-bar">
			<text class="time">9:41</text>
			<view class="status-icons">
				<view class="signal">
					<view class="bar bar-1"></view>
					<view class="bar bar-2"></view>
					<view class="bar bar-3"></view>
					<view class="bar bar-4"></view>
				</view>
				<view class="wifi">📶</view>
				<view class="battery">
					<view class="battery-body">
						<view class="battery-level"></view>
					</view>
					<view class="battery-tip"></view>
				</view>
			</view>
		</view> -->
		
		<!-- 导航栏 -->
		<!-- <view class="nav-bar">
			<view class="back-button" @click="goBack">
				<text class="back-arrow">‹</text>
			</view>
			<text class="nav-title">更换账号</text>
		</view> -->
		
		<!-- 当前账号 -->
		<view class="current-account">
			<view class="account-header">
				<text class="header-text">当前账号</text>
			</view>
			<view class="account-item current">
				<view class="account-left">
					<view class="account-avatar">👩</view>
					<view class="account-info">
						<text class="account-name">当前用户</text>
						<text class="account-phone">138****8888</text>
					</view>
				</view>
				<view class="current-badge">当前</view>
			</view>
		</view>
		
		<!-- 其他账号 -->
		<view class="other-accounts">
			<view class="account-header">
				<text class="header-text">其他账号</text>
			</view>
			<view class="account-item" @click="switchToAccount('account1')">
				<view class="account-left">
					<view class="account-avatar">👨</view>
					<view class="account-info">
						<text class="account-name">张三</text>
						<text class="account-phone">139****9999</text>
					</view>
				</view>
				<text class="arrow">›</text>
			</view>
			<view class="account-item" @click="switchToAccount('account2')">
				<view class="account-left">
					<view class="account-avatar">👧</view>
					<view class="account-info">
						<text class="account-name">李四</text>
						<text class="account-phone">137****7777</text>
					</view>
				</view>
				<text class="arrow">›</text>
			</view>
		</view>
		
		<!-- 添加账号 -->
		<view class="add-account">
			<view class="account-item" @click="addNewAccount">
				<view class="account-left">
					<view class="account-avatar add-icon">+</view>
					<view class="account-info">
						<text class="account-name">添加账号</text>
						<text class="account-phone">使用其他账号登录</text>
					</view>
				</view>
				<text class="arrow">›</text>
			</view>
		</view>
		
		<!-- 底部操作按钮 -->
		<view class="bottom-actions">
			<view class="action-button logout" @click="logoutCurrent">
				<text class="action-text">退出当前账号</text>
			</view>
		</view>
		
		<!-- 底部手势条 -->
		<view class="home-indicator"></view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				
			}
		},
		methods: {
			goBack() {
				uni.navigateBack();
			},
			switchToAccount(accountId) {
				uni.showModal({
					title: '切换账号',
					content: '确定要切换到该账号吗？',
					success: (res) => {
						if (res.confirm) {
							uni.showToast({
								title: '账号切换成功',
								icon: 'success'
							});
							// 延迟跳转到首页
							setTimeout(() => {
								uni.redirectTo({
									url: '/pages/feed/content-feed'
								});
							}, 1500);
						}
					}
				});
			},
			addNewAccount() {
				uni.navigateTo({
					url: '/pages/login/login-replica'
				});
			},
			logoutCurrent() {
				uni.showModal({
					title: '确认退出',
					content: '确定要退出当前账号吗？',
					success: (res) => {
						if (res.confirm) {
							uni.showToast({
								title: '已退出当前账号',
								icon: 'success'
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
	.container {
		height: 100vh;
		background-color: #f8f8f8;
		display: flex;
		flex-direction: column;
	}
	
	/* 状态栏 */
	.status-bar {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 20rpx 40rpx;
		background-color: #fff;
		z-index: 1000;
		position: relative;
	}
	
	.time {
		font-size: 32rpx;
		font-weight: 600;
		color: #000;
	}
	
	.status-icons {
		display: flex;
		align-items: center;
		gap: 20rpx;
	}
	
	.signal {
		display: flex;
		align-items: flex-end;
		gap: 2rpx;
		height: 20rpx;
	}
	
	.bar {
		width: 6rpx;
		background-color: #000;
		border-radius: 2rpx;
	}
	
	.bar-1 {
		height: 8rpx;
	}
	
	.bar-2 {
		height: 12rpx;
	}
	
	.bar-3 {
		height: 16rpx;
	}
	
	.bar-4 {
		height: 20rpx;
	}
	
	.wifi {
		font-size: 24rpx;
		color: #000;
	}
	
	.battery {
		display: flex;
		align-items: center;
		gap: 4rpx;
	}
	
	.battery-body {
		width: 32rpx;
		height: 16rpx;
		border: 2rpx solid #000;
		border-radius: 2rpx;
		position: relative;
	}
	
	.battery-level {
		position: absolute;
		top: 2rpx;
		left: 2rpx;
		right: 2rpx;
		bottom: 2rpx;
		background-color: #000;
		border-radius: 1rpx;
	}
	
	.battery-tip {
		width: 4rpx;
		height: 8rpx;
		background-color: #000;
		border-radius: 0 2rpx 2rpx 0;
	}
	
	/* 导航栏 */
	.nav-bar {
		display: flex;
		align-items: center;
		padding: 20rpx 40rpx;
		background-color: #fff;
		border-bottom: 1rpx solid #f0f0f0;
		position: relative;
	}
	
	.back-button {
		width: 60rpx;
		height: 60rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
	}
	
	.back-arrow {
		font-size: 48rpx;
		color: #666;
		font-weight: bold;
	}
	
	.nav-title {
		position: absolute;
		left: 50%;
		transform: translateX(-50%);
		font-size: 36rpx;
		font-weight: bold;
		color: #000;
	}
	
	/* 账号分组 */
	.account-header {
		background-color: #f8f8f8;
		padding: 20rpx 40rpx;
	}
	
	.header-text {
		font-size: 28rpx;
		color: #666;
		font-weight: 500;
	}
	
	/* 账号项 */
	.account-item {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 30rpx 40rpx;
		background-color: #fff;
		border-bottom: 1rpx solid #f0f0f0;
	}
	
	.account-item:last-child {
		border-bottom: none;
	}
	
	.account-left {
		display: flex;
		align-items: center;
		gap: 20rpx;
	}
	
	.account-avatar {
		width: 80rpx;
		height: 80rpx;
		border-radius: 50%;
		background-color: #f0f0f0;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 32rpx;
		color: #666;
	}
	
	.add-icon {
		background-color: #e8f5e8;
		color: #28a745;
		font-size: 40rpx;
		font-weight: bold;
	}
	
	.account-info {
		display: flex;
		flex-direction: column;
		gap: 8rpx;
	}
	
	.account-name {
		font-size: 32rpx;
		color: #333;
		font-weight: 500;
	}
	
	.account-phone {
		font-size: 26rpx;
		color: #999;
	}
	
	.current-badge {
		background-color: #007AFF;
		color: #fff;
		font-size: 24rpx;
		padding: 8rpx 16rpx;
		border-radius: 20rpx;
	}
	
	.arrow {
		font-size: 36rpx;
		color: #ccc;
		font-weight: bold;
	}
	
	/* 当前账号样式 */
	.current-account {
		margin-bottom: 20rpx;
	}
	
	.current-account .account-item {
		background-color: #fff;
	}
	
	/* 其他账号样式 */
	.other-accounts {
		margin-bottom: 20rpx;
	}
	
	.other-accounts .account-item {
		background-color: #fff;
	}
	
	/* 添加账号样式 */
	.add-account {
		margin-bottom: 20rpx;
	}
	
	.add-account .account-item {
		background-color: #fff;
	}
	
	/* 底部操作按钮 */
	.bottom-actions {
		padding: 40rpx;
		background-color: #f8f8f8;
	}
	
	.action-button {
		width: 100%;
		text-align: center;
		padding: 30rpx 0;
		border-radius: 10rpx;
		background-color: #fff;
		border: 2rpx solid #ff6b6b;
	}
	
	.action-text {
		font-size: 32rpx;
		color: #ff6b6b;
		font-weight: 500;
	}
	
	/* 底部手势条 */
	.home-indicator {
		width: 100rpx;
		height: 10rpx;
		background-color: #000;
		border-radius: 5rpx;
		margin: 20rpx auto;
		opacity: 0.5;
	}
</style>
