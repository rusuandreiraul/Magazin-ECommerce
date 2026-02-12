<script setup>
import { BuildingStorefrontIcon, TruckIcon } from '@heroicons/vue/24/outline'
</script>

<template>
  <footer class="bg-gray-800 text-white p-6 text-center fixed bottom-0 w-full z-50">
    <div class="flex flex-col md:flex-row items-center justify-center gap-6">
    
      <div class="flex items-center gap-2">
        <BuildingStorefrontIcon class="w-6 h-6 text-green-400" />
        <span>Ridicare gratuită din magazin</span>
      </div>

      
      <div class="flex items-center gap-2">
        <TruckIcon class="w-6 h-6 text-yellow-400" />
        <span>Livrare rapidă în 24h oriunde în România</span>
      </div>
    </div>

    <p class="mt-4 text-sm text-gray-400">© 2025 DD Shop.</p>
  </footer>
</template>
