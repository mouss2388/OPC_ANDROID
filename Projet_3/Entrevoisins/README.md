[![OpenClassrooms Logo](https://www.anaf.fr/wp-content/uploads/2020/09/OpenClassroom_LOGO-768x92.png)](https://openclassrooms.com/fr/)

# PROJET 3 (EntreVoisins)

Ce dépôt contient _L'Application EntreVoisins_ pour le parcours **Développeur D'Application Android**.

## Procédure Installation du projet

1. **Récupérer le code**
* Télécharger et installer _[Git](https://git-scm.com/downloads "Télécharger Git") puis ouvrir terminal (ex: Git Bash)_
* Dans terminal taper : cd _"Chemin où vous souhaitez récupérer le code"_
* Taper :  _git clone https://github.com/mouss2388/OPC_ANDROID.git_

2. **Ouvrir l'éditeur de code**
* Télécharger, installer et ouvrir _[Android Studio](https://developer.android.com/studio "Télécharger Android Studio")_
* Puis File --> Open --> CheminDuProjet
* Cliquer sur OK
* " _Si Invalid VCS root mapping signale une erreur alors cliquer sur Configure..._
* _Selectionner le Chemin du directory en rouge puis l'éffacer avec " - "_
* _Cliquer sur Apply puis OK_ "
* Ouvrir préférences (File --> Settings) et taper gradle
* Vérifier que Gradle JDK à la version 11.0.10 (Android Studio default JDK) sinon télécharger là

3. **Ouvrir Device virtual**
* Cliquer sur onglet Tools --> AVD MEDIA
* Cliquer sur Create Virtual Device
* Choisir un Device ex: Pixel 3
* Télécharger Image System Recommandée par Google  exemple: (Release name: R)
* Installer puis Next
* Possibilié de renommer le device puis Finish
* Lancer le device virtuel
* cela peut prendre quelques dizaines de secondes (tout dépend de votre ordinateur)

4. **Lancer l'application**
* Sous Android Studio cliquer sur play sinon utiliser le raccourci Maj + F10

## Projet EntreVoisin

### Identification du projet et de la mission
* **Nom et nature :** Développement d’une nouvelle fonctionnalité pour l’application
  Entrevoisins
* **Origine :** Collecte de retours utilisateurs amenant au développement d’une nouvelle
  fonctionnalité pour l'application Entrevoisins
* **Enjeu :** Améliorer l’expérience utilisateur

### Analyse de l'existant

Ci-après les fonctionnalités présentes dans l’application Entrevoisins :
* lister mes voisins
* ajouter un voisin
* suppression d’un voisin

### Cadrage de la fonctionnalité

Au clic sur un utilisateur, nouvel écran avec :
* un bouton de retour à l'élément précédent
* l'avatar de l'utilisateur
* le nom de l'utilisateur
* un bouton d’ajout du voisin à la liste de favoris
* un onglet Favoris dans lequel les utilisateurs marqués comme favoris s’affichent

### Cadrage des tests

Réalisation de 3 tests instrumentalisés :
* test vérifiant que lorsqu’on clique sur un élément de la liste, l’écran de
  détails est bien lancé
* test vérifiant qu’au démarrage de ce nouvel écran, le TextView indiquant
  le nom de l’utilisateur en question est bien rempli
* test vérifiant qu’au clic sur le bouton de suppression, la liste d’utilisateurs
  compte bien un utilisateur en moins
* test vérifiant que l’onglet Favoris n’affiche que les voisins marqués comme
  favoris
* Création d’un test unitaire pour chaque fonctionnalité

## Remerciement

*Je tiens à remercier Grégoire Cattan mon mentor qui m'a aiguillié  lors de la réalisation du projet*
