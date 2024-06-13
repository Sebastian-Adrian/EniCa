<script>
export default {
  name: 'ZählerAblesung',
  data () {
    return {
      msg: ''
    }
  },
  mounted() {
    fetch("api/messages/hello")
      .then(response => response.text())
      .then(data => {
        this.msg = data
      })
      .catch(error => {
        console.error("Error fetching hello", error)
      })
  }
}
</script>

<template>
  <div class="ablesung">
    <h1>{{ msg }}</h1>
    <div class="mb-3">
      <label for="zaehlerstand">Zählerstand</label>
      <input type="number" id="zaehlerstand" class="form-control"/>
    </div>
    <div class="mb-3">
      <label for="datum">Datum</label>
      <input type="date" id="datum" class="form-control"/>
    </div>
  </div>
  <div class="speichern">
    <div class="mb-3">
      <button class="btn btn-primary">Speichern</button>
    </div>
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