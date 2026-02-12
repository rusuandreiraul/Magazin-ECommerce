<script setup>
import { ref, onMounted } from 'vue'
import { useAuth } from '~/composables/useAuth'

import {
  DocumentTextIcon,
  CalendarIcon,
  CreditCardIcon,
  TruckIcon,
  CurrencyDollarIcon
} from '@heroicons/vue/24/outline'

const { user } = useAuth()
const orders = ref([])


async function getOrders() {
  const id_user = user.value.id
  try {
    const response = await fetch(`http://localhost:8081/order/see-orders/${id_user}`, {
      method: "GET"
    })
    if (!response.ok) {
      console.error("eroare la preluarea comenzilor");
      return
    }
    const data = await response.json()
    orders.value = data
  } catch (error) {
    console.error(error)
  }
}

onMounted(getOrders)
</script>

<template>


  <div class="max-w-5xl mx-auto px-4 py-8 bg-gray-50 min-h-screen">
    <h2 class="text-3xl font-bold mb-6 text-center text-blue-600 flex justify-center items-center gap-2">
      <DocumentTextIcon class="w-8 h-8 text-blue-600" />
      Istoric Comenzi
    </h2>

    <div v-if="orders.length > 0" class="space-y-6">
      <div v-for="order in orders" :key="order.id" class="bg-white rounded-xl shadow-lg p-6 border border-gray-200">
    
        <div class="flex justify-between items-start mb-4">
          <div>
            <h3 class="text-xl font-semibold text-gray-800 flex items-center gap-2">
              <DocumentTextIcon class="w-6 h-6 text-blue-500" />
              Comandă #{{ order.id }}
            </h3>
            <p class="text-sm text-gray-600 mt-1 flex items-center gap-1">
              <CalendarIcon class="w-4 h-4 text-gray-500" />
              {{ order.orderDate }}
            </p>
            <p class="text-sm text-gray-600 flex items-center gap-1">
              <CreditCardIcon class="w-4 h-4 text-gray-500" />
              {{ order.paymentType }}
            </p>
          </div>

          <div class="text-right text-sm text-gray-700">
            <p class="font-semibold text-blue-600 flex items-center gap-1">
              <TruckIcon class="w-5 h-5" />
              Livrare
            </p>
            <p>{{ order.deliveryAddress.street }}, {{ order.deliveryAddress.city }}</p>
            <p>{{ order.deliveryAddress.county }}, {{ order.deliveryAddress.country }}</p>
            <p>Cod poștal: {{ order.deliveryAddress.postalCode }}</p>
          </div>
        </div>

        <div class="space-y-3 border-t border-gray-100 pt-4">
          <div v-for="entry in order.cart.cartEntries" :key="entry.id" class="flex justify-between items-center bg-gray-50 rounded-md px-3 py-2">
            <div>
              <p class="font-medium text-gray-800 flex items-center gap-1">
                {{ entry.product.name }}
              </p>
              <p class="text-sm text-gray-600">
                {{ entry.quantity }} x {{ entry.pricePerPiece }} RON
              </p>
            </div>
            <p class="text-sm font-semibold text-gray-800">
              {{ entry.totalPricePerEntry }} RON
            </p>
          </div>
        </div>
        <div class="mt-4 text-right">
          <p class="text-lg font-bold text-green-700 flex items-center justify-end gap-1">
            <CurrencyDollarIcon class="w-6 h-6" />
            Total: {{ order.totalPrice }} RON
          </p>
        </div>
      </div>
    </div>

    <div v-else class="text-center text-gray-500 mt-10 text-lg">
      Nu ai nicio comandă înregistrată momentan.
    </div>
  </div>
</template>
