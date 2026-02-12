<script setup>
import { onMounted, ref, reactive, computed } from 'vue'
import sidebar from '~/components/sidebar.vue'
import BannerSlider from '~/components/BannerSlider.vue'
import ProductList from '~/components/ProductList.vue'
import WelcomeUser from '~/components/WelcomeUser.vue'
import ShowMoreButton from '~/components/ShowMoreButton.vue'

import { useAuth } from '~/composables/useAuth'
import { useRouter } from 'vue-router'

const toateProdusele = ref([])
const produseNoi = ref([])
const produsePopulare = ref([])
const produsePromovate=ref([])

const { user } = useAuth()
const router = useRouter()

const idCat = ref(null)
const filter = ref(null)
const allAttributes = ref([])
const selectedAttribute = ref([])

const likedProducts = ref([])
const clickedProducts = ref([])
const successMessages = reactive({})

const showMorePopular = ref(false)


const currentPage = ref(1)
const pageSize = 8

// Calculeaza numarul total de pagini
const totalPages = computed(() => Math.ceil(toateProdusele.value.length / pageSize))

// Produsele afisate in pagina curenta
const paginatedProducts = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return toateProdusele.value.slice(start, start + pageSize)
})

function goToPreviousPage() {
  if (currentPage.value > 1) {
    currentPage.value--
  }
}

function goToNextPage() {
  if (currentPage.value < totalPages.value) {
    currentPage.value++
  }
}

function goToPage(page) {
  currentPage.value = page
}


const filtersAreActive = computed(() =>
  idCat.value || filter.value || selectedAttribute.value.length > 0
)

const displayedPopularProducts = computed(() =>
  showMorePopular.value ? produsePopulare.value : produsePopulare.value.slice(0, 4)
)

async function addToCart(idProd) {
  if (!user.value) {
    alert("Nu poți adăuga produse în coș dacă nu ești logat")
    router.push("/user/login")
    return
  }

  try {
    const response = await fetch(`http://localhost:8081/cart/${user.value.id}/add-product-cart`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ quantity: 1, productId: idProd }),
    })

    if (!response.ok) throw new Error("Produsul nu a putut fi adăugat în coș")

    clickedProducts.value.push(idProd)
    successMessages[idProd] = "Produs adăugat în coș!"
    setTimeout(() => {
      clickedProducts.value = clickedProducts.value.filter(id => id !== idProd)
      delete successMessages[idProd]
    }, 1500)
  } catch (error) {
    console.error(error)
  }
}

async function fetchNewestProducts() {
  try {
    const response = await fetch('http://localhost:8081/products/new')
    produseNoi.value = await response.json()
    console.log("produse noi", produsePopulare);
  } catch (error) {
    console.error(error)
  }
}

async function fetchPopularProducts() {
  try {
    const response = await fetch('http://localhost:8081/products/popular')
    produsePopulare.value = await response.json()
    console.log("produse populare", produsePopulare);
  } catch (error) {
    console.error(error)
  }
}

async function fetchPromoProducts() {
  try {
    const response = await fetch('http://localhost:8081/promo')
    produsePromovate.value = await response.json()
  } catch (error) {
    console.error(error)
  }
}

async function fetchProducts() {
  let url = 'http://localhost:8081/products?'
  if (idCat.value) url += `id_cat=${idCat.value}`
  if (filter.value) url += `&filter=${filter.value}`
  if (selectedAttribute.value.length) {
    const attributesJson = encodeURIComponent(JSON.stringify(selectedAttribute.value))
    url += `&attr=${attributesJson}`
  }

  try {
    const response = await fetch(url)
    toateProdusele.value = await response.json()
    console.log('toateProdusele:', toateProdusele.value)
    allAttributes.value = toateProdusele.value.flatMap(p => p.attributes)
  } catch (error) {
    console.error(error)
  }
}

function handleCategory(id) {
  idCat.value = id
  fetchProducts()
}

function handleFilter(filterParam) {
  filter.value = filterParam
  fetchProducts()
}

function handleAttribute(attr) {
  selectedAttribute.value = attr
  fetchProducts()
}

function resetFilters() {
  currentPage.value=1
  idCat.value = null
  filter.value = null
  selectedAttribute.value = []
  fetchProducts()
}

async function toggleLike(productId) {
  if (!user.value || !user.value.id) {
    alert('Trebuie să fii logat pentru a adăuga produsul la favorite!')
    return
  }

  try {
    const isLiked = likedProducts.value.includes(productId)
    const method = isLiked ? 'DELETE' : 'POST'
    const response = await fetch(`http://localhost:8081/favorites/${user.value.id}/${productId}`, { method })

    if (response.ok) {
      likedProducts.value = isLiked ? likedProducts.value.filter(id => id !== productId) : [...likedProducts.value, productId]
    }
  } catch (error) {
    console.error(error)
  }
}

function toggleShowMore() {
  showMorePopular.value = !showMorePopular.value
}

onMounted(() => {
  fetchNewestProducts()
  fetchProducts()
  fetchPopularProducts()
  fetchPromoProducts()
})
</script>

<template>
  <NuxtLayout>
    <div class="w-full bg-white shadow-md rounded-lg p-6 mb-8 text-center">
      <h1 class="text-5xl font-extrabold text-green-700 mb-6">DDshop</h1>
      <BannerSlider />
    </div>

    <div class="flex min-h-screen bg-gray-50">
      <Sidebar
        :attributes="allAttributes"
        @attributeSelected="handleAttribute"
        @categorySelected="handleCategory"
        @filterSelected="handleFilter"
        @resetFilters="resetFilters"
        class="w-1/5 min-w-[220px] max-w-[280px] border-r border-gray-200 bg-white"
      />

      <div class="flex-1 p-8 relative overflow-hidden pb-[120px]">
        <template v-if="!filtersAreActive">
          
          <WelcomeUser v-if="user" :first-name="user.firstName" />
          <ProductList
            title="Produse promovate"
            :products="produsePromovate"
            :likedProducts="likedProducts"
            :clickedProducts="clickedProducts"
            :successMessages="successMessages"
            @addToCart="addToCart"
            @toggleLike="toggleLike"
          />

          <h1 class="text-3xl font-bold text-center mt-6 mb-6">Produse populare</h1>
          <ProductList
            :products="displayedPopularProducts"
            :likedProducts="likedProducts"
            :clickedProducts="clickedProducts"
            :successMessages="successMessages"
            @addToCart="addToCart"
            @toggleLike="toggleLike"
          />

          <div class="text-center mt-4">
            <ShowMoreButton :expanded="showMorePopular" @toggle="toggleShowMore" />
          </div>
        </template>

        <h2 class="font-bold m-7 text-center text-3xl">Toate produsele</h2>
        <ProductList
          :products="paginatedProducts"
          :likedProducts="likedProducts"
          :clickedProducts="clickedProducts"
          :successMessages="successMessages"
          @addToCart="addToCart"
          @toggleLike="toggleLike"
        />
        <div class="flex items-center justify-between border-t border-gray-200 bg-white px-4 py-3 sm:px-6 mt-6">
      <div class="flex flex-1 justify-between sm:hidden">
        <button @click="goToPreviousPage" :disabled="currentPage === 1" class="relative inline-flex items-center rounded-md border border-gray-300 bg-white px-4 py-2 text-sm font-medium text-gray-700 hover:bg-gray-50">
          Previous
        </button>
        <button @click="goToNextPage" :disabled="currentPage === totalPages" class="relative ml-3 inline-flex items-center rounded-md border border-gray-300 bg-white px-4 py-2 text-sm font-medium text-gray-700 hover:bg-gray-50">
          Next
        </button>
      </div>
      <div class="hidden sm:flex sm:flex-1 sm:items-center sm:justify-between">
        <div>
          <p class="text-sm text-gray-700">
            Showing
            <span class="font-medium">{{ (currentPage - 1) * pageSize + 1 }}</span>
            to
            <span class="font-medium">
              {{
                currentPage * pageSize > toateProdusele.length
                  ? toateProdusele.length
                  : currentPage * pageSize
              }}
            </span>
            of
            <span class="font-medium">{{ toateProdusele.length }}</span>
            products
          </p>
        </div>
        <div>
          <nav class="isolate inline-flex -space-x-px rounded-md shadow-xs" aria-label="Pagination">
            <button  @click="goToPreviousPage"  :disabled="currentPage === 1" class="relative inline-flex items-center rounded-l-md px-2 py-2 text-gray-400 ring-1 ring-gray-300 ring-inset hover:bg-gray-50" >
              <span class="sr-only">Previous</span>
              <svg class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd" d="M11.78 5.22a.75.75 0 0 1 0 1.06L8.06 10l3.72 3.72a.75.75 0 1 1-1.06 1.06l-4.25-4.25a.75.75 0 0 1 0-1.06l4.25-4.25a.75.75 0 0 1 1.06 0Z" clip-rule="evenodd"/>
              </svg>
            </button>
            <button v-for="page in totalPages" :key="page" @click="goToPage(page)":class="[
                'relative inline-flex items-center px-4 py-2 text-sm font-semibold ring-1 ring-gray-300 ring-inset hover:bg-blue-700',
                currentPage === page ? 'z-10 bg-blue-700 text-white' : 'text-gray-900',
              ]"
            >
              {{ page }}
            </button>
            <button @click="goToNextPage" :disabled="currentPage === totalPages" class="relative inline-flex items-center rounded-r-md px-2 py-2 text-gray-400 ring-1 ring-gray-300 ring-inset hover:bg-gray-50" >
              <span class="sr-only">Next</span>
              <svg class="h-5 w-5" viewBox="0 0 20 20" fill="currentColor">
                <path fill-rule="evenodd"  d="M8.22 5.22a.75.75 0 0 1 1.06 0l4.25 4.25a.75.75 0 0 1 0 1.06l-4.25 4.25a.75.75 0 0 1-1.06-1.06L11.94 10 8.22 6.28a.75.75 0 0 1 0-1.06Z" clip-rule="evenodd"/>
              </svg>
            </button>
          </nav>
        </div>
      </div>
    </div>
      </div>
    </div>
  </NuxtLayout>
</template>
