<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";

const families = ref([]);

onMounted(async () => {
  const response = await axios.get(`${import.meta.env.VITE_API_URI}/families`);
  families.value = response.data;
});
</script>

<template>
  <h1>Families</h1>

  <ul>
    <li v-for="family in families" :key="family.id">
      <router-link :to="`/families/${family.id}`">{{ family.name }}</router-link>
    </li>
  </ul>
</template>
