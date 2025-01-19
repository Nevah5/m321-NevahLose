<template>
  <h2 class="countdown">{{ countdown }}</h2>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";

const { seconds } = defineProps<{
  seconds: number;
}>();

const countdown = ref(seconds);
onMounted(() => {
  const interval = setInterval(() => {
    countdown.value--;
    if (countdown.value === 0) {
      clearInterval(interval);
    }
  }, 1000);
});
</script>
