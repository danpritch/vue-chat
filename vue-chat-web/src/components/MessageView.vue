<template>
  <div class="card d-flex flex-column" :style="{ height: 'calc(100vh - 120px)' }">
    <div class="card-body flex-grow-1 overflow-auto" ref="messagesContainer">
      <div v-for="msg in currentMessages" :key="msg.ID" class="mb-2">
        <strong :class="{ 'current-user': msg.SENDER_ID === currentUser.id }">
          {{ msg.SENDER_ID === currentUser.id ? 'You' : getParticipantName(msg.SENDER_ID) }}:
        </strong>
        <span>{{ msg.CONTENT }}</span>
      </div>
    </div>
    <div class="card-footer">
      <div class="input-group">
        <input
          type="text"
          v-model="messageToSend"
          class="form-control"
          placeholder="Type a message"
          @keyup.enter="sendMessage"
        />
        <div class="input-group-append">
          <button class="btn send-btn" @click="sendMessage">
            ⏎
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { storeToRefs } from 'pinia';
import { useChatStore } from '../stores/chatStore';
import { ref, watch } from 'vue';

const store = useChatStore();
const { currentUser, currentConversation, currentMessages, messageToSend } = storeToRefs(store);
const { sendMessage, getParticipantName, getParticipantNames } = store;
const messagesContainer = ref(null);

watch(currentMessages, () => {
  if (messagesContainer.value) {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight;
  }
});
</script>

<style scoped>
.card {
  border-radius: 0; /* Squared edges */
  border: 2px solid #333; /* Thicker, darker border */
}

.send-btn {
  background-color: orange;
  color: white;
  font-size: 2rem; /* Large symbol */
  width: 3rem;
  height: 3rem;
  display: flex;
  justify-content: center;
  align-items: center;
  border: none;
  border-radius: 0; /* Square edges */
  padding: 0;
}

.current-user {
  color: orange;
}
</style>
