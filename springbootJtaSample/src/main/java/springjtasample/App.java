package springjtasample;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAutoConfiguration  
@EnableTransactionManagement
public class App {
	
	public static void main(String[] args) {
		new SpringApplicationBuilder(App.class).web(true).run(args);
	}
	
}
