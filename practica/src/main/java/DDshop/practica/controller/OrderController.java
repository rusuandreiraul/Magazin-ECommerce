package DDshop.practica.controller;


import DDshop.practica.dto.OrderRequestDTO;
import DDshop.practica.dto.OrderResponseDTO;
import DDshop.practica.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000/")
public class OrderController {

    @Autowired
    OrderService orderService;


    @PostMapping("/order/{id_user}/add-order/{id_cart}")
    public String addOrder(@RequestBody OrderRequestDTO dto, @PathVariable Long id_user, @PathVariable Long id_cart){
       return orderService.addOrder(dto, id_user, id_cart);
    }

    @GetMapping("/order/see-orders/{id_user}")
    public List<OrderResponseDTO> getOrdersUser(@PathVariable Long id_user) {
        return orderService.getOrdersByUser(id_user);
    }

    @GetMapping("/orders")
    public List<OrderResponseDTO> getOrders(@RequestParam(required = false) String cat, @RequestParam(required = false) String searchQuery, @RequestParam(name = "selectedDate",required = false)LocalDate data){
       return orderService.getOrderByFilters(cat, searchQuery, data);
    }

    @GetMapping("order/{id}")
    public OrderResponseDTO getOrderId(@PathVariable Long id){
        return orderService.findOrderId(id);
    }

}
