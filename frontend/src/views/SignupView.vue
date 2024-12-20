<template>
  <LoadingOverlay :enabled="isLoading" v-if="isLoading" />
  <div class="wrapper" v-else>
    <form @submit.prevent="confirm">
      <h2>What's your name?</h2>
      <p>
        By pressing confirm below, you agree to the
        <router-link to="/">Terms of Use</router-link>.
      </p>
      <input
        type="text"
        name="username"
        :placeholder="usernamePlaceholder"
        maxlength="20"
        v-model="username"
      />
      <input type="submit" value="Confirm" />
    </form>
  </div>
</template>

<script lang="ts" setup>
import LoadingOverlay from "@/components/LoadingOverlay.vue";
import { onMounted, ref } from "vue";
import { getRandomUsername } from "@/api/playerService";

const isLoading = ref(true);
const isConfirmed = ref(false);
const username = ref("");
const usernamePlaceholder = ref("");

const confirm = () => {
  if (username.value === "") {
    username.value = usernamePlaceholder.value;
  } else if (username.value.length < 4) {
    alert(`Please enter a username with the minimum length of 4 characters.`);
    return;
  }
  isConfirmed.value = true;
};
onMounted(async () => {
  usernamePlaceholder.value = await getRandomUsername();
  isLoading.value = false;
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
  margin-right: $input-gap;
  border: solid 2px var(--vt-c-stratos);
  border-radius: 0.5rem;
}

input[type="submit"] {
  width: calc(30% - $input-gap / 2);
  padding: 10px;
  margin: 10px 0;
  border: 1px solid var(--vt-c-stratos);
  background-color: var(--vt-c-stratos);
  color: #fff;
  font-weight: 1000;
  cursor: pointer;
  border-radius: 0.5rem;
}
</style>
