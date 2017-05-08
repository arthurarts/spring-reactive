package it.codedvalue.service;

import java.math.BigDecimal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringReactiveApplicationTests {

	WebTestClient client = WebTestClient
			.bindToController(new SavingsTransactionServerApplication())
			.build();

	@Test
	public void contextLoads() {
		FluxExchangeResult<SavingsTransactionEvent> result = client.get().uri("/savingstransactions")
				.accept(TEXT_EVENT_STREAM)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(TEXT_EVENT_STREAM)
				.returnResult(SavingsTransactionEvent.class);

		StepVerifier.create(result.getResponseBody())
				.expectNextMatches(saving -> saving.getClass().equals(SavingsTransactionEvent.class))
				.expectNextMatches(saving -> saving.getAmount().compareTo(BigDecimal.valueOf(500L))==1)
				.expectNextCount(9)
				.thenCancel()
				.verify();
	}

}
