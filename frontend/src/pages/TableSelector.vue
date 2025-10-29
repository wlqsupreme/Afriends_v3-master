<template>
  <div class="table-selector">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>数据库表选择器</span>
          <div class="header-actions">
            <el-button 
              type="primary" 
              @click="loadTableData"
              :loading="loading"
              :disabled="selectedTables.length === 0"
            >
              加载选中表数据
            </el-button>
            <el-button @click="clearSelection">清空选择</el-button>
            <el-button @click="refreshTableList">刷新列表</el-button>
            <el-button type="success" @click="loadEntityData" :loading="entityLoading">
              加载实体类
            </el-button>
          </div>
        </div>
      </template>

      <!-- 实体类列表 -->
      <div v-if="entityList.length > 0" class="entity-section">
        <h3>实体类列表 ({{ entityList.length }})</h3>
        <el-checkbox-group v-model="selectedEntities" @change="handleEntitySelectionChange">
          <el-row :gutter="20">
            <el-col 
              v-for="entity in entityList" 
              :key="entity.className" 
              :span="8"
              class="table-item-col"
            >
              <el-card 
                class="table-item entity-item"
                :class="{ 'selected': entity.selected }"
                @click="toggleEntitySelection(entity.className)"
              >
                <div class="table-content">
                  <el-checkbox 
                    :model-value="entity.selected"
                    @change="toggleEntitySelection(entity.className)"
                    @click.stop
                  >
                    {{ entity.className }}
                  </el-checkbox>
                  <p class="table-comment">
                    {{ entity.tableName || '无表名' }} ({{ entity.fieldCount }} 字段)
                  </p>
                  <div class="table-actions">
                    <el-button 
                      size="small" 
                      type="primary" 
                      @click.stop="previewEntity(entity)"
                    >
                      预览
                    </el-button>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </el-checkbox-group>
      </div>

      <!-- 传统表列表 -->
      <div class="table-list">
        <h3>传统表列表 ({{ tableList.length }})</h3>
        <el-checkbox-group v-model="selectedTables" @change="handleSelectionChange">
          <el-row :gutter="20">
            <el-col 
              v-for="table in tableList" 
              :key="table.id" 
              :span="8"
              class="table-item-col"
            >
              <el-card 
                class="table-item"
                :class="{ 'selected': table.selected }"
                @click="toggleTableSelection(table.id)"
              >
                <div class="table-content">
                  <el-checkbox 
                    :model-value="table.selected"
                    @change="toggleTableSelection(table.id)"
                    @click.stop
                  >
                    {{ table.name }}
                  </el-checkbox>
                  <p class="table-comment">{{ table.comment }}</p>
                  <div class="table-actions">
                    <el-button 
                      size="small" 
                      type="primary" 
                      @click.stop="previewTable(table.id)"
                    >
                      预览
                    </el-button>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </el-checkbox-group>
      </div>

      <div v-if="selectedTables.length > 0 || selectedEntities.length > 0" class="selection-info">
        <el-alert
          :title="`已选择 ${selectedTables.length + selectedEntities.length} 个项目`"
          type="info"
          :closable="false"
          show-icon
        >
          <template #default>
            <div class="selected-tables">
              <div v-if="selectedEntities.length > 0">
                <strong>实体类:</strong>
                <el-tag 
                  v-for="entityName in selectedEntities" 
                  :key="entityName"
                  closable
                  @close="removeEntity(entityName)"
                  class="table-tag"
                  type="success"
                >
                  {{ entityName }}
                </el-tag>
              </div>
              <div v-if="selectedTables.length > 0">
                <strong>传统表:</strong>
                <el-tag 
                  v-for="tableId in selectedTables" 
                  :key="tableId"
                  closable
                  @close="removeTable(tableId)"
                  class="table-tag"
                >
                  {{ getTableName(tableId) }}
                </el-tag>
              </div>
            </div>
          </template>
        </el-alert>
      </div>
    </el-card>

    <!-- 数据预览对话框 -->
    <el-dialog
      v-model="previewVisible"
      :title="`表数据预览 - ${previewTableName}`"
      width="80%"
      :before-close="handlePreviewClose"
    >
      <el-table 
        :data="tableData" 
        v-loading="loading"
        stripe
        border
        height="400"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="friendId" label="好友ID" width="100" />
        <el-table-column prop="friendName" label="好友名称" width="150" />
        <el-table-column prop="avatarUrl" label="头像URL" width="200" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastActive" label="最后活跃" width="180">
          <template #default="{ row }">
            {{ formatDate(row.lastActive) }}
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column prop="updatedAt" label="更新时间" width="180">
          <template #default="{ row }">
            {{ formatDate(row.updatedAt) }}
          </template>
        </el-table-column>
      </el-table>
      
      <template #footer>
        <el-button @click="previewVisible = false">关闭</el-button>
        <el-button type="primary" @click="exportData">导出数据</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { useTableStore } from '@/store/table'
import { formatDate } from '@/utils'

const tableStore = useTableStore()

// 响应式数据
const previewVisible = ref(false)
const previewTableName = ref('')
const selectedEntities = ref<string[]>([])

// 计算属性
const tableList = computed(() => tableStore.tableList)
const selectedTables = computed(() => tableStore.selectedTables)
const tableData = computed(() => tableStore.tableData)
const loading = computed(() => tableStore.loading)
const entityList = computed(() => tableStore.entityList)
const entityLoading = computed(() => tableStore.entityLoading)

// 方法
const loadTableList = async () => {
  await tableStore.fetchTableList()
}

const loadEntityData = async () => {
  try {
    await tableStore.fetchAllEntities()
    ElMessage.success('实体类信息加载成功')
  } catch (error) {
    ElMessage.error('加载实体类信息失败')
  }
}

const toggleTableSelection = (tableId: string) => {
  tableStore.toggleTableSelection(tableId)
}

const toggleEntitySelection = (entityName: string) => {
  const index = selectedEntities.value.indexOf(entityName)
  if (index > -1) {
    selectedEntities.value.splice(index, 1)
  } else {
    selectedEntities.value.push(entityName)
  }
}

const handleSelectionChange = (value: string[]) => {
  // 同步更新store中的选中状态
  tableList.value.forEach(table => {
    table.selected = value.includes(table.id)
  })
}

const handleEntitySelectionChange = (value: string[]) => {
  selectedEntities.value = value
}

const clearSelection = () => {
  tableStore.clearSelection()
  selectedEntities.value = []
}

const refreshTableList = () => {
  loadTableList()
  ElMessage.success('列表已刷新')
}

const getTableName = (tableId: string) => {
  const table = tableList.value.find(t => t.id === tableId)
  return table ? table.name : tableId
}

const removeTable = (tableId: string) => {
  toggleTableSelection(tableId)
}

const removeEntity = (entityName: string) => {
  toggleEntitySelection(entityName)
}

const previewTable = async (tableId: string) => {
  try {
    previewTableName.value = getTableName(tableId)
    await tableStore.fetchTableData(tableId)
    previewVisible.value = true
  } catch (error) {
    ElMessage.error('预览数据失败')
  }
}

const previewEntity = (entity: any) => {
  ElMessage.info(`预览实体类: ${entity.className} (${entity.fieldCount} 字段)`)
  // 这里可以打开实体类详情对话框
}

const loadTableData = async () => {
  if (selectedTables.value.length === 0 && selectedEntities.value.length === 0) {
    ElMessage.warning('请先选择要加载的表或实体类')
    return
  }
  
  try {
    const totalCount = selectedTables.value.length + selectedEntities.value.length
    ElMessage.success(`开始加载 ${totalCount} 个项目的数据`)
    // 实际实现中，这里会调用相应的API
  } catch (error) {
    ElMessage.error('加载数据失败')
  }
}

const getStatusType = (status: string) => {
  switch (status) {
    case 'ONLINE':
      return 'success'
    case 'OFFLINE':
      return 'info'
    case 'BUSY':
      return 'warning'
    default:
      return 'info'
  }
}

const handlePreviewClose = () => {
  previewVisible.value = false
}

const exportData = () => {
  ElMessage.info('导出功能开发中...')
}

// 生命周期
onMounted(() => {
  loadTableList()
})
</script>

<style scoped>
.table-selector {
  max-width: 1200px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
  font-size: 18px;
}

.header-actions {
  display: flex;
  gap: 10px;
}

.table-list {
  margin-bottom: 20px;
}

.table-item-col {
  margin-bottom: 20px;
}

.table-item {
  cursor: pointer;
  transition: all 0.3s;
  height: 120px;
}

.table-item:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.table-item.selected {
  border-color: #409eff;
  background-color: #f0f9ff;
}

.table-content {
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.table-comment {
  margin: 8px 0;
  color: #909399;
  font-size: 14px;
  flex: 1;
}

.table-actions {
  display: flex;
  justify-content: flex-end;
}

.selection-info {
  margin-top: 20px;
}

.selected-tables {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 10px;
}

.table-tag {
  margin-right: 8px;
  margin-bottom: 8px;
}

.entity-section {
  margin-bottom: 30px;
}

.entity-section h3 {
  margin: 0 0 20px 0;
  color: #303133;
  font-size: 18px;
  font-weight: 600;
  border-bottom: 2px solid #409eff;
  padding-bottom: 10px;
}

.table-list h3 {
  margin: 0 0 20px 0;
  color: #303133;
  font-size: 18px;
  font-weight: 600;
  border-bottom: 2px solid #67c23a;
  padding-bottom: 10px;
}

.entity-item {
  border-left: 4px solid #409eff;
}

.entity-item.selected {
  border-left-color: #67c23a;
  background-color: #f0f9ff;
}

.selected-tables > div {
  margin-bottom: 10px;
}

.selected-tables strong {
  margin-right: 10px;
  color: #303133;
}
</style>
