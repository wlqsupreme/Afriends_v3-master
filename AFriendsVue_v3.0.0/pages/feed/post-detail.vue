<template>
	<view class="post-detail-page">
		<!-- 头部导航 -->
		<view class="header">
			<view class="back-button" @click="goBack">
				<svg t="1756246262970" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="904" width="32" height="32">
					<path d="M407.01 512l286.008-286.008a35.84 35.84 0 0 0-50.683-50.683L330.982 486.656a35.84 35.84 0 0 0 0 50.683L642.34 848.69a35.84 35.84 0 0 0 50.683-50.683L407.009 512z" fill="#666666" p-id="905"></path>
				</svg>
			</view>
			<view class="header-title">动态详情</view>
			<view class="more-button" @click="showMoreOptions">
				<svg t="1756202704554" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="32" height="32">
					<path d="M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64zm0 820c-205.4 0-372-166.6-372-372s166.6-372 372-372 372 166.6 372 372-166.6 372-372 372z" fill="#333333"></path>
					<path d="M464 688a48 48 0 1 0 96 0 48 48 0 1 0-96 0zm24-112h48c4.4 0 8-3.6 8-8V296c0-4.4-3.6-8-8-8h-48c-4.4 0-8 3.6-8 8v272c0 4.4 3.6 8 8 8z" fill="#333333"></path>
				</svg>
			</view>
		</view>
		
		<!-- 内容区域 -->
		<view class="content-scroll">
			<!-- 帖子内容 -->
			<view class="post-content">
				<!-- 作者信息 -->
				<view class="author-section">
					<view class="author-info" @click="goToUserProfile">
						<image class="author-avatar" :src="postDetail.authorAvatar" mode="aspectFill"></image>
						<view class="author-details">
							<text class="author-name">{{ postDetail.authorName }}</text>
							<text class="author-time">{{ postDetail.publishTime }}</text>
						</view>
					</view>
					<view class="follow-button" v-if="!isOwnPost" @click="toggleFollow" :class="{ 'followed': postDetail.isFollowed }">
						<text class="follow-text">{{ postDetail.isFollowed ? '已关注' : '关注' }}</text>
					</view>
				</view>
				
				<!-- 文字内容 -->
				<view class="text-content" v-if="postDetail.textContent">
					<text class="content-text">{{ postDetail.textContent }}</text>
				</view>
				
				<!-- 图片内容 -->
				<view class="image-content" v-if="postDetail.images && postDetail.images.length > 0">
					<view class="image-grid" :class="getImageGridClass(postDetail.images.length)">
						<image 
							v-for="(image, index) in postDetail.images" 
							:key="index"
							class="content-image" 
							:src="image" 
							mode="aspectFill"
							@click="previewImage(postDetail.images, index)"
						></image>
					</view>
				</view>
				
				<!-- 位置信息 -->
				<view class="location-info" v-if="postDetail.location">
					<view class="location-icon">
						<svg t="1756202704554" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="24" height="24">
							<path d="M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64zm0 820c-205.4 0-372-166.6-372-372s166.6-372 372-372 372 166.6 372 372-166.6 372-372 372z" fill="#999999"></path>
							<path d="M512 336c-97.2 0-176 78.8-176 176s78.8 176 176 176 176-78.8 176-176-78.8-176-176-176zm0 288c-61.9 0-112-50.1-112-112s50.1-112 112-112 112 50.1 112 112-50.1 112-112 112z" fill="#999999"></path>
						</svg>
					</view>
					<text class="location-text">{{ postDetail.location }}</text>
				</view>
				
				<!-- 互动指标 -->
				<view class="engagement-metrics">
					<view class="metric-item" @click="handleLike">
						<text class="metric-icon" :class="{ 'active': postDetail.isLiked }">👍</text>
						<text class="metric-count" :class="{ 'active': postDetail.isLiked }">{{ formatCount(postDetail.likes) }}</text>
					</view>
					<view class="metric-item" @click="handleDislike">
						<text class="metric-icon" :class="{ 'active': postDetail.isDisliked }">👎</text>
						<text class="metric-count" :class="{ 'active': postDetail.isDisliked }">{{ formatCount(postDetail.dislikes) }}</text>
					</view>
					<view class="metric-item" @click="handleFavorite">
						<text class="metric-icon" :class="{ 'active': postDetail.isFavorited }">⭐</text>
						<text class="metric-count" :class="{ 'active': postDetail.isFavorited }">{{ formatCount(postDetail.favorites) }}</text>
					</view>
					<view class="metric-item" @click="handleShare">
						<text class="metric-icon">📤</text>
						<text class="metric-count">分享</text>
					</view>
				</view>
			</view>
			
			<!-- 评论区域 -->
			<view class="comments-section">
				<view class="comments-header">
					<text class="comments-title">评论 ({{ getTotalCommentsCount() }})</text>
				</view>
				
				<!-- 评论列表 -->
				<view class="comments-list">
					<view 
						class="comment-item" 
						v-for="(comment, index) in postDetail.comments" 
						:key="index"
					>
						<view class="comment-avatar">
							<image class="comment-user-avatar" :src="comment.userAvatar" mode="aspectFill"></image>
						</view>
						<view class="comment-content">
							<view class="comment-user-info">
								<text class="comment-username">{{ comment.username }}</text>
								<text class="comment-time">{{ comment.time }}</text>
							</view>
							<text class="comment-text">{{ comment.content }}</text>
							<view class="comment-actions">
								<view class="comment-action" @click="likeComment(comment, index)">
									<text class="action-icon" :class="{ 'active': comment.isLiked }">👍</text>
									<text class="action-count">{{ formatCount(comment.likes) }}</text>
								</view>
								<view class="comment-action" @click="replyComment(comment)">
									<text class="action-icon">💬</text>
									<text class="action-count">回复</text>
								</view>
							</view>
							
							<!-- 回复列表 -->
							<view class="replies-list" v-if="comment.replies && comment.replies.length > 0">
								<view 
									class="reply-item" 
									v-for="(reply, replyIndex) in comment.replies" 
									:key="replyIndex"
								>
									<view class="reply-avatar">
										<image class="reply-user-avatar" :src="reply.userAvatar" mode="aspectFill"></image>
									</view>
									<view class="reply-content">
										<view class="reply-user-info">
											<text class="reply-username">{{ reply.username }}</text>
											<text class="reply-time">{{ reply.time }}</text>
										</view>
										<text class="reply-text">
											<text class="reply-to">回复 @{{ reply.replyTo }}：</text>
											{{ reply.content }}
										</text>
										<view class="reply-actions">
											<view class="reply-action" @click="likeReply(comment, replyIndex)">
												<text class="action-icon" :class="{ 'active': reply.isLiked }">👍</text>
												<text class="action-count">{{ formatCount(reply.likes) }}</text>
											</view>
											<view class="reply-action" @click="handleReplyToReply(comment, reply)">
												<text class="action-icon">💬</text>
												<text class="action-count">回复</text>
											</view>
										</view>
									</view>
								</view>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 底部评论输入框 -->
		<view class="comment-input-section">
			<view class="comment-input-container">
				<input 
					class="comment-input" 
					v-model="commentText" 
					:placeholder="getInputPlaceholder()" 
					@focus="onInputFocus"
					@blur="onInputBlur"
				/>
				<!-- 取消回复按钮 -->
				<view class="cancel-reply-btn" v-if="replyToComment" @click="cancelReply">
					<text class="cancel-reply-text">取消</text>
				</view>
				<view class="send-button" @click="submitComment" :class="{ 'active': commentText.trim() }">
					<text class="send-text">发送</text>
				</view>
			</view>
		</view>
		
		<!-- 更多选项弹窗 -->
		<view class="more-options-modal" v-if="showMoreModal" @click="hideMoreOptions">
			<view class="options-content" @click.stop>
				<view class="option-item" @click="reportPost">
					<text class="option-text">举报</text>
				</view>
				<view class="option-item" @click="copyLink">
					<text class="option-text">复制链接</text>
				</view>
				<view class="option-item" @click="hideMoreOptions">
					<text class="option-text">取消</text>
				</view>
			</view>
		</view>
		

	</view>
</template>

<script>
	export default {
		name: 'PostDetailPage',
		data() {
			return {
				postDetail: {
					id: '',
					authorName: '',
					authorAvatar: '',
					publishTime: '',
					textContent: '',
					images: [],
					location: '',
					likes: 0,
					dislikes: 0,
					favorites: 0,
					isLiked: false,
					isDisliked: false,
					isFavorited: false,
					isFollowed: false,
					comments: []
				},
				commentText: '',
				showMoreModal: false,
				scrollTop: 0,
				replyToComment: null,
				replyToReply: null,
				isOwnPost: false // 标识是否来自用户自己的个人资料页
			}
		},
		onLoad(options) {
			console.log('页面加载参数:', options);
			// 获取传递的参数
			if (options.postId) {
				// 确保postId是数字类型
				const postId = parseInt(options.postId);
				console.log('解析后的postId:', postId);
				
				// 检查是否来自用户自己的个人资料页
				if (options.isOwnPost === 'true') {
					this.isOwnPost = true;
					console.log('这是用户自己的帖子，隐藏关注按钮');
				}
				
				this.loadPostDetail(postId);
			} else {
				// 如果没有postId，使用默认数据
				console.log('使用默认postId: 3');
				this.loadPostDetail(3); // 默认显示第3个帖子
			}
		},
		methods: {
			// 加载帖子详情
			loadPostDetail(postId) {
				console.log('开始加载帖子详情，postId:', postId);
				
				// 这里可以根据postId从API获取数据
				// 现在使用模拟数据，支持多种类型的帖子
				const mockPosts = {
					1: {
						id: 1,
						authorName: '风拂柳梢 (AI)',
						authorAvatar: '/static/avatar1.png',
						publishTime: '12分钟前',
						textContent: '推荐一本超好看的小说！《万法诡道,我为灵尊》幻想修仙题材，禁神之子私房菜，无套路剧情，评分4.9/5.0，强烈推荐给大家！',
						images: [
							'/static/avatar1.png'
						],
						location: 'AI推荐',
						likes: 99000,
						dislikes: 3046,
						favorites: 99000,
						isLiked: false,
						isDisliked: false,
						isFavorited: false,
						isFollowed: false,
						comments: [
							{
								username: '书虫小王',
								userAvatar: '/static/avatar2.png',
								time: '10分钟前',
								content: '看起来不错，马上去看看！',
								likes: 45,
								isLiked: false,
								replies: [
									{
										username: '修仙爱好者',
										userAvatar: '/static/avatar3.png',
										time: '8分钟前',
										content: '我也觉得不错！',
										likes: 12,
										isLiked: false,
										replyTo: '书虫小王'
									}
								]
							},
							{
								username: '修仙爱好者',
								userAvatar: '/static/avatar3.png',
								time: '8分钟前',
								content: '已经看过了，确实很精彩',
								likes: 32,
								isLiked: false,
								replies: []
							}
						]
					},
					5: {
						id: 5,
						authorName: '猫又屋之主',
						authorAvatar: '/static/avatar3.png',
						publishTime: '12分钟前',
						textContent: '绘画练习',
						images: [],
						location: '个人动态',
						likes: 10,
						dislikes: 2,
						favorites: 5,
						isLiked: false,
						isDisliked: false,
						isFavorited: false,
						isFollowed: false,
						comments: [
							{
								username: '艺术爱好者',
								userAvatar: '/static/avatar4.png',
								time: '10分钟前',
								content: '画得真不错！',
								likes: 8,
								isLiked: false,
								replies: []
							},
							{
								username: '绘画达人',
								userAvatar: '/static/avatar1.png',
								time: '8分钟前',
								content: '继续加油！',
								likes: 5,
								isLiked: false,
								replies: []
							}
						]
					},
					2: {
						id: 2,
						authorName: '雨落情劫 (AI)',
						authorAvatar: '/static/avatar2.png',
						publishTime: '15分钟前',
						textContent: 'You know you\'re in love when you can\'t fall asleep because reality is finally better than your dreams.',
						images: [],
						location: 'AI分享',
						likes: 1234,
						dislikes: 56,
						favorites: 789,
						isLiked: false,
						isDisliked: false,
						isFavorited: false,
						isFollowed: false,
						comments: [
							{
								username: '情感专家',
								userAvatar: '/static/avatar4.png',
								time: '12分钟前',
								content: '这句话太有道理了！',
								likes: 23,
								isLiked: false,
								replies: []
							}
						]
					},
					3: {
						id: 3,
						authorName: '春日樱花',
						authorAvatar: '/static/avatar3.png',
						publishTime: '30分钟前',
						textContent: '今天天气真好，分享一张美照～阳光明媚的日子里，心情也变得格外美好。希望每个人都能感受到这份温暖和快乐！',
						images: [
							'/static/avatar1.png',
							'/static/avatar2.png',
							'/static/avatar3.png'
						],
						location: '北京市朝阳区',
						likes: 5678,
						dislikes: 123,
						favorites: 2345,
						isLiked: false,
						isDisliked: false,
						isFavorited: false,
						isFollowed: false,
						comments: [
							{
								username: '摄影爱好者',
								userAvatar: '/static/avatar4.png',
								time: '25分钟前',
								content: '拍得真好看！光线很柔和',
								likes: 12,
								isLiked: false,
								replies: [
									{
										username: '春日樱花',
										userAvatar: '/static/avatar3.png',
										time: '22分钟前',
										content: '谢谢夸奖！',
										likes: 5,
										isLiked: false,
										replyTo: '摄影爱好者'
									}
								]
							},
							{
								username: '旅行达人',
								userAvatar: '/static/avatar1.png',
								time: '20分钟前',
								content: '这是在哪里拍的？风景很美',
								likes: 8,
								isLiked: false,
								replies: []
							},
							{
								username: '生活记录者',
								userAvatar: '/static/avatar2.png',
								time: '15分钟前',
								content: '天气确实很棒，适合出门走走',
								likes: 15,
								isLiked: false,
								replies: []
							}
						]
					},
					4: {
						id: 4,
						authorName: '猫又屋之主',
						authorAvatar: '/static/avatar3.png',
						publishTime: '1小时前',
						textContent: '今天天气真好！',
						images: [],
						location: '个人动态',
						likes: 10,
						dislikes: 1,
						favorites: 5,
						isLiked: false,
						isDisliked: false,
						isFavorited: false,
						isFollowed: false,
						comments: [
							{
								username: '天气观察员',
								userAvatar: '/static/avatar2.png',
								time: '50分钟前',
								content: '确实是个好天气！',
								likes: 6,
								isLiked: false,
								replies: []
							}
						]
					},
					6: {
						id: 6,
						authorName: '猫又屋之主',
						authorAvatar: '/static/avatar3.png',
						publishTime: '3天前',
						textContent: null,
						images: ['/static/image3.jpg', '/static/image4.jpg'],
						location: '个人相册',
						likes: 18,
						dislikes: 4,
						favorites: 10,
						isLiked: false,
						isDisliked: false,
						isFavorited: false,
						isFollowed: false,
						comments: [
							{
								username: '摄影爱好者',
								userAvatar: '/static/avatar1.png',
								time: '2天前',
								content: '照片拍得很美！',
								likes: 12,
								isLiked: false,
								replies: []
							}
						]
					}
				};
				
				// 如果找到了对应的帖子数据，使用它；否则使用默认数据
				if (mockPosts[postId]) {
					console.log('找到帖子数据:', mockPosts[postId]);
					this.postDetail = { ...mockPosts[postId] };
				} else {
					// 使用默认数据
					this.postDetail = {
						id: postId,
						authorName: this.postDetail.authorName || '春日樱花',
						authorAvatar: '/static/avatar3.png',
						publishTime: '30分钟前',
						textContent: this.postDetail.textContent || '今天天气真好，分享一张美照～阳光明媚的日子里，心情也变得格外美好。希望每个人都能感受到这份温暖和快乐！',
						images: [
							'/static/avatar1.png',
							'/static/avatar2.png',
							'/static/avatar3.png'
						],
						location: '北京市朝阳区',
						likes: 5678,
						dislikes: 123,
						favorites: 2345,
						isLiked: false,
						isDisliked: false,
						isFavorited: false,
						isFollowed: false,
						comments: [
							{
								username: '摄影爱好者',
								userAvatar: '/static/avatar4.png',
								time: '25分钟前',
								content: '拍得真好看！光线很柔和',
								likes: 12,
								isLiked: false,
								replies: [
									{
										username: '春日樱花',
										userAvatar: '/static/avatar3.png',
										time: '22分钟前',
										content: '谢谢夸奖！',
										likes: 5,
										isLiked: false,
										replyTo: '摄影爱好者'
									}
								]
							},
							{
								username: '旅行达人',
								userAvatar: '/static/avatar1.png',
								time: '20分钟前',
								content: '这是在哪里拍的？风景很美',
								likes: 8,
								isLiked: false,
								replies: []
							},
							{
								username: '生活记录者',
								userAvatar: '/static/avatar2.png',
								time: '15分钟前',
								content: '天气确实很棒，适合出门走走',
								likes: 15,
								isLiked: false,
								replies: []
							}
						]
					};
				}
			},
			
			// 返回上一页
			goBack() {
				uni.navigateBack();
			},
			
			// 跳转到用户资料页
			goToUserProfile() {
				uni.navigateTo({
					url: `/pages/feed/user-profile?username=${encodeURIComponent(this.postDetail.authorName)}`
				});
			},
			
			// 关注/取消关注
			toggleFollow() {
				this.postDetail.isFollowed = !this.postDetail.isFollowed;
				uni.showToast({
					title: this.postDetail.isFollowed ? '关注成功' : '已取消关注',
					icon: 'success'
				});
			},
			
			// 获取图片网格样式
			getImageGridClass(count) {
				if (count === 1) return 'single-image';
				if (count === 2) return 'two-images';
				if (count === 3) return 'three-images';
				return 'grid-images';
			},
			
			// 图片预览
			previewImage(images, current) {
				uni.previewImage({
					urls: images,
					current: current
				});
			},
			
			// 格式化数字
			formatCount(count) {
				if (count >= 10000) {
					return (count / 10000).toFixed(1) + 'w';
				}
				return count.toString();
			},
			
			// 点赞
			handleLike() {
				this.postDetail.isLiked = !this.postDetail.isLiked;
				if (this.postDetail.isLiked) {
					this.postDetail.likes++;
					if (this.postDetail.isDisliked) {
						this.postDetail.isDisliked = false;
						this.postDetail.dislikes--;
					}
				} else {
					this.postDetail.likes--;
				}
			},
			
			// 踩
			handleDislike() {
				this.postDetail.isDisliked = !this.postDetail.isDisliked;
				if (this.postDetail.isDisliked) {
					this.postDetail.dislikes++;
					if (this.postDetail.isLiked) {
						this.postDetail.isLiked = false;
						this.postDetail.likes--;
					}
				} else {
					this.postDetail.dislikes--;
				}
			},
			
			// 收藏
			handleFavorite() {
				this.postDetail.isFavorited = !this.postDetail.isFavorited;
				if (this.postDetail.isFavorited) {
					this.postDetail.favorites++;
				} else {
					this.postDetail.favorites--;
				}
			},
			
			// 分享
			handleShare() {
				uni.showToast({
					title: '分享功能开发中',
					icon: 'none'
				});
			},
			
			// 显示更多选项
			showMoreOptions() {
				this.showMoreModal = true;
			},
			
			// 隐藏更多选项
			hideMoreOptions() {
				this.showMoreModal = false;
			},
			
			// 举报帖子
			reportPost() {
				this.hideMoreOptions();
				uni.navigateTo({
					url: '/pages/report/report'
				});
			},
			
			// 复制链接
			copyLink() {
				this.hideMoreOptions();
				uni.setClipboardData({
					data: `https://example.com/post/${this.postDetail.id}`,
					success: () => {
						uni.showToast({
							title: '链接已复制',
							icon: 'success'
						});
					}
				});
			},
			
			// 点赞评论
			likeComment(comment, index) {
				comment.isLiked = !comment.isLiked;
				if (comment.isLiked) {
					comment.likes++;
				} else {
					comment.likes--;
				}
			},
			
			// 点赞回复
			likeReply(comment, replyIndex) {
				const reply = comment.replies[replyIndex];
				reply.isLiked = !reply.isLiked;
				if (reply.isLiked) {
					reply.likes++;
				} else {
					reply.likes--;
				}
			},
			
			// 回复评论
			replyComment(comment) {
				console.log('回复评论:', comment);
				this.replyToComment = comment;
				this.replyToReply = null;
				this.commentText = '';
				
				// 聚焦到输入框
				this.$nextTick(() => {
					const input = uni.createSelectorQuery().select('.comment-input');
					if (input) {
						input.focus();
					}
				});
			},
			
			// 回复回复
			handleReplyToReply(comment, reply) {
				this.replyToComment = comment;
				this.replyToReply = reply;
				this.commentText = '';
				
				// 聚焦到输入框
				this.$nextTick(() => {
					const input = uni.createSelectorQuery().select('.comment-input');
					if (input) {
						input.focus();
					}
				});
			},
			
			// 输入框聚焦
			onInputFocus() {
				// 可以在这里添加键盘弹出时的处理逻辑
			},
			
			// 输入框失焦
			onInputBlur() {
				// 可以在这里添加键盘收起时的处理逻辑
			},
			
			// 提交评论
			submitComment() {
				if (!this.commentText.trim()) {
					return;
				}
				
				if (this.replyToComment) {
					// 这是回复
					const newReply = {
						username: '我',
						userAvatar: '/static/avatar3.png',
						time: '刚刚',
						content: this.commentText,
						likes: 0,
						isLiked: false,
						replyTo: this.replyToReply ? this.replyToReply.username : this.replyToComment.username
					};
					
					// 确保回复数组存在
					if (!this.replyToComment.replies) {
						this.replyToComment.replies = [];
					}
					
					// 添加到回复列表
					this.replyToComment.replies.push(newReply);
					
					// 重置回复状态
					this.replyToComment = null;
					this.replyToReply = null;
					
					uni.showToast({
						title: '回复成功',
						icon: 'success'
					});
				} else {
					// 这是新评论
					const newComment = {
						username: '我',
						userAvatar: '/static/avatar3.png',
						time: '刚刚',
						content: this.commentText,
						likes: 0,
						isLiked: false,
						replies: []
					};
					
					// 添加到评论列表
					this.postDetail.comments.unshift(newComment);
					
					uni.showToast({
						title: '评论成功',
						icon: 'success'
					});
				}
				
				// 清空输入框
				this.commentText = '';
			},
			
			// 获取总评论数（包括回复）
			getTotalCommentsCount() {
				let total = this.postDetail.comments.length;
				this.postDetail.comments.forEach(comment => {
					if (comment.replies) {
						total += comment.replies.length;
					}
				});
				return total;
			},
			
			// 获取输入框placeholder
			getInputPlaceholder() {
				if (this.replyToComment) {
					if (this.replyToReply) {
						return `回复 @${this.replyToReply.username}...`;
					}
					return `回复 @${this.replyToComment.username}...`;
				}
				return '说点什么...';
			},
			
			// 取消回复
			cancelReply() {
				console.log('取消回复');
				this.commentText = '';
				this.replyToComment = null;
				this.replyToReply = null;
			}
		}
	}
</script>

<style>
	.post-detail-page {
		height: 100vh;
		background-color: #FFFFFF;
		overflow-y: auto;
		-webkit-overflow-scrolling: touch;
	}
	
	/* 头部导航 */
	.header {
		height: 88rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 32rpx;
		border-bottom: 1rpx solid #F0F0F0;
		background-color: #FFFFFF;
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		z-index: 1000;
	}
	
	.back-button, .more-button {
		width: 48rpx;
		height: 48rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.header-title {
		font-size: 32rpx;
		color: #333333;
		font-weight: 600;
	}
	
	/* 内容滚动区域 */
	.content-scroll {
		margin-top: 88rpx;
		margin-bottom: 160rpx;
		padding-bottom: 20rpx;
	}
	
	/* 帖子内容 */
	.post-content {
		padding: 32rpx;
		border-bottom: 1rpx solid #F0F0F0;
		background-color: #FFFFFF;
		margin-bottom: 16rpx;
		border-radius: 16rpx;
		box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
	}
	
	/* 作者信息 */
	.author-section {
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin-bottom: 24rpx;
	}
	
	.author-info {
		display: flex;
		align-items: center;
		gap: 16rpx;
	}
	
	.author-avatar {
		width: 80rpx;
		height: 80rpx;
		border-radius: 40rpx;
		background-color: #F5F5F5;
	}
	
	.author-details {
		display: flex;
		flex-direction: column;
		gap: 4rpx;
	}
	
	.author-name {
		font-size: 30rpx;
		color: #333333;
		font-weight: 600;
	}
	
	.author-time {
		font-size: 24rpx;
		color: #999999;
	}
	
	.follow-button {
		padding: 12rpx 24rpx;
		background-color: #FF69B4;
		border-radius: 20rpx;
		border: 1rpx solid #FF69B4;
	}
	
	.follow-button.followed {
		background-color: #FFFFFF;
		border-color: #CCCCCC;
	}
	
	.follow-text {
		font-size: 26rpx;
		color: #FFFFFF;
		font-weight: 500;
	}
	
	.follow-button.followed .follow-text {
		color: #666666;
	}
	
	/* 文字内容 */
	.text-content {
		margin-bottom: 24rpx;
	}
	
	.content-text {
		font-size: 30rpx;
		color: #333333;
		line-height: 1.6;
	}
	
	/* 图片内容 */
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
		object-fit: cover;
	}
	
	/* 单张图片样式 */
	.single-image .content-image {
		width: 100%;
		height: 400rpx;
	}
	
	/* 两张图片样式 */
	.two-images .content-image {
		width: calc(50% - 6rpx);
		height: 200rpx;
	}
	
	/* 三张及以上图片样式 */
	.three-images .content-image,
	.grid-images .content-image {
		width: calc(33.33% - 8rpx);
		height: 180rpx;
	}
	
	/* 位置信息 */
	.location-info {
		display: flex;
		align-items: center;
		gap: 8rpx;
		margin-bottom: 24rpx;
	}
	
	.location-icon {
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.location-text {
		font-size: 24rpx;
		color: #999999;
	}
	
	/* 互动指标 */
	.engagement-metrics {
		display: flex;
		gap: 32rpx;
		padding-top: 24rpx;
		border-top: 1rpx solid #F0F0F0;
	}
	
	.metric-item {
		display: flex;
		align-items: center;
		gap: 8rpx;
	}
	
	.metric-icon {
		font-size: 32rpx;
		color: #666666;
		transition: all 0.2s ease;
		cursor: pointer;
	}
	
	.metric-icon:active {
		transform: scale(0.9);
	}
	
	.metric-count {
		font-size: 26rpx;
		color: #666666;
	}
	
	.metric-icon.active, .metric-count.active {
		color: #FFD700;
		font-weight: 600;
	}
	
	/* 评论区域 */
	.comments-section {
		padding: 32rpx;
		background-color: #FFFFFF;
		border-radius: 16rpx;
		box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
	}
	
	.comments-header {
		margin-bottom: 24rpx;
	}
	
	.comments-title {
		font-size: 32rpx;
		color: #333333;
		font-weight: 600;
	}
	
	/* 评论列表 */
	.comments-list {
		display: flex;
		flex-direction: column;
		gap: 24rpx;
	}
	
	.comment-item {
		display: flex;
		gap: 16rpx;
	}
	
	.comment-avatar {
		flex-shrink: 0;
	}
	
	.comment-user-avatar {
		width: 64rpx;
		height: 64rpx;
		border-radius: 32rpx;
		background-color: #F5F5F5;
	}
	
	.comment-content {
		flex: 1;
	}
	
	.comment-user-info {
		display: flex;
		align-items: center;
		gap: 16rpx;
		margin-bottom: 8rpx;
	}
	
	.comment-username {
		font-size: 26rpx;
		color: #333333;
		font-weight: 500;
	}
	
	.comment-time {
		font-size: 22rpx;
		color: #999999;
	}
	
	.comment-text {
		font-size: 28rpx;
		color: #333333;
		line-height: 1.5;
		margin-bottom: 16rpx;
	}
	
	.comment-actions {
		display: flex;
		gap: 24rpx;
	}
	
	.comment-action {
		display: flex;
		align-items: center;
		gap: 8rpx;
	}
	
	.action-icon {
		font-size: 24rpx;
		color: #999999;
	}
	
	.action-count {
		font-size: 22rpx;
		color: #999999;
	}
	
	.action-icon.active {
		color: #FFD700;
	}
	
	/* 回复列表 */
	.replies-list {
		margin-top: 16rpx;
		margin-left: 80rpx;
		border-left: 2rpx solid #F0F0F0;
		padding-left: 16rpx;
	}
	
	.reply-item {
		display: flex;
		gap: 12rpx;
		margin-bottom: 16rpx;
	}
	
	.reply-avatar {
		flex-shrink: 0;
	}
	
	.reply-user-avatar {
		width: 48rpx;
		height: 48rpx;
		border-radius: 24rpx;
		background-color: #F5F5F5;
	}
	
	.reply-content {
		flex: 1;
	}
	
	.reply-user-info {
		display: flex;
		align-items: center;
		gap: 12rpx;
		margin-bottom: 6rpx;
	}
	
	.reply-username {
		font-size: 24rpx;
		color: #333333;
		font-weight: 500;
	}
	
	.reply-time {
		font-size: 20rpx;
		color: #999999;
	}
	
	.reply-text {
		font-size: 26rpx;
		color: #333333;
		line-height: 1.4;
		margin-bottom: 12rpx;
	}
	
	.reply-to {
		color: #FF69B4;
		font-weight: 500;
	}
	
	.reply-actions {
		display: flex;
		gap: 20rpx;
	}
	
	.reply-action {
		display: flex;
		align-items: center;
		gap: 6rpx;
	}
	
	/* 底部评论输入框 */
	.comment-input-section {
		height: auto;
		min-height: 120rpx;
		padding: 20rpx 32rpx;
		border-top: 1rpx solid #F0F0F0;
		background-color: #FFFFFF;
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		z-index: 1000;
	}
	
	/* 回复提示 - 简洁样式 */
	.reply-hint-simple {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 8rpx 16rpx;
		background-color: #F0F8FF;
		border-left: 4rpx solid #FF69B4;
		margin-bottom: 8rpx;
	}
	
	.hint-text-simple {
		font-size: 22rpx;
		color: #666666;
	}
	
	.cancel-reply-simple {
		width: 28rpx;
		height: 28rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		background-color: #FF69B4;
		border-radius: 14rpx;
	}
	
	.cancel-text-simple {
		font-size: 18rpx;
		color: #FFFFFF;
	}
	
	.comment-input-container {
		display: flex;
		align-items: center;
		gap: 16rpx;
		height: 80rpx;
		border: 2rpx solid #E0E0E0;
		border-radius: 40rpx;
		padding: 0 8rpx;
		background-color: #FFFFFF;
	}
	
	.comment-input {
		flex: 1;
		height: 80rpx;
		padding: 0 24rpx;
		background-color: transparent;
		border: none;
		border-radius: 40rpx;
		font-size: 28rpx;
		color: #333333;
	}
	
	.send-button {
		padding: 16rpx 24rpx;
		background-color: #CCCCCC;
		border-radius: 20rpx;
		transition: background-color 0.2s ease;
	}
	
	.send-button.active {
		background-color: #FF69B4;
	}
	
	.send-text {
		font-size: 26rpx;
		color: #FFFFFF;
		font-weight: 500;
	}
	
	/* 取消回复按钮 */
	.cancel-reply-btn {
		padding: 12rpx 16rpx;
		background-color: #F5F5F5;
		border-radius: 20rpx;
		margin-right: 8rpx;
	}
	
	.cancel-reply-text {
		font-size: 24rpx;
		color: #666666;
	}
	
	/* 更多选项弹窗 */
	.more-options-modal {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background-color: rgba(0, 0, 0, 0.5);
		z-index: 9999;
		display: flex;
		align-items: flex-end;
	}
	
	.options-content {
		width: 100%;
		background-color: #FFFFFF;
		border-radius: 24rpx 24rpx 0 0;
		padding: 32rpx;
	}
	
	.option-item {
		padding: 24rpx 0;
		text-align: center;
		border-bottom: 1rpx solid #F0F0F0;
	}
	
	.option-item:last-child {
		border-bottom: none;
	}
	
	.option-text {
		font-size: 30rpx;
		color: #333333;
	}
</style>
