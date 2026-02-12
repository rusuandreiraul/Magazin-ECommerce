package DDshop.practica.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ImageRequestDTO {
    private String url;
    private Long id_produs;
}
