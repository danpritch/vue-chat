<template>
  <div id="app" class="container-fluid p-0 m-0">
    <Header />
    <div class="row g-0">
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