<template>
  <main>
    <LoadingOverlay :enabled="isLoading" />
    <h1>Game id {{ gameId }}</h1>
  </main>
</template>

<script setup lang="ts">
import LoadingOverlay from "@/components/LoadingOverlay.vue";
import { onMounted, ref } from "vue";
import { useRoute } from "vue-router";

const route = useRoute();
const gameId = ref<string>("");
const isLoading = ref(true);

onMounted(() => {
  gameId.value = route.params.id as string;
  isLoading.value = true;
});
</script>

<style scoped>
h1 {
  font-size: 2rem;
  margin: 0;
}
</style>
