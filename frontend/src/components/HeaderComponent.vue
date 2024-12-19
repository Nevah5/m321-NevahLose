<template>
  <header>
    <LogoIcon />

    <nav>
      <RouterLink to="/">Home</RouterLink>
      <RouterLink to="/signup">Singup</RouterLink>
    </nav>
  </header>
</template>

<script setup>
import LogoIcon from "./icons/LogoIcon.vue";
</script>

<style lang="scss" scoped>
header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  background-color: #f8f9fa;
  width: 100vw;
}
</style>
