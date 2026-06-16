# Phoenix Dating — CLAUDE.md

## Objectif du projet

API REST backend pour une application de rencontre (style Tinder). Permet aux utilisateurs de consulter des profils, de les découvrir dans un fil de swipe, et d'enregistrer des actions (like / refus) avec détection de match mutuel.

---

## Stack technique

| Couche | Technologie |
|---|---|
| Langage | Java 21 |
| Framework | Spring Boot 3.5.0 |
| Sécurité | Spring Security + JWT (jjwt 0.12.6) |
| Persistance | Spring Data JPA / Hibernate |
| Base de données (prod) | PostgreSQL |
| Base de données (dev) | H2 in-memory (`jdbc:h2:mem:testdb`) |
| Documentation API | SpringDoc OpenAPI 3 / Swagger UI (`/swagger-ui.html`) |
| Build | Maven (wrapper `mvnw`) |
| Utilitaires | Lombok |

---

## Architecture des packages

```
com.phoenix.dating
├── DatingApplication.java          # Point d'entrée Spring Boot
├── common/
│   └── StatusController.java       # GET /status — health check
├── profile/
│   ├── ProfileController.java      # Endpoints REST profils
│   ├── ProfileService.java         # Logique métier profils (mock in-memory)
│   ├── dto/
│   │   └── ProfileResponse.java    # Record de réponse profil
│   └── model/
│       └── Gender.java             # Enum MALE | FEMALE | NON_BINARY
├── swipe/
│   ├── SwipeController.java        # Endpoints REST swipes
│   ├── SwipeService.java           # Logique métier swipe (mock)
│   ├── dto/
│   │   ├── SwipeRequest.java       # Body { action: ACCEPT | REFUSE }
│   │   └── SwipeResponse.java      # { matched: bool, matchId: UUID? }
│   └── model/
│       └── SwipeAction.java        # Enum ACCEPT | REFUSE
└── security/
    └── SecurityConfig.java         # JWT stateless, CSRF désactivé
```

---

## Endpoints API

### Status
| Méthode | Chemin | Auth | Description |
|---|---|---|---|
| GET | `/status` | Non | Health check |

### Profils
| Méthode | Chemin | Auth | Description |
|---|---|---|---|
| GET | `/profiles/me` | Non (mock) | Profil de l'utilisateur connecté |
| GET | `/profiles/discover?limit=10` | Non (mock) | Fil de découverte (ordre aléatoire) |
| GET | `/profiles/{userId}` | Non (mock) | Profil public par UUID |

### Swipes
| Méthode | Chemin | Auth | Description |
|---|---|---|---|
| POST | `/swipes/{userId}` | Oui | Enregistrer un swipe (ACCEPT / REFUSE) |

---

## État actuel

- Les données de profils sont des **mocks in-memory** (7 profils fictifs dans `ProfileService`).
- `SwipeService.postSwipeAction` retourne toujours `{ matched: false, matchId: null }` — logique de match non implémentée.
- La vérification JWT est configurée dans `SecurityConfig` mais le filtre d'extraction du token n'est pas encore branché.
- La base H2 est active en développement ; le schéma JPA est en `create-drop`.

---

## Attentes de performance

| Critère | Cible |
|---|---|
| Latence P99 `/profiles/discover` | < 200 ms |
| Latence P99 `/swipes/{userId}` | < 150 ms |
| Throughput minimal | 500 req/s par instance |
| Disponibilité cible | 99.9 % |
| Temps de démarrage (JVM) | < 5 s |
| Taille du JAR | < 80 MB |

> Ces cibles sont indicatives pour une instance unique. La mise à l'échelle horizontale est prévue via plusieurs répliques sans état (stateless JWT).

---

## Conventions de développement

- **Langue du code** : anglais (noms de classes, méthodes, variables, commentaires dans le code).
- **Langue de la documentation / annotations Swagger** : anglais (voir mémoire projet).
- **Langue des échanges** : français.
- Records Java pour tous les DTOs (immuables, pas de Lombok sur les DTOs).
- Lombok (`@RequiredArgsConstructor`, `@Builder`, etc.) autorisé dans les services et contrôleurs.
- Pas de commentaires dans le code sauf si le WHY est non évident.
- Les tests d'intégration doivent cibler une vraie base de données, pas des mocks (éviter la divergence mock/prod).

---

## Lancer le projet

```bash
# Développement (H2 in-memory)
./mvnw spring-boot:run

# Build + tests
./mvnw verify

# Package
./mvnw package -DskipTests
java -jar target/dating-0.0.1-SNAPSHOT.jar
```

Swagger UI disponible sur : `http://localhost:8080/swagger-ui.html`

---

## Prochaines étapes identifiées

- [ ] Implémenter les entités JPA (`User`, `Swipe`, `Match`) et migrer hors du mock
- [ ] Brancher le filtre JWT (`OncePerRequestFilter`) pour extraire l'utilisateur authentifié
- [ ] Implémenter la logique de match mutuel dans `SwipeService`
- [ ] Ajouter les endpoints d'authentification (`/auth/register`, `/auth/login`)
- [ ] Passer en configuration PostgreSQL pour l'environnement de staging/prod
- [ ] Ajouter des tests d'intégration sur base H2 (ou Testcontainers PostgreSQL)
