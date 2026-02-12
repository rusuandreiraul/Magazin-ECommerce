package DDshop.practica.controller;

import DDshop.practica.clase.Product;
import DDshop.practica.clase.User;
import DDshop.practica.dto.FavoritesResponseDTO;
import DDshop.practica.dto.UserRequestDTO;
import DDshop.practica.dto.UserResponseDTO;
import DDshop.practica.repository.AddressRepository;
import DDshop.practica.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@CrossOrigin(origins = "http://localhost:3000/")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AddressRepository addressRepository;


    @GetMapping("/users")
    public List<User> getUser(){
        return userService.getAllUsers();
    }

        //----------------------INREGISTRARE SI LOGARE------------------------------
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        UserResponseDTO dto=userService.loginUser(credentials);

        if (dto!=null) {
            return ResponseEntity.ok(dto);
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email sau parola incorecta");
        }
    }



    @PostMapping("/register")
    public String registerUser(@RequestBody UserRequestDTO u){
        return userService.registerUser(u);

    }

    @PutMapping("/changeData")
    public ResponseEntity<?> updateUser(@RequestBody UserRequestDTO u){
        UserResponseDTO dto= userService.updateUser(u);

        if(dto!=null) {
            ResponseEntity.ok(dto);
        }
            return null;

    }
    //---------------RESETARE PAROLA-------------------
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> email){
       boolean found= userService.forgotPassword(email.get("email"));
       if(found){
           return  ResponseEntity.ok("email trimis cu success, verifica posta");
       }
       else{
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("emailul introdus nu a fost gasit in baza de date , introdu un mail valid");
       }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
       boolean success=userService.userResetPassword(request);

       if(success){
           return  ResponseEntity.ok("parola modificata cu success");
       }
       else{
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Parola nu a putut fii resetata verifica daca cele doua parole sunt identice");
       }
    }

    //-------------------UPDATE LA ADMIN-------------------------

    @PutMapping("update-admin")
    public UserResponseDTO updateToAdmin(@RequestBody Map<String, String> email){
        return userService.updateToAdmin(email);
    }

     // ----------------ADAUGARE FAVORITE----------------
    @PostMapping("/favorites/{id_user}/{id_prod}")
    public void addToFavorites(@PathVariable Long id_user, @PathVariable Long id_prod){
         userService.saveToFavorites(id_user, id_prod);
    }

    @DeleteMapping("/favorites/{id_user}/{id_prod}")
    public void deleteFromFavorites(@PathVariable Long id_user, @PathVariable Long id_prod){
        userService.removeFromFavorites(id_user, id_prod);
    }

    @GetMapping("/favorites/{idUser}")
    public ResponseEntity<?> getFavorites(@PathVariable Long idUser) {
        Set<FavoritesResponseDTO> favorites = userService.getFavorites(idUser);

        if (favorites.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Utilizatorul nu are produse favorite sau nu a fost găsit.");
        }

        return ResponseEntity.ok(favorites);
    }

}
