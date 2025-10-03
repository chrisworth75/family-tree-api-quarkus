# Family Tree API (Quarkus)

A RESTful API for managing family trees, built with Quarkus and Panache.

## Technologies

- **Java 17**
- **Quarkus 3.6.0**
- **Hibernate ORM with Panache**
- **PostgreSQL**
- **Maven**
- **REST Assured** (for API testing)
- **Docker**

## Features

- Create and manage family trees
- Add family members with detailed information
- Define relationships between family members
- Panache Active Record pattern for simplified database operations
- RESTful API endpoints
- Docker containerization
- Jenkins CI/CD pipeline with REST Assured tests
- Fast startup time with Quarkus

## API Endpoints

- `POST /api/trees` - Create a new family tree
- `GET /api/trees` - Get all family trees
- `GET /api/trees/{treeId}` - Get a specific tree
- `DELETE /api/trees/{treeId}` - Delete a tree
- `POST /api/trees/{treeId}/members` - Add a member to a tree
- `GET /api/trees/{treeId}/members` - Get all members in a tree
- `GET /api/trees/{treeId}/members/{memberId}` - Get a specific member
- `POST /api/trees/{treeId}/members/{memberId}/partner` - Add a partner relationship
- `POST /api/trees/{treeId}/members/{memberId}/children` - Add a child relationship
- `GET /api/trees/{treeId}/members/{memberId}/relationships` - Get member relationships
- `GET /health` - Health check endpoint

## Running Locally

```bash
# Build the application
mvn clean package

# Run with Quarkus
mvn quarkus:dev

# Or run the JAR
java -jar target/quarkus-app/quarkus-run.jar
```

## Docker

```bash
# Build image
docker build -t family-tree-api-quarkus .

# Run container
docker run -p 3000:3000 \
  -e DB_HOST=localhost \
  -e DB_PORT=5432 \
  -e DB_NAME=familytree \
  -e DB_USER=postgres \
  -e DB_PASSWORD=postgres \
  family-tree-api-quarkus
```

## Environment Variables

- `PORT` - Server port (default: 3000)
- `DB_HOST` - Database host (default: localhost)
- `DB_PORT` - Database port (default: 5432)
- `DB_NAME` - Database name (default: familytree)
- `DB_USER` - Database user (default: postgres)
- `DB_PASSWORD` - Database password (default: postgres)

## Testing

Uses **REST Assured** for API testing:

```bash
mvn test
```

## CI/CD

Jenkins pipeline includes:
- Maven build
- Docker image creation
- Database deployment
- Health checks
- REST Assured API tests

## Quarkus Benefits

- Supersonic Subatomic Java
- Fast startup time (~0.05s)
- Low memory footprint
- Developer joy with live reload
- Optimized for containers and cloud
