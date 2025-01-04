<template>
  <main>
    <LoadingOverlay :enabled="isLoading" />
    <h1>Game id {{ gameId }}</h1>
  </main>
</template>

<script setup lang="ts">
import { gameService } from "@/api";
import LoadingOverlay from "@/components/LoadingOverlay.vue";
import { onMounted, ref } from "vue";
import { useRoute } from "vue-router";

const route = useRoute();
const gameId = ref<string>("");
const isLoading = ref(true);

onMounted(() => {
  gameId.value = route.params.id as string;
  isLoading.value = true;
  const token = localStorage.getItem("token");
  gameService
    .connectWebsocket(token!)
    .then(() => {
      isLoading.value = false;
    })
    .catch((error) => {
      window.alert(error);
    });
});
</script>

<style scoped>
h1 {
  font-size: 2rem;
  margin: 0;
}
</style>
