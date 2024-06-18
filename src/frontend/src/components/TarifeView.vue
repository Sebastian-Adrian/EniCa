<script setup>
import {ref, onMounted} from 'vue'
import axios from 'axios'

const tarifeList = ref([])
const selectedTarif = ref(null)
const zaehlerList = ref([]) // Neue ref für die Zählerliste
const newTarif = ref({ tarifName: '', preisProKwh: '', grundpreis: '', zaehler: '', gueltigVon: '', gueltigBis: ''})

onMounted(async () => {
  const response = await axios.get('/api/tarife')
  tarifeList.value = response.data
  const responseZaehler = await axios.get('/api/zaehler') // Abrufen der Zählerdaten
  zaehlerList.value = responseZaehler.data
})

const selectTarif = (tarif) => {
  selectedTarif.value = { ...tarif }
}

const updateTarif = async () => {
  if (selectedTarif.value) {
    const response = await axios.put(`/api/tarife/${selectedTarif.value.id}`, selectedTarif.value)
    const index = tarifeList.value.findIndex(tarif => tarif.id === selectedTarif.value.id)
    tarifeList.value.splice(index, 1, response.data)
    selectedTarif.value = null
  }
}

const deleteTarif = async (tarif) => {
  if (window.confirm('Sicher, dass du diesen Tarif löschen möchtest?')) {
    await axios.delete(`/api/tarife/${tarif.id}`)
    const index = tarifeList.value.findIndex(item => item.id === tarif.id)
    tarifeList.value.splice(index, 1)
  }
}

const createTarif = async () => {
  const response = await axios.post('/api/tarife', newTarif.value)
  tarifeList.value.push(response.data)
  newTarif.value = { tarifName: '', preisProKwh: '', grundpreis: '', zaehler: '', gueltigVon: '', gueltigBis: ''}
}

</script>

<template>
  <div class="tarife p-3 border rounded">
    <h3>Tarife</h3>
    <table class="table">
      <thead>
      <tr>
        <th>Bezeichnung</th>
        <th>Preis pro kWh</th>
        <th>Grundpreis</th>
        <th>Gültig ab</th>
        <th>Gültig bis</th>
        <th>Zähler</th>
        <th>Aktionen</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="tarif in tarifeList" :key="tarif.id">
        <td>
          <input type="text" v-if="selectedTarif && selectedTarif.id === tarif.id" v-model="selectedTarif.tarifName" class="form-control-sm"/>
          <span v-else>{{ tarif.tarifName }}</span>
        </td>
        <td>
          <input type="number" inputmode="numeric" v-if="selectedTarif && selectedTarif.id === tarif.id"
                 v-model="selectedTarif.preisProKwh"
                 class="form-control-sm"/>
          <span v-else>{{ tarif.preisProKwh }}</span>
        </td>
        <td>
          <input type="number" inputmode="numeric" v-if="selectedTarif && selectedTarif.id === tarif.id" v-model="selectedTarif.grundpreis" class="form-control-sm"/>
          <span v-else>{{ tarif.grundpreis }}</span>
        </td>
        <td>
          <input type="date" v-if="selectedTarif && selectedTarif.id === tarif.id" v-model="selectedTarif.gueltigVon" class="form-control-sm"/>
          <span v-else>{{ tarif.gueltigVon }}</span>
        </td>
        <td>
          <input type="date" v-if="selectedTarif && selectedTarif.id === tarif.id" v-model="selectedTarif.gueltigBis" class="form-control-sm"/>
          <span v-else>{{ tarif.gueltigBis }}</span>
        </td>
        <td>
          <select v-if="selectedTarif && selectedTarif.id === tarif.id" v-model="selectedTarif.zaehler" class="form-control-sm">
            <option v-for="zaehler in zaehlerList" :key="zaehler.id" :value="zaehler">
              {{ zaehler.zaehlerNr }}
            </option>
          </select>
          <span v-else>{{ tarif.zaehler.zaehlerNr }}</span>
        </td>
        <td>
          <button v-if="selectedTarif && selectedTarif.id === tarif.id" @click="updateTarif" class="btn btn-primary">Speichern</button>
          <button v-else @click="selectTarif(tarif)" class="btn btn-secondary">Bearbeiten</button>
          <button @click="deleteTarif(tarif)" class="btn btn-danger">Löschen</button>
        </td>
      </tr>
      <tr>
        <td><input v-model="newTarif.tarifName" class="form-control-sm"/></td>
        <td><input v-model="newTarif.preisProKwh" class="form-control-sm"/></td>
        <td><input v-model="newTarif.grundpreis" class="form-control-sm"/></td>
        <td><input type="date" v-model="newTarif.gueltigVon" class="form-control-sm"/></td>
        <td><input type="date" v-model="newTarif.gueltigBis" class="form-control-sm"/></td>
        <td>
          <select v-model="newTarif.zaehler" class="form-control-sm">
            <option v-for="zaehler in zaehlerList" :key="zaehler.id" :value="zaehler">
              {{ zaehler.zaehlerNr }}
            </option>
          </select>
        </td>
        <td><button @click="createTarif" class="btn btn-primary">Erstellen</button></td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<style scoped>

.tarife {
  margin-top: 20px;
}

</style>