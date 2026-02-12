<script setup>
import { ref, onMounted } from 'vue'
import { useAuth } from '~/composables/useAuth'
import { useCart } from '~/composables/useCart'
import { getAddressData } from '~/utils/getAddressData'
import CheckoutForm from '~/components/CheckoutForm.vue'
import OrderSummary from '~/components/OrderSummary.vue'

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
    const response = await fetch(`http://localhost:8081/cart/inactive-cart/${id}`, { method: 'PUT' })
    const data = await response.json()
    if (!response.ok) {
      errorMessage.value = "Eroare la ștergerea coșului"
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
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(orderData)
    })

    if (!response.ok) throw new Error('Eroare la plasarea comenzii.')

    successMessage.value = 'Comanda a fost plasată cu succes!'
    inactiveCart(cart.value.id)
    errorMessage.value = ''
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
  <div class="max-w-5xl mx-auto p-6">
    <h2 class="text-3xl font-bold mb-8 text-center text-blue-700">Finalizare comandă</h2>

    <div class="grid grid-cols-1 md:grid-cols-2 gap-10">
      <OrderSummary :cart="cart" />
      <CheckoutForm
        v-model:email="email"
        v-model:firstName="firstName"
        v-model:lastName="lastName"
        v-model:paymentTypeSelected="paymentTypeSelected"
        v-model:sameAddress="sameAddress"
        v-model:street="street"
        v-model:postalCode="postalCode"
        v-model:city="city"
        v-model:county="county"
        v-model:country="country"
        v-model:bstreet="bstreet"
        v-model:bpostalCode="bpostalCode"
        v-model:bcity="bcity"
        v-model:bcounty="bcounty"
        v-model:bcountry="bcountry"
        :errorMessage="errorMessage"
        :successMessage="successMessage"
        @submit="handleCheckout"
      />
    </div>
  </div>
</template>
