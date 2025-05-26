// springecommerce/backend/JdbcUserRepository.java

package springecommerce.backend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JdbcUserRepository extends JpaRepository<User, Integer> { // <<< QUI: Cambiato da String a Integer!
    // Il metodo findByLoginAndPassword(String login, String password) è obsoleto
    // perché la verifica della password sarà gestita dal PasswordEncoder nel UserService.
    // Puoi rimuoverlo o commentarlo per chiarezza, dato che non verrà più usato.
    // User findByLoginAndPassword(String login, String password);

    Optional<User> findByLogin(String login);
}