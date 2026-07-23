package com.example.qa_accessibility_app

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.qa_accessibility_app.ui.theme.QA_Accessibility_AppTheme

// A back button that finishes the current orientation activity.
@Composable
private fun OrientationBackButton() {
    val context = LocalContext.current
    IconButton(onClick = { (context as? Activity)?.finish() }) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Go back"
        )
    }
}

// TE-20569 — App & Screen Orientation Lock (WCAG 1.3.4).
// Each activity is declared in AndroidManifest.xml with an android:screenOrientation
// value. The FixedOrientationLock rule reads that manifest attribute: values that pin
// a single orientation FAIL; values that allow rotation (fullSensor, unspecified, …)
// PASS. The rule reads the manifest, so scanning any of these screens reflects it.

class OrientationLockActivity : ComponentActivity() {  // manifest: portrait (violation)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { QA_Accessibility_AppTheme { OrientationScreenContent("portrait", isViolation = true) } }
    }
}

class LandscapeLockActivity : ComponentActivity() {  // manifest: landscape (violation)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { QA_Accessibility_AppTheme { OrientationScreenContent("landscape", isViolation = true) } }
    }
}

class NoSensorLockActivity : ComponentActivity() {  // manifest: nosensor (violation)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { QA_Accessibility_AppTheme { OrientationScreenContent("nosensor", isViolation = true) } }
    }
}

class RotatableActivity : ComponentActivity() {  // manifest: fullSensor (pass — rotates with device)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { QA_Accessibility_AppTheme { FreeRotationContent() } }
    }
}

// Mirrors iOS TC-04 (Free Rotation): supports both orientations and shows the
// CURRENT orientation live. LocalConfiguration recomposes on rotation, so the
// "Current" line updates as the device is rotated.
@Composable
private fun FreeRotationContent() {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OrientationBackButton()
            Text("Free Rotation (Pass)", style = MaterialTheme.typography.headlineSmall)
            Text(
                "This screen supports BOTH orientations (android:screenOrientation=\"fullSensor\"). Rotate the device — it follows.",
                style = MaterialTheme.typography.bodyMedium
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x1A00C853), MaterialTheme.shapes.medium)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text("Portrait: Supported ✓", style = MaterialTheme.typography.bodyMedium)
                Text("Landscape: Supported ✓", style = MaterialTheme.typography.bodyMedium)
                Text(
                    "Current: ${if (isLandscape) "Landscape" else "Portrait"}",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Text(
                "✅ PASS: Screen rotates freely with the device orientation.",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF007A33)
            )
        }
    }
}

@Composable
private fun OrientationScreenContent(orientation: String, isViolation: Boolean) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OrientationBackButton()
            Text(
                if (isViolation) "Orientation-Locked Screen" else "Rotatable Screen (Pass)",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                if (isViolation)
                    "This activity is declared with android:screenOrientation=\"$orientation\" in the manifest — a single fixed orientation. It is an App & Screen Orientation Lock (WCAG 1.3.4) violation."
                else
                    "This activity is declared with android:screenOrientation=\"$orientation\" in the manifest, so it rotates when you rotate the device. It passes the App & Screen Orientation Lock rule. Rotate the device to see it follow.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
