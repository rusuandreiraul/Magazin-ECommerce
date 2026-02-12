<script setup>
import { ref, onMounted } from 'vue'
import { useAuth } from '~/composables/useAuth'
import { getAddressData } from '~/utils/getAddressData'
import { useCart } from '~/composables/useCart'

const { cart } = useCart()
const { user } = useAuth()

const email = ref('')
const firstName = ref('')
const lastName = ref('')
const paymentTypeSelected = ref('')
const sameAddress = ref(true)

const street = ref('')
const postalCode = ref('')
const city = ref('')
const county = ref('')
const country = ref('')

const bstreet = ref('')
const bpostalCode = ref('')
const bcity = ref('')
const bcounty = ref('')
const bcountry = ref('')

const errorMessage = ref('')
const successMessage = ref('')

async function inactiveCart(id) {
  try {
    const response = await fetch(`http://localhost:8081/cart/inactive-cart/${id}`, {
      method: 'PUT',
    })
    const data = await response.json()
    if (!response.ok) {
      errorMessage.value = 'Eroare la dezactivarea coșului'
    }
  } catch (error) {
    console.error(error)
  }
}

async function handleCheckout() {
  try {
    const { deliveryAddress, billingAddress } = getAddressData({
      sameAddress: sameAddress.value,
      street: street.value,
      postalCode: postalCode.value,
      city: city.value,
      county: county.value,
      country: country.value,
      bstreet: bstreet.value,
      bpostalCode: bpostalCode.value,
      bcity: bcity.value,
      bcounty: bcounty.value,
      bcountry: bcountry.value
    })

    const id_user = user.value.id
    const id_cart = cart.value.id

    const orderData = {
      userId: id_user,
      cartId: id_cart,
      paymentType: paymentTypeSelected.value,
      totalPrice: cart.value.totalPrice,
      deliveryAddress,
      invoiceAddress: billingAddress
    }

    const response = await fetch(`http://localhost:8081/order/${id_user}/add-order/${id_cart}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(orderData)
    })

    if (!response.ok) {
      throw new Error('Eroare la plasarea comenzii.')
    }

    successMessage.value = 'Comanda a fost plasată cu succes!'
    errorMessage.value = ''
    inactiveCart(id_cart)
  } catch (error) {
    console.error(error)
    errorMessage.value = 'A apărut o eroare la trimiterea comenzii.'
    successMessage.value = ''
  }
}

onMounted(() => {
  if (user.value) {
    street.value = user.value.defaultDeliveryAddress.street
    postalCode.value = user.value.defaultDeliveryAddress.postalCode
    city.value = user.value.defaultDeliveryAddress.city
    county.value = user.value.defaultDeliveryAddress.county
    country.value = user.value.defaultDeliveryAddress.country

    bstreet.value = user.value.defaultBillingAddress.street
    bpostalCode.value = user.value.defaultBillingAddress.postalCode
    bcity.value = user.value.defaultBillingAddress.city
    bcounty.value = user.value.defaultBillingAddress.county
    bcountry.value = user.value.defaultBillingAddress.country
  }
})
</script>

<template>
  <form @submit.prevent="handleCheckout" class="space-y-6 bg-white p-6 rounded-lg shadow">
    <h3 class="text-xl font-semibold mb-2">Date personale</h3>
    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
      <input v-model="email" type="email" placeholder="Email" class="input" />
      <input v-model="firstName" type="text" placeholder="Prenume" class="input" />
      <input v-model="lastName" type="text" placeholder="Nume" class="input" />
    </div>

    <div>
      <label class="block font-medium mb-2">Metodă de plată:</label>
      <label class="inline-flex items-center mr-4">
        <input type="radio" v-model="paymentTypeSelected" value="CASH" class="mr-1" /> Cash
      </label>
      <label class="inline-flex items-center">
        <input type="radio" v-model="paymentTypeSelected" value="CARD" class="mr-1" /> Card
      </label>
    </div>

    <div>
      <h3 class="text-lg font-semibold mb-2">Adresă de livrare</h3>
      <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <input v-model="street" type="text" placeholder="Stradă" class="input" />
        <input v-model="postalCode" type="text" placeholder="Cod poștal" class="input" />
        <input v-model="city" type="text" placeholder="Oraș" class="input" />
        <input v-model="county" type="text" placeholder="Județ" class="input" />
        <input v-model="country" type="text" placeholder="Țară" class="input" />
      </div>
    </div>

    <div>
      <label class="inline-flex items-center">
        <input type="checkbox" v-model="sameAddress" class="mr-2" /> Adresa de facturare este aceeași
      </label>
    </div>

    <div v-if="!sameAddress">
      <h3 class="text-lg font-semibold mb-2">Adresă de facturare</h3>
      <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <input v-model="bstreet" type="text" placeholder="Stradă" class="input" />
        <input v-model="bpostalCode" type="text" placeholder="Cod poștal" class="input" />
        <input v-model="bcity" type="text" placeholder="Oraș" class="input" />
        <input v-model="bcounty" type="text" placeholder="Județ" class="input" />
        <input v-model="bcountry" type="text" placeholder="Țară" class="input" />
      </div>
    </div>

    <p v-if="errorMessage" class="text-red-500">{{ errorMessage }}</p>
    <p v-if="successMessage" class="text-green-600">{{ successMessage }}</p>

    <button type="submit" class="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700">
      Plasează comanda
    </button>

    <div class="mt-4 text-sm text-center">
      <NuxtLink to="/" class="text-blue-500 hover:underline">Înapoi la homepage</NuxtLink>
    </div>
  </form>
</template>