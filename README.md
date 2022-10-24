# Base

# Springfox

Documentation brute :
http://localhost:8080/swagger/v2/api-docs

Documentation avec UI : 
http://localhost:8080/swagger/swagger-ui/

# Développement (Windows)

## Prérequis
- Java jdk 11 minimum https://jdk.java.net/
- npm (inclus dans Node.js https://nodejs.org/en/download/)

## Exécution de l'application
### Le back

Une première console à la racine du projet permet de lancer l'application.
```bash
.\gradlew bootRun
```

Optionnellement, une deuxième console à la racine du projet permet de build automatiquement le projet lorsque le code source est modifié.
```bash
.\gradlew build -t
```

Une documentation des endpoints API est générée par Springfox, voir : http://localhost:8080/swagger/swagger-ui/

### Le front
Une console dans `src/main/front` permet de lancer le front.
```bash
npm run serve
```
Au premier lancement il sera nécessaire d'exécuter :
```bash
npm ci
```
L'URL local du site sera affiché dans la console, normalement : http://localhost:8081/

Avant de commit il est recommandé, pour uniformiser le formatage du code, d'exécuter dans `src/main/front` :
```bash
npm run lint
```

Il est intéressant d'ouvrir `src/main/front` dans une instance d'IDE séparée pour que les paramètres soient mieux détectés, il est aussi possible d'activer l'option `format on save` pour aider à garder le formatage uniforme (VSCode).
 
# Installation (Debian)

L'exécution du projet requiert `java` (version 11) et une base de données `MySQL`.

La compilation du projet requiert `java sdk` (version 11) et `npm`.

Pour exemple, voici les commandes que j'ai utilisé quand j'ai testé l'installation sur une VM Debian vierge : 

```bash
# NOTE : `sudo` omis
apt install openjdk-11-jdk-headless
apt install git
apt install npm
# NOTE : la version de npm des dépôts Debian est trop vielle,
#        mettre à jour avec :
npm install npm@latest -g
# NOTE : redémarrer le terminal pour appliquer la màj de npm
apt install mariadb-server
```

Pour information, voici la commande que j'ai utilisé pour générer un certificat SSL :

```bash
keytool -genkeypair -alias springboot -keyalg RSA -keysize 4096 -storetype PKCS12 -keystore springboot.p12 -validity 3650 -storepass password
```

Pour information, voici les commandes que j'ai utilisé pour configurer la base de données de test :

```bash
mysql_secure_installation
mysql -u root -p
```

Pour information, voici les commandes SQL que j'ai utilisé pour initialiser la base de données de test :

```sql
CREATE DATABASE gvs;
CREATE USER 'springuser'@'localhost' IDENTIFIED BY 'ThePassword';
GRANT ALL PRIVILEGES ON gvs.* TO 'springuser'@'localhost' WITH GRANT OPTION;
```

Les tables et leur contenu sera importé automatiquement par migration grâce à `flywaydb`.

# Déploiement

Pour le déploiement en production, le front Vue.js est compilé puis injecté dans le jar, il faut donc compiler le front avant le back. (Les fichiers générés sont automatiquement mis au bon endroit par `npm run build` car configuré ainsi)

Script complet pour le build :

```bash
cd src/main/front
npm ci # premier build, installe l'environnement npm selon package-lock.json
npm run build
cd ../../..
sudo chmod +x ./gradlew # si besoin
./gradlew clean build
```

Pour l'exécution :

```bash
java -jar ./build/libs/baseGVS-X.X.X.jar
```

Les propriétés de l'application peuvent être configurées en les passant en argument à la suite du .jar ou en définissant des variables d'environnement.

Exemple :

```bash
java -jar ./build/libs/baseGVS-X.X.X.jar --gvs.environment=prod --MYSQL_USERNAME=springuser --MYSQL_PASSWORD=ThePassword --SSL_KEYSTORE=springboot.p12 --SSL_KEYSTORE_PASSWORD=password --SSL_KEY_ALIAS=springboot --SSL_KEY_PASSWORD=password
```

Exemple de propriétés :
- `--GVS_ENVIRONMENT=<prod|dev>` : L'environnement courant. Impacte les configurations par défaut. La documentation springfox est désactivée en `prod`. Une connexion HTTPS est exigée en `prod`, défaut `dev`
- `--HTTPS_PORT=443` : Port de connexion https, défaut `443` en prod, `8443` en dev
- `--HTTP_PORT=80` : Port du serveur, défaut `80` en prod, `8080` en dev
- `--MYSQL_HOST=localhost` : Adresse de la base de données, défaut `localhost`
- `--MYSQL_PORT=3306` : Port de la base de données, défaut `3306`
- `--MYSQL_DATABASE=GVS` : Nom de la base de données, défaut `gvs`
- `--MYSQL_USERNAME=springuser` : Nom d'utilisateur base de données, défaut `springuser` en `dev`, exigé en `prod`
- `--MYSQL_PASSWORD=ThePassword` : Mot de passe de l'utilisateur de base de données, défaut `ThePassword` en `dev`, exigé en `prod`
- `--SSL_ENABLED=<true|false>` : Permet d'activer l'authentification ssl, défaut `false` en `dev`, forcé à `true` en prod
- `--SSL_KEYSTORE=springboot.p12` : Fichier keystore, défaut `springboot.p12` en `dev`, exigé en `prod`
- `--SSL_KEYSTORE_PASSWORD=password` : Mot de passe du fichier keystore, défaut `password` en `dev`, exigé en `prod`
- `--SSL_KEYSTORE_TYPE=pkcs12` : Format du fichier keystore, défaut `pkcs12`
- `--SSL_KEY_ALIAS=springboot` : Alias de la clé ssl dans le fichier keystore, défaut `springboot` en `dev`, exigé en `prod`
- `--SSL_KEY_PASSWORD=password` : Mot de passe de la clé ssl dans le fichier keystore, défaut `password` en `dev`, exigé en `prod`

