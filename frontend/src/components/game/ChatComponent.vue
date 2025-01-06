<template>
  <div class="chat-box">
    <div class="history">
      <p
        v-for="m in history"
        :key="m.id"
        :class="'message' + m.type == 'activity' ? ' activity-message' : ''"
      >
        <slot v-if="m.type == 'activity'"
          ><b>{{ m.sender }}</b> joined.</slot
        >
        <slot v-else>
          <b>{{ m.sender }}</b
          >: {{ m.message }}
        </slot>
      </p>
      <p class="message activity-message"></p>
    </div>
    <form @submit.prevent="sendMessage">
      <input
        type="text"
        name="message"
        id="message-input"
        v-model="message"
        placeholder="Type your chat message here..."
      />
      <button type="submit">
        <slot><PlaneIcon color="white" :iconWidth="20" /></slot>
      </button>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import PlaneIcon from "../icons/PlaneIcon.vue";

interface ChatMessage {
  id: string;
  sender: string;
  type: "activity" | "message";
  joined?: boolean;
  message?: string;
}

const message = ref("");
const history = ref<ChatMessage[]>([]);

const sendMessage = () => {
  history.value.push({
    id: Math.random().toString(36).substring(2, 9),
    sender: localStorage.getItem("username") || "Player",
    type: "message",
    message: message.value,
  });
  message.value = "";
};
</script>

<style scoped lang="scss">
.chat-box {
  display: flex;
  flex-direction: column;

  .history {
    height: 30%;
    overflow-y: auto;
    margin-bottom: 10px;

    .message.activity-message {
      display: flex;
      gap: 0.5em;
      margin: 0.5em 0;
    }
  }

  form {
    display: flex;
    justify-content: flex-start;
    gap: 0.3em;

    input {
      height: 40px;
      border: 2px solid var(--color-text);
      outline: none;
      width: 240px;
      border-radius: 0.4em;
      padding-left: 10px;
    }
    button[type="submit"] {
      background-color: var(--color-text);
      padding: 5px;
      width: 40px;
      height: 40px;
      border: none;
      cursor: pointer;
      border-radius: 0.4em;
      display: flex;
      justify-content: center;
      align-items: center;
    }
  }
}
</style>
