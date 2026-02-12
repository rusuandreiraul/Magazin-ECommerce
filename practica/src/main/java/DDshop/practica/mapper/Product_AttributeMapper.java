package DDshop.practica.mapper;

import DDshop.practica.clase.Product_Attribute;
import DDshop.practica.dto.Product_AttributeRequestDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface Product_AttributeMapper {


    Product_Attribute toEntity(Product_AttributeRequestDTO dto);

    Product_AttributeRequestDTO toDTO(Product_Attribute entity);
}