<template>
  <main>
    <h1>Game id {{ gameId }}</h1>
  </main>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useRoute } from "vue-router";

const route = useRoute();
const gameId = ref<string>("");

onMounted(() => {
  gameId.value = route.params.id;
});
</script>

<style scoped>
h1 {
  font-size: 2rem;
  margin: 0;
}
</style>
