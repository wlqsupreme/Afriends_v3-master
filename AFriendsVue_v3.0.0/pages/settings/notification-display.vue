<!-- 通知显示内容设置页面 -->
<template>
	<view class="notification-display-page">
		<!-- 导航栏 -->
		<!-- <view class="nav-bar">
			<view class="nav-left" @click="goBack">
				<text class="back-arrow">←</text>
			</view>
			<view class="nav-center">
				<text class="nav-title">通知显示内容</text>
			</view>
			<view class="nav-right"></view>
		</view> -->
		
		<!-- 内容区域 -->
		<view class="content-area">
			<!-- 通知显示方式选项 -->
			<view class="display-options">
				<view class="option-item" 
					v-for="(option, index) in displayOptions" 
					:key="index"
					:class="{ active: currentDisplayMode === option.value }"
					@click="selectDisplayMode(option.value)">
					<view class="option-info">
						<text class="option-title">{{ option.title }}</text>
						<text class="option-desc">{{ option.description }}</text>
					</view>
					<view class="option-check" v-if="currentDisplayMode === option.value">
						<text class="check-icon">✓</text>
					</view>
				</view>
			</view>
			
			<!-- 说明文字 -->
			<view class="display-note">
				<text class="note-text">选择通知在锁屏和通知栏中的显示方式</text>
			</view>
		</view>
		
		<!-- 底部手势条 -->
		<view class="home-indicator"></view>
	</view>
</template>

<script>
	export default {
		name: 'NotificationDisplay',
		data() {
			return {
				currentDisplayMode: 'full',
				displayOptions: [
					{
						value: 'minimal',
						title: '仅显示【你收到了一条消息】',
						description: '最简洁的通知方式，保护隐私'
					},
					{
						value: 'medium',
						title: '显示朋友名称、群聊名',
						description: '显示发送者信息，但不显示具体内容'
					},
					{
						value: 'full',
						title: '显示朋友名称、群聊名及消息内容',
						description: '显示完整的消息信息'
					}
				]
			}
		},
		onLoad() {
			// 加载当前通知显示设置
			this.loadCurrentDisplayMode()
		},
		methods: {
			goBack() {
				uni.navigateBack()
			},
			
			loadCurrentDisplayMode() {
				try {
					const savedMode = uni.getStorageSync('notificationDisplayMode')
					if (savedMode) {
						this.currentDisplayMode = savedMode
					}
				} catch (e) {
					console.error('加载通知显示设置失败:', e)
				}
			},
			
			selectDisplayMode(mode) {
				if (this.currentDisplayMode === mode) {
					return
				}
				
				this.currentDisplayMode = mode
				this.saveDisplayMode(mode)
				
				uni.showToast({
					title: '设置已保存',
					icon: 'success'
				})
			},
			
			saveDisplayMode(mode) {
				try {
					uni.setStorageSync('notificationDisplayMode', mode)
				} catch (e) {
					console.error('保存通知显示设置失败:', e)
				}
			}
		}
	}
</script>

<style>
	.notification-display-page {
		width: 100%;
		min-height: 100vh;
		background-color: #f8f8f8;
		display: flex;
		flex-direction: column;
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
	
	/* 显示选项列表 */
	.display-options {
		background-color: #ffffff;
		margin-bottom: 32rpx;
	}
	
	.option-item {
		padding: 32rpx;
		border-bottom: 1rpx solid #f0f0f0;
		display: flex;
		align-items: center;
		justify-content: space-between;
		min-height: 88rpx;
		cursor: pointer;
		transition: background-color 0.2s ease;
	}
	
	.option-item:last-child {
		border-bottom: none;
	}
	
	.option-item:active {
		background-color: rgba(0, 0, 0, 0.05);
	}
	
	.option-item.active {
		background-color: rgba(0, 122, 255, 0.05);
	}
	
	.option-info {
		flex: 1;
		display: flex;
		flex-direction: column;
		gap: 8rpx;
	}
	
	.option-title {
		font-size: 32rpx;
		color: #333333;
		font-weight: 500;
	}
	
	.option-desc {
		font-size: 28rpx;
		color: #666666;
		line-height: 1.4;
	}
	
	.option-check {
		width: 48rpx;
		height: 48rpx;
		background-color: #007aff;
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.check-icon {
		font-size: 24rpx;
		color: #ffffff;
		font-weight: 600;
	}
	
	/* 说明文字 */
	.display-note {
		padding: 32rpx;
		background-color: #ffffff;
	}
	
	.note-text {
		font-size: 28rpx;
		color: #999999;
		line-height: 1.5;
		text-align: center;
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
