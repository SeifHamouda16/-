package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.AppLanguage
import com.example.ui.theme.PharaohGold
import com.example.ui.theme.PharaohGoldContainer
import com.example.ui.theme.PureBlack
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TerracottaRed
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * Egyptian Games Hub
 * Features:
 * 1. Royal Game of Senet (لعبة السنت الملكية - أقدم لعبة لوحية في تاريخ البشرية)
 * 2. Pharaonic Sacred Amulets Match (لعبة مطابقة التمائم الفرعونية المقدسة)
 * 3. Pyramid Builder & Stone Balance (لغز بناء الهرم الأكبر وتوازن الكتل)
 * 4. Hieroglyph Decipher Blitz (تحدي كاتب البردي وفك رموز الهيروغليفية)
 */

enum class GameCategory(val titleAr: String, val titleEn: String, val iconEmoji: String) {
    SENET("لعبة السنت الملكية", "Royal Senet", "♟️"),
    AMULETS_MATCH("مطابقة التمائم", "Amulet Match", "🪲"),
    PYRAMID_BUILDER("بناء الهرم", "Pyramid Architect", "🔺"),
    HIEROGLYPH_BLITZ("لغز الهيروغليفية", "Hieroglyph Blitz", "📜")
}

@Composable
fun AncientEgyptianGamesScreen(
    currentLanguage: AppLanguage,
    isDarkTheme: Boolean,
    onNavigateBack: () -> Unit,
    onToggleTheme: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onNavigateBack() }

    var selectedGame by remember { mutableStateOf(GameCategory.SENET) }
    var showHowToPlayDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.testTag("ancient_egyptian_games_screen"),
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 4.dp,
                border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            IconButton(
                                onClick = onNavigateBack,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .testTag("games_back_button")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = PharaohGold
                                )
                            }

                            Column {
                                Text(
                                    text = if (currentLanguage == AppLanguage.ARABIC) "ألعاب المصري القديم" else "Ancient Egyptian Games",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (currentLanguage == AppLanguage.ARABIC) "تسلية الملوك وحكمة الأجداد" else "Royalty Entertainment & Wisdom",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = PharaohGold
                                )
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            // How to play button
                            IconButton(
                                onClick = { showHowToPlayDialog = true },
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .testTag("games_info_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Info",
                                    tint = PharaohGold,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            // Theme toggle button
                            IconButton(
                                onClick = onToggleTheme,
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .testTag("games_theme_toggle_button")
                            ) {
                                Text(if (isDarkTheme) "🌙" else "☀️", fontSize = 16.sp)
                            }
                        }
                    }

                    // Games Navigation Tabs
                    TabRow(
                        selectedTabIndex = selectedGame.ordinal,
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = PharaohGold,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedGame.ordinal]),
                                color = PharaohGold,
                                height = 3.dp
                            )
                        }
                    ) {
                        GameCategory.values().forEach { category ->
                            val isSelected = selectedGame == category
                            Tab(
                                selected = isSelected,
                                onClick = { selectedGame = category },
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(category.iconEmoji, fontSize = 14.sp)
                                        Text(
                                            text = if (currentLanguage == AppLanguage.ARABIC) category.titleAr else category.titleEn,
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) PharaohGold else MaterialTheme.colorScheme.onSurfaceVariant,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                },
                                modifier = Modifier.testTag("game_tab_${category.name.lowercase()}")
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedGame) {
                GameCategory.SENET -> RoyalSenetGame(currentLanguage = currentLanguage)
                GameCategory.AMULETS_MATCH -> SacredAmuletsMatchGame(currentLanguage = currentLanguage)
                GameCategory.PYRAMID_BUILDER -> PyramidBuilderGame(currentLanguage = currentLanguage)
                GameCategory.HIEROGLYPH_BLITZ -> HieroglyphBlitzGame(currentLanguage = currentLanguage)
            }

            if (showHowToPlayDialog) {
                GamesHowToPlayDialog(
                    selectedGame = selectedGame,
                    currentLanguage = currentLanguage,
                    onDismiss = { showHowToPlayDialog = false }
                )
            }
        }
    }
}

// -------------------------------------------------------------
// 1. ROYAL GAME OF SENET (لعبة السنت الملكية الخالدة)
// -------------------------------------------------------------

@Composable
fun RoyalSenetGame(currentLanguage: AppLanguage) {
    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()

    var playerPieces by remember { mutableStateOf(listOf(1, 3, 5, 7, 9)) }
    var pharaohPieces by remember { mutableStateOf(listOf(2, 4, 6, 8, 10)) }

    var isPlayerTurn by remember { mutableStateOf(true) }
    var castingSticksResult by remember { mutableIntStateOf(0) }
    var sticksArray by remember { mutableStateOf(listOf(true, false, true, false)) }
    var hasCastSticks by remember { mutableStateOf(false) }
    var selectedPieceSquare by remember { mutableStateOf<Int?>(null) }
    var gameMessage by remember {
        mutableStateOf(
            if (currentLanguage == AppLanguage.ARABIC)
                "ألقِ عصي السنت لتعرف عدد الحركات، ثم انقل قطعتك نحو بيت الخلود!"
            else
                "Cast the Senet sticks to determine your moves, then guide your piece to Eternity!"
        )
    }
    var playerBorneOffCount by remember { mutableIntStateOf(0) }
    var pharaohBorneOffCount by remember { mutableIntStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }
    var winnerName by remember { mutableStateOf("") }
    var isCastingAnimation by remember { mutableStateOf(false) }

    val houseOfRebirth = 15 // بيت البعث
    val houseOfBeauty = 26  // بيت الجمال والخير
    val houseOfWater = 27   // بيت الماء (فخ الخطر)
    val houseOfThreeTruths = 28 // بيت الحقائق الثلاث
    val houseOfRe = 29      // بيت رع حور
    val houseOfEternity = 30 // بيت الخلود

    fun restartGame() {
        playerPieces = listOf(1, 3, 5, 7, 9)
        pharaohPieces = listOf(2, 4, 6, 8, 10)
        isPlayerTurn = true
        castingSticksResult = 0
        hasCastSticks = false
        selectedPieceSquare = null
        playerBorneOffCount = 0
        pharaohBorneOffCount = 0
        isGameOver = false
        winnerName = ""
        gameMessage = if (currentLanguage == AppLanguage.ARABIC)
            "بدأت جولة سنت جديدة! ألقِ العصي الملكية لبدء التحرك."
        else
            "New Senet game started! Cast the royal sticks to begin."
    }

    fun castSticks(): Int {
        val sticks = List(4) { Random.nextBoolean() }
        sticksArray = sticks
        val lightCount = sticks.count { it }
        return if (lightCount == 0) 5 else lightCount
    }

    fun triggerPharaohMove() {
        coroutineScope.launch {
            delay(900)
            isCastingAnimation = true
            delay(500)
            val aiMoves = castSticks()
            castingSticksResult = aiMoves
            isCastingAnimation = false
            delay(600)

            val validAiMoves = mutableListOf<Pair<Int, Int>>()
            for (pos in pharaohPieces) {
                val target = pos + aiMoves
                if (target <= 30) {
                    if (!pharaohPieces.contains(target)) {
                        validAiMoves.add(Pair(pos, target))
                    }
                } else if (target == 31) {
                    validAiMoves.add(Pair(pos, 31))
                }
            }

            if (validAiMoves.isNotEmpty()) {
                val bestMove = validAiMoves.maxByOrNull { move ->
                    var score = move.second
                    if (playerPieces.contains(move.second)) score += 50
                    if (move.second == houseOfBeauty) score += 30
                    if (move.second == houseOfWater) score -= 40
                    if (move.second == 31) score += 100
                    score
                } ?: validAiMoves.first()

                val from = bestMove.first
                var to = bestMove.second

                if (to == houseOfWater) {
                    to = houseOfRebirth
                }

                val newAiPieces = pharaohPieces.toMutableList()
                newAiPieces.remove(from)

                if (to == 31) {
                    pharaohBorneOffCount++
                    if (pharaohBorneOffCount >= 5) {
                        isGameOver = true
                        winnerName = if (currentLanguage == AppLanguage.ARABIC) "الفرعون الذكي (AI)" else "Pharaoh AI"
                    }
                } else {
                    if (playerPieces.contains(to)) {
                        val newPlayerPieces = playerPieces.toMutableList()
                        newPlayerPieces.remove(to)
                        newPlayerPieces.add(from)
                        playerPieces = newPlayerPieces.sorted()
                    }
                    newAiPieces.add(to)
                }
                pharaohPieces = newAiPieces.sorted()

                gameMessage = if (currentLanguage == AppLanguage.ARABIC)
                    "حرّك الفرعون قطعته بمقدار $aiMoves مربعات. دورك الآن!"
                else
                    "Pharaoh moved a piece by $aiMoves squares. Your turn!"
            } else {
                gameMessage = if (currentLanguage == AppLanguage.ARABIC)
                    "لم يجد الفرعون نقلة شرعية بمقدار $aiMoves. انتقل الدور إليك!"
                else
                    "Pharaoh has no valid moves with $aiMoves. Your turn!"
            }

            isPlayerTurn = true
            hasCastSticks = false
            selectedPieceSquare = null
        }
    }

    fun makePlayerMove(fromSquare: Int) {
        if (!hasCastSticks || castingSticksResult == 0 || !isPlayerTurn) return

        var targetSquare = fromSquare + castingSticksResult

        if (targetSquare == houseOfWater) {
            targetSquare = houseOfRebirth
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }

        if (targetSquare <= 30) {
            if (playerPieces.contains(targetSquare)) {
                gameMessage = if (currentLanguage == AppLanguage.ARABIC)
                    "لا يمكنك الوقوف على مربع تشغله قطعتك بالفعل!"
                else
                    "Cannot land on a square occupied by your own piece!"
                return
            }
        }

        val newPlayerPieces = playerPieces.toMutableList()
        newPlayerPieces.remove(fromSquare)

        if (targetSquare >= 31) {
            playerBorneOffCount++
            gameMessage = if (currentLanguage == AppLanguage.ARABIC)
                "✨ عبرت قطعة ملكية إلى بيت الخلود والأبدية!"
            else
                "✨ A piece passed into eternal victory!"
            if (playerBorneOffCount >= 5) {
                isGameOver = true
                winnerName = if (currentLanguage == AppLanguage.ARABIC) "أنت (بطل السنت الملكي)" else "You (Senet Champion)"
            }
        } else {
            if (pharaohPieces.contains(targetSquare)) {
                val newPharaohPieces = pharaohPieces.toMutableList()
                newPharaohPieces.remove(targetSquare)
                newPharaohPieces.add(fromSquare)
                pharaohPieces = newPharaohPieces.sorted()
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                gameMessage = if (currentLanguage == AppLanguage.ARABIC)
                    "⚔️ أسرَت قطعتك قطعة الفرعون وأعادتها للخلف!"
                else
                    "⚔️ You captured Pharaoh's piece!"
            } else {
                gameMessage = if (currentLanguage == AppLanguage.ARABIC)
                    "نقلت قطعتك إلى المربع $targetSquare."
                else
                    "Moved your piece to square $targetSquare."
            }
            newPlayerPieces.add(targetSquare)
        }

        playerPieces = newPlayerPieces.sorted()
        hasCastSticks = false
        selectedPieceSquare = null

        if (!isGameOver) {
            isPlayerTurn = false
            triggerPharaohMove()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .testTag("senet_game_view"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header & Score Board
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.2.dp, PharaohGold.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (currentLanguage == AppLanguage.ARABIC) "لعبة السنت الملكية (30 مربعاً)" else "Royal Game of Senet (30 Squares)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (currentLanguage == AppLanguage.ARABIC) "عثر عليها في مقبرة توت عنخ آمون والملكة نفرتاري" else "Found in Tutankhamun & Nefertari tombs",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        IconButton(
                            onClick = { restartGame() },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .testTag("senet_restart_button")
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = "Restart", tint = PharaohGold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            color = if (isPlayerTurn) PharaohGold.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
                            border = BorderStroke(if (isPlayerTurn) 1.5.dp else 0.5.dp, if (isPlayerTurn) PharaohGold else MaterialTheme.colorScheme.outline)
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text("♟️", fontSize = 18.sp)
                                Column {
                                    Text(
                                        text = if (currentLanguage == AppLanguage.ARABIC) "قطعك (أبيض)" else "You (White)",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = PharaohGold
                                    )
                                    Text(
                                        text = if (currentLanguage == AppLanguage.ARABIC) "عبرت: $playerBorneOffCount / 5" else "Saved: $playerBorneOffCount / 5",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }

                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            color = if (!isPlayerTurn) PharaohGold.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
                            border = BorderStroke(if (!isPlayerTurn) 1.5.dp else 0.5.dp, if (!isPlayerTurn) PharaohGold else MaterialTheme.colorScheme.outline)
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text("♙", fontSize = 18.sp)
                                Column {
                                    Text(
                                        text = if (currentLanguage == AppLanguage.ARABIC) "الفرعون (أسود)" else "Pharaoh (Black)",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = if (currentLanguage == AppLanguage.ARABIC) "عبرت: $pharaohBorneOffCount / 5" else "Saved: $pharaohBorneOffCount / 5",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = gameMessage,
                        style = MaterialTheme.typography.bodySmall,
                        color = PharaohGold,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Senet Casting Sticks Section
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (currentLanguage == AppLanguage.ARABIC) "عصي السنت (بديل النرد الفرعوني)" else "Senet Casting Sticks (Ancient Dice)",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        sticksArray.forEachIndexed { idx, isLight ->
                            Surface(
                                modifier = Modifier
                                    .width(16.dp)
                                    .height(48.dp)
                                    .graphicsLayer {
                                        rotationZ = if (isCastingAnimation) (idx * 35f) else 0f
                                    },
                                shape = RoundedCornerShape(8.dp),
                                color = if (isLight) PureWhite else PureBlack,
                                border = BorderStroke(1.5.dp, PharaohGold)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    if (isLight) {
                                        Text("•", color = PureBlack, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = PharaohGoldContainer,
                            border = BorderStroke(1.dp, PharaohGold)
                        ) {
                            Text(
                                text = if (hasCastSticks) "$castingSticksResult" else "-",
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = PharaohGold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            if (isPlayerTurn && !hasCastSticks) {
                                isCastingAnimation = true
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                coroutineScope.launch {
                                    delay(400)
                                    val moves = castSticks()
                                    castingSticksResult = moves
                                    hasCastSticks = true
                                    isCastingAnimation = false
                                    gameMessage = if (currentLanguage == AppLanguage.ARABIC)
                                        "حصلت على $moves حركات! اختر قطعة من قطعك لتحريكها."
                                    else
                                        "You got $moves moves! Select one of your pieces to move."
                                }
                            }
                        },
                        enabled = isPlayerTurn && !hasCastSticks && !isGameOver,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("senet_cast_sticks_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = PharaohGold),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Casino, contentDescription = null, tint = PureBlack, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (currentLanguage == AppLanguage.ARABIC) "إلقاء العصي الملكية ✦" else "Cast Sticks ✦",
                            color = PureBlack,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Interactive 30-Squares Board
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.2.dp, PharaohGold)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = if (currentLanguage == AppLanguage.ARABIC) "لوحة السنت (المسار الحلزوني 1 ➔ 30)" else "Senet Board (Boustrophedon 1 ➔ 30)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    val row1 = (1..10).toList()
                    val row2 = (20 downTo 11).toList()
                    val row3 = (21..30).toList()

                    listOf(row1, row2, row3).forEach { rowSquares ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            rowSquares.forEach { squareNumber ->
                                val hasPlayer = playerPieces.contains(squareNumber)
                                val hasPharaoh = pharaohPieces.contains(squareNumber)
                                val isSelected = selectedPieceSquare == squareNumber

                                val isSpecialSquare = squareNumber in listOf(
                                    houseOfRebirth,
                                    houseOfBeauty,
                                    houseOfWater,
                                    houseOfThreeTruths,
                                    houseOfRe,
                                    houseOfEternity
                                )

                                val squareSymbol = when (squareNumber) {
                                    houseOfRebirth -> "☥"
                                    houseOfBeauty -> "✨"
                                    houseOfWater -> "🌊"
                                    houseOfThreeTruths -> "⚖️"
                                    houseOfRe -> "☀️"
                                    houseOfEternity -> "👑"
                                    else -> "$squareNumber"
                                }

                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp)
                                        .clickable {
                                            if (isPlayerTurn && hasCastSticks && hasPlayer) {
                                                selectedPieceSquare = squareNumber
                                                makePlayerMove(squareNumber)
                                            }
                                        }
                                        .testTag("senet_square_$squareNumber"),
                                    shape = RoundedCornerShape(4.dp),
                                    color = when {
                                        isSelected -> PharaohGold.copy(alpha = 0.35f)
                                        squareNumber == houseOfWater -> TerracottaRed.copy(alpha = 0.25f)
                                        isSpecialSquare -> PharaohGold.copy(alpha = 0.12f)
                                        (squareNumber % 2 == 0) -> MaterialTheme.colorScheme.surfaceVariant
                                        else -> MaterialTheme.colorScheme.surface
                                    },
                                    border = BorderStroke(
                                        if (isSelected) 1.5.dp else 0.6.dp,
                                        if (isSelected) PharaohGold else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                                    )
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            when {
                                                hasPlayer -> {
                                                    Text(
                                                        text = "♟️",
                                                        fontSize = 18.sp,
                                                        color = PureWhite
                                                    )
                                                }
                                                hasPharaoh -> {
                                                    Text(
                                                        text = "♙",
                                                        fontSize = 18.sp,
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                }
                                                else -> {
                                                    Text(
                                                        text = squareSymbol,
                                                        fontSize = if (isSpecialSquare) 11.sp else 8.sp,
                                                        color = if (isSpecialSquare) PharaohGold else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                                        fontWeight = if (isSpecialSquare) FontWeight.Bold else FontWeight.Normal
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Victory banner
        if (isGameOver) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = PharaohGoldContainer),
                    border = BorderStroke(1.5.dp, PharaohGold)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("👑", fontSize = 32.sp)
                        Text(
                            text = if (currentLanguage == AppLanguage.ARABIC) "نهاية المباراة الملكية!" else "Royal Match Concluded!",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PharaohGold
                        )
                        Text(
                            text = if (currentLanguage == AppLanguage.ARABIC) "الفائز بالخلود: $winnerName" else "Winner of Eternity: $winnerName",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = { restartGame() },
                            colors = ButtonDefaults.buttonColors(containerColor = PharaohGold)
                        ) {
                            Text(
                                text = if (currentLanguage == AppLanguage.ARABIC) "العب جولة جديدة ✦" else "Play New Game ✦",
                                color = PureBlack,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 2. SACRED AMULETS MATCH (مطابقة التمائم الفرعونية)
// -------------------------------------------------------------

data class AmuletTemplate(
    val key: String,
    val emoji: String,
    val nameAr: String,
    val nameEn: String,
    val loreAr: String,
    val loreEn: String
)

data class AmuletCard(
    val id: Int,
    val template: AmuletTemplate,
    val isFlipped: Boolean = false,
    val isMatched: Boolean = false
)

@Composable
fun SacredAmuletsMatchGame(currentLanguage: AppLanguage) {
    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()

    val amuletTemplates = remember {
        listOf(
            AmuletTemplate("ankh", "☥", "مفتاح الحياة (عنخ)", "Ankh (Key of Life)", "رمز الخلود والأبدية والحياة التي وهبتها الآلهة لملوك مصر.", "Symbol of eternity and eternal breath of life given by gods."),
            AmuletTemplate("eye_horus", "👁️", "عين حورس (واجيت)", "Eye of Horus (Wadjet)", "تميمة الحماية الكبرى والشفاء والعدالة والكمال الكوني.", "Supreme amulet of divine protection, healing, and cosmic order."),
            AmuletTemplate("scarab", "🪲", "الجعران المقدس (خبري)", "Sacred Scarab (Khepri)", "رمز تجدد الشمس والبعث والتحول والنشاط الأبدي.", "Symbol of renewal, rising sun, rebirth, and eternal transformation."),
            AmuletTemplate("djed", "🏛️", "عامود الجد", "Djed Pillar", "رمز الثبات والقوة ويرمز للعمود الفقري للإله أوزيريس.", "Symbol of stability and endurance representing the backbone of Osiris."),
            AmuletTemplate("crown", "👑", "التاج المزدوج (بشنت)", "Pschent Double Crown", "رمز اتحاد الشمال والجنوب وسيادة الفرعون على القطرين.", "Symbol of unification between Upper and Lower kingdoms of Egypt."),
            AmuletTemplate("papyrus", "📜", "لفافة البردي والريشة", "Papyrus & Feather of Maat", "رمز الحكمة والتدوين المقدس وميزان محكمة الموتى والعدل.", "Symbol of divine scribal wisdom and the balance of Ma'at justice."),
            AmuletTemplate("jar", "🏺", "الأواني الكانوبية", "Canopic Jars", "أربعة أوانٍ مقدسة تحمي أحشاء المتوفى تحت حراسة أبناء حورس.", "Sacred four vessels protecting the organs under Horus's sons."),
            AmuletTemplate("mask", "⚱️", "قناع توت عنخ آمون", "Tutankhamun Mask", "القناع الجنائزي الذهبي الخالد الذي حمى روح الملك الشاب.", "Pure solid gold mask preserving the eternal identity of the young pharaoh.")
        )
    }

    var cards by remember {
        mutableStateOf(
            (amuletTemplates + amuletTemplates)
                .shuffled()
                .mapIndexed { index, template ->
                    AmuletCard(id = index, template = template)
                }
        )
    }

    var selectedFirstCardIndex by remember { mutableStateOf<Int?>(null) }
    var selectedSecondCardIndex by remember { mutableStateOf<Int?>(null) }
    var isCheckingMatch by remember { mutableStateOf(false) }
    var movesCount by remember { mutableIntStateOf(0) }
    var matchedPairsCount by remember { mutableIntStateOf(0) }
    var discoveredAmulet by remember { mutableStateOf<AmuletTemplate?>(null) }

    fun restartGame() {
        cards = (amuletTemplates + amuletTemplates)
            .shuffled()
            .mapIndexed { index, template ->
                AmuletCard(id = index, template = template)
            }
        selectedFirstCardIndex = null
        selectedSecondCardIndex = null
        isCheckingMatch = false
        movesCount = 0
        matchedPairsCount = 0
        discoveredAmulet = null
    }

    fun onCardClicked(index: Int) {
        if (isCheckingMatch) return
        val card = cards[index]
        if (card.isMatched || card.isFlipped) return

        haptic.performHapticFeedback(HapticFeedbackType.LongPress)

        val updated = cards.toMutableList()
        updated[index] = card.copy(isFlipped = true)
        cards = updated

        if (selectedFirstCardIndex == null) {
            selectedFirstCardIndex = index
        } else if (selectedSecondCardIndex == null) {
            selectedSecondCardIndex = index
            movesCount++
            isCheckingMatch = true

            val firstIdx = selectedFirstCardIndex!!
            val firstCard = cards[firstIdx]
            val secondCard = cards[index]

            if (firstCard.template.key == secondCard.template.key) {
                coroutineScope.launch {
                    delay(400)
                    val matchedList = cards.toMutableList()
                    matchedList[firstIdx] = firstCard.copy(isMatched = true)
                    matchedList[index] = secondCard.copy(isMatched = true)
                    cards = matchedList
                    matchedPairsCount++
                    discoveredAmulet = firstCard.template
                    selectedFirstCardIndex = null
                    selectedSecondCardIndex = null
                    isCheckingMatch = false
                }
            } else {
                coroutineScope.launch {
                    delay(900)
                    val unflipList = cards.toMutableList()
                    unflipList[firstIdx] = firstCard.copy(isFlipped = false)
                    unflipList[index] = secondCard.copy(isFlipped = false)
                    cards = unflipList
                    selectedFirstCardIndex = null
                    selectedSecondCardIndex = null
                    isCheckingMatch = false
                }
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
            .testTag("amulets_match_game_view"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, PharaohGold.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (currentLanguage == AppLanguage.ARABIC) "لعبة مطابقة التمائم الفرعونية" else "Sacred Pharaonic Amulets Match",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (currentLanguage == AppLanguage.ARABIC) "المحاولات: $movesCount | الأزواج: $matchedPairsCount / 8" else "Moves: $movesCount | Pairs: $matchedPairsCount / 8",
                            style = MaterialTheme.typography.bodySmall,
                            color = PharaohGold
                        )
                    }

                    IconButton(
                        onClick = { restartGame() },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .testTag("amulets_restart_button")
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Restart", tint = PharaohGold)
                    }
                }
            }
        }

        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                cards.chunked(4).forEach { rowCards ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowCards.forEach { card ->
                            val cardIndex = cards.indexOf(card)
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(72.dp)
                                    .clickable { onCardClicked(cardIndex) }
                                    .testTag("amulet_card_${card.id}"),
                                shape = RoundedCornerShape(12.dp),
                                color = when {
                                    card.isMatched -> PharaohGold.copy(alpha = 0.25f)
                                    card.isFlipped -> MaterialTheme.colorScheme.surfaceVariant
                                    else -> MaterialTheme.colorScheme.surface
                                },
                                border = BorderStroke(
                                    if (card.isMatched) 1.5.dp else 1.dp,
                                    if (card.isMatched) PharaohGold else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                                )
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (card.isFlipped || card.isMatched) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text(card.template.emoji, fontSize = 24.sp)
                                            Text(
                                                text = if (currentLanguage == AppLanguage.ARABIC) card.template.nameAr.split(" ").first() else card.template.nameEn.split(" ").first(),
                                                style = MaterialTheme.typography.labelSmall,
                                                fontSize = 9.sp,
                                                color = PharaohGold,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    } else {
                                        Text("☥", fontSize = 22.sp, color = MaterialTheme.colorScheme.outline)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        discoveredAmulet?.let { amulet ->
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.2.dp, PharaohGold)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(amulet.emoji, fontSize = 28.sp)
                            Column {
                                Text(
                                    text = if (currentLanguage == AppLanguage.ARABIC) "تميمة مكشوفة: ${amulet.nameAr}" else "Discovered: ${amulet.nameEn}",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = PharaohGold
                                )
                                Text(
                                    text = if (currentLanguage == AppLanguage.ARABIC) amulet.loreAr else amulet.loreEn,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }

        if (matchedPairsCount >= 8) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = PharaohGoldContainer),
                    border = BorderStroke(1.5.dp, PharaohGold)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🎉", fontSize = 32.sp)
                        Text(
                            text = if (currentLanguage == AppLanguage.ARABIC) "أحسنت! طابقت جميع التمائم المقدسة" else "Victory! All Amulets Matched",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PharaohGold
                        )
                        Text(
                            text = if (currentLanguage == AppLanguage.ARABIC) "أنهيت التحدي في $movesCount حركة بنجاح باهر." else "Completed in $movesCount moves.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = { restartGame() },
                            colors = ButtonDefaults.buttonColors(containerColor = PharaohGold)
                        ) {
                            Text(
                                text = if (currentLanguage == AppLanguage.ARABIC) "العب مجدداً ✦" else "Play Again ✦",
                                color = PureBlack,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 3. PYRAMID BUILDER GAME (بناء الهرم الأكبر وتوازن الكتل)
// -------------------------------------------------------------

@Composable
fun PyramidBuilderGame(currentLanguage: AppLanguage) {
    val haptic = LocalHapticFeedback.current

    val totalTiers = 7
    var currentTier by remember { mutableIntStateOf(1) }
    var score by remember { mutableIntStateOf(0) }
    var placedBlocks by remember { mutableStateOf(listOf<Float>()) }
    var isMovingRight by remember { mutableStateOf(true) }
    var blockOffset by remember { mutableStateOf(0f) }
    var isGameOver by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }
    var feedbackMessage by remember {
        mutableStateOf(
            if (currentLanguage == AppLanguage.ARABIC)
                "اضغط 'وضع الكتلة 🧱' عندما تتطابق الكتلة الحجرية مع قمة المصطبة!"
            else
                "Tap 'Place Stone 🧱' when the block is aligned with the lower tier!"
        )
    }

    LaunchedEffect(currentTier, isGameOver) {
        if (!isGameOver) {
            while (true) {
                delay(30)
                val step = 0.05f + (currentTier * 0.015f)
                if (isMovingRight) {
                    blockOffset += step
                    if (blockOffset >= 1.0f) isMovingRight = false
                } else {
                    blockOffset -= step
                    if (blockOffset <= -1.0f) isMovingRight = true
                }
            }
        }
    }

    fun restartGame() {
        currentTier = 1
        score = 0
        placedBlocks = emptyList()
        blockOffset = 0f
        isGameOver = false
        isSuccess = false
        feedbackMessage = if (currentLanguage == AppLanguage.ARABIC)
            "بدأ بناء هرم جديد! اضغط في الوقت المناسب لمحاذاة الكتل الحجرية الضخمة."
        else
            "New pyramid construction started! Time your taps to align stone blocks."
    }

    fun placeBlock() {
        if (isGameOver) return
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)

        val deviation = kotlin.math.abs(blockOffset)
        val accuracy = (1.0f - deviation).coerceIn(0f, 1f)

        if (deviation > 0.65f) {
            isGameOver = true
            isSuccess = false
            feedbackMessage = if (currentLanguage == AppLanguage.ARABIC)
                "انزلقت الكتلة الحجرية وانهار البناء! حاول مرة أخرى بدقة المهندس المعماري إيمحتب."
            else
                "The stone block slipped! Try again with architect Imhotep's precision."
        } else {
            placedBlocks = placedBlocks + blockOffset
            val gainedPoints = (accuracy * 150).toInt()
            score += gainedPoints

            if (currentTier >= totalTiers) {
                isGameOver = true
                isSuccess = true
                feedbackMessage = if (currentLanguage == AppLanguage.ARABIC)
                    "🏆 تم تتويج الهرم بالهريم الذهبي (Benben)! عمل هندسي أسطوري خالد!"
                else
                    "🏆 The pyramid is crowned with the golden Capstone! Monumental achievement!"
            } else {
                currentTier++
                blockOffset = 0f
                feedbackMessage = if (currentLanguage == AppLanguage.ARABIC)
                    "محاذاة ممتازة! (+${gainedPoints} نقطة). ارتفعت المصطبة للمستوى $currentTier / $totalTiers"
                else
                    "Great alignment! (+${gainedPoints} pts). Tier $currentTier / $totalTiers reached."
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
            .testTag("pyramid_builder_game_view"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.2.dp, PharaohGold.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (currentLanguage == AppLanguage.ARABIC) "بناء الهرم وتوازن الكتل" else "Pyramid Architect & Stone Balance",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (currentLanguage == AppLanguage.ARABIC) "المستوى: $currentTier / $totalTiers | النقاط: $score" else "Tier: $currentTier / $totalTiers | Score: $score",
                            style = MaterialTheme.typography.bodySmall,
                            color = PharaohGold
                        )
                    }

                    IconButton(
                        onClick = { restartGame() },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .testTag("pyramid_restart_button")
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Restart", tint = PharaohGold)
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        if (!isGameOver) {
                            val activeWidth = (240 - (currentTier * 26)).dp
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(28.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Surface(
                                    modifier = Modifier
                                        .width(activeWidth)
                                        .height(26.dp)
                                        .graphicsLayer {
                                            translationX = blockOffset * 80.dp.toPx()
                                        },
                                    shape = RoundedCornerShape(4.dp),
                                    color = if (currentTier == totalTiers) PharaohGold else PureWhite,
                                    border = BorderStroke(1.5.dp, PharaohGold)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = if (currentTier == totalTiers) "▲" else "🧱",
                                            fontSize = 12.sp,
                                            color = PureBlack
                                        )
                                    }
                                }
                            }
                        }

                        for (tier in currentTier - 1 downTo 1) {
                            val tierWidth = (240 - (tier * 26)).dp
                            val offsetVal = placedBlocks.getOrNull(tier - 1) ?: 0f

                            Surface(
                                modifier = Modifier
                                    .padding(vertical = 1.dp)
                                    .width(tierWidth)
                                    .height(24.dp)
                                    .graphicsLayer {
                                        translationX = offsetVal * 20.dp.toPx()
                                    },
                                shape = RoundedCornerShape(4.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                border = BorderStroke(1.dp, PharaohGold.copy(alpha = 0.6f))
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = if (tier == totalTiers) "👑 Benben" else "طبقة $tier",
                                        fontSize = 10.sp,
                                        color = PharaohGold,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp),
                            shape = RoundedCornerShape(6.dp),
                            color = PharaohGold.copy(alpha = 0.4f)
                        ) {}
                    }
                }
            }
        }

        item {
            Button(
                onClick = { placeBlock() },
                enabled = !isGameOver,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("pyramid_place_block_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PharaohGold)
            ) {
                Text(
                    text = if (currentLanguage == AppLanguage.ARABIC) "ضع الكتلة الحجرية الآن 🧱" else "Place Stone Block Now 🧱",
                    style = MaterialTheme.typography.titleSmall,
                    color = PureBlack,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSuccess) PharaohGoldContainer else MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, if (isSuccess) PharaohGold else MaterialTheme.colorScheme.outline)
            ) {
                Text(
                    text = feedbackMessage,
                    modifier = Modifier.padding(14.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isSuccess) PharaohGold else MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// -------------------------------------------------------------
// 4. HIEROGLYPH BLITZ GAME (تحدي كاتب البردي السريع)
// -------------------------------------------------------------

data class HieroglyphTriviaQuestion(
    val glyph: String,
    val phonogram: String,
    val meaningAr: String,
    val meaningEn: String,
    val optionsAr: List<String>,
    val optionsEn: List<String>,
    val correctIndex: Int
)

@Composable
fun HieroglyphBlitzGame(currentLanguage: AppLanguage) {
    val haptic = LocalHapticFeedback.current

    val questions = remember {
        listOf(
            HieroglyphTriviaQuestion("𓄿", "Alef (A)", "العقاب المصري (رمز القوة والبداية)", "Egyptian Vulture", listOf("العقاب المصري", "نهر النيل", "المفتاح", "التاج"), listOf("Egyptian Vulture", "River Nile", "Key", "Crown"), 0),
            HieroglyphTriviaQuestion("𓃀", "B", "القدم أو الساق (رمز الحركة والخطوات)", "Foot / Leg", listOf("العين", "القدم أو الساق", "حبة القمح", "القارب"), listOf("Eye", "Foot / Leg", "Grain", "Boat"), 1),
            HieroglyphTriviaQuestion("𓅓", "M", "البومة الحكيمة (رمز الرؤية والحكمة)", "Owl (Wisdom)", listOf("الصقر", "الثعبان", "البومة", "السمكة"), listOf("Falcon", "Serpent", "Owl", "Fish"), 2),
            HieroglyphTriviaQuestion("𓂋", "R", "الفم الإنساني (رمز الكلام والنداء)", "Human Mouth", listOf("الفم", "اليد", "الرأس", "الذراع"), listOf("Mouth", "Hand", "Head", "Arm"), 0),
            HieroglyphTriviaQuestion("𓈖", "N", "تموجات مياه النيل الخالد", "Water Waves", listOf("الرمال", "الشمس", "تموجات مياه النيل", "الأرض"), listOf("Sands", "Sun", "Water Waves", "Earth"), 2),
            HieroglyphTriviaQuestion("𓊪", "P", "قاعدة أو بساط الخوص", "Reed Mat", listOf("الرمح", "بساط الخوص", "الدرع", "الشجرة"), listOf("Spear", "Reed Mat", "Shield", "Tree"), 1),
            HieroglyphTriviaQuestion("𓆓", "Dj", "أفعى الكوبرا الملكية (الصل)", "Cobras / Uraeus", listOf("الكوبرا الملكية", "الجمل", "التمساح", "الكلب"), listOf("Royal Cobra", "Camel", "Crocodile", "Dog"), 0)
        )
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var isAnswered by remember { mutableStateOf(false) }
    var isQuizCompleted by remember { mutableStateOf(false) }

    val currentQ = questions[currentIndex]

    fun restart() {
        currentIndex = 0
        score = 0
        streak = 0
        selectedOptionIndex = null
        isAnswered = false
        isQuizCompleted = false
    }

    fun submitAnswer(idx: Int) {
        if (isAnswered) return
        selectedOptionIndex = idx
        isAnswered = true
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)

        if (idx == currentQ.correctIndex) {
            streak++
            score += 100 + (streak * 20)
        } else {
            streak = 0
        }
    }

    fun nextQuestion() {
        if (currentIndex < questions.size - 1) {
            currentIndex++
            selectedOptionIndex = null
            isAnswered = false
        } else {
            isQuizCompleted = true
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp)
            .testTag("hieroglyph_blitz_game_view"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, PharaohGold.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (currentLanguage == AppLanguage.ARABIC) "لغز الهيروغليفية السريع" else "Hieroglyphs Blitz Duel",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (currentLanguage == AppLanguage.ARABIC) "النقاط: $score | المتتالية: 🔥 $streak" else "Score: $score | Streak: 🔥 $streak",
                            style = MaterialTheme.typography.bodySmall,
                            color = PharaohGold
                        )
                    }

                    IconButton(
                        onClick = { restart() },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .testTag("hieroglyph_restart_button")
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Restart", tint = PharaohGold)
                    }
                }
            }
        }

        if (!isQuizCompleted) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.5.dp, PharaohGold)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (currentLanguage == AppLanguage.ARABIC) "السؤال ${currentIndex + 1} من ${questions.size}" else "Question ${currentIndex + 1} of ${questions.size}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Surface(
                            modifier = Modifier
                                .size(96.dp)
                                .clip(RoundedCornerShape(18.dp)),
                            color = PharaohGoldContainer,
                            border = BorderStroke(1.5.dp, PharaohGold)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = currentQ.glyph,
                                    fontSize = 46.sp,
                                    color = PharaohGold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = if (currentLanguage == AppLanguage.ARABIC) "ما معنى وقيمة هذا الرمز الهيروغليفي؟" else "What does this hieroglyphic symbol represent?",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    val options = if (currentLanguage == AppLanguage.ARABIC) currentQ.optionsAr else currentQ.optionsEn

                    options.forEachIndexed { idx, opt ->
                        val isCorrect = idx == currentQ.correctIndex
                        val isSelected = selectedOptionIndex == idx

                        val btnColor = when {
                            !isAnswered -> MaterialTheme.colorScheme.surface
                            isCorrect -> PharaohGold.copy(alpha = 0.25f)
                            isSelected -> TerracottaRed.copy(alpha = 0.25f)
                            else -> MaterialTheme.colorScheme.surface
                        }

                        val borderColor = when {
                            !isAnswered -> MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                            isCorrect -> PharaohGold
                            isSelected -> TerracottaRed
                            else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        }

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { submitAnswer(idx) }
                                .testTag("blitz_option_$idx"),
                            shape = RoundedCornerShape(12.dp),
                            color = btnColor,
                            border = BorderStroke(if (isSelected || (isAnswered && isCorrect)) 1.5.dp else 1.dp, borderColor)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = opt,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected || (isAnswered && isCorrect)) FontWeight.Bold else FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                if (isAnswered && isCorrect) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = PharaohGold)
                                }
                            }
                        }
                    }
                }
            }

            if (isAnswered) {
                item {
                    Button(
                        onClick = { nextQuestion() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("blitz_next_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PharaohGold)
                    ) {
                        Text(
                            text = if (currentLanguage == AppLanguage.ARABIC) "السؤال التالي ➔" else "Next Question ➔",
                            color = PureBlack,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } else {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = PharaohGoldContainer),
                    border = BorderStroke(1.5.dp, PharaohGold)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("📜", fontSize = 36.sp)
                        Text(
                            text = if (currentLanguage == AppLanguage.ARABIC) "اكتمل تحدي كاتب البردي!" else "Scribe Challenge Completed!",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PharaohGold
                        )
                        Text(
                            text = if (currentLanguage == AppLanguage.ARABIC) "حققت $score نقطة فرعونية ملكية." else "You achieved $score royal points.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = { restart() },
                            colors = ButtonDefaults.buttonColors(containerColor = PharaohGold)
                        ) {
                            Text(
                                text = if (currentLanguage == AppLanguage.ARABIC) "العب جولة جديدة ✦" else "Play Again ✦",
                                color = PureBlack,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// HOW TO PLAY EXPLANATION DIALOG
// -------------------------------------------------------------

@Composable
fun GamesHowToPlayDialog(
    selectedGame: GameCategory,
    currentLanguage: AppLanguage,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.5.dp, PharaohGold)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(selectedGame.iconEmoji, fontSize = 24.sp)
                    Text(
                        text = if (currentLanguage == AppLanguage.ARABIC) "كيف تلعب ${selectedGame.titleAr}؟" else "How to Play ${selectedGame.titleEn}?",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PharaohGold
                    )
                }

                Text(
                    text = when (selectedGame) {
                        GameCategory.SENET -> if (currentLanguage == AppLanguage.ARABIC)
                            "السنت هي أقدم لعبة طاولة معروفة في تاريخ البشرية منذ أكثر من 5000 عام. الهدف هو تحريك قطعك الخمس عبر مسار حلزوني من المربع 1 حتى 30 (بيت الخلود). ألقِ عصي السنت لتحديد عدد الخطوات، واحذر الوقوع في مربع الماء (المربع 27) لأنه يعيدك للوراء!"
                        else
                            "Senet is the oldest known board game in human history, dating back over 5,000 years. Move your 5 pieces through 30 squares into eternity. Cast sticks to move, and avoid the Water trap (Square 27)!"

                        GameCategory.AMULETS_MATCH -> if (currentLanguage == AppLanguage.ARABIC)
                            "اقلب البطاقات وابحث عن التمائم المتطابقة مثل عنخ وعين حورس والجعران وعامود الجد. عند كل تطابق ناجح ستتعرف على سر التميمة ومعناها في معتقدات الفراعنة القدماء."
                        else
                            "Flip cards to match sacred amulets like Ankh, Eye of Horus, and Scarab to uncover ancient mysteries!"

                        GameCategory.PYRAMID_BUILDER -> if (currentLanguage == AppLanguage.ARABIC)
                            "تقمص دور المعماري إيمحتب واضغط في اللحظة المناسبة لتثبيت الكتل الحجرية الضخمة طبقة فوق طبقة وصولاً إلى تتويج الهرم بالهريم الذهبي (Benben stone)."
                        else
                            "Step into the shoes of royal architect Imhotep and time your placement of limestone blocks to crown the pyramid with the golden Benben capstone."

                        GameCategory.HIEROGLYPH_BLITZ -> if (currentLanguage == AppLanguage.ARABIC)
                            "اختبر سرعتك في معرفة الرموز الهيروغليفية وأصواتها ومعانيها الأصلية في سرعة وبديهة لكسب نقاط الكاتب الملكي."
                        else
                            "Test your speed and memory in recognizing sacred hieroglyphic phonograms and ancient symbols to earn royal scribe points."
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PharaohGold)
                ) {
                    Text(
                        text = if (currentLanguage == AppLanguage.ARABIC) "حسناً، فهمت!" else "Got it!",
                        color = PureBlack,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
