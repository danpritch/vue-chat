<!-- src/components/ConversationList.vue -->
<template>
    <div class="card">
      <div class="card-header d-flex justify-content-between align-items-center">
        <h5 class="mb-0">Conversations</h5>
        <button class="btn btn-success btn-sm" @click="openConversationModal" :disabled="!currentUser">
          New Conversation
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
  const { currentUser, currentConversation, conversations } = storeToRefs(store); // include currentConversation here
  const { openConversationModal, setCurrentConversation, getParticipantNames } = store;
  </script>
  
  <style scoped>
  /* Add any conversation list specific styles here */
  </style>
  