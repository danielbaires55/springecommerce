
package springecommerce.backend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JdbcUserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByLogin(String login);
}