package DDshop.practica.mapper;

import DDshop.practica.clase.Cart;
import DDshop.practica.dto.CartRequestDTO;
import DDshop.practica.dto.CartResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CartEntryMapper.class})
public interface CartMapper {

    CartResponseDTO toResponseDTO(Cart cart);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "cartEntries", ignore = true)
    Cart toEntity(CartRequestDTO dto);
}