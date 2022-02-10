import { createApp } from "vue";
import { createRouter, createWebHistory } from "vue-router";
import App from "./App.vue";
import Families from "./components/Families.vue";
import Family from "./components/Family.vue";

const routes = [
  { path: "/families", component: Families },
  { path: "/families/:id", component: Family, props: true }
];
const router = createRouter({ history: createWebHistory(), routes });

createApp(App).use(router).mount("#app");
