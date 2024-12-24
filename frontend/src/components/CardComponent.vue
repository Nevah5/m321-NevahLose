<template>
  <div :class="'card card-' + type">
    <div class="title">
      <span class="title-name">{{ name }}</span>
      <span class="method">{{ type.toUpperCase() }}</span>
    </div>
    <section class="top" :style="cardTopStyle">
      <div class="subject" :style="cardSubjectStyle"></div>
    </section>
    <section class="middle"></section>
    <section class="bottom">
      <LogoIcon :logoWidth="50" :disableLink="true" logoStyle="light" />
      <p>API Aces</p>
    </section>
  </div>
</template>

<script lang="ts" setup>
import LogoIcon from "./icons/LogoIcon.vue";
import { onMounted, ref } from "vue";

const cardTopStyle = ref({});
const cardSubjectStyle = ref({});
const {
  type = "options",
  name = "placeholder",
  backgroundName = "TheWatcher.webp",
  subjectName = "TheWatcher_subject.webp",
} = defineProps<{
  type: string;
  name: string;
  backgroundName: string;
  subjectName: string;
}>();

onMounted(async () => {
  const backgroundImage = (
    await import(/* @vite-ignore */ `../assets/cards/${backgroundName}`)
  ).default;
  const subjectImage = (
    await import(/* @vite-ignore */ `../assets/cards/${subjectName}`)
  ).default;

  cardTopStyle.value = {
    backgroundImage: `url(${backgroundImage})`,
  };
  cardSubjectStyle.value = {
    backgroundImage: `url(${subjectImage})`,
  };
});
</script>

<style lang="scss" scoped>
.card {
  position: relative;
  aspect-ratio: 3/4;
  width: 250px;
  border-radius: 15px;
  overflow: hidden;
  box-shadow: 10px 10px 12px 0px rgba(0, 0, 0, 0.25);
  padding: 0.6em;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;

  &-get {
    background-color: var(--color-api-method-get);

    div.title span.method {
      color: var(--color-api-method-get);
    }
  }
  &-post {
    background-color: var(--color-api-method-post);

    div.title span.method {
      color: var(--color-api-method-post);
    }
  }
  &-put {
    background-color: var(--color-api-method-put);

    div.title span.method {
      color: var(--color-api-method-put);
    }
  }
  &-delete {
    background-color: var(--color-api-method-delete);

    div.title span.method {
      color: var(--color-api-method-delete);
    }
  }
  &-options {
    background-color: var(--color-api-method-options);

    div.title span.method {
      color: var(--color-api-method-options);
    }
  }

  div.title {
    z-index: 10;
    position: absolute;
    top: 7%;
    left: 50%;
    transform: translate(-50%, -50%);
    display: flex;
    justify-content: space-between;
    align-items: center;
    background-color: var(--color-background);
    font-size: 12px;
    font-weight: bold;
    padding: 1px 10px;
    width: 80%;
  }

  section {
    width: 100%;

    &.top {
      height: 45%;
      position: relative;
      margin-top: 5%;
      background-position: top;
      background-size: cover;
      background-repeat: no-repeat;
      overflow: hidden;
      // TODO: clip path

      div.subject {
        z-index: 11;
        position: absolute;
        height: 110%;
        width: 80%;
        left: 50%;
        transform: translateX(-50%);
        background-position: top;
        background-size: cover;
        background-repeat: no-repeat;
      }
    }
    &.middle {
      height: 30%;
      display: flex;
      justify-content: center;
      align-items: center;
      background-color: var(--color-white);
      color: var(--color-black);
      font-size: 1.5rem;
      font-weight: bold;
    }
    &.bottom {
      height: 5%;
      display: flex;
      justify-content: space-between;
      align-items: flex-end;

      p {
        color: var(--color-background);
        font-weight: bold;
        font-size: 10px;
      }
    }
  }
}
</style>
