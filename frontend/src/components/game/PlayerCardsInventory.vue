<template>
  <div class="inventory">
    <h2>Card Inventory</h2>
    <div class="card-wrapper">
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
  justify-content: flex-start;
  align-items: center;
  flex-direction: column;
  gap: 1rem;
  background-color: rgba(#ffffff, 0.5);
  height: 50px;
  overflow: hidden;
  transition: height 0.3s ease-in-out;
  margin-bottom: 10px;

  .card {
    cursor: pointer;
  }

  &:hover {
    height: 600px;
  }

  .card-wrapper {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    gap: 1rem;
  }
}
</style>
