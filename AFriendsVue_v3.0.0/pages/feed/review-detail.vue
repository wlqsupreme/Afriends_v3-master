<template>
	<view class="review-detail-page">
		<!-- 头部导航 -->
		<view class="header">
			<view class="back-button" @click="goBack" @touchstart="onBackTouchStart" @touchend="onBackTouchEnd">
				<svg t="1756246262970" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="904" width="32" height="32">
					<path d="M407.01 512l286.008-286.008a35.84 35.84 0 0 0-50.683-50.683L330.982 486.656a35.84 35.84 0 0 0 0 50.683L642.34 848.69a35.84 35.84 0 0 0 50.683-50.683L407.009 512z" fill="#666666" p-id="905"></path>
				</svg>
			</view>
			<view class="header-title">书评详情</view>
			<view class="more-button" @click="showMoreOptions">
				<svg t="1756202704554" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="32" height="32">
					<path d="M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64zm0 820c-205.4 0-372-166.6-372-372s166.6-372 372-372 372 166.6 372 372-166.6 372-372 372z" fill="#333333"></path>
					<path d="M464 688a48 48 0 1 0 96 0 48 48 0 1 0-96 0zm24-112h48c4.4 0 8-3.6 8-8V296c0-4.4-3.6-8-8-8h-48c-4.4 0-8 3.6-8 8v272c0 4.4 3.6 8 8 8z" fill="#333333"></path>
				</svg>
			</view>
		</view>
		
		<!-- 内容区域 -->
		<view class="content-scroll">
			<!-- 加载状态 -->
			<view class="loading-container" v-if="loading">
				<text class="loading-text">正在加载数据...</text>
			</view>
			
			<!-- 错误状态 -->
			<view class="error-container" v-if="hasError && !loading">
				<view class="error-icon">⚠️</view>
				<text class="error-text">{{ errorMessage }}</text>
				<view class="error-actions">
					<button class="retry-button" @click="loadPostDetail">重新加载</button>
				</view>
			</view>
			
			<!-- 书评内容 -->
			<view class="review-content" v-if="!hasError && !loading">
				<!-- 评论者信息 -->
				<view class="reviewer-section">
					<view class="reviewer-info" @click="goToUserProfile">
						<image class="reviewer-avatar" :src="reviewDetail.avatar" mode="aspectFill"></image>
						<view class="reviewer-details">
							<text class="reviewer-name">{{ reviewDetail.reviewer }}</text>
							<view class="review-meta">
								<view class="rating-stars">
									<text class="star" v-for="n in reviewDetail.rating" :key="n">★</text>
									<text class="star empty" v-for="n in (5 - reviewDetail.rating)" :key="n">☆</text>
								</view>
								<text class="review-time">{{ reviewDetail.time }}</text>
							</view>
						</view>
					</view>
					<view class="follow-button" @click="toggleFollow" :class="{ 'followed': reviewDetail.isFollowed }">
						<text class="follow-text">{{ reviewDetail.isFollowed ? '已关注' : '关注' }}</text>
					</view>
				</view>
				
				<!-- 书评内容 -->
				<view class="text-content" v-if="reviewDetail.content">
					<text class="content-text">{{ reviewDetail.content }}</text>
				</view>
				
				<!-- 图片内容 -->
				<view class="image-content" v-if="reviewDetail.images && reviewDetail.images.length > 0">
					<view class="image-grid">
						<image 
							v-for="(image, index) in reviewDetail.images" 
							:key="index"
							class="content-image" 
							:src="image" 
							mode="aspectFill"
							@click="previewImage(reviewDetail.images, index)"
						></image>
					</view>
				</view>
				
				<!-- 互动指标 -->
				<view class="engagement-metrics">
					<view class="metric-item" @click="handleLike">
						<text class="metric-icon" :class="{ 'active': reviewDetail.isLiked }">👍</text>
						<text class="metric-count" :class="{ 'active': reviewDetail.isLiked }">{{ formatCount(reviewDetail.likes) }}</text>
					</view>
					<view class="metric-item" @click="handleDislike">
						<text class="metric-icon" :class="{ 'active': reviewDetail.isDisliked }">👎</text>
						<text class="metric-count" :class="{ 'active': reviewDetail.isDisliked }">{{ formatCount(reviewDetail.dislikes) }}</text>
					</view>
					<view class="metric-item" @click="handleFavorite">
						<text class="metric-icon" :class="{ 'active': reviewDetail.isFavorited }">⭐</text>
						<text class="metric-count" :class="{ 'active': reviewDetail.isFavorited }">{{ formatCount(reviewDetail.favorites) }}</text>
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
						v-for="(comment, index) in reviewDetail.comments" 
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
				<view class="option-item" @click="reportReview">
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
		name: 'ReviewDetailPage',
		data() {
			return {
				reviewDetail: {
					id: '',
					reviewer: '',
					avatar: '',
					rating: 5,
					content: '',
					time: '',
					likes: 0,
					dislikes: 0,
					favorites: 0,
					isLiked: false,
					isDisliked: false,
					isFavorited: false,
					isFollowed: false,
					comments: [],
					images: [] // 添加图片数组
				},
				commentText: '',
				showMoreModal: false,
				scrollTop: 0,
				replyToComment: null,
				replyToReply: null,
				postId: null,
				postType: 'text',
				userId: 1000100,
				loading: false,
				hasError: false,
				errorMessage: ''
			}
		},
		onLoad(options) {
			console.log('页面加载参数:', options);
			// 获取传递的参数
			if (options.postId !== undefined) {
				this.postId = parseInt(options.postId);
				this.postType = options.postType || 'text';
				this.userId = parseInt(options.userId) || 1000100;
				console.log('解析后的参数 - postId:', this.postId, 'postType:', this.postType, 'userId:', this.userId);
				this.loadPostDetail();
			} else if (options.id !== undefined) {
				// 兼容旧的参数格式
				const reviewId = parseInt(options.id);
				console.log('解析后的reviewId:', reviewId);
				this.loadReviewDetail(options);
			} else {
				console.log('使用默认数据');
				this.loadDefaultReviewDetail();
			}
		},
		methods: {
			// 加载帖子详情数据
			async loadPostDetail() {
				try {
					console.log('=== 开始加载帖子详情数据 ===');
					console.log('帖子ID:', this.postId);
					console.log('帖子类型:', this.postType);
					console.log('用户ID:', this.userId);
					console.log('请求URL:', `http://localhost:8888/api/post-detail/data?postId=${this.postId}&postType=${this.postType}&userId=${this.userId}`);
					
					this.loading = true;
					this.hasError = false;
					this.errorMessage = '';
					
					const response = await uni.request({
						url: `http://localhost:8888/api/post-detail/data?postId=${this.postId}&postType=${this.postType}&userId=${this.userId}`,
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('=== API响应详情 ===');
					console.log('状态码:', response.statusCode);
					console.log('响应数据:', response.data);
					
					if (response.statusCode === 200 && response.data.success) {
						const postData = response.data.data;
						console.log('获取到的帖子数据:', postData);
						
						// 更新页面数据
						this.reviewDetail = {
							id: postData.id,
							reviewer: postData.reviewer,
							avatar: postData.avatar,
							rating: postData.rating,
							content: postData.content,
							time: postData.time,
							likes: postData.likes,
							dislikes: postData.dislikes,
							favorites: postData.favorites,
							isLiked: postData.isLiked,
							isDisliked: postData.isDisliked,
							isFavorited: postData.isFavorited,
							isFollowed: postData.isFollowed,
							comments: postData.comments || [],
							images: postData.images || []
						};
						
						console.log('帖子详情数据加载成功');
						
						uni.showToast({
							title: '数据加载成功',
							icon: 'success',
							duration: 1500
						});
					} else {
						console.error('加载帖子详情数据失败:', response.data.message);
						this.hasError = true;
						this.errorMessage = response.data.message || '加载数据失败';
						uni.showToast({
							title: '加载数据失败',
							icon: 'error'
						});
					}
				} catch (error) {
					console.error('加载帖子详情数据异常:', error);
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
			
			// 加载书评详情
			loadReviewDetail(options) {
				console.log('开始加载书评详情，参数:', options);
				
				// 从传递的参数构建书评数据
				this.reviewDetail = {
					id: options.id || 0,
					reviewer: decodeURIComponent(options.reviewer || ''),
					avatar: '/static/avatar1.png',
					rating: parseInt(options.rating) || 5,
					content: decodeURIComponent(options.content || ''),
					time: decodeURIComponent(options.time || ''),
					likes: parseInt(options.likes) || 0,
					dislikes: parseInt(options.dislikes) || 0,
					favorites: 0,
					isLiked: false,
					isDisliked: false,
					isFavorited: false,
					isFollowed: false,
					comments: this.generateMockComments()
				};
				
				console.log('加载的书评数据:', this.reviewDetail);
			},
			
			// 生成模拟评论数据
			generateMockComments() {
				return [
					{
						username: '书虫小王',
						userAvatar: '/static/avatar2.png',
						time: '10分钟前',
						content: '这个书评写得很中肯！',
						likes: 45,
						isLiked: false,
						replies: [
							{
								username: '修仙爱好者',
								userAvatar: '/static/avatar3.png',
								time: '8分钟前',
								content: '我也觉得分析得很到位！',
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
						content: '完全同意楼主的观点，这本书确实很棒',
						likes: 32,
						isLiked: false,
						replies: []
					},
					{
						username: '文学评论家',
						userAvatar: '/static/avatar4.png',
						time: '5分钟前',
						content: '文笔流畅，分析透彻，值得推荐',
						likes: 28,
						isLiked: false,
						replies: []
					}
				];
			},
			
			// 加载默认书评详情
			loadDefaultReviewDetail() {
				this.reviewDetail = {
					id: 0,
					reviewer: '书友123456',
					avatar: '/static/avatar1.png',
					rating: 5,
					content: '文笔剧情很好,很难得的女强无cp游戏侵入现实的文,世界观设计都很有画面感,女主也很聪明,抓...',
					time: '阅读不足30分钟后点评',
					likes: 234,
					dislikes: 12,
					favorites: 0,
					isLiked: false,
					isDisliked: false,
					isFavorited: false,
					isFollowed: false,
					comments: this.generateMockComments()
				};
			},
			
			// 返回上一页
			goBack() {
				console.log('返回按钮被点击');
				try {
					// 尝试返回上一页
					uni.navigateBack({
						success: () => {
							console.log('返回成功');
						},
						fail: (err) => {
							console.log('返回失败:', err);
							// 如果返回失败，尝试跳转到热门书评页面
							uni.navigateTo({
								url: '/pages/feed/novel-more-reviews'
							});
						}
					});
				} catch (error) {
					console.error('返回操作出错:', error);
					// 出错时跳转到热门书评页面
					uni.navigateTo({
						url: '/pages/feed/novel-more-reviews'
					});
				}
			},
			
			// 跳转到用户资料页
			goToUserProfile() {
				uni.navigateTo({
					url: `/pages/feed/user-profile?username=${encodeURIComponent(this.reviewDetail.reviewer)}`
				});
			},
			
			// 关注/取消关注
			toggleFollow() {
				this.reviewDetail.isFollowed = !this.reviewDetail.isFollowed;
				uni.showToast({
					title: this.reviewDetail.isFollowed ? '关注成功' : '已取消关注',
					icon: 'success'
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
			async handleLike() {
				try {
					console.log('处理点赞操作:', this.postId, this.postType, this.userId);
					
					const response = await uni.request({
						url: 'http://localhost:8888/api/post-detail/like',
						method: 'POST',
						data: {
							postId: this.postId,
							postType: this.postType,
							userId: this.userId
						},
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('点赞API响应:', response);
					
					if (response.statusCode === 200 && response.data.success) {
						// 更新前端状态
						this.reviewDetail.isLiked = response.data.isLiked;
						if (this.reviewDetail.isLiked) {
							this.reviewDetail.likes++;
							// 如果之前点踩了，取消点踩
							if (this.reviewDetail.isDisliked) {
								this.reviewDetail.isDisliked = false;
								this.reviewDetail.dislikes--;
							}
						} else {
							this.reviewDetail.likes--;
						}
						
						uni.showToast({
							title: response.data.message,
							icon: 'success',
							duration: 1500
						});
					} else {
						console.error('点赞操作失败:', response.data.message);
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
				}
			},
			
			// 踩
			async handleDislike() {
				try {
					console.log('处理点踩操作:', this.postId, this.postType, this.userId);
					
					const response = await uni.request({
						url: 'http://localhost:8888/api/post-detail/dislike',
						method: 'POST',
						data: {
							postId: this.postId,
							postType: this.postType,
							userId: this.userId
						},
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('点踩API响应:', response);
					
					if (response.statusCode === 200 && response.data.success) {
						// 更新前端状态
						this.reviewDetail.isDisliked = response.data.isDisliked;
						if (this.reviewDetail.isDisliked) {
							this.reviewDetail.dislikes++;
							// 如果之前点赞了，取消点赞
							if (this.reviewDetail.isLiked) {
								this.reviewDetail.isLiked = false;
								this.reviewDetail.likes--;
							}
						} else {
							this.reviewDetail.dislikes--;
						}
						
						uni.showToast({
							title: response.data.message,
							icon: 'success',
							duration: 1500
						});
					} else {
						console.error('点踩操作失败:', response.data.message);
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
				}
			},
			
			// 收藏
			async handleFavorite() {
				try {
					console.log('处理收藏操作:', this.postId, this.postType, this.userId);
					
					const response = await uni.request({
						url: 'http://localhost:8888/api/post-detail/favorite',
						method: 'POST',
						data: {
							postId: this.postId,
							postType: this.postType,
							userId: this.userId
						},
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('收藏API响应:', response);
					
					if (response.statusCode === 200 && response.data.success) {
						// 更新前端状态
						this.reviewDetail.isFavorited = response.data.isFavorited;
						if (this.reviewDetail.isFavorited) {
							this.reviewDetail.favorites++;
						} else {
							this.reviewDetail.favorites--;
						}
						
						uni.showToast({
							title: response.data.message,
							icon: 'success',
							duration: 1500
						});
					} else {
						console.error('收藏操作失败:', response.data.message);
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
				}
			},
			
			// 分享
			handleShare() {
				uni.showToast({
					title: '分享功能开发中',
					icon: 'none'
				});
			},
			
			// 图片预览
			previewImage(images, current) {
				uni.previewImage({
					urls: images,
					current: current
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
			
			// 举报书评
			reportReview() {
				this.hideMoreOptions();
				uni.navigateTo({
					url: '/pages/report/report'
				});
			},
			
			// 复制链接
			copyLink() {
				this.hideMoreOptions();
				uni.setClipboardData({
					data: `https://example.com/review/${this.reviewDetail.id}`,
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
			async submitComment() {
				if (!this.commentText.trim()) {
					return;
				}
				
				try {
					console.log('提交评论:', this.commentText, '帖子ID:', this.postId, '帖子类型:', this.postType, '用户ID:', this.userId);
					
					const response = await uni.request({
						url: 'http://localhost:8888/api/post-detail/comment',
						method: 'POST',
						data: {
							postId: this.postId,
							postType: this.postType,
							userId: this.userId,
							commentText: this.commentText,
							parentCommentId: this.replyToComment ? this.replyToComment.id : null
						},
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('评论API响应:', response);
					
					if (response.statusCode === 200 && response.data.success) {
						// 重新加载帖子详情以获取最新评论
						await this.loadPostDetail();
						
						// 重置回复状态
						this.replyToComment = null;
						this.replyToReply = null;
						
						uni.showToast({
							title: '评论成功',
							icon: 'success'
						});
					} else {
						console.error('评论操作失败:', response.data.message);
						uni.showToast({
							title: response.data.message || '评论失败',
							icon: 'error'
						});
					}
				} catch (error) {
					console.error('评论操作异常:', error);
					uni.showToast({
						title: '网络错误',
						icon: 'error'
					});
				}
				
				// 清空输入框
				this.commentText = '';
			},
			
			// 获取总评论数（包括回复）
			getTotalCommentsCount() {
				let total = this.reviewDetail.comments.length;
				this.reviewDetail.comments.forEach(comment => {
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
			},
			
			// 返回按钮触摸开始
			onBackTouchStart() {
				console.log('返回按钮触摸开始');
				// 可以添加触摸反馈效果
			},
			
			// 返回按钮触摸结束
			onBackTouchEnd() {
				console.log('返回按钮触摸结束');
				// 可以添加触摸反馈效果
			}
		}
	}
</script>

<style>
	.review-detail-page {
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
		width: 56rpx;
		height: 56rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
		transition: all 0.2s ease;
		border-radius: 50%;
		background-color: rgba(255, 255, 255, 0.9);
		box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
	}
	
	.back-button:hover, .more-button:hover {
		background-color: #F5F5F5;
		box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.15);
	}
	
	.back-button:active, .more-button:active {
		background-color: #E0E0E0;
		transform: scale(0.95);
		box-shadow: 0 1rpx 4rpx rgba(0, 0, 0, 0.1);
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
	
	/* 书评内容 */
	.review-content {
		padding: 32rpx;
		border-bottom: 1rpx solid #F0F0F0;
		background-color: #FFFFFF;
		margin-bottom: 16rpx;
		border-radius: 16rpx;
		box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.05);
	}
	
	/* 评论者信息 */
	.reviewer-section {
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin-bottom: 24rpx;
	}
	
	.reviewer-info {
		display: flex;
		align-items: center;
		gap: 16rpx;
	}
	
	.reviewer-avatar {
		width: 80rpx;
		height: 80rpx;
		border-radius: 40rpx;
		background-color: #F5F5F5;
	}
	
	.reviewer-details {
		display: flex;
		flex-direction: column;
		gap: 8rpx;
	}
	
	.reviewer-name {
		font-size: 30rpx;
		color: #333333;
		font-weight: 600;
	}
	
	.review-meta {
		display: flex;
		align-items: center;
		gap: 16rpx;
	}
	
	.rating-stars {
		display: flex;
		gap: 4rpx;
	}
	
	.star {
		font-size: 24rpx;
		color: #FFD700;
	}
	
	.star.empty {
		color: #E0E0E0;
	}
	
	.review-time {
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
	
	/* 图片内容样式 */
	.image-content {
		margin-bottom: 24rpx;
	}
	
	.image-grid {
		display: flex;
		flex-wrap: wrap;
		gap: 12rpx;
	}
	
	.content-image {
		width: 200rpx;
		height: 150rpx;
		border-radius: 8rpx;
		background-color: #F0F0F0;
	}
	
	/* 加载状态样式 */
	.loading-container {
		display: flex;
		justify-content: center;
		align-items: center;
		padding: 80rpx 40rpx;
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
