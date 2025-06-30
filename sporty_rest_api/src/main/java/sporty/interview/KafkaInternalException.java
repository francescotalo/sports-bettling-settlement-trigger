package sporty.interview;

import sporty.interview.model.EventOutcome;

public class KafkaInternalException extends RuntimeException {

	KafkaInternalException () {
	    super("Internal Server Error. Try again later.");
	  }
}
