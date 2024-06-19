<script setup>

import { ref, onMounted} from 'vue'
import axios from 'axios'

const zaehlerList = ref([])
const selectedZaehler = ref(null)
const zaehlerstandMap = ref({})
const newZaehler = ref({ zaehlerNr: '', zaehlerArt: '', zaehlerName: ''})
const zaehlerArtOptions = ref(['GAS', 'WASSER', 'STROM']);

const errorMessage = ref('')

onMounted(async () => {
  try {
    const response = await axios.get('/api/zaehler')
    zaehlerList.value = response.data
  } catch (error) {
    errorMessage.value = `Es gab einen Fehler beim Abrufen der Zähler: ${error.message}`
  }
})

const selectZaehler = (zaehler) => {
  selectedZaehler.value = { ...zaehler }
}

const saveZaehler = async () => {
  if (selectedZaehler.value) {
    try {
      const response = await axios.put(`/api/zaehler/${selectedZaehler.value.id}`, selectedZaehler.value)
      const index = zaehlerList.value.findIndex(zaehler => zaehler.id === selectedZaehler.value.id)
      zaehlerList.value.splice(index, 1, response.data)
    } catch (error) {
      errorMessage.value = `Es gab einen Fehler beim Speichern des Zählers: ${error.message}`
    }
    selectedZaehler.value = null
  }
}

const deleteZaehler = async (zaehler) => {
  if (window.confirm('sicher, dass du diesen Zähler löschen möchtest?')) {
    try {
      await axios.delete(`/api/zaehler/${zaehler.id}`)
      const index = zaehlerList.value.findIndex(item => item.id === zaehler.id)
      zaehlerList.value.splice(index, 1)
    } catch (error) {
      errorMessage.value = `Es gab einen Fehler beim Löschen des Zählers: ${error.message}`
    }
  }
}

const createZaehler = async () => {
  try {
    const response = await axios.post('/api/zaehler', newZaehler.value)
    zaehlerList.value.push(response.data)
    newZaehler.value = { zaehlerNr: '', zaehlerArt: '', zaehlerName: '' }
  } catch (error) {
    errorMessage.value = `Es gab einen Fehler beim Erstellen des Zählers: ${error.message}`
  }
}

const getZaehlerArtClass = (zaehlerArt) => {
  switch (zaehlerArt) {
    case 'GAS':
      return 'bg-secondary text-white p-1 border-dark rounded-pill badge'
    case 'WASSER':
      return 'bg-primary text-white p-1 border-dark rounded-pill badge'
    case 'STROM':
      return 'bg-warning text-white p-1 border-dark rounded-pill badge'
    default:
      return ''
  }
}

const fetchLatestZaehlerstand = async () => {
  for (const zaehler of zaehlerList.value) {
    const response = await axios.get(`/api/ablesungen/zaehler/${zaehler.id}`)
    const ablesungen = response.data
    if (ablesungen.length > 0) {
      ablesungen.sort((a, b) => new Date(b.datum) - new Date(a.datum))
      zaehlerstandMap.value[zaehler.zaehlerNr] = ablesungen[0].zaehlerstand.toLocaleString()
    } else {
      zaehlerstandMap.value[zaehler.zaehlerNr] = 'N/A'
    }
  }
}

onMounted(async () => {

  if (zaehlerList.value.length > 0) {
    const response = await axios.get('/api/zaehler')
    zaehlerList.value = response.data
    await fetchLatestZaehlerstand()
  }
})

</script>

<template>

  <div class="zaehler p-3 border rounded">
    <h3>Zähler</h3>
    <!-- Fehlermeldung anzeigen, wenn errorMessage nicht null ist -->
    <div v-if="errorMessage" class="alert alert-danger">{{ errorMessage }}</div>
    <table class="table">
      <thead>
      <tr>
        <th>Zählernummer</th>
        <th>Zählername</th>
        <th>Zählerart</th>
        <th>Zählerstand</th>
        <th>Aktionen</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="zaehler in zaehlerList" :key="zaehler.id">
        <td>
          <input v-if="selectedZaehler && selectedZaehler.id === zaehler.id" v-model="selectedZaehler.zaehlerNr" class="form-control"/>
          <span v-else>{{ zaehler.zaehlerNr }}</span>
        </td>
        <td>
          <input v-if="selectedZaehler && selectedZaehler.id === zaehler.id" v-model="selectedZaehler.zaehlerName" class="form-control"/>
          <span v-else>{{ zaehler.zaehlerName }}</span>
        </td>
        <td>
          <select v-if="selectedZaehler && selectedZaehler.id === zaehler.id" id="newZaehlerArt" v-model="selectedZaehler.zaehlerArt" class="form-control">
            <option v-for="option in zaehlerArtOptions" :key="option" :value="option">
              {{ option }}
            </option>
          </select>
          <span v-else :class="getZaehlerArtClass(zaehler.zaehlerArt)">{{ zaehler.zaehlerArt }}</span>
        </td>
        <td>
          <span class="bg-light text-black p-1 border-dark rounded-pill badge" > {{ zaehlerstandMap[zaehler.zaehlerNr] }}</span>
        </td>
        <td class="d-grid gap-2 d-md-flex justify-content-sm-start">
          <button v-if="selectedZaehler && selectedZaehler.id" @click="saveZaehler" class="btn btn-primary">Speichern</button>
          <button v-else @click="selectZaehler(zaehler)" class="btn btn-secondary">Bearbeiten</button>
          <button v-if="!selectedZaehler" @click="deleteZaehler(zaehler)" class="btn btn-danger">Löschen</button>
          <button v-else @click="selectedZaehler = null" class="btn btn-secondary">Abbrechen</button>
        </td>
      </tr>
      <tr>
        <td><input v-model="newZaehler.zaehlerNr" class="form-control"></td>
        <td><input v-model="newZaehler.zaehlerName" class="form-control"></td>
        <td>
          <select v-model="newZaehler.zaehlerArt" class="form-control">
            <option v-for="option in zaehlerArtOptions" :key="option" :value="option">
              {{ option }}
            </option>
          </select>
        </td>
        <td><button @click="createZaehler" class="btn btn-primary">Erstellen</button></td>
      </tr>
      </tbody>
    </table>
  </div>

</template>

<style scoped>

.zaehler {
  margin-top: 20px;
  margin-left: 20px;
  margin-right: 20px;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

</style>