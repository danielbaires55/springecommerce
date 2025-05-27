
package springecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import springecommerce.ui.Ui;

@SpringBootApplication(scanBasePackages = {"springecommerce.backend", "springecommerce.ui"})
public class Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);

        Ui ui = context.getBean(Ui.class);

        ui.doUi();
    }
}