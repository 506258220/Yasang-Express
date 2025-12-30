<template>
  <div class="preview-container">
    <waybill-render v-if="template" :template="template" :order-data="orderData" />
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
  }
};
</script>

<style scoped>
.preview-container {
  padding: 10px;
  font-size: 14px;
}
</style>
