package DDshop.practica.clase;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol")
    private Rol rol;

    @OneToOne
    @JoinColumn(name="default_delivery_address", nullable = true)
    private Address defaultDeliveryAddress;

    @OneToOne
    @JoinColumn(name="default_billing_address", nullable = true)
    private Address defaultBillingAddress;

    @ManyToMany
    @JoinTable(
            name = "user_favorites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> favoriteProducts = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Rating> ratings;

    public User(String firstName, String lastName, String email, String phoneNumber, String password, Address defaultDeliveryAddress, Address defaultBillingAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.defaultDeliveryAddress = defaultDeliveryAddress;
        this.defaultBillingAddress = defaultBillingAddress;
    }

}
