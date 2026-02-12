package DDshop.practica.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AddressRequestDTO {
    private String street;
    private int postalCode;
    private String city;
    private String county;
    private String country;

}
