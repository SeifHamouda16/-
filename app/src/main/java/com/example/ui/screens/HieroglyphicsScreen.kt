package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CivilizationData
import com.example.model.HieroglyphGlyph
import com.example.ui.components.PharaohTopAppBar
import com.example.ui.theme.NileLapisDark
import com.example.ui.theme.PharaohGold
import com.example.ui.theme.PharaohGoldDark
import com.example.ui.theme.PharaohGoldLight

@Composable
fun HieroglyphicsScreen(
    inputText: String,
    translatedText: String,
    onInputChange: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onToggleTheme: () -> Unit = {},
    isDarkTheme: Boolean = true,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onNavigateBack()
    }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            PharaohTopAppBar(
                title = "الخرطوشة الملكية والهيروغليفية",
                onNavigateBack = onNavigateBack,
                onToggleTheme = onToggleTheme,
                isDarkTheme = isDarkTheme
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("hieroglyphics_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Cartouche Generator Section
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("cartouche_generator_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = NileLapisDark),
                    border = androidx.compose.foundation.BorderStroke(2.dp, PharaohGold)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("📜", fontSize = 24.sp)
                            Text(
                                text = "اكتب اسمك بالهيروغليفية الملكية",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = PharaohGold
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "اكتب اسمك أو أي كلمة بالعربية أو الإنجليزية لترى ترجمتها داخل الخرطوشة الملكية",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = inputText,
                            onValueChange = onInputChange,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("hieroglyphs_input_field"),
                            placeholder = { Text("اكتب اسمك هنا...", color = Color.Gray) },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = PharaohGold,
                                unfocusedBorderColor = PharaohGold.copy(alpha = 0.5f)
                            )
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Royal Cartouche Box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(32.dp))
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0xFF282012),
                                            Color(0xFF161208)
                                        )
                                    )
                                )
                                .border(
                                    3.dp,
                                    Brush.linearGradient(
                                        listOf(PharaohGoldLight, PharaohGold, PharaohGoldDark)
                                    ),
                                    RoundedCornerShape(32.dp)
                                )
                                .padding(horizontal = 24.dp, vertical = 20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                // Top royal loop knot
                                Surface(
                                    shape = CircleShape,
                                    color = PharaohGold,
                                    modifier = Modifier.size(12.dp)
                                ) {}

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = if (translatedText.isNotBlank()) translatedText else "𓄿 𓅓 𓏏 𓆓",
                                    fontSize = 34.sp,
                                    color = PharaohGold,
                                    letterSpacing = 4.sp,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 44.sp,
                                    modifier = Modifier.testTag("cartouche_result_text")
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = if (inputText.isNotBlank()) "« $inputText »" else "اسمك الملكي",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                // Bottom cartouche bar knot
                                Box(
                                    modifier = Modifier
                                        .width(60.dp)
                                        .height(4.dp)
                                        .background(PharaohGold, RoundedCornerShape(2.dp))
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Copy button
                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Hieroglyphs", "$inputText: $translatedText")
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "تم نسخ الاسم الهيروغليفي بنجاح!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.testTag("copy_hieroglyphs_button")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "نسخ", tint = PharaohGold)
                                Text("نسخ النص", color = PharaohGold, style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }

            // Alphabet Guide Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "جدول الأبجدية الهيروغليفية الصوتية",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = PharaohGold.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "${CivilizationData.hieroglyphicAlphabet.size} رمزاً",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = PharaohGold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Alphabet Glyphs List
            items(CivilizationData.hieroglyphicAlphabet) { glyph ->
                HieroglyphItemCard(glyph = glyph)
            }
        }
    }
}

@Composable
fun HieroglyphItemCard(glyph: HieroglyphGlyph) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Glyph Box
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = PharaohGold.copy(alpha = 0.15f),
                border = androidx.compose.foundation.BorderStroke(1.dp, PharaohGold),
                modifier = Modifier.size(54.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = glyph.hieroglyphSymbol,
                        fontSize = 30.sp,
                        color = PharaohGold
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "الحرف: ${glyph.arabicLetter}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "(${glyph.englishLetter})",
                        style = MaterialTheme.typography.bodySmall,
                        color = PharaohGold
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "الشكل: ${glyph.name}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = glyph.meaning,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
            }
        }
    }
}
