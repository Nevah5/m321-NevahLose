<div align="center">
  <img align="center" src="./resources/logo.svg" alt="API Aces Logo" width="200"/>
  <h1>API Aces - Card Game</h1>
  <span>Project for Module 321</span>
</div>

## Description

This project is a small card game that can be played from 2 to 4 players. The aim is to call different APIs by using the cards to change the behavior of the game. The player that has the most points at the end of the game wins.

## Rules

### Basics

This game is played almost like UNO, but with a few twists. A deck consists of 52 cards. Each player gets 5 cards to begin. Because each cards modifies the game in a certain way, there is no winning condition yet and the players have to create one, by using the cards. It is possible to lay down up to 2 cards, but be careful, there you might lose the game if you are not paying attention to the current rules.

Every card that is being played is hidden for the other players, but the effects are visible for everyone.

### Player Turns

Each player takes turn after each other. There is the possibility to draw a card from the deck each turn, but this skips the turn for the player.

The direction and order of player is chosen randomly at the beginning of the game.

### Cards

#### Card Types

| Card Type | Description            | Amount in Deck |
| --------- | ---------------------- | -------------- |
| `GET`     | Retrieves information  | 12             |
| `POST`    | Creates new game rules | 12             |
| `PUT`     | Modifies game rules    | 12             |
| `DELETE`  | Removed game rules     | 12             |
| `OPTIONS` | Does things            | 4              |
| **TOTAL** |                        | **52**         |

#### Card Actions

| #   | Card Action                    | Description                                                     | Type      |
| --- | ------------------------------ | --------------------------------------------------------------- | --------- |
| 1   | See a player's card            | Allows you to see what cards a player has                       | `GET`     |
| 2   | See current game rules         | Allows you to see the current game rules for one turn           | `GET`     |
| 3   | See remaining cards in deck    | Allows you to see how many cards are left in the deck           | `GET`     |
| 4   | Winning condition 10           | Creates a game rule and makes players with exactly 10 cards win | `POST`    |
| 5   | Winning condition 0            | Creates a game rule and makes players with exactly 0 cards win  | `POST`    |
| 6   | Drawing condition 3            | Creates a game rule and makes players draw 3 cards instead of 1 | `POST`    |
| 8   | Change card owner              | Allows you to steal a card from another player                  | `PUT`     |
| 9   | Disable random rule            | Disables a random rule for everyone until your next play        | `PUT`     |
| 10  | Give card to another player    | Allows you to give a card to another player                     | `PUT`     |
| 11  | Remove random rule             | Removes a random rule for everyone until your next play         | `DELETE`  |
| 12  | Remove random card from player | Allows you to remove a card from another player                 | `DELETE`  |
| 13  | Remove card from random player | Allows you to pick a specific card from a random player         | `DELETE`  |
| 14  | Random Action                  | Will act as a random card as soon as played                     | `OPTIONS` |

Each card is exactly contained 4 times in the deck.

### Special Rules

If a player takes the last card from the deck, he loses.

If there is only one player left, that player wins.

## Architecture

This project is divided into multiple parts. It contains the backend and different microservices to handle the requests for the backend.
