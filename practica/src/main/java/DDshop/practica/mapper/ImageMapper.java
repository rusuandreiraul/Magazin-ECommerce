package DDshop.practica.mapper;

import DDshop.practica.dto.ImageRequestDTO;
import DDshop.practica.dto.ImageResponseDTO;
import DDshop.practica.clase.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ImageMapper {


    ImageResponseDTO toResponseDTO(Image image);

    @Mapping(target = "product", ignore = true)
    Image toEntity(ImageRequestDTO dto);
}
