// springecommerce/backend/ProductRepository.java
package springecommerce.backend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Spring Data JPA fornirà automaticamente i metodi CRUD per l'entità Product.
    // Ad esempio: save(), findById(), findAll(), deleteById(), ecc.
}