package com.example.dace.service;

import com.example.dace.model.Person;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    private final List<Person> persons = new ArrayList<>();

    public void addPerson(Person person) {
        persons.add(person);
    }

    public List<Person> getAllPersons() {
        return persons;
    }

    public Person getPersonById(String id) {
        return persons.stream()
                .filter(p -> id.equals(p.getStudentId()))
                .findFirst()
                .orElse(null);
    }
}
