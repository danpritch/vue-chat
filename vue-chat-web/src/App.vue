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
                <input type="text" v-model="userName" class="form-control" placeholder="Enter username" />
                <div class="input-group-append">
                  <button type="submit" class="btn btn-primary">Add User</button>
                </div>
              </div>
            </form>
          </div>
          <!-- Users List -->
          <ul class="list-group list-group-flush">
            <li v-for="user in users" :key="user.id" 
                class="list-group-item d-flex justify-content-between align-items-center"
                :style="{ backgroundColor: currentUser && currentUser.id === user.id ? 'orange' : '' }">
              <span>{{ user.name }}</span>
              <button class="btn btn-outline-success btn-sm" @click="becomeUser(user)">Become</button>
            </li>
          </ul>
        </div>
      </nav>

      <!-- Conversations Navigation Bar -->
      <nav class="col-md-3">
        <div class="card">
          <div class="card-header d-flex justify-content-between align-items-center">
            <h5 class="mb-0">Conversations</h5>
            <button class="btn btn-success btn-sm" @click="openConversationModal" :disabled="!currentUser">
              New Conversation
            </button>
          </div>
          <ul class="list-group list-group-flush">
            <li v-for="conv in conversations" :key="conv.id" 
                class="list-group-item"
                :style="{ backgroundColor: currentConversation && currentConversation.id === conv.id ? 'orange' : '' }"
                @click="setCurrentConversation(conv)">
              <span>{{ getParticipantNames(conv) }}</span>
            </li>
          </ul>
        </div>
      </nav>

      <!-- Messages and Send Message Column -->
      <div class="col-md-6" v-if="currentConversation">
        <div class="card d-flex flex-column" :style="{ height: 'calc(100vh - 80px)' }">
          <div class="card-header">
            <h5 class="mb-0">
              Conversation with: {{ getParticipantNames(currentConversation) }}
            </h5>
          </div>
          <!-- Messages display area: scrollable -->
          <div class="card-body flex-grow-1 overflow-auto" ref="messagesContainer">
            <div v-for="msg in currentMessages" :key="msg.ID" class="mb-2">
              <strong>{{ msg.SENDER_ID === currentUser.id ? 'You' : getParticipantName(msg.SENDER_ID) }}:</strong>
              <span>{{ msg.CONTENT }}</span>
            </div>
          </div>
          <!-- Send message component -->
          <div class="card-footer">
            <div class="input-group">
              <input type="text" v-model="messageToSend" class="form-control" placeholder="Type a message" @keyup.enter="sendMessage" />
              <div class="input-group-append">
                <button class="btn btn-primary" @click="sendMessage">Send</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- New Conversation Modal -->
    <div class="modal fade show" tabindex="-1" role="dialog" 
         v-if="showConversationModal" 
         style="display: block; background: rgba(0, 0, 0, 0.5);">
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
              <div v-for="user in availableUsers" :key="user.id" class="form-check">
                <input class="form-check-input" type="checkbox" :id="'user-' + user.id" 
                       :value="user.id" v-model="selectedUserIds">
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
            <button type="button" class="btn btn-primary" @click="createConversation" 
                    :disabled="selectedUserIds.length === 0">
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
      conversationMap: {},
      currentConversation: null,
      // EventSource streams:
      userEventSource: null,
      conversationEventSource: null,
      messageEventSource: null,
      // For messages: mapping conversationId -> object of messages keyed by message ID.
      messageMap: {},
      messageToSend: "",
      // Modal state:
      showConversationModal: false,
      selectedUserIds: []
    };
  },
  computed: {
    users() {
      return Object.values(this.usersMap);
    },
    availableUsers() {
      return this.users.filter(user => !this.currentUser || user.id !== this.currentUser.id);
    },
    conversations() {
      return Object.values(this.conversationMap);
    },
    currentMessages() {
      if (this.currentConversation && this.messageMap[this.currentConversation.CONVERSATION_ID_DUP]) {
        let messages = Object.values(this.messageMap[this.currentConversation.CONVERSATION_ID_DUP]);
        messages.sort((a, b) => a.ID - b.ID);
        return messages;
      }
      return [];
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
        this.usersMap[newUser.id] = newUser;
        // If no user is currently set, initialize the user.
        if (!this.currentUser) {
          this.currentUser = newUser;
          this.subscribeToConversationsStream();
          this.subscribeToMessagesStream();
        }
        this.userName = "";
      } catch (error) {
        console.error("Error creating user:", error);
      }
    },
    becomeUser(user) {
      // Only switch if the selected user is different.
      if (this.currentUser && this.currentUser.id === user.id) return;
      // Close existing user-specific streams.
      if (this.conversationEventSource) {
        this.conversationEventSource.close();
        this.conversationEventSource = null;
      }
      if (this.messageEventSource) {
        this.messageEventSource.close();
        this.messageEventSource = null;
      }
      // Reset conversation state.
      this.conversationMap = {};
      this.currentConversation = null;
      this.currentUser = user;
      // Reinitialize streams for the new user.
      this.subscribeToConversationsStream();
      this.subscribeToMessagesStream();
    },
    subscribeToUsersStream() {
      if (this.userEventSource) return; // Already connected.
      this.userEventSource = new EventSource("http://localhost:8080/users");

      this.userEventSource.onopen = (event) => {
        console.info("Users stream connection opened:", event);
      };

      this.userEventSource.onmessage = (event) => {
        // Skip empty or comment events (keep-alives).
        if (!event.data || event.data.trim() === "" || event.data.trim().startsWith(':')) {
          console.warn("Skipping non-JSON or keep-alive event:", event.data);
          return;
        }
        try {
          const user = JSON.parse(event.data);
          this.usersMap[user.id] = user;
        } catch (err) {
          console.error("Error parsing user event data:", event.data, err);
        }
      };

      this.userEventSource.onerror = (error) => {
        console.error("Users stream encountered an error:", error);
        if (this.userEventSource.readyState === EventSource.CLOSED) {
          console.error("Users stream connection closed.");
        }
      };
    },

    subscribeToConversationsStream() {
      if (!this.currentUser) return;
      if (this.conversationEventSource) return; // Already connected.
      const url = `http://localhost:8080/users/${this.currentUser.id}/conversations`;
      this.conversationEventSource = new EventSource(url);

      this.conversationEventSource.onopen = (event) => {
        console.info("Conversations stream connection opened:", event);
      };

      this.conversationEventSource.onmessage = (event) => {
        // Skip empty or comment events (keep-alives).
        if (!event.data || event.data.trim() === "" || event.data.trim().startsWith(':')) {
          console.warn("Skipping non-JSON or keep-alive event:", event.data);
          return;
        }
        try {
          const conversation = JSON.parse(event.data);
          conversation.PARTICIPANT_IDS = conversation.PARTICIPANT_IDS || [];
          this.conversationMap[conversation.CONVERSATION_ID_DUP] = conversation;
        } catch (err) {
          console.error("Error parsing conversation JSON:", event.data, err);
        }
      };

      this.conversationEventSource.onerror = (error) => {
        console.error("Conversations stream encountered an error:", error);
        if (this.conversationEventSource.readyState === EventSource.CLOSED) {
          console.error("Conversations stream connection closed.");
        }
      };
    },

    subscribeToMessagesStream() {
      if (!this.currentUser) return;
      if (this.messageEventSource) return; // Already connected.
      const url = `http://localhost:8080/users/${this.currentUser.id}/messages`;
      this.messageEventSource = new EventSource(url);

      this.messageEventSource.onopen = (event) => {
        console.info("Messages stream connection opened:", event);
      };

      this.messageEventSource.onmessage = (event) => {
        // Skip empty or comment events (keep-alives).
        if (!event.data || event.data.trim() === "" || event.data.trim().startsWith(':')) {
          console.warn("Skipping non-JSON or keep-alive event:", event.data);
          return;
        }
        try {
          const msg = JSON.parse(event.data);
          const convId = msg.CONVERSATION_ID_DUP;
          // Initialize as an object if not already
          if (!this.messageMap[convId]) {
            this.messageMap[convId] = {};
          }
          // Use the unique message ID as key, overwriting any duplicate.
          this.messageMap[convId][msg.ID] = msg;

          // Scroll to the bottom if this is the active conversation.
          if (this.currentConversation && this.currentConversation.CONVERSATION_ID_DUP === convId) {
            this.$nextTick(() => {
              const container = this.$refs.messagesContainer;
              if (container) container.scrollTop = container.scrollHeight;
            });
          }
        } catch (err) {
          console.error("Error parsing message JSON:", event.data, err);
        }
      };

      this.messageEventSource.onerror = (error) => {
        console.error("Messages stream encountered an error:", error);
        if (this.messageEventSource.readyState === EventSource.CLOSED) {
          console.error("Messages stream connection closed.");
        }
      };
    },

    setCurrentConversation(conv) {
      this.currentConversation = conv;
      this.$nextTick(() => {
        const container = this.$refs.messagesContainer;
        if (container) {
          container.scrollTop = container.scrollHeight;
        }
      });
    },

    async sendMessage() {
      if (!this.messageToSend.trim() || !this.currentConversation || !this.currentUser) return;
      const newMessage = {
        CONVERSATION_ID_DUP: this.currentConversation.CONVERSATION_ID_DUP,
        CONTENT: this.messageToSend
      };
      try {
        await fetch(`http://localhost:8080/users/${this.currentUser.id}/messages`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(newMessage)
        });
        this.messageToSend = "";
      } catch (error) {
        console.error("Error sending message:", error);
      }
    },
    openConversationModal() {
      this.selectedUserIds = [];
      this.showConversationModal = true;
    },
    closeConversationModal() {
      this.showConversationModal = false;
    },
    async createConversation() {
      if (!this.currentUser) return;
      try {
        await fetch(`http://localhost:8080/users/${this.currentUser.id}/conversations`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ PARTICIPANT_IDS: this.selectedUserIds })
        });
      } catch (error) {
        console.error("Error creating conversation:", error);
      } finally {
        this.closeConversationModal();
      }
    },
    getParticipantName(id) {
      if (!id) return "?";
      return this.usersMap[id] ? this.usersMap[id].name : "?";
    },
    getParticipantNames(conversation) {
      if (!conversation || !conversation.PARTICIPANT_IDS) return "";
      return conversation.PARTICIPANT_IDS
        .map(id => this.usersMap[id] ? this.usersMap[id].name : null)
        .filter(name => name !== null)
        .join(", ");
    }
  },
  mounted() {
    // Initialize the global users stream.
    this.subscribeToUsersStream();
  }
};
</script>

<style scoped>
.card-body {
  overflow-y: auto;
}
</style>
