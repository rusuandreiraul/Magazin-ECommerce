<script setup>
  import { Swiper, SwiperSlide } from 'swiper/vue'
  import { Autoplay } from 'swiper/modules'
  import 'swiper/css'
  import 'swiper/css/autoplay'
  </script>

<template>
    <Swiper :slides-per-view="1" :loop="true" :autoplay="{ delay: 7000 }" :modules="[Autoplay]" class="w-full max-w-6xl mx-auto rounded-lg overflow-hidden">
      <SwiperSlide>
        <img src="/banner3.jpg" alt="Promo 1" class="w-full h-72 object-cover" />
      </SwiperSlide>
      <SwiperSlide>
        <img src="/banner2.jpg" alt="Promo 2" class="w-full h-72 object-cover" />
      </SwiperSlide>
    </Swiper>
  </template>
  
