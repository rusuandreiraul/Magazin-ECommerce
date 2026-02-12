<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import { useAuth } from "~/composables/useAuth";
import UserInfoForm from "~/components/UserInfoForm.vue";
import AddressForm from "~/components/AddressForm.vue";

const router = useRouter();
const { user } = useAuth();

const sameAddress = ref(true);
const phone = ref("");
const delivery = ref({});
const billing = ref({});
const successMessage = ref("");
const errorMessage = ref("");

function addressChanged(original, current) {
  return (
    original.street !== current.street ||
    original.postalCode !== current.postalCode ||
    original.city !== current.city ||
    original.county !== current.county ||
    original.country !== current.country
  );
}

async function handleChange() {
  const userData = { email: user.value.email };

  if (user.value.phoneNumber !== phone.value) {
    userData.phoneNumber = phone.value;
  }

  if (addressChanged(user.value.defaultDeliveryAddress, delivery.value)) {
    userData.defaultDeliveryAddress = delivery.value;
  }

  const billingAddress = sameAddress.value ? delivery.value : billing.value;

  if (addressChanged(user.value.defaultBillingAddress, billingAddress)) {
    userData.defaultBillingAddress = billingAddress;
  }

  try {
    const response = await fetch("http://localhost:8081/changeData", {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(userData),
    });

    if (!response.ok) throw new Error("Eroare la modificarea datelor");

    successMessage.value = "Datele au fost modificate cu succes!";
    setTimeout(() => router.push("/"), 2000);
  } catch (err) {
    errorMessage.value = err.message || "Eroare neașteptată";
  }
}

onMounted(() => {
  if (user.value) {
    phone.value = user.value.phoneNumber;
    delivery.value = { ...user.value.defaultDeliveryAddress };
    billing.value = { ...user.value.defaultBillingAddress };
  }
});
</script>

<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-50">
    <form
      @submit.prevent="handleChange"
      class="bg-white p-8 rounded shadow-md w-full max-w-md space-y-6"
    >
      <h1 class="text-2xl font-bold text-center">Modificare date</h1>

      <UserInfoForm v-model:phone="phone" />
      <AddressForm :prefix="'d'" v-model:address="delivery" />

      <div class="flex items-center">
        <input
          type="checkbox"
          v-model="sameAddress"
          id="sameAddress"
          class="form-checkbox h-5 w-5 text-blue-600"
        />
        <label for="sameAddress" class="ml-2 text-sm text-gray-700"
          >Adresa de facturare este aceeași</label
        >
      </div>

      <AddressForm
        v-if="!sameAddress"
        :prefix="'b'"
        v-model:address="billing"
      />

      <p v-if="errorMessage" class="text-red-600 text-center text-sm">
        {{ errorMessage }}
      </p>
      <p v-if="successMessage" class="text-green-600 text-center text-sm">
        {{ successMessage }}
      </p>

      <button
        type="submit"
        class="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700 transition"
      >
        Modifică datele
      </button>

      <div class="mt-4 flex justify-between text-sm text-gray-600">
        <NuxtLink to="/" class="hover:underline">Home</NuxtLink>
      </div>
    </form>
  </div>
</template>
