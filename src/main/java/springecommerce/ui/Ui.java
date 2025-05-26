
package springecommerce.ui;

import java.util.Scanner;
import java.util.Map; 


import lombok.extern.slf4j.Slf4j;

import springecommerce.backend.Product;
import springecommerce.backend.ProductRepository;
import springecommerce.backend.User;
import springecommerce.backend.JdbcUserRepository;
import springecommerce.backend.UserService;
import springecommerce.backend.CartService; 
import springecommerce.backend.CartItem; 

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Component
public class Ui {
    @Autowired
    JdbcUserRepository userRepo;

    @Autowired
    ProductRepository productRepo;

    @Autowired
    UserService userService;

    @Autowired 
    CartService cartService;

    private Scanner s = new Scanner(System.in);

    private void print(String s) {
        System.out.println(s);
    }

    private int readInt(String prompt) {
        System.out.print(prompt + ":");
        while (!s.hasNextInt()) {
            System.out.println("Input non valido. Inserisci un numero.");
            s.next();
            System.out.print(prompt + ":");
        }
        int value = s.nextInt();
        s.nextLine();
        return value;
    }

    private double readDouble(String prompt) {
        System.out.print(prompt + ":");
        while (!s.hasNextDouble()) {
            System.out.println("Input non valido. Inserisci un numero decimale (usa la virgola per i decimali se il tuo locale lo richiede, o il punto).");
            s.next();
            System.out.print(prompt + ":");
        }
        double value = s.nextDouble();
        s.nextLine(); 
        return value;
    }

    private String readString(String prompt) {
        System.out.print(prompt + ":");
        return s.nextLine();
    }

    private User login() {
        String login = readString("login");
        String pass = readString("password");
        return userService.loginUser(login, pass);
    }

    // Aggiungi le nuove costanti per le opzioni del menu
    public static final int LOGOUT = 0, PRINT_PRODUCTS = 1, ADD_PRODUCT = 2,
                            ADD_TO_CART = 3, VIEW_CART = 4, CHECKOUT = 5;
    User u = null;

    private int menu() {
        // Recupera il saldo più recente dell'utente loggato
        if (u != null) {
            u = userService.findByLogin(u.getLogin()).orElse(u); 
        }

        String fullName = (u.getName() != null && u.getSurname() != null) ?
                           (u.getName() + " " + u.getSurname()) : u.getLogin();
        print("\n--- Utente: " + fullName + " | Saldo: " + String.format("%.2f", u.getBalance()) + " ---"); 
        return readInt("1) print products\n2) add product\n3) add to cart\n4) view cart\n5) checkout\n0) LOGOUT"); 
    }

    private void printProducts() {
        print("\n--- Catalogo Prodotti ---");
        Iterable<Product> products = productRepo.findAll();
        if (!products.iterator().hasNext()) {
            print("Nessun prodotto disponibile nel catalogo.");
            return;
        }
        for (Product p : products) {
            print("ID: " + p.getId() + ", Nome: " + p.getName() + ", Prezzo: " + p.getPrice());
        }
    }

    private void addProduct() {
        String name = readString("name");
        double price = readDouble("price"); 
        productRepo.save(new Product(null, name, price));
        print("Prodotto aggiunto con successo!");
    }

    private void addToCart() {
        if (u == null) {
            print("Devi essere loggato per aggiungere prodotti al carrello.");
            return;
        }
        printProducts();
        int productId = readInt("Inserisci l'ID del prodotto da aggiungere al carrello");
        int quantity = readInt("Inserisci la quantità");

        try {
            cartService.addProductToCart(u.getLogin(), (long) productId, quantity);
            print("Prodotto aggiunto al carrello con successo.");
        } catch (RuntimeException e) {
            print("Errore: " + e.getMessage());
        }
    }

    private void viewCart() {
        if (u == null) {
            print("Devi essere loggato per visualizzare il carrello.");
            return;
        }
        print("\n--- Il Tuo Carrello ---");
        Map<Long, CartItem> cartContents = cartService.getCartContents(u.getLogin());
        if (cartContents.isEmpty()) {
            print("Il tuo carrello è vuoto.");
            return;
        }
        double total = 0;
        for (CartItem item : cartContents.values()) {
            print("Prodotto: " + item.getProduct().getName() +
                  ", Quantità: " + item.getQuantity() +
                  ", Prezzo Unitario: " + item.getProduct().getPrice() +
                  ", Subtotale: " + (item.getProduct().getPrice() * item.getQuantity()));
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        print("Costo Totale del Carrello: " + String.format("%.2f", total));
    }

    private void checkout() {
        if (u == null) {
            print("Devi essere loggato per effettuare il checkout.");
            return;
        }
        viewCart();
        String confirm = readString("Confermare l'acquisto? (s/n)");
        if (!confirm.equalsIgnoreCase("s")) {
            print("Acquisto annullato.");
            return;
        }

        try {
            boolean success = cartService.checkoutCart(u.getLogin());
            if (success) {
                print("Acquisto completato con successo. Saldo aggiornato.");
                u = userService.findByLogin(u.getLogin()).orElse(u);
            } else {
                print("Operazione di checkout non riuscita.");
            }
        } catch (RuntimeException e) {
            print("Errore durante il checkout: " + e.getMessage());
        }
    }


    public void doUi() {
        while (true) {
            if (u == null) {
                print("\n--- Effettua il Login ---");
                u = login();
                if (u == null) {
                    print("Credenziali non valide. Riprova.");
                } else {
                    print("Benvenuto, " + u.getLogin() + "!");
                }
            } else {
                int choice = menu();
                switch (choice) {
                    case PRINT_PRODUCTS:
                        printProducts();
                        break;
                    case ADD_PRODUCT:
                        addProduct();
                        break;
                    case ADD_TO_CART: 
                        addToCart();
                        break;
                    case VIEW_CART: 
                        viewCart();
                        break;
                    case CHECKOUT:
                        checkout();
                        break;
                    case LOGOUT:
                        u = null;
                        print("Logout effettuato.");
                        break;
                    default:
                        print("Scelta non valida. Riprova.");
                }
            }
        }
    }
}