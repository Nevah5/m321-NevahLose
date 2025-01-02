<template>
  <div class="wrapper">
    <form>
      <h2>Join Game</h2>
      <div class="room-id-input" ref="inputs">
        <input type="text" maxlength="1" v-model="num1" />
        <input type="text" maxlength="1" v-model="num2" />
        <input class="space" type="text" maxlength="1" v-model="num3" />
        <input type="text" maxlength="1" v-model="num4" />
        <input type="text" maxlength="1" v-model="num5" />
        <input type="text" maxlength="1" v-model="num6" />
      </div>
    </form>
    <span>OR</span>
    <form @submit.prevent="createGame">
      <button type="submit" :disabled="isLoadingNewGame">
        <slot v-if="!isLoadingNewGame">Create Game</slot>
        <slot v-if="isLoadingNewGame"><LoadingIcon /></slot>
      </button>
    </form>
  </div>
</template>

<script setup lang="ts">
import LoadingIcon from "@/components/icons/LoadingIcon.vue";
import { onMounted, ref, useTemplateRef } from "vue";
import { useRouter } from "vue-router";

const inputWrapper = useTemplateRef<HTMLInputElement>("inputs");
const inputs = ref<NodeListOf<HTMLInputElement>>();
const num1 = ref("");
const num2 = ref("");
const num3 = ref("");
const num4 = ref("");
const num5 = ref("");
const num6 = ref("");
const isLoadingNewGame = ref(false);
const router = useRouter();

onMounted(() => {
  inputs.value = inputWrapper.value!.querySelectorAll(
    "input"
  ) as NodeListOf<HTMLInputElement>;
  inputs.value!.forEach((input) => {
    input.addEventListener("keyup", handleInput);
    input.addEventListener("paste", handlePaste);
  });
});

const handleInput = (e: KeyboardEvent) => {
  e.preventDefault();
  const target = e.target as HTMLInputElement;
  if (e.key === "Backspace") {
    const previous: HTMLInputElement | null =
      target.previousElementSibling as HTMLInputElement;
    if (previous == null) return;
    previous.focus();
  } else if (/\d/.test(e.key)) {
    const next: HTMLInputElement | null =
      target.nextElementSibling as HTMLInputElement;
    if (next == null) {
      //TODO: submit
      return;
    }
    next.focus();
  }
};

const handlePaste = (e: ClipboardEvent) => {
  e.preventDefault();
  const paste = e.clipboardData?.getData("text");
  if (paste && paste.length <= 6) {
    const pasteArray = paste.split("");
    inputs.value!.forEach((input, index) => {
      input.value = pasteArray[index];
    });
    inputs.value![pasteArray.length - 1].focus();
    // TODO: submit
  }
};

const createGame = () => {
  isLoadingNewGame.value = true;

  // TODO: send the create game request
};
</script>

<style lang="scss" scoped>
.wrapper {
  width: 100%;
  height: 80vh;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;

  form {
    width: 338px;
  }

  .room-id-input {
    input {
      height: 50px;
      width: 50px;
      font-size: 32px;
      padding: 10px;
      text-align: center;
      border-radius: 0.5rem;
      margin: 2px;
      border: 2px solid var(--color-primary);
      background: var(--color-background);
      font-weight: bold;
      color: var(--color-primary);
      outline: none;
      transition: all 0.1s;

      &:focus {
        box-shadow: 0 0 2px 2px rgba(var(--color-primary), 0.4);
      }
      &.disabled {
        opacity: 0.5;
      }
      &.space {
        margin-right: 1rem !important;
      }
    }
  }
  span {
    margin: 1rem 0;
  }
  button[type="submit"] {
    width: 100%;
    height: 36px;
    background-color: var(--color-primary);
    color: var(--color-background);
    outline: none;
    border: none;
    cursor: pointer;
    font-weight: bold;
    font-size: 16px;
    padding: 0.5rem 1rem;
    border-radius: 0.5rem;
    position: relative;

    img {
      position: absolute;
      top: 50%;
      left: 50%;
      height: 90%;
      aspect-ratio: 1 / 1;
      transform: translate(-50%, -50%);
    }
  }
}
</style>
