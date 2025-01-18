<template>
  <div class="turn-order-reveal">
    <h2>Determining player order...</h2>
    <div class="line"></div>
    <ol>
      <li
        v-for="(player, i) in players"
        :key="player.playerId"
        :data-name="player.username"
        :data-offset="i * 1000"
      >
        XXXXXXXXXXXXXXXXXXXX
      </li>
    </ol>
  </div>
</template>

<script setup lang="ts">
import type { GamePlayer } from "@/api/types";
import { onMounted } from "vue";

const { players } = defineProps<{
  players: GamePlayer[];
}>();

const possibleCharacters =
  "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
onMounted(() => {
  const liElements = document.querySelectorAll("li");
  liElements.forEach((li) => {
    li.innerHTML = `<b>${getRandomCharacters(20)}</b>`;
    const offset = li.getAttribute("data-offset");
    let index = 1;
    setTimeout(() => {
      setInterval(() => {
        li.innerHTML =
          li.getAttribute("data-name")?.slice(0, index) +
          `<b>${getRandomCharacters(20).slice(index, -1)!}</b>`;
        index++;
      }, 50);
    }, parseInt(offset as string));
  });
});

function getRandomCharacters(length: number) {
  let result = "";
  const charactersLength = possibleCharacters.length;
  for (let i = 0; i < length; i++) {
    result += possibleCharacters.charAt(
      Math.floor(Math.random() * charactersLength)
    );
  }
  return result;
}
</script>

<style scoped lang="scss">
.line {
  width: 100%;
  height: 1px;
  background-color: var(--color-primary);
  margin: 1rem 0;
}
ol {
  padding: 0;
  margin: 0 auto;
}
</style>
