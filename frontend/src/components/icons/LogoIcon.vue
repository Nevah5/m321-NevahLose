<template>
  <router-link to="/" class="logo" v-if="!disableLink">
    <img
      alt="API Aces Logo"
      class="logo"
      :src="logoStyle === 'dark' ? logoDark : logoLight"
      :width="logoWidth"
    />
  </router-link>
  <img
    v-else
    alt="API Aces Logo"
    class="logo"
    :src="logoStyle === 'dark' ? logoDark : logoLight"
    :width="logoWidth"
  />
</template>

<script lang="ts" setup>
import { RouterLink } from "vue-router";
import logoLight from "@/assets/logo_light.svg";
import logoDark from "@/assets/logo.svg";

const {
  logoWidth = 120,
  disableLink = false,
  logoStyle = "light",
} = defineProps<{
  logoWidth?: number;
  disableLink?: boolean;
  logoStyle?: "dark" | "light";
}>();
</script>
