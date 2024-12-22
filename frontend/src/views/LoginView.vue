<template>
  <LoadingOverlay :enabled="isLoading" v-if="isLoading" />
  <div class="wrapper" v-else>
    <form @submit.prevent="confirm">
      <h2 v-if="!currentUsername">What's your name?</h2>
      <h2 v-else>Hey, {{ currentUsername }}!</h2>
      <p v-if="currentUsername">
        You are already logged in, if you want to logout, click
        <a href="#logout" @click="logout">here</a>.
      </p>
      <p v-else>
        By pressing confirm below, you agree to the
        <router-link to="/">Terms of Use</router-link>.
      </p>
      <input
        v-if="!currentUsername"
        type="text"
        name="username"
        :placeholder="usernamePlaceholder"
        maxlength="20"
        v-model="username"
        :disabled="isConfirmed"
      />
      <button v-if="!currentUsername" type="submit" :disabled="isConfirmed">
        <slot v-if="!isConfirmed">Confirm</slot>
        <slot v-if="isConfirmed"><LoadingIcon height="13" /></slot>
      </button>
    </form>
  </div>
</template>

<script lang="ts" setup>
import LoadingOverlay from "@/components/LoadingOverlay.vue";
import LoadingIcon from "@/components/icons/LoadingIcon.vue";
import { onMounted, ref } from "vue";
import { playerService } from "@/api";
import { useRouter } from "vue-router";
import type { ApiError, TokenResponse } from "@/api/types";

const isLoading = ref(true);
const isConfirmed = ref(false);
const currentUsername = ref("");
const username = ref("");
const usernamePlaceholder = ref("");
const router = useRouter();

const confirm = () => {
  if (username.value === "") {
    username.value = usernamePlaceholder.value;
  } else if (username.value.length < 4) {
    alert(`Please enter a username with the minimum length of 4 characters.`);
    return;
  }
  isConfirmed.value = true;
  login();
};

const login = async () => {
  try {
    const token: TokenResponse = await playerService.register(username.value);
    localStorage.setItem("token", token.token);
    localStorage.setItem(
      "expires_in",
      (Date.now() + token.expires_in * 1000).toString()
    );
    setTimeout(() => {
      isConfirmed.value = false;
      router.push("/");
    }, Math.floor(Math.random() * 1000));
  } catch (e: ApiError | any) {
    // TODO: emit toast error
    alert(e.message);
    isConfirmed.value = false;
  }
};

const logout = () => {
  localStorage.clear();
  router.push("/");
};

onMounted(async () => {
  usernamePlaceholder.value = await playerService.getRandomUsername();
  isLoading.value = false;

  const name = localStorage.getItem("username");
  if (name != "") {
    currentUsername.value = name as string;
  }
});
</script>

<style lang="scss" scoped>
.wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 30vh;
}

$input-gap: 0.5rem;
input[type="text"] {
  width: calc(70% - $input-gap / 2);
  padding: 10px;
  margin: 10px 0;
  margin-right: $input-gap;
  border: solid 2px var(--vt-c-stratos);
  border-radius: 0.5rem;
}

button[type="submit"] {
  width: calc(30% - $input-gap / 2);
  height: 40px;
  max-height: 40px;
  padding: 10px;
  margin: 10px 0;
  border: 1px solid var(--vt-c-stratos);
  background-color: var(--vt-c-stratos);
  color: #fff;
  font-weight: 1000;
  cursor: pointer;
  border-radius: 0.5rem;

  .loading-icon {
    max-height: 100%;
    scale: 2;
  }
}
</style>
