package spring.forlazy.devhub.plugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StringHandle {
	public static String toFolderPath(String input) {
		if (input == null || input.isEmpty()) {
			return "";
		}
		return input.replace('.', '/');
	}

	public static List<String> splitByDot(String input) {
		if (input == null || input.isEmpty()) {
			return Arrays.asList(); // Trả về danh sách rỗng nếu chuỗi đầu vào là null hoặc rỗng
		}
		return Arrays.stream(input.split("\\.")).collect(Collectors.toList());
	}

	public static Map<String, String> formatString(String input) {
		Map<String, String> result = new HashMap<>();

		// Convert to camel case (MyApp)
		String camelCase = toCamelCase(input);
		result.put("[0]", camelCase);

		// Convert to lower case with hyphens replaced by underscores (my_app)
		String snakeCase = toSnakeCase(input);
		result.put("[1]", snakeCase);

		// Convert to lower case (myapp)
		String lowerCase = input.toLowerCase();
		result.put("[2]", lowerCase);

		return result;
	}

	private static String toCamelCase(String input) {
		String[] words = input.split("[-_]");
		StringBuilder camelCase = new StringBuilder();

		for (int i = 0; i < words.length; i++) {
			if (i == 0) {
				camelCase.append(words[i].substring(0, 1).toUpperCase()).append(words[i].substring(1).toLowerCase());
			} else {
				camelCase.append(words[i].substring(0, 1).toUpperCase()).append(words[i].substring(1).toLowerCase());
			}
		}

		return camelCase.toString();
	}

	private static String toSnakeCase(String input) {
		return input.toLowerCase().replace('-', '_');
	}

	public static String getControllerPath(String className) {
		return toFolderPath(className) + "/controller";
	}

	// Xử lý đường dẫn Entity
	public static String getEntityPath(String className) {
		return toFolderPath(className) + "/entity";
	}

	// Xử lý đường dẫn Repository
	public static String getRepositoryPath(String className) {
		return toFolderPath(className) + "/repository";
	}

	public static void main(String[] args) {
		String input = "my-app";
		Map<String, String> formattedStrings = formatString(input);

		for (Map.Entry<String, String> entry : formattedStrings.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
		String input2 = "spring.forlazy.devhub.plugin";

		// Chuyển chuỗi thành đường dẫn folder
		String folderPath = toFolderPath(input2);
		System.out.println("Folder Path: " + folderPath);

		// Chia chuỗi thành danh sách các chuỗi
		List<String> parts = splitByDot(input2);
		System.out.println("Parts: " + parts);

		String folderPath2 = toFolderPath(input);
		System.out.println("Folder Path: " + folderPath);

		// Chia chuỗi thành danh sách các chuỗi
		List<String> parts2 = splitByDot(input2);
		System.out.println("Parts: " + parts2);

		// Xử lý các loại đường dẫn theo loại lớp
		String controllerPath = getControllerPath(input2);
		String entityPath = getEntityPath(input2);
		String repositoryPath = getRepositoryPath(input2);

		System.out.println("Controller Path: " + controllerPath);
		System.out.println("Entity Path: " + entityPath);
		System.out.println("Repository Path: " + repositoryPath);
	}

}
