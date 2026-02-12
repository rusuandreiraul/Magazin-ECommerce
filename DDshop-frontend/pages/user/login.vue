<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuth } from '~/composables/useAuth'

const email = ref('')
const password = ref('')
const errorMessage = ref('')
const router = useRouter()
const { login } = useAuth()

const{ user}=useAuth() //folsoesc user din useAuth pentru a vedea daca e logat sau nu

async function handleLogin() {
    console.log('Se încearcă login')
    errorMessage.value = '' 
  try {
    const loggedUser=await login(email.value, password.value)
    console.log("user logat",loggedUser)

    if(loggedUser.rol==='CLIENT'){
      router.push('/')
    }
  else{
    router.push('/admin/dashboard')
  }
   
  } catch (error) {
    errorMessage.value = 'Email sau parola incorectă'
  }
}
</script>

<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-50">
    <form @submit.prevent="handleLogin" class="bg-white p-8 rounded shadow-md w-full max-w-sm">
      <h1 class="text-2xl font-bold mb-6 text-center">Login</h1>

      <div class="mb-4">
        <label for="email" class="block mb-1 font-medium text-gray-700">Email</label>
        <input v-model="email" id="email" type="email" placeholder="Email" required class="w-full px-3 py-2 border rounded border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-400"/>
      </div>
      <div class="mb-4">
        <label for="password" class="block mb-1 font-medium text-gray-700">Parola</label>
        <input v-model="password" id="password" type="password" placeholder="Parola" required class="w-full px-3 py-2 border rounded border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-400"/>
      </div>

      <p v-if="errorMessage" class="text-red-600 mb-4 text-center">{{ errorMessage }}</p>

      <button type="submit" class="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700 transition">
        Conectează-te
      </button>

      <div class="mt-4 flex justify-between text-sm text-gray-600">
        <NuxtLink to="/user/forgot-password" class="hover:underline">Ai uitat parola?</NuxtLink>
        <NuxtLink to="/user/registration" class="hover:underline">Înregistrează-te</NuxtLink>
      </div>
    </form>
  </div>
</template>
