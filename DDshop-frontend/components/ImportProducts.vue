
<script setup>
import { ref } from 'vue'

const selectedFile = ref(null)
const isUploading = ref(false)
const message = ref('')

function handleFileChange(event) {
  selectedFile.value = event.target.files[0]
}

function handleDrop(event) {  //functia gestioneaza fisierul 
  const files = event.dataTransfer.files //de aici se primeste fisierul practic si se salveaza in selectedFile
  if (files.length > 0) {
    selectedFile.value = files[0]
  }
}
async function uploadFile() {
  if (!selectedFile.value) return

  isUploading.value = true
  message.value = ''

  const formData = new FormData()
  formData.append('file', selectedFile.value)

  try {
    const response = await fetch('http://localhost:8081/admin/import-csv', {
      method: 'POST',
      body: formData,
    })

    if (!response.ok) throw new Error('Eroare la import')

    const result = await response.json()
    message.value = `Import reușit: ${result.success} produse. Eșecuri: ${result.failed}.`
  } catch (err) {
    message.value = 'Eroare la trimiterea fișierului!'
    console.error(err)
  } finally {
    isUploading.value = false
  }
}
</script>

<template>
    <div class="max-w-xl mx-auto mt-12 p-6 border rounded-lg shadow-lg bg-white">
      <h1 class="text-2xl font-bold mb-4">Import produse CSV</h1>
  
      <div class="border-dashed border-2 border-gray-400 p-6 text-center rounded-md bg-gray-50 mb-4"  @dragover.prevent @drop.prevent="handleDrop">
        <p class="mb-2">Trage fișierul aici sau selectează-l manual</p>
        <input type="file" accept=".csv" @change="handleFileChange" class="block mx-auto" />
      </div>
  
      <p v-if="selectedFile" class="text-sm text-gray-700 mb-4">Fișier selectat: {{ selectedFile.name }}</p>
  
      <button @click="uploadFile" :disabled="!selectedFile || isUploading" class="px-6 py-2 bg-green-600 text-white rounded hover:bg-green-700 disabled:opacity-50" >
        {{ isUploading ? 'Se încarcă...' : 'Trimite fișierul' }}
      </button>
  
      <p v-if="message" class="mt-4 text-green-700 font-semibold">{{ message }}</p>
    </div>
  </template>
  
  
  <style scoped>
  input[type="file"] {
    display: block;
  }
  </style>
  