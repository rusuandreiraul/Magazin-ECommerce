package DDshop.practica.dto;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CartRequestDTO {
    private double totalPrice;
    private Long userId;

    //intrarile in cart se gestioneaza separat
}