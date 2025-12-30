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
      <el-form-item label="商户名称" prop="merchantName">
        <el-input
          v-model="queryParams.merchantName"
          placeholder="请输入商户名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户昵称" prop="userNickname">
        <el-input
          v-model="queryParams.userNickname"
          placeholder="请输入用户昵称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="订单状态" prop="orderStatus">
        <el-select v-model="queryParams.orderStatus" placeholder="请选择订单状态" clearable>
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
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['express:order:import']"
        >导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-document"
          size="mini"
          :disabled="multiple"
          @click="handleBatchGenerate"
          v-hasPermi="['express:third:order:generate']"
        >批量生成快递订单</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="订单ID" align="center" prop="orderId" />
      <el-table-column label="订单类型" align="center" prop="orderType" />
      <el-table-column label="订单号" align="center" prop="orderNo" />
      <el-table-column label="商户名称" align="center" prop="merchantName" />
      <el-table-column label="用户昵称" align="center" prop="userNickname" />
      <el-table-column label="实际支付金额" align="center" prop="actualPayAmount" />
      <el-table-column label="支付状态" align="center" prop="payStatus" />
      <el-table-column label="支付方式" align="center" prop="payType" />
      <el-table-column label="支付渠道" align="center" prop="payChannel" />
      <el-table-column label="订单状态" align="center" prop="orderStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.order_status" :value="scope.row.orderStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="退款状态" align="center" prop="refundStatus" />
      <el-table-column label="订单创建时间" align="center" prop="orderCreateTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.orderCreateTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="商品信息" align="center" prop="productInfo" />
      <el-table-column label="收货人" align="center" prop="receiverName" />
      <el-table-column label="收货电话" align="center" prop="receiverPhone" />
      <el-table-column label="收货地址" align="center" prop="receiverAddress" />
      <el-table-column label="用户备注" align="center" prop="userRemark" />
      <el-table-column label="商户备注" align="center" prop="merchantRemark" />
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

    <!-- 批量生成快递订单配置对话框 -->
    <el-dialog
      title="批量生成快递订单配置"
      :visible.sync="generateDialogVisible"
      width="700px"
      append-to-body
    >
      <el-form ref="generateForm" :model="generateForm" :rules="generateRules" label-width="140px">
        <el-alert
          :title="`已选择 ${ids.length} 个第三方订单，将批量生成快递订单`"
          type="info"
          :closable="false"
          show-icon
          style="margin-bottom: 20px;"
        />

        <el-divider content-position="left">寄件人信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="寄件人姓名" prop="senderName">
              <el-input v-model="generateForm.senderName" placeholder="请输入寄件人姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="寄件人手机号" prop="senderPhone">
              <el-input v-model="generateForm.senderPhone" placeholder="请输入寄件人手机号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="寄件人地址" prop="senderAddress">
          <el-input v-model="generateForm.senderAddress" type="textarea" :rows="2" placeholder="请输入寄件人详细地址" />
        </el-form-item>

        <el-divider content-position="left">订单默认信息</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="快递类型" prop="expressType">
              <el-select v-model="generateForm.expressType" placeholder="请选择快递类型" style="width: 100%;">
                <el-option label="普通件" value="普通件" />
                <el-option label="生鲜件" value="生鲜件" />
                <el-option label="大件" value="大件" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="快递单号前缀" prop="orderNoPrefix">
              <el-input v-model="generateForm.orderNoPrefix" placeholder="如：YS" maxlength="4" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="默认重量(kg)" prop="defaultWeight">
              <el-input-number v-model="generateForm.defaultWeight" :min="0.1" :precision="2" :step="0.1" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="默认运费(元)" prop="defaultFreight">
              <el-input-number v-model="generateForm.defaultFreight" :min="0" :precision="2" :step="1" style="width: 100%;" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="generateDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmBatchGenerate" :loading="generating">开始生成</el-button>
      </div>
    </el-dialog>

    <!-- 生成结果对话框 -->
    <el-dialog
      title="生成结果"
      :visible.sync="resultDialogVisible"
      width="800px"
      append-to-body
    >
      <el-alert
        :title="`成功生成 ${generateResult.successCount} 个，失败 ${generateResult.failCount} 个`"
        :type="generateResult.failCount > 0 ? 'warning' : 'success'"
        :closable="false"
        show-icon
        style="margin-bottom: 20px;"
      />

      <el-tabs v-model="resultActiveTab">
        <el-tab-pane label="成功列表" name="success">
          <el-table :data="generateResult.successList" border max-height="400">
            <el-table-column label="第三方订单号" prop="thirdOrderNo" width="180" />
            <el-table-column label="快递单号" prop="expressOrderNo" width="180" />
            <el-table-column label="收件人" prop="receiverName" />
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="失败列表" name="fail" v-if="generateResult.failCount > 0">
          <el-table :data="generateResult.failList" border max-height="400">
            <el-table-column label="第三方订单号" prop="thirdOrderNo" width="180" />
            <el-table-column label="失败原因" prop="reason" />
          </el-table>
        </el-tab-pane>
      </el-tabs>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="resultDialogVisible = false">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 导入对话框 -->
    <el-dialog
      title="导入第三方商品订单"
      :visible.sync="importDialogVisible"
      width="400px"
      append-to-body
    >
      <el-form :model="form" label-width="80px">
        <el-form-item label="更新支持">
          <el-switch
            v-model="updateSupport"
            active-text="是"
            inactive-text="否"
          ></el-switch>
          <div class="el-form-item__help">如果开启，已存在的订单会被更新</div>
        </el-form-item>
      </el-form>
      <div class="import-container">
        <el-button type="primary" plain @click="downloadTemplate" style="margin-bottom: 10px;">
          <i class="el-icon-download"></i> 下载导入模板
        </el-button>
        <FileUpload
          ref="fileUpload"
          :action="'/express/third/order/importData'"
          :file-type="['xlsx', 'xls']"
          :file-size="10"
          :limit="1"
          :data="{ updateSupport: updateSupport }"
          :disabled="false"
          :show-file-list="true"
          :auto-upload="false"
          :file-list="fileList"
          @input="handleFileChange"
          @import-success="handleImportSuccess"
        />
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="importDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSubmitImport" :disabled="fileList.length === 0">开始导入</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改第三方商品订单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="订单号" prop="orderNo">
          <el-input v-model="form.orderNo" placeholder="请输入订单号" />
        </el-form-item>
        <el-form-item label="商户名称" prop="merchantName">
          <el-input v-model="form.merchantName" placeholder="请输入商户名称" />
        </el-form-item>
        <el-form-item label="用户昵称" prop="userNickname">
          <el-input v-model="form.userNickname" placeholder="请输入用户昵称" />
        </el-form-item>
        <el-form-item label="实际支付金额" prop="actualPayAmount">
          <el-input v-model="form.actualPayAmount" placeholder="请输入实际支付金额" />
        </el-form-item>
        <el-form-item label="支付渠道" prop="payChannel">
          <el-input v-model="form.payChannel" placeholder="请输入支付渠道" />
        </el-form-item>
        <el-form-item label="订单状态" prop="orderStatus">
          <el-radio-group v-model="form.orderStatus">
            <el-radio
              v-for="dict in dict.type.order_status"
              :key="dict.value"
              :label="parseInt(dict.value)"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="订单创建时间" prop="orderCreateTime">
          <el-date-picker clearable
                          v-model="form.orderCreateTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择订单创建时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="商品信息" prop="productInfo">
          <el-input v-model="form.productInfo" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="收货人" prop="receiverName">
          <el-input v-model="form.receiverName" placeholder="请输入收货人姓名" />
        </el-form-item>
        <el-form-item label="收货电话" prop="receiverPhone">
          <el-input v-model="form.receiverPhone" placeholder="请输入收货电话" />
        </el-form-item>
        <el-form-item label="收货地址" prop="receiverAddress">
          <el-input v-model="form.receiverAddress" placeholder="请输入收货详细地址" />
        </el-form-item>
        <el-form-item label="用户备注" prop="userRemark">
          <el-input v-model="form.userRemark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="商户备注" prop="merchantRemark">
          <el-input v-model="form.merchantRemark" type="textarea" placeholder="请输入内容" />
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
import { listOrder, getOrder, delOrder, addOrder, updateOrder, batchGenerateExpressOrders, importOrder, importTemplate } from "@/api/express/thirdOrder"
import FileUpload from "@/components/FileUpload/index.vue"

export default {
  name: "Order",
  components: {
    FileUpload
  },
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
      // 第三方商品订单表格数据
      orderList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 导入相关
      importDialogVisible: false,
      updateSupport: false,
      // 导入文件列表
      fileList: [],
      // 批量生成相关
      generateDialogVisible: false,
      generating: false,
      resultDialogVisible: false,
      resultActiveTab: "success",
      generateForm: {
        senderName: "",
        senderPhone: "",
        senderAddress: "",
        customerId: null,
        expressType: "普通件",
        orderNoPrefix: "YS",
        defaultWeight: 1.0,
        defaultFreight: 0.0,
        warehouseId: null,
        waybillTemplateId: null
      },
      generateRules: {
        senderName: [
          { required: true, message: "寄件人姓名不能为空", trigger: "blur" }
        ],
        senderPhone: [
          { required: true, message: "寄件人手机号不能为空", trigger: "blur" },
          { pattern: /^1[3-9]\d{9}$/, message: "请输入正确的手机号", trigger: "blur" }
        ],
        senderAddress: [
          { required: true, message: "寄件人地址不能为空", trigger: "blur" }
        ],
        expressType: [
          { required: true, message: "快递类型不能为空", trigger: "change" }
        ],
        orderNoPrefix: [
          { required: true, message: "快递单号前缀不能为空", trigger: "blur" },
          { pattern: /^[A-Z]{2,4}$/, message: "前缀应为2-4位大写字母", trigger: "blur" }
        ]
      },
      generateResult: {
        successCount: 0,
        failCount: 0,
        totalCount: 0,
        successList: [],
        failList: []
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderType: null,
        orderNo: null,
        merchantName: null,
        userNickname: null,
        payStatus: null,
        payType: null,
        orderStatus: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        orderType: [
          { required: true, message: "订单类型不能为空", trigger: "change" }
        ],
        orderNo: [
          { required: true, message: "订单号不能为空", trigger: "blur" }
        ],
        merchantName: [
          { required: true, message: "商户名称不能为空", trigger: "blur" }
        ],
        actualPayAmount: [
          { required: true, message: "实际支付金额不能为空", trigger: "blur" }
        ],
        payStatus: [
          { required: true, message: "支付状态不能为空", trigger: "change" }
        ],
        orderStatus: [
          { required: true, message: "订单状态不能为空", trigger: "change" }
        ],
        refundStatus: [
          { required: true, message: "退款状态不能为空", trigger: "change" }
        ],
        orderCreateTime: [
          { required: true, message: "订单创建时间不能为空", trigger: "blur" }
        ],
        createBy: [
          { required: true, message: "创建人不能为空", trigger: "blur" }
        ],
        createTime: [
          { required: true, message: "记录创建时间不能为空", trigger: "blur" }
        ],
        updateBy: [
          { required: true, message: "更新人不能为空", trigger: "blur" }
        ],
        updateTime: [
          { required: true, message: "记录更新时间不能为空", trigger: "blur" }
        ],
        delFlag: [
          { required: true, message: "逻辑删除标志：0-正常，1-删除不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询第三方商品订单列表 */
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
        orderType: null,
        orderNo: null,
        merchantName: null,
        userNickname: null,
        actualPayAmount: null,
        payStatus: null,
        payType: null,
        payChannel: null,
        orderStatus: null,
        refundStatus: null,
        orderCreateTime: null,
        productInfo: null,
        receiverName: null,
        receiverPhone: null,
        receiverAddress: null,
        userRemark: null,
        merchantRemark: null,
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
      this.title = "添加第三方商品订单"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const orderId = row.orderId || this.ids
      getOrder(orderId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改第三方商品订单"
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
      this.$modal.confirm('是否确认删除第三方商品订单编号为"' + orderIds + '"的数据项？').then(function() {
        return delOrder(orderIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('express/third/order/export', {
        ...this.queryParams
      }, `order_${new Date().getTime()}.xlsx`)
    },

    /** 导入按钮操作 */
    handleImport() {
      this.importDialogVisible = true
    },

    /** 导入模板下载 */
    downloadTemplate() {
      importTemplate().then(response => {
        this.download(response, "第三方商品订单导入模板.xlsx")
      })
    },

    /** 文件选择变化处理 */
    handleFileChange(fileList) {
      this.fileList = fileList
    },

    /** 提交导入 */
    handleSubmitImport() {
      if (this.fileList.length === 0) {
        this.$modal.msgWarning("请先选择要导入的文件")
        return
      }

      // 调用FileUpload组件的提交方法
      this.$refs.fileUpload.submit()
    },

    /** 导入成功处理 */
    handleImportSuccess(res) {
      this.$modal.msgSuccess(res.msg)
      this.importDialogVisible = false
      this.fileList = []
      this.getList()
    },



    /** 批量生成快递订单按钮操作 */
    handleBatchGenerate() {
      if (this.ids.length === 0) {
        this.$modal.msgWarning("请至少选择一个第三方订单进行生成");
        return;
      }

      // 检查选中的订单是否都有完整的收货信息
      const selectedOrders = this.orderList.filter(order => this.ids.includes(order.orderId));
      const incompleteOrders = selectedOrders.filter(order =>
        !order.receiverName || !order.receiverPhone || !order.receiverAddress
      );

      if (incompleteOrders.length > 0) {
        this.$modal.confirm(`有 ${incompleteOrders.length} 个订单收货信息不完整，是否继续生成？`).then(() => {
          this.generateDialogVisible = true;
          this.resetGenerateForm();
        }).catch(() => {});
      } else {
        this.generateDialogVisible = true;
        this.resetGenerateForm();
      }
    },

    /** 重置生成表单 */
    resetGenerateForm() {
      this.generateForm = {
        senderName: "",
        senderPhone: "",
        senderAddress: "",
        customerId: null,
        expressType: "普通件",
        orderNoPrefix: "YS",
        defaultWeight: 1.0,
        defaultFreight: 0.0,
        warehouseId: null,
        waybillTemplateId: null
      };
      if (this.$refs.generateForm) {
        this.$refs.generateForm.clearValidate();
      }
    },

    /** 确认批量生成 */
    confirmBatchGenerate() {
      this.$refs["generateForm"].validate(valid => {
        if (valid) {
          this.generating = true;

          // 构建配置参数
          const config = {
            senderName: this.generateForm.senderName,
            senderPhone: this.generateForm.senderPhone,
            senderAddress: this.generateForm.senderAddress,
            customerId: this.generateForm.customerId,
            expressType: this.generateForm.expressType,
            orderNoPrefix: this.generateForm.orderNoPrefix.toUpperCase(),
            defaultWeight: this.generateForm.defaultWeight,
            defaultFreight: this.generateForm.defaultFreight,
            warehouseId: this.generateForm.warehouseId,
            waybillTemplateId: this.generateForm.waybillTemplateId
          };

          // 调用批量生成接口
          batchGenerateExpressOrders(this.ids, config).then(response => {
            this.generating = false;
            this.generateDialogVisible = false;

            // 显示生成结果
            this.generateResult = response.data;
            this.resultDialogVisible = true;
            this.resultActiveTab = "success";

            // 刷新列表
            this.getList();

            // 提示信息
            if (this.generateResult.failCount === 0) {
              this.$modal.msgSuccess(`成功生成 ${this.generateResult.successCount} 个快递订单`);
            } else {
              this.$modal.msgWarning(`成功生成 ${this.generateResult.successCount} 个，失败 ${this.generateResult.failCount} 个`);
            }
          }).catch(error => {
            this.generating = false;
            this.$modal.msgError("生成失败：" + (error.msg || "请求异常"));
          });
        }
      });
    }
  }
}
</script>
