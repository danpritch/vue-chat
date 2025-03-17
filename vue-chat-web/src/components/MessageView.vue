<!-- src/components/MessageView.vue -->
<template>
    <div class="card d-flex flex-column" :style="{ height: 'calc(100vh - 80px)' }">
      <div class="card-header">
        <h5 class="mb-0">
          Conversation with: {{ getParticipantNamesForMessages(currentConversation) }}
        </h5>
      </div>
      <!-- Messages display area: scrollable -->
      <div class="card-body flex-grow-1 overflow-auto" ref="messagesContainer">
        <div v-for="msg in currentMessages" :key="msg.ID" class="mb-2">
          <strong>{{ msg.SENDER_ID === currentUser.id ? 'You' : getParticipantName(msg.SENDER_ID) }}:</strong>
          <span>{{ msg.CONTENT }}</span>
        </div>
      </div>
      <!-- Send message input -->
      <div class="card-footer">
        <div class="input-group">
          <input type="text" v-model="messageToSend" class="form-control" placeholder="Type a message" @keyup.enter="sendMessage" />
          <div class="input-group-append">
            <button class="btn btn-primary" @click="sendMessage">Send</button>
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
  const { sendMessage, getParticipantName, getParticipantNamesForMessages } = store;
  const messagesContainer = ref(null);
  
  // Auto-scroll when messages update.
  watch(currentMessages, () => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight;
    }
  });
  </script>
  
  <style scoped>
  .card-body {
    overflow-y: auto;
  }
  </style>
  