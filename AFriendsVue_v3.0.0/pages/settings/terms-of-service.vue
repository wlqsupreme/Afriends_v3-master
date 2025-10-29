<!--
 * @description 服务条款页面
 * @author AI Assistant
 * @created 2024-12-25
 * @version 1.0.0
 * 
 * 功能说明：
 * - 显示交友软件服务条款
 * - 包含账号注册、服务内容、隐私保护等条款
 * - 提供返回功能
 * 
 * 页面设计：
 * - 白底黑字主题
 * - 清晰的条款结构
 * - 易于阅读的排版
-->

<template>
	<view class="terms-of-service-page">
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
			<text class="title">服务条款</text>
			<view class="placeholder"></view>
		</view> -->
		
		<!-- 主要内容区域 -->
		<view class="main-content">
			<!-- 介绍部分 -->
			<view class="intro-section">
				<text class="intro-title">交友软件服务条款</text>
				<text class="intro-text">欢迎使用本软件！本条款是您与我们之间就本软件的使用所达成的协议。请您在注册、登录或使用本软件前，仔细阅读并充分理解本条款的全部内容。如果您不同意本条款的任何内容，请不要注册、登录或使用本软件。您注册、登录或使用本软件的行为，即视为您已同意受本条款约束。</text>
			</view>
			
			<!-- 条款内容 -->
			<view class="terms-content">
				<!-- 第一条 -->
				<view class="term-section">
					<text class="term-number">I.</text>
					<text class="term-title">账号注册与管理</text>
					<view class="term-items">
						<view class="term-item">
							<text class="item-number">1.</text>
							<text class="item-title">注册资格</text>
							<text class="item-content">用户必须年满18周岁，具有完全民事行为能力。我们保留根据法律法规和实际情况调整注册资格的权利。</text>
						</view>
						<view class="term-item">
							<text class="item-number">2.</text>
							<text class="item-title">注册信息</text>
							<text class="item-content">用户必须提供真实、准确、完整的个人信息（如手机号、昵称、性别、年龄、头像等），并保持信息更新。用户对提供虚假、不准确、不完整或过时信息造成的后果承担责任。</text>
						</view>
						<view class="term-item">
							<text class="item-number">3.</text>
							<text class="item-title">账号安全</text>
							<text class="item-content">用户必须妥善保管自己的账号和密码，不得转让、出租、出售或分享账号。如发现账号被非法使用或存在安全风险，应立即通知我们。因用户保管不当造成的损失，我们不承担责任。</text>
						</view>
						<view class="term-item">
							<text class="item-number">4.</text>
							<text class="item-title">账号注销</text>
							<text class="item-content">用户有权随时申请注销账号。注销后，个人信息将按照隐私政策进行处理，用户将无法使用相关服务。</text>
						</view>
					</view>
				</view>
				
				<!-- 第二条 -->
				<view class="term-section">
					<text class="term-number">II.</text>
					<text class="term-title">服务内容与使用规则</text>
					<view class="term-items">
						<view class="term-item">
							<text class="item-number">1.</text>
							<text class="item-title">服务内容</text>
							<text class="item-content">本软件提供交友相关服务，包括但不限于用户匹配、聊天沟通、动态分享、活动组织等。具体内容以本软件实际提供为准。我们保留根据业务发展和用户需求调整、更新或终止服务内容的权利，并会适当通知。</text>
						</view>
						<view class="term-item">
							<text class="item-number">2.</text>
							<text class="item-title">使用规则</text>
							<text class="item-content">用户在使用本软件时，必须遵守国家法律法规、社会道德和公共秩序。禁止行为包括：发布违法、违规、虚假、欺诈、淫秽、色情、暴力、恐怖、歧视、侮辱或诽谤等有害信息；侵犯他人知识产权、隐私权、名誉权或其他合法权益；利用本软件进行诈骗、传销、赌博或其他违法犯罪活动；干扰或破坏本软件正常运行，或恶意攻击、修改、破解本软件；违反本软件发布的其他规则和规定。</text>
						</view>
						<view class="term-item">
							<text class="item-number">3.</text>
							<text class="item-title">信息发布与传播</text>
							<text class="item-content">用户对本软件上发布的内容（包括但不限于文字、图片、视频）承担责任，确保内容合法、真实、准确，不侵犯他人权利。我们有权对发布的内容进行审核。如内容违规，我们可以直接删除、屏蔽或限制传播，无需通知，并可能根据情节轻重对账号进行警告、暂停或永久封禁。</text>
						</view>
						<view class="term-item">
							<text class="item-number">4.</text>
							<text class="item-title">付费服务</text>
							<text class="item-content">部分服务为付费服务。用户可根据需要选择购买。购买前，用户必须仔细阅读服务说明和价格信息。付费服务的具体内容、价格和有效期以本软件相关页面为准。购买后，付费服务一般不予退款，除非法律法规另有规定或双方另有约定。</text>
						</view>
					</view>
				</view>
				
				<!-- 第三条 -->
				<view class="term-section">
					<text class="term-number">III.</text>
					<text class="term-title">隐私保护</text>
					<view class="term-items">
						<view class="term-item">
							<text class="item-number">1.</text>
							<text class="item-title">信息收集</text>
							<text class="item-content">我们按照隐私政策收集、使用、存储和保护用户的个人信息。在注册和使用过程中，我们可能收集基本信息（如姓名、性别、年龄）、联系信息、位置信息和行为数据，以提供更好的服务和个性化体验。</text>
						</view>
						<view class="term-item">
							<text class="item-number">2.</text>
							<text class="item-title">信息使用与共享</text>
							<text class="item-content">我们仅在本条款和隐私政策规定的范围内使用用户的个人信息，不会向无关第三方分享、转让或披露个人信息，除非在以下情况下：获得用户明确同意；法律法规要求或主管机关指示；保护我们或其他用户的合法权益；与关联公司、合作伙伴等共享以提供软件服务所必需。</text>
						</view>
						<view class="term-item">
							<text class="item-number">3.</text>
							<text class="item-title">信息安全</text>
							<text class="item-content">我们将采取合理的技术和管理措施保护用户个人信息安全，防止信息泄露、损坏或丢失。如发生个人信息安全事件，我们将及时通知用户并按照相关法律法规处理。</text>
						</view>
					</view>
				</view>
				
				<!-- 第四条 -->
				<view class="term-section">
					<text class="term-number">IV.</text>
					<text class="term-title">知识产权</text>
					<view class="term-items">
						<view class="term-item">
							<text class="item-number">1.</text>
							<text class="item-title">软件与内容</text>
							<text class="item-content">本软件及其内容（如文字、图片、音频、视频、图标）的所有知识产权均属于我们或相关权利人。用户仅有权在本条款允许的范围内使用本软件和相关内容。未经我们或相关权利人书面同意，用户不得复制、传播、修改、改编、出租、出售或以其他方式使用本软件和相关内容。</text>
						</view>
						<view class="term-item">
							<text class="item-number">2.</text>
							<text class="item-title">用户内容授权</text>
							<text class="item-content">对于用户在本软件上发布的内容，用户授予我们全球性、不可撤销、永久性、免费且可再许可的权利，包括使用、复制、修改、改编、发布、翻译、传播、展示、表演和创作衍生作品等，用于本软件的运营、推广和其他合法目的。</text>
						</view>
					</view>
				</view>
				
				<!-- 第五条 -->
				<view class="term-section">
					<text class="term-number">V.</text>
					<text class="term-title">责任限制与免责声明</text>
					<view class="term-items">
						<view class="term-item">
							<text class="item-number">1.</text>
							<text class="item-title">责任限制</text>
							<text class="item-content">在法律法规允许的范围内，我们对本软件服务或相关事项给用户造成的任何直接、间接、偶然、特殊、惩罚性或后果性损失（包括但不限于利润损失、数据丢失、业务中断）不承担责任，无论原因如何（包括但不限于合同、侵权、过失），除非损失由我们的故意行为或重大过失造成。</text>
						</view>
						<view class="term-item">
							<text class="item-number">2.</text>
							<text class="item-title">免责声明</text>
							<text class="item-content">本软件服务可能受到各种因素影响，包括但不限于网络故障、服务器故障、通信故障、黑客攻击、计算机病毒、法律法规变化，导致服务中断、延迟、错误或不可用。我们对此不承担责任，但会努力减少此类情况的发生并及时通知。</text>
						</view>
					</view>
				</view>
				
				<!-- 第六条 -->
				<view class="term-section">
					<text class="term-number">VI.</text>
					<text class="term-title">协议变更与终止</text>
					<view class="term-items">
						<view class="term-item">
							<text class="item-number">1.</text>
							<text class="item-title">协议变更</text>
							<text class="item-content">我们有权根据业务发展、法律法规变化等修改或更新本条款。修改后的条款将在本软件上发布，自发布之日起生效。如用户继续使用本软件服务，即视为用户已接受变更后的条款。如用户不同意变更后的条款，应停止使用本软件服务。</text>
						</view>
						<view class="term-item">
							<text class="item-number">2.</text>
							<text class="item-title">协议终止</text>
							<text class="item-content">在以下情况下，我们有权直接终止本条款和用户对本软件服务的使用，无需通知：用户违反本条款任何规定；法律法规要求或主管机关指示；本软件因业务调整、技术升级或其他原因需要停止运营。协议终止后，我们将按照相关规定处理用户个人信息，用户应立即停止使用本软件服务。如用户违反本条款给造成损失，用户应承担赔偿责任。</text>
						</view>
					</view>
				</view>
				
				<!-- 第七条 -->
				<view class="term-section">
					<text class="term-number">VII.</text>
					<text class="term-title">争议解决</text>
					<view class="term-items">
						<view class="term-item">
							<text class="item-number">1.</text>
							<text class="item-title">适用法律</text>
							<text class="item-content">本条款的签署、履行、解释和争议解决均适用中华人民共和国法律（为本条款之目的，不包括香港特别行政区、澳门特别行政区和台湾地区的法律）。</text>
						</view>
						<view class="term-item">
							<text class="item-number">2.</text>
							<text class="item-title">争议解决方式</text>
							<text class="item-content">如用户与我们之间就本条款或本软件服务发生争议，双方应首先通过友好协商解决。如协商不成，任何一方均有权向有管辖权的人民法院提起诉讼。</text>
						</view>
					</view>
				</view>
				
				<!-- 第八条 -->
				<view class="term-section">
					<text class="term-number">VIII.</text>
					<text class="term-title">其他条款</text>
					<view class="term-items">
						<view class="term-item">
							<text class="item-number">1.</text>
							<text class="item-title">条款完整性</text>
							<text class="item-content">本条款构成用户与我们之间关于使用本软件服务的完整协议，取代之前关于本软件服务的所有口头或书面协议。</text>
						</view>
						<view class="term-item">
							<text class="item-number">2.</text>
							<text class="item-title">条款可分割性</text>
							<text class="item-content">如本条款任何规定被视为无效或不可执行，不影响其他规定的有效性和可执行性，此类无效或不可执行的规定应被视为可分割的，不影响本条款其他部分的效力。</text>
						</view>
						<view class="term-item">
							<text class="item-number">3.</text>
							<text class="item-title">弃权</text>
							<text class="item-content">我们未行使或延迟行使本条款下的任何权利或救济，不应被视为放弃该权利或救济；任何单一行使权利或救济的行为不影响我们行使其他权利或救济。</text>
						</view>
						<view class="term-item">
							<text class="item-number">4.</text>
							<text class="item-title">通知</text>
							<text class="item-content">我们向用户发出的通知可通过应用内消息、短信、邮件或其他合理方式进行。通知自发送之日起视为送达。如用户因自身原因无法接收通知，不影响通知的效力。</text>
						</view>
					</view>
				</view>
			</view>
			
			<!-- 结尾说明 -->
			<view class="closing-section">
				<text class="closing-text">请您再次仔细阅读本条款，确保您已充分理解并同意其全部内容。如有任何疑问或建议，请随时联系我们的客服人员。</text>
			</view>
		</view>
		
		<!-- 底部指示器 -->
		<view class="home-indicator"></view>
	</view>
</template>

<script>
	export default {
		name: 'TermsOfServicePage',
		methods: {
			goBack() {
				uni.navigateBack();
			}
		}
	}
</script>

<style scoped>
	.terms-of-service-page {
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
		border-radius: 24rpx;
		background: rgba(0, 0, 0, 0.05);
		transition: all 0.2s ease;
	}
	
	.back-button:active {
		background: rgba(0, 0, 0, 0.1);
		transform: scale(0.95);
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
	
	/* 主要内容区域 */
	.main-content {
		flex: 1;
		padding: 32rpx;
	}
	
	/* 介绍部分 */
	.intro-section {
		margin-bottom: 48rpx;
	}
	
	.intro-title {
		font-size: 32rpx;
		color: #333333;
		font-weight: 600;
		display: block;
		margin-bottom: 24rpx;
	}
	
	.intro-text {
		font-size: 28rpx;
		color: #666666;
		line-height: 1.6;
		display: block;
	}
	
	/* 条款内容 */
	.terms-content {
		margin-bottom: 48rpx;
	}
	
	.term-section {
		margin-bottom: 40rpx;
	}
	
	.term-number {
		font-size: 32rpx;
		color: #333333;
		font-weight: 600;
		display: block;
		margin-bottom: 16rpx;
	}
	
	.term-title {
		font-size: 30rpx;
		color: #333333;
		font-weight: 600;
		display: block;
		margin-bottom: 24rpx;
	}
	
	.term-items {
		display: flex;
		flex-direction: column;
		gap: 24rpx;
	}
	
	.term-item {
		display: flex;
		flex-direction: column;
		gap: 12rpx;
	}
	
	.item-number {
		font-size: 28rpx;
		color: #FF6B6B;
		font-weight: 600;
		display: block;
	}
	
	.item-title {
		font-size: 28rpx;
		color: #333333;
		font-weight: 600;
		display: block;
	}
	
	.item-content {
		font-size: 26rpx;
		color: #666666;
		line-height: 1.6;
		display: block;
	}
	
	/* 结尾说明 */
	.closing-section {
		padding: 32rpx 0;
		border-top: 1rpx solid #F0F0F0;
	}
	
	.closing-text {
		font-size: 26rpx;
		color: #999999;
		line-height: 1.6;
		text-align: center;
		display: block;
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
