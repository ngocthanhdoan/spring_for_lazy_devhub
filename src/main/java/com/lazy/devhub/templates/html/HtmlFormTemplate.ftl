<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${entityName} Management</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>

<h1>${entityName} Management</h1>

<!-- Form for creating or updating an entity -->
<form id="${entityName?lower_case}Form" style="display: none;">
<#list fields as field>
    <label for="${field.name}">${field.name}</label>
    <#if field.isRelationship>
        <select id="${field.name}" name="${field.name}">
            <option value="">--Select--</option>
            <!-- Populate relationship options dynamically -->
        </select>
    <#else>
        <input type="text" id="${field.name}" name="${field.name}" />
    </#if>
</#list>
    <button type="submit">Submit</button>
    <input type="hidden" id="entityId" name="entityId" />
</form>

<!-- Table for displaying the list of entities -->
<table id="${entityName?lower_case}Table">
    <thead>
        <tr>
<#list fields as field>
            <th>${field.name}</th>
</#list>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <!-- Data will be populated here -->
    </tbody>
</table>

<script>
$(document).ready(function() {
    // Load all entities
    loadEntities();

    // Submit form for Create or Update
    $('#${entityName?lower_case}Form').on('submit', function(e) {
        e.preventDefault();
        var entityId = $('#entityId').val();
        var url = entityId ? '/api/${entityName?lower_case}s/' + entityId : '/api/${entityName?lower_case}s';
        var type = entityId ? 'PUT' : 'POST';

        $.ajax({
            url: url,
            type: type,
            data: $(this).serialize(),
            success: function(response) {
                alert('Saved successfully!');
                loadEntities();
                clearForm();
            },
            error: function(xhr, status, error) {
                alert('Error: ' + xhr.responseText);
            }
        });
    });

    // Load all entities into the table
    function loadEntities() {
        $.ajax({
            url: '/api/${entityName?lower_case}s',
            type: 'GET',
            success: function(data) {
                var tableBody = $('#${entityName?lower_case}Table tbody');
                tableBody.empty(); // Clear existing data
                data.forEach(function(entity) {
                    var row = '<tr>';
<#list fields as field>
                    row += '<td>' + entity.${field.name} + '</td>';
</#list>
                    row += '<td>' +
                        '<button class="edit" data-id="' + entity.id + '">Edit</button>' +
                        '<button class="delete" data-id="' + entity.id + '">Delete</button>' +
                        '</td>' +
                        '</tr>';
                    tableBody.append(row);
                });

                // Attach click event for edit and delete buttons
                $('.edit').on('click', function() {
                    var id = $(this).data('id');
                    editEntity(id);
                });

                $('.delete').on('click', function() {
                    var id = $(this).data('id');
                    deleteEntity(id);
                });
            },
            error: function(xhr, status, error) {
                alert('Error loading entities: ' + xhr.responseText);
            }
        });
    }

    // Edit entity
    function editEntity(id) {
        $.ajax({
            url: '/api/${entityName?lower_case}s/' + id,
            type: 'GET',
            success: function(entity) {
                $('#entityId').val(entity.id);
<#list fields as field>
                $('#${field.name}').val(entity.${field.name});
</#list>
                $('#${entityName?lower_case}Form').show();
            },
            error: function(xhr, status, error) {
                alert('Error fetching entity: ' + xhr.responseText);
            }
        });
    }

    // Delete entity
    function deleteEntity(id) {
        if (confirm('Are you sure you want to delete this item?')) {
            $.ajax({
                url: '/api/${entityName?lower_case}s/' + id,
                type: 'DELETE',
                success: function() {
                    alert('Deleted successfully!');
                    loadEntities();
                },
                error: function(xhr, status, error) {
                    alert('Error deleting entity: ' + xhr.responseText);
                }
            });
        }
    }

    // Clear form
    function clearForm() {
        $('#${entityName?lower_case}Form')[0].reset();
        $('#entityId').val('');
        $('#${entityName?lower_case}Form').hide();
    }
});
</script>

</body>
</html>
