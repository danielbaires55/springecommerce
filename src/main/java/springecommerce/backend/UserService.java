// springecommerce/backend/UserService.java

package springecommerce.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private JdbcUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        if (userRepository.findByLogin(user.getLogin()).isPresent()) {
            throw new RuntimeException("Login già in uso: " + user.getLogin());
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getBalance() == null) {
            user.setBalance(100.0);
        }

        return userRepository.save(user);
    }

    public User loginUser(String login, String rawPassword) {
        Optional<User> userOptional = userRepository.findByLogin(login);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(rawPassword, user.getPassword())) {
                return user;
            }
        }
        return null;
    }

    public Optional<User> findUserById(Integer id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    // <<< NUOVO METODO: Aggiungi questo metodo per aggiornare un utente esistente
    public User updateUser(User user) {
        // Quando chiami save() su un'entità che ha già un ID, Spring Data JPA esegue un UPDATE.
        // Non è necessario rifare l'hash della password se l'utente esiste e la password non è cambiata.
        // Se la password potesse essere cambiata da questo metodo, dovremmo gestirlo,
        // ma per ora è solo per aggiornare il saldo.
        return userRepository.save(user);
    }
}