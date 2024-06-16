<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const zaehlerList = ref([])
const selectedZaehler = ref(null)
const newZaehler = ref({ zaehlerNr: '', zaehlerArt: '' })
const zaehlerArtOptions = ref(['GAS', 'WASSER', 'STROM']); // Ersetzen Sie dies durch die tatsächlichen Werte Ihrer zaehlerArt Enum
const showNewZaehlerForm = ref(false) // Neue ref hinzufügen

onMounted(async () => {
  const response = await axios.get('/api/zaehler')
  zaehlerList.value = response.data
})

const selectZaehler = (zaehler) => {
  selectedZaehler.value = { ...zaehler }
}

const saveZaehler = async () => {
  if (selectedZaehler.value) {
    const response = await axios.put(`/api/zaehler/${selectedZaehler.value.id}`, selectedZaehler.value)
    const index = zaehlerList.value.findIndex(zaehler => zaehler.id === selectedZaehler.value.id)
    zaehlerList.value.splice(index, 1, response.data)
    selectedZaehler.value = null
  }
}

const deleteZaehler = async (zaehler) => {
  if (window.confirm('sicher, dass du diesen Zähler löschen möchtest?')) {
    await axios.delete(`/api/zaehler/${zaehler.id}`)
    const index = zaehlerList.value.findIndex(item => item.id === zaehler.id)
    zaehlerList.value.splice(index, 1)
  }
}

const createZaehler = async () => {
  const response = await axios.post('/api/zaehler', newZaehler.value)
  zaehlerList.value.push(response.data)
  newZaehler.value = { zaehlerNr: '', zaehlerArt: '' }
  showNewZaehlerForm.value = false // Formular verbergen
}

const toggleNewZaehlerForm = () => {
  showNewZaehlerForm.value = !showNewZaehlerForm.value
}
</script>

<template>
  <h3>Zähler</h3>
  <ul class="list-group">
    <li v-for="zaehler in zaehlerList" :key="zaehler.id" class="list-group-item">
      {{ zaehler.zaehlerNr }} - {{ zaehler.zaehlerArt }}
      <button class="btn btn-primary float-end" @click="selectZaehler(zaehler)">Bearbeiten</button>
      <button class="btn btn-danger float-end me-2" @click="deleteZaehler(zaehler)">Löschen</button>
    </li>
  </ul>

  <div v-if="selectedZaehler" class="mt-3">
    <h4>Zähler bearbeiten</h4>
    <form @submit.prevent="saveZaehler">
      <div class="mb-3">
        <label for="zaehlerNr" class="form-label">Zähler Nr</label>
        <input type="number" id="zaehlerNr" v-model="selectedZaehler.zaehlerNr" class="form-control"/>
      </div>
      <div class="mb-3">
        <label for="zaehlerArt" class="form-label">Zähler Art</label>
        <select id="zaehlerArt" v-model="selectedZaehler.zaehlerArt" class="form-control">
          <option v-for="option in zaehlerArtOptions" :key="option" :value="option">
            {{ option }}
          </option>
        </select>
      </div>
      <button type="submit" class="btn btn-primary">Speichern</button>
    </form>
  </div>

  <button class="btn btn-primary mt-3" @click="toggleNewZaehlerForm">Neuen Zähler erstellen</button> <!-- Neuen Button hinzufügen -->
  <div v-if="showNewZaehlerForm" class="mt-3">
    <h4>Neuen Zähler erstellen</h4>
    <form @submit.prevent="createZaehler">
      <div class="mb-3">
        <label for="newZaehlerNr" class="form-label">Zähler Nr</label>
        <input type="number" id="newZaehlerNr" v-model="newZaehler.zaehlerNr" class="form-control"/>
      </div>
      <div class="mb-3">
        <label for="newZaehlerArt" class="form-label">Zähler Art</label>
        <select id="newZaehlerArt" v-model="newZaehler.zaehlerArt" class="form-control">
          <option v-for="option in zaehlerArtOptions" :key="option" :value="option">
            {{ option }}
          </option>
        </select>
      </div>
      <button type="submit" class="btn btn-primary">Erstellen</button>
    </form>
  </div>
</template>

<style scoped>
.list-group-item {
  position: relative;
}
</style>