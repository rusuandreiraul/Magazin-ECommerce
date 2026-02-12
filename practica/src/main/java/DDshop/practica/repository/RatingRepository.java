package DDshop.practica.repository;

import DDshop.practica.clase.Product;
import DDshop.practica.clase.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByProduct(Product product);
}
