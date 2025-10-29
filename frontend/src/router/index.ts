import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/pages/Home.vue'),
    meta: {
      title: '首页'
    }
  },
  {
    path: '/table-selector',
    name: 'TableSelector',
    component: () => import('@/pages/TableSelector.vue'),
    meta: {
      title: '表选择器'
    }
  },
  {
    path: '/recommendation-admin',
    name: 'RecommendationAdmin',
    component: () => import('@/pages/RecommendationAdmin.vue'),
    meta: {
      title: '推荐算法管理'
    }
  },
  {
    path: '/text-recommendation',
    name: 'TextRecommendation',
    component: () => import('@/pages/TextRecommendation.vue'),
    meta: {
      title: '文字推荐算法'
    }
  },
  {
    path: '/image-recommendation',
    name: 'ImageRecommendation',
    component: () => import('@/pages/ImageRecommendation.vue'),
    meta: {
      title: '图片推荐算法'
    }
  },
  {
    path: '/novel-recommendation',
    name: 'NovelRecommendation',
    component: () => import('@/pages/NovelRecommendation.vue'),
    meta: {
      title: '小说推荐算法'
    }
  },
  {
    path: '/weight-config',
    name: 'WeightConfig',
    component: () => import('@/pages/WeightConfig.vue'),
    meta: {
      title: '权重配置'
    }
  },
  {
    path: '/tag-management',
    name: 'TagManagement',
    component: () => import('@/pages/TagManagement.vue'),
    meta: {
      title: '标签管理'
    }
  },
  {
    path: '/recommendation-stats',
    name: 'RecommendationStats',
    component: () => import('@/pages/RecommendationStats.vue'),
    meta: {
      title: '推荐统计'
    }
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/pages/NotFound.vue'),
    meta: {
      title: '页面未找到'
    }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  if (to.meta?.title) {
    document.title = `${to.meta.title} - Afriends 管理系统`
  }
  next()
})

export default router
