<template>
  <div id="app">
    <h1>User Manager</h1>
    
    <!-- Form to add a new user -->
    <form @submit.prevent="createUser">
      <input v-model="userName" placeholder="Enter user name" />
      <button type="submit">Add User</button>
    </form>

    <h2>Users List</h2>
    <ul>
      <li v-for="user in users" :key="user.id">
        {{ user.name }} (ID: {{ user.id }})
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: "App",
  data() {
    return {
      userName: "",
      users: []
    };
  },
  methods: {
    async createUser() {
      if (!this.userName.trim()) return;
      try {
        // Adjust the URL as needed. If your Spring Boot app runs on port 8080,
        // you may need to set up a proxy during development.
        const response = await fetch("http://localhost:8080/users", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ name: this.userName })
        });
        const newUser = await response.json();
        // Optionally, you can immediately update the users list:
        // this.users.push(newUser);
        this.userName = "";
      } catch (error) {
        console.error("Error creating user:", error);
      }
    }
  },
  mounted() {
    // Establish a WebSocket connection.
    // Ensure the URL matches your backend WebSocket endpoint.
    const ws = new WebSocket("ws://localhost:8080/ws/users");

    ws.onmessage = (event) => {
      try {
        const user = JSON.parse(event.data);
        // Update the users array. Depending on your backend behavior,
        // you might receive one user at a time or an array of users.
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

<style>
/* Basic styling */
#app {
  max-width: 600px;
  margin: 2rem auto;
  font-family: Arial, sans-serif;
}
input {
  margin-right: 0.5rem;
  padding: 0.5rem;
}
button {
  padding: 0.5rem 1rem;
}
</style>

