import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap"

import { createApp } from 'vue'
import { createPinia } from 'pinia';
import App from './App.vue'

const app = createApp(App);
const pinia = createPinia();
app.use(pinia); // Install Pinia
app.mount('#app');

