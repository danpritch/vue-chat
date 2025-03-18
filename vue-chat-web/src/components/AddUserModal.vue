<template>
  <div class="modal fade show" tabindex="-1" style="display: block; background: rgba(0, 0, 0, 0.5);">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Add New User</h5>
          <button type="button" class="btn-close" @click="$emit('close')"></button>
        </div>
        <div class="modal-body">
          <input
            type="text"
            v-model="userName"
            class="form-control"
            placeholder="Enter user name"
          />
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" @click="$emit('close')">Cancel</button>
          <button
            type="button"
            class="btn btn-primary"
            @click="submitUser"
            :disabled="!userName.trim()"
          >
            Add User
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useChatStore } from '../stores/chatStore';

const store = useChatStore();
const userName = ref('');

const emit = defineEmits(['close']);

function submitUser() {
  if (userName.value.trim()) {
    store.userName = userName.value;
    store.createUser();
    userName.value = '';
    emit('close');
  }
}
</script>

<style scoped>
.modal-content {
  border-radius: 0; /* Square corners for modal */
  border: 2px solid #333; /* Optional: thicker, darker border */
}

.form-control {
  border-radius: 0; /* Square corners for the text input */
}

.btn-secondary {
  background-color: #d3d3d3; /* Light grey background for Cancel */
  border: none;
  border-radius: 0; /* Square corners for the button */
  color: black;
}

.btn-primary {
  background-color: orange; /* Orange background for Add User */
  border: none;
  border-radius: 0; /* Square corners for the button */
  color: white;
}
</style>
