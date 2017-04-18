package it.codedvalue.client;


import it.codedvalue.service.SavingsTransactionEvent;
import java.util.Collections;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class SavingsTransactionClientApplication {

    @Bean
    WebClient webClient() {
        return WebClient.create("http://localhost:8080");
    }

    @Bean
    CommandLineRunner demo(WebClient webClient) {
        return args -> {
            webClient
                    .get()
                    .uri("/savingstransactions")
                    .accept(MediaType.TEXT_EVENT_STREAM)
                    .retrieve().bodyToFlux(SavingsTransactionEvent.class)
                    .subscribe(ste -> System.out.println(ste.getAmount() + " euro gestort op " + ste.getTransactionDate()));
        };
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(SavingsTransactionClientApplication.class)
                .properties(Collections.singletonMap("server.port", "8082"))
                .run(args);
    }

}
