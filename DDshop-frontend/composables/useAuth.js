import { ref, onMounted } from 'vue'
import { useCart } from '~/composables/useCart'



export const useAuth = createSharedComposable(() => {
  const {cart,inactiveCart}=useCart();
const user = ref(null)

function saveUserToStorage(userData) {
  sessionStorage.setItem('user', JSON.stringify(userData))
}

function loadUserFromStorage() {
  const data = sessionStorage.getItem('user')
  if (data) user.value = JSON.parse(data)
}


async function login(email, password) { //login face loginul si verifica daca datele sunt corect
  try {
    const response = await fetch('http://localhost:8081/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password })
    })
    if (!response.ok) throw new Error('Invalid credentials')

    const userData = await response.json()
    user.value = userData

    saveUserToStorage(userData)

    return userData


  } catch (e) {
    throw e
  }
}

async function logout() { //logout elimina userul din sessionStorage
  user.value = null
   await inactiveCart(cart.value.id)
  sessionStorage.removeItem('user')
}

onMounted(() => {
    loadUserFromStorage()
  })
  return {
    user,
    login,
    logout
  }
})
