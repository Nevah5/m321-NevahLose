<template>
  <div
    class="tooltip-container"
    @mouseover="showTooltip"
    @mouseleave="hideTooltip"
  >
    <slot></slot>
    <div
      v-if="isVisible"
      class="tooltip"
      :class="[position, color]"
      ref="tooltip"
      v-motion-pop
      @mouseover="showTooltip"
      @mouseleave="hideTooltip"
    >
      {{ text }}
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, defineProps } from "vue";

const {
  text,
  position = "top",
  color = "blue",
} = defineProps<{
  text: string;
  position?: "top" | "right" | "bottom" | "left";
  color?: "green" | "blue";
}>();

const isVisible = ref(false);

function showTooltip() {
  isVisible.value = true;
}

function hideTooltip() {
  isVisible.value = false;
}
</script>

<style scoped lang="scss">
.tooltip-container {
  position: relative;
  display: inline-block;

  .tooltip {
    --tooltip-color: var(--color-text);
    --tooltip-text-color: var(--color-background);
    position: absolute;
    background-color: var(--tooltip-color);
    color: var(--tooltip-text-color);
    padding: 8px;
    border-radius: 4px;
    font-size: 12px;
    white-space: nowrap;
    z-index: 1000;

    &::after {
      position: absolute;
      content: "";
      display: block;
      width: 0;
      height: 0;
      border-style: solid;
      border-color: transparent;
    }

    &.green {
      --tooltip-color: #73cd5c;
    }

    &.top {
      bottom: 100%;
      left: 50%;
      transform: translateX(-50%) !important;
      margin-bottom: 8px;

      &::after {
        border-left: 5px solid transparent;
        border-right: 5px solid transparent;
        border-top: 5px solid var(--tooltip-color);
        transform: translate(-50%, 100%);
        bottom: 0;
        left: 50%;
      }
    }

    &.right {
      top: 50%;
      left: 100%;
      transform: translateY(-50%) !important;
      margin-left: 8px;

      &::after {
        border-top: 5px solid transparent;
        border-bottom: 5px solid transparent;
        border-right: 5px solid var(--tooltip-color);
        transform: translate(-100%, -50%);
        top: 50%;
        left: 0;
      }
    }

    &.bottom {
      top: 100%;
      left: 50%;
      transform: translateX(-50%) !important;
      margin-top: 8px;

      &::after {
        border-left: 5px solid transparent;
        border-right: 5px solid transparent;
        border-bottom: 5px solid var(--tooltip-color);
        transform: translate(-50%, -100%);
        top: 0;
        left: 50%;
      }
    }

    &.left {
      top: 50%;
      right: 100%;
      transform: translateY(-50%) !important;
      margin-right: 8px;

      &::after {
        border-top: 5px solid transparent;
        border-bottom: 5px solid transparent;
        border-left: 5px solid var(--tooltip-color);
        transform: translate(100%, -50%);
        top: 50%;
        right: 0;
      }
    }
  }
}
</style>
