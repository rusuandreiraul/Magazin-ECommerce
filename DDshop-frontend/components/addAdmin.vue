<script setup>
import { ref } from 'vue'
import { ShieldCheckIcon } from '@heroicons/vue/24/outline'

const email = ref('')
const message = ref('')
const success = ref(false)

async function handleSubmit() {
  try {
    const response = await fetch('http://localhost:8081/update-admin', {
      method: 'PUT',
      headers: {
        'Content-type': 'application/json'
      },
      body: JSON.stringify({ email: email.value })
    })

    const data = await response.json()
    console.log("response user", data)

    if (response.ok) {
      message.value = "Admin adăugat cu succes"
      success.value = true
    } else {
      message.value = "Userul nu a putut fi adăugat ca admin"
      success.value = false
    }
  } catch (error) {
    console.error(error)
    message.value = "Eroare de rețea"
    success.value = false
  }
}
</script>

<template>
  <div class="min-h-screen flex items-center justify-center bg-yellow-100">
    <div class="w-full max-w-xs bg-white rounded-lg shadow-md p-4">
      <div class="flex items-center space-x-2 mb-3">
        <ShieldCheckIcon class="h-5 w-5 text-yellow-600" />
        <h2 class="text-lg font-semibold text-gray-800">Adaugă un admin</h2>
      </div>
      <p class="text-xs  text-yellow-500 mb-4">Introdu adresa de email a utilizatorului.</p>
      <form @submit.prevent="handleSubmit" class="space-y-3">
        <input v-model="email" type="email" placeholder="admin@example.com" class="input input-sm input-bordered w-full text-sm" required />
        <button type="submit" class="btn btn-sm btn-warning w-full">➕ Adaugă</button>
      </form>
      <p v-if="message" :class="success ? 'text-green-600' : 'text-red-600'" class="mt-2 text-center text-sm font-medium">
        {{ message }}
      </p>
    </div>
  </div>
</template>