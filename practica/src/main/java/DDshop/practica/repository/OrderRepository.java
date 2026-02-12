package DDshop.practica.repository;

import DDshop.practica.clase.Order;
import DDshop.practica.clase.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {
    List<Order> findAllByUser(User user);
}
