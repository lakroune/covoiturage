# Application de covoiturage

## 1. Contexte du projet

Cette application permet de gérer une plateforme de covoiturage entre conducteurs et passagers.
Un conducteur peut enregistrer un véhicule et publier un trajet composé de plusieurs points d'arrêt.
Un passager peut rechercher un trajet, choisir un point de montée et un point de descente, puis réserver une ou plusieurs places.

Le projet est développé avec Spring Boot, Spring Data JPA, PostgreSQL et une API REST documentée avec Swagger/OpenAPI.

## 2. Objectifs fonctionnels

- gérer les conducteurs et les passagers ;
- gérer les véhicules appartenant aux conducteurs ;
- publier des trajets avec plusieurs étapes ordonnées ;
- rechercher un trajet entre deux villes du même parcours ;
- calculer le prix d'un segment selon les points choisis ;
- vérifier la disponibilité des places sur le segment réservé ;
- créer et annuler des réservations ;
- gérer les statuts d'un trajet et d'une réservation.

## 3. Conception et architecture

L'application suit une architecture en couches :

```text
Controller  ->  Service  ->  Repository  ->  PostgreSQL
     |             |
    DTO         Entity / Mapper
```

- `controllers` : exposent les endpoints REST ;
- `services` : contiennent les règles métier ;
- `repositories` : communiquent avec la base de données ;
- `entities` : représentent les tables et les relations JPA ;
- `dtos/requests` : objets reçus par l'API ;
- `dtos/responses` : objets retournés par l'API ;
- `mappers` : convertissent les entities en DTO et inversement ;
- `exceptions` : centralisent la gestion des erreurs HTTP.

Le diagramme de classes est disponible dans [conception-uml/](./conception-uml/) avec une version image et une version modifiable sur Lucidchart.

![Diagramme de classes UML](./conception-uml/Covoiturage%20%E2%80%94%20UML%20-%20Diagramme%20de%20classes%20UML%20%285%29.png)

[Ouvrir le diagramme modifiable sur Lucidchart](https://lucid.app/lucidchart/5aa4fa89-e003-4a67-94f7-07a8dfb3e602/edit?view_items=reservation%2Cpassager%2CM0aNezkhj~e1%2CpPvNZ6RWi.PR%2CX0aNoPSbf6xY%2CuPvN5xQsdQGU&page=uml&invitationId=inv_182cef68-f1a9-4de5-b483-a8182823e34e)

### Carte du parcours de covoiturage

![Carte du parcours de covoiturage](./conception-uml/carte-covoiturage.png)

## 4. Modèle métier

- `Utilisateur` est la classe de base de `Conducteur` et `Passager`.
- Un `Conducteur` possède plusieurs `Vehicule`.
- Un `Vehicule` peut être utilisé dans plusieurs `Trajet`.
- Un `Trajet` contient au minimum deux `PointArret` ordonnés.
- Une `Reservation` appartient à un `Passager` et à un `Trajet`.
- Une réservation possède un point de montée et un point de descente.

### Règles métier principales

- l'e-mail, le téléphone, le numéro de permis et l'immatriculation sont uniques ;
- la capacité d'un véhicule est comprise entre 1 et 8 places passagers ;
- un trajet contient au moins deux points d'arrêt ;
- l'ordre des points commence à `0` et ne se répète pas ;
- une ville n'apparaît qu'une seule fois dans le parcours d'un trajet ;
- les heures de passage sont croissantes et futures ;
- le prix du premier point est `0` et les prix suivants sont croissants ;
- le nombre de places proposées doit être compris entre `1` et la capacité du véhicule ;
- une réservation doit respecter l'ordre : montée avant descente ;
- le prix d'une réservation est calculé ainsi :

```text
prix du segment = prix du point de descente - prix du point de montée
montant total = prix du segment × nombre de places
```

## 5. Statuts

### Trajet

`PUBLIE` → `COMPLET` ou `TERMINE`  
`PUBLIE` → `ANNULE`

Un trajet publié peut être annulé. Il peut être terminé une seule fois.

### Réservation

`CONFIRMEE` → `ANNULEE`

## 6. Principaux endpoints

L'API est accessible sous `/api/v1`.

| Méthode | Endpoint | Fonctionnalité |
|---|---|---|
| POST | `/conducteurs` | Créer un conducteur |
| GET | `/conducteurs` | Lister les conducteurs |
| GET | `/conducteurs/{id}` | Consulter un conducteur |
| PUT | `/conducteurs/{id}` | Modifier un profil conducteur |
| GET | `/conducteurs/search?email=...` | Rechercher par e-mail |
| POST | `/passagers` | Créer un passager |
| GET | `/passagers` | Lister les passagers |
| GET | `/passagers/{id}` | Consulter un passager |
| PUT | `/passagers/{id}` | Modifier un profil passager |
| GET | `/passagers/search?email=...` | Rechercher par e-mail |
| GET | `/passagers/{id}/reservations` | Consulter l'historique |
| POST | `/vehicules` | Ajouter un véhicule |
| GET | `/vehicules/{id}` | Consulter un véhicule |
| GET | `/vehicules?conducteurId={id}` | Lister les véhicules d'un conducteur |
| PUT | `/vehicules/{id}` | Modifier un véhicule |
| DELETE | `/vehicules/{id}` | Supprimer un véhicule |
| POST | `/trajets` | Publier un trajet multi-étapes |
| GET | `/trajets/{id}` | Consulter un trajet |
| GET | `/trajets/recherche` | Rechercher des trajets avec pagination |
| GET | `/trajets/{id}/points-arret` | Consulter les points d'arrêt |
| PATCH | `/trajets/{id}/terminer` | Terminer un trajet |
| PATCH | `/trajets/{id}/annuler` | Annuler un trajet |
| POST | `/reservations` | Réserver des places |
| GET | `/reservations/{id}` | Consulter une réservation |
| GET | `/reservations?passagerId={id}` | Lister les réservations d'un passager |
| PATCH | `/reservations/{id}/annuler` | Annuler une réservation |

Swagger UI : [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## 7. Exemple de publication d'un trajet

```json
{
  "vehiculeId": 1,
  "placesProposees": 3,
  "pointsArret": [
    {
      "ville": "Rabat",
      "ordre": 0,
      "heurePassage": "2026-11-25T17:00:00",
      "prixDepuisDepart": 0
    },
    {
      "ville": "Casablanca",
      "ordre": 1,
      "heurePassage": "2026-11-25T18:15:00",
      "prixDepuisDepart": 40
    },
    {
      "ville": "Marrakech",
      "ordre": 2,
      "heurePassage": "2026-11-25T21:30:00",
      "prixDepuisDepart": 120
    }
  ]
}
```

## 8. Installation et lancement

### Prérequis

- Java 17 ;
- Maven ;
- Docker et Docker Compose ;
- PostgreSQL 16 si Docker n'est pas utilisé.

### Démarrer PostgreSQL

```bash
docker compose up -d postgres
```

### Lancer l'application

```bash
./mvnw spring-boot:run
```

Sous Windows :

```powershell
.\mvnw.cmd spring-boot:run
```

La base de données de développement utilise actuellement :

```text
URL          : jdbc:postgresql://localhost:5433/covoiturage
Utilisateur  : postgres
Mot de passe : postgres
```

## 9. Vérification

Compiler le projet sans exécuter les tests :

```bash
./mvnw clean compile -DskipTests
```

Tester les endpoints avec Swagger UI ou un client HTTP. Les erreurs de validation et les exceptions métier sont transformées en réponses HTTP par `GlobalExceptionHandler`.
