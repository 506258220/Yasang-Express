<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="车牌号" prop="vehicleNo">
        <el-input
          v-model="queryParams.vehicleNo"
          placeholder="请输入车牌号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="载重" prop="loadWeight">
        <el-input
          v-model="queryParams.loadWeight"
          placeholder="请输入载重"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="容积" prop="loadVolume">
        <el-input
          v-model="queryParams.loadVolume"
          placeholder="请输入容积"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="GPS设备编号" prop="gpsDeviceNo">
        <el-input
          v-model="queryParams.gpsDeviceNo"
          placeholder="请输入GPS设备编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="绑定司机ID，关联sys_user" prop="driverId">
        <el-input
          v-model="queryParams.driverId"
          placeholder="请输入绑定司机ID，关联sys_user"
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
          v-hasPermi="['express:vehicle:add']"
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
          v-hasPermi="['express:vehicle:edit']"
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
          v-hasPermi="['express:vehicle:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['express:vehicle:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="vehicleList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="id" />
      <el-table-column label="车牌号" align="center" prop="vehicleNo" />
      <el-table-column label="车型" align="center" prop="vehicleType" />
      <el-table-column label="载重" align="center" prop="loadWeight" />
      <el-table-column label="容积" align="center" prop="loadVolume" />
      <el-table-column label="GPS设备编号" align="center" prop="gpsDeviceNo" />
      <el-table-column label="绑定司机ID，关联sys_user" align="center" prop="driverId" />
      <el-table-column label="状态" align="center" prop="status" />
      <el-table-column label="车辆备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['express:vehicle:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['express:vehicle:remove']"
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

    <!-- 添加或修改车辆信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="车牌号" prop="vehicleNo">
          <el-input v-model="form.vehicleNo" placeholder="请输入车牌号" />
        </el-form-item>
        <el-form-item label="载重" prop="loadWeight">
          <el-input v-model="form.loadWeight" placeholder="请输入载重" />
        </el-form-item>
        <el-form-item label="容积" prop="loadVolume">
          <el-input v-model="form.loadVolume" placeholder="请输入容积" />
        </el-form-item>
        <el-form-item label="GPS设备编号" prop="gpsDeviceNo">
          <el-input v-model="form.gpsDeviceNo" placeholder="请输入GPS设备编号" />
        </el-form-item>
        <el-form-item label="绑定司机ID，关联sys_user" prop="driverId">
          <el-input v-model="form.driverId" placeholder="请输入绑定司机ID，关联sys_user" />
        </el-form-item>
        <el-form-item label="车辆备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
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
import { listVehicle, getVehicle, delVehicle, addVehicle, updateVehicle } from "@/api/express/vehicle"

export default {
  name: "Vehicle",
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
      // 车辆信息表格数据
      vehicleList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        vehicleNo: null,
        vehicleType: null,
        loadWeight: null,
        loadVolume: null,
        gpsDeviceNo: null,
        driverId: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        vehicleNo: [
          { required: true, message: "车牌号不能为空", trigger: "blur" }
        ],
        vehicleType: [
          { required: true, message: "车型不能为空", trigger: "change" }
        ],
        loadWeight: [
          { required: true, message: "载重不能为空", trigger: "blur" }
        ],
        loadVolume: [
          { required: true, message: "容积不能为空", trigger: "blur" }
        ],
        gpsDeviceNo: [
          { required: true, message: "GPS设备编号不能为空", trigger: "blur" }
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
    /** 查询车辆信息列表 */
    getList() {
      this.loading = true
      listVehicle(this.queryParams).then(response => {
        this.vehicleList = response.rows
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
        vehicleNo: null,
        vehicleType: null,
        loadWeight: null,
        loadVolume: null,
        gpsDeviceNo: null,
        driverId: null,
        status: null,
        remark: null,
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
      this.title = "添加车辆信息"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getVehicle(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改车辆信息"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateVehicle(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addVehicle(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除车辆信息编号为"' + ids + '"的数据项？').then(function() {
        return delVehicle(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('express/vehicle/export', {
        ...this.queryParams
      }, `vehicle_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
