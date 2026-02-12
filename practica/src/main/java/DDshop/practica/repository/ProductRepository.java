package DDshop.practica.repository;

import DDshop.practica.clase.Category;
import DDshop.practica.clase.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    List<Product> findByCategory(Category category);

    List<Product> findByCategoryId(Long idCat);

    List<Product> findAllByOrderByPriceAsc();

    List<Product> findAllByOrderByPriceDesc();

    List<Product> findByCategoryIdOrderByPriceDesc(Long idCat);

    List<Product> findByCategoryIdOrderByPriceAsc(Long idCat);

    Product findByName(String name);

    List<Product> findTop4ByOrderByAddedDateDesc();

    @Modifying
    @Query("UPDATE Product p SET p.promo = false")
    void resetAllPromoted();

    @Modifying
    @Query("UPDATE Product p SET p.promo = true WHERE p.id IN :ids")
    void setPromotedForIds(@Param("ids") List<Long> ids);

    List<Product> findByPromo(boolean b);
}
