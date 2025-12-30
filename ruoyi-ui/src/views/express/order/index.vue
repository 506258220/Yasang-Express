<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="124  px">
      <el-form-item label="快递单号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入快递单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="寄件人姓名" prop="senderName">
        <el-input
          v-model="queryParams.senderName"
          placeholder="请输入寄件人姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="寄件人真实手机号" prop="senderPhone">
        <el-input
          v-model="queryParams.senderPhone"
          placeholder="请输入寄件人真实手机号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="寄件人虚拟号" prop="senderVirtualPhone">
        <el-input
          v-model="queryParams.senderVirtualPhone"
          placeholder="请输入寄件人虚拟号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="收件人姓名" prop="receiverName">
        <el-input
          v-model="queryParams.receiverName"
          placeholder="请输入收件人姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="收件人真实手机号" prop="receiverPhone">
        <el-input
          v-model="queryParams.receiverPhone"
          placeholder="请输入收件人真实手机号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="收件人虚拟号" prop="receiverVirtualPhone">
        <el-input
          v-model="queryParams.receiverVirtualPhone"
          placeholder="请输入收件人虚拟号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="支付时间" prop="payTime">
        <el-date-picker clearable
          v-model="queryParams.payTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择支付时间">
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
          icon="el-icon-printer"
          size="mini"
          :disabled="multiple"
          @click="handleBatchPrint"
          v-hasPermi="['express:order:print']"
        >批量打印</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="id" />
      <el-table-column label="快递单号" align="center" prop="orderNo" />
      <el-table-column label="寄件人客户" align="center" prop="customerId" />
      <el-table-column label="寄件人姓名" align="center" prop="senderName" />
      <el-table-column label="寄件人真实手机号" align="center" prop="senderPhone" />
      <el-table-column label="寄件人虚拟号" align="center" prop="senderVirtualPhone" />
      <el-table-column label="寄件人详细地址" align="center" prop="senderAddress" />
      <el-table-column label="收件人姓名" align="center" prop="receiverName" />
      <el-table-column label="收件人真实手机号" align="center" prop="receiverPhone" />
      <el-table-column label="收件人虚拟号" align="center" prop="receiverVirtualPhone" />
      <el-table-column label="收件人详细地址" align="center" prop="receiverAddress" />
      <el-table-column label="快递类型" align="center" prop="expressType" />
      <el-table-column label="快递重量" align="center" prop="weight" />
      <el-table-column label="运费金额" align="center" prop="freight" />
      <el-table-column label="支付状态" align="center" prop="payStatus" />
      <el-table-column label="支付时间" align="center" prop="payTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.payTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="订单状态" align="center" prop="orderStatus" />
      <el-table-column label="始发仓库" align="center" prop="warehouseId" />
      <el-table-column label="运输车辆" align="center" prop="vehicleId" />
      <el-table-column label="派送司机" align="center" prop="driverId" />
      <el-table-column label="面单模板" align="center" prop="waybillTemplateId" />
      <el-table-column label="订单备注" align="center" prop="remark" />
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

    <!-- 批量打印模板选择对话框 -->
    <el-dialog
      title="选择打印模板"
      :visible.sync="printDialogVisible"
      width="600px"
      append-to-body
    >
      <el-form label-width="100px">
        <el-form-item label="选择模板" required>
          <el-select
            v-model="selectedTemplateId"
            placeholder="请选择面单模板"
            style="width: 100%;"
            filterable
          >
            <el-option
              v-for="template in templateList"
              :key="template.id"
              :label="template.templateName"
              :value="template.id"
            >
              <span style="float: left">{{ template.templateName }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">{{ template.paperSize }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-alert
            :title="`已选择 ${ids.length} 个订单进行打印`"
            type="info"
            :closable="false"
            show-icon
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="printDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmBatchPrint" :loading="printing">开始打印</el-button>
      </div>
    </el-dialog>

    <!-- 添加或修改快递订单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="快递单号" prop="orderNo">
          <el-input v-model="form.orderNo" placeholder="请输入快递单号" />
        </el-form-item>
        <el-form-item label="寄件人客户" prop="customerId">
          <el-input v-model="form.customerId" placeholder="请输入寄件人客户" />
        </el-form-item>
        <el-form-item label="寄件人姓名" prop="senderName">
          <el-input v-model="form.senderName" placeholder="请输入寄件人姓名" />
        </el-form-item>
        <el-form-item label="寄件人真实手机号" prop="senderPhone">
          <el-input v-model="form.senderPhone" placeholder="请输入寄件人真实手机号" />
        </el-form-item>
        <el-form-item label="寄件人虚拟号" prop="senderVirtualPhone">
          <el-input v-model="form.senderVirtualPhone" placeholder="请输入寄件人虚拟号" />
        </el-form-item>
        <el-form-item label="寄件人详细地址" prop="senderAddress">
          <el-input v-model="form.senderAddress" placeholder="请输入寄件人详细地址" />
        </el-form-item>
        <el-form-item label="收件人姓名" prop="receiverName">
          <el-input v-model="form.receiverName" placeholder="请输入收件人姓名" />
        </el-form-item>
        <el-form-item label="收件人真实手机号" prop="receiverPhone">
          <el-input v-model="form.receiverPhone" placeholder="请输入收件人真实手机号" />
        </el-form-item>
        <el-form-item label="收件人虚拟号" prop="receiverVirtualPhone">
          <el-input v-model="form.receiverVirtualPhone" placeholder="请输入收件人虚拟号" />
        </el-form-item>
        <el-form-item label="收件人详细地址" prop="receiverAddress">
          <el-input v-model="form.receiverAddress" placeholder="请输入收件人详细地址" />
        </el-form-item>
        <el-form-item label="快递重量" prop="weight">
          <el-input v-model="form.weight" placeholder="请输入快递重量" />
        </el-form-item>
        <el-form-item label="运费金额" prop="freight">
          <el-input v-model="form.freight" placeholder="请输入运费金额" />
        </el-form-item>
        <el-form-item label="支付时间" prop="payTime">
          <el-date-picker clearable
            v-model="form.payTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择支付时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="始发仓库ID" prop="warehouseId">
          <el-input v-model="form.warehouseId" placeholder="请输入始发仓库" />
        </el-form-item>
        <el-form-item label="运输车辆ID" prop="vehicleId">
          <el-input v-model="form.vehicleId" placeholder="请输入运输车辆" />
        </el-form-item>
        <el-form-item label="派送司机ID" prop="driverId">
          <el-input v-model="form.driverId" placeholder="请输入派送司机ID" />
        </el-form-item>
        <el-form-item label="面单模板ID" prop="waybillTemplateId">
          <el-input v-model="form.waybillTemplateId" placeholder="请输入面单模板ID" />
        </el-form-item>
        <el-form-item label="订单备注" prop="remark">
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
import { listOrder, getOrder, delOrder, addOrder, updateOrder } from "@/api/express/order"
import { listWaybillTemplate } from "@/api/express/waybillTemplate"
import { generatePrintHtml } from "@/api/express/waybillTemplate"

export default {
  name: "Order",
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
      // 快递订单表格数据
      orderList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 批量打印相关
      printDialogVisible: false,
      selectedTemplateId: null,
      templateList: [],
      printing: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNo: null,
        customerId: null,
        senderName: null,
        senderPhone: null,
        senderVirtualPhone: null,
        senderAddress: null,
        receiverName: null,
        receiverPhone: null,
        receiverVirtualPhone: null,
        receiverAddress: null,
        expressType: null,
        weight: null,
        freight: null,
        payStatus: null,
        payTime: null,
        orderStatus: null,
        warehouseId: null,
        vehicleId: null,
        driverId: null,
        waybillTemplateId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        orderNo: [
          { required: true, message: "快递单号不能为空", trigger: "blur" }
        ],
        senderName: [
          { required: true, message: "寄件人姓名不能为空", trigger: "blur" }
        ],
        senderPhone: [
          { required: true, message: "寄件人真实手机号不能为空", trigger: "blur" }
        ],
        senderAddress: [
          { required: true, message: "寄件人详细地址不能为空", trigger: "blur" }
        ],
        receiverName: [
          { required: true, message: "收件人姓名不能为空", trigger: "blur" }
        ],
        receiverPhone: [
          { required: true, message: "收件人真实手机号不能为空", trigger: "blur" }
        ],
        receiverAddress: [
          { required: true, message: "收件人详细地址不能为空", trigger: "blur" }
        ],
        expressType: [
          { required: true, message: "快递类型不能为空", trigger: "change" }
        ],
        weight: [
          { required: true, message: "快递重量不能为空", trigger: "blur" }
        ],
        freight: [
          { required: true, message: "运费金额不能为空", trigger: "blur" }
        ],
        payStatus: [
          { required: true, message: "支付状态不能为空", trigger: "change" }
        ],
        orderStatus: [
          { required: true, message: "订单状态不能为空", trigger: "change" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询快递订单列表 */
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
        id: null,
        orderNo: null,
        customerId: null,
        senderName: null,
        senderPhone: null,
        senderVirtualPhone: null,
        senderAddress: null,
        receiverName: null,
        receiverPhone: null,
        receiverVirtualPhone: null,
        receiverAddress: null,
        expressType: null,
        weight: null,
        freight: null,
        payStatus: null,
        payTime: null,
        orderStatus: null,
        warehouseId: null,
        vehicleId: null,
        driverId: null,
        waybillTemplateId: null,
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
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加快递订单"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getOrder(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改快递订单"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
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
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除快递订单编号为"' + ids + '"的数据项？').then(function () {
        return delOrder(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('express/order/export', {
        ...this.queryParams
      }, `order_${new Date().getTime()}.xlsx`)
    },

    /** 批量打印按钮操作 */
    handleBatchPrint() {
      if (this.ids.length === 0) {
        this.$modal.msgWarning("请至少选择一个订单进行打印");
        return;
      }
      // 加载模板列表
      this.loadTemplateList();
      this.printDialogVisible = true;
    },

    /** 加载模板列表 */
    loadTemplateList() {
      listWaybillTemplate({
        status: "1", // 只加载启用的模板
        pageNum: 1,
        pageSize: 100
      }).then(response => {
        this.templateList = response.rows || [];
        if (this.templateList.length === 0) {
          this.$modal.msgWarning("没有可用的打印模板，请先创建模板");
          this.printDialogVisible = false;
        }
      });
    },

    /** 确认批量打印 */
    confirmBatchPrint() {
      if (!this.selectedTemplateId) {
        this.$modal.msgWarning("请选择打印模板");
        return;
      }
      this.printing = true;

      // 获取选中的订单数据
      const selectedOrders = this.orderList.filter(order => this.ids.includes(order.id));

      // 逐个生成打印数据
      const printPromises = selectedOrders.map(order => {
        return generatePrintHtml(order.id, this.selectedTemplateId).then(response => {
          return {
            orderNo: order.orderNo,
            template: response.data.template,
            orderData: response.data.orderData
          };
        });
      });

      Promise.all(printPromises).then(results => {
        this.printing = false;
        this.printDialogVisible = false;
        // 打开打印窗口
        this.openPrintWindow(results, this.selectedTemplateId);
        this.$modal.msgSuccess(`成功生成 ${results.length} 个订单的打印内容`);
      }).catch(error => {
        this.printing = false;
        this.$modal.msgError("打印生成失败：" + (error.msg || "请求异常"));
      });
    },

    /** 打开打印窗口 */
    openPrintWindow(printData, templateId) {
      // 数据验证
      if (!printData || !Array.isArray(printData) || printData.length === 0) {
        this.$modal.msgError("打印数据为空");
        return;
      }

      // 获取模板信息
      const template = this.templateList.find(t => t.id === templateId);
      const paperSize = template && template.paperSize ? template.paperSize : "100*150mm";
      const sizeParts = paperSize.split("*");
      const width = sizeParts[0] ? sizeParts[0].replace("mm", "") : "100";
      const height = sizeParts.length > 1 ? sizeParts[1].replace("mm", "") : "150";

      // 创建打印窗口
      const printWindow = window.open("", "_blank");
      if (!printWindow) {
        this.$modal.msgError("无法打开打印窗口，请检查浏览器弹窗设置");
        return;
      }

      // 写入打印窗口并打印
      printWindow.document.write(printHtml);
      printWindow.document.close();

      // 延迟打印，确保内容加载完成
      setTimeout(() => {
        printWindow.print();
        // 打印后可选择关闭窗口
        // printWindow.close();
      }, 1000);
    }
  }
}
</script>
