package com.example.qa_accessibility_app

import android.R.attr.text
import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
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
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.zIndex
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
    // Retains each destination's saveable state (incl. scroll position) so
    // navigating away and back restores where the user was, not the top.
    val saveableStateHolder = rememberSaveableStateHolder()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        if (currentDestination == null) {
            saveableStateHolder.SaveableStateProvider("home") {
            val homeScrollState = rememberScrollState()
            Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(homeScrollState)
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
            ScrollArrows(scrollState = homeScrollState)
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
                // Content for the current destination, wrapped so each screen's
                // scroll position survives navigating away and back.
                saveableStateHolder.SaveableStateProvider(currentDestination ?: Unit) {
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
                    AppDestinations.OVERLAPPING_INTERACTIVE -> OverlappingInteractiveElementsScreen()
                    AppDestinations.TWO_DIMENSIONAL_SCROLLING -> TwoDimensionalScrollingScreen()
                    AppDestinations.NON_ACCESSIBLE_INTERACTION -> NonAccessibleInteractionScreen()
                    AppDestinations.ORIENTATION_LOCK -> OrientationLockScreen()
                    AppDestinations.MINIMUM_TEXT_SIZE -> MinimumTextSizeScreen()
                    AppDestinations.INVALID_RANGE_VALUES -> InvalidRangeValuesScreen()
                    AppDestinations.UNIQUE_OPTION_NAMES -> UniqueOptionNamesScreen()
                    AppDestinations.LABEL_AT_FRONT -> LabelAtFrontScreen()
                    AppDestinations.KEYBOARD_FOCUS -> KeyboardFocusScreen()
                    AppDestinations.LABEL_IN_NAME -> LabelInNameScreen()
                    AppDestinations.TEXT_SPACING -> TextSpacingScreen()
                    null -> {}
                }
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
    OVERLAPPING_INTERACTIVE("Overlapping Elements", Icons.Default.Build),
    TWO_DIMENSIONAL_SCROLLING("Two-Dimensional Scrolling", Icons.Default.Build),
    NON_ACCESSIBLE_INTERACTION("Non-accessible Interaction", Icons.Default.Build),
    ORIENTATION_LOCK("Orientation Lock", Icons.Default.Build),
    MINIMUM_TEXT_SIZE("Minimum Text Size", Icons.Default.Build),
    INVALID_RANGE_VALUES("Invalid Range Values", Icons.Default.Build),
    UNIQUE_OPTION_NAMES("Unique Option Names", Icons.Default.Build),
    LABEL_AT_FRONT("Misplaced Field Label", Icons.Default.Build),
    KEYBOARD_FOCUS("Non-Focusable Interactive Element", Icons.Default.Build),
    LABEL_IN_NAME("Mismatched Label Text", Icons.Default.Build),
    TEXT_SPACING("Text Spacing", Icons.Default.Build),
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
    Column(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            ImageInTextContent(scrollState = scrollState)
        }
        // Up/down arrows in a fixed bottom bar, below the scroll content
        ScrollArrows(scrollState = scrollState)
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

// Up/down step-scroll buttons rendered as a fixed bottom bar. Kept OUT of the
// scroll content (the caller places it below the scrollable area, not overlaid)
// so the clickable FABs never overlap interactive elements — which would
// otherwise legitimately trip the Overlapping Interactive Elements rule.
// 56dp FABs (>= 48dp touch target) with 16dp spacing so adjacent targets don't
// trip the insufficient-target-spacing rule either.
@Composable
private fun ScrollArrows(
    scrollState: ScrollState,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    // Scroll by roughly one screen-height per tap
    val stepPx = with(LocalDensity.current) { 600.dp.roundToPx() }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
        verticalAlignment = Alignment.CenterVertically
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
    Column(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            MeaningfulReadingOrderContent(scrollState = scrollState)
        }
        ScrollArrows(scrollState = scrollState)
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

// =====================================================================
// TE-20569 helpers + four new rule screens
// =====================================================================

@Composable
private fun RuleCard(title: String, subtitle: String, content: @Composable () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(subtitle, style = MaterialTheme.typography.bodySmall)
            content()
        }
    }
}

// ---------------------------------------------------------------------
// OVERLAPPING INTERACTIVE ELEMENTS (WCAG 2.5.5, serious)
// FAIL: two interactive (clickable/long-clickable/checkable) visible
// elements whose bounds intersect by > 10% of the smaller element's area.
// PASS: spaced, ancestor↔descendant, edge-adjacent, or overlap where only
// one element is interactive.
// ---------------------------------------------------------------------
@Composable
fun OverlappingInteractiveElementsScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Column(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.weight(1f).fillMaxWidth().verticalScroll(scrollState).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Overlapping Interactive Elements", style = MaterialTheme.typography.headlineSmall)
            Text("WCAG 2.5.5 (AAA), Serious. Two interactive elements whose touch targets overlap by more than 10% of the smaller area both fail.",
                style = MaterialTheme.typography.bodySmall)

            // V-01: two clickable buttons overlapping
            RuleCard("V-01: Two Overlapping Buttons", "Both are clickable and their bounds overlap heavily.") {
                Box(modifier = Modifier.fillMaxWidth().height(96.dp)) {
                    Button(onClick = {}, modifier = Modifier.align(Alignment.TopStart)) { Text("Confirm") }
                    Button(onClick = {}, modifier = Modifier.align(Alignment.TopStart).offset(x = 70.dp, y = 22.dp).zIndex(1f)) { Text("Cancel") }
                }
            }

            // V-02: icon button overlapping a text button
            RuleCard("V-02: Icon Over Button", "A clickable icon button sits on top of a clickable button.") {
                Box(modifier = Modifier.fillMaxWidth().height(72.dp)) {
                    Button(onClick = {}, modifier = Modifier.align(Alignment.CenterStart)) { Text("Add to Cart") }
                    IconButton(onClick = {}, modifier = Modifier.align(Alignment.CenterStart).offset(x = 40.dp, y = 8.dp).zIndex(1f)) {
                        Icon(Icons.Default.Favorite, contentDescription = "Add to favourites")
                    }
                }
            }

            // V-03: two overlapping checkboxes (checkable candidates)
            RuleCard("V-03: Overlapping Checkboxes", "Two checkable elements whose touch targets overlap.") {
                Box(modifier = Modifier.fillMaxWidth().height(64.dp)) {
                    var a by remember { mutableStateOf(true) }
                    var b by remember { mutableStateOf(false) }
                    Checkbox(checked = a, onCheckedChange = { a = it }, modifier = Modifier.align(Alignment.CenterStart))
                    Checkbox(checked = b, onCheckedChange = { b = it }, modifier = Modifier.align(Alignment.CenterStart).offset(x = 14.dp, y = 6.dp).zIndex(1f))
                }
            }

            // V-04: two overlapping clickable cards (sibling clickables)
            RuleCard("V-04: Overlapping Clickable Cards", "Two sibling clickable cards whose bounds overlap.") {
                Box(modifier = Modifier.fillMaxWidth().height(120.dp)) {
                    Box(modifier = Modifier.align(Alignment.TopStart).size(160.dp, 90.dp).clickable {}.background(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.shapes.medium), contentAlignment = Alignment.Center) {
                        Text("Card A")
                    }
                    Box(modifier = Modifier.align(Alignment.TopStart).offset(x = 110.dp, y = 24.dp).size(160.dp, 90.dp).clickable {}.background(MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.shapes.medium).zIndex(1f), contentAlignment = Alignment.Center) {
                        Text("Card B")
                    }
                }
            }

            // V-05: switch overlapping a button (checkable + clickable)
            RuleCard("V-05: Switch Over Button", "A checkable switch overlaps a clickable button.") {
                Box(modifier = Modifier.fillMaxWidth().height(64.dp)) {
                    Button(onClick = {}, modifier = Modifier.align(Alignment.CenterStart)) { Text("Save changes") }
                    var on by remember { mutableStateOf(true) }
                    Switch(checked = on, onCheckedChange = { on = it }, modifier = Modifier.align(Alignment.CenterStart).offset(x = 120.dp).zIndex(1f))
                }
            }

            // V-06: two overlapping icon buttons
            RuleCard("V-06: Overlapping Icon Buttons", "Two clickable icon buttons overlapping each other.") {
                Box(modifier = Modifier.fillMaxWidth().height(64.dp)) {
                    IconButton(onClick = {}, modifier = Modifier.align(Alignment.CenterStart)) {
                        Icon(Icons.Default.Favorite, contentDescription = "Like")
                    }
                    IconButton(onClick = {}, modifier = Modifier.align(Alignment.CenterStart).offset(x = 16.dp, y = 6.dp).zIndex(1f)) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            }

            // P-01: spaced buttons
            RuleCard("P-01: Spaced Buttons (Pass)", "Two clickable buttons with clear space between them.") {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    Button(onClick = {}) { Text("Back") }
                    Button(onClick = {}) { Text("Next") }
                }
            }

            // P-02: clickable child inside clickable container (ancestor exempt)
            RuleCard("P-02: Clickable Child In Container (Pass)", "Container is clickable and encloses its own clickable child — ancestor/descendant pairs are exempt.") {
                Box(modifier = Modifier.fillMaxWidth().clickable {}.background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.medium).padding(16.dp)) {
                    Button(onClick = {}, modifier = Modifier.align(Alignment.Center)) { Text("Inner Button") }
                }
            }

            // P-03: edge-adjacent buttons (zero-area intersection)
            RuleCard("P-03: Edge-Adjacent Buttons (Pass)", "Two clickable buttons that touch but do not overlap.") {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(0.dp)) {
                    Button(onClick = {}, modifier = Modifier.weight(1f)) { Text("Left") }
                    Button(onClick = {}, modifier = Modifier.weight(1f)) { Text("Right") }
                }
            }

            // P-04: decorative (non-interactive) badge over a button
            RuleCard("P-04: Decorative Badge Over Button (Pass)", "Only the button is interactive; the overlapping badge is a non-interactive decoration.") {
                Box(modifier = Modifier.fillMaxWidth().height(64.dp)) {
                    Button(onClick = {}, modifier = Modifier.align(Alignment.CenterStart)) { Text("Subscribe") }
                    Box(modifier = Modifier.align(Alignment.CenterStart).offset(x = 108.dp, y = (-8).dp).size(36.dp).background(Color.Red, CircleShape).zIndex(1f), contentAlignment = Alignment.Center) {
                        Text("NEW", color = Color.White, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }

            // L-01: full-edge overlap — a known limitation, NOT detected.
            RuleCard("L-01: Full-Edge Overlap (Known Limitation — Not Detected)", "The top button covers the lower one flush along its full right edge (x-only offset). Android reports occlusion-clipped bounds via getBoundsInScreen(), so the lower element's rect shrinks to its uncovered strip and the pair reads as edge-adjacent — the rule cannot detect this. Contrast with the diagonal overlaps above, which DO fire.") {
                Box(modifier = Modifier.fillMaxWidth().height(56.dp)) {
                    Button(onClick = {}, modifier = Modifier.align(Alignment.CenterStart)) { Text("Base") }
                    Button(onClick = {}, modifier = Modifier.align(Alignment.CenterStart).offset(x = 64.dp).zIndex(1f)) { Text("Overlay") }
                }
            }

            // XML variant — same rule rendered from a native XML layout so you can
            // compare Compose vs traditional Views in the scan.
            RuleCard("XML Variant (native views)", "Inflated from res/layout/overlapping_elements_xml.xml — native Buttons with solid bounds.") {
                AndroidView(
                    factory = { ctx -> android.view.View.inflate(ctx, R.layout.overlapping_elements_xml, null) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        ScrollArrows(scrollState = scrollState)
    }
}

@Preview(showBackground = true)
@Composable
fun OverlappingInteractiveElementsScreenPreview() {
    QA_Accessibility_AppTheme { OverlappingInteractiveElementsScreen() }
}

// ---------------------------------------------------------------------
// TWO-DIMENSIONAL SCROLLING (WCAG 1.4.10, serious)
// FAIL: a single node that can scroll BOTH horizontally and vertically.
// PASS: one-directional scrollers, including a horizontal carousel nested
// inside a vertical feed (separate one-directional nodes).
// ---------------------------------------------------------------------
@Composable
fun TwoDimensionalScrollingScreen(modifier: Modifier = Modifier) {
    val outerScroll = rememberScrollState()
    Column(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.weight(1f).fillMaxWidth().verticalScroll(outerScroll).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Two-Dimensional Scrolling", style = MaterialTheme.typography.headlineSmall)
            Text("WCAG 1.4.10 (AA), Serious. A single container that scrolls both horizontally and vertically fails.",
                style = MaterialTheme.typography.bodySmall)

            // V-01: one box scrollable on BOTH axes
            RuleCard("V-01: Scrolls Both Axes", "This box has horizontalScroll AND verticalScroll on the same node.") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .horizontalScroll(rememberScrollState())
                        .verticalScroll(rememberScrollState())
                ) {
                    Column {
                        repeat(12) { r ->
                            Row {
                                repeat(12) { c ->
                                    Box(modifier = Modifier.size(80.dp).padding(2.dp).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) {
                                        Text("$r,$c")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // V-02: wide + tall data table scrollable both axes
            RuleCard("V-02: Data Table Both Axes", "A table wider and taller than the viewport, scrollable on both axes.") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .horizontalScroll(rememberScrollState())
                        .verticalScroll(rememberScrollState())
                ) {
                    Column {
                        repeat(15) { r ->
                            Row {
                                repeat(8) { c ->
                                    Box(modifier = Modifier.size(110.dp, 40.dp).padding(1.dp).background(MaterialTheme.colorScheme.surfaceVariant), contentAlignment = Alignment.Center) {
                                        Text("R${r + 1}C${c + 1}")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // P-01: vertical-only scroller
            RuleCard("P-01: Vertical Only (Pass)", "Scrolls on a single axis.") {
                Column(modifier = Modifier.fillMaxWidth().height(140.dp).background(MaterialTheme.colorScheme.surfaceVariant).verticalScroll(rememberScrollState()).padding(8.dp)) {
                    repeat(20) { Text("Row ${it + 1}", modifier = Modifier.padding(vertical = 6.dp)) }
                }
            }

            // P-02: horizontal carousel nested inside a vertical feed
            RuleCard("P-02: Carousel In Feed (Pass)", "A horizontal carousel nested inside a vertical feed — two separate one-directional nodes.") {
                Column(modifier = Modifier.fillMaxWidth().height(200.dp).verticalScroll(rememberScrollState())) {
                    Text("Feed item above", modifier = Modifier.padding(vertical = 8.dp))
                    Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        repeat(10) {
                            Box(modifier = Modifier.size(96.dp).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) {
                                Text("Card ${it + 1}")
                            }
                        }
                    }
                    repeat(6) { Text("Feed item ${it + 1} below", modifier = Modifier.padding(vertical = 8.dp)) }
                }
            }
        }
        ScrollArrows(scrollState = outerScroll)
    }
}

@Preview(showBackground = true)
@Composable
fun TwoDimensionalScrollingScreenPreview() {
    QA_Accessibility_AppTheme { TwoDimensionalScrollingScreen() }
}

// ---------------------------------------------------------------------
// SCREEN READER FOCUS / NON-ACCESSIBLE INTERACTION (WCAG 4.1.2, critical)
// FAIL: a clickable, visible element with announceable text (or an image)
// that is importantForAccessibility=NO — a real touch target the screen
// reader can never focus. Rendered as native Views so importantForAccessibility
// is carried in the View tree the rule reads.
// PASS: clickable elements properly exposed to accessibility.
// ---------------------------------------------------------------------
@Composable
fun NonAccessibleInteractionScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Column(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.weight(1f).fillMaxWidth().verticalScroll(scrollState).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Screen Reader Focus for Interactive Elements", style = MaterialTheme.typography.headlineSmall)
            Text("WCAG 4.1.2 (A), Critical. A clickable element that is important-for-accessibility=NO can never be focused by a screen reader.",
                style = MaterialTheme.typography.bodySmall)

            // V-01: clickable text row, importantForAccessibility = NO
            RuleCard("V-01: Clickable Text, Not Important", "A clickable row with visible text marked importantForAccessibility=NO.") {
                ClickableNativeText("Tap to continue", important = false,
                    modifier = Modifier.fillMaxWidth().height(52.dp))
            }

            // V-02: clickable image, importantForAccessibility = NO
            RuleCard("V-02: Clickable Image, Not Important", "A clickable image marked importantForAccessibility=NO.") {
                ClickableNativeImage(R.drawable.buy_now_button, important = false,
                    modifier = Modifier.fillMaxWidth().height(72.dp))
            }

            // V-03: clickable native container (LinearLayout + text), not important
            RuleCard("V-03: Clickable Container, Not Important", "A clickable native card (LinearLayout + text) marked importantForAccessibility=NO.") {
                AndroidView(
                    factory = { ctx ->
                        android.widget.LinearLayout(ctx).apply {
                            orientation = android.widget.LinearLayout.HORIZONTAL
                            isClickable = true
                            setPadding(32, 24, 32, 24)
                            importantForAccessibility = android.view.View.IMPORTANT_FOR_ACCESSIBILITY_NO
                            setOnClickListener {}
                            addView(android.widget.TextView(ctx).apply { text = "Open details" })
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // V-04: Compose clickable hidden from a11y via clearAndSetSemantics
            RuleCard("V-04: Compose Clickable Hidden From A11y", "A clickable Compose row with visible text but clearAndSetSemantics {} — the screen reader can never focus it.") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {}
                        .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.medium)
                        .padding(16.dp)
                        .clearAndSetSemantics {},
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Hidden clickable row")
                }
            }

            // P-01: proper Compose button
            RuleCard("P-01: Labelled Button (Pass)", "A standard button — clickable and exposed to accessibility.") {
                Button(onClick = {}) { Text("Continue") }
            }

            // P-02: clickable native text, important + contentDescription
            RuleCard("P-02: Clickable Text, Important (Pass)", "A clickable row that is important-for-accessibility with a label.") {
                ClickableNativeText("Open settings", important = true,
                    modifier = Modifier.fillMaxWidth().height(52.dp))
            }

            // XML variant — same rule rendered from a native XML layout.
            RuleCard("XML Variant (native views)", "Inflated from res/layout/non_accessible_interaction_xml.xml — clickable Views with android:importantForAccessibility=\"no\".") {
                AndroidView(
                    factory = { ctx -> android.view.View.inflate(ctx, R.layout.non_accessible_interaction_xml, null) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        ScrollArrows(scrollState = scrollState)
    }
}

// A clickable native TextView whose importantForAccessibility flag is toggleable.
@Composable
private fun ClickableNativeText(text: String, important: Boolean, modifier: Modifier = Modifier) {
    AndroidView(
        factory = { ctx ->
            android.widget.TextView(ctx).apply {
                isClickable = true
                gravity = android.view.Gravity.CENTER_VERTICAL
                setOnClickListener {}
            }
        },
        update = { tv ->
            tv.text = text
            tv.importantForAccessibility = if (important)
                android.view.View.IMPORTANT_FOR_ACCESSIBILITY_YES
            else
                android.view.View.IMPORTANT_FOR_ACCESSIBILITY_NO
        },
        modifier = modifier
    )
}

// A clickable native ImageView whose importantForAccessibility flag is toggleable.
@Composable
private fun ClickableNativeImage(
    @androidx.annotation.DrawableRes resId: Int,
    important: Boolean,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { ctx ->
            android.widget.ImageView(ctx).apply {
                isClickable = true
                adjustViewBounds = true
                scaleType = android.widget.ImageView.ScaleType.FIT_START
                setOnClickListener {}
            }
        },
        update = { iv ->
            iv.setImageResource(resId)
            iv.importantForAccessibility = if (important)
                android.view.View.IMPORTANT_FOR_ACCESSIBILITY_YES
            else
                android.view.View.IMPORTANT_FOR_ACCESSIBILITY_NO
        },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun NonAccessibleInteractionScreenPreview() {
    QA_Accessibility_AppTheme { NonAccessibleInteractionScreen() }
}

// ---------------------------------------------------------------------
// APP & SCREEN ORIENTATION LOCK (WCAG 1.3.4, moderate)
// Detection reads the foreground activity's MANIFEST android:screenOrientation.
// The violation therefore lives in a dedicated Activity locked to portrait
// in AndroidManifest.xml; this launcher screen opens it. MainActivity has no
// activity-level screenOrientation, so it is the pass reference.
// ---------------------------------------------------------------------
@Composable
fun OrientationLockScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("App & Screen Orientation Lock", style = MaterialTheme.typography.headlineSmall)
        Text("WCAG 1.3.4 (AA), Moderate. An activity whose manifest android:screenOrientation locks it to a single orientation fails. Detection is manifest-based, so the violation is a separate locked activity.",
            style = MaterialTheme.typography.bodySmall)

        RuleCard("V-01: Portrait-Locked Activity", "Opens an activity declared with android:screenOrientation=\"portrait\" — scan that screen to see the violation.") {
            Button(onClick = { context.startActivity(Intent(context, OrientationLockActivity::class.java)) }) {
                Text("Open portrait-locked screen")
            }
        }

        RuleCard("V-02: Landscape-Locked Activity", "Opens an activity declared with android:screenOrientation=\"landscape\".") {
            Button(onClick = { context.startActivity(Intent(context, LandscapeLockActivity::class.java)) }) {
                Text("Open landscape-locked screen")
            }
        }

        RuleCard("V-03: No-Sensor-Locked Activity", "Opens an activity declared with android:screenOrientation=\"nosensor\" (ignores the rotation sensor).") {
            Button(onClick = { context.startActivity(Intent(context, NoSensorLockActivity::class.java)) }) {
                Text("Open nosensor-locked screen")
            }
        }

        RuleCard("P-01: Rotatable Activity (Pass)", "Opens an activity declared with android:screenOrientation=\"fullSensor\" — it rotates with the device and passes the rule.") {
            Button(onClick = { context.startActivity(Intent(context, RotatableActivity::class.java)) }) {
                Text("Open rotatable screen")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrientationLockScreenPreview() {
    QA_Accessibility_AppTheme { OrientationLockScreen() }
}

// =====================================================================
// TE-21951 — three new rule screens + helpers
// =====================================================================

// Native TextView with an explicit, non-scalable text unit (dp/px/pt/sp).
@Composable
private fun NativeSizedText(text: String, unit: Int, size: Float, modifier: Modifier = Modifier) {
    AndroidView(
        factory = { ctx -> android.widget.TextView(ctx) },
        update = { tv ->
            tv.text = text
            tv.setTextSize(unit, size)
        },
        modifier = modifier
    )
}

// A custom View that exposes an arbitrary RangeInfo to the a11y tree.
// Native SeekBar/ProgressBar clamp their values, so this is the reliable way
// to produce invalid ranges for the InvalidRangeValues rule.
@Composable
private fun RangeInfoView(min: Float, max: Float, current: Float, label: String, modifier: Modifier = Modifier) {
    // A real SeekBar (native slider look) that reports the given RangeInfo to the
    // a11y tree instead of its own clamped one — a proper-looking control that
    // still exposes invalid min/max/current for the rule.
    AndroidView(
        factory = { ctx ->
            object : android.widget.SeekBar(ctx) {
                override fun onInitializeAccessibilityNodeInfo(info: android.view.accessibility.AccessibilityNodeInfo) {
                    super.onInitializeAccessibilityNodeInfo(info)
                    @Suppress("DEPRECATION")
                    info.rangeInfo = android.view.accessibility.AccessibilityNodeInfo.RangeInfo.obtain(
                        android.view.accessibility.AccessibilityNodeInfo.RangeInfo.RANGE_TYPE_FLOAT,
                        min, max, current
                    )
                }
            }.apply {
                this.max = 100
                progress = 50
                contentDescription = label
            }
        },
        modifier = modifier
    )
}

// A native RadioGroup (a real selection group in the a11y tree).
@Composable
private fun NativeRadioGroup(labels: List<String>, modifier: Modifier = Modifier) {
    AndroidView(
        factory = { ctx ->
            android.widget.RadioGroup(ctx).apply {
                orientation = android.widget.RadioGroup.VERTICAL
                labels.forEach { label ->
                    addView(android.widget.RadioButton(ctx).apply { text = label })
                }
            }
        },
        modifier = modifier
    )
}

// Material Components views (ChipGroup / BottomNavigationView / TabLayout) require a
// Material3 theme to inflate. The app's activity theme is a framework Material theme,
// so wrap only these widgets in a Material3 ContextThemeWrapper — no global theme change.
private fun materialCtx(ctx: Context) =
    android.view.ContextThemeWrapper(ctx, com.google.android.material.R.style.Theme_Material3_Light)

// A Material ChipGroup in single-selection mode — a selection group in the a11y tree.
@Composable
private fun MaterialChipGroup(labels: List<String>, modifier: Modifier = Modifier) {
    AndroidView(
        factory = { ctx ->
            val themed = materialCtx(ctx)
            com.google.android.material.chip.ChipGroup(themed).apply {
                isSingleSelection = true
                labels.forEach { label ->
                    addView(com.google.android.material.chip.Chip(themed).apply {
                        text = label
                        isCheckable = true
                    })
                }
            }
        },
        modifier = modifier
    )
}

// A Material BottomNavigationView — a selection group (menu items).
@Composable
private fun MaterialBottomNav(labels: List<String>, modifier: Modifier = Modifier) {
    AndroidView(
        factory = { ctx ->
            com.google.android.material.bottomnavigation.BottomNavigationView(materialCtx(ctx)).apply {
                labels.forEachIndexed { i, label -> menu.add(0, i, i, label) }
            }
        },
        modifier = modifier
    )
}

// A Material TabLayout — a selection group (tabs).
@Composable
private fun MaterialTabLayout(labels: List<String>, modifier: Modifier = Modifier) {
    AndroidView(
        factory = { ctx ->
            com.google.android.material.tabs.TabLayout(materialCtx(ctx)).apply {
                tabMode = com.google.android.material.tabs.TabLayout.MODE_SCROLLABLE
                labels.forEach { label -> addTab(newTab().setText(label)) }
            }
        },
        modifier = modifier
    )
}

// ---------------------------------------------------------------------
// MINIMUM TEXT SIZE (Best Practice, minor)
// Non-scalable text (dp/px/pt) < 16dp fails. SP text and Compose Text
// (SP by default) are skipped, so violations use native TextViews.
// ---------------------------------------------------------------------
@Composable
fun MinimumTextSizeScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Column(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.weight(1f).fillMaxWidth().verticalScroll(scrollState).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Minimum Text Size", style = MaterialTheme.typography.headlineSmall)
            Text("Best Practice (Minor). Non-scalable text (dp/px/pt) below 16dp fails. SP text and Compose Text (SP by default) are skipped because they scale with the user's font setting.",
                style = MaterialTheme.typography.bodySmall)

            RuleCard("V-01: 8dp (DIP)", "Native text sized 8dp — below the 16dp minimum.") {
                NativeSizedText("Tiny 8dp text", android.util.TypedValue.COMPLEX_UNIT_DIP, 8f, Modifier.fillMaxWidth())
            }
            RuleCard("V-02: 10px (PX)", "Native text sized 10px — below 16dp.") {
                NativeSizedText("Tiny 10px text", android.util.TypedValue.COMPLEX_UNIT_PX, 10f, Modifier.fillMaxWidth())
            }
            RuleCard("V-03: 12dp (DIP)", "Native text sized 12dp — below 16dp.") {
                NativeSizedText("Small 12dp text", android.util.TypedValue.COMPLEX_UNIT_DIP, 12f, Modifier.fillMaxWidth())
            }
            RuleCard("V-04: 6pt (PT)", "Native text sized 6pt — below 16dp.") {
                NativeSizedText("Tiny 6pt text", android.util.TypedValue.COMPLEX_UNIT_PT, 6f, Modifier.fillMaxWidth())
            }
            RuleCard("P-01: 16dp (DIP, at threshold)", "At the 16dp threshold — passes.") {
                NativeSizedText("16dp text", android.util.TypedValue.COMPLEX_UNIT_DIP, 16f, Modifier.fillMaxWidth())
            }
            RuleCard("P-02: 20dp (DIP)", "Above threshold — passes.") {
                NativeSizedText("20dp text", android.util.TypedValue.COMPLEX_UNIT_DIP, 20f, Modifier.fillMaxWidth())
            }
            RuleCard("P-03: 10sp (SP, skipped)", "SP is scalable — the rule skips it.") {
                NativeSizedText("10sp text", android.util.TypedValue.COMPLEX_UNIT_SP, 10f, Modifier.fillMaxWidth())
            }
            RuleCard("P-04: Compose Text 14.sp (skipped)", "Compose Text uses SP by default — skipped.") {
                Text("Compose 14.sp text", fontSize = 14.sp)
            }
            RuleCard("XML Variant (native layout)", "Inflated from res/layout/minimum_text_size_xml.xml — TextViews with android:textSize in dp/px/pt (violations) and sp (skipped).") {
                AndroidView(
                    factory = { ctx -> android.view.View.inflate(ctx, R.layout.minimum_text_size_xml, null) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        ScrollArrows(scrollState = scrollState)
    }
}

@Preview(showBackground = true)
@Composable
fun MinimumTextSizeScreenPreview() {
    QA_Accessibility_AppTheme { MinimumTextSizeScreen() }
}

// ---------------------------------------------------------------------
// INVALID RANGE VALUES (WCAG 4.1.2, serious)
// A range control fails if min >= max, or current is outside [min, max].
// ---------------------------------------------------------------------
@Composable
fun InvalidRangeValuesScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Column(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.weight(1f).fillMaxWidth().verticalScroll(scrollState).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Invalid Range Values", style = MaterialTheme.typography.headlineSmall)
            Text("WCAG 4.1.2 (A), Serious. Fails when min >= max, or current falls outside [min, max]. Native SeekBar clamps values, so these use a custom View that sets RangeInfo directly.",
                style = MaterialTheme.typography.bodySmall)

            RuleCard("V-01: current > max (0, 100, 150)", "RangeInfo current 150 exceeds max 100.") {
                RangeInfoView(0f, 100f, 150f, "Volume", Modifier.fillMaxWidth().height(48.dp))
            }
            RuleCard("V-02: min > max (100, 50, 75)", "Inverted range — min 100 is greater than max 50.") {
                RangeInfoView(100f, 50f, 75f, "Brightness", Modifier.fillMaxWidth().height(48.dp))
            }
            RuleCard("V-03: min == max (50, 50, 50)", "Zero-width range — min equals max.") {
                RangeInfoView(50f, 50f, 50f, "Level", Modifier.fillMaxWidth().height(48.dp))
            }
            RuleCard("V-04: current < min (0, 100, -10)", "RangeInfo current -10 is below min 0.") {
                RangeInfoView(0f, 100f, -10f, "Progress", Modifier.fillMaxWidth().height(48.dp))
            }
            RuleCard("P-01: valid (0, 100, 50)", "Current within range — passes.") {
                RangeInfoView(0f, 100f, 50f, "Volume", Modifier.fillMaxWidth().height(48.dp))
            }
            RuleCard("P-02: current at min (0, 100, 0)", "Current at the min boundary — passes.") {
                RangeInfoView(0f, 100f, 0f, "Brightness", Modifier.fillMaxWidth().height(48.dp))
            }
            RuleCard("P-03: current at max (0, 100, 100)", "Current at the max boundary — passes.") {
                RangeInfoView(0f, 100f, 100f, "Progress", Modifier.fillMaxWidth().height(48.dp))
            }
            RuleCard("XML Variant (native layout)", "Inflated from res/layout/invalid_range_values_xml.xml — RangeInfoSeekBar declares invalid ranges via app:range* attributes.") {
                AndroidView(
                    factory = { ctx -> android.view.View.inflate(ctx, R.layout.invalid_range_values_xml, null) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        ScrollArrows(scrollState = scrollState)
    }
}

@Preview(showBackground = true)
@Composable
fun InvalidRangeValuesScreenPreview() {
    QA_Accessibility_AppTheme { InvalidRangeValuesScreen() }
}

// ---------------------------------------------------------------------
// UNIQUE OPTION NAMES (WCAG 4.1.2 + 1.3.1, moderate)
// Options within a selection group must have distinct labels. Lists
// (RecyclerView / plain Column of buttons) are NOT selection groups.
// ---------------------------------------------------------------------
@Composable
fun UniqueOptionNamesScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Column(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.weight(1f).fillMaxWidth().verticalScroll(scrollState).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Unique Option Names", style = MaterialTheme.typography.headlineSmall)
            Text("WCAG 4.1.2 + 1.3.1 (A), Moderate. Options within a selection group (RadioGroup / SelectableGroup) must have distinct labels. Same labels in separate groups, or in a plain list, are fine.",
                style = MaterialTheme.typography.bodySmall)

            RuleCard("V-01: RadioGroup — duplicate 'Option'", "Two 'Option' + one 'Different' — the 'Option' pair is flagged, 'Different' is not.") {
                NativeRadioGroup(listOf("Option", "Option", "Different"), Modifier.fillMaxWidth())
            }
            RuleCard("V-02: RadioGroup — three 'Yes'", "All three identical labels are flagged.") {
                NativeRadioGroup(listOf("Yes", "Yes", "Yes"), Modifier.fillMaxWidth())
            }
            RuleCard("V-03: Compose SelectableGroup — duplicates", "SelectableGroup auto-populates CollectionInfo; the duplicate 'Standard' pair is flagged.") {
                val options = listOf("Standard", "Standard", "Premium")
                var selected by remember { mutableStateOf(0) }
                Column(modifier = Modifier.selectableGroup()) {
                    options.forEachIndexed { i, label ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(selected = selected == i, onClick = { selected = i }, role = Role.RadioButton)
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = selected == i, onClick = null)
                            Text(label, modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                }
            }
            RuleCard("P-01: RadioGroup — unique labels", "Monthly / Yearly / Lifetime — all distinct.") {
                NativeRadioGroup(listOf("Monthly", "Yearly", "Lifetime"), Modifier.fillMaxWidth())
            }
            RuleCard("P-02: Two separate groups, same labels", "Same 'Yes'/'No' labels in DIFFERENT groups is fine.") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Notifications", style = MaterialTheme.typography.labelLarge)
                    NativeRadioGroup(listOf("Yes", "No"), Modifier.fillMaxWidth())
                    Text("Marketing emails", style = MaterialTheme.typography.labelLarge)
                    NativeRadioGroup(listOf("Yes", "No"), Modifier.fillMaxWidth())
                }
            }
            RuleCard("P-03: List of buttons (not a group)", "A plain list with repeated 'Add to cart' — not a selection group, so not flagged.") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(3) {
                        Button(onClick = {}, modifier = Modifier.fillMaxWidth()) { Text("Add to cart") }
                    }
                }
            }
            RuleCard("P-04: Empty-label radios", "Empty labels are skipped.") {
                NativeRadioGroup(listOf("", "", ""), Modifier.fillMaxWidth())
            }

            // Other detectable group container types (beyond RadioGroup).
            RuleCard("V-04: ChipGroup — duplicate 'Filter'", "Single-selection ChipGroup with two 'Filter' chips — the duplicate pair is flagged.") {
                MaterialChipGroup(listOf("Filter", "Filter", "Sort"), Modifier.fillMaxWidth())
            }
            RuleCard("P-05: ChipGroup — unique chips", "Small / Medium / Large — all distinct.") {
                MaterialChipGroup(listOf("Small", "Medium", "Large"), Modifier.fillMaxWidth())
            }
            RuleCard("V-05: BottomNavigationView — duplicate 'Home'", "Bottom nav with two 'Home' items — the duplicate pair is flagged.") {
                MaterialBottomNav(listOf("Home", "Home", "Profile"), Modifier.fillMaxWidth())
            }
            RuleCard("P-06: BottomNavigationView — unique items", "Home / Search / Profile — all distinct.") {
                MaterialBottomNav(listOf("Home", "Search", "Profile"), Modifier.fillMaxWidth())
            }
            RuleCard("V-06: TabLayout — three 'Tab'", "TabLayout with three identical 'Tab' labels — all flagged.") {
                MaterialTabLayout(listOf("Tab", "Tab", "Tab"), Modifier.fillMaxWidth())
            }
            RuleCard("P-07: TabLayout — unique tabs", "Overview / Details / Reviews — all distinct.") {
                MaterialTabLayout(listOf("Overview", "Details", "Reviews"), Modifier.fillMaxWidth())
            }

            RuleCard("XML Variant (native layout)", "Inflated from res/layout/unique_option_names_xml.xml — native RadioGroups with duplicate labels (violations), unique labels, and same labels in separate groups (passes).") {
                AndroidView(
                    factory = { ctx -> android.view.View.inflate(ctx, R.layout.unique_option_names_xml, null) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        ScrollArrows(scrollState = scrollState)
    }
}

@Preview(showBackground = true)
@Composable
fun UniqueOptionNamesScreenPreview() {
    QA_Accessibility_AppTheme { UniqueOptionNamesScreen() }
}

// =====================================================================
// TE-21959 — four new rule screens (Label in Name, Label at Front,
// Keyboard Focus, Text Spacing)
// =====================================================================

// Native clickable View whose focusable flag can be set explicitly.
// Compose's Modifier.clickable() also flips focusable=true, so violations
// for NonFocusableInteractiveElement need the flag set here at the View level.
// importantForAccessibility is set to YES explicitly to guarantee the node
// APPEARS in the a11y tree — the standard UIAutomator dump doesn't actually
// print this attribute, but the resolved isImportantForAccessibility()
// controls tree presence, which the effective detection depends on.
@Composable
private fun NativeClickableView(
    label: String,
    focusable: Boolean,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { ctx ->
            android.widget.TextView(ctx).apply {
                text = label
                setPadding(32, 24, 32, 24)
                setBackgroundColor(0xFF6650A4.toInt())
                setTextColor(0xFFFFFFFF.toInt())
                isClickable = true
                isFocusable = focusable
                importantForAccessibility = android.view.View.IMPORTANT_FOR_ACCESSIBILITY_YES
                setOnClickListener { }
            }
        },
        modifier = modifier
    )
}

// Native TextView that lets us dial per-line and per-letter spacing to below
// the TextSpacing thresholds (Compose Text applies system-wide sensible
// defaults that make the rule hard to trip).
@Composable
private fun NativeSpacedText(
    text: String,
    sizeSp: Float,
    lineSpacingMultiplier: Float = 1.2f,
    lineSpacingExtraDp: Int = 0,
    letterSpacingEm: Float = 0f,
    modifier: Modifier = Modifier
) {
    // Wrapper LinearLayout with a leading android.widget.Space child that
    // ALWAYS appears in the a11y tree (native View with bounds). This breaks
    // the paragraph rule's backward-sibling scan two ways:
    //   - if the wrapper LinearLayout appears in the a11y tree, TextView's
    //     parent is the LinearLayout and Space is TextView's preceding sibling
    //     inside it → empty-text sibling → rule returns ratioAbsent.
    //   - if the wrapper collapses, Space and TextView both flatten into
    //     RuleCard's Column. Space sits BETWEEN the subtitle Text and the
    //     TextView → still empty-text preceding sibling → still ratioAbsent.
    AndroidView(
        factory = { ctx ->
            android.widget.LinearLayout(ctx).apply {
                orientation = android.widget.LinearLayout.VERTICAL
                layoutParams = android.view.ViewGroup.LayoutParams(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT
                )
                // DO NOT set contentDescription on this ViewGroup — that would
                // cause Android to absorb the child TextView into a single
                // accessibility node, making the TextView invisible to MAE's
                // refreshWithExtraData call → no char-tops → rule silently
                // passes even when spacing is genuinely crushed.
                importantForAccessibility = android.view.View.IMPORTANT_FOR_ACCESSIBILITY_YES
                // Leading Space guarantees an empty-text preceding sibling for
                // the TextView so the paragraph rule bails out with ratioAbsent
                // (even if this wrapper collapses in the tree).
                addView(
                    android.widget.Space(ctx).apply {
                        layoutParams = android.widget.LinearLayout.LayoutParams(
                            android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                            1
                        )
                    }
                )
                addView(
                    android.widget.TextView(ctx).apply {
                        layoutParams = android.widget.LinearLayout.LayoutParams(
                            android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                            android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                    }
                )
            }
        },
        update = { root ->
            // Children: [0]=Space, [1]=TextView
            val tv = root.getChildAt(1) as android.widget.TextView
            tv.text = text
            tv.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, sizeSp)
            val extraPx = lineSpacingExtraDp * root.resources.displayMetrics.density
            tv.setLineSpacing(extraPx, lineSpacingMultiplier)
            tv.letterSpacing = letterSpacingEm
        },
        modifier = modifier
    )
}

// Two TextViews under a single LinearLayout so they are strictly consecutive
// siblings under the same parent in the a11y XML dump — the shape the
// TextSpacing paragraph-gap rule needs. `spacerHeightDp = 0` puts them
// directly adjacent (paragraph FAIL); a positive value inserts an
// android.widget.Space between them (rule returns N/A on the paragraph
// sub-check, so only line/word spacing determine the verdict).
@Composable
private fun NativeParagraphPair(
    firstText: String,
    secondText: String,
    sizeSp: Float = 16f,
    lineSpacingMultiplier: Float = 1.2f,
    spacerHeightDp: Int = 0,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val spacerPx = with(density) { spacerHeightDp.dp.roundToPx() }
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            android.widget.LinearLayout(ctx).apply {
                orientation = android.widget.LinearLayout.VERTICAL
                layoutParams = android.widget.LinearLayout.LayoutParams(
                    android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                    android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
                )
                // No contentDescription — that would absorb the child TextViews
                // and hide them from MAE's geometry capture.
                importantForAccessibility = android.view.View.IMPORTANT_FOR_ACCESSIBILITY_YES
                // Leading Space isolates the FIRST paragraph from the RuleCard
                // subtitle even if this LinearLayout collapses in the a11y tree.
                // Paragraph pairing between the two internal TextViews is not
                // affected because this Space sits BEFORE the first paragraph,
                // not between them.
                addView(android.widget.Space(ctx).apply {
                    layoutParams = android.widget.LinearLayout.LayoutParams(
                        android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                        1
                    )
                })
                fun makeText(txt: String) = android.widget.TextView(ctx).apply {
                    text = txt
                    setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, sizeSp)
                    setLineSpacing(0f, lineSpacingMultiplier)
                    layoutParams = android.widget.LinearLayout.LayoutParams(
                        android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                        android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                }
                addView(makeText(firstText))
                if (spacerPx > 0) {
                    addView(android.widget.Space(ctx).apply {
                        layoutParams = android.widget.LinearLayout.LayoutParams(
                            android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                            spacerPx
                        )
                    })
                }
                addView(makeText(secondText))
            }
        }
    )
}

// ---------------------------------------------------------------------
// LABEL IN NAME — MismatchedLabelText (WCAG 2.5.3 A, Serious)
// contentDescription REPLACES visible text for TalkBack, so it must
// contain the visible label (ACT-normalized whole-word containment).
// Exempt: symbolic-only, single-character, or numeric-only labels.
// ---------------------------------------------------------------------
@Composable
fun LabelInNameScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Column(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.weight(1f).fillMaxWidth().verticalScroll(scrollState).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Mismatched Label Text", style = MaterialTheme.typography.headlineSmall)
            Text("WCAG 2.5.3 (A), Serious. Accessible name must contain the visible label (whole-word, case/punctuation-ignored). Exempt: symbolic, single-char, or numeric-only labels.",
                style = MaterialTheme.typography.bodySmall)

            // ---------- VIOLATIONS ----------
            RuleCard(
                "V-01: Compose Button — unrelated cd (Submit / Send)",
                "Compose Button. Child Text's cd='Send' overrides the visible label on the merged node. Why: TalkBack announces 'Send', so a voice-control user saying the visible 'Submit' cannot activate this button — the spoken name doesn't match what they see."
            ) {
                Button(onClick = {}) {
                    Text("Submit", modifier = Modifier.semantics { contentDescription = "Send" })
                }
            }
            RuleCard(
                "V-02: partial-word match (Add / Address book)",
                "Visible 'Add', child Text cd='Address book'. Why: 'Add' appears as a substring inside 'Address' but not as a whole word — voice-control matchers walk word boundaries, so the user's spoken 'Add' never lands on this control."
            ) {
                Button(onClick = {}) {
                    Text("Add", modifier = Modifier.semantics { contentDescription = "Address book" })
                }
            }
            RuleCard(
                "V-03: Compose container (Checkout / Proceed to payment)",
                "Clickable Row wrapping a Text('Checkout') whose semantics override the label to 'Proceed to payment'. Why: the visible action word is completely omitted from the spoken name — a screen-reader user hears one thing while a sighted colleague reads another, and voice-users can't guess the internal wording."
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable(role = Role.Button) {}
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        "Checkout",
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.semantics { contentDescription = "Proceed to payment" }
                    )
                }
            }
            RuleCard(
                "V-04: native android.widget.Button — opposite meaning (Sign in / Log out)",
                "Native Button, android:text='Sign in', contentDescription='Log out'. Why: label and action are opposites; a voice command based on the visible label triggers behaviour the user did not intend — one of the highest-severity 2.5.3 failures."
            ) {
                AndroidView(
                    factory = { ctx ->
                        android.widget.Button(ctx).apply {
                            text = "Sign in"
                            contentDescription = "Log out"
                        }
                    }
                )
            }
            RuleCard(
                "V-05: cd is a truncation (Save changes / Save)",
                "Native Button, text='Save changes', cd='Save'. Why: the visible label is longer than the accessible name — 'save changes' (two words) is not contained inside 'save' (one word); voice-users saying 'Save changes' cannot activate the button."
            ) {
                AndroidView(
                    factory = { ctx ->
                        android.widget.Button(ctx).apply {
                            text = "Save changes"
                            contentDescription = "Save"
                        }
                    }
                )
            }
            RuleCard(
                "V-06 (scanner miss): Modifier.clearAndSetSemantics { contentDescription = ... }",
                "Compose Button with clearAndSetSemantics dropping the child Text and setting cd='Move to trash'. Real WCAG 2.5.3 harm — visible 'Delete' is unspoken and unmatchable by voice — BUT the AAE rule silently PASSES: with the child cleared, the merged node has text='' and no descendants, so VisibleLabelText() returns '' → IsApplicable is false. This card exists to expose that scanner blind spot. Detection requires OCR-vs-a11y-tree cross-referencing (like Google ATF's prerelease UnexposedTextCheck), not this rule."
            ) {
                Button(
                    onClick = {},
                    modifier = Modifier.clearAndSetSemantics { contentDescription = "Move to trash" }
                ) { Text("Delete") }
            }
            RuleCard(
                "V-07: icon + text button, cd names only the icon (★ Favorites / Star icon)",
                "Compose Button with a leading ★ character and visible text 'Favorites'; cd on the inner Text='Star icon' — describes the glyph, not the action. Why: cd should describe the action, not the visual. Visible 'Favorites' isn't in the accessible name, so voice-command 'Favorites' fails; screen-reader users hear 'Star icon' with no hint of what tapping does."
            ) {
                Button(onClick = {}) {
                    Text(
                        "★ Favorites",
                        modifier = Modifier.semantics { contentDescription = "Star icon" }
                    )
                }
            }
            RuleCard(
                "V-08: card with 'Learn more' described as 'Read the full article' (thematic replacement)",
                "Clickable Row rendering a link-style Text('Learn more') whose cd='Read the full article'. Why: the two labels are semantically related but share no whole word. A common editor pattern — writers 'improve' the accessible copy without checking it still contains the visible words the user might say."
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .clickable(role = Role.Button) {}
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        "Learn more",
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.semantics { contentDescription = "Read the full article" }
                    )
                }
            }
            RuleCard(
                "V-09: XML declarative variants (inflated from res/layout/label_in_name_xml.xml)",
                "Four native Buttons defined in XML with android:contentDescription. Includes V-XML-01 (unrelated), V-XML-02 (opposite), V-XML-03 (partial word), plus P-XML-01 (contained) and P-XML-02 (no cd, N/A). Why an XML variant matters: proves the rule catches the mismatch regardless of whether cd is set imperatively at runtime or declared in a layout XML."
            ) {
                AndroidView(
                    factory = { ctx -> android.view.View.inflate(ctx, R.layout.label_in_name_xml, null) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // ---------- PASSES ----------
            RuleCard("P-01: label contained inside cd (Submit / Submit, opens settings)", "Visible 'Submit', cd='Submit, opens settings' — 'Submit' present as a whole word after normalization → PASS.") {
                Button(onClick = {}) {
                    Text("Submit", modifier = Modifier.semantics { contentDescription = "Submit, opens settings" })
                }
            }
            RuleCard("P-02: case + punctuation normalized (Pay Now! / pay now, submits the order)", "NormalizeLabelText lowercases and replaces non-alphanumerics with spaces before comparison → PASS.") {
                Button(onClick = {}) {
                    Text("Pay Now!", modifier = Modifier.semantics { contentDescription = "pay now, submits the order" })
                }
            }
            RuleCard("P-03: single-character label exempt (B / Bold)", "VisibleLabelText returns '' for labels shorter than 2 characters after normalization — rule not applicable.") {
                Button(onClick = {}) {
                    Text("B", modifier = Modifier.semantics { contentDescription = "Bold" })
                }
            }
            RuleCard("P-04: symbolic label exempt (× / Close)", "VisibleLabelText returns '' when the visible text contains no letters — icon-glyph labels are exempt.") {
                Button(onClick = {}) {
                    Text("×", modifier = Modifier.semantics { contentDescription = "Close" })
                }
            }
            RuleCard("P-05: numeric-only label exempt (42 / 42 unread notifications)", "Numeric-only labels contain no letters — VisibleLabelText returns '' and the rule silently skips the node.") {
                Button(onClick = {}) {
                    Text("42", modifier = Modifier.semantics { contentDescription = "42 unread notifications" })
                }
            }
            RuleCard("P-06: no contentDescription — rule not applicable", "Native Button with text='Print' and no contentDescription set. IsApplicable requires cd != '' — rule silently skips.") {
                AndroidView(
                    factory = { ctx ->
                        android.widget.Button(ctx).apply { text = "Print" }
                    }
                )
            }
        }
        ScrollArrows(scrollState = scrollState)
    }
}

@Preview(showBackground = true)
@Composable
fun LabelInNameScreenPreview() {
    QA_Accessibility_AppTheme { LabelInNameScreen() }
}

// ---------------------------------------------------------------------
// LABEL AT FRONT — MisplacedFieldLabel (Best Practice, Moderate)
// Once containment is confirmed, the accessible name should START with
// the visible label. A containment miss belongs to MismatchedLabelText,
// not this rule — the two never fire on the same node.
// ---------------------------------------------------------------------
@Composable
fun LabelAtFrontScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Column(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.weight(1f).fillMaxWidth().verticalScroll(scrollState).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Misplaced Field Label", style = MaterialTheme.typography.headlineSmall)
            Text("WCAG 2.5.3 Best Practice, Moderate. Accessible name should START with the visible label (whole-word prefix, case-normalized). Containment misses go to MismatchedLabelText.",
                style = MaterialTheme.typography.bodySmall)

            // ---------- VIOLATIONS ----------
            RuleCard(
                "V-01: label in the middle (EN / Wikipedia EN language)",
                "Compose Button, visible 'EN' via inner Text's cd='Wikipedia EN language'. Why: voice-control command recognisers front-anchor on the accessible name; 'EN' is contained but not at the head, so the user saying 'EN' cannot activate it — even though the label IS technically present."
            ) {
                Button(onClick = {}) {
                    Text("EN", modifier = Modifier.semantics { contentDescription = "Wikipedia EN language" })
                }
            }
            RuleCard(
                "V-02: label at the end (Submit / Please submit the form now)",
                "Visible 'Submit', cd='Please submit the form now'. Why: 'submit' buried after 'please' — voice-users typically say the button's visible label; front-anchored matchers won't reach a mid-string label reliably."
            ) {
                Button(onClick = {}) {
                    Text("Submit", modifier = Modifier.semantics { contentDescription = "Please submit the form now" })
                }
            }
            RuleCard(
                "V-03: Compose container, label mid-string (Save / Quickly save your work)",
                "Clickable Row wrapping a Text('Save') with cd='Quickly save your work'. Why: containers set descriptive prose as cd (\"quickly do X\") that reads well to TalkBack — but pushes the visible action word off the front, breaking voice-command activation."
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable(role = Role.Button) {}
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        "Save",
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.semantics { contentDescription = "Quickly save your work" }
                    )
                }
            }
            RuleCard(
                "V-04: native Button — polite-prefix cd (Cancel / Please Cancel to abort)",
                "Native android.widget.Button, text='Cancel', cd='Please Cancel to abort'. Why: politeness prefixes ('Please', 'Tap to', 'Click to') are the most common front-of-cd anti-pattern — they front-shift the actual verb, silently breaking voice-command reach."
            ) {
                AndroidView(
                    factory = { ctx ->
                        android.widget.Button(ctx).apply {
                            text = "Cancel"
                            contentDescription = "Please Cancel to abort"
                        }
                    }
                )
            }
            RuleCard(
                "V-05: grouped view — role/value BEFORE label (Email / 'edit box containing john@example.com for email')",
                "Clickable Row rendering visible 'Email' with a composed cd that puts role and value ahead of the label. Why: for grouped/form fields the announcement order matters — a screen-reader user scanning quickly needs the identifying WORD first ('Email address, john@example.com, edit box'), not the widget class. Reversed order buries the label, and voice-users trying to say 'Email' can't front-anchor."
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable(role = Role.Button) {}
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        "Email",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.semantics {
                            contentDescription = "edit box containing john@example.com for email"
                        }
                    )
                }
            }
            RuleCard(
                "V-06: XML declarative variants (inflated from res/layout/label_at_front_xml.xml)",
                "Five native Buttons defined in XML. V-XML-01/02/03 place the label mid-string, at the end, or buried. P-XML-01/02 show label-at-front and exact-match passes. Why an XML variant: designers often write cd copy directly in strings.xml / layout files — the rule catches those declarative violations the same as code-set ones."
            ) {
                AndroidView(
                    factory = { ctx -> android.view.View.inflate(ctx, R.layout.label_at_front_xml, null) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // ---------- PASSES ----------
            RuleCard("P-01: label at the front (EN / EN Wikipedia language)", "Normalized cd starts with the visible label followed by a space → labelAtFront returns true → PASS.") {
                Button(onClick = {}) {
                    Text("EN", modifier = Modifier.semantics { contentDescription = "EN Wikipedia language" })
                }
            }
            RuleCard("P-02: exact match (Submit / Submit)", "Normalized cd equals normalized visible label → PASS.") {
                Button(onClick = {}) {
                    Text("Submit", modifier = Modifier.semantics { contentDescription = "Submit" })
                }
            }
            RuleCard("P-03: case difference at front (Pay Now / pay now and finish checkout)", "NormalizeLabelText lowercases both sides; prefix check runs on normalized text → PASS.") {
                Button(onClick = {}) {
                    Text("Pay Now", modifier = Modifier.semantics { contentDescription = "pay now and finish checkout" })
                }
            }
            RuleCard("P-04: label absent — this rule stays silent (EN / Wikipedia language)", "LabelContains returns false (no containment), so MisplacedFieldLabel PASSES silently. MismatchedLabelText will FLAG this node instead — that's the intended two-rule split.") {
                Button(onClick = {}) {
                    Text("EN", modifier = Modifier.semantics { contentDescription = "Wikipedia language" })
                }
            }
            RuleCard("P-05: no cd + non-interactive TextView — rule N/A", "accessibleName(node) returns '' for a non-interactive node with no cd → rule silently skips.") {
                AndroidView(
                    factory = { ctx ->
                        android.widget.TextView(ctx).apply {
                            text = "Static heading"
                            setPadding(16, 12, 16, 12)
                        }
                    }
                )
            }
        }
        ScrollArrows(scrollState = scrollState)
    }
}

@Preview(showBackground = true)
@Composable
fun LabelAtFrontScreenPreview() {
    QA_Accessibility_AppTheme { LabelAtFrontScreen() }
}

// ---------------------------------------------------------------------
// KEYBOARD FOCUS — NonFocusableInteractiveElement (WCAG 2.1.1 A, Serious)
// Clickable but not focusable, with no focusable+clickable ancestor or
// descendant to delegate focus. Focusable-only (non-clickable) wrappers
// do NOT count as delegation.
// ---------------------------------------------------------------------
@Composable
fun KeyboardFocusScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Column(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.weight(1f).fillMaxWidth().verticalScroll(scrollState).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Non-Focusable Interactive Element", style = MaterialTheme.typography.headlineSmall)
            Text("WCAG 2.1.1 (A), Serious. Every clickable / long-clickable / checkable control must be keyboard-focusable. Delegation counts only if the ancestor/descendant is BOTH focusable AND interactive.",
                style = MaterialTheme.typography.bodySmall)

            // ---------- VIOLATIONS ----------
            RuleCard(
                "V-01: clickable TextView, isFocusable=false (Buy now)",
                "Native TextView with setOnClickListener + isFocusable=false. Why: hardware-keyboard users use Tab / arrow keys to traverse the input-focus chain; a control that opts out of focus is invisible to that traversal — the button might as well not exist for them. Switch-access users hit the same wall."
            ) {
                NativeClickableView("Buy now", focusable = false, modifier = Modifier.fillMaxWidth())
            }
            RuleCard(
                "V-02: second clickable-not-focusable target (Add to cart)",
                "Same shape as V-01 with a different label. Why: this pattern usually creeps in via custom Views that copy click handling from a parent stub but forget to set focusable=true — one buggy helper multiplies across a whole app."
            ) {
                NativeClickableView("Add to cart", focusable = false, modifier = Modifier.fillMaxWidth())
            }
            RuleCard(
                "V-03: focusable-only wrapper does NOT delegate",
                "Outer FrameLayout is focusable=true but clickable=false — cannot activate the child. Rule fires on the inner clickable-not-focusable TextView. Why: focus-delegation requires an ancestor that is BOTH focusable AND clickable (a click on the ancestor must actually perform the child's action). A focus-only wrapper eats keyboard focus but does nothing when the user presses Enter."
            ) {
                AndroidView(
                    modifier = Modifier.fillMaxWidth(),
                    factory = { ctx ->
                        android.widget.FrameLayout(ctx).apply {
                            isFocusable = true
                            isClickable = false
                            addView(
                                android.widget.TextView(ctx).apply {
                                    text = "Wrapped, still unreachable"
                                    setPadding(32, 24, 32, 24)
                                    setBackgroundColor(0xFFB3261E.toInt())
                                    setTextColor(0xFFFFFFFF.toInt())
                                    isClickable = true
                                    isFocusable = false
                                    importantForAccessibility = android.view.View.IMPORTANT_FOR_ACCESSIBILITY_YES
                                    setOnClickListener { }
                                }
                            )
                        }
                    }
                )
            }
            RuleCard(
                "V-04: CheckBox with isFocusable=false (Checkable path)",
                "android.widget.CheckBox is Checkable=true; forced isFocusable=false. Why: IsInteractive() covers Clickable OR LongClickable OR Checkable — a toggle that responds to touch but not keyboard traps power-users on external keyboards. The rule fires because Checkable alone is enough interactivity."
            ) {
                AndroidView(
                    factory = { ctx ->
                        android.widget.CheckBox(ctx).apply {
                            text = "Enable analytics"
                            isFocusable = false
                            importantForAccessibility = android.view.View.IMPORTANT_FOR_ACCESSIBILITY_YES
                        }
                    }
                )
            }
            RuleCard(
                "V-05: LongClickable-only View with focusable=false",
                "Native View with setOnLongClickListener (long-press only), isFocusable=false. Why: covers the LongClickable arm of IsInteractive. Real-world example: draggable list items whose primary interaction is long-press; if they aren't focusable, keyboard users can't trigger the drag menu."
            ) {
                AndroidView(
                    factory = { ctx ->
                        android.widget.TextView(ctx).apply {
                            text = "Long-press to reorder"
                            setPadding(32, 24, 32, 24)
                            setBackgroundColor(0xFF6650A4.toInt())
                            setTextColor(0xFFFFFFFF.toInt())
                            isLongClickable = true
                            isFocusable = false
                            importantForAccessibility = android.view.View.IMPORTANT_FOR_ACCESSIBILITY_YES
                            setOnLongClickListener { true }
                        }
                    }
                )
            }
            RuleCard(
                "V-06 (scanner miss): Compose Modifier.pointerInput { detectTapGestures { } }",
                "Box that handles taps via pointerInput / detectTapGestures instead of Modifier.clickable. Real 2.1.1 harm — touch works but keyboard / D-pad / switch access cannot reach it — BUT AAE will SILENTLY PASS: pointerInput adds no semantics, so the a11y dump shows clickable=false, and IsApplicable requires IsInteractive=true. Verified against Compose source: Modifier.clickable sets both clickable AND focusable via semantics; pointerInput sets neither. Detection here has to be behavioural (does the widget respond to touch?), not attribute-based."
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.error, MaterialTheme.shapes.medium)
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = { /* handled via raw touch */ })
                        }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Tap via raw pointerInput", color = MaterialTheme.colorScheme.onError)
                }
            }
            RuleCard(
                "V-07 (scanner miss): custom View overriding onTouchEvent() — no isClickable, no focusable",
                "Custom android.view.View that consumes ACTION_UP directly in onTouchEvent() and never sets isClickable / isFocusable. Same shape as V-06 but at the View layer. AAE will SILENTLY PASS: the a11y dump shows clickable=false (verified against AOSP source — View.isClickable() must be true, and setOnClickListener / setClickable(true) is the only way). Detection here has to be behavioural (does the widget respond to touch?), not attribute-based."
            ) {
                AndroidView(
                    modifier = Modifier.fillMaxWidth(),
                    factory = { ctx ->
                        object : android.view.View(ctx) {
                            init {
                                setBackgroundColor(0xFFB3261E.toInt())
                                minimumHeight = (48 * resources.displayMetrics.density).toInt()
                            }
                            override fun onTouchEvent(event: android.view.MotionEvent): Boolean {
                                if (event.action == android.view.MotionEvent.ACTION_UP) {
                                    performClick()
                                }
                                return true
                            }
                            override fun performClick(): Boolean {
                                super.performClick()
                                return true
                            }
                        }
                    }
                )
            }
            RuleCard(
                "V-08: XML declarative variants (inflated from res/layout/keyboard_focus_xml.xml)",
                "Three XML violations (clickable TextView with focusable=false, clickable LinearLayout with focusable=false, focusable-only FrameLayout wrapper) plus three XML passes (Button, clickable+focusable=true, focusable+clickable ancestor row). All entries in this XML fire (or pass) NonFocusableInteractiveElement — no related-trap items mixed in. Why: catches the same trap declared in layout XML — most legacy Android UIs live in XML, so declarative violations are common."
            ) {
                AndroidView(
                    factory = { ctx -> android.view.View.inflate(ctx, R.layout.keyboard_focus_xml, null) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // ---------- PASSES ----------
            RuleCard("P-01: Compose Button (inherently focusable + clickable)", "Modifier.clickable inside Button sets both Focusable and Clickable — PASS.") {
                Button(onClick = {}) { Text("Confirm") }
            }
            RuleCard("P-02: clickable + isFocusable=true", "Same native View as V-01 but with focus flipped on — keyboard can reach it → PASS.") {
                NativeClickableView("Reachable action", focusable = true, modifier = Modifier.fillMaxWidth())
            }
            RuleCard("P-03: focusable+clickable ancestor delegates for child", "Outer LinearLayout is Clickable AND Focusable — inner clickable-not-focusable TextView passes via hasFocusableClickableAncestor.") {
                AndroidView(
                    modifier = Modifier.fillMaxWidth(),
                    factory = { ctx ->
                        android.widget.LinearLayout(ctx).apply {
                            orientation = android.widget.LinearLayout.HORIZONTAL
                            isClickable = true
                            isFocusable = true
                            setOnClickListener { }
                            setBackgroundColor(0xFF625B71.toInt())
                            setPadding(24, 16, 24, 16)
                            addView(
                                android.widget.TextView(ctx).apply {
                                    text = "Row content"
                                    setTextColor(0xFFFFFFFF.toInt())
                                    isClickable = true
                                    isFocusable = false
                                    setOnClickListener { }
                                }
                            )
                        }
                    }
                )
            }
            RuleCard("P-04: descendant delegation — clickable container with focusable+clickable inner Button", "Outer clickable container is Focusable=false, but the inner Button is Focusable+Clickable → hasFocusableClickableDescendant returns true → PASS for the container.") {
                AndroidView(
                    modifier = Modifier.fillMaxWidth(),
                    factory = { ctx ->
                        android.widget.LinearLayout(ctx).apply {
                            orientation = android.widget.LinearLayout.HORIZONTAL
                            isClickable = true
                            isFocusable = false
                            setOnClickListener { }
                            setPadding(24, 16, 24, 16)
                            addView(
                                android.widget.Button(ctx).apply { text = "Nested action" }
                            )
                        }
                    }
                )
            }
            RuleCard("P-05: disabled interactive — rule N/A", "TextView with isClickable=true but isEnabled=false. IsApplicable requires Enabled=true → rule silently skips.") {
                AndroidView(
                    factory = { ctx ->
                        android.widget.TextView(ctx).apply {
                            text = "Disabled action"
                            setPadding(32, 24, 32, 24)
                            setBackgroundColor(0xFF888888.toInt())
                            setTextColor(0xFFFFFFFF.toInt())
                            isClickable = true
                            isFocusable = false
                            isEnabled = false
                            setOnClickListener { }
                        }
                    }
                )
            }

            // ---------- RELATED TRAPS (not this rule) ----------
            Text(
                "Related trap — NOT flagged by NonFocusableInteractiveElement",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                "The card below is a real keyboard-UX problem but sits OUTSIDE this rule's scope. NonFocusableInteractiveElement's IsApplicable check requires IsInteractive (Clickable / LongClickable / Checkable) — a non-interactive container that is merely focusable never triggers the rule. Some rulesets ship a separate 'UnnecessaryFocusable' rule for this; AAE does not.",
                style = MaterialTheme.typography.bodySmall
            )

            RuleCard(
                "Related trap: android:focusable=\"true\" on a non-interactive ViewGroup — phantom tab stop",
                "Common TalkBack-grouping mistake: a ViewGroup is marked focusable=true so its children announce as one unit. That ALSO puts it in the keyboard-focus chain, so hardware-keyboard users hit a tab stop on a container that does nothing when Enter is pressed. From API 28+, use android:screenReaderFocusable=\"true\" instead — accessibility focus without keyboard focus. AAE stays silent on this pattern; catch it via a keyboard walk (Tab through the screen and see where focus lands with no action)."
            ) {
                AndroidView(
                    modifier = Modifier.fillMaxWidth(),
                    factory = { ctx ->
                        android.widget.LinearLayout(ctx).apply {
                            orientation = android.widget.LinearLayout.VERTICAL
                            isFocusable = true // trap: creates a keyboard tab stop on a non-interactive group
                            isClickable = false
                            setBackgroundColor(0xFFB3261E.toInt())
                            setPadding(24, 16, 24, 16)
                            addView(android.widget.TextView(ctx).apply {
                                text = "Title of grouped content"
                                setTextColor(0xFFFFFFFF.toInt())
                                textSize = 14f
                            })
                            addView(android.widget.TextView(ctx).apply {
                                text = "Body copy that a screen reader should announce together with the title above."
                                setTextColor(0xFFFFFFFF.toInt())
                                textSize = 12f
                            })
                        }
                    }
                )
            }
        }
        ScrollArrows(scrollState = scrollState)
    }
}

@Preview(showBackground = true)
@Composable
fun KeyboardFocusScreenPreview() {
    QA_Accessibility_AppTheme { KeyboardFocusScreen() }
}

// ---------------------------------------------------------------------
// TEXT SPACING — TextSpacing (WCAG 1.4.12 AA, Minor)
// MCAG SC 3.3.1 thresholds evaluated server-side from raw char geometry:
//   line height   ≥ 0.9× font size
//   word spacing  ≥ 0.16× font size
//   paragraph gap ≥ 2× line spacing (between strictly-consecutive
//                   multi-line siblings)
// Requires MAE-captured char-tops / space-advances — plain Compose Text
// often does not surface geometry, so violations use native TextViews.
// ---------------------------------------------------------------------

// TextSpacing-specific replacement for RuleCard. The default RuleCard uses
// plain Compose Text for title + subtitle; those emit char-tops in modern
// Compose and are exposed as android.widget.TextView in the a11y tree, so the
// TextSpacing paragraph rule pairs them up and flags EVERY card's subtitle
// (confirmed by API scan: paragraphSpacingRatio=0.50x on subtitle nodes).
// clearAndSetSemantics drops the child Text's semantics entirely and installs
// only a contentDescription — TalkBack still announces the description, but
// the a11y node has no text/GetTextLayoutResult, so no char-tops are emitted,
// IsApplicable returns false, and the paragraph rule cannot fire on it.
// Scoped to TextSpacingScreen — other screens keep using the shared RuleCard.
@Composable
private fun TextSpacingRuleCard(
    title: String,
    subtitle: String,
    content: @Composable () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.clearAndSetSemantics { contentDescription = title }
            )
            Text(
                subtitle,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.clearAndSetSemantics { contentDescription = subtitle }
            )
            content()
        }
    }
}

@Composable
fun TextSpacingScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    Column(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.weight(1f).fillMaxWidth().verticalScroll(scrollState).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val screenTitle = "Text Spacing"
            val screenDescription = "WCAG 1.4.12 (AA). AAE floors: line ≥ 0.9× font, word ≥ 0.16× font, paragraph ≥ 2.0× line-pitch. BrowserStack: line ≥ 1.5×, paragraph over font-size. V-06 (1.2× line) passes AAE, fails BrowserStack."
            Text(
                screenTitle,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.clearAndSetSemantics { contentDescription = screenTitle }
            )
            Text(
                screenDescription,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.clearAndSetSemantics { contentDescription = screenDescription }
            )

            // ---------- VIOLATIONS ----------
            TextSpacingRuleCard(
                "V-01: crushed line height (0.7× multiplier)",
                "Native TextView, 16sp text, lineSpacingMultiplier=0.7f. Computed lineRatio ≈ 0.84 — below the 0.9× floor. Why: line-height below the floor makes ascenders and descenders bleed into adjacent lines; low-vision readers lose their place mid-line and readers with dyslexia can't track horizontally."
            ) {
                NativeSpacedText(
                    text = "Reading dense text is exhausting for low-vision users when line height is squeezed below the WCAG floor, and the effect compounds when the paragraph is longer than a single wrapped line. This copy is intentionally long enough to force at least three rendered rows on any typical Android screen width so the scanner's line-pitch measurement has multiple charTops rows to work with.",
                    sizeSp = 16f,
                    lineSpacingMultiplier = 0.7f,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            TextSpacingRuleCard(
                "V-02: negative letter spacing crushes word gaps (-0.15em)",
                "Native TextView, 18sp text, letterSpacing=-0.15em. Computed wordRatio ≈ 0.10 — below the 0.16× floor. Why: the space glyph advance shrinks with tracking; when it drops below the floor, adjacent words merge visually — cognitive-disability users cannot parse word boundaries."
            ) {
                NativeSpacedText(
                    text = "Word spacing collapses when letters overlap because negative tracking pushes the interior space glyph advance below the sixteen-percent floor, and the scanner measures interior spaces only, so this copy is deliberately long with many interior spaces to yield a stable median.",
                    sizeSp = 18f,
                    lineSpacingMultiplier = 1.2f,
                    letterSpacingEm = -0.15f,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            TextSpacingRuleCard(
                "V-03: paragraphs jammed together (paragraph gap = 0)",
                "Two multi-line TextViews as strictly-consecutive siblings under one LinearLayout with zero spacer. Paragraph ratio = 0, far below the 2.0× line-pitch floor. Rule anchors on the LOWER paragraph. Why: paragraph gaps are the primary visual scan-cue for readers with cognitive disabilities; when gap < 2× line pitch, sighted users can't tell where one thought ends and the next begins."
            ) {
                NativeParagraphPair(
                    firstText = "First paragraph spans multiple lines so the scanner can compute a line pitch to compare against for the paragraph rule.",
                    secondText = "Second paragraph starts immediately, with no visual separation from the first one at all in the layout.",
                    sizeSp = 16f,
                    lineSpacingMultiplier = 1.0f,
                    spacerHeightDp = 0,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            TextSpacingRuleCard(
                "V-04: line + word failure on one node (combined evidence)",
                "Same TextView with lineSpacingMultiplier=0.7f AND letterSpacing=-0.15em. Why: rules emit multiple evidence flags per node — minimumLineSpacing and minimumWordSpacing appear together in wrongProps with their measured ratios; the dashboard shows both failing sub-checks so triage can prioritise the worst offender."
            ) {
                NativeSpacedText(
                    text = "Everything is too tight here because the lines are crushed together and the letters overlap each other constantly, which trips both the line-height sub-check and the word-spacing sub-check simultaneously. Enough text is provided to guarantee multiple wrapped lines and enough interior spaces for the scanner to compute both medians reliably.",
                    sizeSp = 16f,
                    lineSpacingMultiplier = 0.7f,
                    letterSpacingEm = -0.15f,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            TextSpacingRuleCard(
                "V-05: extreme -0.20em tracking on 20sp text",
                "Aggressive negative letter spacing pushes word ratio to ~0.05 — well past the floor. Why: shows the ratio scales linearly with tracking; harsher tracking = harsher fail, and the evidence prop 'wordSpacingRatio' surfaces the exact measured value."
            ) {
                NativeSpacedText(
                    text = "Extreme letter spacing collapses adjacent words into one blur when tracking is this aggressive, and the space glyph advance shrinks proportionally so the measured word-spacing ratio falls well below the sixteen-percent floor for readers who need spacing to parse boundaries.",
                    sizeSp = 20f,
                    lineSpacingMultiplier = 1.2f,
                    letterSpacingEm = -0.20f,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            TextSpacingRuleCard(
                "V-06 (threshold discriminator): default TextView line spacing (~1.2×)",
                "Native TextView with no line-spacing override — pitch ≈ 1.15–1.2× font size. Why include this: it sits BETWEEN the two thresholds — > 0.9 (PASSES AAE MCAG) and < 1.5 (FAILS BrowserStack WCAG). Use this card to identify which ruleset your scanner actually implements: if the scan flags this, you're on BrowserStack; if it stays silent while V-01 fires, you're on AAE MCAG."
            ) {
                NativeSpacedText(
                    text = "Default line spacing — this text uses the platform default line pitch to discriminate between the AAE and BrowserStack thresholds.",
                    sizeSp = 16f,
                    lineSpacingMultiplier = 1.0f,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            TextSpacingRuleCard(
                "V-07: XML declarative variants (inflated from res/layout/text_spacing_xml.xml)",
                "Three XML violations (android:lineSpacingMultiplier=0.6, android:letterSpacing=-0.15, jammed sibling TextViews) plus XML passes (comfortable line height, positive letter spacing, paragraphs separated by an empty-text View spacer). Why: developers set these via layout XML more often than via code — the rule catches declarative violations the same way, using the SAME captured char-geometry pipeline."
            ) {
                AndroidView(
                    factory = { ctx -> android.view.View.inflate(ctx, R.layout.text_spacing_xml, null) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // ------- CLASSIC WCAG 1.4.12 "content-loss" patterns -------
            // These are the WEB-original interpretation: with the user-agent bumping
            // spacing to line 1.5x / para 2x / letter 0.12x / word 0.16x, no content
            // or functionality is lost. Mobile-adapted: your layout must tolerate
            // increased spacing without clipping, overlap, or truncation.
            //
            // The AAE `TextSpacing` rule measures BASELINE spacing (MCAG floors),
            // so these next three MAY NOT trigger it — they're calibration cards
            // for manual / visual QA and for scanners that implement the classic
            // WCAG interpretation.
            val classicHeader = "Classic WCAG 1.4.12 (content-loss) patterns — visual/manual test"
            val classicDescription = "The AAE scanner measures baseline spacing (MCAG 3.3.1). The cases below demonstrate the ORIGINAL 1.4.12 interpretation — content lost when spacing grows. They may not trigger the AAE rule; screenshot before/after bumping spacing and diff visually."
            Text(
                classicHeader,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.clearAndSetSemantics { contentDescription = classicHeader }
            )
            Text(
                classicDescription,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.clearAndSetSemantics { contentDescription = classicDescription }
            )

            TextSpacingRuleCard(
                "V-08 (classic): hardcoded 24dp height clips descenders / second line",
                "TextView forced to layout_height=24dp holding a two-line string. Why: fixed heights are the #1 shape of 1.4.12 failure — bump lineHeight to 1.5x and the second line is cut off entirely; low-vision users lose content silently. Use wrap_content plus minHeight instead."
            ) {
                AndroidView(
                    modifier = Modifier.fillMaxWidth(),
                    factory = { ctx ->
                        android.widget.LinearLayout(ctx).apply {
                            orientation = android.widget.LinearLayout.VERTICAL
                            layoutParams = android.view.ViewGroup.LayoutParams(
                                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                                android.view.ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            importantForAccessibility = android.view.View.IMPORTANT_FOR_ACCESSIBILITY_YES
                            addView(android.widget.Space(ctx).apply {
                                layoutParams = android.widget.LinearLayout.LayoutParams(
                                    android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                                    1
                                )
                            })
                            addView(
                                android.widget.TextView(ctx).apply {
                                    layoutParams = android.widget.LinearLayout.LayoutParams(
                                        android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                                        (24 * resources.displayMetrics.density).toInt()
                                    )
                                    setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 16f)
                                    setLineSpacing(0f, 1.5f)
                                    setBackgroundColor(0xFFB3261E.toInt())
                                    setTextColor(0xFFFFFFFF.toInt())
                                    text = "This body copy is going to be clipped because the container height is smaller than the rendered text needs."
                                }
                            )
                        }
                    }
                )
            }
            TextSpacingRuleCard(
                "V-09 (classic): maxLines=2 + ellipsize=end — the ellipsis IS content loss",
                "TextView with android:maxLines=2 android:ellipsize=end holding a longer paragraph. Why: ellipsis-truncated text hides information from ALL users, and hides more with any spacing increase. WCAG treats truncation as content loss; if the ellipsis appears, the criterion has already failed."
            ) {
                AndroidView(
                    modifier = Modifier.fillMaxWidth(),
                    factory = { ctx ->
                        android.widget.LinearLayout(ctx).apply {
                            orientation = android.widget.LinearLayout.VERTICAL
                            layoutParams = android.view.ViewGroup.LayoutParams(
                                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                                android.view.ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            importantForAccessibility = android.view.View.IMPORTANT_FOR_ACCESSIBILITY_YES
                            addView(android.widget.Space(ctx).apply {
                                layoutParams = android.widget.LinearLayout.LayoutParams(
                                    android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                                    1
                                )
                            })
                            addView(
                                android.widget.TextView(ctx).apply {
                                    layoutParams = android.widget.LinearLayout.LayoutParams(
                                        android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                                        android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
                                    )
                                    setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 16f)
                                    maxLines = 2
                                    ellipsize = android.text.TextUtils.TruncateAt.END
                                    setBackgroundColor(0xFF6650A4.toInt())
                                    setTextColor(0xFFFFFFFF.toInt())
                                    setPadding(24, 16, 24, 16)
                                    text = "Screen readers announce only what is present; ellipsized text is content the user cannot reach with any assistive technology, so any ellipsis in body copy is a 1.4.12 failure by definition."
                                }
                            )
                        }
                    }
                )
            }
            TextSpacingRuleCard(
                "V-10 (classic): fixed-width Button truncates its own label",
                "Button pinned to layout_width=80dp with a long label. Why: fixed widths that can't expand to fit the label truncate at any translation, any font-scale change, and any spacing increase — a triple-failure surface. Use wrap_content or minWidth, and let the button grow."
            ) {
                AndroidView(
                    factory = { ctx ->
                        android.widget.Button(ctx).apply {
                            layoutParams = android.widget.LinearLayout.LayoutParams(
                                (80 * resources.displayMetrics.density).toInt(),
                                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                            text = "Confirm cancellation"
                            isSingleLine = true
                            ellipsize = android.text.TextUtils.TruncateAt.END
                        }
                    }
                )
            }
            TextSpacingRuleCard(
                "V-11 (classic): fixed-height Card + long content — overlap on spacing bump",
                "Fixed-height 64dp container wrapping stacked lines of copy. Why: fixed-height containers overflow when line/paragraph spacing grows; subsequent siblings overlap, and the scroll position no longer reaches the truncated content."
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .background(MaterialTheme.colorScheme.errorContainer, MaterialTheme.shapes.medium)
                        .padding(8.dp)
                ) {
                    Column {
                        Text(
                            "Line one of a critical notification message.",
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Text(
                            "Line two — likely to overflow the fixed-height container.",
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Text(
                            "Line three — definitely clipped, user cannot scroll to it.",
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }

            // ---------- PASSES ----------
            TextSpacingRuleCard("P-01: comfortable line height (1.4× multiplier)", "16sp text, lineSpacingMultiplier=1.4 → computed lineRatio ≈ 1.68 — well above AAE's 0.9× floor. letterSpacing=0.05em pushes space-advance to ~0.18× on typefaces with narrower native space glyphs (Huawei/EMUI) so the word floor is cleared with margin. Wrapper LinearLayout is explicitly important-for-accessibility, so paragraph sub-check finds no preceding sibling → ratioAbsent → N/A.") {
                NativeSpacedText(
                    text = "Comfortable line height gives dense body text room to breathe for low-vision readers.",
                    sizeSp = 16f,
                    lineSpacingMultiplier = 1.4f,
                    letterSpacingEm = 0.05f,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            TextSpacingRuleCard("P-02: positive letter spacing (+0.05em)", "18sp text, letterSpacing=+0.05em → space glyph advance ~0.30× font → passes 0.16× word floor. Line ratio ≈ 1.56 passes 0.9× floor.") {
                NativeSpacedText(
                    text = "Loose letter spacing preserves clear word boundaries for readers who need them.",
                    sizeSp = 18f,
                    lineSpacingMultiplier = 1.3f,
                    letterSpacingEm = 0.05f,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            TextSpacingRuleCard("P-03: paragraphs separated by android.widget.Space (64dp)", "Two paragraphs under one LinearLayout with a 64dp Space child. Space is an empty-text sibling → paragraph sub-check returns ratioAbsent → N/A. Line spacing 1.3× carries the verdict.") {
                NativeParagraphPair(
                    firstText = "First paragraph, long enough to occupy two full rendered lines for the scanner to compute a pitch.",
                    secondText = "Second paragraph, separated cleanly from the previous one by an empty-text Space sibling in the tree.",
                    sizeSp = 16f,
                    lineSpacingMultiplier = 1.3f,
                    spacerHeightDp = 64,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            TextSpacingRuleCard("P-04: short text (<20 chars) — rule N/A via length gate", "11-char text 'Short label' with deliberately crushed mult=0.5. AAE MAE requires text.trim() ≥ 20 chars for geometry capture — below the gate, no charTops emitted → IsApplicable returns false → rule silently skips despite the crushed spacing.") {
                NativeSpacedText(
                    text = "Short label",
                    sizeSp = 16f,
                    lineSpacingMultiplier = 0.5f,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            TextSpacingRuleCard("P-05: single-line long text — line sub-check N/A, word passes", "Short text 'Reads on one line.' — likely single-line. CharTops has 1 row → lineHeightRatio returns ratioAbsent → line sub-check N/A. Word sub-check passes at default space advance.") {
                NativeSpacedText(
                    text = "Reads on one line.",
                    sizeSp = 16f,
                    lineSpacingMultiplier = 1.2f,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        ScrollArrows(scrollState = scrollState)
    }
}

@Preview(showBackground = true)
@Composable
fun TextSpacingScreenPreview() {
    QA_Accessibility_AppTheme { TextSpacingScreen() }
}
