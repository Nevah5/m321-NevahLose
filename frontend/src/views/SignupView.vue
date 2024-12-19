<template>
  <div class="wrapper">
    <form>
      <h2>What's your name?</h2>
      <p>
        By pressing confirm below, you agree to the
        <router-link to="/">Terms of Use</router-link>.
      </p>
      <input
        type="text"
        name="username"
        placeholder="LazyCoder123"
        maxlength="20"
      />
      <input type="button" value="Confirm" />
    </form>
  </div>
</template>

<script lang="ts" setup></script>

<style lang="scss" scoped>
.wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 30vh;
}

$input-gap: 0.5rem;
input[type="text"] {
  width: calc(70% - $input-gap / 2);
  padding: 10px;
  margin-right: $input-gap;
  border: solid 2px var(--vt-c-stratos);
  border-radius: 0.5rem;
}

input[type="button"] {
  width: calc(30% - $input-gap / 2);
  padding: 10px;
  margin: 10px 0;
  border: 1px solid var(--vt-c-stratos);
  background-color: var(--vt-c-stratos);
  color: #fff;
  font-weight: 1000;
  cursor: pointer;
  border-radius: 0.5rem;
}
</style>
