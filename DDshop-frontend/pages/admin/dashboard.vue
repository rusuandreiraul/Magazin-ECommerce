<script setup>
import addProduct from '~/components/addProduct.vue'
import editProduct from '~/components/editProduct.vue'
import addAdmin from '~/components/addAdmin.vue'
import seeOrders from '~/components/seeOrders.vue'
import ImportProducts from '~/components/ImportProducts.vue'
import PromoProducts from '~/components/PromoProducts.vue'
import { HomeIcon, PlusIcon, PencilIcon, UserPlusIcon, ClipboardDocumentListIcon, ArrowUpOnSquareIcon, StarIcon } from '@heroicons/vue/24/outline'

const activeComponent = ref(null)

function add() {
  activeComponent.value = addProduct
}

function edit() {
  activeComponent.value = editProduct
}

function orders() {
  activeComponent.value = seeOrders
}

function admin() {
  activeComponent.value = addAdmin
}
function importProducts(){
  activeComponent.value=ImportProducts
}

function promo(){
  activeComponent.value=PromoProducts
}


</script>

<template>
  <div class="min-h-screen bg-gray-50">

    <nav class="bg-white shadow-md px-8 py-5 flex items-center justify-between">
      <h1 class="text-2xl font-bold text-indigo-600">Admin Dashboard</h1>
      <NuxtLink to="/" class="text-gray-600 hover:text-indigo-600 transition-all">
        <HomeIcon class="w-8 h-8" />
      </NuxtLink>
    </nav>

  
    <div class="bg-indigo-100 py-6 px-8 flex gap-4 flex-wrap justify-center">
      <button @click="add" class="flex items-center gap-2 bg-indigo-500 hover:bg-indigo-600 text-white font-semibold px-4 py-2 rounded shadow transition-all">
        <PlusIcon class="w-5 h-5" /> Adaugă produs
      </button>
      <button @click="edit" class="flex items-center gap-2 bg-emerald-500 hover:bg-emerald-600 text-white font-semibold px-4 py-2 rounded shadow transition-all">
        <PencilIcon class="w-5 h-5" /> Modifică produs
      </button>
      <button @click="admin" class="flex items-center gap-2 bg-yellow-500 hover:bg-yellow-600 text-white font-semibold px-4 py-2 rounded shadow transition-all">
        <UserPlusIcon class="w-5 h-5" /> Adaugă admin
      </button>
      <button @click="orders" class="flex items-center gap-2 bg-rose-500 hover:bg-rose-600 text-white font-semibold px-4 py-2 rounded shadow transition-all">
        <ClipboardDocumentListIcon class="w-5 h-5" /> Vezi comenzi
      </button>
      <button @click="importProducts" class="flex items-center gap-2 bg-teal-400 hover:bg-teal-500 text-white font-semibold px-4 py-2 rounded shadow transition-all">
        <ArrowUpOnSquareIcon class="w-5 h-5" /> Import Produse
      </button>
      <button @click="promo" class="flex items-center gap-2 bg-yellow-700  hover:bg-yellow-800 text-white font-semibold px-4 py-2 rounded shadow transition-all">
        <StarIcon class="w-5 h-5" />  Promo Products
      </button>
    </div>

  
    <div class="max-w-5xl mx-auto p-6">
      <div v-if="activeComponent" class="bg-white shadow-lg rounded-xl p-6">
        <component :is="activeComponent"></component>
      </div>
    </div>
  </div>
</template>
