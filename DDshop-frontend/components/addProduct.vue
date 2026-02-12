<script setup>
import { ref } from 'vue'

const name = ref('')
const price = ref('')
const quantity = ref('')
const category = ref('')
const description = ref('')

const message = ref('')
const images = ref([])

// attributes este un array de obiecte {key, value}
const attributes = ref([])

function addAttribute() {
  attributes.value.push({ name: '', value: '' })
}

function removeAttribute(index) {
  attributes.value.splice(index, 1)
}

async function handleAdd() {
  const product = {
    name: name.value,
    price: price.value,
    availableQuantity: quantity.value,
    category: category.value,
    description: description.value,
    attributes: attributes.value.filter(attr => attr.name && attr.value), // trimitem doar atribute valide
    imagesUrl: images.value.map(i => i.url)
  }

  try {
    const response = await fetch('http://localhost:8081/add-product', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(product)
    })
    console.log("produs", product)
    message.value = response.ok ? 'Produs introdus cu success!' : 'Produsul nu a fost introdus. Verifică baza de date — poate există deja.'
  } catch (err) {
    console.error(err)
    message.value = 'Eroare la trimitere.'
  }
}
</script>

<template>
  <div class="flex justify-center items-center min-h-screen bg-white">
    <div class="bg-white p-8 rounded-2xl shadow-lg w-full max-w-xl space-y-6">
      <h2 class="text-3xl font-bold text-center text-indigo-500">Adaugă produs</h2>

      <form @submit.prevent="handleAdd" class="space-y-6">
        <div class="space-y-4">
          <label for="name" class="font-semibold text-indigo-500">Nume Produs</label>
          <input v-model="name"  type="text" placeholder="Nume produs" class="input w-full p-2 border rounded-md" />
          <label for="price" class="font-semibold text-indigo-500">Pret</label>
          <input v-model="price" type="number" step="0.1" placeholder="Preț" class="input w-full p-2 border rounded-md" />
          <label for="quantity" class="font-semibold text-indigo-500">Cantitate</label>
          <input v-model="quantity" type="number" placeholder="Cantitate" class="input w-full p-2 border rounded-md" />
          <label for="category" class="font-semibold text-indigo-500">Categorie</label>
          <input v-model="category" type="text" placeholder="Categorie" class="input w-full p-2 border rounded-md" />
          <label for="description" class="font-semibold text-indigo-500">Descriere</label>
          <textarea v-model="description" placeholder="Descriere" class="input w-full p-2 border rounded-md h-24"></textarea>
        </div>

        <div>
          <h3 class="font-semibold mb-2 text-indigo-500">Atribute produs</h3>
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
          <h3 class="font-semibold mb-2 text-indigo-500">Imagini produs</h3>
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

        <button type="submit" class=" bg-indigo-500 hover:bg-indigo-600 text-white font-semibold px-4 py-2 rounded shadow">Salvează</button>
      </form>

      <p v-if="message" class="text-center text-blue-600 p-3 bg-gray-200 rounded">{{ message }}</p>
    </div>
  </div>
</template>
