package springecommerce.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Component("connector")
public class JdbcConnector {

	@Value("jdbc:mysql://localhost:3306/ttfecommerce")
	private String url;
	@Value("test")
	private String username;
	@Value("test")
	private String password;
	@Value("com.mysql.cj.jdbc.Driver")
	private String driver;
	
	public Connection getConnection() throws ClassNotFoundException, SQLException {
		log.info("connecting to {} using {}", url, driver);
		Class.forName(driver);
		return DriverManager.getConnection(url, username, password);
	}
}
