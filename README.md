# 🤝 Collapp Backend

REST API backend for Collapp, a professional collaboration platform for creatives in the audiovisual industry (makeup, photography/video, modeling/talent, and production). It allows users to register, publish project offers, apply to them, and manage received applications.

## 🔗 Repositories:

Frontend: [https://github.com/Collapp-Final-Project/collapp-frontend]
Backend: [https://github.com/Collapp-Final-Project/collapp-backend]

## 🛠️ Tech Stack

- Java 25
- Spring Boot 3.5.16 (Web, Data JPA, Security, Validation)
- PostgreSQL (production) / H2 (tests)
- JWT (jjwt 0.12.6) — stateless authentication
- Maven (wrapper included)
- Lombok

## ✅ Prerequisites

- Java 25
- PostgreSQL (default port: 5432)
- Maven (optional, the `./mvnw` wrapper is included)

## ⚙️ Configuration

Copy the `.env.example` file to `.env` and configure the environment variables:

```bash
cp .env.example .env
```

```
DB_URL=jdbc:postgresql://localhost:5432/collapp_app
DB_USER=postgres
DB_PASSWORD=your_password
JWT_SECRET=your_jwt_secret
```

| Variable | Description | Default |
|---|---|---|
| `DB_URL` | PostgreSQL connection URL | `jdbc:postgresql://localhost:5432/collapp_app` |
| `DB_USER` | PostgreSQL username | — |
| `DB_PASSWORD` | PostgreSQL password | — |
| `JWT_SECRET` | Secret key for signing JWT tokens | — |

The application automatically loads these variables thanks to `spring-dotenv`.

Make sure to create the database in PostgreSQL before starting:

```sql
CREATE DATABASE collapp_app;
```

## 🚀 Installation and Running

```bash
# Clone the repository
git clone <repo-url>
cd collapp-backend

# Configure .env
cp .env.example .env
# Edit .env with your credentials

# Run the application (development)
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

## 🧪 Tests

```bash
./mvnw test
```

Tests use an in-memory H2 database (`application-test.properties`), no PostgreSQL required.

## 🏗️ Architecture Diagram

```
┌─────────────────────────────────────────────────────────┐
│                      HTTP CLIENT                          │
│                 (React/Vite on :5173)                    │
└─────────────────────┬───────────────────────────────────┘
                      │
              ┌───────▼────────┐
              │  Controllers   │  ← DTOs, @Valid
              │  (REST API)    │
              └───────┬────────┘
                      │
              ┌───────▼────────┐
              │   Services     │  ← Business logic
              │   (Interfaces) │     Authorization
              └───────┬────────┘
                      │
              ┌───────▼────────┐
              │  Repositories  │  ← Spring Data JPA
              │   (JPA)        │
              └───────┬────────┘
                      │
              ┌───────▼────────┐
              │   PostgreSQL   │  ← Database
              └────────────────┘

┌─────────────────────────────────────────────────────────┐
│                      SECURITY                             │
│                                                         │
│  JwtAuthFilter ──► SecurityConfig ──► BCrypt + JWT      │
│  (OncePerRequestFilter)    (antMatchers, CORS)          │
└─────────────────────────────────────────────────────────┘
```

## 🗄️ Database Diagram

**users**
| Field | Type | Notes |
|---|---|---|
| id | BIGINT | PK |
| full_name | VARCHAR | |
| email | VARCHAR | UK |
| username | VARCHAR | UK |
| password_hash | VARCHAR | |
| system_role | VARCHAR | |
| specialty | VARCHAR | |
| bio | TEXT | |
| portfolio_url | VARCHAR | |
| instagram_url | VARCHAR | |
| avatar_url | VARCHAR | |
| created_at | TIMESTAMP | |

**offers**
| Field | Type | Notes |
|---|---|---|
| id | BIGINT | PK |
| creator_id | BIGINT | FK |
| title | VARCHAR | |
| description | TEXT | |
| category | VARCHAR | |
| location | VARCHAR | |
| start_date | DATE | |
| end_date | DATE | |
| compensation_type | VARCHAR | |
| status | VARCHAR | |
| created_at | TIMESTAMP | |

**applications**
| Field | Type | Notes |
|---|---|---|
| id | BIGINT | PK |
| offer_id | BIGINT | FK |
| applicant_id | BIGINT | FK |
| message | TEXT | |
| status | VARCHAR | |
| created_at | TIMESTAMP | |

Relationships: a user **creates** offers, **applies** to offers, and **receives** applications.

## 📡 API Endpoints

### 🔐 Auth
| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/api/auth/register` | No | Register user (CREATIVE role) |
| POST | `/api/auth/login` | No | Login, returns JWT token |

### 👤 Users
| Method | Endpoint | Auth | Description |
|---|---|---|---|
| GET | `/api/users/me` | Yes | Authenticated user's profile |
| PUT | `/api/users/me` | Yes | Update profile |

### 📋 Offers
| Method | Endpoint | Auth | Description |
|---|---|---|---|
| GET | `/api/offers` | No | List public offers (paginated, optional `?category=` filter) |
| GET | `/api/offers/{id}` | No | Offer detail |
| GET | `/api/offers/mine` | Yes | Offers created by the current user |
| GET | `/api/offers/all` | ADMIN | All offers (admin) |
| GET | `/api/offers/{id}/applications` | Yes (owner) | Applications received for an offer |
| POST | `/api/offers` | Yes | Create offer |
| PUT | `/api/offers/{id}` | Yes (owner) | Edit offer |
| DELETE | `/api/offers/{id}` | Yes (owner/admin) | Delete offer |
| PATCH | `/api/offers/{id}/status` | Yes (owner) | Change status (`OPEN` / `PAUSED` / `COVERED`) |

### 📨 Applications
| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/api/applications` | Yes | Apply to an offer |
| GET | `/api/applications/mine` | Yes | My applications |
| PATCH | `/api/applications/{id}/status` | Yes (offer owner) | Accept/reject application (`ACCEPTED` / `REJECTED`) |

## 📏 Business Rules

- You cannot apply to your own offers
- You cannot apply twice to the same offer
- Only the offer's creator can view and manage its applications
- When an application is accepted, the applicant's email and Instagram are revealed

## 📁 Project Structure

```
src/main/java/com/collapp/project/
├── CollappApplication.java          # Entry point
├── config/
│   └── SecurityConfig.java          # Spring Security + CORS
├── controller/
│   ├── AuthController.java
│   ├── UserController.java
│   ├── OfferController.java
│   └── ApplicationController.java
├── dto/
│   ├── auth/                        # RegisterRequest, LoginRequest, AuthResponse
│   ├── user/                        # UserResponse, UserUpdateRequest
│   ├── offer/                       # OfferRequest, OfferResponse, OfferStatusRequest
│   └── application/                 # ApplicationRequest, ApplicationResponse, ApplicationStatusRequest
├── entity/
│   ├── User.java
│   ├── Offer.java
│   ├── Application.java
│   └── enums/                       # SystemRole, Specialty, ApplicationStatus, OfferStatus, CompensationType
├── exception/
│   ├── ResourceNotFoundException.java
│   ├── ForbiddenOperationException.java
│   ├── DuplicateApplicationException.java
│   ├── SelfApplicationException.java
│   └── GlobalExceptionHandler.java
├── mapper/
│   ├── UserMapper.java
│   ├── OfferMapper.java
│   └── ApplicationMapper.java
├── repository/
│   ├── UserRepository.java
│   ├── OfferRepository.java
│   └── ApplicationRepository.java
├── seeder/
│   └── DataSeeder.java
├── security/
│   ├── JwtService.java
│   ├── JwtAuthFilter.java
│   ├── CustomUserDetails.java
│   ├── CustomUserDetailsService.java
│   └── AuthHelper.java
└── service/
    ├── AuthService.java
    ├── UserService.java
    ├── OfferService.java
    ├── ApplicationService.java
    └── impl/                        # Implementations
```

## 🧑‍🤝‍🧑 Roles and Specialties

### Roles
| Role | Description |
|---|---|
| `ROLE_CREATIVE` | Standard user. Creates offers and applies to them. |
| `ROLE_ADMIN` | Administrator. Can view and delete any offer. |

### Specialties
| Specialty | Description |
|---|---|
| `MAKEUP` | Makeup |
| `PHOTOGRAPHY_VIDEO` | Photography and video |
| `MODEL_TALENT` | Modeling and talent |
| `PRODUCTION` | Production |

## 🌱 Seeders

On startup with an empty database, 3 test users are automatically created:

| User | Email | Password | Role | Specialty |
|---|---|---|---|---|
| admin | admin@collapp.com | 12345678 | ADMIN | PRODUCTION |
| lola_sfx | lola@makeup.com | 12345678 | CREATIVE | MAKEUP |
| alex_photo | alex@photo.com | 12345678 | CREATIVE | PHOTOGRAPHY_VIDEO |

## 🌐 CORS

Configured to accept requests from `http://localhost:5173` (React/Vite frontend).

---

Made by **Andrea Tapia** 💜