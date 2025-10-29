<template>
	<view class="publish-page">
		<!-- 头部导航 -->
		<view class="header">
			<view class="cancel-button" @click="goBack">
				<text class="cancel-text">取消</text>
			</view>
			<view class="title-section">
				<text class="page-title">Afriends</text>
				<text class="subtitle">曇鬲</text>
			</view>
			<view class="send-button" :class="{ disabled: !canPublish }" @click="publishContent">
				<text class="send-text">发送</text>
			</view>
		</view>
		
		<!-- 内容输入区域 -->
		<view class="content-input-area">
			<!-- 标题输入（我有、我要专栏） -->
			<view class="title-input" v-if="selectedColumn === 'ihave' || selectedColumn === 'iwant'">
				<input 
					class="title-field" 
					v-model="title"
					:placeholder="selectedColumn === 'ihave' ? '物品标题（如：我的iPhone 15）' : '需求标题（如：需要一台MacBook）'"
					placeholder-class="input-placeholder"
				/>
			</view>
			
			<!-- 描述输入（我有专栏） -->
			<view class="description-input" v-if="selectedColumn === 'ihave'">
				<textarea 
					class="description-field" 
					v-model="description"
					placeholder="详细描述（如：购入时间、使用情况等）"
					placeholder-class="input-placeholder"
					:show-confirm-bar="false"
					:adjust-position="true"
					:hold-keyboard="true"
				/>
			</view>
			
			<!-- 主要内容输入 -->
			<textarea 
				class="input-field" 
				v-model="contentText"
				:placeholder="getContentPlaceholder()"
				placeholder-class="input-placeholder"
				:auto-focus="true"
				:show-confirm-bar="false"
				:adjust-position="true"
				:hold-keyboard="true"
			/>
		</view>
		
		<!-- 功能选项按钮 -->
		<view class="feature-buttons">
			<!-- 第一行：可左右滑动的硬标签 -->
			<view class="button-row scrollable-row" v-if="showFeatureRow">
				<view class="feature-button" v-for="(item, index) in featureItems" :key="index">
					<view class="button-icon" :class="item.iconClass"></view>
					<text class="button-text">{{ item.text }}</text>
				</view>
				<view class="close-row-button" @click="closeFeatureRow">
					<text class="close-text">×</text>
				</view>
			</view>
			
			<!-- 第二行：添加位置和超话靠左，公开靠右 -->
			<view class="button-row second-row">
				<view class="left-buttons">
					<view class="feature-button">
						<view class="button-icon pin-icon"></view>
						<text class="button-text">添加位置</text>
					</view>
					<view class="feature-button">
						<view class="button-icon diamond-icon"></view>
						<text class="button-text">超话</text>
					</view>
				</view>
				<view class="right-buttons">
					<view class="feature-button">
						<view class="button-icon lock-icon"></view>
						<text class="button-text">公开</text>
					</view>
				</view>
			</view>
			
			<!-- 第三行：位置标签，可左右滑动 -->
			<view class="location-tags scrollable-row" v-if="showLocationTags">
				<view class="tag-item" v-for="(tag, index) in locationTags" :key="index">
					<text class="tag-text">{{ tag }}</text>
				</view>
				<view class="close-row-button" @click="closeLocationRow">
					<text class="close-text">×</text>
				</view>
			</view>
		</view>
		
		<!-- 必选专栏 -->
		<view class="required-column">
			<text class="column-title">必选专栏 *</text>
			<view class="column-options">
				<view 
					class="column-option" 
					:class="{ selected: selectedColumn === option.value }"
					v-for="option in columnOptions" 
					:key="option.value"
					@click="selectColumn(option.value)"
				>
					<text class="option-text">{{ option.label }}</text>
				</view>
			</view>
		</view>
		
		<!-- 底部工具栏 -->
		<view class="bottom-toolbar">
			<view class="tool-item" @click="selectImage">
				<view class="tool-icon gallery-icon"></view>
			</view>
			<view class="tool-item" @click="mentionUser">
				<view class="tool-icon at-icon">@</view>
			</view>
			<view class="tool-item" @click="addHashtag">
				<view class="tool-icon hashtag-icon">#</view>
			</view>
			<view class="tool-item" @click="addGif">
				<view class="tool-icon gif-icon">GIF</view>
			</view>
			<view class="tool-item" @click="addEmoji">
				<view class="tool-icon emoji-icon">😊</view>
			</view>
			<view class="tool-item" @click="addMore">
				<view class="tool-icon plus-icon">+</view>
			</view>
		</view>
		
	</view>
</template>

<script>
	export default {
		name: 'PublishPage',
		data() {
			return {
				userId: 1000100, // 用户ID，从页面参数获取
				contentText: '', // 内容文本
				selectedColumn: '', // 选中的必选专栏
				showFeatureRow: true, // 是否显示功能按钮行
				showLocationTags: true, // 是否显示位置标签行
				imageUrls: '', // 图片URLs
				title: '', // 标题（用于我有、我要）
				description: '', // 描述（用于我有）
				featureItems: [
					{ iconClass: 'green-icon', text: '手记' },
					{ iconClass: 'diamond-icon', text: '小说' },
					{ iconClass: 'diamond-icon', text: '小日常' },
					{ iconClass: 'diamond-icon', text: '睡觉' }
				],
				locationTags: ['杭州', '酒球会(杭州店)', '烟火万塘', 'LiT CX'],
				columnOptions: [
					{ value: 'mood', label: '心情' },
					{ value: 'ihave', label: '我有' },
					{ value: 'iwant', label: '我要' },
					{ value: 'ilike', label: '我喜欢' },
					{ value: 'ihate', label: '我讨厌' }
				]
			}
		},
		onLoad(options) {
			// 接收页面参数
			if (options.userId) {
				this.userId = parseInt(options.userId);
				console.log('PublishPage: 接收用户ID:', this.userId);
			}
		},
		computed: {
			canPublish() {
				return this.selectedColumn !== '' && this.contentText.trim() !== '';
			}
		},
		methods: {
			goBack() {
				uni.navigateBack();
			},
			selectColumn(value) {
				this.selectedColumn = value;
				console.log('选择专栏:', value);
			},
			getContentPlaceholder() {
				switch (this.selectedColumn) {
					case 'mood':
						return '分享你的心情...';
					case 'ihave':
						return '描述你拥有的物品...';
					case 'iwant':
						return '描述你的需求...';
					case 'ilike':
						return '分享你喜欢的东西...';
					case 'ihate':
						return '分享你讨厌的东西...';
					default:
						return '分享你的想法...';
				}
			},
			closeFeatureRow() {
				this.showFeatureRow = false;
			},
			closeLocationRow() {
				this.showLocationTags = false;
			},
			selectImage() {
				uni.showToast({
					title: '选择图片功能开发中',
					icon: 'none'
				});
			},
			mentionUser() {
				uni.showToast({
					title: '艾特用户功能开发中',
					icon: 'none'
				});
			},
			addHashtag() {
				uni.showToast({
					title: '添加标签功能开发中',
					icon: 'none'
				});
			},
			addGif() {
				uni.showToast({
					title: '添加GIF功能开发中',
					icon: 'none'
				});
			},
			addEmoji() {
				uni.showToast({
					title: '添加表情功能开发中',
					icon: 'none'
				});
			},
			addMore() {
				uni.showToast({
					title: '更多功能开发中',
					icon: 'none'
				});
			},
			async publishContent() {
				if (!this.canPublish) {
					uni.showToast({
						title: '请先选择必选专栏',
						icon: 'none'
					});
					return;
				}
				
				if (!this.contentText.trim()) {
					uni.showToast({
						title: '请输入内容',
						icon: 'none'
					});
					return;
				}
				
				uni.showModal({
					title: '确认发布',
					content: '确定要发布这条内容吗？',
					success: async (res) => {
						if (res.confirm) {
							await this.submitContent();
						}
					}
				});
			},
			
			// 提交内容到后端
			async submitContent() {
				try {
					console.log('PublishPage: 开始发布内容');
					console.log('用户ID:', this.userId);
					console.log('专栏类型:', this.selectedColumn);
					console.log('内容:', this.contentText);
					
					// 根据专栏类型准备不同的数据
					let requestData = {
						userId: this.userId,
						contentText: this.contentText,
						columnType: this.selectedColumn
					};
					
					// 如果有图片，添加图片URLs
					if (this.imageUrls) {
						requestData.imageUrls = this.imageUrls;
					}
					
					// 对于"我有"和"我要"，需要标题和描述
					if (this.selectedColumn === 'ihave' || this.selectedColumn === 'iwant') {
						requestData.title = this.title || this.contentText.substring(0, 20) + '...';
						if (this.selectedColumn === 'ihave') {
							requestData.description = this.description || this.contentText;
						}
					}
					
					console.log('请求数据:', requestData);
					
					const response = await uni.request({
						url: 'http://localhost:8888/api/publish/content',
						method: 'POST',
						data: requestData,
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('发布API响应:', response);
					
					if (response.statusCode === 200 && response.data.success) {
						uni.showToast({
							title: response.data.message || '发布成功',
							icon: 'success'
						});
						
						// 延迟返回上一页
						setTimeout(() => {
							uni.navigateBack();
						}, 1500);
					} else {
						console.error('发布失败:', response.data.message);
						uni.showToast({
							title: response.data.message || '发布失败',
							icon: 'error'
						});
					}
				} catch (error) {
					console.error('发布异常:', error);
					uni.showToast({
						title: '网络错误，请重试',
						icon: 'error'
					});
				}
			}
		}
	}
</script>

<style>
	.publish-page {
		min-height: 100vh;
		background-color: #FFFFFF;
		display: flex;
		flex-direction: column;
	}
	
	/* 头部导航 */
	.header {
		height: 88rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 32rpx;
		background-color: #FFFFFF;
		border-bottom: 1rpx solid #F0F0F0;
	}
	
	.cancel-button {
		padding: 16rpx 24rpx;
		cursor: pointer;
	}
	
	.cancel-text {
		font-size: 28rpx;
		color: #000000;
		font-weight: 500;
	}
	
	.title-section {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 4rpx;
	}
	
	.page-title {
		font-size: 36rpx;
		color: #000000;
		font-weight: 600;
	}
	
	.subtitle {
		font-size: 24rpx;
		color: #666666;
	}
	
	.send-button {
		padding: 16rpx 32rpx;
		background-color: #FF9500;
		border-radius: 24rpx;
		cursor: pointer;
		transition: all 0.2s ease;
	}
	
	.send-button.disabled {
		background-color: #CCCCCC;
		cursor: not-allowed;
	}
	
	.send-text {
		font-size: 28rpx;
		color: #FFFFFF;
		font-weight: 600;
	}
	
	/* 内容输入区域 */
	.content-input-area {
		padding: 32rpx;
		flex: 1;
		min-height: 300rpx;
	}
	
	.title-input {
		margin-bottom: 24rpx;
	}
	
	.title-field {
		width: 100%;
		height: 80rpx;
		border: 2rpx solid #F0F0F0;
		border-radius: 16rpx;
		padding: 0 24rpx;
		font-size: 28rpx;
		color: #000000;
		background-color: #FFFFFF;
		box-sizing: border-box;
	}
	
	.description-input {
		margin-bottom: 24rpx;
	}
	
	.description-field {
		width: 100%;
		min-height: 120rpx;
		border: 2rpx solid #F0F0F0;
		border-radius: 16rpx;
		padding: 24rpx;
		font-size: 26rpx;
		color: #000000;
		background-color: #FFFFFF;
		box-sizing: border-box;
		resize: none;
	}
	
	.input-field {
		min-height: 300rpx;
		border: 2rpx solid #F0F0F0;
		border-radius: 16rpx;
		padding: 24rpx;
		font-size: 28rpx;
		color: #000000;
		background-color: #FFFFFF;
		width: 100%;
		box-sizing: border-box;
		resize: none;
	}
	
	.input-placeholder {
		color: #999999;
	}
	
	/* 功能选项按钮 */
	.feature-buttons {
		padding: 0 32rpx 32rpx;
	}
	
	.button-row {
		display: flex;
		gap: 16rpx;
		margin-bottom: 24rpx;
		align-items: center;
	}
	
	.scrollable-row {
		overflow-x: auto;
		white-space: nowrap;
		padding: 8rpx 0;
	}
	
	.second-row {
		justify-content: space-between;
	}
	
	.left-buttons, .right-buttons {
		display: flex;
		gap: 16rpx;
		align-items: center;
	}
	
	.feature-button {
		display: flex;
		align-items: center;
		gap: 8rpx;
		padding: 12rpx 20rpx;
		background-color: #F8F8F8;
		border-radius: 20rpx;
		cursor: pointer;
		transition: all 0.2s ease;
		white-space: nowrap;
	}
	
	.feature-button:active {
		background-color: #F0F0F0;
		transform: scale(0.95);
	}
	
	.close-row-button {
		width: 40rpx;
		height: 40rpx;
		background-color: #F0F0F0;
		border-radius: 20rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
		margin-left: 16rpx;
	}
	
	.close-text {
		font-size: 24rpx;
		color: #999999;
		font-weight: 600;
	}
	
	.button-icon {
		width: 24rpx;
		height: 24rpx;
		border-radius: 50%;
		flex-shrink: 0;
	}
	
	.green-icon {
		background-color: #4CD964;
	}
	
	.diamond-icon {
		background-color: #FFD700;
		clip-path: polygon(50% 0%, 100% 50%, 50% 100%, 0% 50%);
	}
	
	.pin-icon {
		background-color: #FF3B30;
		clip-path: polygon(50% 0%, 0% 100%, 100% 100%);
	}
	
	.lock-icon {
		background-color: #007AFF;
	}
	
	.button-text {
		font-size: 22rpx;
		color: #000000;
		font-weight: 500;
	}
	
	/* 位置标签 */
	.location-tags {
		display: flex;
		gap: 16rpx;
		overflow-x: auto;
		padding: 8rpx 0;
		white-space: nowrap;
	}
	
	.tag-item {
		display: flex;
		align-items: center;
		gap: 8rpx;
		padding: 10rpx 18rpx;
		background-color: #F0F0F0;
		border-radius: 18rpx;
		white-space: nowrap;
	}
	
	.tag-text {
		font-size: 22rpx;
		color: #000000;
	}
	
	/* 必选专栏 */
	.required-column {
		padding: 0 32rpx 32rpx;
		border-bottom: 1rpx solid #F0F0F0;
	}
	
	.column-title {
		font-size: 28rpx;
		color: #000000;
		font-weight: 600;
		margin-bottom: 24rpx;
		display: block;
	}
	
	.column-options {
		display: flex;
		gap: 16rpx;
		flex-wrap: wrap;
	}
	
	.column-option {
		padding: 16rpx 24rpx;
		background-color: #F8F8F8;
		border: 2rpx solid #E0E0E0;
		border-radius: 24rpx;
		cursor: pointer;
		transition: all 0.2s ease;
	}
	
	.column-option.selected {
		background-color: #007AFF;
		border-color: #007AFF;
	}
	
	.column-option.selected .option-text {
		color: #FFFFFF;
	}
	
	.option-text {
		font-size: 26rpx;
		color: #000000;
		font-weight: 500;
	}
	
	/* 底部工具栏 */
	.bottom-toolbar {
		height: 120rpx;
		display: flex;
		align-items: center;
		justify-content: space-around;
		padding: 0 32rpx;
		background-color: #FFFFFF;
		border-top: 1rpx solid #F0F0F0;
	}
	
	.tool-item {
		width: 80rpx;
		height: 80rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
		transition: all 0.2s ease;
	}
	
	.tool-item:active {
		transform: scale(0.95);
	}
	
	.tool-icon {
		width: 48rpx;
		height: 48rpx;
		border-radius: 8rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 20rpx;
		color: #FFFFFF;
		font-weight: 600;
	}
	
	.gallery-icon {
		background-color: #4CD964;
	}
	
	.at-icon {
		background-color: #007AFF;
	}
	
	.hashtag-icon {
		background-color: #FF9500;
	}
	
	.gif-icon {
		background-color: #FF3B30;
		font-size: 16rpx;
	}
	
	.emoji-icon {
		background-color: #FFD700;
		font-size: 24rpx;
	}
	
	.plus-icon {
		background-color: #8E8E93;
	}
	
</style>
