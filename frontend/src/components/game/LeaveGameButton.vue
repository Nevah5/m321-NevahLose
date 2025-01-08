<template>
  <button
    @click="click"
    :class="['leave-button', confirm ? 'state-confirm' : '']"
  >
    {{ text }}
  </button>
</template>

<script setup lang="ts">
import { ref } from "vue";

const emit = defineEmits(["confirm"]);
const text = ref("Leave game");
const confirm = ref(false);

const click = () => {
  if (confirm.value) {
    console.log("Leaving game");
    emit("confirm");
    return;
  }

  confirm.value = true;
  text.value = "Confirm?";

  setTimeout(() => {
    confirm.value = false;
    text.value = "Leave game";
  }, 3000);
};
</script>

<style scoped lang="scss">
button {
  padding: 0.6rem 1.5em;
  background-color: var(--color-warning-light);
  outline: none;
  border: none;
  color: var(--color-background);
  border-radius: 5px;
  font-weight: bold;
  cursor: pointer;

  &.state-confirm {
    background-color: var(--color-warning);
  }
}
</style>
