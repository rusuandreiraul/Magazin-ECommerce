<script setup>
import { onMounted, ref } from 'vue'
import { useAuth } from '~/composables/useAuth'
import { HeartIcon as HeartSolid } from '@heroicons/vue/24/solid'

const { user } = useAuth()
const products=ref([])
const message=ref('');

async function getProductsFav(){
  const id_user=user.value.id
  try{
    const response=await fetch(`http://localhost:8081/favorites/${id_user}`,{
      method:'GET'
    })

    if (!response.ok) {
      message.value = "A apărut o eroare la încărcarea produselor favorite"
      return
    }


    const data=await response.json()
    console.log("produse favorite", data)
    products.value=data

    
  }
  catch(error){
    console.error(error)
  }
}




async function removeFavorite(id) {
    const id_user=user.value.id
    const id_prod=id
    try{
    const response=await fetch(`http://localhost:8081/favorites/${id_user}/${id_prod}`,{
      method:'DELETE'
    })

      if (response.ok) {
        message.value = "Produsul nu mai e la favorite"
        products.value = products.value.filter(p => p.id !== id)
      }
    
   
  }
  catch(error){
    console.error(error)
  }
}

onMounted(getProductsFav)

</script>

<template>
  <NuxtLayout>
    <div class="min-h-screen bg-gray-50 py-12 px-6">
      <h1 class="text-4xl font-bold text-center text-green-700 mb-10">Produse favorite</h1>

      <div v-if="products.length === 0" class="text-center text-gray-500 text-lg">
        Nu ai produse favorite momentan.
      </div>

      <div v-if="message" class="text-center text-gray-500 text-lg">
        {{ message }}
      </div>


      <div class="grid gap-6 grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4">
        <div v-for="p in products":key="p.id" class="relative bg-white shadow-md rounded-lg overflow-hidden hover:shadow-xl transition duration-300 flex flex-col">
          <img :src="p.images?.[0]?.url || '/placeholder.jpg'" alt="Imagine produs" class="w-full h-40 object-contain" />
          <button @click="removeFavorite(p.id)" class="absolute top-2 right-2 z-10 text-red-600 hover:scale-110 transition" title="Șterge din favorite">
            <HeartSolid class="h-6 w-6" />
          </button>

          <div class="p-4 flex flex-col justify-between flex-grow">
            <div>
              <h2 class="text-lg font-semibold mb-1">{{ p.name }}</h2>
              <p class="text-gray-600 text-sm line-clamp-2">{{ p.description }}</p>
            </div>
            <p class="text-green-600 font-bold mt-3">{{ p.price }} RON</p>
          </div>
        </div>
      </div>
    </div>
  </NuxtLayout>
</template>


