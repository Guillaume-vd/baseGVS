# Backend

Le backend de l'application est réalisé dans le langage Java, avec le Framework SpringBoot.

Les sources se trouvent dans le dossier [src/main/java](../src/main/java), dans le package `fr.gvs.base`.

Le main du projet se trouve dans [BaseApplication.java](../src/main/java/fr/gvs/base/BaseApplication.java).

# Endpoints API

Un `endpoint` est défini par un `controller`, par convention, chaque `controller` ne contient qu'un `endpoint`.
Les `controllers` sont définis dans le package [fr.gvs.base.controller](../src/main/java/fr/gvs/base/controller/).
Les `controllers` appellent des `services` pour effectuer des opérations sur les données.

## Comment est écrit un Controller

Un endpoint est organisé comme tel :

```java
@Api(tags = {"Account"})
@RestController
public class GetDemoController {
    
    // services

    // endpoint

    // structures
}
```

### Déclaration de la classe

La classe est annotée par deux annotations :
- `@Api` : prend en paramètre `tags`, les tags servent à regrouper les `endpoints` d'un même groupe dans la documentation Swagger
- `@RestController` : Annotation de SpringBoot, déclare que la `class` est un controller REST. Permet quelle soit trouvée et utilisée par SpringBoot.

Le nom de la classe est préfixé selon la méthode HTTP de l'`endpoint` :
- `GET` : `Get` ou `List`
- `POST` : `Create`
- `PUT` : `Edit`
- `DELETE` : `Delete`

Le nom de la classe est postfixé par `Controller`.

### Les services

Les services interface sont déclarés ici :
```java
@Autowired
private DemoService demoService;
```

## Comment est écrit un Service

```java
@Service
public class Service {

    @Autowired
    // Déclaration des interface en @Autowired

    // Service
    public Type serviceName(Type param) throws Http400BadRequestException {
        //....
        return Type;
    }
}
```

# Base de données
La base de données doit avoir être créée manuellement et instanciée dans l'application par l'utilisateur.

Les nouveaux scripts de la base de données sont exécutés automatiquement à partir du 
dossier [db.migration](../src/main/resources/db/migration). Les scripts sql doivent 
respecter le nommage suivant `VXXX__script_name.sql`, pour pouvoir être exécuté.

Attention un script ne peut être exécuté qu'une seule fois et ne doit donc jamais être 
modifié. Et une table `hibernate_sequence`, contient la liste des scripts exécutés.


# Sécurité

