<script setup>
const props = defineProps({
  total: Number
})
const emit = defineEmits(['save'])
</script>

<template>
  <div class="mt-8 text-right">
    <h3 class="text-2xl font-bold text-indigo-800 mb-4">Total de plată: {{ props.total.toFixed(2) }} RON</h3>
    <div class="flex justify-end gap-4">
      <NuxtLink to="/checkout">
        <button class="bg-green-600 hover:bg-green-700 text-white px-6 py-2 rounded shadow transition-all">
          ✅ Plasează comanda
        </button>
      </NuxtLink>
      <button @click="$emit('save')" class="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded shadow transition-all">
        💾 Salvează modificările
      </button>
    </div>
  </div>
</template>
