  
  <script setup>
  import { useAuth } from '~/composables/useAuth'
  import { HeartIcon, ShoppingCartIcon } from '@heroicons/vue/24/outline'
  import { UserIcon } from '@heroicons/vue/24/outline'
import { useCart } from '~/composables/useCart'

  const { user, logout } = useAuth()

  const {cartCount}=useCart();
  
  const showMenu=ref(false)
  function handleLogout() {
    logout()
  }


  function toggleMenu() {
  showMenu.value = !showMenu.value
}


  </script>

<template>
    <nav class="navbar flex justify-between items-center px-8 py-4 bg-gray-800 text-white">
      <NuxtLink to="/">
      <h1 class="font-bold text-xl">DD Shop</h1>
    </NuxtLink>
  
      <div class="space-x-4 flex items-center">
       <div v-if="user && user.firstName">
          <NuxtLink to="/user/favorites">
          <button class="relative">
          <HeartIcon class="w-8 h-8 text-white-700 hover:text-white-800" />
        </button>
        </NuxtLink>
      </div>
        <NuxtLink to="/cart">
        <button @click="goToCart" class="relative">
        <ShoppingCartIcon class="w-8 h-8 text-white-700 hover:text-white-800" />
        <span v-if="cartCount > 0" class="absolute -top-2 -right-2 bg-red-600 text-white text-xs font-bold rounded-full w-5 h-5 flex items-center justify-center">
          {{ cartCount }}
        </span>
       </button>
      </NuxtLink>
  
      <div>
        <template v-if="user && user.firstName">
         <button @click="toggleMenu" class="flex items-center gap-2 hover:text-gray-700">
          <UserIcon class="w-7 h-7 text-white-700 hover:text-white-800" />
        </button>
            <div v-if="showMenu" class="absolute mt-2 right-0 bg-white border border-gray-300 rounded shadow-lg z-50 w-56">
            <NuxtLink to="/user/changeData" class="block px-4 py-2 hover:bg-gray-200 text-black">Modificare date personale</NuxtLink>
            <NuxtLink to="/user/orders" class="block px-4 py-2 hover:bg-gray-200 text-black">Comenzile mele</NuxtLink>
            <NuxtLink to="/user/favorites" class="block px-4 py-2 hover:bg-gray-200 text-black">Produse favorite</NuxtLink>
            <button @click="handleLogout" class="block w-full text-left px-4 py-2 hover:bg-gray-200 text-red-600 font-semibold">Logout</button>
            </div>
      </template>

    <template v-else>
      <NuxtLink to="/user/login" class="p-3 rounded-2xl bg-blue-500 hover:bg-blue-600">Login</NuxtLink>
    </template>
</div>
</div>
    </nav>
  </template>


  