package com.example.demoinitial.web.rest;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.matchesRegex;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demoinitial.config.MyComponent;
import com.example.demoinitial.domain.Person;
import com.example.demoinitial.service.PersonService;
import com.example.demoinitial.web.api.response.PagedPersonsResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(PersonController.class)
@WithMockUser(username = "admin",           // 🔐 mocked principal
		roles = {"ADMIN","USER"})
class PersonControllerTest {

	@MockitoBean
	MyComponent myComponent;

	private Person johnDoe;
	private Person janeDoe;
	private Person felixMuster;
	private Person johnSmith;
	private Person aliceWonder;

	@BeforeEach
	void setUp() {
		johnDoe = new Person();
		johnDoe.setFirstName("John");
		johnDoe.setLastName("Doe");

		janeDoe = new Person();
		janeDoe.setFirstName("Jane");
		janeDoe.setLastName("Doe");

		felixMuster = new Person();
		felixMuster.setFirstName("Felix");
		felixMuster.setLastName("Muster");

		johnSmith = new Person();
		johnSmith.setFirstName("John");
		johnSmith.setLastName("Smith");
		johnSmith.setId(4L);

		aliceWonder = new Person();
		aliceWonder.setFirstName("Alice");
		aliceWonder.setLastName("Wonder");
		aliceWonder.setId(5L);
	}


	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private PersonService personService;

	@Autowired
	private ObjectMapper objectMapper;   // comes from Spring Boot starter‑json

	/* ---------- GET /api/persons/{id} --------------------------------- */

	@Test
	@DisplayName("200 ‑ getPersonById")
	void getPersonById_returnsPerson() throws Exception {
		given(personService.findById(5L)).willReturn(Optional.of(aliceWonder));

		mockMvc.perform(get("/api/persons/5"))
			   .andExpect(status().isOk())
			   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			   .andExpect(jsonPath("$.id").value(5))
			   .andExpect(jsonPath("$.firstName").value("Alice"));
	}

	@Test
	@DisplayName("404 ‑ getPersonById not found")
	void getPersonById_notFound() throws Exception {
		given(personService.findById(anyLong())).willReturn(Optional.empty());

		mockMvc.perform(get("/api/persons/99"))
			   .andExpect(status().isNotFound());
	}

	/* ---------- GET /api/persons -------------------------------------- */

	@Test
	void getAllPersons() throws Exception {
		given(personService.getAllPersons()).willReturn(List.of(johnDoe, janeDoe));

		mockMvc.perform(get("/api/persons"))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$", hasSize(2)))
			   .andExpect(jsonPath("$[1].lastName").value("Doe"));
	}

	/* ---------- GET /api/persons/paged -------------------------------- */

	@Test
	void getPersonsPaged() throws Exception {
		PagedPersonsResponse resp = new PagedPersonsResponse();
		resp.setContent(List.of(aliceWonder));
		resp.setPageNo(0);
		resp.setPageSize(1);
		resp.setTotalElements(1L);
		resp.setTotalPages(1);
		resp.setLast(true);

		given(personService.getAllPersons(0, 1, "id", "asc")).willReturn(resp);

		mockMvc.perform(get("/api/persons/paged")
								.param("pageNo", "0")
								.param("pageSize", "1")
								.param("sortBy", "id")
								.param("sortDir", "asc"))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$.content[0].id").value(5))
			   .andExpect(jsonPath("$.totalElements").value(1));
	}

	/* ---------- GET /api/persons/filtered ----------------------------- */

	@Test
	void getPersonsFiltered() throws Exception {
		Person person = new Person();
		person.setFirstName("John");
		person.setLastName("Smith");
		person.setId(4L);
		given(personService.getAllPersons("John", "Smith")).willReturn(List.of(person));

		mockMvc.perform(get("/api/persons/filtered")
								.param("firstName", "John")
								.param("lastName", "Smith"))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$[0].id").value(4));
	}

	/* ---------- POST /api/persons (201 + Location) -------------------- */

	@Test
	void createPerson_returns201() throws Exception {
		given(personService.createPerson(any(Person.class)))
				.willReturn(aliceWonder); // id=5

		mockMvc.perform(post("/api/persons")
								.with(csrf())                         // 🔐 CSRF token
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(aliceWonder)))
			   .andExpect(status().isCreated())
			   .andExpect(header().string("Location", matchesRegex(".*/api/persons/5$")));
	}

	/* ---------- POST /api/persons/new (200 + body) -------------------- */

	@Test
	void createPersonImmediate() throws Exception {
		given(personService.createPerson(any(Person.class))).willReturn(johnSmith); // id=4

		mockMvc.perform(post("/api/persons/new")
								.with(csrf())
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(johnSmith)))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$.id").value(4))
			   .andExpect(jsonPath("$.firstName").value("John"));
	}

	/* ---------- PUT /api/persons/{id} --------------------------------- */

	@Test
	void updatePerson() throws Exception {
		Person patched = johnDoe;
		given(personService.updatePerson(any(Person.class), eq(1L))).willReturn(patched);

		mockMvc.perform(put("/api/persons/1")
								.with(csrf())
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(patched)))
			   .andExpect(status().isOk())
			   .andExpect(jsonPath("$.firstName").value("John"));
	}

	/* ---------- DELETE /api/persons/delete/{id} ----------------------- */

	@Test
	void deletePerson() throws Exception {
		given(personService.deletePerson(1L)).willReturn(true);

		mockMvc.perform(delete("/api/persons/delete/1").with(csrf()))
			   .andExpect(status().isOk())
			   .andExpect(content().string("true"));
	}
}