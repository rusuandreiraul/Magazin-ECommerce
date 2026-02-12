package DDshop.practica.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AddressResponseDTO {

    private Long id;
    private String street;
    private int postalCode;
    private String city;
    private String country;
    private String county;


}
