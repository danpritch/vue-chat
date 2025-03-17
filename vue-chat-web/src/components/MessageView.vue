<template>
    <div class="card d-flex flex-column" :style="{ height: 'calc(100vh - 120px)' }">
      <div class="card-body flex-grow-1 overflow-auto" ref="messagesContainer">
        <div v-for="msg in currentMessages" :key="msg.ID" class="mb-2">
          <strong>{{ msg.SENDER_ID === currentUser.id ? 'You' : getParticipantName(msg.SENDER_ID) }}:</strong>
          <span>{{ msg.CONTENT }}</span>
        </div>
      </div>
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
  const { sendMessage, getParticipantName, getParticipantNames } = store;
  const messagesContainer = ref(null);
  
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
  