
package springecommerce.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map; 

@RestController 
@RequestMapping("/api/users") 
public class UserController {

    @Autowired
    private UserService userService; 

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user);
            registeredUser.setPassword(null);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED); 
        } catch (RuntimeException e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            
            return new ResponseEntity<>("Errore durante la registrazione: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> credentials) {
        String login = credentials.get("login");
        String password = credentials.get("password");

        if (login == null || password == null) {
            return new ResponseEntity<>("Login e password sono obbligatori.", HttpStatus.BAD_REQUEST);
        }

        User user = userService.loginUser(login, password);

        if (user != null) {
            user.setPassword(null); 
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            // Login fallito
            return new ResponseEntity<>("Credenziali non valide.", HttpStatus.UNAUTHORIZED); // Codice 401 Unauthorized
        }
    }
}