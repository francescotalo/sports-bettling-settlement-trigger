package sporty.interview;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import sporty.interview.model.EventOutcome;

@Service
public class ServiceSporty {

	@Autowired
	KafkaTemplate<Long, EventOutcome> kafkaTemplate;
	
	@Value("${topic.name}")
	private String topicName;
	
	@Value("${topic.wait}")
	private long topicWait;
	
	public void sendEventOutcome(EventOutcome outcome) throws KafkaInternalException{
		try {
			sendKafkaMessage(topicName, outcome.getEventId(), outcome);
	    }
	    catch (Exception e) {
	    	throwKafkaException();
	    }
	}
	
	protected void sendKafkaMessage(String topic, Long key, EventOutcome value) throws Exception{
		kafkaTemplate.send(topic, key, value).get(topicWait, TimeUnit.MILLISECONDS);
	}
	
	protected void throwKafkaException() throws KafkaInternalException{
		throw new KafkaInternalException();
	}
}
