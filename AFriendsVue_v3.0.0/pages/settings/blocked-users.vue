<!--
 * @description 黑名单管理页面
 * @author AI Assistant
 * @created 2024-12-25
 * @version 1.0.0
 * 
 * 功能说明：
 * - 显示已拉黑的用户列表
 * - 支持解除拉黑操作
 * - 支持搜索黑名单用户
 * - 与隐私设置页面关联
 * 
 * 页面设计：
 * - 白底黑字主题
 * - 响应式布局
 * - 用户友好的交互体验
-->

<template>
	<view class="blocked-users-page">
		<!-- 状态栏 -->
		<!-- <view class="status-bar">
			<text class="time">12:00</text>
			<view class="status-icons">
				<view class="signal"></view>
				<view class="wifi"></view>
				<view class="battery"></view>
			</view>
		</view> -->
		
		<!-- 导航栏 -->
		<!-- <view class="header">
			<view class="back-button" @click="goBack">
				<view class="back-arrow"></view>
			</view>
			<text class="title">黑名单管理</text>
			<view class="placeholder"></view>
		</view> -->
		
		<!-- 搜索栏 -->
		<view class="search-section">
			<view class="search-input">
				<view class="search-icon">
					<view class="search-magnifier"></view>
				</view>
				<input 
					class="search-field" 
					placeholder="搜索黑名单用户" 
					v-model="searchKeyword"
					@input="onSearchInput"
				/>
				<view v-if="searchKeyword" class="clear-button" @click="clearSearch">
					<view class="clear-icon"></view>
				</view>
			</view>
		</view>
		
		<!-- 统计信息 -->
		<view class="stats-section">
			<view class="stats-item">
				<text class="stats-number">{{ totalBlockedUsers }}</text>
				<text class="stats-label">已拉黑用户</text>
			</view>
			<view class="stats-item">
				<text class="stats-number">{{ searchResults.length }}</text>
				<text class="stats-label">搜索结果</text>
			</view>
		</view>
		
		<!-- 黑名单用户列表 -->
		<view class="users-list">
			<view v-if="filteredUsers.length === 0" class="empty-state">
				<view class="empty-icon">
					<view class="empty-face"></view>
				</view>
				<text class="empty-title">暂无黑名单用户</text>
				<text class="empty-description">你还没有拉黑任何用户</text>
			</view>
			
			<view 
				v-for="user in filteredUsers" 
				:key="user.id" 
				class="user-item"
			>
				<view class="user-info">
					<view class="user-avatar">
						<image v-if="user.avatar" :src="user.avatar" class="avatar-image" />
						<view v-else class="avatar-placeholder">
							<text class="avatar-text">{{ user.name.charAt(0) }}</text>
						</view>
					</view>
					<view class="user-details">
						<text class="user-name">{{ user.name }}</text>
						<text class="user-id">ID: {{ user.id }}</text>
						<text class="block-date">拉黑时间: {{ formatDate(user.blockDate) }}</text>
					</view>
				</view>
				<view class="user-actions">
					<view class="action-button unblock-btn" @click="unblockUser(user)">
						<text class="action-text">解除拉黑</text>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 底部提示 -->
		<view class="bottom-tip">
			<text class="tip-text">拉黑的用户将无法与你联系，也无法看到你的动态</text>
		</view>
		
		<!-- 底部指示器 -->
		<view class="home-indicator"></view>
	</view>
</template>

<script>
	export default {
		name: 'BlockedUsersPage',
		data() {
			return {
				searchKeyword: '',
				blockedUsers: [
					{
						id: '001',
						name: '张三',
						avatar: '',
						blockDate: '2024-12-20',
						reason: '骚扰行为'
					},
					{
						id: '002',
						name: '李四',
						avatar: '',
						blockDate: '2024-12-18',
						reason: '不当言论'
					},
					{
						id: '003',
						name: '王五',
						avatar: '',
						blockDate: '2024-12-15',
						reason: '恶意举报'
					},
					{
						id: '004',
						name: '赵六',
						avatar: '',
						blockDate: '2024-12-10',
						reason: '虚假信息'
					}
				]
			}
		},
		computed: {
			totalBlockedUsers() {
				return this.blockedUsers.length;
			},
			searchResults() {
				if (!this.searchKeyword.trim()) {
					return this.blockedUsers;
				}
				return this.blockedUsers.filter(user => 
					user.name.toLowerCase().includes(this.searchKeyword.toLowerCase()) ||
					user.id.includes(this.searchKeyword)
				);
			},
			filteredUsers() {
				return this.searchResults;
			}
		},
		methods: {
			goBack() {
				uni.navigateBack();
			},
			onSearchInput() {
				// 搜索逻辑已在computed中处理
			},
			clearSearch() {
				this.searchKeyword = '';
			},
			unblockUser(user) {
				uni.showModal({
					title: '确认解除拉黑',
					content: `确定要解除对 ${user.name} 的拉黑吗？`,
					success: (res) => {
						if (res.confirm) {
							// 从黑名单中移除用户
							const index = this.blockedUsers.findIndex(u => u.id === user.id);
							if (index > -1) {
								this.blockedUsers.splice(index, 1);
								uni.showToast({
									title: '已解除拉黑',
									icon: 'success'
								});
							}
						}
					}
				});
			},
			formatDate(dateString) {
				const date = new Date(dateString);
				return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`;
			}
		}
	}
</script>

<style>
	.blocked-users-page {
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
	
	/* 导航栏 */
	.header {
		height: 88rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 32rpx;
		border-bottom: 1rpx solid #F0F0F0;
	}
	
	.back-button {
		width: 48rpx;
		height: 48rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.back-arrow {
		width: 0;
		height: 0;
		border-right: 12rpx solid #000000;
		border-top: 8rpx solid transparent;
		border-bottom: 8rpx solid transparent;
	}
	
	.title {
		font-size: 32rpx;
		color: #333333;
		font-weight: 600;
	}
	
	.placeholder {
		width: 48rpx;
	}
	
	/* 搜索栏 */
	.search-section {
		padding: 24rpx 32rpx;
		background-color: #FFFFFF;
	}
	
	.search-input {
		display: flex;
		align-items: center;
		background-color: #F5F5F5;
		border-radius: 16rpx;
		padding: 16rpx 20rpx;
		gap: 16rpx;
	}
	
	.search-icon {
		width: 32rpx;
		height: 32rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.search-magnifier {
		width: 20rpx;
		height: 20rpx;
		border: 2rpx solid #999999;
		border-radius: 50%;
		position: relative;
	}
	
	.search-magnifier::after {
		content: '';
		position: absolute;
		bottom: -6rpx;
		right: -6rpx;
		width: 8rpx;
		height: 2rpx;
		background-color: #999999;
		transform: rotate(45deg);
	}
	
	.search-field {
		flex: 1;
		font-size: 28rpx;
		color: #333333;
		background: transparent;
		border: none;
		outline: none;
	}
	
	.clear-button {
		width: 32rpx;
		height: 32rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		cursor: pointer;
	}
	
	.clear-icon {
		width: 16rpx;
		height: 16rpx;
		position: relative;
	}
	
	.clear-icon::before,
	.clear-icon::after {
		content: '';
		position: absolute;
		top: 50%;
		left: 50%;
		width: 16rpx;
		height: 2rpx;
		background-color: #999999;
	}
	
	.clear-icon::before {
		transform: translate(-50%, -50%) rotate(45deg);
	}
	
	.clear-icon::after {
		transform: translate(-50%, -50%) rotate(-45deg);
	}
	
	/* 统计信息 */
	.stats-section {
		display: flex;
		padding: 0 32rpx 24rpx;
		gap: 48rpx;
	}
	
	.stats-item {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 8rpx;
	}
	
	.stats-number {
		font-size: 36rpx;
		color: #007AFF;
		font-weight: 600;
	}
	
	.stats-label {
		font-size: 24rpx;
		color: #666666;
	}
	
	/* 用户列表 */
	.users-list {
		flex: 1;
		padding: 0 32rpx;
	}
	
	.empty-state {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 120rpx 0;
		gap: 24rpx;
	}
	
	.empty-icon {
		width: 120rpx;
		height: 120rpx;
		background-color: #F0F0F0;
		border-radius: 60rpx;
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.empty-face {
		width: 80rpx;
		height: 80rpx;
		background-color: #CCCCCC;
		border-radius: 40rpx;
		position: relative;
	}
	
	.empty-face::before {
		content: '';
		position: absolute;
		top: 20rpx;
		left: 20rpx;
		width: 12rpx;
		height: 12rpx;
		background-color: #FFFFFF;
		border-radius: 50%;
	}
	
	.empty-face::after {
		content: '';
		position: absolute;
		top: 20rpx;
		right: 20rpx;
		width: 12rpx;
		height: 12rpx;
		background-color: #FFFFFF;
		border-radius: 50%;
	}
	
	.empty-title {
		font-size: 32rpx;
		color: #333333;
		font-weight: 500;
	}
	
	.empty-description {
		font-size: 26rpx;
		color: #999999;
	}
	
	.user-item {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 24rpx 0;
		border-bottom: 1rpx solid #F0F0F0;
	}
	
	.user-item:last-child {
		border-bottom: none;
	}
	
	.user-info {
		display: flex;
		align-items: center;
		gap: 20rpx;
		flex: 1;
	}
	
	.user-avatar {
		width: 80rpx;
		height: 80rpx;
		border-radius: 40rpx;
		overflow: hidden;
	}
	
	.avatar-image {
		width: 100%;
		height: 100%;
	}
	
	.avatar-placeholder {
		width: 100%;
		height: 100%;
		background: linear-gradient(135deg, #007AFF, #00C6FF);
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.avatar-text {
		font-size: 32rpx;
		color: #FFFFFF;
		font-weight: 600;
	}
	
	.user-details {
		display: flex;
		flex-direction: column;
		gap: 8rpx;
	}
	
	.user-name {
		font-size: 28rpx;
		color: #333333;
		font-weight: 500;
	}
	
	.user-id {
		font-size: 24rpx;
		color: #999999;
	}
	
	.block-date {
		font-size: 22rpx;
		color: #CCCCCC;
	}
	
	.user-actions {
		display: flex;
		gap: 16rpx;
	}
	
	.action-button {
		padding: 16rpx 24rpx;
		border-radius: 20rpx;
		cursor: pointer;
		transition: all 0.2s ease;
	}
	
	.unblock-btn {
		background-color: #007AFF;
	}
	
	.unblock-btn:active {
		background-color: #0056CC;
	}
	
	.action-text {
		font-size: 24rpx;
		color: #FFFFFF;
		font-weight: 500;
	}
	
	/* 底部提示 */
	.bottom-tip {
		padding: 32rpx;
		text-align: center;
	}
	
	.tip-text {
		font-size: 24rpx;
		color: #999999;
		line-height: 1.5;
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


