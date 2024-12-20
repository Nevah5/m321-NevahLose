<template>
  <img
    alt="Loading icon"
    class="loading-icon"
    src="@/assets/loading.svg"
    :height="height"
  />
</template>

<script lang="ts" setup>
const { height = "100%" } = defineProps<{
  height?: number | string;
}>();
</script>
