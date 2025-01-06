<template>
  <div class="invite-code">
    <h3>Invite Code</h3>
    <TooltipComponent text="Click to copy!">
      <span>{{ code.substring(0, 3) }} {{ code.substring(3) }}</span>
    </TooltipComponent>
  </div>
</template>

<script setup lang="ts">
import { ref, defineProps } from "vue";
import TooltipComponent from "@/components/TooltipComponent.vue";

const { code } = defineProps<{
  code: string;
}>();
</script>

<style scoped lang="scss">
.invite-code {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  cursor: pointer;

  h3 {
    font-size: 1.2rem;
    margin-bottom: -15px;
  }
  span {
    font-weight: bold;
    font-size: 2.3rem;
  }
}
</style>
