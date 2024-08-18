package spring.forlazy.devhub.generate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import spring.forlazy.devhub.config.EntityConfig;
import spring.forlazy.devhub.plugin.StringHandle;

public class Repository {
	private static String generateRepositoryFromTemplate(EntityConfig config, String templatePath) {
		try {
			// Đọc nội dung template từ file
			String templateContent = new String(Files.readAllBytes(Paths.get(templatePath)));

			// Tạo map để lưu trữ các giá trị cần thay thế
			Map<String, String> replacements = new HashMap<>();
			replacements.put("${packageName}", config.getPackageName());
			replacements.put("${className}", config.getClassName());
			replacements.put("${idType}", config.getIdType());

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

	public static void main(String[] args) {
		// Tạo một đối tượng EntityConfig (đã set các giá trị)
		EntityConfig entityConfig = new EntityConfig();
		entityConfig.setPackageName("com.example.myproject");
		entityConfig.setTableName("Users");
		entityConfig.setClassName("User");
		entityConfig.setIdType("UUID"); // Sử dụng UUID làm kiểu ID

		// Đường dẫn tới file template
		String repositoryPath = StringHandle.getRepositoryPath("source.template");
		String templatePath = "TemplateRepository.txt";

		// Gọi hàm tạo repository từ template
		String repositoryCode = generateRepositoryFromTemplate(entityConfig, repositoryPath + "/repository.txt");

		// In ra kết quả
		System.out.println(repositoryCode);
	}

}
