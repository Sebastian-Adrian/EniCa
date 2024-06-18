<script setup>

import { ref, onMounted} from 'vue'
import axios from 'axios'

const zaehlerList = ref([])
const selectedZaehler = ref(null)
const zaehlerstandMap = ref({})
const newZaehler = ref({ zaehlerNr: '', zaehlerArt: '', zaehlerName: ''})
const zaehlerArtOptions = ref(['GAS', 'WASSER', 'STROM']);
const showNewZaehlerForm = ref(false)

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
  newZaehler.value = { zaehlerNr: '', zaehlerArt: '', zaehlerName: ''}
  showNewZaehlerForm.value = false // Formular verbergen
}

/*
const toggleNewZaehlerForm = () => {
  showNewZaehlerForm.value = !showNewZaehlerForm.value
}
*/
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
  const response = await axios.get('/api/zaehler')
  zaehlerList.value = response.data
  await fetchLatestZaehlerstand()
})

</script>

<template>

  <div class="zaehler p-3 border rounded">
    <h3>Zähler</h3>
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
          <button @click="deleteZaehler(zaehler)" class="btn btn-danger">Löschen</button>
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

<!--  <div class="zaehler"><h3>Zähler</h3>
    <ul class="list-group">
      <li v-for="zaehler in zaehlerList" :key="zaehler.id" class="list-group-item">
        {{ zaehler.zaehlerNr }} - {{ zaehler.zaehlerName }} – <span :class="getZaehlerArtClass(zaehler.zaehlerArt)">{{ zaehler.zaehlerArt }}</span>
        <span class="bg-light text-black p-1 border-dark rounded-pill badge" > {{ zaehlerstandMap[zaehler.zaehlerNr] }}</span> &lt;!&ndash; Letzten Zählerstand anzeigen &ndash;&gt;
        <button class="btn btn-primary float-end btn-sm" @click="selectZaehler(zaehler)">Bearbeiten</button>
        <button class="btn btn-danger float-end me-2 btn-sm" @click="deleteZaehler(zaehler)">Löschen</button>
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

    <button class="btn btn-primary mt-3" @click="toggleNewZaehlerForm">Neuen Zähler erstellen</button>
    <div v-if="showNewZaehlerForm" class="mt-3">
      <h4>Neuen Zähler erstellen</h4>
      <form @submit.prevent="createZaehler">
        <div class="mb-3">
          <label for="newZaehlerNr" class="form-label">Zähler Nr</label>
          <input type="number" id="newZaehlerNr" v-model="newZaehler.zaehlerNr" class="form-control"/>
        </div>
        <div class="mb-3">
          <label for="newZaehlerName" class="form-label">Name</label>
          <input type="text" id="newZaehlerName" v-model="newZaehler.zaehlerName" class="form-control"/>
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
  </div>-->
</template>

<style scoped>
.list-group-item {
  position: relative;
}

.zaehler {
  margin-top: 20px;
  margin-left: 20px;
  margin-right: 20px;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

</style>