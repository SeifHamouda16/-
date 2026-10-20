# Black & White Theme Mode & Navigation Polish

Implement a high-contrast Black & White monochrome theme with a Dark/Light mode toggle, and remove the Grand Challenge tab from the bottom navigation bar while preserving its access on the home screen.

## User Review & Critical Decisions

> [!IMPORTANT]
> The following user preferences were confirmed in Phase 1:
> - **Theme Mode**: Add an interactive Black & White theme toggle supporting both Dark Mode (deep black background, crisp white text) and Light Mode (pure white background, sharp dark text) with instant switching.
> - **Grand Challenge Removal**: Remove **only the tab from the bottom navigation bar** (`PharaohBottomNav`), keeping the 4 core tabs (Sections, Hieroglyphs, Badges, Account) clean and uncluttered, while keeping the Grand Exam banner accessible from the Home screen.

---

## 1. Overview & Core Concept

- **Visual Refresh**: Replace the heavy dark lapis default with an elegant, minimalist Black & White (Monochrome Pharaoh) aesthetic.
  - **Dark Mode**: Pitch black surfaces (`#0A0A0A`), crisp white typography (`#FFFFFF`), subtle silver/gold accents for active selections and badges.
  - **Light Mode**: Clean pure white background (`#FFFFFF`), deep charcoal/black text (`#111111`), subtle borders and high readability.
- **Navigation Simplification**: Reduce bottom navigation clutter by removing the redundant 5th "Grand Exam" tab, establishing a focused 4-tab bar:
  1. **الأقسام / Sections** (Home & learning library)
  2. **الهيروغليفية / Hieroglyphs** (Cartouche maker & alphabet)
  3. **الأوسمة / Badges** (Hall of Fame & Room DB records)
  4. **الحساب / Account** (Profile, theme toggle, and language switcher)

---

## 2. User Experience & Visual Design

### Theme Switcher Flow
- Users can switch between **الوضع الليلي (Dark Mode)** and **الوضع النهاري (Light Mode)** via:
  1. Quick theme toggle icon (Sun / Moon) in the Top App Bar on all screens.
  2. Dedicated Theme section inside the **Account & Settings** screen (`AccountSettingsScreen`).
- Instant reactive recomposition with saved preference in `UserProgressManager` (`SharedPreferences`).

### Color Tokens & Palette

| Token | Dark Mode (Black) | Light Mode (White) |
|---|---|---|
| `background` | `#0A0A0A` (Deep Obsidian) | `#FFFFFF` (Pure White) |
| `surface` | `#161616` (Dark Charcoal) | `#F8F8F8` (Off-White Card) |
| `surfaceVariant` | `#222222` (Card Outline) | `#EEEEEE` (Soft Gray) |
| `onBackground` | `#FFFFFF` (Crisp White) | `#0E0E0E` (Deep Black) |
| `onSurface` | `#F0F0F0` (High Legibility) | `#1A1A1A` (Dark Charcoal) |
| `outline` | `#333333` (Subtle Dark Stroke) | `#DCDCDC` (Subtle Light Stroke) |
| `primary` | `#E5C06E` / `#FFFFFF` | `#121212` / `#B8860B` |

---

## 3. Key Product Decisions & Trade-Offs

- **Decision 1: Bottom Navigation Cleanup**
  - *Chosen Approach*: Remove the Grand Exam tab from `PharaohBottomNav.kt` and keep the Grand Exam accessible via the hero banner on the Home screen.
  - *Why*: Prevents overcrowding on smaller screens (4 tabs is standard Material 3 ergonomic density) and honors the user's explicit choice.
- **Decision 2: Black & White Theme System**
  - *Chosen Approach*: Introduce a `ThemeMode` state (`SYSTEM`, `DARK`, `LIGHT`) managed in `UserProgressManager` and consumed by `EgyptianCivilizationTheme`.
  - *Why*: Allows users who prefer high contrast or OLED battery saving to use pure black, while readers in bright environments can use clean crisp white.

---

## 4. Technical Architecture & Data Strategy

```
┌────────────────────────────────────────────────────────┐
│                   MainActivity                         │
│  - Observes: currentLanguage, themeMode, uiState      │
│  - Provides: CompositionLocalProvider (RTL/LTR)       │
│  - EgyptianCivilizationTheme(darkTheme = isDark)      │
└──────────────────────────┬─────────────────────────────┘
                           │
         ┌─────────────────┴─────────────────┐
         ▼                                   ▼
┌──────────────────────┐          ┌──────────────────────┐
│  PharaohBottomNav    │          │  AccountSettings     │
│  - Sections          │          │  - Login / Sync      │
│  - Hieroglyphs       │          │  - Language (AR/EN)  │
│  - Badges (Room DB)  │          │  - Theme Mode Toggle │
│  - Account           │          │    (Dark / Light)    │
│  (Grand Exam REMOVED)│          └──────────────────────┘
└──────────────────────┘
```

### Affected Files
1. `model/AccountModels.kt`: Add `ThemeMode` enum (`DARK`, `LIGHT`, `SYSTEM`).
2. `data/UserProgressManager.kt`: Store and expose `themeMode` StateFlow.
3. `ui/theme/Color.kt` & `Theme.kt`: Update palettes for pure Black & White high-contrast styling.
4. `ui/components/PharaohBottomNav.kt`: Remove the Grand Exam tab item.
5. `ui/components/PharaohTopAppBar.kt`: Add quick theme toggle icon (Sun / Moon).
6. `ui/screens/AccountSettingsScreen.kt`: Add theme selector card (Dark / Light).
7. `MainActivity.kt`: Bind `themeMode` to `EgyptianCivilizationTheme`.
