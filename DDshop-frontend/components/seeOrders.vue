<script setup>
import { ref, onMounted, watch} from 'vue'


const orders=ref([])

const isDisabled=ref({})
const categories=ref([])
const selectedCategory=ref('')
const searchQuery=ref('')
const selectedDate=ref('')
const orderSize=ref(1)


async function fetchOrders(){
let url = 'http://localhost:8081/orders?'
  if (selectedCategory.value && selectedCategory.value !== 'toate') {
    url += `cat=${selectedCategory.value}`
  }
  if (searchQuery.value) {
    url += `&searchQuery=${searchQuery.value}`
  }
  if(selectedDate.value){
    console.log("data", selectedDate.value)
    url+=`&selectedDate=${selectedDate.value}`
  }

  try{
    const response=await fetch(url,{
     method:'GET'
    })

    const data=await response.json();
    console.log("data primite:", data)

    orders.value=data;
    orderSize.value=data.length

  }catch(error){
    console.error(error);
  }
}

const itemsPerPage = 3;
const currentPage = ref(1);

const totalPages = computed(() => Math.ceil(orders.value.length / itemsPerPage));

function goToPreviousPage() {
  if (currentPage.value > 1) currentPage.value--;
}

function goToNextPage() {
  if (currentPage.value < totalPages.value) currentPage.value++;
}

async function getCategories(){
  const response=await fetch('http://localhost:8081/categories', {
    method: 'GET',
})
  categories.value=await response.json()
}




const displayedOrder = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  return orders.value.slice(start, start + itemsPerPage);
});

function markAsDone(orderId){
  isDisabled.value[orderId]=true;
}



onMounted(() => {
  fetchOrders();
  getCategories();
});



</script>

<template>

<div class="flex flex-col gap-4 p-4 m-4 rounded border max-w-3xl mx-auto bg-white:bg-white-900 shadow">

    <h1 class="font-bold text-2xl text-center text-rose-500">Filtrare</h1>


<input type="text" v-model="searchQuery" @change="fetchOrders" placeholder="Caută comenzi..." class="px-4 py-2 border rounded w-full"/>
<input type="date" v-model="selectedDate" @change="fetchOrders" placeholder="Data comanda" class="px-4 py-2 border rounded w-full"/>
<select v-model="selectedCategory" @change="fetchOrders" class="px-4 py-2 border rounded w-full">
  <option disabled value="">Filtrare după categorie</option>
  <option value="toate">Toate</option>
  <option v-for="cat in categories" :key="cat.name" :value="cat.name">
    {{ cat.name }}
  </option>
</select>
  </div>

<div v-for="o in displayedOrder"  :key="o.id"  class="flex flex-row justify-between items-start border p-4 rounded-md shadow-sm mb-4 bg-white">
  <div class="flex flex-col">
    <h1 class="font-bold text-lg text-gray-800 mb-2">Comanda #{{ o.id }}</h1>
    <p class="text-gray-700"><span class="font-semibold">Client:</span> {{ o.user.firstName }}</p>
    <p class="text-gray-700">
      <span class="font-semibold">Adresa:</span>
      {{ o.deliveryAddress.street }}, {{ o.deliveryAddress.city }}
      {{ o.deliveryAddress.county }} {{ o.deliveryAddress.postalCode }}
    </p>
    <ul class="mt-3 list-disc list-inside text-sm text-gray-600">
      <li v-for="entry in o.cart.cartEntries" :key="entry.id" class="mb-1">
        <span class="font-semibold text-gray-800">{{ entry.product.name }}</span> -
        {{ entry.quantity }} x
        <span class="text-blue-600 font-medium">{{ entry.product.price }} RON</span>
      </li>
    </ul>
  </div>

  <div class="flex flex-col justify-between text-right h-full ml-6 w-44 max-w-[180px]">
  <p class="font-bold text-lg bg-gray-100 p-2 mb-5 rounded text-gray-800 shadow-sm">
    Total: <span class="text-green-600">{{ o.totalPrice }} RON</span>
  </p>

  <div class="flex flex-col gap-2 mt-auto">
    <NuxtLink :to="`/orders/${o.id}`">
    <button class="text-sm px-2 py-1 border border-blue-500 text-blue-600 rounded hover:bg-blue-50 whitespace-nowrap">
      Detalii
    </button>
  </NuxtLink>
    <button @click="markAsDone(o.id)" :disabled="isDisabled[o.id]" class="text-sm px-2 py-1 border border-green-500 text-green-600 rounded transition  hover:bg-green-50 disabled:opacity-50 disabled:shadow-inner disabled:cursor-not-allowed whitespace-nowrap">
      Marchează ca finalizată
    </button>
  </div>
</div>

</div>




 <div class="flex items-center justify-between border-t border-gray-200 bg-white px-4 py-3 sm:px-6">
  <div class="flex flex-1 justify-between sm:hidden">
    <a href="#" class="relative inline-flex items-center rounded-md border border-gray-300 bg-white px-4 py-2 text-sm font-medium text-gray-700 hover:bg-gray-50">Previous</a>
    <a href="#" class="relative ml-3 inline-flex items-center rounded-md border border-gray-300 bg-white px-4 py-2 text-sm font-medium text-gray-700 hover:bg-gray-50">Next</a>
  </div>
  <div class="hidden sm:flex sm:flex-1 sm:items-center sm:justify-between">
    <div>
      <p class="text-sm text-gray-700">
        Showing
        <span class="font-medium">{{ currentPage }}</span>
        to
        <span class="font-medium">3</span>
        of
        <span class="font-medium">{{ orderSize }}</span>
      </p>
    </div>
    <div>
      <nav class="isolate inline-flex -space-x-px rounded-md shadow-xs" aria-label="Pagination">
        <button @click="goToPreviousPage" :disabled="currentPage === 1" class="relative inline-flex items-center rounded-l-md px-2 py-2 text-gray-400 ring-1 ring-gray-300 ring-inset hover:bg-gray-50">
         <span class="sr-only">Previous</span>
          <svg class="size-5" viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M11.78 5.22a.75.75 0 0 1 0 1.06L8.06 10l3.72 3.72a.75.75 0 1 1-1.06 1.06l-4.25-4.25a.75.75 0 0 1 0-1.06l4.25-4.25a.75.75 0 0 1 1.06 0Z" clip-rule="evenodd" />
          </svg>
        </button>
        <button v-for="page in totalPages"  :key="page" @click="currentPage = page" :class="['relative inline-flex items-center px-4 py-2 text-sm font-semibold ring-1 ring-gray-300 ring-inset hover:bg-rose-500',currentPage === page? 'z-10 bg-rose-500 text-white': 'text-gray-900']">
          {{ page }}
        </button> 
        <button @click="goToNextPage" :disabled="currentPage === totalPages"  class="relative inline-flex items-center rounded-r-md px-2 py-2 text-gray-400 ring-1 ring-gray-300 ring-inset hover:bg-gray-50">
          <span class="sr-only">Next</span>
          <svg class="size-5" viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M8.22 5.22a.75.75 0 0 1 1.06 0l4.25 4.25a.75.75 0 0 1 0 1.06l-4.25 4.25a.75.75 0 0 1-1.06-1.06L11.94 10 8.22 6.28a.75.75 0 0 1 0-1.06Z" clip-rule="evenodd" />
          </svg>
        </button>
      </nav>
    </div>
  </div>
</div>
  </template>
  
  