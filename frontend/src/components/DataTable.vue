<template>
  <div class="data-table">
    <div class="table-header" v-if="showHeader">
      <div class="header-left">
        <slot name="header-left">
          <h3>{{ title }}</h3>
        </slot>
      </div>
      <div class="header-right">
        <slot name="header-right">
          <el-input
            v-model="searchText"
            placeholder="搜索..."
            style="width: 200px"
            @input="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </slot>
      </div>
    </div>

    <el-table
      :data="filteredData"
      v-loading="loading"
      stripe
      border
      :height="height"
      @selection-change="handleSelectionChange"
    >
      <el-table-column
        v-if="selectable"
        type="selection"
        width="55"
      />
      
      <el-table-column
        v-for="column in columns"
        :key="column.prop"
        :prop="column.prop"
        :label="column.label"
        :width="column.width"
        :min-width="column.minWidth"
        :show-overflow-tooltip="column.showOverflowTooltip"
      >
        <template #default="{ row, $index }">
          <slot
            :name="column.prop"
            :row="row"
            :index="$index"
            :value="row[column.prop]"
          >
            {{ row[column.prop] }}
          </slot>
        </template>
      </el-table-column>

      <el-table-column
        v-if="showActions"
        label="操作"
        :width="actionWidth"
        fixed="right"
      >
        <template #default="{ row, $index }">
          <slot name="actions" :row="row" :index="$index">
            <el-button size="small" @click="handleEdit(row, $index)">
              编辑
            </el-button>
            <el-button 
              size="small" 
              type="danger" 
              @click="handleDelete(row, $index)"
            >
              删除
            </el-button>
          </slot>
        </template>
      </el-table-column>
    </el-table>

    <div class="table-footer" v-if="showPagination">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="pageSizes"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { debounce } from '@/utils'

interface Column {
  prop: string
  label: string
  width?: number
  minWidth?: number
  showOverflowTooltip?: boolean
}

interface Props {
  data: any[]
  columns: Column[]
  loading?: boolean
  selectable?: boolean
  showActions?: boolean
  showHeader?: boolean
  showPagination?: boolean
  title?: string
  height?: number
  actionWidth?: number
  pageSizes?: number[]
}

const props = withDefaults(defineProps<Props>(), {
  loading: false,
  selectable: false,
  showActions: true,
  showHeader: true,
  showPagination: true,
  title: '数据表格',
  height: 400,
  actionWidth: 150,
  pageSizes: () => [10, 20, 50, 100]
})

const emit = defineEmits<{
  search: [value: string]
  selectionChange: [selection: any[]]
  edit: [row: any, index: number]
  delete: [row: any, index: number]
  pageChange: [page: number, size: number]
}>()

// 响应式数据
const searchText = ref('')
const currentPage = ref(1)
const pageSize = ref(20)
const total = ref(0)

// 计算属性
const filteredData = computed(() => {
  let result = props.data
  
  if (searchText.value) {
    result = result.filter(row => {
      return Object.values(row).some(value => 
        String(value).toLowerCase().includes(searchText.value.toLowerCase())
      )
    })
  }
  
  total.value = result.length
  
  if (props.showPagination) {
    const start = (currentPage.value - 1) * pageSize.value
    const end = start + pageSize.value
    result = result.slice(start, end)
  }
  
  return result
})

// 方法
const handleSearch = debounce((value: string) => {
  emit('search', value)
}, 300)

const handleSelectionChange = (selection: any[]) => {
  emit('selectionChange', selection)
}

const handleEdit = (row: any, index: number) => {
  emit('edit', row, index)
}

const handleDelete = (row: any, index: number) => {
  emit('delete', row, index)
}

const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  emit('pageChange', currentPage.value, pageSize.value)
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
  emit('pageChange', currentPage.value, pageSize.value)
}

// 监听数据变化
watch(() => props.data, () => {
  currentPage.value = 1
}, { deep: true })
</script>

<style scoped>
.data-table {
  background: white;
  border-radius: 4px;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #ebeef5;
}

.header-left h3 {
  margin: 0;
  color: #303133;
}

.table-footer {
  padding: 16px;
  text-align: right;
  border-top: 1px solid #ebeef5;
}
</style>
