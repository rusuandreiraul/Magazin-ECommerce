package DDshop.practica.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;

    private AddressRequestDTO defaultDeliveryAddress;
    private AddressRequestDTO defaultBillingAddress;
}
