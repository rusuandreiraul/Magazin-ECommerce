package DDshop.practica.dao;

import DDshop.practica.clase.*;
import DDshop.practica.clase.Order;
import DDshop.practica.repository.OrderRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.apache.commons.lang3.reflect.Typed;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Order> findOrdersByFilter(String text, String category, LocalDate data) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);

        Root<Order> root = cq.from(Order.class);
        List<Predicate> predicates = new java.util.ArrayList<>();

        if (text != null && !text.isEmpty()) {
            Join<Order, Cart> joinCart = root.join("cart");
            Join<Cart, Cart_Entry> joinEntry = joinCart.join("cartEntries");
            Join<Cart_Entry, Product> joinProduct = joinEntry.join("product");
            Predicate nameMatch = cb.like(cb.lower(joinProduct.get("name")), "%" + text.toLowerCase() + "%");
            Predicate userMatch = cb.like(cb.lower(root.get("user").get("firstName")), "%" + text.toLowerCase() + "%");

            predicates.add(cb.or(nameMatch, userMatch));
        }

        if (category != null && !category.isEmpty()) {
            Join<Order, Cart> joinCart = root.join("cart");
            Join<Cart, Cart_Entry> joinEntry = joinCart.join("cartEntries");
            Join<Cart_Entry, Product> joinProduct = joinEntry.join("product");
            Join<Product, Category> joinCategory = joinProduct.join("category");
            predicates.add(cb.equal(joinCategory.get("name"), category));
        }

        if (data != null) {
            LocalDateTime startOfDay = data.atStartOfDay();
            LocalDateTime endOfDay = data.atTime(LocalTime.MAX);
            predicates.add(cb.between(root.get("orderDate"), startOfDay, endOfDay));
        }

        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        TypedQuery<Order> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

}
