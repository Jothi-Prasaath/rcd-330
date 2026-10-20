package com.budz.vwpolo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Background = Color(0xFF080B10)
private val Panel = Color(0xFF121821)
private val Panel2 = Color(0xFF19222E)
private val Accent = Color(0xFFFF3B45)
private val Muted = Color(0xFF9BA8B8)
private val White = Color(0xFFF4F7FB)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Accent,
                    background = Background,
                    surface = Panel,
                    onBackground = White,
                    onSurface = White
                )
            ) {
                PoloDashboard(
                    onNavigate = { openMaps() },
                    onOpenMusic = { openMusic() },
                    onCall = { openDialer() }
                )
            }
        }
    }

    private fun openMaps() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q="))
        intent.setPackage("com.google.android.apps.maps")
        try { startActivity(intent) } catch (_: Exception) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.google.com")))
        }
    }

    private fun openMusic() {
        val intent = packageManager.getLaunchIntentForPackage("com.spotify.music")
            ?: Intent(Intent.ACTION_VIEW, Uri.parse("https://music.youtube.com"))
        try { startActivity(intent) } catch (_: Exception) {}
    }

    private fun openDialer() {
        try { startActivity(Intent(Intent.ACTION_DIAL)) } catch (_: Exception) {}
    }
}

@Composable
private fun PoloDashboard(
    onNavigate: () -> Unit,
    onOpenMusic: () -> Unit,
    onCall: () -> Unit
) {
    var selectedTab by remember { mutableStateOf("Drive") }
    var driveMode by remember { mutableStateOf("NORMAL") }
    var showNotice by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize(), color = Background) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 22.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("POLO / COMPANION", color = Muted, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                        Text("Good drive.", color = White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    }
                    Box(
                        modifier = Modifier.size(44.dp).background(Panel2, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.DirectionsCar, contentDescription = "Car", tint = Accent, modifier = Modifier.size(25.dp))
                    }
                }
            }
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().background(
                        androidx.compose.ui.graphics.Brush.linearGradient(listOf(Color(0xFF202B3A), Color(0xFF10151D))),
                        RoundedCornerShape(24.dp)
                    ).padding(20.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("YOUR COCKPIT", color = Accent, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                                Text("Volkswagen Polo", color = White, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
                                Text("1.5 TDI  •  Highline Plus", color = Muted, fontSize = 13.sp)
                            }
                            Icon(Icons.Default.Speed, contentDescription = "Dashboard", tint = Accent, modifier = Modifier.size(42.dp))
                        }
                        HorizontalDivider(color = Color(0xFF344050))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            StatusChip("SYSTEM", "READY")
                            StatusChip("MODE", driveMode)
                        }
                        Text("Companion interface only. Vehicle data is not connected.", color = Muted, fontSize = 11.sp)
                    }
                }
            }
            item {
                Text("QUICK ACCESS", color = Muted, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    ActionTile("Navigate", "Open Maps", Icons.Default.Navigation, Modifier.weight(1f), onNavigate)
                    ActionTile("Music", "Open player", Icons.Default.MusicNote, Modifier.weight(1f), onOpenMusic)
                }
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    ActionTile("Phone", "Open dialer", Icons.Default.Call, Modifier.weight(1f), onCall)
                    ActionTile("Assistant", "Voice later", Icons.Default.MicNone, Modifier.weight(1f)) { showNotice = true }
                }
            }
            item {
                Text("DRIVE PROFILE", color = Muted, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    listOf("NORMAL", "FOCUS", "NIGHT").forEach { mode ->
                        val active = driveMode == mode
                        Box(
                            modifier = Modifier.weight(1f).background(if (active) Accent.copy(alpha = 0.18f) else Panel, RoundedCornerShape(14.dp))
                                .clickable { driveMode = mode }
                                .then(if (active) Modifier.background(Accent.copy(alpha = 0.08f), RoundedCornerShape(14.dp)) else Modifier)
                                .padding(vertical = 15.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(mode, color = if (active) Accent else Muted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            item {
                Text("SHORTCUTS", color = Muted, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ShortcutRow(Icons.Default.Settings, "App settings", "Preferences and appearance") { showNotice = true }
                    ShortcutRow(Icons.Default.Info, "Vehicle profile", "Polo 1.5 TDI • editable later") { showNotice = true }
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(14.dp)).padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Shield, contentDescription = null, tint = Accent)
                    Spacer(Modifier.width(10.dp))
                    Text("Built as a standalone app. No ECU access, no firmware changes.", color = Muted, fontSize = 11.sp)
                }
            }
        }
    }

    if (showNotice) {
        AlertDialog(
            onDismissRequest = { showNotice = false },
            title = { Text("Coming in the next build") },
            text = { Text("This control is a placeholder for the next implementation step. Navigation, music and phone shortcuts are wired to launch apps.") },
            confirmButton = { TextButton(onClick = { showNotice = false }) { Text("Got it") } },
            containerColor = Panel,
            titleContentColor = White,
            textContentColor = Muted
        )
    }
}

@Composable
private fun StatusChip(label: String, value: String) {
    Column(
        modifier = Modifier.background(Color(0xFF0B1017).copy(alpha = 0.7f), RoundedCornerShape(10.dp)).padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(label, color = Muted, fontSize = 9.sp, letterSpacing = 1.sp)
        Text(value, color = White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ActionTile(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier.height(112.dp).background(Panel, RoundedCornerShape(18.dp))
            .clickable(onClick = onClick).padding(15.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(icon, contentDescription = title, tint = Accent, modifier = Modifier.size(24.dp))
        Column {
            Text(title, color = White, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            Text(subtitle, color = Muted, fontSize = 11.sp)
        }
    }
}

@Composable
private fun ShortcutRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick).padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Accent)
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Text(subtitle, color = Muted, fontSize = 11.sp)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Muted)
    }
}
