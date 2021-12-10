package com.example.demoinitial.service;

import com.example.demoinitial.domain.Person;
import com.example.demoinitial.web.dto.PagedPersonsDto;
import com.example.demoinitial.repository.PersonRepository;
import com.example.demoinitial.web.exception.PersonNotFoundException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PersonService {

    @Autowired
    PersonRepository personRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public Person createPerson(Person person) {
        person = personRepository.save(person);
        return person;
    }

    public Optional<Person> findById(Long id) {
        return personRepository.findById(id);
    }

    public Person updatePerson(Person person, Long id) {
        findById(id).orElseThrow(() -> new PersonNotFoundException("Update not successful: person with id=" + id + " not found"));
        return personRepository.save(person);
    }

    public Boolean deletePerson(Long id) {
        Person person = findById(id).orElseThrow(() -> new PersonNotFoundException("Delete not successful: person with id=" + id + " not found"));
        personRepository.deleteById(id);
        return true;
    }

    public PagedPersonsDto getAllPersons(int pageNo, int pageSize, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Person> persons = personRepository.findAll(pageable);

        // get content for page object
        List<Person> content = persons.getContent();

        PagedPersonsDto pagedPersonsDto = new PagedPersonsDto();
        pagedPersonsDto.setContent(content);
        pagedPersonsDto.setPageNo(persons.getNumber());
        pagedPersonsDto.setPageSize(persons.getSize());
        pagedPersonsDto.setTotalElements(persons.getTotalElements());
        pagedPersonsDto.setTotalPages(persons.getTotalPages());
        pagedPersonsDto.setLast(persons.isLast());

        return pagedPersonsDto;
    }

    public List<Person> getAllPersons(String firstName, String lastName) {
        return personRepository.findByFirstNameAndLastName(firstName, lastName);
    }
}

