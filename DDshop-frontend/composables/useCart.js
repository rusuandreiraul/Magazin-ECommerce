
import { ref, computed } from 'vue'


const cart = ref({
  cartEntries: [],
  totalPrice: 0
})

const cartCount = computed(() =>
  cart.value.cartEntries.reduce((acc, item) => acc + item.quantity, 0)
)

export function useCart() {
  return {
    cart,
    cartCount
  }
}
