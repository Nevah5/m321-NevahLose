<template>
  <img
    v-if="dark"
    alt="Loading icon"
    class="loading-icon"
    src="@/assets/loading_dark.svg"
    :height="height"
  />
  <img
    v-else
    alt="Loading icon"
    class="loading-icon"
    src="@/assets/loading.svg"
    :height="height"
  />
</template>

<script lang="ts" setup>
const { height = "100%", dark = false } = defineProps<{
  height?: number | string;
  dark?: boolean;
}>();
</script>
