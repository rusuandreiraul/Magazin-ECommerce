package DDshop.practica.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CartEntryRequestDTO {
    private int quantity;
    private double pricePerPiece;

    private Long cartId;
    private Long productId;
}