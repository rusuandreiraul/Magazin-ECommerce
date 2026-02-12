<script setup>
import { ref, onMounted } from 'vue'
import { useAuth } from '~/composables/useAuth'

const props = defineProps({
  productId: Number //aici primit propsul de la parinte
})

const { user } = useAuth()
const rating = ref(0)
const comment = ref('')
const ratings = ref([])
const message=ref('')

async function fetchRatings() {
  try {
    const response = await fetch(`http://localhost:8081/ratings/product/${props.productId}`)
    ratings.value = await response.json()
  } catch (err) {
    console.error("Eroare la fetch ratinguri:", err)
  }
}

async function submitRating() {
    if (!rating.value || rating.value < 1 || rating.value > 5 || !comment.value.trim()) {
        alert("Completează toate câmpurile corect. Rating-ul trebuie să fie între 1 și 5.");
        return;
    }

  const payload = {
    valoare: rating.value,
    comentariu: comment.value,
    userId: user.value.id,
    productId: props.productId
  }

  const response = await fetch(`http://localhost:8081/ratings`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(payload)
  })

  const mesajBackend=await response.text()
  console.log(mesajBackend);

  if (response.ok) {
    message.value=mesajBackend;
    comment.value = ''
    rating.value = 0
    fetchRatings()
  } 
  else {
    alert("A apărut o eroare la trimiterea recenziei.")
  }
}

onMounted(fetchRatings)
</script>



<template>
    <div class="mt-12 space-y-6">
      <h2 class="text-2xl font-bold text-blue-800">Recenzii și ratinguri</h2>
  
      <div v-if="user" class="bg-white p-6 rounded-lg shadow-md">
        <h3 class="text-lg font-semibold mb-2">Lasă o recenzie</h3>
  
        <div class="flex items-center space-x-1 mb-3">
          <span
            v-for="star in 5"
            :key="star"
            @click="rating = star"
            class="cursor-pointer text-2xl">
            <span :class="star <= rating ? 'text-yellow-500' : 'text-gray-300'">★</span>
          </span>
        </div>
  
        <textarea
          v-model="comment"
          rows="3"
          placeholder="Scrie un comentariu..."
          class="w-full border border-gray-300 rounded p-2 mb-3"
        ></textarea>
  
        <button @click="submitRating" class="btn bg-blue-600 hover:bg-blue-700 text-white">
          Trimite
        </button>

        <p v-if="message" class="text-green-600 mt-2">{{ message }}</p>

      </div>
  
      <div class="space-y-4">
        <div v-if="ratings.length === 0" class="text-gray-500">Nu există recenzii pentru acest produs.</div>
        <div
          v-for="recenzie in ratings"
          :key="recenzie.id"
          class="border border-gray-200 rounded p-4 bg-gray-50"
        >
          <div class="flex items-center mb-1">
            <span class="font-semibold mr-2">{{ recenzie.userName }}</span>
            <span class="text-yellow-500 text-sm">
              {{ '★'.repeat(recenzie.valoare) }}<span class="text-gray-300">{{ '★'.repeat(5 - recenzie.valoare) }}</span>
            </span>
          </div>
          <p class="text-sm text-gray-400">{{ recenzie.user.firstName }}</p>
          <p class="text-gray-700 text-sm">{{ recenzie.comentariu }}</p>
        </div>
      </div>
    </div>
  </template>
  
