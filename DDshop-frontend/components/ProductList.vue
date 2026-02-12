<script setup>
import ProductCard from './ProductCard.vue'
const props = defineProps({
  products: Array,
  likedProducts: Array,
  clickedProducts: Array,
  successMessages: Object,
  title: String,
  onAddToCart: Function,
  onToggleLike: Function
})
</script>

<template>
    <div>
      <h2 v-if="title" class="text-3xl font-bold text-center mb-6">{{ title }}</h2>
      <div class="grid gap-6 grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 items-stretch overflow-hidden">
        <ProductCard v-for="product in products" :key="product.id" :product="product" :liked="likedProducts.includes(product.id)" :clicked="clickedProducts.includes(product.id)" :successMessage="successMessages[product.id]" :onAddToCart="onAddToCart" :onToggleLike="onToggleLike"
        />
      </div>
    </div>
  </template>
