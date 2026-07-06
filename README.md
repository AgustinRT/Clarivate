# UserAPI - Spring Boot Service

A Java Spring Boot microservice developed using **Hexagonal Architecture (Ports and Adapters)**. This service acts as a proxy for retrieving random user data from an external provider with an integrated local database fallback mechanism for high availability and fault tolerance.

---

## 🚀 Key Features

- **Resilient External Integration**: Fetches random user profiles from the public [Random User Generator API](https://randomuser.me/).
- **Self-Healing Fallback Mechanism**: If the external API is unreachable, times out, or throws an error, the application transparently serves high-quality mock data from a local JSON repository (`sampleUsers.json`) to guarantee a 100% service uptime.
- **Hexagonal Architecture**: Absolute separation of core domain models, application orchestration logic, and infrastructure adapters (HTTP clients, REST Controllers, config, etc.).
- **Validation**: Strict boundary validation for input parameters (limits requests between 1 and 1000 users).
- **Interactive API Documentation**: Out-of-the-box integration with Springdoc OpenAPI and Swagger UI.
- **Robust Testing**: Complete unit and integration test coverage verifying normal operations, API exceptions, and external network failure states.

---

## 🏗️ Architectural Design

The project strictly follows the **Hexagonal Architecture** pattern, dividing code into three concentric rings:

```
                  +----------------------------------------------+
                  |                 Infrastructure               |
                  |                                              |
                  |   +--------------------------------------+   |
                  |   |             Application              |   |
                  |   |                                      |   |
                  |   |   +------------------------------+   |   |
                  |   |   |            Domain            |   |   |
                  |   |   |                              |   |   |
                  |   |   |   [User]       [Location]    |   |   |
                  |   |   |   [Gender]     [Exceptions]  |   |   |
                  |   |   +------------------------------+   |   |
                  |   |                   ^                  |   |
                  |   |             [ApiUserPort]            |   |
                  |   |                   ^                  |   |
                  |   |             [UserAdapter]            |   |
                  |   +--------------------------------------+   |
                  |                       ^                      |
                  |  [RandomUserController]  [StaticUsersRequester]|
                  |  [RandomUserRequester]   [SourceProperties]  |
                  +----------------------------------------------+
```

### 1. Domain Layer (`com.clarivate.UserAPI.domain`)
The core of the system. It has **zero dependencies** on external libraries, frameworks, or database technologies.
- **Models / Records**: Immutable Java Records representing user profiles:
  - `User`: Root entity mapping gender, name, location, email, login details, registered status, profile pictures, and nationality.
  - `Location`, `Street`, `Coordinates`, `Timezone`, `Login`, `Picture`, `TimeProgresed`: Hierarchical components of the `User` aggregate.
- **Enums**: `Gender` representing user classifications.
- **Exceptions**:
  - `InvalidParamRequest`: Thrown when boundaries or constraints are violated (e.g., request < 1 or > 1000).
  - `UserServiceFail`: Generic business exception for errors originating inside underlying data sources.

### 2. Application Layer (`com.clarivate.UserAPI.application`)
Contains the application services/adapters coordinating the flow of data to and from the domain.
- **Ports (Inbound)**: `ApiUserPort` defines the interface used by REST controllers to fetch random users.
- **Adapters**: `UserAdapter` orchestrates the fetching strategy:
  1. Requests `request` number of users from the remote `RandomUserPort`.
  2. If the remote port fails (e.g., due to a `ResourceAccessException` or generic `UserServiceFail`), it falls back to local data via `StaticUserPort`.
  3. If the remote port succeeds but returns fewer records than requested, it gracefully supplements the remaining list with randomized records from the local static port.

### 3. Infrastructure Layer (`com.clarivate.UserAPI.infraestructure`)
Implements adapters for external systems, configurations, framework components, and secondary ports.
- **REST API Controller**:
  - `RandomUserController`: Exposes the HTTP endpoint `GET /api/random-users/list-users`.
  - Configures Swagger UI documentation through `@Tag` and `@ApiResponses`.
  - Incorporates global exception handling using `@ExceptionHandler` for `InvalidParamRequest`.
- **Sources of Truth (`sourceOfTrue`)**:
  - `SourceOfTrueProperties`: Loads environment variables and configurations under the prefix `userapi`.
  - `RandomUserPort` & `StaticUserPort`: Boundary ports defining requirements for data source adaptors.
  - `RandomUserRequester`: Implementation of `RandomUserPort` utilizing Spring's `RestTemplate` to request external API data.
  - `StaticUsersRequester`: Implementation of `StaticUserPort` that reads, parses, and caches `sampleUsers.json` using Jackson with `JavaTimeModule` support. Returns random sub-lists from the cache on demand.
- **Configuration**:
  - `UserApiRequestConfig`: Defines the `RestTemplate` Bean used for external requests.

---

## 🔄 Resiliency & Fallback Workflow

When an API client hits the endpoint `GET /api/random-users/list-users?request=10`, the system acts as follows:

```
              +---------------------------+
              |   Client REST Request     |
              +-------------+-------------+
                            |
              +-------------v-------------+
              |    RandomUserController   |
              +-------------+-------------+
                            |
              +-------------v-------------+
              |        UserAdapter        |
              +-------------+-------------+
                            |
             /--------------v--------------\
            /  Attempt Remote API Request   \
            \    via RandomUserRequester    /
             \--------------+--------------/
                            |
             +--------------+--------------+
             |                             |
      (Remote OK?)                  (Remote Fails?)
             |                             |
   +---------v---------+         +---------v---------+
   | Got N users.      |         | ResourceAccess/   |
   | Is N == requested?|         | Service Exception |
   +---------+---------+         +---------+---------+
             |                             |
     +-------+-------+                     |
     |               |                     |
  (Yes)             (No)                   |
     |               |                     |
+----v-----+   +-----v-----+               |
| Return   |   | Supplement|               |
| Remote   |   | from Local|               |
| Users    |   | Static DTO|               |
+----------+   +-----+-----+               |
                     |                     |
                     +<--------------------+
                     |
           +---------v---------+
           | Serve Fallback/   |
           | Aggregated Users  |
           +-------------------+
```

---

## 🛠️ Configuration Properties

Configured in `src/main/resources/application.yaml`:

| Property | Default Value | Description |
| :--- | :--- | :--- |
| `spring.application.name` | `UserAPI` | Application identifier |
| `springdoc.swagger-ui.path` | `/swagger-ui.html` | Path to Swagger interactive dashboard |
| `springdoc.api-docs.path` | `/api-docs` | Path to OpenAPI metadata endpoint |
| `userapi.basepath` | `https://randomuser.me/api/` | Base URL of the remote random user service |
| `userapi.minimal` | `0` | Minimum allowed value for requested users count |
| `userapi.max` | `1000` | Maximum allowed value for requested users count |

---

## 📌 API Endpoints

### 1. Get Random Users
Returns a list of random user profiles, either from the remote API or locally stored fallback data.

- **URL**: `/api/random-users/list-users`
- **Method**: `GET`
- **Query Parameters**:
  - `request` *(Integer, Required)*: Number of random users to return. Must be between `1` and `1000` (inclusive).
- **Responses**:
  - `200 OK`: Returns a `ResponseUsersApi` object, with a list of `User` objects and a `InfoResponse` object.
  - `400 Bad Request`: Triggered if the `request` parameter is missing, non-numeric, or outside the range `[1, 1000]`.

#### Sample Response (Truncated)
```json
[
  {
    "gender": "male",
    "name": {
      "title": "Mr",
      "first": "John",
      "last": "Doe"
    },
    "location": {
      "street": {
        "number": 123,
        "name": "Main Street"
      },
      "city": "Springfield",
      "state": "Illinois",
      "country": "United States",
      "postcode": "62701",
      "coordinates": {
        "latitude": "39.7817",
        "longitude": "-89.6501"
      },
      "timezone": {
        "offset": "-06:00",
        "description": "Central Time"
      }
    },
    "email": "john.doe@example.com",
    "login": {
      "uuid": "a879946d-9614-41bd-8c46-9f170cf48bb8",
      "username": "johndoe",
      "password": "secretpassword",
      "salt": "xyz123",
      "md5": "...",
      "sha1": "...",
      "sha256": "..."
    },
    "dob": {
      "date": "1990-01-01T00:00:00.000Z",
      "age": 36
    },
    "registered": {
      "date": "2015-05-15T00:00:00.000Z",
      "age": 11
    },
    "phone": "555-0199",
    "cell": "555-0100",
    "id": {
      "name": "SSN",
      "value": "000-12-3456"
    },
    "picture": {
      "large": "https://randomuser.me/api/portraits/men/1.jpg",
      "medium": "https://randomuser.me/api/portraits/med/men/1.jpg",
      "thumbnail": "https://randomuser.me/api/portraits/thumb/men/1.jpg"
    },
    "nat": "US"
  }
]
```

### 2. Swagger Interactive UI
- **URL**: `http://localhost:8080/swagger-ui.html`
- Useful for executing API requests interactively in a web browser.

---

## 📂 Project Directory Structure

```text
UserAPI/
├── src/
│   ├── main/
│   │   ├── java/com/clarivate/UserAPI/
│   │   │   ├── UserApiApplication.java                  # Main Spring Boot entry point
│   │   │   ├── domain/                                  # Domain core (framework-free)
│   │   │   │   ├── enums/                               # Domain enums (Gender)
│   │   │   │   ├── exceptions/                          # Business exceptions (InvalidParamRequest, UserServiceFail)
│   │   │   │   └── model/                               # Entities/Records (User, Location, Street, etc.)
│   │   │   ├── application/                             # Hexagonal ports & orchestration
│   │   │   │   ├── portIn/                              # API Use Case Ports
│   │   │   │   └── adapter/                             # Orchestration Implementation (UserAdapter)
│   │   │   └── infraestructure/                         # System adapters
│   │   │       ├── config/                              # Beans config (RestTemplate)
│   │   │       ├── restAPI/controller/                  # REST Controllers & mappings
│   │   │       └── sourceOfTrue/                        # Source of truth implementations
│   │   │           ├── dto/                             # Data Transfer Objects (RandomUsersDTO)
│   │   │           ├── externalRandomUser/              # Remote API client (RandomUserRequester)
│   │   │           ├── portOut/                         # Source interface ports
│   │   │           └── staticUsers/                     # Local fallback service (StaticUsersRequester)
│   │   └── resources/
│   │       ├── application.yaml                         # Configuration profile
│   │       └── sampleUsers.json                         # 20 high-quality local fallback user records
│   └── test/                                            # Complete unit/integration suite
│       ├── java/com/clarivate/UserAPI/
│       │   ├── application/                             # Tests for orchestration and fallback business rules
│       │   └── infraestructure/                         # Tests for controllers and infrastructure ports
│       └── resources/
│           ├── application.yaml
│           ├── response.json
│           └── sampleUsers.json
├── pom.xml                                              # Maven project descriptor (Java 25, SB 4.1.0)
└── mvnw / mvnw.cmd                                      # Maven wrappers
```

---

## 🏗️ Building and Running the Application

### Prerequisites
- **Java Development Kit (JDK)**: Version 21 or later.
- **Apache Maven**: Version 3.8+ (or use the provided Maven wrapper `mvnw`).

### Build the Project
To compile the source code, validate files, and package the jar:
```bash
./mvnw clean package -DskipTests
```

### Run Tests
To run all automated JUnit tests, verifying both API endpoint routing and fallback logic:
```bash
./mvnw test
```

### Run the Application locally
To run the server locally on port `8080`:
```bash
./mvnw spring-boot:run
```
Once started, you can access:
- **API Endpoint**: `http://localhost:8080/api/random-users/list-users?request=10`
- **Swagger Documentation**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI OpenAPI Spec**: `http://localhost:8080/api-docs`

---

## 🧪 Testing Coverage Strategy

The application includes unit and integration tests to ensure maximum reliability:
1. **Controller Layer Tests (`RandomUserControllerTest`)**: Checks request routing, parameter verification, query-param binding, and validation limits (`@Min`/`@Max`).
2. **Adapter Layer Tests (`UserAdapterTest`)**: Ensures that when external requests fail or return partial results, the fallback is triggered, filling the list correctly up to the exact requested size.
3. **HTTP Requester Tests (`RandomUserRequestTest`)**: Validates successful REST request mapping, and handles exceptions like HTTP `400` or `500` and resource access issues (timeouts/offline errors).
4. **Static Cache Tests (`StaticUsersRequesterTest`)**: Ensures the local `sampleUsers.json` matches record mappings and correctly samples subsets of variable sizes.
