package DDshop.practica.mapper;

import DDshop.practica.clase.Address;
import DDshop.practica.dto.AddressRequestDTO;
import DDshop.practica.dto.AddressResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressResponseDTO toResponseDTO(Address address);

    Address toEntity(AddressRequestDTO dto);

}
