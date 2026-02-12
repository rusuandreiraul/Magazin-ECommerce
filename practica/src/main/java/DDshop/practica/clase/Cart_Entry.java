package DDshop.practica.clase;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Cart_Entry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    private double pricePerPiece;


    @Column(name = "total_price_per_entry")
    private Double totalPricePerEntry;

    @ManyToOne
    @JoinColumn(name="cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Cart_Entry(int quantity, double pricePerPiece, Double totalPricePerEntry, Cart cart, Product product) {
        this.quantity = quantity;
        this.pricePerPiece = pricePerPiece;

        this.cart = cart;
        this.product = product;
    }
}
