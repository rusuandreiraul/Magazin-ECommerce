package DDshop.practica.repository;

import DDshop.practica.clase.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepositoryCustom {

    List<Order> findOrdersByFilter(String text, String category, LocalDate data);

}

