<template>
	<view class="interaction-messages-page">
		<!-- 头部导航 -->
		<view class="header">
			<view class="back-button" @click="goBack">
				<svg class="back-icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="32" height="32">
					<path d="M407.01 512l286.008-286.008a35.84 35.84 0 0 0-50.683-50.683L330.982 486.656a35.84 35.84 0 0 0 0 50.683L642.34 848.69a35.84 35.84 0 0 0 50.683-50.683L407.009 512z" fill="#666666"></path>
				</svg>
			</view>
			<view class="title-section" @click="toggleFilter">
				<text class="page-title">{{ getCurrentFilterName() }}</text>
				<view class="filter-arrow" :class="{ expanded: filterExpanded }">
					<svg class="arrow-icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="32" height="32">
						<path d="M832 288l-320 448-320-448z" fill="#020202"></path>
					</svg>
				</view>
			</view>
			<view class="check-button" @click="markAllAsRead">
				<view class="check-icon">✓</view>
			</view>
		</view>
		
		<!-- 消息分类筛选 -->
		<view class="filter-section" v-if="filterExpanded">
			<view class="filter-item" 
				v-for="(filter, index) in messageFilters" 
				:key="index"
				:class="{ active: currentFilter === filter.type }"
				@click="selectFilter(filter.type)">
				<view class="filter-icon">
					<text class="icon-text">{{ filter.icon }}</text>
				</view>
				<text class="filter-text">{{ filter.name }}</text>
				<view class="filter-check" v-if="currentFilter === filter.type">
					<text class="check-text">✓</text>
				</view>
			</view>
		</view>
		
		<!-- 消息列表 -->
		<view class="message-list">
			<!-- 加载状态 -->
			<view class="loading-container" v-if="loading">
				<text class="loading-text">正在加载消息...</text>
			</view>
			
			<!-- 错误状态 -->
			<view class="error-container" v-if="hasError && !loading">
				<view class="error-icon">⚠️</view>
				<text class="error-text">{{ errorMessage }}</text>
				<view class="error-actions">
					<button class="retry-button" @click="loadInteractionMessages">重新加载</button>
				</view>
			</view>
			
			<!-- 消息列表 -->
			<view v-if="!hasError && !loading">
				<view 
					class="message-item" 
					v-for="(message, index) in messages" 
					:key="index"
					:class="{ 'unread': !message.isRead }"
					@click="handleMessageClick(message)"
				>
					<view class="avatar-section">
						<image class="user-avatar" :src="message.fromUserAvatar || ''" mode="aspectFill"></image>
						<!-- 根据消息类型显示不同的徽章 -->
						<view class="action-badge" v-if="message.actionType === 'like'">❤️</view>
						<view class="action-badge" v-else-if="message.actionType === 'favorite'">⭐</view>
						<view class="action-badge" v-else-if="message.actionType === 'received-comment'">💬</view>
						<view class="action-badge" v-else-if="message.actionType === 'sent-comment'">💬</view>
					</view>
					<view class="message-content">
						<view class="user-info">
							<text class="username">{{ message.fromUsername || message.toUsername }}</text>
							<text class="relationship" v-if="message.relationship">{{ message.relationship }}</text>
						</view>
						<text class="message-text">{{ message.messageText }}</text>
						<text class="comment-text" v-if="message.commentText">{{ message.commentText }}</text>
						<text class="message-date">{{ message.timeAgo }}</text>
					</view>
					<view class="content-thumbnail" v-if="message.contentThumbnail">
						<image class="thumbnail-image" :src="message.contentThumbnail" mode="aspectFill"></image>
					</view>
					<!-- 未读标识 -->
					<view class="unread-dot" v-if="!message.isRead"></view>
				</view>
				
				<!-- 空状态 -->
				<view class="empty-container" v-if="messages.length === 0">
					<view class="empty-icon">📭</view>
					<text class="empty-text">暂无{{ getCurrentFilterName() }}消息</text>
				</view>
			</view>
		</view>
		
		<!-- 底部指示器 -->
		<view class="home-indicator"></view>
	</view>
</template>

<script>
	export default {
		name: 'InteractionMessagesPage',
		data() {
			return {
				filterExpanded: false,
				currentFilter: 'all',
				userId: 1000100, // 默认用户ID，从chat-feed页面传递过来
				loading: false,
				hasError: false,
				errorMessage: '',
				messages: [], // 从后端API获取的真实数据
				messageFilters: [
					{ type: 'all', name: '全部消息', icon: '〰️' },
					{ type: 'likes', name: '赞与收藏', icon: '❤️' },
					/* { type: 'mentions', name: '提及', icon: '@' }, */
					{ type: 'received-comments', name: '收到的评论', icon: '💬' },
					{ type: 'sent-comments', name: '发出的评论', icon: '💬' },
					/* { type: 'received-danmu', name: '收到的弹幕', icon: '弹' },
					{ type: 'sent-danmu', name: '发出的弹幕', icon: '弹' } */
				]
			}
		},
		onLoad(options) {
			// 接收从chat-feed页面传递的用户ID
			if (options.userId) {
				this.userId = parseInt(options.userId);
				console.log('接收到用户ID:', this.userId);
			}
			// 页面加载时获取数据
			this.loadInteractionMessages();
		},
		methods: {
			// 加载互动消息数据
			async loadInteractionMessages() {
				try {
					console.log('=== 开始加载互动消息数据 ===');
					console.log('用户ID:', this.userId);
					console.log('筛选类型:', this.currentFilter);
					console.log('请求URL:', `http://localhost:8888/api/interaction-messages/data?userId=${this.userId}&filterType=${this.currentFilter}`);
					
					this.loading = true;
					this.hasError = false;
					this.errorMessage = '';
					
					const response = await uni.request({
						url: `http://localhost:8888/api/interaction-messages/data?userId=${this.userId}&filterType=${this.currentFilter}`,
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('=== API响应详情 ===');
					console.log('状态码:', response.statusCode);
					console.log('响应数据:', response.data);
					
					if (response.statusCode === 200) {
						// 检查响应数据结构
						console.log('响应数据结构检查:');
						console.log('- response.data:', response.data);
						console.log('- response.data.success:', response.data.success);
						console.log('- response.data.data:', response.data.data);
						
						// 处理不同的响应格式
						let messagesData = [];
						if (response.data && response.data.data && Array.isArray(response.data.data)) {
							// 标准格式：{success: true, data: [...]}
							messagesData = response.data.data;
						} else if (Array.isArray(response.data)) {
							// 直接返回数组
							messagesData = response.data;
						} else {
							console.error('无法识别的响应格式:', response.data);
							this.hasError = true;
							this.errorMessage = '响应数据格式错误';
							return;
						}
						
						this.messages = messagesData;
						console.log('成功加载互动消息数据，共', this.messages.length, '条');
						console.log('第一条数据示例:', this.messages.length > 0 ? this.messages[0] : '无数据');
						
						if (this.messages.length === 0) {
							this.hasError = true;
							this.errorMessage = '暂无互动消息，请稍后再试';
							console.log('没有获取到任何互动消息数据');
						} else {
							// 显示成功提示
							uni.showToast({
								title: `加载了${this.messages.length}条消息`,
								icon: 'success',
								duration: 2000
							});
						}
					} else {
						console.error('加载互动消息数据失败:', response.data.message);
						this.hasError = true;
						this.errorMessage = response.data.message || '加载数据失败';
						uni.showToast({
							title: '加载数据失败',
							icon: 'error'
						});
					}
				} catch (error) {
					console.error('加载互动消息数据异常:', error);
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
			
			goBack() {
				uni.navigateBack();
			},
			toggleFilter() {
				this.filterExpanded = !this.filterExpanded;
			},
			selectFilter(filterType) {
				this.currentFilter = filterType;
				this.filterExpanded = false;
				// 根据筛选类型重新加载数据
				this.loadInteractionMessages();
				uni.showToast({
					title: `已切换到${this.getMessageFilterName(filterType)}`,
					icon: 'none'
				});
			},
			getMessageFilterName(filterType) {
				const filter = this.messageFilters.find(f => f.type === filterType);
				return filter ? filter.name : '';
			},
			getCurrentFilterName() {
				return this.getMessageFilterName(this.currentFilter);
			},
			async markAllAsRead() {
				uni.showModal({
					title: '确认操作',
					content: '确定要将所有消息标记为已读吗？',
					success: async (res) => {
						if (res.confirm) {
							try {
								console.log('开始标记所有消息为已读');
								
								const response = await uni.request({
									url: `http://localhost:8888/api/interaction-messages/mark-all-read?userId=${this.userId}`,
									method: 'POST',
									header: {
										'Content-Type': 'application/json'
									}
								});
								
								console.log('标记已读API响应:', response);
								
								if (response.statusCode === 200 && response.data.success) {
									uni.showToast({
										title: response.data.message,
										icon: 'success'
									});
									// 重新加载数据以更新状态
									this.loadInteractionMessages();
								} else {
									console.error('标记已读操作失败:', response.data.message);
									uni.showToast({
										title: response.data.message || '标记已读失败',
										icon: 'error'
									});
								}
							} catch (error) {
								console.error('标记已读操作异常:', error);
								uni.showToast({
									title: '网络错误',
									icon: 'error'
								});
							}
						}
					}
				});
			},
			
			// 处理消息点击
			handleMessageClick(message) {
				console.log('点击消息:', message);
				
				// 根据消息类型跳转到相应的详情页
				if (message.contentType) {
					let url = '';
					
					switch (message.contentType) {
						case 'novel':
							// 跳转到小说详情页
							url = `/pages/feed/novel-detail?id=${message.contentId}`;
							break;
						case 'image':
							// 跳转到图文详情页
							url = `/pages/feed/post-detail?postId=${message.contentId}`;
							break;
						case 'text':
							// 跳转到文字详情页
							url = `/pages/feed/post-detail?postId=${message.contentId}`;
							break;
						case 'video':
							// 跳转到视频详情页
							url = `/pages/feed/post-detail?postId=${message.contentId}`;
							break;
						default:
							// 默认跳转到通用详情页
							url = `/pages/feed/post-detail?postId=${message.contentId}`;
							break;
					}
					
					if (url) {
						uni.navigateTo({
							url: url
						});
					}
				} else {
					// 如果没有内容类型，显示消息详情
					uni.showModal({
						title: '消息详情',
						content: message.messageText + (message.commentText ? '\n\n评论内容：' + message.commentText : ''),
						showCancel: false
					});
				}
			}
		}
	}
</script>

<style>
	.interaction-messages-page {
		min-height: 100vh;
		background-color: #FFFFFF;
		display: flex;
		flex-direction: column;
		padding-top: 120rpx; /* 为固定头部留出空间 */
	}
	
	/* 头部导航 */
	.header {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		z-index: 1000;
		height: 88rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 32rpx;
		background-color: #FFFFFF;
		border-bottom: 1rpx solid #F0F0F0;
	}
	
	.back-button {
		display: flex;
		align-items: center;
		justify-content: center;
		width: 48rpx;
		height: 48rpx;
		cursor: pointer;
		border-radius: 24rpx;
		transition: background-color 0.2s ease;
	}
	
	.back-button:active {
		background-color: rgba(0, 0, 0, 0.05);
	}
	
	.back-icon {
		width: 32rpx;
		height: 32rpx;
		transition: transform 0.2s ease;
	}
	
	.back-button:active .back-icon {
		transform: scale(0.9);
	}
	
	.unread-count {
		font-size: 24rpx;
		color: #000000;
		font-weight: 600;
	}
	
	.title-section {
		display: flex;
		align-items: center;
		gap: 16rpx;
	}
	
	.page-title {
		font-size: 32rpx;
		color: #000000;
		font-weight: 600;
	}
	
	.filter-arrow {
		width: 32rpx;
		height: 32rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		transition: transform 0.3s ease;
	}
	
	.filter-arrow.expanded {
		transform: rotate(180deg);
	}
	
	.arrow-icon {
		width: 32rpx;
		height: 32rpx;
		transition: transform 0.3s ease;
	}
	
	.check-button {
		width: 48rpx;
		height: 48rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		background-color: #F44336;
		border-radius: 24rpx;
	}
	
	.check-icon {
		font-size: 24rpx;
		color: #FFFFFF;
		font-weight: 600;
	}
	
	/* 消息分类筛选 */
	.filter-section {
		background-color: #F8F8F8;
		padding: 24rpx 32rpx;
		border-bottom: 1rpx solid #F0F0F0;
	}
	
	.filter-item {
		display: flex;
		align-items: center;
		gap: 16rpx;
		padding: 20rpx 0;
		border-bottom: 1rpx solid rgba(0, 0, 0, 0.05);
		transition: background-color 0.3s ease;
	}
	
	.filter-item:last-child {
		border-bottom: none;
	}
	
	.filter-item.active {
		background-color: rgba(0, 0, 0, 0.05);
	}
	
	.filter-item:active {
		background-color: rgba(0, 0, 0, 0.1);
	}
	
	.filter-icon {
		width: 40rpx;
		height: 40rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		flex-shrink: 0;
	}
	
	.icon-text {
		font-size: 24rpx;
	}
	
	.filter-text {
		flex: 1;
		font-size: 28rpx;
		color: #000000;
		font-weight: 500;
	}
	
	.filter-check {
		width: 32rpx;
		height: 32rpx;
		background-color: #F44336;
		border-radius: 16rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.check-text {
		font-size: 20rpx;
		color: #FFFFFF;
		font-weight: 600;
	}
	
	/* 消息列表 */
	.message-list {
		flex: 1;
		padding: 0 32rpx;
	}
	
	.message-item {
		display: flex;
		align-items: flex-start;
		gap: 24rpx;
		padding: 32rpx 0;
		border-bottom: 1rpx solid #F0F0F0;
	}
	
	.message-item:last-child {
		border-bottom: none;
	}
	
	.avatar-section {
		position: relative;
		flex-shrink: 0;
	}
	
	.user-avatar {
		width: 80rpx;
		height: 80rpx;
		border-radius: 40rpx;
		background-color: #F5F5F5;
	}
	
	.star-badge, .heart-badge {
		position: absolute;
		bottom: -4rpx;
		left: -4rpx;
		width: 32rpx;
		height: 32rpx;
		background-color: #FFFFFF;
		border-radius: 16rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
	}
	
	.star-badge {
		color: #FFD700;
	}
	
	.heart-badge {
		color: #F44336;
	}
	
	.message-content {
		flex: 1;
		display: flex;
		flex-direction: column;
		gap: 8rpx;
	}
	
	.user-info {
		display: flex;
		align-items: center;
		gap: 16rpx;
		flex-wrap: wrap;
	}
	
	.username {
		font-size: 28rpx;
		color: #000000;
		font-weight: 600;
	}
	
	.user-badge {
		display: flex;
		align-items: center;
		gap: 4rpx;
		padding: 4rpx 8rpx;
		background-color: #FFF3E0;
		border-radius: 12rpx;
	}
	
	.flame-icon {
		font-size: 20rpx;
	}
	
	.badge-text {
		font-size: 20rpx;
		color: #FF9800;
		font-weight: 600;
	}
	
	.relationship {
		font-size: 22rpx;
		color: #666666;
		padding: 4rpx 12rpx;
		background-color: #F5F5F5;
		border-radius: 12rpx;
	}
	
	.message-text {
		font-size: 26rpx;
		color: #333333;
		line-height: 1.4;
	}
	
	.message-date {
		font-size: 22rpx;
		color: #999999;
	}
	
	.content-thumbnail {
		flex-shrink: 0;
	}
	
	.thumbnail-image {
		width: 120rpx;
		height: 80rpx;
		border-radius: 12rpx;
		background-color: #F5F5F5;
	}
	
	/* 底部指示器 */
	.home-indicator {
		height: 8rpx;
		background-color: #000000;
		border-radius: 4rpx;
		margin: 32rpx auto;
		width: 120rpx;
	}
	
	/* 未读消息样式 */
	.message-item.unread {
		background-color: #F8F9FF;
		border-left: 4rpx solid #FF69B4;
	}
	
	.unread-dot {
		position: absolute;
		top: 20rpx;
		right: 20rpx;
		width: 16rpx;
		height: 16rpx;
		background-color: #FF69B4;
		border-radius: 50%;
	}
	
	/* 动作徽章样式 */
	.action-badge {
		position: absolute;
		bottom: -4rpx;
		left: -4rpx;
		width: 32rpx;
		height: 32rpx;
		background-color: #FFFFFF;
		border-radius: 16rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
		font-size: 20rpx;
	}
	
	/* 评论文字样式 */
	.comment-text {
		font-size: 24rpx;
		color: #666666;
		line-height: 1.4;
		margin-top: 8rpx;
		display: block;
		font-style: italic;
	}
	
	/* 加载状态样式 */
	.loading-container {
		display: flex;
		justify-content: center;
		align-items: center;
		padding: 80rpx 40rpx;
		background-color: #FFFFFF;
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
		background-color: #FFFFFF;
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
	
	/* 空状态样式 */
	.empty-container {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 120rpx 40rpx;
		background-color: #FFFFFF;
	}
	
	.empty-icon {
		font-size: 120rpx;
		margin-bottom: 32rpx;
		opacity: 0.6;
	}
	
	.empty-text {
		font-size: 28rpx;
		color: #999999;
		text-align: center;
	}
	
	/* 响应式设计 */
	@media (max-width: 750rpx) {
		.message-item {
			padding: 24rpx 0;
		}
		
		.user-avatar {
			width: 70rpx;
			height: 70rpx;
		}
		
		.thumbnail-image {
			width: 100rpx;
			height: 70rpx;
		}
		
		.username {
			font-size: 26rpx;
		}
		
		.message-text {
			font-size: 24rpx;
		}
		
		.comment-text {
			font-size: 22rpx;
		}
	}
</style>




