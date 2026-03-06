# 🐾 Amimaux

Application mobile Android pour gérer vos animaux de compagnie et suivre leurs activités.

## 📱 Fonctionnalités

- **Gestion des animaux** : Ajouter, consulter et déleter vos animaux de compagnie
  - Fiche complète avec nom, espèce et photo
  - Stockage des photos en local

- **Suivi des activités** : Enregistrez les activités de vos animaux
  - Créer et gérer différentes types d'activités
  - Associer les activités aux animaux
  - Historique des activités par animal

- **Notifications** : Rappels personnalisés pour les activités importantes
  - Notifications programmables
  - Gestion des délais de notification

- **Interface intuitive** : Application moderne avec Jetpack Compose
  - Navigation fluide entre les écrans
  - Thème personnalisable

## 🛠️ Architecture

### Stack Technologique

- **Language** : Kotlin
- **Framework UI** : Jetpack Compose
- **Base de données** : Room (SQLite)
- **Architecture** : MVVM avec ViewModels
- **Target** : Android 14+ (API 34+)
- **Compilation** : JDK 17

### Structure du Projet

```
app/
├── data/                    # Couche données
│   ├── dao/                # Data Access Objects
│   │   ├── AnimalDao.kt
│   │   ├── ActiviteDao.kt
│   │   └── ActiviteAnimalDao.kt
│   ├── database/           # Configuration Room
│   │   ├── AnimalBD.kt
│   │   ├── ActiviteBD.kt
│   │   └── ActivityAnimalBD.kt
│   ├── entity/             # Modèles de données
│   │   ├── Animal.kt
│   │   ├── Activite.kt
│   │   └── ActiviteAnimal.kt
│   └── NotifDelay.kt       # Gestion des délais
├── presentation/           # Couche présentation
│   ├── ui/                # Écrans Compose
│   │   ├── MainActivity.kt
│   │   ├── AddAnimal.kt
│   │   ├── AddActivity.kt
│   │   ├── InfoActivity.kt
│   │   ├── Common.kt
│   │   ├── MyApplication.kt
│   │   └── theme/
│   │       ├── Color.kt
│   │       ├── Theme.kt
│   │       └── Type.kt
│   ├── viewmodel/         # ViewModels
│   │   ├── MainViewModel.kt
│   │   ├── AddAniViewModel.kt
│   │   ├── AddActViewModel.kt
│   │   └── InfoViewModel.kt
│   └── notification/      # Notifications
│       └── MyWorker.kt
```

## 🚀 Installation et Démarrage

### Prérequis

- Android Studio Koala ou plus récent
- JDK 17+
- Gradle 8.0+

### Build et Exécution

```bash
# Cloner le projet (si nécessaire)
git clone <repository-url>
cd Amimaux

# Build debug
./gradlew buildDebug

# Installer et exécuter
./gradlew installDebug
```

Ou simplement utilisez Android Studio pour compiler et exécuter l'application.

## 📊 Base de Données

### Entités principales

**Animal**
- `id` : Identifiant unique
- `nom` : Nom de l'animal
- `espece` : Espèce/Type
- `photo` : Chemin de la photo

**Activite**
- `id` : Identifiant unique
- Détails de l'activité

**ActiviteAnimal**
- Liaison entre animaux et activités

## 🔌 Permissions

L'application demande les permissions suivantes :

```xml
- READ_EXTERNAL_STORAGE   : Lecture des photos
- READ_MEDIA_IMAGES       : Accès aux images
- POST_NOTIFICATIONS      : Envoi de notifications
```

## 📚 Dépendances Principales

- Jetpack Compose (UI moderne composable)
- Room (Persistence avec SQLite)
- ViewModel (Gestion d'état)
- WorkManager (Tâches programmées pour les notifications)

## 🧪 Tests

```bash
# Tests unitaires
./gradlew test

# Tests d'instrumentation (Android)
./gradlew connectedAndroidTest
```

## 📝 Conventions

- Nommage en français pour les classes de domaine
- Architecture MVVM avec séparation des responsabilités
- Kotlin coroutines pour opérations asynchrones
- Room pour persistance locale

## 🤝 Contribution

Pour contribuer :

1. Créer une branche feature (`git checkout -b feature/NomFeature`)
2. Commiter les changements (`git commit -m 'Add NomFeature'`)
3. Pousser la branche (`git push origin feature/NomFeature`)
4. Ouvrir une Pull Request

## 📄 License

Ce projet est un exercice académique du cours de Programmation Mobile M1.

---

**Auteur** : Guillaume  
**Version** : 1.0  
**Dernière mise à jour** : Mars 2026
