package DDshop.practica.service;


import DDshop.practica.clase.Category;
import DDshop.practica.clase.Image;
import DDshop.practica.clase.Product;
import DDshop.practica.clase.Product_Attribute;
import DDshop.practica.dto.ProductRequestDTO;
import DDshop.practica.dto.ProductResponseDTO;
import DDshop.practica.dto.Product_AttributeRequestDTO;
import DDshop.practica.mapper.ProductMapper;
import DDshop.practica.repository.CategoryRepository;
import DDshop.practica.repository.ImageRepository;
import DDshop.practica.repository.ProductRepository;
import DDshop.practica.repository.Product_AttributeRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    ImageRepository imageRepository;


    @Autowired
    Product_AttributeRepository product_attributeRepository;

    public List<ProductResponseDTO> getAllProduse() {

        List<Product> products=productRepository.findAll();

        return products.stream()
                .map(productMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public void saveProduct(Product p) {
        Long catId = p.getCategory().getId();
        Optional<Category> cat = categoryRepository.findById(catId);

        if (cat.isPresent()) {
            p.setCategory(cat.get());
        } else {
            throw new RuntimeException("Error, nu exista categoria respectiva");
        }
        productRepository.save(p);
    }

    public List<ProductResponseDTO> getProductsByCategory(Long idCat) {
        List<Product> products = productRepository.findByCategoryId(idCat);
        return products.stream()
                .map(productMapper::toResponseDTO)
                .collect(Collectors.toList());
    }



    public boolean addProduct(ProductRequestDTO dto) {
        Product p=productRepository.findByName(dto.getName());
        if(p!=null){
            return false;
        }
        if(dto.getPrice()<0 || dto.getAvailableQuantity()<0){
            return false;
        }
        else{
            Category c=categoryRepository.findByName(dto.getCategory());
            if(c==null){
                Category newCategory=new Category();
                newCategory.setName(dto.getCategory());
                categoryRepository.save(newCategory);
                c=newCategory;
            }
            List<Image> images=new ArrayList<>();
            List<Product_Attribute>productAttributes=new ArrayList<>();
            Product product=new Product();
            product.setName(dto.getName());
            product.setPrice(dto.getPrice());
            product.setDescription(dto.getDescription());
            product.setAvailableQuantity(dto.getAvailableQuantity());
            product.setAddedDate(LocalDateTime.now());
            product.setCategory(c);
            for(Product_AttributeRequestDTO attribute:dto.getAttributes()){
                Product_Attribute pa=product_attributeRepository.findByNameAndValue(attribute.getName(), attribute.getValue());
                if (pa != null) {
                    productAttributes.add(pa);
                } else {
                    pa = new Product_Attribute();
                    pa.setName(attribute.getName());
                    pa.setValue(attribute.getValue());

                    product_attributeRepository.save(pa);
                    productAttributes.add(pa);
                }
            }

            product.setAttributes(productAttributes);

            productRepository.save(product);

            for(String image : dto.getImagesUrl()){
                Image i=imageRepository.findByUrl(image);
                if (i != null) {
                    images.add(i);
                } else {
                    i = new Image();
                    i.setUrl(image);
                    i.setProduct(product);
                    imageRepository.save(i);
                    images.add(i);
                }
            }

            product.setImages(images);

            productRepository.save(product);

            return true;
        }
    }

    public ProductResponseDTO getProductById(Long id) {
        Optional<Product> p=productRepository.findById(id);
        return p.map(product -> productMapper.toResponseDTO(product)).orElse(null);
    }

   public List<ProductResponseDTO> getProductsByFilter(String filter) {
        List<Product> sortedProductPrice;
        if(filter.equals("ascending")) {
           sortedProductPrice= productRepository.findAllByOrderByPriceAsc();
        }
        else{
            sortedProductPrice = productRepository.findAllByOrderByPriceDesc();
        }

        return sortedProductPrice.stream()
                .map(productMapper::toResponseDTO)
                .toList();
    }

    public List<ProductResponseDTO> getProductsByCategoryAndFilter(Long idCat, String filter) {
        List<Product> products;

        if(filter.equals("ascending")){
            products=productRepository.findByCategoryIdOrderByPriceAsc(idCat);
        }
        else{
            products=productRepository.findByCategoryIdOrderByPriceDesc(idCat);
        }
        return products.stream()
                .map(productMapper::toResponseDTO)
                .toList();
    }


    public void editProduct(ProductRequestDTO dto) {
        Product p = productRepository.findByName(dto.getName());
        if (p == null) throw new RuntimeException("Product not found");

        p.setDescription(dto.getDescription());
        p.setAvailableQuantity(dto.getAvailableQuantity());
        p.setPrice(dto.getPrice());
        if(dto.getDiscountPrice()!=null)
            p.setDiscountPrice(dto.getDiscountPrice());

        for (Product_AttributeRequestDTO pa : dto.getAttributes()) {
            Product_Attribute existing = product_attributeRepository.findByNameAndValue(pa.getName(), pa.getValue());

            if (existing == null) {
                existing = new Product_Attribute();
                existing.setName(pa.getName());
                existing.setValue(pa.getValue());
                product_attributeRepository.save(existing);
            }


            if (!p.getAttributes().contains(existing)) {
                p.getAttributes().add(existing);
            }
        }
        for (String url : dto.getImagesUrl()) {
            Image image = imageRepository.findByUrl(url);
            if (image == null) {
                image = new Image();
                image.setUrl(url);
                image.setProduct(p); // doar dacă e necesar
                imageRepository.save(image);
            }

            boolean alreadyExists = p.getImages().stream()
                    .anyMatch(img -> img.getUrl().equals(url));
            if (!alreadyExists) {
                p.getImages().add(image);
            }
        }
        productRepository.save(p);
    }

    public List<ProductResponseDTO> getNewest() {
        List<Product> products=productRepository.findTop4ByOrderByAddedDateDesc();

        return products.stream()
                .map(productMapper::toResponseDTO)
                .toList();
    }

    /*
    public List<ProductResponseDTO> filterProductsByAttributes(List<ProductResponseDTO> products, List<Product_Attribute> attributes) {
        List<ProductResponseDTO> productsFinal = new ArrayList<>();

        for (ProductResponseDTO p : products) {
            for (Product_Attribute pa : attributes) {
                boolean found = p.getAttributes().stream().anyMatch(attr ->
                        (
                                attr.getName().equals(pa.getName()) && attr.getValue().equals(pa.getValue())
                        )
                );
                if (found) {
                    productsFinal.add(p);
                    break;
                }
            }
        }
        return productsFinal;
    }

    */

    public List<ProductResponseDTO> getProductsByDynamicFilters(Long idCat, String filter, List<Product_Attribute> attributes) {
        List<Product> products=productRepository.findProductsByFilters(idCat, filter, attributes);
        return products.stream().map(productMapper::toResponseDTO).toList();
    }

    public List<ProductResponseDTO> getPopular() {
       List<Product> products= productRepository.findPopularProducts();

       return products.stream().map(productMapper::toResponseDTO).toList();

    }

    public Map<String, Integer> readCSV(MultipartFile file) throws IOException {
        boolean firstLine = true;
        int successCount = 0;
        int failedCount = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                try {
                    String[] val = line.split(",");

                    if (val.length < 7) {
                        failedCount++;
                        continue;
                    }

                    String name = val[0].trim();
                    String description = val[1].trim();
                    double price = Double.parseDouble(val[2].trim());
                    int quantity = Integer.parseInt(val[3].trim());
                    String category = val[4].trim();

                    List<Product_AttributeRequestDTO> attributes = Arrays.stream(val[5].split(";"))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty() && s.contains(":"))
                            .map(attr -> {
                                String[] parts = attr.split(":", 2);
                                return new Product_AttributeRequestDTO(parts[0].trim(), parts[1].trim());
                            })
                            .collect(Collectors.toList());

                    List<String> images = Arrays.stream(val[6].split(";"))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .collect(Collectors.toList());

                    // Validare minimă: nume și imagini obligatorii
                    if (name.isEmpty() || description.isEmpty() || price<0) {
                        failedCount++;
                        continue;
                    }

                    ProductRequestDTO dto = new ProductRequestDTO(
                            name,
                            description,
                            price,
                            quantity,
                            LocalDateTime.now(),
                            category,
                            attributes,
                            images
                    );


                     this.addProduct(dto);

                    successCount++;

                } catch (Exception e) {
                    failedCount++;
                }
            }
        }

        Map<String, Integer> result = new HashMap<>();
        result.put("success", successCount);
        result.put("failed", failedCount);
        return result;
    }


    public List<ProductResponseDTO> getPromo() {
        List<Product> produsePromo=productRepository.findByPromo(true);

        return produsePromo.stream()
                .map(productMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public void setPromoProducts(List<Long> idsToPromote) {
        productRepository.resetAllPromoted();

        if (!idsToPromote.isEmpty()) {
            productRepository.setPromotedForIds(idsToPromote);
        }
    }


    public void setDiscountPrice(Map<String, Object> produsSelectat) {
        Long id=((Number)produsSelectat.get("id")).longValue();
        Double newPrice=((Number)produsSelectat.get("newPrice")).doubleValue();

        Optional<Product> p=productRepository.findById(id);

        if(p.isPresent()){
            p.get().setDiscountPrice(newPrice);
            productRepository.save(p.get());
        }
    }
}
