package spring.forlazy.devhub.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class EntityConfigExample {

	public static void main(String[] args) {
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			// Đọc và chuyển đổi JSON thành đối tượng EntityConfig
			EntityConfig entityConfig = objectMapper.readValue(new File("entityConfig.json"), EntityConfig.class);

			// Truy cập các thuộc tính của đối tượng EntityConfig
			System.out.println("Package Name: " + entityConfig.getPackageName());
			System.out.println("Table Name: " + entityConfig.getTableName());
			System.out.println("Class Name: " + entityConfig.getClassName());

			System.out.println("Fields:");
			for (EntityConfig.FieldConfig field : entityConfig.getFields()) {
				System.out.println("    Name: " + field.getName());
				System.out.println("    Type: " + field.getType());
				System.out.println("    Annotations: " + field.getAnnotations());
				System.out.println();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
