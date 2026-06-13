# Repartio

Una app Android limpia e intuitiva para dividir gastos entre grupos de personas. Desarrollada como proyecto de portfolio para demostrar buenas prácticas de desarrollo Android moderno.

[🇬🇧 Read in English](README.md)

---

## Funcionalidades

- **Gestión de grupos** — Crea y gestiona múltiples grupos de gastos (viajes, pisos compartidos, cenas, etc.)
- **División flexible de gastos** — Reparte equitativamente entre todos o participantes seleccionados, o establece importes personalizados por persona
- **Liquidación inteligente de deudas** — Algoritmo greedy que minimiza el número de transferencias necesarias para saldar todas las deudas
- **Soporte multidivisa** — Detecta automáticamente la divisa del dispositivo o permite elegirla manualmente
- **Temas** — Tres temas de color predefinidos (Océano, Bosque, Atardecer) con control de modo claro/oscuro
- **Internacionalización** — Disponible en inglés, español, francés, alemán, portugués, chino, japonés y coreano

---

## Stack tecnológico

| Capa | Tecnología |
|---|---|
| Lenguaje | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Arquitectura | Clean Architecture + MVVM |
| Inyección de dependencias | Hilt (Dagger) |
| Base de datos | Room (SQLite) |
| Asincronía | Coroutines + Flow |
| Navegación | Navigation Compose |
| Preferencias | DataStore |
| SDK mínimo | Android 8.0 (API 26) |

---

## Arquitectura

El proyecto sigue **Clean Architecture** con tres capas claramente separadas, un patrón conceptualmente similar a la Arquitectura Hexagonal:

```
app/
├── data/               # Capa de datos
│   ├── local/          # Base de datos Room, DAOs, entidades, mappers
│   └── repository/     # Implementaciones de repositorios
├── domain/             # Capa de dominio (sin dependencias Android)
│   ├── model/          # Modelos de dominio
│   ├── repository/     # Interfaces de repositorio (puertos)
│   └── usecase/        # Casos de uso con lógica de negocio
└── ui/                 # Capa de presentación
    ├── screens/        # Pantallas Composable
    ├── viewmodel/      # ViewModels
    ├── theme/          # Temas, ThemeManager, LanguageManager, CurrencyManager
    └── navigation/     # Grafo de navegación
```

### Decisiones de diseño clave

**Aislamiento del dominio** — Los modelos de dominio están desacoplados de las entidades de Room. Los mappers gestionan la conversión entre capas, por lo que la capa de dominio no tiene dependencias de Android y podría reutilizarse en otra plataforma.

**Casos de uso con responsabilidad única** — Cada acción del usuario (crear grupo, añadir gasto, calcular balances) está encapsulada en su propia clase de caso de uso, manteniendo los ViewModels ligeros y la lógica de negocio testeable de forma aislada.

**Flujo de datos reactivo** — Los DAOs de Room exponen `Flow`, que se propaga a través de repositorios y casos de uso hasta la UI mediante `StateFlow`. Cualquier cambio en la base de datos actualiza la UI automáticamente sin necesidad de refresco manual.

**Algoritmo de liquidación de deudas** — El `CalculateBalancesUseCase` utiliza un algoritmo greedy que calcula el balance neto de cada miembro y empareja deudores con acreedores para minimizar el número total de transferencias. Para N participantes, el número máximo de transferencias es N-1.

---

## Esquema de base de datos

```
groups
  └── members (FK: groupId → CASCADE)
        └── expenses (FK: groupId, payerId → CASCADE)
              └── expense_participants (FK: expenseId, memberId → CASCADE)
```

Las cascadas de claves foráneas garantizan la integridad referencial — eliminar un grupo borra automáticamente todos los datos relacionados.

---

## Cómo empezar

1. Clona el repositorio
   ```bash
   git clone https://github.com/Axelprogramador/repartio.git
   ```
2. Ábrelo en Android Studio (Hedgehog o más reciente recomendado)
3. Sincroniza Gradle y ejecuta en un emulador o dispositivo físico (Android 8.0+)

No se necesitan claves de API ni servicios externos — la app funciona completamente sin conexión.

---

## Roadmap

- [ ] Exportar resumen de liquidación como PDF o compartir por texto
- [ ] Categorías de gastos y filtros
- [ ] Editar gastos existentes
- [ ] Tests unitarios para casos de uso y el algoritmo de liquidación
- [ ] Adjuntar foto de ticket por gasto

---

## Autor

Axel — [github.com/Axelprogramador](https://github.com/Axelprogramador)
