package DDshop.practica.service;

import DDshop.practica.clase.*;
import DDshop.practica.dto.AddressRequestDTO;
import DDshop.practica.dto.AddressResponseDTO;
import DDshop.practica.dto.OrderRequestDTO;
import DDshop.practica.dto.OrderResponseDTO;
import DDshop.practica.mapper.AddressMapper;
import DDshop.practica.mapper.OrderMapper;
import DDshop.practica.repository.AddressRepository;
import DDshop.practica.repository.CartRepository;
import DDshop.practica.repository.OrderRepository;
import DDshop.practica.repository.UserRepository;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    EmailService emailService;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    AddressMapper addressMapper;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    CartRepository cartRepository;



    public void sendOrderConfirmationEmail(String userEmail, Order o) {
        StringBuilder htmlBody = new StringBuilder();

        htmlBody.append("<h1>Multumim ca ai comandat de la noi</h1>");
        htmlBody.append("<table border='1' cellpadding='5' cellspacing='0'>");
        htmlBody.append("<tr>")
                .append("<th>Id comanda</th>")
                .append("<th>Data comanda</th>")
                .append("<th>Produse Comandate</th>")
                .append("<th>Pret total</th>")
                .append("</tr>");

        htmlBody.append("<tr>")
                .append("<td>").append(o.getId()).append("</td>")
                .append("<td>").append(o.getOrderDate()).append("</td>")
                .append("<td><ul>");

        for (Cart_Entry ce : o.getCart().getCartEntries()) {
            htmlBody.append("<li>")
                    .append(ce.getProduct().getName())
                    .append(" x").append(ce.getQuantity())
                    .append("</li>");
        }

        htmlBody.append("</ul></td>")
                .append("<td>").append(o.getTotalPrice()).append(" RON</td>")
                .append("</tr>")
                .append("</table>");

        htmlBody.append("<br><a href='http://localhost:3000/user/orders'>Vezi comenzile tale</a>");

        emailService.sendHtmlMail(userEmail, "Comanda plasata cu success", htmlBody.toString());
    }



    public String addOrder(OrderRequestDTO dto, Long idUser, Long idCart) {
        AddressRequestDTO deliverydto = dto.getDeliveryAddress();
        AddressRequestDTO invoicedto = dto.getInvoiceAddress();

        Optional<User> u=userRepository.findById(idUser);
        Optional<Cart> c=cartRepository.findById(idCart);

        Optional<Address> deliveryDb = addressRepository.findByStreetAndCityAndPostalCode(
                deliverydto.getStreet(), deliverydto.getCity(), deliverydto.getPostalCode()
        );

        Address delivery=addressMapper.toEntity(deliverydto);
        Address invoice=addressMapper.toEntity(invoicedto);

        if (deliveryDb.isPresent()) {
            delivery = deliveryDb.get();
        } else {
            delivery = addressRepository.save(delivery);
        }

        boolean sameAddress = delivery.equals(invoice);

        if (!sameAddress) {
            Optional<Address> invoiceDb = addressRepository.findByStreetAndCityAndPostalCode(
                    invoicedto.getStreet(), invoicedto.getCity(), invoicedto.getPostalCode());
            if (invoiceDb.isPresent()) {
                invoice = invoiceDb.get();
            } else {
                invoice = addressRepository.save(invoice);
            }
        } else {
            invoice = delivery;
        }


        Order order=new Order();

        order.setCart(c.get());
        order.setUser(u.get());
        order.setPaymentType(PaymentType.valueOf(dto.getPaymentType()));
        order.setOrderDate(LocalDateTime.now());
        order.setTotalPrice(c.get().getTotalPrice());
        order.setDeliveryAddress(delivery);
        order.setInvoiceAddress(invoice);

        orderRepository.save(order);
        this.sendOrderConfirmationEmail(u.get().getEmail(), order);
        return "order salvat cu success!";

    }

    public List<OrderResponseDTO> getOrdersByUser(Long idUser) {
        Optional<User> u=userRepository.findById(idUser);
        List<Order> orders = new ArrayList<>();
        if(u.isPresent()) {
             orders = orderRepository.findAllByUser(u.get());
        }
        return orders.stream()
                .map(orderMapper::toResponseDTO)
                .toList();
    }

    public List<OrderResponseDTO> findAllOrders() {
        List<Order> orders=orderRepository.findAll();

        return orders.stream()
                .map(orderMapper::toResponseDTO)
                .toList();
    }

    public OrderResponseDTO findOrderId(Long id) {
        Optional<Order> o=orderRepository.findById(id);
        if(o.isPresent()){
            return orderMapper.toResponseDTO(o.get());
        }
        else {
            return null;
        }
    }

    public List<OrderResponseDTO> getOrderByFilters(String cat, String searchQuery, LocalDate data) {
        List<Order> orders=orderRepository.findOrdersByFilter(searchQuery, cat, data);

        return orders.stream().map(orderMapper::toResponseDTO).toList();
    }
}
