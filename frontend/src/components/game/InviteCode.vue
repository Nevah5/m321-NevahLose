<template>
  <div class="invite-code">
    <h3>Invite Code</h3>
    <TooltipComponent :text="tooltipText" :color="tooltipColor">
      <span @click="copyCode"
        >{{ code.substring(0, 3) }} {{ code.substring(3) }}</span
      >
    </TooltipComponent>
  </div>
</template>

<script setup lang="ts">
import { ref, defineProps } from "vue";
import TooltipComponent from "@/components/page/TooltipComponent.vue";

const tooltipText = ref("Click to copy!");
const tooltipColor = ref<"blue" | "green">("blue");

const { code } = defineProps<{
  code: string;
}>();

const copyCode = () => {
  navigator.clipboard.writeText(code);
  tooltipText.value = "Copied!";
  tooltipColor.value = "green";
  setTimeout(() => {
    tooltipText.value = "Click to copy!";
    tooltipColor.value = "blue";
  }, 2000);
};
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
