<template>
  <button @click="click" class="start-button">Start Game</button>
</template>

<script setup lang="ts">
import toastApi from "@/api/toastApi";

const click = () => {
  localStorage.removeItem("isHost"); // run when switching view
  toastApi.emit({
    title: "Game started",
    message: "Good luck!",
  });
};
</script>

<style scoped lang="scss">
button {
  padding: 0.6rem 1.5em;
  background-color: var(--color-api-method-get);
  outline: none;
  border: none;
  color: var(--color-background);
  border-radius: 5px;
  font-weight: bold;
  cursor: pointer;
}
</style>
