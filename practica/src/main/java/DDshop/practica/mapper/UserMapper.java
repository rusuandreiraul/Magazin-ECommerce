package DDshop.practica.mapper;
import DDshop.practica.clase.User;
import DDshop.practica.dto.UserRequestDTO;
import DDshop.practica.dto.UserResponseDTO;
import DDshop.practica.dto.AddressResponseDTO;
import DDshop.practica.clase.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring") //pentru a putea fi folosit ca bean de spring
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    //mapping ia valoarea din dto si o pune in entitate, ia valoarea din dto id si o pune in entitate cu valoarea id
    @Mapping(source = "defaultDeliveryAddress", target = "defaultDeliveryAddress")
    @Mapping(source = "defaultBillingAddress", target = "defaultBillingAddress")
    User toEntity(UserRequestDTO dto);

    @Mapping(source = "defaultDeliveryAddress", target = "defaultDeliveryAddress")
    @Mapping(source = "defaultBillingAddress", target = "defaultBillingAddress")
    UserResponseDTO toResponseDTO(User user);
}