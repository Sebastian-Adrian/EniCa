<script>
export default {
  data() {
    return {
      ablesung: {
        zaehlerNr: '',
        zaehlerstand: '',
        datum: ''
      }
    }
  },
  methods: {
    submitForm() {
      fetch('http://localhost:8080/api/ablesungen', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(this.ablesung)
      })
          .then(response => response.json())
          .then(data => {
            console.log('Success:', data);
          })
          .catch((error) => {
            console.error('Error:', error);
          });
    }
  }
}
</script>

<template>
  <div class="ablesung">
    <h2>Ablesung</h2>
    <form @submit.prevent="submitForm">
      <div class="mb-3">
        <label for="zaehlerNr">Zähler Nr</label>
        <input type="number" id="zaehlerNr" v-model="ablesung.zaehlerNr" class="form-control"/>
      </div>
      <div class="mb-3">
        <label for="zaehlerstand">Zählerstand</label>
        <input type="number" id="zaehlerstand" v-model="ablesung.zaehlerstand" class="form-control"/>
      </div>
      <div class="mb-3">
        <label for="datum">Datum</label>
        <input type="date" id="datum" v-model="ablesung.datum" class="form-control"/>
      </div>
      <button type="submit" class="btn btn-primary">Speichern</button>
    </form>
  </div>
</template>

<style scoped>
.ablesung {
  margin-top: 20px;
  margin-left: 20px;
  margin-right: 20px;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.speichern {
  margin-top: 20px;
  margin-left: 20px;
  margin-right: 20px;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
}
</style>