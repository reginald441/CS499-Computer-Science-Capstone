# 3-2 Milestone Two: Enhancement One — Software Design and Engineering

**Reginald Kargbo**  
CS 499 Computer Science Capstone  
Southern New Hampshire University

## 1. Description of the Artifact

The artifact I selected for the Software Design and Engineering category is the Android Inventory Tracker that I originally developed for CS 360: Mobile Architecture and Programming. The application was created as an Android Studio project using Java, XML, and SQLite. Its purpose is to give users a simple way to create an account, log in, add inventory records, view item quantities and storage locations, update records, delete records, and manage the optional SMS permission needed for future low-inventory notifications.

When I reviewed the original project for CS 499, I found that it worked as a functional learning project, but its internal design was not strong enough to represent my current software engineering abilities. Most of the application was contained inside one `MainActivity.java` file. That file created screens, performed validation, handled authentication, managed inventory operations, defined the SQLite database, and requested SMS permission. These combined responsibilities made the application more difficult to maintain and test.

## 2. Justification for Including the Artifact in My ePortfolio

I selected this artifact because it demonstrates mobile interface design, Java programming, account management, CRUD operations, database persistence, input handling, navigation, and Android permission management. The original version shows that I could build a working application, while the enhanced version shows that I can evaluate existing code, identify structural weaknesses, and redesign a system using stronger engineering practices.

The most significant improvement was refactoring the application toward an MVVM-style architecture. I separated the project into model, data, repository, utility, ViewModel, and view responsibilities. `User` and `InventoryItem` represent application data. `DatabaseHelper` owns the SQLite schema. `AuthRepository` and `InventoryRepository` handle persistence. `AuthViewModel` and `InventoryViewModel` contain presentation and business logic. `ValidationUtils` centralizes validation, while `PasswordUtils` handles password hashing. `MainActivity` now focuses mainly on displaying screens, responding to actions, and coordinating navigation.

I also moved the interface into separate XML layout files for login, account creation, inventory management, SMS settings, and the edit dialog. The inventory update function now supports full editing of item name, quantity, and location. A delete-confirmation dialog reduces accidental record removal. Error messages are based on centralized validation and database results.

The enhanced artifact also demonstrates a stronger security mindset. New passwords are stored using a random salt and SHA-256 hash rather than plain text. Legacy accounts are migrated after a successful login. Parameterized SQLite selections are used for authentication and data operations. Inventory records are associated with the logged-in username so users cannot view or modify another account's inventory.

## 3. Course Outcomes and Updated Outcome-Coverage Plan

This enhancement supports the course outcomes I planned to address in Module One. The strongest connection is the ability to design and evaluate computing solutions while managing design trade-offs. The original single-class design was simple during initial development, but it created maintainability and testing problems. Refactoring into separate layers added more files and planning, but it produced a clearer system in which each component has a focused responsibility.

The artifact also demonstrates the use of Android Studio conventions, XML layouts, SQLite, repository classes, ViewModels, reusable validation, and unit tests. `ValidationUtilsTest` includes cases for valid inventory data, nonnumeric quantities, negative quantities, strong passwords, and short-password rejection.

The enhancement provides evidence of professional technical communication through organized package names, documentation, comments, this narrative, and the ePortfolio. The security outcome became more important during the enhancement because the original plain-text password storage and shared inventory table were major design weaknesses. The enhanced project uses salted password hashes, user-scoped queries, validation, parameterized selections, and safer database-result handling.

## 4. Reflection on the Enhancement Process

The most important lesson I learned is that a program can meet its basic functional requirements and still need substantial software engineering improvement. The original application could create accounts, authenticate users, and manage inventory, but every feature depended heavily on `MainActivity`. As features grow, one large activity becomes difficult to understand, test, debug, and modify.

One challenge was deciding how far to refactor without completely replacing the original application. I preserved the original requirements and workflow while moving responsibilities into focused classes. Another challenge was database compatibility. The original users table stored plain-text passwords, while the enhanced design required hash and salt columns. I increased the database version, added the new columns during migration, and included a legacy-login path that upgrades an old password after successful authentication.

Input validation was another improvement area. The earlier quantity logic called `Integer.parseInt` directly, which could fail on invalid input. The enhanced validation checks format and range before conversion. It also limits username, item-name, and location lengths and requires stronger passwords. Centralizing these rules reduced duplication and made validation independently testable.

I also learned that user experience is part of software engineering. The new edit dialog supports complete record changes, delete confirmation reduces accidental data loss, user-specific inventory improves multi-account behavior, and clear success and failure messages help users understand each action.

The enhanced source, XML, manifest, and Java validation classes were reviewed during packaging. Final environment testing should include Gradle synchronization in Android Studio, emulator testing, account creation, authentication, CRUD operations, user separation, SMS permission handling, navigation, and database migration.

## ePortfolio

https://reginald441.github.io/CS499-Computer-Science-Capstone/
