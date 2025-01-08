<template>
  <div
    :class="[
      'card',
      hoverEffect && !isTurned ? ' card-hover' : '',
      'card-' + size,
    ]"
    ref="card"
  >
    <div class="card__back" v-if="isTurned && isTurnable">
      <div class="inner">
        <LogoIcon :logoWidth="100" :disableLink="true" />
      </div>
    </div>
    <div
      class="card__back card-clickable"
      v-else-if="isTurned"
      @click="revealCard"
      ref="card-turn"
    >
      <div class="inner">
        <LogoIcon v-if="!backSideText" :logoWidth="100" :disableLink="true" />
        <p v-else>{{ backSideText }}</p>
      </div>
    </div>
    <div
      :class="'card__content card-' + type + (isTurned ? ' card-turned' : '')"
      ref="card-content"
    >
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
import LogoIcon from "@/components/icons/LogoIcon.vue";
import { onMounted, ref, useTemplateRef, watch } from "vue";

const card = useTemplateRef<HTMLDivElement>("card");
const cardGlow = useTemplateRef<HTMLDivElement>("card-glow");
const cardTurn = useTemplateRef<HTMLDivElement>("card-turn");
const cardContent = useTemplateRef<HTMLDivElement>("card-content");
const cardTopStyle = ref({});
const cardSubjectStyle = ref({});
const isTurned = ref(false);

const {
  type = "options",
  name = "",
  backgroundName = "",
  subjectName = "",
  description = "",
  hoverEffect = true,
  isTurnedProp = false,
  isTurnable = false,
  size = "medium",
  backSideText = null,
} = defineProps<{
  type: string;
  name: string;
  backgroundName: string;
  subjectName: string;
  description: string;
  hoverEffect?: boolean;
  isTurnedProp?: boolean;
  isTurnable?: boolean;
  size?: "medium" | "small" | "large";
  backSideText?: string;
}>();

watch(
  () => backgroundName,
  async (newValue) => {
    if (backgroundName === "") return;
    setupBackgroundImage();
  }
);
watch(
  () => subjectName,
  async (newValue) => {
    if (subjectName === "") return;
    setupSubjectImage();
  }
);

onMounted(async () => {
  isTurned.value = isTurnedProp;

  setupBackgroundImage();
  setupSubjectImage();
  setupHoverEffect();
});

const setupBackgroundImage = async () => {
  if (backgroundName === "") return;
  const backgroundImage = new URL(
    `../../assets/cards/${backgroundName}`,
    import.meta.url
  ).href;

  cardTopStyle.value = {
    backgroundImage: `url(${backgroundImage})`,
  };
};

const setupSubjectImage = async () => {
  if (subjectName === "") return;
  const subjectImage = new URL(
    `../../assets/cards/${subjectName}`,
    import.meta.url
  ).href;
  cardSubjectStyle.value = {
    backgroundImage: `url(${subjectImage})`,
  };
};

let bounds: DOMRect;

const setupHoverEffect = () => {
  if (!hoverEffect) return;
  if (hoverEffect && isTurned.value) return;
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

const revealCard = () => {
  cardTurn.value!.classList.add("card-open");
  cardContent.value!.classList.add("card-turned-open");

  setTimeout(() => {
    isTurned.value = false;
    setupHoverEffect();
  }, 300);
};
</script>

<style lang="scss" scoped>
@use "sass:math";

@keyframes card-flip {
  from {
    transform: rotateY(0deg);
  }
  to {
    transform: rotateY(90deg);
  }
}

.card {
  // dynamic values
  --card-width: 250px;
  --card-height: 333.33px; // width / 3 * 4
  --card-padding: 9.6px;
  --title-font-size: 12px;
  --middle-font-size: 12px;
  --bottom-font-size: 10px;
  --back-font-size: 1.5rem;

  // calculated values
  --image-width: calc(var(--card-width) - var(--card-padding) * 2);
  --image-height: calc(var(--card-height) * 0.5);
  --text-height: calc(var(--card-height) * 0.3);
  --text-margin-top: calc(var(--card-height) * 0.3 * 0.382 - 12px);

  position: relative;
  aspect-ratio: 3 / 4;
  width: var(--card-width);
  border-radius: 15px;
  overflow: hidden;
  transition-duration: 300ms;
  transition-property: transform, box-shadow;
  transition-timing-function: ease-out;
  transform: rotate3d(0);

  &-small {
    // dynamic values
    --card-width: 200px;
    --card-height: 266.66px; // width / 3 * 4
    --card-padding: 9.6px;
    --title-font-size: 10px;
    --middle-font-size: 10px;
    --bottom-font-size: 8px;
    --back-font-size: 1.5rem;

    // calculated values
    --image-width: calc(var(--card-width) - var(--card-padding) * 2);
    --image-height: calc(var(--card-height) * 0.5);
    --text-height: calc(var(--card-height) * 0.3);
    --text-margin-top: calc(var(--card-height) * 0.3 * 0.382 - 12px);
  }

  &-large {
    // dynamic values
    --card-width: 300px;
    --card-height: 400px; // width / 3 * 4
    --card-padding: 9.6px;
    --title-font-size: 16px;
    --middle-font-size: 16px;
    --bottom-font-size: 12px;
    --back-font-size: 1.5rem;

    // calculated values
    --image-width: calc(var(--card-width) - var(--card-padding) * 2);
    --image-height: calc(var(--card-height) * 0.5);
    --text-height: calc(var(--card-height) * 0.3);
    --text-margin-top: calc(var(--card-height) * 0.3 * 0.382 - 12px);
  }
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

      clip-path: polygon(
        10% 0,
        90% 0,
        100% 13.33%,
        100% 86.67%,
        90% 100%,
        10% 100%,
        0 86.67%,
        0 13.33%
      );
    }
  }

  .card__content {
    position: absolute;
    top: 0;
    left: 0;
    height: 100%;
    width: 100%;
    padding: var(--card-padding);
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    align-items: center;
    box-shadow: 0 0 12px 0px rgba(0, 0, 0, 0.2);
    background-color: var(--color-primary);

    &.card-turned {
      transform: rotateY(90deg);

      &-open {
        animation: card-flip 100ms 100ms reverse forwards;
      }
    }
    section {
      width: 100%;
      position: relative;

      &.top {
        overflow: hidden;

        clip-path: polygon(
          10% 0,
          90% 0,
          100% 13.33%,
          100% 86.67%,
          90% 100%,
          10% 100%,
          0 86.67%,
          0 13.33%
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
          font-size: var(--title-font-size);
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
          height: var(--image-height);
          margin-top: 5%;
          background-position: top;
          background-size: cover;
          background-repeat: no-repeat;
          clip-path: polygon(
            10% 0,
            90% 0,
            100% 13.33%,
            100% 86.67%,
            90% 100%,
            10% 100%,
            0 86.67%,
            0 13.33%
          );
        }
      }
      &.middle {
        height: var(--text-height);
        display: flex;
        justify-content: center;
        align-items: flex-start;
        background-color: var(--color-background);
        color: var(--color-text);
        font-weight: bold;
        position: relative;
        clip-path: polygon(
          0 0,
          90% 0,
          100% 22.5%,
          100% 100%,
          10% 100%,
          0 77.5%
        );

        p {
          text-align: center;
          font-size: var(--middle-font-size);
          width: 80%;
          margin-top: var(--text-margin-top);
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
          font-size: var(--bottom-font-size);
        }
      }
    }
    &.card-get {
      div.title span.method {
        color: var(--color-api-method-get);
      }
    }
    &.card-post {
      div.title span.method {
        color: var(--color-api-method-post);
      }
    }
    &.card-put {
      div.title span.method {
        color: var(--color-api-method-put);
      }
    }
    &.card-delete {
      div.title span.method {
        color: var(--color-api-method-delete);
      }
    }
    &.card-options {
      div.title span.method {
        color: var(--color-api-method-options);
      }
    }
  }
  .card__back {
    z-index: 30;
    position: absolute;
    top: 0;
    left: 0;
    height: 100%;
    width: 100%;
    background-color: var(--color-text);
    padding: 0;
    display: flex;
    justify-content: center;
    align-items: center;

    .inner {
      --corner-cut-radius: 35px;
      --inner-width: calc(var(--card-width) - 24px);
      --inner-height: calc(var(--card-height) - 24px);

      display: flex;
      justify-content: center;
      align-items: center;
      width: var(--inner-width);
      height: var(--inner-height);
      background-color: var(--color-background);
      color: var(--color-text);
      font-size: var(--back-font-size);
      font-weight: bold;

      clip-path: polygon(
        13.33% 0,
        86.67% 0,
        100% 10%,
        100% 90%,
        86.67% 100%,
        13.33% 100%,
        0 90%,
        0 10%
      );
    }

    &.card-clickable {
      cursor: pointer;
    }
    &.card-open {
      animation: card-flip 100ms forwards;
    }
  }
}
</style>
