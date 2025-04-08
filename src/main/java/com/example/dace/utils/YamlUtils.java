package com.example.dace.utils;
import com.example.dace.model.Person;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.List;

public class YamlUtils {

    private static final String YAML_FILE_PATH = "src/main/resources/data.yaml";  // YAML文件路径

    // 读取YAML文件
    public static List<Person> readYamlFile() {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = new FileInputStream(YAML_FILE_PATH)) {
            // 读取文件并反序列化
            return yaml.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 写入YAML文件
    public static void writeYamlFile(List<Person> persons) {
        Yaml yaml = new Yaml();
        try (FileWriter writer = new FileWriter(YAML_FILE_PATH)) {
            // 将数据序列化为YAML并写入文件
            yaml.dump(persons, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
