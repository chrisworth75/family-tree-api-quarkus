package com.familytree.api;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FamilyTreeResourceTest {

    private static Long treeId;
    private static Long memberId;
    private static Long childId;

    @Test
    @Order(1)
    public void testHealthEndpoint() {
        given()
            .when().get("/health")
            .then()
            .statusCode(200)
            .body("status", is("healthy"));
    }

    @Test
    @Order(2)
    public void testCreateFamilyTree() {
        String requestBody = """
            {
                "name": "Test Family Tree",
                "createdBy": "test-user",
                "rootMember": {
                    "firstName": "John",
                    "lastName": "Doe",
                    "birthDate": "1950-01-15",
                    "gender": "Male",
                    "isAlive": true
                }
            }
            """;

        treeId = given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .when()
            .post("/api/trees")
            .then()
            .statusCode(201)
            .body("name", is("Test Family Tree"))
            .body("createdBy", is("test-user"))
            .body("id", notNullValue())
            .extract()
            .path("id");
    }

    @Test
    @Order(3)
    public void testGetAllTrees() {
        given()
            .when()
            .get("/api/trees")
            .then()
            .statusCode(200)
            .body("size()", greaterThan(0))
            .body("[0].name", notNullValue());
    }

    @Test
    @Order(4)
    public void testGetTreeById() {
        given()
            .pathParam("treeId", treeId)
            .when()
            .get("/api/trees/{treeId}")
            .then()
            .statusCode(200)
            .body("id", is(treeId.intValue()))
            .body("name", is("Test Family Tree"));
    }

    @Test
    @Order(5)
    public void testAddMemberToTree() {
        String requestBody = """
            {
                "firstName": "Jane",
                "lastName": "Doe",
                "birthDate": "1952-03-20",
                "gender": "Female",
                "isAlive": true
            }
            """;

        memberId = given()
            .contentType(ContentType.JSON)
            .pathParam("treeId", treeId)
            .body(requestBody)
            .when()
            .post("/api/trees/{treeId}/members")
            .then()
            .statusCode(201)
            .body("firstName", is("Jane"))
            .body("lastName", is("Doe"))
            .body("id", notNullValue())
            .extract()
            .path("id");
    }

    @Test
    @Order(6)
    public void testGetMembersOfTree() {
        given()
            .pathParam("treeId", treeId)
            .when()
            .get("/api/trees/{treeId}/members")
            .then()
            .statusCode(200)
            .body("size()", greaterThan(0));
    }

    @Test
    @Order(7)
    public void testGetMemberById() {
        given()
            .pathParam("treeId", treeId)
            .pathParam("memberId", memberId)
            .when()
            .get("/api/trees/{treeId}/members/{memberId}")
            .then()
            .statusCode(200)
            .body("id", is(memberId.intValue()))
            .body("firstName", is("Jane"));
    }

    @Test
    @Order(8)
    public void testAddChildMember() {
        String requestBody = """
            {
                "firstName": "Bob",
                "lastName": "Doe",
                "birthDate": "1980-05-10",
                "gender": "Male",
                "isAlive": true
            }
            """;

        childId = given()
            .contentType(ContentType.JSON)
            .pathParam("treeId", treeId)
            .body(requestBody)
            .when()
            .post("/api/trees/{treeId}/members")
            .then()
            .statusCode(201)
            .body("firstName", is("Bob"))
            .extract()
            .path("id");
    }

    @Test
    @Order(9)
    public void testAddPartnerRelationship() {
        String requestBody = String.format("{\"partnerId\": %d}", memberId);

        given()
            .contentType(ContentType.JSON)
            .pathParam("treeId", treeId)
            .pathParam("memberId", 1) // Root member
            .body(requestBody)
            .when()
            .post("/api/trees/{treeId}/members/{memberId}/partner")
            .then()
            .statusCode(201)
            .body("relationshipType", is("PARTNER"));
    }

    @Test
    @Order(10)
    public void testAddChildRelationship() {
        String requestBody = String.format("{\"childId\": %d}", childId);

        given()
            .contentType(ContentType.JSON)
            .pathParam("treeId", treeId)
            .pathParam("memberId", memberId)
            .body(requestBody)
            .when()
            .post("/api/trees/{treeId}/members/{memberId}/children")
            .then()
            .statusCode(201)
            .body("parentRelationship", notNullValue())
            .body("childRelationship", notNullValue());
    }

    @Test
    @Order(11)
    public void testGetMemberRelationships() {
        given()
            .pathParam("treeId", treeId)
            .pathParam("memberId", memberId)
            .when()
            .get("/api/trees/{treeId}/members/{memberId}/relationships")
            .then()
            .statusCode(200)
            .body("asFirstMember", notNullValue())
            .body("asSecondMember", notNullValue());
    }

    @Test
    @Order(12)
    public void testGetNonExistentTree() {
        given()
            .pathParam("treeId", 99999)
            .when()
            .get("/api/trees/{treeId}")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(13)
    public void testGetNonExistentMember() {
        given()
            .pathParam("treeId", treeId)
            .pathParam("memberId", 99999)
            .when()
            .get("/api/trees/{treeId}/members/{memberId}")
            .then()
            .statusCode(404);
    }

    @Test
    @Order(14)
    public void testDeleteTree() {
        given()
            .pathParam("treeId", treeId)
            .when()
            .delete("/api/trees/{treeId}")
            .then()
            .statusCode(204);

        // Verify deletion
        given()
            .pathParam("treeId", treeId)
            .when()
            .get("/api/trees/{treeId}")
            .then()
            .statusCode(404);
    }
}
