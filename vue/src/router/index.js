import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Search from "@/components/Search";

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    children:[
      {
        path: '/search',
        name: 'Search',
        component: Search,
      },
    ],
  },

]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
