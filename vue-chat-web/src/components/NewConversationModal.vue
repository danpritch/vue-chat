<template>
  <div
    class="modal fade show"
    tabindex="-1"
    role="dialog"
    v-if="showConversationModal"
    style="display: block; background: rgba(0, 0, 0, 0.5);"
  >
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">New Conversation</h5>
        </div>
        <div class="modal-body">
          <p>Select users to include in the conversation:</p>
          <div v-if="availableUsers.length > 0">
            <div v-for="user in availableUsers" :key="user.id" class="form-check">
              <input
                class="form-check-input"
                type="checkbox"
                :id="'user-' + user.id"
                :value="user.id"
                v-model="selectedUserIds"
              />
              <label class="form-check-label" :for="'user-' + user.id">{{ user.name }}</label>
            </div>
          </div>
          <div v-else>
            <p>No available users to start a conversation with.</p>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" @click="closeConversationModal">
            Cancel
          </button>
          <button
            type="button"
            class="btn btn-primary"
            @click="createConversation"
            :disabled="selectedUserIds.length === 0"
          >
            Create
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
  import { storeToRefs } from 'pinia';
  import { useChatStore } from '../stores/chatStore';

  const store = useChatStore();
  const { showConversationModal, availableUsers, selectedUserIds } = storeToRefs(store);
  const { closeConversationModal, createConversation } = store;
</script>

