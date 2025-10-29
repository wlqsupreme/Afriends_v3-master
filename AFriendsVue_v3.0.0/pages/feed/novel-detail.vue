<template>
	<view class="novel-detail-page">
		<!-- 头部导航 -->
		<!-- <view class="header">
			<view class="back-button" @click="goBack">
				<view class="back-arrow"></view>
			</view>
			<view class="header-title">
				<text class="title-text">小说详情</text>
			</view>
			<view class="listen-button" @click="startListening">
				<text class="listen-text">听书</text>
			</view>
		</view> -->
		<!-- 这个听书图标应该是不留 -->
		
		<!-- 主要内容区域 -->
		<scroll-view class="content-scroll" scroll-y="true" :scroll-top="scrollTop" @scroll="onScroll">
			<!-- 加载状态 -->
			<view class="loading-container" v-if="loading">
				<text class="loading-text">正在加载小说详情...</text>
			</view>
			
			<!-- 错误状态 -->
			<view class="error-container" v-if="hasError && !loading">
				<view class="error-icon">⚠️</view>
				<text class="error-text">{{ errorMessage }}</text>
				<view class="error-actions">
					<button class="retry-button" @click="loadNovelDetailFromAPI">重新加载</button>
				</view>
			</view>
			
			<!-- 正常内容 -->
			<view v-if="!hasError && !loading">
			<!-- 返回按钮 -->
			<!-- <view class="back-button-container">
				<view class="back-button" @click="goBack">
					<svg t="1756247334143" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="1052" width="32" height="32">
						<path d="M407.01 512l286.008-286.008a35.84 35.84 0 0 0-50.683-50.683L330.982 486.656a35.84 35.84 0 0 0 0 50.683L642.34 848.69a35.84 35.84 0 0 0 50.683-50.683L407.009 512z" fill="#666666" p-id="1053"></path>
					</svg>
				</view>
			</view> -->
			
			<!-- 小说封面 -->
			<view class="novel-cover-section">
				<image class="novel-cover" :src="novelData.coverImage || ''" mode="aspectFill"></image>
			</view>
			
			<!-- 小说信息 -->
			<view class="novel-info-section">
				<text class="novel-title">{{ novelData.title || '小说名' }}</text>
				<view class="author-section">
					<text class="author-name">{{ novelData.author || '小说作者' }}</text>
					<view class="follow-button" @click="followAuthor">
						<text class="follow-text">{{ isFollowed ? '已关注' : '+关注' }}</text>
					</view>
				</view>
			</view>
			
			<!-- 统计数据 -->
			<view class="stats-section">
				<view class="stat-item">
					<text class="stat-value">{{ novelData.rating || '9.5' }}分</text>
					<text class="stat-label">{{ novelData.reviewCount || '11.9万人点评' }}</text>
				</view>
				<view class="stat-item">
					<text class="stat-value">{{ novelData.readerCount || '157.4万人' }}</text>
					<text class="stat-label">正在阅读</text>
				</view>
				<view class="stat-item">
					<text class="stat-value">{{ novelData.wordCount || '214.7万字' }}</text>
					<text class="stat-label">{{ novelData.updateDays || '连续更新268天' }}</text>
				</view>
			</view>
			
			<!-- 简介部分 -->
			<view class="synopsis-section">
				<view class="section-header">
					<text class="section-title">简介</text>
				</view>
				<view class="tags-container">
					<view class="tag" v-for="(tag, index) in novelData.tags" :key="index">
						<text class="tag-text">{{ tag }}</text>
					</view>
				</view>
				<view class="synopsis-content">
					<text class="synopsis-text" :class="{ 'expanded': synopsisExpanded }">
						{{ novelData.synopsis || '【游戏入侵+unp+升级流+独狼+游戏】(又名: 游戏入侵) 这是一个关于游戏入侵现实的故事，主角在游戏中获得了强大的能力，开始了一段惊险刺激的冒险之旅。故事融合了游戏元素和现实世界，充满了悬念和惊喜。主角从一个普通玩家逐渐成长为游戏世界的强者，在这个过程中不仅要面对游戏中的挑战，还要处理现实世界中的各种问题。' }}
					</text>
					<view class="expand-button" v-if="novelData.synopsis && novelData.synopsis.length > 80" @click="toggleSynopsis">
						<text class="expand-text">{{ synopsisExpanded ? '收起' : '展开' }}</text>
					</view>
				</view>
			</view>
			
			<!-- 热门书评 -->
			<view class="reviews-section">
				<view class="section-header">
					<text class="section-title">热门书评</text>
					<view class="more-reviews" @click="goToMoreReviews">
						<text class="more-text">更多书评</text>
					</view>
				</view>
				
				<!-- 有书评数据时显示书评列表 -->
				<view class="reviews-list" v-if="novelData.reviews && novelData.reviews.length > 0">
					<view class="review-item" v-for="(review, index) in novelData.reviews" :key="index">
						<view class="review-header">
							<image class="reviewer-avatar" :src="review.avatar || '/static/avatar-default.png'" mode="aspectFill"></image>
							<view class="review-info">
								<text class="reviewer-name">{{ review.reviewer }}</text>
								<view class="review-rating">
									<text class="star" v-for="n in review.rating" :key="n">★</text>
									<text class="star empty" v-for="n in (5 - review.rating)" :key="n">☆</text>
								</view>
								<text class="review-time">{{ review.time }}</text>
							</view>
						</view>
						<text class="review-content" :class="{ 'expanded': reviewExpanded[index] }">
							{{ review.content }}
						</text>
						<view class="expand-button" v-if="review.content && review.content.length > 80" @click="toggleReview(index)">
							<text class="expand-text">{{ reviewExpanded[index] ? '收起' : '展开' }}</text>
						</view>
						
						<!-- 书评互动 -->
						<view class="review-actions">
							<view class="action-item" @click.stop="handleReviewLike(review, index)">
								<text class="action-icon" :class="{ 'active': review.isLiked }">👍</text>
								<text class="action-count" :class="{ 'active': review.isLiked }">{{ formatCount(review.likes) }}</text>
							</view>
							<view class="action-item" @click.stop="handleReviewDislike(review, index)">
								<text class="action-icon" :class="{ 'active': review.isDisliked }">👎</text>
								<text class="action-count" :class="{ 'active': review.isDisliked }">{{ formatCount(review.dislikes) }}</text>
							</view>
							<view class="action-item" @click.stop="handleReviewComment(review, index)">
								<text class="action-icon">💬</text>
								<text class="action-count">{{ formatCount(review.comments) }}</text>
							</view>
						</view>
					</view>
				</view>
				
				<!-- 没有书评数据时显示提示 -->
				<view class="no-reviews" v-else>
					<text class="no-reviews-text">暂无书评，点击"更多书评"查看全部</text>
				</view>
			</view>
			
			<!-- 开始阅读按钮 -->
			<view class="read-button-container">
				<view class="read-button" @click="startReading">
					<view class="arrow-up"></view>
					<text class="read-text">上滑开始阅读</text>
				</view>
			</view>
			
			<!-- 小说内容区域 -->
			<view class="novel-content-section">
				<view class="chapter-title">
					<text class="chapter-text">第一章 兰亭序全文（晋）王羲之</text>
				</view>
				<view class="novel-text">
					<text class="novel-paragraph">永和九年，岁在癸丑，暮春之初，会于会稽山阴之兰亭，修禊事也。群贤毕至，少长咸集。</text>
					<text class="novel-paragraph">此地有崇山峻岭，茂林修竹；又有清流激湍，映带左右，引以为流觞曲水，列坐其次。虽无丝竹管弦之盛，一觞一咏，亦足以畅叙幽情。</text>
					<text class="novel-paragraph">是日也，天朗气清，惠风和畅，仰观宇宙之大，俯察品类之盛，所以游目骋怀，足以极视听之娱，信可乐也。</text>
					<text class="novel-paragraph">夫人之相与，俯仰一世，或取诸怀抱，晤言一室之内；或因寄所托，放浪形骸之外。虽取舍万殊，静躁不同，当其欣于所遇，暂得于己，快然自足，不知老之将至。</text>
					<text class="novel-paragraph">及其所之既倦，情随事迁，感慨系之矣。向之所欣，俯仰之间，已为陈迹，犹不能不以之兴怀。况修短随化，终期于尽。</text>
					<text class="novel-paragraph">古人云："死生亦大矣。"岂不痛哉！每览昔人兴感之由，若合一契，未尝不临文嗟悼，不能喻之于怀。</text>
					<text class="novel-paragraph">固知一死生为虚诞，齐彭殇为妄作。后之视今，亦犹今之视昔。</text>
					<text class="novel-paragraph">悲夫！故列叙时人，录其所述，虽世殊事异，所以兴怀，其致一也。后之览者，亦将有感于斯文。</text>
				</view>
			</view>
			
			<!-- 底部留白 -->
			<view class="bottom-spacer"></view>
			</view> <!-- 结束正常内容区域 -->
		</scroll-view>
	</view>
</template>

<script>
	export default {
		name: 'NovelDetailPage',
		data() {
			return {
				novelId: '',
				userId: 1000100, // 默认用户ID
				novelTitle: '',
				synopsisExpanded: false,
				reviewExpanded: [false, false],
				showNovelContent: false,
				isFollowed: false,
				scrollTop: 0,
				loading: false,
				hasError: false,
				errorMessage: '',
				novelData: {
					title: '游戏降临: 我要这个这个还有这个 (游戏入侵)',
					author: '猫不秃',
					rating: '9.5',
					reviewCount: '11.9万人点评',
					readerCount: '157.4万人',
					wordCount: '214.7万字',
					updateDays: '连续更新268天',
					coverImage: '/static/novel-cover.jpg',
					tags: ['#原创小说', '#游戏体育', '#重生', '#无CP'],
					synopsis: '【游戏入侵+无cp+升级流+独狼+游戏】(又名: 游戏入侵) 这是一个关于游戏入侵现实的故事，主角在游戏中获得了强大的能力，开始了一段惊险刺激的冒险之旅。故事融合了游戏元素和现实世界，充满了悬念和惊喜。主角从一个普通玩家逐渐成长为游戏世界的强者，在这个过程中不仅要面对游戏中的挑战，还要处理现实世界中的各种问题。',
					reviews: [
						{
							reviewer: '书友123456',
							avatar: '/static/avatar1.png',
							content: '文笔剧情很好,很难得的女强无cp游戏侵入现实的文,世界观设计都很有画面感,女主也很聪明,抓...',
							readTime: '阅读不足30分钟后点评'
						},
						{
							reviewer: '书友789012',
							avatar: '/static/avatar2.png',
							content: '现在更的全部看完了,技能/道具/天赋设定都好新颖,不会一样望去就猜的大差不差食之无味。而且主角的成长路线很合理，每个阶段都有相应的挑战和收获，让人看得停不下来。',
							readTime: '阅读3小时后点评'
						}
					],
					chapters: [] // 章节列表
				}
			}
		},
		onLoad(options) {
			console.log('=== 小说详情页面加载 ===');
			console.log('接收到的参数:', options);
			
			// 接收传递的参数
			if (options.id) {
				this.novelId = options.id;
			}
			if (options.userId) {
				this.userId = parseInt(options.userId);
			}
			
			// 如果有传递完整参数，先使用传递的参数
			if (options.title) {
				this.novelTitle = decodeURIComponent(options.title);
				this.novelData.title = this.novelTitle;
			}
			if (options.author) {
				this.novelData.author = decodeURIComponent(options.author);
			}
			if (options.rating) {
				this.novelData.rating = decodeURIComponent(options.rating);
			}
			if (options.reviewCount) {
				this.novelData.reviewCount = decodeURIComponent(options.reviewCount);
			}
			if (options.readerCount) {
				this.novelData.readerCount = decodeURIComponent(options.readerCount);
			}
			if (options.wordCount) {
				this.novelData.wordCount = decodeURIComponent(options.wordCount);
			}
			if (options.updateDays) {
				this.novelData.updateDays = decodeURIComponent(options.updateDays);
			}
			if (options.tags) {
				this.novelData.tags = decodeURIComponent(options.tags).split(',');
			}
			if (options.synopsis) {
				this.novelData.synopsis = decodeURIComponent(options.synopsis);
			}
			
			// 如果有novelId，从后端加载完整数据
			if (this.novelId) {
				this.loadNovelDetailFromAPI();
			} else {
				// 如果没有novelId，使用本地数据
				this.loadNovelData();
			}
			
			console.log('更新后的小说数据:', this.novelData);
		},
		methods: {
			// 从后端API加载小说详情数据
			async loadNovelDetailFromAPI() {
				try {
					console.log('=== 开始从后端加载小说详情数据 ===');
					console.log('小说ID:', this.novelId);
					console.log('用户ID:', this.userId);
					console.log('请求URL:', `http://localhost:8888/api/novel-detail/data?novelId=${this.novelId}&userId=${this.userId}`);
					
					this.loading = true;
					this.hasError = false;
					this.errorMessage = '';
					
					const response = await uni.request({
						url: `http://localhost:8888/api/novel-detail/data?novelId=${this.novelId}&userId=${this.userId}`,
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('=== 小说详情API响应详情 ===');
					console.log('状态码:', response.statusCode);
					console.log('响应数据:', response.data);
					
					if (response.statusCode === 200 && response.data.success) {
						const apiData = response.data.data;
						
						// 更新小说数据
						this.novelData = {
							...this.novelData, // 保留现有数据
							...apiData, // 用API数据覆盖
							// 确保某些字段的格式正确
							title: apiData.title || this.novelData.title,
							author: apiData.author || this.novelData.author,
							rating: apiData.rating || this.novelData.rating,
							reviewCount: apiData.reviewCount || this.novelData.reviewCount,
							readerCount: apiData.readerCount || this.novelData.readerCount,
							wordCount: apiData.wordCount || this.novelData.wordCount,
							updateDays: apiData.updateDays || this.novelData.updateDays,
							coverImage: apiData.coverImage || this.novelData.coverImage,
							tags: apiData.tags || this.novelData.tags,
							synopsis: apiData.synopsis || this.novelData.synopsis,
							chapters: apiData.chapters || [],
							reviews: apiData.reviews || [] // 确保书评数据被正确设置
						};
						
						// 更新用户互动状态
						this.isFollowed = apiData.isFavorited || false;
						
						console.log('成功加载小说详情数据');
						console.log('章节数量:', this.novelData.chapters.length);
						console.log('书评数量:', this.novelData.reviews.length);
						console.log('书评数据:', this.novelData.reviews);
						
						// 显示成功提示
						uni.showToast({
							title: '加载成功',
							icon: 'success',
							duration: 1500
						});
						
					} else {
						console.error('加载小说详情数据失败:', response.data.message);
						this.hasError = true;
						this.errorMessage = response.data.message || '加载数据失败';
						uni.showToast({
							title: '加载数据失败',
							icon: 'error'
						});
					}
				} catch (error) {
					console.error('加载小说详情数据异常:', error);
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
			
			loadNovelData() {
				// 根据小说ID加载对应的小说数据
				// 这里可以根据实际需求从API或本地数据获取
				const novelDatabase = {
					1: {
						title: '神秘的猫又',
						author: '猫又屋之主',
						rating: '4.5',
						reviewCount: '2.3万人点评',
						readerCount: '45.2万人',
						wordCount: '156.8万字',
						updateDays: '连续更新156天',
						coverImage: '/static/novel-cover.png',
						tags: ['#原创小说', '#奇幻', '#猫又', '#冒险'],
						synopsis: '在一个古老的村庄里，流传着一个关于猫又的神秘传说。主角意外获得了猫又的力量，开始了一段惊险刺激的冒险之旅。故事融合了东方神话和现代元素，充满了悬念和惊喜。',
						reviews: [
							{
								reviewer: '书友猫奴001',
								avatar: '/static/avatar1.png',
								content: '猫又题材很新颖，文笔流畅，剧情紧凑。主角的成长路线很合理，每个阶段都有相应的挑战和收获。',
								readTime: '阅读2小时后点评'
							},
							{
								reviewer: '书友奇幻迷',
								avatar: '/static/avatar2.png',
								content: '世界观设定很有创意，猫又的能力系统设计得很完整。作者对细节的把控很到位，让人看得停不下来。',
								readTime: '阅读5小时后点评'
							}
						]
					},
					2: {
						title: '猫又的奇幻之旅',
						author: '猫又屋之主',
						rating: '4.8',
						reviewCount: '3.1万人点评',
						readerCount: '67.8万人',
						wordCount: '298.5万字',
						updateDays: '连续更新298天',
						coverImage: '/static/novel-cover.png',
						tags: ['#原创小说', '#奇幻', '#猫又', '#成长'],
						synopsis: '猫又发现了一个通往神秘世界的传送门。在这个充满魔法和危险的世界里，她必须学会运用自己的力量，结交朋友，面对各种挑战。这是一个关于成长、友情和勇气的故事。',
						reviews: [
							{
								reviewer: '书友冒险家',
								avatar: '/static/avatar3.png',
								content: '传送门设定很吸引人，每个世界都有独特的规则和文化。主角的成长过程很真实，不是一蹴而就的。',
								readTime: '阅读8小时后点评'
							},
							{
								reviewer: '书友魔法师',
								avatar: '/static/avatar4.png',
								content: '魔法系统设计得很完整，世界观构建得很详细。每个角色都有鲜明的个性，剧情发展很合理。',
								readTime: '阅读12小时后点评'
							}
						]
					},
					4: {
						title: '游戏降临: 我要这个这个还有这个 (游戏入侵)',
						author: '猫不秃',
						rating: '9.5',
						reviewCount: '11.9万人点评',
						readerCount: '157.4万人',
						wordCount: '214.7万字',
						updateDays: '连续更新268天',
						coverImage: '/static/novel-cover.jpg',
						tags: ['#原创小说', '#游戏体育', '#重生', '#无CP'],
						synopsis: '【游戏入侵+无cp+升级流+独狼+游戏】(又名: 游戏入侵) 这是一个关于游戏入侵现实的故事，主角在游戏中获得了强大的能力，开始了一段惊险刺激的冒险之旅。故事融合了游戏元素和现实世界，充满了悬念和惊喜。主角从一个普通玩家逐渐成长为游戏世界的强者，在这个过程中不仅要面对游戏中的挑战，还要处理现实世界中的各种问题。',
						reviews: [
							{
								reviewer: '书友123456',
								avatar: '/static/avatar1.png',
								content: '文笔剧情很好,很难得的女强无cp游戏侵入现实的文,世界观设计都很有画面感,女主也很聪明,抓...',
								readTime: '阅读不足30分钟后点评'
							},
							{
								reviewer: '书友789012',
								avatar: '/static/avatar2.png',
								content: '现在更的全部看完了,技能/道具/天赋设定都好新颖,不会一样望去就猜的大差不差食之无味。而且主角的成长路线很合理，每个阶段都有相应的挑战和收获，让人看得停不下来。',
								readTime: '阅读3小时后点评'
							}
						]
					}
				};
				
				// 根据ID获取小说数据
				if (novelDatabase[this.novelId]) {
					const novel = novelDatabase[this.novelId];
					this.novelData = {
						...this.novelData, // 保留默认值
						...novel // 用新数据覆盖
					};
					
					// 重置展开状态
					this.synopsisExpanded = false;
					this.reviewExpanded = new Array(novel.reviews.length).fill(false);
				}
			},
			
			goBack() {
				uni.navigateBack();
			},
			startListening() {
				uni.showToast({
					title: '听书功能开发中',
					icon: 'none'
				});
			},
			followAuthor() {
				this.isFollowed = !this.isFollowed;
				uni.showToast({
					title: this.isFollowed ? '关注成功' : '取消关注',
					icon: 'success'
				});
			},
			toggleSynopsis() {
				this.synopsisExpanded = !this.synopsisExpanded;
			},
			toggleReview(index) {
				this.$set(this.reviewExpanded, index, !this.reviewExpanded[index]);
			},
			goToMoreReviews() {
				uni.navigateTo({
					url: `/pages/feed/novel-more-reviews?novelId=${this.novelId}&title=${encodeURIComponent(this.novelData.title)}&author=${encodeURIComponent(this.novelData.author)}`
				});
			},
			
			// 书评互动方法
			async handleReviewLike(review, index) {
				try {
					console.log('=== 开始处理书评点赞 ===');
					console.log('用户ID:', this.userId);
					console.log('评论ID:', review.commentId);
					
					const response = await uni.request({
						url: `http://localhost:8888/api/novel-detail/comment/like?userId=${this.userId}&commentId=${review.commentId}`,
						method: 'POST',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('=== 书评点赞API响应 ===');
					console.log('状态码:', response.statusCode);
					console.log('响应数据:', response.data);
					
					if (response.statusCode === 200 && response.data.success) {
						const result = response.data.data;
						
						// 更新书评状态
						review.isLiked = result.isLiked;
						review.likes = result.likeCount;
						
						// 如果点赞了，取消点踩状态
						if (review.isLiked && review.isDisliked) {
							review.isDisliked = false;
							review.dislikes--;
						}
						
						uni.showToast({
							title: result.message,
							icon: 'success',
							duration: 1000
						});
					} else {
						console.error('书评点赞失败:', response.data.message);
						uni.showToast({
							title: response.data.message || '操作失败',
							icon: 'error'
						});
					}
				} catch (error) {
					console.error('书评点赞异常:', error);
					uni.showToast({
						title: '网络错误',
						icon: 'error'
					});
				}
			},
			
			handleReviewDislike(review, index) {
				review.isDisliked = !review.isDisliked;
				if (review.isDisliked) {
					review.dislikes++;
					if (review.isLiked) {
						review.isLiked = false;
						review.likes--;
					}
				} else {
					review.dislikes--;
				}
				
				uni.showToast({
					title: review.isDisliked ? '点踩成功' : '取消点踩',
					icon: 'success',
					duration: 1000
				});
			},
			
			handleReviewComment(review, index) {
				// 跳转到更多书评页面，并定位到该评论
				uni.navigateTo({
					url: `/pages/feed/novel-more-reviews?novelId=${this.novelId}&title=${encodeURIComponent(this.novelData.title)}&author=${encodeURIComponent(this.novelData.author)}&commentId=${review.commentId}`
				});
			},
			
			// 格式化数字显示
			formatCount(count) {
				if (count >= 10000) {
					return (count / 10000).toFixed(1) + 'w';
				}
				return count.toString();
			},
			startReading() {
				// 直接设置滚动位置到小说内容区域
				console.log('开始滚动到小说内容区域');
				this.scrollTop = 1200; // 增加滚动距离，让页面顶部跳到第一章标题
				
				// 备用方案：使用 uni.pageScrollTo，提高滑动速度
				setTimeout(() => {
					uni.pageScrollTo({
						scrollTop: 1120,
						duration: 100 // 减少动画时间，提高滑动速度
					});
				}, 50); // 减少延迟时间
			},
			onScroll(e) {
				// 可以在这里处理滚动事件
			}
		}
	}
</script>

<style>
	.novel-detail-page {
		min-height: 100vh;
		background: linear-gradient(180deg, #FFFFFF 0%, #F8F8F8 100%);
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
	
	/* 头部导航 */
	.header {
		height: 88rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 32rpx;
		border-bottom: 1rpx solid #F0F0F0;
		background-color: #FFFFFF;
		position: sticky;
		top: 0;
		z-index: 100;
	}
	
	.back-button {
		width: 60rpx;
		height: 60rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		background-color: #F5F5F5;
		border-radius: 50%;
	}
	
	.back-arrow {
		width: 0;
		height: 0;
		border-right: 12rpx solid #333333;
		border-top: 8rpx solid transparent;
		border-bottom: 8rpx solid transparent;
	}
	
	.header-title {
		flex: 1;
		text-align: center;
	}
	
	.title-text {
		font-size: 32rpx;
		color: #333333;
		font-weight: 700;
	}
	
	.listen-button {
		width: 80rpx;
		height: 80rpx;
		background: linear-gradient(135deg, #FF69B4, #FF8E53);
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: 0 4rpx 16rpx rgba(255, 105, 180, 0.3);
	}
	
	.listen-text {
		font-size: 28rpx;
		color: #FFFFFF;
		font-weight: 600;
	}
	
	/* 主要内容区域 */
	.content-scroll {
		flex: 1;
		height: calc(100vh - 88rpx);
		background-color: transparent;
	}
	
	/* 返回按钮容器 */
	.back-button-container {
		position: fixed;
		top: 60rpx;
		left: 32rpx;
		z-index: 100;
	}
	
	.back-button {
		width: 60rpx;
		height: 60rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		background-color: rgba(255, 255, 255, 0.9);
		border-radius: 50%;
		box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.1);
	}
	
	.back-arrow {
		width: 0;
		height: 0;
		border-right: 12rpx solid #333333;
		border-top: 8rpx solid transparent;
		border-bottom: 8rpx solid transparent;
	}
	
	/* 小说封面 */
	.novel-cover-section {
		padding: 32rpx;
		display: flex;
		justify-content: center;
	}
	
	.novel-cover {
		width: 320rpx;
		height: 420rpx;
		border-radius: 20rpx;
		background-color: #F5F5F5;
		box-shadow: 0 12rpx 32rpx rgba(0, 0, 0, 0.15);
		border: 4rpx solid #FFFFFF;
	}
	
	/* 小说信息区域 */
	.novel-info-section { 
		padding: 24rpx 32rpx; 
		background-color: #FFFFFF;
		margin: 0 32rpx 24rpx;
		border-radius: 20rpx;
		box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.05);
	}
	
	.novel-title { 
		font-size: 36rpx; 
		color: #333333; 
		font-weight: 700; 
		margin-bottom: 16rpx; 
		display: block; 
		line-height: 1.4;
	}
	
	.author-section { 
		display: flex; 
		align-items: center; 
		gap: 16rpx; 
		margin-top: 12rpx; 
	}
	
	.author-name { 
		font-size: 28rpx; 
		color: #666666; 
	}
	
	.follow-button { 
		background: linear-gradient(135deg, #FF69B4, #FF8E53); 
		padding: 12rpx 24rpx; 
		border-radius: 24rpx; 
		box-shadow: 0 4rpx 12rpx rgba(255, 105, 180, 0.3);
	}
	
	.follow-text { 
		font-size: 24rpx; 
		font-weight: 600; 
		color: #FFFFFF;
	}
	
	/* 统计数据 */
	.stats-section { 
		display: flex; 
		justify-content: space-around; 
		padding: 32rpx; 
		background-color: #F8F8F8; 
		border-radius: 20rpx; 
		margin: 0 32rpx 24rpx; 
		box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.05);
	}
	
	.stat-item { 
		text-align: center; 
		flex: 1;
	}
	
	.stat-value { 
		font-size: 32rpx; 
		color: #FF6B35; 
		font-weight: 700; 
		display: block;
		margin-bottom: 8rpx;
	}
	
	.stat-label { 
		font-size: 24rpx; 
		color: #999999; 
		display: block;
	}
	
	/* 简介部分 */
	.synopsis-section { 
		padding: 24rpx 32rpx; 
		margin: 0 32rpx 24rpx;
		background-color: #FFFFFF;
		border-radius: 20rpx;
		box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.05);
	}
	
	.section-header { 
		display: flex; 
		justify-content: space-between; 
		align-items: center; 
		margin-bottom: 16rpx; 
	}
	
	.section-title { 
		font-size: 32rpx; 
		color: #333333; 
		font-weight: 600; 
	}
	
	.tags-container { 
		display: flex; 
		flex-wrap: wrap; 
		gap: 12rpx; 
		margin: 16rpx 0; 
	}
	
	.tag { 
		background: linear-gradient(135deg, #FFE4E1, #FFF0F5); 
		padding: 10rpx 20rpx; 
		border-radius: 24rpx; 
		box-shadow: 0 2rpx 8rpx rgba(255, 107, 53, 0.2);
	}
	
	.tag-text { 
		font-size: 24rpx; 
		color: #FF6B35;
		font-weight: 500;
	}
	
	.synopsis-content { 
		position: relative; 
		margin-top: 16rpx;
	}
	
	.synopsis-text { 
		font-size: 28rpx; 
		color: #666666; 
		line-height: 1.6; 
		display: -webkit-box;
		-webkit-line-clamp: 3;
		-webkit-box-orient: vertical;
		overflow: hidden;
	}
	
	.synopsis-text.expanded {
		display: block;
		-webkit-line-clamp: unset;
		overflow: visible;
	}
	
	.expand-button { 
		display: flex;
		justify-content: flex-end;
		margin-top: 16rpx;
	}
	
	.expand-text { 
		font-size: 28rpx; 
		color: #FF6B35; 
		font-weight: 600; 
	}
	
	/* 热门书评 */
	.reviews-section { 
		padding: 24rpx 32rpx; 
		margin: 0 32rpx 24rpx;
		background-color: #FFFFFF;
		border-radius: 20rpx;
		box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.05);
	}
	
	.more-reviews { 
		display: flex; 
		align-items: center; 
		gap: 8rpx; 
		padding: 8rpx 16rpx;
		background-color: #F5F5F5;
		border-radius: 20rpx;
	}
	
	.more-text { 
		font-size: 26rpx; 
		color: #FF6B35; 
		font-weight: 600; 
	}
	
	.reviews-list { 
		display: flex; 
		flex-direction: column; 
		gap: 32rpx; 
		margin-top: 16rpx;
	}
	
	.review-item { 
		display: flex; 
		flex-direction: column; 
		gap: 16rpx; 
	}
	
	.review-header { 
		display: flex; 
		align-items: center; 
		gap: 16rpx; 
	}
	
	.reviewer-avatar { 
		width: 60rpx; 
		height: 60rpx; 
		border-radius: 30rpx; 
		background-color: #F5F5F5; 
		box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
	}
	
	.review-info { 
		display: flex; 
		flex-direction: column; 
		gap: 4rpx; 
		flex: 1;
	}
	
	.reviewer-name { 
		font-size: 28rpx; 
		color: #333333; 
		font-weight: 600; 
	}
	
	.review-rating { 
		display: flex; 
		gap: 4rpx; 
		margin-bottom: 4rpx;
	}
	
	.star { 
		font-size: 24rpx; 
		color: #FFD700; 
	}
	
	.review-time { 
		font-size: 24rpx; 
		color: #999999; 
	}
	
	.review-content { 
		font-size: 28rpx; 
		color: #333333; 
		line-height: 1.6; 
		margin-left: 76rpx; 
		display: -webkit-box;
		-webkit-line-clamp: 3;
		-webkit-box-orient: vertical;
		overflow: hidden;
	}
	
	.review-content.expanded {
		display: block;
		-webkit-line-clamp: unset;
		overflow: visible;
	}
	
	/* 书评互动区域 */
	.review-actions {
		display: flex;
		gap: 24rpx;
		margin-top: 16rpx;
		padding-top: 16rpx;
		border-top: 1rpx solid #F0F0F0;
	}
	
	.review-actions .action-item {
		display: flex;
		align-items: center;
		gap: 8rpx;
	}
	
	.review-actions .action-icon {
		font-size: 28rpx;
		color: #666666;
		transition: all 0.2s ease;
	}
	
	.review-actions .action-count {
		font-size: 22rpx;
		color: #666666;
	}
	
	.review-actions .action-icon.active {
		color: #FFD700;
		transform: scale(1.1);
	}
	
	.review-actions .action-count.active {
		color: #FFD700;
		font-weight: 600;
	}
	
	/* 暂无书评提示 */
	.no-reviews {
		padding: 40rpx 20rpx;
		text-align: center;
		background-color: #F8F8F8;
		border-radius: 12rpx;
		margin-top: 16rpx;
	}
	
	.no-reviews-text {
		font-size: 26rpx;
		color: #999999;
		line-height: 1.5;
	}
	
	/* 小说内容区域 */
	.novel-content-section {
		padding: 32rpx;
		margin: 24rpx 32rpx;
		/* background-color: #FFFFFF;
		margin: 24rpx 32rpx;
		border-radius: 20rpx;
		box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.05); */
	}
	
	.chapter-title {
		text-align: left;
		margin-bottom: 32rpx;
		padding-bottom: 24rpx;
		border-bottom: 2rpx solid #F0F0F0;
	}
	
	.chapter-text {
		font-size: 36rpx;
		color: #333333;
		font-weight: 700;
	}
	
	.novel-text {
		display: flex;
		flex-direction: column;
		gap: 24rpx;
	}
	
	.novel-paragraph {
		font-size: 30rpx;
		color: #333333;
		line-height: 1.8;
		text-align: justify;
	}
	
	/* 底部留白 */
	.bottom-spacer { 
		height: 120rpx; 
		background-color: transparent;
	}
	
	/* 开始阅读按钮 */
	.read-button-container {
		padding: 60rpx 32rpx;
		display: flex;
		justify-content: center;
		margin-bottom: 60rpx;
	}
	
	.read-button {
		width: 300rpx;
		height: 80rpx;
		/* background-color: #FFFFFF; */
		border: 2rpx solid #E0E0E0;
		border-radius: 40rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		gap: 12rpx;
		/* box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1); */
	}
	
	.arrow-up {
		width: 0;
		height: 0;
		border-left: 8rpx solid transparent;
		border-right: 8rpx solid transparent;
		border-bottom: 12rpx solid #333333;
	}
	
	.read-text {
		font-size: 25rpx;
		color: #333333;
		font-weight: 400;
	}
	
	/* 加载状态样式 */
	.loading-container {
		display: flex;
		justify-content: center;
		align-items: center;
		padding: 80rpx 40rpx;
		background-color: #F5F5F5;
		min-height: 400rpx;
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

