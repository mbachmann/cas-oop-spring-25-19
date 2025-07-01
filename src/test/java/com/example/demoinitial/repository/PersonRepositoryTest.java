package com.example.demoinitial.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.example.demoinitial.config.MyComponent;
import com.example.demoinitial.domain.Person;

@DataJpaTest
class PersonRepositoryTest {

	@MockitoBean
	MyComponent myComponent;

	@Autowired
	private PersonRepository personRepository;

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

	@Test
	void findByFirstNameAndLastName_returnsMatchingPersons() {
		// given

		Person anotherJohn = new Person();
		anotherJohn.setFirstName("John");
		anotherJohn.setLastName("Smith");
		personRepository.saveAll(List.of(johnDoe, janeDoe, anotherJohn));

		// when
		List<Person> result = personRepository.findByFirstNameAndLastName("John", "Doe");

		// then
		assertThat(result).hasSize(1).containsExactly(johnDoe);
	}

	@Test
	void findQueryByLastName_returnsAllWithThatLastName() {
		// given
		Person anotherJohn = new Person();
		anotherJohn.setFirstName("John");
		anotherJohn.setLastName("Smith");
		personRepository.saveAll(List.of(johnDoe, janeDoe, anotherJohn));

		// when
		List<Person> result = personRepository.findQueryByLastName("Doe");

		// then
		assertThat(result).hasSize(2).extracting(Person::getFirstName)
						  .containsExactlyInAnyOrder("John", "Jane");
	}
}