package DDshop.practica.clase;


import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String street;
    private int postalCode;
    private String city;
    private String county;
    private String country;


    public Address(String street, int postalCode, String city, String county, String country) {
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
        this.county = county;
        this.country = country;
    }
}
