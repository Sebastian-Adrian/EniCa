<script setup>

import { ref, onMounted} from 'vue'
import axios from 'axios'

const zaehlerList = ref([])
const zaehlerstandMap = ref({})
const newZaehler = ref({ zaehlerNr: '', zaehlerArt: '', zaehlerName: ''})
const zaehlerArtOptions = ref(['GAS', 'WASSER', 'STROM']);

const errorMessage = ref('')

onMounted(async () => {
  try {
    const response = await axios.get('/api/zaehler')
    zaehlerList.value = response.data.map(zaehler => ({
      ...zaehler,
      editMode: false,
      tempZaehlerNr: zaehler.zaehlerNr,
      tempZaehlerName: zaehler.zaehlerName
    }));
    await fetchLatestZaehlerstand()
  } catch (error) {
    errorMessage.value = `Es gab einen Fehler beim Abrufen der Zähler: ${error.message}`
    console.error('Fehler beim Abrufen der Zähler:', error)
  }
})


const startEditing = (zaehler) => {
  zaehler.editMode = true;
};

const abortEditing = (zaehler) => {
  zaehler.editMode = false;
  zaehler.tempZaehlerNr = zaehler.zaehlerNr;
  zaehler.tempZaehlerName = zaehler.zaehlerName;
};


const saveZaehler = async (zaehler) => {
  if (zaehler) {
    try {
      const response = await axios.put(`/api/zaehler/${zaehler.id}`, zaehler)
      const index = zaehlerList.value.findIndex(oldZaehler => oldZaehler.id === oldZaehler.id)
      zaehlerList.value.splice(index, 1, response.data)
      zaehler.editMode = false
    } catch (error) {
      errorMessage.value = `Es gab einen Fehler beim Speichern des Zählers: ${error.message}`
      console.error('Fehler beim Speichern des Zählers:', error)
    }
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
      console.error('Fehler beim Löschen des Zählers:', error)
    }
  }
}

const createZaehler = async () => {
  try {
    const response = await axios.post('/api/zaehler', newZaehler.value)

    response.data.editMode = false
    response.data.tempZaehlerNr = response.data.zaehlerNr
    response.data.tempZaehlerName = response.data.zaehlerName

    zaehlerList.value.push(response.data)

    newZaehler.value = { zaehlerNr: '', zaehlerArt: '', zaehlerName: '' }
  } catch (error) {
    errorMessage.value = `Es gab einen Fehler beim Erstellen des Zählers: ${error.message}`
    console.error('Fehler beim Erstellen des Zählers:', error)
  }
}

const getZaehlerArtClass = (zaehlerArt) => {
  switch (zaehlerArt) {
    case 'GAS':
      return 'bg-secondary text-white border-dark rounded-pill badge'
    case 'WASSER':
      return 'bg-primary border-dark rounded-pill badge'
    case 'STROM':
      return 'bg-warning text-black border-dark rounded-pill badge'
    default:
      return ''
  }
}

const fetchLatestZaehlerstand = async () => {
  for (const zaehler of zaehlerList.value) {
    try {
      const response = await axios.get(`/api/ablesungen/zaehler/${zaehler.id}`)
      const ablesungen = response.data
      if (ablesungen.length > 0) {
        ablesungen.sort((a, b) => new Date(b.datum) - new Date(a.datum))
        zaehlerstandMap.value[zaehler.zaehlerNr] = ablesungen[0].zaehlerstand.toLocaleString()
      } else {
        zaehlerstandMap.value[zaehler.zaehlerNr] = 'N/A'
      }
    } catch (error) {
      errorMessage.value = `Es gab einen Fehler beim Abrufen des Zählerstands: ${error.message}`
      console.error('Fehler beim Abrufen des Zählerstands:', error)
    }
  }
}

</script>

<template>

  <div class="zaehler p-3 border rounded">
    <h3>Zähler</h3>
    <!-- Fehlermeldung anzeigen, wenn errorMessage nicht null ist -->
    <div v-if="errorMessage" class="alert alert-danger">{{ errorMessage }}</div>
    <table class="table table-sm table-striped">
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
          <input v-if="zaehler.editMode" v-model="zaehler.tempZaehlerNr" class="form-control"/>
          <span v-else>{{ zaehler.zaehlerNr }}</span>
        </td>
        <td>
          <input v-if="zaehler.editMode" v-model="zaehler.tempZaehlerName" class="form-control"/>
          <span v-else>{{ zaehler.zaehlerName }}</span>
        </td>
        <td>
          <select v-if="zaehler.editMode" id="newZaehlerArt" v-model="zaehler.zaehlerArt" class="form-control">
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
          <button v-if="zaehler.editMode" @click="saveZaehler(zaehler)" class="btn btn-primary">Speichern</button>
          <button v-else @click="startEditing(zaehler)" class="btn btn-secondary">Bearbeiten</button>
          <button v-if="!zaehler.editMode" @click="deleteZaehler(zaehler)" class="btn btn-danger">Löschen</button>
          <button v-else @click="abortEditing(zaehler)" class="btn btn-secondary">Abbrechen</button>
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