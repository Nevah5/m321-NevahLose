<template>
  <div :class="'card card-' + type">
    <section class="top">
      <div class="title">
        <span class="title-name">{{ name }}</span>
        <span class="method">{{ type.toUpperCase() }}</span>
      </div>
      <div class="subject" :style="cardSubjectStyle"></div>
      <div class="background" :style="cardTopStyle"></div>
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
@use "sass:math";
.card {
  $card-aspect-ratio-width: 3;
  $card-aspect-ratio-height: 4;
  $card-width: 250px;
  $card-height: calc(
    $card-width / $card-aspect-ratio-width * $card-aspect-ratio-height
  );
  $card-padding: 9.6px;
  aspect-ratio: calc($card-aspect-ratio-width / $card-aspect-ratio-height);
  position: relative;
  width: $card-width;
  border-radius: 15px;
  overflow: hidden;
  box-shadow: 10px 10px 12px 0px rgba(0, 0, 0, 0.25);
  padding: $card-padding;
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

  section {
    width: 100%;
    position: relative;

    &.top {
      overflow: hidden;
      $image-width: calc($card-width - $card-padding * 2);
      $image-height: calc($card-height * 0.5);
      $corner-cut-radius: 20px;

      // only cut bottom, because of subject image
      clip-path: polygon(
        0 0,
        100% 0,
        100%
          math.percentage(
            calc(1 / $image-height * ($image-height - $corner-cut-radius))
          ),
        math.percentage(
            calc(1 / $image-width * ($image-width - $corner-cut-radius))
          )
          100%,
        math.percentage(calc(1 / $image-width * $corner-cut-radius)) 100%,
        0
          math.percentage(
            calc(1 / $image-height * ($image-height - $corner-cut-radius))
          )
      );

      div.title {
        position: absolute;
        top: 5%;
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
        z-index: 10;
      }

      div.subject {
        position: absolute;
        height: 110%;
        width: 90%;
        left: 50%;
        top: 5%;
        transform: translateX(-50%);
        background-position: top;
        background-size: cover;
        background-repeat: no-repeat;
        z-index: 20;
      }
      div.background {
        height: $image-height;
        margin-top: 5%;
        background-position: top;
        background-size: cover;
        background-repeat: no-repeat;
        clip-path: polygon(
          0
            math.percentage(
              calc(1 / $image-height * ($image-height - $corner-cut-radius))
            ),
          0 math.percentage(calc(1 / $image-height * $corner-cut-radius)),
          math.percentage(calc(1 / $image-width * $corner-cut-radius)) 0,
          math.percentage(
              calc(1 / $image-width * ($image-width - $corner-cut-radius))
            )
            0,
          100% math.percentage(calc(1 / $image-height * $corner-cut-radius)),
          100%
            math.percentage(
              calc(1 / $image-height * ($image-height - $corner-cut-radius))
            ),
          math.percentage(
              calc(1 / $image-width * ($image-width - $corner-cut-radius))
            )
            100%,
          math.percentage(calc(1 / $image-width * $corner-cut-radius)) 100%
        );
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
