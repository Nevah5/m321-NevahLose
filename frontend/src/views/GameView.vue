<template>
  <main>
    <LoadingOverlay :enabled="isLoading" />
    <LeaveGameButton @confirm="leaveGame" />
    <JoinedPlayersList v-if="!isLoading" :game-id="gameId" />
    <ChatComponent v-if="!isLoading" />
    <InviteCode :code="inviteCode" />
  </main>
</template>

<script setup lang="ts">
import { gameService } from "@/api";
import toastApi from "@/api/toastApi";
import type { GamePlayer } from "@/api/types";
import ChatComponent from "@/components/game/ChatComponent.vue";
import InviteCode from "@/components/game/InviteCode.vue";
import JoinedPlayersList from "@/components/game/JoinedPlayersList.vue";
import LeaveGameButton from "@/components/game/LeaveGameButton.vue";
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
const initialPlayers = ref<GamePlayer[]>([]);

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
    router.push("/");
    return;
  }
  inviteCode.value = codeStored;

  // establish websocket and join game
  const token = localStorage.getItem("token");
  try {
    client.value = await gameService.joinGame(gameId.value, token!);
  } catch (error) {
    toastApi.emit({
      title: "An error occurred",
      message: error as string,
    });
    router.push("/");
  }

  isLoading.value = false;
});

onUnmounted(() => {
  gameService.leaveGame();
});

const leaveGame = () => {
  gameService.leaveGame();
  router.push("/");
};
</script>

<style scoped>
main {
  height: 100vh;
}
h1 {
  font-size: 2rem;
  margin: 0;
}
.leave-button {
  left: 10px;
  top: 10px;
  position: absolute;
}
.invite-code {
  bottom: 10px;
  right: 10px;
  position: absolute;
}
.chat-box {
  position: absolute;
  bottom: 10px;
  left: 10px;
  max-height: 30%;
}
.players {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 80%;
}
</style>
