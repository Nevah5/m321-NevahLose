<script setup lang="ts">
import { RouterView } from "vue-router";
import HeaderComponent from "./components/HeaderComponent.vue";
import FooterComponent from "./components/FooterComponent.vue";
import LoadingOverlay from "./components/LoadingOverlay.vue";
import ToastOverlay from "./components/ToastOverlay.vue";
</script>

<template>
  <div>
    <ToastOverlay />
    <LoadingOverlay />
    <HeaderComponent />
    <RouterView />
    <FooterComponent />
  </div>
</template>
