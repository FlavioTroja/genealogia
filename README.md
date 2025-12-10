# Backend Genealogia - Istruzioni di Installazione

## 📁 Struttura dei file

Sostituisci/aggiungi questi file nel tuo progetto Spring Boot:

```
src/main/java/it/overzoom/genealogia/
├── model/
│   ├── User.java           ⬅️ NUOVO
│   └── Person.java         ⬅️ AGGIORNATO
├── repository/
│   ├── UserRepository.java      ⬅️ NUOVO
│   └── PersonRepository.java    ⬅️ AGGIORNATO
├── dto/
│   ├── LoginRequest.java        ⬅️ NUOVO
│   ├── RegisterRequest.java     ⬅️ NUOVO
│   └── AuthResponse.java        ⬅️ NUOVO
├── service/
│   ├── UserService.java         ⬅️ NUOVO
│   └── PersonService.java       ⬅️ AGGIORNATO
├── controller/
│   ├── AuthController.java      ⬅️ NUOVO
│   └── PersonController.java    ⬅️ AGGIORNATO
└── config/
    └── SecurityConfig.java      ⬅️ AGGIORNATO
```

## 🔧 Modifiche necessarie al build.gradle

Non servono nuove dipendenze! Tutto è già incluso.

## 🚀 Nuove funzionalità

### 1. Sistema di Autenticazione

- **POST** `/api/auth/register` - Registrazione utente
- **POST** `/api/auth/login` - Login utente
- **GET** `/api/auth/me` - Info utente corrente

### 2. Funzionalità Social

- **GET** `/api/persons/search?q=nome` - Cerca persone
- **GET** `/api/persons/matches?firstName=X&lastName=Y` - Trova potenziali match
- **GET** `/api/persons/by-user/{userId}` - Persone create da un utente
- **GET** `/api/persons/connection?person1=ID1&person2=ID2` - Trova connessioni
- **POST** `/api/persons/{id}/verify` - Verifica crowdsourced
- **GET** `/api/persons/stats/most-connected` - Persone più connesse
- **GET** `/api/persons/stats/orphans` - Persone senza collegamenti

### 3. Tracciamento Utenti

Ogni persona ora ha:

- `createdBy` - Chi l'ha creata
- `createdAt` / `updatedAt` - Timestamp
- `verified` - Se verificata dalla community
- `verificationCount` - Numero di verifiche

## 📝 Come usare

### 1. Registrazione

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "mario_rossi",
    "email": "mario@example.com",
    "password": "password123",
    "firstName": "Mario",
    "lastName": "Rossi"
  }'
```

### 2. Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "mario_rossi",
    "password": "password123"
  }'
```

Risposta:

```json
{
  "userId": "xxx-xxx-xxx",
  "username": "mario_rossi",
  "email": "mario@example.com",
  "token": "fake-jwt-token-xxx"
}
```

### 3. Creare una persona (con tracking utente)

```bash
curl -X POST http://localhost:8080/api/persons \
  -H "Content-Type: application/json" \
  -H "X-User-Id: xxx-xxx-xxx" \
  -d '{
    "firstName": "Giuseppe",
    "lastName": "Rossi",
    "gender": "MALE",
    "birthDate": "1920-05-15",
    "birthCity": "Napoli"
  }'
```

### 4. Cercare persone

```bash
curl "http://localhost:8080/api/persons/search?q=Rossi"
```

### 5. Trovare connessioni tra persone

```bash
curl "http://localhost:8080/api/persons/connection?person1=ID1&person2=ID2&maxDepth=10"
```

## ⚠️ TODO - Implementazioni future

1. **JWT Token reale** - Attualmente usa token fake
2. **Authorization** - Implementare controlli accessi
3. **Notification system** - Notifiche quando qualcuno collega i tuoi antenati
4. **Data validation** - Validazioni più robuste
5. **File upload** - Per foto e documenti
6. **Activity feed** - Stream di attività degli utenti
7. **Conflict resolution** - Gestione conflitti nei dati

## 🎯 Prossimo step: Frontend

Ora possiamo creare il frontend React con:

- Sistema di registrazione/login
- Dashboard personale
- Ricerca e collegamento persone
- Visualizzazione albero genealogico interattivo
- Sistema di verifica collaborativo

Pronto per il frontend? 🚀
