import { createRouter, createWebHistory } from "vue-router";
import HomeView from "@/views/HomeView.vue";
import SignupView from "@/views/LoginView.vue";
import RulesView from "@/views/RulesView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: HomeView,
    },
    {
      path: "/rules",
      name: "rules",
      component: RulesView,
    },
    {
      path: "/login",
      name: "login",
      component: SignupView,
    },
  ],
});

export default router;
