<script setup>
import {ref, onMounted} from 'vue'

const products=ref([])
const selectedProduct=ref({})

const images = ref([])
// attributes este un array de obiecte {key, value}
const attributes = ref([])
const message=ref('')


const itemsPerPage = 10;
const currentPage = ref(1);


const totalPages = computed(() => Math.ceil(products.value.length / itemsPerPage));

function goToPreviousPage() {
  if (currentPage.value > 1) currentPage.value--;
}

function goToNextPage() {
  if (currentPage.value < totalPages.value) currentPage.value++;
}

const paginatedProducts = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  return products.value.slice(start, start + itemsPerPage);
});

function addAttribute() {
  attributes.value.push({ name: '', value: '' })
}

function removeAttribute(index) {
  attributes.value.splice(index, 1)
}

async function fetchProducts(){
    try{
        const response=await fetch('http://localhost:8081/products',{
            method:'GET'
        })

        const data=await response.json();

        products.value=data;
    }
    catch(error){
        console.log(error)
    }
}

async function saveChanges(){
  const data={
    availableQuantity:selectedProduct.value.availableQuantity,
    price:selectedProduct.value.price,
    discountPrice: selectedProduct.value.discountPrice || null,
    description:selectedProduct.value.description,
    category: selectedProduct.value.category?.name ?? selectedProduct.value.category,
    name:selectedProduct.value.name,
    attributes: attributes.value.filter(attr => attr.name && attr.value),
    imagesUrl: images.value.map(i => i.url)
  }
  
  try{
    const response=await fetch('http://localhost:8081/edit-product', {
      method:'PUT',
      headers:{
        'Content-type':'application/json'
      },
      body: JSON.stringify(data)
    })

    console.log("raspuns", data)
    if(response.ok){
      message.value="Produsul a fost modificat cu success";
    }
    else{
      message.value="Produsul nu a fost modificat,v verifica daca datele sunt valide"
    }
  }
  catch(error){
    console.error(error)
  }
}

function selectProduct(product){
    selectedProduct.value=product
}


onMounted(fetchProducts)


</script>
<template>
    
    <div class="grid grid-cols-2 h-screen">
    <div class="p-4 m-2 shadow-3xl rounded">
      <h2 class="text-xl font-bold mb-4 text-emerald-700">Selectează un produs</h2>
  
      <ul class="space-y-2 mb-6">
        <li v-for="product in paginatedProducts" :key="product.id" class="cursor-pointer px-4 py-2 bg-white shadow rounded hover:bg-gray-100" @click="selectProduct(product)">
          {{ product.name }}
        </li>
      </ul>
      <div class="flex items-center justify-between border-t border-gray-200 bg-white px-4 py-3 sm:px-6">
  <div class="flex flex-1 justify-between sm:hidden">
    <a href="#" @click.prevent="goToPreviousPage" class="relative inline-flex items-center rounded-md border border-gray-300 bg-white px-4 py-2 text-sm font-medium text-gray-700 hover:bg-gray-50">Previous</a>
    <a href="#" @click.prevent="goToNextPage" class="relative ml-3 inline-flex items-center rounded-md border border-gray-300 bg-white px-4 py-2 text-sm font-medium text-gray-700 hover:bg-gray-50">Next</a>
  </div>
  <div class="hidden sm:flex sm:flex-1 sm:items-center sm:justify-between">
    <div>
      <p class="text-sm text-gray-700">
        Showing
        <span class="font-medium">{{ currentPage }}</span>
        to
        <span class="font-medium">10</span>
        of
        <span class="font-medium">{{ products.length }}</span>

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
          <button v-for="page in totalPages"  :key="page" @click="currentPage = page" :class="['relative inline-flex items-center px-4 py-2 text-sm font-semibold ring-1 ring-gray-300 ring-inset hover:bg-emerald-600',currentPage === page? 'z-10 bg-emerald-600 text-white': 'text-gray-900']">
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

    </div>
    <div v-if="selectedProduct" class="bg-white p-6 rounded shadow">
        <h3 class="text-lg font-semibold mb-4 text-emerald-700">Editează produsul</h3>
        <form @submit.prevent="saveChanges" class="space-y-4">
          <div>
            <label class="block mb-1  text-emerald-500">Nume produs</label>
            <p>{{ selectedProduct.name }}</p>
          </div>
          <div>
            <label class="block mb-1  text-emerald-500">Preț</label>
            <input v-model="selectedProduct.price" type="number" step="0.01" class="border px-3 py-2 rounded w-full"/>
          </div>
          <div>
            <label class="block mb-1  text-emerald-500">Preț Discount</label>
            <input v-model="selectedProduct.discountPrice" type="number" step="0.01" class="border px-3 py-2 rounded w-full"/>
          </div>
          <div>
            <label class="block mb-1  text-emerald-500">Cantitate</label>
            <input v-model="selectedProduct.availableQuantity" type="number" step="1" class="border px-3 py-2 rounded w-full"/>
          </div>
          <div>
            <label class="block mb-1  text-emerald-500">Descriere</label>
            <textarea v-model="selectedProduct.description" class="border px-3 py-2 rounded w-full"></textarea>
          </div>
          <div>
          <h3 class="font-semibold mb-2  text-emerald-500">Adauga atribute</h3>
          <table class="w-full text-center border-collapse">
            <thead>
              <tr>
                <th class="border px-2 py-1">Nume</th>
                <th class="border px-2 py-1">Valoare</th>
                <th class="border px-2 py-1">Acțiune</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(attr, index) in attributes" :key="index">
                <td class="border px-2 py-1">
                  <input v-model="attr.name" type="text" placeholder="Nume" class="w-full p-1 border rounded" />
                </td>
                <td class="border px-2 py-1">
                  <input v-model="attr.value" type="text" placeholder="Valoare" class="w-full p-1 border rounded" />
                </td>
                <td class="border px-2 py-1 text-center">
                  <button type="button" @click="removeAttribute(index)" class="text-red-600 hover:text-red-800 font-semibold">
                    Șterge
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
          <button type="button" @click="addAttribute" class="mt-2 px-3 py-1 bg-green-600 text-white rounded hover:bg-green-700">
            Adaugă atribut
          </button>
        </div>

        <div>
          <h3 class="font-semibold mb-2  text-emerald-500">Adauga imagini</h3>
          <table class="w-full text-center border-collapse">
            <thead>
              <tr>
                <th class="border px-2 py-1">URL Imagine</th>
                <th class="border px-2 py-1">Acțiune</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(img, index) in images" :key="index">
                <td class="border px-2 py-1">
                  <input v-model="img.url" type="text" placeholder="https://..." class="w-full p-1 border rounded" />
                </td>
                <td class="border px-2 py-1 text-center">
                  <button type="button" @click="images.splice(index, 1)" class="text-red-600 hover:text-red-800 font-semibold">
                    Șterge
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
          <button type="button" @click="images.push({ url: '' })" class="mt-2 px-3 py-1 bg-green-600 text-white rounded hover:bg-green-700">
            Adaugă imagine
          </button>
        </div>
        <p v-if="message" class="text-blue-500 font-bold">{{ message }}</p>
          <button type="submit" class="btn t">
            Salvează modificările
          </button>
        </form>
      </div>

  </div>
  </template>