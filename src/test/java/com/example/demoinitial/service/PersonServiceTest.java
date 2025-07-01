package com.example.demoinitial.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import com.example.demoinitial.domain.Person;
import com.example.demoinitial.repository.PersonRepository;
import com.example.demoinitial.web.api.response.PagedPersonsResponse;
import com.example.demoinitial.web.exception.PersonNotFoundException;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

	@Mock
	private PersonRepository personRepository;

	@InjectMocks
	private PersonService personService;

	private Person johnDoe;
	private Person janeDoe;

	@BeforeEach
	void setUp() {
		johnDoe = new Person();
		johnDoe.setFirstName("John");
		johnDoe.setLastName("Doe");
		janeDoe = new Person();
		janeDoe.setFirstName("Jane");
		janeDoe.setLastName("Doe");
	}

	/* ------------------------------------------------------------------
	 *  getAllPersons()
	 * ------------------------------------------------------------------ */
	@Test
	void getAllPersons_returnsListFromRepository() {
		when(personRepository.findAll()).thenReturn(List.of(johnDoe, janeDoe));

		List<Person> result = personService.getAllPersons();

		assertThat(result).containsExactly(johnDoe, janeDoe);
		verify(personRepository).findAll();
	}

	/* ------------------------------------------------------------------
	 *  createPerson()
	 * ------------------------------------------------------------------ */
	@Test
	void createPerson_savesAndReturnsEntity() {
		when(personRepository.save(johnDoe)).thenReturn(johnDoe);

		Person saved = personService.createPerson(johnDoe);

		assertThat(saved).isSameAs(johnDoe);
		verify(personRepository).save(johnDoe);
	}

	/* ------------------------------------------------------------------
	 *  findById()
	 * ------------------------------------------------------------------ */
	@Test
	void findById_returnsOptionalFromRepository() {
		when(personRepository.findById(1L)).thenReturn(Optional.of(johnDoe));

		Optional<Person> result = personService.findById(1L);

		assertThat(result).contains(johnDoe);
		verify(personRepository).findById(1L);
	}

	/* ------------------------------------------------------------------
	 *  updatePerson()
	 * ------------------------------------------------------------------ */
	@Test
	void updatePerson_whenFound_updatesAndReturnsEntity() {
		when(personRepository.findById(1L)).thenReturn(Optional.of(johnDoe));
		when(personRepository.save(johnDoe)).thenReturn(johnDoe);

		Person updated = personService.updatePerson(johnDoe, 1L);

		assertThat(updated).isSameAs(johnDoe);
		verify(personRepository).save(johnDoe);
	}

	@Test
	void updatePerson_whenNotFound_throwsException() {
		when(personRepository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> personService.updatePerson(johnDoe, 99L))
				.isInstanceOf(PersonNotFoundException.class)
				.hasMessageContaining("id=99");

		verify(personRepository, never()).save(any());
	}

	/* ------------------------------------------------------------------
	 *  deletePerson()
	 * ------------------------------------------------------------------ */
	@Test
	void deletePerson_whenFound_deletesAndReturnsTrue() {
		when(personRepository.findById(1L)).thenReturn(Optional.of(johnDoe));

		boolean result = personService.deletePerson(1L);

		assertThat(result).isTrue();
		verify(personRepository).deleteById(1L);
	}

	@Test
	void deletePerson_whenNotFound_throwsException() {
		when(personRepository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> personService.deletePerson(99L))
				.isInstanceOf(PersonNotFoundException.class)
				.hasMessageContaining("id=99");

		verify(personRepository, never()).deleteById(any());
	}

	/* ------------------------------------------------------------------
	 *  getAllPersons(pageable)
	 * ------------------------------------------------------------------ */
	@Test
	void getAllPersons_withPaging_mapsPageToResponse() {
		PageRequest pageRequest = PageRequest.of(0, 2, Sort.by("id").ascending());
		Page<Person> pageMock = new PageImpl<>(List.of(johnDoe, janeDoe), pageRequest, 4);

		when(personRepository.findAll(pageRequest)).thenReturn(pageMock);

		PagedPersonsResponse response =
				personService.getAllPersons(0, 2, "id", "asc");

		assertThat(response.getContent()).containsExactly(johnDoe, janeDoe);
		assertThat(response.getPageNo()).isEqualTo(0);
		assertThat(response.getPageSize()).isEqualTo(2);
		assertThat(response.getTotalElements()).isEqualTo(4);
		assertThat(response.getTotalPages()).isEqualTo(2);
		assertThat(response.isLast()).isFalse();
	}

	/* ------------------------------------------------------------------
	 *  getAllPersons(first, last)
	 * ------------------------------------------------------------------ */
	@Test
	void getAllPersons_byFirstAndLastName_delegatesToRepository() {
		when(personRepository.findByFirstNameAndLastName("John", "Doe"))
				.thenReturn(List.of(johnDoe));

		List<Person> result = personService.getAllPersons("John", "Doe");

		assertThat(result).containsExactly(johnDoe);
		verify(personRepository).findByFirstNameAndLastName("John", "Doe");
	}
}