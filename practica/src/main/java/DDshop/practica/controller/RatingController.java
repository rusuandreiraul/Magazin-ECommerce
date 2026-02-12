package DDshop.practica.controller;


import DDshop.practica.dto.RatingRequestDTO;
import DDshop.practica.dto.RatingResponseDTO;
import DDshop.practica.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000/")
public class RatingController {


    @Autowired
    RatingService ratingService;

    @PostMapping("/ratings")
    public ResponseEntity<String> addRating(@RequestBody RatingRequestDTO dto){
        String mesaj=ratingService.add(dto);

        return ResponseEntity.ok(mesaj);
    }

    @GetMapping("/ratings/product/{productId}")
    public ResponseEntity<List<RatingResponseDTO>> ratingPerProduct(@PathVariable Long productId) {
        List<RatingResponseDTO> ratings = ratingService.getRatingsProduct(productId);
        if (ratings.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ratings);
    }

}
