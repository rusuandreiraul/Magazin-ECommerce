package DDshop.practica.dto;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CartEntryResponseDTO {
    private Long id;
    private int quantity;
    private double pricePerPiece;
    private Double totalPricePerEntry;

    private ProductResponseDTO product;
}
