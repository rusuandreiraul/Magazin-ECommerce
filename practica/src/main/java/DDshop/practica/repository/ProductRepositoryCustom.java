package DDshop.practica.repository;

import DDshop.practica.clase.Product;
import DDshop.practica.clase.Product_Attribute;

import java.util.List;

public interface ProductRepositoryCustom {
    List<Product> findProductsByFilters(Long categoryId, String filter, List<Product_Attribute> attributes);


    List<Product> findPopularProducts();
}
