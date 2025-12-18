<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="订单号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="支付渠道" prop="payChannel">
        <el-select v-model="queryParams.payChannel" placeholder="请选择支付渠道" clearable>
          <el-option
              v-for="dict in dict.type.express_order_pay_channel"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="支付状态" prop="orderStatus">
        <el-select v-model="queryParams.orderStatus" placeholder="请选择支付状态" clearable>
          <el-option
            v-for="dict in dict.type.express_order_pay_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="退款状态" prop="refundStatus">
        <el-select v-model="queryParams.refundStatus" placeholder="请选择退款状态" clearable>
          <el-option
            v-for="dict in dict.type.express_order_refund_status"
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
          v-hasPermi="['express:order:add']"
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
          v-hasPermi="['express:order:edit']"
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
          v-hasPermi="['express:order:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['express:order:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['express:order:import']"
        >导入</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="订单ID" align="center" prop="orderId" />-->
      <el-table-column label="订单号" align="center" prop="orderNo" />
      <el-table-column label="支付方式" align="center" prop="payType" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.express_order_pay_type" :value="scope.row.payType"/>
        </template>
      </el-table-column>
      <el-table-column label="支付渠道" align="center" prop="payChannel" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.express_order_pay_channel" :value="scope.row.payChannel"/>
        </template>
      </el-table-column>
      <el-table-column label="支付状态" align="center" prop="orderStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.express_order_pay_status" :value="scope.row.orderStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="退款状态" align="center" prop="refundStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.express_order_refund_status" :value="scope.row.refundStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="商品信息" align="left" prop="goodsInfo" width="200">
        <template slot-scope="scope">
          <div style="word-break: break-all; white-space: normal;">{{ scope.row.goodsInfo }}</div>
        </template>
      </el-table-column>
      <el-table-column label="收货人姓名" align="center" prop="consignee" width="100"/>
      <el-table-column label="收货电话" align="center" prop="consigneePhone" />
      <el-table-column label="收货地址" align="left" prop="consigneeAddress" width="300">
        <template slot-scope="scope">
          <div style="word-break: break-all; white-space: normal;">{{ scope.row.consigneeAddress }}</div>
        </template>
      </el-table-column>
      <el-table-column label="用户备注" align="center" prop="userRemark" />
<!--      <el-table-column label="商户备注" align="center" prop="merchantRemark" />-->
      <el-table-column label="实际支付金额" align="center" prop="orderAmount" width="100"/>
      <el-table-column label="支付时间" align="center" prop="payTime" width="80">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.payTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="退款时间" align="center" prop="refundTime" width="80">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.refundTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['express:order:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['express:order:remove']"
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

    <!-- 添加或修改订单管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="订单号" prop="orderNo">
          <el-input v-model="form.orderNo" placeholder="请输入订单号" />
        </el-form-item>
        <el-form-item label="支付渠道" prop="payChannel">
          <el-input v-model="form.payChannel" placeholder="请输入支付渠道" />
        </el-form-item>
        <el-form-item label="支付状态" prop="orderStatus">
          <el-radio-group v-model="form.orderStatus">
            <el-radio
              v-for="dict in dict.type.express_order_pay_status"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="退款状态" prop="refundStatus">
          <el-radio-group v-model="form.refundStatus">
            <el-radio
              v-for="dict in dict.type.express_order_refund_status"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="商品信息" prop="goodsInfo">
          <el-input v-model="form.goodsInfo" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="收货人姓名" prop="consignee">
          <el-input v-model="form.consignee" placeholder="请输入收货人姓名" />
        </el-form-item>
        <el-form-item label="收货电话" prop="consigneePhone">
          <el-input v-model="form.consigneePhone" placeholder="请输入收货电话" />
        </el-form-item>
        <el-form-item label="收货地址" prop="consigneeAddress">
          <el-input v-model="form.consigneeAddress" placeholder="请输入收货地址" />
        </el-form-item>
        <el-form-item label="用户备注" prop="userRemark">
          <el-input v-model="form.userRemark" placeholder="请输入用户备注" />
        </el-form-item>
<!--        <el-form-item label="商户备注" prop="merchantRemark">-->
<!--          <el-input v-model="form.merchantRemark" placeholder="请输入商户备注" />-->
<!--        </el-form-item>-->
        <el-form-item label="实际支付金额" prop="orderAmount">
          <el-input v-model="form.orderAmount" placeholder="请输入实际支付金额" />
        </el-form-item>
        <el-form-item label="支付时间" prop="payTime">
          <el-date-picker clearable
            v-model="form.payTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择支付时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="退款时间" prop="refundTime">
          <el-date-picker clearable
            v-model="form.refundTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择退款时间">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 导入订单管理对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <span>仅允许上传xlsx、xls格式文件</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="downloadTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitUpload">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listOrder, getOrder, delOrder, addOrder, updateOrder, importOrder, importTemplate } from "@/api/express/order"
import { getToken } from "@/utils/auth"

export default {
  name: "Order",
  dicts: ['express_order_pay_status','express_order_refund_status','express_order_pay_type','express_order_pay_channel'],
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
      // 订单管理表格数据
      orderList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNo: null,
        payType: null,
        payChannel: null,
        orderStatus: null,
        refundStatus: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        orderNo: [
          { required: true, message: "订单号不能为空", trigger: "blur" }
        ],
        goodsInfo: [
          { required: true, message: "商品信息不能为空", trigger: "blur" }
        ],
        consignee: [
          { required: true, message: "收货人姓名不能为空", trigger: "blur" }
        ],
        consigneePhone: [
          { required: true, message: "收货电话不能为空", trigger: "blur" }
        ],
        consigneeAddress: [
          { required: true, message: "收货地址不能为空", trigger: "blur" }
        ],
      },
      // 导入参数
      upload: {
        // 是否显示弹出层（导入）
        open: false,
        // 弹出层标题（导入）
        title: "导入订单管理",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/express/order/importData"
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询订单管理列表 */
    getList() {
      this.loading = true
      listOrder(this.queryParams).then(response => {
        this.orderList = response.rows
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
        orderId: null,
        orderNo: null,
        payType: null,
        payChannel: null,
        orderStatus: null,
        refundStatus: null,
        goodsInfo: null,
        consignee: null,
        consigneePhone: null,
        consigneeAddress: null,
        userRemark: null,
        merchantRemark: null,
        orderAmount: null,
        payTime: null,
        refundTime: null,
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
      this.ids = selection.map(item => item.orderId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加订单管理"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const orderId = row.orderId || this.ids
      getOrder(orderId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改订单管理"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.orderId != null) {
            updateOrder(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addOrder(this.form).then(response => {
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
      const orderIds = row.orderId || this.ids
      this.$modal.confirm('是否确认删除订单管理编号为"' + orderIds + '"的数据项？').then(function() {
        return delOrder(orderIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('express/order/export', {
        ...this.queryParams
      }, `order_${new Date().getTime()}.xlsx`)
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.open = true
    },
    /** 下载模板操作 */
    downloadTemplate() {
      const url = process.env.VUE_APP_BASE_API + "/express/order/importTemplate"
      this.download(url, {}, "订单导入模板.xlsx")
    },
    /** 提交上传 */
    submitUpload() {
      this.$refs.upload.submit()
    },
    /** 文件上传中处理 */
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true
    },
    /** 文件上传成功处理 */
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false
      this.upload.isUploading = false
      if (response.code === 200) {
        this.$modal.msgSuccess("导入成功")
        this.getList()
      } else {
        this.$modal.msgError(response.msg)
      }
    }
  }
}
</script>
