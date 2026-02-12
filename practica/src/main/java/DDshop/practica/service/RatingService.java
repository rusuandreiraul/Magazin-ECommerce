package DDshop.practica.service;

import DDshop.practica.clase.Product;
import DDshop.practica.clase.Rating;
import DDshop.practica.dto.RatingRequestDTO;
import DDshop.practica.dto.RatingResponseDTO;
import DDshop.practica.mapper.RatingMapper;
import DDshop.practica.repository.ProductRepository;
import DDshop.practica.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class RatingService {

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    RatingMapper ratingMapper;

    public String add(RatingRequestDTO dto) {
        Rating r=ratingMapper.toEntity(dto);

        ratingRepository.save(r);
        return "Rating-ul tau a fost inregistrat cu succes! Multumim pentru feedback";

    }

    public List<RatingResponseDTO> getRatingsProduct(Long productId) {
        Optional<Product> p = productRepository.findById(productId);

        if (p.isEmpty()) {
            return Collections.emptyList();
        }

        List<Rating> ratings = ratingRepository.findByProduct(p.get());

        return ratings.stream()
                .map(ratingMapper::toDTO)
                .toList();
    }

}
