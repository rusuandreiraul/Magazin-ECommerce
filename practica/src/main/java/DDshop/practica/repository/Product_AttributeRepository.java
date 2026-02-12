package DDshop.practica.repository;

import DDshop.practica.clase.Product_Attribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Product_AttributeRepository extends JpaRepository<Product_Attribute, Long> {
    Product_Attribute findByNameAndValue(String name, String value);
}
