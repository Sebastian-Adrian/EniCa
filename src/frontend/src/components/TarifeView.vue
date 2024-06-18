<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const tarifeList = ref([])
const selectedTarif = ref(null)
const zaehlerList = ref([]) // Neue ref für die Zählerliste
const newTarif = ref({ tarifName: '', preisProKwh: '', grundpreis: '', zaehler: '', gueltigVon: '', gueltigBis: '' })
const errorMessage = ref('')

onMounted(async () => {
  try {
    const response = await axios.get('/api/tarife')
    tarifeList.value = response.data
    const responseZaehler = await axios.get('/api/zaehler') // Abrufen der Zählerdaten
    zaehlerList.value = responseZaehler.data
  } catch (error) {
    console.error('Fehler beim Abrufen der Daten:', error)
  }
})

const selectTarif = (tarif) => {
  selectedTarif.value = { ...tarif }
}

const updateTarif = async () => {
  if (selectedTarif.value) {
    try {
      const response = await axios.put(`/api/tarife/${selectedTarif.value.id}`, selectedTarif.value)
      const index = tarifeList.value.findIndex(tarif => tarif.id === selectedTarif.value.id)
      tarifeList.value.splice(index, 1, response.data)
      selectedTarif.value = null
      errorMessage.value = ''
    } catch (error) {
      if (error.response && error.response.data) {
        errorMessage.value = error.response.data
      } else {
        errorMessage.value = 'Ein unbekannter Fehler ist aufgetreten.'
      }
      console.error('Fehler beim Aktualisieren des Tarifs: ', errorMessage.value)
    }
  }
}

const deleteTarif = async (tarif) => {
  if (window.confirm('Sicher, dass du diesen Tarif löschen möchtest?')) {
    try {
      await axios.delete(`/api/tarife/${tarif.id}`)
      const index = tarifeList.value.findIndex(item => item.id === tarif.id)
      tarifeList.value.splice(index, 1)
      errorMessage.value = ''
    } catch (error) {
      errorMessage.value = 'Fehler beim Löschen des Tarifs.'
      console.error('Fehler beim Löschen des Tarifs:', error)
    }
  }
}

const createTarif = async () => {
  try {
    const response = await axios.post('/api/tarife', newTarif.value)
    tarifeList.value.push(response.data)
    newTarif.value = { tarifName: '', preisProKwh: '', grundpreis: '', zaehler: '', gueltigVon: '', gueltigBis: '' }
    errorMessage.value = ''
  } catch (error) {
    if (error.response && error.response.data) {
      errorMessage.value = error.response.data
    } else {
      errorMessage.value = 'Ein unbekannter Fehler ist aufgetreten.'
    }
    console.error('Fehler beim Aktualisieren des Tarifs:', errorMessage.value)
  }
}

const isTarifGueltig = (tarif) => {
  const heute = new Date()
  const gueltigVon = new Date(tarif.gueltigVon)
  const gueltigBis = new Date(tarif.gueltigBis)

  return heute >= gueltigVon && heute <= gueltigBis
}

</script>

<template>
  <div class="tarife p-3 border rounded table-responsive">
    <h3>Tarife</h3>
    <div v-if="errorMessage" class="alert alert-danger">{{ errorMessage }}</div>
    <table class="table table-sm table-striped">
      <thead>
      <tr>
        <th>Bezeichnung</th>
        <th>Preis pro kWh</th>
        <th>Grundpreis</th>
        <th>Gültig von</th>
        <th>Gültig bis</th>
        <th>Zähler</th>
        <th>Aktionen</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="tarif in tarifeList" :key="tarif.id">
        <td>
          <input type="text" v-if="selectedTarif && selectedTarif.id === tarif.id" v-model="selectedTarif.tarifName"
                 class="form-control-sm"/>
          <span v-else>{{ tarif.tarifName }}</span>
          <span v-if="isTarifGueltig(tarif)" class="badge bg-success">Aktuell</span>
          <span v-else class="badge bg-secondary">Abgelaufen</span>
        </td>
        <td>
          <input type="number" inputmode="numeric" v-if="selectedTarif && selectedTarif.id === tarif.id"
                 v-model="selectedTarif.preisProKwh"
                 class="form-control-sm"/>
          <span v-else>{{ tarif.preisProKwh }} €</span>
        </td>
        <td>
          <input type="number" inputmode="numeric" v-if="selectedTarif && selectedTarif.id === tarif.id"
                 v-model="selectedTarif.grundpreis" class="form-control-sm"/>
          <span v-else>{{ tarif.grundpreis }} €</span>
        </td>
        <td>
          <input type="date" v-if="selectedTarif && selectedTarif.id === tarif.id" v-model="selectedTarif.gueltigVon"
                 class="form-control-sm"/>
          <span v-else>{{ new Date(tarif.gueltigVon).toLocaleDateString() }}</span>
        </td>
        <td>
          <input type="date" v-if="selectedTarif && selectedTarif.id === tarif.id" v-model="selectedTarif.gueltigBis"
                 class="form-control-sm"/>
          <span v-else>{{ new Date(tarif.gueltigBis).toLocaleDateString() }}</span>
        </td>
        <td>
          <select v-if="selectedTarif && selectedTarif.id === tarif.id" v-model="selectedTarif.zaehler"
                  class="form-control-sm">
            <option v-for="zaehler in zaehlerList" :key="zaehler.id" :value="zaehler">
              {{ zaehler.zaehlerNr }}
            </option>
          </select>
          <span v-else>{{ tarif.zaehler.zaehlerNr }}</span>
        </td>
        <td class="d-grid gap-2 d-md-flex justify-content-sm-start">
          <button v-if="selectedTarif && selectedTarif.id === tarif.id" @click="updateTarif" class="btn btn-sm btn-primary">
            Speichern
          </button>
          <button v-else @click="selectTarif(tarif)" class="btn btn-sm btn-secondary">Bearbeiten</button>
          <button @click="deleteTarif(tarif)" class="btn btn-sm btn-danger">Löschen</button>

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
        <td class="d-grid gap-2 d-md-flex justify-content-sm-start">
          <button @click="createTarif" class="btn btn-primary">Erstellen</button>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<style scoped>
.tarife {
  margin-top: 20px;
}

.badge {
  margin-left: 5px;
}

</style>
