<template>
  <main>
    <h1>Hello World! 👋🌍</h1>
    <TooltipComponent text="Hello World!" position="right">
      <p>Hover me!</p>
    </TooltipComponent>
  </main>
</template>

<script setup lang="ts">
import toastApi from "@/api/toastApi";
import TooltipComponent from "@/components/page/TooltipComponent.vue";
import { onMounted, ref } from "vue";

// const connection = ref<WebSocket>();

onMounted(() => {
  toastApi.emit({
    title: "Hello World!",
    message: "👋🌍",
    timeout: 5000,
    type: "info",
  });
  setTimeout(() => {
    toastApi.emit({
      title: "Hello World 2!",
      message: "👋🌍",
      timeout: 3000,
      type: "info",
    });
  }, 3000);
});
</script>
