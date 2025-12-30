<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="司机ID" prop="driverId">
        <el-input
          v-model="queryParams.driverId"
          placeholder="请输入司机ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="统计日期" prop="statisticDate">
        <el-date-picker clearable
          v-model="queryParams.statisticDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择统计日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="揽收订单数" prop="receiveCount">
        <el-input
          v-model="queryParams.receiveCount"
          placeholder="请输入揽收订单数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="派送订单数" prop="deliverCount">
        <el-input
          v-model="queryParams.deliverCount"
          placeholder="请输入派送订单数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="异常订单数" prop="exceptionCount">
        <el-input
          v-model="queryParams.exceptionCount"
          placeholder="请输入异常订单数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="业绩提成金额" prop="performanceAmount">
        <el-input
          v-model="queryParams.performanceAmount"
          placeholder="请输入业绩提成金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['express:performance:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['express:performance:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['express:performance:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['express:performance:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="performanceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="id" />
      <el-table-column label="司机ID" align="center" prop="driverId" />
      <el-table-column label="统计日期" align="center" prop="statisticDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.statisticDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="揽收订单数" align="center" prop="receiveCount" />
      <el-table-column label="派送订单数" align="center" prop="deliverCount" />
      <el-table-column label="异常订单数" align="center" prop="exceptionCount" />
      <el-table-column label="业绩提成金额" align="center" prop="performanceAmount" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['express:performance:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['express:performance:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改司机业绩统计对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="司机ID" prop="driverId">
          <el-input v-model="form.driverId" placeholder="请输入司机ID" />
        </el-form-item>
        <el-form-item label="统计日期" prop="statisticDate">
          <el-date-picker clearable
            v-model="form.statisticDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择统计日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="揽收订单数" prop="receiveCount">
          <el-input v-model="form.receiveCount" placeholder="请输入揽收订单数" />
        </el-form-item>
        <el-form-item label="派送订单数" prop="deliverCount">
          <el-input v-model="form.deliverCount" placeholder="请输入派送订单数" />
        </el-form-item>
        <el-form-item label="异常订单数" prop="exceptionCount">
          <el-input v-model="form.exceptionCount" placeholder="请输入异常订单数" />
        </el-form-item>
        <el-form-item label="业绩提成金额" prop="performanceAmount">
          <el-input v-model="form.performanceAmount" placeholder="请输入业绩提成金额" />
        </el-form-item>
        <el-form-item label="逻辑删除" prop="delFlag">
          <el-input v-model="form.delFlag" placeholder="请输入逻辑删除" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listPerformance, getPerformance, delPerformance, addPerformance, updatePerformance } from "@/api/express/performance"

export default {
  name: "Performance",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 司机业绩统计表格数据
      performanceList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        driverId: null,
        statisticDate: null,
        receiveCount: null,
        deliverCount: null,
        exceptionCount: null,
        performanceAmount: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        driverId: [
          { required: true, message: "司机ID不能为空", trigger: "blur" }
        ],
        statisticDate: [
          { required: true, message: "统计日期不能为空", trigger: "blur" }
        ],
        receiveCount: [
          { required: true, message: "揽收订单数不能为空", trigger: "blur" }
        ],
        deliverCount: [
          { required: true, message: "派送订单数不能为空", trigger: "blur" }
        ],
        exceptionCount: [
          { required: true, message: "异常订单数不能为空", trigger: "blur" }
        ],
        performanceAmount: [
          { required: true, message: "业绩提成金额不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询司机业绩统计列表 */
    getList() {
      this.loading = true
      listPerformance(this.queryParams).then(response => {
        this.performanceList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        driverId: null,
        statisticDate: null,
        receiveCount: null,
        deliverCount: null,
        exceptionCount: null,
        performanceAmount: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        delFlag: null
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加司机业绩统计"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getPerformance(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改司机业绩统计"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatePerformance(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addPerformance(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除司机业绩统计编号为"' + ids + '"的数据项？').then(function() {
        return delPerformance(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('express/performance/export', {
        ...this.queryParams
      }, `performance_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
