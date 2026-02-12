<script setup>
import {
  ShoppingCartIcon,
  HeartIcon,
  PercentBadgeIcon,
} from "@heroicons/vue/24/outline";
import { HeartIcon as HeartSolid } from "@heroicons/vue/24/solid";

const props = defineProps({
  product: Object,
  liked: Boolean,
  clicked: Boolean,
  successMessage: String,
  onAddToCart: Function,
  onToggleLike: Function,
});
</script>

<template>
  <div class="relative flex flex-col overflow-hidden">
    <!-- buton favorite -->
    <button
      @click.stop="onToggleLike(product.id)"
      class="absolute top-2 right-2 z-10 text-red-600"
    >
      <component :is="liked ? HeartSolid : HeartIcon" class="h-6 w-6" />
    </button>

    <NuxtLink
      :to="`/products/${product.id}`"
      class="bg-white shadow-md rounded-lg overflow-hidden hover:shadow-xl transition duration-300 flex flex-col h-full min-h-[320px] relative"
    >
      <div class="relative">
        <img
          :src="product.images?.[0]?.url || '/placeholder.jpg'"
          alt="Imagine produs"
          class="w-full h-40 object-contain relative z-0"
        />

        <div
          v-if="
            product.discountPrice > 0 && product.discountPrice < product.price
          "
          class="absolute bottom-0 left-10 z-10"
        >
          <PercentBadgeIcon
            class="bg-blue-500 text-white rounded w-9 h-9 shadow-md"
          />
        </div>
      </div>

      <div class="p-4 flex flex-col justify-between flex-grow">
        <div>
          <h2 class="text-lg font-semibold mb-1">{{ product.name }}</h2>
          <p class="text-gray-600 text-sm line-clamp-2">
            {{ product.description }}
          </p>
        </div>

        <div class="flex justify-between items-center mt-3">
          <div
            v-if="
              product.discountPrice > 0 && product.discountPrice < product.price
            "
          >
            <p class="text-red-600 font-semibold text-sm line-through">
              {{ product.price }} RON
            </p>
            <p class="text-blue-600 font-bold">
              {{ product.discountPrice }} RON
            </p>
          </div>
          <div v-else>
            <p class="text-green-600 font-bold">{{ product.price }} RON</p>
          </div>

          <button
            @click.stop.prevent="onAddToCart(product.id)"
            :class="
              clicked
                ? 'bg-white text-black border border-gray-300'
                : 'bg-blue-500 hover:bg-blue-600 text-white'
            "
            class="h-7 w-7 rounded flex items-center justify-center transition-all duration-300"
          >
            <ShoppingCartIcon class="h-5 w-5" />
          </button>
        </div>

        <p v-if="successMessage" class="text-green-500 mt-2 text-center">
          {{ successMessage }}
        </p>
      </div>
    </NuxtLink>
  </div>
</template>
