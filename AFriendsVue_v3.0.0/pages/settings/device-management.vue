<template>
	<view class="device-management-page">
		<!-- 头部导航 -->
		<!-- <view class="header">
			<view class="back-button" @click="goBack">
				<view class="back-arrow"></view>
			</view>
			<text class="page-title">登录设备管理</text>
			<view class="manage-button" @click="showManageOptions">
				<text class="manage-text">管理</text>
			</view>
		</view> -->
		
		<!-- 信息提示栏 -->
		<view class="info-bar">
			<text class="info-text">设备删除后再次登录需身份验证。</text>
			<text class="help-link" @click="showHelp">存在疑问?</text>
		</view>
		
		<!-- 设备列表 -->
		<view class="device-list">
			<!-- 设备项目1 - 本机 -->
			<view class="device-item">
				<view class="device-icon">
					<view class="icon-phone"></view>
				</view>
				<view class="device-info">
					<text class="device-name">qwe</text>
					<text class="device-type">iPhone 13</text>
					<text class="device-login">2025-08-18 15:23 | 中国浙江杭州</text>
				</view>
				<view class="device-status">
					<text class="status-text">本机</text>
				</view>
			</view>
			
			<!-- 设备项目2 -->
			<view class="device-item">
				<view class="device-icon">
					<view class="icon-phone"></view>
				</view>
				<view class="device-info">
					<text class="device-name">PEQM00</text>
					<text class="device-type">PEQM00</text>
					<text class="device-login">2025-08-21 08:34 | 中国天津天津</text>
				</view>
				<view class="device-actions">
					<view class="action-btn" @click="removeDevice(1)">
						<text class="action-text">移除</text>
					</view>
				</view>
			</view>
			
			<!-- 设备项目3 -->
			<view class="device-item">
				<view class="device-icon">
					<view class="icon-desktop"></view>
				</view>
				<view class="device-info">
					<text class="device-name">王柳清</text>
					<text class="device-type">Windows</text>
					<text class="device-login">2024-11-19 08:03 | 中国山西太原</text>
				</view>
				<view class="device-actions">
					<view class="action-btn" @click="removeDevice(2)">
						<text class="action-text">移除</text>
					</view>
				</view>
			</view>
			
			<!-- 设备项目4 -->
			<view class="device-item">
				<view class="device-icon">
					<view class="icon-phone"></view>
				</view>
				<view class="device-info">
					<text class="device-name">iPad7,5</text>
					<text class="device-type">iPad</text>
					<text class="device-login">2025-05-09 22:53 | 中国吉林长春</text>
				</view>
				<view class="device-actions">
					<view class="action-btn" @click="removeDevice(3)">
						<text class="action-text">移除</text>
					</view>
				</view>
			</view>
			
			<!-- 设备项目5 -->
			<view class="device-item">
				<view class="device-icon">
					<view class="icon-desktop"></view>
				</view>
				<view class="device-info">
					<text class="device-name">王柳清</text>
					<text class="device-type">Windows</text>
					<text class="device-login">2025-07-13 17:23 | 中国浙江</text>
				</view>
				<view class="device-actions">
					<view class="action-btn" @click="removeDevice(4)">
						<text class="action-text">移除</text>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 底部指示器 -->
		<view class="home-indicator"></view>
	</view>
</template>

<script>
	export default {
		name: 'DeviceManagementPage',
		data() {
			return {
				devices: [
					{
						id: 0,
						name: 'qwe',
						type: 'iPhone 13',
						lastLogin: '2025-08-18 15:23',
						location: '中国浙江杭州',
						isCurrent: true
					},
					{
						id: 1,
						name: 'PEQM00',
						type: 'PEQM00',
						lastLogin: '2025-08-21 08:34',
						location: '中国天津天津',
						isCurrent: false
					},
					{
						id: 2,
						name: '王柳清',
						type: 'Windows',
						lastLogin: '2024-11-19 08:03',
						location: '中国山西太原',
						isCurrent: false
					},
					{
						id: 3,
						name: 'iPad7,5',
						type: 'iPad',
						lastLogin: '2025-05-09 22:53',
						location: '中国吉林长春',
						isCurrent: false
					},
					{
						id: 4,
						name: '王柳清',
						type: 'Windows',
						lastLogin: '2025-07-13 17:23',
						location: '中国浙江',
						isCurrent: false
					}
				]
			}
		},
		methods: {
			goBack() {
				uni.navigateBack();
			},
			showManageOptions() {
				uni.showActionSheet({
					itemList: ['批量移除设备', '导出设备列表', '设备安全设置'],
					success: (res) => {
						switch (res.tapIndex) {
							case 0:
								this.batchRemoveDevices();
								break;
							case 1:
								this.exportDeviceList();
								break;
							case 2:
								this.deviceSecuritySettings();
								break;
						}
					}
				});
			},
			showHelp() {
				uni.showModal({
					title: '帮助说明',
					content: '设备删除后，该设备再次登录时需要重新进行身份验证，包括手机验证码、密码等。这是为了保护您的账户安全。',
					showCancel: false,
					confirmText: '我知道了'
				});
			},
			removeDevice(deviceId) {
				uni.showModal({
					title: '确认移除',
					content: '确定要移除这个设备吗？移除后该设备需要重新登录。',
					success: (res) => {
						if (res.confirm) {
							// 从设备列表中移除
							this.devices = this.devices.filter(device => device.id !== deviceId);
							uni.showToast({
								title: '设备已移除',
								icon: 'success'
							});
						}
					}
				});
			},
			batchRemoveDevices() {
				uni.showModal({
					title: '批量移除',
					content: '确定要移除所有非本机设备吗？',
					success: (res) => {
						if (res.confirm) {
							this.devices = this.devices.filter(device => device.isCurrent);
							uni.showToast({
								title: '批量移除完成',
								icon: 'success'
							});
						}
					}
				});
			},
			exportDeviceList() {
				uni.showToast({
					title: '导出功能开发中',
					icon: 'none'
				});
			},
			deviceSecuritySettings() {
				uni.showToast({
					title: '安全设置功能开发中',
					icon: 'none'
				});
			}
		}
	}
</script>

<style>
	.device-management-page {
		min-height: 100vh;
		background-color: #FFFFFF;
		display: flex;
		flex-direction: column;
	}
	
	/* 头部导航 */
	.header {
		height: 88rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 32rpx;
		background-color: #FFFFFF;
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
	
	.page-title {
		font-size: 32rpx;
		color: #000000;
		font-weight: 600;
	}
	
	.manage-button {
		padding: 8rpx 16rpx;
		border-radius: 16rpx;
		background-color: #F5F5F5;
	}
	
	.manage-text {
		font-size: 26rpx;
		color: #000000;
		font-weight: 500;
	}
	
	/* 信息提示栏 */
	.info-bar {
		padding: 24rpx 32rpx;
		background-color: #F8F8F8;
		display: flex;
		align-items: center;
		gap: 16rpx;
	}
	
	.info-text {
		font-size: 26rpx;
		color: #000000;
		line-height: 1.4;
	}
	
	.help-link {
		font-size: 26rpx;
		color: #007AFF;
		text-decoration: underline;
	}
	
	/* 设备列表 */
	.device-list {
		flex: 1;
		padding: 0 32rpx;
	}
	
	.device-item {
		display: flex;
		align-items: center;
		gap: 24rpx;
		padding: 32rpx 0;
		border-bottom: 1rpx solid #F0F0F0;
	}
	
	.device-item:last-child {
		border-bottom: none;
	}
	
	.device-icon {
		width: 60rpx;
		height: 60rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		flex-shrink: 0;
	}
	
	.icon-phone {
		width: 40rpx;
		height: 40rpx;
		background-color: #000000;
		border-radius: 8rpx;
		position: relative;
	}
	
	.icon-phone::before {
		content: '';
		position: absolute;
		top: 8rpx;
		left: 50%;
		transform: translateX(-50%);
		width: 16rpx;
		height: 2rpx;
		background-color: #FFFFFF;
		border-radius: 1rpx;
	}
	
	.icon-desktop {
		width: 40rpx;
		height: 30rpx;
		background-color: #000000;
		border-radius: 4rpx;
		position: relative;
	}
	
	.icon-desktop::after {
		content: '';
		position: absolute;
		bottom: -8rpx;
		left: 50%;
		transform: translateX(-50%);
		width: 20rpx;
		height: 4rpx;
		background-color: #000000;
		border-radius: 2rpx;
	}
	
	.device-info {
		flex: 1;
		display: flex;
		flex-direction: column;
		gap: 8rpx;
	}
	
	.device-name {
		font-size: 28rpx;
		color: #000000;
		font-weight: 600;
	}
	
	.device-type {
		font-size: 26rpx;
		color: #666666;
	}
	
	.device-login {
		font-size: 24rpx;
		color: #999999;
		line-height: 1.4;
	}
	
	.device-status {
		padding: 8rpx 16rpx;
		background-color: #F0F0F0;
		border-radius: 16rpx;
	}
	
	.status-text {
		font-size: 22rpx;
		color: #666666;
		font-weight: 500;
	}
	
	.device-actions {
		display: flex;
		align-items: center;
	}
	
	.action-btn {
		padding: 8rpx 16rpx;
		background-color: #F44336;
		border-radius: 16rpx;
	}
	
	.action-text {
		font-size: 22rpx;
		color: #FFFFFF;
		font-weight: 500;
	}
	
	/* 底部指示器 */
	.home-indicator {
		height: 8rpx;
		background-color: #000000;
		border-radius: 4rpx;
		margin: 32rpx auto;
		width: 120rpx;
	}
	
	/* 响应式设计 */
	@media (max-width: 750rpx) {
		.device-item {
			padding: 24rpx 0;
		}
		
		.device-icon {
			width: 50rpx;
			height: 50rpx;
		}
		
		.icon-phone, .icon-desktop {
			width: 35rpx;
			height: 35rpx;
		}
		
		.device-name {
			font-size: 26rpx;
		}
		
		.device-type {
			font-size: 24rpx;
		}
		
		.device-login {
			font-size: 22rpx;
		}
	}
</style>


