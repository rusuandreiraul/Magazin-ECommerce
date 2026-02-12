package DDshop.practica.mapper;

import DDshop.practica.clase.Category;
import DDshop.practica.dto.CategoryRequestDTO;
import DDshop.practica.dto.CategoryResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponseDTO toResponseDTO(Category category);

    Category toEntity(CategoryRequestDTO dto);
}