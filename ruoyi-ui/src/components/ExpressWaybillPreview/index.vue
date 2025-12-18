<template>
  <div class="waybill-preview" ref="waybillRef">
    <!-- 空状态提示 -->
    <div class="empty-preview-state" v-show="!previewVisible">
      <i class="fa fa-file-text-o empty-icon"></i>
      <p class="empty-text-main">请点击预览按钮生成面单预览</p>
      <p class="empty-text-sub">顺丰标准面单尺寸：76×130mm</p>
    </div>

    <!-- 顺丰面单内容 -->
    <div class="sf-waybill-scaler" v-show="previewVisible" ref="printContentRef">
      <div class="sf-waybill-container" :style="waybillStyle">
        <!-- 面单头部 -->
        <div class="sf-header">
          <div class="sf-logo">
            <div class="sf-logo-main">SF</div>
            <div class="sf-logo-sub">
              <div class="sf-logo-sub-main">顺丰速运</div>
              <div class="sf-logo-sub-sub">SHUNFENG</div>
            </div>
          </div>
          <div class="sf-service">标准快递</div>
          <div class="sf-hotline">
            <i class="fa fa-phone"></i>4008 111 111
          </div>
        </div>

        <!-- 核心信息区 -->
        <div class="sf-core">
          <!-- 左侧二维码区 -->
          <div class="sf-qrcode-area">
            <div class="sf-qrcode-box">
              <div class="sf-qrcode-placeholder">
                <i class="fa fa-qrcode"></i>
              </div>
            </div>
            <div class="sf-qrcode-label">寄件联</div>
            <div class="sf-waybill-tag">国内标准快递</div>
          </div>

          <!-- 右侧条码区 -->
          <div class="sf-barcode-area">
            <div class="sf-barcode-container">
              <canvas id="dynamicBarcode" v-if="fieldsConfig.includes('waybillNo')"></canvas>
            </div>
            <div class="sf-waybill-no" v-if="fieldsConfig.includes('waybillNo')">
              {{ previewData.waybillNo || 'SF1234567890123' }}
            </div>
          </div>

          <!-- 寄件人信息区 -->
          <div class="sf-sender-area" v-if="fieldsConfig.includes('consignee') || fieldsConfig.includes('phone') || fieldsConfig.includes('address')">
            <div class="sf-section-header">寄件人</div>
            <div class="sf-info-group">
              <div class="sf-info-item" v-if="fieldsConfig.includes('consignee')">
                <span class="sf-info-label">姓名：</span>
                <span class="sf-info-value">{{ previewData.senderName || '张三' }}</span>
              </div>
              <div class="sf-info-item" v-if="fieldsConfig.includes('phone')">
                <span class="sf-info-label">电话：</span>
                <span class="sf-info-value">{{ previewData.senderPhone || '13800138000' }}</span>
              </div>
              <div class="sf-info-item" v-if="fieldsConfig.includes('address')">
                <span class="sf-info-label">地址：</span>
                <span class="sf-info-value">{{ previewData.senderAddress || '广东省深圳市南山区科技园' }}</span>
              </div>
            </div>
          </div>

          <!-- 收件人信息区 -->
          <div class="sf-receiver-area" v-if="fieldsConfig.includes('consignee') || fieldsConfig.includes('phone') || fieldsConfig.includes('address')">
            <div class="sf-section-header">收件人</div>
            <div class="sf-info-group">
              <div class="sf-info-item" v-if="fieldsConfig.includes('consignee')">
                <span class="sf-info-label">姓名：</span>
                <span class="sf-info-value">{{ previewData.consignee || '李四' }}</span>
              </div>
              <div class="sf-info-item" v-if="fieldsConfig.includes('phone')">
                <span class="sf-info-label">电话：</span>
                <span class="sf-info-value">{{ previewData.phone || '13900139000' }}</span>
              </div>
              <div class="sf-info-item" v-if="fieldsConfig.includes('address')">
                <span class="sf-info-label">地址：</span>
                <span class="sf-info-value">{{ previewData.address || '北京市海淀区中关村大街1号' }}</span>
              </div>
            </div>
          </div>

          <!-- 物品信息区 -->
          <div class="sf-goods-area" v-if="fieldsConfig.includes('goodsInfo')">
            <div class="sf-section-header">物品信息</div>
            <div class="sf-goods-info">
              <div class="sf-goods-item">
                <span class="sf-goods-label">内容：</span>
                <span class="sf-goods-value">{{ previewData.goodsInfo || '电子产品' }}</span>
              </div>
              <div class="sf-goods-item" v-if="fieldsConfig.includes('weight')">
                <span class="sf-goods-label">重量：</span>
                <span class="sf-goods-value">{{ previewData.weight || '1.0' }} kg</span>
              </div>
            </div>
          </div>

          <!-- 订单信息区 -->
          <div class="sf-order-area" v-if="fieldsConfig.includes('orderNo')">
            <div class="sf-section-header">订单信息</div>
            <div class="sf-order-info">
              <span class="sf-order-label">订单号：</span>
              <span class="sf-order-value">{{ previewData.orderNo || 'ORD202401010001' }}</span>
            </div>
          </div>
        </div>

        <!-- 底部区域 -->
        <div class="sf-footer">
          <div class="sf-footer-text">
            <div>顺丰速运 标准快递</div>
            <div>www.sf-express.com | 服务热线：4008 111 111</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import JsBarcode from 'jsbarcode';

export default {
  name: 'ExpressWaybillPreview',
  props: {
    previewVisible: {
      type: Boolean,
      default: false
    },
    fieldsConfig: {
      type: Array,
      default: () => ['waybillNo', 'consignee', 'phone', 'address', 'goodsInfo', 'weight', 'orderNo']
    },
    printConfig: {
      type: Object,
      default: () => ({ fontSize: 12, printHeader: true, printFooter: true })
    },
    previewData: {
      type: Object,
      default: () => ({})
    },
    waybillStyle: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      // 存储打印前的body样式，用于恢复
      originalBodyStyle: '',
      // 存储非面单元素的显示状态
      originalElements: []
    };
  },
  mounted() {
    this.generateDynamicBarcode();
    // 监听打印事件（兼容不同浏览器）
    this.addPrintListeners();
  },
  updated() {
    this.generateDynamicBarcode();
  },
  beforeUnmount() {
    // 移除打印事件监听
    this.removePrintListeners();
  },
  methods: {
    // 生成条形码
    generateDynamicBarcode() {
      if (!this.previewVisible || !this.fieldsConfig.includes('waybillNo')) return;

      const barcodeCanvas = document.getElementById('dynamicBarcode');
      if (!barcodeCanvas) return;

      JsBarcode(barcodeCanvas, this.previewData.waybillNo || 'SF1234567890123', {
        format: "CODE128",
        width: 2.2,
        height: 35,
        displayValue: false,
        margin: 6,
        lineColor: "#000000"
      });
    },

    // 添加打印事件监听（兼容Chrome/Firefox/Edge）
    addPrintListeners() {
      const beforePrint = () => this.handleBeforePrint();
      const afterPrint = () => this.handleAfterPrint();

      // 标准事件
      window.addEventListener('beforeprint', beforePrint);
      window.addEventListener('afterprint', afterPrint);

      // 兼容Webkit内核浏览器（Chrome/Safari）
      if (window.matchMedia) {
        const mediaQueryList = window.matchMedia('print');
        mediaQueryList.addEventListener('change', (e) => {
          if (e.matches) {
            beforePrint();
          } else {
            afterPrint();
          }
        });
      }

      // 存储监听函数，用于销毁
      this.beforePrintHandler = beforePrint;
      this.afterPrintHandler = afterPrint;
    },

    // 移除打印事件监听
    removePrintListeners() {
      if (this.beforePrintHandler) {
        window.removeEventListener('beforeprint', this.beforePrintHandler);
      }
      if (this.afterPrintHandler) {
        window.removeEventListener('afterprint', this.afterPrintHandler);
      }
      if (window.matchMedia) {
        const mediaQueryList = window.matchMedia('print');
        mediaQueryList.removeEventListener('change', () => {});
      }
    },

    // 打印前处理：只显示面单，隐藏其他所有内容
    handleBeforePrint() {
      if (!this.previewVisible) return;

      // 1. 保存body原始样式
      this.originalBodyStyle = document.body.style.cssText;

      // 2. 隐藏body下所有非面单元素
      const waybillEl = this.$refs.waybillRef;
      const bodyChildren = Array.from(document.body.children);

      this.originalElements = [];
      bodyChildren.forEach(el => {
        if (el !== waybillEl) {
          // 保存原始display状态
          this.originalElements.push({
            el,
            display: el.style.display,
            visibility: el.style.visibility
          });
          // 隐藏非面单元素
          el.style.display = 'none';
          el.style.visibility = 'hidden';
        }
      });

      // 3. 强制面单元素显示
      waybillEl.style.display = 'block';
      waybillEl.style.visibility = 'visible';
      waybillEl.style.position = 'absolute';
      waybillEl.style.top = '0';
      waybillEl.style.left = '0';
      waybillEl.style.margin = '0';
      waybillEl.style.boxShadow = 'none';
      waybillEl.style.border = 'none';

      // 4. 隐藏空状态（防止打印空状态）
      const emptyState = waybillEl.querySelector('.empty-preview-state');
      if (emptyState) {
        emptyState.style.display = 'none';
      }
    },

    // 打印后恢复：还原所有样式和显示状态
    handleAfterPrint() {
      // 1. 恢复body原始样式
      document.body.style.cssText = this.originalBodyStyle;

      // 2. 恢复非面单元素的显示状态
      this.originalElements.forEach(({ el, display, visibility }) => {
        el.style.display = display;
        el.style.visibility = visibility;
      });
      this.originalElements = [];

      // 3. 恢复面单容器样式
      const waybillEl = this.$refs.waybillRef;
      if (waybillEl) {
        waybillEl.style.display = '';
        waybillEl.style.visibility = '';
        waybillEl.style.position = '';
        waybillEl.style.top = '';
        waybillEl.style.left = '';
        waybillEl.style.margin = '';
        waybillEl.style.boxShadow = '';
        waybillEl.style.border = '';
      }
    },

    // 主动触发打印（供外部调用，比如按钮点击）
    triggerPrint() {
      if (!this.previewVisible) {
        this.$emit('print-error', '请先生成面单预览再打印');
        return;
      }
      // 延迟执行，确保DOM更新完成
      setTimeout(() => {
        window.print();
      }, 100);
    }
  }
};
</script>

<style scoped>
/* 全局颜色变量 */
:root {
  --sf-red: #D0021B;
  --gray-100: #f1f5f9;
  --gray-200: #e2e8f0;
  --gray-300: #cbd5e1;
  --gray-400: #94a3b8;
  --gray-500: #64748b;
  --gray-600: #475569;
  --white: #ffffff;
  --gray-50: #f8fafc;
}

/* 预览容器（7倍缩放：532×910px） */
.waybill-preview {
  width: 532px;
  height: 910px;
  margin: 0 auto 24px;
  background-color: var(--white);
  position: relative;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  border-radius: 10px;
  border: 1px solid var(--gray-200);
  box-sizing: border-box;
}

/* 空状态样式 */
.empty-preview-state {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: var(--white);
  z-index: 10;
  animation: fadeIn 0.3s ease forwards;
}
.empty-icon {
  font-size: 70px;
  color: var(--gray-300);
  margin-bottom: 20px;
}
.empty-text-main {
  font-size: 20px;
  color: var(--gray-500);
  margin-bottom: 10px;
}
.empty-text-sub {
  font-size: 16px;
  color: var(--gray-400);
}

/* 面单缩放容器 */
.sf-waybill-scaler {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 15px;
  box-sizing: border-box;
}

/* 顺丰面单容器 */
.sf-waybill-container {
  font-family: "Arial", "SimHei", "Microsoft YaHei", sans-serif;
  width: 100%;
  height: 100%;
  box-sizing: border-box;
  color: #000;
  animation: fadeIn 0.3s ease-out;
}

/* 面单头部 */
.sf-header {
  background-color: var(--sf-red);
  color: var(--white);
  padding: 10px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 50px;
  box-sizing: border-box;
  border-radius: 8px 8px 0 0;
}
/* SF Logo 样式 */
.sf-logo {
  display: flex;
  align-items: center;
}
.sf-logo-main {
  font-size: 36px;
  font-weight: 900;
  letter-spacing: -1.5px;
  line-height: 1;
  margin-right: 6px;
}
.sf-logo-sub {
  margin-left: 6px;
}
.sf-logo-sub-main {
  font-size: 16px;
  font-weight: 700;
  line-height: 1.2;
}
.sf-logo-sub-sub {
  font-size: 12px;
  opacity: 0.9;
  line-height: 1.2;
}
/* 头部右侧样式 */
.sf-service {
  font-size: 16px;
  font-weight: 700;
}
.sf-hotline {
  font-size: 14px;
  display: flex;
  align-items: center;
}
.sf-hotline i {
  margin-right: 6px;
  font-size: 16px;
}

/* 核心信息区网格布局 */
.sf-core {
  display: grid;
  grid-template-columns: repeat(12, 1fr);
  gap: 6px;
  padding: 10px;
  box-sizing: border-box;
  background-color: var(--white);
}

/* 左侧二维码区 */
.sf-qrcode-area {
  grid-column: span 3;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 12px 0;
}
.sf-qrcode-box {
  border: 2px solid var(--gray-300);
  padding: 6px;
  background-color: var(--white);
  width: 140px;
  height: 140px;
  border-radius: 6px;
}
.sf-qrcode-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: var(--gray-100);
  border-radius: 4px;
}
.sf-qrcode-placeholder i {
  font-size: 50px;
  color: var(--gray-400);
}
.sf-qrcode-label {
  font-size: 14px;
  color: var(--gray-500);
  margin-top: 6px;
  font-weight: 500;
}
.sf-waybill-tag {
  font-size: 12px;
  color: var(--gray-500);
  margin-top: 12px;
  transform: rotate(-90deg);
  white-space: nowrap;
  font-weight: 500;
}

/* 右侧条码区 */
.sf-barcode-area {
  grid-column: span 9;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 12px 0;
}
.sf-barcode-container {
  width: 100%;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: var(--gray-100);
  border-radius: 4px;
  margin-bottom: 6px;
}
.sf-barcode-container canvas {
  width: 90%;
  height: 100%;
}
.sf-waybill-no {
  font-size: 16px;
  font-family: monospace;
  margin-top: 6px;
  letter-spacing: 6px;
  color: var(--gray-600);
  font-weight: 500;
}

/* 信息区通用标题 */
.sf-section-header {
  background-color: var(--sf-red);
  color: var(--white);
  font-size: 16px;
  padding: 3px 10px;
  display: inline-block;
  margin-bottom: 10px;
  border-radius: 4px;
  font-weight: 600;
}

/* 寄件人/收件人信息区 */
.sf-sender-area, .sf-receiver-area {
  grid-column: span 6;
  padding: 12px;
  border-right: 2px solid var(--gray-200);
  border-bottom: 2px solid var(--gray-200);
  border-radius: 6px;
  background-color: var(--gray-50);
  margin-bottom: 6px;
}
.sf-receiver-area {
  border-right: none;
}

/* 信息组通用样式 */
.sf-info-group {
  font-size: 16px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.sf-info-item {
  display: flex;
  align-items: center;
}
.sf-info-label {
  width: 50px;
  color: var(--gray-600);
  flex-shrink: 0;
  font-weight: 600;
  margin-right: 6px;
}
.sf-info-value {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: var(--gray-800);
  font-weight: 500;
}

/* 物品信息区 */
.sf-goods-area {
  grid-column: span 12;
  padding: 12px;
  border-bottom: 2px solid var(--gray-200);
  border-radius: 6px;
  background-color: var(--gray-50);
  margin-bottom: 6px;
}
.sf-goods-info {
  font-size: 16px;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  column-gap: 12px;
}
.sf-goods-item {
  display: flex;
  align-items: center;
}
.sf-goods-label {
  width: 70px;
  color: var(--gray-600);
  flex-shrink: 0;
  font-weight: 600;
  margin-right: 6px;
}
.sf-goods-value {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: var(--gray-800);
  font-weight: 500;
}

/* 订单信息区 */
.sf-order-area {
  grid-column: span 12;
  padding: 12px;
  border-radius: 6px;
  background-color: var(--gray-50);
}
.sf-order-info {
  font-size: 16px;
  display: flex;
  align-items: center;
}
.sf-order-label {
  width: 80px;
  color: var(--gray-600);
  flex-shrink: 0;
  font-weight: 600;
  margin-right: 6px;
}
.sf-order-value {
  flex: 1;
  font-family: monospace;
  color: var(--gray-800);
  font-weight: 500;
}

/* 底部区域 */
.sf-footer {
  background-color: var(--gray-50);
  border-top: 2px solid var(--gray-200);
  padding: 10px;
  box-sizing: border-box;
  border-radius: 0 0 8px 8px;
}
.sf-footer-text {
  font-size: 14px;
  color: var(--gray-500);
  text-align: center;
  line-height: 1.4;
  font-weight: 500;
}
.sf-footer-text div:last-child {
  margin-top: 6px;
}

/* 动画效果 */
@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}
</style>

<!-- 打印专属样式：强制面单尺寸为实际76×130mm -->
<style>
@media print {
  /* 重置所有默认样式 */
  * {
    margin: 0 !important;
    padding: 0 !important;
    box-sizing: border-box !important;
  }

  /* 仅显示面单容器 */
  body > *:not(.waybill-preview) {
    display: none !important;
    visibility: hidden !important;
  }

  /* 面单容器强制为实际打印尺寸 */
  .waybill-preview {
    width: 76mm !important;
    height: 130mm !important;
    transform: none !important;
    position: absolute !important;
    top: 0 !important;
    left: 0 !important;
    box-shadow: none !important;
    border: none !important;
    page-break-after: avoid !important;
    page-break-before: avoid !important;
  }

  /* 隐藏空状态 */
  .empty-preview-state {
    display: none !important;
  }

  /* 面单内容适配打印尺寸 */
  .sf-waybill-scaler {
    padding: 0 !important;
  }
  .sf-waybill-container {
    width: 100% !important;
    height: 100% !important;
  }

  /* 打印页面设置 */
  @page {
    size: 76mm 130mm !important; /* 顺丰标准面单尺寸 */
    margin: 0 !important; /* 去除页边距 */
  }

  body {
    margin: 0 !important;
    padding: 0 !important;
    background: white !important;
  }
}
</style>
