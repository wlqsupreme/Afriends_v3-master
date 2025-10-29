<template>
	<view class="task-detail-page">
		<!-- 头部导航 -->
		<view class="header">
			<view class="back-button" @click="goBack">
				<svg class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" width="32" height="32">
					<path d="M407.01 512l286.008-286.008a35.84 35.84 0 0 0-50.683-50.683L330.982 486.656a35.84 35.84 0 0 0 0 50.683L642.34 848.69a35.84 35.84 0 0 0 50.683-50.683L407.009 512z" fill="#666666"></path>
				</svg>
			</view>
			<view class="header-title">
				<text class="title-text">任务详情</text>
			</view>
			<view class="placeholder"></view>
		</view>

		<!-- 任务基本信息 -->
		<view class="task-info-card" v-if="taskData && taskData.title">
			<view class="task-header">
				<view class="task-title-row">
					<view class="task-status" :class="taskData.status">
						<view v-if="taskData.status === 'completed'" class="status-icon">✓</view>
						<view v-else-if="taskData.status === 'active'" class="status-icon">●</view>
						<view v-else class="status-icon">○</view>
					</view>
					<text class="task-title">{{ taskData.title }}</text>
					<view v-if="taskData.status === 'completed'" class="completed-badge">已完成</view>
				</view>
				<text class="task-time">{{ taskData.createTime }}</text>
			</view>
			
			<text class="task-description">{{ taskData.description }}</text>
			
			<view class="task-progress" v-if="taskData.status === 'active'">
				<view class="progress-info">
					<text class="progress-label">完成进度</text>
					<text class="progress-text">{{ taskData.progress }}%</text>
				</view>
				<view class="progress-bar">
					<view class="progress-fill" :style="{ width: taskData.progress + '%' }"></view>
				</view>
			</view>
		</view>
		
		<!-- 加载中提示 -->
		<view v-else class="loading-card">
			<text class="loading-text">加载中...</text>
		</view>

		<!-- AI执行过程 -->
		<view class="execution-process" v-if="taskData && taskData.processSteps">
			<view class="section-header">
				<text class="section-title">AI执行过程</text>
				<view class="process-status" :class="taskData.status">
					<text>{{ getProcessStatusText() }}</text>
				</view>
			</view>
			
			<view class="process-steps">
				<view 
					class="process-step" 
					v-for="(step, index) in taskData.processSteps" 
					:key="index"
					:class="{ completed: step.completed, active: step.active }"
				>
					<view class="step-indicator">
						<view v-if="step.completed" class="step-icon completed">✓</view>
						<view v-else-if="step.active" class="step-icon active">●</view>
						<view v-else class="step-icon">○</view>
					</view>
					<view class="step-content">
						<text class="step-title">{{ step.title }}</text>
						<text class="step-description">{{ step.description }}</text>
						<text class="step-time" v-if="step.completed">{{ step.completedTime }}</text>
					</view>
				</view>
			</view>
		</view>

		<!-- AI分析结果 -->
		<view class="analysis-result" v-if="taskData && taskData.analysisResult && taskData.analysisResult.length > 0">
			<view class="section-header">
				<text class="section-title">AI分析结果</text>
			</view>
			
			<view class="result-content">
				<view class="result-item" v-for="(result, index) in taskData.analysisResult" :key="index">
					<view class="result-header">
						<text class="result-title">{{ result.title }}</text>
						<view class="result-score" :class="getScoreClass(result.score)">
							<text>{{ result.score }}</text>
						</view>
					</view>
					<text class="result-description">{{ result.description }}</text>
					<view class="result-details" v-if="result.details">
						<text class="detail-item" v-for="(detail, idx) in result.details" :key="idx">• {{ detail }}</text>
					</view>
				</view>
			</view>
		</view>

		<!-- 建议和下一步 -->
		<view class="suggestions" v-if="taskData && taskData.suggestions && taskData.suggestions.length > 0">
			<view class="section-header">
				<text class="section-title">建议和下一步</text>
			</view>
			
			<view class="suggestion-content">
				<view class="suggestion-item" v-for="(suggestion, index) in taskData.suggestions" :key="index">
					<view class="suggestion-icon">{{ suggestion.icon }}</view>
					<view class="suggestion-text">
						<text class="suggestion-title">{{ suggestion.title }}</text>
						<text class="suggestion-desc">{{ suggestion.description }}</text>
					</view>
				</view>
			</view>
		</view>

		<!-- 操作按钮 -->
		<view class="action-buttons" v-if="taskData && taskData.status">
			<view v-if="taskData.status === 'pending'" class="action-btn start-btn" @click="startTask">
				<text>开始任务</text>
			</view>
			<view v-else-if="taskData.status === 'completed'" class="action-btn rate-btn" @click="showRatingModal">
				<text>评价任务</text>
			</view>
			<view v-else-if="taskData.status === 'active'" class="action-btn terminate-btn" @click="showTerminateModal">
				<text>终止任务</text>
			</view>
		</view>

		<!-- 评分弹窗 -->
		<view class="rating-modal" :class="{ active: showRating }" @click="hideRatingModal">
			<view class="rating-content" @click.stop>
				<view class="rating-header">
					<text class="rating-title">任务评价</text>
					<view class="close-btn" @click="hideRatingModal">×</view>
				</view>
				
				<view class="rating-section">
					<text class="rating-label">请为AI的表现评分：</text>
					<view class="star-rating">
						<view 
							class="star" 
							v-for="i in 5" 
							:key="i"
							:class="{ active: i <= rating }"
							@click="setRating(i)"
						>
							<text>★</text>
						</view>
					</view>
					<text class="rating-text">{{ ratingText }}</text>
				</view>
				
				<view class="comment-section">
					<text class="comment-label">评价内容：</text>
					<textarea 
						class="comment-input" 
						v-model="comment" 
						placeholder="请输入您的评价..."
						maxlength="200"
					></textarea>
					<text class="char-count">{{ comment.length }}/200</text>
				</view>
				
				<view class="rating-actions">
					<view class="cancel-btn" @click="hideRatingModal">
						<text>取消</text>
					</view>
					<view class="submit-btn" @click="submitRating">
						<text>提交评价</text>
					</view>
				</view>
			</view>
		</view>

		<!-- 终止任务确认弹窗 -->
		<view class="terminate-modal" :class="{ active: showTerminate }" @click="hideTerminateModal">
			<view class="terminate-content" @click.stop>
				<view class="terminate-header">
					<text class="terminate-title">终止任务确认</text>
					<view class="close-btn" @click="hideTerminateModal">×</view>
				</view>
				
				<view class="terminate-body">
					<text class="terminate-message">确定要终止当前任务吗？</text>
					<text class="terminate-warning">终止后任务将无法恢复，请谨慎操作。</text>
				</view>
				
				<view class="terminate-actions">
					<view class="cancel-btn" @click="hideTerminateModal">
						<text>取消</text>
					</view>
					<view class="confirm-btn" @click="confirmTerminate">
						<text>确认终止</text>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		name: 'TaskDetailPage',
		data() {
			return {
				taskId: null,
				showRating: false,
				showTerminate: false,
				rating: 0,
				comment: '',
				taskData: {}
			}
		},
		computed: {
			ratingText() {
				const texts = ['', '很差', '较差', '一般', '较好', '很好'];
				return texts[this.rating] || '';
			}
		},
		onLoad(options) {
			if (options.taskId) {
				this.taskId = parseInt(options.taskId);
				this.loadTaskDetail();
			} else {
				this.taskId = 1;
				this.loadTaskDetail();
			}
		},
		methods: {
			goBack() {
				uni.navigateBack();
			},
			
			async loadTaskDetail() {
				this.loading = true;
				this.errorMessage = '';
				
				try {
					console.log('开始加载任务详情，任务ID:', this.taskId);
					
					const response = await uni.request({
						url: `http://localhost:8888/api/a-entities/ai-task-log/by-user-task/${this.taskId}`,
						method: 'GET',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('任务详情响应:', response);
					
					if (response.statusCode === 200 && response.data) {
						// 转换后端数据为前端显示格式
						this.taskData = this.convertTaskData(response.data);
						console.log('转换后的任务数据:', this.taskData);
					} else if (response.statusCode === 404) {
						this.errorMessage = '任务不存在';
						console.log('任务不存在，任务ID:', this.taskId);
					} else {
						throw new Error('加载任务详情失败');
					}
				} catch (error) {
					console.error('加载任务详情异常:', error);
					this.errorMessage = '加载任务详情失败，请重试';
					uni.showToast({
						title: '加载失败',
						icon: 'error'
					});
				} finally {
					this.loading = false;
				}
			},
			
			// 转换后端数据为前端显示格式
			convertTaskData(backendData) {
				// 检查是否是新的API返回格式（包含aiTaskLog和taskPercent）
				let taskLog = backendData;
				let taskPercent = 0;
				let userTaskStatus = 0;
				
				if (backendData.aiTaskLog) {
					// 新格式：包含aiTaskLog和taskPercent
					taskLog = backendData.aiTaskLog;
					taskPercent = backendData.taskPercent || 0;
					userTaskStatus = backendData.userTaskStatus || 0;
				}
				
				// 根据状态码转换状态
				let status = 'pending';
				if (taskLog.status === 0) {
					status = 'pending';  // 未完成
				} else if (taskLog.status === 1) {
					status = 'active';   // 正在进行
				} else if (taskLog.status === 2) {
					status = 'completed'; // 已完成
				} else if (taskLog.status === 3) {
					status = 'error';    // 出错了
				}
				
				// 格式化时间
				const formatTime = (timestamp) => {
					if (!timestamp) return '';
					const date = new Date(timestamp);
					return date.toLocaleString('zh-CN', {
						year: 'numeric',
						month: '2-digit',
						day: '2-digit',
						hour: '2-digit',
						minute: '2-digit'
					});
				};
				
				// 使用从数据库读取的真实进度值
				let progress = taskPercent;
				if (status === 'completed') {
					progress = 100;
				} else if (status === 'pending') {
					progress = 0;
				}
				
				// 生成执行步骤（基于任务描述）
				const processSteps = this.generateProcessSteps(taskLog.aiTaskDesc, status);
				
				// 生成分析结果（基于任务评论和星级）
				const analysisResult = this.generateAnalysisResult(taskLog.taskComment, taskLog.taskStar);
				
				// 生成建议
				const suggestions = this.generateSuggestions(status, taskLog.taskComment);
				
				return {
					id: taskLog.aiTaskLogId,
					title: taskLog.aiTaskName || '未命名任务',
					description: taskLog.aiTaskDesc || '暂无描述',
					status: status,
					progress: progress,
					createTime: formatTime(taskLog.createdAt),
					completedTime: status === 'completed' ? formatTime(taskLog.updatedAt) : null,
					processSteps: processSteps,
					analysisResult: analysisResult,
					suggestions: suggestions
				};
			},
			
			// 生成执行步骤
			generateProcessSteps(description, status) {
				if (!description) return [];
				
				const steps = [
					{
						title: '任务分析',
						description: '分析任务需求和目标',
						completed: true,
						completedTime: '09:00'
					},
					{
						title: '方案制定',
						description: '制定执行方案和策略',
						completed: status === 'completed' || status === 'active',
						active: status === 'active',
						completedTime: status === 'completed' ? '10:30' : null
					},
					{
						title: '任务执行',
						description: '执行具体任务内容',
						completed: status === 'completed',
						active: status === 'active',
						completedTime: status === 'completed' ? '14:20' : null
					},
					{
						title: '结果评估',
						description: '评估任务执行结果',
						completed: status === 'completed',
						completedTime: status === 'completed' ? '16:45' : null
					}
				];
				
				return steps;
			},
			
			// 生成分析结果
			generateAnalysisResult(comment, star) {
				if (!comment && !star) return [];
				
				const results = [];
				
				if (star) {
					let score = 'B';
					if (star >= 4) score = 'A';
					else if (star >= 3) score = 'B+';
					else if (star <= 2) score = 'C';
					
					results.push({
						title: '任务完成质量',
						score: score,
						description: this.getScoreDescription(score),
						details: this.getScoreDetails(score)
					});
				}
				
				if (comment) {
					results.push({
						title: '用户反馈',
						score: 'A',
						description: comment,
						details: ['用户评价内容']
					});
				}
				
				return results;
			},
			
			// 生成建议
			generateSuggestions(status, comment) {
				const suggestions = [];
				
				if (status === 'completed') {
					suggestions.push({
						icon: '✅',
						title: '任务完成',
						description: '任务已成功完成，感谢您的使用'
					});
				} else if (status === 'active') {
					suggestions.push({
						icon: '⏳',
						title: '继续执行',
						description: '任务正在执行中，请耐心等待'
					});
				} else {
					suggestions.push({
						icon: '🚀',
						title: '开始任务',
						description: '建议尽快开始执行任务'
					});
				}
				
				if (comment) {
					suggestions.push({
						icon: '💬',
						title: '用户反馈',
						description: '已收到您的反馈，我们会持续改进'
					});
				}
				
				return suggestions;
			},
			
			// 获取评分描述
			getScoreDescription(score) {
				const descriptions = {
					'A': '任务完成质量优秀，超出预期',
					'B+': '任务完成质量良好，符合预期',
					'B': '任务完成质量一般，基本符合预期',
					'C': '任务完成质量有待提升'
				};
				return descriptions[score] || '任务完成质量待评估';
			},
			
			// 获取评分详情
			getScoreDetails(score) {
				const details = {
					'A': ['执行效率高', '结果质量优秀', '用户体验良好'],
					'B+': ['执行效率良好', '结果质量较好', '用户体验满意'],
					'B': ['执行效率一般', '结果质量基本合格', '用户体验一般'],
					'C': ['执行效率较低', '结果质量需要改进', '用户体验有待提升']
				};
				return details[score] || ['待评估'];
			},
			
			getTaskDetails() {
				return {
					1: {
						id: 1,
						title: '帮助用户制定学习计划',
						description: '根据用户的学习目标和时间安排，制定详细的学习计划，包括每日任务、复习安排等。',
						status: 'completed',
						progress: 100,
						createTime: '2024-01-15 09:30',
						completedTime: '2024-01-15 16:45',
						processSteps: [
							{
								title: '分析用户需求',
								description: '了解用户的学习目标、当前水平、可用时间等基本信息',
								completed: true,
								completedTime: '09:35'
							},
							{
								title: '制定学习框架',
								description: '基于用户需求设计整体学习框架和阶段性目标',
								completed: true,
								completedTime: '10:15'
							},
							{
								title: '细化每日任务',
								description: '将学习目标分解为具体的每日学习任务和复习安排',
								completed: true,
								completedTime: '14:20'
							},
							{
								title: '优化学习计划',
								description: '根据用户反馈调整学习计划，确保可行性和有效性',
								completed: true,
								completedTime: '16:45'
							}
						],
						analysisResult: [
							{
								title: '学习目标分析',
								score: 'A+',
								description: '用户目标明确，时间安排合理，学习动机强烈',
								details: [
									'目标具体可量化',
									'时间分配科学合理',
									'学习资源充足'
								]
							},
							{
								title: '学习计划质量',
								score: 'A',
								description: '制定的学习计划结构清晰，循序渐进',
								details: [
									'计划层次分明',
									'任务难度适中',
									'复习安排科学'
								]
							}
						],
						suggestions: [
							{
								icon: '📚',
								title: '坚持执行',
								description: '建议严格按照计划执行，定期评估学习效果'
							},
							{
								icon: '📊',
								title: '定期调整',
								description: '根据学习进度和效果，适时调整学习计划'
							},
							{
								icon: '🎯',
								title: '目标导向',
								description: '保持目标导向，及时调整学习策略'
							}
						]
					},
					2: {
						id: 2,
						title: '分析用户情感状态',
						description: '通过对话分析用户的情绪变化，提供相应的情感支持和建议。',
						status: 'active',
						progress: 65,
						createTime: '2024-01-16 14:20',
						processSteps: [
							{
								title: '情感数据收集',
								description: '收集用户对话中的情感表达和情绪指标',
								completed: true,
								completedTime: '14:25'
							},
							{
								title: '情绪模式分析',
								description: '分析用户的情感变化模式和触发因素',
								completed: true,
								completedTime: '15:10'
							},
							{
								title: '情感支持策略',
								description: '制定个性化的情感支持和干预策略',
								completed: false,
								active: true
							},
							{
								title: '效果评估',
								description: '评估情感支持的效果并持续优化',
								completed: false
							}
						],
						analysisResult: [
							{
								title: '情绪稳定性',
								score: 'B+',
								description: '用户情绪波动较大，需要重点关注',
								details: [
									'情绪变化频率较高',
									'压力反应明显',
									'需要情感支持'
								]
							}
						],
						suggestions: [
							{
								icon: '💝',
								title: '情感关怀',
								description: '提供更多情感支持和关怀，帮助稳定情绪'
							},
							{
								icon: '🧘',
								title: '放松技巧',
								description: '学习一些放松和情绪调节技巧'
							}
						]
					},
					3: {
						id: 3,
						title: '推荐适合的社交活动',
						description: '基于用户的兴趣爱好和社交偏好，推荐合适的社交活动和交友建议。',
						status: 'active',
						progress: 30,
						createTime: '2024-01-17 10:15',
						processSteps: [
							{
								title: '兴趣分析',
								description: '分析用户的兴趣爱好和社交偏好',
								completed: true,
								completedTime: '10:20'
							},
							{
								title: '活动匹配',
								description: '根据用户兴趣匹配合适的社交活动',
								completed: false,
								active: true
							},
							{
								title: '推荐优化',
								description: '优化推荐算法，提高推荐准确性',
								completed: false
							},
							{
								title: '效果跟踪',
								description: '跟踪推荐效果，持续改进',
								completed: false
							}
						],
						analysisResult: [
							{
								title: '兴趣匹配度',
								score: 'A-',
								description: '用户兴趣明确，匹配度较高',
								details: [
									'兴趣爱好多样化',
									'社交需求明确',
									'参与意愿强烈'
								]
							}
						],
						suggestions: [
							{
								icon: '🎉',
								title: '积极参与',
								description: '建议积极参与推荐的社交活动'
							},
							{
								icon: '👥',
								title: '扩大社交圈',
								description: '通过活动认识更多志同道合的朋友'
							}
						]
					},
					4: {
						id: 4,
						title: '协助用户解决问题',
						description: '帮助用户分析当前遇到的问题，提供解决方案和行动建议。',
						status: 'completed',
						progress: 100,
						createTime: '2024-01-14 16:00',
						completedTime: '2024-01-15 11:30',
						processSteps: [
							{
								title: '问题识别',
								description: '准确识别用户遇到的核心问题',
								completed: true,
								completedTime: '16:05'
							},
							{
								title: '原因分析',
								description: '深入分析问题的根本原因',
								completed: true,
								completedTime: '16:30'
							},
							{
								title: '方案制定',
								description: '制定多种解决方案供用户选择',
								completed: true,
								completedTime: '09:15'
							},
							{
								title: '实施指导',
								description: '提供详细的实施指导和后续支持',
								completed: true,
								completedTime: '11:30'
							}
						],
						analysisResult: [
							{
								title: '问题解决能力',
								score: 'A+',
								description: '问题分析透彻，解决方案有效',
								details: [
									'问题识别准确',
									'分析深入全面',
									'方案切实可行'
								]
							}
						],
						suggestions: [
							{
								icon: '✅',
								title: '执行方案',
								description: '按照制定的方案逐步执行'
							},
							{
								icon: '📞',
								title: '寻求支持',
								description: '如遇困难及时寻求帮助和支持'
							}
						]
					},
					5: {
						id: 5,
						title: '制定健身计划',
						description: '根据用户的身体状况和健身目标，制定个性化的健身计划和饮食建议。',
						status: 'pending',
						progress: 0,
						createTime: '2024-01-18 08:45',
						processSteps: [
							{
								title: '身体状况评估',
								description: '评估用户当前的身体状况和健康水平',
								completed: false
							},
							{
								title: '目标设定',
								description: '与用户一起设定合理的健身目标',
								completed: false
							},
							{
								title: '计划制定',
								description: '制定个性化的健身计划和饮食建议',
								completed: false
							},
							{
								title: '跟踪调整',
								description: '跟踪执行情况并适时调整计划',
								completed: false
							}
						],
						analysisResult: [],
						suggestions: [
							{
								icon: '🏃',
								title: '开始行动',
								description: '建议尽快开始制定和执行健身计划'
							},
							{
								icon: '💪',
								title: '坚持锻炼',
								description: '制定计划后要坚持执行，才能看到效果'
							}
						]
					}
				};
			},
			
			getProcessStatusText() {
				switch(this.taskData.status) {
					case 'completed': return '已完成';
					case 'active': return '进行中';
					case 'pending': return '待开始';
					default: return '未知状态';
				}
			},
			
			getScoreClass(score) {
				if (score.includes('A+')) return 'score-excellent';
				if (score.includes('A')) return 'score-good';
				if (score.includes('B')) return 'score-average';
				return 'score-poor';
			},
			
			showRatingModal() {
				this.rating = 0;
				this.comment = '';
				this.showRating = true;
			},
			
			hideRatingModal() {
				this.showRating = false;
			},
			
			setRating(score) {
				this.rating = score;
			},
			
			submitRating() {
				if (this.rating === 0) {
					uni.showToast({
						title: '请选择评分',
						icon: 'none'
					});
					return;
				}
				
				uni.showToast({
					title: '评价提交成功！',
					icon: 'success'
				});
				
				this.hideRatingModal();
			},
			
			showTerminateModal() {
				this.showTerminate = true;
			},
			
			hideTerminateModal() {
				this.showTerminate = false;
			},
			
			confirmTerminate() {
				// 更新任务状态为已终止
				this.taskData.status = 'terminated';
				this.taskData.progress = 0;
				
				// 隐藏弹窗
				this.hideTerminateModal();
				
				// 显示成功提示
				uni.showToast({
					title: '任务已终止',
					icon: 'success'
				});
				
				// 通知父页面更新任务状态
				const pages = getCurrentPages();
				const prevPage = pages[pages.length - 2];
				if (prevPage && prevPage.updateTaskStatus) {
					prevPage.updateTaskStatus(this.taskId, 'terminated');
				}
				
				// 延迟返回上一页
				setTimeout(() => {
					uni.navigateBack();
				}, 1500);
			},
			
			// 开始任务
			async startTask() {
				try {
					console.log('开始任务，任务ID:', this.taskId);
					
					uni.showLoading({
						title: '正在开始任务...'
					});
					
					const response = await uni.request({
						url: `http://localhost:8888/api/a-entities/ai-task-log/start-task/${this.taskId}`,
						method: 'POST',
						header: {
							'Content-Type': 'application/json'
						}
					});
					
					console.log('开始任务响应:', response);
					
					if (response.statusCode === 200 && response.data.success) {
						// 更新本地状态
						this.taskData.status = 'active';
						this.taskData.progress = 10; // 开始任务时设置初始进度
						
						// 重新生成执行步骤
						this.taskData.processSteps = this.generateProcessSteps(this.taskData.description, 'active');
						
						// 重新生成建议
						this.taskData.suggestions = this.generateSuggestions('active', this.taskData.comment);
						
						uni.hideLoading();
						uni.showToast({
							title: '任务已开始',
							icon: 'success'
						});
						
						// 通知父页面更新任务状态
						const pages = getCurrentPages();
						const prevPage = pages[pages.length - 2];
						if (prevPage && prevPage.updateTaskStatus) {
							prevPage.updateTaskStatus(this.taskId, 'active');
						}
					} else {
						uni.hideLoading();
						uni.showToast({
							title: response.data.message || '开始任务失败',
							icon: 'error'
						});
					}
				} catch (error) {
					console.error('开始任务异常:', error);
					uni.hideLoading();
					uni.showToast({
						title: '开始任务失败，请重试',
						icon: 'error'
					});
				}
			},
			
		}
	}
</script>

<style>
	.task-detail-page {
		min-height: 100vh;
		background: #f5f5f5;
	}

	/* 头部 */
	.header {
		height: 88rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 0 32rpx;
		background-color: #FFFFFF;
		border-bottom: 1rpx solid #F0F0F0;
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

	.back-button .icon {
		width: 32rpx;
		height: 32rpx;
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

	.placeholder {
		width: 60rpx;
	}

	/* 加载中提示 */
	.loading-card {
		background: #FFFFFF;
		margin: 24rpx;
		border-radius: 20rpx;
		padding: 80rpx 32rpx;
		text-align: center;
		box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.1);
	}

	.loading-text {
		font-size: 28rpx;
		color: #666;
	}

	/* 任务信息卡片 */
	.task-info-card {
		background: #FFFFFF;
		margin: 24rpx;
		border-radius: 20rpx;
		padding: 32rpx;
		box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.1);
	}

	.task-header {
		display: flex;
		justify-content: space-between;
		align-items: flex-start;
		margin-bottom: 24rpx;
	}

	.task-title-row {
		display: flex;
		align-items: center;
		gap: 16rpx;
		flex: 1;
	}

	.task-status {
		width: 32rpx;
		height: 32rpx;
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		flex-shrink: 0;
	}

	.task-status.active {
		background: #4CAF50;
	}

	.task-status.completed {
		background: #2196F3;
	}

	.task-status.pending {
		background: #FF9800;
	}

	.status-icon {
		color: white;
		font-size: 20rpx;
		font-weight: bold;
	}

	.task-title {
		font-size: 32rpx;
		color: #333;
		font-weight: 600;
		flex: 1;
	}

	.completed-badge {
		padding: 4rpx 12rpx;
		background: #E3F2FD;
		color: #1976D2;
		border-radius: 12rpx;
		font-size: 22rpx;
	}

	.task-time {
		font-size: 24rpx;
		color: #999;
		flex-shrink: 0;
	}

	.task-description {
		font-size: 28rpx;
		color: #666;
		line-height: 1.5;
		margin-bottom: 24rpx;
	}

	.task-progress {
		background: #F8F9FA;
		padding: 24rpx;
		border-radius: 16rpx;
	}

	.progress-info {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 16rpx;
	}

	.progress-label {
		font-size: 26rpx;
		color: #333;
		font-weight: 600;
	}

	.progress-text {
		font-size: 28rpx;
		color: #4CAF50;
		font-weight: 600;
	}

	.progress-bar {
		height: 12rpx;
		background: #E0E0E0;
		border-radius: 6rpx;
		overflow: hidden;
	}

	.progress-fill {
		height: 100%;
		background: linear-gradient(90deg, #4CAF50, #8BC34A);
		border-radius: 6rpx;
		transition: width 0.3s ease;
	}

	/* 执行过程 */
	.execution-process {
		background: #FFFFFF;
		margin: 0 24rpx 24rpx 24rpx;
		border-radius: 20rpx;
		padding: 32rpx;
		box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.1);
	}

	.section-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 32rpx;
	}

	.section-title {
		font-size: 32rpx;
		color: #333;
		font-weight: 600;
	}

	.process-status {
		padding: 8rpx 16rpx;
		border-radius: 16rpx;
		font-size: 24rpx;
	}

	.process-status.completed {
		background: #E3F2FD;
		color: #1976D2;
	}

	.process-status.active {
		background: #E8F5E8;
		color: #4CAF50;
	}

	.process-status.pending {
		background: #FFF3E0;
		color: #FF9800;
	}

	.process-steps {
		display: flex;
		flex-direction: column;
		gap: 24rpx;
	}

	.process-step {
		display: flex;
		gap: 20rpx;
		opacity: 0.6;
		transition: opacity 0.3s ease;
	}

	.process-step.completed {
		opacity: 1;
	}

	.process-step.active {
		opacity: 1;
	}

	.step-indicator {
		flex-shrink: 0;
		width: 40rpx;
		height: 40rpx;
		border-radius: 50%;
		display: flex;
		align-items: center;
		justify-content: center;
		background: #F5F5F5;
	}

	.step-icon {
		font-size: 24rpx;
		color: #999;
	}

	.step-icon.completed {
		color: #4CAF50;
	}

	.step-icon.active {
		color: #2196F3;
	}

	.step-content {
		flex: 1;
	}

	.step-title {
		font-size: 28rpx;
		color: #333;
		font-weight: 600;
		margin-bottom: 8rpx;
		display: block;
	}

	.step-description {
		font-size: 26rpx;
		color: #666;
		line-height: 1.4;
		margin-bottom: 8rpx;
		display: block;
	}

	.step-time {
		font-size: 22rpx;
		color: #999;
	}

	/* 分析结果 */
	.analysis-result {
		background: #FFFFFF;
		margin: 0 24rpx 24rpx 24rpx;
		border-radius: 20rpx;
		padding: 32rpx;
		box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.1);
	}

	.result-content {
		display: flex;
		flex-direction: column;
		gap: 24rpx;
	}

	.result-item {
		background: #F8F9FA;
		padding: 24rpx;
		border-radius: 16rpx;
	}

	.result-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 12rpx;
	}

	.result-title {
		font-size: 28rpx;
		color: #333;
		font-weight: 600;
	}

	.result-score {
		padding: 4rpx 12rpx;
		border-radius: 12rpx;
		font-size: 24rpx;
		font-weight: 600;
	}

	.score-excellent {
		background: #E8F5E8;
		color: #4CAF50;
	}

	.score-good {
		background: #E3F2FD;
		color: #2196F3;
	}

	.score-average {
		background: #FFF3E0;
		color: #FF9800;
	}

	.score-poor {
		background: #FFEBEE;
		color: #F44336;
	}

	.result-description {
		font-size: 26rpx;
		color: #666;
		line-height: 1.4;
		margin-bottom: 12rpx;
		display: block;
	}

	.result-details {
		display: flex;
		flex-direction: column;
		gap: 8rpx;
	}

	.detail-item {
		font-size: 24rpx;
		color: #666;
		line-height: 1.4;
	}

	/* 建议 */
	.suggestions {
		background: #FFFFFF;
		margin: 0 24rpx 24rpx 24rpx;
		border-radius: 20rpx;
		padding: 32rpx;
		box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.1);
	}

	.suggestion-content {
		display: flex;
		flex-direction: column;
		gap: 20rpx;
	}

	.suggestion-item {
		display: flex;
		gap: 16rpx;
		align-items: flex-start;
	}

	.suggestion-icon {
		font-size: 32rpx;
		flex-shrink: 0;
	}

	.suggestion-text {
		flex: 1;
	}

	.suggestion-title {
		font-size: 26rpx;
		color: #333;
		font-weight: 600;
		margin-bottom: 4rpx;
		display: block;
	}

	.suggestion-desc {
		font-size: 24rpx;
		color: #666;
		line-height: 1.4;
	}

	/* 操作按钮 */
	.action-buttons {
		display: flex;
		gap: 16rpx;
		padding: 24rpx;
		background: #FFFFFF;
		margin: 0 24rpx 24rpx 24rpx;
		border-radius: 20rpx;
		box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.1);
	}

	.action-btn {
		flex: 1;
		height: 80rpx;
		border-radius: 40rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 28rpx;
		font-weight: 600;
		cursor: pointer;
		transition: all 0.2s ease;
	}

	.rate-btn {
		background: #FF9800;
		color: white;
	}

	.rate-btn:active {
		background: #F57C00;
		transform: scale(0.95);
	}

	.terminate-btn {
		background: #F44336;
		color: white;
	}

	.terminate-btn:active {
		background: #D32F2F;
		transform: scale(0.95);
	}

	.start-btn {
		background: #4CAF50;
		color: white;
	}

	.start-btn:active {
		background: #388E3C;
		transform: scale(0.95);
	}


	/* 评分弹窗 */
	.rating-modal {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background: rgba(0, 0, 0, 0.5);
		display: flex;
		align-items: center;
		justify-content: center;
		z-index: 1000;
		opacity: 0;
		visibility: hidden;
		transition: all 0.3s ease;
	}

	.rating-modal.active {
		opacity: 1;
		visibility: visible;
	}

	.rating-content {
		width: 600rpx;
		max-height: 80vh;
		background: white;
		border-radius: 20rpx;
		padding: 40rpx;
		overflow-y: auto;
	}

	.rating-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 32rpx;
	}

	.rating-title {
		font-size: 32rpx;
		color: #333;
		font-weight: 600;
	}

	.close-btn {
		width: 48rpx;
		height: 48rpx;
		border-radius: 50%;
		background: #F5F5F5;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 32rpx;
		color: #666;
		cursor: pointer;
	}

	.rating-section {
		margin-bottom: 32rpx;
	}

	.rating-label {
		font-size: 26rpx;
		color: #333;
		margin-bottom: 16rpx;
		display: block;
	}

	.star-rating {
		display: flex;
		gap: 16rpx;
		margin-bottom: 16rpx;
	}

	.star {
		font-size: 48rpx;
		color: #E0E0E0;
		cursor: pointer;
		transition: color 0.2s ease;
	}

	.star.active {
		color: #FFD700;
	}

	.rating-text {
		font-size: 24rpx;
		color: #666;
		text-align: center;
		display: block;
	}

	.comment-section {
		margin-bottom: 32rpx;
	}

	.comment-label {
		font-size: 26rpx;
		color: #333;
		margin-bottom: 16rpx;
		display: block;
	}

	.comment-input {
		width: 100%;
		height: 120rpx;
		padding: 20rpx;
		border: 1rpx solid #E0E0E0;
		border-radius: 12rpx;
		font-size: 26rpx;
		resize: none;
		box-sizing: border-box;
	}

	.char-count {
		font-size: 22rpx;
		color: #999;
		text-align: right;
		margin-top: 8rpx;
		display: block;
	}

	.rating-actions {
		display: flex;
		gap: 24rpx;
	}

	.cancel-btn, .submit-btn {
		flex: 1;
		height: 80rpx;
		border-radius: 40rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 28rpx;
		cursor: pointer;
		transition: all 0.2s ease;
	}

	.cancel-btn {
		background: #F5F5F5;
		color: #666;
	}

	.cancel-btn:active {
		background: #E0E0E0;
	}

	.submit-btn {
		background: #4CAF50;
		color: white;
	}

	.submit-btn:active {
		background: #388E3C;
	}

	/* 终止任务弹窗 */
	.terminate-modal {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background: rgba(0, 0, 0, 0.5);
		display: flex;
		align-items: center;
		justify-content: center;
		z-index: 1000;
		opacity: 0;
		visibility: hidden;
		transition: all 0.3s ease;
	}

	.terminate-modal.active {
		opacity: 1;
		visibility: visible;
	}

	.terminate-content {
		width: 600rpx;
		background: white;
		border-radius: 20rpx;
		padding: 40rpx;
		overflow: hidden;
	}

	.terminate-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 32rpx;
	}

	.terminate-title {
		font-size: 32rpx;
		color: #333;
		font-weight: 600;
	}

	.terminate-body {
		margin-bottom: 32rpx;
	}

	.terminate-message {
		font-size: 28rpx;
		color: #333;
		margin-bottom: 16rpx;
		display: block;
	}

	.terminate-warning {
		font-size: 24rpx;
		color: #F44336;
		line-height: 1.4;
	}

	.terminate-actions {
		display: flex;
		gap: 24rpx;
	}

	.confirm-btn {
		flex: 1;
		height: 80rpx;
		border-radius: 40rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 28rpx;
		cursor: pointer;
		transition: all 0.2s ease;
		background: #F44336;
		color: white;
	}

	.confirm-btn:active {
		background: #D32F2F;
	}
</style>
