package DDshop.practica.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class CategoryResponseDTO {

    private Long id;
    private String name;
    private String description;
}
