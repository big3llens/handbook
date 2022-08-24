package ru.bfad.handbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import ru.bfad.handbook.repositories.LdapPersonRepository;

@SpringBootApplication
@PropertySource("classpath:secured.properties")
public class FinalProjectApplication {

	@Autowired
	private LdapPersonRepository ldapPersonRepository;

	public static void main(String[] args) {
		SpringApplication.run(FinalProjectApplication.class, args);
	}

//	@Bean
//	public WebMvcConfigurer corConfig(){
//		return new WebMvcConfigurer() {
//			@Override
//			public void addCorsMappings(CorsRegistry corsRegistry) {
//				corsRegistry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST").allowedHeaders("*");
//			}
//		};
//	}
}
