<template>
  <div id="app" class="container-fluid">
    <!-- Header -->
    <header class="bg-primary text-white p-3 mb-4">
      <div class="container">
        <h1 class="mb-0">vue.chat</h1>
      </div>
    </header>

    <div class="row">
      <!-- Users Navigation Bar -->
      <nav class="col-md-3">
        <div class="card">
          <!-- Create User Form -->
          <div class="card-header">
            <form @submit.prevent="createUser">
              <div class="input-group">
                <input 
                  type="text" 
                  v-model="userName" 
                  class="form-control" 
                  placeholder="Enter username" 
                />
                <div class="input-group-append">
                  <button type="submit" class="btn btn-primary">
                    Add User
                  </button>
                </div>
              </div>
            </form>
          </div>
          <!-- Users List -->
          <ul class="list-group list-group-flush">
            <li 
              v-for="user in users" 
              :key="user.id" 
              class="list-group-item d-flex justify-content-between align-items-center"
              :style="{ backgroundColor: currentUser && currentUser.id === user.id ? 'orange' : '' }"
            >
              <span>{{ user.name }}</span>
              <button 
                class="btn btn-outline-success btn-sm" 
                @click="becomeUser(user)"
              >
                Become
              </button>
            </li>
          </ul>
        </div>
      </nav>

      <!-- Conversations Navigation Bar -->
      <nav class="col-md-3">
        <div class="card">
          <div class="card-header d-flex justify-content-between align-items-center">
            <h5 class="mb-0">Conversations</h5>
            <!-- New Conversation Button -->
            <button 
              class="btn btn-success btn-sm" 
              @click="openConversationModal" 
              :disabled="!currentUser"
            >
              New Conversation
            </button>
          </div>
          <ul class="list-group list-group-flush">
            <li 
              v-for="conv in conversations" 
              :key="conv.id" 
              class="list-group-item"
              :style="{ backgroundColor: currentConversation && currentConversation.id === conv.id ? 'orange' : '' }"
              @click="setCurrentConversation(conv)"
            >
              <span>{{ getParticipantNames(conv) }}</span>
            </li>
          </ul>
        </div>
      </nav>
    </div>

    <!-- New Conversation Modal -->
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
            <button type="button" class="close" @click="closeConversationModal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
            <p>Select users to include in the conversation:</p>
            <div v-if="availableUsers.length > 0">
              <div 
                v-for="user in availableUsers" 
                :key="user.id" 
                class="form-check"
              >
                <input 
                  class="form-check-input" 
                  type="checkbox" 
                  :id="'user-' + user.id" 
                  :value="user.id" 
                  v-model="selectedUserIds"
                >
                <label 
                  class="form-check-label" 
                  :for="'user-' + user.id"
                >
                  {{ user.name }}
                </label>
              </div>
            </div>
            <div v-else>
              <p>No available users to start a conversation with.</p>
            </div>
          </div>
          <div class="modal-footer">
            <button 
              type="button" 
              class="btn btn-secondary" 
              @click="closeConversationModal"
            >
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
  </div>
</template>

<script>
export default {
  name: "App",
  data() {
    return {
      userName: "",
      usersMap: {},
      currentUser: null,
      // Replace the conversations array with a map.
      conversationMap: {},
      currentConversation: null,
      conversationWebSocket: null,
      userWebSocket: null, // Store the "users" WebSocket reference
      // Modal control and selected participants
      showConversationModal: false,
      selectedUserIds: []
    };
  },
  computed: {
    // Compute an array of users from the usersMap
    users() {
      return Object.values(this.usersMap);
    },
    // Compute available users for conversation creation (exclude current user)
    availableUsers() {
      return this.users.filter(user => {
        return !this.currentUser || user.id !== this.currentUser.id;
      });
    },
    // Convert the conversationMap into an array for rendering
    conversations() {
      return Object.values(this.conversationMap);
    }
  },
  methods: {
    async createUser() {
      if (!this.userName.trim()) return;
      try {
        const response = await fetch("http://localhost:8080/users", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ name: this.userName })
        });
        const newUser = await response.json();
        // Update or add the new user in usersMap
        this.usersMap[newUser.id] = newUser;
        // Clear previous conversation state.
        this.conversationMap = {};
        // If there's an existing users WebSocket, close it before switching user.
        if (this.userWebSocket) {
          this.userWebSocket.close();
          this.userWebSocket = null;
        }
        this.currentUser = newUser;
        this.userName = "";
        // Reinitialize the "users" WebSocket for the new current user.
        this.initializeUserWebSocket();
        // Subscribe to conversations for the new current user.
        this.subscribeToConversations();
      } catch (error) {
        console.error("Error creating user:", error);
      }
    },
    becomeUser(user) {
      // Close any existing conversation WebSocket.
      if (this.conversationWebSocket) {
        this.conversationWebSocket.close();
        this.conversationWebSocket = null;
      }
      // Close the existing users WebSocket if present.
      if (this.userWebSocket) {
        this.userWebSocket.close();
        this.userWebSocket = null;
      }
      // Clear previous conversation state.
      this.conversationMap = {};
      this.currentConversation = null;
      this.currentUser = user;
      // Reinitialize the "users" WebSocket for the new user.
      this.initializeUserWebSocket();
      // Subscribe to conversations for the new current user.
      this.subscribeToConversations();
    },
    initializeUserWebSocket() {
      // Create and store the "users" WebSocket connection.
      this.userWebSocket = new WebSocket("ws://localhost:8080/ws/users");
      this.userWebSocket.onmessage = (event) => {
        try {
          const user = JSON.parse(event.data);
          // Update the user entry in usersMap if it exists, or add it if not.
          this.usersMap[user.id] = user;
        } catch (err) {
          console.error("Error parsing user JSON:", err);
        }
      };
      this.userWebSocket.onerror = (error) => {
        console.error("User WebSocket error:", error);
      };
      this.userWebSocket.onclose = () => {
        console.log("User WebSocket connection closed");
      };
    },
    subscribeToConversations() {
      if (!this.currentUser) return;
      const wsUrl = `ws://localhost:8080/ws/users/${this.currentUser.id}/conversations`;
      this.conversationWebSocket = new WebSocket(wsUrl);
      this.conversationWebSocket.onmessage = (event) => {
        try {
          const conversation = JSON.parse(event.data);
          // Ensure participant_ids is an array; if null, use an empty array.
          conversation.participant_ids = conversation.participant_ids || [];
          // Replace any existing conversation with the same id.
          this.conversationMap[conversation.id] = conversation;
        } catch (err) {
          console.error("Error parsing conversation JSON:", err);
        }
      };
      this.conversationWebSocket.onerror = (error) => {
        console.error("Conversation WebSocket error:", error);
      };
      this.conversationWebSocket.onclose = () => {
        console.log("Conversation WebSocket connection closed");
      };
    },
    setCurrentConversation(conv) {
      this.currentConversation = conv;
    },
    openConversationModal() {
      // Reset any previous selections
      this.selectedUserIds = [];
      this.showConversationModal = true;
    },
    closeConversationModal() {
      this.showConversationModal = false;
    },
    async createConversation() {
      if (!this.currentUser) return;
      try {
        // Post the conversation with selected participant IDs.
        await fetch(`http://localhost:8080/users/${this.currentUser.id}/conversations`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ PARTICIPANT_IDS: this.selectedUserIds })
        });
        // We wait for the WebSocket update to add the conversation to the UI.
      } catch (error) {
        console.error("Error creating conversation:", error);
      } finally {
        this.closeConversationModal();
      }
    },
    // Helper method to return a comma-separated string of participant names.
    // It ignores participant IDs that do not match any user in usersMap.
    getParticipantNames(conversation) {
      if (!conversation || !conversation.participant_ids) return "";
      return conversation.participant_ids
        .map(id => this.usersMap[id] ? this.usersMap[id].name : null)
        .filter(name => name !== null)
        .join(", ");
    }
  },
  mounted() {
    // Initialize the "users" WebSocket when the component mounts.
    this.initializeUserWebSocket();
  }
};
</script>
