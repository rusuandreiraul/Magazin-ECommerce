package DDshop.practica.mapper;

import DDshop.practica.clase.Cart_Entry;
import DDshop.practica.dto.CartEntryRequestDTO;
import DDshop.practica.dto.CartEntryResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface CartEntryMapper {

    CartEntryResponseDTO toResponseDTO(Cart_Entry entity);

    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "product", ignore = true)
    Cart_Entry toEntity(CartEntryRequestDTO dto);
}