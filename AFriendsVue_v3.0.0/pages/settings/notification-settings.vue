<template>
	<view class="notification-settings-page">
		<!-- 状态栏 -->
		<!-- <view class="status-bar">
			<text class="status-time">9:41</text>
			<view class="status-right">
				<text class="status-signal">📶</text>
				<text class="status-wifi">📶</text>
				<text class="status-battery">🔋</text>
			</view>
		</view> -->
		
		<!-- 导航栏 -->
		<!-- <view class="nav-bar">
			<view class="nav-left" @click="goBack">
				<text class="back-arrow">←</text>
			</view>
			<view class="nav-center">
				<text class="nav-title">通知设置</text>
			</view>
			<view class="nav-right"></view>
		</view> -->
		
		<!-- 内容区域 -->
		<view class="content-area">
			<!-- 聊天消息通知分组 -->
			<view class="section-group">
				<view class="section-header">
					<text class="section-title">聊天消息</text>
				</view>
				
				<view class="settings-list">
					<view class="setting-item">
						<view class="setting-left">
							<text class="setting-label">聊天消息通知</text>
						</view>
						<view class="setting-right">
							<switch class="setting-switch" :checked="notificationSettings.chatNotification" @change="toggleSetting('chatNotification', $event)"></switch>
						</view>
					</view>
					
					<view class="setting-item" @click="goToNotificationDisplay">
						<text class="setting-label">通知显示内容</text>
						<text class="setting-arrow">›</text>
					</view>
				</view>
			</view>
			
			<!-- 互动通知分组 -->
			<view class="section-group">
				<view class="section-header">
					<text class="section-title">互动通知</text>
				</view>
				
				<view class="settings-list">
					<view class="setting-item">
						<view class="setting-left">
							<text class="setting-label">赞和收藏</text>
						</view>
						<view class="setting-right">
							<switch class="setting-switch" :checked="notificationSettings.likeAndFavorite" @change="toggleSetting('likeAndFavorite', $event)"></switch>
						</view>
					</view>
					
					<view class="setting-item">
						<view class="setting-left">
							<text class="setting-label">评论</text>
						</view>
						<view class="setting-right">
							<switch class="setting-switch" :checked="notificationSettings.comment" @change="toggleSetting('comment', $event)"></switch>
						</view>
					</view>
					
					<view class="setting-item">
						<view class="setting-left">
							<text class="setting-label">@</text>
						</view>
						<view class="setting-right">
							<switch class="setting-switch" :checked="notificationSettings.mention" @change="toggleSetting('mention', $event)"></switch>
						</view>
					</view>
				</view>
			</view>
			
			<!-- 推荐通知分组 -->
			<view class="section-group">
				<view class="section-header">
					<text class="section-title">推荐通知</text>
				</view>
				
				<view class="settings-list">
					<view class="setting-item">
						<view class="setting-left">
							<text class="setting-label">内容推荐</text>
						</view>
						<view class="setting-right">
							<switch class="setting-switch" :checked="notificationSettings.contentRecommend" @change="toggleSetting('contentRecommend', $event)"></switch>
						</view>
					</view>
					
					<view class="setting-item">
						<view class="setting-left">
							<text class="setting-label">用户推荐</text>
						</view>
						<view class="setting-right">
							<switch class="setting-switch" :checked="notificationSettings.userRecommend" @change="toggleSetting('userRecommend', $event)"></switch>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 底部手势条 -->
		<!-- <view class="home-indicator"></view> -->
	</view>
</template>

<script>
	export default {
		name: 'NotificationSettings',
		data() {
			return {
				notificationSettings: {
					chatNotification: true,
					likeAndFavorite: false,
					comment: false,
					mention: false,
					contentRecommend: false,
					userRecommend: false
				}
			}
		},
		methods: {
			goBack() {
				uni.navigateBack();
			},
			goToNotificationDisplay() {
				uni.navigateTo({
					url: '/pages/settings/notification-display'
				});
			},
			toggleSetting(key, event) {
				this.notificationSettings[key] = event.detail.value;
				
				let settingName = '';
				switch(key) {
					case 'chatNotification':
						settingName = '聊天消息';
						break;
					case 'likeAndFavorite':
						settingName = '赞和收藏';
						break;
					case 'comment':
						settingName = '评论';
						break;
					case 'mention':
						settingName = '@';
						break;
					case 'contentRecommend':
						settingName = '内容推荐';
						break;
					case 'userRecommend':
						settingName = '用户推荐';
						break;
				}
				
				uni.showToast({
					title: `${settingName}通知已${event.detail.value ? '开启' : '关闭'}`,
					icon: 'none'
				});
			}
		}
	}
</script>

<style>
	.notification-settings-page {
		width: 100%;
		min-height: 100vh;
		background-color: #f8f8f8;
		display: flex;
		flex-direction: column;
	}
	
	/* 状态栏 */
	.status-bar {
		height: 88rpx;
		background-color: #ffffff;
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 0 32rpx;
		font-size: 28rpx;
		font-weight: 600;
	}
	
	.status-right {
		display: flex;
		gap: 8rpx;
	}
	
	/* 导航栏 */
	.nav-bar {
		height: 88rpx;
		background-color: #ffffff;
		display: flex;
		align-items: center;
		padding: 0 32rpx;
		border-bottom: 1rpx solid #f0f0f0;
	}
	
	.nav-left {
		width: 80rpx;
		height: 88rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
		padding: 0 16rpx;
		z-index: 10;
		position: relative;
	}
	
	.nav-left:active {
		background-color: rgba(0, 0, 0, 0.1);
	}
	
	.back-arrow {
		font-size: 48rpx;
		color: #333333;
		font-weight: 300;
	}
	
	.nav-center {
		flex: 1;
		display: flex;
		justify-content: center;
	}
	
	.nav-title {
		font-size: 32rpx;
		color: #333333;
		font-weight: 600;
	}
	
	.nav-right {
		width: 80rpx;
	}
	
	/* 内容区域 */
	.content-area {
		flex: 1;
		padding: 32rpx 0;
	}
	
	.section-group {
		margin-bottom: 32rpx;
	}
	
	.section-header {
		padding: 0 32rpx 16rpx 32rpx;
	}
	
	.section-title {
		font-size: 28rpx;
		color: #999999;
		font-weight: 500;
	}
	
	.settings-list {
		background-color: #ffffff;
	}
	
	.setting-item {
		padding: 32rpx;
		border-bottom: 1rpx solid #f0f0f0;
		display: flex;
		align-items: center;
		justify-content: space-between;
		min-height: 88rpx;
		cursor: pointer;
	}
	
	.setting-item:last-child {
		border-bottom: none;
	}
	
	.setting-left {
		flex: 1;
	}
	
	.setting-label {
		font-size: 32rpx;
		color: #333333;
	}
	
	.setting-right {
		display: flex;
		align-items: center;
	}
	
	.setting-arrow {
		font-size: 32rpx;
		color: #cccccc;
		font-weight: 300;
	}
	
	.setting-switch {
		transform: scale(0.8);
	}
	
	/* 底部手势条 */
	.home-indicator {
		height: 68rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.home-indicator::after {
		content: '';
		width: 200rpx;
		height: 8rpx;
		background-color: #333333;
		border-radius: 4rpx;
	}
</style>
