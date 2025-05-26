// springecommerce/backend/UserController.java

package springecommerce.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map; // Per gestire il payload del login

@RestController // Indica a Spring che questa classe è un controller REST
@RequestMapping("/api/users") // Definisce il percorso base per gli endpoint in questo controller
public class UserController {

    @Autowired
    private UserService userService; // Inietta il servizio che abbiamo creato

    // Endpoint per la registrazione di un nuovo utente
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user);
            // Non restituire la password hashata per sicurezza
            registeredUser.setPassword(null);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED); // Codice 201 Created
        } catch (RuntimeException e) {
            // Se il login è già in uso, restituisci un errore 409 Conflict
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            // Per altri errori generici, restituisci un errore 500 Internal Server Error
            return new ResponseEntity<>("Errore durante la registrazione: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint per il login di un utente
    // Utilizziamo Map<String, String> per un payload JSON semplice
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> credentials) {
        String login = credentials.get("login");
        String password = credentials.get("password");

        if (login == null || password == null) {
            return new ResponseEntity<>("Login e password sono obbligatori.", HttpStatus.BAD_REQUEST);
        }

        User user = userService.loginUser(login, password);

        if (user != null) {
            // Login riuscito: restituisci l'utente (senza password)
            user.setPassword(null); // Rimuovi la password per sicurezza
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            // Login fallito
            return new ResponseEntity<>("Credenziali non valide.", HttpStatus.UNAUTHORIZED); // Codice 401 Unauthorized
        }
    }
}