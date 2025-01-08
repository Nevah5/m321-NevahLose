<template>
  <div class="wrapper">
    <form>
      <h2>Join Game</h2>
      <div class="room-id-input" ref="inputs">
        <input type="text" maxlength="1" :disabled="isLoadingGame" />
        <input type="text" maxlength="1" :disabled="isLoadingGame" />
        <input
          class="space"
          type="text"
          maxlength="1"
          :disabled="isLoadingGame"
        />
        <input type="text" maxlength="1" :disabled="isLoadingGame" />
        <input type="text" maxlength="1" :disabled="isLoadingGame" />
        <input type="text" maxlength="1" :disabled="isLoadingGame" />
        <div id="loading-background" v-if="isLoadingGame"></div>
        <LoadingIcon v-if="isLoadingGame" :dark="true" />
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
import { gameService } from "@/api";
import { type Game, type ApiError } from "@/api/types.ts";
import toastApi from "@/api/toastApi";

const inputWrapper = useTemplateRef<HTMLInputElement>("inputs");
const inputs = ref<NodeListOf<HTMLInputElement>>();
const isLoadingNewGame = ref(false);
const isLoadingGame = ref(false);
const router = useRouter();

onMounted(() => {
  inputs.value = inputWrapper.value!.querySelectorAll(
    "input"
  ) as NodeListOf<HTMLInputElement>;
  inputs.value!.forEach((input) => {
    input.addEventListener("keydown", handleInput);
    input.addEventListener("paste", handlePaste);
  });
});

const handleInput = (e: KeyboardEvent) => {
  let target = e.target as HTMLInputElement;
  if (e.key === "Backspace") {
    if (target.value === "") {
      // if already empty, focus previous
      const previous: HTMLInputElement | null =
        target.previousElementSibling as HTMLInputElement;
      if (previous == null) return;
      target = previous;
      previous.focus();
    }
    const previous: HTMLInputElement | null =
      target.previousElementSibling as HTMLInputElement;
    if (previous == null) return;
    setTimeout(() => previous.focus(), 1);
  } else if (/\d/.test(e.key)) {
    if (target.value !== "") {
      // if already filled, replace
      target.value = e.key;
    }
    const next: HTMLInputElement | null =
      target.nextElementSibling as HTMLInputElement;
    setTimeout(() => {
      let code = "";
      inputs.value!.forEach((input) => (code += input.value));
      if (next == null && code.length === 6) {
        joinGame(code);
        return;
      }
      next.focus();
    }, 1);
  }
};

const handlePaste = (e: ClipboardEvent) => {
  const paste = e.clipboardData?.getData("text");
  if (paste && paste.length <= 6) {
    const pasteArray = paste.split("");
    inputs.value!.forEach((input, index) => {
      input.value = pasteArray[index];
    });
    inputs.value![pasteArray.length - 1].focus();
    joinGame(paste);
  }
};

const joinGame = (roomId: string) => {
  isLoadingGame.value = true;
  const token = localStorage.getItem("token");
  gameService
    .getGameFromRoomId(roomId, token!)
    .then((game) => {
      localStorage.setItem("game_roomId", game.roomId);
      router.push("/game/" + game.id);
    })
    .catch((e: ApiError | any) => {
      isLoadingGame.value = false;
      toastApi.emit({
        title: "Issue with joining the game",
        message: e.message,
      });
    });
};

const createGame = async () => {
  isLoadingNewGame.value = true;

  try {
    const token = localStorage.getItem("token");
    const game: Game = await gameService.createGame(token!);
    localStorage.setItem("game_roomId", game.roomId);
    setTimeout(() => {
      router.push("/game/" + game.id);
    }, Math.floor(Math.random() * 1000));
  } catch (e: ApiError | any) {
    toastApi.emit({
      title: "Game creation error",
      message: e.message,
    });
    isLoadingNewGame.value = false;
  }
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
    position: relative;

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
    #loading-background {
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background-color: rgba(255, 255, 255, 0.5);
      z-index: 1;
    }
    img {
      position: absolute;
      top: 50%;
      left: 50%;
      height: 90%;
      aspect-ratio: 1 / 1;
      transform: translate(-50%, -50%);
      z-index: 2;
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
