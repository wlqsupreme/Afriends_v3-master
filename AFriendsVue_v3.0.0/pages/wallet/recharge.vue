<!-- 40号页面 -->
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
			<text class="nav-title">充值</text>
		</view> -->
		
		<!-- 账户余额 -->
		<view class="balance-section">
			<view class="balance-item">
				<view class="balance-left">
					<view class="balance-icon">$</view>
					<text class="balance-text">余额</text>
				</view>
				<text class="balance-amount">{{ formatBalance(balance) }}</text>
			</view>
		</view>
		
		<!-- 充值选项 -->
		<view class="recharge-section">
			<view class="recharge-item">
				<view class="recharge-left">
					<view class="recharge-icon">💳</view>
					<text class="recharge-text">充值</text>
				</view>
			</view>
		</view>
		
		<!-- 充值输入区域 -->
		<view class="input-section">
			<!-- 汇率 -->
			<view class="exchange-rate">100C=1元</view>
			
			<!-- 自定义充值金额 -->
			<view class="custom-amount">
				<text class="custom-label">自定义充值金额:</text>
				<view class="amount-input-container">
					<input 
						class="amount-input" 
						v-model="rechargeAmount" 
						type="number" 
						placeholder="10086"
						@input="calculateCCoins"
					/>
				</view>
				<view class="ccoin-equivalent">= {{ccoinAmount}}C</view>
			</view>
			
			<!-- 充值按钮 -->
			<view class="recharge-button" @click="doRecharge">
				<text class="recharge-button-text">立即充值{{rechargeAmount}}元</text>
			</view>
			
			<!-- 协议文本 -->
			<view class="agreement-text">
				●充值则表示您同意
				<text class="agreement-link" @click="goToServiceAgreement">《AFriends充值服务协议》</text>
				<text class="agreement-link" @click="goToMinorRefund">《未成年人退款申请》</text>
			</view>
		</view>
		
		<!-- 充值记录 -->
		<view class="record-section">
			<view class="record-header">
				<view class="record-left">
					<view class="record-icon">📄</view>
					<text class="record-text">充值记录</text>
				</view>
			</view>
			
			<view class="record-item" v-for="record in rechargeRecords" :key="record.id">
				<view class="record-amount">{{record.amountFormatted}}</view>
				<view class="record-time">{{record.timeAgo}}</view>
				<view class="order-number">订单号: {{record.orderNumber}}</view>
			</view>
		</view>
		
		<!-- 底部服务条款 -->
		<view class="bottom-section">
			<text class="service-terms" @click="goToServiceTerms">服务条款</text>
		</view>
		
		<!-- 底部手势条 -->
		<!-- <view class="home-indicator"></view> -->
	</view>
</template>

<script>
	export default {
		data() {
			return {
				userId: 1000100, // 默认用户ID
				rechargeAmount: '10086',
				ccoinAmount: '1008600',
				balance: 10000000, // 当前余额
				loading: false,
				errorMessage: '',
				hasError: false,
				// 充值记录数据
				rechargeRecords: [],
				// 支付方式列表
				paymentMethods: [
					{ id: 'wechat', name: '微信支付', icon: '💚' },
					{ id: 'alipay', name: '支付宝', icon: '💙' },
					{ id: 'unionpay', name: '银联支付', icon: '💛' }
				]
			}
		},
		onLoad(options) {
			// 接收从其他页面传递的userId参数
			if (options.userId) {
				this.userId = parseInt(options.userId);
				console.log('充值页面接收到用户ID:', this.userId);
			}
			// 加载钱包数据和充值记录
			this.loadWalletData();
			this.loadRechargeRecords();
		},
		methods: {
			// 加载钱包数据
			async loadWalletData() {
				try {
					console.log('=== 开始加载钱包数据 ===');
					console.log('用户ID:', this.userId);
					
					const response = await uni.request({
						url: `http://localhost:8888/api/wallet/balance?userId=${this.userId}`,
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('=== 钱包余额API响应 ===');
					console.log('状态码:', response.statusCode);
					console.log('响应数据:', response.data);
					
					if (response.statusCode === 200 && response.data.success) {
						const balanceData = response.data.data;
						this.balance = balanceData.currentBalance;
						console.log('成功加载钱包余额:', this.balance);
					} else {
						console.error('加载钱包余额失败:', response.data.message);
					}
				} catch (error) {
					console.error('加载钱包数据异常:', error);
				}
			},
			
			// 加载充值记录
			async loadRechargeRecords() {
				try {
					console.log('=== 开始加载充值记录 ===');
					console.log('用户ID:', this.userId);
					
					const response = await uni.request({
						url: `http://localhost:8888/api/wallet/recharge-records?userId=${this.userId}`,
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('=== 充值记录API响应 ===');
					console.log('状态码:', response.statusCode);
					console.log('响应数据:', response.data);
					
					if (response.statusCode === 200 && response.data.success) {
						this.rechargeRecords = response.data.data;
						console.log('成功加载充值记录，共', this.rechargeRecords.length, '条');
					} else {
						console.error('加载充值记录失败:', response.data.message);
					}
				} catch (error) {
					console.error('加载充值记录异常:', error);
				}
			},
			
			goBack() {
				uni.navigateBack();
			},
			calculateCCoins() {
				if (this.rechargeAmount && !isNaN(this.rechargeAmount)) {
					this.ccoinAmount = (parseInt(this.rechargeAmount) * 100).toString();
				} else {
					this.ccoinAmount = '0';
				}
			},
			// 充值方法
			doRecharge() {
				// 验证充值金额
				if (!this.rechargeAmount || isNaN(this.rechargeAmount) || parseInt(this.rechargeAmount) <= 0) {
					uni.showToast({
						title: '请输入有效的充值金额',
						icon: 'none'
					});
					return;
				}
				
				// 显示支付方式选择弹窗
				this.showPaymentMethodDialog();
			},
			
			// 显示支付方式选择弹窗
			showPaymentMethodDialog() {
				uni.showActionSheet({
					itemList: this.paymentMethods.map(item => `${item.icon} ${item.name}`),
					success: (res) => {
						const selectedMethod = this.paymentMethods[res.tapIndex];
						this.processPayment(selectedMethod);
					},
					fail: () => {
						console.log('用户取消选择支付方式');
					}
				});
			},
			
			// 处理支付
			async processPayment(paymentMethod) {
				try {
					// 显示加载提示
					uni.showLoading({
						title: '正在跳转支付...'
					});
					
					// 模拟跳转支付app的延迟
					await new Promise(resolve => setTimeout(resolve, 1500));
					
					// 隐藏加载提示
					uni.hideLoading();
					
					// 模拟支付成功（实际项目中这里应该是支付回调）
					this.handlePaymentSuccess(paymentMethod);
					
				} catch (error) {
					uni.hideLoading();
					uni.showToast({
						title: '支付跳转失败',
						icon: 'none'
					});
				}
			},
			
			// 处理支付成功
			async handlePaymentSuccess(paymentMethod) {
				try {
					// 显示充值成功弹窗
					uni.showModal({
						title: '充值成功',
						content: `已成功充值${this.rechargeAmount}元，获得${this.ccoinAmount}C`,
						showCancel: false,
						confirmText: '确定',
						success: async () => {
							// 提交充值信息到后端
							await this.submitRechargeInfo(paymentMethod);
							
							// 更新充值记录
							this.updateRechargeRecord();
							
							// 重置充值金额
							this.rechargeAmount = '';
							this.ccoinAmount = '0';
						}
					});
					
				} catch (error) {
					console.error('处理支付成功失败:', error);
				}
			},
			
			// 提交充值信息到后端
			async submitRechargeInfo(paymentMethod) {
				try {
					console.log('=== 开始提交充值信息到后端 ===');
					console.log('用户ID:', this.userId);
					console.log('充值金额:', this.rechargeAmount);
					console.log('支付方式:', paymentMethod);
					
					const response = await uni.request({
						url: 'http://localhost:8888/api/wallet/recharge',
						method: 'POST',
						data: {
							userId: this.userId,
							amount: parseFloat(this.rechargeAmount),
							paymentMethod: paymentMethod.id
						},
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('=== 充值API响应 ===');
					console.log('状态码:', response.statusCode);
					console.log('响应数据:', response.data);
					
					if (response.statusCode === 200 && response.data.success) {
						console.log('充值信息提交成功:', response.data);
						
						// 更新本地余额
						this.balance = response.data.newBalance;
						
						// 重新加载充值记录
						await this.loadRechargeRecords();
						
						uni.showToast({
							title: '充值成功',
							icon: 'success',
							duration: 2000
						});
					} else {
						throw new Error(response.data.message || '充值失败');
					}
					
				} catch (error) {
					console.error('提交充值信息失败:', error);
					uni.showToast({
						title: '充值信息提交失败: ' + error.message,
						icon: 'none'
					});
				}
			},
			
			// 更新充值记录
			updateRechargeRecord() {
				// 生成新的充值记录
				const newRecord = {
					id: this.rechargeRecords.length + 1,
					amount: `+${this.ccoinAmount}C`,
					time: this.formatDateTime(new Date()),
					orderNumber: this.generateOrderNumber()
				};
				
				// 添加到记录列表开头
				this.rechargeRecords.unshift(newRecord);
				
				// 实际项目中，这里应该调用后端API更新记录
				console.log('充值记录已更新:', newRecord);
			},
			
			// 生成订单号
			generateOrderNumber() {
				const timestamp = Date.now();
				const random = Math.random().toString(36).substr(2, 9);
				return `ORDER${timestamp}${random}`.toUpperCase();
			},
			
			// 格式化日期时间
			formatDateTime(date) {
				const year = date.getFullYear();
				const month = String(date.getMonth() + 1).padStart(2, '0');
				const day = String(date.getDate()).padStart(2, '0');
				const hours = String(date.getHours()).padStart(2, '0');
				const minutes = String(date.getMinutes()).padStart(2, '0');
				
				return `${year}/${month}/${day} ${hours}:${minutes}`;
			},
			
			goToServiceAgreement() {
				uni.navigateTo({
					url: '/pages/settings/terms-of-service'
				});
			},
			goToMinorRefund() {
				uni.navigateTo({
					url: '/pages/settings/terms-of-service'
				});
			},
			goToServiceTerms() {
				uni.navigateTo({
					url: '/pages/settings/terms-of-service'
				});
			},
			// 格式化余额显示
			formatBalance(balance) {
				if (balance >= 10000) {
					return (balance / 10000).toFixed(1) + 'wC';
				} else if (balance >= 1000) {
					return (balance / 1000).toFixed(1) + 'kC';
				} else {
					return balance + 'C';
				}
			}
		},
		mounted() {
			this.calculateCCoins();
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
	
	/* 账户余额 */
	.balance-section {
		background-color: #fff;
		border-bottom: 1rpx solid #f0f0f0;
	}
	
	.balance-item {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 30rpx 40rpx;
	}
	
	.balance-left {
		display: flex;
		align-items: center;
		gap: 20rpx;
	}
	
	.balance-icon {
		width: 60rpx;
		height: 60rpx;
		border-radius: 50%;
		background-color: #f0f0f0;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 24rpx;
		color: #666;
	}
	
	.balance-text {
		font-size: 30rpx;
		color: #333;
	}
	
	.balance-amount {
		font-size: 32rpx;
		color: #333;
		font-weight: bold;
	}
	
	/* 充值选项 */
	.recharge-section {
		background-color: #fff;
		border-bottom: 1rpx solid #f0f0f0;
	}
	
	.recharge-item {
		display: flex;
		align-items: center;
		padding: 30rpx 40rpx;
	}
	
	.recharge-left {
		display: flex;
		align-items: center;
		gap: 20rpx;
	}
	
	.recharge-icon {
		width: 60rpx;
		height: 60rpx;
		border-radius: 50%;
		background-color: #f0f0f0;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 24rpx;
		color: #666;
	}
	
	.recharge-text {
		font-size: 30rpx;
		color: #333;
	}
	
	/* 充值输入区域 */
	.input-section {
		background-color: #fff;
		padding: 40rpx;
		border-bottom: 1rpx solid #f0f0f0;
	}
	
	.exchange-rate {
		font-size: 28rpx;
		color: #666;
		margin-bottom: 30rpx;
	}
	
	.custom-amount {
		margin-bottom: 40rpx;
	}
	
	.custom-label {
		font-size: 28rpx;
		color: #333;
		margin-bottom: 20rpx;
		display: block;
	}
	
	.amount-input-container {
		margin-bottom: 20rpx;
	}
	
	.amount-input {
		width: 100%;
		height: 80rpx;
		border: 2rpx solid #e0e0e0;
		border-radius: 10rpx;
		padding: 0 20rpx;
		font-size: 32rpx;
		color: #333;
		background-color: #fff;
	}
	
	.ccoin-equivalent {
		font-size: 28rpx;
		color: #666;
	}
	
	/* 充值按钮 */
	.recharge-button {
		background: linear-gradient(135deg, #FF6B6B, #FF8E8E);
		border-radius: 50rpx;
		padding: 30rpx 0;
		margin-bottom: 30rpx;
		box-shadow: 0 10rpx 25rpx rgba(255, 107, 107, 0.3);
	}
	
	.recharge-button-text {
		color: #fff;
		font-size: 32rpx;
		font-weight: bold;
		text-align: center;
		display: block;
	}
	
	/* 协议文本 */
	.agreement-text {
		font-size: 24rpx;
		color: #999;
		line-height: 1.5;
	}
	
	.agreement-link {
		color: #007AFF;
		text-decoration: underline;
	}
	
	/* 充值记录 */
	.record-section {
		background-color: #fff;
		border-bottom: 1rpx solid #f0f0f0;
	}
	
	.record-header {
		display: flex;
		align-items: center;
		padding: 30rpx 40rpx;
		border-bottom: 1rpx solid #f0f0f0;
	}
	
	.record-left {
		display: flex;
		align-items: center;
		gap: 20rpx;
	}
	
	.record-icon {
		width: 60rpx;
		height: 60rpx;
		border-radius: 50%;
		background-color: #f0f0f0;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 24rpx;
		color: #666;
	}
	
	.record-text {
		font-size: 30rpx;
		color: #333;
	}
	
	.record-item {
		padding: 30rpx 40rpx;
		border-bottom: 1rpx solid #f5f5f5;
	}
	
	.record-item:last-child {
		border-bottom: none;
	}
	
	.record-amount {
		font-size: 32rpx;
		color: #28a745;
		font-weight: bold;
		margin-bottom: 10rpx;
	}
	
	.record-time {
		font-size: 26rpx;
		color: #666;
		margin-bottom: 10rpx;
	}
	
	.order-number {
		font-size: 24rpx;
		color: #999;
	}
	
	/* 底部服务条款 */
	.bottom-section {
		padding: 40rpx;
		display: flex;
		justify-content: center;
		background-color: #fff;
	}
	
	.service-terms {
		color: #007AFF;
		font-size: 28rpx;
		cursor: pointer;
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




