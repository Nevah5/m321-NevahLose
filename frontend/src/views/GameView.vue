<template>
  <main>
    <LoadingOverlay :enabled="isLoading" />
    <h1>Game id {{ gameId }}</h1>
  </main>
</template>

<script setup lang="ts">
import { gameService } from "@/api";
import LoadingOverlay from "@/components/LoadingOverlay.vue";
import type { Client } from "@stomp/stompjs";
import { onMounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const gameId = ref<string>("");
const isLoading = ref(true);
const router = useRouter();

onMounted(async () => {
  gameId.value = route.params.id as string;
  isLoading.value = true;
  const token = localStorage.getItem("token");
  gameService
    .connectWebsocket(token!)
    .then((client: Client) => {
      client.subscribe("/app/game/" + gameId.value, (message) => {
        console.log(message.body);
      });
      client.publish({
        destination: "/app/game.joinGame",
        body: JSON.stringify({ gameId: gameId.value }),
      });
      isLoading.value = false;
    })
    .catch((error) => {
      window.alert(
        "Failed to connect to the game. Please try again later.\n" + error
      );
      router.push("/");
      isLoading.value = false;
    });
});
</script>

<style scoped>
h1 {
  font-size: 2rem;
  margin: 0;
}
</style>
