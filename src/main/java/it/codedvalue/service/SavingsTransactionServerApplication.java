package it.codedvalue.service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Random;
import java.util.stream.Stream;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@SpringBootApplication
@RestController
public class SavingsTransactionServerApplication {

    @GetMapping("/savingstransactions/{id}")
    Mono<SavingsTransactionEvent> eventById(@PathVariable Long id) {
        return Mono.just(new SavingsTransactionEvent(id, BigDecimal.valueOf((new Random()).nextInt((1500 - 0) + 1) + 0 ), new Date()));
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE, value = "/savingstransactions")
    Flux<SavingsTransactionEvent> transactionEvents() {
        Flux<SavingsTransactionEvent> savingsTransactionsFlux =
                Flux.fromStream(
                        Stream.generate(
                                () -> new SavingsTransactionEvent(System.currentTimeMillis(), BigDecimal.valueOf(
                                        (new Random()).nextInt((1500 - 0) + 1) + 0), new Date())
                        ));

        Flux<Long> durationsFlux = Flux.interval(Duration.ofSeconds(1));

        return Flux.zip(savingsTransactionsFlux, durationsFlux).map(Tuple2::getT1); // http://rxmarbles.com/#zip
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(SavingsTransactionServerApplication.class)
                .properties(Collections.singletonMap("server.port", "8080"))
                .run(args);
    }
}
