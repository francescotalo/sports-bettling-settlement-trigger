package sporty.interview;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import sporty.interview.jpa.repository.BetsRepository;
import sporty.interview.model.EventOutcome;
import sporty.interview.rocketMQ.RocketMQProducer;

@Component
public class EventsOutcomeConsumer {

	@Value("${topic.name}")
	private String topicName;
	
	@Value("${spring.kafka.consumer.group-id}")
	private String groupId;
	
	@Autowired
	RocketMQProducer rocketMqProducer;
	
	@Autowired
	BetsRepository betRepository;

    @KafkaListener(topics = "event-outcomes", groupId = "group-events-outcome-for-bettling")
    public void listen(EventOutcome eventOutcome) {
        System.out.println("event outcome message to process: " + eventOutcome);
        
        /*we filter out the bets that have already been settled because they have an event winner id already populated.
          *   Then we update the event_winner_id field for the bet and send to RocketMQ producer*/
        betRepository.findByEventIdAndEventWinnerIdIsNull(eventOutcome.getEventId()).stream().
        map(bet -> {
        	bet.setEventWinnerId(eventOutcome.getEventWinnerId());
        	return bet;
        }).forEach(bet  -> {
        	betRepository.save(bet);
        	rocketMqProducer.sendBet(bet);
        });
        
        System.out.println("event outcome message consumed successfully: " + eventOutcome+ "processed");
        
    }

}
