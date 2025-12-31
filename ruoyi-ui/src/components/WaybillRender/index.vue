<template>
  <div class="waybill-render" :style="containerStyle">
    <!-- 渲染LOGO -->
    <img v-if="template && template.logoUrl" :src="template.logoUrl" class="logo" />
    <!-- 渲染元素 -->
    <div 
      v-for="(elem, index) in elements" 
      :key="`elem-${index}`"
      class="canvas-element"
      :class="`elem-${elem.type}`"
      :style="getElementStyle(elem)"
    >
      <!-- 文本元素 -->
      <template v-if="elem.type === 'text'">
        <div 
          class="content text-content" 
          :style="{
            fontSize: `${elem.fontSize || 14}px`,
            color: elem.fontColor || '#000',
            textAlign: elem.textAlign || 'left',
            lineHeight: `${(elem.fontSize || 14) * 1.5}px`
          }"
        >{{ replacePlaceholders(elem.content) }}</div>
      </template>
      <!-- 时间元素 -->
      <template v-else-if="elem.type === 'time'">
        <div class="content time-content">{{ currentTime }}</div>
      </template>
      <!-- 条形码元素 -->
      <template v-else-if="elem.type === 'barcode'">
        <canvas :ref="`barcode-${index}`" class="content barcode-content"></canvas>
      </template>
      <!-- 二维码元素 -->
      <template v-else-if="elem.type === 'qrcode'">
        <canvas :ref="`qrcode-${index}`" class="content qrcode-content"></canvas>
      </template>
      <!-- 水平线元素 -->
      <template v-else-if="elem.type === 'hline'">
        <div class="content hline-content"></div>
      </template>
      <!-- 垂直线元素 -->
      <template v-else-if="elem.type === 'vline'">
        <div class="content vline-content"></div>
      </template>
      <!-- 图片元素 -->
      <template v-else-if="elem.type === 'image'">
        <img :src="replacePlaceholders(elem.content)" class="content image-content" />
      </template>
      <!-- 图标元素 -->
      <template v-else-if="elem.type === 'icon'">
        <div class="content icon-content"></div>
      </template>
      <!-- 形状元素 -->
      <template v-else-if="elem.type === 'shape'">
        <div class="content shape-content"></div>
      </template>
      <!-- 斜线元素 -->
      <template v-else-if="elem.type === 'diagonal'">
        <div class="content diagonal-content"></div>
      </template>
      <!-- 未知元素类型 -->
      <template v-else>
        <div class="content unknown-content">未知元素</div>
      </template>
    </div>
  </div>
</template>

<script>
import JsBarcode from 'jsbarcode';
import QRCode from 'qrcode';

export default {
  name: 'WaybillRender',
  props: {
    // 模板数据
    template: {
      type: Object,
      required: true
    },
    // 订单数据，用于替换占位符
    orderData: {
      type: Object,
      default: () => ({})
    },
    // 订单数据映射，优先级高于orderData
    orderDataMap: {
      type: Object,
      default: () => ({})
    },
    // 是否为打印模式
    printMode: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      currentTime: '',
      elements: []
    };
  },
  computed: {
    // 容器样式
    containerStyle() {
      if (!this.template || !this.template.paperSize) {
        return { width: '76mm', height: '130mm' };
      }
      const [width, height] = this.template.paperSize.split('*').map(size => size.replace('mm', ''));
      return {
        width: `${width}mm`,
        height: `${height}mm`
      };
    }
  },
  watch: {
    template: {
      handler(newVal) {
        this.parseElements();
      },
      deep: true, immediate: true
    },
    orderData: {
      handler() {
        this.updateTime();
        this.$nextTick(() => {
          this.generateBarcodesAndQRCodes();
        });
      },
      deep: true, immediate: true
    },
    orderDataMap: {
      handler() {
        this.$nextTick(() => {
          this.generateBarcodesAndQRCodes();
        });
      },
      deep: true, immediate: true
    }
  },
  mounted() {
    this.updateTime();
    this.parseElements();
    // 每秒更新时间
    this.timeInterval = setInterval(() => {
      this.updateTime();
    }, 1000);
  },
  beforeDestroy() {
    if (this.timeInterval) {
      clearInterval(this.timeInterval);
    }
  },
  methods: {
    // 更新当前时间
    updateTime() {
      const now = new Date();
      this.currentTime = now.toLocaleString('zh-CN');
    },
    // 解析模板元素
    parseElements() {
      if (!this.template || !this.template.templateJson) {
        this.elements = [];
        return;
      }
      try {
        const templateJson = JSON.parse(this.template.templateJson);
        if (Array.isArray(templateJson)) {
          this.elements = templateJson;
        } else {
          // 旧格式处理（如果需要）
          this.elements = [];
        }
        this.$nextTick(() => {
          this.generateBarcodesAndQRCodes();
        });
      } catch (error) {
        console.error('解析模板元素失败:', error);
        this.elements = [];
      }
    },
    // 获取元素样式
    getElementStyle(elem) {
      const style = {
        left: `${elem.x || 0}mm`,
        top: `${elem.y || 0}mm`,
        width: `${elem.width || 10}mm`,
        height: `${elem.height || 10}mm`,
        zIndex: elem.zIndex || 10,
        backgroundColor: 'transparent'
      };
      
      // 为非文本元素添加基础样式
      if (elem.type !== 'text') {
        style.display = 'flex';
        style.justifyContent = 'center';
        style.alignItems = 'center';
      } else {
        style.display = 'block';
      }
      
      // 为线条元素添加样式
      if (['hline', 'vline', 'diagonal'].includes(elem.type)) {
        // 使用CSS变量设置线条属性
        style['--line-width'] = `${elem.lineWidth || 1}px`;
        style['--line-color'] = elem.lineColor || '#000';
        style['--line-style'] = elem.lineStyle || 'solid';
      }
      
      // 水平线条
      if (elem.type === 'hline') {
        // 设置默认宽度和高度
        if (!elem.width) style.width = '100mm';
        if (!elem.height) style.height = '2mm';
        // 样式已通过CSS类和变量实现
      } 
      // 垂直线条
      else if (elem.type === 'vline') {
        // 设置默认宽度和高度
        if (!elem.width) style.width = '2mm';
        if (!elem.height) style.height = '50mm';
        // 样式已通过CSS类和变量实现
      } 
      // 斜线
      else if (elem.type === 'diagonal') {
        // 设置默认宽度和高度
        if (!elem.width) style.width = '40mm';
        if (!elem.height) style.height = '40mm';
        
        style.position = 'relative';
        // 使用CSS变量设置对角线属性
        const width = elem.width || 40;
        const height = elem.height || 40;
        style['--length'] = `${Math.sqrt(width * width + height * height)}mm`;
        style['--angle'] = `${Math.atan2(height, width) * 180 / Math.PI}deg`;
      }
      // 文本元素
      else if (elem.type === 'text') {
        // 设置默认宽度和高度
        if (!elem.width) style.width = '50mm';
        if (!elem.height) style.height = '20mm';
        
        // 文本样式由模板中的内联样式绑定处理
        style.padding = '2px';
      }
      // 时间元素
      else if (elem.type === 'time') {
        // 设置默认宽度和高度
        if (!elem.width) style.width = '50mm';
        if (!elem.height) style.height = '20mm';
        
        style.fontSize = `${elem.fontSize || 14}px`;
        style.color = elem.fontColor || '#000';
        style.fontFamily = 'Microsoft Yahei, sans-serif';
        style.padding = '2px';
      }
      // 形状元素
      else if (elem.type === 'shape') {
        // 设置默认宽度和高度
        if (!elem.width) style.width = '40mm';
        if (!elem.height) style.height = '40mm';
        
        style.backgroundColor = elem.fillColor || 'transparent';
        style.borderColor = elem.borderColor || '#000';
        style.borderWidth = `${elem.borderWidth || 1}px`;
        style.borderStyle = elem.borderStyle || 'solid';
        style.borderRadius = elem.borderRadius || '0';
      }
      // 图标元素
      else if (elem.type === 'icon') {
        // 设置默认宽度和高度
        if (!elem.width) style.width = '30mm';
        if (!elem.height) style.height = '30mm';
        
        style.fontSize = `${elem.fontSize || 24}px`;
        style.color = elem.color || '#000';
        // 使用CSS变量设置图标类型
        style['--icon-type'] = elem.iconType || 'default';
      }
      // 图片元素
      else if (elem.type === 'image') {
        // 设置默认宽度和高度
        if (!elem.width) style.width = '30mm';
        if (!elem.height) style.height = '30mm';
      }
      // 条形码元素
      else if (elem.type === 'barcode') {
        // 设置默认宽度和高度
        if (!elem.width) style.width = '60mm';
        if (!elem.height) style.height = '20mm';
        
        // 使用CSS变量设置旋转
        if (elem.rotation) {
          style['--rotation'] = `${elem.rotation}deg`;
        }
        
        // 确保使用绝对定位，与TemplateDesign一致
        style.position = 'absolute';
      }
      // 二维码元素
      else if (elem.type === 'qrcode') {
        // 设置默认宽度和高度
        if (!elem.width) style.width = '30mm';
        if (!elem.height) style.height = '30mm';
        
        style.position = 'relative';
        style.display = 'flex';
        style.justifyContent = 'center';
        style.alignItems = 'center';
      }
      
      return style;
    },
    // 替换占位符
    replacePlaceholders(content) {
      if (!content || typeof content !== 'string') {
        return content;
      }
      let result = content;
      // 优先使用orderDataMap
      const dataMap = { ...this.orderData, ...this.orderDataMap };
      // 支持多种占位符格式：{key} 和 {{key}}
      const placeholderPatterns = [
        (key) => `{${key}}`,
        (key) => `{{${key}}}`
      ];
      // 替换所有占位符
      for (const [key, value] of Object.entries(dataMap)) {
        for (const pattern of placeholderPatterns) {
          const placeholder = pattern(key);
          if (result.includes(placeholder)) {
            result = result.replace(new RegExp(placeholder.replace(/[.*+?^${}()|[\]\\]/g, '\\$&'), 'g'), value);
          }
        }
      }
      return result;
    },
    // 生成条形码和二维码
    generateBarcodesAndQRCodes() {
      // 生成条形码
      this.$nextTick(() => {
        this.elements.forEach((elem, index) => {
          if (elem.type === 'barcode') {
            const canvasRef = `barcode-${index}`;
            const canvas = this.$refs[canvasRef]?.[0];
            if (canvas) {
              try {
                const content = this.replacePlaceholders(elem.content || '123456789012');
                
                // 设置canvas尺寸以匹配组件尺寸（确保高DPI显示清晰）
                const canvasWidth = elem.width * 3.779528; // 转换为像素
                const canvasHeight = elem.height * 3.779528; // 转换为像素
                
                // 设置canvas的像素尺寸
                canvas.width = canvasWidth;
                canvas.height = canvasHeight;
                
                // 动态设置margin，根据canvas尺寸调整，确保留白合理
                const margin = Math.max(2, Math.min(5, canvasWidth * 0.02)); // 2-5px之间，根据宽度动态调整
                const marginVertical = Math.max(2, Math.min(4, canvasHeight * 0.03)); // 垂直方向margin，稍大一些确保不会紧贴边框
                
                // 计算总可用高度（减去上下margin）
                const availableHeight = canvasHeight - marginVertical * 2;
                
                // 动态计算文字高度（占可用高度的15-25%）
                const textHeightRatio = 0.2; // 文字高度占可用高度的比例
                let textHeight = 0;
                let textSize = 0;
                
                if (elem.dataPosition !== 'none') {
                  // 计算文字高度，确保有足够空间
                  textHeight = Math.max(10, Math.min(18, availableHeight * textHeightRatio));
                  // 动态计算文字大小（与文字高度成比例）
                  textSize = Math.max(7, Math.min(12, textHeight * 0.75)); // 文字大小约为文字高度的75%
                }
                
                // 计算条形码主体高度，确保不会超出可用高度
                const barcodeHeight = elem.dataPosition !== 'none' ? 
                  (availableHeight - textHeight) : availableHeight;
                
                // 确保条形码高度至少为10px，并且总和不会超出canvas高度
                let finalBarcodeHeight = Math.max(10, Math.min(barcodeHeight, availableHeight));
                
                // 最后的安全检查：确保所有部分总和不超过canvas高度
                let totalHeight = marginVertical * 2 + finalBarcodeHeight + (elem.dataPosition !== 'none' ? textHeight : 0);
                if (totalHeight > canvasHeight) {
                  // 如果总高度超出，按比例缩小各部分
                  const scaleRatio = canvasHeight / totalHeight;
                  
                  if (elem.dataPosition !== 'none') {
                    // 按比例缩小文字高度和大小
                    textHeight *= scaleRatio;
                    textSize *= scaleRatio;
                  }
                  
                  // 按比例缩小条形码高度
                  finalBarcodeHeight = Math.max(8, finalBarcodeHeight * scaleRatio);
                }
                
                // 动态计算条形码条宽度，使条形码适应canvas宽度
                const availableWidth = canvasWidth - margin * 2;
                
                // 估算每个字符需要的条宽度数量（不同条形码类型可能不同，这里使用CODE128的估算值）
                const barsPerChar = 11; // CODE128大约每个字符11个条
                const totalBars = content.length * barsPerChar;
                
                // 计算合适的条宽度，确保至少为0.75px以避免模糊，同时不超过2px
                let barWidth = Math.max(0.75, Math.min(2, availableWidth / totalBars));
                
                // 确保barWidth是0.25px的倍数，提高渲染清晰度
                barWidth = Math.round(barWidth * 4) / 4;
                
                // 使用与TemplateDesign一致的JsBarcode配置
                JsBarcode(canvas, content, {
                  format: elem.barcodeType || 'CODE128',
                  width: barWidth,
                  height: finalBarcodeHeight,
                  displayValue: elem.dataPosition !== 'none',
                  textPosition: elem.dataPosition === 'top' ? 'top' : 'bottom',
                  textSize: textSize,
                  margin: margin,
                  lineColor: "#000000",
                  textMargin: marginVertical
                });
              } catch (error) {
                console.error('生成条形码失败:', error);
              }
            }
          } else if (elem.type === 'qrcode') {
            const canvasRef = `qrcode-${index}`;
            const canvas = this.$refs[canvasRef]?.[0];
            if (canvas) {
              try {
                // 处理可能的数组情况
                const qrcodeCanvas = Array.isArray(canvas) ? canvas[0] : canvas;
                
                // 确保qrcodeCanvas是一个Canvas元素
                if (!(qrcodeCanvas instanceof HTMLCanvasElement)) {
                  console.error('qrcodeCanvas is not a Canvas element:', qrcodeCanvas);
                  return;
                }
                
                const content = this.replacePlaceholders(elem.content || 'https://www.example.com');
                
                // 设置canvas尺寸以匹配组件尺寸
                const canvasWidth = elem.width * 3.779528; // 转换为像素
                const canvasHeight = elem.height * 3.779528; // 转换为像素
                qrcodeCanvas.width = canvasWidth;
                qrcodeCanvas.height = canvasHeight;
                
                // 清空canvas
                const ctx = qrcodeCanvas.getContext('2d');
                ctx.clearRect(0, 0, canvasWidth, canvasHeight);
                
                QRCode.toCanvas(qrcodeCanvas, content, {
                  width: canvasWidth,
                  margin: 2,
                  color: {
                    dark: '#000000',
                    light: '#ffffff'
                  }
                }, (error) => {
                  if (error) {
                    console.error('生成二维码失败:', error);
                  }
                });
              } catch (error) {
                console.error('生成二维码失败:', error);
              }
            }
          }
        });
      });
    }
  }
};
</script>

<style scoped>
.waybill-render {
  position: relative;
  background: #fff;
  border: 1px solid #000;
  overflow: hidden;
  margin: 0 auto;
}

.logo {
  position: absolute;
  top: 5px;
  right: 5px;
  width: 30mm;
  height: 15mm;
  object-fit: contain;
}

.canvas-element {
  position: absolute;
  box-sizing: border-box;
  border: 1px dashed transparent;
  cursor: move;
  z-index: 10;
}

/* 元素样式 */
.canvas-element .content {
  overflow: hidden;
}

/* 图标元素 */
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

/* 文本元素容器 */
.elem-text {
  /* 与TemplateDesign完全一致的容器样式 */
  width: 50mm;
  height: 20mm;
  padding: 2px;
}

/* 文本元素内容 */
.elem-text .text-content {
  /* 与TemplateDesign完全一致的样式 */
  width: 100%;
  height: 100%;
  outline: none;
  color: #000;
  white-space: normal;
  word-break: break-word;
  /* 明确覆盖通用.content类的flex布局 */
  display: block;
  /* 移除flex相关属性 */
  align-items: normal;
  justify-content: normal;
}

/* 时间元素 */
.elem-time .time-content {
  font-size: 14px;
  color: #000;
  padding: 2px;
}

/* 条形码元素 */
.elem-barcode {
  width: 60mm;
  height: 20mm;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 12px;
  color: #666;
  /* 移除position: relative，避免与绝对定位冲突 */
  /* position: relative; */
  transform: rotate(var(--rotation, 0deg));
  transform-origin: center center;
}

.elem-barcode .content,
.elem-barcode .barcode-content {
  display: block;
  font-size: 0;
  line-height: 0;
}

/* 二维码元素 */
.elem-qrcode {
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 12px;
  color: #666;
  position: relative;
}

.elem-qrcode .qrcode-content {
  width: 100%;
  height: 100%;
}

/* 水平线元素 */
.elem-hline {
  border-top: var(--line-width) var(--line-style) var(--line-color);
  background: transparent;
}

.elem-hline .hline-content {
  width: 100%;
  height: 100%;
}

/* 垂直线元素 */
.elem-vline {
  border-left: var(--line-width) var(--line-style) var(--line-color);
  background: transparent;
}

.elem-vline .vline-content {
  width: 100%;
  height: 100%;
}

/* 图片元素 */
.elem-image .image-content {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

/* 形状元素 */
.elem-shape .shape-content {
  width: 100%;
  height: 100%;
  border: 1px solid #000;
  background: transparent;
  box-sizing: border-box;
}

/* 斜线元素 */
.elem-diagonal {
  position: relative;
  background: transparent;
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

.elem-diagonal .diagonal-content {
  width: 100%;
  height: 100%;
  overflow: hidden;
}

/* 未知元素 */
.elem-unknown .unknown-content {
  color: #999;
  font-size: 12px;
}

/* 打印模式样式 */
@media print {
  .waybill-render {
    box-shadow: none;
  }
}
</style>