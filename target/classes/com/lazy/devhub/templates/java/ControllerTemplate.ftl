package ${packageName}.controller;

import ${entityPackage}.${entityName};
import ${packageName}.repository.${entityName}Repository;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import java.util.List;

@RestController
@RequestMapping(value = "/api/${entityName?lower_case}s", produces = MediaType.APPLICATION_JSON_VALUE)
public class ${entityName}Controller {

    private final ${entityName}Repository ${entityName?lower_case}Repository;

    public ${entityName}Controller(${entityName}Repository ${entityName?lower_case}Repository) {
        this.${entityName?lower_case}Repository = ${entityName?lower_case}Repository;
    }

    @GetMapping
    public ResponseEntity<List<${entityName}>> getAll() {
        return ResponseEntity.ok(${entityName?lower_case}Repository.findAll());
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<${entityName}> create(@RequestBody ${entityName} ${entityName?lower_case}) {
        return ResponseEntity.status(201).body(${entityName?lower_case}Repository.save(${entityName?lower_case}));
    }

    @GetMapping("/{id}")
    public ResponseEntity<${entityName}> getById(@PathVariable Long id) {
        return ${entityName?lower_case}Repository.findById(id)
            .map(entity -> ResponseEntity.ok(entity))
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<${entityName}> update(@PathVariable Long id, @RequestBody ${entityName} ${entityName?lower_case}) {
        if (!${entityName?lower_case}Repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        ${entityName}.setId(id); // Assuming there is a setId method
        return ResponseEntity.ok(${entityName?lower_case}Repository.save(${entityName?lower_case}));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!${entityName?lower_case}Repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        ${entityName?lower_case}Repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
