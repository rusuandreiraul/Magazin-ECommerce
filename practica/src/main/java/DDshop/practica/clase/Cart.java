package DDshop.practica.clase;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double totalPrice;

    private String status;


    @ManyToOne
    @JoinColumn(name = "user_id") //in cart va exista user_id ca FK
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cart_Entry> cartEntries = new ArrayList<>();

    public Cart(double totalPrice, User user, List<Cart_Entry> cartEntries) {
        this.totalPrice = totalPrice;
        this.user = user;
        this.cartEntries = cartEntries;
        this.status="ACTIV";
    }
}