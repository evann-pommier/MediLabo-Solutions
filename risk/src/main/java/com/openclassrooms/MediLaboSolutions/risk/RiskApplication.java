package com.openclassrooms.MediLaboSolutions.risk;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;

/**
 * Point d'entrée du microservice risk-service. Ne possède pas de base de
 * données propre : son rôle est d'interroger patient-service et
 * notes-service pour calculer un niveau de risque de diabète.
 */
@SpringBootApplication
public class RiskApplication {

	public static void main(String[] args) {
		SpringApplication.run(RiskApplication.class, args);
	}

	/**
	 * Client HTTP unique, partagé par {@code PatientClient} et
	 * {@code NoteClient}, configuré pour s'authentifier automatiquement en
	 * HTTP Basic sur chaque requête sortante grâce à
	 * {@link BasicAuthenticationInterceptor}.
	 * <p>
	 * Les identifiants réutilisent volontairement ceux déjà configurés pour
	 * la sécurité entrante de risk-service ({@code security.user.name/password})
	 * : c'est le même utilisateur "gateway" que patient-service et
	 * notes-service acceptent déjà, pas besoin d'en définir un nouveau.
	 */
	@Bean
	public RestTemplate restTemplate(
			@Value("${security.user.name}") String username,
			@Value("${security.user.password}") String password) {

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(username, password));
		return restTemplate;
	}
}