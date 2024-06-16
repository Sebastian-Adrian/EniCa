<template>
  <nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container-fluid">
      <a class="navbar-brand" href="#">My App</a>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav">
          <li class="nav-item">
            <RouterLink class="nav-link" to="/">Home</RouterLink>
          </li>
          <li class="nav-item">
            <RouterLink class="nav-link" to="/zaehler">Zähler</RouterLink>
          </li>
          <li class="nav-item">
            <RouterLink class="nav-link" to="/ablesung">Ablesung</RouterLink>
          </li>
        </ul>
      </div>
    </div>
  </nav>
  <main>
    <RouterView />
  </main>
</template>

<script>

import { RouterLink, RouterView } from 'vue-router'
export default {
  name: 'App',
  components: {
    RouterLink,
    RouterView
  }

}
</script>
