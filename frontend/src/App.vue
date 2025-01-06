<script setup lang="ts">
import { RouterView, useRoute } from "vue-router";
import { ref, watch } from "vue";
import HeaderComponent from "@/components/page/HeaderComponent.vue";
import FooterComponent from "@/components/page/FooterComponent.vue";
import LoadingOverlay from "@/components/page/LoadingOverlay.vue";
import ToastOverlay from "@/components/page/ToastOverlay.vue";

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
