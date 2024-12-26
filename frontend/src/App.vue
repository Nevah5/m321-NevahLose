<script setup lang="ts">
import { RouterView } from "vue-router";
import HeaderComponent from "./components/HeaderComponent.vue";
import FooterComponent from "./components/FooterComponent.vue";
import LoadingOverlay from "./components/LoadingOverlay.vue";
</script>

<template>
  <div>
    <LoadingOverlay />
    <HeaderComponent />
    <RouterView />
    <FooterComponent />
  </div>
</template>
