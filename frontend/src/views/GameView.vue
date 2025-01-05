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
import { onMounted, onUnmounted, ref } from "vue";
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
    .joinGame(gameId.value, token!)
    .then((client: Client) => {
      client.publish({
        destination: `/app/games.joinGame`,
        body: JSON.stringify({ gameId: gameId.value }),
        headers: { Authorization: `Bearer ${token}` },
      });
      isLoading.value = false;
    })
    .catch((error) => {
      router.push("/");
    });
});

onUnmounted(() => {
  gameService.leaveGame();
});
</script>

<style scoped>
h1 {
  font-size: 2rem;
  margin: 0;
}
</style>
