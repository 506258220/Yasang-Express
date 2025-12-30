<template>
  <div class="waybill-designer">
    <!-- 顶部操作栏 -->
    <div class="top-bar">
<!--      <div style="display: flex; align-items: center;">-->
<!--        <div class="logo">汉码</div>-->
<!--        <div class="exit-btn" @click="handleExitEdit">退出编辑</div>-->
<!--      </div>-->
<!--      <div class="waybill-info">-->
<!--        中通(无框) <span class="size">76.00mm × 130.00mm</span>-->
<!--      </div>-->
      <!-- 扩展后的工具栏 -->
      <div class="top-tools">
        <!-- 基础操作 -->
        <button class="btn-default" @click="toggleCanvasLock" :disabled="false">
          {{ isCanvasLocked ? '画布解锁' : '画布锁定' }}
        </button>
        <button class="btn-default" @click="undo" :disabled="historyIndex <= 0">撤销</button>
        <button class="btn-default" @click="redo" :disabled="historyIndex >= historyStack.length - 1">恢复</button>

        <!-- 对齐操作 -->
        <button class="btn-default" @click="alignLeft" :disabled="selectedElemIds.length < 1">左对齐</button>
        <button class="btn-default" @click="alignHCenter" :disabled="selectedElemIds.length < 1">水平居中</button>
        <button class="btn-default" @click="alignRight" :disabled="selectedElemIds.length < 1">右对齐</button>
        <button class="btn-default" @click="alignTop" :disabled="selectedElemIds.length < 1">顶对齐</button>
        <button class="btn-default" @click="alignVCenter" :disabled="selectedElemIds.length < 1">垂直居中</button>
        <button class="btn-default" @click="alignBottom" :disabled="selectedElemIds.length < 1">底对齐</button>

        <!-- 分布操作 -->
        <button class="btn-default" @click="distributeHorizontal" :disabled="selectedElemIds.length < 2">水平分布</button>
        <button class="btn-default" @click="distributeVertical" :disabled="selectedElemIds.length < 2">垂直分布</button>

        <!-- 层级操作 -->
        <button class="btn-default" @click="bringToFront" :disabled="selectedElemIds.length < 1">置顶</button>
        <button class="btn-default" @click="sendToBack" :disabled="selectedElemIds.length < 1">置底</button>

        <!-- 编辑操作 -->
        <button class="btn-default" @click="copyElem" :disabled="selectedElemIds.length < 1">复制</button>
        <button class="btn-default" @click="pasteElem" :disabled="!copiedElemData">粘贴</button>
        <button class="btn-default btn-danger" @click="deleteElem" :disabled="selectedElemIds.length < 1">删除</button>

        <!-- 原有按钮 -->
        <button class="btn-primary" @click="handleLabelSetting">标签设置</button>
        <button class="btn-success" @click="handleSave">保存</button>
        <button class="btn-warning" @click="handlePrint">打印</button>
      </div>
    </div>

    <!-- 主容器：左侧元素库 + 画布 -->
    <div class="main-container">
      <!-- 左侧垂直元素库 -->
      <div class="left-element-library">
        <div
          class="element-item"
          v-for="item in elementLibrary"
          :key="item.type"
          :data-type="item.type"
          draggable="true"
          @dragstart="handleElemDragStart($event, item.type)"
          @dragover.prevent
        >
          <i class="icon" :class="`icon-${item.type}`"></i>
          <span>{{ item.name }}</span>
        </div>
      </div>

      <!-- 画布区域 -->
      <div
        class="canvas-wrapper"
        @mousedown="handleCanvasDragStart($event)"
        @mousemove="handleCanvasDragMove($event)"
        @mouseup="handleCanvasDragEnd"
        @mouseleave="handleCanvasDragEnd"
      >
        <div class="canvas-ruler">
          <!-- 画布尺码显示 -->
          <div class="canvas-size-info">76mm × 130mm</div>
          <!-- 中通面单画布 -->
          <div
            class="waybill-canvas"
            :class="{ locked: isCanvasLocked }"
            :style="{
              transform: `scale(${zoomScale}) translate(${canvasOffset.x}px, ${canvasOffset.y}px)`,
              transformOrigin: 'top left'
            }"
            @dragover.prevent
            @drop="handleElemDrop($event)"
            @click="handleCanvasClick($event)"
            ref="waybillCanvas"
          >
            <!-- 动态渲染画布元素 -->
            <div
              v-for="(elem, index) in canvasElements"
              :key="elem.id"
              class="canvas-element"
              :class="[
                `elem-${elem.type}`,
                { selected: selectedElemIds.includes(elem.id) }
              ]"
              :style="{
                  left: `${elem.x}mm`,
                  top: `${elem.y}mm`,
                  width: `${elem.width}mm`,
                  height: `${elem.height}mm`,
                  zIndex: elem.zIndex,
                  // 为斜线元素添加CSS变量
                  ...(elem.type === 'diagonal' ? {
                    '--width': `${elem.width}mm`,
                    '--height': `${elem.height}mm`,
                    '--length': `${Math.sqrt(elem.width * elem.width + elem.height * elem.height)}mm`,
                    '--angle': `${Math.atan2(elem.height, elem.width) * 180 / Math.PI}deg`
                  } : {}),
                  // 为线条元素添加CSS变量
                  ...(['diagonal', 'hline', 'vline'].includes(elem.type) ? {
                    '--line-width': `${elem.lineWidth}px`,
                    '--line-color': elem.lineColor,
                    '--line-style': elem.lineStyle
                  } : {}),
                  // 为图标元素添加CSS变量
                  ...(elem.type === 'icon' ? {
                    '--icon-type': elem.iconType
                  } : {}),
                  // 为条形码元素添加旋转属性
                  ...(elem.type === 'barcode' ? {
                    '--rotation': `${elem.rotation}deg`,
                    '--barcode-type': elem.barcodeType,
                  } : {})
              }"
              :data-index="index"
              @mousedown="handleElemMouseDown($event, elem)"
            >
              <!-- 不同类型元素内容 -->
              <template v-if="elem.type === 'text'">
                <div
                  class="content"
                  contenteditable="true"
                  :style="{
                    fontSize: `${elem.fontSize}px`,
                    color: elem.fontColor
                  }"
                  @input="handleTextInput($event, elem.id)"
                >{{ elem.content }}</div>
              </template>
              <template v-else-if="elem.type === 'time'">
                <div class="content">{{ elem.content }}</div>
              </template>
              <template v-else-if="elem.type === 'barcode'">
                <canvas :ref="`barcode-${elem.id}`" class="content"></canvas>
              </template>
              <template v-else-if="elem.type === 'qrcode'">
                <canvas :ref="`qrcode-${elem.id}`" class="content"></canvas>
              </template>
              <template v-else-if="elem.type === 'image'">
                <div class="content">{{ elem.content }}</div>
              </template>
              <template v-else-if="elem.type === 'icon'">
                <div class="content">{{ elem.content }}</div>
              </template>
              <template v-else-if="elem.type === 'diagonal'">
                <div class="content" :style="{
                  '--width': `${elem.width}px`,
                  '--height': `${elem.height}px`,
                  '--length': `${Math.sqrt(elem.width * elem.width + elem.height * elem.height)}px`,
                  '--angle': `${Math.atan2(elem.height, elem.width) * 180 / Math.PI}deg`
                }"></div>
              </template>
              <template v-else-if="elem.type === 'hline'">
                <div class="content"></div>
              </template>
              <template v-else-if="elem.type === 'vline'">
                <div class="content"></div>
              </template>

              <!-- 调整大小手柄 -->
              <div
                class="resize-handle"
                @mousedown="handleResizeStart($event, elem)"
              ></div>
            </div>
          </div>
        </div>
        <!-- 放大缩小控制按钮 -->
        <div class="zoom-controls">
          <button class="zoom-btn zoom-in" @click="zoomIn">+</button>
          <div class="zoom-level">{{ Math.round(zoomScale * 100) }}%</div>
          <button class="zoom-btn zoom-out" @click="zoomOut">-</button>
        </div>
      </div>

    <!-- 右侧属性面板 -->
    <div class="right-props-panel" v-show="selectedElemIds.length > 0">
      <div class="panel-title">元素属性</div>
      <div class="form-group">
        <label>位置X (mm)</label>
        <input
          type="number"
          v-model.number="propsForm.x"
          min="0"
          max="76"
          step="0.1"
          @change="updateElemProp('x', propsForm.x)"
        >
      </div>
      <div class="form-group">
        <label>位置Y (mm)</label>
        <input
          type="number"
          v-model.number="propsForm.y"
          min="0"
          max="130"
          step="0.1"
          @change="updateElemProp('y', propsForm.y)"
        >
      </div>
      <div class="form-group">
        <label>宽度 (mm)</label>
        <input
          type="number"
          v-model.number="propsForm.width"
          min="1"
          max="76"
          step="0.1"
          @change="updateElemProp('width', propsForm.width)"
        >
      </div>
      <div class="form-group">
        <label>高度 (mm)</label>
        <input
          type="number"
          v-model.number="propsForm.height"
          min="1"
          max="130"
          step="0.1"
          @change="updateElemProp('height', propsForm.height)"
        >
      </div>
      <!-- 通用内容属性 -->
      <div class="form-group" v-show="currentElem && ['text', 'time', 'barcode', 'qrcode', 'image', 'icon'].includes(currentElem.type)">
        <label>内容</label>
        <input
          type="text"
          v-model="propsForm.content"
          @change="updateElemProp('content', propsForm.content)"
          placeholder="输入元素内容"
        >
      </div>
      <!-- 文本专属属性 -->
      <div class="form-group text-prop" v-show="currentElem && currentElem.type === 'text'">
        <label>字体大小 (px)</label>
        <input
          type="number"
          v-model.number="propsForm.fontSize"
          min="8"
          max="72"
          @change="updateElemProp('fontSize', propsForm.fontSize)"
        >
      </div>
      <div class="form-group text-prop" v-show="currentElem && currentElem.type === 'text'">
        <label>字体颜色</label>
        <input
          type="color"
          v-model="propsForm.fontColor"
          @change="updateElemProp('fontColor', propsForm.fontColor)"
        >
      </div>
      <!-- 条形码专属属性 -->
      <div class="form-group barcode-prop" v-show="currentElem && currentElem.type === 'barcode'">
        <label>条码类型</label>
        <select
          v-model="propsForm.barcodeType"
          @change="updateElemProp('barcodeType', propsForm.barcodeType)"
        >
          <option value="CODE128">CODE128</option>
          <option value="CODE39">CODE39</option>
          <option value="EAN13">EAN13</option>
          <option value="EAN8">EAN8</option>
          <option value="UPCA">UPCA</option>
          <option value="UPCE">UPCE</option>
        </select>
      </div>
      <div class="form-group barcode-prop" v-show="currentElem && currentElem.type === 'barcode'">
        <label>数据位置</label>
        <select
          v-model="propsForm.dataPosition"
          @change="updateElemProp('dataPosition', propsForm.dataPosition)"
        >
          <option value="none">不显示</option>
          <option value="top">上方</option>
          <option value="bottom">下方</option>
        </select>
      </div>
      <div class="form-group barcode-prop" v-show="currentElem && currentElem.type === 'barcode'">
        <label>旋转</label>
        <select
          v-model="propsForm.rotation"
          @change="updateElemProp('rotation', parseInt(propsForm.rotation))"
        >
          <option value="0">0°</option>
          <option value="90">90°</option>
          <option value="180">180°</option>
          <option value="270">270°</option>
        </select>
      </div>
      <div class="form-group barcode-prop" v-show="currentElem && currentElem.type === 'barcode'">
        <label style="display: flex; align-items: center;">
          <input
            type="checkbox"
            v-model="propsForm.keepAspectRatio"
            @change="updateElemProp('keepAspectRatio', propsForm.keepAspectRatio)"
            style="margin-right: 8px;"
          >
          等比缩放
        </label>
      </div>
      <!-- 二维码专属属性 -->
      <!-- 图标专属属性 -->
      <div class="form-group icon-prop" v-show="currentElem && currentElem.type === 'icon'">
        <label>图标类型</label>
        <select
          v-model="propsForm.iconType"
          @change="updateElemProp('iconType', propsForm.iconType)"
        >
          <option value="default">默认图标</option>
          <option value="check">勾选图标</option>
          <option value="arrow">箭头图标</option>
          <option value="star">星形图标</option>
        </select>
      </div>
      <!-- 线条专属属性（斜线、水平线、垂直线） -->
      <div class="form-group line-prop" v-show="currentElem && ['diagonal', 'hline', 'vline'].includes(currentElem.type)">
        <label>线宽 (px)</label>
        <input
          type="number"
          v-model.number="propsForm.lineWidth"
          min="1"
          max="10"
          @change="updateElemProp('lineWidth', propsForm.lineWidth)"
        >
      </div>
      <div class="form-group line-prop" v-show="currentElem && ['diagonal', 'hline', 'vline'].includes(currentElem.type)">
        <label>线条颜色</label>
        <input
          type="color"
          v-model="propsForm.lineColor"
          @change="updateElemProp('lineColor', propsForm.lineColor)"
        >
      </div>
      <div class="form-group line-prop" v-show="currentElem && ['diagonal', 'hline', 'vline'].includes(currentElem.type)">
        <label>线条样式</label>
        <select
          v-model="propsForm.lineStyle"
          @change="updateElemProp('lineStyle', propsForm.lineStyle)"
        >
          <option value="solid">实线</option>
          <option value="dashed">虚线</option>
          <option value="dotted">点线</option>
        </select>
      </div>
    </div>
  </div>
  </div>
</template>

<script>
import request from '@/utils/request'

import JsBarcode from 'jsbarcode';

export default {
  name: 'WaybillDesigner',
  props: {
    value: {
      type: String,
      default: ''
    },
    paperSize: {
      type: String,
      default: '76*130mm'
    }
  },
  data() {
    return {
      // 左侧元素库
      elementLibrary: [
        { type: 'text', name: '文本' },
        { type: 'time', name: '时间' },
        { type: 'barcode', name: '条形码' },
        { type: 'qrcode', name: '二维码' },
        { type: 'image', name: '图片' },
        { type: 'shape', name: '形状' },
        { type: 'icon', name: '图标' },
        { type: 'diagonal', name: '斜线' },
        { type: 'hline', name: '水平线' },
        { type: 'vline', name: '垂直线' }
      ],
      // 画布元素列表（响应式存储）
      canvasElements: [],
      // 选中元素ID列表（支持多选）
      selectedElemIds: [],
      // 当前选中的主元素
      currentElem: null,
      // 拖拽/调整状态
      isDragging: false,
      isResizing: false,
      dragStartX: 0,
      dragStartY: 0,
      elemStartX: 0,
      elemStartY: 0,
      elemStartWidth: 0,
      elemStartHeight: 0,
      // 画布状态
      isCanvasLocked: false,
      // 缩放比例
      zoomScale: 1,
      // 画布移动状态
      isCanvasDragging: false,
      canvasOffset: { x: 0, y: 0 },
      canvasStartPos: { x: 0, y: 0 },
      // 历史记录
      historyStack: [],
      historyIndex: -1,
      // 复制的元素数据
      copiedElemData: null,
      // 属性面板表单
      propsForm: {
        x: 0,
        y: 0,
        width: 0,
        height: 0,
        fontSize: 14,
        fontColor: '#000000',
        content: '',
        // 线条属性
        lineWidth: 2,
        lineColor: '#000000',
        lineStyle: 'solid',
        // 图标属性
        iconType: 'default',
        // 条形码属性
        barcodeType: 'CODE128',
        dataPosition: 'bottom',
        rotation: 0,
        keepAspectRatio: true
      }
    }
  },
  mounted() {
    // 初始化事件监听
    this.initGlobalEvents()
    // 初始化画布数据
    this.updateCanvasData(this.value)
    // 初始化历史记录
    this.recordHistory()
    // 生成所有条形码
    this.$nextTick(() => {
      this.generateAllBarcodes()
    })
  },
  watch: {
    // 监听value变化，更新画布数据
    value: {
      handler(newValue) {
        this.updateCanvasData(newValue)
      },
      immediate: true
    }
  },
  updated() {
    // 当组件更新时重新生成条形码
    this.$nextTick(() => {
      this.generateAllBarcodes()
    })
  },
  beforeDestroy() {
    // 移除全局事件监听，防止内存泄漏
    document.removeEventListener('mousemove', this.handleGlobalMouseMove)
    document.removeEventListener('mouseup', this.handleGlobalMouseUp)
  },
  methods: {
    // ========== 基础初始化 ==========
    updateCanvasData(templateJson = this.value) {
      // 初始化画布数据
      if (templateJson) {
        try {
          const parsedData = JSON.parse(templateJson)
          // 确保canvasElements始终是一个数组
          this.canvasElements = Array.isArray(parsedData) ? parsedData : []
          console.log('画布数据初始化成功', this.canvasElements)
        } catch (error) {
          console.error('画布数据解析失败', error)
          this.canvasElements = []
        }
      } else {
        this.canvasElements = []
      }
    },
    initGlobalEvents() {
      // 全局鼠标移动事件（处理拖拽/调整大小）
      document.addEventListener('mousemove', this.handleGlobalMouseMove)
      // 全局鼠标松开事件（结束拖拽/调整大小）
      document.addEventListener('mouseup', this.handleGlobalMouseUp)
    },

    // ========== 拖拽相关 ==========
    // 左侧元素开始拖拽
    handleElemDragStart(e, type) {
      if (this.isCanvasLocked) return
      e.dataTransfer.setData('elemType', type)
      e.dataTransfer.effectAllowed = 'copy'
    },

    // 元素拖入画布
    handleElemDrop(e) {
      if (this.isCanvasLocked) return
      const elemType = e.dataTransfer.getData('elemType')
      if (!elemType) return

      // 计算鼠标在画布内的坐标（转mm）
      const canvasRect = this.$refs.waybillCanvas.getBoundingClientRect()
      const x = ((e.clientX - canvasRect.left) / canvasRect.width) * 76
      const y = ((e.clientY - canvasRect.top) / canvasRect.height) * 130

      // 创建新元素
      this.createCanvasElement(elemType, x, y)
      // 记录历史
      this.recordHistory()
    },

    // ========== 画布元素操作 ==========
    // 创建画布元素
    createCanvasElement(type, x, y, data = null) {
      // 默认属性
      const defaultProps = {
        id: data && data.id || Date.now() + Math.random().toString(36).substr(2, 9),
        type,
        x: data && data.x || Math.min(Math.max(x, 0), 76),
        y: data && data.y || Math.min(Math.max(y, 0), 130),
        width: data && data.width || this.getDefaultWidth(type),
        height: data && data.height || this.getDefaultHeight(type),
        zIndex: data && data.zIndex || 10,
        content: data && data.content || this.getDefaultContent(type),
        fontSize: data && data.fontSize || 14,
        fontColor: data && data.fontColor || '#000000',
        // 线条属性
        lineWidth: data && data.lineWidth || 2,
        lineColor: data && data.lineColor || '#000000',
        lineStyle: data && data.lineStyle || 'solid',
        // 图标属性
        iconType: data && data.iconType || 'default',
        // 条形码属性
        barcodeType: data && data.barcodeType || 'CODE128',
        dataPosition: data && data.dataPosition || 'bottom',
        rotation: data && data.rotation || 0,
        keepAspectRatio: data && data.keepAspectRatio !== undefined ? data.keepAspectRatio : true
      }

      // 添加到画布元素列表
      this.canvasElements.push(defaultProps)
      // 选中新元素（粘贴时不自动选中）
      if (!data) {
        this.selectElement(defaultProps.id)
      }

      return defaultProps
    },

    // 获取元素默认宽度
    getDefaultWidth(type) {
      switch (type) {
        case 'text': case 'time': return 50
        case 'barcode': return 60
        case 'qrcode': case 'image': case 'icon': return 30
        case 'shape': case 'diagonal': return 40
        case 'hline': return 100
        case 'vline': return 2
        default: return 50
      }
    },

    // 获取元素默认高度
    getDefaultHeight(type) {
      switch (type) {
        case 'text': case 'time': case 'barcode': return 20
        case 'qrcode': case 'image': case 'icon': return 30
        case 'shape': case 'diagonal': return 40
        case 'hline': return 2
        case 'vline': return 50
        default: return 20
      }
    },

    // 获取元素默认内容
    getDefaultContent(type) {
      switch (type) {
        case 'text': return '请输入文本'
        case 'time': return new Date().toLocaleString()
        case 'barcode': return '123456789012'
        case 'qrcode': return 'https://www.example.com'
        case 'image': return '点击上传图片'
        case 'icon': return '默认图标'
        case 'diagonal': case 'hline': case 'vline': return ''
        default: return ''
      }
    },

    // ========== 选中/取消选中 ==========
    // 选中元素
    selectElement(elemId, isMulti = false) {
      if (!isMulti) {
        this.selectedElemIds = []
      }
      if (!this.selectedElemIds.includes(elemId)) {
        this.selectedElemIds.push(elemId)
      }
      // 更新当前主元素
      this.currentElem = this.canvasElements.find(elem => elem.id === elemId)
      // 更新属性面板
      this.updatePropsForm()
    },

    // 取消所有选中
    unselectAllElements() {
      this.selectedElemIds = []
      this.currentElem = null
    },

    // 画布点击事件（选中/取消选中）
    handleCanvasClick(e) {
      if (this.isCanvasLocked) return

      // 点击调整手柄不触发选中
      if (e.target.classList.contains('resize-handle')) return

      const elemDom = e.target.closest('.canvas-element')
      if (elemDom) {
        // 获取元素ID
        const elemId = this.canvasElements[parseInt(elemDom.dataset.index)].id
        // Ctrl多选
        if (!e.ctrlKey) {
          this.unselectAllElements()
        }
        this.selectElement(elemId, true)
      } else {
        this.unselectAllElements()
      }
    },

    // ========== 拖拽/调整大小 ==========
    // 元素鼠标按下（拖拽/调整大小）
    handleElemMouseDown(e, elem) {
      if (this.isCanvasLocked) return

      // 非调整手柄时触发拖拽
      if (!e.target.classList.contains('resize-handle')) {
        e.preventDefault()
        this.isDragging = true
        // 记录拖拽起始位置
        const canvasRect = this.$refs.waybillCanvas.getBoundingClientRect()
        this.dragStartX = e.clientX
        this.dragStartY = e.clientY
        // 记录选中元素初始位置
        this.selectedElemIds.forEach(id => {
          const item = this.canvasElements.find(i => i.id === id)
          item.startX = item.x
          item.startY = item.y
        })
      }
    },

    // 调整大小开始
    handleResizeStart(e, elem) {
      if (this.isCanvasLocked || this.selectedElemIds.length !== 1) return
      e.preventDefault()
      this.isResizing = true
      this.currentElem = elem
      // 记录初始值
      const canvasRect = this.$refs.waybillCanvas.getBoundingClientRect()
      this.dragStartX = e.clientX
      this.dragStartY = e.clientY
      this.elemStartX = elem.x
      this.elemStartY = elem.y
      this.elemStartWidth = elem.width
      this.elemStartHeight = elem.height
    },

    // 全局鼠标移动（处理拖拽/调整大小）
    handleGlobalMouseMove(e) {
      // 处理元素拖拽
      if (this.isDragging && this.selectedElemIds.length > 0) {
        const canvasRect = this.$refs.waybillCanvas.getBoundingClientRect()
        // 计算移动距离（转mm）
        const deltaX = ((e.clientX - this.dragStartX) / canvasRect.width) * 76
        const deltaY = ((e.clientY - this.dragStartY) / canvasRect.height) * 130

        // 更新选中元素位置
        this.selectedElemIds.forEach(id => {
          const elem = this.canvasElements.find(i => i.id === id)
          if (elem.startX !== undefined && elem.startY !== undefined) {
            // 获取元素旋转角度
            const rotation = elem.rotation || 0
            // 当旋转90度或270度时，实际宽度和高度应该互换
            const isRotated90Or270 = [90, 270].includes(rotation)
            const actualWidth = isRotated90Or270 ? elem.height : elem.width
            const actualHeight = isRotated90Or270 ? elem.width : elem.height

            // 限制在画布内
            const newX = Math.min(Math.max(elem.startX + deltaX, 0), 76 - actualWidth)
            const newY = Math.min(Math.max(elem.startY + deltaY, 0), 130 - actualHeight)
            elem.x = Number(newX.toFixed(1))
            elem.y = Number(newY.toFixed(1))
          }
        })

        // 更新属性面板
        this.updatePropsForm()
      }

      // 处理调整大小
      if (this.isResizing && this.currentElem) {
        const canvasRect = this.$refs.waybillCanvas.getBoundingClientRect()
        // 计算调整距离（转mm）
        const deltaX = ((e.clientX - this.dragStartX) / canvasRect.width) * 76
        const deltaY = ((e.clientY - this.dragStartY) / canvasRect.height) * 130

        // 获取元素旋转角度
        const rotation = this.currentElem.rotation || 0
        // 当旋转90度或270度时，调整大小的逻辑需要特殊处理
        const isRotated90Or270 = [90, 270].includes(rotation)

        // 计算新宽高（限制范围）
        // 调整大小时，无论是否旋转，都使用原始宽高进行计算
        // 旋转会影响元素的实际占用空间，但调整大小时修改的是原始宽高
        const newWidth = Math.min(Math.max(this.elemStartWidth + deltaX, 1), 76 - this.elemStartX)
        const newHeight = Math.min(Math.max(this.elemStartHeight + deltaY, 1), 130 - this.elemStartY)

        // 更新当前元素大小
        this.currentElem.width = Number(newWidth.toFixed(1))
        this.currentElem.height = Number(newHeight.toFixed(1))

        // 更新属性面板
        this.updatePropsForm()
      }
    },

    // 全局鼠标松开（结束拖拽/调整大小）
    handleGlobalMouseUp() {
      if (this.isDragging || this.isResizing) {
        this.recordHistory() // 记录历史
      }
      this.isDragging = false
      this.isResizing = false
      // 清除起始位置记录
      this.canvasElements.forEach(elem => {
        delete elem.startX
        delete elem.startY
      })
    },

    // ========== 属性面板 ==========
    // 更新属性面板表单
    updatePropsForm() {
      if (!this.currentElem) return
      this.propsForm = {
        x: this.currentElem.x,
        y: this.currentElem.y,
        width: this.currentElem.width,
        height: this.currentElem.height,
        fontSize: this.currentElem.fontSize || 14,
        fontColor: this.currentElem.fontColor || '#000000',
        content: this.currentElem.content || '',
        // 线条属性
        lineWidth: this.currentElem.lineWidth || 2,
        lineColor: this.currentElem.lineColor || '#000000',
        lineStyle: this.currentElem.lineStyle || 'solid',
        // 图标属性
        iconType: this.currentElem.iconType || 'default',
        // 条形码属性
        barcodeType: this.currentElem.barcodeType || 'CODE128',
        dataPosition: this.currentElem.dataPosition || 'bottom',
        rotation: this.currentElem.rotation || 0,
        keepAspectRatio: this.currentElem.keepAspectRatio !== undefined ? this.currentElem.keepAspectRatio : true
      }
    },

    // 更新元素属性
    updateElemProp(prop, value) {
      if (!this.currentElem || this.isCanvasLocked) return

      // 数值格式化 - 对字符串属性特殊处理
      let val = value
      // 需要数值格式化的属性
      const numericProps = ['x', 'y', 'width', 'height', 'fontSize', 'lineWidth', 'rotation']
      if (numericProps.includes(prop)) {
        val = prop === 'lineWidth' ? Number(value) : Number(value.toFixed(1))
        // 获取元素旋转角度
        const rotation = this.currentElem.rotation || 0
        // 当旋转90度或270度时，实际宽度和高度应该互换
        const isRotated90Or270 = [90, 270].includes(rotation)
        const actualWidth = isRotated90Or270 ? this.currentElem.height : this.currentElem.width
        const actualHeight = isRotated90Or270 ? this.currentElem.width : this.currentElem.height

        // 范围限制
        switch (prop) {
          case 'x':
            val = Math.min(Math.max(val, 0), 76 - actualWidth)
            break
          case 'y':
            val = Math.min(Math.max(val, 0), 130 - actualHeight)
            break
          case 'width':
            // 当旋转90度或270度时，宽度的边界需要考虑实际高度
            const maxWidth = isRotated90Or270 ? 130 - this.currentElem.y : 76 - this.currentElem.x
            val = Math.min(Math.max(val, 1), maxWidth)
            break
          case 'height':
            // 当旋转90度或270度时，高度的边界需要考虑实际宽度
            const maxHeight = isRotated90Or270 ? 76 - this.currentElem.x : 130 - this.currentElem.y
            val = Math.min(Math.max(val, 1), maxHeight)
            break
          case 'fontSize':
            val = Math.min(Math.max(val, 8), 72)
            break
          case 'lineWidth':
            val = Math.min(Math.max(val, 1), 10)
            break
        }
      }

      // 更新元素属性
      this.$set(this.currentElem, prop, val)
      // 同步更新画布元素列表
      const index = this.canvasElements.findIndex(elem => elem.id === this.currentElem.id)
      if (index > -1) {
        this.$set(this.canvasElements, index, { ...this.currentElem })
      }
      // 记录历史
      this.recordHistory()

      // 当条形码相关属性变化时重新生成条形码
      if ((prop === 'content' || prop === 'barcodeType' || prop === 'dataPosition' || prop === 'width' || prop === 'height') && this.currentElem.type === 'barcode') {
        this.$nextTick(() => this.generateBarcode(this.currentElem))
      }
      // 当二维码相关属性变化时重新生成二维码
      if ((prop === 'content' || prop === 'width' || prop === 'height') && this.currentElem.type === 'qrcode') {
        this.$nextTick(() => this.generateQRCode(this.currentElem))
      }
    },

    // 文本内容输入
    handleTextInput(e, elemId) {
      if (this.isCanvasLocked) return
      const elem = this.canvasElements.find(i => i.id === elemId)
      if (elem) {
        elem.content = e.target.innerText
        // 如果当前选中的就是这个元素，同步更新属性面板
        if (this.currentElem && this.currentElem.id === elemId) {
          this.updatePropsForm()
        }
        this.recordHistory()
      }
    },

    // ========== 工具栏功能 ==========
    // 画布锁定/解锁
    toggleCanvasLock() {
      this.isCanvasLocked = !this.isCanvasLocked
      if (this.isCanvasLocked) {
        this.unselectAllElements()
      }
    },

    // 撤销
    undo() {
      if (this.historyIndex <= 0) return
      this.historyIndex--
      this.restoreCanvasState(this.historyStack[this.historyIndex])
      this.unselectAllElements()
    },

    // 恢复
    redo() {
      if (this.historyIndex >= this.historyStack.length - 1) return
      this.historyIndex++
      this.restoreCanvasState(this.historyStack[this.historyIndex])
      this.unselectAllElements()
    },

    // 记录历史状态
    recordHistory() {
      // 深拷贝当前画布状态
      const currentState = JSON.parse(JSON.stringify(this.canvasElements))
      // 截断撤销后的历史
      if (this.historyIndex < this.historyStack.length - 1) {
        this.historyStack = this.historyStack.slice(0, this.historyIndex + 1)
      }
      // 添加新状态
      this.historyStack.push(currentState)
      this.historyIndex = this.historyStack.length - 1
    },

    // 生成单个条形码
    generateBarcode(elem) {
      const barcodeRef = `barcode-${elem.id}`
      // Vue 2.x 中使用 $refs 获取动态引用的方法
      let barcodeCanvas
      for (const ref in this.$refs) {
        if (ref === barcodeRef) {
          barcodeCanvas = this.$refs[ref]
          break
        }
      }
      if (!barcodeCanvas) return

      // 设置canvas尺寸以匹配组件尺寸
      const canvasWidth = elem.width * 3.779528 // 转换为像素
      const canvasHeight = elem.height * 3.779528 // 转换为像素
      barcodeCanvas.width = canvasWidth
      barcodeCanvas.height = canvasHeight

      // 动态计算文字高度和大小，根据canvas尺寸调整
      const content = elem.content || '123456789012'

      // 动态设置margin，根据canvas尺寸调整，确保留白合理
      const margin = Math.max(2, Math.min(5, canvasWidth * 0.02)) // 2-5px之间，根据宽度动态调整
      const marginVertical = Math.max(2, Math.min(4, canvasHeight * 0.03)) // 垂直方向margin，稍大一些确保不会紧贴边框

      // 计算总可用高度（减去上下margin）
      const availableHeight = canvasHeight - marginVertical * 2

      // 动态计算文字高度（占可用高度的15-25%）
      const textHeightRatio = 0.2 // 文字高度占可用高度的比例
      let textHeight = 0
      let textSize = 0

      if (elem.dataPosition !== 'none') {
        // 计算文字高度，确保有足够空间
        textHeight = Math.max(10, Math.min(18, availableHeight * textHeightRatio))
        // 动态计算文字大小（与文字高度成比例）
        textSize = Math.max(7, Math.min(12, textHeight * 0.75)) // 文字大小约为文字高度的75%
      }

      // 计算条形码主体高度，确保不会超出可用高度
      const barcodeHeight = elem.dataPosition !== 'none' ?
        (availableHeight - textHeight) : availableHeight

      // 确保条形码高度至少为10px，并且总和不会超出canvas高度
      let finalBarcodeHeight = Math.max(10, Math.min(barcodeHeight, availableHeight))

      // 最后的安全检查：确保所有部分总和不超过canvas高度
      let totalHeight = marginVertical * 2 + finalBarcodeHeight + (elem.dataPosition !== 'none' ? textHeight : 0)
      if (totalHeight > canvasHeight) {
        // 如果总高度超出，按比例缩小各部分
        const scaleRatio = canvasHeight / totalHeight

        if (elem.dataPosition !== 'none') {
          // 按比例缩小文字高度和大小
          textHeight *= scaleRatio
          textSize *= scaleRatio
        }

        // 按比例缩小条形码高度
        finalBarcodeHeight = Math.max(8, finalBarcodeHeight * scaleRatio)

        // 再次检查总高度
        totalHeight = marginVertical * 2 + finalBarcodeHeight + (elem.dataPosition !== 'none' ? textHeight : 0)
      }

      // 动态计算条形码条宽度，使条形码适应canvas宽度
      const availableWidth = canvasWidth - margin * 2

      // 估算每个字符需要的条宽度数量（不同条形码类型可能不同，这里使用CODE128的估算值）
      const barsPerChar = 11 // CODE128大约每个字符11个条
      const totalBars = content.length * barsPerChar

      // 计算合适的条宽度，确保至少为0.5px
      let barWidth = Math.max(0.5, availableWidth / totalBars)

      JsBarcode(barcodeCanvas, content, {
        format: elem.barcodeType || 'CODE128',
        width: barWidth,
        height: finalBarcodeHeight,
        displayValue: elem.dataPosition !== 'none',
        textPosition: elem.dataPosition === 'top' ? 'top' : 'bottom',
        textSize: textSize,
        margin: margin,
        lineColor: "#000000",
        textMargin: marginVertical
      })
    },

    // 生成单个二维码
    generateQRCode(elem) {
      const qrcodeRef = `qrcode-${elem.id}`
      // Vue 2.x 中使用 $refs 获取动态引用的方法
      let qrcodeCanvas
      for (const ref in this.$refs) {
        if (ref === qrcodeRef) {
          qrcodeCanvas = this.$refs[ref]
          break
        }
      }
      if (!qrcodeCanvas) return

      // 处理可能的数组情况（Vue 2中动态refs可能返回数组）
      if (Array.isArray(qrcodeCanvas)) {
        qrcodeCanvas = qrcodeCanvas[0]
      }

      // 确保qrcodeCanvas是一个Canvas元素
      if (!(qrcodeCanvas instanceof HTMLCanvasElement)) {
        console.error('qrcodeCanvas is not a Canvas element:', qrcodeCanvas)
        return
      }

      // 设置canvas尺寸以匹配组件尺寸
      const canvasWidth = elem.width * 3.779528 // 转换为像素
      const canvasHeight = elem.height * 3.779528 // 转换为像素
      qrcodeCanvas.width = canvasWidth
      qrcodeCanvas.height = canvasHeight

      // 清空canvas
      const ctx = qrcodeCanvas.getContext('2d')
      ctx.clearRect(0, 0, canvasWidth, canvasHeight)

      // 生成二维码
      require('qrcode').toCanvas(qrcodeCanvas, elem.content || 'https://www.example.com', {
        width: canvasWidth,
        margin: 2,
        color: {
          dark: '#000000',
          light: '#ffffff'
        }
      }, (error) => {
        if (error) console.error('二维码生成失败:', error)
      })
    },
    // 生成所有条形码和二维码
    generateAllBarcodes() {
      this.canvasElements.forEach(elem => {
        if (elem.type === 'barcode') {
          this.generateBarcode(elem)
        } else if (elem.type === 'qrcode') {
          this.generateQRCode(elem)
        }
      })
    },

    // 恢复画布状态
    restoreCanvasState(state) {
      this.canvasElements = JSON.parse(JSON.stringify(state))
    },

    // 左对齐
    alignLeft() {
      if (this.selectedElemIds.length < 1) return
      const firstElem = this.canvasElements.find(elem => elem.id === this.selectedElemIds[0])
      const targetX = firstElem.x
      this.selectedElemIds.forEach(id => {
        const elem = this.canvasElements.find(i => i.id === id)
        elem.x = targetX
      })
      this.recordHistory()
    },

    // 水平居中
    alignHCenter() {
      if (this.selectedElemIds.length < 1) return
      const canvasCenterX = 76 / 2
      this.selectedElemIds.forEach(id => {
        const elem = this.canvasElements.find(i => i.id === id)
        elem.x = canvasCenterX - elem.width / 2
      })
      this.recordHistory()
    },

    // 右对齐
    alignRight() {
      if (this.selectedElemIds.length < 1) return
      const firstElem = this.canvasElements.find(elem => elem.id === this.selectedElemIds[0])
      const targetX = firstElem.x + firstElem.width
      this.selectedElemIds.forEach(id => {
        const elem = this.canvasElements.find(i => i.id === id)
        elem.x = targetX - elem.width
      })
      this.recordHistory()
    },

    // 顶对齐
    alignTop() {
      if (this.selectedElemIds.length < 1) return
      const firstElem = this.canvasElements.find(elem => elem.id === this.selectedElemIds[0])
      const targetY = firstElem.y
      this.selectedElemIds.forEach(id => {
        const elem = this.canvasElements.find(i => i.id === id)
        elem.y = targetY
      })
      this.recordHistory()
    },

    // 垂直居中
    alignVCenter() {
      if (this.selectedElemIds.length < 1) return
      const canvasCenterY = 130 / 2
      this.selectedElemIds.forEach(id => {
        const elem = this.canvasElements.find(i => i.id === id)
        elem.y = canvasCenterY - elem.height / 2
      })
      this.recordHistory()
    },

    // 底对齐
    alignBottom() {
      if (this.selectedElemIds.length < 1) return
      const firstElem = this.canvasElements.find(elem => elem.id === this.selectedElemIds[0])
      const targetY = firstElem.y + firstElem.height
      this.selectedElemIds.forEach(id => {
        const elem = this.canvasElements.find(i => i.id === id)
        elem.y = targetY - elem.height
      })
      this.recordHistory()
    },

    // 水平分布
    distributeHorizontal() {
      if (this.selectedElemIds.length < 2) return
      // 按X坐标排序
      const sortedElems = this.selectedElemIds
        .map(id => this.canvasElements.find(i => i.id === id))
        .sort((a, b) => a.x - b.x)
      // 计算间距
      const totalWidth = sortedElems[sortedElems.length - 1].x - sortedElems[0].x
      const gap = totalWidth / (sortedElems.length - 1)
      // 重新分布
      sortedElems.forEach((elem, index) => {
        elem.x = sortedElems[0].x + gap * index
      })
      this.recordHistory()
    },

    // 垂直分布
    distributeVertical() {
      if (this.selectedElemIds.length < 2) return
      // 按Y坐标排序
      const sortedElems = this.selectedElemIds
        .map(id => this.canvasElements.find(i => i.id === id))
        .sort((a, b) => a.y - b.y)
      // 计算间距
      const totalHeight = sortedElems[sortedElems.length - 1].y - sortedElems[0].y
      const gap = totalHeight / (sortedElems.length - 1)
      // 重新分布
      sortedElems.forEach((elem, index) => {
        elem.y = sortedElems[0].y + gap * index
      })
      this.recordHistory()
    },

    // 置顶
    bringToFront() {
      if (this.selectedElemIds.length < 1) return
      const maxZIndex = Math.max(...this.canvasElements.map(elem => elem.zIndex)) + 1
      this.selectedElemIds.forEach(id => {
        const elem = this.canvasElements.find(i => i.id === id)
        elem.zIndex = maxZIndex
      })
      this.recordHistory()
    },

    // 置底
    sendToBack() {
      if (this.selectedElemIds.length < 1) return
      const minZIndex = Math.min(...this.canvasElements.map(elem => elem.zIndex)) - 1
      this.selectedElemIds.forEach(id => {
        const elem = this.canvasElements.find(i => i.id === id)
        elem.zIndex = minZIndex
      })
      this.recordHistory()
    },

    // 复制元素
    copyElem() {
      if (this.selectedElemIds.length < 1) return
      // 深拷贝选中的第一个元素（仅支持复制单个）
      const copyElem = this.canvasElements.find(elem => elem.id === this.selectedElemIds[0])
      this.copiedElemData = JSON.parse(JSON.stringify(copyElem))
    },

    // 粘贴元素
    pasteElem() {
      if (!this.copiedElemData) return
      // 偏移粘贴（避免重叠）
      const newData = {
        ...this.copiedElemData,
        id: Date.now() + Math.random().toString(36).substr(2, 9),
        x: Math.min(this.copiedElemData.x + 5, 76 - this.copiedElemData.width),
        y: Math.min(this.copiedElemData.y + 5, 130 - this.copiedElemData.height)
      }
      this.createCanvasElement(newData.type, newData.x, newData.y, newData)
      this.recordHistory()
    },

    // 删除元素
    deleteElem() {
      if (this.selectedElemIds.length < 1) return
      this.canvasElements = this.canvasElements.filter(elem => !this.selectedElemIds.includes(elem.id))
      this.unselectAllElements()
      this.recordHistory()
    },

    // ========== 其他按钮事件 ==========
    handleExitEdit() {
      // 退出编辑逻辑（可自行扩展）
      console.log('退出编辑')
    },
    handleLabelSetting() {
      // 标签设置逻辑（可自行扩展）
      console.log('标签设置')
    },
    handleSave() {
      // 保存逻辑
      console.log('保存面单配置', this.canvasElements)

      // 准备保存数据
      const saveData = {
        templateJson: JSON.stringify(this.canvasElements),
        paperSize: this.paperSize
      }

      // 通知父组件更新数据并保存
      this.$emit('input', saveData.templateJson)
      this.$emit('save', saveData)
    },
    // 放大画布
    zoomIn() {
      if (this.zoomScale < 2) {
        this.zoomScale += 0.1
      }
    },

    // 缩小画布
    zoomOut() {
      if (this.zoomScale > 0.5) {
        this.zoomScale -= 0.1
      }
    },

    // 画布拖拽开始
    handleCanvasDragStart(e) {
      // 只有在空白区域点击才触发画布拖拽
      if (e.target.classList.contains('canvas-wrapper') || e.target.classList.contains('canvas-ruler')) {
        this.isCanvasDragging = true
        this.canvasStartPos = {
          x: e.clientX,
          y: e.clientY
        }
      }
    },

    // 画布拖拽移动
    handleCanvasDragMove(e) {
      if (this.isCanvasDragging) {
        const deltaX = e.clientX - this.canvasStartPos.x
        const deltaY = e.clientY - this.canvasStartPos.y

        // 考虑缩放比例，确保拖动距离与屏幕实际移动一致
        const scaleFactor = 1 / this.zoomScale
        this.canvasOffset.x += deltaX * scaleFactor
        this.canvasOffset.y += deltaY * scaleFactor

        this.canvasStartPos = {
          x: e.clientX,
          y: e.clientY
        }
      }
    },

    // 画布拖拽结束
    handleCanvasDragEnd() {
      this.isCanvasDragging = false
    },

    handlePrint() {
      // 打印逻辑
      console.log('打印面单')

      // 创建一个新的打印窗口
      const printWindow = window.open('', '_blank')

      // 获取画布元素的HTML内容
      const waybillCanvas = this.$refs.waybillCanvas
      const canvasHtml = waybillCanvas.outerHTML

      // 获取纸张尺寸
      const [width, height] = this.paperSize.split('*').map(size => size.replace('mm', ''))

      // 设置打印窗口的内容
      printWindow.document.write(`
        <!DOCTYPE html>
        <html>
        <head>
          <title>打印面单</title>
          <style>
            /* 打印样式 */
            @media print {
              body {
                margin: 0;
                padding: 0;
                width: ${width}mm;
                height: ${height}mm;
                overflow: hidden;
              }
              .waybill-canvas {
                width: ${width}mm !important;
                height: ${height}mm !important;
                transform: none !important;
                position: relative;
                border: 1px solid #000;
              }
              /* 隐藏编辑时的选中样式 */
              .canvas-element.selected {
                outline: none !important;
              }
            }
            /* 预览样式 */
            body {
              margin: 20px;
              font-family: Arial, sans-serif;
            }
            .waybill-canvas {
              width: ${width}mm;
              height: ${height}mm;
              border: 1px solid #000;
              margin: 0 auto;
            }
          </style>
        </head>
        <body>
          ${canvasHtml}
        </body>
        </html>
      `)

      // 关闭文档流
      printWindow.document.close()

      // 延迟打印，确保内容加载完成
      setTimeout(() => {
        printWindow.print()
        printWindow.close()
      }, 500)
    }
  }
}
</script>
<style scoped lang="scss">
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: "Microsoft Yahei", sans-serif;
}

.waybill-designer {
  width: 97vw;
  height: 100vh;
  overflow: hidden;
  box-sizing: border-box;
}

/* 顶部操作栏 */
.top-bar {
  height: auto;
  min-height: 40px;
  line-height: normal;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  align-items: center;
  padding: 8px 15px;
  border-bottom: 1px solid #e4e7ed;
  background: #fff;
  gap: 10px;
}

.top-bar .logo {
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

.top-bar .exit-btn {
  color: #666;
  cursor: pointer;
  margin-left: 20px;
}

.top-bar .waybill-info {
  font-size: 14px;
  color: #666;
  white-space: nowrap;
}

.top-bar .waybill-info .size {
  margin-left: 10px;
  font-size: 12px;
}

/* 工具栏按钮样式 */
.top-bar .top-tools {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  align-items: center;
}

.top-bar .top-tools button {
  padding: 4px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  transition: all 0.2s;
}

.top-bar .top-tools button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.top-bar .top-tools .btn-primary {
  background: #409eff;
  color: #fff;
}

.top-bar .top-tools .btn-success {
  background: #67c23a;
  color: #fff;
}

.top-bar .top-tools .btn-warning {
  background: #e6a23c;
  color: #fff;
}

.top-bar .top-tools .btn-default {
  background: #f5f7fa;
  color: #666;
  border: 1px solid #e4e7ed;
}

.top-bar .top-tools .btn-default:hover {
  background: #e4e7ed;
}

.top-bar .top-tools .btn-danger {
  background: #f56c6c;
  color: #fff;
}

/* 主容器 */
.main-container {
  display: flex;
  height: calc(100vh - 60px);
  padding: 10px;
  background: #f5f7fa;
  background-image:
    linear-gradient(rgba(200, 200, 200, 0.5) 1px, transparent 1px),
    linear-gradient(90deg, rgba(200, 200, 200, 0.5) 1px, transparent 1px);
  background-size: 5mm 5mm;
  background-position: -1px -1px;
  overflow: hidden;
  box-sizing: border-box;
}

/* 左侧垂直元素库 */
.left-element-library {
  width: 50px;
  display: flex;
  flex-direction: column;
  gap: 10px;
  background: #fff;
  border-radius: 4px;
  padding: 10px 0;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.left-element-library .element-item {
  width: 100%;
  height: 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  cursor: move;
  font-size: 12px;
  color: #666;
  transition: background 0.2s;
}

.left-element-library .element-item:hover {
  background: #f5f7fa;
}

.left-element-library .element-item i {
  font-size: 16px;
  margin-bottom: 2px;
}

/* 画布容器 */
.canvas-wrapper {
  flex: 1;
  margin: 0 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  padding-top: 20px;
  overflow: auto;
  cursor: grab;
  position: relative;
}

.canvas-wrapper:active {
  cursor: grabbing;
}

/* 标尺样式 */
.canvas-ruler {
  position: relative;
  width: 76mm;
  height: 130mm;
}



/* 画布尺码显示 */
.canvas-size-info {
  position: absolute;
  top: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.7);
  color: #fff;
  padding: 2px 6px;
  font-size: 10px;
  border-radius: 0 0 0 4px;
  z-index: 20;
}

/* 放大缩小控制按钮 */
.zoom-controls {
  position: absolute;
  bottom: 20px;
  right: 20px;
  background: #fff;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  display: flex;
  align-items: center;
  padding: 4px;
  gap: 4px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.15);
  z-index: 100;
}

.zoom-btn {
  width: 24px;
  height: 24px;
  border: 1px solid #dcdfe6;
  background: #fff;
  cursor: pointer;
  font-size: 14px;
  font-weight: bold;
  color: #606266;
  border-radius: 2px;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: all 0.2s;
}

.zoom-btn:hover {
  background: #f5f7fa;
  color: #409eff;
  border-color: #c6e2ff;
}

.zoom-level {
  padding: 0 8px;
  font-size: 12px;
  color: #606266;
  min-width: 40px;
  text-align: center;
}

/* 面单画布（76×130mm） */
.waybill-canvas {
  width: 76mm;
  height: 130mm;
  background: #fff;
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
  position: relative;
  overflow: hidden;
}

/* 画布锁定样式 */
.waybill-canvas.locked {
  pointer-events: none;
  position: relative;
}

.waybill-canvas.locked::after {
  content: "画布已锁定";
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: rgba(0, 0, 0, 0.5);
  color: #fff;
  padding: 10px 20px;
  border-radius: 4px;
  font-size: 14px;
  z-index: 999;
}

/* 画布元素样式 */
.canvas-element {
  position: absolute;
  border: 1px dashed transparent;
  cursor: move;
  z-index: 10;
}

.canvas-element.selected {
  border-color: #409eff;
}

.canvas-element .resize-handle {
  position: absolute;
  right: 0;
  bottom: 0;
  width: 10px;
  height: 10px;
  background: #409eff;
  cursor: se-resize;
  border-radius: 0 0 0 2px;
  display: none;
}

.canvas-element.selected .resize-handle {
  display: block;
}

/* 不同元素的占位样式 */
.elem-text {
  width: 50mm;
  height: 20mm;
  padding: 2px;
}

.elem-text .content {
  width: 100%;
  height: 100%;
  outline: none;
  font-size: 14px;
  color: #000;
}

.elem-time {
  width: 50mm;
  height: 20mm;
  padding: 2px;
  font-size: 14px;
  color: #000;
  display: flex;
  align-items: center;
}

.elem-barcode {
  width: 60mm;
  height: 20mm;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 12px;
  color: #666;
  position: relative;
  transform: rotate(var(--rotation, 0deg));
  transform-origin: center center;
}

/* 条形码数据位置样式 */
.canvas-element.elem-barcode[style*="--data-position: none"] .content { display: none; }

.canvas-element.elem-barcode[style*="--data-position: top"] .content {
  position: absolute;
  bottom: 100%;
  left: 50%;
  transform: translateX(-50%);
  margin-bottom: 5px;
}

.canvas-element.elem-barcode[style*="--data-position: bottom"] .content {
  position: absolute;
  top: 100%;
  left: 50%;
  transform: translateX(-50%);
  margin-top: 5px;
}

.elem-qrcode {
  width: 30mm;
  height: 30mm;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 12px;
  color: #666;
}

.elem-image {
  width: 30mm;
  height: 20mm;
  border: 1px dashed #ccc;
  display: flex;
  justify-content: center;
  align-items: center;
  color: #999;
  font-size: 12px;
}

.elem-shape {
  width: 40mm;
  height: 20mm;
  border: 1px solid #000;
  background: transparent;
}

.elem-icon {
  width: 30mm;
  height: 30mm;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 24px;
  color: #333;
  cursor: pointer;
}

.elem-diagonal {
  width: 40mm;
  height: 40mm;
  position: relative;
  background: transparent;
  cursor: pointer;
  overflow: hidden;
}

.elem-diagonal::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  /* 使用CSS变量设置对角线长度、角度、宽度、颜色和样式 */
  width: var(--length);
  height: var(--line-width);
  background: var(--line-color);
  transform: rotate(var(--angle));
  transform-origin: 0 0;
  /* 设置线条样式 */
  border-top-style: var(--line-style);
}

.elem-hline {
  width: 100mm;
  height: 10mm;
  border-top: var(--line-width) var(--line-style) var(--line-color);
  background: transparent;
  cursor: pointer;
}

.elem-vline {
  width: 10mm;
  height: 50mm;
  border-left: var(--line-width) var(--line-style) var(--line-color);
  background: transparent;
  cursor: pointer;
}

/* 图标元素样式 */
.elem-icon {
  width: 30mm;
  height: 30mm;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 24px;
  color: #333;
  cursor: pointer;
}

/* 根据图标类型显示不同的内容 */
.elem-icon .content::before {
  content: '';
  display: inline-block;
  width: 24px;
  height: 24px;
  background-size: contain;
  background-repeat: no-repeat;
  background-position: center;
}

/* 默认图标 */
.elem-icon .content::before {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1024 1024'%3E%3Cpath d='M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64zm0 820c-205.4 0-372-166.6-372-372s166.6-372 372-372 372 166.6 372 372-166.6 372-372 372z'/%3E%3Cpath d='M512 160c-193.3 0-352 158.7-352 352s158.7 352 352 352 352-158.7 352-352-158.7-352-352-352zm0 640c-158.7 0-288-129.3-288-288s129.3-288 288-288 288 129.3 288 288-129.3 288-288 288z'/%3E%3Cpath d='M512 320c-105.9 0-192 86.1-192 192s86.1 192 192 192 192-86.1 192-192-86.1-192-192-192zm0 320c-69.1 0-126.7-57.6-126.7-128s57.6-128 126.7-128 126.7 57.6 126.7 128-57.6 128-126.7 128z'/%3E%3C/svg%3E");
}

/* 勾选图标 */
.elem-icon[style*="--icon-type: check"] .content::before {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1024 1024'%3E%3Cpath d='M912 190h-69.9c-9.8 0-19.1 4.5-25.1 12.2L404.7 724.5 207 474a32 32 0 0 0-25.1-12.2H112c-6.7 0-10.4 7.7-6.3 12.9l273.9 347c12.8 16.2 37.4 16.2 50.3 0l488.4-618.9c4.1-5.1.4-12.8-6.3-12.8z'/%3E%3C/svg%3E");
}

/* 箭头图标 */
.elem-icon[style*="--icon-type: arrow"] .content::before {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1024 1024'%3E%3Cpath d='M869 487.8L491.2 159.9c-2.9-2.5-6.6-3.9-10.5-3.9h-88.5c-7.4 0-10.8 9.2-5.2 13.8L759 512 386.7 854.3c-5.6 4.6-2.2 13.8 5.2 13.8h88.5c3.9 0 7.6-1.4 10.5-3.9l377.8-327.8c2.8-2.4 4.4-5.5 4.4-8.9 0-3.4-1.5-6.5-4.4-8.9z'/%3E%3C/svg%3E");
}

/* 星形图标 */
.elem-icon[style*="--icon-type: star"] .content::before {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1024 1024'%3E%3Cpath d='M908.1 353.1l-253.9-36.9L540.7 86.1c-3.1-6.3-8.2-11.4-14.5-14.5-15.8-7.8-35-1.3-42.9 14.5L369.8 316.2l-253.9 36.9c-7 1-13.4 4.3-18.3 9.3a32.05 32.05 0 0 0 .6 45.3l183.7 179.1-43.4 252.9a31.95 31.95 0 0 0 46.4 33.7L512 829.3l227.1 119.4c6.2 3.3 13.4 4.4 20.3 3.2 17.4-3 29.1-19.5 26.1-36.9l-43.4-252.9 183.7-179.1c5-4.9 8.3-11.3 9.3-18.3 2.7-17.5-9.5-33.7-27-36.4z'/%3E%3C/svg%3E");
}

/* 右侧属性面板 */
.right-props-panel {
  width: 300px;
  max-width: 300px;
  border-left: 1px solid #e4e7ed;
  background: #fff;
  padding: 16px;
  overflow-y: auto;
  box-shadow: -2px 0 12px rgba(0, 0, 0, 0.05);
  flex-shrink: 0;
  box-sizing: border-box;
}

.right-props-panel .panel-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 10px;
  color: #333;
}

.right-props-panel .form-group {
  margin-bottom: 16px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.right-props-panel .form-group label {
  width: 100%;
  font-size: 14px;
  color: #666;
  margin-bottom: 6px;
  font-weight: 500;
}

.right-props-panel .form-group input,
.right-props-panel .form-group select {
  width: 100%;
  padding: 6px 10px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  font-size: 14px;
  box-sizing: border-box;
}

.right-props-panel .form-group input[type="number"] {
  width: 100%;
}

/* 模拟图标字体 */
.icon {
  display: inline-block;
  width: 16px;
  height: 16px;
  background: #666;
  mask-size: contain;
  -webkit-mask-size: contain;
  mask-repeat: no-repeat;
  -webkit-mask-repeat: no-repeat;
}

.icon-text {
  mask-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1024 1024'%3E%3Cpath d='M872 794H152c-14.2 0-26-11.8-26-26V152c0-14.2 11.8-26 26-26h720c14.2 0 26 11.8 26 26v616c0 14.2-11.8 26-26 26zM299.9 345.3h424.2c2.9 0 5.6 1.6 7 4.4l86 128.8c1.4 2.1 1.7 4.9.8 7.2a6.5 6.5 0 0 1-5.9 3.7H210.7c-3.7 0-7-2.7-7.5-6.4a7.3 7.3 0 0 1 6.7-8.7l85.7-128.4c1.4-2.2 4.1-3.8 7-3.8z'/%3E%3C/svg%3E");
  -webkit-mask-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1024 1024'%3E%3Cpath d='M872 794H152c-14.2 0-26-11.8-26-26V152c0-14.2 11.8-26 26-26h720c14.2 0 26 11.8 26 26v616c0 14.2-11.8 26-26 26zM299.9 345.3h424.2c2.9 0 5.6 1.6 7 4.4l86 128.8c1.4 2.1 1.7 4.9.8 7.2a6.5 6.5 0 0 1-5.9 3.7H210.7c-3.7 0-7-2.7-7.5-6.4a7.3 7.3 0 0 1 6.7-8.7l85.7-128.4c1.4-2.2 4.1-3.8 7-3.8z'/%3E%3C/svg%3E");
}

.icon-time {
  mask-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1024 1024'%3E%3Cpath d='M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64zm0 820c-205.4 0-372-166.6-372-372s166.6-372 372-372 372 166.6 372 372-166.6 372-372 372z'/%3E%3Cpath d='M689.7 628.9L544.4 746.8V416h80c4.4 0 8-3.6 8-8v-64c0-4.4-3.6-8-8-8h-160c-4.4 0-8 3.6-8 8v392c0 4.4 3.6 8 8 8h64c4.4 0 8-3.6 8-8v-180.9l129.3 103.4c7.3 5.8 18.7 1.1 18.7-8.5v-140.8c0-9.7-11.4-14.3-18.7-8.5z'/%3E%3C/svg%3E");
  -webkit-mask-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1024 1024'%3E%3Cpath d='M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64zm0 820c-205.4 0-372-166.6-372-372s166.6-372 372-372 372 166.6 372 372-166.6 372-372 372z'/%3E%3Cpath d='M689.7 628.9L544.4 746.8V416h80c4.4 0 8-3.6 8-8v-64c0-4.4-3.6-8-8-8h-160c-4.4 0-8 3.6-8 8v392c0 4.4 3.6 8 8 8h64c4.4 0 8-3.6 8-8v-180.9l129.3 103.4c7.3 5.8 18.7 1.1 18.7-8.5v-140.8c0-9.7-11.4-14.3-18.7-8.5z'/%3E%3C/svg%3E");
}

.icon-barcode {
  mask-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1024 1024'%3E%3Cpath d='M880 512h-56c-4.4 0-8 3.6-8 8v400c0 4.4 3.6 8 8 8h56c4.4 0 8-3.6 8-8V520c0-4.4-3.6-8-8-8zm-160 0h-56c-4.4 0-8 3.6-8 8v400c0 4.4 3.6 8 8 8h56c4.4 0 8-3.6 8-8V520c0-4.4-3.6-8-8-8zm-160 0h-56c-4.4 0-8 3.6-8 8v400c0 4.4 3.6 8 8 8h56c4.4 0 8-3.6 8-8V520c0-4.4-3.6-8-8-8zm-160 0h-56c-4.4 0-8 3.6-8 8v400c0 4.4 3.6 8 8 8h56c4.4 0 8-3.6 8-8V520c0-4.4-3.6-8-8-8zm-160 0h-56c-4.4 0-8 3.6-8 8v400c0 4.4 3.6 8 8 8h56c4.4 0 8-3.6 8-8V520c0-4.4-3.6-8-8-8zm-160 0h-56c-4.4 0-8 3.6-8 8v400c0 4.4 3.6 8 8 8h56c4.4 0 8-3.6 8-8V520c0-4.4-3.6-8-8-8zM144 256h56c4.4 0 8-3.6 8-8V48c0-4.4-3.6-8-8-8h-56c-4.4 0-8 3.6-8 8v200c0 4.4 3.6 8 8 8zm160 0h56c4.4 0 8-3.6 8-8V48c0-4.4-3.6-8-8-8h-56c-4.4 0-8 3.6-8 8v200c0 4.4 3.6 8 8 8zm160 0h56c4.4 0 8-3.6 8-8V48c0-4.4-3.6-8-8-8h-56c-4.4 0-8 3.6-8 8v200c0 4.4 3.6 8 8 8zm160 0h56c4.4 0 8-3.6 8-8V48c0-4.4-3.6-8-8-8h-56c-4.4 0-8 3.6-8 8v200c0 4.4 3.6 8 8 8zm160 0h56c4.4 0 8-3.6 8-8V48c0-4.4-3.6-8-8-8h-56c-4.4 0-8 3.6-8 8v200c0 4.4 3.6 8 8 8zm160 0h56c4.4 0 8-3.6 8-8V48c0-4.4-3.6-8-8-8h-56c-4.4 0-8 3.6-8 8v200c0 4.4 3.6 8 8 8z'/%3E%3C/svg%3E");
  -webkit-mask-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1024 1024'%3E%3Cpath d='M880 512h-56c-4.4 0-8 3.6-8 8v400c0 4.4 3.6 8 8 8h56c4.4 0 8-3.6 8-8V520c0-4.4-3.6-8-8-8zm-160 0h-56c-4.4 0-8 3.6-8 8v400c0 4.4 3.6 8 8 8h56c4.4 0 8-3.6 8-8V520c0-4.4-3.6-8-8-8zm-160 0h-56c-4.4 0-8 3.6-8 8v400c0 4.4 3.6 8 8 8h56c4.4 0 8-3.6 8-8V520c0-4.4-3.6-8-8-8zm-160 0h-56c-4.4 0-8 3.6-8 8v400c0 4.4 3.6 8 8 8h56c4.4 0 8-3.6 8-8V520c0-4.4-3.6-8-8-8zm-160 0h-56c-4.4 0-8 3.6-8 8v400c0 4.4 3.6 8 8 8h56c4.4 0 8-3.6 8-8V520c0-4.4-3.6-8-8-8zm-160 0h-56c-4.4 0-8 3.6-8 8v400c0 4.4 3.6 8 8 8h56c4.4 0 8-3.6 8-8V520c0-4.4-3.6-8-8-8zM144 256h56c4.4 0 8-3.6 8-8V48c0-4.4-3.6-8-8-8h-56c-4.4 0-8 3.6-8 8v200c0 4.4 3.6 8 8 8zm160 0h56c4.4 0 8-3.6 8-8V48c0-4.4-3.6-8-8-8h-56c-4.4 0-8 3.6-8 8v200c0 4.4 3.6 8 8 8zm160 0h56c4.4 0 8-3.6 8-8V48c0-4.4-3.6-8-8-8h-56c-4.4 0-8 3.6-8 8v200c0 4.4 3.6 8 8 8zm160 0h56c4.4 0 8-3.6 8-8V48c0-4.4-3.6-8-8-8h-56c-4.4 0-8 3.6-8 8v200c0 4.4 3.6 8 8 8zm160 0h56c4.4 0 8-3.6 8-8V48c0-4.4-3.6-8-8-8h-56c-4.4 0-8 3.6-8 8v200c0 4.4 3.6 8 8 8zm160 0h56c4.4 0 8-3.6 8-8V48c0-4.4-3.6-8-8-8h-56c-4.4 0-8 3.6-8 8v200c0 4.4 3.6 8 8 8z'/%3E%3C/svg%3E");
}

.icon-qrcode {
  mask-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1024 1024'%3E%3Cpath d='M352 368v64a32 32 0 0 1-64 0v-64a32 32 0 0 1 64 0zm128 0v64a32 32 0 0 1-64 0v-64a32 32 0 0 1 64 0zm128 0v64a32 32 0 0 1-64 0v-64a32 32 0 0 1 64 0zM352 512v64a32 32 0 0 1-64 0v-64a32 32 0 0 1 64 0zm128 0v64a32 32 0 0 1-64 0v-64a32 32 0 0 1 64 0zm128 0v64a32 32 0 0 1-64 0v-64a32 32 0 0 1 64 0zm128 0v64a32 32 0 0 1-64 0v-64a32 32 0 0 1 64 0zM352 656v64a32 32 0 0 1-64 0v-64a32 32 0 0 1 64 0zm128 0v64a32 32 0 0 1-64 0v-64a32 32 0 0 1 64 0zm128 0v64a32 32 0 0 1-64 0v-64a32 32 0 0 1 64 0zM816 208H208c-17.7 0-32 14.3-32 32v576c0 17.7 14.3 32 32 32h608c17.7 0 32-14.3 32-32V240c0-17.7-14.3-32-32-32zm-32 568H240V272h544v504z'/%3E%3C/svg%3E");
  -webkit-mask-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1024 1024'%3E%3Cpath d='M352 368v64a32 32 0 0 1-64 0v-64a32 32 0 0 1 64 0zm128 0v64a32 32 0 0 1-64 0v-64a32 32 0 0 1 64 0zm128 0v64a32 32 0 0 1-64 0v-64a32 32 0 0 1 64 0zM352 512v64a32 32 0 0 1-64 0v-64a32 32 0 0 1 64 0zm128 0v64a32 32 0 0 1-64 0v-64a32 32 0 0 1 64 0zm128 0v64a32 32 0 0 1-64 0v-64a32 32 0 0 1 64 0zm128 0v64a32 32 0 0 1-64 0v-64a32 32 0 0 1 64 0zM352 656v64a32 32 0 0 1-64 0v-64a32 32 0 0 1 64 0zm128 0v64a32 32 0 0 1-64 0v-64a32 32 0 0 1 64 0zm128 0v64a32 32 0 0 1-64 0v-64a32 32 0 0 1 64 0zM816 208H208c-17.7 0-32 14.3-32 32v576c0 17.7 14.3 32 32 32h608c17.7 0 32-14.3 32-32V240c0-17.7-14.3-32-32-32zm-32 568H240V272h544v504z'/%3E%3C/svg%3E");
}

.icon-image {
  mask-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1024 1024'%3E%3Cpath d='M940.8 192H83.2c-17.7 0-32 14.3-32 32v640c0 17.7 14.3 32 32 32h857.6c17.7 0 32-14.3 32-32V224c0-17.7-14.3-32-32-32zM832 728c0 22.1-17.9 40-40 40H232c-22.1 0-40-17.9-40-40V328c0-22.1 17.9-40 40-40h560c22.1 0 40 17.9 40 40v400zM320 472c0 30.9-25.1 56-56 56s-56-25.1-56-56 25.1-56 56-56 56 25.1 56 56zm64 208a96 96 0 1 1 0-192 96 96 0 0 1 0 192z'/%3E%3C/svg%3E");
  -webkit-mask-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1024 1024'%3E%3Cpath d='M940.8 192H83.2c-17.7 0-32 14.3-32 32v640c0 17.7 14.3 32 32 32h857.6c17.7 0 32-14.3 32-32V224c0-17.7-14.3-32-32-32zM832 728c0 22.1-17.9 40-40 40H232c-22.1 0-40-17.9-40-40V328c0-22.1 17.9-40 40-40h560c22.1 0 40 17.9 40 40v400zM320 472c0 30.9-25.1 56-56 56s-56-25.1-56-56 25.1-56 56-56 56 25.1 56 56zm64 208a96 96 0 1 1 0-192 96 96 0 0 1 0 192z'/%3E%3C/svg%3E");
}

.icon-shape {
  mask-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1024 1024'%3E%3Cpath d='M880 112H144c-17.7 0-32 14.3-32 32v736c0 17.7 14.3 32 32 32h736c17.7 0 32-14.3 32-32V144c0-17.7-14.3-32-32-32zM696 784H328c-4.4 0-8-3.6-8-8v-56c0-4.4 3.6-8 8-8h368c4.4 0 8 3.6 8 8v56c0 4.4-3.6 8-8 8zM696 624H328c-4.4 0-8-3.6-8-8v-56c0-4.4 3.6-8 8-8h368c4.4 0 8 3.6 8 8v56c0 4.4-3.6 8-8 8zM696 464H328c-4.4 0-8-3.6-8-8v-56c0-4.4 3.6-8 8-8h368c4.4 0 8 3.6 8 8v56c0 4.4-3.6 8-8 8zM696 304H328c-4.4 0-8-3.6-8-8v-56c0-4.4 3.6-8 8-8h368c4.4 0 8 3.6 8 8v56c0 4.4-3.6 8-8 8z'/%3E%3C/svg%3E");
  -webkit-mask-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 1024 1024'%3E%3Cpath d='M880 112H144c-17.7 0-32 14.3-32 32v736c0 17.7 14.3 32 32 32h736c17.7 0 32-14.3 32-32V144c0-17.7-14.3-32-32-32zM696 784H328c-4.4 0-8-3.6-8-8v-56c0-4.4 3.6-8 8-8h368c4.4 0 8 3.6 8 8v56c0 4.4-3.6 8-8 8zM696 624H328c-4.4 0-8-3.6-8-8v-56c0-4.4 3.6-8 8-8h368c4.4 0 8 3.6 8 8v56c0 4.4-3.6 8-8 8zM696 464H328c-4.4 0-8-3.6-8-8v-56c0-4.4 3.6-8 8-8h368c4.4 0 8 3.6 8 8v56c0 4.4-3.6 8-8 8zM696 304H328c-4.4 0-8-3.6-8-8v-56c0-4.4 3.6-8 8-8h368c4.4 0 8 3.6 8 8v56c0 4.4-3.6 8-8 8z'/%3E%3C/svg%3E");
}

/* 适配移动端的响应式调整 */
@media (max-width: 768px) {
  .top-bar {
    flex-direction: column;
    align-items: flex-start;
    padding: 8px 10px;
  }

  .top-bar .waybill-info {
    margin: 5px 0;
  }

  .top-bar .top-tools {
    width: 100%;
    margin-top: 5px;
    justify-content: flex-start;
    overflow-x: auto;
  }

  .main-container {
    height: calc(100vh - 120px);
    padding: 5px;
    overflow-x: auto;
  }

  .canvas-wrapper {
    margin-left: 10px;
    padding-top: 10px;
  }

  .waybill-canvas {
    transform: scale(0.8);
    transform-origin: top left;
  }

  /* 在小屏幕上右侧面板自动隐藏 */
  .right-props-panel {
    display: none;
  }
}

/* 滚动条样式优化 */
.right-props-panel::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

.right-props-panel::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.right-props-panel::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.right-props-panel::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* 元素拖拽时的临时样式 */
.canvas-element.dragging {
  opacity: 0.7;
  z-index: 999;
}

/* 多选状态样式 */
.canvas-element.selected-multiple {
  border-color: #67c23a;
}
</style>
