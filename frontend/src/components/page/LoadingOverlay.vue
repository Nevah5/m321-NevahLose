<template>
  <div class="loading-overlay" v-if="enabled">
    <div class="center">
      <h3>Welcome to</h3>
      <LogoIcon :logoWidth="300" :disableLink="true" />
      <h2>API Aces</h2>
      <div class="loading-bar"></div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import LogoIcon from "@/components/icons/LogoIcon.vue";

const { enabled = false } = defineProps<{
  enabled?: boolean;
}>();
</script>

<style lang="scss" scoped>
.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  height: 100vh;
  width: 100vw;
  background-color: var(--color-background);
  z-index: 999;
  display: flex;
  justify-content: center;
  align-items: center;

  .center {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }
}

.loading-bar {
  $loading-bar-height: 7px;
  width: 100%;
  height: $loading-bar-height;
  border-radius: 0.5rem;
  margin-top: 2.5rem;
  overflow: hidden;
  background-color: var(--color-primary);
  position: relative;

  &::after {
    position: absolute;
    top: 0;
    content: "";
    display: block;
    height: $loading-bar-height;
    width: 100%;
    animation: loading 1s 0s infinite ease-in-out;
    background: linear-gradient(
      to right,
      var(--color-primary) 0%,
      var(--color-primary) 10%,
      var(--color-background) 45%,
      var(--color-background) 55%,
      var(--color-primary) 90%,
      var(--color-primary) 100%
    );
  }
  @keyframes loading {
    0% {
      transform: translateX(-100%);
    }
    100% {
      transform: translateX(100%);
    }
  }
}
</style>
