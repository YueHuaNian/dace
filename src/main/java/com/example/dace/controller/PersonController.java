package com.example.dace.controller;

import com.example.dace.model.Person;
import com.example.dace.service.PersonService;
import com.example.dace.utils.YamlUtils;
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
            List<Person> persons = YamlUtils.readYamlFile();
            if (persons != null) {
                persons.add(person);  // 将新人员添加到现有人员列表
            } else {
                persons = new ArrayList<>();
                persons.add(person);  // 如果没有现有数据，创建一个新的列表
            }

            // 将更新后的人员列表保存回 YAML 文件
            YamlUtils.writeYamlFile(persons);

            // 返回成功响应，包含添加的人员信息
            return ResponseEntity.ok(Map.of("success", true, "message", "添加成功", "person", person));
        } catch (Exception e) {
            e.printStackTrace(); // 控制台输出堆栈信息
            return ResponseEntity.internalServerError().body(Map.of("success", false, "message", "服务器错误：" + e.getMessage()));
        }
    }

    // 获取所有人员
    @GetMapping("/list")
    public List<Person> getAllPersons() {
        // 返回当前存储在 YAML 文件中的所有人员数据
        return YamlUtils.readYamlFile();
    }

    // 根据ID获取人员
    @GetMapping("/{id}")
    public Person getPersonById(@PathVariable String id) {
        return personService.getPersonById(id);
    }
}
