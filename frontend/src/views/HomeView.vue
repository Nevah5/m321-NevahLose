<template>
  <main>
    <LoadingOverlay :enabled="isLoading" />
    <h1>Welcome!</h1>
  </main>
</template>

<script lang="ts" setup>
import { onMounted, ref } from "vue";
import router from "@/router";
import LoadingOverlay from "@/components/LoadingOverlay.vue";

const isLoading = ref(false);

onMounted(() => {
  isLoading.value = true;
  const token = localStorage.getItem("token");
  if (token == null || token == "") {
    setTimeout(() => {
      isLoading.value = false;
      router.push("/signup");
    }, 1000);
  }
});
</script>
