<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router' 
import { UserIcon, ClipboardDocumentListIcon, MapPinIcon, CreditCardIcon, ArrowLeftIcon } from '@heroicons/vue/24/outline'


const route = useRoute() //sa iau id din ruta
const order = ref(null)

async function fetchOrder(id) {
  try {
    const response = await fetch(`http://localhost:8081/order/${id}`)
    order.value = await response.json()
    console.log("order", order.value)
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => {
  const id = route.params.id
  fetchOrder(id)
})
</script>

<template>
    <div class="m-3">
      <NuxtLink class="inline-flex items-center gap-2 px-4 py-2 rounded-lg bg-green-600 text-white hover:bg-green-700 transition font-medium shadow" to="/admin/dashboard"> 
        <ArrowLeftIcon class="w-5 h-5" />
        Înapoi la comenzi
        </NuxtLink>
    </div>
  
    <div v-if="order" class="max-w-5xl mx-auto p-4">
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <!-- Detalii comandă -->
        <div class="p-6 shadow-xl border border-gray-200 rounded-2xl space-y-4 bg-white">
          <h2 class="text-xl font-bold flex items-center gap-2">
            <ClipboardDocumentListIcon class="w-6 h-6 text-green-600" />
            Comanda #{{ order.id }}
          </h2>
          <p class="text-sm text-gray-600">Plasată la: {{ new Date(order.orderDate).toLocaleString() }}</p>
          <p class="text-sm">
            Status:
            <span class="font-semibold text-green-600">Procesată</span>
          </p>
  
          <div>
            <h3 class="font-semibold mb-2">Produse comandate:</h3>
            <ul class="space-y-2">
              <li v-for="entry in order.cart.cartEntries" :key="entry.id" class="flex items-center space-x-4" >
                <img :src="entry.product.images[0]?.url" alt="product" class="w-12 h-16 object-cover rounded shadow"/>
                <div>
                  <p class="font-medium">{{ entry.product.name }}</p>
                  <p class="text-sm text-gray-600">Cantitate: {{ entry.quantity }}</p>
                  <p class="text-sm text-gray-600">Preț: {{ entry.totalPricePerEntry }} RON</p>
                </div>
              </li>
            </ul>
          </div>
  
          <div>
            <h3 class="font-semibold mt-4 flex items-center gap-2">
              <CreditCardIcon class="w-5 h-5 text-green-600" />
              Metodă de plată:
            </h3>
            <p>{{ order.paymentType === 'CASH' ? 'Cash' : order.paymentType }}</p>
          </div>
  
          <div class="text-green-700 font-bold text-lg border-t pt-4">
            Total: {{ order.totalPrice }} RON
          </div>
        </div>
  
        <!-- Detalii client -->
        <div class="p-6 shadow-xl border border-gray-200 rounded-2xl space-y-4 bg-white">
          <h2 class="text-xl font-bold flex items-center gap-2">
            <UserIcon class="w-6 h-6 text-green-600" />
            Client
          </h2>
          <p class="font-semibold">{{ order.user.firstName }} {{ order.user.lastName }}</p>
          <p class="text-sm text-gray-600">{{ order.user.email }}</p>
          <p class="text-sm text-gray-600">{{ order.user.phoneNumber }}</p>
  
          <div>
            <h3 class="font-semibold mt-4 flex items-center gap-2">
              <MapPinIcon class="w-5 h-5 text-green-600" />
              Adresă de livrare:
            </h3>
            <p>
              {{ order.deliveryAddress.street }}, {{ order.deliveryAddress.city }},
              {{ order.deliveryAddress.county }}, {{ order.deliveryAddress.country }},
              {{ order.deliveryAddress.postalCode }}
            </p>
          </div>
  
          <div>
            <h3 class="font-semibold mt-4 flex items-center gap-2">
              <MapPinIcon class="w-5 h-5 text-green-600" />
              Adresă de facturare:
            </h3>
            <p>
              {{ order.invoiceAddress.street }}, {{ order.invoiceAddress.city }},
              {{ order.invoiceAddress.county }}, {{ order.invoiceAddress.country }},
              {{ order.invoiceAddress.postalCode }}
            </p>
          </div>
        </div>
      </div>
    </div>
  
    <div v-else class="text-center text-gray-500 p-6">
      Se încarcă detaliile comenzii...
    </div>
  </template>
  