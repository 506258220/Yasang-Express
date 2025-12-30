<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="虚拟手机号" prop="virtualPhone">
        <el-input
          v-model="queryParams.virtualPhone"
          placeholder="请输入虚拟手机号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="绑定的真实手机号" prop="realPhone">
        <el-input
          v-model="queryParams.realPhone"
          placeholder="请输入绑定的真实手机号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="关联快递单号，可空" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入关联快递单号，可空"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="虚拟号服务商" prop="provider">
        <el-input
          v-model="queryParams.provider"
          placeholder="请输入虚拟号服务商"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="绑定时间" prop="bindTime">
        <el-date-picker clearable
          v-model="queryParams.bindTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择绑定时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="有效期截止时间" prop="expireTime">
        <el-date-picker clearable
          v-model="queryParams.expireTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择有效期截止时间">
        </el-date-picker>
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
          v-hasPermi="['express:number:add']"
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
          v-hasPermi="['express:number:edit']"
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
          v-hasPermi="['express:number:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['express:number:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="numberList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="id" />
      <el-table-column label="虚拟手机号" align="center" prop="virtualPhone" />
      <el-table-column label="绑定的真实手机号" align="center" prop="realPhone" />
      <el-table-column label="关联快递单号，可空" align="center" prop="orderNo" />
      <el-table-column label="虚拟号服务商" align="center" prop="provider" />
      <el-table-column label="绑定时间" align="center" prop="bindTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.bindTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="有效期截止时间" align="center" prop="expireTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.expireTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['express:number:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['express:number:remove']"
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

    <!-- 添加或修改虚拟号管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="虚拟手机号" prop="virtualPhone">
          <el-input v-model="form.virtualPhone" placeholder="请输入虚拟手机号" />
        </el-form-item>
        <el-form-item label="绑定的真实手机号" prop="realPhone">
          <el-input v-model="form.realPhone" placeholder="请输入绑定的真实手机号" />
        </el-form-item>
        <el-form-item label="关联快递单号，可空" prop="orderNo">
          <el-input v-model="form.orderNo" placeholder="请输入关联快递单号，可空" />
        </el-form-item>
        <el-form-item label="虚拟号服务商" prop="provider">
          <el-input v-model="form.provider" placeholder="请输入虚拟号服务商" />
        </el-form-item>
        <el-form-item label="绑定时间" prop="bindTime">
          <el-date-picker clearable
            v-model="form.bindTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择绑定时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="有效期截止时间" prop="expireTime">
          <el-date-picker clearable
            v-model="form.expireTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择有效期截止时间">
          </el-date-picker>
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
import { listNumber, getNumber, delNumber, addNumber, updateNumber } from "@/api/express/number"

export default {
  name: "Number",
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
      // 虚拟号管理表格数据
      numberList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        virtualPhone: null,
        realPhone: null,
        orderNo: null,
        provider: null,
        bindTime: null,
        expireTime: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        virtualPhone: [
          { required: true, message: "虚拟手机号不能为空", trigger: "blur" }
        ],
        realPhone: [
          { required: true, message: "绑定的真实手机号不能为空", trigger: "blur" }
        ],
        provider: [
          { required: true, message: "虚拟号服务商不能为空", trigger: "blur" }
        ],
        expireTime: [
          { required: true, message: "有效期截止时间不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "change" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询虚拟号管理列表 */
    getList() {
      this.loading = true
      listNumber(this.queryParams).then(response => {
        this.numberList = response.rows
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
        virtualPhone: null,
        realPhone: null,
        orderNo: null,
        provider: null,
        bindTime: null,
        expireTime: null,
        status: null,
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
      this.title = "添加虚拟号管理"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getNumber(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改虚拟号管理"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateNumber(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addNumber(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除虚拟号管理编号为"' + ids + '"的数据项？').then(function() {
        return delNumber(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('express/number/export', {
        ...this.queryParams
      }, `number_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
