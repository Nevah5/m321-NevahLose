<template>
  <router-link to="/" class="logo" v-if="!disableLink">
    <img
      alt="API Aces Logo"
      class="logo"
      src="@/assets/logo.svg"
      :width="logoWidth"
    />
  </router-link>
  <img
    v-else
    alt="API Aces Logo"
    class="logo"
    src="@/assets/logo.svg"
    :width="logoWidth"
  />
</template>

<script lang="ts" setup>
import { RouterLink } from "vue-router";
const { logoWidth = 120, disableLink = false } = defineProps<{
  logoWidth?: number;
  disableLink?: boolean;
}>();
</script>
