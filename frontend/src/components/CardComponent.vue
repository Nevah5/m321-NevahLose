<template>
  <div
    :class="'card card-' + type + (hoverEffect ? ' card-hover' : '')"
    ref="card"
  >
    <div class="card__content">
      <section class="top">
        <div class="title">
          <span class="title-name">{{ name }}</span>
          <span class="method">{{ type.toUpperCase() }}</span>
        </div>
        <div class="card-glow" v-if="hoverEffect" ref="card-glow"></div>
        <div class="subject" :style="cardSubjectStyle"></div>
        <div class="background" :style="cardTopStyle"></div>
      </section>
      <section class="middle">
        <p>{{ description }}</p>
      </section>
      <section class="bottom">
        <LogoIcon :logoWidth="50" :disableLink="true" logoStyle="light" />
        <p>API Aces</p>
      </section>
    </div>
  </div>
</template>

<script lang="ts" setup>
import LogoIcon from "./icons/LogoIcon.vue";
import { onMounted, ref, useTemplateRef } from "vue";

const card = useTemplateRef<HTMLDivElement>("card");
const cardGlow = useTemplateRef<HTMLDivElement>("card-glow");
const cardTopStyle = ref({});
const cardSubjectStyle = ref({});
const {
  type = "options",
  name = "placeholder",
  backgroundName = "TheWatcher.webp",
  subjectName = "TheWatcher_subject.webp",
  description = "This is a placeholder description",
  hoverEffect = true,
} = defineProps<{
  type: string;
  name: string;
  backgroundName: string;
  subjectName: string;
  description: string;
  hoverEffect?: boolean;
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
  setupHoverEffect();
});

let bounds: DOMRect;

const setupHoverEffect = () => {
  if (!hoverEffect) return;
  if (card.value === undefined) return;
  card.value!.addEventListener("mouseenter", () => {
    bounds = card.value!.getBoundingClientRect();
    document.addEventListener("mousemove", rotateToMouse);
  });

  card.value!.addEventListener("mouseleave", () => {
    document.removeEventListener("mousemove", rotateToMouse);
    card.value!.style.transform = "";
    card.value!.style.background = "";
    cardGlow.value!.style.backgroundImage = "";
  });
};

const rotateToMouse = (e: MouseEvent) => {
  const mouseX = e.clientX;
  const mouseY = e.clientY;
  const leftX = mouseX - bounds.x;
  const topY = mouseY - bounds.y;
  const center = {
    x: leftX - bounds.width / 2,
    y: topY - bounds.height / 2,
  };
  const distance = Math.sqrt(center.x ** 2 + center.y ** 2);

  card.value!.style.transform = `
    scale3d(1.07, 1.07, 1.07)
    rotate3d(
      ${center.y / 100},
      ${-center.x / 100},
      0,
      ${Math.log(distance) * 2}deg
    )
  `;

  cardGlow.value!.style.backgroundImage = `
    radial-gradient(
      circle at
      ${center.x * 2 + bounds.width / 2}px
      ${center.y * 2 + bounds.height / 2}px,
      #ffffff55,
      #0000000f
    )
  `;
};
</script>

<style lang="scss" scoped>
@use "sass:math";
.card {
  $card-aspect-ratio-width: 3;
  $card-aspect-ratio-height: 4;
  $corner-cut-radius: 20px;
  $card-width: 250px;
  $card-height: calc(
    $card-width / $card-aspect-ratio-width * $card-aspect-ratio-height
  );
  $card-padding: 9.6px;
  position: relative;
  aspect-ratio: calc($card-aspect-ratio-width / $card-aspect-ratio-height);
  width: $card-width;
  border-radius: 15px;
  overflow: hidden;
  box-shadow: 0 0 12px 0px rgba(0, 0, 0, 0.2);
  transition-duration: 300ms;
  transition-property: transform, box-shadow;
  transition-timing-function: ease-out;
  transform: rotate3d(0);

  &-hover {
    &:hover {
      transition-duration: 150ms;
      transform: scale(1.05);
      box-shadow: 0 5px 20px 5px #00000044;
    }

    & .card-glow {
      position: absolute;
      width: 100%;
      height: 100%;
      left: 0;
      top: 5%;
      z-index: 9;
      transition: background-image 150ms;

      background-image: radial-gradient(
        circle at 50% -20%,
        #ffffff22,
        #0000000f
      );

      $image-width: calc($card-width - $card-padding * 2);
      $image-height: calc($card-height * 0.5);
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

  .card__content {
    position: absolute;
    top: 0;
    left: 0;
    height: 100%;
    width: 100%;
    padding: $card-padding;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    align-items: center;

    section {
      width: 100%;
      position: relative;

      &.top {
        overflow: hidden;
        $image-width: calc($card-width - $card-padding * 2);
        $image-height: calc($card-height * 0.5);

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
          width: 100%;
          left: 50%;
          top: 5%;
          transform: translateX(-50%);
          background-position: top;
          background-size: contain;
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
        $text-height: calc($card-height * 0.3);
        height: $text-height;
        display: flex;
        justify-content: center;
        align-items: flex-start;
        background-color: var(--color-background);
        color: var(--color-text);
        font-size: 1.5rem;
        font-weight: bold;
        position: relative;
        $text-width: $card-width - $card-padding * 2;
        clip-path: polygon(
          0 0,
          math.percentage(
              calc(1 / $text-width * ($text-width - $corner-cut-radius))
            )
            0,
          100% math.percentage(calc(1 / $text-height * $corner-cut-radius)),
          100% 100%,
          math.percentage(calc(1 / $text-width * $corner-cut-radius)) 100%,
          0
            math.percentage(
              calc(1 / $text-height * ($text-height - $corner-cut-radius))
            )
        );

        p {
          text-align: center;
          font-size: 12px;
          width: 80%;
          margin-top: calc($text-height * 0.382 - 12px);
        }
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
}
</style>
