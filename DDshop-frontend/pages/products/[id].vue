<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import Navbar from '~/components/navbar.vue'
import Footer from '~/components/footer.vue'
import RatingSection from '~/components/RatingSection.vue'
import { useAuth } from '~/composables/useAuth'
import { ShoppingCartIcon, EyeIcon, ChevronDownIcon, ChevronUpIcon } from '@heroicons/vue/24/outline'

const produs = ref({})
const produseSimilare = ref([])
const route = useRoute()
const router = useRouter()
const quantityProduct = ref(1)
const { user } = useAuth()
const id = route.params.id
const successMessage = ref('')
const showAttributes = ref(false)
const image = ref('')

function toggleAttributes() {
  showAttributes.value = !showAttributes.value
}

async function addToCart() {
  try {
    if (!user.value) {
      alert("Nu poți adăuga produse în coș dacă nu ești logat")
      router.push("/user/login")
      return
    }

    const cartEntry = {
      quantity: quantityProduct.value,
      productId: produs.value.id
    }

    const response = await fetch(`http://localhost:8081/cart/${user.value.id}/add-product-cart`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(cartEntry),
    })

    if (!response.ok) {
      throw new Error("Produsul nu a putut fi adăugat în coș")
    }

    successMessage.value = "Produs adăugat în coș"
    setTimeout(() => successMessage.value = '', 3000)

  } catch (error) {
    console.log(error)
  }
}

onMounted(async () => {
  try {
    const response = await fetch(`http://localhost:8081/products/${id}`)
    const data = await response.json()
    produs.value = data
    image.value = produs.value.images?.[0]?.url || ''

    const id_cat = produs.value.category?.id
    if (id_cat) {
      const responseSimilar = await fetch(`http://localhost:8081/products/category/${id_cat}`)
      const dataSimilare = await responseSimilar.json()
      produseSimilare.value = dataSimilare.filter(p => p.id !== produs.value.id)
    }
  } catch (error) {
    console.error('Eroare la fetch produs:', error)
  }
})
</script>

<template>
  <Navbar />
  <div class="max-w-7xl mx-auto p-6 pb-24">
    <div class="grid grid-cols-1 md:grid-cols-2 gap-10 items-start">
      <div class="card bg-white shadow-2xl rounded-xl overflow-hidden border border-gray-100">
        <figure class="bg-gray-100 p-6 flex items-center justify-center h-[400px]">
          <img :src="image" alt="Produs" class="object-contain max-h-full w-full" />
        </figure>
      </div>
      <div class="space-y-6">
        <h1 class="text-4xl font-extrabold text-blue-900 flex items-center gap-2">
          <EyeIcon class="w-6 h-6 text-blue-500" /> {{ produs.name }}
        </h1>
        <p class="text-gray-700 leading-relaxed text-lg border-l-4 border-blue-500 pl-4 italic">{{ produs.description }}</p>

        <button @click="toggleAttributes" class="flex items-center gap-2 btn btn-outline btn-sm text-gray-700 hover:text-blue-700 border border-gray-300 hover:border-blue-400">
          <component :is="showAttributes ? ChevronUpIcon : ChevronDownIcon" class="w-4 h-4" />
          {{ showAttributes ? 'Ascunde atribute' : 'Vezi atribute' }}
        </button>

        <ul v-if="showAttributes" class="max-h-48 overflow-y-auto border border-gray-200 rounded-lg p-4 bg-blue-50 text-blue-900 space-y-2 shadow-inner">
          <li v-for="(value, key) in produs.attributes" :key="key" class="flex justify-between border-b pb-1">
            <span class="font-semibold">{{ value.name }}</span>
            <span>{{ value.value }}</span>
          </li>
        </ul>

        <p class="inline-block bg-green-100 text-green-700 px-4 py-2 rounded-full text-2xl font-bold shadow-sm">
          {{ produs.price }} RON
        </p>

        <div class="flex flex-col gap-4">
          <label class="label font-semibold text-gray-700">Cantitate:</label>
          <input type="number" v-model="quantityProduct" min="1" class="input rounded border w-24 border-gray-600" />
          <button @click="addToCart" class="btn bg-blue-600 text-white hover:bg-blue-700 flex items-center gap-2 w-fit">
            <ShoppingCartIcon class="w-5 h-5" />
            Adaugă în coș
          </button>
          <p v-if="successMessage" class="text-green-600 font-medium">{{ successMessage }}</p>
        </div>
      </div>
    </div>

    <div v-if="produs.id" class="mt-16">
    <RatingSection :productId="produs.id" />
    </div>

    <div v-if="produseSimilare.length" class="mt-16">
      <h2 class="text-3xl font-bold mb-6 text-blue-800 flex items-center gap-2">
        <EyeIcon class="w-6 h-6 text-blue-500" /> Produse similare
      </h2>
      <div class="flex space-x-6 pb-4 overflow-x-auto scroll-smooth">
        <div v-for="prod in produseSimilare" :key="prod.id" class="card w-56 bg-white shadow-md rounded-lg hover:shadow-2xl hover:scale-[1.03] transition-transform duration-300 ease-in-out flex-shrink-0">
          <figure class="h-40 bg-gray-100 rounded-t-lg flex items-center justify-center overflow-hidden">
            <img :src="prod.images?.[0]?.url || ''" alt="Produs similar" class="object-contain h-full w-full" />
          </figure>
          <div class="p-4">
            <h3 class="font-semibold text-lg truncate mb-1 text-gray-800">{{ prod.name }}</h3>
            <p class="text-green-600 font-bold mb-2">{{ prod.price }} RON</p>
            <NuxtLink :to="`/products/${prod.id}`" class="text-blue-600 hover:underline text-sm font-medium flex items-center gap-1">
              <EyeIcon class="w-4 h-4" />
              Vezi produs
            </NuxtLink>
          </div>
        </div>
      </div>
    </div>
  </div>
  <Footer />
</template>
