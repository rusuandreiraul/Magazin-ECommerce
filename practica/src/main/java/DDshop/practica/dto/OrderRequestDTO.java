package DDshop.practica.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderRequestDTO {

        private Long userId;
        private Long cartId;
        private String paymentType;
        private AddressRequestDTO deliveryAddress;
        private AddressRequestDTO invoiceAddress;

        private double totalPrice;
        private LocalDateTime orderDate;
    }

