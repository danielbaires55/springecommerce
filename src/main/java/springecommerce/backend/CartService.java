
package springecommerce.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap; // Thread-safe map per i carrelli

@Service
public class CartService {

    @Autowired
    private ProductRepository productRepository; // Per recuperare i dettagli dei prodotti
    @Autowired
    private UserService userService;             // Per aggiornare il saldo dell'utente

    private final Map<String, Map<Long, CartItem>> userCarts = new ConcurrentHashMap<>();

    // Ottiene il carrello per un dato utente (creandone uno nuovo se non esiste)
    private Map<Long, CartItem> getCartForUser(String userLogin) {
        return userCarts.computeIfAbsent(userLogin, k -> new HashMap<>());
    }

    // Aggiungi un prodotto al carrello
    public CartItem addProductToCart(String userLogin, Long productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("La quantità deve essere maggiore di zero.");
        }

        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            throw new RuntimeException("Prodotto non trovato con ID: " + productId);
        }
        Product product = productOpt.get();

        Map<Long, CartItem> cart = getCartForUser(userLogin);
        CartItem item = cart.getOrDefault(productId, new CartItem(product, 0));
        item.setQuantity(item.getQuantity() + quantity);
        cart.put(productId, item);

        return item;
    }

    // Rimuovi o decrementa la quantità di un prodotto dal carrello
    public void removeProductFromCart(String userLogin, Long productId, int quantityToRemove) {
        Map<Long, CartItem> cart = getCartForUser(userLogin);
        CartItem item = cart.get(productId);

        if (item == null) {
            throw new RuntimeException("Prodotto non presente nel carrello con ID: " + productId);
        }

        if (quantityToRemove <= 0 || quantityToRemove >= item.getQuantity()) {
            cart.remove(productId);
        } else {
            item.setQuantity(item.getQuantity() - quantityToRemove);
            cart.put(productId, item);
        }
    }

    // Visualizza il contenuto del carrello
    public Map<Long, CartItem> getCartContents(String userLogin) {
        return getCartForUser(userLogin);
    }

    // Processa l'acquisto del carrello
    public boolean checkoutCart(String userLogin) {
        User user = userService.findByLogin(userLogin)
                               .orElseThrow(() -> new RuntimeException("Utente non trovato: " + userLogin));

        Map<Long, CartItem> cart = getCartForUser(userLogin);
        if (cart.isEmpty()) {
            return false; // Il carrello è vuoto
        }

        double totalCost = cart.values().stream()
                               .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                               .sum();

        if (user.getBalance() < totalCost) {
            throw new RuntimeException("Credito insufficiente. Saldo disponibile: " + user.getBalance() + ", Costo totale: " + totalCost);
        }

        // Decurtazione del saldo
        user.setBalance(user.getBalance() - totalCost);
        userService.updateUser(user); 

        // Svuota il carrello dopo l'acquisto
        userCarts.remove(userLogin);
        return true;
    }
}