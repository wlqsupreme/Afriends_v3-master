import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getTableList, getTableData, getAllEntities } from '@/utils/api'

export interface TableInfo {
  id: string
  name: string
  comment: string
  selected: boolean
}

export interface TableData {
  id: number
  friendId: number
  friendName: string
  avatarUrl: string
  status: string
  lastActive: string
  createdAt: string
  updatedAt: string
}

export interface EntityField {
  name: string
  type: string
  fullType: string
  annotations: {
    tableId?: boolean
    idType?: string
    tableField?: boolean
    exist?: boolean
  }
}

export interface EntityInfo {
  className: string
  fullName: string
  packageName: string
  tableName?: string
  fields: EntityField[]
  fieldCount: number
}

export interface ListInfo {
  className: string
  fullName: string
  packageName: string
  genericInfo?: string
}

export const useTableStore = defineStore('table', () => {
  // 状态
  const tableList = ref<TableInfo[]>([])
  const selectedTables = ref<string[]>([])
  const tableData = ref<TableData[]>([])
  const loading = ref(false)

  // 实体类相关状态
  const entityList = ref<EntityInfo[]>([])
  const listClasses = ref<ListInfo[]>([])
  const entityLoading = ref(false)

  // 动作
  const fetchTableList = async () => {
    try {
      loading.value = true
      // 模拟获取表列表
      const mockTables: TableInfo[] = [
        { id: 'friend_list', name: 'friend_list', comment: '好友列表表', selected: false },
        { id: 'user_info', name: 'user_info', comment: '用户信息表', selected: false },
        { id: 'message_log', name: 'message_log', comment: '消息日志表', selected: false }
      ]
      tableList.value = mockTables
    } catch (error) {
      console.error('获取表列表失败:', error)
    } finally {
      loading.value = false
    }
  }

  const toggleTableSelection = (tableId: string) => {
    const table = tableList.value.find(t => t.id === tableId)
    if (table) {
      table.selected = !table.selected
      if (table.selected) {
        selectedTables.value.push(tableId)
      } else {
        const index = selectedTables.value.indexOf(tableId)
        if (index > -1) {
          selectedTables.value.splice(index, 1)
        }
      }
    }
  }

  const fetchTableData = async (tableName: string) => {
    try {
      loading.value = true
      const data = await getTableData(tableName)
      tableData.value = data
    } catch (error) {
      console.error('获取表数据失败:', error)
    } finally {
      loading.value = false
    }
  }

  const clearSelection = () => {
    selectedTables.value = []
    tableList.value.forEach(table => {
      table.selected = false
    })
  }

  // 获取所有实体类信息
  const fetchAllEntities = async () => {
    try {
      entityLoading.value = true
      const response = await getAllEntities()
      entityList.value = response.entityClasses || []
      listClasses.value = response.listClasses || []
    } catch (error) {
      console.error('获取实体类信息失败:', error)
    } finally {
      entityLoading.value = false
    }
  }

  return {
    tableList,
    selectedTables,
    tableData,
    loading,
    entityList,
    listClasses,
    entityLoading,
    fetchTableList,
    toggleTableSelection,
    fetchTableData,
    clearSelection,
    fetchAllEntities
  }
})
