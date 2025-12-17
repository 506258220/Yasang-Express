<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="关联快递单号" prop="waybillNo">
        <el-input
          v-model="queryParams.waybillNo"
          placeholder="请输入关联快递单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="轨迹状态" prop="trackStatus">
        <el-select v-model="queryParams.trackStatus" placeholder="请选择轨迹状态" clearable>
          <el-option
            v-for="dict in dict.type.order_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
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
          v-hasPermi="['express:track:add']"
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
          v-hasPermi="['express:track:edit']"
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
          v-hasPermi="['express:track:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['express:track:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="trackList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="轨迹ID" align="center" prop="trackId" />
      <el-table-column label="关联快递单号" align="center" prop="waybillNo" />
      <el-table-column label="轨迹状态" align="center" prop="trackStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.order_status" :value="scope.row.trackStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="轨迹描述" align="center" prop="trackDesc" />
      <el-table-column label="轨迹地点" align="center" prop="trackLocation" />
      <el-table-column label="轨迹时间" align="center" prop="trackTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.trackTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['express:track:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['express:track:remove']"
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

    <!-- 添加或修改物流轨迹对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="关联快递单号" prop="waybillNo">
          <el-input v-model="form.waybillNo" placeholder="请输入关联快递单号" />
        </el-form-item>
        <el-form-item label="轨迹状态" prop="trackStatus">
          <el-radio-group v-model="form.trackStatus">
            <el-radio
              v-for="dict in dict.type.order_status"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="轨迹描述" prop="trackDesc">
          <el-input v-model="form.trackDesc" placeholder="请输入轨迹描述" />
        </el-form-item>
        <el-form-item label="轨迹地点" prop="trackLocation">
          <el-input v-model="form.trackLocation" placeholder="请输入轨迹地点" />
        </el-form-item>
        <el-form-item label="轨迹时间" prop="trackTime">
          <el-date-picker clearable
            v-model="form.trackTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择轨迹时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input v-model="form.sort" placeholder="请输入排序" />
        </el-form-item>
        <el-form-item label="删除标志" prop="delFlag">
          <el-input v-model="form.delFlag" placeholder="请输入删除标志" />
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
import { listTrack, getTrack, delTrack, addTrack, updateTrack } from "@/api/express/track"

export default {
  name: "Track",
  dicts: ['order_status'],
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
      // 物流轨迹表格数据
      trackList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        waybillNo: null,
        trackStatus: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        waybillNo: [
          { required: true, message: "关联快递单号不能为空", trigger: "blur" }
        ],
        trackStatus: [
          { required: true, message: "轨迹状态不能为空", trigger: "change" }
        ],
        trackDesc: [
          { required: true, message: "轨迹描述不能为空", trigger: "blur" }
        ],
        trackLocation: [
          { required: true, message: "轨迹地点不能为空", trigger: "blur" }
        ],
        trackTime: [
          { required: true, message: "轨迹时间不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询物流轨迹列表 */
    getList() {
      this.loading = true
      listTrack(this.queryParams).then(response => {
        this.trackList = response.rows
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
        trackId: null,
        waybillNo: null,
        trackStatus: null,
        trackDesc: null,
        trackLocation: null,
        trackTime: null,
        sort: null,
        createBy: null,
        createTime: null,
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
      this.ids = selection.map(item => item.trackId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加物流轨迹"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const trackId = row.trackId || this.ids
      getTrack(trackId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改物流轨迹"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.trackId != null) {
            updateTrack(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addTrack(this.form).then(response => {
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
      const trackIds = row.trackId || this.ids
      this.$modal.confirm('是否确认删除物流轨迹编号为"' + trackIds + '"的数据项？').then(function() {
        return delTrack(trackIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('express/track/export', {
        ...this.queryParams
      }, `track_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
