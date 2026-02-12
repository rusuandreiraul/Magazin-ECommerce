package DDshop.practica.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FavoritesResponseDTO {
    private Long id;
    private String name;
    private double price;
    private List<ImageResponseDTO> images;
    private String description;
}
