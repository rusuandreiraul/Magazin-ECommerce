<script setup>

import {ref} from 'vue'

const email=ref('')
const password1=ref('')
const password2=ref('')



const message=ref('')
async function handleReset(){
    const request={
    email:email.value,
    password1:password1.value,
    password2:password2.value
}
    try{
    const response=await fetch('http://localhost:8081/reset-password',{
        method:'POST',
        headers:{
            'Content-type':'application/json'
        },
        body: JSON.stringify(request)
    })

    message.value=await response.text()
}
catch(error){
    message.value=error
}
}

</script>


<template>

<div class="min-h-screen flex items-center justify-center bg-gray-50">
    <form @submit.prevent="handleReset" class="bg-white p-8 rounded shadow-md w-full max-w-sm">
      <h1 class="text-2xl font-bold mb-6 text-center">Autentificare</h1>

      <div class="mb-4">
        <label for="email" class="block mb-1 font-medium text-gray-700">Email</label>
        <input v-model="email" id="email" type="email" placeholder="Email" required class="w-full px-3 py-2 border rounded border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-400"/>
      </div>
      <div class="mb-4">
        <label for="password1" class="block mb-1 font-medium text-gray-700">Noua parola</label>
        <input v-model="password1" id="password1" type="password" placeholder="Parola" required class="w-full px-3 py-2 border rounded border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-400"/>
      </div>
      <div class="mb-4">
        <label for="password2" class="block mb-1 font-medium text-gray-700">Confirmare parola</label>
        <input v-model="password2" id="password2" type="password" placeholder="Confirmare parola" required class="w-full px-3 py-2 border rounded border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-400"/>
      </div>
      <button type="submit" class="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700 transition">
        Reseteaza parola
      </button>
      <p v-if="message" style="color: blue" class="mt-4">{{ message }}</p>
    </form>
    
</div>

</template>