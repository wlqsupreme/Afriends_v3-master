<template>
	<view class="likes-dislikes-page">
		<!-- 头部导航 -->
		<view class="header">
			<!-- <view class="back-button" @click="goBack">
				<view class="back-arrow"></view>
			</view> -->
			<!-- <text class="page-title">喜恶</text> -->
			<!-- <view class="placeholder"></view> -->
		</view>
		
		<!-- 统计卡片 -->
		<view class="stats-card">
			<view class="stats-content">
				<view class="stats-item">
					<text class="stats-number">{{ likesCount }}</text>
					<text class="stats-label">喜欢</text>
				</view>
				<view class="stats-divider"></view>
				<view class="stats-item">
					<text class="stats-number">{{ dislikesCount }}</text>
					<text class="stats-label">讨厌</text>
				</view>
				<view class="stats-divider"></view>
				<view class="stats-item">
					<text class="stats-number">{{ totalCount }}</text>
					<text class="stats-label">总计</text>
				</view>
			</view>
		</view>
		
		<!-- 喜欢列表 -->
		<view class="preference-section">
			<view class="section-header" @click="toggleLikes">
				<text class="section-title">我喜欢的</text>
				<view class="expand-icon" :class="{ expanded: likesExpanded }">
					<text class="expand-arrow">^</text>
				</view>
			</view>
			<view class="section-content" v-if="likesExpanded">
				<!-- 加载状态 -->
				<view class="loading-container" v-if="loading">
					<text class="loading-text">正在加载数据...</text>
				</view>
				
				<!-- 错误状态 -->
				<view class="error-container" v-if="hasError && !loading">
					<view class="error-icon">⚠️</view>
					<text class="error-text">{{ errorMessage }}</text>
					<view class="error-actions">
						<button class="retry-button" @click="loadLikesDislikesData">重新加载</button>
						<button class="retry-button" @click="testConnection">测试连接</button>
					</view>
				</view>
				
				<!-- 喜欢列表 -->
				<view v-if="!hasError && !loading">
					<view class="preference-item" v-for="(item, index) in likesList" :key="'like-' + index">
						<view class="item-icon like-icon">
							<text class="icon-text">❤️</text>
						</view>
						<view class="item-content">
							<text class="item-text">{{ item.text || item }}</text>
							<text class="item-time" v-if="item.timeAgo">{{ item.timeAgo }}</text>
						</view>
						<view class="item-actions">
							<view class="action-btn edit-btn" @click="editItem('like', index)">
								<text class="action-text">编辑</text>
							</view>
							<view class="action-btn delete-btn" @click="deleteItem('like', index)">
								<text class="action-text">删除</text>
							</view>
						</view>
					</view>
					<view class="add-item-btn" @click="addItem('like')">
						<text class="add-text">+ 添加喜欢</text>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 讨厌列表 -->
		<view class="preference-section">
			<view class="section-header" @click="toggleDislikes">
				<text class="section-title">我讨厌的</text>
				<view class="expand-icon" :class="{ expanded: dislikesExpanded }">
					<text class="expand-arrow">^</text>
				</view>
			</view>
			<view class="section-content" v-if="dislikesExpanded">
				<!-- 讨厌列表 -->
				<view v-if="!hasError && !loading">
					<view class="preference-item" v-for="(item, index) in dislikesList" :key="'dislike-' + index">
						<view class="item-icon dislike-icon">
							<text class="icon-text">💔</text>
						</view>
						<view class="item-content">
							<text class="item-text">{{ item.text || item }}</text>
							<text class="item-time" v-if="item.timeAgo">{{ item.timeAgo }}</text>
						</view>
						<view class="item-actions">
							<view class="action-btn edit-btn" @click="editItem('dislike', index)">
								<text class="action-text">编辑</text>
							</view>
							<view class="action-btn delete-btn" @click="deleteItem('dislike', index)">
								<text class="action-text">删除</text>
							</view>
						</view>
					</view>
					<view class="add-item-btn" @click="addItem('dislike')">
						<text class="add-text">+ 添加讨厌</text>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 浮动操作按钮 -->
		<!-- <view class="fab-button" @click="showAddModal">
			<text class="fab-icon">+</text>
		</view> -->
		
		<!-- 添加/编辑模态框 -->
		<view class="modal-overlay" v-if="showModal" @click="hideModal">
			<view class="modal-content" @click.stop>
				<view class="modal-header">
					<text class="modal-title">{{ isEditing ? '编辑' : '添加' }}{{ modalType === 'like' ? '喜欢' : '讨厌' }}</text>
					<view class="close-btn" @click="hideModal">
						<text class="close-text">×</text>
					</view>
				</view>
				<view class="modal-body">
					<input 
						class="input-field" 
						v-model="inputValue" 
						:placeholder="'请输入您' + (modalType === 'like' ? '喜欢' : '讨厌') + '的内容'"
						maxlength="50"
					/>
					<view class="modal-actions">
						<view class="modal-btn cancel-btn" @click="hideModal">
							<text class="btn-text">取消</text>
						</view>
						<view class="modal-btn confirm-btn" @click="confirmAction">
							<text class="btn-text">确定</text>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 底部指示器 -->
		<!-- <view class="home-indicator"></view> -->
	</view>
</template>

<script>
	export default {
		name: 'LikesDislikesPage',
		data() {
			return {
				likesExpanded: true,
				dislikesExpanded: true,
				likesList: [],
				dislikesList: [],
				showModal: false,
				modalType: 'like', // 'like' or 'dislike'
				inputValue: '',
				isEditing: false,
				editingIndex: -1,
				editingId: null,
				userId: 1000100, // 默认用户ID，从页面参数获取
				loading: false,
				errorMessage: '',
				hasError: false
			}
		},
		onLoad(options) {
			// 从页面参数获取用户ID
			if (options.userId) {
				this.userId = parseInt(options.userId);
			}
			console.log('页面加载，用户ID:', this.userId);
			// 加载数据
			this.loadLikesDislikesData();
		},
		computed: {
			likesCount() {
				return this.likesList.length;
			},
			dislikesCount() {
				return this.dislikesList.length;
			},
			totalCount() {
				return this.likesCount + this.dislikesCount;
			}
		},
		methods: {
			// 加载喜欢讨厌数据
			async loadLikesDislikesData() {
				try {
					console.log('=== 开始加载喜欢讨厌数据 ===');
					console.log('用户ID:', this.userId);
					console.log('请求URL:', `http://localhost:8888/api/likes-dislikes/data?userId=${this.userId}`);
					this.loading = true;
					this.hasError = false;
					this.errorMessage = '';
					
					const response = await uni.request({
						url: `http://localhost:8888/api/likes-dislikes/data?userId=${this.userId}`,
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('=== API响应详情 ===');
					console.log('状态码:', response.statusCode);
					console.log('响应数据:', response.data);
					
					if (response.statusCode === 200 && response.data.success) {
						const data = response.data.data;
						this.likesList = data.likes || [];
						this.dislikesList = data.dislikes || [];
						
						console.log('成功加载喜欢讨厌数据，喜欢', this.likesList.length, '条，讨厌', this.dislikesList.length, '条');
						
						if (this.likesList.length === 0 && this.dislikesList.length === 0) {
							this.hasError = true;
							this.errorMessage = '暂无喜欢讨厌数据，请添加一些内容';
						}
					} else {
						console.error('加载喜欢讨厌数据失败:', response.data.message);
						this.hasError = true;
						this.errorMessage = response.data.message || '加载数据失败';
					}
				} catch (error) {
					console.error('加载喜欢讨厌数据异常:', error);
					this.hasError = true;
					this.errorMessage = '网络连接失败，请检查网络设置';
				} finally {
					this.loading = false;
				}
			},
			
			// 测试后端连接
			async testConnection() {
				try {
					console.log('=== 测试后端连接 ===');
					const response = await uni.request({
						url: 'http://localhost:8888/api/likes-dislikes/test',
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('测试连接响应:', response);
					
					if (response.statusCode === 200) {
						uni.showToast({
							title: '后端连接正常',
							icon: 'success'
						});
					} else {
						uni.showToast({
							title: '后端连接失败',
							icon: 'error'
						});
					}
				} catch (error) {
					console.error('测试连接异常:', error);
					uni.showToast({
						title: '连接异常',
						icon: 'error'
					});
				}
			},
			
			goBack() {
				uni.navigateBack();
			},
			toggleLikes() {
				this.likesExpanded = !this.likesExpanded;
			},
			toggleDislikes() {
				this.dislikesExpanded = !this.dislikesExpanded;
			},
			showAddModal() {
				this.modalType = 'like';
				this.isEditing = false;
				this.inputValue = '';
				this.showModal = true;
			},
			hideModal() {
				this.showModal = false;
				this.inputValue = '';
				this.isEditing = false;
				this.editingIndex = -1;
				this.editingId = null;
			},
			addItem(type) {
				this.modalType = type;
				this.isEditing = false;
				this.inputValue = '';
				this.showModal = true;
			},
			editItem(type, index) {
				this.modalType = type;
				this.isEditing = true;
				this.editingIndex = index;
				const item = type === 'like' ? this.likesList[index] : this.dislikesList[index];
				this.inputValue = item.text || item;
				this.editingId = item.id || null;
				this.showModal = true;
			},
			async deleteItem(type, index) {
				uni.showModal({
					title: '确认删除',
					content: '确定要删除这个项目吗？',
					success: async (res) => {
						if (res.confirm) {
							try {
								const item = type === 'like' ? this.likesList[index] : this.dislikesList[index];
								const itemId = item.id || item;
								
								const response = await uni.request({
									url: `http://localhost:8888/api/likes-dislikes/delete-${type}`,
									method: 'POST',
									data: {
										userId: this.userId,
										[type + 'Id']: itemId
									},
									header: {
										'Content-Type': 'application/json'
									}
								});
								
								if (response.statusCode === 200 && response.data.success) {
									// 从列表中移除
									if (type === 'like') {
										this.likesList.splice(index, 1);
									} else {
										this.dislikesList.splice(index, 1);
									}
									uni.showToast({
										title: '删除成功',
										icon: 'success'
									});
								} else {
									uni.showToast({
										title: response.data.message || '删除失败',
										icon: 'error'
									});
								}
							} catch (error) {
								console.error('删除操作异常:', error);
								uni.showToast({
									title: '删除失败',
									icon: 'error'
								});
							}
						}
					}
				});
			},
			async confirmAction() {
				if (!this.inputValue.trim()) {
					uni.showToast({
						title: '请输入内容',
						icon: 'none'
					});
					return;
				}
				
				try {
					if (this.isEditing) {
						// 编辑模式
						const response = await uni.request({
							url: `http://localhost:8888/api/likes-dislikes/edit-${this.modalType}`,
							method: 'POST',
							data: {
								userId: this.userId,
								[this.modalType + 'Id']: this.editingId,
								[this.modalType + 'Text']: this.inputValue.trim(),
								[this.modalType + 'Type']: 'general'
							},
							header: {
								'Content-Type': 'application/json'
							}
						});
						
						if (response.statusCode === 200 && response.data.success) {
							// 更新本地数据
							const item = this.modalType === 'like' ? this.likesList[this.editingIndex] : this.dislikesList[this.editingIndex];
							item.text = this.inputValue.trim();
							item.updatedAt = new Date();
							
							uni.showToast({
								title: '编辑成功',
								icon: 'success'
							});
						} else {
							uni.showToast({
								title: response.data.message || '编辑失败',
								icon: 'error'
							});
						}
					} else {
						// 添加模式
						const response = await uni.request({
							url: `http://localhost:8888/api/likes-dislikes/add-${this.modalType}`,
							method: 'POST',
							data: {
								userId: this.userId,
								[this.modalType + 'Text']: this.inputValue.trim(),
								[this.modalType + 'Type']: 'general'
							},
							header: {
								'Content-Type': 'application/json'
							}
						});
						
						if (response.statusCode === 200 && response.data.success) {
							// 添加到本地数据
							const newItem = {
								id: response.data.data,
								text: this.inputValue.trim(),
								type: 'general',
								createdAt: new Date(),
								updatedAt: new Date(),
								timeAgo: '刚刚'
							};
							
							if (this.modalType === 'like') {
								this.likesList.unshift(newItem);
							} else {
								this.dislikesList.unshift(newItem);
							}
							
							uni.showToast({
								title: '添加成功',
								icon: 'success'
							});
						} else {
							uni.showToast({
								title: response.data.message || '添加失败',
								icon: 'error'
							});
						}
					}
				} catch (error) {
					console.error('操作异常:', error);
					uni.showToast({
						title: '操作失败',
						icon: 'error'
					});
				}
				
				this.hideModal();
			}
		}
	}
</script>

<style>
	.likes-dislikes-page {
		min-height: 100vh;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		display: flex;
		flex-direction: column;
	}
	
	/* 头部导航 */
	.header {
		height: 40rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 32rpx;
		margin-bottom: 24rpx;
	}
	
	.back-button {
		width: 48rpx;
		height: 48rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		background-color: rgba(255, 255, 255, 0.2);
		border-radius: 24rpx;
		backdrop-filter: blur(10rpx);
	}
	
	.back-arrow {
		width: 0;
		height: 0;
		border-right: 12rpx solid #FFFFFF;
		border-top: 8rpx solid transparent;
		border-bottom: 8rpx solid transparent;
	}
	
	.page-title {
		font-size: 36rpx;
		color: #FFFFFF;
		font-weight: 700;
		text-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.3);
	}
	
	.placeholder {
		width: 48rpx;
	}
	
	/* 统计卡片 */
	.stats-card {
		margin: 0 32rpx 32rpx;
		background: rgba(255, 255, 255, 0.95);
		border-radius: 24rpx;
		padding: 32rpx;
		box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
		backdrop-filter: blur(10rpx);
	}
	
	.stats-content {
		display: flex;
		align-items: center;
		justify-content: space-around;
	}
	
	.stats-item {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 8rpx;
	}
	
	.stats-number {
		font-size: 48rpx;
		color: #667eea;
		font-weight: 800;
	}
	
	.stats-label {
		font-size: 24rpx;
		color: #666666;
		font-weight: 500;
	}
	
	.stats-divider {
		width: 2rpx;
		height: 60rpx;
		background: linear-gradient(to bottom, transparent, #E0E0E0, transparent);
	}
	
	/* 偏好部分 */
	.preference-section {
		margin: 0 32rpx 24rpx;
		background: rgba(255, 255, 255, 0.95);
		border-radius: 20rpx;
		overflow: hidden;
		box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.08);
		backdrop-filter: blur(10rpx);
	}
	
	.section-header {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 32rpx;
		background: rgba(255, 255, 255, 0.8);
		border-bottom: 1rpx solid rgba(0, 0, 0, 0.05);
	}
	
	.section-title {
		font-size: 32rpx;
		color: #333333;
		font-weight: 600;
	}
	
	.expand-icon {
		width: 40rpx;
		height: 40rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		transition: transform 0.3s ease;
	}
	
	.expand-icon.expanded {
		transform: rotate(180deg);
	}
	
	.expand-arrow {
		font-size: 24rpx;
		color: #666666;
		font-weight: 600;
	}
	
	.section-content {
		padding: 0 32rpx 32rpx;
	}
	
	.preference-item {
		display: flex;
		align-items: center;
		gap: 24rpx;
		padding: 24rpx 0;
		border-bottom: 1rpx solid rgba(0, 0, 0, 0.05);
	}
	
	.preference-item:last-child {
		border-bottom: none;
	}
	
	.item-icon {
		width: 60rpx;
		height: 60rpx;
		border-radius: 30rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		flex-shrink: 0;
	}
	
	.like-icon {
		background: linear-gradient(135deg, #FF6B6B, #FF8E8E);
	}
	
	.dislike-icon {
		background: linear-gradient(135deg, #6C5CE7, #A29BFE);
	}
	
	.icon-text {
		font-size: 28rpx;
	}
	
	.item-content {
		flex: 1;
	}
	
	.item-text {
		font-size: 28rpx;
		color: #333333;
		font-weight: 500;
		display: block;
		margin-bottom: 4rpx;
	}
	
	.item-time {
		font-size: 20rpx;
		color: #999999;
		display: block;
	}
	
	.item-actions {
		display: flex;
		gap: 16rpx;
	}
	
	.action-btn {
		padding: 8rpx 16rpx;
		border-radius: 16rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.edit-btn {
		background: linear-gradient(135deg, #4CAF50, #45a049);
	}
	
	.delete-btn {
		background: linear-gradient(135deg, #F44336, #D32F2F);
	}
	
	.action-text {
		font-size: 20rpx;
		color: #FFFFFF;
		font-weight: 500;
	}
	
	.add-item-btn {
		margin-top: 24rpx;
		padding: 20rpx;
		background: rgba(102, 126, 234, 0.1);
		border: 2rpx dashed rgba(102, 126, 234, 0.3);
		border-radius: 16rpx;
		text-align: center;
		transition: all 0.3s ease;
	}
	
	.add-item-btn:active {
		background: rgba(102, 126, 234, 0.2);
		border-color: rgba(102, 126, 234, 0.5);
	}
	
	.add-text {
		font-size: 26rpx;
		color: #667eea;
		font-weight: 500;
	}
	
	/* 浮动操作按钮 */
	.fab-button {
		position: fixed;
		bottom: 120rpx;
		right: 32rpx;
		width: 120rpx;
		height: 120rpx;
		background: linear-gradient(135deg, #4CAF50, #45a049);
		border-radius: 60rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: 0 8rpx 32rpx rgba(76, 175, 80, 0.4);
		transition: all 0.3s ease;
	}
	
	.fab-button:active {
		transform: scale(0.95);
		box-shadow: 0 4rpx 16rpx rgba(76, 175, 80, 0.6);
	}
	
	.fab-icon {
		font-size: 48rpx;
		color: #FFFFFF;
		font-weight: 300;
	}
	
	/* 模态框 */
	.modal-overlay {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background: rgba(0, 0, 0, 0.5);
		display: flex;
		align-items: center;
		justify-content: center;
		z-index: 1000;
	}
	
	.modal-content {
		width: 600rpx;
		background: #FFFFFF;
		border-radius: 24rpx;
		overflow: hidden;
		box-shadow: 0 16rpx 64rpx rgba(0, 0, 0, 0.2);
	}
	
	.modal-header {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 32rpx;
		border-bottom: 1rpx solid #F0F0F0;
	}
	
	.modal-title {
		font-size: 32rpx;
		color: #333333;
		font-weight: 600;
	}
	
	.close-btn {
		width: 48rpx;
		height: 48rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		border-radius: 24rpx;
		background: #F5F5F5;
	}
	
	.close-text {
		font-size: 32rpx;
		color: #666666;
		font-weight: 600;
	}
	
	.modal-body {
		padding: 32rpx;
	}
	
	.input-field {
		width: 100%;
		height: 80rpx;
		border: 2rpx solid #E0E0E0;
		border-radius: 16rpx;
		padding: 0 24rpx;
		font-size: 28rpx;
		color: #333333;
		background: #FFFFFF;
		margin-bottom: 32rpx;
		box-sizing: border-box;
		outline: none;
		display: block;
		position: relative;
	}
	
	.input-field:focus {
		border-color: #667eea;
		border-width: 2rpx;
		border-style: solid;
	}
	
	.modal-actions {
		display: flex;
		gap: 24rpx;
	}
	
	.modal-btn {
		flex: 1;
		height: 80rpx;
		border-radius: 16rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.cancel-btn {
		background: #F5F5F5;
	}
	
	.confirm-btn {
		background: linear-gradient(135deg, #667eea, #764ba2);
	}
	
	.btn-text {
		font-size: 28rpx;
		font-weight: 500;
	}
	
	.cancel-btn .btn-text {
		color: #666666;
	}
	
	.confirm-btn .btn-text {
		color: #FFFFFF;
	}
	
	/* 底部指示器 */
	.home-indicator {
		height: 8rpx;
		background-color: rgba(255, 255, 255, 0.6);
		border-radius: 4rpx;
		margin: 32rpx auto;
		width: 120rpx;
	}
	
	/* 加载状态样式 */
	.loading-container {
		display: flex;
		justify-content: center;
		align-items: center;
		padding: 40rpx;
		background-color: #F5F5F5;
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
		background-color: #F5F5F5;
		min-height: 200rpx;
	}
	
	.error-icon {
		font-size: 60rpx;
		margin-bottom: 16rpx;
	}
	
	.error-text {
		font-size: 26rpx;
		color: #FF6B35;
		text-align: center;
		margin-bottom: 24rpx;
		line-height: 1.5;
	}
	
	.error-actions {
		display: flex;
		gap: 16rpx;
	}
	
	.retry-button {
		background-color: #667eea;
		color: #FFFFFF;
		border: none;
		border-radius: 20rpx;
		padding: 12rpx 24rpx;
		font-size: 24rpx;
		font-weight: 500;
	}
	
	.retry-button:active {
		background-color: #5a6fd8;
		transform: scale(0.95);
	}
	
	/* 响应式设计 */
	@media (max-width: 750rpx) {
		.preference-item {
			padding: 20rpx 0;
		}
		
		.item-icon {
			width: 50rpx;
			height: 50rpx;
		}
		
		.item-text {
			font-size: 26rpx;
		}
		
		.modal-content {
			width: 90vw;
		}
	}
</style>




