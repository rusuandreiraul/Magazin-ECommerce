package DDshop.practica.service;

import DDshop.practica.clase.Cart;
import DDshop.practica.clase.Cart_Entry;
import DDshop.practica.clase.Product;
import DDshop.practica.clase.User;
import DDshop.practica.dto.CartEntryRequestDTO;
import DDshop.practica.dto.CartResponseDTO;

import DDshop.practica.mapper.CartEntryMapper;
import DDshop.practica.mapper.CartMapper;
import DDshop.practica.repository.CartEntryRepository;
import DDshop.practica.repository.CartRepository;
import DDshop.practica.repository.ProductRepository;
import DDshop.practica.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {

    @Autowired
    CartMapper cartMapper;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartEntryRepository cartEntryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartEntryMapper cartEntryMapper;

    public CartResponseDTO addToCart(CartEntryRequestDTO dto, Long id_user) {
        Cart cart=createOrGetCart(id_user);
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Produsul nu exista"));

        Cart_Entry cartEntry = new Cart_Entry();
        cartEntry.setCart(cart);
        cartEntry.setProduct(product);
        if(product.getDiscountPrice()!=null && product.getDiscountPrice()<product.getPrice()) {
            cartEntry.setPricePerPiece(product.getDiscountPrice());
            cartEntry.setQuantity(dto.getQuantity());
            cartEntry.setTotalPricePerEntry(dto.getQuantity() * product.getDiscountPrice());

            cart.getCartEntries().add(cartEntry);

            cart.setTotalPrice(cart.getCartEntries().stream()
                    .mapToDouble(entry -> entry.getPricePerPiece() * entry.getQuantity()).sum());

            cartRepository.save(cart);
        }
        else{
            cartEntry.setPricePerPiece(product.getPrice());
            cartEntry.setQuantity(dto.getQuantity());
            cartEntry.setTotalPricePerEntry(dto.getQuantity() * product.getPrice());

            cart.getCartEntries().add(cartEntry);

            cart.setTotalPrice(cart.getCartEntries().stream()
                    .mapToDouble(entry -> entry.getPricePerPiece() * entry.getQuantity()).sum());

            cartRepository.save(cart);
        }
        return cartMapper.toResponseDTO(cart);
    }

    public Cart createOrGetCart(Long userId) {
        Optional<Cart> optionalCart = cartRepository.findByUserIdAndStatus(userId, "ACTIV");

        if (optionalCart.isPresent()) {
            return optionalCart.get();
        }

        return userRepository.findById(userId).map(user -> {
            Cart cart = new Cart();
            cart.setUser(user);
            cart.setTotalPrice(0);
            cart.setStatus("ACTIV");
            cart.setCartEntries(new ArrayList<>());
            return cartRepository.save(cart);
        }).orElseThrow(() -> new RuntimeException("Userul nu există"));
    }

    private Optional<Cart> getCart(Long idCart) {
        return cartRepository.findById(idCart);
    }

    public CartResponseDTO getProductCart(Long idUser) {
        Cart cart = createOrGetCart(idUser);
        return cartMapper.toResponseDTO(cart);
    }

    @Transactional
    public String deleteFromCart(Long idUser, Long idProduct) {
        Optional<User> u = userRepository.findById(idUser);
        Optional<Product> p = productRepository.findById(idProduct);

        if (u.isEmpty()) {
            return "Eroare: userul nu a fost găsit";
        }
        if (p.isEmpty()) {
            return "Eroare: produsul nu a fost găsit";
        }

        Cart c = cartRepository.findByUserAndStatus(u.get(), "ACTIV");
        if (c == null) {
            return "Eroare: coșul nu a fost găsit";
        }

        Optional<Cart_Entry> entry = c.getCartEntries().stream()
                .filter(e -> e.getProduct().getId().equals(idProduct))
                .findFirst();

        if (entry.isEmpty()) {
            return "Produsul nu există în coș";
        }


        c.getCartEntries().remove(entry.get());


        double total = c.getCartEntries().stream()
                .mapToDouble(e -> e.getProduct().getPrice() * e.getQuantity())
                .sum();
        c.setTotalPrice(total);


        cartRepository.save(c);
        cartRepository.flush();

        return "Produsul a fost șters cu succes";
    }


    public void updateCart(Long idUser, Map<Long, Integer> quantities) {
        Cart cart=createOrGetCart(idUser);
        Set<Long> keys=quantities.keySet();
        for(Cart_Entry entry : cart.getCartEntries()){
            if(quantities.containsKey(entry.getProduct().getId())){
                entry.setQuantity(quantities.get(entry.getProduct().getId()));
                entry.setTotalPricePerEntry(entry.getProduct().getPrice()*entry.getQuantity());
            }
        }

        cart.setTotalPrice(cart.getCartEntries().stream()
                .mapToDouble(e -> e.getProduct().getPrice() * e.getQuantity())
                .sum()
        );

        cartRepository.save(cart);



    }

    public Optional<Cart> inactive(Long idCart) {
        Optional<Cart> c = getCart(idCart);

        if (c.isPresent()) {
            Cart cart = c.get();
            cart.setStatus("INACTIV");
            cartRepository.save(cart);
            return Optional.of(cart);
        }

        return Optional.empty();
    }


}
