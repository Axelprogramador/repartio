# Repartio

[🇪🇸 Leer en español](README.es.md)

A clean, intuitive Android app for splitting expenses among groups of people. Built as a portfolio project to demonstrate modern Android development practices.

---

## Features

- **Group management** — Create and manage multiple expense groups (trips, shared flats, dinners, etc.)
- **Flexible expense splitting** — Split equally among all or selected participants, or set custom amounts per person
- **Smart debt settlement** — Greedy algorithm that minimizes the number of transfers needed to settle all debts
- **Multi-currency support** — Auto-detects device currency or lets the user choose manually
- **Theming** — Three predefined color themes (Ocean, Forest, Sunset) with light/dark mode control
- **Internationalization** — Available in English, Spanish, French, German, Portuguese, Chinese, Japanese and Korean

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Architecture | Clean Architecture + MVVM |
| DI | Hilt (Dagger) |
| Database | Room (SQLite) |
| Async | Coroutines + Flow |
| Navigation | Navigation Compose |
| Preferences | DataStore |
| Min SDK | Android 8.0 (API 26) |

---

## Architecture

The project follows **Clean Architecture** with three clearly separated layers, a pattern conceptually similar to Hexagonal Architecture:

```
app/
├── data/               # Data layer
│   ├── local/          # Room database, DAOs, entities, mappers
│   └── repository/     # Repository implementations
├── domain/             # Domain layer (no Android dependencies)
│   ├── model/          # Domain models
│   ├── repository/     # Repository interfaces (ports)
│   └── usecase/        # Business logic use cases
└── ui/                 # Presentation layer
    ├── screens/        # Composable screens
    ├── viewmodel/      # ViewModels
    ├── theme/          # Theming, ThemeManager, LanguageManager, CurrencyManager
    └── navigation/     # Navigation graph
```

### Key design decisions

**Domain isolation** — Domain models are decoupled from Room entities. Mappers handle the conversion between layers, so the domain layer has zero Android dependencies and could be reused in a different platform.

**Use cases as single-responsibility units** — Each user action (create group, add expense, calculate balances) is encapsulated in its own use case class, keeping ViewModels thin and business logic testable in isolation.

**Reactive data flow** — Room DAOs expose `Flow`, which propagates through repositories and use cases up to the UI via `StateFlow`. Any database change automatically updates the UI without manual refresh.

**Debt settlement algorithm** — The `CalculateBalancesUseCase` uses a greedy algorithm that computes the net balance of each member and pairs debtors with creditors to minimize the total number of transfers. For N participants, the maximum number of transfers is N-1.

---

## Database Schema

```
groups
  └── members (FK: groupId → CASCADE)
        └── expenses (FK: groupId, payerId → CASCADE)
              └── expense_participants (FK: expenseId, memberId → CASCADE)
```

Foreign key cascades ensure referential integrity — deleting a group removes all related data automatically.

---

## Getting Started

1. Clone the repo
   ```bash
   git clone https://github.com/Axelprogramador/repartio.git
   ```
2. Open in Android Studio (Hedgehog or newer recommended)
3. Sync Gradle and run on an emulator or physical device (Android 8.0+)

No API keys or external services required — the app is fully offline.

---

## Roadmap

- [ ] Export settlement summary as PDF or share via text
- [ ] Expense categories and filtering
- [ ] Edit existing expenses
- [ ] Unit tests for use cases and the settlement algorithm
- [ ] Receipt photo attachment per expense

---

## Author

Axel — [github.com/Axelprogramador](https://github.com/Axelprogramador)
