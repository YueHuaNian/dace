package com.example.dace.service;

import com.example.dace.model.Person;

import java.util.ArrayList;
import java.util.List;

import com.example.dace.utils.YamlUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
@Service
public class PersonService {

    private final List<Person> persons = new ArrayList<>();

    @PostConstruct
    public void init() {
        // 从 YAML 文件中加载数据
        List<Person> personsFromYaml = YamlUtils.readYamlFile();
        if (personsFromYaml != null) {
            persons.addAll(personsFromYaml);
        }
    }

    public void addPerson(Person person) {
        persons.add(person);
    }

    public List<Person> getAllPersons() {
        return persons;
    }

    public void deletePerson(String id) {
        persons.removeIf(p -> p.getStudentId().equals(id));
    }

    public void updatePerson(Person person) {
        for (int i = 0; i < persons.size(); i++) {
            if (persons.get(i).getStudentId().equals(person.getStudentId())) {
                persons.set(i, person);
                break;
            }
        }
    }

    public Person getPersonById(String id) {
        return persons.stream()
                .filter(p -> id.equals(p.getStudentId()))
                .findFirst()
                .orElse(null);
    }
}