<template>
	<view class="container">
		<!-- 状态栏 -->
		<!-- <view class="status-bar">
			<text class="time">16:38</text>
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
			<view class="back-button" @click="goBack">
				<text class="back-arrow">‹</text>
			</view>
			<text class="nav-title">{{friendName}}</text>
			<view class="more-button" @click="showMoreOptions">
				<text class="more-dots">⋯</text>
			</view>
		</view>
		
		<!-- 好友信息区域 -->
		<view class="friend-info-section">
			<!-- 加载状态 -->
			<view v-if="loading" class="loading-container">
				<view class="loading-spinner"></view>
				<text class="loading-text">加载好友信息中...</text>
			</view>
			
			<!-- 错误状态 -->
			<view v-else-if="errorMessage" class="error-container">
				<view class="error-icon">⚠️</view>
				<text class="error-text">{{ errorMessage }}</text>
				<button class="retry-button" @click="loadFriendInfo">
					<text class="retry-text">重试</text>
				</button>
			</view>
			
			<!-- 正常显示 -->
			<view v-else>
				<view class="avatar-container">
					<view class="avatar">
						<image v-if="friendInfo.avatar" class="avatar-image" :src="friendInfo.avatar" mode="aspectFill"></image>
						<text v-else class="avatar-text">🐱</text>
					</view>
				</view>
				<view class="friend-details">
					<text class="friend-name">{{friendInfo.nickname || friendName}}</text>
					<text class="friend-nickname">昵称: {{friendInfo.nickname || friendName}}</text>
					<text v-if="friendInfo.location" class="friend-location">地区: {{friendInfo.location}}</text>
				</view>
			</view>
		</view>
		
		<!-- 功能选项列表 -->
		<view class="options-list">
			<view class="option-item" @click="goToFriendInfo">
				<text class="option-text">朋友资料</text>
				<text class="option-desc" v-if="profileData.friendsProfile">{{profileData.friendsProfile}}</text>
				<text class="option-desc" v-else>添加朋友的备注名、电话、标签、备忘、照片等。</text>
				<text class="arrow">›</text>
			</view>
			
			<view class="option-item" @click="goToFriendHomepage">
				<text class="option-text">好友主页</text>
				<text class="arrow">›</text>
			</view>
		</view>
		
		<!-- 操作按钮区域 -->
		<view class="action-buttons">
			<view class="action-btn send-message" @click="sendMessage">
				<view class="btn-icon">
					<svg class="message-icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="32" height="32">
						<path d="M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64zm0 820c-205.4 0-372-166.6-372-372s166.6-372 372-372 372 166.6 372 372-166.6 372-372 372z" fill="#333333"></path>
						<path d="M512 336c-97.2 0-176 78.8-176 176s78.8 176 176 176 176-78.8 176-176-78.8-176-176-176z" fill="#333333"></path>
					</svg>
				</view>
				<text class="btn-text">发消息</text>
			</view>
		</view>
		
		<!-- 底部手势条 -->
		<view class="home-indicator"></view>
		
		<!-- 朋友资料编辑弹窗 -->
		<view v-if="showProfileModal" class="modal-overlay" @click="closeProfileModal">
			<view class="modal-content" @click.stop>
				<view class="modal-header">
					<text class="modal-title">朋友资料</text>
					<view class="close-button" @click="closeProfileModal">
						<text class="close-icon">×</text>
					</view>
				</view>
				<view class="modal-body">
					<view class="input-group">
						<text class="input-label">备注</text>
						<textarea 
							class="textarea-field" 
							v-model="profileData.friendsProfile" 
							placeholder="请输入备注信息"
							maxlength="255"
							:show-confirm-bar="false"
						></textarea>
						<text class="char-count">{{ profileData.friendsProfile.length }}/255</text>
					</view>
				</view>
				<view class="modal-footer">
					<button class="cancel-btn" @click="closeProfileModal">取消</button>
					<button class="save-btn" @click="saveProfile" :disabled="saving">
						<text v-if="saving">保存中...</text>
						<text v-else>保存</text>
					</button>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				friendName: '',
				friendId: null,
				userId: null,
				friendInfo: {
					nickname: '',
					avatar: '',
					location: ''
				},
				loading: false,
				errorMessage: '',
				// 朋友资料弹窗相关
				showProfileModal: false,
				saving: false,
				profileData: {
					friendsProfile: ''
				}
			}
		},
		onLoad(options) {
			// 接收传递过来的好友名称和ID
			if (options.friendName) {
				this.friendName = decodeURIComponent(options.friendName);
			}
			if (options.friendId) {
				this.friendId = parseInt(options.friendId);
			}
			// 获取用户ID
			this.userId = uni.getStorageSync('userId') || 1;
			
			console.log('friend-card页面加载参数:', options);
			console.log('用户ID:', this.userId);
			console.log('好友ID:', this.friendId);
			console.log('好友名称:', this.friendName);
			
			// 加载好友信息
			this.loadFriendInfo();
			// 加载朋友资料
			this.loadFriendProfile();
		},
		methods: {
			// 加载好友信息
			async loadFriendInfo() {
				if (!this.friendId) {
					console.log('好友ID不存在，使用默认信息');
					this.friendInfo.nickname = this.friendName;
					return;
				}

				this.loading = true;
				this.errorMessage = '';

				try {
					console.log('开始加载好友信息，friendId:', this.friendId);
					const response = await uni.request({
						url: `http://localhost:8888/api/users/${this.friendId}`,
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});

					console.log('好友信息API响应状态码:', response.statusCode);
					console.log('好友信息API响应数据:', response.data);

					if (response.statusCode === 200 && response.data) {
						this.friendInfo = {
							nickname: response.data.realName || response.data.username || this.friendName,
							avatar: response.data.profilePicUrl || '',
							location: response.data.location || ''
						};
						console.log('好友信息加载成功:', this.friendInfo);
					} else {
						console.error('加载好友信息失败:', response);
						this.errorMessage = '加载好友信息失败';
						// 使用默认信息
						this.friendInfo.nickname = this.friendName;
					}
				} catch (error) {
					console.error('加载好友信息异常:', error);
					this.errorMessage = '网络错误，请检查连接';
					// 使用默认信息
					this.friendInfo.nickname = this.friendName;
				} finally {
					this.loading = false;
				}
			},

			goBack() {
				uni.navigateBack();
			},
			showMoreOptions() {
				// 显示更多选项
				uni.showActionSheet({
					itemList: ['设置备注和标签', '设置朋友圈权限', '加入黑名单', '删除好友'],
					success: (res) => {
						console.log('选择了第' + (res.tapIndex + 1) + '个选项');
					}
				});
			},
			goToFriendInfo() {
				// 打开朋友资料编辑弹窗
				this.showProfileModal = true;
				// 加载现有的朋友资料
				this.loadFriendProfile();
			},
			goToFriendHomepage() {
				// 跳转到好友主页
				uni.navigateTo({
					url: `/pages/feed/user-profile?friendName=${encodeURIComponent(this.friendName)}`
				});
			},
			sendMessage() {
				// 跳转到聊天页面
				if (this.friendId) {
					uni.navigateTo({
						url: `/pages/chat/chat?friendId=${this.friendId}&friendName=${encodeURIComponent(this.friendInfo.nickname || this.friendName)}`
					});
				} else {
					uni.showToast({
						title: '好友信息不完整',
						icon: 'error'
					});
				}
			},

			// 加载朋友资料
			async loadFriendProfile() {
				if (!this.userId || !this.friendId) {
					console.log('用户ID或好友ID不存在，无法加载朋友资料');
					return;
				}

				try {
					console.log('开始加载朋友资料，userId:', this.userId, 'friendId:', this.friendId);
					const response = await uni.request({
						url: `http://localhost:8888/api/u-entities/friends-profile/${this.userId}/${this.friendId}`,
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});

					console.log('朋友资料API响应状态码:', response.statusCode);
					console.log('朋友资料API响应数据:', response.data);

					if (response.statusCode === 200 && response.data) {
						this.profileData = {
							friendsProfile: response.data.friendsProfile || ''
						};
						console.log('朋友资料加载成功:', this.profileData);
					} else {
						console.log('朋友资料不存在，使用默认值');
						// 重置为默认值
						this.profileData = {
							friendsProfile: ''
						};
					}
				} catch (error) {
					console.error('加载朋友资料异常:', error);
					// 使用默认值
					this.profileData = {
						friendsProfile: ''
					};
				}
			},

			// 保存朋友资料
			async saveProfile() {
				if (!this.userId || !this.friendId) {
					uni.showToast({
						title: '参数错误，无法保存',
						icon: 'error'
					});
					return;
				}

				// 检查备注长度
				if (this.profileData.friendsProfile.length > 255) {
					uni.showToast({
						title: '备注内容不能超过255个字符',
						icon: 'error'
					});
					return;
				}

				this.saving = true;

				try {
					const profileData = {
						userId: this.userId,
						friendId: this.friendId,
						friendsProfile: this.profileData.friendsProfile
					};

					console.log('开始保存朋友资料:', profileData);
					const response = await uni.request({
						url: 'http://localhost:8888/api/u-entities/friends-profile/save',
						method: 'POST',
						header: {
							'Content-Type': 'application/json'
						},
						data: profileData
					});

					console.log('保存朋友资料API响应状态码:', response.statusCode);
					console.log('保存朋友资料API响应数据:', response.data);

					if (response.statusCode === 200) {
						uni.showToast({
							title: '保存成功',
							icon: 'success'
						});
						this.closeProfileModal();
					} else {
						console.error('保存朋友资料失败:', response);
						uni.showToast({
							title: '保存失败',
							icon: 'error'
						});
					}
				} catch (error) {
					console.error('保存朋友资料异常:', error);
					uni.showToast({
						title: '保存失败',
						icon: 'error'
					});
				} finally {
					this.saving = false;
				}
			},

			// 关闭朋友资料弹窗
			closeProfileModal() {
				this.showProfileModal = false;
				// 重置数据
				this.profileData = {
					friendsProfile: ''
				};
			}
		}
	}
</script>

<style>
	.container {
		min-height: 100vh;
		background-color: #FFFFFF;
		display: flex;
		flex-direction: column;
	}
	
	/* 状态栏 */
	.status-bar {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 20rpx 40rpx;
		background-color: #FFFFFF;
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
		background-color: #FFFFFF;
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
	
	.more-button {
		position: absolute;
		right: 40rpx;
		width: 60rpx;
		height: 60rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
	}
	
	.more-dots {
		font-size: 36rpx;
		color: #000;
		font-weight: bold;
	}
	
	/* 好友信息区域 */
	.friend-info-section {
		padding: 40rpx;
		background-color: #FFFFFF;
		border-bottom: 1rpx solid #f0f0f0;
		min-height: 200rpx;
	}
	
	.avatar-container {
		margin-right: 30rpx;
	}
	
	.avatar {
		width: 120rpx;
		height: 120rpx;
		border-radius: 50%;
		background: linear-gradient(135deg, #FFD700, #FFA500);
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: 0 8rpx 24rpx rgba(255, 215, 0, 0.3);
	}
	
	.avatar-text {
		font-size: 60rpx;
	}
	
	.avatar-image {
		width: 100%;
		height: 100%;
		border-radius: 50%;
	}
	
	.friend-details {
		flex: 1;
		display: flex;
		flex-direction: column;
		gap: 12rpx;
	}
	
	.friend-name {
		font-size: 36rpx;
		color: #000;
		font-weight: bold;
		display: flex;
		align-items: center;
		gap: 8rpx;
	}
	
	.friend-nickname,
	.friend-wechat-id,
	.friend-location {
		font-size: 28rpx;
		color: #333;
		line-height: 1.4;
	}
	
	/* 功能选项列表 */
	.options-list {
		background-color: #FFFFFF;
		margin-top: 20rpx;
	}
	
	.option-item {
		display: flex;
		align-items: center;
		padding: 30rpx 40rpx;
		border-bottom: 1rpx solid #f0f0f0;
		background-color: #FFFFFF;
		position: relative;
	}
	
	.option-item:last-child {
		border-bottom: none;
	}
	
	.option-text {
		font-size: 32rpx;
		color: #000;
		font-weight: 500;
		margin-right: 20rpx;
	}
	
	.option-desc {
		font-size: 24rpx;
		color: #666;
		flex: 1;
		margin-right: 20rpx;
	}
	
	.arrow {
		font-size: 36rpx;
		color: #ccc;
		font-weight: bold;
	}
	
	/* 操作按钮区域 */
	.action-buttons {
		display: flex;
		justify-content: center;
		padding: 60rpx 40rpx;
		background-color: #FFFFFF;
	}
	
	.action-btn {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 16rpx;
		padding: 30rpx 60rpx;
		border-radius: 16rpx;
		cursor: pointer;
		transition: all 0.2s ease;
		min-width: 200rpx;
	}
	
	.action-btn:active {
		transform: scale(0.95);
	}
	
	.send-message {
		background-color: #F8F8F8;
		border: 2rpx solid #E0E0E0;
	}
	
	.btn-icon {
		width: 60rpx;
		height: 60rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.message-icon {
		width: 32rpx;
		height: 32rpx;
	}
	
	.btn-text {
		font-size: 28rpx;
		color: #333333;
		font-weight: 500;
	}
	
	/* 加载状态样式 */
	.loading-container {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 40rpx;
		gap: 24rpx;
	}

	.loading-spinner {
		width: 60rpx;
		height: 60rpx;
		border: 4rpx solid #f3f3f3;
		border-top: 4rpx solid #FF69B4;
		border-radius: 50%;
		animation: spin 1s linear infinite;
	}

	@keyframes spin {
		0% { transform: rotate(0deg); }
		100% { transform: rotate(360deg); }
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
		padding: 40rpx;
		gap: 24rpx;
	}

	.error-icon {
		font-size: 80rpx;
		margin-bottom: 16rpx;
	}

	.error-text {
		font-size: 28rpx;
		color: #ff4757;
		text-align: center;
		margin-bottom: 16rpx;
	}

	.retry-button {
		background-color: #FF69B4;
		color: #FFFFFF;
		border: none;
		border-radius: 24rpx;
		padding: 16rpx 32rpx;
		font-size: 28rpx;
		cursor: pointer;
		transition: all 0.2s ease;
	}

	.retry-button:active {
		background-color: #e55a9b;
		transform: scale(0.95);
	}

	.retry-text {
		color: #FFFFFF;
		font-weight: 600;
	}

	/* 朋友资料弹窗样式 */
	.modal-overlay {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background-color: rgba(0, 0, 0, 0.5);
		display: flex;
		align-items: center;
		justify-content: center;
		z-index: 1000;
		padding: 40rpx;
	}

	.modal-content {
		background-color: #FFFFFF;
		border-radius: 20rpx;
		width: 100%;
		max-width: 600rpx;
		max-height: 80vh;
		overflow: hidden;
		box-shadow: 0 20rpx 60rpx rgba(0, 0, 0, 0.3);
	}

	.modal-header {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 40rpx 40rpx 20rpx;
		border-bottom: 1rpx solid #f0f0f0;
	}

	.modal-title {
		font-size: 36rpx;
		font-weight: bold;
		color: #000;
	}

	.close-button {
		width: 60rpx;
		height: 60rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
	}

	.close-icon {
		font-size: 48rpx;
		color: #999;
		font-weight: bold;
	}

	.modal-body {
		padding: 40rpx;
		max-height: 60vh;
		overflow-y: auto;
	}

	.input-group {
		margin-bottom: 40rpx;
	}

	.input-label {
		display: block;
		font-size: 28rpx;
		color: #333;
		margin-bottom: 16rpx;
		font-weight: 500;
	}

	.input-field {
		width: 100%;
		height: 80rpx;
		border: 2rpx solid #e0e0e0;
		border-radius: 12rpx;
		padding: 0 24rpx;
		font-size: 28rpx;
		color: #333;
		background-color: #fff;
		box-sizing: border-box;
	}

	.input-field:focus {
		border-color: #FF69B4;
		outline: none;
	}

	.textarea-field {
		width: 100%;
		min-height: 120rpx;
		border: 2rpx solid #e0e0e0;
		border-radius: 12rpx;
		padding: 20rpx 24rpx;
		font-size: 28rpx;
		color: #333;
		background-color: #fff;
		box-sizing: border-box;
		resize: none;
	}

	.textarea-field:focus {
		border-color: #FF69B4;
		outline: none;
	}

	.char-count {
		display: block;
		text-align: right;
		font-size: 24rpx;
		color: #999;
		margin-top: 8rpx;
	}

	.modal-footer {
		display: flex;
		align-items: center;
		justify-content: flex-end;
		gap: 24rpx;
		padding: 20rpx 40rpx 40rpx;
		border-top: 1rpx solid #f0f0f0;
	}

	.cancel-btn {
		padding: 20rpx 40rpx;
		border: 2rpx solid #e0e0e0;
		border-radius: 12rpx;
		background-color: #fff;
		color: #666;
		font-size: 28rpx;
		cursor: pointer;
		transition: all 0.2s ease;
	}

	.cancel-btn:active {
		background-color: #f5f5f5;
		transform: scale(0.95);
	}

	.save-btn {
		padding: 20rpx 40rpx;
		border: none;
		border-radius: 12rpx;
		background-color: #FF69B4;
		color: #fff;
		font-size: 28rpx;
		font-weight: 600;
		cursor: pointer;
		transition: all 0.2s ease;
	}

	.save-btn:disabled {
		background-color: #ccc;
		cursor: not-allowed;
	}

	.save-btn:not(:disabled):active {
		background-color: #e55a9b;
		transform: scale(0.95);
	}

	/* 底部手势条 */
	.home-indicator {
		position: fixed;
		bottom: 0;
		left: 50%;
		transform: translateX(-50%);
		width: 134rpx;
		height: 8rpx;
		background-color: rgba(0, 0, 0, 0.3);
		border-radius: 4rpx;
		z-index: 101;
		margin: 20rpx auto;
		opacity: 0.5;
	}
</style>
