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
		<view class="nav-bar">
			<!-- <view class="back-button" @click="goBack">
				<text class="back-arrow">‹</text>
			</view>
			<text class="nav-title">模式选择</text> -->
		</view>
		
		<!-- 模式选择内容 -->
		<view class="content-area">
			<!-- 分组标题 -->
			<view class="section-header">模式切换</view>
			
			<!-- 模式选项列表 -->
			<view class="mode-list">
				<view class="mode-item" @click="selectMode('normal')">
					<text class="mode-text">普通模式</text>
				</view>
				<view class="mode-item" @click="selectMode('concise')">
					<text class="mode-text">简洁模式</text>
				</view>
				<view class="mode-item" @click="selectMode('minor')">
					<text class="mode-text">未成年人模式</text>
				</view>
				<view class="mode-item" @click="selectMode('care')">
					<text class="mode-text">关怀模式</text>
				</view>
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
				selectedMode: 'normal'
			}
		},
		methods: {
			goBack() {
				uni.navigateBack();
			},
			selectMode(mode) {
				this.selectedMode = mode;
				uni.showToast({
					title: `已选择${this.getModeName(mode)}`,
					icon: 'success'
				});
				
				// 延迟返回上一页
				setTimeout(() => {
					uni.navigateBack();
				}, 1500);
			},
			getModeName(mode) {
				const modeNames = {
					'normal': '普通模式',
					'concise': '简洁模式',
					'minor': '未成年人模式',
					'care': '关怀模式'
				};
				return modeNames[mode] || '未知模式';
			}
		}
	}
</script>

<style>
	.container {
		height: 100vh;
		background-color: #fff;
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
		padding: 1rpx 40rpx;
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
		color: #000;
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
	
	/* 内容区域 */
	.content-area {
		flex: 1;
		background-color: #fff;
	}
	
	/* 分组标题 */
	.section-header {
		background-color: #f8f8f8;
		padding: 20rpx 40rpx;
		font-size: 28rpx;
		color: #666;
		font-weight: 500;
		text-align: left;
	}
	
	/* 模式选项列表 */
	.mode-list {
		background-color: #fff;
	}
	
	.mode-item {
		display: flex;
		align-items: center;
		padding: 30rpx 40rpx;
		border-bottom: 1rpx solid #f0f0f0;
		background-color: #fff;
		cursor: pointer;
	}
	
	.mode-item:last-child {
		border-bottom: none;
	}
	
	.mode-text {
		font-size: 30rpx;
		color: #333;
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
