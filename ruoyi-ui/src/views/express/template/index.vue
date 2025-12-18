<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="模板名称" prop="templateName">
        <el-input
          v-model="queryParams.templateName"
          placeholder="请输入模板名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="模板编码" prop="templateCode">
        <el-input
          v-model="queryParams.templateCode"
          placeholder="请输入模板编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in dict.type.express_template_status"
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
          v-hasPermi="['express:template:add']"
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
          v-hasPermi="['express:template:edit']"
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
          v-hasPermi="['express:template:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['express:template:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="templateList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="模板ID" align="center" prop="templateId" />
      <el-table-column label="模板名称" align="center" prop="templateName" />
      <el-table-column label="模板编码" align="center" prop="templateCode" />
      <el-table-column label="面单尺寸" align="center" prop="templateSize" />
      <el-table-column label="模板样式" align="center" prop="templateStyle" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.express_waybill_type" :value="scope.row.templateStyle"/>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.express_template_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['express:template:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-cog"
            @click="handleConfig(scope.row)"
            v-hasPermi="['express:template:config']"
          >配置</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['express:template:remove']"
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

    <!-- 添加或修改快递模板配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="模板名称" prop="templateName">
          <el-input v-model="form.templateName" placeholder="请输入模板名称" />
        </el-form-item>
        <el-form-item label="模板编码" prop="templateCode">
          <el-input v-model="form.templateCode" placeholder="请输入模板编码" />
        </el-form-item>
        <el-form-item label="面单尺寸" prop="templateSize">
          <el-input v-model="form.templateSize" placeholder="请输入面单尺寸" />
        </el-form-item>
        <el-form-item label="模板样式" prop="templateStyle">
          <el-select style="width: 100%" v-model="form.templateStyle" placeholder="模板样式" clearable>
            <el-option
                v-for="dict in dict.type.express_waybill_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.express_template_status"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 配置打印模板对话框 -->
    <el-dialog :title="'配置打印模板 - ' + configForm.templateName" :visible.sync="configOpen" width="1200px" append-to-body>
      <el-row :gutter="20">
        <!-- 左侧参数信息（调整为1:2比例） -->
        <el-col :span="8">
          <div class="config-card rounded-lg shadow-card p-5 scale-hover">
            <h2 class="text-lg font-semibold mb-4 flex items-center gap-2">
              <i class="fa fa-pencil text-primary"></i>
              面单内容编辑
            </h2>
            <el-form ref="configForm" :model="configForm" label-width="100px" class="config-form">
              <!-- 基本信息区块 -->
              <div class="form-section">
                <div class="form-section-title">
                  <i class="fa fa-info-circle"></i>
                  基本信息
                </div>
                <div class="form-section-content">
                  <el-form-item label="模板名称">
                    <el-input v-model="configForm.templateName" disabled class="form-input" />
                  </el-form-item>
                  <el-form-item label="面单尺寸">
                    <el-input v-model="configForm.templateSize" placeholder="请输入面单尺寸，如：76x130" @input="handleTemplateSizeChange" class="form-input" />
                  </el-form-item>
                  <el-form-item label="打印方向">
                    <el-radio-group v-model="configForm.printDirection" class="radio-group">
                      <el-radio label="portrait" border>纵向</el-radio>
                      <el-radio label="landscape" border>横向</el-radio>
                    </el-radio-group>
                  </el-form-item>
                </div>
              </div>

              <!-- 打印边距区块 -->
              <div class="form-section">
                <div class="form-section-title">
                  <i class="fa fa-arrows"></i>
                  打印边距设置
                </div>
                <div class="form-section-content">
                  <el-form-item label="">
                    <el-row :gutter="15">
                      <el-col :span="6">
                        <div class="input-with-label">
                          <label class="input-label">上</label>
                          <el-input v-model="configForm.marginTop" placeholder="上" class="form-input" />
                        </div>
                      </el-col>
                      <el-col :span="6">
                        <div class="input-with-label">
                          <label class="input-label">右</label>
                          <el-input v-model="configForm.marginRight" placeholder="右" class="form-input" />
                        </div>
                      </el-col>
                      <el-col :span="6">
                        <div class="input-with-label">
                          <label class="input-label">下</label>
                          <el-input v-model="configForm.marginBottom" placeholder="下" class="form-input" />
                        </div>
                      </el-col>
                      <el-col :span="6">
                        <div class="input-with-label">
                          <label class="input-label">左</label>
                          <el-input v-model="configForm.marginLeft" placeholder="左" class="form-input" />
                        </div>
                      </el-col>
                    </el-row>
                  </el-form-item>
                </div>
              </div>

              <!-- 字段配置区块 -->
              <div class="form-section">
                <div class="form-section-title">
                  <i class="fa fa-list"></i>
                  字段配置
                </div>
                <div class="form-section-content">
                  <el-form-item label="">
                    <el-checkbox-group v-model="fieldsConfigArray" class="checkbox-grid">
                      <el-checkbox label="waybillNo" border class="checkbox-item">快递单号</el-checkbox>
                      <el-checkbox label="consignee" border class="checkbox-item">收件人</el-checkbox>
                      <el-checkbox label="phone" border class="checkbox-item">电话</el-checkbox>
                      <el-checkbox label="address" border class="checkbox-item">地址</el-checkbox>
                      <el-checkbox label="goodsInfo" border class="checkbox-item">商品信息</el-checkbox>
                      <el-checkbox label="weight" border class="checkbox-item">重量</el-checkbox>
                      <el-checkbox label="orderNo" border class="checkbox-item">订单号</el-checkbox>
                    </el-checkbox-group>
                  </el-form-item>
                </div>
              </div>

              <!-- 打印配置区块 -->
              <div class="form-section">
                <div class="form-section-title">
                  <i class="fa fa-print"></i>
                  打印配置
                </div>
                <div class="form-section-content">
                  <el-form-item label="">
                    <el-row :gutter="15">
                      <el-col :span="8">
                        <div class="input-with-label">
                          <label class="input-label">字体大小</label>
                          <el-input v-model="printConfigForm.fontSize" placeholder="请输入字体大小" type="number" min="8" max="24" class="form-input" />
                        </div>
                      </el-col>
                      <el-col :span="8">
                        <div class="input-with-label">
                          <label class="input-label">上边距</label>
                          <el-input v-model="printConfigForm.marginTop" placeholder="请输入上边距" type="number" min="0" max="50" class="form-input" />
                        </div>
                      </el-col>
                      <el-col :span="8">
                        <div class="input-with-label">
                          <label class="input-label">左边距</label>
                          <el-input v-model="printConfigForm.marginLeft" placeholder="请输入左边距" type="number" min="0" max="50" class="form-input" />
                        </div>
                      </el-col>
                    </el-row>
                    <el-row :gutter="20" style="margin-top: 15px;">
                      <el-col :span="12">
                        <el-checkbox v-model="printConfigForm.printHeader" class="checkbox-option">打印头部</el-checkbox>
                      </el-col>
                      <el-col :span="12">
                        <el-checkbox v-model="printConfigForm.printFooter" class="checkbox-option">打印底部</el-checkbox>
                      </el-col>
                    </el-row>
                  </el-form-item>
                </div>
              </div>

              <!-- 面单预览控制 -->
              <el-form-item label="预览控制">
                <el-button type="primary" size="mini" @click="previewWaybill" class="config-btn">预览面单</el-button>
                <el-button size="mini" @click="resetPreview" class="config-btn">重置预览</el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-col>

        <!-- 右侧快递单预览（调整为1:2比例，整合顺丰HTML） -->
        <el-col :span="16">
          <div class="preview-card rounded-lg shadow-card p-5 scale-hover flex flex-col items-center">
            <h2 class="text-lg font-semibold mb-4 flex items-center gap-2 self-start">
              <i class="fa fa-eye text-primary"></i>
              顺丰面单预览 ({{configForm.templateSize || '76×130'}}mm)
            </h2>
            <div class="preview-content" :style="{
              width: configForm.templateSize ? (configForm.templateSize.split('x')[0] * 5) + 'px' : '380px',
              height: configForm.templateSize ? (configForm.templateSize.split('x')[1] * 5) + 'px' : '650px',
              marginTop: configForm.marginTop ? configForm.marginTop + 'px' : '0px',
              marginRight: configForm.marginRight ? configForm.marginRight + 'px' : '0px',
              marginBottom: configForm.marginBottom ? configForm.marginBottom + 'px' : '0px',
              marginLeft: configForm.marginLeft ? configForm.marginLeft + 'px' : '0px'
            }">
              <!-- 使用快递单预览组件 -->
              <express-waybill-preview
                :preview-visible="previewVisible"
                :fields-config="fieldsConfigArray"
                :print-config="printConfigForm"
                :preview-data="previewData"
                :waybill-style="{
                  fontSize: printConfigForm.fontSize + 'px',
                  padding: '10px',
                  height: '100%'
                }"
              />
            </div>
          </div>
        </el-col>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitConfig">保存配置</el-button>
        <el-button @click="cancelConfig">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listTemplate, getTemplate, delTemplate, addTemplate, updateTemplate } from "@/api/express/template"
// 引入JsBarcode（需先安装：npm install jsbarcode --save 或使用CDN）
import JsBarcode from 'jsbarcode'
// 引入快递单预览组件
import ExpressWaybillPreview from '@/components/ExpressWaybillPreview/index'

export default {
  name: "Template",
  components: {
    ExpressWaybillPreview
  },
  dicts: ['express_template_status','express_waybill_type'],
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
      // 快递模板配置表格数据
      templateList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 配置弹窗是否显示
      configOpen: false,
      // 预览是否可见
      previewVisible: false,
      // 预览数据
      previewData: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        templateName: null,
        templateCode: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 新增/修改模板的字段配置数组（用于复选框组）
      formFieldsConfigArray: [],
      // 新增/修改模板的打印配置表单（用于表单控件）
      formPrintConfigForm: {
        fontSize: 12,
        marginTop: 5,
        marginLeft: 5,
        printHeader: true,
        printFooter: true
      },
      // 配置表单参数
      configForm: {
        templateId: null,
        templateName: null,
        templateSize: null,
        printDirection: 'portrait',
        marginTop: '0',
        marginRight: '0',
        marginBottom: '0',
        marginLeft: '0',
        fieldsConfig: null,
        printConfig: null
      },
      // 字段配置数组（用于复选框组）
      fieldsConfigArray: [],
      // 打印配置表单（用于表单控件）
      printConfigForm: {
        fontSize: 12,
        marginTop: 5,
        marginLeft: 5,
        printHeader: true,
        printFooter: true
      },
      // 表单校验
      rules: {
        templateName: [
          { required: true, message: "模板名称不能为空", trigger: "blur" }
        ],
        templateCode: [
          { required: true, message: "模板编码不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
    // 初始化JsBarcode（如果使用CDN可省略）
    if (typeof window !== 'undefined') {
      window.JsBarcode = JsBarcode
    }
  },
  methods: {
    /** 查询快递模板配置列表 */
    getList() {
      this.loading = true
      listTemplate(this.queryParams).then(response => {
        this.templateList = response.rows
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
        templateId: null,
        templateName: null,
        templateCode: null,
        templateSize: null,
        templateStyle: null,
        fieldsConfig: null,
        printConfig: null,
        status: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        delFlag: null
      }
      // 重置组件数据
      this.formFieldsConfigArray = []
      this.formPrintConfigForm = {
        fontSize: 12,
        marginTop: 5,
        marginLeft: 5,
        printHeader: true,
        printFooter: true
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
      this.ids = selection.map(item => item.templateId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加快递模板配置"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const templateId = row.templateId || this.ids
      getTemplate(templateId).then(response => {
        this.form = response.data

        // 解析字段配置JSON为数组
        if (this.form.fieldsConfig) {
          try {
            const fieldsConfig = JSON.parse(this.form.fieldsConfig)
            this.formFieldsConfigArray = Object.keys(fieldsConfig).filter(key => fieldsConfig[key])
          } catch (e) {
            this.formFieldsConfigArray = []
          }
        } else {
          this.formFieldsConfigArray = []
        }

        // 解析打印配置JSON为表单
        if (this.form.printConfig) {
          try {
            const printConfig = JSON.parse(this.form.printConfig)
            this.formPrintConfigForm = { ...this.formPrintConfigForm, ...printConfig }
          } catch (e) {
            // 使用默认值
          }
        }

        this.open = true
        this.title = "修改快递模板配置"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 将字段配置数组转换为JSON对象
          const fieldsConfig = {}
          const allFields = ['waybillNo', 'consignee', 'phone', 'address', 'goodsInfo', 'weight', 'orderNo']
          allFields.forEach(field => {
            fieldsConfig[field] = this.formFieldsConfigArray.includes(field)
          })
          this.form.fieldsConfig = JSON.stringify(fieldsConfig)

          // 将打印配置表单转换为JSON字符串
          this.form.printConfig = JSON.stringify(this.formPrintConfigForm)

          if (this.form.templateId != null) {
            updateTemplate(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addTemplate(this.form).then(response => {
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
      const templateIds = row.templateId || this.ids
      this.$modal.confirm('是否确认删除快递模板配置编号为"' + templateIds + '"的数据项？').then(function() {
        return delTemplate(templateIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 配置按钮操作 */
    handleConfig(row) {
      this.resetConfig()
      const templateId = row.templateId
      getTemplate(templateId).then(response => {
        this.configForm = response.data

        // 解析字段配置JSON为数组
        if (this.configForm.fieldsConfig) {
          try {
            const fieldsConfig = JSON.parse(this.configForm.fieldsConfig)
            this.fieldsConfigArray = Object.keys(fieldsConfig).filter(key => fieldsConfig[key])
          } catch (e) {
            this.fieldsConfigArray = []
          }
        }

        // 解析打印配置JSON为表单
        if (this.configForm.printConfig) {
          try {
            const printConfig = JSON.parse(this.configForm.printConfig)
            this.printConfigForm = { ...this.printConfigForm, ...printConfig }
          } catch (e) {
            // 使用默认值
          }
        }

        this.configOpen = true
        this.previewVisible = false // 初始隐藏预览
      })
    },
    /** 配置表单重置 */
    resetConfig() {
      this.configForm = {
        templateId: null,
        templateName: null,
        templateSize: null,
        printDirection: 'portrait',
        marginTop: '0',
        marginRight: '0',
        marginBottom: '0',
        marginLeft: '0',
        fieldsConfig: null,
        printConfig: null
      }
      this.previewVisible = false
      this.previewData = {}
      this.$refs['configForm'] && this.$refs['configForm'].resetFields()
    },
    /** 面单尺寸输入变化处理 */
    handleTemplateSizeChange() {
      // 验证输入格式是否为数字x数字
      const pattern = /^\d+x\d+$/;
      if (this.configForm.templateSize && !pattern.test(this.configForm.templateSize)) {
        this.$message.warning('请输入正确的面单尺寸格式，如：76x130');
      }
    },
    /** 配置弹窗取消按钮 */
    cancelConfig() {
      this.configOpen = false
      this.resetConfig()
    },
    /** 配置表单提交 */
    submitConfig() {
      this.$refs['configForm'].validate(valid => {
        if (valid) {
          // 将字段配置数组转换为JSON对象
          const fieldsConfig = {}
          const allFields = ['waybillNo', 'consignee', 'phone', 'address', 'goodsInfo', 'weight', 'orderNo']
          allFields.forEach(field => {
            fieldsConfig[field] = this.fieldsConfigArray.includes(field)
          })
          this.configForm.fieldsConfig = JSON.stringify(fieldsConfig)

          // 将打印配置表单转换为JSON字符串
          this.configForm.printConfig = JSON.stringify(this.printConfigForm)

          updateTemplate(this.configForm).then(response => {
            this.$modal.msgSuccess("配置保存成功")
            this.configOpen = false
            this.getList()
          })
        }
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('express/template/export', {
        ...this.queryParams
      }, `template_${new Date().getTime()}.xlsx`)
    },
    /** 预览面单 */
    previewWaybill() {
      this.previewVisible = true

      // 生成预览数据
      this.previewData = {
        waybillNo: this.generateSFWaybillNo(),
        expressType: '顺丰标快',
        sendDate: new Date().toISOString().split('T')[0],
        goodsType: '文件',
        senderName: '张三',
        senderPhone: '13800138000',
        senderAddress: '广东省深圳市南山区科技园',
        receiverName: '李四',
        receiverPhone: '13900139000',
        receiverAddress: '北京市朝阳区建国路88号',
        weight: '0.5',
        quantity: '1',
        fee: '18.00',
        remarks: '无特殊要求'
      }

      // 解析字段配置（如果有JSON配置）
      if (this.configForm.fieldsConfig) {
        try {
          const fields = JSON.parse(this.configForm.fieldsConfig)
          Object.assign(this.previewData, fields)
        } catch (e) {
          this.$message.warning('字段配置JSON格式错误，使用默认数据预览')
        }
      }

      // 生成条形码
      this.generateDynamicBarcode(this.previewData.waybillNo)

      this.$message.success('面单预览已生成')
    },
    /** 重置预览 */
    resetPreview() {
      this.previewVisible = false
      this.previewData = {}
    },
    /** 生成顺丰运单号 */
    generateSFWaybillNo() {
      const prefix = 'SF';
      const date = new Date().getFullYear().toString().substr(2) +
                   (new Date().getMonth() + 1).toString().padStart(2, '0') +
                   new Date().getDate().toString().padStart(2, '0');
      const random = Math.floor(Math.random() * 1000000).toString().padStart(6, '0');
      return prefix + date + random;
    },
    /** 生成动态条形码 */
    generateDynamicBarcode(waybillNo) {
      const barcodeCanvas = document.getElementById('dynamicBarcode');
      if (!barcodeCanvas) return;

      // 使用JsBarcode生成条形码
      JsBarcode(barcodeCanvas, waybillNo, {
        format: "CODE128",       // 条形码格式
        width: 2,                // 条码宽度
        height: 60,              // 条码高度
        displayValue: false,     // 不显示下方数字
        margin: 10,              // 边距
        lineColor: "#000000"     // 条码颜色
      });
    }
  },
  watch: {
    // 监听字段配置变化，实时更新预览
    fieldsConfigArray: {
      handler() {
        if (this.previewVisible) {
          // 重新生成条形码（如果快递单号字段被选中）
          if (this.fieldsConfigArray.includes('waybillNo')) {
            this.generateDynamicBarcode(this.previewData.waybillNo)
          }
        }
      },
      deep: true
    },
    // 监听打印配置变化，实时更新预览
    printConfigForm: {
      handler() {
        if (this.previewVisible) {
          // 重新生成条形码以适应新的字体大小
          if (this.fieldsConfigArray.includes('waybillNo')) {
            this.generateDynamicBarcode(this.previewData.waybillNo)
          }
        }
      },
      deep: true
    }
  }
}
</script>

<style scoped>

</style>


