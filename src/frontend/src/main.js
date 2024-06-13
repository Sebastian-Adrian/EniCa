import 'bootstrap/dist/css/bootstrap.css';
import { createApp } from 'vue'
import App from './App.vue'
import bootstrap from 'bootstrap/dist/js/bootstrap'

const app = createApp(App);
app.mount('#app');
app.use(bootstrap);
