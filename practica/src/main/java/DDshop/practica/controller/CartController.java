package DDshop.practica.controller;


import DDshop.practica.clase.Cart;
import DDshop.practica.dto.CartEntryRequestDTO;
import DDshop.practica.dto.CartEntryResponseDTO;
import DDshop.practica.dto.CartResponseDTO;
import DDshop.practica.service.CartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000/")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/cart/{id_user}/add-product-cart")
    public CartResponseDTO addProductToCart(@RequestBody CartEntryRequestDTO dto, @PathVariable Long id_user){
         return cartService.addToCart(dto, id_user);
    }
    @GetMapping("/cart/{id_user}")
    public CartResponseDTO showCart(@PathVariable Long id_user){
        return cartService.getProductCart(id_user);

    }

    @DeleteMapping("/cart/{id_user}/delete-product/{id_product}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable Long id_user, @PathVariable Long id_product) {
        String result = cartService.deleteFromCart(id_user, id_product);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/cart/{id_user}/update-cart")
    public void updateCart(@PathVariable Long id_user, @RequestBody Map<Long, Integer> quantities){
        cartService.updateCart(id_user, quantities);
    }

    @PutMapping("/cart/inactive-cart/{id}")
    public ResponseEntity<Cart> inactiveCart(@PathVariable Long id) {
        Optional<Cart> cart = cartService.inactive(id);
        return cart.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }



}
