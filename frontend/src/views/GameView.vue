<template>
  <main>
    <LoadingOverlay :enabled="isLoading" />
    <h1>Game id {{ gameId }}</h1>
  </main>
</template>

<script setup lang="ts">
import { gameService } from "@/api";
import toastApi from "@/api/toastApi";
import LoadingOverlay from "@/components/LoadingOverlay.vue";
import type { Client } from "@stomp/stompjs";
import { onMounted, onUnmounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const gameId = ref<string>("");
const isLoading = ref(true);
const router = useRouter();
const client = ref<Client | null>(null);

onMounted(async () => {
  gameId.value = route.params.id as string;
  isLoading.value = true;
  const token = localStorage.getItem("token");
  try {
    client.value = await gameService.joinGame(gameId.value, token!);
    isLoading.value = false;
  } catch (error) {
    toastApi.emit({
      title: "An error occurred",
      message: error as string,
    });
    router.push("/");
  }
});

onUnmounted(() => {
  gameService.leaveGame();
});
</script>

<style scoped>
main {
  height: 100vh;
}
h1 {
  font-size: 2rem;
  margin: 0;
}
</style>
