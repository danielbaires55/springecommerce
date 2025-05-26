
package springecommerce.backend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private Product product; // Il prodotto nel carrello
    private int quantity;    // La quantità di quel prodotto
}