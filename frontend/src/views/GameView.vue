<template>
  <main>
    <LoadingOverlay :enabled="isLoading" />
    <div class="wrapper" v-if="phase === 0">
      <LeaveGameButton @confirm="leaveGame" />
      <StartGameButton v-if="isHost" :game-id="gameId" />
      <JoinedPlayersList v-if="!isLoading" :game-id="gameId" />
      <ChatComponent v-if="!isLoading" />
      <InviteCode :code="inviteCode" />
    </div>
    <div class="wrapper" v-if="phase === 1">
      <TurnOrderReveal :players="turnOrder" />
      <CountdownComponent :seconds="10" />
    </div>
    <div class="wrapper" v-if="phase === 2">
      <LeaveGameButton @confirm="leaveGame" />
      <TurnOrderComponent
        :turn-order="turnOrder"
        :current-player-id="currentTurnPlayerId"
      />
      <PlayerCardsInventory :cards="playerCards" />
    </div>
  </main>
</template>

<script setup lang="ts">
import { cardService, gameService } from "@/api";
import toastApi from "@/api/toastApi";
import { type Card, type GamePlayer } from "@/api/types";
import ChatComponent from "@/components/game/ChatComponent.vue";
import CountdownComponent from "@/components/game/CountdownComponent.vue";
import InviteCode from "@/components/game/InviteCode.vue";
import JoinedPlayersList from "@/components/game/JoinedPlayersList.vue";
import LeaveGameButton from "@/components/game/LeaveGameButton.vue";
import PlayerCardsInventory from "@/components/game/PlayerCardsInventory.vue";
import StartGameButton from "@/components/game/StartGameButton.vue";
import TurnOrderComponent from "@/components/game/TurnOrderComponent.vue";
import TurnOrderReveal from "@/components/game/TurnOrderReveal.vue";
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
const isHost = ref(false);
const phase = ref(0);
const turnOrder = ref<GamePlayer[]>([]);
const currentTurnPlayerId = ref("");
const cards = ref<Card[]>([]);
const playerCards = ref<Card[]>([]);

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
  isHost.value = localStorage.getItem("isHost") === "true";

  // load cards
  cards.value = await cardService.getCards();
  isLoading.value = false;

  gameService.gameActivityListener((message) => {
    switch (message.type) {
      case "GAME_TERMINATE":
        toastApi.emit({
          title: "Game terminated",
          message: message.message || "The game has been terminated",
          type: "info",
        });
        leaveGame();
        break;
      case "GAME_START":
        phase.value = 1;
        turnOrder.value = message.turnOrder!;
        currentTurnPlayerId.value = message.turnOrder![0].playerId;
        setTimeout(() => {
          phase.value = 2;
        }, 10000);
        break;
      case "CURRENT_CARDS":
        playerCards.value = message.cards!.map(
          (cardId) => cards.value.find((card) => card.id === cardId)!
        );
        break;
    }
  });
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
.wrapper {
  width: 100%;
  height: 100%;
}
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
.start-button {
  position: absolute;
  top: 10px;
  right: 10px;
}
.players {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 80%;
}
.turn-order-reveal {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
.countdown {
  position: fixed;
  bottom: 15px;
  right: 15px;
  font-size: 3rem;
}
.turn-order {
  position: absolute;
  left: 15px;
  top: 80px;
}
.inventory {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  bottom: 0;
}
</style>
