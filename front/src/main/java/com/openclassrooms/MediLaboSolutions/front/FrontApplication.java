package com.openclassrooms.MediLaboSolutions.front;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Point d'entrée du microservice front. Interface utilisateur (Thymeleaf)
 * qui n'expose aucune API REST : son unique rôle est de récupérer des
 * données auprès des microservices back via la gateway, et de les afficher.
 */
@SpringBootApplication
public class FrontApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrontApplication.class, args);
    }

    /**
     * Client HTTP unique, partagé par tous les contrôleurs, utilisé pour
     * interroger la gateway. Pas d'authentification configurée ici : le front
     * appelle la gateway sans credentials, c'est elle qui s'authentifie
     * ensuite auprès des microservices back.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}