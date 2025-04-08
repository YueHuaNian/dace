package com.example.dace.utils;

import com.example.dace.model.Person;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String JSON_FILE_PATH = "src/main/resources/static/data.json";  // JSON文件路径

    // 读取JSON文件
    public static List<Person> readJsonFile() {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream inputStream = new FileInputStream(JSON_FILE_PATH)) {
            return mapper.readValue(inputStream, new TypeReference<List<Person>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();  // 如果文件不存在，返回空列表
    }

    // 写入JSON文件
    public static void writeJsonFile(List<Person> persons) {
        ObjectMapper mapper = new ObjectMapper();
        try (FileWriter writer = new FileWriter(JSON_FILE_PATH)) {
            mapper.writerWithDefaultPrettyPrinter().writeValue(writer, persons);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}