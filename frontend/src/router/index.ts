import { createRouter, createWebHistory } from "vue-router";
import HomeView from "@/views/HomeView.vue";
import SignupView from "@/views/LoginView.vue";

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
      component: () => import("@/views/RulesView.vue"),
    },
    {
      path: "/login",
      name: "login",
      component: SignupView,
    },
    {
      path: "/game/:id",
      name: "game",
      component: () => import("@/views/GameView.vue"),
      meta: {
        disableHeader: true,
        disableFooter: true,
      },
    },
    {
      path: "/debug",
      name: "debug",
      component: () => import("@/views/DebugView.vue"),
    },
    {
      path: "/:catchAll(.*)",
      name: "notfound",
      component: () => import("@/views/NotFoundView.vue"),
      meta: {
        titleName: "404 Not Found",
      },
    },
  ],
});

export default router;
