package sporty.interview;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import sporty.interview.jpa.model.Bet;
import sporty.interview.jpa.repository.BetsRepository;
import sporty.interview.model.EventOutcome;
import sporty.interview.rocketMQ.RocketMQProducer;

@SpringBootTest
class EventsOutcomeConsumerApplicationTests {

	@Autowired
	EventsOutcomeConsumer eventConsumer;
	
	@MockitoBean
	RocketMQProducer rocketMqProducer;
	
	@MockitoBean
	BetsRepository betRepository;
	
	private static EventOutcome eventOutcome;
	
	private static List<Bet> betData;

	@BeforeAll
	public static void prepareData() {
		eventOutcome = new EventOutcome();
		eventOutcome.setEventId(2349L);
		eventOutcome.setEventWinnerId(44888L);
		eventOutcome.setEventName("testEventName");
		
		betData = new ArrayList<Bet>();
		
		Bet bet = new Bet();
		bet.setAmount(111.0);
		bet.setEventId(2349L);
		bet.setEventMarketId(11444L);
		bet.setId(3L);
		bet.setUserId(222L);
		
		betData.add(bet);
		
		bet = new Bet();
		bet.setAmount(115.0);
		bet.setEventId(2349L);
		bet.setEventMarketId(11444L);
		bet.setId(4L);
		bet.setUserId(222L);
		
		betData.add(bet);
	}
	
	@Test
	public void testNoBetToSettle() {
		when(betRepository.findByEventIdAndEventWinnerIdIsNull(eventOutcome.getEventId())).thenReturn(new ArrayList<Bet>());
		eventConsumer.listen(eventOutcome);
		verify(betRepository, times(0)).save(any());
		verify(rocketMqProducer, times(0)).sendBet(any());;
	}
	
	@Test
	public void testBetToSettle() {
		when(betRepository.findByEventIdAndEventWinnerIdIsNull(eventOutcome.getEventId())).thenReturn(betData);
		eventConsumer.listen(eventOutcome);
		verify(betRepository, times(2)).save(any());
		verify(rocketMqProducer, times(2)).sendBet(any());;
	}

}
