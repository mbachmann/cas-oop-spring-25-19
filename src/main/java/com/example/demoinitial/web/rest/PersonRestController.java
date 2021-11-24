package com.example.demoinitial.web.rest;

import com.example.demoinitial.domain.Person;
import com.example.demoinitial.domain.dto.PagedPersonsDto;
import com.example.demoinitial.service.PersonService;
import com.example.demoinitial.utils.AppConstants;
import com.example.demoinitial.web.exception.PersonNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/persons")
public class PersonRestController {
    private final PersonService personService;


    @Autowired
    public PersonRestController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/{id}")
    public Person getPersonById(@PathVariable Long id) {
        return personService.findById(id).orElseThrow(() -> new PersonNotFoundException("Get not successful: person with id=" + id + " not found"));
    }

    @GetMapping("/paged")
    public PagedPersonsDto getPersonsPaged(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return personService.getAllPersons(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/filtered")
    public List<Person> getPersonsPaged(
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName
    ){
        return personService.getAllPersons(firstName, lastName);
    }


    @GetMapping("/")
    public List<Person> getPersons() {
        return personService.getAllPersons();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        Person personCreated = personService.createPerson(person);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(personCreated.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PostMapping(value = "/new", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> newTodoItem(@RequestBody Person person) {
        return ResponseEntity.ok(personService.createPerson(person));
    }


    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<Person> patchPerson(@PathVariable Long id,
                                                   @RequestBody JsonPatch patch) {
        try {
            Person personPatched = personService.patchPerson(patch, id);
            return ResponseEntity.ok(personPatched);
        } catch (PersonNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> updatePerson(@PathVariable Long id,
                                               @RequestBody Person person) {
        return ResponseEntity.ok(personService.updatePerson(person, id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deletePerson(@PathVariable Long id) {
        return ResponseEntity.ok(personService.deletePerson(id));
    }


}
