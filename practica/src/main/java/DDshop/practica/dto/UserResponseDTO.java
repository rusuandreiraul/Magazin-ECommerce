package DDshop.practica.dto;

import DDshop.practica.clase.Rol;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Rol rol;
    private AddressResponseDTO defaultDeliveryAddress;
    private AddressResponseDTO defaultBillingAddress;


}
