<template>
  <div class="card">
    <div class="card-header d-flex justify-content-between align-items-center">
      <h5 class="mb-0">Conversations</h5>
      <button class="add-conversation-btn" @click="openConversationModal" :disabled="!currentUser">
        +
      </button>
    </div>
    <ul class="list-group list-group-flush">
      <li v-for="conv in conversations" :key="conv.CONVERSATION_ID_DUP"
          class="list-group-item"
          :style="{ backgroundColor: currentConversation && currentConversation.CONVERSATION_ID_DUP === conv.CONVERSATION_ID_DUP ? 'orange' : '' }"
          @click="setCurrentConversation(conv)">
        <span>{{ getParticipantNames(conv) }}</span>
      </li>
    </ul>
  </div>
</template>
  
<script setup>
import { storeToRefs } from 'pinia';
import { useChatStore } from '../stores/chatStore';
  
const store = useChatStore();
const { currentUser, currentConversation, conversations } = storeToRefs(store); 
const { openConversationModal, setCurrentConversation, getParticipantNames } = store;
</script>
  
<style scoped>
.card {
  border-radius: 0; /* Squared edges */
  border: 2px solid #333; /* Thicker, darker border */
  min-height: calc(100vh - 120px); /* Extend card to bottom of the screen */
  display: flex;
  flex-direction: column;
}

.add-conversation-btn {
  background-color: orange;
  color: white;
  font-size: 2rem; /* Large '+' sign */
  width: 3rem;
  height: 3rem;
  display: flex;
  justify-content: center;
  align-items: center;
  border: none;
  border-radius: 0; /* Square edges */
}

.list-group {
  flex: 1;
  overflow-y: auto;
  padding-bottom: 1rem; /* Optional bottom padding */
}

.list-group-item {
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.list-group-item:hover {
  background-color: rgba(255, 165, 0, 0.3);
}
</style>
