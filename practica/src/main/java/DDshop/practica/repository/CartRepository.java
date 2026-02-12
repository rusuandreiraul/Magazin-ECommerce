package DDshop.practica.repository;

import DDshop.practica.clase.Cart;
import DDshop.practica.clase.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUser(User u);

    Optional<Cart> findByUserId(Long userId);

        Cart findByUserAndStatus(User user, String status);

    Optional<Cart> findByUserIdAndStatus(Long userId, String activ);
}
