<template>
  <div class="card-wrapper" v-if="cards.length > 0">
    <CardComponent
      v-for="card in cards"
      :key="card.id"
      :type="card.cardType.toLowerCase()"
      :name="card.name"
      :background-name="card.backgroundFilename"
      :subject-name="card.subjectFilename"
      :description="card.description"
    />
  </div>
</template>

<script lang="ts" setup>
import CardComponent from "./CardComponent.vue";
import { cardService } from "@/api";
import type { Card } from "@/api/types";
import { onMounted, ref } from "vue";

const cards = ref<Card[]>([]);

onMounted(async () => {
  try {
    const cardsList = await cardService.getCards();
    cards.value = cardsList;
  } catch (error) {
    console.error(error);
    // TODO: implement toast service error handling
  }
});
</script>

<style lang="scss" scoped>
.card-wrapper {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  gap: 1em;
  flex-wrap: wrap;
}
</style>
