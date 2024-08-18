package spring.forlazy.devhub.generate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import spring.forlazy.devhub.config.EntityConfig;

public class Service {
	private static String generateServiceFromTemplate(EntityConfig config, String templatePath) {
		try {
			// Đọc nội dung template từ file
			String templateContent = new String(Files.readAllBytes(Paths.get(templatePath)));

			// Tạo map để lưu trữ các giá trị cần thay thế
			Map<String, String> replacements = new HashMap<>();
			replacements.put("${packageName}", config.getPackageName());
			replacements.put("${className}", config.getClassName());
			replacements.put("${idType}", config.getIdType());
			replacements.put("${className.toLowerCase()}", config.getClassName().toLowerCase());

			// Thay thế các giá trị trong template
			for (Map.Entry<String, String> entry : replacements.entrySet()) {
				templateContent = templateContent.replace(entry.getKey(), entry.getValue());
			}

			return templateContent;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
