<template>
	<view class="friend-list">
		<!-- 顶部状态栏和导航 -->
		<view class="header">
			<view class="nav-header">
				<view class="back-button" @click="goBack">
					<svg t="1756277721385" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="848" width="32" height="32">
						<path d="M407.01 512l286.008-286.008a35.84 35.84 0 0 0-50.683-50.683L330.982 486.656a35.84 35.84 0 0 0 0 50.683L642.34 848.69a35.84 35.84 0 0 0 50.683-50.683L407.009 512z" fill="#666666" p-id="849"></path>
					</svg>
				</view>
				<view class="header-title">好友</view>
				<view class="placeholder"></view>
			</view>
		</view>
		
		<!-- 好友列表内容 -->
		<scroll-view class="friend-content" scroll-y>
			<!-- 搜索框 -->
			<view class="search-container">
				<view class="search-box">
					<view class="search-icon">
						<svg t="1756202042594" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="6084" width="24" height="24">
							<path d="M446.112323 177.545051c137.567677 0.219798 252.612525 104.59798 266.162424 241.493333 13.562828 136.895354-78.778182 261.818182-213.617777 289.008485-134.852525 27.203232-268.386263-52.156768-308.945455-183.608889s25.018182-272.252121 151.738182-325.779394A267.235556 267.235556 0 0 1 446.112323 177.545051m0-62.060607c-182.794343 0-330.989899 148.195556-330.989899 330.989899s148.195556 330.989899 330.989899 330.989899 330.989899-148.195556 330.989899-330.989899-148.195556-330.989899-330.989899-330.989899z m431.321212 793.341415a30.849293 30.849293 0 0 1-21.94101-9.102223l-157.220202-157.220202c-11.752727-12.179394-11.584646-31.534545 0.37495-43.50707 11.972525-11.972525 31.327677-12.140606 43.494141-0.37495l157.220202 157.220202a31.036768 31.036768 0 0 1 6.723232 33.810101 31.004444 31.004444 0 0 1-28.651313 19.174142z m0 0" p-id="6085" fill="#2c2c2c"></path>
						</svg>
					</view>
					<input 
						class="search-input" 
						type="text" 
						v-model="searchKeyword" 
						placeholder="搜索好友..." 
						@input="onSearchInput"
						@focus="onSearchFocus"
						@blur="onSearchBlur"
					/>
					<view v-if="searchKeyword" class="clear-button" @click="clearSearch">
						<svg class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="20" height="20">
							<path d="M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64z m165.4 618.2l-66-.3L512 563.4l-99.3 118.4-66.1.3c-4.4 0-8-3.5-8-8 0-1.9.7-3.7 1.9-5.2l130.1-155L340.5 359a8.32 8.32 0 0 1-1.9-5.2c0-4.4 3.6-8 8-8l66.1.3L512 464.6l99.3-118.4 66-.3c4.4 0 8 3.5 8 8 0 1.9-.7 3.7-1.9 5.2L553.5 514l130 155c1.2 1.5 1.9 3.3 1.9 5.2 0 4.4-3.6 8-8 8z" fill="#999999"></path>
						</svg>
					</view>
				</view>
			</view>

			<!-- 加载状态 -->
			<view v-if="loading" class="loading-container">
				<view class="loading-spinner"></view>
				<text class="loading-text">加载中...</text>
			</view>

			<!-- 错误状态 -->
			<view v-else-if="errorMessage" class="error-container">
				<view class="error-icon">⚠️</view>
				<text class="error-text">{{ errorMessage }}</text>
				<button class="retry-button" @click="loadFriends">
					<text class="retry-text">重试</text>
				</button>
			</view>

			<!-- 好友列表 -->
			<view v-else-if="Object.keys(displayedFriends).length > 0">
				<view v-for="(friends, letter) in displayedFriends" :key="letter" class="letter-section">
					<view class="letter-header">{{ letter }}</view>
					<view class="friend-item" v-for="(friend, index) in friends" :key="index" @click="selectFriend(friend)">
						<view class="friend-avatar">
							<image class="avatar-image" :src="friend.avatarUrl" mode="aspectFill"></image>
						</view>
						<text class="friend-name">{{ friend.friendName }}</text>
					</view>
				</view>
			</view>

			<!-- 空状态 -->
			<view v-else class="empty-container">
				<view class="empty-icon">{{ searchKeyword ? '🔍' : '👥' }}</view>
				<text class="empty-text">{{ searchKeyword ? '未找到相关好友' : '暂无好友' }}</text>
				<text class="empty-desc">{{ searchKeyword ? '试试其他关键词' : '快去添加一些好友吧' }}</text>
			</view>
		</scroll-view>
		
		<!-- 底部导航栏 -->
		<view class="bottom-navigation">
			<view class="nav-item" @click="goToHome">
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
			<view class="nav-item active" @click="goToFriendList">
				<text class="nav-text">好友</text>
			</view>
			<view class="nav-avatar" @click="goToProfile">
				<image class="avatar-small" src="" mode="aspectFill"></image>
			</view>
		</view>
		
		<!-- 底部手势条 -->
		<view class="home-indicator"></view>

	</view>
</template>

<script>
	export default {
		data() {
			return {
				userId: null,
				friends: [],
				groupedFriends: {},
				displayedFriends: {},
				searchKeyword: '',
				loading: false,
				errorMessage: ''
			};
		},
		onLoad() {
			// 获取用户ID，这里假设从本地存储或全局状态获取
			this.userId = uni.getStorageSync('userId') || 1000001; // 默认用户ID为1000001
			console.log('好友列表页面加载，userId:', this.userId);
			this.loadFriends();
		},
		methods: {
			// 加载好友列表
			async loadFriends() {
				if (!this.userId) {
					this.errorMessage = '用户ID不存在';
					return;
				}

				this.loading = true;
				this.errorMessage = '';

				try {
					console.log('开始加载好友列表，userId:', this.userId);
					const response = await uni.request({
						url: `http://localhost:8888/api/u-entities/user-chat-list/user/${this.userId}`,
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});

					console.log('好友列表API响应状态码:', response.statusCode);
					console.log('好友列表API响应数据:', response.data);

					if (response.statusCode === 200 && response.data) {
						this.friends = this.convertChatListToFriends(response.data);
						this.groupedFriends = this.groupFriendsByFirstLetter(this.friends);
						this.displayedFriends = this.groupedFriends; // 初始显示所有好友
						console.log('好友列表加载成功:', this.friends);
						console.log('分组后的好友列表:', this.groupedFriends);
					} else {
						this.errorMessage = `加载好友列表失败，状态码: ${response.statusCode}`;
						console.error('加载好友列表失败:', response);
					}
				} catch (error) {
					this.errorMessage = '网络错误，请检查连接';
					console.error('加载好友列表异常:', error);
				} finally {
					this.loading = false;
				}
			},

			// 转换聊天列表数据为好友显示格式
			convertChatListToFriends(chatListData) {
				if (!Array.isArray(chatListData)) {
					return [];
				}

				return chatListData.map(chat => {
					// 处理头像URL，如果为空或无效则使用默认头像
					let avatarUrl = chat.avatarUrl;
					if (!avatarUrl || avatarUrl.trim() === '' || avatarUrl === 'null') {
						avatarUrl = '/static/default-avatar.png';
					}

					// 处理好友名字，优先使用realName，其次username，最后使用默认值
					let friendName = chat.friendName;
					if (!friendName || friendName.trim() === '' || friendName === 'null') {
						friendName = '未知用户';
					}

					return {
						friendId: chat.friendId,
						friendName: friendName,
						avatarUrl: avatarUrl,
						status: 'online', // 默认状态
						lastActive: chat.lastMessageTime,
						createdAt: chat.createdAt,
						sessionId: chat.sessionId
					};
				});
			},

			// 按首字母分组好友
			groupFriendsByFirstLetter(friends) {
				const grouped = {};
				
				friends.forEach(friend => {
					const firstLetter = this.getFirstLetter(friend.friendName || '');
					if (!grouped[firstLetter]) {
						grouped[firstLetter] = [];
					}
					grouped[firstLetter].push(friend);
				});

				// 对每个分组内的好友按名称排序
				Object.keys(grouped).forEach(letter => {
					grouped[letter].sort((a, b) => {
						return (a.friendName || '').localeCompare(b.friendName || '');
					});
				});

				// 对字母组进行排序，创建有序的对象
				const sortedGrouped = {};
				const sortedLetters = Object.keys(grouped).sort((a, b) => {
					// 特殊处理：'#' 组放在最后
					if (a === '#') return 1;
					if (b === '#') return -1;
					// 其他字母按字母顺序排序
					return a.localeCompare(b);
				});

				sortedLetters.forEach(letter => {
					sortedGrouped[letter] = grouped[letter];
				});

				return sortedGrouped;
			},

			// 获取首字母（简化版拼音首字母）
			getFirstLetter(name) {
				if (!name) return '#';
				
				const firstChar = name.charAt(0);
				
				// 如果是英文字母
				if (/[A-Za-z]/.test(firstChar)) {
					return firstChar.toUpperCase();
				}
				
				// 扩展的中文拼音首字母映射
				const pinyinMap = {
					// A组
					'阿': 'A', '安': 'A', '艾': 'A', '爱': 'A',
					// B组
					'白': 'B', '北': 'B', '本': 'B', '包': 'B', '边': 'B',
					// C组
					'陈': 'C', '程': 'C', '成': 'C', '春': 'C', '蔡': 'C', '曹': 'C',
					// D组
					'大': 'D', '丁': 'D', '东': 'D', '邓': 'D', '段': 'D',
					// E组
					'二': 'E', '恩': 'E',
					// F组
					'方': 'F', '冯': 'F', '付': 'F', '风': 'F', '范': 'F', '费': 'F',
					// G组
					'高': 'G', '郭': 'G', '顾': 'G', '关': 'G', '谷': 'G',
					// H组
					'韩': 'H', '何': 'H', '黄': 'H', '胡': 'H', '华': 'H', '侯': 'H',
					// I组
					'伊': 'I',
					// J组
					'江': 'J', '金': 'J', '贾': 'J', '蒋': 'J', '姜': 'J', '季': 'J',
					// K组
					'康': 'K', '孔': 'K', '柯': 'K',
					// L组
					'李': 'L', '刘': 'L', '林': 'L', '卢': 'L', '梁': 'L', '罗': 'L', '陆': 'L',
					// M组
					'马': 'M', '毛': 'M', '孟': 'M', '莫': 'M', '米': 'M',
					// N组
					'南': 'N', '牛': 'N', '倪': 'N', '奶': 'N', '宁': 'N', '聂': 'N',
					// O组
					'欧': 'O', '欧': 'O',
					// P组
					'潘': 'P', '彭': 'P', '皮': 'P',
					// Q组
					'钱': 'Q', '秦': 'Q', '邱': 'Q', '齐': 'Q',
					// R组
					'任': 'R', '阮': 'R', '荣': 'R',
					// S组
					'孙': 'S', '宋': 'S', '苏': 'S', '沈': 'S', '石': 'S', '史': 'S',
					// T组
					'唐': 'T', '田': 'T', '陶': 'T', '汤': 'T', '谭': 'T',
					// U组
					'吴': 'U', '武': 'U',
					// V组
					'王': 'V', '魏': 'V', '韦': 'V', '温': 'V',
					// W组
					'王': 'W', '吴': 'W', '武': 'W', '魏': 'W', '韦': 'W', '温': 'W',
					// X组
					'谢': 'X', '徐': 'X', '许': 'X', '夏': 'X', '星': 'X', '小': 'X', '肖': 'X', '薛': 'X',
					// Y组
					'杨': 'Y', '叶': 'Y', '于': 'Y', '袁': 'Y', '砚': 'Y', '夜': 'Y', '姚': 'Y', '严': 'Y', '易': 'Y',
					// Z组
					'张': 'Z', '赵': 'Z', '周': 'Z', '朱': 'Z', '郑': 'Z', '钟': 'Z', '曾': 'Z'
				};
				
				return pinyinMap[firstChar] || '#';
			},

			goBack() {
				uni.navigateBack();
			},
		
			selectFriend(friend) {
				// 跳转到聊天界面，并传递好友信息
				const friendName = friend.friendName || friend;
				const friendId = friend.friendId || null;
				const sessionId = friend.sessionId || 10000000; // 使用从聊天列表获取的sessionId
				
				console.log('选择好友:', {
					friendName,
					friendId,
					sessionId
				});
				
				uni.navigateTo({
					url: `/pages/chat/chat?friendName=${encodeURIComponent(friendName)}&friendId=${friendId}&sessionId=${sessionId}`
				});
			},
			goToHome() {
				uni.navigateTo({
					url: '/pages/feed/content-feed'
				});
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
				// 当前页面，无需跳转
			},
			goToProfile() {
				uni.navigateTo({
					url: '/pages/feed/user-profile'
				});
			},

			// 搜索相关方法
			onSearchInput() {
				this.filterFriends();
			},

			onSearchFocus() {
				console.log('搜索框获得焦点');
			},

			onSearchBlur() {
				console.log('搜索框失去焦点');
			},

			clearSearch() {
				this.searchKeyword = '';
				this.filterFriends();
			},

			// 过滤好友
			filterFriends() {
				if (!this.searchKeyword || this.searchKeyword.trim() === '') {
					// 如果没有搜索关键词，显示所有好友
					this.displayedFriends = this.groupedFriends;
				} else {
					// 根据搜索关键词过滤好友
					const keyword = this.searchKeyword.toLowerCase().trim();
					const filteredFriends = this.friends.filter(friend => {
						const friendName = (friend.friendName || '').toLowerCase();
						return friendName.includes(keyword);
					});
					
					// 重新分组过滤后的好友
					this.displayedFriends = this.groupFriendsByFirstLetter(filteredFriends);
				}
				
				console.log('搜索关键词:', this.searchKeyword);
				console.log('过滤后的好友:', this.displayedFriends);
			}
		}
	}
</script>

<style lang="scss" scoped>
	.friend-list {
		height: 100vh;
		background-color: #FAFAFA;
		display: flex;
		flex-direction: column;
		overflow: hidden;
	}

	.header {
		background-color: #FFFFFF;
		border-bottom: 1rpx solid #efefef;
	}

	.nav-header {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 16rpx 32rpx;
		height: 88rpx;
	}

	.back-button {
		width: 60rpx;
		height: 60rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
	}

	.header-title {
		font-size: 32rpx;
		color: #333333;
		font-weight: 600;
	}

	.placeholder {
		width: 60rpx;
	}

	/* 搜索框样式 */
	.search-container {
		padding: 16rpx 32rpx;
		background-color: #FFFFFF;
		border-bottom: 1rpx solid #F0F0F0;
		display: flex;
		justify-content: center;
	}

	.search-box {
		display: flex;
		align-items: center;
		background-color: #F0F0F0;
		border-radius: 24rpx;
		padding: 8rpx 16rpx;
		width: 90%;
		max-width: 500rpx;
		position: relative;
	}

	.search-icon {
		width: 24rpx;
		height: 24rpx;
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

	.search-input {
		flex: 1;
		font-size: 24rpx;
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

	.clear-button {
		width: 32rpx;
		height: 32rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
		margin-left: 8rpx;
		flex-shrink: 0;
	}

	.clear-button svg {
		width: 100%;
		height: 100%;
	}

	.clear-button:active {
		opacity: 0.6;
		transform: scale(0.9);
	}

	.friend-content {
		flex: 1;
		padding: 0;
		overflow-y: auto;
		padding-bottom: 140rpx; /* 为固定的底部导航栏留出空间 */
		margin-top: 20rpx;
	}

	.letter-section {
		margin-bottom: 20rpx;
	}

	.letter-header {
		background-color: #F5F5F5;
		padding: 12rpx 32rpx;
		font-size: 28rpx;
		font-weight: 600;
		color: #666666;
		border-top: 1rpx solid #E0E0E0;
		border-bottom: 1rpx solid #E0E0E0;
	}

	.friend-item {
		display: flex;
		align-items: center;
		padding: 16rpx 32rpx;
		background-color: #FFFFFF;
		border-bottom: 1rpx solid #efefef;
		cursor: pointer;
	}

	.friend-item:active {
		background-color: #f5f5f5;
	}

	.friend-avatar {
		margin-right: 24rpx;
	}

	.avatar-image {
		width: 90rpx;
		height: 90rpx;
		border-radius: 45rpx;
		background-color: #e5e5e5;
	}

	.friend-name {
		font-size: 32rpx;
		color: #333333;
		font-weight: 500;
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

	/* 加载状态样式 */
	.loading-container {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 80rpx 32rpx;
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
		padding: 80rpx 32rpx;
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

	/* 空状态样式 */
	.empty-container {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 120rpx 32rpx;
		gap: 24rpx;
	}

	.empty-icon {
		font-size: 120rpx;
		margin-bottom: 16rpx;
		opacity: 0.6;
	}

	.empty-text {
		font-size: 32rpx;
		color: #333333;
		font-weight: 600;
		margin-bottom: 8rpx;
	}

	.empty-desc {
		font-size: 28rpx;
		color: #666666;
		text-align: center;
	}

	/* 底部手势条 */
	.home-indicator {
		position: fixed;
		bottom: 8rpx;
		left: 50%;
		transform: translateX(-50%);
		width: 134rpx;
		height: 5rpx;
		background-color: #000;
		border-radius: 3rpx;
		z-index: 999;
	}
	

</style>