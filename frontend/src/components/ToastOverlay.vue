<template>
  <div class="toasts">
    <div
      class="toast"
      v-for="toast in activeToasts"
      :key="toast.id"
      :id="'toast-' + toast.id"
      @mouseover="stopTimeout(toast)"
      @mouseleave="resumeTimeout(toast)"
    >
      <div class="background" style="width: 100%"></div>
      <img alt="Warning Icon" class="icon" :src="warningIcon" :width="50" />
      <h4>{{ toast.title }}</h4>
      <p>{{ toast.message }}</p>
      <CloseButton :size="25" @click="removeToast(toast)" />
    </div>
  </div>
</template>

<script setup lang="ts">
import toastApi from "@/api/toastApi";
import type { Toast, ToastTechnical } from "@/api/toastApi";
import { onMounted, ref } from "vue";
import CloseButton from "@/components/icons/CloseButton.vue";
import warningIcon from "@/assets/warning.svg";

const DEFAULT_TIMEOUT = 5000;

const activeToasts = ref<ToastTechnical[]>([]);

onMounted(() => {
  toastApi.subscribe((toast: Toast) => {
    const technicalToast: ToastTechnical = {
      ...toast,
      id: (Math.random() * 1000000).toFixed(0),
    };
    renewToastTimeoutUpdater(technicalToast);
    activeToasts.value.push(technicalToast);
  });
});

const renewToastTimeoutUpdater = (toast: ToastTechnical) => {
  clearInterval(toast.timeoutUpdaterRef);
  toast.timeoutStarted = Date.now();
  toast.timeoutUpdaterRef = setInterval(() => {
    const toastElement = document
      .getElementById(`toast-${toast.id}`)
      ?.querySelector(".background") as HTMLElement;
    if (toastElement) {
      const timeoutDelay = toast.timeout ?? DEFAULT_TIMEOUT;
      const timeLeft = timeoutDelay - (Date.now() - toast.timeoutStarted!);
      if (timeLeft <= 0) {
        removeToast(toast);
      } else {
        toastElement.style.transition = `width ${timeLeft}ms linear`;
        toastElement.style.width = "0";
      }
    }
  }, 10);
};

const stopTimeout = (toast: ToastTechnical) => {
  clearTimeout(toast.timeoutRef);
  clearInterval(toast.timeoutUpdaterRef);

  const toastElement = document
    .getElementById(`toast-${toast.id}`)
    ?.querySelector(".background") as HTMLElement;
  toastElement.style.transition = "";
  toastElement.style.width = "100%";
};

const resumeTimeout = (toast: ToastTechnical) => {
  renewToastTimeoutUpdater(toast);
};

const removeToast = (toast: ToastTechnical) => {
  clearInterval(toast.timeoutUpdaterRef);
  activeToasts.value = activeToasts.value.filter(
    (activeToast) => activeToast.id !== toast.id
  );
};
</script>

<style scoped lang="scss">
.toasts {
  position: fixed;
  display: flex;
  justify-content: flex-end;
  align-items: flex-end;
  flex-direction: column;
  bottom: 0;
  right: 0;
  z-index: 9999;

  .toast {
    display: grid;
    grid-template-areas:
      ". . . ."
      ". icon title ."
      ". icon message ."
      ". . . .";
    grid-template-columns: 1fr 60px 275px 1fr;
    grid-template-rows: 1fr 20px 40px 1fr;
    position: relative;
    box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
    background: var(--color-warning-light);
    border-radius: 15px;
    padding: 20px;
    overflow: hidden;
    margin: 10px 20px 0;

    &:last-child {
      margin-bottom: 60px;
    }

    img {
      grid-area: icon;
      margin: auto;
      z-index: 1;
    }
    h4 {
      font-size: 16px;
      grid-area: title;
      color: var(--color-background);
      z-index: 1;
    }
    p {
      font-size: 14px;
      grid-area: message;
      color: var(--color-background);
      z-index: 1;
    }

    .background {
      background-color: var(--color-warning);
      position: absolute;
      top: 0;
      left: 0;
      height: 100%;
      z-index: 0;
    }

    .close {
      position: absolute;
      top: 10px;
      right: 10px;
      padding: 5px;
      z-index: 1;
    }
  }
}
</style>
