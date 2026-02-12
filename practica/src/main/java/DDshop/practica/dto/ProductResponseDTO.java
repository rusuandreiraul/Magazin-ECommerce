package DDshop.practica.dto;

import DDshop.practica.clase.Product_Attribute;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class ProductResponseDTO {

    private Long id;
    private String name;
    private String description;
    private double price;
    private Double discountPrice;
    private int availableQuantity;
    private LocalDateTime addedDate;

    private CategoryResponseDTO category;
    private List<Product_AttributeResponseDTO> attributes;
    private List<ImageResponseDTO> images;


}
