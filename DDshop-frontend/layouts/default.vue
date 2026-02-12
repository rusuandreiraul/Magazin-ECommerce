<script setup>
import Sidebar from '~/components/sidebar.vue'
import Navbar from '~/components/navbar.vue'
import Footer from '~/components/footer.vue'
import { useAttrs } from 'vue'

useAuth()

</script>
<template>
<div class="flex flex-col min-h-screen">
  <Navbar />
  
  <main class="flex-grow max-w-7xl mx-auto p-6 pb-24">
    <slot />
  </main>

  <Footer />
</div>
</template>