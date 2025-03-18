<template>
  <div class="card">
    <div class="card-header d-flex justify-content-between align-items-center">
      <h5 class="mb-0">Users</h5>
      <button class="add-user-btn" @click="openUserModal">+</button>
    </div>
    <ul class="list-group list-group-flush">
      <li
        v-for="user in users"
        :key="user.id"
        class="list-group-item"
        :class="{ selected: currentUser && currentUser.id === user.id }"
        @click="becomeUser(user)"
      >
        <span>{{ user.name }}</span>
      </li>
    </ul>
  </div>

  <AddUserModal v-if="showUserModal" @close="closeUserModal" />
</template>

<script setup>
import { ref } from 'vue';
import { storeToRefs } from 'pinia';
import { useChatStore } from '../stores/chatStore';
import AddUserModal from './AddUserModal.vue';

const store = useChatStore();
const { users, currentUser } = storeToRefs(store);
const { becomeUser } = store;

const showUserModal = ref(false);

function openUserModal() {
  showUserModal.value = true;
}

function closeUserModal() {
  showUserModal.value = false;
}
</script>

<style scoped>
/* Make the card fill the viewport height and use flex layout */
.card {
  border-radius: 0; /* Squared edges */
  border: 2px solid #333; /* Thicker, darker border */
  min-height: calc(100vh - 120px); /* 100vh; */ /* Extend to the bottom of the screen */
  display: flex;
  flex-direction: column;
}

/* Let the list grow to fill remaining space */
.list-group {
  flex: 1;
  overflow-y: auto;
  padding-bottom: 1rem; /* Optional extra padding at the bottom */
}

.add-user-btn {
  background-color: orange;
  color: white;
  font-size: 2rem;
  width: 3rem;
  height: 3rem;
  display: flex;
  justify-content: center;
  align-items: center;
  border: none;
  border-radius: 0; /* Square edges */
}

/* Existing list-group-item styles */
.list-group-item {
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.selected {
  background-color: orange;
}

.list-group-item:hover:not(.selected) {
  background-color: rgba(255, 165, 0, 0.3);
}
</style>
