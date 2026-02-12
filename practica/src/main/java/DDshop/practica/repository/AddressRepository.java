package DDshop.practica.repository;

import DDshop.practica.clase.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {




    Optional<Address> findByStreetAndCityAndPostalCode(String street, String city, int postalCode);
}
