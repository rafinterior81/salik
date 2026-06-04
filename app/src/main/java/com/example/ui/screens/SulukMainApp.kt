package com.example.ui.screens

import android.app.Application
import android.content.Context
import android.os.Vibrator
import android.os.VibratorManager
import android.view.HapticFeedbackConstants
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.entity.DailyJournal
import com.example.data.entity.CustomWorshipItem
import com.example.data.entity.KhalwatRetreat
import com.example.data.entity.ZikirCounter
import com.example.data.model.SpiritualData
import com.example.ui.theme.*
import com.example.ui.viewmodel.SulukViewModel
import java.text.SimpleDateFormat
import java.util.*

enum class SulukTab(val label: String, val icon: ImageVector) {
    DASHBOARD("Beranda", Icons.Default.Home),
    IBADAH("Jurnal", Icons.Default.MenuBook),
    WIRID("Tasbih", Icons.Default.Fingerprint),
    RUHANI("Mutiara", Icons.Default.AutoAwesome)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SulukMainApp(viewModel: SulukViewModel) {
    var currentTab by remember { mutableStateOf(SulukTab.DASHBOARD) }
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val dateLabel = viewModel.getFormattedDateLabel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) {
                        // Lincon Ahmed User Avatar (Round, grey with user's profile text)
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "AH",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFEB441) // SoftGold
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(10.dp))
                        
                        Column {
                            Text(
                                text = "Assalamu Alaikum!",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                            Text(
                                text = "Lincon Ahmed",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                },
                actions = {
                    // Date picker toggle
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(end = 6.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White.copy(alpha = 0.12f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        IconButton(
                            onClick = { viewModel.selectPreviousDay() },
                            modifier = Modifier.size(24.dp).testTag("prev_date_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = "Hari Sebelumnya",
                                modifier = Modifier.size(18.dp),
                                tint = Color(0xFFFEB441)
                            )
                        }
                        
                        Text(
                            text = selectedDate.substring(5), // Show MM-DD for brevity
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp),
                            color = Color.White
                        )

                        IconButton(
                            onClick = { viewModel.selectNextDay() },
                            modifier = Modifier.size(24.dp).testTag("next_date_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "Hari Berikutnya",
                                modifier = Modifier.size(18.dp),
                                tint = Color(0xFFFEB441)
                            )
                        }
                    }

                    // Bell Notification Icon
                    IconButton(onClick = { 
                        Toast.makeText(viewModel.getApplication(), "Notifikasi Syu'ur & Amalan aktif!", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifikasi",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(4.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0F3222) // Emerald green to blend beautifully with header background gradient
                )
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(vertical = 8.dp, horizontal = 12.dp)
                    .drawBehind {
                        // Sleek top border divider
                        drawLine(
                            color = Color(0xFFE5ECE8),
                            start = androidx.compose.ui.geometry.Offset(0f, 0f),
                            end = androidx.compose.ui.geometry.Offset(size.width, 0f),
                            strokeWidth = 1.dp.toPx()
                        )
                    },
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SulukTab.values().forEach { tab ->
                    val isSelected = currentTab == tab
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                                indication = null
                            ) { currentTab = tab }
                    ) {
                        if (isSelected) {
                            // Warm gold/mango gradient pill container with white icon
                            Box(
                                modifier = Modifier
                                    .width(64.dp)
                                    .height(34.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(
                                        Brush.horizontalGradient(
                                            colors = listOf(
                                                Color(0xFFFEB441), // left mango yellow-orange
                                                Color(0xFFFFA000)  // right deeper sunset orange
                                            )
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.label,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .width(64.dp)
                                    .height(34.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.label,
                                    tint = Color(0xFF86978C),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = tab.label,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color(0xFFFFA000) else Color(0xFF86978C)
                        )
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = {
                    fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(220))
                },
                label = "TabTransition"
            ) { targetTab ->
                when (targetTab) {
                    SulukTab.DASHBOARD -> DashboardScreen(viewModel = viewModel, onNavigateToTab = { currentTab = it })
                    SulukTab.IBADAH -> JurnalIbadahScreen(viewModel = viewModel, dateLabel = dateLabel)
                    SulukTab.WIRID -> WiridTasbihScreen(viewModel = viewModel)
                    SulukTab.RUHANI -> MutiaraSulukScreen(viewModel = viewModel)
                }
            }
        }
    }
}

// ==========================================
// SCREEN 1: DASHBOARD (BERANDA)
// ==========================================
data class ShalatTimeInfo(
    val name: String,
    val time: String,
    val isDone: Boolean,
    val onToggle: () -> Unit
)

@Composable
fun DashboardScreen(viewModel: SulukViewModel, onNavigateToTab: (SulukTab) -> Unit) {
    val journal by viewModel.dailyJournal.collectAsStateWithLifecycle()
    val zikirList by viewModel.zikirList.collectAsStateWithLifecycle()
    val activeRetreat by viewModel.activeRetreat.collectAsStateWithLifecycle()
    val dateLabel = viewModel.getFormattedDateLabel()
    val context = LocalContext.current

    val azanPrayerName by viewModel.azanPrayerName.collectAsStateWithLifecycle()
    val isAzanPlaying by viewModel.isAzanPlaying.collectAsStateWithLifecycle()
    val azanProgress by viewModel.azanPlaybackProgress.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        while (true) {
            viewModel.checkAzanTrigger()
            kotlinx.coroutines.delay(20000)
        }
    }

    // Quotes cycling by hour of day (for a dynamic feeling)
    val randomQuote = remember(System.currentTimeMillis() / 7200000) {
        SpiritualData.kalamQuotes.random()
    }

    // Dynamic, live computed overall spiritual progress percentage
    val progressPercent = remember(journal, zikirList) {
        var score = 0f
        var totalWeight = 0f

        val j = journal
        if (j != null) {
            var fardhuDone = 0
            if (j.shalatSubuh) fardhuDone++
            if (j.shalatDzuhur) fardhuDone++
            if (j.shalatAshar) fardhuDone++
            if (j.shalatMaghrib) fardhuDone++
            if (j.shalatIsya) fardhuDone++
            score += (fardhuDone / 5f) * 45f
            totalWeight += 45f

            var sunnahDone = 0
            if (j.tahajjud) sunnahDone++
            if (j.dhuha) sunnahDone++
            if (j.rawatib) sunnahDone++
            score += (sunnahDone / 3f) * 20f
            totalWeight += 20f
        } else {
            totalWeight += 65f
        }

        val z = zikirList
        if (z.isNotEmpty()) {
            val completedCount = z.count { it.currentCount >= it.target && it.target > 0 }
            score += (completedCount.toFloat() / z.size) * 35f
            totalWeight += 35f
        } else {
            totalWeight += 35f
        }

        if (totalWeight > 0f) {
            ((score / totalWeight) * 100f).toInt().coerceIn(0, 100)
        } else {
            75 // design default fallback
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F3222), // Deep spiritual emerald green
                        Color(0xFF071911)  // Darker moss obsidian base
                    )
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            contentPadding = PaddingValues(top = 0.dp, bottom = 12.dp)
        ) {
            // === AUTOMATIC AZAN PLAYER ===
            if (azanPrayerName != null) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 4.dp),
                        shape = RoundedCornerShape(24.dp),
                        border = BorderStroke(1.5.dp, Color(0xFFFEB441)),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF134E3A))
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MusicNote,
                                        contentDescription = null,
                                        tint = Color(0xFFFEB441),
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "ADZAN BERKUMANDANG",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFFEB441),
                                            letterSpacing = 1.sp
                                        )
                                        Text(
                                            text = "Waktu Shalat $azanPrayerName",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val infiniteTransition = rememberInfiniteTransition(label = "audio_wave")
                                    val h1 by infiniteTransition.animateFloat(
                                        initialValue = 0.3f, targetValue = 1f,
                                        animationSpec = infiniteRepeatable(animation = tween(400), repeatMode = RepeatMode.Reverse),
                                        label = "w1"
                                    )
                                    val h2 by infiniteTransition.animateFloat(
                                        initialValue = 1f, targetValue = 0.2f,
                                        animationSpec = infiniteRepeatable(animation = tween(550), repeatMode = RepeatMode.Reverse),
                                        label = "w2"
                                    )
                                    val h3 by infiniteTransition.animateFloat(
                                        initialValue = 0.4f, targetValue = 0.9f,
                                        animationSpec = infiniteRepeatable(animation = tween(350), repeatMode = RepeatMode.Reverse),
                                        label = "w3"
                                    )

                                    if (isAzanPlaying) {
                                        Box(Modifier.width(3.dp).height((20 * h1).dp).background(Color(0xFFFEB441), RoundedCornerShape(2.dp)))
                                        Box(Modifier.width(3.dp).height((20 * h2).dp).background(Color(0xFFFEB441), RoundedCornerShape(2.dp)))
                                        Box(Modifier.width(3.dp).height((20 * h3).dp).background(Color(0xFFFEB441), RoundedCornerShape(2.dp)))
                                    } else {
                                        Box(Modifier.width(3.dp).height(8.dp).background(Color(0xFFFEB441).copy(alpha = 0.6f), RoundedCornerShape(2.dp)))
                                        Box(Modifier.width(3.dp).height(12.dp).background(Color(0xFFFEB441).copy(alpha = 0.6f), RoundedCornerShape(2.dp)))
                                        Box(Modifier.width(3.dp).height(6.dp).background(Color(0xFFFEB441).copy(alpha = 0.6f), RoundedCornerShape(2.dp)))
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            LinearProgressIndicator(
                                progress = { azanProgress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(CircleShape),
                                color = Color(0xFFFEB441),
                                trackColor = Color.White.copy(alpha = 0.15f)
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedButton(
                                    onClick = { viewModel.stopAzan() },
                                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.4f)),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.height(38.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VolumeMute,
                                        contentDescription = "Mute",
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Matikan", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = {
                                        if (isAzanPlaying) {
                                            viewModel.pauseAzan()
                                        } else {
                                            viewModel.resumeAzan()
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFFEB441),
                                        contentColor = Color(0xFF134E3A)
                                    ),
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.height(38.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isAzanPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                                        contentDescription = if (isAzanPlaying) "Pause" else "Play",
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isAzanPlaying) "Jeda" else "Putar",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ==========================================
            // UPPER DARK GREEN SECTION (Banner & Dots)
            // ==========================================
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // 1. Target Progress Card Styled as the orange yellow "Remember Allah / Start Tasbih" banner
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        shape = RoundedCornerShape(24.dp),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFFFEB441), // Mango-Gold radiant warm start
                                            Color(0xFFFFA000)  // Deep sunset orange end
                                        )
                                    )
                                )
                                .padding(20.dp)
                        ) {
                            // Canvas Drawing for beautiful, artistic background prayer beads on the right side
                            Canvas(modifier = Modifier.matchParentSize()) {
                                val canvasWidth = size.width
                                val canvasHeight = size.height
                                
                                // Draw a series of connected decorative prayer bead circles
                                val centerPointX = canvasWidth * 0.85f
                                val centerPointY = canvasHeight * 0.45f
                                val beadRadius = 14f
                                val loopRadius = 60f
                                
                                // Draw dangling beads representation in soft warm brown outline
                                val strokeColor = Color(0xFFDD6B20).copy(alpha = 0.4f)
                                for (i in 0 until 12) {
                                    val angle = (i * (360f / 12)) * (Math.PI / 180f)
                                    val beadX = centerPointX + (loopRadius * Math.cos(angle)).toFloat()
                                    val beadY = centerPointY + (loopRadius * Math.sin(angle)).toFloat()
                                    drawCircle(
                                        color = strokeColor,
                                        radius = beadRadius,
                                        center = androidx.compose.ui.geometry.Offset(beadX, beadY),
                                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3f)
                                    )
                                }
                                // Draw tassel / string line dangling down
                                drawLine(
                                    color = strokeColor,
                                    start = androidx.compose.ui.geometry.Offset(centerPointX, centerPointY + loopRadius),
                                    end = androidx.compose.ui.geometry.Offset(centerPointX, centerPointY + loopRadius + 40f),
                                    strokeWidth = 4f
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.weight(1.2f),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "Remember Allah",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.8.sp,
                                        color = Color(0xFF6B4610) // Elegant dark brown
                                    )
                                    Text(
                                        text = "Start Tasbih\nCounter",
                                        fontSize = 24.sp,
                                        lineHeight = 28.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFF1E1404) // Deep high-contrast brown charcoal
                                    )
                                    
                                    Spacer(modifier = Modifier.height(10.dp))
                                    
                                    // Black "Get Start Now" button that routes path to tasbih
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(Color(0xFF111C15)) // dark forest/black
                                            .clickable { onNavigateToTab(SulukTab.WIRID) }
                                            .padding(horizontal = 16.dp, vertical = 8.dp)
                                    ) {
                                        Text(
                                            text = "Get Start Now",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }

                                // Interactive Target Wirid circular dial integrated with the exact styling 
                                Column(
                                    modifier = Modifier.weight(0.8f),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Box(
                                        modifier = Modifier.size(82.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        // Thick deep emerald outline background ring
                                        CircularProgressIndicator(
                                            progress = { progressPercent / 100f },
                                            modifier = Modifier.fillMaxSize(),
                                            color = Color(0xFF0F3222), // Deep emerald
                                            strokeWidth = 6.dp,
                                            trackColor = Color.White.copy(alpha = 0.35f)
                                        )
                                        // Highlight indicator center dot
                                        Box(
                                            modifier = Modifier
                                                .size(10.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFF0F3222))
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "$progressPercent% Selesai",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFF1E1404)
                                    )
                                }
                            }
                        }
                    }

                    // 4 Indicator dots centered. First dot represents active (soft mint pill), rest are circles
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 2.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = 16.dp, height = 5.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF52C38F)) // Clean bright mint/green
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.5f))
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.5f))
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.5f))
                        )
                    }
                }
            }

            // ==========================================
            // MAIN SHEET PORTION (Soft elegant white rounded container)
            // ==========================================
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    
                    // A. Quick Actions Grid (4 customized cards, matching the button structure of the reference)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val actions = listOf(
                            Triple("📖", "Quran", SulukTab.IBADAH),
                            Triple("📿", "Wirid", SulukTab.WIRID),
                            Triple("📔", "Journal", SulukTab.IBADAH),
                            Triple("🏠", "Khalwat", SulukTab.DASHBOARD)
                        )
                        
                        // Theme colors for soft custom glow/outline per grid action card
                        val themeAccents = listOf(
                            Color(0xFF52C38F), // Quran -> Emerald
                            Color(0xFFFFA000), // Wirid -> Amber Gold
                            Color(0xFF9C27B0), // Journal -> Purple Lavender
                            Color(0xFFFF5722)  // Khalwat -> Coral
                        )

                        actions.forEachIndexed { i, (emoji, name, tab) ->
                            val accentColor = themeAccents[i]
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        if (name == "Khalwat") {
                                            Toast.makeText(context, "Suluk Khalwat: Atur target riyadhoh Anda!", Toast.LENGTH_SHORT).show()
                                        } else {
                                            onNavigateToTab(tab)
                                        }
                                    }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(Color.White)
                                        .border(1.dp, accentColor.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                                        .shadow(
                                            elevation = 3.dp,
                                            shape = RoundedCornerShape(20.dp),
                                            clip = false,
                                            ambientColor = accentColor.copy(alpha = 0.2f),
                                            spotColor = accentColor.copy(alpha = 0.3f)
                                        )
                                        .padding(12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = emoji, fontSize = 24.sp)
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = name,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF233028),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    // B. Namaz Timings Card (Elegant deep emerald/pine card mimicking the timing block of the reference)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0xFF134E3A), // Deep forest pine
                                            Color(0xFF0C241B)  // Dark spruce green
                                        )
                                    )
                                )
                                .padding(18.dp)
                        ) {
                            // Artistic hanging lamps/lanterns on left/right using custom canvas drawing
                            Canvas(modifier = Modifier.matchParentSize()) {
                                val orangeGlow = Color(0xFFFFD54F)
                                val lineTint = Color.White.copy(alpha = 0.2f)
                                
                                // Left Lamp
                                drawLine(
                                    color = lineTint,
                                    start = androidx.compose.ui.geometry.Offset(40f, 0f),
                                    end = androidx.compose.ui.geometry.Offset(40f, 60f),
                                    strokeWidth = 2f
                                )
                                drawCircle(
                                    color = orangeGlow,
                                    radius = 12f,
                                    center = androidx.compose.ui.geometry.Offset(40f, 60f)
                                )
                                drawCircle(
                                    color = orangeGlow.copy(alpha = 0.25f),
                                    radius = 24f,
                                    center = androidx.compose.ui.geometry.Offset(40f, 60f)
                                )
                                
                                // Right Lamp
                                val rOffset = size.width - 40f
                                drawLine(
                                    color = lineTint,
                                    start = androidx.compose.ui.geometry.Offset(rOffset, 0f),
                                    end = androidx.compose.ui.geometry.Offset(rOffset, 60f),
                                    strokeWidth = 2f
                                )
                                drawCircle(
                                    color = orangeGlow,
                                    radius = 12f,
                                    center = androidx.compose.ui.geometry.Offset(rOffset, 60f)
                                )
                                drawCircle(
                                    color = orangeGlow.copy(alpha = 0.25f),
                                    radius = 24f,
                                    center = androidx.compose.ui.geometry.Offset(rOffset, 60f)
                                )
                            }

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // 1. Location badge capsule
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFF0C2C20))
                                        .padding(horizontal = 14.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Place,
                                        contentDescription = "Lokasi",
                                        tint = Color(0xFFFEB441), // Gold accent
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Jakarta, Indonesia",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = null,
                                        tint = Color.White.copy(alpha = 0.7f),
                                        modifier = Modifier.size(14.dp)
                                    )
                                }

                                // 2. Prayer Header Titles
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Namaz Timings",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    
                                    // Compact Simulation Controls
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Simulasi Adzan:",
                                            fontSize = 9.sp,
                                            color = Color.White.copy(alpha = 0.6f),
                                            fontWeight = FontWeight.Bold
                                        )
                                        listOf("Fajr", "Duh'r", "Asr", "Magrib", "Isha'a").forEach { pr ->
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(
                                                        if (azanPrayerName == pr) Color(0xFFFEB441)
                                                        else Color.White.copy(alpha = 0.12f)
                                                    )
                                                    .clickable { viewModel.playAzan(pr) }
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = pr,
                                                    fontSize = 8.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (azanPrayerName == pr) Color(0xFF134E3A) else Color.White
                                                )
                                            }
                                        }
                                    }
                                }
                                Text(
                                    text = "19 Ramadan 1447 Hijri • $dateLabel",
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.7f),
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                // 3. Horizontal Row of 5 timing cards. 
                                // Clicking toggles the respective shalat completion.
                                // If completed, renders in gorgeous warm glowing orange/gold gradient (like active Asr in reference).
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    val prayersInfo = listOf(
                                        ShalatTimeInfo("Fajr", "04:35", journal?.shalatSubuh == true, { viewModel.toggleShalatSubuh() }),
                                        ShalatTimeInfo("Duh'r", "12:05", journal?.shalatDzuhur == true, { viewModel.toggleShalatDzuhur() }),
                                        ShalatTimeInfo("Asr", "15:20", journal?.shalatAshar == true, { viewModel.toggleShalatAshar() }),
                                        ShalatTimeInfo("Magrib", "18:05", journal?.shalatMaghrib == true, { viewModel.toggleShalatMaghrib() }),
                                        ShalatTimeInfo("Isha'a", "19:15", journal?.shalatIsya == true, { viewModel.toggleShalatIsya() })
                                    )

                                    prayersInfo.forEach { prayer ->
                                        val name = prayer.name
                                        val time = prayer.time
                                        val isDone = prayer.isDone
                                        val onToggle = prayer.onToggle
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(
                                                    if (isDone) {
                                                        Brush.verticalGradient(
                                                            colors = listOf(
                                                                Color(0xFFFEB441), // highlighted gold amber glow
                                                                Color(0xFFFFA000)
                                                            )
                                                        )
                                                    } else {
                                                        Brush.verticalGradient(
                                                            colors = listOf(
                                                                Color(0xFF133F30).copy(alpha = 0.8f),
                                                                Color(0xFF0A2A20).copy(alpha = 0.8f)
                                                            )
                                                        )
                                                    }
                                                )
                                                .border(
                                                    width = 1.3.dp,
                                                    color = if (isDone) Color.White else Color.White.copy(alpha = 0.15f),
                                                    shape = RoundedCornerShape(12.dp)
                                                )
                                                .clickable { onToggle() }
                                                .padding(vertical = 10.dp, horizontal = 2.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Text(
                                                    text = time,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isDone) Color(0xFF1E1404) else Color.White,
                                                    textAlign = TextAlign.Center
                                                )
                                                
                                                // Icon representing sun state/moon state
                                                Icon(
                                                    imageVector = when (name) {
                                                        "Fajr" -> Icons.Default.WbTwilight
                                                        "Duh'r" -> Icons.Default.WbSunny
                                                        "Asr" -> Icons.Default.LightMode
                                                        "Magrib" -> Icons.Default.WbCloudy
                                                        else -> Icons.Default.NightsStay
                                                    },
                                                    contentDescription = null,
                                                    tint = if (isDone) Color(0xFF1E1404) else Color(0xFFFEB441),
                                                    modifier = Modifier.size(14.dp)
                                                )

                                                Text(
                                                    text = name,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isDone) Color(0xFF1E1404) else Color.White.copy(alpha = 0.8f)
                                                )
                                            }
                                        }
                                    }
                                }
                                
                                Text(
                                    text = "*Ketuk waktu prayers untuk mencatat kehadiran shalat Anda!",
                                    fontSize = 10.sp,
                                    fontStyle = FontStyle.Italic,
                                    color = Color.White.copy(alpha = 0.5f),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    // C. Kalam Mursyid Card
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Kalam Mursyid",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF134E3A),
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "Lihat Semua",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFA000),
                                modifier = Modifier.clickable { onNavigateToTab(SulukTab.RUHANI) }
                            )
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FBF9)),
                            border = BorderStroke(1.dp, Color(0xFFE5ECE8))
                        ) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "“",
                                    fontSize = 64.sp,
                                    fontFamily = FontFamily.Serif,
                                    color = Color(0xFF134E3A).copy(alpha = 0.08f),
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(end = 16.dp, top = 2.dp)
                                )

                                Column(modifier = Modifier.padding(18.dp)) {
                                    Text(
                                        text = "\"Tazkiyatun Nafs bukanlah tentang menjadi sempurna, melainkan tentang senantiasa merasa butuh kepada Allah di setiap detak jantung.\"",
                                        fontSize = 14.sp,
                                        fontStyle = FontStyle.Italic,
                                        fontFamily = FontFamily.Serif,
                                        lineHeight = 22.sp,
                                        color = Color(0xFF233028),
                                        modifier = Modifier.padding(bottom = 12.dp)
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        HorizontalDivider(
                                            modifier = Modifier.weight(1f),
                                            thickness = 1.dp,
                                            color = Color(0xFFE5ECE8)
                                        )
                                        Text(
                                            text = "Syekh Al-Arif Billah",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF86978C)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // D. Next Reminder Activity (Beautiful timing container banner)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToTab(SulukTab.WIRID) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F2)),
                        border = BorderStroke(1.dp, Color(0xFFE5ECE8))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(width = 54.dp, height = 48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White)
                                    .border(1.dp, Color(0xFFE5ECE8), RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "MAGHRIB",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF134E3A),
                                        letterSpacing = 0.5.sp
                                    )
                                    Text(
                                        text = "18:05",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF233028)
                                    )
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Amalan Riyadhoh Terdekat",
                                    fontSize = 11.sp,
                                    color = Color(0xFF86978C),
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "Dzikir Petang & Ratib Al-Haddad",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF233028)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFFA000)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.NavigateNext,
                                    contentDescription = "Selanjutnya",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    // E. Active Khalwat Banner 
                    activeRetreat?.let { retreat ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7E6)),
                            border = BorderStroke(1.dp, Color(0xFFFEB441).copy(alpha = 0.4f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFFEB441).copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FilterDrama,
                                        contentDescription = "Khalwat",
                                        tint = Color(0xFFFFA000),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1.0f)) {
                                    Text(
                                        text = "KHALWAT SEDANG AKTIF",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFFFA000),
                                        letterSpacing = 1.sp
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Hari Ke-${retreat.currentDayOfRetreat} dari ${retreat.targetDays} Hari",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF233028)
                                    )
                                    if (retreat.note.isNotBlank()) {
                                        Text(
                                            text = "Niat: \"${retreat.note}\"",
                                            fontSize = 11.sp,
                                            fontStyle = FontStyle.Italic,
                                            color = Color(0xFF86978C),
                                            maxLines = 1
                                        )
                                    }
                                }
                                IconButton(
                                    onClick = { viewModel.advanceKhalwatDay() },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(Color(0xFFFFA000), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Lanjut Hari",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }

                    // F. Metrics Header
                    Text(
                        text = "Ringkasan Mujahadah Harian",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF134E3A),
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(start = 4.dp, end = 4.dp, top = 4.dp)
                    )

                    // Metrics Grid (Fardhu & Sunnah & Taharah & Quran)
                    journal?.let { j ->
                        var shalatDone = 0
                        if (j.shalatSubuh) shalatDone++
                        if (j.shalatDzuhur) shalatDone++
                        if (j.shalatAshar) shalatDone++
                        if (j.shalatMaghrib) shalatDone++
                        if (j.shalatIsya) shalatDone++

                        var sunnahCount = 0
                        if (j.tahajjud) sunnahCount++
                        if (j.dhuha) sunnahCount++
                        if (j.rawatib) sunnahCount++

                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                DashboardMetricCard(
                                    title = "Fardhu",
                                    value = "$shalatDone/5 Waktu",
                                    progress = shalatDone / 5.0f,
                                    color = Color(0xFF134E3A),
                                    icon = Icons.Default.CheckCircle,
                                    modifier = Modifier.weight(1f)
                                )
                                DashboardMetricCard(
                                    title = "Sunnah",
                                    value = "$sunnahCount/3 Amalan",
                                    progress = sunnahCount / 3.0f,
                                    color = Color(0xFFFFA000),
                                    icon = Icons.Default.Brightness5,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                DashboardMetricCard(
                                    title = "Taharah Wudhu",
                                    value = if (j.wudhu) "Menjaga Wudhu" else "Belum Centang",
                                    progress = if (j.wudhu) 1f else 0f,
                                    color = Color(0xFF52C38F),
                                    icon = Icons.Default.WaterDrop,
                                    modifier = Modifier.weight(1f)
                                )
                                DashboardMetricCard(
                                    title = "Tilawah Quran",
                                    value = "${j.tilawahPage} Halaman",
                                    progress = (j.tilawahPage / 10.0f).coerceAtMost(1.0f),
                                    color = Color(0xFF134E3A),
                                    icon = Icons.Default.MenuBook,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    // G. Zikir Progress Summary Card (progres-progres)
                    if (zikirList.isNotEmpty()) {
                        val completedCount = zikirList.count { it.currentCount >= it.target && it.target > 0 }
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, Color(0xFFE5ECE8))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Fingerprint,
                                        contentDescription = "Zikir",
                                        tint = Color(0xFF134E3A),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Progres Wirid & Dzikir",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF233028)
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Text(
                                        text = "$completedCount / ${zikirList.size} Selesai",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF134E3A)
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                
                                LinearProgressIndicator(
                                    progress = { if (zikirList.isNotEmpty()) completedCount.toFloat() / zikirList.size else 0f },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = Color(0xFF134E3A),
                                    trackColor = Color(0xFFE5ECE8)
                                )
                                
                                Spacer(modifier = Modifier.height(10.dp))
                                
                                // Peek first two items beautifully
                                zikirList.take(2).forEach { zikir ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 3.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = zikir.name,
                                            fontSize = 12.sp,
                                            color = Color(0xFF233028)
                                        )
                                        Text(
                                            text = "${zikir.currentCount} / ${zikir.target}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (zikir.currentCount >= zikir.target) Color(0xFF52C38F) else Color(0xFF86978C)
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

@Composable
fun DashboardMetricCard(
    title: String,
    value: String,
    progress: Float,
    color: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            // Minimal progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progress.coerceIn(0f, 1f))
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }
    }
}

// ==========================================
// SCREEN 2: DAILY WORSHIP JOURNAL (JURNAL)
// ==========================================
@Composable
fun JurnalIbadahScreen(viewModel: SulukViewModel, dateLabel: String) {
    val journal by viewModel.dailyJournal.collectAsStateWithLifecycle()
    val customWorshipItems by viewModel.customWorshipItems.collectAsStateWithLifecycle()
    
    var selectedSubTab by remember { mutableStateOf(0) } // 0 = Shalat & Tilawah, 1 = Jurnal Puasa
    var showAddDialog by remember { mutableStateOf(false) }
    var dialogCategory by remember { mutableStateOf("") }
    var dialogCategoryLabel by remember { mutableStateOf("") }
    var newItemLabel by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Text(
                    text = "Jurnal Mujahadah Harian",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Catat amalan lahiriah dan batiniah Anda hari ini.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Segmented Control Pill Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf(
                    "📿 Shalat & Tilawah" to 0,
                    "🌾 Jurnal Puasa" to 1
                ).forEach { (label, index) ->
                    val isSelected = selectedSubTab == index
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primary
                                else Color.Transparent
                            )
                            .clickable { selectedSubTab = index }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Day selection info
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Event,
                        contentDescription = "Tanggal",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Tanggal Jurnal: $dateLabel",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

        journal?.let { j ->
            if (selectedSubTab == 0) {
                // Card 1: Shalat Fardhu
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = ForestEmerald)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Salat Fardhu (Berjamaah)",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))

                            CheckableWorshipRow(
                                label = "Subuh",
                                isChecked = j.shalatSubuh,
                                onCheckedChange = { viewModel.toggleShalatSubuh() },
                                tag = "subuh_check"
                            )
                            CheckableWorshipRow(
                                label = "Dzuhur",
                                isChecked = j.shalatDzuhur,
                                onCheckedChange = { viewModel.toggleShalatDzuhur() },
                                tag = "dzuhur_check"
                            )
                            CheckableWorshipRow(
                                label = "Ashar",
                                isChecked = j.shalatAshar,
                                onCheckedChange = { viewModel.toggleShalatAshar() },
                                tag = "ashar_check"
                            )
                            CheckableWorshipRow(
                                label = "Maghrib",
                                isChecked = j.shalatMaghrib,
                                onCheckedChange = { viewModel.toggleShalatMaghrib() },
                                tag = "maghrib_check"
                            )
                            CheckableWorshipRow(
                                label = "Isya",
                                isChecked = j.shalatIsya,
                                onCheckedChange = { viewModel.toggleShalatIsya() },
                                tag = "isya_check"
                            )
                        }
                    }
                }

                // Card 2: Shalat Sunnah
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.NightsStay, contentDescription = null, tint = SoftGold)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Salat Sunnah & Qiyam",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        dialogCategory = "sunnah_qiyam"
                                        dialogCategoryLabel = "Salat Sunnah & Qiyam"
                                        newItemLabel = ""
                                        showAddDialog = true
                                    },
                                    modifier = Modifier.size(28.dp).testTag("add_sunnah_item_btn")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Tambah Sunnah",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))

                            CheckableWorshipRow(
                                label = "Qiyamul Lail (Tahajjud & Witir)",
                                isChecked = j.tahajjud,
                                onCheckedChange = { viewModel.toggleTahajjud() },
                                tag = "tahajjud_check"
                            )
                            CheckableWorshipRow(
                                label = "Salat Duha",
                                isChecked = j.dhuha,
                                onCheckedChange = { viewModel.toggleDhuha() },
                                tag = "dhuha_check"
                            )
                            CheckableWorshipRow(
                                label = "Salat Isyraq",
                                isChecked = j.shalatIsyraq,
                                onCheckedChange = { viewModel.toggleShalatIsyraq() },
                                tag = "isyraq_check"
                            )
                            CheckableWorshipRow(
                                label = "Rawatib (Sunnah Qobliyyah & Ba'diyyah)",
                                isChecked = j.rawatib,
                                onCheckedChange = { viewModel.toggleRawatib() },
                                tag = "rawatib_check"
                            )

                            // Custom entries
                            val sunnahCustom = customWorshipItems.filter { it.category == "sunnah_qiyam" }
                            if (sunnahCustom.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                sunnahCustom.forEach { item ->
                                    CustomCheckableRow(
                                        item = item,
                                        onCheckedChange = { viewModel.toggleCustomWorshipItem(item) },
                                        onDelete = { viewModel.deleteCustomWorshipItem(item.id) }
                                    )
                                }
                            }
                        }
                    }
                }

                // Card 3: Spiritual Reading & Kesucian (Quran, Wudhu)
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.MenuBook, contentDescription = null, tint = SoftJade)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Tazkiyah & Tilawah",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        dialogCategory = "tazkiyah_tilawah"
                                        dialogCategoryLabel = "Tazkiyah & Tilawah"
                                        newItemLabel = ""
                                        showAddDialog = true
                                    },
                                    modifier = Modifier.size(28.dp).testTag("add_tazkiyah_item_btn")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Tambah Tazkiyah",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))

                            CheckableWorshipRow(
                                label = "Daimul/Mudawamul Wudhu (Menjaga kesucian)",
                                isChecked = j.wudhu,
                                onCheckedChange = { viewModel.toggleWudhu() },
                                tag = "wudhu_check"
                            )
                            CheckableWorshipRow(
                                label = "Membaca Wirid Pagi (As-Shabah)",
                                isChecked = j.zikirPagi,
                                onCheckedChange = { viewModel.toggleZikirPagi() },
                                tag = "pagi_check"
                            )
                            CheckableWorshipRow(
                                label = "Membaca Wirid Sore (Al-Masa')",
                                isChecked = j.zikirSore,
                                onCheckedChange = { viewModel.toggleZikirSore() },
                                tag = "sore_check"
                            )

                            // Custom entries
                            val tazkiyahCustom = customWorshipItems.filter { it.category == "tazkiyah_tilawah" }
                            if (tazkiyahCustom.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                tazkiyahCustom.forEach { item ->
                                    CustomCheckableRow(
                                        item = item,
                                        onCheckedChange = { viewModel.toggleCustomWorshipItem(item) },
                                        onDelete = { viewModel.deleteCustomWorshipItem(item.id) }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            HorizontalDivider()
                            Spacer(modifier = Modifier.height(14.dp))

                            // Tilawah pages incremental input
                            Text(
                                text = "Tilawah Qur'an Hari Ini",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Halaman terbaca:",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Button(
                                        onClick = { if (j.tilawahPage > 0) viewModel.updateTilawahPages(j.tilawahPage - 1) },
                                        shape = CircleShape,
                                        contentPadding = PaddingValues(0.dp),
                                        modifier = Modifier.size(36.dp).testTag("quran_dec_btn"),
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f), contentColor = MaterialTheme.colorScheme.onSurface)
                                    ) {
                                        Icon(Icons.Default.Remove, contentDescription = "Kurang", modifier = Modifier.size(16.dp))
                                    }
                                    
                                    Text(
                                        text = "${j.tilawahPage}",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                        color = MaterialTheme.colorScheme.primary
                                    )

                                    Button(
                                        onClick = { viewModel.updateTilawahPages(j.tilawahPage + 1) },
                                        shape = CircleShape,
                                        contentPadding = PaddingValues(0.dp),
                                        modifier = Modifier.size(36.dp).testTag("quran_inc_btn"),
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f), contentColor = MaterialTheme.colorScheme.onSurface)
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = "Tambah", modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }

                // Card 4: Riyadhoh & Nafs Control
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Favorite, contentDescription = null, tint = SpiritualGold)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Riyadhoh Nafs (Latihan Jiwa)",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        dialogCategory = "riyadhoh_nafs"
                                        dialogCategoryLabel = "Riyadhoh Nafs"
                                        newItemLabel = ""
                                        showAddDialog = true
                                    },
                                    modifier = Modifier.size(28.dp).testTag("add_riyadhoh_item_btn")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Tambah Latihan Jiwa",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))

                            CheckableWorshipRow(
                                label = "Riyadhoh Sabar & Hening (Menjaga lisan/amarah)",
                                isChecked = j.riyadhohSabar,
                                onCheckedChange = { viewModel.toggleRiyadhohSabar() },
                                tag = "sabar_check"
                            )

                            // Custom entries
                            val riyadhohCustom = customWorshipItems.filter { it.category == "riyadhoh_nafs" }
                            if (riyadhohCustom.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                riyadhohCustom.forEach { item ->
                                    CustomCheckableRow(
                                        item = item,
                                        onCheckedChange = { viewModel.toggleCustomWorshipItem(item) },
                                        onDelete = { viewModel.deleteCustomWorshipItem(item.id) }
                                    )
                                }
                            }
                        }
                    }
                }

                // Card 5: Spiritual Notes & Reflection Card (Evaluating spiritual state)
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.RateReview,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Refleksi Diri & Catatan Spiritual",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Tuliskan muhasabah diri, syukur, evaluasi amalan, atau komitmen perbaikan spiritual harian Anda.",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(bottom = 10.dp)
                            )
                            OutlinedTextField(
                                value = j.notes,
                                onValueChange = { viewModel.updateJournal(j.copy(notes = it)) },
                                modifier = Modifier.fillMaxWidth().height(115.dp).testTag("spiritual_notes_input"),
                                shape = RoundedCornerShape(12.dp),
                                placeholder = {
                                    Text(
                                        text = "Tulis catatan refleksi spiritual Anda di sini...",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                    )
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                                ),
                                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp)
                            )
                        }
                    }
                }
            } else {
                // ==========================================
                // FASTING JOURNAL TAB (Halaman Jurnal Puasa)
                // ==========================================
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "🌾",
                                    fontSize = 20.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Program Jurnal Puasa & Riyadhoh",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Pilih jenis puasa yang Anda laksanakan hari ini untuk melatih disiplin spiritual (mujahadah).",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Overall Fasting Checkbox Toggle
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f))
                                    .clickable { viewModel.togglePuasa() }
                                    .padding(horizontal = 12.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = j.riyadhohPuasa,
                                    onCheckedChange = { viewModel.togglePuasa() },
                                    colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Sedang Melaksanakan Puasa Hari Ini",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                    Text(
                                        text = "Centang jika Anda menjalankan ibadah puasa",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                            Spacer(modifier = Modifier.height(12.dp))

                            // Fasting Option Items
                            Text(
                                text = "JENIS PUASA YANG DIJALANKAN",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            val fastingOptions = listOf(
                                Triple("senin_kamis", "Puasa Senin & Kamis", "Puasa sunnah mingguan di hari Senin & Kamis sebagai teladan Rasulullah SAW."),
                                Triple("daud", "Puasa Daud", "Puasa sunnah tingkat tinggi berulang selang-seling (sehari puasa, sehari berbuka)."),
                                Triple("diam", "Puasa Diam (Shoum Al-Shamt)", "Tidak berbicara kecuali dzikir atau hal mendesak untuk menenangkan riak nafsu lisan."),
                                Triple("tidak_bernyawa", "Puasa Tidak Makan yang Bernyawa", "Riyadhoh/mutih menghindari unsur hewani untuk menundukkan hawa nafsu basyariah."),
                                Triple("lainnya", "Puasa Sunnah Lainnya", "Fasting Ayyamul Bidh, nazar, qodho, puasa sunnah mutlak, atau riyadhoh khusus lainnya.")
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                fastingOptions.forEach { (typeId, title, desc) ->
                                    val isSelected = j.fastingType == typeId
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(
                                                if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                                                else Color.Transparent
                                            )
                                            .border(
                                                1.dp,
                                                if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                                                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                                RoundedCornerShape(12.dp)
                                            )
                                            .clickable {
                                                val newType = if (isSelected) "" else typeId
                                                viewModel.updateFastingType(newType)
                                                if (newType.isNotEmpty() && !j.riyadhohPuasa) {
                                                    viewModel.togglePuasa()
                                                }
                                            }
                                            .padding(horizontal = 12.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = isSelected,
                                            onClick = {
                                                val newType = if (isSelected) "" else typeId
                                                viewModel.updateFastingType(newType)
                                                if (newType.isNotEmpty() && !j.riyadhohPuasa) {
                                                    viewModel.togglePuasa()
                                                }
                                            },
                                            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = title,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = desc,
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                lineHeight = 15.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Card 6: Fasting Reflection
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Edit, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Refleksi & Catatan Riyadhoh",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Tuliskan pengalaman rohani, tantangan nafsu, atau kedalaman rasa hening yang Anda rasakan selama riyadhoh puasa hari ini.",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            OutlinedTextField(
                                value = j.fastingNotes,
                                onValueChange = { viewModel.updateFastingNotes(it) },
                                modifier = Modifier.fillMaxWidth().height(150.dp).testTag("fasting_notes_input"),
                                shape = RoundedCornerShape(16.dp),
                                placeholder = {
                                    Text(
                                        text = "Contoh: Di pertengahan hari nafsu amarah membuncah, namun diredam dengan shoumal-shamt (puasa diam) dan menarik zikir nafas...",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                    )
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                                ),
                                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp)
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = {
                Text(
                    text = "Tambah Item Mandiri",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Kategori: $dialogCategoryLabel",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    OutlinedTextField(
                        value = newItemLabel,
                        onValueChange = { newItemLabel = it },
                        placeholder = { Text("Nama amalan baru / sunnah...", fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth().testTag("custom_item_name_input"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newItemLabel.isNotBlank()) {
                            viewModel.addCustomWorshipItem(newItemLabel.trim(), dialogCategory)
                            showAddDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Tambah", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Batal", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        )
    }
}

@Composable
fun CustomCheckableRow(
    item: CustomWorshipItem,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Checkbox(
                checked = item.isChecked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = item.label,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        IconButton(
            onClick = onDelete,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Hapus",
                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.6f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun CheckableWorshipRow(
    label: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    tag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!isChecked) }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = if (isChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            fontWeight = if (isChecked) FontWeight.SemiBold else FontWeight.Normal,
            modifier = Modifier.weight(1f)
        )
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.testTag(tag),
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
                uncheckedColor = MaterialTheme.colorScheme.outline
            )
        )
    }
}

// ==========================================
// SCREEN 3: WIRID & TASBIH CLICKER
// ==========================================
@Composable
fun WiridTasbihScreen(viewModel: SulukViewModel) {
    val zikirs by viewModel.zikirList.collectAsStateWithLifecycle()
    var selectedZikirForClicker by remember { mutableStateOf<ZikirCounter?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }

    // If an item is currently selected, display the full majestic Digital clicker board.
    // Otherwise show the list of tasks.
    if (selectedZikirForClicker != null) {
        // Find latest updated values of this zikir in the list
        val currentActiveZikir = zikirs.find { it.id == selectedZikirForClicker?.id }
        if (currentActiveZikir != null) {
            TasbihClickerBoard(
                zikir = currentActiveZikir,
                onDismiss = { selectedZikirForClicker = null },
                onIncrement = { viewModel.incrementZikirCount(currentActiveZikir.id, currentActiveZikir.currentCount) },
                onDecrement = { viewModel.decrementZikirCount(currentActiveZikir.id, currentActiveZikir.currentCount) },
                onReset = { viewModel.resetZikirCount(currentActiveZikir.id) }
            )
        } else {
            selectedZikirForClicker = null
        }
    } else {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showAddDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.testTag("add_custom_zikir_fab")
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Tambah Zikir Kustom")
                }
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                item {
                    Column(modifier = Modifier.padding(top = 8.dp)) {
                        Text(
                            text = "Wirid & Zikir Harian",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Ketuk salah satu wirid untuk membuka Tasbih Digital penyucian jiwa.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (zikirs.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Text(
                                "Mempersiapkan wirid harian Anda...",
                                fontSize = 14.sp,
                                modifier = Modifier.padding(24.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    items(zikirs, key = { it.id }) { zikir ->
                        ZikirItemCard(
                            zikir = zikir,
                            onClick = { selectedZikirForClicker = zikir },
                            onDelete = {
                                viewModel.deleteZikirCounter(zikir.id)
                            },
                            onReset = {
                                viewModel.resetZikirCount(zikir.id)
                            }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddCustomZikirDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, arabic, trans, target ->
                viewModel.addCustomZikir(name, target, arabic, trans)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun ZikirItemCard(
    zikir: ZikirCounter,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onReset: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("zikir_card_${zikir.id}"),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = zikir.name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (zikir.zikirKey == "custom") {
                            Spacer(modifier = Modifier.width(6.dp))
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
                            ) {
                                Text(
                                    "Kustom",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }
                    if (zikir.translation.isNotBlank()) {
                        Text(
                            text = zikir.translation,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${zikir.currentCount} / ${zikir.target}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (zikir.currentCount >= zikir.target) SoftJade else MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = if (zikir.currentCount >= zikir.target) "Selesai" else "Target",
                        fontSize = 9.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (zikir.arabic.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = zikir.arabic,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = FontFamily.Serif,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            // Completion metric line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(if (zikir.target > 0) (zikir.currentCount.toFloat() / zikir.target).coerceAtMost(1f) else 0f)
                        .clip(CircleShape)
                        .background(if (zikir.currentCount >= zikir.target) SoftJade else MaterialTheme.colorScheme.primary)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Expand settings
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.Settings else Icons.Default.MoreHoriz,
                        contentDescription = "Menu Tambahan",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = {
                            onReset()
                            expanded = false
                        }
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reset", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Reset Hitungan", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                    }

                    if (zikir.zikirKey == "custom") {
                        IconButton(
                            onClick = onDelete
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Hapus Zikir", tint = Color.Red.copy(alpha = 0.8f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TasbihClickerBoard(
    zikir: ZikirCounter,
    onDismiss: () -> Unit,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onReset: () -> Unit
) {
    val context = LocalContext.current
    val view = LocalView.current
    var lastTargetState = remember { zikir.currentCount >= zikir.target }

    // Play tactile vibration and soft tone simulation
    val triggerClickEffect = {
        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        onIncrement()
        
        val nowDone = (zikir.currentCount + 1) >= zikir.target
        if (nowDone && !lastTargetState) {
            Toast.makeText(context, "Selamat, Target Wirid Tercapai!", Toast.LENGTH_SHORT).show()
            lastTargetState = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onDismiss, modifier = Modifier.testTag("dismiss_tasbih")) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Kembail", tint = MaterialTheme.colorScheme.onBackground)
                }
                Text(
                    text = "Tasbih Ruhani",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                IconButton(onClick = onReset) {
                    Icon(Icons.Default.Refresh, contentDescription = "Ulang", tint = MaterialTheme.colorScheme.onBackground)
                }
            }

            // Arabic text & info
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Text(
                    text = zikir.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                if (zikir.arabic.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = zikir.arabic,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = FontFamily.Serif,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center,
                            lineHeight = 36.sp
                        )
                    }
                }
                if (zikir.translation.isNotBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = zikir.translation,
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            // Clicker count dial & big clicker circle
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${zikir.currentCount}",
                    fontSize = 54.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (zikir.currentCount >= zikir.target) SoftJade else MaterialTheme.colorScheme.primary,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "Target Salik: ${zikir.target}",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(24.dp))

                // Majestic touch clicker with glowing border brush
                Box(
                    modifier = Modifier
                        .size(170.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.03f)
                                )
                            )
                        )
                        .border(
                            BorderStroke(
                                4.dp,
                                Brush.sweepGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary,
                                        SoftGold,
                                        SoftJade,
                                        MaterialTheme.colorScheme.primary
                                    )
                                )
                            ),
                            CircleShape
                        )
                        .clickable { triggerClickEffect() }
                        .testTag("big_clicker_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(130.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Fingerprint,
                                contentDescription = "Tep",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "TEKAN",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onPrimary,
                                letterSpacing = 2.sp
                            )
                        }
                    }
                }
            }

            // Decrement & target helper actions
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onDecrement,
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Kurang")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Kurang 1")
                }

                Button(
                    onClick = {
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurfaceVariant),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Selesai")
                }
            }
        }
    }
}

@Composable
fun AddCustomZikirDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, arabic: String, translation: String, target: Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var arabic by remember { mutableStateOf("") }
    var translation by remember { mutableStateOf("") }
    var targetText by remember { mutableStateOf("100") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Tambah Wirid Kustom",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama Wirid (e.g. Ya Adhim)") },
                    modifier = Modifier.fillMaxWidth().testTag("custom_zikir_name"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = arabic,
                    onValueChange = { arabic = it },
                    label = { Text("Lafadz Arab (Opsional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = translation,
                    onValueChange = { translation = it },
                    label = { Text("Arti/Terjemahan (Opsional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = targetText,
                    onValueChange = { targetText = it },
                    label = { Text("Target Hitungan") },
                    modifier = Modifier.fillMaxWidth().testTag("custom_zikir_target"),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Batal")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        modifier = Modifier.testTag("custom_zikir_save_btn"),
                        onClick = {
                            val target = targetText.toIntOrNull() ?: 100
                            if (name.isNotBlank()) {
                                onConfirm(name, arabic, translation, target)
                            }
                        }
                    ) {
                        Text("Simpan")
                    }
                }
            }
        }
    }
}

// ==========================================
// SCREEN 4: SPIRITUAL MUTIARA SULUK (TEACHINGS & KHALWAT)
// ==========================================
@Composable
fun MutiaraSulukScreen(viewModel: SulukViewModel) {
    var subTabState by remember { mutableStateOf(0) }
    val categories = listOf("Hikmah", "Adab", "Nafs & Riyadhoh", "Quran Hadis", "Khalwat")

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ScrollableTabRow(
            selectedTabIndex = subTabState,
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.primary,
            edgePadding = 16.dp
        ) {
            categories.forEachIndexed { index, title ->
                Tab(
                    selected = subTabState == index,
                    onClick = { subTabState = index },
                    text = { Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
            }
        }

        Divider()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            when (subTabState) {
                0 -> KalamHikmahTab()
                1 -> AdabMuridTab()
                2 -> TazkiyahNafsTab()
                3 -> QuranHadistTab()
                4 -> KhalwatRetreatTab(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun KalamHikmahTab() {
    var searchQuery by remember { mutableStateOf("") }
    val authors = listOf("Semua", "Imam Al-Ghazali", "Syekh Ibnu Atha'illah Al-Iskandari", "Syekh Abdul Qadir Al-Jilani", "Maulana Jalaluddin Rumi")
    var selectedAuthor by remember { mutableStateOf("Semua") }

    val filteredQuotes = remember(searchQuery, selectedAuthor) {
        SpiritualData.kalamQuotes.filter { quote ->
            val matchesAuthor = selectedAuthor == "Semua" || quote.author == selectedAuthor
            val matchesSearch = quote.quoteIndo.contains(searchQuery, ignoreCase = true) ||
                    quote.author.contains(searchQuery, ignoreCase = true) ||
                    quote.book.contains(searchQuery, ignoreCase = true)
            matchesAuthor && matchesSearch
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Cari kalam mursyid...") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp)
            )
        }

        item {
            ScrollableTabRow(
                selectedTabIndex = authors.indexOf(selectedAuthor),
                containerColor = Color.Transparent,
                edgePadding = 0.dp,
                divider = {}
            ) {
                authors.forEach { author ->
                    Tab(
                        selected = selectedAuthor == author,
                        onClick = { selectedAuthor = author },
                        text = { Text(author.replace("Syekh ", ""), fontSize = 11.sp) }
                    )
                }
            }
        }

        if (filteredQuotes.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Kalam hikmah tidak ditemukan.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            items(filteredQuotes) { quote ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.ChatBubbleOutline,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = quote.author,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = "Kitab: ${quote.book}",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        if (quote.quoteArabic.isNotBlank()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = quote.quoteArabic,
                                fontSize = 21.sp,
                                fontFamily = FontFamily.Serif,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.End,
                                lineHeight = 32.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "\"${quote.quoteIndo}\"",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(color = MaterialTheme.colorScheme.surfaceVariant)
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "Penjelasan Ruhani:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = quote.commentary,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AdabMuridTab() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Adab Murid & Salik",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Perjalanan spiritual (suluk) tidak bernilai jika tanpa adab lahir dan bathin.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        items(SpiritualData.adabMurid) { adab ->
            var isExpanded by remember { mutableStateOf(false) }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = adab.category,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Text(
                                text = adab.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = "Expand",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    if (isExpanded) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        adab.points.forEachIndexed { i, point ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = "${i + 1}. ",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = point,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TazkiyahNafsTab() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Konsep Tazkiyatun Nafs",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "3 Tahapan Pemurnian Jiwa penempuh thariqah.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Takhalli Tahalli Tajalli Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Proses Emas Ruhani",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    TazkiyahStageRow(
                        title = "1. Takhalli (Pembersihan)",
                        desc = "Menguras dan membersihkan hati dari segala sifat-sifat buruk (tercela/maksiat batin) seperti riya, takabbur, dendam, hasad, dan cinta berlebih pada dunia."
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    TazkiyahStageRow(
                        title = "2. Tahalli (Penghiasan)",
                        desc = "Menghias hati yang kosong tadi dengan sifat-sifat baik (terpuji) seperti ikhlas, sabar, syukur, ridha atas qadha, tawadhu, cinta khidmat, dan zikrullah konstan."
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    TazkiyahStageRow(
                        title = "3. Tajalli (Ketersingkapan)",
                        desc = "Kondisi tersingkapnya tabir (hijab) bathin, saat pancaran cahaya ketuhanan mulai merasuk dan menerangi kalbu penempuh suluk."
                    )
                }
            }
        }

        item {
            Text(
                text = "Tingkatan Nafsu Manusia",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        items(SpiritualData.nafsStages) { stage ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stage.stageName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Arti: ${stage.meaning}",
                        fontSize = 11.sp,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Penyakit/Sifat Utama:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        stage.traits.forEach { trait ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            ) {
                                Text(
                                    text = trait,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Terapi Mudawamah (Solusi):",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = stage.treatment,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            Text(
                text = "Panduan Praktis Riyadhoh",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SpiritualData.riyadhohGuidelines.forEach { guide ->
                        val parts = guide.split(":")
                        Column(modifier = Modifier.padding(vertical = 6.dp)) {
                            Text(
                                text = parts[0].replace("**", ""),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            if (parts.size > 1) {
                                Text(
                                    text = parts[1].trim(),
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TazkiyahStageRow(title: String, desc: String) {
    Column {
        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = desc,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun QuranHadistTab() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Dalil Quran & Hadist",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Landasan syariat thariqah pembersihan raga dan sukma.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        items(SpiritualData.quranHadistList) { scripture ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = scripture.source,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f))
                        ) {
                            Text(
                                text = scripture.category,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = scripture.arabic,
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Serif,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        lineHeight = 32.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = scripture.translation,
                        fontSize = 13.sp,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Hikmah Syar'i:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = scripture.explanation,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun KhalwatRetreatTab(viewModel: SulukViewModel) {
    val activeRetreat by viewModel.activeRetreat.collectAsStateWithLifecycle()
    var targetDaysText by remember { mutableStateOf("40") }
    var noteText by remember { mutableStateOf("") }

    if (activeRetreat != null) {
        val retreat = activeRetreat!!
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 12.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.08f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "KHALWAT RIYADHOH AKTIF",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.secondary,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Hari Ke-${retreat.currentDayOfRetreat}",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Target Total: ${retreat.targetDays} Hari Seclusion",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(14.dp))
                    if (retreat.note.isNotBlank()) {
                        Text(
                            text = "Tekad Hati (Niat):",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "\"${retreat.note}\"",
                            fontSize = 13.sp,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.secondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Rules of Seclusion / Adab Khalwat
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "4 Undang-Undang Khalwat Ruhani",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "1. Uzlah (Mengurangi interaksi sia-sia dengan mahluk/sosmed).\n" +
                                "2. Khauf & Daimuz-Zikir (Membasahi lisan dengan zikir jahar & khafi).\n" +
                                "3. Sahar (Membatasi tidur untuk bangun malam & munajat).\n" +
                                "4. Ju' (Membatasi nafsu makan agar jiwa lebih peka dan ringan).",
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Progression Actions (Inc/Dec)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(
                    onClick = { viewModel.decreaseKhalwatDay() },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant, CircleShape).testTag("khalwat_dec_btn")
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Kurang Hari")
                }

                Button(
                    onClick = { viewModel.advanceKhalwatDay() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.testTag("khalwat_inc_btn")
                ) {
                    Icon(Icons.Default.NavigateNext, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Lanjut ke Hari Berikutnya")
                }
            }

            OutlinedButton(
                onClick = { viewModel.stopKhalwatRetreat() },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                border = BorderStroke(1.dp, Color.Red.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp).testTag("khalwat_stop_btn")
            ) {
                Icon(Icons.Default.Stop, contentDescription = "Selesai")
                Spacer(modifier = Modifier.width(6.dp))
                Text("Hentikan Khalwat Lebih Awal")
            }
        }
    } else {
        // Form to register new seclusion
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 12.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column {
                Text(
                    text = "Mulai Khalwat / Seclusion",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Masuki fase menyendiri dari keramaian dunia fokus tarbiyah ruhani harian.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Formulir Niat Khalwat",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    OutlinedTextField(
                        value = targetDaysText,
                        onValueChange = { targetDaysText = it },
                        label = { Text("Durasi Khalwat (Hari, e.g. 3, 10, 40)") },
                        modifier = Modifier.fillMaxWidth().testTag("khalwat_days_input"),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = noteText,
                        onValueChange = { noteText = it },
                        label = { Text("Tekad Bathin / Do'a Khusus") },
                        placeholder = { Text("e.g. Mendekatkan diri kepada Allah, membersihkan riya") },
                        modifier = Modifier.fillMaxWidth().testTag("khalwat_notes_input"),
                        minLines = 2
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = {
                            val days = targetDaysText.toIntOrNull() ?: 3
                            viewModel.beginKhalwatRetreat(days, noteText)
                            noteText = ""
                        },
                        modifier = Modifier.fillMaxWidth().testTag("khalwat_start_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Mulai Khalwat Seclusion")
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Apa itu Khalwat?",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Khalwat adalah tradisi para nabi dan awliya' untuk menyendiri dari kesibukan dunia guna menjernihkan pikiran, mengasah intuisi batin, dan menyambungkan kembali hubungan suci yang renggan dengan Rabbul Alamin. Menggunakan tracker ini membantu Anda mendisiplinkan hari demi hari.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
