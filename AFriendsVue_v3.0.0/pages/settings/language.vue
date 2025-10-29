<!-- 语言设置页面 -->
<template>
	<view class="language-page">
		<!-- 导航栏 -->
		<!-- <view class="nav-bar">
			<view class="nav-left" @click="goBack">
				<text class="back-arrow">←</text>
			</view>
			<view class="nav-center">
				<text class="nav-title">语言</text>
			</view>
			<view class="nav-right"></view>
		</view> -->
		
		<!-- 内容区域 -->
		<view class="content-area">
			<!-- 语言选项列表 -->
			<view class="language-list">
				<view class="language-item" 
					v-for="(language, index) in languages" 
					:key="index"
					:class="{ active: currentLanguage === language.code }"
					@click="selectLanguage(language.code)">
					<view class="language-info">
						<text class="language-name">{{ language.name }}</text>
						<text class="language-native">{{ language.nativeName }}</text>
					</view>
					<view class="language-check" v-if="currentLanguage === language.code">
						<text class="check-icon">✓</text>
					</view>
				</view>
			</view>
			
			<!-- 说明文字 -->
			<view class="language-note">
				<text class="note-text">更改语言后，应用将重新启动以应用新语言设置</text>
			</view>
		</view>
		
		<!-- 底部手势条 -->
		<view class="home-indicator"></view>
	</view>
</template>

<script>
	export default {
		name: 'LanguageSettings',
		data() {
			return {
				currentLanguage: 'zh-CN',
				languages: [
					{
						code: 'zh-CN',
						name: '简体中文',
						nativeName: '简体中文'
					},
					{
						code: 'zh-HK',
						name: '繁体中文（香港）',
						nativeName: '繁體中文（香港）'
					},
					{
						code: 'zh-TW',
						name: '繁体中文（台湾）',
						nativeName: '繁體中文（台灣）'
					},
					{
						code: 'en',
						name: 'English',
						nativeName: 'English'
					}
				]
			}
		},
		onLoad() {
			// 加载当前语言设置
			this.loadCurrentLanguage()
		},
		methods: {
			goBack() {
				uni.navigateBack()
			},
			
			loadCurrentLanguage() {
				try {
					const savedLanguage = uni.getStorageSync('appLanguage')
					if (savedLanguage) {
						this.currentLanguage = savedLanguage
					}
				} catch (e) {
					console.error('加载语言设置失败:', e)
				}
			},
			
			selectLanguage(languageCode) {
				if (this.currentLanguage === languageCode) {
					return
				}
				
				uni.showModal({
					title: '确认更改语言',
					content: '更改语言后应用将重新启动，确定要继续吗？',
					success: (res) => {
						if (res.confirm) {
							this.currentLanguage = languageCode
							this.saveLanguage(languageCode)
							
							uni.showToast({
								title: '语言设置已保存',
								icon: 'success'
							})
							
							// 延迟重启应用
							setTimeout(() => {
								this.restartApp()
							}, 1500)
						}
					}
				})
			},
			
			saveLanguage(languageCode) {
				try {
					uni.setStorageSync('appLanguage', languageCode)
				} catch (e) {
					console.error('保存语言设置失败:', e)
				}
			},
			
			restartApp() {
				// 这里可以调用应用重启的API
				// 或者提示用户手动重启应用
				uni.showModal({
					title: '语言设置完成',
					content: '请手动重启应用以应用新的语言设置',
					showCancel: false,
					success: () => {
						uni.navigateBack()
					}
				})
			}
		}
	}
</script>

<style>
	.language-page {
		width: 100%;
		min-height: 100vh;
		background-color: #f8f8f8;
		display: flex;
		flex-direction: column;
	}
	
	/* 导航栏 */
	.nav-bar {
		height: 88rpx;
		background-color: #ffffff;
		display: flex;
		align-items: center;
		padding: 0 32rpx;
		border-bottom: 1rpx solid #f0f0f0;
	}
	
	.nav-left {
		width: 80rpx;
		height: 88rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
		padding: 0 16rpx;
		z-index: 10;
		position: relative;
	}
	
	.nav-left:active {
		background-color: rgba(0, 0, 0, 0.1);
	}
	
	.back-arrow {
		font-size: 48rpx;
		color: #333333;
		font-weight: 300;
	}
	
	.nav-center {
		flex: 1;
		display: flex;
		justify-content: center;
	}
	
	.nav-title {
		font-size: 32rpx;
		color: #333333;
		font-weight: 600;
	}
	
	.nav-right {
		width: 80rpx;
	}
	
	/* 内容区域 */
	.content-area {
		flex: 1;
		padding: 32rpx 0;
	}
	
	/* 语言列表 */
	.language-list {
		background-color: #ffffff;
		margin-bottom: 32rpx;
	}
	
	.language-item {
		padding: 32rpx;
		border-bottom: 1rpx solid #f0f0f0;
		display: flex;
		align-items: center;
		justify-content: space-between;
		min-height: 88rpx;
		cursor: pointer;
		transition: background-color 0.2s ease;
	}
	
	.language-item:last-child {
		border-bottom: none;
	}
	
	.language-item:active {
		background-color: rgba(0, 0, 0, 0.05);
	}
	
	.language-item.active {
		background-color: rgba(0, 122, 255, 0.05);
	}
	
	.language-info {
		flex: 1;
		display: flex;
		flex-direction: column;
		gap: 8rpx;
	}
	
	.language-name {
		font-size: 32rpx;
		color: #333333;
		font-weight: 500;
	}
	
	.language-native {
		font-size: 28rpx;
		color: #666666;
	}
	
	.language-check {
		width: 48rpx;
		height: 48rpx;
		background-color: #007aff;
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.check-icon {
		font-size: 24rpx;
		color: #ffffff;
		font-weight: 600;
	}
	
	/* 说明文字 */
	.language-note {
		padding: 32rpx;
		background-color: #ffffff;
	}
	
	.note-text {
		font-size: 28rpx;
		color: #999999;
		line-height: 1.5;
		text-align: center;
	}
	
	/* 底部手势条 */
	.home-indicator {
		height: 68rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.home-indicator::after {
		content: '';
		width: 200rpx;
		height: 8rpx;
		background-color: #333333;
		border-radius: 4rpx;
	}
</style>
