<template>
  <div id="app">
    <h1>Player Manager</h1>
    
    <!-- Form to add a new player -->
    <form @submit.prevent="createPlayer">
      <input v-model="playerName" placeholder="Enter player name" />
      <button type="submit">Add Player</button>
    </form>

    <h2>Players List</h2>
    <ul>
      <li v-for="player in players" :key="player.id">
        {{ player.name }} (ID: {{ player.id }})
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: "App",
  data() {
    return {
      playerName: "",
      players: []
    };
  },
  methods: {
    async createPlayer() {
      if (!this.playerName.trim()) return;
      try {
        // Adjust the URL as needed. If your Spring Boot app runs on port 8080,
        // you may need to set up a proxy during development.
        const response = await fetch("http://localhost:8080/players", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ name: this.playerName })
        });
        const newPlayer = await response.json();
        // Optionally, you can immediately update the players list:
        // this.players.push(newPlayer);
        this.playerName = "";
      } catch (error) {
        console.error("Error creating player:", error);
      }
    }
  },
  mounted() {
    // Establish a WebSocket connection.
    // Ensure the URL matches your backend WebSocket endpoint.
    const ws = new WebSocket("ws://localhost:8080/ws/players");

    ws.onmessage = (event) => {
      try {
        const player = JSON.parse(event.data);
        // Update the players array. Depending on your backend behavior,
        // you might receive one player at a time or an array of players.
        this.players.push(player);
      } catch (err) {
        console.error("Error parsing player JSON:", err);
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

