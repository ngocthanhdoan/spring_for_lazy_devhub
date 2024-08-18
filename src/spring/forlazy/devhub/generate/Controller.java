package spring.forlazy.devhub.generate;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import spring.forlazy.devhub.config.EntityConfig;
import spring.forlazy.devhub.plugin.StringHandle;

public class Controller {
	private static String generateControllerFromTemplate(EntityConfig config, String templatePath) {
		try {
			// Đọc nội dung template từ file
			String templateContent = new String(Files.readAllBytes(Paths.get(templatePath)));

			// Tạo map để lưu trữ các giá trị cần thay thế
			Map<String, String> replacements = new HashMap();
			replacements.put("${packageName}", config.getPackageName());
			replacements.put("${className}", config.getClassName());
			replacements.put("${idType}", config.getIdType());
			replacements.put("${tableName.toLowerCase()}", config.getTableName().toLowerCase());
			replacements.put("${className.toLowerCase()}", config.getClassName().toLowerCase());

			// Thay thế các giá trị trong template
			for (Map.Entry<String, String> entry : replacements.entrySet()) {
				templateContent = templateContent.replace(entry.getKey(), entry.getValue());
			}

			return templateContent;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {
		// Tạo một đối tượng EntityConfig (đã set các giá trị)
		EntityConfig entityConfig = new EntityConfig();
		entityConfig.setPackageName("com.example.myproject");
		entityConfig.setTableName("Users");
		entityConfig.setClassName("User");
		entityConfig.setIdType("UUID"); // Sử dụng UUID làm kiểu ID

		// Đường dẫn tới file template
		String templatePath = "/controller.txt";

		// Gọi hàm tạo controller từ template
		String controllerCode = generateControllerFromTemplate(entityConfig,
				StringHandle.getControllerPath("source.template")+templatePath);

		// In ra kết quả
		System.out.println(controllerCode);
	}
}
