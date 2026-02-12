package DDshop.practica.mapper;
import DDshop.practica.clase.Order;
import DDshop.practica.dto.OrderRequestDTO;
import DDshop.practica.dto.OrderResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CartMapper.class, AddressMapper.class})
public interface OrderMapper {

    OrderResponseDTO toResponseDTO(Order order);


    @Mapping(target = "user", ignore = true)
    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "deliveryAddress", ignore = true)
    @Mapping(target = "invoiceAddress", ignore = true)
    Order toEntity(OrderRequestDTO dto);
}