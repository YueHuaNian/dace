package com.example.dace.controller;

import com.example.dace.model.Person;
import com.example.dace.service.PersonService;
import com.example.dace.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    @Autowired
    private PersonService personService;

    // 添加人员并将数据保存到 YAML 文件
    @PostMapping("/add")
    public ResponseEntity<?> addPersonFromTemplate(@RequestBody Map<String, Object> request) {
        try {
            Map<String, Object> template = (Map<String, Object>) request.get("template");
            if (template == null || !template.containsKey("data")) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "缺少 template.data"));
            }

            List<Map<String, Object>> data = (List<Map<String, Object>>) template.get("data");

            Person person = new Person();
            for (Map<String, Object> item : data) {
                String field = (String) item.get("name");
                String value = (String) item.get("value");
                if (field == null || value == null) continue;

                switch (field) {
                    case "name": person.setName(value); break;
                    case "studentId": person.setStudentId(value); break;
                    case "email": person.setEmail(value); break;
                    case "phoneNumber": person.setPhoneNumber(value); break;
                    case "interest": person.setInterest(value); break;
                }
            }

            // 调用服务层保存人员信息
            personService.addPerson(person);

            // 获取现有的人员列表
            List<Person> persons = JsonUtils.readJsonFile();
            if (persons != null) {
                persons.add(person);  // 将新人员添加到现有人员列表
            } else {
                persons = new ArrayList<>();
                persons.add(person);  // 如果没有现有数据，创建一个新的列表
            }

            // 将更新后的人员列表保存回 YAML 文件
            JsonUtils.writeJsonFile(persons);

            // 返回成功响应，包含添加的人员信息
            return ResponseEntity.ok(Map.of("success", true, "message", "添加成功", "person", person));
        } catch (Exception e) {
            e.printStackTrace(); // 控制台输出堆栈信息
            return ResponseEntity.internalServerError().body(Map.of("success", false, "message", "服务器错误：" + e.getMessage()));
        }
    }
    // 删除人员
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePerson(@PathVariable String id) {
        try {
            Person person = personService.getPersonById(id);
            if (person == null) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "人员不存在"));
            }

            personService.deletePerson(id);

            // 获取现有的人员列表
            List<Person> persons = JsonUtils.readJsonFile();
            if (persons != null) {
                persons.removeIf(p -> p.getStudentId().equals(id));
                JsonUtils.writeJsonFile(persons);
            }

            return ResponseEntity.ok(Map.of("success", true, "message", "删除成功"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("success", false, "message", "服务器错误：" + e.getMessage()));
        }
    }

    // 更新人员
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePerson(@PathVariable String id, @RequestBody Map<String, Object> request) {
        try {
            Map<String, Object> template = (Map<String, Object>) request.get("template");
            if (template == null || !template.containsKey("data")) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "缺少 template.data"));
            }

            List<Map<String, Object>> data = (List<Map<String, Object>>) template.get("data");

            Person existingPerson = personService.getPersonById(id);
            if (existingPerson == null) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "人员不存在"));
            }

            for (Map<String, Object> item : data) {
                String field = (String) item.get("name");
                String value = (String) item.get("value");
                if (field == null || value == null) continue;

                switch (field) {
                    case "name": existingPerson.setName(value); break;
                    case "email": existingPerson.setEmail(value); break;
                    case "phoneNumber": existingPerson.setPhoneNumber(value); break;
                    case "interest": existingPerson.setInterest(value); break;
                }
            }

            personService.updatePerson(existingPerson);

            // 获取现有的人员列表
            List<Person> persons = JsonUtils.readJsonFile();
            if (persons != null) {
                for (int i = 0; i < persons.size(); i++) {
                    if (persons.get(i).getStudentId().equals(id)) {
                        persons.set(i, existingPerson);
                        break;
                    }
                }
                JsonUtils.writeJsonFile(persons);
            }

            return ResponseEntity.ok(Map.of("success", true, "message", "更新成功", "person", existingPerson));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("success", false, "message", "服务器错误：" + e.getMessage()));
        }
    }
    // 获取所有人员
    @GetMapping("/list")
    public List<Person> getAllPersons() {
        // 返回当前存储在 YAML 文件中的所有人员数据
        return personService.getAllPersons();
    }

    @GetMapping("/{id}")
    public Person getPersonById(@PathVariable String id) {
        return personService.getPersonById(id);
    }
}
