<template>
	<view class="search-page">
		<!-- 状态栏 -->
		<!-- <view class="status-bar">
			<view class="status-icons">
				<view class="signal"></view>
				<view class="wifi"></view>
				<view class="battery"></view>
			</view>
		</view> -->
		
		<!-- 搜索栏区域 -->
		<view class="search-bar-area">
			<view class="search-input-field">
				<input 
					class="search-input" 
					type="text" 
					v-model="searchKeyword" 
					placeholder="搜索" 
					@input="onSearchInput"
					@focus="onSearchFocus"
				/>
			</view>
			<view class="cancel-btn" @click="goBack">
				<text class="cancel-text">取消</text>
			</view>
		</view>
		
		<!-- 搜索结果区域 -->
		<view v-if="searchKeyword && searchResults.length > 0" class="search-results-area">
			<view class="results-header">
				<text class="results-count">找到 {{ searchResults.length }} 条相关记录</text>
			</view>
			<scroll-view class="results-list" scroll-y>
				<view class="result-item" v-for="(result, index) in searchResults" :key="index" @click="openChat(result)">
					<view class="result-avatar">
						<image class="avatar-image" :src="result.avatarUrl" mode="aspectFill"></image>
					</view>
					<view class="result-content">
						<view class="result-header">
							<text class="result-name">{{ result.friendName }}</text>
							<text class="result-time">{{ formatTime(result.lastMessageTime) }}</text>
						</view>
						<view class="result-message">
							<text class="message-text">{{ result.lastMessage }}</text>
						</view>
					</view>
				</view>
			</scroll-view>
		</view>

		<!-- 内容过滤建议区域 -->
		<view v-else-if="!searchKeyword" class="content-filter-area">
			<text class="filter-prompt"></text>
			<view class="category-labels">
				<!-- 日期和图片与视频搜索按钮已移除 -->
			</view>
		</view>

		<!-- 无搜索结果 -->
		<view v-else-if="searchKeyword && searchResults.length === 0" class="no-results-area">
			<view class="no-results-icon">🔍</view>
			<text class="no-results-text">未找到相关聊天记录</text>
			<text class="no-results-desc">试试其他关键词</text>
		</view>
		
		
		<!-- 底部指示器 -->
		<view class="home-indicator"></view>
	</view>
</template>

<script>
	export default {
		name: 'SearchPage',
		data() {
			return {
				searchKeyword: '',
				searchResults: [],
				allChatRecords: [],
				loading: false,
				// 日期搜索相关
				showDateSearchPanel: false,
				startDate: '',
				endDate: '',
				// 媒体搜索相关
				showMediaSearchPanel: false,
				currentMediaType: 'all'
			}
		},
		onLoad() {
			this.loadChatRecords();
			this.initDateRange();
		},
		methods: {
			// 搜索输入处理
			onSearchInput() {
				console.log('搜索关键词:', this.searchKeyword);
				this.performSearch();
			},

			onSearchFocus() {
				console.log('搜索框获得焦点');
			},

			// 返回上一页
			goBack() {
				uni.navigateBack();
			},

			// 显示日期搜索面板
			showDateSearch() {
				this.showDateSearchPanel = true;
				this.showMediaSearchPanel = false;
				this.searchKeyword = '';
				this.searchResults = [];
			},

			// 隐藏日期搜索面板
			hideDateSearch() {
				this.showDateSearchPanel = false;
			},

			// 显示媒体搜索面板
			showMediaSearch() {
				this.showMediaSearchPanel = true;
				this.showDateSearchPanel = false;
				this.searchKeyword = '';
				this.searchResults = [];
			},

			// 隐藏媒体搜索面板
			hideMediaSearch() {
				this.showMediaSearchPanel = false;
			},

			// 初始化日期范围（默认为今天）
			initDateRange() {
				const today = new Date();
				const todayStr = this.formatDateForPicker(today);
				this.startDate = todayStr;
				this.endDate = todayStr;
			},

			// 格式化日期为picker需要的格式
			formatDateForPicker(date) {
				const year = date.getFullYear();
				const month = String(date.getMonth() + 1).padStart(2, '0');
				const day = String(date.getDate()).padStart(2, '0');
				return `${year}-${month}-${day}`;
			},

			// 开始日期变化
			onStartDateChange(e) {
				this.startDate = e.detail.value;
				console.log('开始日期:', this.startDate);
			},

			// 结束日期变化
			onEndDateChange(e) {
				this.endDate = e.detail.value;
				console.log('结束日期:', this.endDate);
			},

			// 按日期搜索
			searchByDate() {
				console.log('按日期搜索:', this.startDate, '到', this.endDate);
				
				if (!this.startDate || !this.endDate) {
					uni.showToast({
						title: '请选择日期范围',
						icon: 'none'
					});
					return;
				}

				const startTime = new Date(this.startDate).getTime();
				const endTime = new Date(this.endDate + ' 23:59:59').getTime();

				const filteredResults = this.allChatRecords.filter(chat => {
					const messageTime = new Date(chat.lastMessageTime || chat.createdAt || 0).getTime();
					return messageTime >= startTime && messageTime <= endTime;
				});

				// 按时间降序排列
				this.searchResults = filteredResults.sort((a, b) => {
					const timeA = new Date(a.lastMessageTime || a.createdAt || 0).getTime();
					const timeB = new Date(b.lastMessageTime || b.createdAt || 0).getTime();
					return timeB - timeA;
				});

				console.log('日期搜索结果:', this.searchResults);
				this.hideDateSearch();
			},

			// 按媒体类型搜索
			searchByMediaType(type) {
				console.log('按媒体类型搜索:', type);
				this.currentMediaType = type;
				
				// 这里可以根据实际需求实现媒体类型搜索
				// 目前先搜索包含图片或视频关键词的消息
				let keywords = [];
				if (type === 'image') {
					keywords = ['图片', '照片', 'image', 'photo', 'jpg', 'png', 'gif', '🖼️', '📷'];
				} else if (type === 'video') {
					keywords = ['视频', 'video', 'mp4', 'avi', 'mov', '🎥', '📹'];
				} else {
					keywords = ['图片', '照片', '视频', 'image', 'photo', 'video', 'jpg', 'png', 'gif', 'mp4', 'avi', 'mov', '🖼️', '📷', '🎥', '📹'];
				}

				const filteredResults = this.allChatRecords.filter(chat => {
					const message = (chat.lastMessage || '').toLowerCase();
					return keywords.some(keyword => message.includes(keyword.toLowerCase()));
				});

				// 按时间降序排列
				this.searchResults = filteredResults.sort((a, b) => {
					const timeA = new Date(a.lastMessageTime || a.createdAt || 0).getTime();
					const timeB = new Date(b.lastMessageTime || b.createdAt || 0).getTime();
					return timeB - timeA;
				});

				console.log('媒体搜索结果:', this.searchResults);
				this.hideMediaSearch();
			},

			// 加载聊天记录
			async loadChatRecords() {
				try {
					this.loading = true;
					const userId = uni.getStorageSync('userId') || 1000001;
					
					console.log('开始加载聊天记录，userId:', userId);
					
					// 同时加载聊天列表和聊天详情
					const [chatListResponse, chatDetailResponse] = await Promise.all([
						uni.request({
							url: `http://localhost:8888/api/u-entities/user-chat-list/user/${userId}`,
							method: 'GET',
							header: {
								'Content-Type': 'application/json'
							}
						}),
						uni.request({
							url: `http://localhost:8888/api/u-entities/user-chat-detail/all`,
							method: 'GET',
							header: {
								'Content-Type': 'application/json'
							}
						})
					]);

					console.log('聊天列表API响应状态码:', chatListResponse.statusCode);
					console.log('聊天详情API响应状态码:', chatDetailResponse.statusCode);

					if (chatListResponse.statusCode === 200 && chatListResponse.data && 
						chatDetailResponse.statusCode === 200 && chatDetailResponse.data) {
						
						console.log('原始聊天列表数据:', chatListResponse.data);
						console.log('原始聊天详情数据:', chatDetailResponse.data);
						
						this.allChatRecords = this.convertChatDataToSearchFormat(
							chatListResponse.data, 
							chatDetailResponse.data
						);
						
						console.log('聊天记录加载成功:', this.allChatRecords);
						console.log('转换后的聊天记录数量:', this.allChatRecords.length);
						
						// 显示每条记录的关键信息
						this.allChatRecords.forEach((chat, index) => {
							console.log(`聊天记录 ${index + 1}:`, {
								friendName: chat.friendName,
								lastMessage: chat.lastMessage,
								lastMessageTime: chat.lastMessageTime,
								friendId: chat.friendId
							});
						});
					} else {
						console.error('加载聊天记录失败:', chatListResponse, chatDetailResponse);
					}
				} catch (error) {
					console.error('加载聊天记录异常:', error);
				} finally {
					this.loading = false;
				}
			},

			// 转换聊天数据为搜索格式
			convertChatDataToSearchFormat(chatListData, chatDetailData) {
				if (!Array.isArray(chatListData) || !Array.isArray(chatDetailData)) {
					return [];
				}

				// 创建聊天详情映射，按sessionId分组
				const chatDetailMap = new Map();
				chatDetailData.forEach(detail => {
					if (detail.sessionId) {
						if (!chatDetailMap.has(detail.sessionId)) {
							chatDetailMap.set(detail.sessionId, []);
						}
						chatDetailMap.get(detail.sessionId).push(detail);
					}
				});

				// 为每个sessionId获取最新的消息
				chatDetailMap.forEach((details, sessionId) => {
					// 按时间排序，获取最新消息
					details.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
				});

				return chatListData.map(chat => {
					// 处理头像URL
					let avatarUrl = chat.avatarUrl;
					if (!avatarUrl || avatarUrl.trim() === '' || avatarUrl === 'null') {
						avatarUrl = '/static/default-avatar.png';
					}

					// 处理好友名字
					let friendName = chat.friendName;
					if (!friendName || friendName.trim() === '' || friendName === 'null') {
						friendName = '未知用户';
					}

					// 获取该会话的最新消息
					let lastMessage = chat.lastMessage || '暂无消息';
					let lastMessageTime = chat.lastMessageTime;

					if (chat.sessionId && chatDetailMap.has(chat.sessionId)) {
						const sessionDetails = chatDetailMap.get(chat.sessionId);
						if (sessionDetails.length > 0) {
							const latestDetail = sessionDetails[0];
							lastMessage = latestDetail.message || '暂无消息';
							lastMessageTime = latestDetail.createdAt;
						}
					}

					return {
						friendId: chat.friendId,
						friendName: friendName,
						avatarUrl: avatarUrl,
						lastMessage: lastMessage,
						lastMessageTime: lastMessageTime,
						createdAt: chat.createdAt,
						sessionId: chat.sessionId,
						status: chat.status
					};
				});
			},

			// 执行搜索
			performSearch() {
				if (!this.searchKeyword || this.searchKeyword.trim() === '') {
					this.searchResults = [];
					return;
				}

				const keyword = this.searchKeyword.toLowerCase().trim();
				console.log('执行搜索，关键词:', keyword);
				console.log('所有聊天记录:', this.allChatRecords);
				console.log('聊天记录数量:', this.allChatRecords.length);

				// 搜索聊天记录
				const filteredResults = this.allChatRecords.filter(chat => {
					const friendName = (chat.friendName || '').toLowerCase();
					const lastMessage = (chat.lastMessage || '').toLowerCase();
					
					console.log('检查聊天记录:', {
						friendName: friendName,
						lastMessage: lastMessage,
						keyword: keyword,
						friendNameMatch: friendName.includes(keyword),
						lastMessageMatch: lastMessage.includes(keyword)
					});
					
					return friendName.includes(keyword) || lastMessage.includes(keyword);
				});

				// 按时间降序排列（最新的在前）
				this.searchResults = filteredResults.sort((a, b) => {
					const timeA = new Date(a.lastMessageTime || a.createdAt || 0).getTime();
					const timeB = new Date(b.lastMessageTime || b.createdAt || 0).getTime();
					return timeB - timeA; // 降序排列
				});

				console.log('搜索结果:', this.searchResults);
				console.log('搜索结果数量:', this.searchResults.length);
			},

			// 格式化时间
			formatTime(timestamp) {
				if (!timestamp) return '';
				
				const date = new Date(timestamp);
				const now = new Date();
				const diff = now - date;
				
				// 如果是今天
				if (diff < 24 * 60 * 60 * 1000 && date.getDate() === now.getDate()) {
					return date.toLocaleTimeString('zh-CN', {
						hour: '2-digit',
						minute: '2-digit'
					});
				}
				
				// 如果是昨天
				const yesterday = new Date(now);
				yesterday.setDate(yesterday.getDate() - 1);
				if (date.getDate() === yesterday.getDate() && 
					date.getMonth() === yesterday.getMonth() && 
					date.getFullYear() === yesterday.getFullYear()) {
					return '昨天';
				}
				
				// 其他情况显示日期
				return date.toLocaleDateString('zh-CN', {
					month: '2-digit',
					day: '2-digit'
				});
			},

			// 打开聊天
			openChat(chat) {
				console.log('打开聊天:', chat);
				
				uni.navigateTo({
					url: `/pages/chat/chat?friendName=${encodeURIComponent(chat.friendName)}&friendId=${chat.friendId}&sessionId=${chat.sessionId}`
				});
			}
		}
	}
</script>

<style>
	.search-page {
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
	
	/* 搜索栏区域 */
	.search-bar-area {
		padding: 24rpx 32rpx;
		display: flex;
		align-items: center;
		gap: 24rpx;
	}
	
	.search-input-field {
		flex: 1;
		height: 80rpx;
		background-color: #F5F5F5;
		border-radius: 16rpx;
		display: flex;
		align-items: center;
		padding: 0 32rpx;
	}
	
	.search-input {
		flex: 1;
		font-size: 28rpx;
		color: #333333;
		background: transparent;
		border: none;
		outline: none;
		padding: 0;
		margin: 0;
	}

	.search-input::placeholder {
		color: #999999;
	}
	
	.cancel-btn {
		padding: 16rpx 0;
	}
	
	.cancel-text {
		font-size: 32rpx;
		color: #007AFF;
	}
	
	/* 内容过滤建议区域 */
	.content-filter-area {
		padding: 80rpx 32rpx 40rpx;
		flex: 1;
		display: flex;
		flex-direction: column;
		justify-content: center;
	}
	
	.filter-prompt {
		font-size: 24rpx;
		color: #999999;
		margin-bottom: 24rpx;
	}
	
	.category-labels {
		display: flex;
		flex-direction: column;
		gap: 32rpx;
		align-items: center;
	}
	
	.label-row {
		display: flex;
		gap: 40rpx;
		justify-content: center;
	}
	
	.category-label {
		padding: 16rpx 32rpx;
		color: #007AFF;
		font-size: 28rpx;
		font-weight: 500;
		cursor: pointer;
		background-color: #F0F8FF;
		border: 2rpx solid #007AFF;
		border-radius: 24rpx;
		text-align: center;
		min-width: 120rpx;
		transition: all 0.2s ease;
	}
	
	.category-label:active {
		background-color: #007AFF;
		color: #FFFFFF;
		transform: scale(0.95);
	}

	/* 搜索结果区域 */
	.search-results-area {
		flex: 1;
		display: flex;
		flex-direction: column;
		background-color: #FFFFFF;
	}

	.results-header {
		padding: 16rpx 32rpx;
		background-color: #F5F5F5;
		border-bottom: 1rpx solid #E0E0E0;
	}

	.results-count {
		font-size: 24rpx;
		color: #666666;
	}

	.results-list {
		flex: 1;
		background-color: #FFFFFF;
	}

	.result-item {
		display: flex;
		align-items: center;
		padding: 20rpx 32rpx;
		border-bottom: 1rpx solid #F0F0F0;
		cursor: pointer;
		transition: background-color 0.2s ease;
	}

	.result-item:active {
		background-color: #F5F5F5;
	}

	.result-avatar {
		margin-right: 24rpx;
		flex-shrink: 0;
	}

	.avatar-image {
		width: 80rpx;
		height: 80rpx;
		border-radius: 40rpx;
		background-color: #E5E5E5;
	}

	.result-content {
		flex: 1;
		display: flex;
		flex-direction: column;
		min-width: 0;
	}

	.result-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 8rpx;
	}

	.result-name {
		font-size: 28rpx;
		color: #333333;
		font-weight: 500;
		flex: 1;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}

	.result-time {
		font-size: 22rpx;
		color: #999999;
		margin-left: 16rpx;
		flex-shrink: 0;
	}

	.result-message {
		display: flex;
		align-items: center;
	}

	.message-text {
		font-size: 24rpx;
		color: #666666;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
		flex: 1;
	}

	/* 无搜索结果区域 */
	.no-results-area {
		flex: 1;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 80rpx 32rpx;
	}

	.no-results-icon {
		font-size: 120rpx;
		margin-bottom: 24rpx;
		opacity: 0.6;
	}

	.no-results-text {
		font-size: 32rpx;
		color: #333333;
		font-weight: 600;
		margin-bottom: 12rpx;
	}

	.no-results-desc {
		font-size: 28rpx;
		color: #666666;
		text-align: center;
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
