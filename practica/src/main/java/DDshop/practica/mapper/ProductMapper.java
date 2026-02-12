package DDshop.practica.mapper;
import DDshop.practica.clase.Product;
import DDshop.practica.dto.ProductRequestDTO;
import DDshop.practica.dto.ProductResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class, Product_AttributeMapper.class})
public interface ProductMapper {


    ProductResponseDTO toResponseDTO(Product product);

    //category si attribute cand facem entitata luam id si cautam obiectul pe baza id
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "attributes", ignore = true)
    Product toEntity(ProductRequestDTO dto);
}