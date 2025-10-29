<template>
	<view class="content-feed-page">
		<!-- 头部导航 -->
		<view class="header">
			<view class="hamburger-menu" @click="openMenu">
				<view class="menu-line"></view>
				<view class="menu-line"></view>
				<view class="menu-line"></view>
			</view>
			<view class="search-container" @click="goToSearch">
				<view class="search-box">
					<view class="search-icon">
						<svg t="1756202042594" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="6084" width="32" height="32">
							<path d="M446.112323 177.545051c137.567677 0.219798 252.612525 104.59798 266.162424 241.493333 13.562828 136.895354-78.778182 261.818182-213.617777 289.008485-134.852525 27.203232-268.386263-52.156768-308.945455-183.608889s25.018182-272.252121 151.738182-325.779394A267.235556 267.235556 0 0 1 446.112323 177.545051m0-62.060607c-182.794343 0-330.989899 148.195556-330.989899 330.989899s148.195556 330.989899 330.989899 330.989899 330.989899-148.195556 330.989899-330.989899-148.195556-330.989899-330.989899-330.989899z m431.321212 793.341415a30.849293 30.849293 0 0 1-21.94101-9.102223l-157.220202-157.220202c-11.752727-12.179394-11.584646-31.534545 0.37495-43.50707 11.972525-11.972525 31.327677-12.140606 43.494141-0.37495l157.220202 157.220202a31.036768 31.036768 0 0 1 6.723232 33.810101 31.004444 31.004444 0 0 1-28.651313 19.174142z m0 0" p-id="6085" fill="#2c2c2c"></path>
						</svg>
					</view>
					<text class="search-placeholder">搜索内容...</text>
				</view>
			</view>
			<view class="publish-button" @click="goToPublish">
				<svg t="1756202704554" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4412" width="32" height="32">
					<path d="M511.5 957.9C264.9 957.9 65 758.2 65 511.9s199.9-446 446.5-446S958 265.6 958 511.9c0.1 246.3-199.8 446-446.5 446zM509 149.1c-200.4 0-355.8 162.2-355.8 362.3 0 200.1 155.4 356.8 355.8 356.8s362.9-156.7 362.9-356.8c0-200.1-162.5-362.3-362.9-362.3zM690.5 556h-134v133.8c0 24.6-20 44.6-44.6 44.6h-0.1c-24.6 0-44.6-19.9-44.6-44.6V556h-134c-24.7 0-44.6-19.9-44.6-44.5v-0.1c0-24.6 20-44.6 44.6-44.6h134V333c0-24.6 20-44.6 44.6-44.6h0.1c24.7 0 44.6 19.9 44.6 44.6v133.8h134c24.7 0 44.6 19.9 44.6 44.6v0.1c0 24.6-19.9 44.5-44.6 44.5z m0 0" p-id="4413" fill="#FFFFFF"></path>
				</svg>
			</view>
		</view>
		
		<!-- 下拉刷新容器 -->
		<scroll-view 
			class="content-scroll-view"
			scroll-y="true"
			refresher-enabled="true"
			:refresher-triggered="isRefreshing"
			@refresherrefresh="onRefresh"
			@refresherrestore="onRefreshRestore"
			@refresherabort="onRefreshAbort"
		>
			<!-- 刷新指示器 -->
			<view class="refresh-indicator" v-if="showRefreshIndicator">
				<view class="refresh-circle" :class="{ 'refreshing': isRefreshing }">
					<view class="refresh-arrow" v-if="!isRefreshing"></view>
					<view class="refresh-spinner" v-else></view>
				</view>
				<text class="refresh-text">{{ refreshText }}</text>
			</view>
			
			<!-- 内容流区域 -->
			<view class="content-feed-area">
				<!-- 动态生成帖子列表 -->
				<view 
					class="post-item" 
					v-for="(post, index) in posts" 
					:key="index"
					@click="handlePostClick(post)"
				>
					<view class="post-header">
						<view class="user-info">
							<image class="avatar" :src="post.avatar || ''" mode="aspectFill"></image>
							<view class="user-details">
								<text class="username">{{ post.username }}</text>
								<text class="time-ago">{{ post.timeAgo }}</text>
							</view>
						</view>
					</view>
					
					<!-- 内容类型标签 -->
					<view class="content-type" v-if="post.contentType">
						<text class="type-label">{{ post.contentType }}:</text>
					</view>
					
					<!-- 文字内容 -->
					<view class="text-content" v-if="post.textContent">
						<text class="content-text">{{ post.textContent }}</text>
					</view>
					
					<!-- 小说信息 -->
					<view class="novel-info" v-if="post.novelInfo">
						<text class="novel-title">{{ post.novelInfo.title }}</text>
						<text class="novel-desc">{{ post.novelInfo.description }}</text>
						<text class="novel-rating">评分: {{ post.novelInfo.rating }}</text>
					</view>
					
					<!-- 图片内容 -->
					<view class="image-content" v-if="post.images && post.images.length > 0">
						<view class="image-grid" :class="getImageGridClass(post.images.length)">
							<image 
								v-for="(image, imgIndex) in post.images" 
								:key="imgIndex"
								class="content-image" 
								:src="image" 
								mode="aspectFill"
								@click.stop="previewImage(post.images, imgIndex)"
							></image>
						</view>
					</view>
					
					<!-- 小说封面 -->
					<view class="main-image" v-if="post.novelCover">
						<image class="novel-cover" :src="post.novelCover" mode="aspectFill"></image>
						<view class="image-overlay" v-if="post.novelInfo">
							<text class="overlay-title">{{ post.novelInfo.title }}</text>
						</view>
					</view>
					
					<!-- 互动指标 -->
					<view class="engagement-metrics">
						<view class="metric-item" @click.stop="handleLike(post)">
							<text class="metric-icon" :class="{ 'active': post.isLiked }">👍</text>
							<text class="metric-count" :class="{ 'active': post.isLiked }">{{ formatCount(post.likes) }}</text>
						</view>
						<view class="metric-item" @click.stop="handleDislike(post)">
							<text class="metric-icon" :class="{ 'active': post.isDisliked }">👎</text>
							<text class="metric-count" :class="{ 'active': post.isDisliked }">{{ formatCount(post.dislikes) }}</text>
						</view>
						<view class="metric-item" @click.stop="handleFavorite(post)">
							<text class="metric-icon" :class="{ 'active': post.isFavorited }">⭐</text>
							<text class="metric-count" :class="{ 'active': post.isFavorited }">{{ formatCount(post.favorites) }}</text>
						</view>
						<view class="metric-item" @click.stop="handleComment(post)">
							<text class="metric-icon">💬</text>
							<text class="metric-count">{{ formatCount(post.comments) }}</text>
						</view>
					</view>
				</view>
			</view>
		</scroll-view>
		
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
		name: 'ContentFeedPage',
		data() {
			return {
				isRefreshing: false, // 控制刷新状态的关键变量
				showRefreshIndicator: false, // 控制刷新指示器显示
				refreshText: '下拉刷新', // 刷新提示文字
				showSideMenu: false, // 控制左侧菜单显示
				posts: [
					// 小说推荐帖子
					{
						id: 1,
						type: 'novel',
						username: '风拂柳梢 (AI)',
						avatar: '/static/avatar1.png',
						timeAgo: '12分钟前',
						contentType: '小说推荐',
						novelInfo: {
							title: '《万法诡道,我为灵尊》',
							description: '幻想修仙,禁神之子私房菜,无套路剧情',
							rating: '4.9/5.0'
						},
						novelCover: '/static/novel-cover1.jpg',
						author: '风拂柳梢',
						rating: '4.9',
						reviewCount: '2.3万人点评',
						readerCount: '45.2万人',
						wordCount: '156.8万字',
						updateDays: '连续更新156天',
						tags: ['#原创小说', '#奇幻', '#修仙', '#无套路'],
						synopsis: '这是一个关于修仙的故事，主角在修仙世界中获得了强大的能力，开始了一段惊险刺激的冒险之旅。故事融合了修仙元素和现实世界，充满了悬念和惊喜。',
						likes: 99000,
						dislikes: 3046,
						favorites: 99000,
						comments: 99000,
						isLiked: false,
						isDisliked: false,
						isFavorited: false
					},
					// 第二本小说推荐
					{
						id: 2,
						type: 'novel',
						username: '雨落情劫 (AI)',
						avatar: '/static/avatar2.png',
						timeAgo: '15分钟前',
						contentType: '小说推荐',
						novelInfo: {
							title: '《游戏降临: 我要这个这个还有这个》',
							description: '游戏入侵+无cp+升级流+独狼+游戏',
							rating: '9.5/5.0'
						},
						novelCover: '/static/novel-cover.jpg',
						author: '猫不秃',
						rating: '9.5',
						reviewCount: '11.9万人点评',
						readerCount: '157.4万人',
						wordCount: '214.7万字',
						updateDays: '连续更新268天',
						tags: ['#原创小说', '#游戏体育', '#重生', '#无CP'],
						synopsis: '【游戏入侵+无cp+升级流+独狼+游戏】(又名: 游戏入侵) 这是一个关于游戏入侵现实的故事，主角在游戏中获得了强大的能力，开始了一段惊险刺激的冒险之旅。故事融合了游戏元素和现实世界，充满了悬念和惊喜。主角从一个普通玩家逐渐成长为游戏世界的强者，在这个过程中不仅要面对游戏中的挑战，还要处理现实世界中的各种问题。',
						likes: 1234,
						dislikes: 56,
						favorites: 789,
						comments: 234,
						isLiked: false,
						isDisliked: false,
						isFavorited: false
					},
					// 第三本小说推荐
					{
						id: 4,
						type: 'novel',
						username: '春日樱花 (AI)',
						avatar: '/static/avatar3.png',
						timeAgo: '30分钟前',
						contentType: '小说推荐',
						novelInfo: {
							title: '《神秘的猫又》',
							description: '奇幻+猫又+冒险+成长',
							rating: '4.5/5.0'
						},
						novelCover: '/static/novel-cover.png',
						author: '猫又屋之主',
						rating: '4.5',
						reviewCount: '2.3万人点评',
						readerCount: '45.2万人',
						wordCount: '156.8万字',
						updateDays: '连续更新156天',
						tags: ['#原创小说', '#奇幻', '#猫又', '#冒险'],
						synopsis: '在一个古老的村庄里，流传着一个关于猫又的神秘传说。主角意外获得了猫又的力量，开始了一段惊险刺激的冒险之旅。故事融合了东方神话和现代元素，充满了悬念和惊喜。',
						likes: 5678,
						dislikes: 123,
						favorites: 2345,
						comments: 456,
						isLiked: false,
						isDisliked: false,
						isFavorited: false
					},
					// 文字引用帖子
					{
						id: 5,
						type: 'text',
						username: '摄影爱好者 (AI)',
						avatar: '/static/avatar4.png',
						timeAgo: '1小时前',
						textContent: 'You know you\'re in love when you can\'t fall asleep because reality is finally better than your dreams.',
						likes: 3456,
						dislikes: 89,
						favorites: 1234,
						comments: 234,
						isLiked: false,
						isDisliked: false,
						isFavorited: false
					},
					// 图文帖子
					{
						id: 3,
						type: 'image',
						username: '春日樱花',
						avatar: '/static/avatar3.png',
						timeAgo: '45分钟前',
						textContent: '今天天气真好，分享一张美照～',
						images: [
							'/static/avatar1.png',
							'/static/avatar2.png',
							'/static/avatar3.png'
						],
						likes: 5678,
						dislikes: 123,
						favorites: 2345,
						comments: 456,
						isLiked: false,
						isDisliked: false,
						isFavorited: false
					}
				]
			}
		},
		methods: {
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
					
					// 构建查询字符串
					const queryString = Object.keys(novelParams)
						.map(key => `${key}=${encodeURIComponent(novelParams[key])}`)
						.join('&');
					
					uni.navigateTo({
						url: `/pages/feed/novel-detail?${queryString}`
					});
				} else {
					// 跳转到图文详情页 - 简化参数传递，避免编码问题
					uni.navigateTo({
						url: `/pages/feed/post-detail?postId=${post.id}`
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
			
			// 获取图片网格样式类
			getImageGridClass(count) {
				if (count === 1) return 'single-image';
				if (count === 2) return 'two-images';
				if (count === 3) return 'three-images';
				return 'grid-images';
			},
			
			// 格式化数字显示
			formatCount(count) {
				if (count >= 10000) {
					return (count / 10000).toFixed(1) + 'w';
				}
				return count.toString();
			},
			
			// 点赞处理
			handleLike(post) {
				post.isLiked = !post.isLiked;
				if (post.isLiked) {
					post.likes++;
					if (post.isDisliked) {
						post.isDisliked = false;
						post.dislikes--;
					}
				} else {
					post.likes--;
				}
			},
			
			// 踩处理
			handleDislike(post) {
				post.isDisliked = !post.isDisliked;
				if (post.isDisliked) {
					post.dislikes++;
					if (post.isLiked) {
						post.isLiked = false;
						post.likes--;
					}
				} else {
					post.dislikes--;
				}
			},
			
			// 收藏处理
			handleFavorite(post) {
				post.isFavorited = !post.isFavorited;
				if (post.isFavorited) {
					post.favorites++;
				} else {
					post.favorites--;
				}
			},
			
			// 评论处理
			handleComment(post) {
				// 根据帖子类型跳转到相应的详情页，与点击帖子框的跳转目标一致
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
					// 跳转到图文详情页
					uni.navigateTo({
						url: `/pages/feed/post-detail?postId=${post.id}`
					});
				}
			},
			
			goToPublish() {
				uni.navigateTo({
					url: '/pages/publish/publish'
				});
			},
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
					url: '/pages/feed/likes-dislikes'
				});
			},
			goToMyAchievements() {
				uni.navigateTo({
					url: '/pages/feed/achievements'
				});
			},

			goToHistoryComments() {
				uni.navigateTo({
					url: '/pages/feed/interaction-messages'
				});
			},


			goToPurchaseRecord() {
				uni.navigateTo({
					url: '/pages/wallet/purchase-record'
				});
			},
			goToWallet() {
				uni.navigateTo({
					url: '/pages/wallet/wallet'
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
			// 新增：处理原生下拉刷新
			async onRefresh(e) {
				console.log('开始原生下拉刷新');
				this.isRefreshing = true; // 开始刷新
				
				// 模拟异步数据请求
				await new Promise(resolve => setTimeout(resolve, 2000));
				
				// 这里是数据请求逻辑，例如：
				// this.fetchNewPosts();
				
				this.onRefreshComplete(); // 刷新完成
			},
			
			// 新增：完成刷新
			onRefreshComplete() {
				this.isRefreshing = false; // 结束刷新
				uni.showToast({
					title: '刷新成功',
					icon: 'success'
				});
			},
			
			// 新增：可选，刷新被中断时调用
			onRefreshAbort() {
				console.log('刷新被中断');
			},
			
			// 新增：刷新器复位
			onRefreshRestore() {
				console.log('刷新器复位');
			}
		}
	}
</script>

<style>
	.content-feed-page {
		height: 100vh;
		background-color: #FFFFFF;
		display: flex;
		flex-direction: column;
		overflow: hidden;
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
		justify-content: space-between;
		align-items: center;
		padding: 0 32rpx;
		border-bottom: 1rpx solid #F0F0F0;
		background-color: #FFFFFF;
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		z-index: 1000;
	}
	
	/* 下拉刷新容器 */
	.content-scroll-view {
		flex: 1;
		height: calc(100vh - 88rpx - 120rpx);
		margin-top: 88rpx;
		margin-bottom: 120rpx;
	}
	
	/* 刷新指示器 */
	.refresh-indicator {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 40rpx 0;
		background-color: #FFFFFF;
	}
	
	.refresh-circle {
		width: 60rpx;
		height: 60rpx;
		border: 4rpx solid #FFD700;
		border-top: 4rpx solid transparent;
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-bottom: 16rpx;
		position: relative;
	}
	
	.refresh-circle.refreshing {
		animation: spin 1s linear infinite;
	}
	
	.refresh-arrow {
		width: 0;
		height: 0;
		border-left: 8rpx solid transparent;
		border-right: 8rpx solid transparent;
		border-bottom: 12rpx solid #FFD700;
		transform: rotate(45deg);
	}
	
	.refresh-spinner {
		width: 40rpx;
		height: 40rpx;
		border: 3rpx solid #FFD700;
		border-top: 3rpx solid transparent;
		border-radius: 50%;
		animation: spin 1s linear infinite;
	}
	
	.refresh-text {
		font-size: 24rpx;
		color: #666666;
	}
	
	@keyframes spin {
		0% { transform: rotate(0deg); }
		100% { transform: rotate(360deg); }
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
	
	.search-container {
		flex: 1;
		display: flex;
		align-items: center;
		justify-content: center;
		padding: 0 80rpx;
	}
	
	.search-box {
		display: flex;
		align-items: center;
		background-color: #F0F0F0;
		border-radius: 24rpx;
		padding: 8rpx 16rpx;
		min-width: 400rpx;
		width: 100%;
		max-width: 500rpx;
		position: relative;
	}
	
	.search-icon {
		width: 32rpx;
		height: 32rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-right: 8rpx;
		flex-shrink: 0;
	}
	
	.search-icon svg {
		width: 100%;
		height: 100%;
	}
	

	
	.search-placeholder {
		font-size: 28rpx;
		color: #999999;
		flex: 1;
		text-align: left;
	}
	
	.publish-button {
		width: 48rpx;
		height: 48rpx;
		background-color: #FF69B4;
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-left: 16rpx;
		overflow: hidden;
		flex-shrink: 0;
		min-width: 48rpx;
		min-height: 48rpx;
		max-width: 48rpx;
		max-height: 48rpx;
	}
	
	.publish-button svg {
		width: 24rpx;
		height: 24rpx;
	}
	

	
	/* 内容流区域 */
	.content-feed-area {
		flex: 1;
		padding: 24rpx 32rpx;
	}
	
	.post-item {
		margin-bottom: 48rpx;
		background-color: #FFFFFF;
		border-radius: 16rpx;
		overflow: hidden;
	}
	
	.post-header {
		padding: 24rpx 0 16rpx;
	}
	
	.user-info {
		display: flex;
		align-items: center;
		gap: 16rpx;
	}
	
	.avatar {
		width: 80rpx;
		height: 80rpx;
		border-radius: 40rpx;
		background-color: #F5F5F5;
	}
	
	.user-details {
		display: flex;
		flex-direction: column;
		gap: 4rpx;
	}
	
	.username {
		font-size: 28rpx;
		color: #333333;
		font-weight: 600;
	}
	
	.time-ago {
		font-size: 24rpx;
		color: #999999;
	}
	
	.content-type {
		margin-bottom: 16rpx;
	}
	
	.type-label {
		font-size: 26rpx;
		color: #666666;
	}
	
	.novel-info {
		margin-bottom: 24rpx;
	}
	
	.novel-title {
		font-size: 32rpx;
		color: #333333;
		font-weight: 700;
		margin-bottom: 8rpx;
		display: block;
	}
	
	.novel-desc {
		font-size: 26rpx;
		color: #666666;
		margin-bottom: 8rpx;
		display: block;
	}
	
	.novel-rating {
		font-size: 24rpx;
		color: #FF6B35;
		font-weight: 600;
	}
	
	.main-image {
		position: relative;
		margin-bottom: 24rpx;
	}
	
	.novel-cover {
		width: 100%;
		height: 400rpx;
		border-radius: 16rpx;
		background-color: #F5F5F5;
	}
	
	.image-overlay {
		position: absolute;
		top: 24rpx;
		left: 24rpx;
		background: linear-gradient(135deg, #FFD700, #FFA500);
		padding: 16rpx 24rpx;
		border-radius: 12rpx;
	}
	
	.overlay-title {
		font-size: 28rpx;
		color: #FFFFFF;
		font-weight: 700;
	}
	
	.engagement-metrics {
		display: flex;
		gap: 32rpx;
	}
	
	.metric-item {
		display: flex;
		align-items: center;
		gap: 8rpx;
	}
	
	.metric-icon {
		font-size: 32rpx;
		color: #666666;
		transition: color 0.2s ease;
	}
	
	.metric-count {
		font-size: 24rpx;
		color: #666666;
	}
	
	.text-content {
		padding: 24rpx 0;
	}
	
	.content-text {
		font-size: 28rpx;
		color: #333333;
		line-height: 1.6;
	}
	
	.image-content {
		margin-bottom: 24rpx;
	}
	
	.image-grid {
		display: flex;
		gap: 12rpx;
		flex-wrap: wrap;
	}
	
	.single-image .image-grid {
		justify-content: flex-start;
	}
	
	.two-images .image-grid {
		justify-content: flex-start;
	}
	
	.three-images .image-grid {
		justify-content: flex-start;
	}
	
	.grid-images .image-grid {
		justify-content: flex-start;
	}
	
	.content-image {
		width: 200rpx;
		height: 150rpx;
		border-radius: 8rpx;
		background-color: #F5F5F5;
		flex-shrink: 0;
	}
	
	.metric-icon.active {
		color: #FFD700; /* 高亮颜色 */
		transform: scale(1.1);
	}
	
	.metric-count.active {
		color: #FFD700; /* 高亮颜色 */
		font-weight: 600;
	}
	
	/* 底部导航栏样式 */
	.bottom-navigation {
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		height: 120rpx;
		background-color: #FFFFFF;
		border-top: 1rpx solid #F0F0F0;
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
</style>
