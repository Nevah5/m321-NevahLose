<template>
  <main>
    <h1>Hello World! 👋🌍</h1>
  </main>
</template>

<script setup lang="ts">
import toastApi from "@/api/toastApi";
import { CompatClient, Stomp } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { onMounted, ref } from "vue";

const connection = ref<WebSocket>();

onMounted(() => {
  toastApi.emit({
    title: "Hello World!",
    message: "👋🌍",
    timeout: 5000,
  });
  setTimeout(() => {
    toastApi.emit({
      title: "Hello World 2!",
      message: "👋🌍",
      timeout: 3000,
    });
  }, 3000);
});
</script>
