package DDshop.practica.service;


import DDshop.practica.clase.Category;
import DDshop.practica.dto.CategoryResponseDTO;
import DDshop.practica.mapper.CategoryMapper;
import DDshop.practica.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CategoryMapper mapper;

    public List<CategoryResponseDTO> getAllCategories(){

        List<Category> categories=categoryRepository.findAll();

        return categories.stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());

    }

}
