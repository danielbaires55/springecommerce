
package springecommerce.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // Endpoint per aggiungere un prodotto al carrello
    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody Map<String, Object> payload) {
        try {
            String userLogin = (String) payload.get("userLogin");
            Long productId = ((Number) payload.get("productId")).longValue();
            Integer quantity = (Integer) payload.get("quantity");

            if (userLogin == null || productId == null || quantity == null) {
                return new ResponseEntity<>("userLogin, productId e quantity sono obbligatori.", HttpStatus.BAD_REQUEST);
            }

            CartItem addedItem = cartService.addProductToCart(userLogin, productId, quantity);
            return new ResponseEntity<>(addedItem, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); // Es: prodotto non trovato
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante l'aggiunta al carrello: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint per rimuovere un prodotto dal carrello
    @PostMapping("/remove")
    public ResponseEntity<?> removeProduct(@RequestBody Map<String, Object> payload) {
        try {
            String userLogin = (String) payload.get("userLogin");
            Long productId = ((Number) payload.get("productId")).longValue();
            Integer quantityToRemove = (Integer) payload.get("quantityToRemove");

            if (userLogin == null || productId == null || quantityToRemove == null) {
                return new ResponseEntity<>("userLogin, productId e quantityToRemove sono obbligatori.", HttpStatus.BAD_REQUEST);
            }

            cartService.removeProductFromCart(userLogin, productId, quantityToRemove);
            return new ResponseEntity<>("Prodotto rimosso dal carrello.", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante la rimozione dal carrello: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint per visualizzare il contenuto del carrello
    // Richiede il login dell'utente come parametro di path
    @GetMapping("/{userLogin}")
    public ResponseEntity<?> getCart(@PathVariable String userLogin) {
        try {
            Map<Long, CartItem> cart = cartService.getCartContents(userLogin);
            return new ResponseEntity<>(cart.values(), HttpStatus.OK); // Restituisce una lista di CartItem
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante il recupero del carrello: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint per effettuare l'acquisto
    // Richiede il login dell'utente come parametro di path
    @PostMapping("/checkout/{userLogin}")
    public ResponseEntity<?> checkout(@PathVariable String userLogin) {
        try {
            boolean success = cartService.checkoutCart(userLogin);
            if (success) {
                return new ResponseEntity<>("Acquisto completato con successo. Saldo aggiornato.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Carrello vuoto o errore durante l'acquisto.", HttpStatus.BAD_REQUEST);
            }
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); // Credito insufficiente, ecc.
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante il checkout: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}