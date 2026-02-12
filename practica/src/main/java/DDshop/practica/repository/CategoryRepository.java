package DDshop.practica.repository;

import DDshop.practica.clase.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {


    Category findByName(String category);
}
