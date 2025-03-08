<template>
  <div id="app" class="container-fluid">
    <!-- Header -->
    <header class="bg-primary text-white p-3 mb-4">
      <div class="container">
        <h1 class="mb-0">vue.chat</h1>
      </div>
    </header>

    <div class="row">
      <!-- Left Navigation Bar -->
      <nav class="col-md-3">
        <div class="card">
          <!-- Create User Form at the Top -->
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

      <!-- Main Content Area -->
      <main class="col-md-9">
        <div v-if="currentUser">
          <h2>Current User: {{ currentUser.name }}</h2>
          <!-- Additional main content can go here -->
        </div>
        <div v-else>
          <h2>Please select or create a user</h2>
        </div>
      </main>
    </div>
  </div>
</template>

<script>
export default {
  name: "App",
  data() {
    return {
      userName: "",
      users: [],
      currentUser: null
    };
  },
  methods: {
    async createUser() {
      if (!this.userName.trim()) return;
      try {
        // Adjust the URL as needed.
        const response = await fetch("http://localhost:8080/users", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ name: this.userName })
        });
        const newUser = await response.json();
        // Add the new user and set it as the current user.
        this.users.push(newUser);
        this.currentUser = newUser;
        this.userName = "";
      } catch (error) {
        console.error("Error creating user:", error);
      }
    },
    becomeUser(user) {
      // Update the session state by setting the current user.
      this.currentUser = user;
    }
  },
  mounted() {
    // Establish a WebSocket connection to receive new users.
    const ws = new WebSocket("ws://localhost:8080/ws/users");

    ws.onmessage = (event) => {
      try {
        const user = JSON.parse(event.data);
        // Depending on your backend behavior, you might receive one user at a time.
        this.users.push(user);
      } catch (err) {
        console.error("Error parsing user JSON:", err);
      }
    };

    ws.onerror = (error) => {
      console.error("WebSocket error:", error);
    };

    ws.onclose = () => {
      console.log("WebSocket connection closed");
    };
  }
};
</script>
