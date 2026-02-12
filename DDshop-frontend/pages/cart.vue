<script setup>
import { onMounted } from 'vue'
import { useAuth } from '~/composables/useAuth'
import { useCart } from '~/composables/useCart'
import CartProduct from '~/components/CartProduct.vue'
import CartSummary from '~/components/CartSummary.vue'
import Navbar from '~/components/navbar.vue'

const { user } = useAuth()
const { cart } = useCart()

async function fetchCart() {
  try {
    const id_user = user.value.id
    const response = await fetch(`http://localhost:8081/cart/${id_user}`)
    const data = await response.json()
    console.log("Cart data from backend:", data)
    cart.value = data
  } catch (error) {
    console.error('Eroare la fetch produs:', error)
  }
}

async function removeEntry(entry) {
  const id_user = user.value.id
  const id_product = entry.product.id
  try {
    await fetch(`http://localhost:8081/cart/${id_user}/delete-product/${id_product}`, {
      method: 'DELETE'
    })
    await fetchCart()
  } catch (error) {
    console.error('Eroare la ștergere produs:', error)
  }
}

async function updateCart() {
  const id_user = user.value.id
  const updatedQuantities = {}

  cart.value.cartEntries.forEach(entry => {
    updatedQuantities[entry.product.id] = entry.quantity
  })

  try {
    const res = await fetch(`http://localhost:8081/cart/${id_user}/update-cart`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(updatedQuantities),
    })

    if (!res.ok) throw new Error("Eroare la actualizare coș")
    await fetchCart()
  } catch (error) {
    console.error("Eroare la update cart:", error)
  }
}

onMounted(fetchCart)
</script>

<template>
  <Navbar></Navbar>
  <div class="max-w-4xl mx-auto p-6">
    <h2 class="text-3xl font-bold mb-8 text-center text-indigo-700">🛒 Coșul tău de cumpărături</h2>

    <div v-if="cart.cartEntries.length > 0">
      <CartProduct v-for="entry in cart.cartEntries" :key="entry.id" :entry="entry" @remove="removeEntry"
      />

      <CartSummary :total="cart.totalPrice" @save="updateCart" />
    </div>

    <div v-else class="text-center text-gray-500 text-lg mt-20">
      <p>Coșul este gol. 🛍️</p>
    </div>
  </div>
</template>