<template>
	<view class="my-comments-page">
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
			<view class="nav-left" @click="goBack">
				<text class="back-arrow">‹</text>
			</view>
			<view class="nav-center">
				<text class="nav-title">我的评论</text>
			</view>
		</view> -->
		
		<!-- 评论列表 -->
		<view class="comments-container">
			<!-- 第一个评论 -->
			<view class="comment-item">
				<view class="comment-left">
					<view class="user-avatar">
						<view class="cat-avatar">
							<view class="cat-body"></view>
							<view class="cat-face">
								<view class="cat-eyes">
									<view class="cat-eye"></view>
									<view class="cat-eye"></view>
								</view>
							</view>
						</view>
					</view>
					<view class="comment-info">
						<text class="username">猫又屋之主</text>
						<text class="timestamp">刚刚</text>
						<text class="comment-text">有点太雷了,报看</text>
						<view class="action-button">
							<text class="like-icon">👍</text>
						</view>
					</view>
				</view>
				<view class="comment-right">
					<view class="content-thumbnail">
						<view class="thumbnail-placeholder"></view>
					</view>
				</view>
			</view>
			
			<!-- 第二个评论 -->
			<view class="comment-item">
				<view class="comment-left">
					<view class="user-avatar">
						<view class="cat-avatar">
							<view class="cat-body"></view>
							<view class="cat-face">
								<view class="cat-eyes">
									<view class="cat-eye"></view>
									<view class="cat-eye"></view>
								</view>
							</view>
						</view>
					</view>
					<view class="comment-info">
						<text class="username">猫又屋之主</text>
						<text class="timestamp">2小时前</text>
						<text class="comment-text">非常好猫猫,爱来自瓷器</text>
						<view class="action-button">
							<text class="like-icon">👍</text>
						</view>
					</view>
				</view>
				<view class="comment-right">
					<view class="content-thumbnail">
						<view class="thumbnail-placeholder cat-thumbnail"></view>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 底部导航栏 -->
		<view class="bottom-navigation">
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
		</view>
		
		<!-- 底部手势条 -->
		<view class="home-indicator"></view>
	</view>
</template>

<script>
	export default {
		name: 'MyComments',
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
			}
		}
	}
</script>

<style>
	.my-comments-page {
		width: 100%;
		min-height: 100vh;
		background-color: #ffffff;
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
	
	.signal {
		display: flex;
		align-items: flex-end;
		gap: 2rpx;
		height: 20rpx;
	}
	
	.bar {
		width: 6rpx;
		background-color: #000000;
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
		color: #000000;
	}
	
	.battery {
		display: flex;
		align-items: center;
		gap: 4rpx;
	}
	
	.battery-body {
		width: 32rpx;
		height: 16rpx;
		border: 2rpx solid #000000;
		border-radius: 2rpx;
		position: relative;
	}
	
	.battery-level {
		position: absolute;
		top: 2rpx;
		left: 2rpx;
		right: 2rpx;
		bottom: 2rpx;
		background-color: #000000;
		border-radius: 1rpx;
	}
	
	.battery-tip {
		width: 4rpx;
		height: 8rpx;
		background-color: #000000;
		border-radius: 0 2rpx 2rpx 0;
	}
	
	/* 导航栏 */
	.nav-bar {
		display: flex;
		align-items: center;
		padding: 20rpx 40rpx;
		background-color: #ffffff;
		border-bottom: 1rpx solid #f0f0f0;
		position: relative;
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
		color: #999;
		font-weight: bold;
	}
	
	.nav-center {
		flex: 1;
		display: flex;
		justify-content: center;
		align-items: center;
	}
	
	.nav-title {
		font-size: 36rpx;
		font-weight: bold;
		color: #333;
	}
	
	/* 评论容器 */
	.comments-container {
		flex: 1;
		background-color: #f8f8f8;
		padding: 20rpx 0;
	}
	
	/* 评论项 */
	.comment-item {
		display: flex;
		align-items: flex-start;
		padding: 30rpx 40rpx;
		background-color: #ffffff;
		margin-bottom: 20rpx;
	}
	
	.comment-left {
		flex: 1;
		display: flex;
		align-items: flex-start;
		gap: 20rpx;
	}
	
	.user-avatar {
		width: 80rpx;
		height: 80rpx;
		flex-shrink: 0;
	}
	
	.cat-avatar {
		width: 100%;
		height: 100%;
		position: relative;
	}
	
	.cat-body {
		width: 60rpx;
		height: 50rpx;
		background: linear-gradient(135deg, #FFA500, #FF8C00);
		border-radius: 30rpx;
		position: absolute;
		top: 15rpx;
		left: 10rpx;
	}
	
	.cat-face {
		position: absolute;
		top: 20rpx;
		left: 50%;
		transform: translateX(-50%);
		width: 40rpx;
		height: 30rpx;
	}
	
	.cat-eyes {
		display: flex;
		justify-content: space-between;
		gap: 8rpx;
	}
	
	.cat-eye {
		width: 6rpx;
		height: 6rpx;
		background-color: #000;
		border-radius: 50%;
	}
	
	.comment-info {
		flex: 1;
		display: flex;
		flex-direction: column;
		gap: 8rpx;
	}
	
	.username {
		font-size: 28rpx;
		color: #333;
		font-weight: 600;
	}
	
	.timestamp {
		font-size: 24rpx;
		color: #999;
	}
	
	.comment-text {
		font-size: 28rpx;
		color: #333;
		line-height: 1.4;
		margin: 8rpx 0;
	}
	
	.action-button {
		margin-top: 8rpx;
	}
	
	.like-icon {
		font-size: 32rpx;
		color: #666;
	}
	
	.comment-right {
		width: 120rpx;
		height: 120rpx;
		flex-shrink: 0;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.content-thumbnail {
		width: 100%;
		height: 100%;
	}
	
	.thumbnail-placeholder {
		width: 100%;
		height: 100%;
		background-color: #f0f0f0;
		border-radius: 8rpx;
		opacity: 0.7;
	}
	
	.cat-thumbnail {
		background: linear-gradient(135deg, #FFB6C1, #FFC0CB);
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
	
	.mini-cat-avatar {
		width: 100%;
		height: 100%;
		position: relative;
	}
	
	.mini-cat-body {
		width: 40rpx;
		height: 32rpx;
		background: linear-gradient(135deg, #FFA500, #FF8C00);
		border-radius: 20rpx;
		position: absolute;
		top: 8rpx;
		left: 4rpx;
	}
	
	.mini-cat-face {
		position: absolute;
		top: 10rpx;
		left: 50%;
		transform: translateX(-50%);
		width: 24rpx;
		height: 20rpx;
	}
	
	.mini-cat-eyes {
		display: flex;
		justify-content: space-between;
		margin-bottom: 4rpx;
	}
	
	.mini-cat-eye {
		width: 3rpx;
		height: 3rpx;
		background-color: #000;
		border-radius: 50%;
	}
	
	/* 底部手势条 */
	.home-indicator {
		position: fixed;
		bottom: 140rpx;
		left: 50%;
		transform: translateX(-50%);
		width: 120rpx;
		height: 6rpx;
		background-color: #000;
		border-radius: 3rpx;
	}
</style>
