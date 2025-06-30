package sporty.interview;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EventOutcomeApiAdvice {
	
	@ExceptionHandler(EventOutcomeRequestMalFormedException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	 String requestMalformed(EventOutcomeRequestMalFormedException ex) {
	    return ex.getMessage();
	 }
	
	@ExceptionHandler(KafkaInternalException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	 String kafkaInternalError(KafkaInternalException ex) {
	    return ex.getMessage();
	 }
}
