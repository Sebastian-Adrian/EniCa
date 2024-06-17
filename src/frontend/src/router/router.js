import { createMemoryHistory, createRouter } from 'vue-router'

import Ablesungen from '../components/AblesungenView.vue'
import HomeView from "@/components/HomeView.vue";
import Zaehler from "@/components/ZaehlerView.vue";
import Tarife from "@/components/TarifeView.vue";

const routes = [
    { path: '/', component: HomeView },
    { path: '/zaehler', component: Zaehler },
    { path: '/ablesungen', component: Ablesungen },
    { path: '/tarife', component: Tarife },
]

const router = createRouter({
    history: createMemoryHistory(),
    routes,
})

export default router