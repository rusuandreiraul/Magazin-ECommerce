<script setup>
import { ref, onMounted } from 'vue'
import { StarIcon } from '@heroicons/vue/24/solid' 
import { CheckCircleIcon, ExclamationCircleIcon } from '@heroicons/vue/24/outline'

const produse = ref(null)
const message = ref('')
const produseSelectate = ref([])
const produsSelectat = ref('')
const discountPrice = ref(null)
const discountMessage = ref('')

async function getProducts() {
  try {
    const response = await fetch('http://localhost:8081/products')
    if (response.ok) {
      produse.value = await response.json()
    } else {
      message.value = '❌ Produsele nu au fost preluate corect din baza de date!'
    }
  } catch (error) {
    console.error(error)
  }
}

async function handlePromo() {
  const response = await fetch('http://localhost:8081/promo-products', {
    method: 'POST',
    headers: { 'Content-type': 'application/json' },
    body: JSON.stringify(produseSelectate.value)
  })

  message.value = response.ok 
    ? '✅ Produsele sunt promovate acum pe prima pagină!' 
    : '❌ Eroare! Ceva nu a funcționat'
}

async function handleSetDiscount() {
  const produsData = {
    id: produsSelectat.value,
    newPrice: discountPrice.value
  }

  const response = await fetch('http://localhost:8081/discount-product', {
    method: 'POST',
    headers: { 'Content-type': 'application/json' },
    body: JSON.stringify(produsData)
  })

  discountMessage.value = response.ok 
    ? '✅ Produsul are un nou preț!' 
    : '❌ Eroare! Ceva nu a funcționat'
}

function toggleProductSelection(id) {
  const index = produseSelectate.value.indexOf(id)
  if (index > -1) {
    produseSelectate.value.splice(index, 1)
  } else if (produseSelectate.value.length < 8) {
    produseSelectate.value.push(id)
  }
}

function isDisabled(id) {
  return produseSelectate.value.length >= 8 && !produseSelectate.value.includes(id)
}

onMounted(getProducts)
</script>


<template>
  <div v-if="produse" class="flex flex-col justify-center items-center gap-6 p-6">
    <h2 class="text-2xl font-bold text-gray-800">Selectează Produse pentru Promo</h2>


    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 w-full max-w-4xl">
      <div
        v-for="produs in produse"
        :key="produs.id"
        class="border rounded-xl p-4 shadow hover:shadow-lg transition-all flex justify-between items-center"
      >
        <span class="font-medium text-gray-700">{{ produs.name }}</span>
        <input
          type="checkbox"
          :checked="produseSelectate.includes(produs.id)"
          @change="toggleProductSelection(produs.id)"
          :disabled="isDisabled(produs.id)"
          class="w-5 h-5 accent-yellow-600"
        />
      </div>
    </div>

    <button 
      @click="handlePromo" 
      class="flex items-center gap-2 bg-yellow-700 hover:bg-yellow-800 text-white font-semibold px-6 py-3 rounded-xl shadow-lg transition-all">
      <StarIcon class="w-6 h-6" /> Trimite Produse Promovate
    </button>

    <p v-if="message" class="flex items-center gap-2 text-lg mt-4"
       :class="message.includes('✅') ? 'text-green-600' : 'text-red-600'">
      <CheckCircleIcon v-if="message.includes('✅')" class="w-6 h-6" />
      <ExclamationCircleIcon v-else class="w-6 h-6" />
      {{ message }}
    </p>
  </div>

  <div v-else class="text-center py-12">
    <h1 class="text-xl font-semibold text-gray-600">Nu există niciun produs în baza de date</h1>
  </div>
</template>
