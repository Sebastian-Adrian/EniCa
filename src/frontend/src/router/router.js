import { createMemoryHistory, createRouter } from 'vue-router'

import Ablesung from '../components/ZaehlerAblesung.vue'
import Ablesungen from '../components/ZaehlerAblesungen.vue'
import HomeView from "@/components/HomeView.vue";
import Zaehler from "@/components/ZaehlerView.vue";

const routes = [
    { path: '/', component: HomeView },
    { path: '/ablesung', component: Ablesung },
    { path: '/zaehler', component: Zaehler },
    { path: '/ablesungen', component: Ablesungen },
]

const router = createRouter({
    history: createMemoryHistory(),
    routes,
})

export default router