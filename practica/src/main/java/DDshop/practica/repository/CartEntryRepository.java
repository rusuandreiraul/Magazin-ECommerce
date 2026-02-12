package DDshop.practica.repository;

import DDshop.practica.clase.Cart_Entry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartEntryRepository extends JpaRepository<Cart_Entry, Long> {
}
