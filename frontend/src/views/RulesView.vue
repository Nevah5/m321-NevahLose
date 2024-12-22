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
      <h3>Card Types</h3>
      <ul>
        <li><b>GET</b>: Retrieves information (12 cards)</li>
        <li><b>POST</b>: Creates new game rules (12 cards)</li>
        <li><b>PUT</b>: Modifies game rules (12 cards)</li>
        <li><b>DELETE</b>: Removes game rules (12 cards)</li>
        <li><b>OPTIONS</b>: Does things (4 cards)</li>
      </ul>
      <h3>Card Actions</h3>
      <ul>
        <li>
          <b>See a player's card</b>: Allows you to see what cards a player has
          (GET)
        </li>
        <li>
          <b>See current game rules</b>: Allows you to see the current game
          rules for one turn (GET)
        </li>
        <li>
          <b>See remaining cards in deck</b>: Allows you to see how many cards
          are left in the deck (GET)
        </li>
        <li>
          <b>Winning condition 10</b>: Creates a game rule and makes players
          with exactly 10 cards win (POST)
        </li>
        <li>
          <b>Winning condition 0</b>: Creates a game rule and makes players with
          exactly 0 cards win (POST)
        </li>
        <li>
          <b>Drawing condition 3</b>: Creates a game rule and makes players draw
          3 cards instead of 1 (POST)
        </li>
        <li>
          <b>Change card owner</b>: Allows you to steal a card from another
          player (PUT)
        </li>
        <li>
          <b>Disable random rule</b>: Disables a random rule for everyone until
          your next play (PUT)
        </li>
        <li>
          <b>Give card to another player</b>: Allows you to give a card to
          another player (PUT)
        </li>
        <li>
          <b>Remove random rule</b>: Removes a random rule for everyone until
          your next play (DELETE)
        </li>
        <li>
          <b>Remove random card from player</b>: Allows you to remove a card
          from another player (DELETE)
        </li>
        <li>
          <b>Remove card from random player</b>: Allows you to pick a specific
          card from a random player (DELETE)
        </li>
        <li>
          <b>Random Action</b>: Will act as a random card as soon as played
          (OPTIONS)
        </li>
      </ul>
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

<script setup></script>
