package DDshop.practica.dao;

import DDshop.practica.clase.Cart_Entry;
import DDshop.practica.clase.Product;
import DDshop.practica.clase.Cart;
import DDshop.practica.clase.Order;
import DDshop.practica.clase.Product_Attribute;

import DDshop.practica.repository.ProductRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.hibernate.engine.transaction.jta.platform.internal.JOTMJtaPlatform;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<Product> findProductsByFilters(Long categoryId, String filter, List<Product_Attribute> attributes) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Product> product = cq.from(Product.class);

        List<Predicate> predicates = new ArrayList<>();


        if (categoryId != null) {
            predicates.add(cb.equal(product.get("category").get("id"), categoryId));
        }


        if (attributes != null && !attributes.isEmpty()) {
            Join<Product, Product_Attribute> attrJoin = product.join("attributes", JoinType.INNER);
            List<Predicate> orPredicates = new ArrayList<>();

            for (Product_Attribute pa : attributes) {
                if ("Pagini".equalsIgnoreCase(pa.getName())) {
                    Integer min = null;
                    Integer max = null;

                    switch (pa.getValue()) {
                        case "<100":
                            max = 100;
                            break;
                        case "100-300":
                            min = 100;
                            max = 300;
                            break;
                        case "300-500":
                            min = 300;
                            max = 500;
                            break;
                        case ">500":
                            min = 500;
                            break;
                    }

                    Expression<String> valueStr = attrJoin.get("value");

                    try {
                        if (min != null && max != null) {
                            Predicate p = cb.and(
                                    cb.equal(attrJoin.get("name"), pa.getName()),
                                    cb.greaterThanOrEqualTo(valueStr, String.valueOf(min)),
                                    cb.lessThanOrEqualTo(valueStr, String.valueOf(max))
                            );
                            orPredicates.add(p);
                        } else if (min != null) {
                            Predicate p = cb.and(
                                    cb.equal(attrJoin.get("name"), pa.getName()),
                                    cb.greaterThanOrEqualTo(valueStr, String.valueOf(min))
                            );
                            orPredicates.add(p);
                        } else if (max != null) {
                            Predicate p = cb.and(
                                    cb.equal(attrJoin.get("name"), pa.getName()),
                                    cb.lessThanOrEqualTo(valueStr, String.valueOf(max))
                            );
                            orPredicates.add(p);
                        }
                    } catch (NumberFormatException e) {

                    }

                } else {
                    // Atribute obișnuite
                    Predicate p = cb.and(
                            cb.equal(attrJoin.get("name"), pa.getName()),
                            cb.equal(attrJoin.get("value"), pa.getValue())
                    );
                    orPredicates.add(p);
                }
            }

            if (!orPredicates.isEmpty()) {
                predicates.add(cb.or(orPredicates.toArray(new Predicate[0])));
            }

            cq.groupBy(product.get("id"));
            cq.having(cb.equal(cb.countDistinct(attrJoin.get("id")), attributes.size()));
        }


        if (filter != null) {
            //folosit pentru a lua discoutnPrice si daca nu exista sa se folosesaca direct priceul normal, e ca un operator ternar pracitc
            Expression<Number> effectivePrice = cb.coalesce(product.get("discountPrice"), product.get("price"));

            if (filter.equalsIgnoreCase("ascending")) {
                cq.orderBy(cb.asc(effectivePrice));
            } else if (filter.equalsIgnoreCase("descending")) {
                cq.orderBy(cb.desc(effectivePrice));
            }
        }



        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        TypedQuery<Product> query = entityManager.createQuery(cq);
        return query.getResultList();
    }



    @Override
    public List<Product> findPopularProducts(){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> cq = cb.createQuery(Product.class);
        Root<Order> root = cq.from(Order.class);

        Join<Order, Cart> joinCart=root.join("cart");
        Join<Cart, Cart_Entry> joinEntry=joinCart.join("cartEntries");
        Join<Cart_Entry, Product> joinProduct=joinEntry.join("product");



        cq.groupBy(joinProduct.get("id"));

        cq.orderBy(cb.desc(cb.sum(joinEntry.get("quantity"))));

        cq.select(joinProduct);

       TypedQuery<Product> query=entityManager.createQuery(cq);
       query.setMaxResults(8);
       return query.getResultList();

    }





}
