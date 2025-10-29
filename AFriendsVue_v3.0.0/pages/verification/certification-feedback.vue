<!--
 * @description 认证反馈页面
 * @author AI Assistant
 * @created 2024-12-25
 * @version 1.0.0
 * 
 * 功能说明：
 * - 显示认证提交成功的反馈信息
 * - 适用于职业认证和学校认证完成后的反馈
 * - 提供返回上一页的功能
 * 
 * 页面设计：
 * - 白底黑字主题
 * - 现代化的卡片设计
 * - 友好的用户反馈体验
 * - 渐变背景和阴影效果
-->

<template>
	<view class="certification-feedback-page">
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
			<text class="title">确认认证</text>
			<view class="placeholder"></view>
		</view> -->
		
		<!-- 主要内容区域 -->
		<view class="main-content">
			<!-- 成功图标 - 只在有真实记录时显示 -->
			<view class="success-icon-container" v-if="certRecords.length > 0">
				<view class="success-icon">
					<view class="checkmark"></view>
				</view>
			</view>
			
			<!-- 成功消息 -->
			<view class="success-message">
				<text class="message-text" v-if="certRecords.length > 0">
					以下是您的认证记录
				</text>
				<text class="message-text" v-else-if="!loading">
					暂无认证记录
				</text>
			</view>
			
			
			<!-- 认证记录列表 -->
			<view class="cert-records-section" v-if="certRecords.length > 0">
				<view class="records-header">
					<view class="records-icon">
						<view class="list-icon"></view>
					</view>
					<text class="records-title">我的认证记录</text>
				</view>
				<view class="records-list">
					<view 
						v-for="record in certRecords" 
						:key="record.recordId" 
						class="record-item"
					>
						<view class="record-header">
							<text class="record-type">{{ getCertTypeText(record.certType) }}</text>
							<view 
								class="status-badge" 
								:style="{ backgroundColor: getStatusColor(record.status) }"
							>
								<text class="status-text">{{ getStatusText(record.status) }}</text>
							</view>
						</view>
						<view class="record-info">
							<text class="record-desc">{{ record.certInfo || '暂无详细信息' }}</text>
						</view>
						<view class="record-time">
							<text class="time-text">提交时间：{{ formatDateTime(record.createdAt) }}</text>
						</view>
					</view>
				</view>
			</view>
			
			<!-- 无认证记录提示 - 只在加载完成且没有记录时显示 -->
			<view class="no-records-section" v-if="!loading && certRecords.length === 0">
				<view class="no-records-icon">
					<view class="empty-icon"></view>
				</view>
				<text class="no-records-text">暂无认证记录</text>
			</view>
			
			<!-- 加载状态 -->
			<view class="loading-section" v-if="loading">
				<view class="loading-spinner"></view>
				<text class="loading-text">正在加载认证记录...</text>
			</view>
			
			<!-- 温馨提示 -->
			<view class="tips-section">
				<view class="tips-header">
					<view class="tips-icon">
						<view class="lightbulb"></view>
					</view>
					<text class="tips-title">温馨提示</text>
				</view>
				<view class="tips-content">
					<text class="tip-item">• 认证期间请保持手机畅通，我们会及时通知您审核结果</text>
					<text class="tip-item">• 如有疑问，请联系客服获取帮助</text>
					<text class="tip-item">• 认证通过后，您的账号将获得相应标识</text>
				</view>
			</view>
		</view>
		
		<!-- 返回按钮 -->
		<view class="return-button-section">
			<view class="return-button" @click="goBack">
				<text class="return-text">点击返回</text>
			</view>
		</view>
		
		<!-- 底部指示器 -->
		<view class="home-indicator"></view>
	</view>
</template>

<script>
	export default {
		name: 'CertificationFeedbackPage',
		data() {
			return {
				certificationType: '职业认证', // 或 '学校认证'
				submitTime: this.formatDateTime(new Date()),
				expectedTime: this.formatExpectedTime(new Date()),
				certRecords: [], // 认证记录列表
				loading: false, // 加载状态
				userId: null // 用户ID
			}
		},
		methods: {
			// 加载认证记录数据
			async loadCertRecords() {
				try {
					this.loading = true;
					
					// 获取用户ID
					const userId = uni.getStorageSync('userId');
					if (!userId) {
						uni.showToast({
							title: '请先登录',
							icon: 'none'
						});
						return;
					}
					
					this.userId = userId;
					
					// 获取认证记录
					const response = await this.getUserCertRecords(userId);
					if (response && response.data) {
						this.certRecords = response.data;
					}
					
				} catch (error) {
					console.error('加载认证记录失败:', error);
					uni.showToast({
						title: '加载认证记录失败',
						icon: 'none'
					});
				} finally {
					this.loading = false;
				}
			},
			
			// 获取用户认证记录
			getUserCertRecords(userId) {
				return new Promise((resolve, reject) => {
					uni.request({
						url: 'http://localhost:8888/api/u-entities/user-cert-record/user/' + userId,
						method: 'GET',
						timeout: 10000, // 10秒超时
						success: (res) => {
							resolve(res);
						},
						fail: (err) => {
							reject(err);
						}
					});
				});
			},
			
			// 获取认证状态文本
			getStatusText(status) {
				switch (status) {
					case 0:
						return '待审核';
					case 1:
						return '已通过';
					case 2:
						return '未通过';
					default:
						return '未知状态';
				}
			},
			
			// 获取认证类型文本
			getCertTypeText(certType) {
				switch (certType) {
					case 'REALNAME':
						return '实名认证';
					case 'STUDENT':
						return '学校认证';
					case 'JOB':
						return '职业认证';
					default:
						return '未知类型';
				}
			},
			
			// 获取状态颜色
			getStatusColor(status) {
				switch (status) {
					case 0:
						return '#FF9500'; // 待审核 - 橙色
					case 1:
						return '#34C759'; // 已通过 - 绿色
					case 2:
						return '#FF3B30'; // 未通过 - 红色
					default:
						return '#8E8E93'; // 未知 - 灰色
				}
			},
			
			goBack() {
				uni.redirectTo({
					url: '/pages/verification/official-certification'
				});
			},
			formatDateTime(date) {
				const year = date.getFullYear();
				const month = (date.getMonth() + 1).toString().padStart(2, '0');
				const day = date.getDate().toString().padStart(2, '0');
				const hours = date.getHours().toString().padStart(2, '0');
				const minutes = date.getMinutes().toString().padStart(2, '0');
				return `${year}-${month}-${day} ${hours}:${minutes}`;
			},
			formatExpectedTime(date) {
				// 3个工作日后的日期
				const expectedDate = new Date(date);
				let workDays = 0;
				while (workDays < 3) {
					expectedDate.setDate(expectedDate.getDate() + 1);
					// 跳过周末
					if (expectedDate.getDay() !== 0 && expectedDate.getDay() !== 6) {
						workDays++;
					}
				}
				const year = expectedDate.getFullYear();
				const month = (expectedDate.getMonth() + 1).toString().padStart(2, '0');
				const day = expectedDate.getDate().toString().padStart(2, '0');
				return `${year}-${month}-${day}`;
			}
		},
		onLoad(options) {
			// 根据传入参数设置认证类型
			if (options.type) {
				if (options.type === 'school') {
					this.certificationType = '学校认证';
				} else if (options.type === 'realName') {
					this.certificationType = '实名认证';
				} else {
					this.certificationType = '职业认证';
				}
			}
			
			// 加载认证记录
			this.loadCertRecords();
		}
	}
</script>

<style>
	.certification-feedback-page {
		min-height: 100vh;
		background: linear-gradient(135deg, #F8FAFF 0%, #FFFFFF 100%);
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
		background: transparent;
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
		background: rgba(255, 255, 255, 0.95);
		backdrop-filter: blur(20rpx);
		border-bottom: 1rpx solid rgba(240, 240, 240, 0.8);
	}
	
	.back-button {
		width: 48rpx;
		height: 48rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		border-radius: 24rpx;
		background: rgba(0, 0, 0, 0.05);
		transition: all 0.2s ease;
	}
	
	.back-button:active {
		background: rgba(0, 0, 0, 0.1);
		transform: scale(0.95);
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
	
	/* 主要内容区域 */
	.main-content {
		flex: 1;
		padding: 48rpx 32rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
	}
	
	/* 成功图标 */
	.success-icon-container {
		margin-bottom: 48rpx;
	}
	
	.success-icon {
		width: 120rpx;
		height: 120rpx;
		background: linear-gradient(135deg, #34C759, #30D158);
		border-radius: 60rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: 0 8rpx 32rpx rgba(52, 199, 89, 0.3);
		position: relative;
	}
	
	.success-icon::before {
		content: '';
		position: absolute;
		width: 140rpx;
		height: 140rpx;
		border: 2rpx solid rgba(52, 199, 89, 0.2);
		border-radius: 70rpx;
		animation: pulse 2s infinite;
	}
	
	@keyframes pulse {
		0% {
			transform: scale(1);
			opacity: 1;
		}
		100% {
			transform: scale(1.2);
			opacity: 0;
		}
	}
	
	.checkmark {
		width: 48rpx;
		height: 48rpx;
		position: relative;
	}
	
	.checkmark::before {
		content: '';
		position: absolute;
		width: 24rpx;
		height: 48rpx;
		border-right: 6rpx solid #FFFFFF;
		border-bottom: 6rpx solid #FFFFFF;
		transform: rotate(45deg);
		top: -4rpx;
		left: 8rpx;
	}
	
	/* 成功消息 */
	.success-message {
		margin-bottom: 48rpx;
		text-align: center;
		max-width: 600rpx;
	}
	
	.message-text {
		font-size: 32rpx;
		color: #333333;
		font-weight: 500;
		line-height: 1.5;
		text-align: center;
	}
	
	/* 认证信息卡片 */
	.certification-card {
		width: 100%;
		background: #FFFFFF;
		border-radius: 24rpx;
		padding: 32rpx;
		margin-bottom: 32rpx;
		box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.08);
		border: 1rpx solid rgba(240, 240, 240, 0.8);
	}
	
	.card-header {
		display: flex;
		align-items: center;
		gap: 16rpx;
		margin-bottom: 24rpx;
		padding-bottom: 16rpx;
		border-bottom: 1rpx solid #F0F0F0;
	}
	
	.card-icon {
		width: 48rpx;
		height: 48rpx;
		background: linear-gradient(135deg, #007AFF, #00C6FF);
		border-radius: 12rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.cert-icon {
		width: 24rpx;
		height: 24rpx;
		background: #FFFFFF;
		border-radius: 50%;
		position: relative;
	}
	
	.cert-icon::before {
		content: '';
		position: absolute;
		top: 50%;
		left: 50%;
		width: 12rpx;
		height: 12rpx;
		background: #007AFF;
		border-radius: 50%;
		transform: translate(-50%, -50%);
	}
	
	.card-title {
		font-size: 28rpx;
		color: #333333;
		font-weight: 600;
	}
	
	.card-content {
		display: flex;
		flex-direction: column;
		gap: 20rpx;
	}
	
	.info-row {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 16rpx 0;
	}
	
	.info-label {
		font-size: 26rpx;
		color: #666666;
		font-weight: 400;
	}
	
	.info-value {
		font-size: 26rpx;
		color: #333333;
		font-weight: 500;
	}
	
	/* 温馨提示 */
	.tips-section {
		width: 100%;
		background: rgba(255, 255, 255, 0.8);
		border-radius: 20rpx;
		padding: 24rpx;
		border: 1rpx solid rgba(240, 240, 240, 0.6);
	}
	
	.tips-header {
		display: flex;
		align-items: center;
		gap: 12rpx;
		margin-bottom: 16rpx;
	}
	
	.tips-icon {
		width: 32rpx;
		height: 32rpx;
		background: linear-gradient(135deg, #FF9500, #FFB340);
		border-radius: 8rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.lightbulb {
		width: 16rpx;
		height: 16rpx;
		background: #FFFFFF;
		border-radius: 50%;
		position: relative;
	}
	
	.lightbulb::before {
		content: '';
		position: absolute;
		top: -4rpx;
		left: 50%;
		width: 8rpx;
		height: 8rpx;
		background: #FFFFFF;
		border-radius: 50%;
		transform: translateX(-50%);
	}
	
	.tips-title {
		font-size: 26rpx;
		color: #FF9500;
		font-weight: 600;
	}
	
	.tips-content {
		display: flex;
		flex-direction: column;
		gap: 12rpx;
	}
	
	.tip-item {
		font-size: 24rpx;
		color: #666666;
		line-height: 1.4;
	}
	
	/* 返回按钮 */
	.return-button-section {
		padding: 32rpx;
		display: flex;
		justify-content: center;
	}
	
	.return-button {
		width: 100%;
		height: 88rpx;
		background: linear-gradient(135deg, #007AFF, #00C6FF);
		border-radius: 44rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: 0 8rpx 24rpx rgba(0, 122, 255, 0.3);
		transition: all 0.2s ease;
		cursor: pointer;
	}
	
	.return-button:active {
		transform: translateY(2rpx);
		box-shadow: 0 4rpx 16rpx rgba(0, 122, 255, 0.4);
	}
	
	.return-text {
		font-size: 28rpx;
		color: #FFFFFF;
		font-weight: 600;
	}
	
	/* 认证记录列表 */
	.cert-records-section {
		width: 100%;
		margin-bottom: 32rpx;
	}
	
	.records-header {
		display: flex;
		align-items: center;
		gap: 12rpx;
		margin-bottom: 20rpx;
	}
	
	.records-icon {
		width: 32rpx;
		height: 32rpx;
		background: linear-gradient(135deg, #007AFF, #00C6FF);
		border-radius: 8rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.list-icon {
		width: 16rpx;
		height: 16rpx;
		background: #FFFFFF;
		border-radius: 2rpx;
		position: relative;
	}
	
	.list-icon::before {
		content: '';
		position: absolute;
		top: 4rpx;
		left: 0;
		width: 16rpx;
		height: 2rpx;
		background: #FFFFFF;
		border-radius: 1rpx;
	}
	
	.list-icon::after {
		content: '';
		position: absolute;
		top: 8rpx;
		left: 0;
		width: 12rpx;
		height: 2rpx;
		background: #FFFFFF;
		border-radius: 1rpx;
	}
	
	.records-title {
		font-size: 28rpx;
		color: #333333;
		font-weight: 600;
	}
	
	.records-list {
		display: flex;
		flex-direction: column;
		gap: 16rpx;
	}
	
	.record-item {
		background: #FFFFFF;
		border-radius: 16rpx;
		padding: 24rpx;
		box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.06);
		border: 1rpx solid rgba(240, 240, 240, 0.8);
	}
	
	.record-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 12rpx;
	}
	
	.record-type {
		font-size: 26rpx;
		color: #333333;
		font-weight: 600;
	}
	
	.status-badge {
		padding: 8rpx 16rpx;
		border-radius: 20rpx;
	}
	
	.status-text {
		font-size: 22rpx;
		color: #FFFFFF;
		font-weight: 500;
	}
	
	.record-info {
		margin-bottom: 12rpx;
	}
	
	.record-desc {
		font-size: 24rpx;
		color: #666666;
		line-height: 1.4;
	}
	
	.record-time {
		border-top: 1rpx solid #F0F0F0;
		padding-top: 12rpx;
	}
	
	.time-text {
		font-size: 22rpx;
		color: #999999;
	}
	
	/* 无认证记录提示 */
	.no-records-section {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 60rpx 32rpx;
		margin-bottom: 32rpx;
	}
	
	.no-records-icon {
		width: 80rpx;
		height: 80rpx;
		background: #F0F0F0;
		border-radius: 40rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-bottom: 20rpx;
	}
	
	.empty-icon {
		width: 40rpx;
		height: 40rpx;
		background: #CCCCCC;
		border-radius: 20rpx;
		position: relative;
	}
	
	.empty-icon::before {
		content: '';
		position: absolute;
		top: 50%;
		left: 50%;
		width: 20rpx;
		height: 2rpx;
		background: #FFFFFF;
		transform: translate(-50%, -50%);
	}
	
	.no-records-text {
		font-size: 26rpx;
		color: #999999;
	}
	
	/* 加载状态 */
	.loading-section {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 60rpx 32rpx;
		margin-bottom: 32rpx;
	}
	
	.loading-spinner {
		width: 40rpx;
		height: 40rpx;
		border: 4rpx solid #f3f3f3;
		border-top: 4rpx solid #007AFF;
		border-radius: 50%;
		animation: spin 1s linear infinite;
		margin-bottom: 20rpx;
	}
	
	@keyframes spin {
		0% { transform: rotate(0deg); }
		100% { transform: rotate(360deg); }
	}
	
	.loading-text {
		font-size: 26rpx;
		color: #666666;
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


