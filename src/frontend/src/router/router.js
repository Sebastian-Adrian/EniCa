import { createMemoryHistory, createRouter } from 'vue-router'

import Ablesung from '../components/ZaehlerAblesung.vue'
import HomeView from "@/components/HomeView.vue";
import Zaehler from "@/components/ZaehlerView.vue";

const routes = [
    { path: '/', component: HomeView },
    { path: '/ablesung', component: Ablesung },
    { path: '/zaehler', component: Zaehler },
]

const router = createRouter({
    history: createMemoryHistory(),
    routes,
})

export default router