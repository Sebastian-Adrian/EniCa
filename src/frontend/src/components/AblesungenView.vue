<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const zaehlerList = ref([]);
const selectedZaehler = ref('');
const ablesungList = ref([]);
const newAblesung = ref({ zaehlerstand: '', datum: '', zaehler: '' });

const errorMessage = ref('');

onMounted(async () => {

  try {
    const responseZaehler = await axios.get('/api/zaehler');
    zaehlerList.value = responseZaehler.data;
  } catch (error) {
    errorMessage.value = `Fehler beim Abrufen der Daten: ${error.message}`;
    console.error('Fehler beim Abrufen der Daten:', error);
  }
});

const fetchAblesungen = async (zaehler) => {
  if (zaehler) {
    try {
      const response = await axios.get(`/api/ablesungen/zaehler/${zaehler.id}`);
      ablesungList.value = response.data.map(ablesung => ({
        ...ablesung,
        editMode: false,
        tempZaehlerstand: ablesung.zaehlerstand,
        tempDatum: ablesung.datum
      }));
      ablesungList.value.sort((a, b) => new Date(b.datum) - new Date(a.datum));
    } catch (error) {
      errorMessage.value = `Fehler beim Abrufen der Ablesungen: ${error.message}`;
      console.error('Fehler beim Abrufen der Ablesungen:', error);
    }
  }
};

const startEditing = (ablesung) => {
  ablesung.editMode = true;
};

const abortEditing = (ablesung) => {
  ablesung.editMode = false;
  ablesung.tempZaehlerstand = ablesung.zaehlerstand;
  ablesung.tempDatum = ablesung.datum;
};

const updateAblesung = async (ablesung) => {
  try {
    await axios.put(`/api/ablesungen/${ablesung.id}`, ablesung);
    ablesung.editMode = false;
    ablesung.zaehlerstand = ablesung.tempZaehlerstand;
    ablesung.datum = ablesung.tempDatum;
  } catch (error) {
    errorMessage.value = `Fehler beim Aktualisieren der Ablesung: ${error.message}`;
    console.error('Fehler beim Aktualisieren der Ablesung:', error);
  }
};

const deleteAblesung = async (ablesung) => {
  if (window.confirm('Sicher, dass du diese Ablesung löschen möchtest?')) {
    try {
      await axios.delete(`/api/ablesungen/${ablesung.id}`);
      const index = ablesungList.value.findIndex(oldAblesung => oldAblesung.id === ablesung.id);
      ablesungList.value.splice(index, 1);
    } catch (error) {
      errorMessage.value = `Fehler beim Löschen der Ablesung: ${error.message}`;
      console.error('Fehler beim Löschen der Ablesung:', error);
    }
  }
};

const createAblesung = async () => {

  if (!newAblesung.value.datum || !newAblesung.value.zaehlerstand) {
    errorMessage.value = 'Bitte fülle alle Felder aus.';
    return;
  }
  try {
    newAblesung.value.zaehler = selectedZaehler.value
    const response = await axios.post('/api/ablesungen', newAblesung.value);

    response.data.editMode = false;
    response.data.tempZaehlerstand = response.data.zaehlerstand;
    response.data.tempDatum = response.data.datum;

    ablesungList.value.push(response.data);

    newAblesung.value = { zaehlerstand: '', datum: '', zaehler: '' };
  } catch (error) {
    errorMessage.value = `Fehler beim Erstellen der Ablesung: ${error.message}`;
    console.error('Fehler beim Erstellen der Ablesung:', error);
  }
};

const formatDate = (date) => {
  return new Date(date).toLocaleDateString([],{ day: '2-digit', month: '2-digit', year: 'numeric' })
};

const formatZaehlerstand = (zaehlerstand) => {
  const userLocale = navigator.language || 'de-DE';
  return new Intl.NumberFormat(userLocale).format(zaehlerstand);
};

</script>
<template>
  <div class="ablesungen">
    <h3>Ablesungen</h3>
    <div v-if="errorMessage" class="alert alert-danger">{{ errorMessage }}</div>
    <select class="form-select" v-model="selectedZaehler" @change="fetchAblesungen(selectedZaehler)">
      <option disabled selected value="">Zähler wählen</option>
      <option v-for="zaehler in zaehlerList" :key="zaehler.id" :value="zaehler">
        {{ zaehler.zaehlerNr }} - {{ zaehler.zaehlerName }}
      </option>
    </select>
    <table class="table table-sm table-striped">
      <thead>
      <tr>
        <th>Zählernummer</th>
        <th>Ablesedatum</th>
        <th>Ablesewert</th>
        <th>Aktionen</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="ablesung in ablesungList" :key="ablesung.id">
        <td>{{ selectedZaehler.zaehlerNr }}</td>
        <td>
          <input v-if="ablesung.editMode" v-model="ablesung.tempDatum" type="date" class="form-control-sm edit-input" />
          <span v-else>{{ formatDate(ablesung.datum) }}</span>
        </td>
        <td>
          <input v-if="ablesung.editMode" v-model="ablesung.tempZaehlerstand" type="number" inputmode="numeric" class="form-control-sm edit-input"  />
          <span v-else>{{ formatZaehlerstand(ablesung.zaehlerstand) }}</span>
        </td>
        <td class="d-grid gap-2 d-md-flex justify-content-sm-start">
          <button v-if="!ablesung.editMode" class="btn btn-sm btn-primary" @click="startEditing(ablesung)">Bearbeiten</button>
          <button v-else class="btn btn-sm btn-success" @click="updateAblesung(ablesung)">Bestätigen</button>
          <button v-if="!ablesung.editMode" @click="deleteAblesung(ablesung)" class="btn btn-sm btn-danger">Löschen</button> <!-- Neuer "Löschen"-Button -->
          <button v-else @click="abortEditing(ablesung)" class="btn btn-sm btn-secondary">Abbrechen</button>
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
