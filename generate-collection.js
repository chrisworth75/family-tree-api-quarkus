#!/usr/bin/env node

const fs = require('fs');
const path = require('path');

/**
 * Postman Collection Generator for Family Tree API (Quarkus)
 *
 * This script generates a Postman collection by analyzing the Quarkus resources
 * and extracting endpoint information from JAX-RS annotations.
 */

const BASE_URL = '{{baseUrl}}';
const COLLECTION_NAME = 'Family Tree API - Quarkus';
const OUTPUT_DIR = path.join(__dirname, 'target', 'postman');
const OUTPUT_FILE = path.join(OUTPUT_DIR, 'family-tree-api-quarkus.postman_collection.json');

// Ensure output directory exists
if (!fs.existsSync(OUTPUT_DIR)) {
  fs.mkdirSync(OUTPUT_DIR, { recursive: true });
}

// Define the Postman collection structure
const collection = {
  info: {
    name: COLLECTION_NAME,
    description: 'Family Tree REST API using Quarkus and Panache',
    schema: 'https://schema.getpostman.com/json/collection/v2.1.0/collection.json'
  },
  variable: [
    {
      key: 'baseUrl',
      value: 'http://localhost:8080',
      type: 'string'
    }
  ],
  item: []
};

// Health Check folder
const healthFolder = {
  name: 'Health',
  item: [
    {
      name: 'Health Check',
      event: [
        {
          listen: 'test',
          script: {
            type: 'text/javascript',
            exec: [
              'pm.test("Status code is 200", function () {',
              '    pm.response.to.have.status(200);',
              '});',
              '',
              'pm.test("Response has status field", function () {',
              '    var jsonData = pm.response.json();',
              '    pm.expect(jsonData).to.have.property("status");',
              '    pm.expect(jsonData.status).to.eql("healthy");',
              '});'
            ]
          }
        }
      ],
      request: {
        method: 'GET',
        header: [],
        url: {
          raw: `${BASE_URL}/health`,
          host: [BASE_URL],
          path: ['health']
        }
      }
    }
  ]
};

// Family Tree Management folder
const treeFolder = {
  name: 'Family Trees',
  item: [
    {
      name: 'Create Family Tree',
      event: [
        {
          listen: 'test',
          script: {
            type: 'text/javascript',
            exec: [
              'pm.test("Status code is 201", function () {',
              '    pm.response.to.have.status(201);',
              '});',
              '',
              'pm.test("Response has tree properties", function () {',
              '    var jsonData = pm.response.json();',
              '    pm.expect(jsonData).to.have.property("id");',
              '    pm.expect(jsonData).to.have.property("name");',
              '    pm.environment.set("treeId", jsonData.id);',
              '});'
            ]
          }
        }
      ],
      request: {
        method: 'POST',
        header: [
          {
            key: 'Content-Type',
            value: 'application/json'
          }
        ],
        body: {
          mode: 'raw',
          raw: JSON.stringify({
            name: 'Johnson Family',
            createdBy: 'testuser',
            rootMember: {
              firstName: 'Robert',
              lastName: 'Johnson',
              gender: 'MALE',
              birthDate: '1960-01-15',
              isAlive: true
            }
          }, null, 2)
        },
        url: {
          raw: `${BASE_URL}/api/trees`,
          host: [BASE_URL],
          path: ['api', 'trees']
        }
      }
    },
    {
      name: 'Get All Trees',
      event: [
        {
          listen: 'test',
          script: {
            type: 'text/javascript',
            exec: [
              'pm.test("Status code is 200", function () {',
              '    pm.response.to.have.status(200);',
              '});',
              '',
              'pm.test("Response is an array", function () {',
              '    var jsonData = pm.response.json();',
              '    pm.expect(jsonData).to.be.an("array");',
              '});'
            ]
          }
        }
      ],
      request: {
        method: 'GET',
        header: [],
        url: {
          raw: `${BASE_URL}/api/trees`,
          host: [BASE_URL],
          path: ['api', 'trees']
        }
      }
    },
    {
      name: 'Get Tree by ID',
      event: [
        {
          listen: 'test',
          script: {
            type: 'text/javascript',
            exec: [
              'pm.test("Status code is 200", function () {',
              '    pm.response.to.have.status(200);',
              '});',
              '',
              'pm.test("Response has tree properties", function () {',
              '    var jsonData = pm.response.json();',
              '    pm.expect(jsonData).to.have.property("id");',
              '    pm.expect(jsonData).to.have.property("name");',
              '});'
            ]
          }
        }
      ],
      request: {
        method: 'GET',
        header: [],
        url: {
          raw: `${BASE_URL}/api/trees/{{treeId}}`,
          host: [BASE_URL],
          path: ['api', 'trees', '{{treeId}}']
        }
      }
    },
    {
      name: 'Get Tree with Members',
      event: [
        {
          listen: 'test',
          script: {
            type: 'text/javascript',
            exec: [
              'pm.test("Status code is 200", function () {',
              '    pm.response.to.have.status(200);',
              '});',
              '',
              'pm.test("Response has members", function () {',
              '    var jsonData = pm.response.json();',
              '    pm.expect(jsonData).to.have.property("members");',
              '});'
            ]
          }
        }
      ],
      request: {
        method: 'GET',
        header: [],
        url: {
          raw: `${BASE_URL}/api/trees/{{treeId}}/full`,
          host: [BASE_URL],
          path: ['api', 'trees', '{{treeId}}', 'full']
        }
      }
    },
    {
      name: 'Delete Tree',
      event: [
        {
          listen: 'test',
          script: {
            type: 'text/javascript',
            exec: [
              'pm.test("Status code is 204", function () {',
              '    pm.response.to.have.status(204);',
              '});'
            ]
          }
        }
      ],
      request: {
        method: 'DELETE',
        header: [],
        url: {
          raw: `${BASE_URL}/api/trees/{{treeId}}`,
          host: [BASE_URL],
          path: ['api', 'trees', '{{treeId}}']
        }
      }
    }
  ]
};

// Members folder
const membersFolder = {
  name: 'Family Members',
  item: [
    {
      name: 'Add Member to Tree',
      event: [
        {
          listen: 'test',
          script: {
            type: 'text/javascript',
            exec: [
              'pm.test("Status code is 201", function () {',
              '    pm.response.to.have.status(201);',
              '});',
              '',
              'pm.test("Response has member properties", function () {',
              '    var jsonData = pm.response.json();',
              '    pm.expect(jsonData).to.have.property("id");',
              '    pm.expect(jsonData).to.have.property("firstName");',
              '    pm.expect(jsonData).to.have.property("lastName");',
              '    pm.environment.set("memberId", jsonData.id);',
              '});'
            ]
          }
        }
      ],
      request: {
        method: 'POST',
        header: [
          {
            key: 'Content-Type',
            value: 'application/json'
          }
        ],
        body: {
          mode: 'raw',
          raw: JSON.stringify({
            firstName: 'Sarah',
            lastName: 'Johnson',
            gender: 'FEMALE',
            birthDate: '1962-08-22',
            isAlive: true
          }, null, 2)
        },
        url: {
          raw: `${BASE_URL}/api/trees/{{treeId}}/members`,
          host: [BASE_URL],
          path: ['api', 'trees', '{{treeId}}', 'members']
        }
      }
    },
    {
      name: 'Get All Members',
      event: [
        {
          listen: 'test',
          script: {
            type: 'text/javascript',
            exec: [
              'pm.test("Status code is 200", function () {',
              '    pm.response.to.have.status(200);',
              '});',
              '',
              'pm.test("Response is an array", function () {',
              '    var jsonData = pm.response.json();',
              '    pm.expect(jsonData).to.be.an("array");',
              '});'
            ]
          }
        }
      ],
      request: {
        method: 'GET',
        header: [],
        url: {
          raw: `${BASE_URL}/api/trees/{{treeId}}/members`,
          host: [BASE_URL],
          path: ['api', 'trees', '{{treeId}}', 'members']
        }
      }
    },
    {
      name: 'Get Member by ID',
      event: [
        {
          listen: 'test',
          script: {
            type: 'text/javascript',
            exec: [
              'pm.test("Status code is 200", function () {',
              '    pm.response.to.have.status(200);',
              '});',
              '',
              'pm.test("Response has member properties", function () {',
              '    var jsonData = pm.response.json();',
              '    pm.expect(jsonData).to.have.property("id");',
              '    pm.expect(jsonData).to.have.property("firstName");',
              '});'
            ]
          }
        }
      ],
      request: {
        method: 'GET',
        header: [],
        url: {
          raw: `${BASE_URL}/api/trees/{{treeId}}/members/{{memberId}}`,
          host: [BASE_URL],
          path: ['api', 'trees', '{{treeId}}', 'members', '{{memberId}}']
        }
      }
    }
  ]
};

// Relationships folder
const relationshipsFolder = {
  name: 'Relationships',
  item: [
    {
      name: 'Add Partner',
      event: [
        {
          listen: 'test',
          script: {
            type: 'text/javascript',
            exec: [
              'pm.test("Status code is 201", function () {',
              '    pm.response.to.have.status(201);',
              '});',
              '',
              'pm.test("Response has relationship properties", function () {',
              '    var jsonData = pm.response.json();',
              '    pm.expect(jsonData).to.have.property("id");',
              '    pm.expect(jsonData).to.have.property("relationshipType");',
              '});'
            ]
          }
        }
      ],
      request: {
        method: 'POST',
        header: [
          {
            key: 'Content-Type',
            value: 'application/json'
          }
        ],
        body: {
          mode: 'raw',
          raw: JSON.stringify({
            partnerId: 2
          }, null, 2)
        },
        url: {
          raw: `${BASE_URL}/api/trees/{{treeId}}/members/{{memberId}}/partner`,
          host: [BASE_URL],
          path: ['api', 'trees', '{{treeId}}', 'members', '{{memberId}}', 'partner']
        }
      }
    },
    {
      name: 'Add Child',
      event: [
        {
          listen: 'test',
          script: {
            type: 'text/javascript',
            exec: [
              'pm.test("Status code is 201", function () {',
              '    pm.response.to.have.status(201);',
              '});',
              '',
              'pm.test("Response has parent and child relationships", function () {',
              '    var jsonData = pm.response.json();',
              '    pm.expect(jsonData).to.have.property("parentRelationship");',
              '    pm.expect(jsonData).to.have.property("childRelationship");',
              '});'
            ]
          }
        }
      ],
      request: {
        method: 'POST',
        header: [
          {
            key: 'Content-Type',
            value: 'application/json'
          }
        ],
        body: {
          mode: 'raw',
          raw: JSON.stringify({
            childId: 3
          }, null, 2)
        },
        url: {
          raw: `${BASE_URL}/api/trees/{{treeId}}/members/{{memberId}}/children`,
          host: [BASE_URL],
          path: ['api', 'trees', '{{treeId}}', 'members', '{{memberId}}', 'children']
        }
      }
    },
    {
      name: 'Get Relationships',
      event: [
        {
          listen: 'test',
          script: {
            type: 'text/javascript',
            exec: [
              'pm.test("Status code is 200", function () {',
              '    pm.response.to.have.status(200);',
              '});',
              '',
              'pm.test("Response has relationship collections", function () {',
              '    var jsonData = pm.response.json();',
              '    pm.expect(jsonData).to.have.property("asFirstMember");',
              '    pm.expect(jsonData).to.have.property("asSecondMember");',
              '});'
            ]
          }
        }
      ],
      request: {
        method: 'GET',
        header: [],
        url: {
          raw: `${BASE_URL}/api/trees/{{treeId}}/members/{{memberId}}/relationships`,
          host: [BASE_URL],
          path: ['api', 'trees', '{{treeId}}', 'members', '{{memberId}}', 'relationships']
        }
      }
    }
  ]
};

// Add all folders to collection
collection.item.push(healthFolder);
collection.item.push(treeFolder);
collection.item.push(membersFolder);
collection.item.push(relationshipsFolder);

// Write the collection to file
fs.writeFileSync(OUTPUT_FILE, JSON.stringify(collection, null, 2));

console.log(`\nPostman collection generated successfully!`);
console.log(`Location: ${OUTPUT_FILE}`);
console.log(`\nTo use this collection:`);
console.log(`1. Import into Postman: ${OUTPUT_FILE}`);
console.log(`2. Run with Newman: newman run ${OUTPUT_FILE}`);
console.log(`3. Set the baseUrl variable in Postman or via environment\n`);
