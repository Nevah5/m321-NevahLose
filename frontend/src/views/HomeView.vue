<template>
  <main>
    <LoadingOverlay :enabled="isLoading" />
    <h1>Welcome {{ username }}</h1>
  </main>
</template>

<script lang="ts" setup>
import { onMounted, ref } from "vue";
import router from "@/router";
import LoadingOverlay from "@/components/LoadingOverlay.vue";
import { getSelf } from "@/api/playerService";
import type Player from "@/api/models/user";

const isLoading = ref(false);
const username = ref("");

onMounted(() => {
  isLoading.value = true;
  const token = localStorage.getItem("token");
  if (token == null || token == "") {
    setTimeout(() => {
      isLoading.value = false;
      router.push("/signup");
    }, 1000);
    return;
  }

  getSelf(token)
    .then((player: Player) => {
      isLoading.value = false;
      localStorage.setItem("playerId", player.id);
      localStorage.setItem("username", player.username);
      username.value = player.username;
    })
    .catch(() => {
      // TODO: implement error Toast on App.vue where you emit "upwards"
      isLoading.value = true;
      localStorage.clear();
      router.push("/signup");
    });
});
</script>
