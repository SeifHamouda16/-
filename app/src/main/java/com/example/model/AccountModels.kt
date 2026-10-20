package com.example.model

enum class AppLanguage(val code: String, val displayName: String, val flag: String) {
    ARABIC("ar", "العربية", "🇪🇬"),
    ENGLISH("en", "English", "🇬🇧")
}

enum class ThemeMode(val code: String, val titleAr: String, val titleEn: String, val icon: String) {
    DARK("dark", "الوضع الليلي (أسود)", "Dark Mode (Black)", "🌙"),
    LIGHT("light", "الوضع النهاري (أبيض)", "Light Mode (White)", "☀️")
}

enum class RoyalThemeColor(
    val code: String,
    val titleAr: String,
    val titleEn: String,
    val hexValue: Long,
    val lightHexValue: Long,
    val darkHexValue: Long,
    val containerHexValue: Long,
    val iconEmoji: String
) {
    GOLD("gold", "الذهب الملكي", "Royal Gold", 0xFFE5C06EL, 0xFFFFE082L, 0xFF9E782FL, 0xFF2C2413L, "👑"),
    LAPIS_BLUE("lapis", "لازورد النيل", "Nile Lapis", 0xFF2979FFL, 0xFF82B1FFL, 0xFF1565C0L, 0xFF0D2550L, "🌊"),
    MALACHITE_GREEN("malachite", "زمرد الملاكيت", "Malachite Green", 0xFF00E676L, 0xFF69F0AEL, 0xFF00B248L, 0xFF083318L, "🌿"),
    CARNELIAN_RED("carnelian", "عقيق الفراعنة", "Carnelian Red", 0xFFFF5252L, 0xFFFF8A80L, 0xFFD50000L, 0xFF420D0DL, "🔥"),
    TURQUOISE("turquoise", "فيروز سيناء", "Sinai Turquoise", 0xFF00E5FFL, 0xFF84FFFFL, 0xFF00B8D4L, 0xFF003840L, "💎"),
    AMETHYST_PURPLE("amethyst", "جمشت الملوك", "Royal Amethyst", 0xFFE040FBL, 0xFFEA80FCL, 0xFFAA00FFL, 0xFF380842L, "🔮"),
    SUN_AMBER("amber", "شمس رع", "Ra Sun Amber", 0xFFFF9100L, 0xFFFFD180L, 0xFFFF6D00L, 0xFF402200L, "☀️");

    companion object {
        fun fromCode(code: String?): RoyalThemeColor {
            return values().firstOrNull { it.code.equals(code, ignoreCase = true) } ?: GOLD
        }
    }
}

data class UserProfile(
    val isLoggedIn: Boolean = false,
    val name: String = "باحث الفراعنة",
    val email: String = "explorer@egypt.ancient",
    val avatarEmoji: String = "👑",
    val avatarId: String = "pharaoh",
    val joinedTimestamp: Long = System.currentTimeMillis(),
    val lastSyncTimestamp: Long = System.currentTimeMillis()
)
