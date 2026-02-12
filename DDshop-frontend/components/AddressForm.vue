<script setup>
defineProps(['prefix', 'address'])
defineEmits(['update:address'])
</script>

<template>
  <fieldset class="border p-4 rounded-md">
    <legend class="text-lg font-semibold text-gray-700">
      Adresă de {{ prefix === 'b' ? 'facturare' : 'livrare' }}
    </legend>

    <div class="grid grid-cols-1 gap-4 mt-4">
      <div v-for="field in ['street', 'postalCode', 'city', 'county', 'country']" :key="field">
        <label :for="prefix + field" class="block mb-1 text-sm font-medium">{{ field }}</label>
        <input :id="prefix + field" v-model="address[field]" :type="field === 'postalCode' ? 'number' : 'text'" class="input"/>
      </div>
    </div>
  </fieldset>
</template>
