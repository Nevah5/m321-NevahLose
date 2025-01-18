<template>
  <div class="inventory">
    <CardComponent
      v-for="card in cards"
      :key="card.id"
      :type="card.cardType"
      :name="card.name"
      :background-name="card.backgroundFilename"
      :subject-name="card.subjectFilename"
      :description="card.description"
      :is-turnable="false"
      :hover-effect="false"
      size="small"
    />
  </div>
</template>

<script setup lang="ts">
import type { Card } from "@/api/types";
import CardComponent from "./CardComponent.vue";

const { cards } = defineProps<{
  cards: Card[];
}>();
</script>

<style scoped lang="scss">
.inventory {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 1rem;
}
</style>
