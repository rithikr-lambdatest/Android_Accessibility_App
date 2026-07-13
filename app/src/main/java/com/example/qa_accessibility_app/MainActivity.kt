package com.example.qa_accessibility_app

import android.R.attr.text
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.text
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.qa_accessibility_app.R
import com.example.qa_accessibility_app.ui.theme.QA_Accessibility_AppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QA_Accessibility_AppTheme {
                QA_Accessibility_AppApp()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun QA_Accessibility_AppApp() {
    var currentDestination by rememberSaveable { mutableStateOf<AppDestinations?>(null) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        if (currentDestination == null) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // --- Violation: Interactive role undefined ---
                // Clickable Box with no role set — scanner flags as missing role
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.shapes.medium)
                        .clickable { }
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Tap here for info",
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                // --- Violation: Duplicate accessibility label ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(onClick = { }, modifier = Modifier.weight(1f)) {
                        Text("Delete")
                    }
                    Button(onClick = { }, modifier = Modifier.weight(1f)) {
                        Text("Delete")
                    }
                }

                // --- Violation: Unlabelled checkbox ---
                // Standalone Checkbox with no label text or contentDescription
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    var checked by remember { mutableStateOf(false) }
                    Checkbox(
                        checked = checked,
                        onCheckedChange = { checked = it }
                        // No contentDescription, no associated label
                    )
                    Text("I agree to the terms")
                }

                Spacer(modifier = Modifier.height(4.dp))

                AppDestinations.entries.chunked(2).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowItems.forEach { destination ->
                            Button(
                                onClick = { currentDestination = destination },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(text = destination.label)
                            }
                        }
                        // If odd number, fill remaining space
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        } else {
            BackHandler {
                currentDestination = null
            }
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                IconButton(
                    onClick = { currentDestination = null },
                    modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Go back"
                    )
                }
                // Content for the current destination
                when (currentDestination) {
                    AppDestinations.ACCESSIBLE_IMAGES -> AccessibleImagesScreen()
                    AppDestinations.INTERACTIVE_ELEMENT_A11Y -> InteractiveElementA11yScreen()
                    AppDestinations.SWITCH_A11Y_LABEL -> SwitchA11yLabelScreen()
                    AppDestinations.CHECKBOX_A11Y_LABEL -> CheckboxA11yLabelScreen()
                    AppDestinations.EDITABLE_A11Y_LABEL -> EditableA11yLabelScreen()
                    AppDestinations.INPUT_FIELD_LABEL -> InputFieldLabelScreen()
                    AppDestinations.TEXT_TOUCH_TARGET -> TextTouchTargetScreen()
                    AppDestinations.LABELS_NAMES -> LabelsAndNamesScreen()
                    AppDestinations.SPECIAL_CHARS_IN_LABEL -> SpecialCharsInLabelScreen()
                    AppDestinations.REDUNDANT_ROLE_IN_LABEL -> RedundantRoleInLabelScreen()
                    AppDestinations.REDUNDANT_STATE_IN_LABEL -> RedundantStateInLabelScreen()
                    AppDestinations.GENERIC_LINK_TEXT -> GenericLinkTextScreen()
                    AppDestinations.TRAVERSAL_ORDER_MISMATCH -> TraversalOrderMismatchScreen()
                    AppDestinations.DYNAMIC_TYPE_SUPPORT -> DynamicTypeSupportScreen()
                    AppDestinations.RESPONSIVE_CONTAINER -> ResponsiveContainerScreen()
                    AppDestinations.IMAGE_IN_TEXT -> ImageInTextScreen()
                    AppDestinations.MEANINGFUL_READING_ORDER -> MeaningfulReadingOrderScreen()
                    null -> {}
                }
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector,
) {
    ACCESSIBLE_IMAGES("Accessible Images", Icons.Default.Home),
    INTERACTIVE_ELEMENT_A11Y("Interactive a11y label", Icons.Default.Home),
    SWITCH_A11Y_LABEL("Switch a11y label", Icons.Default.Home),
    CHECKBOX_A11Y_LABEL("Checkbox a11y label", Icons.Default.Home),
    EDITABLE_A11Y_LABEL("Editable a11y label", Icons.Default.Home),
    INPUT_FIELD_LABEL("Input field label", Icons.Default.Home),
    TEXT_TOUCH_TARGET("Text touch target size", Icons.Default.Home),
    LABELS_NAMES("Labels & Names", Icons.Default.Home),
    SPECIAL_CHARS_IN_LABEL("Special Chars in Label", Icons.Default.Build),
    REDUNDANT_ROLE_IN_LABEL("Redundant Role in Label", Icons.Default.Build),
    REDUNDANT_STATE_IN_LABEL("Redundant State in Label", Icons.Default.Build),
    GENERIC_LINK_TEXT("Generic Link Text", Icons.Default.Build),
    TRAVERSAL_ORDER_MISMATCH("Traversal Order Mismatch", Icons.Default.Build),
    DYNAMIC_TYPE_SUPPORT("Dynamic Type Support", Icons.Default.Build),
    RESPONSIVE_CONTAINER("Responsive Container", Icons.Default.Build),
    IMAGE_IN_TEXT("Text in Image", Icons.Default.Build),
    MEANINGFUL_READING_ORDER("Meaningful Reading Order", Icons.Default.Build),
}

@Composable
fun AccessibleImagesScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Card 1: ImageView element accessibility label (WCAG 1.1.1, Level A)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "ImageView Element Accessibility Label",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Violation: Image with empty contentDescription
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Violation",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.image_with_text),
                            contentDescription = "",
                            modifier = Modifier.size(120.dp)
                        )
                    }
                    // Fix: Image with descriptive contentDescription
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Fix",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.image_with_text),
                            contentDescription = "Squirrel sitting on a branch",
                            modifier = Modifier.size(120.dp)
                        )
                    }
                }
                Text(
                    text = "Violation: Empty contentDescription. Fix: Descriptive contentDescription.",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Card 2: Images with text (WCAG 1.4.5, Level AA)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Images With Text",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Violation: Text embedded in image
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Violation",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.image_with_text),
                            contentDescription = "Squirrel sitting on a branch",
                            modifier = Modifier.size(120.dp)
                        )
                    }
                    // Fix: Native Text alongside image
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Fix",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.image_with_text),
                            contentDescription = "Squirrel sitting on a branch",
                            modifier = Modifier.size(120.dp)
                        )
                        Text(
                            text = "Squirrel sitting on a branch",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
                Text(
                    text = "Violation: Text embedded in image. Fix: Use native Text composables.",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun InteractiveElementA11yScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Interactive Element Accessibility Label",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Violation: Icon button has no contentDescription — screen readers cannot announce its purpose.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                // Violation: Interactive icon with no accessibility label
                IconButton(onClick = { /* Do something */ }) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null
                    )
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Fix: Icon button has a descriptive contentDescription.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                // Fix: Descriptive contentDescription provided
                IconButton(onClick = { /* Do something */ }) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Add to favourites"
                    )
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Violation: Custom clickable view with no label.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                // Violation: Custom interactive element with no contentDescription
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable { /* Do something */ },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Fix: Custom clickable view with a descriptive label.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                // Fix: contentDescription added via semantics
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable(role = Role.Button) { /* Do something */ }
                        .semantics { contentDescription = "Add new item" },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun SwitchA11yLabelScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Switch Element Accessibility Label",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                var isCheckedViolation by remember { mutableStateOf(false) }
                var isCheckedFix by remember { mutableStateOf(false) }

                Text(
                    text = "Violation: Switch without any label — screen reader only announces on/off state.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                // Violation: Standalone switch with no label or contentDescription
                Switch(
                    checked = isCheckedViolation,
                    onCheckedChange = { isCheckedViolation = it }
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Fix: Switch with a descriptive label properly associated.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                // Fix: Row with toggleable merges label and switch into one accessible element
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .toggleable(
                            value = isCheckedFix,
                            onValueChange = { isCheckedFix = it },
                            role = Role.Switch
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Enable notifications",
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = isCheckedFix,
                        onCheckedChange = null // Row handles it
                    )
                }
            }
        }
    }
}

@Composable
fun EditableA11yLabelScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Editable Element Accessibility Label",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                var textViolation by remember { mutableStateOf("") }
                var textFix by remember { mutableStateOf("") }

                Text(
                    text = "Violation: Input field without any label — screen reader cannot convey the field's purpose.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                // Violation: TextField with no label, placeholder, or contentDescription
                TextField(
                    value = textViolation,
                    onValueChange = { textViolation = it },
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Fix: Input field with a label (hint) that screen readers announce.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                // Fix: label acts as the hint — persists as a floating label when the user types
                TextField(
                    value = textFix,
                    onValueChange = { textFix = it },
                    label = { Text("Your Name") }
                )
            }
        }
    }
}

@Composable
fun InputFieldLabelScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Card 1: No visible label
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Missing Visible Label",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                var textViolation by remember { mutableStateOf("") }
                var textFix by remember { mutableStateOf("") }

                Text(
                    text = "Violation: Input field has no visible label — user cannot tell what to enter.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                // Violation: No label, no placeholder — completely unlabeled
                TextField(
                    value = textViolation,
                    onValueChange = { textViolation = it },
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Fix: Input field has a visible, persistent label.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                // Fix: label persists as a floating label when user types
                TextField(
                    value = textFix,
                    onValueChange = { textFix = it },
                    label = { Text("Email address") }
                )
            }
        }

        // Card 2: Placeholder-only label
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Placeholder-Only Label",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                var textViolation by remember { mutableStateOf("") }
                var textFix by remember { mutableStateOf("") }

                Text(
                    text = "Violation: Placeholder disappears when the user starts typing — label is lost.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                // Violation: Placeholder is the only source of labeling
                TextField(
                    value = textViolation,
                    onValueChange = { textViolation = it },
                    placeholder = { Text("Enter your phone number") }
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Fix: Use a persistent label. Placeholder can supplement but not replace it.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                // Fix: label stays visible; placeholder provides additional hint
                TextField(
                    value = textFix,
                    onValueChange = { textFix = it },
                    label = { Text("Phone number") },
                    placeholder = { Text("e.g. +1 234 567 8900") }
                )
            }
        }

        // Card 3: Non-descriptive label
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Non-Descriptive Label",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                var textViolation by remember { mutableStateOf("") }
                var textFix by remember { mutableStateOf("") }

                Text(
                    text = "Violation: Label does not describe the expected input.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                // Violation: Vague label
                TextField(
                    value = textViolation,
                    onValueChange = { textViolation = it },
                    label = { Text("Input") }
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Fix: Label clearly describes the field's purpose and expected input.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                // Fix: Descriptive label
                TextField(
                    value = textFix,
                    onValueChange = { textFix = it },
                    label = { Text("Date of birth (DD/MM/YYYY)") }
                )
            }
        }
    }
}

@Composable
fun TextTouchTargetScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Card 1: Touch Target Size (WCAG 2.5.5, Level AAA)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Touch Target Size",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Violation: Buttons are only 20dp — well below the 44dp minimum.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                // Violation: tiny touch targets
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .clickable {},
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onPrimary)
                    }
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .clickable {},
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Favorite, contentDescription = "Favourite", modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onPrimary)
                    }
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .clickable {},
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Search", modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onPrimary)
                    }
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Fix: Buttons are 48dp — meets the 44dp minimum.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                // Fix: adequate touch targets
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .clickable {},
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(24.dp), tint = MaterialTheme.colorScheme.onPrimary)
                    }
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .clickable {},
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Favorite, contentDescription = "Favourite", modifier = Modifier.size(24.dp), tint = MaterialTheme.colorScheme.onPrimary)
                    }
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .clickable {},
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Search", modifier = Modifier.size(24.dp), tint = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }
        }

        // Card 2: Touch Target Size and Spacing (WCAG 2.5.8, Level AA)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Touch Target Size & Spacing",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Violation: 16dp wide button — below 24dp minimum. Crowded together with no spacing.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                // Violation: too small and no spacing — overlapping touch areas
                Row {
                    Box(
                        modifier = Modifier
                            .size(width = 16.dp, height = 48.dp)
                            .background(MaterialTheme.colorScheme.error)
                            .clickable {},
                        contentAlignment = Alignment.Center
                    ) {
                        Text("A", color = MaterialTheme.colorScheme.onError, style = MaterialTheme.typography.labelSmall)
                    }
                    Box(
                        modifier = Modifier
                            .size(width = 16.dp, height = 48.dp)
                            .background(MaterialTheme.colorScheme.tertiary)
                            .clickable {},
                        contentAlignment = Alignment.Center
                    ) {
                        Text("B", color = MaterialTheme.colorScheme.onTertiary, style = MaterialTheme.typography.labelSmall)
                    }
                    Box(
                        modifier = Modifier
                            .size(width = 16.dp, height = 48.dp)
                            .background(MaterialTheme.colorScheme.error)
                            .clickable {},
                        contentAlignment = Alignment.Center
                    ) {
                        Text("C", color = MaterialTheme.colorScheme.onError, style = MaterialTheme.typography.labelSmall)
                    }
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Fix: Each target is at least 24dp x 24dp with adequate spacing so touch areas don't overlap.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                // Fix: meets 24dp minimum with proper spacing
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.primary)
                            .clickable {},
                        contentAlignment = Alignment.Center
                    ) {
                        Text("A", color = MaterialTheme.colorScheme.onPrimary)
                    }
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.primary)
                            .clickable {},
                        contentAlignment = Alignment.Center
                    ) {
                        Text("B", color = MaterialTheme.colorScheme.onPrimary)
                    }
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.primary)
                            .clickable {},
                        contentAlignment = Alignment.Center
                    ) {
                        Text("C", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }
        }
    }
}

@Composable
fun CheckboxA11yLabelScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Unlabeled Checkbox",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                var isCheckedViolation by remember { mutableStateOf(false) }
                var isCheckedFix by remember { mutableStateOf(false) }

                Text(
                    text = "Violation: Checkbox without a label",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 1.dp)
                )
                Checkbox(
                    checked = isCheckedViolation,
                    onCheckedChange = { isCheckedViolation = it }
                )

                Text(
                    text = "Fix: Checkbox with a label",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .toggleable(
                            value = isCheckedFix,
                            onValueChange = { isCheckedFix = it },
                            role = Role.Checkbox
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isCheckedFix,
                        onCheckedChange = null // null because the Row handles it
                    )
                    Text("I agree to the terms and conditions")
                }
            }
        }
    }
}

@Composable
fun LabelsAndNamesScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Card 6: Mismatched Label Text
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Mismatched Label Text",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Violation: The accessible name does not match the visible label.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                // Violation: The contentDescription "Send" does not match the visible text "Submit".
                Row(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable(role = Role.Button) {}
                        .semantics { contentDescription = "Send" } // Mismatched accessible name
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("Submit", color = MaterialTheme.colorScheme.onPrimary)
                }

                Row(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable(role = Role.Button) {}
                        .semantics { contentDescription = "Submit" } // Matching accessible name
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("Submit", color = MaterialTheme.colorScheme.onPrimary)
                }

                Text(
                    text = "Violation: Fix - The accessible name matches the visible label.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )

                // Fix: The text within the button serves as the accessible name.
                Button(
                    onClick = {},
                    modifier = Modifier.semantics { contentDescription = "Send" }
                ) {
                    Text("Submit")
                }
            }
        }

        // Card 7: Button Label Capitalisation
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Button Label Capitalisation",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Violation: Button label is in all caps.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                // Violation: "SUBMIT" is in all caps.
                Button(onClick = {}) {
                    Text("SUBMIT")
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Fix: Button label uses sentence case.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                // Fix: "Submit" uses sentence case.
                Button(onClick = {},
                    modifier = Modifier
                        .clickable(role = Role.Button) {}
                ) {
                    Text("Submit")
                }
            }
        }
        // Card 8: Duplicate Accessibility Label
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Duplicate Accessibility Label",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Violation: Two buttons have the same label, making them indistinguishable.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                // Violation: Both buttons are just announced as "Delete".
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(onClick = {}) {
                        Text("Delete")
                    }
                    Button(onClick = {}) {
                        Text("Delete")
                    }
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Fix: Provide a unique accessible name for each button.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                // Fix: Each button has a unique contentDescription.
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(
                        onClick = {},
                        modifier = Modifier.semantics { contentDescription = "Delete item 1" }
                    ) {
                        Text("Delete")
                    }
                    Button(
                        onClick = {},
                        modifier = Modifier.semantics { contentDescription = "Delete item 2" }
                    ) {
                        Text("Delete")
                    }
                }
            }
        }

        // Card 9: Link Text Purpose
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Link Text Purpose",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Violation: Link text is not descriptive.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                // Violation: Generic link text
                Text(
                    text = "Click plss",
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {}
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Fix: Link text is descriptive.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                // Fix: Descriptive link text
                Text(
                    text = "View our Privacy Policy",
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {}
                )
            }
        }
    }
}

@Composable
fun SpecialCharsInLabelScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Card 1: Emoji-only labels (100% special characters)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Emoji / Symbol-Only Labels",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Violation: Button labels that are only emoji or symbols — screen readers announce these inconsistently or not at all.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // SC_I01: Single emoji label — 100% special
                Button(onClick = { }) {
                    Text("⭐")
                }
                Spacer(Modifier.height(4.dp))

                // SC_I04: Musical symbols only — 100% special
                Button(onClick = { }) {
                    Text("♫♪")
                }
                Spacer(Modifier.height(4.dp))

                // SC_I06: Telephone symbol only — 100% special
                Button(onClick = { }) {
                    Text("☎")
                }
                Spacer(Modifier.height(4.dp))

                // SC_I08: Bullet points only — 100% special
                Button(onClick = { }) {
                    Text("●●●")
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Fix: Add descriptive text alongside the emoji so screen readers can announce the purpose.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // SC_V01: Emoji with descriptive text — 10% special → PASS
                Button(onClick = { }) {
                    Text("⭐ Favorites")
                }
                Spacer(Modifier.height(4.dp))

                // SC_V06: Musical note with text — 8.3% special → PASS
                Button(onClick = { }) {
                    Text("♫ Music Player")
                }
                Spacer(Modifier.height(4.dp))

                // Fix for telephone
                Button(onClick = { }) {
                    Text("☎ Call Support")
                }
                Spacer(Modifier.height(4.dp))

                // Fix for bullet points
                Button(onClick = { }) {
                    Text("● View Menu")
                }
            }
        }

        // Card 2: Star rating patterns
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Star Rating Labels",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Violation: Star-only ratings — 100% special characters, screen readers cannot convey meaning.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // SC_E02: Star rating all symbols — 100% special → FAIL
                Button(onClick = { }) {
                    Text("★★★★☆")
                }
                Spacer(Modifier.height(4.dp))

                // SC_I02: Star rating on hint text — FAIL
                var ratingText by remember { mutableStateOf("") }
                TextField(
                    value = ratingText,
                    onValueChange = { ratingText = it },
                    placeholder = { Text("★★★☆☆") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Fix: Include descriptive text with the star count so the label is meaningful.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // SC_V05: Single star + descriptive text — 12.5% → PASS
                Button(onClick = { }) {
                    Text("★ Ratings")
                }
                Spacer(Modifier.height(4.dp))

                // SC_E03: Stars + descriptive text — 22% → PASS
                Button(onClick = { }) {
                    Text("★★ Good (2)")
                }
                Spacer(Modifier.height(4.dp))

                // SC_E05: Two emoji stars + text — 33% → PASS
                Button(onClick = { }) {
                    Text("⭐⭐ Rate")
                }
            }
        }

        // Card 3: Arrows and geometric shapes
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Arrows & Geometric Shapes",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Violation: Labels at or above the 50% special character threshold.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // SC_E04: Single arrow — 100% special → FAIL
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "→"
                    )
                }
                Spacer(Modifier.height(4.dp))

                // SC_I03: Two arrows + "Go" — exactly 50% → FAIL (boundary)
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "→→ Go"
                    )
                }
                Spacer(Modifier.height(4.dp))

                // SC_I07: Geometric shape + one letter — exactly 50% → FAIL
                var switchState by remember { mutableStateOf(false) }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(
                        checked = switchState,
                        onCheckedChange = { switchState = it },
                        modifier = Modifier.semantics { contentDescription = "■ X" }
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("■ X", style = MaterialTheme.typography.bodyMedium)
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Fix: Use descriptive text instead of symbol-heavy labels.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Fix: Descriptive contentDescription — 0% special → PASS
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Next"
                    )
                }
                Spacer(Modifier.height(4.dp))

                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Go forward"
                    )
                }
                Spacer(Modifier.height(4.dp))

                var switchStateFix by remember { mutableStateOf(false) }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(
                        checked = switchStateFix,
                        onCheckedChange = { switchStateFix = it },
                        modifier = Modifier.semantics { contentDescription = "Toggle setting" }
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Toggle setting", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        // Card 4: Dingbats and check/cross marks
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Dingbats & Check/Cross Marks",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Violation: Checkboxes using dingbat characters as their only label.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // SC_I05: Check/cross dingbats only — 100% special → FAIL
                var checkState by remember { mutableStateOf(false) }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = checkState,
                        onCheckedChange = { checkState = it },
                        modifier = Modifier.semantics { contentDescription = "✓✗" }
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("✓✗", style = MaterialTheme.typography.bodyMedium)
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Fix: Use clear text that describes what the checkbox controls.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                var checkStateFix by remember { mutableStateOf(false) }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = checkStateFix,
                        onCheckedChange = { checkStateFix = it },
                        modifier = Modifier.semantics { contentDescription = "Accept terms" }
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Accept terms", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        // Card 5: Common punctuation (not flagged)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Common Punctuation (Allowed)",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "These labels contain common punctuation and currency symbols which are NOT flagged as special characters.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // SC_V03: Hyphen and exclamation — 0% special → PASS
                Button(onClick = { }) {
                    Text("Sign-In!")
                }
                Spacer(Modifier.height(4.dp))

                // SC_V04: Currency and punctuation — 0% special → PASS
                Button(onClick = { }) {
                    Text("Price: \$9.99")
                }
                Spacer(Modifier.height(4.dp))

                // SC_E01: Copyright symbol — 12.5% special → PASS
                Button(onClick = { }) {
                    Text("© 2024 Inc")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TextTouchTargetScreenPreview() {
    QA_Accessibility_AppTheme {
        TextTouchTargetScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun InputFieldLabelScreenPreview() {
    QA_Accessibility_AppTheme {
        InputFieldLabelScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun EditableA11yLabelScreenPreview() {
    QA_Accessibility_AppTheme {
        EditableA11yLabelScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun SwitchA11yLabelScreenPreview() {
    QA_Accessibility_AppTheme {
        SwitchA11yLabelScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun InteractiveElementA11yScreenPreview() {
    QA_Accessibility_AppTheme {
        InteractiveElementA11yScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun CheckboxA11yLabelScreenPreview() {
    QA_Accessibility_AppTheme {
        CheckboxA11yLabelScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun AccessibleImagesScreenPreview() {
    QA_Accessibility_AppTheme {
        AccessibleImagesScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun LabelsAndNamesScreenPreview() {
    QA_Accessibility_AppTheme {
        LabelsAndNamesScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun SpecialCharsInLabelScreenPreview() {
    QA_Accessibility_AppTheme {
        SpecialCharsInLabelScreen()
    }
}

@Composable
fun RedundantRoleInLabelScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Redundant Role in Label (WCAG 4.1.2)",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Violation: Button label contains the word \"button\" — TalkBack announces \"Submit button, Button\".",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Button(onClick = { }) { Text("Submit button") }

                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Violation: Switch contentDescription contains \"switch\".",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                var wifi by remember { mutableStateOf(true) }
                Switch(
                    checked = wifi,
                    onCheckedChange = { wifi = it },
                    modifier = Modifier.semantics { contentDescription = "WiFi switch" }
                )

                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Violation: Checkbox row label contains \"checkbox\".",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                var subscribe by remember { mutableStateOf(false) }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .toggleable(
                            value = subscribe,
                            role = Role.Checkbox,
                            onValueChange = { subscribe = it }
                        )
                ) {
                    Checkbox(checked = subscribe, onCheckedChange = null)
                    Text("Subscribe checkbox", modifier = Modifier.padding(start = 8.dp))
                }

                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Violation: Clickable container labelled \"Save btn\" — \"btn\" is the abbreviated role.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.shapes.medium)
                        .clickable(role = Role.Button) { }
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Save btn", color = MaterialTheme.colorScheme.onPrimaryContainer)
                }

                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Violation (XML / native View): android.widget.Button with text \"Submit button\".",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                AndroidView(
                    factory = { ctx ->
                        android.widget.Button(ctx).apply { text = "Submit button" }
                    }
                )

                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Violation (XML / native View): android.widget.CheckBox labelled \"Subscribe checkbox\".",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                AndroidView(
                    factory = { ctx ->
                        android.widget.CheckBox(ctx).apply { text = "Subscribe checkbox" }
                    }
                )

                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Violation (XML / native View): android.widget.Switch with contentDescription \"WiFi switch\".",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                AndroidView(
                    factory = { ctx ->
                        @Suppress("DEPRECATION")
                        android.widget.Switch(ctx).apply { contentDescription = "WiFi switch" }
                    }
                )

                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Fix: Drop the role word from the label — TalkBack appends the role automatically.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Button(onClick = { }) { Text("Submit") }

                Spacer(Modifier.height(8.dp))
                var wifiFix by remember { mutableStateOf(true) }
                Switch(
                    checked = wifiFix,
                    onCheckedChange = { wifiFix = it },
                    modifier = Modifier.semantics { contentDescription = "WiFi" }
                )

                Spacer(Modifier.height(8.dp))
                var subscribeFix by remember { mutableStateOf(false) }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .toggleable(
                            value = subscribeFix,
                            role = Role.Checkbox,
                            onValueChange = { subscribeFix = it }
                        )
                ) {
                    Checkbox(checked = subscribeFix, onCheckedChange = null)
                    Text("Subscribe", modifier = Modifier.padding(start = 8.dp))
                }

                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Fix (XML / native View): android.widget.Button labelled \"Submit\".",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                AndroidView(
                    factory = { ctx ->
                        android.widget.Button(ctx).apply { text = "Submit" }
                    }
                )
            }
        }
    }
}

@Composable
fun RedundantStateInLabelScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Redundant State in Label (WCAG 4.1.2)",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Violation: Switch labelled \"WiFi off\" while its state is also off — TalkBack says \"WiFi off, Switch, off\".",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                var wifi by remember { mutableStateOf(false) }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(
                        checked = wifi,
                        onCheckedChange = { wifi = it },
                        modifier = Modifier.semantics { contentDescription = "WiFi off" }
                    )
                    Text("WiFi off", modifier = Modifier.padding(start = 8.dp))
                }

                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Violation: Checkbox row labelled \"Notifications checked\" while checked.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                var notify by remember { mutableStateOf(true) }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.toggleable(
                        value = notify,
                        role = Role.Checkbox,
                        onValueChange = { notify = it }
                    )
                ) {
                    Checkbox(checked = notify, onCheckedChange = null)
                    Text("Notifications checked", modifier = Modifier.padding(start = 8.dp))
                }

                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Violation: Selected radio labelled \"Daily plan selected\" — TalkBack already announces \"selected\".",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                var plan by remember { mutableStateOf("daily") }
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.selectable(
                            selected = plan == "daily",
                            role = Role.RadioButton,
                            onClick = { plan = "daily" }
                        )
                    ) {
                        RadioButton(selected = plan == "daily", onClick = null)
                        Text("Daily plan selected", modifier = Modifier.padding(start = 8.dp))
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.selectable(
                            selected = plan == "weekly",
                            role = Role.RadioButton,
                            onClick = { plan = "weekly" }
                        )
                    ) {
                        RadioButton(selected = plan == "weekly", onClick = null)
                        Text("Weekly plan", modifier = Modifier.padding(start = 8.dp))
                    }
                }

                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Violation: Disabled button whose label says \"Save (disabled)\".",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Button(onClick = { }, enabled = false) { Text("Save (disabled)") }

                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Violation (XML / native View): android.widget.Switch labelled \"WiFi off\" while off.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                AndroidView(
                    factory = { ctx ->
                        @Suppress("DEPRECATION")
                        android.widget.Switch(ctx).apply {
                            text = "WiFi off"
                            isChecked = false
                        }
                    }
                )

                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Violation (XML / native View): android.widget.CheckBox labelled \"Notifications checked\" while checked.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                AndroidView(
                    factory = { ctx ->
                        android.widget.CheckBox(ctx).apply {
                            text = "Notifications checked"
                            isChecked = true
                        }
                    }
                )

                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Violation (XML / native View): android.widget.RadioButton labelled \"Daily selected\" while selected.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                AndroidView(
                    factory = { ctx ->
                        android.widget.RadioButton(ctx).apply {
                            text = "Daily selected"
                            isChecked = true
                        }
                    }
                )

                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Violation (XML / native View): disabled android.widget.Button labelled \"Save (disabled)\".",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                AndroidView(
                    factory = { ctx ->
                        android.widget.Button(ctx).apply {
                            text = "Save (disabled)"
                            isEnabled = false
                        }
                    }
                )

                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Fix: Label only the thing — let TalkBack announce state from the widget.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                var wifiFix by remember { mutableStateOf(false) }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Switch(
                        checked = wifiFix,
                        onCheckedChange = { wifiFix = it },
                        modifier = Modifier.semantics { contentDescription = "WiFi" }
                    )
                    Text("WiFi", modifier = Modifier.padding(start = 8.dp))
                }

                Spacer(Modifier.height(8.dp))
                var notifyFix by remember { mutableStateOf(true) }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.toggleable(
                        value = notifyFix,
                        role = Role.Checkbox,
                        onValueChange = { notifyFix = it }
                    )
                ) {
                    Checkbox(checked = notifyFix, onCheckedChange = null)
                    Text("Notifications", modifier = Modifier.padding(start = 8.dp))
                }

                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Fix (XML / native View): android.widget.Switch labelled \"WiFi\" only.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                AndroidView(
                    factory = { ctx ->
                        @Suppress("DEPRECATION")
                        android.widget.Switch(ctx).apply {
                            text = "WiFi"
                            isChecked = false
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun GenericLinkTextScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Generic Link Text (WCAG 2.4.4)",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Violation: TextView whose entire label is \"click here\" and is wrapped in a URLSpan.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                UrlSpanOnlyText("click here", "https://example.com/setup")

                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Violation: TextView labelled \"read more\".",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                UrlSpanOnlyText("read more", "https://example.com/a11y")

                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Violation: TextView labelled \"here\".",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                UrlSpanOnlyText("here", "https://example.com/privacy")

                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Violation: TextView labelled \"learn more\".",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                UrlSpanOnlyText("learn more", "https://example.com/more")

                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Violation: TextView labelled \"tap here\".",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                UrlSpanOnlyText("tap here", "https://example.com/tap")

                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Violation: HTML-sourced link — Html.fromHtml produces a URLSpan-bearing TextView whose label is \"click here\".",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                HtmlLinkText("<a href=\"https://example.com/html\">click here</a>")

                Spacer(Modifier.height(12.dp))
                Text(
                    text = "Violation (Compose-native): Text with LinkAnnotation.Url whose label is \"click here\" — Compose lowers LinkAnnotation to URLSpan in the a11y tree.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                ComposeLink("click here", "https://example.com/compose-setup")

                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Violation (Compose-native): Text with LinkAnnotation.Url whose label is \"read more\".",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                ComposeLink("read more", "https://example.com/compose-article")

                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Fix: Link text describes the destination.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                UrlSpanOnlyText("Read the full accessibility guide", "https://example.com/a11y")

                Spacer(Modifier.height(8.dp))
                UrlSpanOnlyText("Open our privacy policy", "https://example.com/privacy")

                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Fix (Compose-native): descriptive LinkAnnotation.Url label.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                ComposeLink("Read the LambdaTest accessibility guide", "https://example.com/compose-guide")
            }
        }
    }
}

@Composable
private fun ComposeLink(label: String, url: String) {
    val annotated = buildAnnotatedString {
        withLink(LinkAnnotation.Url(url)) {
            withStyle(
                SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append(label)
            }
        }
    }
    Text(text = annotated, fontSize = 16.sp)
}

@Composable
private fun UrlSpanOnlyText(label: String, url: String) {
    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { ctx ->
            TextView(ctx).apply {
                movementMethod = LinkMovementMethod.getInstance()
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            }
        },
        update = { tv ->
            val spannable = SpannableString(label)
            spannable.setSpan(
                URLSpan(url),
                0,
                label.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            tv.text = spannable
        }
    )
}

@Composable
private fun HtmlLinkText(html: String) {
    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { ctx ->
            TextView(ctx).apply {
                movementMethod = LinkMovementMethod.getInstance()
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            }
        },
        update = { tv ->
            tv.text = android.text.Html.fromHtml(html, android.text.Html.FROM_HTML_MODE_LEGACY)
        }
    )
}

@Composable
fun TraversalOrderMismatchScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Traversal Order Mismatch (WCAG 2.4.3)",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Note: The rule only evaluates native Views (XML or AndroidView subtrees) plus cycles. Pure-Compose traversalIndex / isTraversalGroup reorders are silenced because Compose auto-stamps traversal edges, so developer intent can't be separated from framework bookkeeping.",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Text(
                    text = "Violation (XML / native View): accessibilityTraversalBefore reorders three native Buttons — visual Alpha, Beta, Gamma; reading Beta → Gamma → Alpha.",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                AndroidView(
                    modifier = Modifier.fillMaxWidth(),
                    factory = { ctx ->
                        LinearLayout(ctx).apply {
                            orientation = LinearLayout.HORIZONTAL
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            val alphaId = android.view.View.generateViewId()
                            val betaId = android.view.View.generateViewId()
                            val gammaId = android.view.View.generateViewId()
                            val weightedParams = LinearLayout.LayoutParams(
                                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f
                            )
                            val alpha = android.widget.Button(ctx).apply {
                                id = alphaId
                                text = "Alpha"
                                layoutParams = weightedParams
                            }
                            val beta = android.widget.Button(ctx).apply {
                                id = betaId
                                text = "Beta"
                                layoutParams = weightedParams
                            }
                            val gamma = android.widget.Button(ctx).apply {
                                id = gammaId
                                text = "Gamma"
                                layoutParams = weightedParams
                            }
                            beta.accessibilityTraversalBefore = gammaId
                            gamma.accessibilityTraversalBefore = alphaId
                            addView(alpha)
                            addView(beta)
                            addView(gamma)
                        }
                    }
                )

                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Violation (XML / native View): A.traversalBefore=B and B.traversalBefore=A — cycle traps TalkBack.",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                AndroidView(
                    modifier = Modifier.fillMaxWidth(),
                    factory = { ctx ->
                        LinearLayout(ctx).apply {
                            orientation = LinearLayout.HORIZONTAL
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            val aId = android.view.View.generateViewId()
                            val bId = android.view.View.generateViewId()
                            val a = android.widget.Button(ctx).apply {
                                id = aId
                                text = "A"
                                layoutParams = LinearLayout.LayoutParams(
                                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f
                                )
                            }
                            val b = android.widget.Button(ctx).apply {
                                id = bId
                                text = "B"
                                layoutParams = LinearLayout.LayoutParams(
                                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f
                                )
                            }
                            a.accessibilityTraversalBefore = bId
                            b.accessibilityTraversalBefore = aId
                            addView(a)
                            addView(b)
                        }
                    }
                )

                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Fix (XML / native View): three native Buttons with no traversal overrides.",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                AndroidView(
                    modifier = Modifier.fillMaxWidth(),
                    factory = { ctx ->
                        LinearLayout(ctx).apply {
                            orientation = LinearLayout.HORIZONTAL
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            val weightedParams = LinearLayout.LayoutParams(
                                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f
                            )
                            addView(android.widget.Button(ctx).apply {
                                text = "Alpha"; layoutParams = weightedParams
                            })
                            addView(android.widget.Button(ctx).apply {
                                text = "Beta"; layoutParams = weightedParams
                            })
                            addView(android.widget.Button(ctx).apply {
                                text = "Gamma"; layoutParams = weightedParams
                            })
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun DynamicTypeSupportScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Dynamic Type Support (WCAG 1.4.4)",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Violation: TextView size set in PX — ignores the user's font scale.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                AndroidView(
                    modifier = Modifier.fillMaxWidth(),
                    factory = { ctx ->
                        TextView(ctx).apply {
                            text = "Hello in PX (does not scale)"
                            setTextSize(TypedValue.COMPLEX_UNIT_PX, 48f)
                        }
                    }
                )

                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Violation: TextView size set in DIP — also ignores font scale.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                AndroidView(
                    modifier = Modifier.fillMaxWidth(),
                    factory = { ctx ->
                        TextView(ctx).apply {
                            text = "Hello in DIP (does not scale)"
                            setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18f)
                        }
                    }
                )

                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Fix: TextView size in SP — respects the user's font scale setting.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                AndroidView(
                    modifier = Modifier.fillMaxWidth(),
                    factory = { ctx ->
                        TextView(ctx).apply {
                            text = "Hello in SP (scales)"
                            setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                        }
                    }
                )

                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Fix: Compose Text with .sp — scalable by default.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(text = "Hello in Compose sp", fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun ResponsiveContainerScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Responsive Container (WCAG 1.4.10)",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Note: Per the RFC, this rule does NOT fire on pure-Compose fixed-size containers — Compose doesn't expose LayoutParams to the a11y framework. Detection works only on real Android Views (native XML, or AndroidView { ... } embedding a real ViewGroup). BrowserStack also does not flag Compose elements for the same reason.",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Text(
                    text = "Violation (XML / native View): LinearLayout pinned to a fixed pixel width — text clips at higher font scales.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                AndroidView(
                    factory = { ctx ->
                        LinearLayout(ctx).apply {
                            orientation = LinearLayout.VERTICAL
                            layoutParams = ViewGroup.LayoutParams(400, ViewGroup.LayoutParams.WRAP_CONTENT)
                            setBackgroundColor(android.graphics.Color.LTGRAY)
                            addView(TextView(ctx).apply {
                                text = "Long text inside a 400px wide container"
                                setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                            })
                        }
                    }
                )

                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Violation (XML / native View): LinearLayout with fixed pixel height — clips when text grows.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                AndroidView(
                    factory = { ctx ->
                        LinearLayout(ctx).apply {
                            orientation = LinearLayout.VERTICAL
                            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 60)
                            setBackgroundColor(android.graphics.Color.LTGRAY)
                            addView(TextView(ctx).apply {
                                text = "Text that should wrap but cannot — height is fixed"
                                setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                            })
                        }
                    }
                )

                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Fix (XML / native View): LinearLayout with WRAP_CONTENT — reflows when text grows.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                AndroidView(
                    factory = { ctx ->
                        LinearLayout(ctx).apply {
                            orientation = LinearLayout.VERTICAL
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            setBackgroundColor(android.graphics.Color.LTGRAY)
                            addView(TextView(ctx).apply {
                                text = "Long text inside a wrap_content container"
                                setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                            })
                        }
                    }
                )

                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Fix (XML / native View): MATCH_PARENT width — fills available space and reflows.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                AndroidView(
                    factory = { ctx ->
                        LinearLayout(ctx).apply {
                            orientation = LinearLayout.VERTICAL
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            setBackgroundColor(android.graphics.Color.LTGRAY)
                            addView(TextView(ctx).apply {
                                text = "Text in a match_parent / wrap_content container"
                                setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                            })
                        }
                    }
                )

                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Pure-Compose container with fixed dp width — present here for completeness; the rule will NOT flag this because Compose doesn't expose LayoutParams to the a11y framework.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Box(
                    modifier = Modifier
                        .width(150.dp)
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(8.dp)
                ) {
                    Text("Long Compose text inside a 150.dp Box (not flagged)")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RedundantRoleInLabelScreenPreview() {
    QA_Accessibility_AppTheme {
        RedundantRoleInLabelScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun RedundantStateInLabelScreenPreview() {
    QA_Accessibility_AppTheme {
        RedundantStateInLabelScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun GenericLinkTextScreenPreview() {
    QA_Accessibility_AppTheme {
        GenericLinkTextScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun TraversalOrderMismatchScreenPreview() {
    QA_Accessibility_AppTheme {
        TraversalOrderMismatchScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun DynamicTypeSupportScreenPreview() {
    QA_Accessibility_AppTheme {
        DynamicTypeSupportScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun ResponsiveContainerScreenPreview() {
    QA_Accessibility_AppTheme {
        ResponsiveContainerScreen()
    }
}

// =====================================================================
// IMAGE-IN-TEXT (WCAG 1.4.5) — images of text + exception pass cases.
// All images rendered via native ImageView (AndroidView) so they remain
// in the XML/View tree and are scannable.
// =====================================================================

@Composable
fun ImageInTextScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Box(modifier = modifier.fillMaxSize()) {
        ImageInTextContent(scrollState = scrollState)
        // Floating up/down arrows to step-scroll the screen content
        ScrollArrows(
            scrollState = scrollState,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }
}

@Composable
private fun ImageInTextContent(
    scrollState: ScrollState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Images of Text — image-in-text",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "WCAG 1.4.5 (AA), Severity Serious. Fails when an image renders readable text in its pixels AND no exception applies. A contentDescription does NOT satisfy this rule — the violation is the embedding itself.",
            style = MaterialTheme.typography.bodySmall
        )

        // ---------- VIOLATION CARDS ----------

        // V-01 .. V-55: images of text — two images per row.
        // Triple = (title, drawable, contentDescription)
        val imageOfTextViolations: List<Triple<String, Int, String?>> = listOf(
            // Images of text + contentDescription variants
            Triple("V-01: No contentDescription", R.drawable.text_image_1, null),
            Triple("V-02: Matching contentDescription", R.drawable.text_image_2, "Transparency"),
            Triple("V-03: Mismatched contentDescription", R.drawable.text_image_4, "Watch on a strap"),
            // Multilingual text-in-image samples
            Triple("V-04: Arabic", R.drawable.arabic_text, null),
            Triple("V-05: Argentina / Spanish", R.drawable.argentina_text, null),
            Triple("V-06: Chinese (CJK)", R.drawable.chinese_text, null),
            Triple("V-07: German", R.drawable.german_text, null),
            Triple("V-08: Hindi (Devanagari)", R.drawable.hindi_text, null),
            Triple("V-09: Hindi + English (mixed scripts)", R.drawable.hindi_english_text, null),
            Triple("V-10: Japanese (CJK)", R.drawable.japanese_text, null),
            Triple("V-11: Korean (CJK)", R.drawable.korean_text, null),
            Triple("V-12: Polish", R.drawable.polish_text, null),
            Triple("V-13: Russian (Cyrillic)", R.drawable.russian_text, null),
            Triple("V-14: Spanish (Latin)", R.drawable.spanish_text, null),
            Triple("V-15: Spanish (second sample)", R.drawable.spanish2_text, null),
            Triple("V-16: Urdu (Arabic script)", R.drawable.urdu_text, null),
            // Banner / CTA-style images of text
            Triple("V-17: Sale Banner", R.drawable.sale_banner, null),
            Triple("V-18: Buy Now Button", R.drawable.buy_now_button, null),
            Triple("V-19: Welcome Hero", R.drawable.welcome_hero, null),
            Triple("V-20: Price Tag", R.drawable.price_tag, null),
            Triple("V-21: New Badge", R.drawable.new_badge, null),
            Triple("V-22: Section Header", R.drawable.section_header, null),
            Triple("V-23: Subscribe Cta", R.drawable.subscribe_cta, null),
            Triple("V-24: Footer Text", R.drawable.footer_text, null),
            Triple("V-25: Notification Banner", R.drawable.notification_banner, null),
            Triple("V-26: Download Banner", R.drawable.download_banner, null),
            Triple("V-27: Login Button", R.drawable.login_button, null),
            Triple("V-28: Offer Card", R.drawable.offer_card, null),
            Triple("V-29: Tab Label", R.drawable.tab_label, null),
            Triple("V-30: Error Message", R.drawable.error_message, null),
            Triple("V-31: Quote Card", R.drawable.quote_card, null),
            Triple("V-32: Signup Button", R.drawable.signup_button, null),
            Triple("V-33: Flash Deal", R.drawable.flash_deal, null),
            Triple("V-34: Coupon Code", R.drawable.coupon_code, null),
            Triple("V-35: Free Shipping", R.drawable.free_shipping, null),
            Triple("V-36: Contact Us", R.drawable.contact_us, null),
            Triple("V-37: Rate Us", R.drawable.rate_us, null),
            Triple("V-38: Out Of Stock", R.drawable.out_of_stock, null),
            Triple("V-39: Membership Banner", R.drawable.membership_banner, null),
            Triple("V-40: Search Placeholder", R.drawable.search_placeholder, null),
            Triple("V-41: Cashback Offer", R.drawable.cashback_offer, null),
            Triple("V-42: Cookie Consent", R.drawable.cookie_consent, null),
            Triple("V-43: Feature Highlight", R.drawable.feature_highlight, null),
            Triple("V-44: Referral Banner", R.drawable.referral_banner, null),
            Triple("V-45: Warranty Badge", R.drawable.warranty_badge, null),
            Triple("V-46: Coming Soon", R.drawable.coming_soon, null),
            // E-commerce product images with readable text in pixels
            Triple("V-47: Product Packaging (Garbage Bags)", R.drawable.garbage_bags_pack, null),
            Triple("V-48: Smartwatch Screen Text", R.drawable.smartwatch_face, null),
            Triple("V-49: Game Box Packaging", R.drawable.sentence_search_game, null),
            Triple("V-50: Book Cover — Indian Millennials", R.drawable.book_indian_millennials, null),
            Triple("V-51: Book Cover — The Tubewell House", R.drawable.book_tubewell_house, null),
            Triple("V-52: Book Cover — Ikigai", R.drawable.book_ikigai, null),
            Triple("V-53: Book Cover — The Final Experiment", R.drawable.book_final_experiment, null),
            Triple("V-54: Cleaner Bottle Label", R.drawable.surface_cleaner_can, null),
            Triple("V-55: Product Infographic (Every Space)", R.drawable.every_space_infographic, null)
        )
        imageOfTextViolations.chunked(2).forEach { rowItems ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowItems.forEach { (title, resId, contentDesc) ->
                        Column(modifier = Modifier.weight(1f)) {
                            Text(title,
                                style = MaterialTheme.typography.titleSmall)
                            NativeImage(
                                resId = resId,
                                contentDescription = contentDesc,
                                modifier = Modifier.fillMaxWidth().height(140.dp)
                            )
                        }
                    }
                }
            }
        }

        // ---------- PASS CARDS ----------

        // P-01: photograph without text
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("P-01: Photograph Without Rendered Text",
                    style = MaterialTheme.typography.titleMedium)
                NativeImage(
                    resId = R.drawable.wooden_dice,
                    contentDescription = "Four wooden dice on a dark surface",
                    modifier = Modifier.fillMaxWidth().height(140.dp)
                )
            }
        }

        // P-02: brand logos / wordmarks
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("P-02: Brand Logos / Wordmarks (Exception)",
                    style = MaterialTheme.typography.titleMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NativeImage(resId = R.drawable.chanel, contentDescription = null, modifier = Modifier.size(60.dp))
                    NativeImage(resId = R.drawable.nike, contentDescription = null, modifier = Modifier.size(60.dp))
                    NativeImage(resId = R.drawable.pepsi, contentDescription = null, modifier = Modifier.size(60.dp))
                    NativeImage(resId = R.drawable.testmu, contentDescription = null, modifier = Modifier.size(60.dp))
                }
            }
        }

        // P-03: icon glyph exception
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("P-03: Icon Glyphs (Exception)",
                    style = MaterialTheme.typography.titleMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NativeImage(resId = R.drawable.bin, contentDescription = null, modifier = Modifier.size(60.dp))
                    NativeImage(resId = R.drawable.camera, contentDescription = null, modifier = Modifier.size(60.dp))
                    NativeImage(resId = R.drawable.folder, contentDescription = null, modifier = Modifier.size(60.dp))
                }
            }
        }

        // P-04: plain shape — real Image assets
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("P-04: Plain Shape (Exception)",
                    style = MaterialTheme.typography.titleMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    NativeImage(resId = R.drawable.green_swatch, contentDescription = null, modifier = Modifier.weight(1f).height(60.dp))
                    NativeImage(resId = R.drawable.purple_swatch, contentDescription = null, modifier = Modifier.weight(1f).height(60.dp))
                    NativeImage(resId = R.drawable.yellow_swatch, contentDescription = null, modifier = Modifier.weight(1f).height(60.dp))
                }
            }
        }

        // P-05: image of text hidden — silently dropped
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("P-05: Image of Text — Hidden (Silently Dropped)",
                    style = MaterialTheme.typography.titleMedium)
                // Native ImageView so the View remains in the XML/View tree for scanning,
                // but importantForAccessibility=NO marks it hidden -> rule silently drops it.
                NativeImage(
                    resId = R.drawable.learn_english,
                    contentDescription = null,
                    hiddenFromAccessibility = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
            }
        }

        // P-06: chart / data viz exception
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("P-06: Chart / Data Visualisation (Exception)",
                    style = MaterialTheme.typography.titleMedium)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        NativeImage(resId = R.drawable.bar_chart, contentDescription = null, modifier = Modifier.weight(1f).height(100.dp))
                        NativeImage(resId = R.drawable.line_chart, contentDescription = null, modifier = Modifier.weight(1f).height(100.dp))
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        NativeImage(resId = R.drawable.pie_chart, contentDescription = null, modifier = Modifier.weight(1f).height(100.dp))
                        NativeImage(resId = R.drawable.point_chart, contentDescription = null, modifier = Modifier.weight(1f).height(100.dp))
                    }
                }
            }
        }

        // P-07: scanned document / receipt exception
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("P-07: Scanned Document / Receipt (Exception)",
                    style = MaterialTheme.typography.titleMedium)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        NativeImage(resId = R.drawable.receipt_1, contentDescription = null, modifier = Modifier.weight(1f).height(140.dp))
                        NativeImage(resId = R.drawable.receipt_2, contentDescription = null, modifier = Modifier.weight(1f).height(140.dp))
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        NativeImage(resId = R.drawable.scanned_doc, contentDescription = null, modifier = Modifier.weight(1f).height(140.dp))
                        NativeImage(resId = R.drawable.scanned_doc_2, contentDescription = null, modifier = Modifier.weight(1f).height(140.dp))
                    }
                }
            }
        }

        // P-08: decorative artwork with incidental text
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("P-08: Decorative Artwork with Incidental Text (Exception)",
                    style = MaterialTheme.typography.titleMedium)
                NativeImage(
                    resId = R.drawable.decorative,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().height(280.dp)
                )
            }
        }

        // P-09 .. P-28: single full-width exception cards.
        // Triple = (title, drawable, contentDescription)
        val passCards: List<Triple<String, Int, String?>> = listOf(
            Triple("P-09: Brand Logo (Brand Logo Exception)", R.drawable.brand_logo, null),
            Triple("P-10: Gradient Bg (Plain Shape Exception)", R.drawable.gradient_bg, null),
            Triple("P-11: Placeholder (Plain Shape Exception)", R.drawable.placeholder, null),
            Triple("P-12: Wall Art (Decorative Artwork Exception)", R.drawable.wall_art, null),
            Triple("P-13: Bar Chart (Chart / Data Viz Exception)", R.drawable.bar_chart_real, null),
            Triple("P-14: Divider (Plain Shape Exception)", R.drawable.divider, null),
            Triple("P-15: Typography Poster (Decorative Artwork Exception)", R.drawable.typography_poster, null),
            Triple("P-16: Scanned Receipt (Scanned Document Exception)", R.drawable.scanned_receipt, null),
            Triple("P-17: Product With Brand (Brand Logo Exception)", R.drawable.product_with_brand, null),
            Triple("P-18: Pie Chart (Chart / Data Viz Exception)", R.drawable.pie_chart_real, null),
            Triple("P-19: App Icon Logo (Brand Logo Exception)", R.drawable.app_icon_logo, null),
            Triple("P-20: Love Sticker (Decorative Artwork Exception)", R.drawable.love_sticker, null),
            Triple("P-21: Map Screenshot (Chart / Data Viz Exception)", R.drawable.map_screenshot, null),
            Triple("P-22: Loading Skeleton (Plain Shape Exception)", R.drawable.loading_skeleton, null),
            Triple("P-23: Greeting Card (Decorative Artwork Exception)", R.drawable.greeting_card, null),
            // E-commerce pass cases (product photos / decorative artwork)
            Triple("P-24: Watch Product Photo (Brand on Product Exception)", R.drawable.gshock_watch, null),
            Triple("P-25: Brush Product Photo (Brand on Product Exception)", R.drawable.paddle_brush, null),
            Triple("P-26: Mona Lisa Pop Art (Decorative Artwork Exception)", R.drawable.mona_lisa_art, null),
            Triple("P-27: Framed Wall Art (Decorative Artwork Exception)", R.drawable.stay_positive_frame, null),
            Triple("P-28: Showpiece (Decorative Artwork Exception)", R.drawable.yoga_se_hoga, null)
        )
        passCards.forEach { (title, resId, contentDesc) ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(title, style = MaterialTheme.typography.titleMedium)
                    NativeImage(
                        resId = resId,
                        contentDescription = contentDesc,
                        modifier = Modifier.fillMaxWidth().height(140.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ImageInTextScreenPreview() {
    QA_Accessibility_AppTheme {
        ImageInTextScreen()
    }
}

// Floating up/down arrow buttons that step-scroll a ScrollState-backed screen.
@Composable
private fun ScrollArrows(
    scrollState: ScrollState,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    // Scroll by roughly one screen-height per tap
    val stepPx = with(LocalDensity.current) { 600.dp.roundToPx() }
    // 56dp FABs (>= 48dp touch target) with 16dp spacing so adjacent
    // targets don't trigger insufficient-target-spacing findings.
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FloatingActionButton(
            onClick = {
                scope.launch {
                    scrollState.animateScrollTo((scrollState.value - stepPx).coerceAtLeast(0))
                }
            }
        ) {
            Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "Scroll up")
        }
        FloatingActionButton(
            onClick = {
                scope.launch {
                    scrollState.animateScrollTo((scrollState.value + stepPx).coerceAtMost(scrollState.maxValue))
                }
            }
        ) {
            Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "Scroll down")
        }
    }
}

@Composable
private fun NativeImage(
    @androidx.annotation.DrawableRes resId: Int,
    contentDescription: String? = null,
    hiddenFromAccessibility: Boolean = false,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { ctx ->
            android.widget.ImageView(ctx).apply {
                adjustViewBounds = true
                scaleType = android.widget.ImageView.ScaleType.FIT_CENTER
            }
        },
        update = { iv ->
            iv.setImageResource(resId)
            iv.contentDescription = contentDescription
            iv.importantForAccessibility = if (hiddenFromAccessibility) {
                android.view.View.IMPORTANT_FOR_ACCESSIBILITY_NO
            } else {
                android.view.View.IMPORTANT_FOR_ACCESSIBILITY_AUTO
            }
        },
        modifier = modifier
    )
}

// =====================================================================
// MEANINGFUL READING ORDER (WCAG 1.3.2 Meaningful Sequence)
// =====================================================================
// Android's uiautomator dump auto-sorts elements by visual position, so
// traversalIndex / accessibilityTraversalBefore tricks do NOT surface as
// violations. The ONLY way to create a real reading-order violation is to
// make the visual top-to-bottom layout itself semantically wrong
// (e.g. price above product name, input above its label).
// Each card is a plain Column (no mergeDescendants) so every element stays
// individually visible in the a11y tree.
// =====================================================================

@Composable
fun MeaningfulReadingOrderScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Box(modifier = modifier.fillMaxSize()) {
        MeaningfulReadingOrderContent(scrollState = scrollState)
        ScrollArrows(
            scrollState = scrollState,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }
}

@Composable
private fun MroCard(title: String, subtitle: String, content: @Composable () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(subtitle, style = MaterialTheme.typography.bodySmall)
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) { content() }
        }
    }
}

@Composable
private fun MeaningfulReadingOrderContent(
    scrollState: ScrollState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Meaningful Reading Order — meaningful-sequence",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "WCAG 1.3.2 (A). On Android the XML dump follows visual position, so a violation must be a visually wrong top-to-bottom order (e.g. price above name, input above label). Each card below renders the layout in the stated order.",
            style = MaterialTheme.typography.bodySmall
        )

        // ---------- VIOLATIONS (visual order semantically wrong) ----------

        // V-01: Price before name
        MroCard("V-01: Price Before Name", "Price rendered ABOVE the product name.") {
            Text("$79.99", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Wireless Headphones", style = MaterialTheme.typography.titleSmall)
        }

        // V-02: Input before label
        MroCard("V-02: Input Before Label", "Text field rendered ABOVE its label.") {
            TextField(value = "", onValueChange = {}, placeholder = { Text("Enter here...") },
                modifier = Modifier.fillMaxWidth())
            Text("Email")
        }

        // V-03: Controls before song
        MroCard("V-03: Controls Before Song", "Playback controls rendered ABOVE the song title/artist.") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {}) { Text("Prev") }
                Button(onClick = {}) { Text("Play") }
                Button(onClick = {}) { Text("Next") }
            }
            Text("Bohemian Rhapsody", style = MaterialTheme.typography.titleMedium)
            Text("Queen", style = MaterialTheme.typography.bodyMedium)
        }

        // V-04: Amount before merchant
        MroCard("V-04: Amount Before Merchant", "Transaction amount rendered ABOVE the merchant name.") {
            Text("-$45.99", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("Starbucks", style = MaterialTheme.typography.bodyLarge)
        }

        // V-05: Error before field
        MroCard("V-05: Error Before Field", "Error message rendered ABOVE the field it refers to.") {
            Text("Error: Please enter a valid email address", color = Color.Red,
                style = MaterialTheme.typography.bodyMedium)
            Text("Email")
            TextField(value = "", onValueChange = {}, placeholder = { Text("you@example.com") },
                modifier = Modifier.fillMaxWidth())
        }

        // V-06: Steps out of order
        MroCard("V-06: Steps Out Of Order", "Numbered steps rendered 3 → 1 → 2.") {
            Text("Step 3: Add water and stir")
            Text("Step 1: Boil the kettle")
            Text("Step 2: Pour into the mug")
        }

        // V-07: Action before content
        MroCard("V-07: Action Before Content", "Primary action rendered ABOVE the content it acts on.") {
            Button(onClick = {}) { Text("Book Now") }
            Text("Grand Plaza Hotel", style = MaterialTheme.typography.titleMedium)
            Text("Downtown • 4.6 ★ • Free cancellation", style = MaterialTheme.typography.bodySmall)
        }

        // V-08: Engagement before post
        MroCard("V-08: Engagement Before Post", "Like/comment counts rendered ABOVE the post text.") {
            Text("2.4K likes • 312 comments", fontWeight = FontWeight.Bold)
            Text("Just had the best coffee of my life at this tiny place downtown ☕")
        }

        // ---------- PASSES (natural top-to-bottom order) ----------

        // P-01: Login form
        MroCard("P-01: Login Form (Pass)", "Label → input pairs in natural order, action last.") {
            Text("Email")
            TextField(value = "", onValueChange = {}, placeholder = { Text("you@example.com") },
                modifier = Modifier.fillMaxWidth())
            Text("Password")
            TextField(value = "", onValueChange = {}, placeholder = { Text("••••••••") },
                modifier = Modifier.fillMaxWidth())
            Button(onClick = {}) { Text("Sign In") }
        }

        // P-02: News article
        MroCard("P-02: News Article (Pass)", "Headline → byline → body in reading order.") {
            Text("City Council Approves New Transit Plan", style = MaterialTheme.typography.titleMedium)
            Text("By Jane Doe • July 9, 2026", style = MaterialTheme.typography.bodySmall)
            Text("The council voted 7-2 on Tuesday to fund the expansion of the light-rail network over the next five years.")
        }

        // P-03: Product card (correct)
        MroCard("P-03: Product Card (Pass)", "Name → price → action in natural order.") {
            Text("Wireless Headphones", style = MaterialTheme.typography.titleMedium)
            Text("$79.99", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Button(onClick = {}) { Text("Add to Cart") }
        }

        // P-04: Settings page
        MroCard("P-04: Settings Page (Pass)", "Section title first, then each labelled control.") {
            Text("Settings", style = MaterialTheme.typography.titleMedium)
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Wi-Fi", modifier = Modifier.weight(1f))
                Switch(checked = true, onCheckedChange = {})
            }
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Bluetooth", modifier = Modifier.weight(1f))
                Switch(checked = false, onCheckedChange = {})
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MeaningfulReadingOrderScreenPreview() {
    QA_Accessibility_AppTheme {
        MeaningfulReadingOrderScreen()
    }
}
