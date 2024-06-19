<template>
  <nav class="navbar navbar-expand-lg">
    <div class="container-fluid">
      <a class="navbar-brand" href="#">EniCa</a>
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
            <RouterLink class="nav-link" to="/tarife">Tarife</RouterLink>
          </li>
          <li class="nav-item">
            <RouterLink class="nav-link" to="/ablesungen">Ablesungen</RouterLink>
          </li>
          <li class="nav-item">
            <RouterLink class="nav-link" to="/kosten">Kosten</RouterLink>
          </li>
        </ul>
      </div>
      <!-- Bootstrap 5 DarkMode Toggle -->
      <div class="form-check form-switch">
        <input class="form-check-input" type="checkbox" id="darkModeSwitch" checked>
        <label class="form-check-label" for="darkModeSwitch">Dark Mode</label>
      </div>
    </div>
  </nav>
  <main>
    <div class="container">
      <RouterView/>
    </div>
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

// Bootstrap 5 DarkMode Toggle
document.addEventListener('DOMContentLoaded', () => {
  const htmlElement = document.documentElement;
  const switchElement = document.getElementById('darkModeSwitch');

  const currentTheme = localStorage.getItem('bsTheme') || 'dark';
  htmlElement.setAttribute('data-bs-theme', currentTheme);
  switchElement.checked = currentTheme === 'dark';

  switchElement.addEventListener('change', function () {
    if (this.checked) {
      htmlElement.setAttribute('data-bs-theme', 'dark');
      localStorage.setItem('bsTheme', 'dark');
    } else {
      htmlElement.setAttribute('data-bs-theme', 'light');
      localStorage.setItem('bsTheme', 'light');
    }
  });
});
</script>

<style lang="sass">

$enable-dark-mode: false !default

</style>

