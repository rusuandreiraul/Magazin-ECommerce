package DDshop.practica.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RatingRequestDTO {
    private int valoare;
    private String comentariu;

    private Long userId;
    private Long productId;
}
