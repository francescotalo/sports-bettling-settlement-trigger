package sporty.interview;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import sporty.interview.jpa.model.Bet;
import sporty.interview.jpa.repository.BetsRepository;
import sporty.interview.model.EventOutcome;
import sporty.interview.rocketMQ.RocketMQProducer;

@SpringBootTest
class EventsOutcomeConsumerIntegrationTest {

	@Autowired
	EventsOutcomeConsumer eventConsumer;
	
	@MockitoBean
	RocketMQProducer rocketMqProducer;
	
	@Autowired
	BetsRepository betRepository;
	
	private static EventOutcome eventOutcome, eventOutcomeNoBet;
	
	private static List<Bet> betData;
	
	private static final Long eventId = 1001L;
	
	private static final Long eventWinnerId = 44888L;

	@BeforeAll
	public static void prepareData() {
		eventOutcome = new EventOutcome();
		eventOutcome.setEventId(eventId);
		eventOutcome.setEventWinnerId(eventWinnerId);
		eventOutcome.setEventName("testEventName");
		
		eventOutcomeNoBet = new EventOutcome();
		eventOutcomeNoBet.setEventId(2222222L);
		eventOutcomeNoBet.setEventWinnerId(eventWinnerId);
		eventOutcomeNoBet.setEventName("testEventName");
		
		
	}
	
	@Test
	public void testNoBetToSettle() {
		eventConsumer.listen(eventOutcomeNoBet);
		verify(rocketMqProducer, times(0)).sendBet(any());
	}
	
	@Test
	public void testBetToSettle() {
		assertTrue(betRepository.findByEventIdAndEventWinnerIdIsNull(eventId).size()==2);
		eventConsumer.listen(eventOutcome);
		verify(rocketMqProducer, times(2)).sendBet(any());
		Mockito.clearInvocations(rocketMqProducer);
		assertTrue(betRepository.findByEventIdAndEventWinnerIdIsNull(eventId).size()==0);
		eventConsumer.listen(eventOutcome);
		verify(rocketMqProducer, times(0)).sendBet(any());
		
	}

}
