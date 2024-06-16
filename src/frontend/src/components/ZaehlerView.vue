<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const zaehlerList = ref([])

onMounted(async () => {
  const response = await axios.get('/api/zaehler')
  zaehlerList.value = response.data
})
</script>

<template>
  <h3>Zähler</h3>
  <ul>
    <li v-for="zaehler in zaehlerList" :key="zaehler.id">
      {{ zaehler.zaehlerNr }} - {{ zaehler.zaehlerArt }}
    </li>
  </ul>
</template>

<style scoped>

</style>