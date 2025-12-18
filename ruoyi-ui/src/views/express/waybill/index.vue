<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="快递单号" prop="waybillNo">
        <el-input
          v-model="queryParams.waybillNo"
          placeholder="请输入快递单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="订单编号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入订单编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="寄件日期" prop="sendDate">
        <el-date-picker clearable
          v-model="queryParams.sendDate"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择寄件日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="收货人" prop="consignee">
        <el-input
          v-model="queryParams.consignee"
          placeholder="请输入收货人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="收货电话" prop="consigneePhone">
        <el-input
          v-model="queryParams.consigneePhone"
          placeholder="请输入收货电话"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="快递状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择快递状态" clearable>
          <el-option
            v-for="dict in dict.type.print_status"
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
          v-hasPermi="['express:waybill:add']"
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
          v-hasPermi="['express:waybill:edit']"
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
          v-hasPermi="['express:waybill:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['express:waybill:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="waybillList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="快递单ID" align="center" prop="waybillId" />
      <el-table-column label="快递单号" align="center" prop="waybillNo" />
      <el-table-column label="订单编号" align="center" prop="orderNo" />
<!--      <el-table-column label="关联模板ID" align="center" prop="templateId" />-->
      <el-table-column label="快递类型" align="center" prop="expressType" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.express_waybill_type" :value="scope.row.expressType"/>
        </template>
      </el-table-column>
      <el-table-column label="寄件日期" align="center" prop="sendDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.sendDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="物品类型" align="center" prop="goodsType" />
      <el-table-column label="收货人" align="center" prop="consignee" />
      <el-table-column label="收货电话" align="center" prop="consigneePhone" />
      <el-table-column label="收货地址" align="center" prop="consigneeAddress" />
      <el-table-column label="重量(kg)" align="center" prop="weight" />
      <el-table-column label="物品数量" align="center" prop="quantity" />
      <el-table-column label="备注信息" align="center" prop="remarks" />
      <el-table-column label="快递状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.print_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="打印人" align="center" prop="printBy" />
      <el-table-column label="打印时间" align="center" prop="printTime" width="100">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.printTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="打印次数" align="center" prop="printCount" width="80" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['express:waybill:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-printer"
            @click="handlePrint(scope.row)"
            v-hasPermi="['express:waybill:print']"
          >打印</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['express:waybill:remove']"
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

    <!-- 添加或修改快递单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="快递单号" prop="waybillNo">
          <el-input v-model="form.waybillNo" placeholder="请输入快递单号" />
        </el-form-item>
        <el-form-item label="订单编号" prop="orderNo">
          <el-input v-model="form.orderNo" placeholder="请输入订单编号" />
        </el-form-item>
        <el-form-item label="寄件日期" prop="sendDate">
          <el-date-picker clearable
            v-model="form.sendDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择寄件日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="收货人" prop="consignee">
          <el-input v-model="form.consignee" placeholder="请输入收货人" />
        </el-form-item>
        <el-form-item label="收货电话" prop="consigneePhone">
          <el-input v-model="form.consigneePhone" placeholder="请输入收货电话" />
        </el-form-item>
        <el-form-item label="收货地址" prop="consigneeAddress">
          <el-input v-model="form.consigneeAddress" placeholder="请输入收货地址" />
        </el-form-item>
        <el-form-item label="重量(kg)" prop="weight">
          <el-input v-model="form.weight" placeholder="请输入重量(kg)" />
        </el-form-item>
        <el-form-item label="物品数量" prop="quantity">
          <el-input v-model="form.quantity" placeholder="请输入物品数量" />
        </el-form-item>
        <el-form-item label="备注信息" prop="remarks">
          <el-input v-model="form.remarks" placeholder="请输入备注信息" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 打印快递单对话框 -->
    <el-dialog
      title="打印快递单"
      :visible.sync="printDialogVisible"
      width="800px"
      append-to-body
    >
      <div class="print-dialog-content">
        <div class="print-preview-area">
          <!-- 使用快递单预览组件 -->
          <express-waybill-preview
            :preview-visible="printPreviewVisible"
            :fields-config="printFieldsConfig"
            :print-config="printConfig"
            :preview-data="printWaybillData"
            :waybill-style="{ fontSize: printConfig.fontSize + 'px', padding: '10px', height: '100%' }"
          />
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="closePrintDialog">取消</el-button>
        <el-button type="primary" @click="downloadPdf">下载PDF</el-button>
        <el-button type="success" @click="doPrint">打印</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listWaybill, getWaybill, delWaybill, addWaybill, updateWaybill } from "@/api/express/waybill"
import ExpressWaybillPreview from '@/components/ExpressWaybillPreview/index'

export default {
  name: "Waybill",
  components: { ExpressWaybillPreview },
  dicts: ['print_status','express_waybill_type'],
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
      // 快递单表格数据
      waybillList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        waybillNo: null,
        orderId: null,
        orderNo: null,
        sendDate: null,
        consignee: null,
        consigneePhone: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        waybillNo: [
          { required: true, message: "快递单号不能为空", trigger: "blur" }
        ],
        templateId: [
          { required: true, message: "关联模板ID不能为空", trigger: "change" }
        ],
        expressType: [
          { required: true, message: "快递类型不能为空", trigger: "change" }
        ],
        sendDate: [
          { required: true, message: "寄件日期不能为空", trigger: "blur" }
        ],
        consignee: [
          { required: true, message: "收货人不能为空", trigger: "blur" }
        ],
        consigneePhone: [
          { required: true, message: "收货电话不能为空", trigger: "blur" }
        ],
        consigneeAddress: [
          { required: true, message: "收货地址不能为空", trigger: "blur" }
        ],
      },
      // 打印相关参数
      printDialogVisible: false, // 打印对话框可见性
      printWaybillData: {}, // 当前要打印的快递单数据
      printPreviewVisible: true, // 预览组件显示状态
      printFieldsConfig: ['waybillNo', 'consignee', 'phone', 'address', 'goodsInfo', 'weight', 'orderNo'], // 预览字段配置
      printConfig: { fontSize: 12 }, // 打印配置
      selectedPrintRow: null // 当前选中的打印行数据
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询快递单列表 */
    getList() {
      this.loading = true
      listWaybill(this.queryParams).then(response => {
        this.waybillList = response.rows
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
        waybillId: null,
        waybillNo: null,
        orderId: null,
        orderNo: null,
        templateId: null,
        expressType: null,
        sendDate: null,
        goodsType: null,
        consignee: null,
        consigneePhone: null,
        consigneeAddress: null,
        weight: null,
        quantity: null,
        remarks: null,
        status: null,
        printBy: null,
        printTime: null,
        printCount: null,
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
      this.ids = selection.map(item => item.waybillId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加快递单"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const waybillId = row.waybillId || this.ids
      getWaybill(waybillId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改快递单"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.waybillId != null) {
            updateWaybill(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addWaybill(this.form).then(response => {
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
      const waybillIds = row.waybillId || this.ids
      this.$modal.confirm('是否确认删除快递单编号为"' + waybillIds + '"的数据项？').then(function() {
        return delWaybill(waybillIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('express/waybill/export', {
        ...this.queryParams
      }, `waybill_${new Date().getTime()}.xlsx`)
    },
    /** 打印按钮操作 */
    handlePrint(row) {
      // 保存选中的行数据
      this.selectedPrintRow = row
      
      // 设置打印数据，将row数据转换为预览组件需要的格式
      this.printWaybillData = {
        waybillNo: row.waybillNo,
        orderNo: row.orderNo,
        consignee: row.consignee,
        phone: row.consigneePhone,
        address: row.consigneeAddress,
        goodsInfo: row.goodsType,
        weight: row.weight,
        // 寄件人信息（这里可以根据实际情况调整）
        senderName: '系统默认寄件人',
        senderPhone: '4008111111',
        senderAddress: '系统默认寄件地址'
      }
      // 打开打印对话框
      this.printDialogVisible = true
      this.printPreviewVisible = true
    },
    /** 关闭打印对话框 */
    closePrintDialog() {
      this.printDialogVisible = false
      this.printPreviewVisible = false
      this.printWaybillData = {}
    },
    /** 执行打印操作 */
    doPrint() {
      // 这里可以添加打印相关的逻辑，例如调用浏览器打印功能
      window.print()
    },
    /** 下载PDF快递单 */
    downloadPdf() {
      // 获取当前要打印的快递单ID（从表格行数据中获取）
      const waybillId = this.selectedPrintRow.waybillId
      // 使用默认模板ID（这里可以根据实际情况调整）
      const templateId = 1
      
      // 构造下载URL
      const url = `/express/waybill/generatePdf/${waybillId}/${templateId}`
      
      // 发起下载请求
      this.download(url, {}, `waybill_${waybillId}_${new Date().getTime()}.pdf`)
      // 关闭打印对话框
      this.closePrintDialog()
    }
  }
}
</script>
