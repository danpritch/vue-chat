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
          <div class="card-header">
            <h5>Conversations</h5>
          </div>
          <ul class="list-group list-group-flush">
            <li 
              v-for="conv in conversations" 
              :key="conv.id" 
              class="list-group-item"
              :style="{ backgroundColor: currentConversation && currentConversation.id === conv.id ? 'orange' : '' }"
              @click="setCurrentConversation(conv)"
            >
              <span>{{ conv.participant_ids.join(', ') }}</span>
            </li>
          </ul>
        </div>
      </nav>
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
      conversations: [],
      currentConversation: null,
      conversationWebSocket: null
    };
  },
  computed: {
    // Compute an array of users from the usersMap
    users() {
      return Object.values(this.usersMap);
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
        this.currentUser = newUser;
        this.userName = "";
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
      // Clear previous conversation state.
      this.conversations = [];
      this.currentConversation = null;
      this.currentUser = user;
      // Subscribe to conversations for the new current user.
      this.subscribeToConversations();
    },
    subscribeToConversations() {
      if (!this.currentUser) return;
      const wsUrl = `ws://localhost:8080/ws/users/${this.currentUser.id}/conversations`;
      this.conversationWebSocket = new WebSocket(wsUrl);
      this.conversationWebSocket.onmessage = (event) => {
        try {
          const conversation = JSON.parse(event.data);
          // Append the new conversation.
          this.conversations.push(conversation);
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
    }
  },
  mounted() {
    // Establish a WebSocket connection for new users.
    const ws = new WebSocket("ws://localhost:8080/ws/users");
    ws.onmessage = (event) => {
      try {
        const user = JSON.parse(event.data);
        // Update the user entry in usersMap if it exists, or add it if not.
        this.usersMap[user.id] = user;
      } catch (err) {
        console.error("Error parsing user JSON:", err);
      }
    };
    ws.onerror = (error) => {
      console.error("User WebSocket error:", error);
    };
    ws.onclose = () => {
      console.log("User WebSocket connection closed");
    };
  }
};
</script>
