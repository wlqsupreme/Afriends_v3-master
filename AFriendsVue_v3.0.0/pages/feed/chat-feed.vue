<template>
	<view class="chat-feed-page">
		<!-- 头部导航 -->
		<view class="header">
			<view class="hamburger-menu" @click="openMenu">
				<view class="menu-line"></view>
				<view class="menu-line"></view>
				<view class="menu-line"></view>
			</view>
			<view class="title-container">
				<text class="page-title">万人聊天</text>
				<text class="online-count">{{ onlineCount }}人在线</text>
			</view>
			<view class="search-button" @click="goToSearch">
				<svg t="1756202042594" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="6084" width="32" height="32">
					<path d="M446.112323 177.545051c137.567677 0.219798 252.612525 104.59798 266.162424 241.493333 13.562828 136.895354-78.778182 261.818182-213.617777 289.008485-134.852525 27.203232-268.386263-52.156768-308.945455-183.608889s25.018182-272.252121 151.738182-325.779394A267.235556 267.235556 0 0 1 446.112323 177.545051m0-62.060607c-182.794343 0-330.989899 148.195556-330.989899 330.989899s148.195556 330.989899 330.989899 330.989899 330.989899-148.195556 330.989899-330.989899-148.195556-330.989899-330.989899-330.989899z m431.321212 793.341415a30.849293 30.849293 0 0 1-21.94101-9.102223l-157.220202-157.220202c-11.752727-12.179394-11.584646-31.534545 0.37495-43.50707 11.972525-11.972525 31.327677-12.140606 43.494141-0.37495l157.220202 157.220202a31.036768 31.036768 0 0 1 6.723232 33.810101 31.004444 31.004444 0 0 1-28.651313 19.174142z m0 0" p-id="6085" fill="#2c2c2c"></path>
				</svg>
			</view>
		</view>
		
		<!-- 聊天消息区域 -->
		<scroll-view 
			class="chat-scroll-view"
			scroll-y="true"
			:scroll-top="scrollTop"
			:scroll-with-animation="true"
			refresher-enabled="true"
			:refresher-triggered="isRefreshing"
			@refresherrefresh="onRefresh"
			@refresherrestore="onRefreshRestore"
			@refresherabort="onRefreshAbort"
		>
			<!-- 加载状态 -->
			<view class="loading-container" v-if="loading">
				<text class="loading-text">正在加载数据...</text>
			</view>
			
			<!-- 错误状态 -->
			<view class="error-container" v-if="hasError && !loading">
				<view class="error-icon">⚠️</view>
				<text class="error-text">{{ errorMessage }}</text>
				<view class="error-actions">
					<button class="retry-button" @click="loadChatFeedData">重新加载</button>
					<button class="retry-button" @click="testConnection">测试连接</button>
				</view>
			</view>
			
			<!-- 聊天消息列表 -->
			<view class="chat-messages" v-if="!hasError && !loading">
				<view 
					class="message-item" 
					v-for="(post, index) in posts" 
					:key="index"
					:class="{ 'is-ai': post.isAI }"
					@click="handlePostClick(post)"
				>
					<!-- 用户头像 -->
					<view class="message-avatar">
						<image class="avatar" :src="post.avatar || ''" mode="aspectFill"></image>
						<view class="ai-badge" v-if="post.isAI">AI</view>
					</view>
					
					<!-- 消息内容 -->
					<view class="message-content">
						<!-- 用户信息 -->
						<view class="message-header">
							<text class="username">{{ post.username }}</text>
							<text class="message-time">{{ post.timeAgo }}</text>
						</view>
						
						<!-- 小说推荐消息 -->
						<view class="novel-message" v-if="post.type === 'novel'">
							<view class="novel-card">
								<view class="novel-header">
									<text class="novel-type">📚 小说推荐</text>
									<text class="novel-rating">{{ post.novelRating || post.novelInfo?.rating }}</text>
								</view>
								<text class="novel-title">{{ post.novelTitle || post.novelInfo?.title }}</text>
								<text class="novel-desc">{{ post.novelDescription || post.novelInfo?.description }}</text>
								
								<!-- 小说封面 -->
								<view class="novel-cover-container" v-if="post.novelCover">
									<image class="novel-cover" :src="post.novelCover" mode="aspectFill"></image>
									<view class="novel-overlay">
										<text class="overlay-title">{{ post.novelTitle || post.novelInfo?.title }}</text>
									</view>
								</view>
								
								<!-- 小说标签 -->
								<view class="novel-tags" v-if="post.tags">
									<text class="tag" v-for="tag in post.tags" :key="tag">{{ tag }}</text>
								</view>
							</view>
						</view>
						
						<!-- 图文消息 -->
						<view class="image-message" v-else-if="post.type === 'image'">
							<text class="message-text" v-if="post.textContent">{{ post.textContent }}</text>
							<!-- 图片区域 -->
							<view class="message-images" v-if="post.images && post.images.length > 0">
								<image 
									v-for="(image, imgIndex) in post.images" 
									:key="imgIndex"
									class="message-image" 
									:src="image" 
									mode="aspectFill"
									@click.stop="previewImage(post.images, imgIndex)"
									@error="handleImageError(post, imgIndex)"
								></image>
							</view>
							<!-- 图片加载失败时的预留位置 -->
							<view class="image-placeholder" v-else-if="!post.hasImages">
								<view class="placeholder-content">
									<text class="placeholder-icon">🖼️</text>
									<text class="placeholder-text">图片内容暂时无法显示</text>
								</view>
							</view>
						</view>
						
						<!-- 纯文字消息 -->
						<view class="text-message" v-else-if="post.type === 'text'">
							<text class="message-text">{{ post.textContent }}</text>
						</view>
						
						<!-- 互动指标 -->
						<view class="message-actions">
							<view class="action-item" @click.stop="handleLike(post)">
								<text class="action-icon" :class="{ 'active': post.isLiked }">👍</text>
								<text class="action-count" :class="{ 'active': post.isLiked }">{{ formatCount(post.likes) }}</text>
							</view>
							<view class="action-item" @click.stop="handleDislike(post)">
								<text class="action-icon" :class="{ 'active': post.isDisliked }">👎</text>
								<text class="action-count" :class="{ 'active': post.isDisliked }">{{ formatCount(post.dislikes) }}</text>
							</view>
							<view class="action-item" @click.stop="handleFavorite(post)">
								<text class="action-icon" :class="{ 'active': post.isFavorited }">⭐</text>
								<text class="action-count" :class="{ 'active': post.isFavorited }">{{ formatCount(post.favorites) }}</text>
							</view>
							<view class="action-item" @click.stop="handleComment(post)">
								<text class="action-icon">💬</text>
								<text class="action-count">{{ formatCount(post.comments) }}</text>
							</view>
						</view>
					</view>
				</view>
			</view>
		</scroll-view>
		
		<!-- 底部输入区域 -->
		<view class="input-area">
			<view class="input-container">
				<view class="input-box" @click="goToPublish">
					<input 
						class="message-input" 
						type="text" 
						placeholder="说点什么..." 
						:value="inputMessage"
						readonly
						:focus="inputFocus"
					/>
					<view class="input-actions">
						<view class="action-btn" @click.stop="toggleImagePicker">
							<text class="action-icon">📷</text>
						</view>
						<view class="action-btn" @click.stop="goToPublish">
							<text class="action-icon">📤</text>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 底部导航栏 -->
		<view class="bottom-navigation">
			<view class="nav-item active" @click="goToHome">
				<text class="nav-text">首页</text>
			</view>
			<view class="nav-item" @click="goToChatList">
				<text class="nav-text">消息</text>
			</view>
			<view class="nav-item" @click="goToAIChat">
				<view class="ai-tab">
					<text class="ai-text">AI</text>
				</view>
			</view>
			<view class="nav-item" @click="goToFriendList">
				<text class="nav-text">好友</text>
			</view>
			<view class="nav-avatar" @click="goToProfile">
				<image class="avatar-small" src="" mode="aspectFill"></image>
			</view>
		</view>
		
		<!-- 左侧菜单面板 -->
		<view class="side-menu" :class="{ 'active': showSideMenu }" @click="closeSideMenu">
			<view class="menu-content" @click.stop>
				<!-- 菜单头部 -->
				<view class="menu-header">
					<view class="menu-close" @click="closeSideMenu">
						<text class="close-icon">×</text>
					</view>
				</view>
				
				<!-- 菜单选项列表 -->
				<view class="menu-list">
					<!-- 我的AI -->
					<view class="menu-section">
						<view class="menu-item" @click="goToMyAI">
							<text class="menu-text">我的AI</text>
						</view>
					</view>
					
					<!-- 我的认证、喜恶、成就 -->
					<view class="menu-section">
						<view class="menu-item" @click="goToMyCertification">
							<text class="menu-text">我的认证</text>
						</view>
						<view class="menu-item" @click="goToMyPreferences">
							<text class="menu-text">我的喜恶</text>
						</view>
						<view class="menu-item" @click="goToMyAchievements">
							<text class="menu-text">我的成就</text>
						</view>
					</view>
					
					<!-- 历史评论 -->
					<view class="menu-section">
						<view class="menu-item" @click="goToHistoryComments">
							<text class="menu-text">互动消息</text>
						</view>
					</view>
					
					<!-- 购买记录、钱包 -->
					<view class="menu-section">
						<view class="menu-item" @click="goToPurchaseRecord">
							<text class="menu-text">购买记录</text>
						</view>
						<view class="menu-item" @click="goToWallet">
							<text class="menu-text">钱包</text>
						</view>
					</view>
					
					<!-- 设置和客服 -->
					<view class="menu-section">
						<view class="menu-item" @click="goToSettings">
							<text class="menu-text">设置</text>
						</view>
						<view class="menu-item" @click="goToCustomerService">
							<text class="menu-text">客服与服务</text>
						</view>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		name: 'ChatFeedPage',
		data() {
			return {
				isRefreshing: false,
				showSideMenu: false,
				scrollTop: 0,
				inputMessage: '',
				inputFocus: false,
				onlineCount: 12345, // 模拟在线人数
				posts: [], // 从后端API获取的真实数据
				userId: 1000100, // 默认用户ID，预留方法以后传参
				loading: false,
				errorMessage: '', // 错误信息
				hasError: false // 是否有错误
			}
		},
		created() {
			// 页面创建时加载数据
			console.log('页面创建，开始加载数据...');
			this.testConnection();
			this.loadChatFeedData();
			this.loadOnlineCount();
		},
		methods: {
			// 加载聊天动态数据
			async loadChatFeedData() {
				try {
					console.log('=== 开始加载聊天动态数据 ===');
					console.log('用户ID:', this.userId);
					console.log('请求URL:', `http://localhost:8888/api/chat-feed/data?userId=${this.userId}`);
					this.loading = true;
					this.hasError = false;
					this.errorMessage = '';
					
					const response = await uni.request({
						url: `http://localhost:8888/api/chat-feed/data?userId=${this.userId}`,
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('=== API响应详情 ===');
					console.log('状态码:', response.statusCode);
					console.log('响应数据:', response.data);
					console.log('响应头:', response.header);
					
					if (response.statusCode === 200) {
						// 检查响应数据结构
						console.log('响应数据结构检查:');
						console.log('- response.data:', response.data);
						console.log('- response.data.success:', response.data.success);
						console.log('- response.data.data:', response.data.data);
						
						// 处理不同的响应格式
						let feedData = [];
						if (response.data && response.data.data && Array.isArray(response.data.data)) {
							// 标准格式：{success: true, data: [...]}
							feedData = response.data.data;
						} else if (Array.isArray(response.data)) {
							// 直接返回数组
							feedData = response.data;
						} else {
							console.error('无法识别的响应格式:', response.data);
							this.hasError = true;
							this.errorMessage = '响应数据格式错误';
							return;
						}
						
						this.posts = feedData;
						console.log('成功加载聊天动态数据，共', this.posts.length, '条');
						console.log('第一条数据示例:', this.posts.length > 0 ? this.posts[0] : '无数据');
						
						if (this.posts.length === 0) {
							this.hasError = true;
							this.errorMessage = '暂无推荐数据，请稍后再试';
							console.log('没有获取到任何推荐数据');
						} else {
							// 显示成功提示
							uni.showToast({
								title: `加载了${this.posts.length}条动态`,
								icon: 'success',
								duration: 2000
							});
						}
					} else {
						console.error('加载聊天动态数据失败:', response.data.message);
						this.hasError = true;
						this.errorMessage = response.data.message || '加载数据失败';
						uni.showToast({
							title: '加载数据失败',
							icon: 'error'
						});
					}
				} catch (error) {
					console.error('加载聊天动态数据异常:', error);
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
			
			// 加载在线人数
			async loadOnlineCount() {
				try {
					console.log('开始加载在线人数');
					
					const response = await uni.request({
						url: 'http://localhost:8888/api/chat-feed/online-count',
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('在线人数API响应:', response);
					
					if (response.statusCode === 200 && response.data.success) {
						this.onlineCount = response.data.onlineCount;
						console.log('成功加载在线人数:', this.onlineCount);
					} else {
						console.error('加载在线人数失败:', response.data.message);
					}
				} catch (error) {
					console.error('加载在线人数异常:', error);
				}
			},
			
			// 搜索功能
			goToSearch() {
				uni.navigateTo({
					url: '/pages/feed/search'
				});
			},
			
			// 帖子点击处理
			handlePostClick(post) {
				if (post.type === 'novel') {
					// 跳转到小说详情页，传递完整的小说信息
					const novelParams = {
						id: post.id,
						userId: this.userId, // 传递用户ID
						title: post.novelInfo ? post.novelInfo.title : post.title,
						author: post.author,
						rating: post.rating,
						reviewCount: post.reviewCount,
						readerCount: post.readerCount,
						wordCount: post.wordCount,
						updateDays: post.updateDays,
						tags: post.tags ? post.tags.join(',') : '',
						synopsis: post.synopsis
					};
					
					// 构建查询字符串
					const queryString = Object.keys(novelParams)
						.map(key => `${key}=${encodeURIComponent(novelParams[key])}`)
						.join('&');
					
					console.log('跳转到小说详情页，参数:', novelParams);
					uni.navigateTo({
						url: `/pages/feed/novel-detail?${queryString}`
					});
				} else {
					// 跳转到帖子详情页（review-detail.vue）
					console.log('跳转到帖子详情页，参数:', { postId: post.id, postType: post.type, userId: this.userId });
					uni.navigateTo({
						url: `/pages/feed/review-detail?postId=${post.id}&postType=${post.type}&userId=${this.userId}`
					});
				}
			},
			
			// 图片预览
			previewImage(images, current) {
				uni.previewImage({
					urls: images,
					current: current
				});
			},
			
			// 处理图片加载错误
			handleImageError(post, imgIndex) {
				console.log('图片加载失败:', post.images[imgIndex]);
				// 将失败的图片URL替换为占位符
				if (post.images && post.images[imgIndex]) {
					post.images[imgIndex] = '/static/image-error.png';
				}
			},
			
			// 格式化数字显示
			formatCount(count) {
				if (count >= 10000) {
					return (count / 10000).toFixed(1) + 'w';
				}
				return count.toString();
			},
			
			// 点赞处理
			async handleLike(post) {
				try {
					console.log('处理点赞操作:', post);
					
					// 防止重复点击
					if (post._isProcessing) {
						console.log('正在处理中，忽略重复点击');
						return;
					}
					post._isProcessing = true;
					
					// 确定内容类型：1=文字, 2=图片, 3=小说
					let contentType = 1; // 默认文字
					if (post.type === 'image') {
						contentType = 2;
					} else if (post.type === 'novel') {
						contentType = 3;
					}
					
					console.log('发送点赞请求:', {
						userId: this.userId,
						contentId: post.id,
						contentType: contentType
					});
					
					const response = await uni.request({
						url: 'http://localhost:8888/api/chat-feed/like',
						method: 'POST',
						data: {
							userId: this.userId,
							contentId: post.id,
							contentType: contentType
						},
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('点赞API响应:', response);
					
					if (response.statusCode === 200 && response.data.success) {
						// 更新前端状态
						const wasLiked = post.isLiked;
						post.isLiked = response.data.isActive;
						
						if (post.isLiked && !wasLiked) {
							// 新点赞
							post.likes++;
							// 如果之前点踩了，取消点踩
							if (post.isDisliked) {
								post.isDisliked = false;
								post.dislikes = Math.max(0, post.dislikes - 1);
							}
						} else if (!post.isLiked && wasLiked) {
							// 取消点赞
							post.likes = Math.max(0, post.likes - 1);
						}
						
						uni.showToast({
							title: response.data.message,
							icon: 'success',
							duration: 1500
						});
						
						console.log('点赞状态更新成功:', {
							isLiked: post.isLiked,
							likes: post.likes,
							dislikes: post.dislikes
						});
					} else {
						console.error('点赞操作失败:', response.data);
						uni.showToast({
							title: response.data.message || '点赞失败',
							icon: 'error'
						});
					}
				} catch (error) {
					console.error('点赞操作异常:', error);
					uni.showToast({
						title: '网络错误',
						icon: 'error'
					});
				} finally {
					// 重置处理状态
					post._isProcessing = false;
				}
			},
			
			// 踩处理
			async handleDislike(post) {
				try {
					console.log('处理点踩操作:', post);
					
					// 防止重复点击
					if (post._isProcessing) {
						console.log('正在处理中，忽略重复点击');
						return;
					}
					post._isProcessing = true;
					
					// 确定内容类型：1=文字, 2=图片, 3=小说
					let contentType = 1; // 默认文字
					if (post.type === 'image') {
						contentType = 2;
					} else if (post.type === 'novel') {
						contentType = 3;
					}
					
					console.log('发送点踩请求:', {
						userId: this.userId,
						contentId: post.id,
						contentType: contentType
					});
					
					const response = await uni.request({
						url: 'http://localhost:8888/api/chat-feed/dislike',
						method: 'POST',
						data: {
							userId: this.userId,
							contentId: post.id,
							contentType: contentType
						},
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('点踩API响应:', response);
					
					if (response.statusCode === 200 && response.data.success) {
						// 更新前端状态
						const wasDisliked = post.isDisliked;
						post.isDisliked = response.data.isActive;
						
						if (post.isDisliked && !wasDisliked) {
							// 新点踩
							post.dislikes++;
							// 如果之前点赞了，取消点赞
							if (post.isLiked) {
								post.isLiked = false;
								post.likes = Math.max(0, post.likes - 1);
							}
						} else if (!post.isDisliked && wasDisliked) {
							// 取消点踩
							post.dislikes = Math.max(0, post.dislikes - 1);
						}
						
						uni.showToast({
							title: response.data.message,
							icon: 'success',
							duration: 1500
						});
						
						console.log('点踩状态更新成功:', {
							isDisliked: post.isDisliked,
							likes: post.likes,
							dislikes: post.dislikes
						});
					} else {
						console.error('点踩操作失败:', response.data);
						uni.showToast({
							title: response.data.message || '点踩失败',
							icon: 'error'
						});
					}
				} catch (error) {
					console.error('点踩操作异常:', error);
					uni.showToast({
						title: '网络错误',
						icon: 'error'
					});
				} finally {
					// 重置处理状态
					post._isProcessing = false;
				}
			},
			
			// 收藏处理
			async handleFavorite(post) {
				try {
					console.log('处理收藏操作:', post);
					
					// 防止重复点击
					if (post._isProcessing) {
						console.log('正在处理中，忽略重复点击');
						return;
					}
					post._isProcessing = true;
					
					// 确定内容类型：1=文字, 2=图片, 3=小说
					let contentType = 1; // 默认文字
					if (post.type === 'image') {
						contentType = 2;
					} else if (post.type === 'novel') {
						contentType = 3;
					}
					
					console.log('发送收藏请求:', {
						userId: this.userId,
						contentId: post.id,
						contentType: contentType
					});
					
					const response = await uni.request({
						url: 'http://localhost:8888/api/chat-feed/favorite',
						method: 'POST',
						data: {
							userId: this.userId,
							contentId: post.id,
							contentType: contentType
						},
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('收藏API响应:', response);
					
					if (response.statusCode === 200 && response.data.success) {
						// 更新前端状态
						const wasFavorited = post.isFavorited;
						post.isFavorited = response.data.isActive;
						
						if (post.isFavorited && !wasFavorited) {
							// 新收藏
							post.favorites++;
						} else if (!post.isFavorited && wasFavorited) {
							// 取消收藏
							post.favorites = Math.max(0, post.favorites - 1);
						}
						
						uni.showToast({
							title: response.data.message,
							icon: 'success',
							duration: 1500
						});
						
						console.log('收藏状态更新成功:', {
							isFavorited: post.isFavorited,
							favorites: post.favorites
						});
					} else {
						console.error('收藏操作失败:', response.data);
						uni.showToast({
							title: response.data.message || '收藏失败',
							icon: 'error'
						});
					}
				} catch (error) {
					console.error('收藏操作异常:', error);
					uni.showToast({
						title: '网络错误',
						icon: 'error'
					});
				} finally {
					// 重置处理状态
					post._isProcessing = false;
				}
			},
			
			// 评论处理
			handleComment(post) {
				// 根据帖子类型跳转到相应的详情页
				if (post.type === 'novel') {
					// 跳转到小说详情页
					const novelParams = {
						id: post.id,
						title: post.novelInfo.title,
						author: post.author,
						rating: post.rating,
						reviewCount: post.reviewCount,
						readerCount: post.readerCount,
						wordCount: post.wordCount,
						updateDays: post.updateDays,
						tags: post.tags.join(','),
						synopsis: post.synopsis
					};
					
					const queryString = Object.keys(novelParams)
						.map(key => `${key}=${encodeURIComponent(novelParams[key])}`)
						.join('&');
					
					uni.navigateTo({
						url: `/pages/feed/novel-detail?${queryString}`
					});
				} else {
					// 跳转到帖子详情页（review-detail.vue）
					uni.navigateTo({
						url: `/pages/feed/review-detail?postId=${post.id}&postType=${post.type}&userId=${this.userId}`
					});
				}
			},
			
			// 跳转到发布页面
			goToPublish() {
				console.log('跳转到发布页面，用户ID:', this.userId);
				uni.navigateTo({
					url: `/pages/publish/publish?userId=${this.userId}`
				});
			},
			
			// 发送消息
			sendMessage() {
				if (this.inputMessage.trim()) {
					// 这里可以添加发送消息的逻辑
					console.log('发送消息:', this.inputMessage);
					this.inputMessage = '';
					this.inputFocus = false;
					
					// 滚动到底部
					this.scrollToBottom();
				}
			},
			
			// 切换图片选择器
			toggleImagePicker() {
				// 这里可以添加图片选择逻辑
				console.log('选择图片');
			},
			
			// 滚动到底部
			scrollToBottom() {
				this.$nextTick(() => {
					this.scrollTop = 99999;
				});
			},
			
			// 导航方法
			goToHome() {
				// 已在首页，无需跳转
			},
			goToChatList() {
				uni.navigateTo({
					url: '/pages/chat/chat-list'
				});
			},
			goToAIChat() {
				uni.navigateTo({
					url: '/pages/ai/ai-chat'
				});
			},
			goToFriendList() {
				uni.navigateTo({
					url: '/pages/chat/friend-list'
				});
			},
			goToProfile() {
				uni.navigateTo({
					url: '/pages/feed/user-profile'
				});
			},
			
			// 侧边菜单相关方法
			openMenu() {
				this.showSideMenu = true;
			},
			closeSideMenu() {
				this.showSideMenu = false;
			},
			goToMyAI() {
				uni.navigateTo({
					url: '/pages/ai/ai'
				});
			},
			goToMyCertification() {
				uni.navigateTo({
					url: '/pages/verification/official-certification'
				});
			},
			goToMyPreferences() {
				uni.navigateTo({
					url: `/pages/feed/likes-dislikes?userId=${this.userId}`
				});
			},
			goToMyAchievements() {
				uni.navigateTo({
					url: '/pages/feed/achievements'
				});
			},
			goToHistoryComments() {
				uni.navigateTo({
					url: `/pages/feed/interaction-messages?userId=${this.userId}`
				});
			},
			goToPurchaseRecord() {
				uni.navigateTo({
					url: `/pages/wallet/purchase-record?userId=${this.userId}`
				});
			},
			goToWallet() {
				uni.navigateTo({
					url: `/pages/wallet/wallet?userId=${this.userId}`
				});
			},
			goToSettings() {
				uni.navigateTo({
					url: '/pages/settings/settings'
				});
			},
			goToCustomerService() {
				uni.navigateTo({
					url: '/pages/chat/chat?friendName=客服'
				});
			},
			
			// 刷新相关方法
			async onRefresh(e) {
				console.log('开始刷新聊天动态数据');
				this.isRefreshing = true;
				this.hasError = false;
				this.errorMessage = '';
				
				try {
					// 先刷新缓存，再重新加载数据
					await this.refreshCache();
					await this.loadChatFeedData();
					await this.loadOnlineCount();
					
					this.onRefreshComplete();
				} catch (error) {
					console.error('刷新数据失败:', error);
					this.hasError = true;
					this.errorMessage = '刷新数据失败，请重试';
					this.onRefreshComplete();
				}
			},
			
			// 刷新缓存
			async refreshCache() {
				try {
					console.log('开始刷新后端缓存...');
					const response = await uni.request({
						url: 'http://localhost:8888/api/chat-feed/refresh-cache',
						method: 'POST',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('缓存刷新响应:', response);
					
					if (response.statusCode === 200 && response.data.success) {
						console.log('后端缓存刷新成功');
					} else {
						console.error('后端缓存刷新失败:', response.data.message);
					}
				} catch (error) {
					console.error('刷新缓存异常:', error);
					// 缓存刷新失败不影响主流程，继续执行
				}
			},
			
			onRefreshComplete() {
				this.isRefreshing = false;
				if (!this.hasError) {
					uni.showToast({
						title: '刷新成功',
						icon: 'success'
					});
				}
			},
			
			onRefreshAbort() {
				console.log('刷新被中断');
			},
			
			onRefreshRestore() {
				console.log('刷新器复位');
			},
			
			// 测试后端连接
			async testConnection() {
				try {
					console.log('=== 测试后端连接 ===');
					const response = await uni.request({
						url: 'http://localhost:8888/api/chat-feed/test',
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
			}
		}
	}
</script>

<style>
	.chat-feed-page {
		height: 100vh;
		background-color: #F5F5F5;
		display: flex;
		flex-direction: column;
		overflow: hidden;
	}
	
	/* 头部导航 */
	.header {
		height: 88rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 0 32rpx;
		background-color: #FFFFFF;
		border-bottom: 1rpx solid #E0E0E0;
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		z-index: 1000;
	}
	
	.hamburger-menu {
		display: flex;
		flex-direction: column;
		gap: 6rpx;
	}
	
	.menu-line {
		width: 32rpx;
		height: 4rpx;
		background-color: #000000;
		border-radius: 2rpx;
	}
	
	.title-container {
		display: flex;
		flex-direction: column;
		align-items: center;
		flex: 1;
	}
	
	.page-title {
		font-size: 32rpx;
		color: #333333;
		font-weight: 600;
	}
	
	.online-count {
		font-size: 20rpx;
		color: #999999;
		margin-top: 4rpx;
	}
	
	.search-button {
		width: 48rpx;
		height: 48rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.search-button svg {
		width: 32rpx;
		height: 32rpx;
	}
	
	/* 聊天消息区域 */
	.chat-scroll-view {
		flex: 1;
		height: calc(100vh - 88rpx - 120rpx - 120rpx);
		margin-top: 88rpx;
		margin-bottom: 240rpx;
		background-color: #F5F5F5;
	}
	
	.chat-messages {
		padding: 24rpx 32rpx;
	}
	
	.message-item {
		display: flex;
		margin-bottom: 32rpx;
		align-items: flex-start;
		gap: 16rpx;
	}
	
	.message-item.is-ai .message-content {
		background-color: #FFFFFF;
		border-radius: 16rpx;
		padding: 24rpx;
		border: 1rpx solid #E0E0E0;
	}
	
	.message-item:not(.is-ai) .message-content {
		background-color: #FFFFFF;
		border-radius: 16rpx;
		padding: 24rpx;
		border: 1rpx solid #E0E0E0;
	}
	
	.message-avatar {
		position: relative;
		flex-shrink: 0;
	}
	
	.avatar {
		width: 80rpx;
		height: 80rpx;
		border-radius: 40rpx;
		background-color: #F0F0F0;
	}
	
	.ai-badge {
		position: absolute;
		top: -8rpx;
		right: -8rpx;
		width: 32rpx;
		height: 32rpx;
		background: linear-gradient(135deg, #FF69B4, #FF8E53);
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 18rpx;
		color: #FFFFFF;
		font-weight: 600;
	}
	
	.message-content {
		flex: 1;
		min-width: 0;
	}
	
	.message-header {
		display: flex;
		align-items: center;
		gap: 16rpx;
		margin-bottom: 12rpx;
	}
	
	.username {
		font-size: 28rpx;
		color: #333333;
		font-weight: 600;
	}
	
	.message-time {
		font-size: 20rpx;
		color: #999999;
	}
	
	/* 小说消息样式 */
	.novel-message {
		width: 100%;
	}
	
	.novel-card {
		background-color: transparent;
		border-radius: 0;
		padding: 0;
		border: none;
	}
	
	.novel-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 16rpx;
	}
	
	.novel-type {
		font-size: 24rpx;
		color: #FF6B35;
		font-weight: 600;
	}
	
	.novel-rating {
		font-size: 24rpx;
		color: #FFD700;
		font-weight: 600;
	}
	
	.novel-title {
		font-size: 32rpx;
		color: #333333;
		font-weight: 700;
		margin-bottom: 12rpx;
		display: block;
		line-height: 1.4;
	}
	
	.novel-desc {
		font-size: 26rpx;
		color: #666666;
		margin-bottom: 16rpx;
		display: block;
		line-height: 1.5;
	}
	
	.novel-cover-container {
		position: relative;
		margin-bottom: 16rpx;
		border-radius: 12rpx;
		overflow: hidden;
	}
	
	.novel-cover {
		width: 100%;
		height: 300rpx;
		background-color: #F0F0F0;
	}
	
	.novel-overlay {
		position: absolute;
		top: 16rpx;
		left: 16rpx;
		background: linear-gradient(135deg, #FFD700, #FFA500);
		padding: 12rpx 20rpx;
		border-radius: 8rpx;
	}
	
	.overlay-title {
		font-size: 24rpx;
		color: #FFFFFF;
		font-weight: 700;
	}
	
	.novel-tags {
		display: flex;
		flex-wrap: wrap;
		gap: 8rpx;
	}
	
	.tag {
		font-size: 20rpx;
		color: #FF69B4;
		background-color: #FFF0F5;
		padding: 4rpx 12rpx;
		border-radius: 12rpx;
	}
	
	/* 图文消息样式 */
	.image-message {
		background-color: transparent;
		border-radius: 0;
		padding: 0;
		border: none;
	}
	
	.message-text {
		font-size: 28rpx;
		color: #333333;
		line-height: 1.6;
		margin-bottom: 16rpx;
		display: block;
	}
	
	.message-images {
		display: flex;
		gap: 12rpx;
		flex-wrap: wrap;
	}
	
	.message-image {
		width: 200rpx;
		height: 150rpx;
		border-radius: 8rpx;
		background-color: #F0F0F0;
	}
	
	/* 图片占位符样式 */
	.image-placeholder {
		margin-top: 16rpx;
		border-radius: 8rpx;
		background-color: #F8F8F8;
		border: 2rpx dashed #E0E0E0;
	}
	
	.placeholder-content {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 40rpx 20rpx;
		min-height: 200rpx;
	}
	
	.placeholder-icon {
		font-size: 48rpx;
		margin-bottom: 16rpx;
		opacity: 0.6;
	}
	
	.placeholder-text {
		font-size: 24rpx;
		color: #999999;
		text-align: center;
	}
	
	/* 纯文字消息样式 */
	.text-message {
		background-color: transparent;
		border-radius: 0;
		padding: 0;
		border: none;
	}
	
	/* 互动指标 */
	.message-actions {
		display: flex;
		gap: 24rpx;
		margin-top: 16rpx;
	}
	
	.action-item {
		display: flex;
		align-items: center;
		gap: 8rpx;
	}
	
	.action-icon {
		font-size: 28rpx;
		color: #666666;
		transition: all 0.2s ease;
	}
	
	.action-count {
		font-size: 22rpx;
		color: #666666;
	}
	
	.action-icon.active {
		color: #FFD700;
		transform: scale(1.1);
	}
	
	.action-count.active {
		color: #FFD700;
		font-weight: 600;
	}
	
	/* 底部输入区域 */
	.input-area {
		position: fixed;
		bottom: 120rpx;
		left: 0;
		right: 0;
		background-color: #FFFFFF;
		border-top: 1rpx solid #E0E0E0;
		padding: 16rpx 32rpx;
		z-index: 100;
	}
	
	.input-container {
		display: flex;
		align-items: center;
		gap: 16rpx;
	}
	
	.input-box {
		flex: 1;
		display: flex;
		align-items: center;
		background-color: #F5F5F5;
		border-radius: 24rpx;
		padding: 12rpx 20rpx;
		gap: 16rpx;
	}
	
	.message-input {
		flex: 1;
		font-size: 28rpx;
		color: #333333;
		background-color: transparent;
		border: none;
		outline: none;
	}
	
	.input-actions {
		display: flex;
		gap: 12rpx;
	}
	
	.action-btn {
		width: 48rpx;
		height: 48rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		background-color: #FF69B4;
		border-radius: 50%;
	}
	
	.action-btn .action-icon {
		font-size: 24rpx;
		color: #FFFFFF;
	}
	
	/* 底部导航栏 */
	.bottom-navigation {
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		height: 120rpx;
		background-color: #FFFFFF;
		border-top: 1rpx solid #E0E0E0;
		display: flex;
		align-items: center;
		justify-content: space-around;
		padding: 0 32rpx;
		z-index: 100;
	}
	
	.nav-item {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 8rpx;
		cursor: pointer;
		padding: 16rpx;
		border-radius: 12rpx;
		transition: all 0.2s ease;
	}
	
	.nav-item:active {
		background-color: #F5F5F5;
		transform: scale(0.95);
	}
	
	.nav-text {
		font-size: 24rpx;
		color: #666666;
		font-weight: 500;
	}
	
	.nav-item.active .nav-text {
		color: #FF69B4;
		font-weight: 600;
	}
	
	.ai-tab {
		width: 48rpx;
		height: 48rpx;
		background: linear-gradient(135deg, #FF69B4, #FF8E53);
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.ai-text {
		font-size: 20rpx;
		color: #FFFFFF;
		font-weight: 600;
	}
	
	.nav-avatar {
		width: 48rpx;
		height: 48rpx;
	}
	
	.avatar-small {
		width: 100%;
		height: 100%;
		border-radius: 50%;
		background: linear-gradient(135deg, #FFA500, #FF8C00);
	}
	
	/* 左侧菜单面板 */
	.side-menu {
		position: fixed;
		top: 0;
		left: -100%;
		width: 100%;
		height: 100vh;
		background-color: rgba(0, 0, 0, 0.5);
		z-index: 9999;
		transition: left 0.3s ease;
	}
	
	.side-menu.active {
		left: 0;
	}
	
	.menu-content {
		position: absolute;
		top: 0;
		left: 0;
		width: 600rpx;
		height: 100vh;
		background-color: #fff;
		box-shadow: 4rpx 0 20rpx rgba(0, 0, 0, 0.1);
		display: flex;
		flex-direction: column;
	}
	
	/* 菜单头部 */
	.menu-header {
		display: flex;
		justify-content: flex-end;
		padding: 40rpx;
		border-bottom: 1rpx solid #f0f0f0;
	}
	
	.menu-close {
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
	
	/* 菜单列表 */
	.menu-list {
		flex: 1;
		padding: 0;
		background-color: #f8f8f8;
	}
	
	.menu-section {
		margin-bottom: 20rpx;
		background-color: #fff;
	}
	
	.menu-item {
		display: flex;
		align-items: center;
		padding: 30rpx 40rpx;
		border-bottom: 1rpx solid #f0f0f0;
		cursor: pointer;
		transition: background-color 0.2s ease;
	}
	
	.menu-item:last-child {
		border-bottom: none;
	}
	
	.menu-item:active {
		background-color: #f5f5f5;
	}
	
	.menu-text {
		font-size: 30rpx;
		color: #333;
		font-weight: 500;
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
		padding: 80rpx 40rpx;
		background-color: #F5F5F5;
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
</style>
