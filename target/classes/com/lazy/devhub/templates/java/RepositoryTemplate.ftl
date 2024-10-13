package ${packageName};

import org.springframework.data.jpa.repository.JpaRepository;
import java.io.Serializable;
import ${entityPackage}.${entityName};

public interface ${entityName}Repository extends JpaRepository<${entityName}, Serializable> {
}
