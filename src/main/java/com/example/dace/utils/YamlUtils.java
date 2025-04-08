package com.example.dace.utils;

import com.example.dace.model.Person;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class YamlUtils {

    private static final String YAML_FILE_PATH = "src/main/resources/data.yaml";  // 确保路径正确

    // 读取YAML文件
    public static List<Person> readYamlFile() {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = new FileInputStream(YAML_FILE_PATH)) {
            return yaml.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();  // 如果文件不存在，返回空列表
    }

    // 写入YAML文件
    public static void writeYamlFile(List<Person> persons) {
        Yaml yaml = new Yaml();
        try (FileWriter writer = new FileWriter(YAML_FILE_PATH)) {
            yaml.dump(persons, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}