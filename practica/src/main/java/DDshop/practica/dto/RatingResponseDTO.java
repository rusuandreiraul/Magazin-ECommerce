package DDshop.practica.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RatingResponseDTO {
    private Long id;
    private int valoare;
    private String comentariu;

    private UserResponseDTO user;
    private ProductResponseDTO product;
}
