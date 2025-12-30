<template>
  <div class="app-container">
    <!-- 查询表单（若依标准） -->
    <el-form :inline="true" :model="queryParams" class="query-form" @keyup.enter="handleQuery">
      <el-form-item label="模板名称" prop="templateName">
        <el-input
          v-model="queryParams.templateName"
          placeholder="请输入模板名称"
          clearable
          style="width: 200px;"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="模板编码" prop="templateCode">
        <el-input
          v-model="queryParams.templateCode"
          placeholder="请输入模板编码"
          clearable
          style="width: 200px;"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="请选择状态"
          clearable
          style="width: 150px;"
        >
          <el-option
            v-for="dict in statusDict"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 工具栏（若依标准） -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['express:template:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          @click="handleEdit"
          v-hasPermi="['express:template:edit']"
          :disabled="single"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          @click="handleDelete"
          v-hasPermi="['express:template:remove']"
          :disabled="multiple"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-view"
          size="mini"
          @click="handlePreview"
          v-hasPermi="['express:template:preview']"
          :disabled="single"
        >预览</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 表格（若依标准） -->
    <el-table
      v-loading="loading"
      :data="templateList"
      @selection-change="handleSelectionChange"
      border
      fit
      highlight-current-row
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" prop="id" width="80" align="center" />
      <el-table-column label="模板名称" prop="templateName" align="center" />
      <el-table-column label="模板编码" prop="templateCode" align="center" />
      <el-table-column label="纸张尺寸" prop="paperSize" align="center" />
      <el-table-column label="适用范围" prop="scopeTypeLabel" align="center" />
      <el-table-column label="状态" prop="statusLabel" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status == '1' ? 'success' : 'danger'">
            {{ scope.row.statusLabel }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" prop="createTime" width="180" align="center" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="primary"
            icon="el-icon-view"
            @click="handlePreview(scope.row.id)"
            v-hasPermi="['express:template:preview']"
          >预览</el-button>
          <el-button
            size="mini"
            type="success"
            icon="el-icon-edit"
            @click="handleEdit(scope.row)"
            v-hasPermi="['express:template:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="warning"
            icon="el-icon-setting"
            @click="handleConfigure(scope.row)"
            v-hasPermi="['express:template:edit']"
          >配置</el-button>
          <el-button
            size="mini"
            type="danger"
            icon="el-icon-delete"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['express:template:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页（若依标准） -->
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 新增/编辑弹窗（核心：ref="templateForm" 绑定正确） -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="800px"
      append-to-body
    >
      <el-form
        ref="templateForm"
        :model="templateForm"
        :rules="rules"
        label-width="80px"
      >
        <el-row>
          <el-col :span="12">
            <el-form-item label="模板名称" prop="templateName">
              <el-input v-model="templateForm.templateName" placeholder="请输入模板名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模板编码" prop="templateCode">
              <el-input v-model="templateForm.templateCode" placeholder="请输入模板编码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="纸张尺寸" prop="paperSize">
              <el-select v-model="templateForm.paperSize" placeholder="请选择纸张尺寸">
                <el-option label="100*150mm" value="100*150mm" />
                <el-option label="80*120mm" value="80*120mm" />
                <el-option label="76*136mm" value="76*136mm" />
                <el-option label="自定义" value="custom" />
              </el-select>
              <el-input v-if="templateForm.paperSize == 'custom'" v-model="templateForm.paperSize" placeholder="请输入自定义尺寸（如110*160mm）" style="margin-top:10px;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="适用范围" prop="scopeType">
              <el-select v-model="templateForm.scopeType" placeholder="请选择适用范围">
                <el-option
                  v-for="dict in scopeTypeDict"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="企业LOGO" prop="logoUrl">
          <upload-component v-model="templateForm.logoUrl" tip="建议尺寸：80*40px" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="templateForm.status">
            <el-radio
              v-for="dict in statusDict"
              :key="dict.dictValue"
              :label="dict.dictValue"
            >{{ dict.dictLabel }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 预览弹窗 -->
    <el-dialog
      title="面单模板预览"
      :visible.sync="previewOpen"
      width="600px"
      append-to-body
    >
      <template-preview :template="previewTemplate" :order-data="previewOrderData" />
      <div slot="footer" class="dialog-footer">
        <el-button @click="previewOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 模板设计弹窗 -->
    <el-dialog
      title="模板设计"
      :visible.sync="designOpen"
      :width="isFullscreen ? '100%' : '900px'"
      :fullscreen="isFullscreen"
      append-to-body
    >
      <div class="design-container">
        <div class="form-item">
          <label class="form-label">纸张尺寸</label>
          <el-select v-model="designForm.paperSize" placeholder="请选择纸张尺寸" style="width: 200px;">
            <el-option label="100*150mm" value="100*150mm" />
            <el-option label="80*120mm" value="80*120mm" />
            <el-option label="76*136mm" value="76*136mm" />
            <el-option label="自定义" value="custom" />
          </el-select>
        </div>
        <template-design v-model="designForm.templateJson" :paperSize="designForm.paperSize" @save="submitDesignForm" />
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="toggleFullscreen">
          <i :class="isFullscreen ? 'el-icon-crop' : 'el-icon-full-screen'"></i>
          {{ isFullscreen ? '退出全屏' : '全屏' }}
        </el-button>
        <el-button type="primary" @click="submitDesignForm">保存</el-button>
        <el-button @click="designOpen = false">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listWaybillTemplate, getWaybillTemplate, delWaybillTemplate, addWaybillTemplate, updateWaybillTemplate, previewWaybillTemplate } from "@/api/express/waybillTemplate";
import TemplateDesign from "./components/TemplateDesign";
import TemplatePreview from "./components/TemplatePreview";
// 核心修改：上传组件路径改为 FileUpload
import UploadComponent from "@/components/FileUpload/index";
import { getDicts } from "@/api/system/dict/data";

export default {
  name: "WaybillTemplate",
  components: { TemplateDesign, TemplatePreview, UploadComponent },
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
      // 面单模板表格数据
      templateList: [],
      // 弹出层标题
      title: "",
      // 是否显示新增/编辑弹窗
      open: false,
      // 预览弹窗相关
      previewOpen: false,
      previewTemplate: null,
      previewOrderData: {},
      // 模板设计弹窗相关
      designOpen: false,
      isFullscreen: false,
      designForm: {
        id: null,
        templateJson: JSON.stringify({
          sender_name: { x: 10, y: 10, width: 50, height: 15, show: true },
          sender_virtual_phone: { x: 10, y: 30, width: 50, height: 15, show: true },
          order_no: { x: 10, y: 50, width: 50, height: 15, show: true },
          receiver_name: { x: 10, y: 70, width: 50, height: 15, show: true },
          receiver_virtual_phone: { x: 10, y: 90, width: 50, height: 15, show: true }
        }),
        paperSize: "100*150mm"
      },
      // 字典数据
      scopeTypeDict: [], // 适用范围字典
      statusDict: [],    // 状态字典
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        templateName: "",
        templateCode: "",
        status: ""
      },
      // 表单参数
      templateForm: {
        id: null,
        templateName: "",
        templateCode: "",
        logoUrl: "",
        templateJson: JSON.stringify({
          sender_name: { x: 10, y: 10, width: 50, height: 15, show: true },
          sender_virtual_phone: { x: 10, y: 30, width: 50, height: 15, show: true },
          order_no: { x: 10, y: 50, width: 50, height: 15, show: true },
          receiver_name: { x: 10, y: 70, width: 50, height: 15, show: true },
          receiver_virtual_phone: { x: 10, y: 90, width: 50, height: 15, show: true }
        }),
        paperSize: "100*150mm",
        scopeType: "1",
        status: "1"
      },
      // 表单校验
      rules: {
        templateName: [
          { required: true, message: "模板名称不能为空", trigger: "blur" }
        ],
        templateCode: [
          { required: true, message: "模板编码不能为空", trigger: "blur" }
        ],
        paperSize: [
          { required: true, message: "纸张尺寸不能为空", trigger: "change" }
        ],
        scopeType: [
          { required: true, message: "适用范围不能为空", trigger: "change" }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.loadDictData();
  },
  methods: {
    /** 加载字典数据 */
    loadDictData() {
      // 加载适用范围字典
      getDicts("waybill_scope_type").then(response => {
        this.scopeTypeDict = response.data;
      });
      // 加载状态字典
      getDicts("sys_normal_disable").then(response => {
        this.statusDict = response.data;
      });
    },

    /** 查询面单模板列表 */
    getList() {
      this.loading = true;
      listWaybillTemplate(this.queryParams).then(response => {
        this.templateList = response.rows;
        // 前端匹配字典标签
        this.templateList.forEach(item => {
          const scopeItem = this.scopeTypeDict.find(d => d.dictValue === item.scopeType);
          item.scopeTypeLabel = scopeItem ? scopeItem.dictLabel : item.scopeType;
          const statusItem = this.statusDict.find(d => d.dictValue === item.status);
          item.statusLabel = statusItem ? statusItem.dictLabel : item.status;
        });
        this.total = response.total;
        this.loading = false;
      });
    },

    /** 表单重置方法（增加判空，避免报错） */
    resetForm(formName) {
      if (this.$refs[formName]) {
        this.$refs[formName].resetFields();
      }
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },

    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        templateName: "",
        templateCode: "",
        status: ""
      };
      this.handleQuery();
    },

    // 多选框选中数据
    handleSelectionChange(val) {
      this.ids = val.map(item => item.id);
      this.single = val.length !== 1;
      this.multiple = val.length === 0;
    },

    /** 新增按钮操作（核心修正：先开弹窗，再重置 + 关闭预览弹窗） */
    handleAdd() {
      this.open = true; // 第一步：打开弹窗，触发DOM渲染
      this.previewOpen = false; // 兜底：关闭预览弹窗，避免误触发
      this.title = "新增面单模板";
      // 第二步：等待DOM渲染完成后重置表单
      this.$nextTick(() => {
        this.resetForm("templateForm");
        // 重置表单默认值
        this.templateForm = {
          id: null,
          templateName: "",
          templateCode: "",
          logoUrl: "",
          templateJson: JSON.stringify({
            sender_name: { x: 10, y: 10, width: 50, height: 15, show: true },
            sender_virtual_phone: { x: 10, y: 30, width: 50, height: 15, show: true },
            order_no: { x: 10, y: 50, width: 50, height: 15, show: true },
            receiver_name: { x: 10, y: 70, width: 50, height: 15, show: true },
            receiver_virtual_phone: { x: 10, y: 90, width: 50, height: 15, show: true }
          }),
          paperSize: "100*150mm",
          scopeType: "1",
          status: "1"
        };
      });
    },

    /** 修改按钮操作（核心修正：先开弹窗，再重置+回显 + 关闭预览弹窗） */
    handleEdit(row) {
      this.open = true; // 第一步：打开弹窗
      this.previewOpen = false; // 兜底：关闭预览弹窗
      this.title = "修改面单模板";
      // 第二步：等待DOM渲染完成后操作
      this.$nextTick(() => {
        this.resetForm("templateForm"); // 重置表单
        // 回显数据
        getWaybillTemplate(row.id).then(response => {
          this.templateForm = response.data;
        });
      });
    },

    /** 提交按钮 */
    submitForm() {
      this.$refs["templateForm"].validate(valid => {
        if (valid) {
          if (this.templateForm.id != null) {
            updateWaybillTemplate(this.templateForm).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addWaybillTemplate(this.templateForm).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },

    /** 取消按钮（清空校验提示 + 关闭预览弹窗） */
    cancel() {
      this.open = false;
      this.previewOpen = false; // 兜底：关闭预览弹窗
      this.$nextTick(() => {
        if (this.$refs.templateForm) {
          this.$refs.templateForm.clearValidate(); // 清空校验提示
        }
      });
    },

    /** 删除按钮操作 */
    handleDelete(id) {
      const ids = id || this.ids;
      this.$modal.confirm('是否确认删除面单模板编号为"' + ids + '"的数据项？').then(() => {
        return delWaybillTemplate(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },

    /** 预览按钮操作（核心修复：避免传入undefined + 异常捕获） */
    handlePreview(id) {
      previewWaybillTemplate(id).then(response => {
        this.previewTemplate = response.data;
        // 模拟订单数据用于预览
        this.previewOrderData = {
          orderNo: "EXP202305120001",
          senderName: "张三",
          senderPhone: "13800138000",
          senderAddress: "北京市朝阳区建国路88号",
          receiverName: "李四",
          receiverPhone: "13900139000",
          receiverAddress: "上海市浦东新区陆家嘴环路166号",
          expressType: "标准快递",
          weight: "1.5kg",
          freight: "15元"
        };
        this.previewOpen = true;
      }).catch(error => {
        // 接口报错时，关闭预览弹窗+提示
        this.previewOpen = false;
        this.$modal.msgError("预览失败：" + (error.msg || "接口请求异常"));
        console.error("预览接口报错：", error);
      });
    },

    /** 配置按钮操作 */
    handleConfigure(row) {
      this.isFullscreen = true; // 默认全屏打开
      this.designOpen = true;
      this.$nextTick(() => {
        getWaybillTemplate(row.id).then(response => {
          this.designForm = {
            id: response.data.id,
            templateJson: response.data.templateJson,
            paperSize: response.data.paperSize
          };
        });
      });
    },

    /** 提交设计表单 */
    submitDesignForm() {
      // 构建更新数据，只包含需要更新的字段
      const updateData = {
        id: this.designForm.id,
        templateJson: this.designForm.templateJson,
        paperSize: this.designForm.paperSize
      };

      updateWaybillTemplate(updateData).then(response => {
        this.$modal.msgSuccess("模板设计保存成功");
        this.designOpen = false;
        this.isFullscreen = false; // 关闭时退出全屏
        this.getList();
      }).catch(error => {
        this.$modal.msgError("保存失败：" + (error.msg || "接口请求异常"));
        console.error("保存模板设计报错：", error);
      });
    },

    /** 切换全屏状态 */
    toggleFullscreen() {
      this.isFullscreen = !this.isFullscreen;
    }
  }
};
</script>

<style scoped>
.mb8 {
  margin-bottom: 8px;
}
</style>
