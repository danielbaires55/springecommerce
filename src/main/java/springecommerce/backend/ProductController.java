// springecommerce/backend/ProductController.java

package springecommerce.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController // Indica a Spring che questa classe è un controller REST
@RequestMapping("/api/products") // Definisce il percorso base per gli endpoint in questo controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository; // Inietta il tuo JpaRepository per i prodotti

    // Endpoint per listare tutti i prodotti a catalogo
    @GetMapping // Questo metodo gestirà le richieste HTTP GET all'URL /api/products
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productRepository.findAll(); // Usa findAll() fornito da JpaRepository
        return ResponseEntity.ok(products); // Restituisce una lista di prodotti con stato 200 OK
    }
}