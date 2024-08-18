package spring.forlazy.devhub.config;
import java.util.List;

public class EntityConfig {
    private String packageName;
    private String tableName;
    private String className;
    private String idType; // Kiểu dữ liệu của ID
    private List<FieldConfig> fields;

    public static class FieldConfig {
        private String name;
        private String type;
        private String annotations;

        // Getters and Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAnnotations() {
            return annotations;
        }

        public void setAnnotations(String annotations) {
            this.annotations = annotations;
        }
    }

    // Getters and Setters
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public List<FieldConfig> getFields() {
        return fields;
    }

    public void setFields(List<FieldConfig> fields) {
        this.fields = fields;
    }
}

