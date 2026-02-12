<script setup>
import Navbar from "~/components/navbar.vue";
</script>

<template>
  <Navbar />
  <div class="max-h-screen h-screen overflow-y-auto">
    <div class="relative w-full h-[500px]">
      <img
        src="/biblioteca.jpg"
        alt="Bibliotecă"
        class="w-full h-full object-cover"
      />
      <div class="absolute inset-0 bg-black/50"></div>
      <div
        class="absolute inset-0 flex flex-col items-center justify-center text-white text-center px-4"
      >
        <h1 class="text-5xl font-extrabold drop-shadow-lg">
          Descoperă lumea cărților
        </h1>
        <p class="text-xl mt-4 max-w-2xl">
          Găsește titlul perfect pentru tine — de la clasici la cele mai noi
          apariții.
        </p>
        <NuxtLink to="/">
          <button
            class="bg-blue-600 hover:bg-blue-700 transition-colors px-6 py-3 rounded-xl mt-10 font-semibold shadow-lg"
          >
            Vezi colecția
          </button>
        </NuxtLink>
      </div>
    </div>

    <div class="grid md:grid-cols-2 gap-10 m-10 items-center">
      <div class="flex flex-col justify-center">
        <h2 class="text-3xl font-bold mb-4 text-gray-900">
          Vino și descoperă fantezia cărților alături de noi
        </h2>
        <p class="text-lg text-gray-600 leading-relaxed mb-6">
          Credem că fiecare carte spune o poveste, iar misiunea noastră este să
          te ajutăm să o descoperi pe cea care ți se potrivește. Explorează
          colecțiile noastre și lasă-te inspirat de magia lecturii.
        </p>
        <NuxtLink to="/about">
          <button
            class="bg-gray-900 text-white px-5 py-3 rounded-lg hover:bg-gray-800 transition-colors shadow-md"
          >
            Află mai multe
          </button>
        </NuxtLink>
      </div>
      <div class="flex justify-center">
        <img
          src="/biblio2.jpg"
          alt="Bibliotecă"
          class="rounded-3xl shadow-xl max-w-md w-full object-cover"
        />
      </div>
    </div>

    <div class="bg-gray-50 py-16">
      <h2 class="text-4xl font-bold text-center text-gray-900 mb-12">
        Ce spun cititorii noștri 📚
      </h2>
      <div class="max-w-6xl mx-auto grid md:grid-cols-3 gap-8 px-6">
        <div class="bg-white rounded-2xl shadow-lg p-6 flex flex-col">
          <p class="text-gray-700 italic mb-4">
            „Am descoperit cărți minunate pe care nu le-aș fi găsit în altă
            parte. Atmosfera acestui magazin online mă face să revin mereu.”
          </p>
          <div class="flex items-center mt-auto">
            <img
              src="https://randomuser.me/api/portraits/women/68.jpg"
              alt="Ana"
              class="w-12 h-12 rounded-full mr-4"
            />
            <div>
              <h4 class="font-semibold text-gray-900">Ana Popescu</h4>
              <p class="text-sm text-gray-500">Cititoare pasionată</p>
            </div>
          </div>
        </div>

        <div class="bg-white rounded-2xl shadow-lg p-6 flex flex-col">
          <p class="text-gray-700 italic mb-4">
            „Servicii excelente și o colecție variată! Am găsit exact ce căutam,
            iar livrarea a fost rapidă și sigură.”
          </p>
          <div class="flex items-center mt-auto">
            <img
              src="https://randomuser.me/api/portraits/men/32.jpg"
              alt="Mihai"
              class="w-12 h-12 rounded-full mr-4"
            />
            <div>
              <h4 class="font-semibold text-gray-900">Mihai Ionescu</h4>
              <p class="text-sm text-gray-500">Profesor de istorie</p>
            </div>
          </div>
        </div>

        <div class="bg-white rounded-2xl shadow-lg p-6 flex flex-col">
          <p class="text-gray-700 italic mb-4">
            „Îmi place mult secțiunea de recomandări personalizate. Am
            descoperit titluri care au devenit preferatele mele.”
          </p>
          <div class="flex items-center mt-auto">
            <img
              src="https://randomuser.me/api/portraits/women/44.jpg"
              alt="Ioana"
              class="w-12 h-12 rounded-full mr-4"
            />
            <div>
              <h4 class="font-semibold text-gray-900">Ioana Marinescu</h4>
              <p class="text-sm text-gray-500">Studentă la Litere</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
