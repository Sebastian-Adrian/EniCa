<script setup>
import {onMounted, ref, watch} from 'vue'
import axios from 'axios'

const zaehlerList = ref([])
const ablesungList = ref([])
const selectedZaehler = ref(null)
const tarifeFromZaehler = ref([])
const errorMessage = ref('')

const getDifferenz = (index) => {
  if (index === ablesungList.value.length -1) return 0
  return ablesungList.value[index].zaehlerstand - ablesungList.value[index + 1].zaehlerstand
}

async function fetchTarifeFromZaehler(id) {
  try {
    const response = await axios.get(`/api/tarife/zaehler/${id}`);
    tarifeFromZaehler.value = await response.data;
  } catch (error) {
    if (error.response && error.response.data) {
      errorMessage.value = await error.response.data
    } else {
      errorMessage.value = `Ein unbekannter Fehler ist aufgetreten. ${error.message}`
    }
    console.error('Fehler beim Aktualisieren des Tarifs: ', error)
  }
}

const getKosten = (index) => {

  const differenz = getDifferenz(index)
  if (differenz === 0) return 0

  const tarif = tarifeFromZaehler.value.find(tarif => {
    const datum = new Date(ablesungList.value[index].datum)
    return datum >= new Date(tarif.gueltigVon) && datum <= new Date(tarif.gueltigBis)
  })

  if (!tarif) return 'Kein Tarif gefunden'

  return ((differenz * tarif.preisProKwh / 100) + tarif.grundpreis).toFixed(2)

}

const fetchZaehler = () => {
  axios.get('/api/zaehler').then(response => {
    zaehlerList.value = response.data
  })
}

const fetchAblesungen = () => {
  if (!selectedZaehler.value) return
  axios.get(`/api/ablesungen/zaehler/${selectedZaehler.value.id}`).then(response => {
    ablesungList.value = response.data
    ablesungList.value.sort((a, b) => new Date(b.datum) - new Date(a.datum));
  })
}

const formatDate = (date) => {
  return new Date(date).toLocaleDateString([],{ day: '2-digit', month: '2-digit', year: 'numeric' })
};

watch(selectedZaehler, () => {
  fetchAblesungen()
  fetchTarifeFromZaehler(selectedZaehler.value.id)

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
      <tr v-for="(ablesung, index) in ablesungList" :key="ablesung.id">
        <td>{{ ablesung.zaehler.zaehlerNr }}</td>
        <td>{{ formatDate(ablesung.datum) }}</td>
        <td>{{ ablesung.zaehlerstand }}</td>
        <td>{{ getDifferenz(index) }} kWh</td>
        <td>{{ getKosten(index) }}</td>
      </tr>
      </tbody>
    </table>
  </div>
</template>
