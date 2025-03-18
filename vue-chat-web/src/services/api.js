const API_BASE = process.env.VUE_APP_API_URL || "http://localhost:8080";

export async function createUserApi(name) {
  const response = await fetch(`${API_BASE}/users`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ name })
  });
  return await response.json();
}

export function subscribeToUsersStreamApi(onMessage, onError) {
  const eventSource = new EventSource(`${API_BASE}/users`);
  eventSource.onmessage = (event) => {
    if (!event.data || event.data.trim() === "" || event.data.trim().startsWith(':')) return;
    try {
      const user = JSON.parse(event.data);
      onMessage(user);
    } catch (err) {
      console.error("Error parsing user event data:", err);
    }
  };
  eventSource.onerror = onError;
  return eventSource;
}

export function subscribeToConversationsStreamApi(userId, onMessage, onError) {
  const eventSource = new EventSource(`${API_BASE}/users/${userId}/conversations`);
  eventSource.onmessage = (event) => {
    if (!event.data || event.data.trim() === "" || event.data.trim().startsWith(':')) return;
    try {
      const conversation = JSON.parse(event.data);
      conversation.PARTICIPANT_IDS = conversation.PARTICIPANT_IDS || [];
      onMessage(conversation);
    } catch (err) {
      console.error("Error parsing conversation event data:", err);
    }
  };
  eventSource.onerror = onError;
  return eventSource;
}

export function subscribeToMessagesStreamApi(userId, onMessage, onError) {
  const eventSource = new EventSource(`${API_BASE}/users/${userId}/messages`);
  eventSource.onmessage = (event) => {
    if (!event.data || event.data.trim() === "" || event.data.trim().startsWith(':')) return;
    try {
      const message = JSON.parse(event.data);
      if (message === null) return; 
      onMessage(message);
    } catch (err) {
      console.error("Error parsing message event data:", err);
    }
  };
  eventSource.onerror = onError;
  return eventSource;
}

export async function sendMessageApi(userId, conversationId, content) {
  await fetch(`${API_BASE}/users/${userId}/messages`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      CONVERSATION_ID_DUP: conversationId,
      CONTENT: content
    })
  });
}

export async function createConversationApi(userId, participantIds) {
  return fetch(`${API_BASE}/users/${userId}/conversations`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ PARTICIPANT_IDS: participantIds })
  });
}
