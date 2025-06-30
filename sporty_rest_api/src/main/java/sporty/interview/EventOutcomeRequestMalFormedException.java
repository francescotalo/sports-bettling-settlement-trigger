package sporty.interview;

import sporty.interview.model.EventOutcome;

public class EventOutcomeRequestMalFormedException extends RuntimeException {

	private static final long serialVersionUID = 8216787079623954394L;
	
	EventOutcomeRequestMalFormedException (EventOutcome request) {
	    super("EventOutcomeRequestMalFormed: " + request.toString());
	  }

}
