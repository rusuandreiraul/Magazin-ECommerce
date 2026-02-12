package DDshop.practica.service;

import DDshop.practica.clase.Product;
import DDshop.practica.clase.Rol;
import DDshop.practica.clase.User;
import DDshop.practica.clase.Address;
import DDshop.practica.dto.*;
import DDshop.practica.mapper.AddressMapper;
import DDshop.practica.mapper.FavoritesMapper;
import DDshop.practica.mapper.UserMapper;
import DDshop.practica.repository.AddressRepository;
import DDshop.practica.repository.ProductRepository;
import DDshop.practica.repository.UserRepository;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import org.hibernate.id.factory.IdentifierGeneratorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    UserMapper userMapper;

    @Autowired
    AddressMapper addressMapper;

    @Autowired
    FavoritesMapper favoritesMapper;

    @Autowired
    ProductRepository productRepository;


    @Autowired
    private EmailService emailService;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void addUser(UserRequestDTO dto){
        User u=userMapper.toEntity(dto);
        this.saveUser(u);
    }



    public void sendPasswordResetEmail(String userEmail) {
            String resetLink = "http://localhost:3000/user/reset-password?email=" + userEmail;
            String subject = "Resetare parola";
            String message = "Click pe link pentru resetare parola:\n" + resetLink;

            emailService.sendMail(userEmail, subject, message);
    }

    public UserResponseDTO updateUser(UserRequestDTO u) {
        if (u.getEmail() == null || u.getEmail().isBlank()) {
            throw new IllegalArgumentException("Emailul este necesar pentru actualizare");
        }

        User uExistent = userRepository.findByEmail(u.getEmail());
        if (uExistent == null) {
            return null;
        }


        if (u.getPhoneNumber() != null && !u.getPhoneNumber().isBlank()) {
            uExistent.setPhoneNumber(u.getPhoneNumber());
        }

        if (u.getDefaultDeliveryAddress() != null) {
            Address delivery = addressMapper.toEntity(u.getDefaultDeliveryAddress());

            Optional<Address> deliveryDb = addressRepository.findByStreetAndCityAndPostalCode(
                    delivery.getStreet(), delivery.getCity(), delivery.getPostalCode()
            );

            if (deliveryDb.isPresent()) {
                delivery = deliveryDb.get();
            } else {
                delivery = addressRepository.save(delivery);
            }

            uExistent.setDefaultDeliveryAddress(delivery);


            if (u.getDefaultBillingAddress() != null) {
                Address billing = addressMapper.toEntity(u.getDefaultBillingAddress());

                boolean sameAddress = delivery.equals(billing);

                if (!sameAddress) {
                    Optional<Address> billingDb = addressRepository.findByStreetAndCityAndPostalCode(
                            billing.getStreet(), billing.getCity(), billing.getPostalCode()
                    );

                    if (billingDb.isPresent()) {
                        billing = billingDb.get();
                    } else {
                        billing = addressRepository.save(billing);
                    }
                } else {
                    billing = delivery;
                }

                uExistent.setDefaultBillingAddress(billing);
            }
        } else if (u.getDefaultBillingAddress() != null) {
            Address billing = addressMapper.toEntity(u.getDefaultBillingAddress());

            Optional<Address> billingDb = addressRepository.findByStreetAndCityAndPostalCode(
                    billing.getStreet(), billing.getCity(), billing.getPostalCode()
            );

            if (billingDb.isPresent()) {
                billing = billingDb.get();
            } else {
                billing = addressRepository.save(billing);
            }

            uExistent.setDefaultBillingAddress(billing);
        }

        userRepository.save(uExistent);
        return userMapper.toResponseDTO(uExistent);
    }

    public String registerUser(UserRequestDTO u) {
        User existent = userRepository.findByEmail(u.getEmail());

        if (existent != null) {
            return "Userul exista deja in baza de date, incearca sa te loghezi";
        } else {

            Address delivery = addressMapper.toEntity(u.getDefaultDeliveryAddress());
            Address billing = addressMapper.toEntity(u.getDefaultBillingAddress());

            Optional<Address> deliveryDb = addressRepository.findByStreetAndCityAndPostalCode(
                    delivery.getStreet(), delivery.getCity(), delivery.getPostalCode()
            );

            if (deliveryDb.isPresent()) {
                delivery = deliveryDb.get();
            } else {
                delivery = addressRepository.save(delivery);
            }

            boolean sameAddress = delivery.equals(billing);

            if (!sameAddress) {
                Optional<Address> billingDb = addressRepository.findByStreetAndCityAndPostalCode(
                        billing.getStreet(), billing.getCity(), billing.getPostalCode());
                if (billingDb.isPresent()) {
                    billing = billingDb.get();
                } else {
                    billing = addressRepository.save(billing);
                }
            } else {
                billing = delivery;
            }

            User newUser = new User();
            newUser.setFirstName(u.getFirstName());
            newUser.setLastName(u.getLastName());
            newUser.setEmail(u.getEmail());
            newUser.setPhoneNumber(u.getPhoneNumber());
            String encodedPassword=passwordEncoder.encode(u.getPassword());
            newUser.setPassword(encodedPassword);
            newUser.setDefaultDeliveryAddress(delivery);
            newUser.setDefaultBillingAddress(billing);
            newUser.setRol(Rol.CLIENT);


            userRepository.save(newUser);

            return "User inregistrat cu succes!";
        }
    }


    public boolean userResetPassword(Map<String, String> request) {
        String email=request.get("email");
        String pass1=request.get("password1");
        String pass2=request.get("password2");

        if(!pass1.equals(pass2)){
           return false;
        }
        else{
            User u=userRepository.findByEmail(email);
            if(u!=null){
                String encoded=passwordEncoder.encode(pass1);
                u.setPassword(encoded);
                this.saveUser(u);
                return true;
            }
            else{
                return false;
            }
        }
    }

    public UserResponseDTO loginUser(Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        User u=userRepository.findByEmail(email);
        if(u!=null && passwordEncoder.matches(password, u.getPassword())){
           return userMapper.toResponseDTO(u);
        }
        return null;
    }

    public boolean forgotPassword(String email) {
        User u=userRepository.findByEmail(email);
        if(u!=null){
            this.sendPasswordResetEmail(email);
            return true;
        }
        else{
            return false;
        }
    }

    public UserResponseDTO updateToAdmin(Map<String, String> emailCredential) {
        String email=emailCredential.get("email");

        User u=userRepository.findByEmail(email);
        if(u!=null && u.getRol()!= Rol.ADMIN){
            u.setRol(Rol.ADMIN);
            userRepository.save(u);
            return userMapper.toResponseDTO(u);
        }
        else return null;
    }

    public void saveToFavorites(Long id_user, Long id_prod) {
        Optional<Product> p=productRepository.findById(id_prod);
        Optional<User> u=userRepository.findById(id_user);
        if(p.isPresent() && u.isPresent()){
            u.get().getFavoriteProducts().add(p.get());
            userRepository.save(u.get());
        }
    }

    public void removeFromFavorites(Long idUser, Long idProd) {
        Optional<Product> product=productRepository.findById(idProd);
        Optional<User> u=userRepository.findById(idUser);

        if(product.isPresent() && u.isPresent()){
            u.get().getFavoriteProducts().remove(product.get());
            userRepository.save(u.get());
        }
    }

    public Set<FavoritesResponseDTO> getFavorites(Long idUser) {
        Optional<User> user = userRepository.findById(idUser);
        return user.get().getFavoriteProducts().stream()
                .map(favoritesMapper::toFavoritesResponseDTO)
                .collect(Collectors.toSet());
    }
}
