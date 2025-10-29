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
			<text class="nav-title">举报证据</text>
		</view> -->
		
		<!-- 说明横幅 -->
		<view class="instruction-banner">
			<text class="instruction-text">请详细填写举报描述及图片证据</text>
		</view>
		
		<!-- 举报理由 -->
		<view class="content-section">
			<view class="section-header">
				<text class="section-label">举报理由</text>
				<text class="selected-reason">{{selectedReason}}</text>
			</view>
		</view>
		
		<!-- 举报描述 -->
		<view class="content-section">
			<view class="section-header">
				<text class="section-label">举报描述</text>
			</view>
			<view class="description-container">
				<textarea 
					class="description-input" 
					v-model="descriptionText" 
					:maxlength="400"
					placeholder="请详细填写,以提高举报通过率"
					@input="updateCharCount"
				></textarea>
				<view class="char-count">{{charCount}}/400</view>
			</view>
		</view>
		
		<!-- 图片证据 -->
		<view class="content-section">
			<view class="section-header">
				<text class="section-label">图片证据</text>
				<text class="upload-limit">最多上传9张</text>
			</view>
			<view class="image-upload-container">
				<view class="upload-button" @click="uploadImage">
					<text class="plus-icon">+</text>
				</view>
				<view class="uploaded-images" v-if="uploadedImages.length > 0">
					<view class="image-item" v-for="(image, index) in uploadedImages" :key="index">
						<image :src="image" class="uploaded-image"></image>
						<view class="delete-button" @click="deleteImage(index)">×</view>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 提交说明 -->
		<view class="submit-info">
			<text class="info-text">提交后信息将发送给平台审核,结果将在三个工作日内发送至系统消息</text>
		</view>
		
		<!-- 提交按钮 -->
		<view class="submit-button" @click="submitReport">
			<text class="submit-text">提交</text>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				selectedReason: '色情低俗',
				descriptionText: '',
				charCount: 0,
				uploadedImages: [],
				userId: 1000100,
				reportedUserId: null,
				contentId: null,
				contentType: null,
				categoryId: 1,
				loading: false,
				hasError: false,
				errorMessage: ''
			}
		},
		onLoad(options) {
			// 接收从举报页面传递过来的举报原因
			if (options.reason) {
				this.selectedReason = options.reason;
			}
			// 接收其他参数
			if (options.userId) {
				this.userId = parseInt(options.userId);
			}
			if (options.reportedUserId) {
				this.reportedUserId = parseInt(options.reportedUserId);
			}
			if (options.contentId) {
				this.contentId = parseInt(options.contentId);
			}
			if (options.contentType) {
				this.contentType = options.contentType;
			}
			// 根据举报原因设置类别ID
			this.categoryId = this.getCategoryIdByReason(this.selectedReason);
		},
		methods: {
			goBack() {
				uni.navigateBack();
			},
			updateCharCount(event) {
				this.charCount = event.detail.value.length;
			},
			uploadImage() {
				// 模拟图片上传
				uni.chooseImage({
					count: 9 - this.uploadedImages.length,
					success: (res) => {
						// 这里应该上传到服务器，这里只是模拟
						this.uploadedImages = this.uploadedImages.concat(res.tempFilePaths);
					}
				});
			},
			deleteImage(index) {
				this.uploadedImages.splice(index, 1);
			},
			async submitReport() {
				if (!this.descriptionText.trim()) {
					uni.showToast({
						title: '请填写举报描述',
						icon: 'none'
					});
					return;
				}
				
				try {
					console.log('=== 开始提交举报 ===');
					console.log('用户ID:', this.userId);
					console.log('被举报用户ID:', this.reportedUserId);
					console.log('内容ID:', this.contentId);
					console.log('内容类型:', this.contentType);
					console.log('举报类别ID:', this.categoryId);
					console.log('举报描述:', this.descriptionText);
					console.log('证据图片:', this.uploadedImages);
					
					this.loading = true;
					this.hasError = false;
					this.errorMessage = '';
					
					const response = await uni.request({
						url: 'http://localhost:8888/api/report/submit',
						method: 'POST',
						data: {
							userId: this.userId,
							reportedUserId: this.reportedUserId,
							contentId: this.contentId,
							contentType: this.contentType,
							categoryId: this.categoryId,
							description: this.descriptionText,
							evidenceImg: JSON.stringify(this.uploadedImages)
						},
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('=== 举报API响应详情 ===');
					console.log('状态码:', response.statusCode);
					console.log('响应数据:', response.data);
					
					if (response.statusCode === 200 && response.data.success) {
						console.log('举报提交成功');
						
						uni.showToast({
							title: '举报提交成功',
							icon: 'success'
						});
						
						// 延迟跳转到反馈页面
						setTimeout(() => {
							uni.navigateTo({
								url: `/pages/report/report-feedback?type=${encodeURIComponent(this.selectedReason)}&reportId=${response.data.reportId}`
							});
						}, 1500);
					} else {
						console.error('举报提交失败:', response.data.message);
						this.hasError = true;
						this.errorMessage = response.data.message || '举报提交失败';
						uni.showToast({
							title: response.data.message || '举报提交失败',
							icon: 'error'
						});
					}
				} catch (error) {
					console.error('举报提交异常:', error);
					this.hasError = true;
					this.errorMessage = '网络连接失败，请检查网络设置';
					uni.showToast({
						title: '网络错误',
						icon: 'error'
					});
				} finally {
					this.loading = false;
				}
			},
			
			// 根据举报原因获取类别ID
			getCategoryIdByReason(reason) {
				const reasonMap = {
					'色情低俗': 1,
					'政治敏感': 2,
					'诈骗信息': 3,
					'种族歧视': 4,
					'攻击谩骂': 5,
					'网络暴力': 6,
					'站外引流': 7,
					'违法违规': 8,
					'涉未成年人': 9,
					'其他违规': 10
				};
				return reasonMap[reason] || 10;
			}
		}
	}
</script>

<style>
	.container {
		min-height: 100vh;
		background-color: #f5f5f5;
		padding-bottom: 120rpx;
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
	
	/* 说明横幅 */
	.instruction-banner {
		background-color: #87CEEB;
		padding: 30rpx 40rpx;
		margin: 30rpx 40rpx;
		border-radius: 20rpx;
	}
	
	.instruction-text {
		color: #fff;
		font-size: 28rpx;
		text-align: center;
		line-height: 1.5;
	}
	
	/* 内容区域 */
	.content-section {
		background-color: #fff;
		margin: 20rpx 40rpx;
		border-radius: 20rpx;
		padding: 30rpx;
		box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
	}
	
	.section-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 20rpx;
	}
	
	.section-label {
		font-size: 32rpx;
		font-weight: bold;
		color: #000;
	}
	
	.selected-reason {
		font-size: 28rpx;
		color: #666;
	}
	
	.upload-limit {
		font-size: 24rpx;
		color: #999;
	}
	
	/* 举报描述 */
	.description-container {
		position: relative;
	}
	
	.description-input {
		width: 100%;
		min-height: 200rpx;
		padding: 20rpx;
		border: 1rpx solid #e0e0e0;
		border-radius: 10rpx;
		font-size: 28rpx;
		line-height: 1.5;
		box-sizing: border-box;
	}
	
	.char-count {
		position: absolute;
		bottom: 20rpx;
		right: 20rpx;
		font-size: 24rpx;
		color: #999;
	}
	
	/* 图片上传 */
	.image-upload-container {
		display: flex;
		flex-wrap: wrap;
		gap: 20rpx;
	}
	
	.upload-button {
		width: 120rpx;
		height: 120rpx;
		background-color: #f0f0f0;
		border: 2rpx dashed #ccc;
		border-radius: 10rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
	}
	
	.plus-icon {
		font-size: 48rpx;
		color: #999;
		font-weight: bold;
	}
	
	.uploaded-images {
		display: flex;
		flex-wrap: wrap;
		gap: 20rpx;
	}
	
	.image-item {
		position: relative;
		width: 120rpx;
		height: 120rpx;
	}
	
	.uploaded-image {
		width: 100%;
		height: 100%;
		border-radius: 10rpx;
	}
	
	.delete-button {
		position: absolute;
		top: -10rpx;
		right: -10rpx;
		width: 40rpx;
		height: 40rpx;
		background-color: #ff4444;
		color: #fff;
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 24rpx;
		font-weight: bold;
		cursor: pointer;
	}
	
	/* 提交说明 */
	.submit-info {
		padding: 0 40rpx;
		margin: 40rpx 0;
	}
	
	.info-text {
		font-size: 24rpx;
		color: #999;
		line-height: 1.5;
		text-align: center;
	}
	
	/* 提交按钮 */
	.submit-button {
		position: fixed;
		bottom: 40rpx;
		left: 40rpx;
		right: 40rpx;
		background: linear-gradient(135deg, #FF69B4, #FFB6C1);
		padding: 30rpx;
		border-radius: 20rpx;
		text-align: center;
		box-shadow: 0 10rpx 25rpx rgba(255, 105, 180, 0.3);
		cursor: pointer;
		transition: transform 0.2s ease;
	}
	
	.submit-button:active {
		transform: scale(0.98);
	}
	
	.submit-text {
		color: #fff;
		font-size: 36rpx;
		font-weight: bold;
	}
</style>
