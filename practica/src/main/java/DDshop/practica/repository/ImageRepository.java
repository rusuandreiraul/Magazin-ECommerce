package DDshop.practica.repository;


import DDshop.practica.clase.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {


    Image findByUrl(String image);
}
