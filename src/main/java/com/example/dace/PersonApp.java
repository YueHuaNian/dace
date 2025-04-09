package com.example.dace;

import com.example.dace.model.Person;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PersonApp {

    public static void main(String[] args) {
        // 解析命令行参数
        Map<String, String> options = parseCommandLineArgs(args);

        // 检查是否需要显示帮助信息
        if (options.containsKey("-h") || options.containsKey("--help")) {
            showHelp();
            return;
        }

        // 检查互斥选项
        if (hasConflictingOptions(options)) {
            System.err.println("错误：不能同时使用多个互斥选项 (-a, -d, -u, -l)");
            showHelp();
            return;
        }

        // 根据选项执行操作
        if (options.containsKey("-a") || options.containsKey("--add")) {
            handleAdd(options);
        } else if (options.containsKey("-d") || options.containsKey("--delete")) {
            handleDelete(options);
        } else if (options.containsKey("-u") || options.containsKey("--update")) {
            handleUpdate(options);
        } else if (options.containsKey("-l") || options.containsKey("--list")) {
            handleList(options);
        } else {
            // 默认操作：列出所有记录（降序）
            handleList(options);
        }
    }

    // 解析命令行参数
    private static Map<String, String> parseCommandLineArgs(String[] args) {
        Map<String, String> options = new java.util.HashMap<>();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("-")) {
                String key = arg;
                String value = null;
                if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                    value = args[i + 1];
                    i++;
                }
                options.put(key, value);
            }
        }
        return options;
    }

    // 检查互斥选项
    private static boolean hasConflictingOptions(Map<String, String> options) {
        int count = 0;
        if (options.containsKey("-a") || options.containsKey("--add")) count++;
        if (options.containsKey("-d") || options.containsKey("--delete")) count++;
        if (options.containsKey("-u") || options.containsKey("--update")) count++;
        if (options.containsKey("-l") || options.containsKey("--list")) count++;
        return count > 1;
    }

    // 显示帮助信息
    private static void showHelp() {
        System.out.println("PersonApp 使用说明：");
        System.out.println("  -h, --help: 显示帮助信息");
        System.out.println("  -a, --add: 添加一条新记录");
        System.out.println("    -n, --name: 姓名");
        System.out.println("    -i, --id: 学号");
        System.out.println("    -m, --mobile: 手机号");
        System.out.println("    -b, --hobby: 兴趣爱好");
        System.out.println("  -d, --delete: 删除一条记录");
        System.out.println("    -i, --id: 学号");
        System.out.println("  -u, --update: 更新一条记录");
        System.out.println("    -i, --id: 学号");
        System.out.println("    -n, --name: 更新后的姓名（可选）");
        System.out.println("    -m, --mobile: 更新后的手机号（可选）");
        System.out.println("    -b, --hobby: 更新后的兴趣爱好（可选）");
        System.out.println("  -l, --list: 列出所有记录");
        System.out.println("    -l, --list [ascend|descend]: 排序方式");
    }

    // 处理添加操作
    private static void handleAdd(Map<String, String> options) {
        // 检查是否提供了所有必要的参数
        if (!options.containsKey("-n") && !options.containsKey("--name") ||
                !options.containsKey("-i") && !options.containsKey("--id") ||
                !options.containsKey("-m") && !options.containsKey("--mobile") ||
                !options.containsKey("-b") && !options.containsKey("--hobby")) {
            System.err.println("错误：添加记录需要提供 -n, -i, -m, -b 参数");
            return;
        }

        // 创建新记录
        Person newPerson = new Person();
        newPerson.setName(options.getOrDefault("-n", options.get("--name")));
        newPerson.setStudentId(options.getOrDefault("-i", options.get("--id")));
        newPerson.setPhoneNumber(options.getOrDefault("-m", options.get("--mobile")));
        newPerson.setInterest(options.getOrDefault("-b", options.get("--hobby")));

        // 添加新记录
        PersonAppUtils.addPerson(newPerson);

        System.out.println("成功：添加记录完成");
    }

    // 处理删除操作
    private static void handleDelete(Map<String, String> options) {
        // 获取学号
        String id = options.getOrDefault("-i", options.get("--id"));
        if (id == null) {
            System.err.println("错误：删除记录需要提供 -i 参数");
            return;
        }

        // 删除记录
        boolean success = PersonAppUtils.deletePerson(id);
        if (success) {
            System.out.println("成功：删除记录完成");
        } else {
            System.err.println("错误：学号不存在");
        }
    }

    // 处理更新操作
    private static void handleUpdate(Map<String, String> options) {
        // 获取学号
        String id = options.getOrDefault("-i", options.get("--id"));
        if (id == null) {
            System.err.println("错误：更新记录需要提供 -i 参数");
            return;
        }

        // 检查是否有更新内容
        if (!options.containsKey("-n") && !options.containsKey("--name") &&
                !options.containsKey("-m") && !options.containsKey("--mobile") &&
                !options.containsKey("-b") && !options.containsKey("--hobby")) {
            System.err.println("错误：更新记录需要提供至少一个更新字段");
            return;
        }

        // 获取原始记录
        List<Person> persons = PersonAppUtils.getAllPersons();
        Optional<Person> personOptional = persons.stream()
                .filter(p -> p.getStudentId().equals(id))
                .findFirst();

        if (!personOptional.isPresent()) {
            System.err.println("错误：学号不存在");
            return;
        }

        Person originalPerson = personOptional.get();
        Person updatedPerson = new Person();
        updatedPerson.setStudentId(id);

        // 只更新提供的字段
        updatedPerson.setName(options.containsKey("-n") || options.containsKey("--name")
                ? options.getOrDefault("-n", options.get("--name"))
                : originalPerson.getName());
        updatedPerson.setPhoneNumber(options.containsKey("-m") || options.containsKey("--mobile")
                ? options.getOrDefault("-m", options.get("--mobile"))
                : originalPerson.getPhoneNumber());
        updatedPerson.setInterest(options.containsKey("-b") || options.containsKey("--hobby")
                ? options.getOrDefault("-b", options.get("--hobby"))
                : originalPerson.getInterest());

        // 更新记录
        boolean success = PersonAppUtils.updatePerson(id, updatedPerson);
        if (success) {
            System.out.println("成功：更新记录完成");
        } else {
            System.err.println("错误：学号不存在");
        }
    }

    // 处理列出操作
    private static void handleList(Map<String, String> options) {
        // 获取排序方式
        String mode = "descend";
        if (options.containsKey("-l") || options.containsKey("--list")) {
            String listOption = options.getOrDefault("-l", options.get("--list"));
            if (listOption != null && (listOption.equalsIgnoreCase("ascend") || listOption.equalsIgnoreCase("descend"))) {
                mode = listOption;
            }
        }

        // 获取排序后的记录
        List<Person> persons = PersonAppUtils.sortPersons("ascend".equalsIgnoreCase(mode));

        // 输出记录
        System.out.println("所有记录：");
        System.out.println("------------------------------------------------------------------------");
        System.out.printf("%-10s %-20s %-20s %-15s%n", "学号", "姓名", "手机号", "兴趣爱好");
        System.out.println("------------------------------------------------------------------------");
        for (Person person : persons) {
            System.out.printf("%-10s %-20s %-20s %-15s%n",
                    person.getStudentId(),
                    person.getName(),
                    person.getPhoneNumber(),
                    person.getInterest());
        }
        System.out.println("------------------------------------------------------------------------");
    }
}