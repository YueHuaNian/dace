package com.example.dace;

import com.example.dace.model.Person;
import com.example.dace.utils.AppJsonUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PersonAppUtils {

    // 添加人员
    public static void addPerson(Person person) {
        List<Person> persons = AppJsonUtils.readJsonFile();
        persons.add(person);
        AppJsonUtils.writeJsonFile(persons);
    }

    // 删除人员
    public static boolean deletePerson(String id) {
        List<Person> persons = AppJsonUtils.readJsonFile();
        boolean removed = persons.removeIf(p -> p.getStudentId().equals(id));
        if (removed) {
            AppJsonUtils.writeJsonFile(persons);
        }
        return removed;
    }

    // 更新人员
    public static boolean updatePerson(String id, Person updatedPerson) {
        List<Person> persons = AppJsonUtils.readJsonFile();
        for (int i = 0; i < persons.size(); i++) {
            if (persons.get(i).getStudentId().equals(id)) {
                persons.set(i, updatedPerson);
                AppJsonUtils.writeJsonFile(persons);
                return true;
            }
        }
        return false;
    }

    // 获取所有人员
    public static List<Person> getAllPersons() {
        return AppJsonUtils.readJsonFile();
    }

    // 按学号排序
    public static List<Person> sortPersons(boolean ascending) {
        List<Person> persons = AppJsonUtils.readJsonFile();
        if (persons.isEmpty()) {
            return persons;
        }
        return persons.stream()
                .sorted(ascending ? Comparator.comparing(Person::getStudentId) : Comparator.comparing(Person::getStudentId).reversed())
                .collect(Collectors.toList());
    }
}