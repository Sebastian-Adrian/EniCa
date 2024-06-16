<script setup>

import { ref, onMounted } from 'vue'
import axios from 'axios'

const zaehlerList = ref([])
const ablesung = ref({ zaehlerNr: '', zaehlerstand: '', datum: '' })

onMounted(async () => {
  const response = await axios.get('/api/zaehler') // Liste der verfügbaren Zähler abrufen
  zaehlerList.value = response.data
})

const submitForm = async () => {
  const response = await axios.post('/api/ablesungen', ablesung.value)
  console.log('Success:', response.data)
}
</script>

<template>
  <div class="ablesung">
    <h2>Ablesung</h2>
    <form @submit.prevent="submitForm">
      <div class="mb-3">
        <label for="zaehlerNr">Zähler Nr</label>
        <select id="zaehlerNr" v-model="ablesung.zaehlerNr" class="form-control"> <!-- select Element hinzufügen -->
          <option v-for="zaehler in zaehlerList" :key="zaehler.id" :value="zaehler.zaehlerNr">
            {{ zaehler.zaehlerNr }}
          </option>
        </select>
      </div>
      <div class="mb-3">
        <label for="zaehlerstand">Zählerstand</label>
        <input type="number" id="zaehlerstand" v-model="ablesung.zaehlerstand" class="form-control"/>
      </div>
      <div class="mb-3">
        <label for="datum">Datum</label>
        <input type="date" id="datum" v-model="ablesung.datum" class="form-control"/>
      </div>
      <button type="submit" class="btn btn-primary">Speichern</button>
    </form>
  </div>
</template>

<style scoped>
.ablesung {
  margin-top: 20px;
  margin-left: 20px;
  margin-right: 20px;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.speichern {
  margin-top: 20px;
  margin-left: 20px;
  margin-right: 20px;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
}
</style>