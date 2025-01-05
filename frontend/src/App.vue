<script setup lang="ts">
import { RouterView, useRoute } from "vue-router";
import { ref, watch } from "vue";
import HeaderComponent from "./components/HeaderComponent.vue";
import FooterComponent from "./components/FooterComponent.vue";
import LoadingOverlay from "./components/LoadingOverlay.vue";
import ToastOverlay from "./components/ToastOverlay.vue";

const disableHeader = ref(false);
const disableFooter = ref(false);

const route = useRoute();
watch(route, () => {
  disableHeader.value = route.meta.disableHeader as boolean;
  disableFooter.value = route.meta.disableFooter as boolean;
});
</script>

<template>
  <div>
    <ToastOverlay />
    <LoadingOverlay />
    <HeaderComponent v-if="!disableHeader" />
    <RouterView />
    <FooterComponent v-if="!disableFooter" />
  </div>
</template>
