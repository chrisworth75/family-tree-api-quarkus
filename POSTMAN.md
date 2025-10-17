# Postman Collection Generation

This project automatically generates a Postman collection during the Maven build process.

## How it Works

The `generate-collection.js` Node.js script analyzes the API endpoints and creates a comprehensive Postman collection with:
- All API endpoints organized into logical folders
- Test assertions for each endpoint
- Variable placeholders for dynamic values (treeId, memberId, etc.)
- Proper HTTP methods and request bodies
- JAX-RS specific response codes (201 for created, 204 for no content)

## Generated Collection Location

The collection is generated at: `target/postman/family-tree-api-quarkus.postman_collection.json`

## Manual Generation

To manually generate the collection without building the entire project:

```bash
node generate-collection.js
```

## Automatic Generation

The collection is automatically generated during the Maven `prepare-package` phase:

```bash
mvn clean package
```

Or with Quarkus:

```bash
mvn clean quarkus:build
```

## Using the Collection

### Import into Postman
1. Open Postman
2. Click "Import"
3. Select the generated file: `target/postman/family-tree-api-quarkus.postman_collection.json`
4. Set the `baseUrl` variable to your API URL (default: `http://localhost:8080`)

### Run with Newman
```bash
# Install Newman (if not already installed)
npm install -g newman

# Run the collection
newman run target/postman/family-tree-api-quarkus.postman_collection.json \
  --env-var "baseUrl=http://localhost:8080"
```

### CI/CD Integration

The collection generation is integrated into the Maven build, so it will automatically run in Jenkins or any CI/CD pipeline that builds the project with Maven.

## Collection Contents

- **Health**: Health check endpoint
- **Family Trees**: CRUD operations for family trees (with Panache)
- **Family Members**: Managing family members
- **Relationships**: Partner and child relationships

Each endpoint includes test assertions to verify:
- Status codes (following REST conventions: 200, 201, 204, 404)
- Response structure
- Required fields

## Differences from Spring Boot Version

- Uses JAX-RS style status codes (201 for POST, 204 for DELETE)
- Request body fields match the Quarkus/Panache entity structure
- Relationship responses match the Quarkus API structure
