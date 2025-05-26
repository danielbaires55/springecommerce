
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

    public User updateUser(User user) {
        return userRepository.save(user);
    }
}