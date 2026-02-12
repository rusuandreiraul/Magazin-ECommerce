import { defineNuxtConfig } from 'nuxt/config'
import { resolve } from 'path'

export default defineNuxtConfig({
  compatibilityDate: '2025-05-15',
  devtools: { enabled: true },
  modules: ['@nuxt/image', '@nuxt/ui', '@nuxt/icon', '@nuxt/fonts', '@nuxtjs/color-mode', '@vueuse/nuxt'],
  alias: {
    '@': resolve(__dirname, '.'),
  },
  css: [ 
    '~/assets/css/main.css', 
  ],
})