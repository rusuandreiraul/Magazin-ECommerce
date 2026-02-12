<script setup>
const props = defineProps({
  entry: Object
})
const emit = defineEmits(['remove'])
</script>

<template>
  <div class="bg-white shadow-lg rounded-xl p-4 mb-6 flex items-center gap-6 border border-gray-200">
    <img :src="props.entry.product.images[0]?.url || 'https://via.placeholder.com/100'" alt="Product" class="w-24 h-24 rounded-lg object-contain border"/>

    <div class="flex-1">
      <p class="text-xl font-semibold text-gray-800">{{ props.entry.product.name }}</p>
      <p class="text-gray-600">Preț: <span class="font-medium">{{ props.entry.pricePerPiece }} RON</span></p>
      <div class="mt-2 flex items-center gap-2">
        <label for="quantity" class="text-sm text-gray-700">Cantitate:</label>
        <input type="number" v-model="props.entry.quantity" class="w-16 border border-gray-300 rounded px-2 py-1" min="1"/>
      </div>
      <p class="mt-2 text-gray-800 font-medium">Total: {{ props.entry.totalPricePerEntry }} RON</p>
    </div>
    <button @click="$emit('remove', props.entry)" class="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded transition-all shadow">
      Șterge
    </button>
  </div>
</template>
