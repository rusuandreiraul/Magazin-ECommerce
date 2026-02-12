package DDshop.practica.dto;
import lombok.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CartResponseDTO {
    private Long id;
    private double totalPrice;
    private UserResponseDTO user;  // DTO-ul pentru utilizator
    private List<CartEntryResponseDTO> cartEntries;  // lista de intrări din coș
}