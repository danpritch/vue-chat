<template>
  <div class="card">
    <div class="card-header">
      <form @submit.prevent="createUser">
        <div class="input-group">
          <input type="text" v-model="userName" class="form-control" placeholder="Enter username" />
          <div class="input-group-append">
            <button type="submit" class="btn btn-primary">Add User</button>
          </div>
        </div>
      </form>
    </div>
    <ul class="list-group list-group-flush">
      <li v-for="user in users" :key="user.id"
          class="list-group-item d-flex justify-content-between align-items-center"
          :style="{ backgroundColor: currentUser && currentUser.id === user.id ? 'orange' : '' }">
        <span>{{ user.name }}</span>
        <button class="btn btn-outline-success btn-sm" @click="becomeUser(user)">Become</button>
      </li>
    </ul>
  </div>
</template>
  
<script setup>
  import { storeToRefs } from 'pinia';
  import { useChatStore } from '../stores/chatStore';

  const store = useChatStore();
  const { userName, users, currentUser } = storeToRefs(store);
  const { createUser, becomeUser } = store;
</script>
  
  