package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import android.app.Activity
import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.SystemUpdate
import com.example.BuildConfig
import com.example.R
import com.example.sync.UpdateInfo
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.example.data.Localization
import com.example.data.local.EarnedBadgeEntity
import com.example.model.AppLanguage
import com.example.model.RoyalThemeColor
import com.example.model.ThemeMode
import com.example.model.UserProfile
import com.example.ui.components.PharaohTopAppBar
import com.example.ui.theme.NileLapisDark
import com.example.ui.theme.PharaohGold
import com.example.ui.theme.PharaohGoldDark
import com.example.ui.theme.PharaohGoldLight
import com.example.ui.theme.TerracottaRed
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AccountSettingsScreen(
    userProfile: UserProfile,
    currentLanguage: AppLanguage,
    themeMode: ThemeMode,
    roomBadges: List<EarnedBadgeEntity>,
    userTitle: Pair<String, String>,
    totalPoints: Int,
    onLogin: (name: String, email: String, avatar: String) -> Unit,
    onLogout: () -> Unit,
    onSyncData: () -> Unit,
    onLanguageChange: (AppLanguage) -> Unit,
    onThemeChange: (ThemeMode) -> Unit,
    royalThemeColor: RoyalThemeColor = RoyalThemeColor.MONOCHROME,
    onSelectRoyalColor: (RoyalThemeColor) -> Unit = {},
    onCycleRoyalColor: () -> Unit = {},
    onNavigateBack: () -> Unit,
    updateInfo: UpdateInfo = UpdateInfo(),
    onCheckForUpdates: () -> Unit = {},
    onDownloadAndInstall: (String) -> Unit = {},
    isCloudSyncing: Boolean = false,
    cloudSyncMessage: String? = null,
    onUploadToCloud: () -> Unit = {},
    onDownloadFromCloud: () -> Unit = {},
    onOpenWelcome: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    BackHandler {
        onNavigateBack()
    }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = remember { CredentialManager.create(context) }
    var isGoogleSigningIn by remember { mutableStateOf(false) }

    var inputName by remember { mutableStateOf(if (userProfile.isLoggedIn) userProfile.name else "") }
    var inputEmail by remember { mutableStateOf(if (userProfile.isLoggedIn) userProfile.email else "") }
    var selectedAvatar by remember { mutableStateOf(userProfile.avatarEmoji) }

    fun triggerGoogleSignIn() {
        val clientId = try {
            context.getString(R.string.default_web_client_id)
        } catch (e: Exception) {
            Toast.makeText(context, "Google Client ID غير معرف", Toast.LENGTH_SHORT).show()
            return
        }

        val signInOption = GetSignInWithGoogleOption.Builder(serverClientId = clientId).build()
        val request = GetCredentialRequest.Builder().addCredentialOption(signInOption).build()

        isGoogleSigningIn = true
        scope.launch {
            try {
                val result = credentialManager.getCredential(context as Activity, request)
                val credential = result.credential
                if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
                    val authCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                    val authResult = Firebase.auth.signInWithCredential(authCredential).await()
                    val user = authResult.user
                    val gName = user?.displayName ?: "باحث الفراعنة"
                    val gEmail = user?.email ?: ""
                    onLogin(gName, gEmail, selectedAvatar)
                    Toast.makeText(context, "تم تسجيل الدخول بواسطة Google وحفظ حسابك سحابياً! 🌐", Toast.LENGTH_SHORT).show()
                    onUploadToCloud()
                }
            } catch (e: GetCredentialCancellationException) {
                Log.w("Auth", "Google sign-in cancelled: ${e.message}")
            } catch (e: Exception) {
                Log.e("Auth", "Google sign-in error: ${e.message}", e)
                Toast.makeText(context, "فشل تسجيل الدخول: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            } finally {
                isGoogleSigningIn = false
            }
        }
    }

    val avatars = listOf("👑", "🧕", "🦅", "☥", "📜", "🦁")

    Scaffold(
        topBar = {
            PharaohTopAppBar(
                title = Localization.get("account_title", currentLanguage),
                onNavigateBack = onNavigateBack,
                onToggleTheme = {
                    onThemeChange(if (themeMode == ThemeMode.DARK) ThemeMode.LIGHT else ThemeMode.DARK)
                },
                onCycleThemeColor = onCycleRoyalColor,
                isDarkTheme = themeMode == ThemeMode.DARK
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("account_settings_screen"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Info Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, PharaohGold)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(PharaohGold)
                                .border(2.dp, Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(userProfile.avatarEmoji, fontSize = 36.sp)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = if (userProfile.isLoggedIn) userProfile.name else Localization.get("guest_user", currentLanguage),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = PharaohGold
                        )

                        Text(
                            text = if (userProfile.isLoggedIn) userProfile.email else Localization.get("account_desc", currentLanguage),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )

                        if (userProfile.isLoggedIn) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = PharaohGold.copy(alpha = 0.2f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, PharaohGold)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(Icons.Default.CloudDone, contentDescription = null, tint = PharaohGold, modifier = Modifier.size(14.dp))
                                    Text(
                                        text = "حساب نشط ومحفوظ",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = PharaohGold,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Black & White Theme Mode Selection Card (Core User Requirement)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("theme_mode_switch_card"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PharaohGold.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("🌓", fontSize = 20.sp)
                            Text(
                                text = if (currentLanguage == AppLanguage.ARABIC) "مظهر التطبيق (أبيض وأسود)" else "Appearance (Black & White)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = if (currentLanguage == AppLanguage.ARABIC) "اختر بين الخلفية السوداء النقية أو البيضاء عالية التباين" else "Switch between pure black background and crisp white theme",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ThemeMode.values().forEach { mode ->
                                val isSelected = themeMode == mode
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) PharaohGold.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                                    border = androidx.compose.foundation.BorderStroke(
                                        if (isSelected) 1.5.dp else 0.5.dp,
                                        if (isSelected) PharaohGold else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { onThemeChange(mode) }
                                        .testTag("theme_btn_${mode.code}")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(mode.icon, fontSize = 20.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = if (currentLanguage == AppLanguage.ARABIC) mode.titleAr else mode.titleEn,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) PharaohGold else MaterialTheme.colorScheme.onSurface,
                                            fontSize = 12.sp
                                        )
                                        if (isSelected) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Icon(Icons.Default.Check, contentDescription = null, tint = PharaohGold, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }



            // Language Switcher Section (Core User Requirement: تغيير اللغة)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("language_switch_card"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PharaohGold.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Language, contentDescription = null, tint = PharaohGold)
                            Text(
                                text = Localization.get("language_section_title", currentLanguage),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = Localization.get("language_section_desc", currentLanguage),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            AppLanguage.values().forEach { lang ->
                                val isSelected = currentLanguage == lang
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) PharaohGold.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                                    border = androidx.compose.foundation.BorderStroke(
                                        if (isSelected) 1.5.dp else 0.5.dp,
                                        if (isSelected) PharaohGold else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { onLanguageChange(lang) }
                                        .testTag("lang_btn_${lang.code}")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(lang.flag, fontSize = 20.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = lang.displayName,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) PharaohGold else MaterialTheme.colorScheme.onSurface
                                        )
                                        if (isSelected) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Icon(Icons.Default.Check, contentDescription = null, tint = PharaohGold, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Sign In / Profile Manager Section (Core User Requirement: مكان لتسجيل الدخول عشان حفظ البيانات)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("account_login_card"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PharaohGold.copy(alpha = 0.4f))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = PharaohGold)
                            Text(
                                text = if (userProfile.isLoggedIn) "إدارة الحساب ومزامنة البيانات" else Localization.get("login_card_title", currentLanguage),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = if (userProfile.isLoggedIn) "بياناتك وتقدمك في الأقسام والأوسمة مربوطة بحسابك" else Localization.get("login_card_subtitle", currentLanguage),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        if (!userProfile.isLoggedIn) {
                            // Name field
                            OutlinedTextField(
                                value = inputName,
                                onValueChange = { inputName = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("login_name_input"),
                                label = { Text(Localization.get("name_field", currentLanguage)) },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PharaohGold
                                )
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // Email field
                            OutlinedTextField(
                                value = inputEmail,
                                onValueChange = { inputEmail = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("login_email_input"),
                                label = { Text(Localization.get("email_field", currentLanguage)) },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PharaohGold
                                )
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Avatar selector
                            Text(
                                text = Localization.get("select_avatar", currentLanguage),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                avatars.forEach { avatar ->
                                    val isSelected = selectedAvatar == avatar
                                    Box(
                                        modifier = Modifier
                                            .size(46.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (isSelected) PharaohGold.copy(alpha = 0.25f) else MaterialTheme.colorScheme.surfaceVariant
                                            )
                                            .border(
                                                if (isSelected) 2.dp else 0.5.dp,
                                                if (isSelected) PharaohGold else Color.Transparent,
                                                CircleShape
                                            )
                                            .clickable { selectedAvatar = avatar },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(avatar, fontSize = 22.sp)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            // Google Sign-In via Credential Manager
                            Button(
                                onClick = { triggerGoogleSignIn() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("google_sign_in_button"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = NileLapisDark,
                                    contentColor = Color.White
                                ),
                                border = androidx.compose.foundation.BorderStroke(1.2.dp, PharaohGold)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text("🌐", fontSize = 16.sp)
                                    Text(
                                        text = if (isGoogleSigningIn) "جاري الاتصال بـ Google..." else if (currentLanguage == AppLanguage.ARABIC) "تسجيل الدخول السريع عبر Google ✦" else "Fast Sign in with Google ✦",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = PharaohGold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Button(
                                onClick = {
                                    val name = inputName.ifBlank { "باحث الفراعنة" }
                                    val email = inputEmail.ifBlank { "explorer@egypt.ancient" }
                                    onLogin(name, email, selectedAvatar)
                                    Toast.makeText(context, "تم تسجيل الدخول وحفظ البيانات بنجاح! ☥", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("submit_login_button"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = PharaohGold,
                                    contentColor = Color.Black
                                )
                            ) {
                                Text(
                                    text = Localization.get("sign_in_btn", currentLanguage),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                        } else {
                            // Logged In view: Sync and Logout
                            val lastSyncStr = SimpleDateFormat("yyyy/MM/dd - hh:mm a", Locale.getDefault())
                                .format(Date(userProfile.lastSyncTimestamp))

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = PharaohGold.copy(alpha = 0.1f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, PharaohGold.copy(alpha = 0.5f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Default.CloudDone, contentDescription = null, tint = PharaohGold)
                                    Column {
                                        Text(
                                            text = "آخر حفظ ومزامنة ناجحة:",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = PharaohGold
                                        )
                                        Text(
                                            text = lastSyncStr,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = {
                                        onSyncData()
                                        Toast.makeText(context, Localization.get("sync_success_msg", currentLanguage), Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(46.dp)
                                        .testTag("sync_data_button"),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = PharaohGold,
                                        contentColor = Color.Black
                                    )
                                ) {
                                    Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = Localization.get("sync_data_btn", currentLanguage),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }

                                OutlinedButton(
                                    onClick = {
                                        onLogout()
                                        Toast.makeText(context, "تم تسجيل الخروج", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(46.dp)
                                        .testTag("logout_button"),
                                    shape = RoundedCornerShape(12.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, TerracottaRed)
                                ) {
                                    Icon(Icons.Default.Logout, contentDescription = null, tint = TerracottaRed, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = Localization.get("sign_out_btn", currentLanguage),
                                        color = TerracottaRed,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Data Persistence Summary
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Storage, contentDescription = null, tint = PharaohGold)
                            Text(
                                text = Localization.get("sync_status_title", currentLanguage),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = Localization.get("sync_status_desc", currentLanguage),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${roomBadges.size}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = PharaohGold
                                )
                                Text("أوسمة Room", style = MaterialTheme.typography.labelSmall)
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$totalPoints",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = PharaohGold
                                )
                                Text("نقاط محفوظة", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }

            // Royal Pharaonic Firestore Cloud Sync & Multi-Device Sync Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("firestore_cloud_sync_card"),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.5.dp,
                        Brush.horizontalGradient(
                            listOf(
                                PharaohGoldDark,
                                PharaohGold,
                                PharaohGoldLight,
                                PharaohGold,
                                PharaohGoldDark
                            )
                        )
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        NileLapisDark.copy(alpha = 0.45f),
                                        Color.Transparent
                                    )
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Column {
                            // Header: Pharaonic Cloud Seal & Title & Status
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = PharaohGold.copy(alpha = 0.2f),
                                        border = androidx.compose.foundation.BorderStroke(1.5.dp, PharaohGold),
                                        modifier = Modifier.size(46.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text("☁️", fontSize = 24.sp)
                                        }
                                    }

                                    Column {
                                        Text(
                                            text = if (currentLanguage == AppLanguage.ARABIC) "سحابة الفراعنة الفورية (Firestore)" else "Royal Firestore Cloud Sync",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = if (currentLanguage == AppLanguage.ARABIC) "مزامنة بين كافة الأجهزة بدون كابلات ✦" else "Wireless instant sync across all devices ✦",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = PharaohGold,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Live Database Connection Status Badge
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, PharaohGold.copy(alpha = 0.4f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text("✨", fontSize = 10.sp)
                                        Text(
                                            text = if (currentLanguage == AppLanguage.ARABIC) "سحابة Firebase متصلة ونشطة" else "Firebase Firestore Connected",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = PharaohGold
                                        )
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = PharaohGold.copy(alpha = 0.15f),
                                        border = androidx.compose.foundation.BorderStroke(0.8.dp, PharaohGold)
                                    ) {
                                        Text(
                                            text = "v${BuildConfig.VERSION_NAME} • Live",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = PharaohGold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Informational Cartouche on Multi-Device Sync (Direct user requirement)
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = NileLapisDark.copy(alpha = 0.6f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, PharaohGoldDark),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Text("📡", fontSize = 20.sp)
                                    Text(
                                        text = if (currentLanguage == AppLanguage.ARABIC) {
                                            "ميزة المزامنة الشاملة: أي تعديل أو تقدم تقوم به هنا، يتم حفظه فوراً على السحابة، ويمكن لأي جهاز قمت بتثبيت التطبيق عليه عبر USB سحب التعديلات فوراً!"
                                        } else {
                                            "Multi-device sync: any updates made here are saved instantly to the cloud so all other devices can pull them immediately!"
                                        },
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            if (isCloudSyncing) {
                                Spacer(modifier = Modifier.height(12.dp))
                                LinearProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(4.dp)
                                        .clip(RoundedCornerShape(2.dp)),
                                    color = PharaohGold,
                                    trackColor = PharaohGold.copy(alpha = 0.2f)
                                )
                            }

                            if (cloudSyncMessage != null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = cloudSyncMessage,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = PharaohGold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // 1. Primary Action: Upload everything to Cloud NOW (User Request: "خط الي انا عماتو دلوقتي عاى السحابه فورا")
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                border = androidx.compose.foundation.BorderStroke(1.2.dp, PharaohGoldLight),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .clickable(enabled = !isCloudSyncing) {
                                        onUploadToCloud()
                                        Toast.makeText(
                                            context,
                                            if (currentLanguage == AppLanguage.ARABIC) "جاري رفع وحفظ كافة بياناتك على السحابة فوراً... ☁️" else "Uploading progress to cloud immediately... ☁️",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    .testTag("upload_to_cloud_button")
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.horizontalGradient(
                                                listOf(
                                                    PharaohGoldDark,
                                                    PharaohGold,
                                                    PharaohGoldLight,
                                                    PharaohGold
                                                )
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CloudUpload,
                                            contentDescription = null,
                                            tint = Color.Black,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Text(
                                            text = if (currentLanguage == AppLanguage.ARABIC) "رفع وحفظ ما تم إنجازه للسحابة فوراً ✦" else "Push & Save Everything to Cloud Now ✦",
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 14.sp,
                                            color = Color.Black
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // 2. Secondary Action: Pull/Download from Cloud to update this device
                            OutlinedButton(
                                onClick = {
                                    onDownloadFromCloud()
                                    Toast.makeText(
                                        context,
                                        if (currentLanguage == AppLanguage.ARABIC) "جاري سحب التعديلات من السحابة لهذا الجهاز... 📥" else "Pulling updates from cloud... 📥",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("download_from_cloud_button"),
                                shape = RoundedCornerShape(14.dp),
                                border = androidx.compose.foundation.BorderStroke(1.2.dp, PharaohGold)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CloudDownload,
                                        contentDescription = null,
                                        tint = PharaohGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = if (currentLanguage == AppLanguage.ARABIC) "سحب ومزامنة بيانات الأجهزة الأخرى 📥" else "Pull & Sync Data from Other Devices 📥",
                                        color = PharaohGold,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Ancient Egypt Overview & Welcome Re-visitation Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenWelcome() }
                        .testTag("account_open_welcome_card"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.2.dp, PharaohGold.copy(alpha = 0.6f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(PharaohGold.copy(alpha = 0.2f))
                                .border(1.2.dp, PharaohGold, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("📜", fontSize = 24.sp)
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (currentLanguage == AppLanguage.ARABIC) "مقدمة تاريخ مصر القديمة والواجهة الترحيبية" else "Ancient Egypt History & Welcome Intro",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = PharaohGold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (currentLanguage == AppLanguage.ARABIC) "أعد قراءة النبذة التاريخية عن فجر التاريخ وعظمة الفراعنة" else "Revisit the historical overview of the dawn of history and pharaohs",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = PharaohGold,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // App Package and APK Management Card (hadarat_misr.apk)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("app_update_apk_card"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.2.dp, PharaohGold.copy(alpha = 0.6f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(PharaohGold.copy(alpha = 0.15f))
                                    .border(1.2.dp, PharaohGold, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SystemUpdate,
                                    contentDescription = null,
                                    tint = PharaohGold,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (currentLanguage == AppLanguage.ARABIC) "حزمة التطبيق (hadarat_misr.apk)" else "App Package (hadarat_misr.apk)",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (currentLanguage == AppLanguage.ARABIC) "الإصدار v${BuildConfig.VERSION_NAME} (Build ${BuildConfig.VERSION_CODE}) • جاهز ومثبت" else "Version v${BuildConfig.VERSION_NAME} (Build ${BuildConfig.VERSION_CODE}) • Installed & Ready",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = PharaohGold.copy(alpha = 0.15f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, PharaohGold)
                            ) {
                                Text(
                                    text = "v${BuildConfig.VERSION_NAME}",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = PharaohGold
                                )
                            }
                        }

                        // Release Notes Box
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = if (currentLanguage == AppLanguage.ARABIC) "ملاحظات الإصدار الجديد:" else "New Version Highlights:",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = PharaohGold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (currentLanguage == AppLanguage.ARABIC) updateInfo.releaseNotesAr else updateInfo.releaseNotesEn,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Actions Row
                        Button(
                            onClick = {
                                onCheckForUpdates()
                                Toast.makeText(
                                    context,
                                    if (currentLanguage == AppLanguage.ARABIC) "حزمة hadarat_misr.apk (الإصدار v${BuildConfig.VERSION_NAME}) محدثة وتعمل بنجاح! ✨" else "hadarat_misr.apk (v${BuildConfig.VERSION_NAME}) is up to date and active! ✨",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("check_update_button"),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PharaohGold)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Sync,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = if (currentLanguage == AppLanguage.ARABIC) "التحقق من حالة الحزمة والتحديثات" else "Check Package & Update Status",
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
