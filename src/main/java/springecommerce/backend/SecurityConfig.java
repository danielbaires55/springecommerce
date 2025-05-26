// springecommerce/backend/SecurityConfig.java

package springecommerce.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration // Indica a Spring che questa classe contiene definizioni di bean
public class SecurityConfig {

    @Bean // Questo metodo produce un bean che Spring gestirà
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Usiamo BCrypt per l'hashing delle password
    }
}