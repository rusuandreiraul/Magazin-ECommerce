<script setup>

import {ref} from 'vue'

const router = useRouter()

const sameAddress=ref(true)
const billing=ref({})

const firstName = ref('')
const lastName = ref('')
const email = ref('')
const password = ref('')
const phoneNumber = ref('')
const errorMessage = ref('')

const city = ref('')
const postalCode = ref('')
const street = ref('')
const county = ref('')
const country = ref('')


const bstreet = ref('')
const bpostalCode = ref('')
const bcity = ref('')
const bcounty = ref('')
const bcountry = ref('')

async function handleRegistration(){
   try{ 
    if(sameAddress.value===true){
        billing.value={
            billingcity:city.value,
            billingpostalcode:postalCode.value,
            billingcounty:county.value,
            billingcountry:country.value,
            billingstreet:street.value
        }

    }
    else{
        billing.value = {
            billingcity: bcity.value,
            billingpostalcode: bpostalCode.value,
            billingcounty: bcounty.value,
            billingcountry: bcountry.value,
            billingstreet: bstreet.value
            }
    }

    const userData = {
      firstName: firstName.value,
      lastName: lastName.value,
      email: email.value,
      password: password.value,
      phoneNumber: phoneNumber.value,
      defaultDeliveryAddress: {
        street: street.value,
        postalCode: postalCode.value,
        city: city.value,
        county: county.value,
        country: country.value,
      },
      defaultBillingAddress: sameAddress.value ? {
        street: street.value,
        postalCode: postalCode.value,
        city: city.value,
        county: county.value,
        country: country.value,
      } : 
      {
        street: bstreet.value,
        postalCode: bpostalCode.value,
        city: bcity.value,
        county: bcounty.value,
        country: bcountry.value,
      }
    }

    const response=await fetch('http://localhost:8081/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(userData),
    })

    if(!response.ok){
        throw new Error("A aparut o eroare de inregistrare")
    }

    const result=await response.text()
    console.log("raspuns inregistrare: ", result)

    router.push("/")


   }
   catch (error) {
    console.error(error)
    errorMessage.value = error.message || 'A apărut o eroare neașteptată'
  }



}



</script>

<template>
    <div class="min-h-screen flex items-center justify-center bg-gray-50">
      <form @submit.prevent="handleRegistration" class="bg-white p-8 rounded shadow-md w-full max-w-md space-y-6">
        <h1 class="text-2xl font-bold text-center">Înregistrare</h1>
  
        
        <fieldset class="border p-4 rounded-md">
          <legend class="text-lg font-semibold text-gray-700">Informații personale</legend>
  
          <div class="grid grid-cols-1 gap-4 mt-4">
            <div>
              <label for="firstName" class="block mb-1 text-sm font-medium">Prenume</label>
              <input v-model="firstName" id="firstName" type="text" required class="input" />
            </div>
            <div>
              <label for="lastName" class="block mb-1 text-sm font-medium">Nume</label>
              <input v-model="lastName" id="lastName" type="text" required class="input" />
            </div>
            <div>
              <label for="email" class="block mb-1 text-sm font-medium">Email</label>
              <input v-model="email" id="email" type="email" required class="input" />
            </div>
            <div>
              <label for="password" class="block mb-1 text-sm font-medium">Parolă</label>
              <input v-model="password" id="password" type="password" required class="input" />
            </div>
            <div>
              <label for="phoneNumber" class="block mb-1 text-sm font-medium">Telefon</label>
              <input v-model="phoneNumber" id="phoneNumber" type="text" required class="input" />
            </div>
          </div>
        </fieldset>
  
    
        <fieldset class="border p-4 rounded-md">
          <legend class="text-lg font-semibold text-gray-700">Adresă de livrare</legend>
  
          <div class="grid grid-cols-1 gap-4 mt-4">
            <div>
              <label for="street" class="block mb-1 text-sm font-medium">Stradă</label>
              <input v-model="street" id="street" type="text"  class="input" />
            </div>
            <div>
              <label for="postalCode" class="block mb-1 text-sm font-medium">Cod poștal</label>
              <input v-model="postalCode" id="postalCode" type="number" class="input" />
            </div>
            <div>
              <label for="city" class="block mb-1 text-sm font-medium">Oraș</label>
              <input v-model="city" id="city" type="text"  class="input" />
            </div>
            <div>
              <label for="county" class="block mb-1 text-sm font-medium">Județ</label>
              <input v-model="county" id="county" type="text"  class="input" />
            </div>
            <div>
              <label for="country" class="block mb-1 text-sm font-medium">Țară</label>
              <input v-model="country" id="country" type="text"  class="input" />
            </div>
          </div>
        </fieldset>
  
       
        <div class="flex items-center">
          <input type="checkbox" v-model="sameAddress" id="sameAddress" class="form-checkbox h-5 w-5 text-blue-600" />
          <label for="sameAddress" class="ml-2 text-sm text-gray-700">Adresa de facturare este aceeași</label>
        </div>
  
    
        <fieldset v-if="!sameAddress" class="border p-4 rounded-md">
          <legend class="text-lg font-semibold text-gray-700">Adresă de facturare</legend>
  
          <div class="grid grid-cols-1 gap-4 mt-4">
            <div>
              <label for="bstreet" class="block mb-1 text-sm font-medium">Stradă</label>
              <input v-model="bstreet" id="bstreet" type="text"  class="input" />
            </div>
            <div>
              <label for="bpostalCode" class="block mb-1 text-sm font-medium">Cod poștal</label>
              <input v-model="bpostalCode" id="bpostalCode" type="number"  class="input" />
            </div>
            <div>
              <label for="bcity" class="block mb-1 text-sm font-medium">Oraș</label>
              <input v-model="bcity" id="bcity" type="text"  class="input" />
            </div>
            <div>
              <label for="bcounty" class="block mb-1 text-sm font-medium">Județ</label>
              <input v-model="bcounty" id="bcounty" type="text"  class="input" />
            </div>
            <div>
              <label for="bcountry" class="block mb-1 text-sm font-medium">Țară</label>
              <input v-model="bcountry" id="bcountry" type="text"  class="input" />
            </div>
          </div>
        </fieldset>
  
      
        <p v-if="errorMessage" class="text-red-600 text-center text-sm">{{ errorMessage }}</p>
  

        <button type="submit" class="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700 transition">
          Înregistrează-te
        </button>
  
  
        <div class="mt-4 flex justify-between text-sm text-gray-600">
          <NuxtLink to="/forgot-password" class="hover:underline">Ai uitat parola?</NuxtLink>
          <NuxtLink to="/login" class="hover:underline">Login</NuxtLink>
        </div>
      </form>
    </div>
  </template>
  