<template>
	<view class="album-permissions-page">
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
			<text class="title">相册权限</text>
			<view class="placeholder"></view>
		</view> -->
		
		<!-- 相册权限设置 -->
		<view class="permission-section">
			<view class="permission-header">
				<text class="permission-title">相册权限</text>
				<view class="go-to-settings" @click="goToSettings">
					<text class="settings-text">去设置</text>
					<view class="arrow-right"></view>
				</view>
			</view>
		</view>
		
		<!-- 权限选项列表 -->
		<view class="permissions-list">
			<!-- 位置信息权限 -->
			<view class="permission-item">
				<view class="permission-content">
					<text class="permission-name">允许使用相册内容的位置信息</text>
					<text class="permission-description">关闭后,将不使用本地相册和已经上传的相册内容中的位置信息,如发布时无法为你推荐相册中照片和视频的拍摄地点等</text>
				</view>
				<view class="toggle-switch" :class="{ active: locationPermission }" @click="toggleLocationPermission">
					<view class="toggle-circle"></view>
				</view>
			</view>
			
			<!-- 快捷分享权限 -->
			<view class="permission-item">
				<view class="permission-content">
					<text class="permission-name">允许快捷分享相册内容到Afriends</text>
					<text class="permission-description">关闭后,你无法将系统相册中的图片和视频快捷分享给Afriends朋友</text>
				</view>
				<view class="toggle-switch" :class="{ active: sharePermission }" @click="toggleSharePermission">
					<view class="toggle-circle"></view>
				</view>
			</view>
			
			<!-- 识别分析权限 -->
			<view class="permission-item">
				<view class="permission-content">
					<text class="permission-name">允许识别并分析相册内容</text>
					<text class="permission-description">在你进入相册后,我们仅会在授权访问的相册范围内识别分析,在设备本地运行,关闭后,将不再对您手机相册中的内容进行识别,如不再提供相册搜索、人像筛选、推荐影集等功能</text>
				</view>
				<view class="toggle-switch" :class="{ active: analysisPermission }" @click="toggleAnalysisPermission">
					<view class="toggle-circle"></view>
				</view>
			</view>
		</view>
		
		<!-- 底部指示器 -->
		<view class="home-indicator"></view>
	</view>
</template>

<script>
	export default {
		name: 'AlbumPermissionsPage',
		data() {
			return {
				locationPermission: true,
				sharePermission: true,
				analysisPermission: true
			}
		},
		methods: {
			goBack() {
				uni.navigateBack();
			},
			goToSettings() {
				uni.showToast({
					title: '跳转到系统设置',
					icon: 'none'
				});
			},
			toggleLocationPermission() {
				this.locationPermission = !this.locationPermission;
			},
			toggleSharePermission() {
				this.sharePermission = !this.sharePermission;
			},
			toggleAnalysisPermission() {
				this.analysisPermission = !this.analysisPermission;
			}
		}
	}
</script>

<style>
	.album-permissions-page {
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
	
	/* 相册权限设置 */
	.permission-section {
		padding: 24rpx 32rpx;
		background-color: #F8F8F8;
		margin: 24rpx 32rpx;
		border-radius: 16rpx;
	}
	
	.permission-header {
		display: flex;
		align-items: center;
		justify-content: space-between;
	}
	
	.permission-title {
		font-size: 28rpx;
		color: #333333;
		font-weight: 500;
	}
	
	.go-to-settings {
		display: flex;
		align-items: center;
		gap: 8rpx;
		cursor: pointer;
	}
	
	.settings-text {
		font-size: 24rpx;
		color: #666666;
	}
	
	.arrow-right {
		width: 0;
		height: 0;
		border-left: 8rpx solid #666666;
		border-top: 6rpx solid transparent;
		border-bottom: 6rpx solid transparent;
	}
	
	/* 权限选项列表 */
	.permissions-list {
		flex: 1;
		padding: 0 32rpx;
	}
	
	.permission-item {
		display: flex;
		align-items: flex-start;
		justify-content: space-between;
		padding: 32rpx 0;
		border-bottom: 1rpx solid #F0F0F0;
	}
	
	.permission-item:last-child {
		border-bottom: none;
	}
	
	.permission-content {
		flex: 1;
		margin-right: 24rpx;
	}
	
	.permission-name {
		display: block;
		font-size: 28rpx;
		color: #333333;
		font-weight: 500;
		margin-bottom: 16rpx;
		line-height: 1.4;
	}
	
	.permission-description {
		display: block;
		font-size: 24rpx;
		color: #999999;
		line-height: 1.5;
	}
	
	/* 开关样式 */
	.toggle-switch {
		width: 80rpx;
		height: 44rpx;
		background-color: #E0E0E0;
		border-radius: 22rpx;
		position: relative;
		transition: all 0.3s ease;
		flex-shrink: 0;
	}
	
	.toggle-switch.active {
		background-color: #34C759;
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
	
	/* 底部指示器 */
	.home-indicator {
		height: 8rpx;
		background-color: #000000;
		border-radius: 4rpx;
		margin: 16rpx auto;
		width: 120rpx;
	}
</style>


