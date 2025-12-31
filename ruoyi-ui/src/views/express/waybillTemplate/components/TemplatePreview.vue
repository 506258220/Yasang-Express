<template>
  <div class="preview-container">
    <div class="preview-header">
      <el-button type="primary" icon="el-icon-printer" @click="handlePrint">打印</el-button>
    </div>
    <div class="preview-content" ref="printArea">
      <waybill-render v-if="template" ref="waybillRender" :template="template" :order-data="orderData" />
    </div>
  </div>
</template>

<script>
import WaybillRender from '@/components/WaybillRender/index.vue';

export default {
  name: "TemplatePreview",
  components: {
    WaybillRender
  },
  props: {
    template: {
      type: Object,
      default: null,
      required: false
    },
    orderData: {
      type: Object,
      default: () => {},
      required: false
    }
  },
  methods: {
    handlePrint() {
      const waybillRender = this.$refs.waybillRender;
      const printContent = this.$refs.printArea;
      if (!printContent || !waybillRender) return;
      
      const printWindow = window.open('', '_blank');
      if (!printWindow) {
        this.$message.warning('请允许弹出窗口以进行打印');
        return;
      }
      
      const paperSize = this.template?.paperSize || '76*130mm';
      const [width, height] = paperSize.split('*').map(s => parseInt(s.replace('mm', '')));
      
      const templateJson = this.template?.templateJson ? JSON.parse(this.template.templateJson) : [];
      const orderData = this.orderData || {};
      
      function replacePlaceholders(content, data) {
        if (!content || typeof content !== 'string') return content;
        let result = content;
        Object.entries(data).forEach(([key, value]) => {
          result = result.replace(new RegExp('\\{' + key + '\\}|\\{\\{' + key + '\\}\\}', 'g'), value || '');
        });
        return result;
      }
      
      function mmToPx(mm) {
        return mm * 3.779528;
      }
      
      function calculateBarcodeParams(content, canvasWidth, canvasHeight, displayValue) {
        const margin = Math.max(2, Math.min(5, canvasWidth * 0.02));
        const marginVertical = Math.max(2, Math.min(4, canvasHeight * 0.03));
        const availableHeight = canvasHeight - marginVertical * 2;
        const textHeightRatio = 0.2;
        let textHeight = 0;
        let textSize = 0;
        
        if (displayValue !== 'none') {
          textHeight = Math.max(10, Math.min(18, availableHeight * textHeightRatio));
          textSize = Math.max(7, Math.min(12, textHeight * 0.75));
        }
        
        const barcodeHeight = displayValue !== 'none' ? (availableHeight - textHeight) : availableHeight;
        let finalBarcodeHeight = Math.max(10, Math.min(barcodeHeight, availableHeight));
        
        let totalHeight = marginVertical * 2 + finalBarcodeHeight + (displayValue !== 'none' ? textHeight : 0);
        if (totalHeight > canvasHeight) {
          const scaleRatio = canvasHeight / totalHeight;
          if (displayValue !== 'none') {
            textHeight *= scaleRatio;
            textSize *= scaleRatio;
          }
          finalBarcodeHeight = Math.max(8, finalBarcodeHeight * scaleRatio);
        }
        
        const availableWidth = canvasWidth - margin * 2;
        const barsPerChar = 11;
        const totalBars = content.length * barsPerChar;
        let barWidth = Math.max(0.75, Math.min(2, availableWidth / totalBars));
        barWidth = Math.round(barWidth * 4) / 4;
        
        return {
          barWidth,
          height: finalBarcodeHeight,
          margin,
          marginVertical,
          textHeight,
          textSize
        };
      }
      
      printWindow.document.write(`
        <!DOCTYPE html>
        <html>
        <head>
          <title>打印预览</title>
          <style>
            * {
              margin: 0;
              padding: 0;
              box-sizing: border-box;
            }
            @page {
              size: ${width}mm ${height}mm;
              margin: 0;
            }
            html, body {
              width: 100%;
              height: 100%;
            }
            body {
              display: flex;
              justify-content: center;
              align-items: center;
              background: #f5f5f5;
            }
            .print-wrapper {
              width: ${width}mm;
              height: ${height}mm;
              background: #fff;
              overflow: hidden;
              box-shadow: 0 0 10px rgba(0,0,0,0.1);
              position: relative;
            }
            .waybill-render {
              width: ${width}mm;
              height: ${height}mm;
              position: relative;
              background: #fff;
              border: 1px solid #000;
              overflow: hidden;
            }
            .waybill-render .logo {
              position: absolute;
              top: 5px;
              right: 5px;
              width: 30mm;
              height: 15mm;
              object-fit: contain;
            }
            .waybill-render .canvas-element {
              position: absolute;
              box-sizing: border-box;
              z-index: 10;
            }
            .waybill-render .elem-text {
              display: block;
              padding: 2px;
              overflow: hidden;
            }
            .waybill-render .elem-text .text-content {
              width: 100%;
              height: 100%;
              color: #000;
              white-space: normal;
              word-break: break-word;
              overflow: hidden;
              font-family: "Microsoft Yahei", "PingFang SC", "Helvetica Neue", Arial, sans-serif;
            }
            .waybill-render .elem-time {
              display: flex;
              justify-content: center;
              align-items: center;
              font-size: 14px;
              color: #000;
              font-family: 'Microsoft Yahei', sans-serif;
              padding: 2px;
            }
            .waybill-render .elem-barcode {
              position: absolute;
              display: flex;
              justify-content: center;
              align-items: center;
              font-size: 12px;
              color: #666;
              transform: rotate(var(--rotation, 0deg));
              transform-origin: center center;
            }
            .waybill-render .elem-barcode canvas {
              display: block;
              width: unset !important;
              height: unset !important;
              max-width: 100%;
              max-height: 100%;
              font-size: 0;
              line-height: 0;
            }
            .waybill-render .elem-qrcode {
              position: relative;
              display: flex;
              justify-content: center;
              align-items: center;
              font-size: 12px;
              color: #666;
            }
            .waybill-render .elem-qrcode canvas {
              width: 100% !important;
              height: 100% !important;
            }
            .waybill-render .elem-image img {
              width: 100%;
              height: 100%;
              object-fit: contain;
            }
            .waybill-render .elem-image {
              overflow: hidden;
            }
            .waybill-render .elem-hline {
              border-top: var(--line-width) var(--line-style) var(--line-color);
              background: transparent;
              position: absolute;
            }
            .waybill-render .elem-vline {
              border-left: var(--line-width) var(--line-style) var(--line-color);
              background: transparent;
              position: absolute;
            }
            .waybill-render .elem-shape {
              border: 1px solid #000;
              background: transparent;
              position: absolute;
            }
            .waybill-render .elem-icon {
              display: flex;
              justify-content: center;
              align-items: center;
              font-size: 24px;
              color: #333;
              position: absolute;
            }
            .waybill-render .elem-diagonal {
              position: relative;
              overflow: hidden;
            }
            .waybill-render .elem-diagonal::before {
              content: '';
              position: absolute;
              top: 0;
              left: 0;
              width: var(--length);
              height: 2px;
              background: var(--line-color);
              transform-origin: 0 0;
              transform: rotate(var(--angle));
            }
          </style>
        </head>
        <body>
          <div class="print-wrapper">
            <div class="waybill-render">
              ${templateJson.map((elem, idx) => {
                const x = elem.x || 0;
                const y = elem.y || 0;
                const w = elem.width || 10;
                const h = elem.height || 10;
                const rotation = elem.rotation || 0;
                const zIndex = elem.zIndex || 10;
                const fontSize = elem.fontSize || 14;
                const fontColor = elem.fontColor || '#000';
                const textAlign = elem.textAlign || 'left';
                const lineWidth = elem.lineWidth || 1;
                const lineColor = elem.lineColor || '#000';
                const lineStyle = elem.lineStyle || 'solid';
                
                if (elem.type === 'text') {
                  let content = replacePlaceholders(elem.content || '', orderData);
                  return `
                    <div class="canvas-element elem-text" 
                         style="left: ${x}mm; top: ${y}mm; width: ${w}mm; height: ${h}mm; z-index: ${zIndex};">
                      <div class="text-content" 
                           style="font-size: ${fontSize}px; color: ${fontColor}; text-align: ${textAlign}; line-height: ${fontSize * 1.5}px;">
                        ${content}
                      </div>
                    </div>
                  `;
                } else if (elem.type === 'time') {
                  return `
                    <div class="canvas-element elem-time" 
                         style="left: ${x}mm; top: ${y}mm; width: ${w}mm; height: ${h}mm; z-index: ${zIndex};">
                      ${new Date().toLocaleString('zh-CN')}
                    </div>
                  `;
                } else if (elem.type === 'barcode') {
                  return `
                    <div class="canvas-element elem-barcode" 
                         style="left: ${x}mm; top: ${y}mm; width: ${w}mm; height: ${h}mm; z-index: ${zIndex}; --rotation: ${rotation}deg;">
                      <canvas data-index="${idx}" data-type="barcode"></canvas>
                    </div>
                  `;
                } else if (elem.type === 'qrcode') {
                  return `
                    <div class="canvas-element elem-qrcode" 
                         style="left: ${x}mm; top: ${y}mm; width: ${w}mm; height: ${h}mm; z-index: ${zIndex};">
                      <canvas data-index="${idx}" data-type="qrcode"></canvas>
                    </div>
                  `;
                } else if (elem.type === 'image') {
                  let content = replacePlaceholders(elem.content || '', orderData);
                  return `
                    <div class="canvas-element elem-image" 
                         style="left: ${x}mm; top: ${y}mm; width: ${w}mm; height: ${h}mm; z-index: ${zIndex};">
                      <img src="${content}" />
                    </div>
                  `;
                } else if (elem.type === 'hline') {
                  return `
                    <div class="canvas-element elem-hline" 
                         style="left: ${x}mm; top: ${y}mm; width: ${w}mm; height: ${h}mm; z-index: ${zIndex}; --line-width: ${lineWidth}px; --line-color: ${lineColor}; --line-style: ${lineStyle};">
                    </div>
                  `;
                } else if (elem.type === 'vline') {
                  return `
                    <div class="canvas-element elem-vline" 
                         style="left: ${x}mm; top: ${y}mm; width: ${w}mm; height: ${h}mm; z-index: ${zIndex}; --line-width: ${lineWidth}px; --line-color: ${lineColor}; --line-style: ${lineStyle};">
                    </div>
                  `;
                } else if (elem.type === 'icon') {
                  return `
                    <div class="canvas-element elem-icon" 
                         style="left: ${x}mm; top: ${y}mm; width: ${w}mm; height: ${h}mm; z-index: ${zIndex};">
                    </div>
                  `;
                } else if (elem.type === 'shape') {
                  return `
                    <div class="canvas-element elem-shape" 
                         style="left: ${x}mm; top: ${y}mm; width: ${w}mm; height: ${h}mm; z-index: ${zIndex};">
                    </div>
                  `;
                } else if (elem.type === 'diagonal') {
                  const diagW = elem.width || 40;
                  const diagH = elem.height || 40;
                  const length = Math.sqrt(diagW * diagW + diagH * diagH);
                  const angle = Math.atan2(diagH, diagW) * 180 / Math.PI;
                  return `
                    <div class="canvas-element elem-diagonal" 
                         style="left: ${x}mm; top: ${y}mm; width: ${w}mm; height: ${h}mm; z-index: ${zIndex}; --line-width: ${lineWidth}px; --line-color: ${lineColor}; --line-style: ${lineStyle}; --length: ${length}mm; --angle: ${angle}deg;">
                    </div>
                  `;
                }
                return '';
              }).join('')}
            </div>
          </div>
          <script src="https://cdn.jsdelivr.net/npm/jsbarcode@3.11.5/dist/JsBarcode.all.min.js"><\/script>
          <script src="https://cdn.jsdelivr.net/npm/qrcode@1.5.1/build/qrcode.min.js"><\/script>
          <script>
            (function() {
              const templateJson = ${JSON.stringify(templateJson)};
              const orderData = ${JSON.stringify(orderData)};
              
              function replacePlaceholders(content, data) {
                if (!content || typeof content !== 'string') return content;
                let result = content;
                Object.entries(data).forEach(([key, value]) => {
                  result = result.replace(new RegExp('\\{' + key + '\\}|\\{\\{' + key + '\\}\\}', 'g'), value || '');
                });
                return result;
              }
              
              function mmToPx(mm) {
                return mm * 3.779528;
              }
              
              function calculateBarcodeParams(content, canvasWidth, canvasHeight, displayValue) {
                const margin = Math.max(2, Math.min(5, canvasWidth * 0.02));
                const marginVertical = Math.max(2, Math.min(4, canvasHeight * 0.03));
                const availableHeight = canvasHeight - marginVertical * 2;
                const textHeightRatio = 0.2;
                let textHeight = 0;
                let textSize = 0;
                
                if (displayValue !== 'none') {
                  textHeight = Math.max(10, Math.min(18, availableHeight * textHeightRatio));
                  textSize = Math.max(7, Math.min(12, textHeight * 0.75));
                }
                
                const barcodeHeight = displayValue !== 'none' ? (availableHeight - textHeight) : availableHeight;
                let finalBarcodeHeight = Math.max(10, Math.min(barcodeHeight, availableHeight));
                
                let totalHeight = marginVertical * 2 + finalBarcodeHeight + (displayValue !== 'none' ? textHeight : 0);
                if (totalHeight > canvasHeight) {
                  const scaleRatio = canvasHeight / totalHeight;
                  if (displayValue !== 'none') {
                    textHeight *= scaleRatio;
                    textSize *= scaleRatio;
                  }
                  finalBarcodeHeight = Math.max(8, finalBarcodeHeight * scaleRatio);
                }
                
                const availableWidth = canvasWidth - margin * 2;
                const barsPerChar = 11;
                const totalBars = content.length * barsPerChar;
                let barWidth = Math.max(0.75, Math.min(2, availableWidth / totalBars));
                barWidth = Math.round(barWidth * 4) / 4;
                
                return {
                  barWidth,
                  height: finalBarcodeHeight,
                  margin,
                  marginVertical,
                  textHeight,
                  textSize
                };
              }
              
              function generateBarcodesAndQRCodes() {
                const canvases = document.querySelectorAll('canvas[data-index]');
                
                canvases.forEach((canvas) => {
                  const idx = parseInt(canvas.dataset.index);
                  const type = canvas.dataset.type;
                  const elem = templateJson[idx];
                  
                  if (!elem) return;
                  
                  const content = replacePlaceholders(elem.content || (type === 'barcode' ? '123456789012' : 'https://www.example.com'), orderData);
                  
                  const elemWidth = mmToPx(elem.width || (type === 'barcode' ? 60 : 30));
                  const elemHeight = mmToPx(elem.height || (type === 'barcode' ? 20 : 30));
                  
                  canvas.width = elemWidth;
                  canvas.height = elemHeight;
                  
                  if (type === 'barcode') {
                    const margin = Math.max(2, Math.min(5, elemWidth * 0.02));
                    const marginVertical = Math.max(2, Math.min(4, elemHeight * 0.03));
                    const availableHeight = elemHeight - marginVertical * 2;
                    const textHeightRatio = 0.2;
                    let textHeight = 0;
                    let textSize = 0;
                    
                    if (elem.dataPosition !== 'none') {
                      textHeight = Math.max(10, Math.min(18, availableHeight * textHeightRatio));
                      textSize = Math.max(7, Math.min(12, textHeight * 0.75));
                    }
                    
                    const barcodeHeight = elem.dataPosition !== 'none' ? (availableHeight - textHeight) : availableHeight;
                    let finalBarcodeHeight = Math.max(10, Math.min(barcodeHeight, availableHeight));
                    
                    let totalHeight = marginVertical * 2 + finalBarcodeHeight + (elem.dataPosition !== 'none' ? textHeight : 0);
                    if (totalHeight > elemHeight) {
                      const scaleRatio = elemHeight / totalHeight;
                      if (elem.dataPosition !== 'none') {
                        textHeight *= scaleRatio;
                        textSize *= scaleRatio;
                      }
                      finalBarcodeHeight = Math.max(8, finalBarcodeHeight * scaleRatio);
                    }
                    
                    const availableWidth = elemWidth - margin * 2;
                    const barsPerChar = 11;
                    const totalBars = content.length * barsPerChar;
                    let barWidth = Math.max(0.75, Math.min(2, availableWidth / totalBars));
                    barWidth = Math.round(barWidth * 4) / 4;
                    
                    try {
                      JsBarcode(canvas, content, {
                        format: elem.barcodeType || 'CODE128',
                        width: barWidth,
                        height: finalBarcodeHeight,
                        displayValue: elem.dataPosition !== 'none',
                        textPosition: elem.dataPosition === 'top' ? 'top' : 'bottom',
                        textSize: textSize,
                        margin: margin,
                        lineColor: '#000000',
                        textMargin: marginVertical
                      });
                    } catch(e) {
                      console.error('条形码生成失败:', e);
                    }
                  } else if (type === 'qrcode') {
                    try {
                      const ctx = canvas.getContext('2d');
                      ctx.clearRect(0, 0, elemWidth, elemHeight);
                      
                      QRCode.toCanvas(canvas, content, {
                        width: elemWidth,
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
                    } catch(e) {
                      console.error('二维码生成失败:', e);
                    }
                  }
                });
              }
              
              window.onload = function() {
                generateBarcodesAndQRCodes();
                setTimeout(function() {
                  window.print();
                }, 500);
              };
            })();
          <\/script>
        </body>
        </html>
      `);
      
      printWindow.document.close();
    }
  }
};
</script>

<style scoped>
.preview-container {
  padding: 10px;
  font-size: 14px;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.preview-header {
  padding: 10px;
  border-bottom: 1px solid #e4e7ed;
  display: flex;
  justify-content: flex-end;
  background: #fff;
}

.preview-content {
  flex: 1;
  overflow: auto;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding: 20px;
  background: #f5f5f5;
}

@media print {
  .preview-header {
    display: none;
  }
}
</style>
