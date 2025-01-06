<template>
  <main>
    <LoadingOverlay :enabled="isLoading" />
    <h1>Game id {{ gameId }}</h1>
    <InviteCode :code="inviteCode" />
  </main>
</template>

<script setup lang="ts">
import { gameService } from "@/api";
import toastApi from "@/api/toastApi";
import InviteCode from "@/components/game/InviteCode.vue";
import LoadingOverlay from "@/components/page/LoadingOverlay.vue";
import type { Client } from "@stomp/stompjs";
import { onMounted, onUnmounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const gameId = ref("");
const inviteCode = ref("");
const isLoading = ref(true);
const router = useRouter();
const client = ref<Client | null>(null);

onMounted(async () => {
  isLoading.value = true;
  gameId.value = route.params.id as string;

  // load invite code
  const codeStored = localStorage.getItem("game_roomId");
  if (codeStored == null) {
    toastApi.emit({
      title: "Error retrieving invite code",
      message: "Invite code from localStorage was not set properly",
    });
    return;
  }
  inviteCode.value = codeStored;

  // establish websocket and join game
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
.invite-code {
  bottom: 0;
  right: 10px;
  position: absolute;
}
</style>
