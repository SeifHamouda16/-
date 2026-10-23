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
    GOLD("gold", "الذهب الفرعوني الملكي", "Royal Pharaoh Gold", 0xFFE5C06EL, 0xFFF3DE9CL, 0xFFC59B27L, 0xFF2C2208L, "👑"),
    LAPIS("lapis", "اللازورد النيلي", "Nile Lapis Lazuli", 0xFF4A90E2L, 0xFF7BB3F0L, 0xFF1D5CA9L, 0xFF102744L, "💎"),
    TURQUOISE("turquoise", "الفيروز السيناوي", "Sinai Turquoise", 0xFF20B2AAL, 0xFF48D1CCL, 0xFF008080L, 0xFF0D3330L, "🪲"),
    CARNELIAN("carnelian", "العقيق الأحمر", "Carnelian Red", 0xFFE65100L, 0xFFFF7043L, 0xFFBF360CL, 0xFF3E1204L, "🔥"),
    MONOCHROME("monochrome", "أبيض وأسود", "Monochrome", 0xFFFFFFFFL, 0xFFFFFFFFL, 0xFF000000L, 0xFF222222L, "♟️");

    companion object {
        fun fromCode(code: String?): RoyalThemeColor {
            return when (code?.lowercase()) {
                "lapis" -> LAPIS
                "turquoise" -> TURQUOISE
                "carnelian" -> CARNELIAN
                "monochrome" -> MONOCHROME
                else -> GOLD
            }
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
