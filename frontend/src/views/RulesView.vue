<template>
  <main>
    <h1>Rules</h1>
    <section>
      <h2>Basics</h2>
      <p>
        This game is played almost like UNO, but with a few twists. A deck
        consists of 52 cards. Each player gets 5 cards to begin. Because each
        card modifies the game in a certain way, there is no winning condition
        yet and the players have to create one by using the cards. It is
        possible to lay down up to 2 cards, but be careful, you might lose the
        game if you are not paying attention to the current rules.
      </p>
      <p>
        Every card that is being played is hidden for the other players, but the
        effects are visible for everyone.
      </p>
    </section>
    <section>
      <h2>Player Turns</h2>
      <p>
        Each player takes turns after each other. There is the possibility to
        draw a card from the deck each turn, but this skips the turn for the
        player.
      </p>
      <p>
        The direction and order of players are chosen randomly at the beginning
        of the game.
      </p>
    </section>
    <section>
      <h2>Cards</h2>
      <CardDisplay />
    </section>
    <section>
      <h2>Special Rules</h2>
      <ul>
        <li>If a player takes the last card from the deck, they lose.</li>
        <li>If there is only one player left, that player wins.</li>
      </ul>
    </section>
  </main>
</template>

<script lang="ts" setup>
import CardDisplay from "@/components/CardDisplay.vue";
</script>

<style lang="scss" scoped>
main {
  margin: 0 auto 2rem;
  max-width: 900px;
}

p {
  max-width: 900px;
}
</style>
