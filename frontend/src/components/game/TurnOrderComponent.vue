<template>
  <ul class="turn-order">
    <li v-for="p in turnOrder" :key="p.playerId">
      {{ currentPlayerId == p.playerId ? ">>" : "" }} {{ p.username }}
      {{ p.playerId == playerId ? " (You)" : "" }}
    </li>
  </ul>
</template>

<script setup lang="ts">
import type { GamePlayer } from "@/api/types";
import { onMounted, ref } from "vue";

const playerId = ref("");

const { currentPlayerId, turnOrder } = defineProps<{
  currentPlayerId: string;
  turnOrder: GamePlayer[];
}>();

onMounted(() => {
  playerId.value = localStorage.getItem("playerId")!;
});
</script>

<style scoped lang="scss">
.turn-order {
  list-style-type: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  font-weight: bold;
}
</style>
