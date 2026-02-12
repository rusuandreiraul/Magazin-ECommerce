package DDshop.practica.mapper;


import DDshop.practica.clase.Product;
import DDshop.practica.clase.Rating;
import DDshop.practica.clase.User;
import DDshop.practica.dto.RatingRequestDTO;
import DDshop.practica.dto.RatingResponseDTO;
import DDshop.practica.dto.ProductResponseDTO;
import DDshop.practica.dto.UserResponseDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ProductMapper.class})
public interface RatingMapper {

    // ENTITY -> DTO
    @Mapping(source = "user", target = "user")
    @Mapping(source = "product", target = "product")
    RatingResponseDTO toDTO(Rating rating);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "userId", qualifiedByName = "userFromId")
    @Mapping(target = "product", source = "productId", qualifiedByName = "productFromId")
    Rating toEntity(RatingRequestDTO dto);


    @Named("userFromId")
    default User mapUser(Long userId) {
        if (userId == null) return null;
        User user = new User();
        user.setId(userId);
        return user;
    }


    @Named("productFromId")
    default Product mapProduct(Long productId) {
        if (productId == null) return null;
        Product product = new Product();
        product.setId(productId);
        return product;
    }
}
