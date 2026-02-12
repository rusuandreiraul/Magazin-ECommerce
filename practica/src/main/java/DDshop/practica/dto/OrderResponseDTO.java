package DDshop.practica.dto;


import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderResponseDTO {
    private Long id;
    private UserResponseDTO user;
    private CartResponseDTO cart;
    private String paymentType;  // enum ca String pentru simplificare

    private AddressResponseDTO deliveryAddress;
    private AddressResponseDTO invoiceAddress;

    private double totalPrice;
    private LocalDateTime orderDate;
}
