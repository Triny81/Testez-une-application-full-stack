Lien Github : https://github.com/Triny81/Testez-une-application-full-stack.git

Installation :
	1. cloner le projet Github via le lien ci-dessus

	2. installer la base de données dans MySQL
		a. Créer un nouveau schéma de base de données nommé "test"
		b. Ouvrir le fichier suivant : ressources/sql/script.sql
		c. Placer le contenu du fichier .sql dans la BD "test"

	3. installer les dépendances
		a. dans le dossier front, exécuter la commande npm install
		b. dans le dossier back, exécuter la commande mvn clean install (des erreurs peuvent apparaître mais ne sont pas gênantes pour la suite)

Lancer les applications :
	1. front-end
		dans le dossier front, exécuter la commande ng serve

	2. back-end
		dans le dossier back, lancer l'application à l'aide de votre IDE

Se connecter en admin :
	- login : yoga@studio.com
	- mdp : test!1234
	
Exécuter les tests et voir la couverture :
- Front-end : 
	Commande à exécuter pour générer le rapport de couverture : npx jest --coverage
	La couverture est publiée dans la console après avoir executé la commande

- Cypress :
	1. Commande à exécuter pour générer le rapport de couverture : 
		a. Démarrer le front instrumenté : ng run yoga:serve-coverage	
		b. Exécuter les tests : npm run cypress:run
		c. Générer le rapport de couverture : npx nyc report --reporter=html --reporter=text-summary

	2. Ouvrir dans un navigateur le fichier suivant : front\coverage\index.html

- Back-end : 
	Commande à exécuter pour générer le rapport de couverture : mvn clean test jacoco:report 
	Ouvrir dans un navigateur le fichier suivant : back\target\site\jacoco\index.html