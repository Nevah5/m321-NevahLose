<template>
  <main>
    <LoadingOverlay :enabled="isLoading" />
    <h1>Welcome, {{ username }}!</h1>
    <RoomManageComponent />
  </main>
</template>

<script lang="ts" setup>
import { onMounted, ref } from "vue";
import LoadingOverlay from "@/components/LoadingOverlay.vue";
import { playerService } from "@/api";
import type { ApiError, Player } from "@/api/types";
import { useRouter } from "vue-router";
import RoomManageComponent from "@/components/RoomManageComponent.vue";

const isLoading = ref(false);
const username = ref("");
const router = useRouter();

onMounted(async () => {
  isLoading.value = true;
  const token = localStorage.getItem("token");
  if (token == null || token == "") {
    setTimeout(() => {
      isLoading.value = false;
      router.push("/login");
    }, 1000);
    return;
  }

  try {
    const player: Player = await playerService.getSelf(token);
    isLoading.value = false;
    localStorage.setItem("playerId", player.id);
    localStorage.setItem("username", player.username);
    username.value = player.username;
  } catch (e: ApiError | any) {
    // TODO: implement error Toast on App.vue where you emit "upwards"
    isLoading.value = true;
    localStorage.clear();
    router.push("/login");
  }
});
</script>
