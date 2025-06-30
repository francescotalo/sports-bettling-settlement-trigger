package sporty.interview;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class RestApiApplicationTests {

	
	private String bodyWrong="{"
			+ "  \"eventId\":2348,"
			+ "  \"eventWinnerId\":4488,"
			+ "  \"eventName\":\"\""
			+ "}";
	
	private String bodyOk="{"
			+ "  \"eventId\":2348,"
			+ "  \"eventWinnerId\":4488,"
			+ "  \"eventName\":\"test\""
			+ "}";
	
	@Autowired
	private RestControllerSporty controller;
	
	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ServiceSporty service;

	@Test
	void contextLoads() {
		assert(controller!=null);
	}
	
	@Test
	void wrongRequest() throws Exception {
		mockMvc.perform(post("/eventOutcome").contentType("application/json").content(bodyWrong)).andDo(print()).andExpect(status().isBadRequest()).
		andExpect(content().string(containsString("EventOutcomeRequestMalFormed")));
	}
	
	@Test
	void okRequest() throws Exception {
		mockMvc.perform(post("/eventOutcome").contentType("application/json").content(bodyOk)).andDo(print()).andExpect(status().isOk()).
		andExpect(content().string(containsString("2348")));
	}
	
	@Test
	void kafkaProblem() throws Exception {
		doThrow(new KafkaInternalException()).when(service).sendEventOutcome(any());
		mockMvc.perform(post("/eventOutcome").contentType("application/json").content(bodyOk)).andDo(print()).andExpect(status().isInternalServerError()).
		andExpect(content().string(containsString("Internal Server Error")));
	}

}
