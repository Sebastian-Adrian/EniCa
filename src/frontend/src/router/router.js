import { createMemoryHistory, createRouter } from 'vue-router'

import Ablesung from '../components/AddAblesung.vue'
import Ablesungen from '../components/AblesungenView.vue'
import HomeView from "@/components/HomeView.vue";
import Zaehler from "@/components/ZaehlerView.vue";
import Tarife from "@/components/TarifeView.vue";

const routes = [
    { path: '/', component: HomeView },
    { path: '/ablesung', component: Ablesung },
    { path: '/zaehler', component: Zaehler },
    { path: '/ablesungen', component: Ablesungen },
    { path: '/tarife', component: Tarife },
]

const router = createRouter({
    history: createMemoryHistory(),
    routes,
})

export default router