<!-- src/App.vue -->
<template>
  <div id="app" class="container-fluid">
    <Header />
    <div class="row">
      <nav class="col-md-3">
        <UserList />
      </nav>
      <nav class="col-md-3">
        <ConversationList />
      </nav>
      <div class="col-md-6" v-if="currentConversation">
        <MessageView />
      </div>
    </div>
    <NewConversationModal />
  </div>
</template>

<script setup>
import { storeToRefs } from 'pinia';
import { onMounted } from 'vue';
import { useChatStore } from './stores/chatStore';
import Header from './components/Header.vue';
import UserList from './components/UserList.vue';
import ConversationList from './components/ConversationList.vue';
import MessageView from './components/MessageView.vue';
import NewConversationModal from './components/NewConversationModal.vue';

const store = useChatStore();

const {  currentConversation } = storeToRefs(store);

onMounted(() => {
  store.subscribeToUsers();
});
</script>

<style>
/* Global styles can be placed here */
</style>
