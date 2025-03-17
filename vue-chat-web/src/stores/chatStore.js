import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { 
  createUserApi, 
  subscribeToUsersStreamApi, 
  subscribeToConversationsStreamApi, 
  subscribeToMessagesStreamApi, 
  sendMessageApi, 
  createConversationApi 
} from '../services/api';

export const useChatStore = defineStore('chat', () => {

  const userName = ref("");
  const usersMap = ref({});
  const currentUser = ref(null);
  const conversationMap = ref({});
  const currentConversation = ref(null);
  const messageMap = ref({});
  const messageToSend = ref("");
  const showConversationModal = ref(false);
  const selectedUserIds = ref([]);

  let userEventSource = null;
  let conversationEventSource = null;
  let messageEventSource = null;

  const users = computed(() => Object.values(usersMap.value));
  const availableUsers = computed(() =>
    users.value.filter(user => !currentUser.value || user.id !== currentUser.value.id)
  );
  const conversations = computed(() => Object.values(conversationMap.value));

  const currentMessages = computed(() => {
    if (currentConversation.value && messageMap.value[currentConversation.value.CONVERSATION_ID_DUP]) {
      let messages = Object.values(messageMap.value[currentConversation.value.CONVERSATION_ID_DUP]);
      messages.sort((a, b) => a.ID - b.ID);
      return messages;
    }
    return [];
  });

  async function createUser() {
    if (!userName.value.trim()) return;
    try {
      const newUser = await createUserApi(userName.value);
      usersMap.value[newUser.id] = newUser;
      if (!currentUser.value) {
        currentUser.value = newUser;
        subscribeToConversations();
        subscribeToMessages();
      }
      userName.value = "";
    } catch (error) {
      console.error("Error creating user:", error);
    }
  }

  function becomeUser(user) {
    if (currentUser.value && currentUser.value.id === user.id) return;
    if (conversationEventSource) {
      conversationEventSource.close();
      conversationEventSource = null;
    }
    if (messageEventSource) {
      messageEventSource.close();
      messageEventSource = null;
    }
    conversationMap.value = {};
    currentConversation.value = null;
    currentUser.value = user;
    subscribeToConversations();
    subscribeToMessages();
  }

  function subscribeToUsers() {
    if (userEventSource) return;
    userEventSource = subscribeToUsersStreamApi(
      (user) => {
        usersMap.value[user.id] = user;
      },
      (error) => console.error("Users stream error:", error)
    );
  }

  function subscribeToConversations() {
    if (!currentUser.value) return;
    if (conversationEventSource) return;
    conversationEventSource = subscribeToConversationsStreamApi(
      currentUser.value.id,
      (conversation) => {
        conversationMap.value[conversation.CONVERSATION_ID_DUP] = conversation;
      },
      (error) => console.error("Conversations stream error:", error)
    );
  }

  function subscribeToMessages() {
    if (!currentUser.value) return;
    if (messageEventSource) return;
    messageEventSource = subscribeToMessagesStreamApi(
      currentUser.value.id,
      (message) => {
        const convId = message.CONVERSATION_ID_DUP;
        if (!messageMap.value[convId]) {
          messageMap.value[convId] = {};
        }
        messageMap.value[convId][message.ID] = message;
      },
      (error) => console.error("Messages stream error:", error)
    );
  }

  function setCurrentConversation(conv) {
    currentConversation.value = conv;
  }

  async function sendMessage() {
    if (!messageToSend.value.trim() || !currentConversation.value || !currentUser.value) return;
    try {
      await sendMessageApi(currentUser.value.id, currentConversation.value.CONVERSATION_ID_DUP, messageToSend.value);
      messageToSend.value = "";
    } catch (error) {
      console.error("Error sending message:", error);
    }
  }

  function openConversationModal() {
    selectedUserIds.value = [];
    showConversationModal.value = true;
  }

  function closeConversationModal() {
    showConversationModal.value = false;
  }

  async function createConversation() {
    if (!currentUser.value) return;
    try {
      await createConversationApi(currentUser.value.id, selectedUserIds.value);
    } catch (error) {
      console.error("Error creating conversation:", error);
    } finally {
      closeConversationModal();
    }
  }

  function getParticipantName(id) {
    if (!id) return "?";
    return usersMap.value[id] ? usersMap.value[id].name : "?";
  }

  function getParticipantNames(conversation) {
    if (!conversation || !conversation.PARTICIPANT_IDS) return "";
    return conversation.PARTICIPANT_IDS
      .map(id => usersMap.value[id] ? usersMap.value[id].name : null)
      .filter(name => name !== null)
      .join(", ");
  }

  return {
    userName,
    users,
    availableUsers,
    conversations,
    currentMessages,
    currentUser,
    currentConversation,
    messageToSend,
    showConversationModal,
    selectedUserIds,
    createUser,
    becomeUser,
    subscribeToUsers,
    subscribeToConversations,
    subscribeToMessages,
    setCurrentConversation,
    sendMessage,
    openConversationModal,
    closeConversationModal,
    createConversation,
    getParticipantName,
    getParticipantNames
  };
});
