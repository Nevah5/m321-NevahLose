<template>
  <div class="cards-list">
    <h3>GET Cards</h3>
    <p>
      Cards with the type GET retrieve information about the game. There are
      twelve GET cards in a deck.
    </p>
    <div class="card-wrapper" v-if="cards.length > 0">
      <CardComponent
        v-for="card in cards.filter((card) => card.cardType === 'GET')"
        :key="card.id"
        :type="card.cardType.toLowerCase()"
        :name="card.name"
        :background-name="card.backgroundFilename"
        :subject-name="card.subjectFilename"
        :description="card.description"
      />
    </div>
    <h3>POST Cards</h3>
    <p>
      Cards with the type POST create new game rules. There are twelve POST
      cards in a deck.
    </p>
    <div class="card-wrapper" v-if="cards.length > 0">
      <CardComponent
        v-for="card in cards.filter((card) => card.cardType === 'POST')"
        :key="card.id"
        :type="card.cardType.toLowerCase()"
        :name="card.name"
        :background-name="card.backgroundFilename"
        :subject-name="card.subjectFilename"
        :description="card.description"
      />
    </div>
    <h3>PUT Cards</h3>
    <p>
      Cards with the type PUT modify aspects of the game. There are twelve PUT
      cards in a deck.
    </p>
    <div class="card-wrapper" v-if="cards.length > 0">
      <CardComponent
        v-for="card in cards.filter((card) => card.cardType === 'PUT')"
        :key="card.id"
        :type="card.cardType.toLowerCase()"
        :name="card.name"
        :background-name="card.backgroundFilename"
        :subject-name="card.subjectFilename"
        :description="card.description"
      />
    </div>
    <h3>DELETE Cards</h3>
    <p>
      Cards with the type DELETE remove certain aspects of the game. There are
      twelve DELETE cards in a deck.
    </p>
    <div class="card-wrapper" v-if="cards.length > 0">
      <CardComponent
        v-for="card in cards.filter((card) => card.cardType === 'DELETE')"
        :key="card.id"
        :type="card.cardType.toLowerCase()"
        :name="card.name"
        :background-name="card.backgroundFilename"
        :subject-name="card.subjectFilename"
        :description="card.description"
      />
    </div>
    <h3>OPTIONS Card</h3>
    <p>
      There is one special OPTIONS card in the deck. This card is special,
      because it acs as any random card in the game, when used. The deck
      contains three of them.
    </p>
    <div class="card-wrapper" v-if="cards.length > 0">
      <CardComponent
        v-for="card in cards.filter((card) => card.cardType === 'OPTIONS')"
        :key="card.id"
        :type="card.cardType.toLowerCase()"
        :name="card.name"
        :background-name="card.backgroundFilename"
        :subject-name="card.subjectFilename"
        :description="card.description"
      />
    </div>
  </div>
</template>

<script lang="ts" setup>
import CardComponent from "@/components/game/CardComponent.vue";
import { cardService } from "@/api";
import toastApi from "@/api/toastApi";
import type { Card } from "@/api/types";
import { onMounted, ref } from "vue";

const cards = ref<Card[]>([]);

onMounted(async () => {
  try {
    const cardsList = await cardService.getCards();
    cards.value = cardsList;
  } catch (error) {
    console.error(error);
    toastApi.emit({
      title: "Retrieving cards failed",
      message: "Failed to retrieve cards from the server",
    });
  }
});
</script>

<style lang="scss" scoped>
.card-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 1em;
  flex-wrap: wrap;
  margin: 1rem 0 2rem;
  max-width: 900px;
}

p {
  max-width: 900px;
}
</style>
