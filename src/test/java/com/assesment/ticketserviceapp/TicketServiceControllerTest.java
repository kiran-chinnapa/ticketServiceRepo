package com.assesment.ticketserviceapp;

import com.assesment.ticketserviceapp.controller.TicketServiceController;
import com.assesment.ticketserviceapp.model.SeatHold;
import com.assesment.ticketserviceapp.service.TicketService;
import com.assesment.ticketserviceapp.validation.HoldSeatRequest;
import com.assesment.ticketserviceapp.validation.ReserveSeatRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.util.ArrayList;
import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketServiceController.class)
@Log4j2
public class TicketServiceControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TicketService ticketService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testGetAvailableSeats_withLevel() throws Exception {
		when(ticketService.numSeatsAvailable(Optional.of(2))).thenReturn(50);

		mockMvc.perform(get("/api/tickets/numSeatsAvailable")
						.param("level", "2"))
				.andExpect(status().isOk())
				.andExpect(content().string("50"));
	}

	@Test
	void testGetAvailableSeats_withoutLevel() throws Exception {
		when(ticketService.numSeatsAvailable(Optional.empty())).thenReturn(0);
		mockMvc.perform(get("/api/tickets/numSeatsAvailable"))
				.andExpect(status().isOk())
				.andExpect(content().string("No level provided"));
	}

	@Test
	void testValidHoldSeatsRequest() throws Exception {
		HoldSeatRequest request = new HoldSeatRequest();
		request.setNumSeats(2);
		request.setMinLevel(1);
		request.setMaxLevel(3);
		request.setCustomerEmail("test@email");

		SeatHold mockSeatHold = new SeatHold(new ArrayList<>(),"test@email",1,10.0,1);

		when(ticketService.findAndHoldSeats(2,
				Optional.of(1),
				Optional.of(3),
				"test@email"))
				.thenReturn(mockSeatHold);

		mockMvc.perform(post("/api/tickets/findAndHoldSeats")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk());
	}

	@Test
	void testInvalidHoldSeatsRequest() throws Exception {
		HoldSeatRequest request = new HoldSeatRequest();
		request.setNumSeats(0); // Invalid: less than 1
		request.setCustomerEmail("bad-email"); // Invalid email

		MvcResult mvcResult= mockMvc.perform(post("/api/tickets/findAndHoldSeats")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andReturn();
		String response = mvcResult.getResponse().getContentAsString();
		log.info("Response body: "+ response);
	}


	@Test
	void testReserveSeats() throws Exception {

		ReserveSeatRequest request = new ReserveSeatRequest();
		request.setSeatHoldId(1);
		request.setCustomerEmail("test@email");

		when(ticketService.reserveSeats(1, "test@email")).thenReturn("Reserved-1");

		mockMvc.perform(post("/api/tickets/reserveSeats")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk());
	}

	@Test
	void testInvalidReserveSeats() throws Exception {

		ReserveSeatRequest request = new ReserveSeatRequest();
		request.setSeatHoldId(-1); // Invalid
		request.setCustomerEmail("bad-email"); // Invalid

		MvcResult result = mockMvc.perform(post("/api/tickets/reserveSeats")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andReturn();

		String response = result.getResponse().getContentAsString();
		log.info("Validation Response: " + response);
	}
}
