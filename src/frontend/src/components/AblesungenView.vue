<script>
import axios from 'axios'

export default {
  data() {
    return {
      zaehlerList: [],
      selectedZaehler: null,
      ablesungen: []
    }
  },
  async created() {
    const response = await axios.get('/api/zaehler')
    this.zaehlerList = response.data
  },
  methods: {
    async fetchAblesungen() {
      if (this.selectedZaehler) {
        const response = await axios.get(`/api/ablesungen/zaehler/${this.selectedZaehler.zaehlerNr}`)
        this.ablesungen = response.data.map(ablesung => ({
          ...ablesung,
          editMode: false,
          tempZaehlerstand: ablesung.zaehlerstand,
          tempDatum: ablesung.datum
        }))
        this.ablesungen.sort((a, b) => new Date(b.datum) - new Date(a.datum))
      }
    },
    startEditing(ablesung) {
      ablesung.editMode = true
    },
    async confirmEditing(ablesung) {
      ablesung.editMode = false
      ablesung.zaehlerstand = ablesung.tempZaehlerstand
      ablesung.datum = ablesung.tempDatum
      await this.updateAblesung(ablesung)
    },
    async updateAblesung(ablesung) {
      await axios.put(`/api/ablesungen/${ablesung.id}`, ablesung)
    },
    formatDate(value) {
      const date = new Date(value)
      const day = date.getDate().toString().padStart(2, '0')
      const month = (date.getMonth() + 1).toString().padStart(2, '0') // Monate beginnen bei 0 in JavaScript
      const year = date.getFullYear()
      return `${day}.${month}.${year}`
    }
  },
}
</script>

<template>
  <div class="ablesungen">
    <select class="form-select" v-model="selectedZaehler" @change="fetchAblesungen">
      <option v-for="zaehler in zaehlerList" :key="zaehler.id" :value="zaehler">
        {{ zaehler.zaehlerNr }}
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
          <input type="text" class="form-control-sm edit-input" v-if="ablesung.editMode" v-model="ablesung.tempZaehlerstand" />
          <span v-else>{{ ablesung.zaehlerstand }}</span>
        </td>
        <td>
          <button v-if="!ablesung.editMode" class="btn btn-sm btn-primary" @click="startEditing(ablesung)">Bearbeiten</button>
          <button v-else class="btn btn-sm btn-success" @click="confirmEditing(ablesung)">Bestätigen</button>
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