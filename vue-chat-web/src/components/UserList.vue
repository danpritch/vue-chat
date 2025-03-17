<template>
  <div class="card">
    <div class="card-header d-flex justify-content-between align-items-center bg-white">
      <h5 class="mb-0">Users</h5>
      <button class="btn btn-primary btn-sm" @click="openUserModal">Add User</button>
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
