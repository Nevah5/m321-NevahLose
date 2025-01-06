<template>
  <img
    src="@/assets/plane.svg"
    alt="Paper plane icon"
    :width="iconWidth"
    :class="color"
  />
</template>

<script setup lang="ts">
const { iconWidth = 128, color = "" } = defineProps<{
  iconWidth?: number;
  color?: "white";
}>();
</script>

<style scoped lang="scss">
img.white {
  filter: invert(100%);
}
</style>
