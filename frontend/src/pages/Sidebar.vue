<template>
	<div class="sidebar" v-if="visible">
		<!-- 遮罩层 -->
		<div class="sidebar-mask" @click="closeSidebar"></div>
		
		<!-- 侧边栏内容 -->
		<div class="sidebar-content" :class="{ 'sidebar-show': visible }">
			<!-- 头部 -->
			<div class="sidebar-header">
				<div class="header-info">
					<img class="admin-avatar" src="/src/assets/logo.svg" alt="Admin" />
					<div class="admin-info">
						<span class="admin-name">推荐算法管理员</span>
						<span class="admin-role">系统管理</span>
					</div>
				</div>
				<div class="close-btn" @click="closeSidebar">
					<el-icon><Close /></el-icon>
				</div>
			</div>
			
			<!-- 菜单列表 -->
			<div class="sidebar-menu">
				<div class="menu-section">
					<span class="section-title">推荐算法管理</span>
					
					<!-- 文字推荐 -->
					<div class="menu-item" @click="navigateTo('/text-recommendation')">
						<div class="menu-icon text-icon">
							<span class="icon">📝</span>
						</div>
						<div class="menu-content">
							<span class="menu-title">文字推荐算法</span>
							<span class="menu-desc">管理文字内容推荐策略</span>
						</div>
						<div class="menu-arrow">
							<el-icon><ArrowRight /></el-icon>
						</div>
					</div>
					
					<!-- 图片推荐 -->
					<div class="menu-item" @click="navigateTo('/image-recommendation')">
						<div class="menu-icon image-icon">
							<span class="icon">🖼️</span>
						</div>
						<div class="menu-content">
							<span class="menu-title">图片推荐算法</span>
							<span class="menu-desc">管理图片内容推荐策略</span>
						</div>
						<div class="menu-arrow">
							<el-icon><ArrowRight /></el-icon>
						</div>
					</div>
					
					<!-- 小说推荐 -->
					<div class="menu-item" @click="navigateTo('/novel-recommendation')">
						<div class="menu-icon novel-icon">
							<span class="icon">📚</span>
						</div>
						<div class="menu-content">
							<span class="menu-title">小说推荐算法</span>
							<span class="menu-desc">管理小说内容推荐策略</span>
						</div>
						<div class="menu-arrow">
							<el-icon><ArrowRight /></el-icon>
						</div>
					</div>
				</div>
				
				<div class="menu-section">
					<span class="section-title">算法配置</span>
					
					<!-- 权重配置 -->
					<div class="menu-item" @click="navigateTo('/weight-config')">
						<div class="menu-icon config-icon">
							<span class="icon">⚙️</span>
						</div>
						<div class="menu-content">
							<span class="menu-title">权重配置</span>
							<span class="menu-desc">调整推荐算法权重参数</span>
						</div>
						<div class="menu-arrow">
							<el-icon><ArrowRight /></el-icon>
						</div>
					</div>
					
					<!-- 标签管理 -->
					<div class="menu-item" @click="navigateTo('/tag-management')">
						<div class="menu-icon tag-icon">
							<span class="icon">🏷️</span>
						</div>
						<div class="menu-content">
							<span class="menu-title">标签管理</span>
							<span class="menu-desc">管理软硬标签体系</span>
						</div>
						<div class="menu-arrow">
							<el-icon><ArrowRight /></el-icon>
						</div>
					</div>
				</div>
				
				<div class="menu-section">
					<span class="section-title">数据监控</span>
					
					<!-- 推荐统计 -->
					<div class="menu-item" @click="navigateTo('/recommendation-stats')">
						<div class="menu-icon stats-icon">
							<span class="icon">📊</span>
						</div>
						<div class="menu-content">
							<span class="menu-title">推荐统计</span>
							<span class="menu-desc">查看推荐效果数据</span>
						</div>
						<div class="menu-arrow">
							<el-icon><ArrowRight /></el-icon>
						</div>
					</div>
					
					<!-- 性能监控 -->
					<div class="menu-item" @click="navigateTo('/performance-monitor')">
						<div class="menu-icon monitor-icon">
							<span class="icon">📈</span>
						</div>
						<div class="menu-content">
							<span class="menu-title">性能监控</span>
							<span class="menu-desc">监控算法执行性能</span>
						</div>
						<div class="menu-arrow">
							<el-icon><ArrowRight /></el-icon>
						</div>
					</div>
				</div>
			</div>
			
			<!-- 底部操作 -->
			<div class="sidebar-footer">
				<div class="footer-btn" @click="goToMainPage">
					<el-icon><House /></el-icon>
					<span class="btn-text">返回首页</span>
				</div>
				<div class="footer-btn" @click="logout">
					<el-icon><SwitchButton /></el-icon>
					<span class="btn-text">退出管理</span>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { Close, ArrowRight, House, SwitchButton } from '@element-plus/icons-vue'

interface Props {
  visible: boolean
}

defineProps<Props>()
const emit = defineEmits<{
  close: []
}>()

const router = useRouter()

// 关闭侧边栏
const closeSidebar = () => {
  emit('close')
}

// 页面导航
const navigateTo = (url: string) => {
  closeSidebar()
  setTimeout(() => {
    router.push(url)
  }, 300)
}

// 返回主页
const goToMainPage = () => {
  closeSidebar()
  setTimeout(() => {
    router.push('/')
  }, 300)
}

// 退出管理
const logout = () => {
  ElMessageBox.confirm(
    '确定要退出推荐算法管理后台吗？',
    '确认退出',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(() => {
    closeSidebar()
    setTimeout(() => {
      router.push('/')
    }, 300)
  }).catch(() => {
    // 用户取消操作
  })
}
</script>

<style scoped>
.sidebar {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 9999;
}

.sidebar-mask {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(5px);
}

.sidebar-content {
  position: absolute;
  top: 0;
  right: -400px;
  width: 400px;
  height: 100%;
  background: #ffffff;
  box-shadow: -10px 0 30px rgba(0, 0, 0, 0.1);
  transition: right 0.3s ease;
  display: flex;
  flex-direction: column;
}

.sidebar-show {
  right: 0;
}
	
/* 头部样式 */
.sidebar-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.header-info {
  display: flex;
  align-items: center;
}

.admin-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin-right: 12px;
  background: rgba(255, 255, 255, 0.2);
  padding: 8px;
}

.admin-info {
  flex: 1;
}

.admin-name {
  display: block;
  font-size: 18px;
  color: #ffffff;
  font-weight: 600;
  margin-bottom: 4px;
}

.admin-role {
  display: block;
  font-size: 14px;
  color: rgba(255, 255, 255, 0.8);
}

.close-btn {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background 0.2s ease;
}

.close-btn:hover {
  background: rgba(255, 255, 255, 0.3);
}
	
/* 菜单样式 */
.sidebar-menu {
  flex: 1;
  padding: 20px 0;
  overflow-y: auto;
}

.menu-section {
  margin-bottom: 20px;
}

.section-title {
  display: block;
  font-size: 14px;
  color: #999999;
  font-weight: 600;
  padding: 0 20px 10px;
  margin-bottom: 5px;
  border-bottom: 1px solid #f0f0f0;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 15px 20px;
  transition: all 0.3s ease;
  cursor: pointer;
  border-radius: 8px;
  margin: 0 10px;
}

.menu-item:hover {
  background: #f8f9fa;
  transform: translateX(4px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.menu-icon {
  width: 50px;
  height: 50px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
}
	
.text-icon {
  background: linear-gradient(135deg, #4ECDC4, #44A08D);
}

.image-icon {
  background: linear-gradient(135deg, #FF69B4, #FF1493);
}

.novel-icon {
  background: linear-gradient(135deg, #9370DB, #8A2BE2);
}

.config-icon {
  background: linear-gradient(135deg, #FFD700, #FFA500);
}

.tag-icon {
  background: linear-gradient(135deg, #45B7D1, #96CEB4);
}

.stats-icon {
  background: linear-gradient(135deg, #FF6B6B, #FF8E8E);
}

.monitor-icon {
  background: linear-gradient(135deg, #2ECC71, #27AE60);
}

.icon {
  font-size: 24px;
}

.menu-content {
  flex: 1;
}

.menu-title {
  display: block;
  font-size: 16px;
  color: #333333;
  font-weight: 600;
  margin-bottom: 4px;
}

.menu-desc {
  display: block;
  font-size: 12px;
  color: #666666;
}

.menu-arrow {
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
}
	
/* 底部样式 */
.sidebar-footer {
  padding: 20px;
  border-top: 1px solid #f0f0f0;
  display: flex;
  gap: 10px;
}

.footer-btn {
  flex: 1;
  height: 40px;
  border-radius: 8px;
  background: #f8f9fa;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  transition: background 0.2s ease;
  cursor: pointer;
}

.footer-btn:hover {
  background: #e9ecef;
}

.btn-text {
  font-size: 14px;
  color: #666666;
  font-weight: 500;
}
</style>
