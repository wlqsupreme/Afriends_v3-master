<template>
	<view class="search-page">
		<view class="search-header">
			<view class="back-button" @click="goBack">
				<text>返回</text>
			</view>
			<input class="search-input" v-model="keyword" placeholder="搜索内容..." />
			<view class="search-btn" @click="search">搜索</view>
		</view>
		<view class="search-results">
			<text>搜索结果将在这里显示</text>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			keyword: ""
		}
	},
	methods: {
		goBack() {
			uni.navigateBack()
		},
		search() {
			uni.showToast({
				title: "搜索功能开发中",
				icon: "none"
			})
		}
	}
}
</script>

<style>
.search-page {
	padding: 20rpx;
}
.search-header {
	display: flex;
	align-items: center;
	gap: 20rpx;
	margin-bottom: 40rpx;
}
.back-button {
	padding: 10rpx 20rpx;
	background: #f0f0f0;
	border-radius: 10rpx;
}
.search-input {
	flex: 1;
	padding: 20rpx;
	border: 1rpx solid #ddd;
	border-radius: 10rpx;
}
.search-btn {
	padding: 20rpx 40rpx;
	background: #FF69B4;
	color: white;
	border-radius: 10rpx;
}
.search-results {
	padding: 40rpx;
	text-align: center;
	color: #666;
}
</style>
