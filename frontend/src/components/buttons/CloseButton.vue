<template>
  <button class="close" :style="{ width: size + 'px', height: size + 'px' }">
    <slot
      ><img
      :src="close"
      alt="Close Icon"
      ></img
    ></slot>
  </button>
</template>

<script setup lang="ts">
import close from "@/assets/close.svg";

const { size = 30 } = defineProps<{
  size?: number;
}>();
</script>

<style scoped lang="scss">
.close {
  cursor: pointer;
  display: block;
  aspect-ratio: 1 / 1;
  border: none;
  outline: none;
  background-color: transparent;
}
</style>
