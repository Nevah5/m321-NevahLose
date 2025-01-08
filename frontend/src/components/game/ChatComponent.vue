<template>
  <div class="chat-box">
    <div class="history" ref="chatBoxHistory">
      <p
        v-for="m in history"
        :key="m.id"
        :class="'message' + m.type == 'ACTIVITY' ? ' activity-message' : ''"
      >
        <slot v-if="m.type == 'ACTIVITY'"
          ><b>{{ m.senderUsername }}</b
          >{{ m.joined ? " joined" : " disconnected" }}.</slot
        >
        <slot v-else>
          <b>{{ m.senderUsername }}</b
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
import { onMounted, ref, nextTick } from "vue";
import PlaneIcon from "../icons/PlaneIcon.vue";
import type { ChatMessage } from "@/api/types";
import { gameService } from "@/api";
import toastApi from "@/api/toastApi";

const message = ref("");
const history = ref<ChatMessage[]>([]);
const chatBoxHistory = ref<HTMLElement | null>(null);

const sendMessage = async () => {
  try {
    const token = localStorage.getItem("token");
    await gameService.sendChatMessage(message.value, token!);
    message.value = "";
  } catch (error) {
    toastApi.emit({
      title: "Error sending chat message",
      message: error as string,
    });
  }
};

onMounted(() => {
  gameService.subscribeChatEvents(async (message: ChatMessage) => {
    history.value.push(message);
    await nextTick();
    if (chatBoxHistory.value) {
      chatBoxHistory.value.scrollTop = chatBoxHistory.value.scrollHeight;
    }
  });
});
</script>

<style scoped lang="scss">
.chat-box {
  display: flex;
  flex-direction: column;

  .history {
    height: 30%;
    overflow-y: auto;
    margin-bottom: 10px;
    max-width: 290px;
    word-wrap: break-word;

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
