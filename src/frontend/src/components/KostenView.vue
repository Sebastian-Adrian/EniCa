<script setup>
import { ref, watch, onMounted } from 'vue'
import axios from 'axios'

const zaehlerList = ref([])
const ablesungen = ref([])
const selectedZaehler = ref(null)

const getDifferenz = (index) => {
  if (index === 0) return 0
  console.log(ablesungen.value[index].zaehlerstand)
return ablesungen.value[index].zaehlerstand - ablesungen.value[index - 1].zaehlerstand
}

const getKosten = (index) => {
  if (!selectedZaehler.value) return 0
  //const differenz = getDifferenz(index)
  console.log(selectedZaehler.value)
  console.log(index)
  //const preisProKwh = selectedZaehler.value.tarif.preisProKwh

}

const fetchZaehler = () => {
  axios.get('/api/zaehler').then(response => {
    console.log('Zaehler data:', response.data)  // Log the returned data
    zaehlerList.value = response.data
  })
}

const fetchAblesungen = () => {
  if (!selectedZaehler.value) return
  axios.get(`/api/ablesungen/zaehler/${selectedZaehler.value.id}`).then(response => {
    console.log('Ablesungen data:', response.data)  // Log the returned data
    ablesungen.value = response.data
  })
}

watch(selectedZaehler, () => {
  fetchAblesungen()
})

onMounted(() => {
  fetchZaehler()
})
</script>

<template>
  <div class="container mt-4">
    <div class="mb-3">
      <label for="zaehlerSelect" class="form-label">Wählen Sie einen Zähler:</label>
      <select class="form-select" v-model="selectedZaehler" id="zaehlerSelect" @change="fetchAblesungen">
        <option v-for="zaehler in zaehlerList" :key="zaehler.id" :value="zaehler">
          {{ zaehler.zaehlerNr }} - {{ zaehler.zaehlerName }}
        </option>
      </select>
    </div>

    <table v-if="selectedZaehler" class="table table-striped">
      <thead class="thead-dark">
      <tr>
        <th>Zähler</th>
        <th>Ablesedatum</th>
        <th>Ablesewert</th>
        <th>Differenz zur vorherigen Ablesung</th>
        <th>Kosten</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(ablesung, index) in ablesungen" :key="ablesung.id">
        <td>{{ ablesung.zaehler.zaehlerNr }}</td>
        <td>{{ ablesung.datum }}</td>
        <td>{{ ablesung.zaehlerstand }}</td>
        <td>{{ getDifferenz(index) }} kWh</td>
        <td>{{ getKosten(index) }}</td>
      </tr>
      </tbody>
    </table>
  </div>
</template>
