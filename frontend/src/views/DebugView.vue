<template>
  <main>
    <h1>Hello World! 👋🌍</h1>
  </main>
</template>

<script setup lang="ts">
import { CompatClient, Stomp } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import { onMounted, ref } from "vue";

const connection = ref<WebSocket>();

onMounted(() => {
  const socket = new SockJS("http://localhost:8082/ws");
  const stompClient: CompatClient = Stomp.over(socket);
  stompClient.connect({}, () => {
    console.log("Connected to WebSocket");
  });
  stompClient.onStompError = (frame) => {
    console.log("Broker reported error: " + frame.headers["message"]);
    console.log("Additional details: " + frame.body);
  };
  stompClient.debug = (msg) => {
    console.log(msg);
  };
  stompClient.activate();
});
</script>
