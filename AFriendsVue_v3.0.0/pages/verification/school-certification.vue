<template>
	<view class="school-certification-page">
		<!-- 状态栏 -->
		<!-- <view class="status-bar">
			<text class="time">9:41</text>
			<view class="status-icons">
				<view class="signal"></view>
				<view class="wifi"></view>
				<view class="battery"></view>
			</view>
		</view> -->
		
		<!-- 头部导航 -->
		<!-- <view class="header">
			<view class="back-button" @click="goBack">
				<view class="back-arrow"></view>
			</view>
			<text class="page-title">学校认证</text>
			<view class="placeholder"></view>
		</view> -->
		
		<!-- 提示文字 -->
		<view class="prompt-text">
			<text class="prompt">请选择学校名称</text>
		</view>
		
		<!-- 学校选择区域 -->
		<view class="school-selection">
			<view class="selection-field" @click="showSchoolPicker" v-if="!loadError">
				<text class="field-label">学校名称</text>
				<view class="selection-content">
					<text class="selected-text" v-if="selectedSchool">{{ selectedSchool }}</text>
					<text class="placeholder-text" v-else-if="!loading">请选择></text>
					<text class="loading-text" v-else>加载中...</text>
				</view>
			</view>
			
			<!-- 错误提示 -->
			<view class="error-section" v-if="loadError">
				<view class="error-icon">⚠️</view>
				<text class="error-text">{{ errorMessage }}</text>
				<view class="retry-button" @click="loadSchoolData">
					<text class="retry-text">重试</text>
				</view>
			</view>
		</view>
		
		<!-- 协议同意区域 -->
		<view class="agreement-section">
			<view class="agreement-item">
				<view class="checkbox" :class="{ checked: isAgreed }" @click="toggleAgreement">
					<text v-if="isAgreed" class="checkmark">✓</text>
				</view>
				<text class="agreement-text">阅读并同意</text>
				<text class="agreement-link" @click="goToTermsOfService">《AFriends官方认证服务协议》</text>
			</view>
		</view>
		
		<!-- 下一步按钮 -->
		<view class="next-button-section">
			<view class="next-button" @click="goToNextStep">
				<text class="next-text">下一步</text>
			</view>
		</view>
		
		<!-- 底部指示器 -->
		<view class="home-indicator"></view>
		
		<!-- 学校选择弹窗 -->
		<view class="school-picker-modal" v-if="showPicker" @click="hideSchoolPicker">
			<view class="modal-content" @click.stop>
				<view class="modal-header">
					<view class="modal-back" @click="hideSchoolPicker">
						<view class="back-arrow"></view>
					</view>
					<text class="modal-title">请选择您的学校</text>
					<view class="search-icon">
						<view class="magnifier"></view>
					</view>
				</view>
				<view class="school-list">
					<view class="school-item" v-for="(province, index) in provinces" :key="index" @click="selectProvince(province)">
						<text class="province-name">{{ province }}</text>
						<view class="arrow-icon">></view>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		name: 'SchoolCertificationPage',
		data() {
			return {
				isAgreed: false,
				showPicker: false,
				provinces: [],
				schools: [],
				selectedProvince: '',
				selectedSchool: '',
				loading: false,
				loadError: false,
				errorMessage: ''
			}
		},
		onLoad() {
			this.loadSchoolData();
		},
		methods: {
			// 加载学校数据
			async loadSchoolData() {
				this.loading = true;
				this.loadError = false;
				this.errorMessage = '';
				
				try {
					const response = await uni.request({
						url: 'http://localhost:8888/api/bc-entities/cert-student-base/all',
						method: 'GET',
						timeout: 10000
					});
					
					if (response.data && response.data.length > 0) {
						this.schools = response.data;
						// 从学校数据中提取省份
						const provinceSet = new Set();
						response.data.forEach(school => {
							if (school.region) {
								provinceSet.add(school.region);
							}
						});
						this.provinces = Array.from(provinceSet).sort();
						console.log('成功加载学校数据:', this.schools.length, '所学校');
					} else {
						// 数据库没有数据，显示错误信息
						this.loadError = true;
						this.errorMessage = '数据库中没有学校数据，请联系管理员';
						this.provinces = [];
						this.schools = [];
					}
				} catch (error) {
					console.error('获取学校数据失败:', error);
					this.loadError = true;
					this.errorMessage = '网络连接失败，请检查网络后重试';
					this.provinces = [];
					this.schools = [];
					
					uni.showToast({
						title: '数据加载失败',
						icon: 'none',
						duration: 3000
					});
				} finally {
					this.loading = false;
				}
			},
			
			// 根据省份获取学校列表
			getSchoolsByProvince(province) {
				return this.schools.filter(school => school.region === province);
			},
			
			goBack() {
				uni.navigateBack();
			},
			toggleAgreement() {
				this.isAgreed = !this.isAgreed;
			},
			showSchoolPicker() {
				this.showPicker = true;
			},
			hideSchoolPicker() {
				this.showPicker = false;
			},
			selectProvince(province) {
				console.log('选择省份:', province);
				this.selectedProvince = province;
				// 跳转到学校选择页面，传递省份信息
				uni.navigateTo({
					url: `/pages/verification/school-list?province=${encodeURIComponent(province)}`
				});
				this.hideSchoolPicker();
			},
			async goToNextStep() {
				// 检查是否同意协议
				if (!this.isAgreed) {
					uni.showToast({
						title: '请先同意服务条款',
						icon: 'none'
					});
					return;
				}
				
				// 检查是否选择了学校
				if (!this.selectedSchool) {
					uni.showToast({
						title: '请选择学校',
						icon: 'none'
					});
					return;
				}
				
				try {
					// 显示加载提示
					uni.showLoading({
						title: '正在提交认证信息...'
					});
					
					// 获取用户ID
					const userId = uni.getStorageSync('userId');
					if (!userId) {
						uni.hideLoading();
						uni.showToast({
							title: '请先登录',
							icon: 'none'
						});
						return;
					}
					
					// 构建认证信息JSON
					const certInfo = {
						name: '', // 这里可以添加姓名输入框
						degree: '', // 这里可以添加学历选择
						school: this.selectedSchool,
						province: this.selectedProvince,
						studentId: '', // 这里可以添加学号输入框
						submitTime: new Date().toISOString()
					};
					
					// 调用后端API保存认证记录
					const response = await this.saveCertRecord(userId, 'STUDENT', certInfo);
					
					uni.hideLoading();
					
					if (response.success) {
						uni.showToast({
							title: '认证信息提交成功',
							icon: 'success'
						});
						
						// 跳转到认证反馈页面
						setTimeout(() => {
							uni.navigateTo({
								url: `/pages/verification/certification-feedback?type=school&school=${encodeURIComponent(this.selectedSchool)}&province=${encodeURIComponent(this.selectedProvince)}`
							});
						}, 1500);
					} else {
						uni.showToast({
							title: response.message || '提交失败',
							icon: 'none'
						});
					}
					
				} catch (error) {
					uni.hideLoading();
					console.error('提交认证信息失败:', error);
					uni.showToast({
						title: '网络错误，请重试',
						icon: 'none'
					});
				}
			},
			goToTermsOfService() {
				uni.navigateTo({
					url: '/pages/settings/terms-of-service'
				});
			},
			
			// 保存认证记录到后端
			async saveCertRecord(userId, certType, certInfo) {
				try {
					const response = await uni.request({
						url: 'http://localhost:8888/api/u-entities/user-cert-record/save',
						method: 'POST',
						header: {
							'Content-Type': 'application/json'
						},
						data: {
							userId: userId,
							certType: certType,
							certInfo: JSON.stringify(certInfo)
						},
						timeout: 10000
					});
					
					if (response.statusCode === 200) {
						return response.data;
					} else {
						throw new Error(`HTTP ${response.statusCode}`);
					}
				} catch (error) {
					console.error('保存认证记录失败:', error);
					throw error;
				}
			}
		}
	}
</script>

<style>
	.school-certification-page {
		min-height: 100vh;
		background-color: #FFFFFF;
		display: flex;
		flex-direction: column;
		position: relative;
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
	
	/* 头部导航 */
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
		border-right: 12rpx solid #666666;
		border-top: 8rpx solid transparent;
		border-bottom: 8rpx solid transparent;
	}
	
	.page-title {
		font-size: 32rpx;
		color: #333333;
		font-weight: 600;
	}
	
	.placeholder {
		width: 48rpx;
	}
	
	/* 提示文字 */
	.prompt-text {
		padding: 24rpx 32rpx;
	}
	
	.prompt {
		font-size: 26rpx;
		color: #999999;
	}
	
	/* 学校选择区域 */
	.school-selection {
		padding: 0 32rpx;
	}
	
	.selection-field {
		height: 120rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 16rpx;
		border-bottom: 1rpx solid #F0F0F0;
		cursor: pointer;
	}
	
	.selection-field:active {
		background-color: #F8F8F8;
	}
	
	.field-label {
		font-size: 28rpx;
		color: #333333;
		font-weight: 500;
	}
	
	.selection-content {
		display: flex;
		align-items: center;
	}
	
	.placeholder-text {
		font-size: 26rpx;
		color: #CCCCCC;
	}
	
	.selected-text {
		font-size: 26rpx;
		color: #333333;
	}
	
	.loading-text {
		font-size: 26rpx;
		color: #999999;
	}
	
	/* 错误提示样式 */
	.error-section {
		padding: 32rpx;
		background-color: #FFF2F0;
		border: 1rpx solid #FFCCC7;
		border-radius: 8rpx;
		margin: 16rpx 0;
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 16rpx;
	}
	
	.error-icon {
		font-size: 48rpx;
	}
	
	.error-text {
		font-size: 26rpx;
		color: #FF4D4F;
		text-align: center;
		line-height: 1.5;
	}
	
	.retry-button {
		padding: 16rpx 32rpx;
		background-color: #FF4D4F;
		border-radius: 8rpx;
		cursor: pointer;
	}
	
	.retry-button:active {
		background-color: #D9363E;
	}
	
	.retry-text {
		font-size: 24rpx;
		color: #FFFFFF;
		font-weight: 500;
	}
	
	/* 协议同意区域 */
	.agreement-section {
		flex: 1;
		padding: 32rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.agreement-item {
		display: flex;
		align-items: center;
		gap: 16rpx;
	}
	
	.checkbox {
		width: 32rpx;
		height: 32rpx;
		border: 2rpx solid #CCCCCC;
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
	}
	
	.checkbox.checked {
		background-color: #007AFF;
		border-color: #007AFF;
	}
	
	.checkmark {
		color: #FFFFFF;
		font-size: 20rpx;
		font-weight: bold;
	}
	
	.agreement-text {
		font-size: 26rpx;
		color: #666666;
	}
	
	.agreement-link {
		font-size: 26rpx;
		color: #007AFF;
		text-decoration: underline;
	}
	
	/* 下一步按钮 */
	.next-button-section {
		padding: 0 32rpx 32rpx;
	}
	
	.next-button {
		height: 88rpx;
		background: linear-gradient(135deg, #FF69B4, #FF1493);
		border-radius: 44rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
		transition: all 0.3s ease;
	}
	
	.next-button.disabled {
		background: #CCCCCC;
		cursor: not-allowed;
	}
	
	.next-button:active:not(.disabled) {
		transform: scale(0.98);
	}
	
	.next-text {
		font-size: 32rpx;
		color: #FFFFFF;
		font-weight: 600;
	}
	
	/* 底部指示器 */
	.home-indicator {
		height: 8rpx;
		background-color: #000000;
		border-radius: 4rpx;
		margin: 32rpx auto;
		width: 120rpx;
	}
	
	/* 学校选择弹窗 */
	.school-picker-modal {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background-color: rgba(0, 0, 0, 0.5);
		z-index: 1000;
		display: flex;
		align-items: flex-end;
	}
	
	.modal-content {
		width: 100%;
		background-color: #FFFFFF;
		border-radius: 24rpx 24rpx 0 0;
		max-height: 80vh;
		overflow: hidden;
	}
	
	.modal-header {
		height: 88rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 32rpx;
		border-bottom: 1rpx solid #F0F0F0;
	}
	
	.modal-back {
		width: 48rpx;
		height: 48rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.modal-title {
		font-size: 28rpx;
		color: #333333;
		font-weight: 600;
	}
	
	.search-icon {
		width: 48rpx;
		height: 48rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.magnifier {
		width: 24rpx;
		height: 24rpx;
		border: 2rpx solid #666666;
		border-radius: 50%;
		position: relative;
	}
	
	.magnifier::after {
		content: '';
		position: absolute;
		bottom: -6rpx;
		right: -6rpx;
		width: 12rpx;
		height: 2rpx;
		background-color: #666666;
		transform: rotate(45deg);
	}
	
	.school-list {
		max-height: 60vh;
		overflow-y: auto;
	}
	
	.school-item {
		height: 100rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 32rpx;
		border-bottom: 1rpx solid #F0F0F0;
		cursor: pointer;
	}
	
	.school-item:active {
		background-color: #F8F8F8;
	}
	
	.province-name {
		font-size: 28rpx;
		color: #333333;
	}
	
	.arrow-icon {
		font-size: 28rpx;
		color: #CCCCCC;
	}
</style>
