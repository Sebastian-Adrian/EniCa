<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const zaehlerList = ref([]);
const selectedZaehler = ref('');
const ablesungen = ref([]);
const newAblesung = ref({ zaehlerstand: '', datum: '', zaehler: '' }); // Neue ref für die neue Ablesung

onMounted(async () => {
  const response = await axios.get('/api/zaehler');
  zaehlerList.value = response.data;
});

const fetchAblesungen = async () => {
  if (selectedZaehler.value) {
    console.log(selectedZaehler.value);
    const response = await axios.get(`/api/ablesungen/zaehler/${selectedZaehler.value.id}`);
    ablesungen.value = response.data.map(ablesung => ({
      ...ablesung,
      editMode: false,
      tempZaehlerstand: ablesung.zaehlerstand,
      tempDatum: ablesung.datum
    }));
    ablesungen.value.sort((a, b) => new Date(b.datum) - new Date(a.datum));
  }
};

const startEditing = (ablesung) => {
  ablesung.editMode = true;
};

const confirmEditing = async (ablesung) => {
  ablesung.editMode = false;
  ablesung.zaehlerstand = ablesung.tempZaehlerstand;
  ablesung.datum = ablesung.tempDatum;
  await updateAblesung(ablesung);
};

const updateAblesung = async (ablesung) => {
  await axios.put(`/api/ablesungen/${ablesung.id}`, ablesung);
};

const deleteAblesung = async (ablesung) => {
  if (window.confirm('Sicher, dass du diese Ablesung löschen möchtest?')) {
    await axios.delete(`/api/ablesungen/${ablesung.id}`);
    const index = ablesungen.value.findIndex(item => item.id === ablesung.id);
    ablesungen.value.splice(index, 1);
  }
};

const createAblesung = async () => {
  newAblesung.value.zaehler = selectedZaehler.value;
  const response = await axios.post('/api/ablesungen', newAblesung.value);
  ablesungen.value.push(response.data);
  newAblesung.value = { zaehlerstand: '', datum: '', zaehler: '' };
};

const formatDate = (value) => {
  const date = new Date(value);
  const day = date.getDate().toString().padStart(2, '0');
  const month = (date.getMonth() + 1).toString().padStart(2, '0');
  const year = date.getFullYear();
  return `${day}.${month}.${year}`;
};

const formatZaehlerstand = (zaehlerstand) => {
  const userLocale = navigator.language || 'de-DE';
  return new Intl.NumberFormat(userLocale).format(zaehlerstand);
};
</script>
<template>
  <div class="ablesungen">
    <select class="form-select" v-model="selectedZaehler" @change="fetchAblesungen">
      <option disabled selected value="">Zähler wählen</option>
      <option v-for="zaehler in zaehlerList" :key="zaehler.id" :value="zaehler">
        {{ zaehler.zaehlerNr }} - {{ zaehler.zaehlerName }}
      </option>
    </select>
    <table class="table mt-3">
      <thead>
      <tr>
        <th>Zählernummer</th>
        <th>Ablesedatum</th>
        <th>Ablesewert</th>
        <th>Aktionen</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="ablesung in ablesungen" :key="ablesung.id">
        <td>{{ selectedZaehler.zaehlerNr }}</td>
        <td>
          <input type="date" class="form-control-sm edit-input" v-if="ablesung.editMode" v-model="ablesung.tempDatum" />
          <span v-else>{{ formatDate(ablesung.datum) }}</span>
        </td>
        <td>
          <input type="number" inputmode="numeric" class="form-control-sm edit-input" v-if="ablesung.editMode" v-model="ablesung.tempZaehlerstand" />
          <span v-else>{{ formatZaehlerstand(ablesung.zaehlerstand) }}</span>
        </td>
        <td class="d-grid gap-2 d-md-flex justify-content-sm-start">
          <button v-if="!ablesung.editMode" class="btn btn-sm btn-primary" @click="startEditing(ablesung)">Bearbeiten</button>
          <button v-else class="btn btn-sm btn-success" @click="confirmEditing(ablesung)">Bestätigen</button>
          <button @click="deleteAblesung(ablesung)" class="btn btn-sm btn-danger">Löschen</button> <!-- Neuer "Löschen"-Button -->
        </td>
      </tr>
      <tr v-if="selectedZaehler['zaehlerNr']">
        <td>{{ selectedZaehler['zaehlerNr'] }}</td>
        <td><input type="date" id="newDatum" v-model="newAblesung.datum" class="form-control-sm"/></td>
        <td>
          <input type="number" inputmode="numeric" id="newZaehlerstand" v-model="newAblesung.zaehlerstand" class="form-control-sm"/>
        </td>
        <td class="d-grid gap-2 d-md-flex justify-content-sm-start">
          <button type="submit" class="btn btn-primary" @click="createAblesung">Hinzufügen</button>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>


<style>
.ablesungen {
  margin-top: 20px;
  margin-left: 20px;
  margin-right: 20px;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.edit-input {
  width: 10em;
  display: inline-block;
  margin-right: 10px;
}
</style>
