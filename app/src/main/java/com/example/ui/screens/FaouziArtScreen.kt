package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.R
import com.example.model.LinksConfig
import com.example.model.SocialLinkItem
import com.example.ui.components.FaouziArtLinkItem
import com.example.ui.components.QrCodeView
import com.example.ui.theme.FaouziDarkTeal
import com.example.ui.theme.FaouziDeepTeal
import com.example.ui.theme.FaouziGold
import com.example.ui.theme.FaouziGoldWarm
import com.example.ui.theme.FaouziLightTurquoise
import com.example.ui.theme.FaouziMediumTeal
import com.example.ui.theme.FaouziSurfaceGlass
import com.example.ui.theme.FaouziSurfaceGlassBorder
import com.example.ui.theme.FaouziVibrantTeal
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary
import com.example.viewmodel.FaouziArtViewModel
import kotlinx.coroutines.launch

@Composable
fun FaouziArtScreen(
    viewModel: FaouziArtViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.userMessage) {
        uiState.userMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearUserMessage()
        }
    }

    // Explicit Arabic Right-to-Left Layout
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = Color.Transparent,
            modifier = modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                FaouziDarkTeal,
                                FaouziDeepTeal,
                                FaouziMediumTeal,
                                Color(0xFF083A41),
                                FaouziDarkTeal
                            )
                        )
                    )
            ) {
                // Subtle decorative arabesque lighting or geometric accent circles in background
                Box(
                    modifier = Modifier
                        .size(320.dp)
                        .align(Alignment.TopCenter)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0x264DD0E1),
                                    Color.Transparent
                                )
                            )
                        )
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Maximum width constrained for tablets and foldables
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .widthIn(max = 560.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Top Quick Bar: QR Code modal + Share Profile
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Algerian artisan hallmark badge
                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = FaouziSurfaceGlass,
                                    border = BorderStroke(1.dp, FaouziSurfaceGlassBorder)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "🇩🇿 الجزائر • حرفة أصيلة",
                                            color = FaouziGoldWarm,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp
                                            )
                                        )
                                    }
                                }

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // QR Code button
                                    Surface(
                                        shape = CircleShape,
                                        color = FaouziSurfaceGlass,
                                        border = BorderStroke(1.dp, FaouziSurfaceGlassBorder),
                                        modifier = Modifier.size(40.dp)
                                    ) {
                                        IconButton(
                                            onClick = { viewModel.openQrDialog() },
                                            modifier = Modifier.testTag("btn_open_qr")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.QrCode,
                                                contentDescription = "عرض رمز QR للبطاقة",
                                                tint = TextPrimary,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }

                                    // Share button
                                    Surface(
                                        shape = CircleShape,
                                        color = FaouziSurfaceGlass,
                                        border = BorderStroke(1.dp, FaouziSurfaceGlassBorder),
                                        modifier = Modifier.size(40.dp)
                                    ) {
                                        IconButton(
                                            onClick = {
                                                shareProfile(context, uiState.websiteUrl)
                                            },
                                            modifier = Modifier.testTag("btn_share_profile")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Share,
                                                contentDescription = "مشاركة الرابط",
                                                tint = TextPrimary,
                                                modifier = Modifier.size(19.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            // 1. Original Workshop Logo Emblem in Rounded Square
                            Box(
                                modifier = Modifier
                                    .padding(top = 8.dp, bottom = 14.dp)
                                    .size(128.dp)
                                    .shadow(elevation = 16.dp, shape = RoundedCornerShape(28.dp), spotColor = FaouziLightTurquoise)
                                    .border(
                                        BorderStroke(
                                            3.dp,
                                            Brush.sweepGradient(
                                                listOf(
                                                    FaouziGoldWarm,
                                                    FaouziLightTurquoise,
                                                    FaouziGoldWarm,
                                                    FaouziLightTurquoise
                                                )
                                            )
                                        ),
                                        shape = RoundedCornerShape(28.dp)
                                    )
                                    .padding(4.dp)
                                    .clip(RoundedCornerShape(24.dp))
                                    .background(Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.img_faouzi_logo),
                                    contentDescription = "شعار ورشة فوزي للفنون",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(10.dp)
                                )
                            }

                            // 2. Prominent Username @faouzi.art
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(bottom = 4.dp)
                            ) {
                                Text(
                                    text = LinksConfig.USERNAME,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 20.sp,
                                        letterSpacing = 0.5.sp
                                    ),
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "حساب موثق للورشة",
                                    tint = FaouziLightTurquoise,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            // 3. Arabic Business Title
                            Text(
                                text = LinksConfig.WORKSHOP_NAME_AR,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp
                                ),
                                color = FaouziGoldWarm,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            // 4. Arabic Description
                            Text(
                                text = LinksConfig.WORKSHOP_DESCRIPTION_AR,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 14.5.sp,
                                    lineHeight = 22.sp
                                ),
                                color = TextSecondary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 4.dp)
                                    .fillMaxWidth()
                            )

                            // Direct Contact / Quick Action Bar
                            Row(
                                modifier = Modifier
                                    .padding(top = 14.dp, bottom = 22.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        copyToClipboard(context, uiState.websiteUrl)
                                        scope.launch {
                                            snackbarHostState.showSnackbar("تم نسخ رابط الصفحة بنجاح!")
                                        }
                                    },
                                    shape = RoundedCornerShape(24.dp),
                                    border = BorderStroke(1.dp, FaouziSurfaceGlassBorder),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = FaouziSurfaceGlass
                                    ),
                                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = null,
                                        tint = FaouziLightTurquoise,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "نسخ رابط الصفحة",
                                        color = TextPrimary,
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                OutlinedButton(
                                    onClick = { viewModel.openQrDialog() },
                                    shape = RoundedCornerShape(24.dp),
                                    border = BorderStroke(1.dp, FaouziSurfaceGlassBorder),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = FaouziSurfaceGlass
                                    ),
                                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.QrCode,
                                        contentDescription = null,
                                        tint = FaouziGoldWarm,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "بطاقة الورشة QR",
                                        color = TextPrimary,
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                }
                            }
                        }
                    }

                    // 5. The Seven Prominent Social Media Links
                    items(items = uiState.links, key = { it.id }) { item ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .widthIn(max = 560.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            FaouziArtLinkItem(
                                item = item,
                                onClick = {
                                    openUrl(context, item.url)
                                },
                                onEditClick = if (item.isEditable) {
                                    { viewModel.startEditingLink(item) }
                                } else null,
                                modifier = Modifier.padding(vertical = 6.dp)
                            )
                        }
                    }

                    // Workshop Location & Phone Contact Card (Below links)
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .widthIn(max = 560.dp)
                                .padding(top = 10.dp, bottom = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
// Contact and Location Info Card
                            Surface(
                                shape = RoundedCornerShape(18.dp),
                                color = FaouziSurfaceGlass,
                                border = BorderStroke(1.dp, FaouziSurfaceGlassBorder),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 4.dp, vertical = 8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                                ) {
                                    // Location Row (Clickable to open in Google Maps)
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable { openUrl(context, LinksConfig.MAPS_URL) }
                                            .padding(vertical = 5.dp, horizontal = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(Color(0x33FFD54F)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.LocationOn,
                                                contentDescription = "موقع الورشة",
                                                tint = FaouziGoldWarm,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "مقر الورشة",
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                                color = FaouziGoldWarm
                                            )
                                            Text(
                                                text = LinksConfig.LOCATION_ADDRESS,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 13.5.sp
                                                ),
                                                color = TextPrimary
                                            )
                                        }
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color(0x264DD0E1)
                                        ) {
                                            Text(
                                                text = "الخريطة",
                                                color = FaouziLightTurquoise,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    // Phone Direct Call Row
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable { openUrl(context, LinksConfig.PHONE_TEL_URI) }
                                            .padding(vertical = 5.dp, horizontal = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(Color(0x3325D366)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Phone,
                                                contentDescription = "اتصال هاتفي",
                                                tint = Color(0xFF69F0AE),
                                                modifier = Modifier.size(19.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "الهاتف والطلبيات",
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                                color = Color(0xFF69F0AE)
                                            )
                                            Text(
                                                text = LinksConfig.PHONE_NUMBER,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 14.sp
                                                ),
                                                color = TextPrimary
                                            )
                                        }
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color(0x3325D366)
                                        ) {
                                            Text(
                                                text = "اتصال",
                                                color = Color(0xFF69F0AE),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 6. Discreet Artisanal Footer (No Linktree branding, no third party ads)
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .widthIn(max = 560.dp)
                                .padding(top = 32.dp, bottom = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "ورشة فوزي للفنون اليدوية",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.5.sp
                                ),
                                color = FaouziGoldWarm
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "صفحة مستقلة ومباشرة • صنع الهدايا والديكور والتحف اليدوية الجزائريّة",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.5.sp
                                ),
                                color = TextTertiary,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "© ${LinksConfig.WORKSHOP_NAME_AR} • جميع الحقوق محفوظة",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.5.sp
                                ),
                                color = TextTertiary.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            // QR Code Dialog for Business Cards
            if (uiState.showQrDialog) {
                QrCodeBusinessCardDialog(
                    websiteUrl = uiState.websiteUrl,
                    onDismiss = { viewModel.closeQrDialog() },
                    onCopyUrl = {
                        copyToClipboard(context, uiState.websiteUrl)
                        scope.launch {
                            snackbarHostState.showSnackbar("تم نسخ الرابط بنجاح!")
                        }
                    },
                    onShareUrl = {
                        shareProfile(context, uiState.websiteUrl)
                    }
                )
            }

            // Edit Link Dialog (for Facebook, Telegram, or any editable link)
            uiState.editingItem?.let { itemToEdit ->
                EditLinkDialog(
                    item = itemToEdit,
                    onDismiss = { viewModel.cancelEditingLink() },
                    onSave = { newUrl ->
                        viewModel.saveEditedLink(itemToEdit.id, newUrl)
                    }
                )
            }
        }
    }
}

/**
 * QR Code Business Card Dialog.
 * Generates an artisan, permanent static QR code suitable for printed cards, stickers, and wooden boxes.
 */
@Composable
fun QrCodeBusinessCardDialog(
    websiteUrl: String,
    onDismiss: () -> Unit,
    onCopyUrl: () -> Unit,
    onShareUrl: () -> Unit
) {
    BackHandler(onBack = onDismiss)

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = FaouziDeepTeal,
        titleContentColor = TextPrimary,
        textContentColor = TextSecondary,
        shape = RoundedCornerShape(24.dp),
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "رمز QR للبطاقة الشخصية",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    color = FaouziGoldWarm,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "@faouzi.art • ورشة فوزي للفنون",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp
                    ),
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // QR Code View
                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .padding(8.dp)
                        .shadow(12.dp, RoundedCornerShape(16.dp))
                ) {
                    QrCodeView(
                        content = websiteUrl,
                        darkColor = Color(0xFF062A2E),
                        lightColor = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "امسح الرمز لفتح الروابط مباشرة أو طباعته على كروت الأعمال والعلب الخشبية.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    ),
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = FaouziDarkTeal,
                    border = BorderStroke(1.dp, FaouziSurfaceGlassBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = websiteUrl,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp
                        ),
                        color = FaouziLightTurquoise,
                        maxLines = 2,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onCopyUrl,
                colors = ButtonDefaults.buttonColors(containerColor = FaouziVibrantTeal),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("نسخ الرابط")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("إغلاق", color = TextSecondary)
            }
        }
    )
}

/**
 * Dialog allowing the user to update destinations that haven't been independently verified
 * (such as Facebook or Telegram), or any customized link.
 */
@Composable
fun EditLinkDialog(
    item: SocialLinkItem,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var urlText by remember { mutableStateOf(item.url) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    BackHandler(onBack = onDismiss)

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = FaouziDeepTeal,
        titleContentColor = TextPrimary,
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(
                text = "تعديل رابط: ${item.title}",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "يمكنك تعديل رابط الوجهة وحفظه ليتم فتحه مباشرة عند الضغط على الزر:",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                OutlinedTextField(
                    value = urlText,
                    onValueChange = {
                        urlText = it
                        errorMessage = null
                    },
                    label = { Text("رابط الوجهة (URL)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = FaouziLightTurquoise,
                        unfocusedBorderColor = FaouziSurfaceGlassBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                errorMessage?.let { err ->
                    Text(
                        text = err,
                        color = Color(0xFFFF8A80),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val trimmed = urlText.trim()
                    if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
                        onSave(trimmed)
                    } else {
                        errorMessage = "يرجى إدخال رابط صالح يبدأ بـ https://"
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = FaouziVibrantTeal)
            ) {
                Text("حفظ")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء", color = TextSecondary)
            }
        }
    )
}

/**
 * Safely opens external URLs or launches specific apps (like WhatsApp).
 */
private fun openUrl(context: Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "تعذر فتح الرابط: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
    }
}

/**
 * Shares the profile link using native Android share sheet.
 */
private fun shareProfile(context: Context, websiteUrl: String) {
    try {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "زوروا ورشة فوزي للفنون اليدوية في الجزائر (${LinksConfig.USERNAME}):\n$websiteUrl\nأعمال فنية، هدايا، ديكور وتحف راقية!"
            )
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "مشاركة صفحة ورشة فوزي للفنون")
        context.startActivity(shareIntent)
    } catch (e: Exception) {
        Toast.makeText(context, "تعذر المشاركة", Toast.LENGTH_SHORT).show()
    }
}

/**
 * Copies link to system clipboard.
 */
private fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("رابط ورشة فوزي للفنون", text)
    clipboard.setPrimaryClip(clip)
}
