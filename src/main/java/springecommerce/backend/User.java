// springecommerce/backend/User.java

package springecommerce.backend;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data; // Se usi Lombok
import lombok.NoArgsConstructor; // Aggiungi questo import se non l'hai già
import lombok.AllArgsConstructor; // Aggiungi questo import se non l'hai già

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor // Necessario per JPA
@AllArgsConstructor // Utile per creare User con tutti i campi
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String login;
    private String password; // Ricorda: questa sarà la password HASHATA
    private String name;    // Presumo questi campi esistano o li aggiungerai
    private String surname; // Presumo questi campi esistano o li aggiungerai

    private Double balance; // <<< NUOVO CAMPO: Saldo del credito disponibile

    // Se non hai già name e surname, e vuoi usarli, aggiungi anche qui un costruttore
    // public User(String login, String password, String name, String surname, Double balance) {
    //     this.login = login;
    //     this.password = password;
    //     this.name = name;
    //     this.surname = surname;
    //     this.balance = balance;
    // }
}