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
			<view class="back-button">
				<text class="back-arrow">‹</text>
			</view>
			<view class="title">购买记录</view>
			<view class="search-button">
				<text class="search-icon">🔍</text>
			</view>
		</view> -->
		
		<!-- 加载状态 -->
		<view class="loading-container" v-if="loading">
			<text class="loading-text">正在加载购买记录...</text>
		</view>
		
		<!-- 错误状态 -->
		<view class="error-container" v-if="hasError && !loading">
			<view class="error-icon">⚠️</view>
			<text class="error-text">{{ errorMessage }}</text>
			<view class="error-actions">
				<button class="retry-button" @click="loadPurchaseRecords">重新加载</button>
			</view>
		</view>
		
		<!-- 交易记录列表 -->
		<view class="transaction-list" v-if="!hasError && !loading">
			<view class="transaction-item" v-for="record in purchaseRecords" :key="record.id">
				<view class="left-section">
					<view class="avatar">
						<view class="cartoon-creature">
							<view class="eyes">
								<view class="eye eye-left"></view>
								<view class="eye eye-right"></view>
							</view>
							<view class="mouth"></view>
						</view>
					</view>
					<view class="transaction-info">
						<text class="item-name">{{ record.itemName }}</text>
						<text class="transaction-time">{{ record.timeAgo }}</text>
					</view>
				</view>
				<view class="right-section">
					<text class="amount">{{ record.amountFormatted }}</text>
					<text class="balance">{{ record.balanceFormatted }}</text>
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
				userId: 1000100, // 默认用户ID
				purchaseRecords: [], // 购买记录数据
				loading: false,
				errorMessage: '',
				hasError: false
			}
		},
		onLoad(options) {
			// 接收从其他页面传递的userId参数
			if (options.userId) {
				this.userId = parseInt(options.userId);
				console.log('购买记录页面接收到用户ID:', this.userId);
			}
			// 加载购买记录数据
			this.loadPurchaseRecords();
		},
		methods: {
			// 加载购买记录数据
			async loadPurchaseRecords() {
				try {
					console.log('=== 开始加载购买记录数据 ===');
					console.log('用户ID:', this.userId);
					this.loading = true;
					this.hasError = false;
					this.errorMessage = '';
					
					const response = await uni.request({
						url: `http://localhost:8888/api/wallet/purchase-records?userId=${this.userId}`,
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('=== 购买记录API响应 ===');
					console.log('状态码:', response.statusCode);
					console.log('响应数据:', response.data);
					
					if (response.statusCode === 200 && response.data.success) {
						this.purchaseRecords = response.data.data;
						console.log('成功加载购买记录，共', this.purchaseRecords.length, '条');
						
						if (this.purchaseRecords.length === 0) {
							this.hasError = true;
							this.errorMessage = '暂无购买记录';
							console.log('没有获取到任何购买记录');
						} else {
							uni.showToast({
								title: `加载了${this.purchaseRecords.length}条购买记录`,
								icon: 'success',
								duration: 2000
							});
						}
					} else {
						console.error('加载购买记录失败:', response.data.message);
						this.hasError = true;
						this.errorMessage = response.data.message || '加载购买记录失败';
						uni.showToast({
							title: '加载购买记录失败',
							icon: 'error'
						});
					}
				} catch (error) {
					console.error('加载购买记录异常:', error);
					this.hasError = true;
					this.errorMessage = '网络连接失败，请检查网络设置';
					uni.showToast({
						title: '网络错误',
						icon: 'error'
					});
				} finally {
					this.loading = false;
				}
			}
		}
	}
</script>

<style>
	.container {
		position: relative;
		min-height: 100vh;
		background-color: #fff;
	}
	
	/* 状态栏 */
	.status-bar {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 20rpx 40rpx;
		background-color: #fff;
		z-index: 1000;
	}
	
	.time {
		font-size: 32rpx;
		font-weight: 600;
		color: #333;
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
		background-color: #333;
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
	
	.wifi {
		font-size: 24rpx;
		color: #333;
	}
	
	.battery {
		display: flex;
		align-items: center;
		gap: 4rpx;
	}
	
	.battery-body {
		width: 32rpx;
		height: 16rpx;
		border: 2rpx solid #333;
		border-radius: 2rpx;
		position: relative;
	}
	
	.battery-level {
		position: absolute;
		top: 2rpx;
		left: 2rpx;
		right: 2rpx;
		bottom: 2rpx;
		background-color: #333;
		border-radius: 1rpx;
	}
	
	.battery-tip {
		width: 4rpx;
		height: 8rpx;
		background-color: #333;
		border-radius: 0 2rpx 2rpx 0;
	}
	
	/* 导航栏 */
	.nav-bar {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 20rpx 40rpx;
		border-bottom: 1rpx solid #f0f0f0;
	}
	
	.back-button {
		width: 60rpx;
		height: 60rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.back-arrow {
		font-size: 48rpx;
		color: #333;
		font-weight: bold;
	}
	
	.title {
		font-size: 40rpx;
		font-weight: bold;
		color: #333;
	}
	
	.search-button {
		width: 60rpx;
		height: 60rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.search-icon {
		font-size: 32rpx;
	}
	
	/* 交易记录列表 */
	.transaction-list {
		padding: 0 40rpx;
	}
	
	.transaction-item {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 40rpx 0;
		border-bottom: 1rpx solid #f0f0f0;
	}
	
	.left-section {
		display: flex;
		align-items: center;
		gap: 30rpx;
	}
	
	.avatar {
		width: 80rpx;
		height: 80rpx;
		border-radius: 50%;
		background: linear-gradient(135deg, #FFB6C1, #FFC0CB);
		display: flex;
		align-items: center;
		justify-content: center;
		position: relative;
	}
	
	.cartoon-creature {
		width: 60rpx;
		height: 60rpx;
		background: linear-gradient(135deg, #FFE4E1, #FFC0CB);
		border-radius: 50%;
		position: relative;
	}
	
	.eyes {
		position: absolute;
		top: 15rpx;
		left: 50%;
		transform: translateX(-50%);
		display: flex;
		gap: 15rpx;
	}
	
	.eye {
		width: 8rpx;
		height: 8rpx;
		background-color: #000;
		border-radius: 50%;
	}
	
	.mouth {
		position: absolute;
		bottom: 15rpx;
		left: 50%;
		transform: translateX(-50%);
		width: 20rpx;
		height: 8rpx;
		border: 2rpx solid #000;
		border-top: none;
		border-radius: 0 0 20rpx 20rpx;
	}
	
	.transaction-info {
		display: flex;
		flex-direction: column;
		gap: 10rpx;
	}
	
	.item-name {
		font-size: 32rpx;
		color: #333;
		font-weight: 500;
	}
	
	.transaction-time {
		font-size: 24rpx;
		color: #999;
	}
	
	.right-section {
		display: flex;
		flex-direction: column;
		align-items: flex-end;
		gap: 10rpx;
	}
	
	.amount {
		font-size: 32rpx;
		color: #333;
		font-weight: 500;
	}
	
	.balance {
		font-size: 24rpx;
		color: #999;
	}
	
	/* 加载状态样式 */
	.loading-container {
		display: flex;
		justify-content: center;
		align-items: center;
		padding: 40rpx;
		background-color: #fff;
	}
	
	.loading-text {
		font-size: 28rpx;
		color: #666666;
	}
	
	/* 错误状态样式 */
	.error-container {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 80rpx 40rpx;
		background-color: #fff;
		min-height: 400rpx;
	}
	
	.error-icon {
		font-size: 80rpx;
		margin-bottom: 20rpx;
	}
	
	.error-text {
		font-size: 28rpx;
		color: #FF6B35;
		text-align: center;
		margin-bottom: 40rpx;
		line-height: 1.5;
	}
	
	.error-actions {
		display: flex;
		gap: 20rpx;
	}
	
	.retry-button {
		background-color: #FF69B4;
		color: #FFFFFF;
		border: none;
		border-radius: 24rpx;
		padding: 16rpx 32rpx;
		font-size: 26rpx;
		font-weight: 600;
	}
	
	.retry-button:active {
		background-color: #FF1493;
		transform: scale(0.95);
	}
	
	/* 底部手势条 */
	.home-indicator {
		position: fixed;
		bottom: 20rpx;
		left: 50%;
		transform: translateX(-50%);
		width: 120rpx;
		height: 6rpx;
		background-color: #000;
		border-radius: 3rpx;
	}
</style>
