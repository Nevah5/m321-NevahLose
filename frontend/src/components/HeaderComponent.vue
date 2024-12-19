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
  width: 100vw;

  .logo {
    width: 10rem;
  }

  nav {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    gap: 1rem;
    width: 40%;
    font-family: "Inter", sans-serif;

    a {
      text-decoration: none;

      &:hover {
        text-decoration: underline;
      }
    }
  }
}
</style>
