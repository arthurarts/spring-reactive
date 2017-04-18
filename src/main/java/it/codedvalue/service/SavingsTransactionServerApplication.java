package it.codedvalue.service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Stream;
import org.springframework.boot.SpringApplication;
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
        return Mono.just(new SavingsTransactionEvent(id, BigDecimal.valueOf(500), new Date()));
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE, value="/savingstransactions")
    public Flux<SavingsTransactionEvent> transactionEvents() {

        Flux<SavingsTransactionEvent> savingsTransactionsFlux =
                Flux.fromStream(
                        Stream.generate(
                                () -> new SavingsTransactionEvent(System.currentTimeMillis(), BigDecimal.valueOf(550), new Date())
                )); // generate a stream of SavingsTransactionsEvents.

        Flux<Long> durationsFlux = Flux.interval(Duration.ofSeconds(1)); // will emit new event every 1 seconds.

        return Flux.zip(savingsTransactionsFlux, durationsFlux).map(Tuple2::getT1); // http://rxmarbles.com/#zip

    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(SavingsTransactionServerApplication.class)
                .properties(Collections.singletonMap("server.port", "8080"))
                .run(args);
    }
}
