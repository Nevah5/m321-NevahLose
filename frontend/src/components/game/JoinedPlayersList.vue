<template>
  <div class="players">
    <CardComponent
      v-for="player in players"
      :key="player.id"
      type=""
      :name="player.name || 'Player'"
      background-name="TheWatcher.webp"
      subject-name="TheThief_subject.webp"
      :description="player.isHost ? 'Host' : 'Player'"
      size="small"
      back-side-text="<empty />"
      :is-turned-prop="!player.joined"
      :is-turnable="false"
      :hover-effect="false"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, defineProps, onMounted } from "vue";
import CardComponent from "./CardComponent.vue";
import { gameService } from "@/api";
import type { ChatMessage, GamePlayer } from "@/api/types";

const { initialPlayers } = defineProps<{
  initialPlayers: GamePlayer[];
}>();

interface Player {
  id?: string;
  name?: string;
  isHost?: boolean;
  joined: boolean;
}

gameService.subscribeChatEvents((event: ChatMessage) => {
  if (event.type !== "ACTIVITY") return;
  if (event.joined) {
    addPlayer(event);
  } else {
    removePlayer(event);
  }
});

const addPlayer = (activity: ChatMessage) => {
  for (const player of players.value) {
    if (!player.joined) {
      player.id = activity.senderId;
      player.name = activity.senderUsername;
      player.isHost = false;
      player.joined = true;
      break;
    }
  }
};

const removePlayer = (activity: ChatMessage) => {
  for (const player of players.value) {
    if (player.id === activity.senderId) {
      player.joined = false;
      break;
    }
  }
};

const players = ref<Player[]>([
  { joined: false },
  { joined: false },
  { joined: false },
  { joined: false },
]);

onMounted(() => {
  for (let i = 0; i < initialPlayers.length; i++) {
    players.value[i].id = initialPlayers[i].playerId;
    players.value[i].name = initialPlayers[i].username;
    players.value[i].isHost = false;
    players.value[i].joined = true;
  }
});
</script>

<style scoped lang="scss">
.players {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 1rem;
}
</style>
