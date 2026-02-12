<script setup>

import {ref} from 'vue'

const email=ref('')
const message=ref('')
const messageColor=ref('')
async function handleForgot(){
   try{
    const response=await fetch('http://localhost:8081/forgot-password', {
        method:'POST',
        headers:{
            'Content-type':'application/json'
        },
        body: JSON.stringify({email:email.value})
    })

    const data=await response.text()
    if(response.ok){
        message.value=data
        messageColor.value="green"
    }
    else{
        message.value=data;
        messageColor.value="red"
    }
    
}
catch (error) {
    message.value = 'A apărut o eroare. Încearcă din nou.'
    messageColor.value = 'red'
}


}

</script>




<template>

<div class="min-h-screen flex items-center justify-center bg-gray-50">
    <form @submit.prevent="handleForgot" class="bg-white p-8 rounded shadow-md w-full max-w-sm">
      <h1 class="text-2xl font-bold mb-6 text-center">Autentificare</h1>

      <div class="mb-4">
        <label for="email" class="block mb-1 font-medium text-gray-700">Email</label>
        <input v-model="email" id="email" type="email" placeholder="Email" required class="w-full px-3 py-2 border rounded border-gray-300 focus:outline-none focus:ring-2 focus:ring-blue-400"/>
      </div>
      <button type="submit" class="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700 transition">
        Trimite link
      </button>
      <p v-if="message" :style="{ color: messageColor }" class="mt-4">{{ message }}</p>
    </form>
    
</div>



</template>