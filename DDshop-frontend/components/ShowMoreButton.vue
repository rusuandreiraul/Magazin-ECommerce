<script setup>
const props = defineProps({ expanded: Boolean })
</script>

  <template>
    <button @click="$emit('toggle')" class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 transition">
      {{ expanded ? 'Afișează mai puține' : 'Afișează mai multe' }}
    </button>
  </template>
  
