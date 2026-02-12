package DDshop.practica.controller;

import DDshop.practica.clase.Category;
import DDshop.practica.clase.Product;
import DDshop.practica.clase.Product_Attribute;
import DDshop.practica.dto.ProductRequestDTO;
import DDshop.practica.dto.ProductResponseDTO;
import DDshop.practica.service.ProductService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "http://localhost:3000/")
public class ProductController {


    @Autowired
    ProductService productService;

    @PostMapping("/add-product")
    public ResponseEntity<String> addProduct(@RequestBody ProductRequestDTO dto){
        try {
            if(productService.addProduct(dto)){
                return ResponseEntity.ok("Produs adaugat cu success in baza de date");
            }
            else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ceva nu a functionat cum trebuie");
            }
        } catch (Exception e) {
            e.printStackTrace();  // aici vei vedea în consola backend ce e în neregulă
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Eroare server: " + e.getMessage());
        }
    }

    @GetMapping("/products/new")
    public List<ProductResponseDTO> getNewestProducts(){
        return productService.getNewest();
    }

    @GetMapping("products/popular")
    public List<ProductResponseDTO> getMostPopular(){
        return productService.getPopular();
    }

    @PostMapping("/promo-products")
    public ResponseEntity<?> setProdusePromo(@RequestBody List<Long> produseSelectate){
        productService.setPromoProducts(produseSelectate);
        return ResponseEntity.ok("Produsele promovate au fost actualizate");
    }

    @PostMapping("/discount-product")
    public ResponseEntity<?> setProdusePromo(@RequestBody Map<String, Object> produsSelectat){
        productService.setDiscountPrice(produsSelectat);
        return ResponseEntity.ok("Produsul are discount acum!");
    }



    @GetMapping("/promo")
    public List<ProductResponseDTO> getPromoProducts(){
        return productService.getPromo();
    }

    @PutMapping("/edit-product")
    public ResponseEntity<?> editProduct(@RequestBody ProductRequestDTO dto) {
        try {
            productService.editProduct(dto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Eroare la editare produs");
        }
    }


    @GetMapping("/products")
    public ResponseEntity<?> getProduct(@RequestParam(required = false) Long id_cat, @RequestParam(required = false) String filter, @RequestParam(required = false) String attr)
    {
        List<Product_Attribute> attributes = new ArrayList<>();
        if (attr != null && !attr.isEmpty()) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                attributes = mapper.readValue(attr, new TypeReference<List<Product_Attribute>>() {});
            } catch (Exception e) {
                e.printStackTrace();

                return ResponseEntity.badRequest().body("Eroare");
            }
        }

        List<ProductResponseDTO> products = productService.getProductsByDynamicFilters(id_cat, filter, attributes);
        return ResponseEntity.ok(products);
    }



    @GetMapping("/products/category/{idCat}")
    public List<ProductResponseDTO> getProductsCategory(@PathVariable Long idCat){
        return productService.getProductsByCategory(idCat);
    }

    @GetMapping("products/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id){
        ProductResponseDTO dto= productService.getProductById(id);

        if(dto!=null){
            return ResponseEntity.ok(dto);
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Produsul nu a fost gasit");
        }
    }

    @GetMapping("/products/filter/{filter}")
    public ResponseEntity<?> getProductFiltered(@PathVariable String filter){
        List<ProductResponseDTO> dto= productService.getProductsByFilter(filter);
        if (!dto.isEmpty()) {
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }


    @PostMapping("/admin/import-csv")
    public ResponseEntity<?> importCSV(@RequestParam("file") MultipartFile file) throws IOException { //multipartFile folosit pentru citirea de fisiere
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Fișierul este gol sau lipsă."));
        }


        Map<String, Integer> result= productService.readCSV(file);

       return ResponseEntity.ok(result);
    }



}
