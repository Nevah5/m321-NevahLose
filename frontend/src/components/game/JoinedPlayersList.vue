<template>
  <div class="players">
    <LoadingIcon v-if="isLoading" height="50" :dark="true" />
    <CardComponent
      v-else
      v-for="(player, i) in players"
      :key="player.id"
      type=""
      :name="player.name || 'Player'"
      :background-name="cards[i]?.backgroundFilename"
      :subject-name="cards[i]?.subjectFilename"
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
import { cardService, gameService } from "@/api";
import type { ApiError, Card, ChatMessage } from "@/api/types";
import toastApi from "@/api/toastApi";
import { useRouter } from "vue-router";
import LoadingIcon from "../icons/LoadingIcon.vue";

const { gameId } = defineProps<{
  gameId: string;
}>();
const isLoading = ref(true);
const router = useRouter();

interface Player {
  id?: string;
  name?: string;
  isHost?: boolean;
  joined: boolean;
}

const addPlayer = (activity: ChatMessage) => {
  for (const player of players.value) {
    if (!player.joined) {
      player.id = activity.senderId;
      player.name = activity.senderUsername;
      player.isHost = activity.host;
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

const cards = ref<Card[]>([]);

const players = ref<Player[]>([
  { joined: false },
  { joined: false },
  { joined: false },
  { joined: false },
]);

onMounted(async () => {
  try {
    for (let i = 0; i < 4; i++) {
      let card = await cardService.getRandom();
      while (cards.value.map((card) => card.id).includes(card.id)) {
        card = await cardService.getRandom();
      }
      cards.value.push(card);
    }
  } catch (error: ApiError | any) {
    toastApi.emit({
      title: "An error occurred",
      message: error.message,
    });
    router.push("/");
  }

  setTimeout(async () => {
    const playerId = localStorage.getItem("playerId");
    const token = localStorage.getItem("token");
    try {
      const initialPlayers = await gameService.getPlayers(gameId, token!);
      if (
        initialPlayers.length == 4 &&
        !initialPlayers.map((player) => player.playerId).includes(playerId!)
      ) {
        // error message comes from /user/queue/errors
        router.push("/");
      }
      for (let i = 0; i < initialPlayers.length; i++) {
        players.value[i].id = initialPlayers[i].playerId;
        players.value[i].name = initialPlayers[i].username;
        players.value[i].isHost = initialPlayers[i].host;
        players.value[i].joined = true;
      }
    } catch (error) {
      toastApi.emit({
        title: "An error occurred",
        message: error as string,
      });
      router.push("/");
    }
    isLoading.value = false;
  }, 300);

  gameService.subscribeChatEvents((event: ChatMessage) => {
    if (event.type !== "ACTIVITY") return;
    if (event.joined) {
      addPlayer(event);
    } else {
      removePlayer(event);
    }
  });
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
