package DDshop.practica.mapper;

import DDshop.practica.clase.Image;
import DDshop.practica.clase.Product;
import DDshop.practica.dto.FavoritesResponseDTO;
import DDshop.practica.dto.ImageResponseDTO;

import java.util.List;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface FavoritesMapper {


    @Mapping(target = "images", source = "images")
    FavoritesResponseDTO toFavoritesResponseDTO(Product product);


    ImageResponseDTO toImageResponseDTO(Image image);


    List<ImageResponseDTO> toImageResponseDTOList(List<Image> images);
}
