<script setup>
defineProps(['phone'])
defineEmits(['update:phone'])
</script>

<template>
  <fieldset class="border p-4 rounded-md">
    <legend class="text-lg font-semibold text-gray-700">Informații personale</legend>
    <div class="grid grid-cols-1 gap-4 mt-4">
      <div>
        <label for="phoneNumber" class="block mb-1 text-sm font-medium">Telefon</label>
        <input
          :value="phone"
          @input="emit('update:phone', $event.target.value)"
          id="phoneNumber"
          type="text"
          class="input"
        />
      </div>
    </div>
  </fieldset>
</template>
