package sporty.interview.rocketMQ;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import sporty.interview.jpa.model.Bet;

@Component
public class RocketMQProducer {

	 /**@Autowired
	 private RocketMQTemplate rocketMQTemplate;*/
	
	@Value("${rocketmq.bet.topic.name}")
	private String betTopicName;
	
	public void sendBet(Bet bet) {
		System.out.println("Bet to settle sent to RocketMQ : " + bet.toString());
		//rocketMQTemplate.convertAndSend(betTopicName, bet);
	}
}
