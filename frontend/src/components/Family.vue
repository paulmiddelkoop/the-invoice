<script setup lang="ts">
import { ref, onMounted } from "vue";
import axios from "axios";

const props = defineProps(["id"]);
const family = ref({});

onMounted(async () => {
  const response = await axios.get(`${import.meta.env.VITE_API_URI}/families/${props.id}`);
  family.value = response.data;
});
</script>

<template>
  <h1>Family {{ family.name }}</h1>
</template>
