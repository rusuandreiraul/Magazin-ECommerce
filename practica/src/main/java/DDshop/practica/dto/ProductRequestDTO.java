package DDshop.practica.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductRequestDTO {

    private String name;
    private String description;
    private double price;
    private Double discountPrice;
    private int availableQuantity;
    private LocalDateTime addedDate;

    private String category;
    private List<Product_AttributeRequestDTO> attributes;
    private List<String> imagesUrl;

    public ProductRequestDTO(String name, String description, double price, int availableQuantity, LocalDateTime addedDate, String category, List<Product_AttributeRequestDTO> attributes, List<String>imagesUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.availableQuantity = availableQuantity;
        this.category = category;
        this.attributes = attributes;
        this.imagesUrl = imagesUrl;

    }

}
