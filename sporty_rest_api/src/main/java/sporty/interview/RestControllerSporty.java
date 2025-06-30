package sporty.interview;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import sporty.interview.model.EventOutcome;

@RestController
public class RestControllerSporty {
	
	@Autowired
	ServiceSporty service;
	
	@PostMapping("/eventOutcome")
	EventOutcome newEmployee(@RequestBody EventOutcome eventOutcome) {
		
		System.out.println("API: Received eventOutcome to store : "+eventOutcome.toString());
		if (eventOutcome.isWellFormed()==false) {
			throw new EventOutcomeRequestMalFormedException(eventOutcome);
		}
		
		service.sendEventOutcome(eventOutcome);
		
		System.out.println("API: EventOutcome processed successfully: "+eventOutcome.toString());
		
		return eventOutcome;
	}
	
	
	
}
