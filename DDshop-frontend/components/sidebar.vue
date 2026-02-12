<script setup>
import { ref, onMounted, watch } from "vue";
const categories = ref([]);
const selectedCategory = ref(null);
const selectedFilter = ref(null);
const selectedAttributes = ref([]);
const selectedPagesRange = ref(null);
const emit = defineEmits([
  "categorySelected",
  "filterSelected",
  "attributeSelected",
  "resetFiltre",
]);

const groupedAttributes = ref({});

const props = defineProps({
  attributes: {
    type: Array,
  },
});

function groupAttributes(attrs) {
  let result = {};

  for (let attr of attrs) {
    if (attr.name === "Pagini") continue;

    let key = attr.name;
    if (!(key in result)) {
      result[key] = [];
    }

    const alreadyExists = result[key].some(
      (existing) => existing.value === attr.value
    );
    if (!alreadyExists) {
      result[key].push(attr);
    }
  }

  return result;
}

watch(
  () => props.attributes,
  (newAttrs) => {
    groupedAttributes.value = groupAttributes(newAttrs);
  },
  { immediate: true }
);

async function getCategories() {
  try {
    const response = await fetch("http://localhost:8081/categories");
    if (!response.ok) throw new Error("Eroare la încărcarea categoriilor");
    categories.value = await response.json();
  } catch (error) {
    console.log(error);
  }
}

onMounted(() => {
  getCategories();
});

function selectCategory(id) {
  selectedCategory.value = id;
  emit("categorySelected", id);
}

function selectFilter(filter) {
  selectedFilter.value = filter;
  emit("filterSelected", filter);
}

function selectPagesRange(range) {
  selectedPagesRange.value = range;
  emit("attributeSelected", [
    ...selectedAttributes.value,
    { name: "Pagini", value: range },
  ]);
}

function toggleAttribute(attr) {
  const isSelected = selectedAttributes.value.some(
    (a) => a.name === attr.name && a.value === attr.value
  );

  if (isSelected) {
    selectedAttributes.value = selectedAttributes.value.filter(
      (a) => !(a.name === attr.name && a.value === attr.value)
    );
  } else {
    selectedAttributes.value = [...selectedAttributes.value, attr];
  }

  emit("attributeSelected", [...selectedAttributes.value]);
}

function resetFiltre() {
  selectedCategory.value = null;
  selectedFilter.value = null;
  selectedAttributes.value = [];
  selectedPagesRange.value = null;

  emit("attributeSelected", []);
  emit("categorySelected", null);
  emit("filterSelected", null);
}
defineExpose({ resetFiltre }); //permite trimitea de filtre de la copil catre parinte(index)
</script>

<template>
  <aside
    class="h-full w-full bg-white shadow rounded-md border p-4 overflow-y-auto"
  >
    <div>
      <h2 class="text-lg font-semibold text-gray-800 mb-3">Categorii</h2>
      <button
        @click="resetFiltre"
        class="bg-gray-300 text-black px-3 py-1 rounded mt-2 hover:bg-gray-400"
      >
        Resetează filtrele
      </button>
      <ul class="space-y-2">
        <li v-for="cat in categories" :key="cat.id">
          <label
            class="flex items-center cursor-pointer px-2 py-2 rounded hover:bg-gray-100 transition"
          >
            <input
              type="radio"
              name="category"
              :value="cat.id"
              @change="selectCategory(cat.id)"
              :checked="selectedCategory === cat.id"
              class="form-radio text-green-600 h-4 w-4"
            />
            <span class="ml-3 text-gray-700">{{ cat.name }}</span>
          </label>
        </li>
      </ul>

      <h2 class="text-lg font-semibold text-gray-800 mt-6 mb-3">
        Sortare după preț
      </h2>
      <div class="space-y-2">
        <label
          class="flex items-center cursor-pointer px-2 py-2 rounded hover:bg-gray-100 transition"
        >
          <input
            type="radio"
            name="filter"
            value="ascending"
            @change="selectFilter('ascending')"
            :checked="selectedFilter === 'ascending'"
            class="form-radio text-green-600 h-4 w-4"
          />
          <span class="ml-3 text-gray-700">Crescător</span>
        </label>
        <label
          class="flex items-center cursor-pointer px-2 py-2 rounded hover:bg-gray-100 transition"
        >
          <input
            type="radio"
            name="filter"
            value="descending"
            @change="selectFilter('descending')"
            :checked="selectedFilter === 'descending'"
            class="form-radio text-green-600 h-4 w-4"
          />
          <span class="ml-3 text-gray-700">Descrescător</span>
        </label>
      </div>

      <h2 class="text-lg font-semibold text-gray-800 mt-6 mb-3">
        Filtrare după pagini
      </h2>
      <div class="space-y-2">
        <label
          class="flex items-center cursor-pointer px-2 py-2 rounded hover:bg-gray-100 transition"
        >
          <input
            type="radio"
            name="pages"
            value="<100"
            @change="selectPagesRange('<100')"
            :checked="selectedPagesRange === '<100'"
            class="form-radio text-green-600 h-4 w-4"
          />
          <span class="ml-3 text-gray-700">&lt; 100 pagini</span>
        </label>
        <label
          class="flex items-center cursor-pointer px-2 py-2 rounded hover:bg-gray-100 transition"
        >
          <input
            type="radio"
            name="pages"
            value="100-300"
            @change="selectPagesRange('100-300')"
            :checked="selectedPagesRange === '100-300'"
            class="form-radio text-green-600 h-4 w-4"
          />
          <span class="ml-3 text-gray-700">100 - 300 pagini</span>
        </label>
        <label
          class="flex items-center cursor-pointer px-2 py-2 rounded hover:bg-gray-100 transition"
        >
          <input
            type="radio"
            name="pages"
            value="300-500"
            @change="selectPagesRange('300-500')"
            :checked="selectedPagesRange === '300-500'"
            class="form-radio text-green-600 h-4 w-4"
          />
          <span class="ml-3 text-gray-700">300 - 500 pagini</span>
        </label>
        <label
          class="flex items-center cursor-pointer px-2 py-2 rounded hover:bg-gray-100 transition"
        >
          <input
            type="radio"
            name="pages"
            value="500+"
            @change="selectPagesRange('500+')"
            :checked="selectedPagesRange === '500+'"
            class="form-radio text-green-600 h-4 w-4"
          />
          <span class="ml-3 text-gray-700">500+ pagini</span>
        </label>
      </div>

      <h2 class="text-lg font-semibold text-gray-800 mt-6 mb-3">
        Filtrare după atribute
      </h2>
      <div
        v-for="(val, attribute) in groupedAttributes"
        :key="attribute"
        class="mb-4"
      >
        <h3 class="text-sm font-medium text-gray-700 mb-1">{{ attribute }}</h3>
        <div v-for="attr in val" :key="attr.id" class="ml-2">
          <label
            class="flex items-center cursor-pointer px-2 py-1 rounded hover:bg-gray-100 transition"
          >
            <input
              type="checkbox"
              :value="attr"
              :checked="
                selectedAttributes.some(
                  (a) => a.name === attr.name && a.value === attr.value
                )
              "
              @change="() => toggleAttribute(attr)"
              class="form-checkbox text-green-600 h-4 w-4"
            />
            <span class="ml-2 text-gray-700">{{ attr.value }}</span>
          </label>
        </div>
      </div>
    </div>
  </aside>
</template>
