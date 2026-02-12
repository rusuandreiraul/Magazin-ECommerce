/** @type {import('tailwindcss').Config} */
module.exports = {
  darkMode: 'class', // <--- necesar pentru dark mode cu clasa `dark`
  content: [
    './components/**/*.{vue,js,ts}',
    './layouts/**/*.{vue,js,ts}',
    './pages/**/*.{vue,js,ts}',
    './app.vue',
    './nuxt.config.ts',
    './node_modules/daisyui/dist/**/*.js',
  ],
  theme: {
    extend: {},
  },
  plugins: [require('daisyui')],
}
