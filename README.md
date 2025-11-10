
# Full-Stack Seller Dashboard (Generated)

## What's included
- backend/ (Spring Boot, Maven)
- frontend/ (Vue 3 + Vite)
- sql/sellers.sql (Postgres schema + seed)
- docker-compose.yml (Postgres + backend service stub)

## How to import backend into STS
1. Open Spring Tool Suite (STS).
2. File -> Import -> Maven -> Existing Maven Projects.
3. Select `backend/` folder (contains pom.xml).
4. Ensure project SDK is set to Java 17.
5. Run `SellerDashboardApplication` (Run As -> Spring Boot App).

## How to run locally (recommended)
### Option A: Using Docker (recommended)
1. From project root where docker-compose.yml lives, run:
   ```bash
   docker-compose up -d --build
   ```
   This starts Postgres and builds/starts the backend container (backend build is a simple Maven Docker build; you may prefer to run backend locally in STS).

### Option B: Run Backend in STS + DB in Docker
1. Start DB only:
   ```bash
   docker-compose up -d db
   ```
2. Import backend into STS and update `src/main/resources/application.yml` if necessary.
3. Run backend in STS (Run As -> Spring Boot App).

### Run Frontend (Vue)
1. `cd frontend`
2. `npm install`
3. `npm run dev`
4. Open displayed Vite URL (usually http://localhost:5173)

## Notes
- The frontend proxies `/api` to `http://localhost:8080` by default (see vite.config.js).
- For development allow CORS on backend or use Vite proxy (proxy is configured).
- Tests use H2 in test scope (no Postgres required for unit tests).
