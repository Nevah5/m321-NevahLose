<template>
  <ul class="turn-order">
    <li v-for="p in turnOrder" :key="p.playerId">
      {{ currentPlayerId == p.playerId ? ">>" : "" }} {{ p.username }}
    </li>
  </ul>
</template>

<script setup lang="ts">
import type { GamePlayer } from "@/api/types";

const { currentPlayerId, turnOrder } = defineProps<{
  currentPlayerId: string;
  turnOrder: GamePlayer[];
}>();
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
