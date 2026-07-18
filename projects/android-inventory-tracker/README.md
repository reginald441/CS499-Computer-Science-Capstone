# Android Inventory Tracker — CS 499 Enhancement One

This directory contains the readable source code and project configuration for the enhanced Android Inventory Tracker selected for the Software Design and Engineering category of CS 499.

## Architecture

The original implementation placed UI construction, navigation, validation, authentication, inventory operations, SQLite schema management, and SMS-permission handling in one `MainActivity.java` file. The enhanced version introduces an MVVM-style separation:

- `model/`: `User` and `InventoryItem`
- `data/`: `DatabaseHelper`, `AuthRepository`, and `InventoryRepository`
- `viewmodel/`: `AuthViewModel` and `InventoryViewModel`
- `util/`: `ValidationUtils` and `PasswordUtils`
- `MainActivity`: view and navigation coordination
- `res/layout/`: separate XML layouts for each screen and the edit dialog

## Security and data improvements

- Salted SHA-256 password hashes replace new plain-text passwords.
- Legacy plain-text passwords are migrated after a successful login.
- Parameterized SQLite queries are used for authentication and CRUD operations.
- Inventory rows include an owner value so users access only their own records.
- Input length, format, and numeric-range validation reduces crashes and invalid data.

## Usability improvements

- Full edit dialog for item name, quantity, and location.
- Delete confirmation.
- Clearer validation and database-result messages.
- Separate screens for login, account creation, inventory, and SMS settings.

## Testing

`ValidationUtilsTest` verifies valid inventory input, nonnumeric quantities, negative quantities, strong passwords, and short-password rejection.

> This public source directory focuses on readable code and configuration. Generated build output and machine-specific files are intentionally excluded.