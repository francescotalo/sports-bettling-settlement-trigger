package sporty.interview;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import sporty.interview.model.EventOutcome;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class SportyServiceTest {

	
	private String bodyWrong="{"
			+ "  \"eventId\":2348,"
			+ "  \"eventWinnerId\":4488,"
			+ "  \"eventName\":\"\""
			+ "}";
	
	private static EventOutcome eventOutcome;

	@BeforeAll
	public static void prepareData() {
		eventOutcome = new EventOutcome();
		eventOutcome.setEventId(1L);
		eventOutcome.setEventWinnerId(2L);
		eventOutcome.setEventName("name");
	}
	
	@MockitoBean
	private KafkaTemplate<Long, EventOutcome> kafkaTemplate;
	
	@MockitoSpyBean
	ServiceSporty service;
	
	@Value("${topic.name}")
	private String topicName;
	
	@Value("${topic.wait}")
	private long topicWait;
	
	@Test
	void kafKaIssue() throws Exception {
		doThrow(new TimeoutException()).when(service).sendKafkaMessage(topicName, eventOutcome.getEventId(), eventOutcome);
		doNothing().when(service).throwKafkaException();
		service.sendEventOutcome(eventOutcome);
		verify(service, times(1)).throwKafkaException();
	}
	
	@Test
	void NoIssue() throws Exception {
		doNothing().when(service).sendKafkaMessage(topicName, eventOutcome.getEventId(), eventOutcome);
		service.sendEventOutcome(eventOutcome);
		verify(service, times(0)).throwKafkaException();
	}
	

}
