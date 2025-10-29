<template>
	<view class="chat-page">
		<!-- 状态栏 -->
		<!-- <view class="status-bar">
			<text class="time">9:41</text>
			<view class="status-icons">
				<view class="signal"></view>
				<view class="wifi"></view>
				<view class="battery"></view>
			</view>
		</view> -->
		
		<!-- 头部栏 -->
		<view class="header">
			<view class="back-btn" @click="goBack">
				<svg class="back-icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="32" height="32">
					<path d="M407.01 512l286.008-286.008a35.84 35.84 0 0 0-50.683-50.683L330.982 486.656a35.84 35.84 0 0 0 0 50.683L642.34 848.69a35.84 35.84 0 0 0 50.683-50.683L407.009 512z" fill="#666666" p-id="727"></path>
				</svg>
			</view>
			<text class="title">{{ currentAiModel.modelName || 'AI 助手' }}</text>
			<view class="more-btn" @click="showMoreOptions">
				<view class="dot"></view>
				<view class="dot"></view>
				<view class="dot"></view>
			</view>
		</view>
		
		<!-- 聊天对话区域 -->
		<scroll-view class="chat-area" scroll-y="true" :scroll-top="scrollTop">
			<!-- AI模型加载状态 -->
			<view v-if="loading" class="loading-container">
				<view class="loading-spinner"></view>
				<text class="loading-text">正在加载AI模型...</text>
			</view>
			
			<!-- 聊天记录加载状态 -->
			<view v-else-if="chatLoading" class="loading-container">
				<view class="loading-spinner"></view>
				<text class="loading-text">正在加载聊天记录...</text>
			</view>
			
			<!-- 聊天消息列表 -->
			<template v-else-if="messages.length > 0">
				<view 
					v-for="message in messages" 
					:key="message.id"
					:class="['message', message.type]"
				>
					<image v-if="message.type === 'left' && currentAiModel.modelImageUrl" class="avatar" :src="currentAiModel.modelImageUrl" mode="aspectFill"></image>
					<view v-else-if="message.type === 'left'" class="avatar avatar-placeholder">
						<text class="avatar-text">{{ getAvatarText() }}</text>
					</view>
					<view class="message-bubble">
						<text class="message-text">{{ message.text }}</text>
						<text v-if="message.time" class="message-time">{{ message.time }}</text>
					</view>
					<image v-if="message.type === 'right'" class="avatar" src="/static/logo.png" mode="aspectFill"></image>
				</view>
			</template>
			
			<!-- 无消息时显示提示 -->
			<view v-else class="no-messages">
				<text class="no-messages-text">暂无聊天记录</text>
			</view>
		</scroll-view>
		
		<!-- 消息输入栏 -->
		<view class="input-bar">
			<view class="mic-btn" @click="toggleVoiceInput">
				<!-- 语音按钮 -->
				<svg v-if="!isVoiceMode" class="mic-icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="32" height="32">
					<path d="M593.024 234.496l-46.165333 46.229333c0.106667 0.085333 0.149333 0.149333 0.277333 0.234667 127.552 127.658667 127.552 334.677333 0 462.336l46.122667 46.250667c153.045333-153.194667 153.045333-401.621333 0-554.816C593.173333 234.666667 593.130667 234.56 593.024 234.496z" p-id="3544"></path>
					<path d="M500.885333 327.189333c-0.042667-0.085333-0.128-0.149333-0.234667-0.213333l-46.165333 46.229333c0.042667 0.085333 0.128 0.149333 0.234667 0.256 76.48 76.565333 76.48 200.789333 0 277.397333l46.144 46.229333C603.008 595.008 603.008 429.333333 500.885333 327.189333z" p-id="3545"></path>
					<path d="M408.32 419.434667l-92.394667 92.458667 92.629333 92.714667c51.008-51.114667 51.008-133.888 0-184.938667C408.469333 419.584 408.362667 419.52 408.32 419.434667z" p-id="3546"></path>
					<path d="M512 0C229.696 0 0 229.696 0 512c0 282.282667 229.696 512 512 512 282.282667 0 512-229.717333 512-512C1024 229.696 794.282667 0 512 0zM512 981.290667C253.226667 981.290667 42.688 770.773333 42.688 512S253.226667 42.688 512 42.688 981.290667 253.226667 981.290667 512 770.773333 981.290667 512 981.290667z" p-id="3547"></path>
				</svg>
				<!-- 键盘按钮 -->
				<svg v-else class="keyboard-icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="32" height="32">
					<path d="M512 64A448 448 0 1 0 960 512 448.5 448.5 0 0 0 512 64z m0 832a384 384 0 1 1 384-384 384.5 384.5 0 0 1-384 384z" fill="#333333" p-id="2120"></path>
					<path d="M320 400m-48 0a48 48 0 1 0 96 0 48 48 0 1 0-96 0Z" fill="#333333" p-id="2121"></path>
					<path d="M448 448A48 48 0 1 0 400 400a48 48 0 0 0 48 48zM576 352a48 48 0 1 0 48 48 48 48 0 0 0-48-48zM704 352a48 48 0 1 0 48 48 48 48 0 0 0-48-48z" fill="#333333" p-id="2122"></path>
					<path d="M320 528m-48 0a48 48 0 1 0 96 0 48 48 0 1 0-96 0Z" fill="#333333" p-id="2123"></path>
					<path d="M448 576a48 48 0 1 0-48-48 48 48 0 0 0 48 48zM576 640H448a48 48 0 0 0 0 96h128a48 48 0 1 0 0-96zM576 480a48 48 0 1 0 48 48 48 48 0 0 0-48-48zM704 480a48 48 0 1 0 48 48 48 48 0 0 0-48-48z" fill="#333333" p-id="2124"></path>
				</svg>
			</view>
			
			<!-- 输入框区域 -->
			<view class="input-field" v-if="!isVoiceMode">
				<input 
					class="message-input" 
					v-model="inputMessage" 
					placeholder="输入消息..." 
					:focus="inputFocus"
					@focus="onInputFocus"
					@blur="onInputBlur"
					@confirm="sendMessage"
				/>
			</view>
			
			<!-- 语音按钮区域 -->
			<view class="voice-field" v-else @touchstart="startVoiceRecord" @touchend="endVoiceRecord">
				<text class="voice-text">按住说话</text>
			</view>
			
			<view class="action-btns">
				<view class="emoji-btn" @click="toggleEmoji">
					<svg class="emoji-icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="32" height="32">
						<path d="M512 160C317.5936 160 160 317.5936 160 512S317.5936 864 512 864 864 706.4064 864 512 706.4064 160 512 160z m0 64c159.0528 0 288 128.9472 288 288S671.0528 800 512 800 224 671.0528 224 512 352.9472 224 512 224z" fill="#2c2c2c" p-id="4603"></path>
						<path d="M683.3408 501.248H340.6592a25.6 25.6 0 0 0-25.6 26.112l0.1536 6.912a196.9408 196.9408 0 0 0 393.728-7.3984 25.6 25.6 0 0 0-25.6-25.6z m-27.8784 51.2l-0.3072 1.792A145.7664 145.7664 0 0 1 512 672.5888l-5.7088-0.1024a145.8176 145.8176 0 0 1-137.2928-117.4272l-0.4864-2.6112h286.9504z" fill="#2c2c2c" p-id="4604"></path>
						<path d="M411.1104 411.4432m-38.7584 0a38.7584 38.7584 0 1 0 77.5168 0 38.7584 38.7584 0 1 0-77.5168 0Z" fill="#2c2c2c" p-id="4605"></path>
						<path d="M612.8896 411.4432m-38.7584 0a38.7584 38.7584 0 1 0 77.5168 0 38.7584 38.7584 0 1 0-77.5168 0Z" fill="#2c2c2c" p-id="4606"></path>
					</svg>
				</view>
				<view class="plus-btn" @click="togglePlusMenu">
					<svg class="plus-icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="32" height="32">
						<path d="M511.5 957.9C264.9 957.9 65 758.2 65 511.9s199.9-446 446.5-446S958 265.6 958 511.9c0.1 246.3-199.8 446-446.5 446zM509 149.1c-200.4 0-355.8 162.2-355.8 362.3 0 200.1 155.4 356.8 355.8 356.8s362.9-156.7 362.9-356.8c0-200.1-162.5-362.3-362.9-362.3zM690.5 556h-134v133.8c0 24.6-20 44.6-44.6 44.6h-0.1c-24.6 0-44.6-19.9-44.6-44.6V556h-134c-24.7 0-44.6-19.9-44.6-44.5v-0.1c0-24.6 20-44.6 44.6-44.6h134V333c0-24.6 20-44.6 44.6-44.6h0.1c24.7 0 44.6 19.9 44.6 44.6v133.8h134c24.7 0 44.6 19.9 44.6 44.6v0.1c0 24.6-19.9 44.5-44.6 44.5z m0 0" p-id="1640"></path>
					</svg>
				</view>
			</view>
		</view>
		

		
		<!-- 表情面板 -->
		<view class="emoji-panel" :class="{ 'active': showEmojiPanel }" @click="hideEmojiPanel">
			<view class="emoji-content" @click.stop>
				<view class="emoji-grid">
					<!-- 第一行 -->
					<view class="emoji-item" v-for="i in 4" :key="`row1-${i}`">
						<image class="emoji-image" src="" mode="aspectFill"></image>
					</view>
					<!-- 第二行 -->
					<view class="emoji-item" v-for="i in 4" :key="`row2-${i}`">
						<image class="emoji-image" src="" mode="aspectFill"></image>
					</view>
					<!-- 第三行 -->
					<view class="emoji-item" v-for="i in 4" :key="`row3-${i}`">
						<image class="emoji-image" src="" mode="aspectFill"></image>
					</view>
				</view>
				<!-- 翻页指示器 -->
				<view class="emoji-pagination">
					<view class="page-dot active"></view>
					<view class="page-dot"></view>
					<view class="page-dot"></view>
				</view>
			</view>
		</view>

		<!-- 加号功能面板 -->
		<view class="plus-panel" :class="{ 'active': showPlusPanel }" @click="hidePlusPanel">
			<view class="plus-content" @click.stop>
				<view class="plus-grid">
					<view class="plus-item" @click="selectImage">
						<view class="plus-icon-wrapper">
							<svg class="plus-feature-icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="48" height="48">
								<path d="M928 160H96c-17.7 0-32 14.3-32 32v640c0 17.7 14.3 32 32 32h832c17.7 0 32-14.3 32-32V192c0-17.7-14.3-32-32-32zM338 304c35.3 0 64 28.7 64 64s-28.7 64-64 64-64-28.7-64-64 28.7-64 64-64zm513.9 436.1H172.1c-12.9 0-24.8-5.9-32.7-16.1L73.4 659.8c-7.9-10.2-7.9-23.4 0-33.6l66-84.2c7.9-10.2 19.8-16.1 32.7-16.1h679.8c12.9 0 24.8 5.9 32.7 16.1l66 84.2c7.9 10.2 7.9 23.4 0 33.6l-66 84.2c-7.9 10.2-19.8 16.1-32.7 16.1z" fill="#333333"></path>
							</svg>
						</view>
						<text class="plus-text">图片与视频</text>
					</view>
					<view class="plus-item" @click="openCamera">
						<view class="plus-icon-wrapper">
							<svg class="plus-feature-icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="48" height="48">
								<path d="M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64zm0 820c-205.4 0-372-166.6-372-372s166.6-372 372-372 372 166.6 372 372-166.6 372-372 372z" fill="#333333"></path>
								<path d="M464 336a48 48 0 1 0 96 0 48 48 0 1 0-96 0zm72 112h-48c-4.4 0-8 3.6-8 8v272c0 4.4 3.6 8 8 8h48c4.4 0 8-3.6 8-8V456c0-4.4-3.6-8-8-8z" fill="#333333"></path>
							</svg>
						</view>
						<text class="plus-text">相机</text>
					</view>
					<view class="plus-item" @click="makeCall">
						<view class="plus-icon-wrapper">
							<svg class="plus-feature-icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="48" height="48">
								<path d="M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64zm0 820c-205.4 0-372-166.6-372-372s166.6-372 372-372 372 166.6 372 372-166.6 372-372 372z" fill="#333333"></path>
								<path d="M512 336c-97.2 0-176 78.8-176 176s78.8 176 176 176 176-78.8 176-176-78.8-176-176-176z" fill="#333333"></path>
							</svg>
						</view>
						<text class="plus-text">语音/视频通话</text>
					</view>
					<view class="plus-item" @click="shareLocation">
						<view class="plus-icon-wrapper">
							<svg class="plus-feature-icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="48" height="48">
								<path d="M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64zm0 820c-205.4 0-372-166.6-372-372s166.6-372 372-372 372 166.6 372 372-166.6 372-372 372z" fill="#333333"></path>
								<path d="M512 336c-97.2 0-176 78.8-176 176s78.8 176 176 176 176-78.8 176-176-78.8-176-176-176z" fill="#333333"></path>
							</svg>
						</view>
						<text class="plus-text">定位</text>
					</view>
				</view>
			</view>
		</view>

		<!-- 底部操作菜单 -->
		<view class="action-sheet" :class="{ 'active': showActionSheet }" @click="hideActionSheet">
			<view class="action-content" @click.stop>
				<view class="action-item" @click="goToMyAIModels">
					<text class="action-text">我购买的AI模型</text>
				</view>
				<view class="action-item" @click="goToAIDetails">
					<text class="action-text">AI详情信息</text>
				</view>
				<view class="action-item" @click="goToAIStore">
					<text class="action-text">AI模型商城</text>
				</view>
				<view class="action-cancel" @click="hideActionSheet">
					<text class="cancel-text">取消</text>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		name: 'AiChat',
		data() {
			return {
				showActionSheet: false,
				scrollTop: 0,
				inputMessage: '',
				inputFocus: false,
				isVoiceMode: false, // 语音模式标识
				showEmojiPanel: false, // 表情面板显示标识
				showPlusPanel: false, // 加号面板显示标识
				currentAiModel: {}, // 当前AI模型信息
				loading: false, // 加载状态
				chatLoading: false, // 聊天记录加载状态
				messages: [], // 聊天消息列表
				modelId: null // AI模型ID
			}
		},
		onLoad(options) {
			console.log('ai-chat页面加载，接收到的参数:', options);
			
			// 获取AI模型ID参数，如果没有参数则显示错误
			if (!options.modelId) {
				console.log('没有modelId参数，显示错误');
				this.showModelNotFoundError();
				return;
			}
			
			// 保存modelId到组件数据中
			this.modelId = options.modelId;
			console.log('保存的modelId:', this.modelId);
			
			// 获取用户ID
			const userId = uni.getStorageSync('userId');
			console.log('获取到的userId:', userId);
			if (!userId) {
				console.log('没有userId，显示登录错误');
				this.showUserNotLoginError();
				return;
			}
			
			// 加载AI模型数据和聊天记录
			console.log('开始加载AI模型数据和聊天记录');
			this.loadAiModelData(options.modelId, userId);
			this.loadChatMessages(userId, options.modelId);
			
			// 页面加载完成后滚动到底部显示最新消息
			this.$nextTick(() => {
				this.scrollToBottom();
			});
		},
		methods: {
			// 加载AI模型数据
			async loadAiModelData(modelId, userId) {
				try {
					this.loading = true;
					
					// 首先尝试从用户AI模型接口获取（通过userId查询）
					const userModelResponse = await this.getUserAiModelByUserId(userId);
					if (userModelResponse && userModelResponse.data) {
						// 在用户的AI模型列表中查找匹配的模型
						const userModels = userModelResponse.data;
						const targetModel = userModels.find(model => 
							model.userAiId == modelId || model.parentModelId == modelId
						);
						if (targetModel) {
							this.currentAiModel = targetModel;
							return;
						}
					}
					
					// 如果用户模型不存在，则从基础模型接口获取
					const baseModelResponse = await this.getAimodelBaseInfoById(modelId);
					if (baseModelResponse && baseModelResponse.data) {
						this.currentAiModel = baseModelResponse.data;
						return;
					}
					
					// 如果都获取不到，显示模型不存在错误
					this.showModelNotFoundError();
					
				} catch (error) {
					console.error('加载AI模型数据失败:', error);
					this.showNetworkError();
				} finally {
					this.loading = false;
				}
			},
			
			// 获取用户AI模型数据（通过userId）
			getUserAiModelByUserId(userId) {
				return new Promise((resolve, reject) => {
					uni.request({
						url: 'http://localhost:8888/api/user-ai-model/user/' + userId,
						method: 'GET',
						timeout: 10000, // 10秒超时
						success: (res) => {
							resolve(res);
						},
						fail: (err) => {
							reject(err);
						}
					});
				});
			},
			
			// 获取用户AI模型数据（通过userAiId）
			getUserAiModelById(modelId) {
				return new Promise((resolve, reject) => {
					uni.request({
						url: 'http://localhost:8888/api/user-ai-model/' + modelId,
						method: 'GET',
						timeout: 10000, // 10秒超时
						success: (res) => {
							resolve(res);
						},
						fail: (err) => {
							reject(err);
						}
					});
				});
			},
			
			// 获取AI模型基础信息
			getAimodelBaseInfoById(modelId) {
				return new Promise((resolve, reject) => {
					uni.request({
						url: 'http://localhost:8888/api/entity/aimodel-base-info/' + modelId,
						method: 'GET',
						timeout: 10000, // 10秒超时
						success: (res) => {
							resolve(res);
						},
						fail: (err) => {
							reject(err);
						}
					});
				});
			},
			
			// 加载聊天记录
			async loadChatMessages(userId, userAiId) {
				try {
					this.chatLoading = true;
					console.log('开始加载聊天记录，userId:', userId, 'userAiId:', userAiId);
					
					const response = await this.getChatMessages(userId, userAiId);
					console.log('获取聊天记录响应:', response);
					
					if (response.data && response.data.length > 0) {
						console.log('找到聊天记录，数量:', response.data.length);
						console.log('原始聊天数据:', response.data);
						// 将后端数据转换为前端消息格式
						this.messages = this.convertChatDataToMessages(response.data);
						console.log('转换后的消息:', this.messages);
					} else {
						console.log('没有找到聊天记录，显示默认欢迎消息');
						// 如果没有聊天记录，显示欢迎消息
						this.messages = [{
							id: 1,
							type: 'left',
							text: '你好！我是你的 AI 助手，有什么可以帮助你的吗？',
							time: this.getCurrentTime()
						}];
					}
				} catch (error) {
					console.error('加载聊天记录失败:', error);
					// 如果加载失败，显示默认欢迎消息
					this.messages = [{
						id: 1,
						type: 'left',
						text: '你好！我是你的 AI 助手，有什么可以帮助你的吗？',
						time: this.getCurrentTime()
					}];
				} finally {
					this.chatLoading = false;
				}
			},
			
			// 获取聊天记录
			getChatMessages(userId, userAiId) {
				return new Promise((resolve, reject) => {
					uni.request({
						url: `http://localhost:8888/api/a-entities-wlq/ai-chat-list-detail-r/user/${userId}/ai/${userAiId}`,
						method: 'GET',
						timeout: 10000,
						success: (res) => {
							resolve(res);
						},
						fail: (err) => {
							reject(err);
						}
					});
				});
			},
			
			// 将后端聊天数据转换为前端消息格式
			convertChatDataToMessages(chatData) {
				const messages = [];
				let messageId = 1;
				
				// 按时间戳排序，最早的在前
				chatData.sort((a, b) => {
					if (a.messageTimestamp && b.messageTimestamp) {
						return new Date(a.messageTimestamp) - new Date(b.messageTimestamp);
					}
					return 0;
				});
				
				chatData.forEach(chat => {
					// 添加用户消息
					if (chat.userMessage) {
						messages.push({
							id: messageId++,
							type: 'right',
							text: chat.userMessage,
							time: this.formatTimestamp(chat.messageTimestamp)
						});
					}
					
					// 添加AI回复
					if (chat.aiResponse) {
						messages.push({
							id: messageId++,
							type: 'left',
							text: chat.aiResponse,
							time: this.formatTimestamp(chat.messageTimestamp)
						});
					}
				});
				
				return messages;
			},
			
			// 格式化时间戳
			formatTimestamp(timestamp) {
				if (!timestamp) return this.getCurrentTime();
				
				const date = new Date(timestamp);
				const now = new Date();
				const diff = now - date;
				
				// 如果是今天
				if (diff < 24 * 60 * 60 * 1000 && date.getDate() === now.getDate()) {
					const hours = date.getHours();
					const minutes = date.getMinutes();
					const timeStr = hours < 12 ? '上午' : '下午';
					const displayHours = hours < 12 ? hours : hours - 12;
					return `${timeStr}${displayHours}:${minutes.toString().padStart(2, '0')}`;
				}
				
				// 如果是昨天
				if (diff < 48 * 60 * 60 * 1000 && date.getDate() === now.getDate() - 1) {
					return '昨天 ' + this.formatTime(date);
				}
				
				// 其他情况显示日期
				return `${date.getMonth() + 1}月${date.getDate()}日 ${this.formatTime(date)}`;
			},
			
			// 格式化时间
			formatTime(date) {
				const hours = date.getHours();
				const minutes = date.getMinutes();
				const timeStr = hours < 12 ? '上午' : '下午';
				const displayHours = hours < 12 ? hours : hours - 12;
				return `${timeStr}${displayHours}:${minutes.toString().padStart(2, '0')}`;
			},
			
			// 显示模型不存在错误
			showModelNotFoundError() {
				this.currentAiModel = {
					modelId: null,
					modelName: '模型不存在',
					modelDesc: '抱歉，您要访问的AI模型不存在或已被删除',
					modelImageUrl: null
				};
				uni.showToast({
					title: 'AI模型不存在',
					icon: 'none',
					duration: 3000
				});
			},
			
			// 显示网络错误
			showNetworkError() {
				this.currentAiModel = {
					modelId: null,
					modelName: '网络连接失败',
					modelDesc: '网络连接超时，请检查网络设置后重试',
					modelImageUrl: null
				};
				uni.showToast({
					title: '网络连接超时',
					icon: 'none',
					duration: 3000
				});
			},
			
			// 显示用户未登录错误
			showUserNotLoginError() {
				this.currentAiModel = {
					modelId: null,
					modelName: '请先登录',
					modelDesc: '您需要先登录才能使用AI聊天功能',
					modelImageUrl: null
				};
				uni.showToast({
					title: '请先登录',
					icon: 'none',
					duration: 3000
				});
				
				// 延迟跳转到登录页面
				setTimeout(() => {
					uni.navigateTo({
						url: '/pages/login/login-replica'
					});
				}, 2000);
			},
			
			goBack() {
				// 尝试返回上一页，如果失败则跳转到AI列表
				try {
					uni.navigateBack({
						fail: () => {
							// 如果返回失败，跳转到AI列表页面
							uni.navigateTo({
								url: '/pages/ai/ai-list'
							});
						}
					});
				} catch (error) {
					// 如果出错，跳转到AI列表页面
					uni.navigateTo({
						url: '/pages/ai/ai-list'
					});
				}
			},
			
			showMoreOptions() {
				this.showActionSheet = true;
			},
			
			hideActionSheet() {
				this.showActionSheet = false;
			},
			
			// 跳转到我购买的AI模型页面
			goToMyAIModels() {
				this.hideActionSheet();
				uni.navigateTo({
					url: '/pages/ai/ai-list'
				});
			},
			
			// 跳转到AI详情信息页面
			goToAIDetails() {
				this.hideActionSheet();
				// 传递AI模型ID和名称
				const aiName = this.currentAiModel.modelName || this.modelId || 'AI助手';
				uni.navigateTo({
					url: `/pages/ai/gift?userAiId=${this.modelId}&aiName=${encodeURIComponent(aiName)}`
				});
			},
			
			// 跳转到AI模型商城页面
			goToAIStore() {
				this.hideActionSheet();
				uni.navigateTo({
					url: '/pages/ai/ai-store'
				});
			},
			
			toggleVoiceInput() {
				// 切换语音/键盘模式
				this.isVoiceMode = !this.isVoiceMode;
				if (this.isVoiceMode) {
					// 隐藏表情和加号面板
					this.showEmojiPanel = false;
					this.showPlusPanel = false;
				}
			},
			
			toggleEmoji() {
				// 切换表情面板
				this.showEmojiPanel = !this.showEmojiPanel;
				this.showPlusPanel = false; // 隐藏加号面板
			},
			
			togglePlusMenu() {
				// 切换加号菜单
				this.showPlusPanel = !this.showPlusPanel;
				this.showEmojiPanel = false; // 隐藏表情面板
			},
			
			hideEmojiPanel() {
				this.showEmojiPanel = false;
			},
			
			hidePlusPanel() {
				this.showPlusPanel = false;
			},
			
			startVoiceRecord() {
				// 开始录音
				uni.showToast({
					title: '开始录音',
					icon: 'none'
				});
			},
			
			endVoiceRecord() {
				// 结束录音
				uni.showToast({
					title: '录音完成',
					icon: 'none'
				});
			},
			
			selectImage() {
				// 选择图片
				this.hidePlusPanel();
				uni.showToast({
					title: '图片选择功能开发中',
					icon: 'none'
				});
			},
			
			openCamera() {
				// 打开相机
				this.hidePlusPanel();
				uni.showToast({
					title: '相机功能开发中',
					icon: 'none'
				});
			},
			
			makeCall() {
				// 拨打电话
				this.hidePlusPanel();
				uni.showToast({
					title: '通话功能开发中',
					icon: 'none'
				});
			},
			
			shareLocation() {
				// 分享位置
				this.hidePlusPanel();
				uni.showToast({
					title: '位置分享功能开发中',
					icon: 'none'
				});
			},
			
			onInputFocus() {
				this.inputFocus = true;
			},
			
			onInputBlur() {
				this.inputFocus = false;
			},
			
			async sendMessage() {
				if (!this.inputMessage.trim()) {
					return;
				}
				
				const userMessage = this.inputMessage.trim();
				const userId = uni.getStorageSync('userId');
				
				if (!userId) {
					uni.showToast({
						title: '请先登录',
						icon: 'none'
					});
					return;
				}
				
				// 添加用户消息到界面
				const userMsg = {
					id: Date.now(),
					type: 'right',
					text: userMessage,
					time: this.getCurrentTime()
				};
				this.messages.push(userMsg);
				
				// 清空输入框
				this.inputMessage = '';
				
				// 滚动到底部
				this.$nextTick(() => {
					this.scrollToBottom();
				});
				
				try {
					// 生成AI回复
					const aiResponse = this.generateAIResponse(userMessage);
					
					// 添加AI回复到界面
					const aiMsg = {
						id: Date.now() + 1,
						type: 'left',
						text: aiResponse,
						time: this.getCurrentTime()
					};
					this.messages.push(aiMsg);
					
					// 保存完整的对话到数据库（用户问题 + AI回答）
					await this.saveChatMessage(userId, this.modelId, userMessage, aiResponse, 'ai_chat');
					
					// 滚动到底部
					this.$nextTick(() => {
						this.scrollToBottom();
					});
					
				} catch (error) {
					console.error('发送消息失败:', error);
					uni.showToast({
						title: '发送失败，请重试',
						icon: 'none'
					});
				}
			},
			
			generateAIResponse(userMessage) {
				// 简单的 AI 回复逻辑
				const responses = [
					'我理解你的问题，让我来帮你解答。',
					'这是一个很有趣的问题！',
					'根据我的分析，我认为...',
					'让我为你提供一些建议。',
					'这个问题需要仔细考虑，我的建议是...',
					'谢谢你的提问，让我想想...',
					'这是一个很好的观点！',
					'我建议你可以尝试...'
				];
				return responses[Math.floor(Math.random() * responses.length)];
			},
			
			getCurrentTime() {
				const now = new Date();
				const hours = now.getHours();
				const minutes = now.getMinutes();
				const timeStr = hours < 12 ? '上午' : '下午';
				const displayHours = hours < 12 ? hours : hours - 12;
				return `${timeStr}${displayHours}:${minutes.toString().padStart(2, '0')}`;
			},
			
			scrollToBottom() {
				// 滚动到底部
				this.scrollTop = 999999;
			},
			
			// 获取头像显示文字
			getAvatarText() {
				if (!this.currentAiModel.modelName) {
					return '?';
				}
				// 取模型名称的第一个字符
				return this.currentAiModel.modelName.charAt(0);
			},
			
			// 保存聊天消息到数据库
			async saveChatMessage(userId, userAiId, userMessage, aiResponse, chatType) {
				return new Promise((resolve, reject) => {
					const chatData = {
						userId: parseInt(userId),
						userAiId: userAiId ? parseInt(userAiId) : null,
						userMessage: userMessage,
						aiResponse: aiResponse,
						chatType: chatType,
						messageTimestamp: new Date().toISOString(),
						createdAt: new Date().toISOString(),
						updatedAt: new Date().toISOString()
					};
					
					console.log('发送聊天数据:', chatData);
					
					uni.request({
						url: 'http://localhost:8888/api/a-entities-wlq/ai-chat-list-detail-r/save',
						method: 'POST',
						header: {
							'Content-Type': 'application/json'
						},
						data: chatData,
						timeout: 10000,
						success: (res) => {
							console.log('聊天消息保存成功:', res);
							resolve(res);
						},
						fail: (err) => {
							console.error('聊天消息保存失败:', err);
							reject(err);
						}
					});
				});
			}
		}
	}
</script>

<style>
	.chat-page {
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
	
	/* 头部栏 */
	.header {
		height: 88rpx;
		display: flex;
		justify-content: center;
		align-items: center;
		position: relative;
		border-bottom: 1rpx solid #F0F0F0;
	}
	
	.back-btn {
		position: absolute;
		left: 32rpx;
		width: 60rpx;
		height: 60rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
		background-color: #F5F5F5;
		border-radius: 50%;
		transition: background-color 0.2s ease;
	}
	
	.back-btn:active {
		background-color: #E0E0E0;
	}
	
	.back-icon {
		width: 32rpx;
		height: 32rpx;
	}
	
	.title {
		font-size: 36rpx;
		color: #333333;
		font-weight: 600;
	}
	
	.more-btn {
		position: absolute;
		right: 32rpx;
		display: flex;
		flex-direction: column;
		gap: 4rpx;
	}
	
	.dot {
		width: 8rpx;
		height: 8rpx;
		background-color: #333333;
		border-radius: 50%;
	}
	
	/* 聊天区域 */
	.chat-area {
		flex: 1;
		padding: 32rpx;
		padding-bottom: 140rpx; /* 为固定的输入栏留出空间 */
	}
	
	/* 加载状态 */
	.loading-container {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		height: 400rpx;
		gap: 20rpx;
	}
	
	.loading-spinner {
		width: 60rpx;
		height: 60rpx;
		border: 4rpx solid #f3f3f3;
		border-top: 4rpx solid #20B2AA;
		border-radius: 50%;
		animation: spin 1s linear infinite;
	}
	
	@keyframes spin {
		0% { transform: rotate(0deg); }
		100% { transform: rotate(360deg); }
	}
	
	.loading-text {
		font-size: 28rpx;
		color: #666;
	}
	
	/* 无消息提示 */
	.no-messages {
		display: flex;
		justify-content: center;
		align-items: center;
		height: 400rpx;
	}
	
	.no-messages-text {
		font-size: 28rpx;
		color: #999;
	}
	
	.message {
		display: flex;
		align-items: flex-start;
		margin-bottom: 32rpx;
	}
	
	.message.left {
		justify-content: flex-start;
	}
	
	.message.right {
		justify-content: flex-end;
	}
	
	.avatar {
		width: 80rpx;
		height: 80rpx;
		border-radius: 40rpx;
		background-color: #F5F5F5; /* 头像占位 */
		flex-shrink: 0;
	}
	
	.avatar-placeholder {
		display: flex;
		align-items: center;
		justify-content: center;
		background-color: #E0E0E0;
		border: 2rpx solid #CCCCCC;
	}
	
	.avatar-text {
		font-size: 32rpx;
		font-weight: bold;
		color: #666666;
	}
	
	.message-bubble {
		max-width: 400rpx;
		padding: 24rpx;
		border-radius: 24rpx;
		margin: 0 16rpx;
	}
	
	.message.left .message-bubble {
		background-color: #F0F0F0;
	}
	
	.message.right .message-bubble {
		background-color: #20B2AA;
	}
	
	.message-text {
		font-size: 28rpx;
		color: #333333;
		line-height: 1.4;
	}
	
	.message.right .message-text {
		color: #FFFFFF;
	}
	
	.message-time {
		font-size: 20rpx;
		color: #999999;
		margin-top: 8rpx;
		display: block;
	}
	
	.message.right .message-time {
		color: rgba(255, 255, 255, 0.7);
	}
	
	.timestamp {
		text-align: center;
		margin: 32rpx 0;
	}
	
	.time-text {
		font-size: 24rpx;
		color: #999999;
		background-color: #F8F8F8;
		padding: 8rpx 24rpx;
		border-radius: 20rpx;
	}
	
	/* 输入栏 */
	.input-bar {
		height: 120rpx;
		display: flex;
		align-items: center;
		padding: 0 32rpx;
		background-color: #FFFFFF;
		border-top: 1rpx solid #F0F0F0;
		position: fixed;
		bottom: 0; /* 固定在底部 */
		left: 0;
		right: 0;
		z-index: 1000;
	}
	
	.mic-btn {
		width: 60rpx;
		height: 60rpx;
		background-color: #F0F0F0;
		border-radius: 30rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
	}
	
	.mic-icon, .keyboard-icon {
		width: 32rpx;
		height: 32rpx;
	}
	
	.input-field {
		flex: 1;
		height: 80rpx;
		background-color: #F8F8F8;
		border-radius: 40rpx;
		display: flex;
		align-items: center;
		padding: 0 32rpx;
		margin: 0 20rpx;
	}
	
	.voice-field {
		flex: 1;
		height: 80rpx;
		background-color: #F8F8F8;
		border-radius: 40rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		margin: 0 20rpx;
		cursor: pointer;
	}
	
	.voice-text {
		font-size: 28rpx;
		color: #666;
	}
	
	.message-input {
		flex: 1;
		height: 100%;
		font-size: 28rpx;
		color: #333;
		background: transparent;
		border: none;
		outline: none;
	}
	
	.action-btns {
		display: flex;
		gap: 24rpx;
		margin-left: 24rpx;
	}
	
	.emoji-btn, .plus-btn {
		width: 60rpx;
		height: 60rpx;
		background-color: #F0F0F0;
		border-radius: 30rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
		margin-left: 20rpx;
	}
	
	.emoji-icon, .plus-icon {
		width: 32rpx;
		height: 32rpx;
	}
	

	
	/* 表情面板 */
	.emoji-panel {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background-color: rgba(0, 0, 0, 0.5);
		z-index: 9999;
		display: flex;
		align-items: flex-end;
		opacity: 0;
		visibility: hidden;
		transition: all 0.3s ease;
	}
	
	.emoji-panel.active {
		opacity: 1;
		visibility: visible;
	}
	
	.emoji-content {
		width: 100%;
		background-color: #FFFFFF;
		border-radius: 20rpx 20rpx 0 0;
		padding: 32rpx;
		transform: translateY(100%);
		transition: transform 0.3s ease;
	}
	
	.emoji-panel.active .emoji-content {
		transform: translateY(0);
	}
	
	.emoji-grid {
		display: grid;
		grid-template-columns: repeat(4, 1fr);
		gap: 32rpx;
		margin-bottom: 32rpx;
	}
	
	.emoji-item {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 16rpx;
		cursor: pointer;
	}
	
	.emoji-image {
		width: 80rpx;
		height: 80rpx;
		background-color: #F0F0F0;
		border-radius: 16rpx;
	}
	
	.emoji-pagination {
		display: flex;
		justify-content: center;
		gap: 16rpx;
	}
	
	.page-dot {
		width: 16rpx;
		height: 16rpx;
		border-radius: 50%;
		background-color: #E0E0E0;
		transition: background-color 0.3s ease;
	}
	
	.page-dot.active {
		background-color: #007AFF;
	}

	/* 加号功能面板 */
	.plus-panel {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background-color: rgba(0, 0, 0, 0.5);
		z-index: 9999;
		display: flex;
		align-items: flex-end;
		opacity: 0;
		visibility: hidden;
		transition: all 0.3s ease;
	}
	
	.plus-panel.active {
		opacity: 1;
		visibility: visible;
	}
	
	.plus-content {
		width: 100%;
		background-color: #FFFFFF;
		border-radius: 20rpx 20rpx 0 0;
		padding: 32rpx;
		transform: translateY(100%);
		transition: transform 0.3s ease;
	}
	
	.plus-panel.active .plus-content {
		transform: translateY(0);
	}
	
	.plus-grid {
		display: grid;
		grid-template-columns: repeat(4, 1fr);
		gap: 32rpx;
	}
	
	.plus-item {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 16rpx;
		cursor: pointer;
		padding: 16rpx;
		border-radius: 16rpx;
		transition: background-color 0.2s ease;
	}
	
	.plus-item:active {
		background-color: #F8F8F8;
	}
	
	.plus-icon-wrapper {
		width: 80rpx;
		height: 80rpx;
		background-color: #F0F0F0;
		border-radius: 16rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.plus-feature-icon {
		width: 48rpx;
		height: 48rpx;
	}
	
	.plus-text {
		font-size: 24rpx;
		color: #333333;
		text-align: center;
		line-height: 1.2;
	}

	/* 底部操作菜单 */
	.action-sheet {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background-color: rgba(0, 0, 0, 0.5);
		z-index: 9999;
		display: flex;
		align-items: flex-end;
		opacity: 0;
		visibility: hidden;
		transition: all 0.3s ease;
	}
	
	.action-sheet.active {
		opacity: 1;
		visibility: visible;
	}
	
	.action-content {
		width: 100%;
		background-color: #FFFFFF;
		border-radius: 20rpx 20rpx 0 0;
		overflow: hidden;
		transform: translateY(100%);
		transition: transform 0.3s ease;
	}
	
	.action-sheet.active .action-content {
		transform: translateY(0);
	}
	
	.action-item {
		padding: 30rpx;
		border-bottom: 1rpx solid #F0F0F0;
		text-align: center;
		cursor: pointer;
		transition: background-color 0.2s ease;
	}
	
	.action-item:active {
		background-color: #F8F8F8;
	}
	
	.action-text {
		font-size: 32rpx;
		color: #333333;
		font-weight: 500;
	}
	
	.action-cancel {
		padding: 30rpx;
		text-align: center;
		cursor: pointer;
		background-color: #F8F8F8;
		margin-top: 20rpx;
	}
	
	.cancel-text {
		font-size: 32rpx;
		color: #666666;
		font-weight: 500;
	}
</style>
