<script setup>
import { useCart } from "~/composables/useCart";

const { cart } = useCart();
</script>

<template>
  <div class="bg-gray-100 p-6 rounded-lg shadow">
    <h3 class="text-xl font-semibold mb-4">Rezumat comandă</h3>
    <ul class="divide-y divide-gray-200">
      <li
        v-for="entry in cart.cartEntries"
        :key="entry.product.id"
        class="py-2 flex justify-between"
      >
        <span>{{ entry.product.name }} x{{ entry.quantity }}</span>
        <span>{{ (entry.product.price * entry.quantity).toFixed(2) }} RON</span>
      </li>
    </ul>
    <div class="mt-4 border-t pt-4 flex justify-between font-bold">
      <span>Total:</span>
      <span>{{ cart.totalPrice.toFixed(2) }} RON</span>
    </div>
  </div>
</template>
