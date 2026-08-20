package com.example.qa_accessibility_app

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.qa_accessibility_app.ui.theme.QA_Accessibility_AppTheme
import kotlinx.coroutines.launch

// =====================================================================
// TE-24440 — Android Screen Reader Automation QA fixture (TalkBack).
//
// Android counterpart of the iOS TE-22788 fixture. The RFC renumbers the
// rules 1..7 (iOS used 1,2,4,5,6,7,8 with reading order excluded):
//   R1 Interactive Focus Order      — empty label on clickable elements
//   R2 Non-Interactive Focus Order  — empty label on images / text
//   R3 Meaningless Spoken Output    — generic labels ("button", "view")
//   R4 Image Missing Spoken Output  — images without labels
//   R5 Duplicate State Info         — selected/checked baked into the label
//   R6 Duplicate Type Info          — button/switch baked into the label
//   R7 Visible Label Mismatch       — visible text != spoken output
//
// TWO THINGS THIS FIXTURE IS BUILT TO ANSWER
//
// 1. Does the role leak into spokenOutput, as the trait does on iOS?
//    On iOS spokenOutput = label + value + trait, and the rule tested
//    spokenOutput == "". Because Apple always attaches a trait, an unlabelled
//    button could never satisfy it and R1/R2 were structurally unable to fire
//    for anything except text fields. RFC §4.2 defines the Android spoken
//    output as `contentDescription > text + role + state`, which has the same
//    shape — so an unlabelled Button may well speak "Button" and be skipped
//    for exactly the same reason. Every interactive class is therefore
//    represented below, unlike the iOS fixture where the unsatisfiable cases
//    were removed. Here they are the experiment.
//
// 2. Does classification survive Compose?
//    The SDK classifies on android.widget.* class names, but Compose creates
//    no real widgets — it renders one AndroidComposeView and SYNTHESISES a
//    className from the semantics Role (Role.Button -> android.widget.Button,
//    no role -> android.view.View). RFC §10 already flags clickable
//    android.view.View as a known limitation. Every rule therefore has both a
//    Compose case and a native android.widget.* case; where they disagree, the
//    classification map is the culprit, not the rule.
//
// Element id convention: sr_r<rule>_<v|p><nn>_<slug>, matching the iOS fixture.
// Compose ids are testTag values surfaced as resource-id via
// testTagsAsResourceId; native ids are real android:id values.
// =====================================================================

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ScreenReaderComposeRulesScreen(
    modifier: Modifier = Modifier,
    onNavigate: (AppDestinations) -> Unit = {}
) {
    val scrollState = rememberScrollState()
    var toggleA by remember { mutableStateOf(true) }
    var toggleB by remember { mutableStateOf(false) }
    var toggleC by remember { mutableStateOf(false) }
    var checkA by remember { mutableStateOf(true) }
    var checkB by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(16.dp)
                // Without this, testTag never reaches AccessibilityNodeInfo and every
                // Compose element arrives at the report with an empty resource-id.
                .semantics { testTagsAsResourceId = true },
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(modifier = Modifier.clearAndSetSemantics { }) {
                Text(
                    "Screen Reader — Compose page",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "TE-24440. Every element here is Compose, so its class name is SYNTHESISED " +
                        "from the semantics Role. Same rules as the XML page — compare the two " +
                        "reports element for element.",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // ---------- R1: Interactive Focus Order ----------
            SrSection(
                "R1 — Interactive Focus Order (2.4.3, critical)",
                "Clickable elements with NO accessible name. One card per control type, so the " +
                    "scan reveals which types R1 actually catches — if only EditText fires, the " +
                    "role is leaking into spokenOutput exactly as the trait does on iOS."
            )
            SrCard("R1 V-01: IconButton, icon contentDescription = null", "Role.Button -> android.widget.Button, no name — violation") {
                IconButton(
                    onClick = {},
                    modifier = Modifier.testTag("sr_r1_v01_iconbutton")
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = null)
                }
            }
            SrCard("R1 V-02: Clickable Box, no role and no label", "Renders as android.view.View — RFC §10 known limitation") {
                Box(
                    modifier = Modifier
                        .testTag("sr_r1_v02_clickable_box")
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(Color(0xFF90A4AE), RoundedCornerShape(6.dp))
                        .clickable { }
                )
            }
            SrCard("R1 V-08: Switch with no label", "Role.Switch, no contentDescription — violation") {
                Switch(
                    checked = toggleC,
                    onCheckedChange = { toggleC = it },
                    modifier = Modifier.testTag("sr_r1_v08_switch")
                )
            }
            SrCard("R1 V-09: Checkbox with no label", "Role.Checkbox, no contentDescription — violation") {
                Checkbox(
                    checked = checkB,
                    onCheckedChange = { checkB = it },
                    modifier = Modifier.testTag("sr_r1_v09_checkbox")
                )
            }
            SrCard("R1 P-01: IconButton WITH a label (pass)", "Same control, now named") {
                IconButton(
                    onClick = {},
                    modifier = Modifier.testTag("sr_r1_p01_iconbutton_labeled")
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                }
            }
            SrCard("R1 P-02: Text field WITH a label (pass)", "Named input — negative control for the EditText class") {
                TextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("Email address") },
                    modifier = Modifier.testTag("sr_r1_p02_textfield_labeled")
                )
            }

            // ---------- R2: Non-Interactive Focus Order ----------
            SrSection(
                "R2 — Non-Interactive Focus Order (2.4.3, serious)",
                "Non-interactive element reachable by TalkBack that announces nothing. On iOS the " +
                    "equivalent rule never fired once, because StaticText and Image both carry a " +
                    "trait. Watch whether the Android role does the same thing here."
            )
            SrCard("R2 V-01: Image with an EMPTY contentDescription", "Focusable but announces nothing — violation") {
                Image(
                    painter = painterResource(R.drawable.nike),
                    contentDescription = "",
                    modifier = Modifier
                        .testTag("sr_r2_v01_empty_image")
                        .size(64.dp)
                )
            }
            SrCard("R2 V-02: Sized Box forced into the tree, empty label", "Non-clickable, non-image, empty name — violation") {
                Box(
                    modifier = Modifier
                        .testTag("sr_r2_v02_empty_box")
                        .fillMaxWidth()
                        .height(24.dp)
                        .background(Color(0xFFCFD8DC))
                        .semantics { contentDescription = "" }
                )
            }
            SrCard("R2 V-03: Text with a blanked-out label", "Visible text, empty accessible name — violation") {
                Text(
                    "Important notice",
                    modifier = Modifier
                        .testTag("sr_r2_v03_blanked_text")
                        .semantics { contentDescription = "" }
                )
            }
            SrCard("R2 P-01: Text that announces normally (pass)", "Ordinary announced text") {
                Text("Section divider", modifier = Modifier.testTag("sr_r2_p01_labeled_text"))
            }

            // ---------- R3: Meaningless Spoken Output ----------
            SrSection(
                "R3 — Meaningless Spoken Output (1.1.1, serious)",
                "The accessible name is a generic word (button, view, image, icon, label, container)."
            )
            SrCard("R3 V-01: Button labelled \"Button\"", "Generic word as the whole name — violation") {
                Button(onClick = {}, modifier = Modifier.testTag("sr_r3_v01_generic_button")) {
                    Text("Button")
                }
            }
            SrCard("R3 V-02: Image described as \"image\"", "Generic word — violation (also an R4 case)") {
                Image(
                    painter = painterResource(R.drawable.pepsi),
                    contentDescription = "image",
                    modifier = Modifier
                        .testTag("sr_r3_v02_generic_image")
                        .size(64.dp)
                )
            }
            SrCard("R3 V-05: Element described as \"container\"", "Generic word — violation") {
                Box(
                    modifier = Modifier
                        .testTag("sr_r3_v05_generic_container")
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(Color(0xFFB0BEC5))
                        .semantics { contentDescription = "container" }
                )
            }
            SrCard("R3 P-01: Descriptive label (pass)", "\"Submit order\" is meaningful") {
                Button(onClick = {}, modifier = Modifier.testTag("sr_r3_p01_descriptive")) {
                    Text("Submit order")
                }
            }

            // ---------- R4: Image Missing Spoken Output ----------
            SrSection(
                "R4 — Image Missing Spoken Output (1.1.1, serious)",
                "Image elements with an empty or generic name (image, icon, picture, photo, img). " +
                    "Overlaps R2 by design — record which rule actually claims each element."
            )
            SrCard("R4 V-01: Image described as \"photo\"", "Generic image word — violation") {
                Image(
                    painter = painterResource(R.drawable.chanel),
                    contentDescription = "photo",
                    modifier = Modifier
                        .testTag("sr_r4_v01_generic_photo")
                        .size(64.dp)
                )
            }
            SrCard("R4 V-02: Image described as \"icon\"", "Generic image word — violation") {
                Image(
                    painter = painterResource(R.drawable.nike),
                    contentDescription = "icon",
                    modifier = Modifier
                        .testTag("sr_r4_v02_generic_icon")
                        .size(64.dp)
                )
            }
            SrCard("R4 P-01: Properly described image (pass)", "\"Nike logo\" is meaningful") {
                Image(
                    painter = painterResource(R.drawable.nike),
                    contentDescription = "Nike logo",
                    modifier = Modifier
                        .testTag("sr_r4_p01_described")
                        .size(64.dp)
                )
            }

            // ---------- R5: Duplicate State Info ----------
            SrSection(
                "R5 — Duplicate State Info (4.1.2, moderate)",
                "The label bakes in state TalkBack already announces (selected/checked/disabled; " +
                    "on/off only on toggles). Word-boundary matching must hold: \"Sony\" is not \"on\"."
            )
            SrCard("R5 V-01: Switch labelled \"WiFi on\"", "\"on\" on a real switch — violation") {
                Switch(
                    checked = toggleA,
                    onCheckedChange = { toggleA = it },
                    modifier = Modifier
                        .testTag("sr_r5_v01_switch_on")
                        .semantics { contentDescription = "WiFi on" }
                )
            }
            SrCard("R5 V-02: Switch labelled \"Bluetooth off\"", "\"off\" on a real switch — violation") {
                Switch(
                    checked = toggleB,
                    onCheckedChange = { toggleB = it },
                    modifier = Modifier
                        .testTag("sr_r5_v02_switch_off")
                        .semantics { contentDescription = "Bluetooth off" }
                )
            }
            SrCard("R5 V-03: Checkbox labelled \"Task checked\"", "\"checked\" in the label — violation") {
                Checkbox(
                    checked = checkA,
                    onCheckedChange = { checkA = it },
                    modifier = Modifier
                        .testTag("sr_r5_v03_checkbox_checked")
                        .semantics { contentDescription = "Task checked" }
                )
            }
            SrCard("R5 V-04: Button labelled \"Item selected\"", "\"selected\" applies to any element — violation") {
                Button(onClick = {}, modifier = Modifier.testTag("sr_r5_v04_selected")) {
                    Text("Item selected")
                }
            }
            SrCard("R5 V-05: Button labelled \"Sound disabled\"", "\"disabled\" in the label — violation") {
                Button(onClick = {}, modifier = Modifier.testTag("sr_r5_v05_disabled")) {
                    Text("Sound disabled")
                }
            }
            SrCard("R5 P-01: \"Sony headphones\" (pass)", "Word boundary: \"Sony\" must NOT match \"on\"") {
                Button(onClick = {}, modifier = Modifier.testTag("sr_r5_p01_sony")) {
                    Text("Sony headphones")
                }
            }
            SrCard("R5 P-02: \"on\" on plain text (pass)", "Not a toggle, so \"on\" must NOT fire") {
                Text(
                    "Free shipping on orders over \$50",
                    modifier = Modifier.testTag("sr_r5_p02_shipping")
                )
            }

            // ---------- R6: Duplicate Type Info ----------
            SrSection(
                "R6 — Duplicate Type Info (4.1.2, moderate)",
                "The label repeats the element type TalkBack already appends (button, link, switch, checkbox)."
            )
            SrCard("R6 V-01: Button labelled \"Submit Button\"", "Type word in the label — violation") {
                Button(onClick = {}, modifier = Modifier.testTag("sr_r6_v01_button_word")) {
                    Text("Submit Button")
                }
            }
            SrCard("R6 V-02: Button labelled \"Settings Link\"", "Type word \"link\" — violation") {
                Button(onClick = {}, modifier = Modifier.testTag("sr_r6_v02_link_word")) {
                    Text("Settings Link")
                }
            }
            SrCard("R6 V-03: Switch labelled \"WiFi Switch\"", "Type word \"switch\" — violation") {
                Switch(
                    checked = toggleA,
                    onCheckedChange = { toggleA = it },
                    modifier = Modifier
                        .testTag("sr_r6_v03_switch_word")
                        .semantics { contentDescription = "WiFi Switch" }
                )
            }
            SrCard("R6 V-04: Checkbox labelled \"Terms Checkbox\"", "Type word \"checkbox\" — violation") {
                Checkbox(
                    checked = checkA,
                    onCheckedChange = { checkA = it },
                    modifier = Modifier
                        .testTag("sr_r6_v04_checkbox_word")
                        .semantics { contentDescription = "Terms Checkbox" }
                )
            }
            SrCard("R6 P-01: \"Add to Cart\" (pass)", "No type word") {
                Button(onClick = {}, modifier = Modifier.testTag("sr_r6_p01_clean")) {
                    Text("Add to Cart")
                }
            }
            SrCard("R6 P-02: \"Onboarding\" (pass)", "Substring trap: must not match \"on\" or a type word") {
                Button(onClick = {}, modifier = Modifier.testTag("sr_r6_p02_onboarding")) {
                    Text("Onboarding")
                }
            }

            // ---------- R7: Visible Label Mismatch ----------
            SrSection(
                "R7 — Visible Label Mismatch (2.5.3, serious)",
                "Visible text and spoken output disagree. Android reads visible text from " +
                    "node.getText(), so no OCR is involved — unlike iOS, this should be exact."
            )
            SrCard("R7 V-01: Reads \"Buy Now\", speaks something else", "Visible text not contained in the spoken name — violation") {
                Button(
                    onClick = {},
                    modifier = Modifier
                        .testTag("sr_r7_v01_buy_now")
                        .semantics { contentDescription = "Complete purchase immediately" }
                ) { Text("Buy Now") }
            }
            SrCard("R7 V-02: Reads \"Next\", speaks \"Continue\"", "Different word entirely — violation") {
                Button(
                    onClick = {},
                    modifier = Modifier
                        .testTag("sr_r7_v02_next")
                        .semantics { contentDescription = "Continue" }
                ) { Text("Next") }
            }
            SrCard("R7 P-01: Reads \"Send\", speaks \"Send message\" (pass)", "Visible text is contained in the spoken name") {
                Button(
                    onClick = {},
                    modifier = Modifier
                        .testTag("sr_r7_p01_send")
                        .semantics { contentDescription = "Send message" }
                ) { Text("Send") }
            }
            SrCard("R7 P-02: Visible text == spoken output (pass)", "Identical — must be skipped") {
                Button(onClick = {}, modifier = Modifier.testTag("sr_r7_p02_save")) {
                    Text("Save")
                }
            }

            // Footer navigation to the other fixtures, mirroring the iOS screen. Left IN the
            // accessibility tree so a manual TalkBack walk can reach them; the names are
            // deliberately clean and must not trigger any rule.
            SrSection("Other screen-reader fixtures", "")
            Button(
                onClick = { onNavigate(AppDestinations.SCREEN_READER_LINEAR_COMPOSE) },
                modifier = Modifier
                    .testTag("sr_nav_linear_compose")
                    .fillMaxWidth()
            ) { Text("Open Linear Navigation (Compose)") }
            Button(
                onClick = { onNavigate(AppDestinations.SCREEN_READER_PHASH_SIMILAR) },
                modifier = Modifier
                    .testTag("sr_nav_phash_similar_cmp")
                    .fillMaxWidth()
            ) { Text("Open pHash Similar Screens") }
            Button(
                onClick = { onNavigate(AppDestinations.SCREEN_READER_PHASH_ANIMATED) },
                modifier = Modifier
                    .testTag("sr_nav_phash_animated_cmp")
                    .fillMaxWidth()
            ) { Text("Open pHash Animated Screen") }

            Spacer(modifier = Modifier.height(24.dp))
        }
        SrScrollArrows(scrollState = scrollState)
    }
}

// =====================================================================
// Linear-navigation fixture: content well beyond one viewport, plus horizontal
// carousels. The violations at the bottom and inside the carousels can only be
// reported if linear-navigation mode actually scrolled the whole page — in
// auto-report mode they should be absent unless a test scrolls there.
// =====================================================================

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ScreenReaderLinearNavComposeScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    val carouselScroll = rememberScrollState()

    Column(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(16.dp)
                .semantics { testTagsAsResourceId = true },
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(modifier = Modifier.clearAndSetSemantics { }) {
                Text(
                    "Linear Navigation — Compose page",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Violations sit at the top, at the bottom and inside the carousel. " +
                        "Linear-navigation mode must scroll the full page and swipe the carousel " +
                        "to find them all; auto-report mode should only see what is on screen.",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // TOP violation — visible without scrolling.
            SrCard("TOP: unlabelled input (R1)", "On screen from the start") {
                TextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier.testTag("sr_lin_cmp_top_r1")
                )
            }

            // Compose horizontal carousel. Compose maps a horizontally scrollable
            // container to android.widget.HorizontalScrollView, which is one of the
            // classes RFC §4.2 looks for — worth confirming it really does.
            Text("Compose carousel (swipe →)", fontWeight = FontWeight.SemiBold)
            Row(
                modifier = Modifier
                    .testTag("sr_lin_cmp_carousel")
                    .fillMaxWidth()
                    .horizontalScroll(carouselScroll),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 20 cards, with violations planted at three depths. Card 1 is visible at rest,
                // so card 8 needs a couple of swipes and cards 18/20 need the carousel taken almost
                // to its end. If the shallow violation is reported and the deep ones are not, the
                // horizontal pass stopped short rather than exhausting the carousel — which a
                // single violation could never tell us.
                repeat(20) { i ->
                    val index = i + 1
                    val tag = when (index) {
                        8 -> "sr_lin_cmp_carousel_r4"
                        18 -> "sr_lin_cmp_carousel_r4b"
                        20 -> "sr_lin_cmp_carousel_r6"
                        else -> "sr_lin_cmp_carousel_$index"
                    }
                    val label = when (index) {
                        8 -> "image"            // R4 — generic name
                        18 -> ""                // R2/R4 — no name at all
                        20 -> "Checkout Button" // R6 — label repeats the element type
                        else -> "Card $index"
                    }
                    Box(
                        modifier = Modifier
                            .testTag(tag)
                            .width(120.dp)
                            .height(100.dp)
                            .background(Color(0xFFECEFF1), RoundedCornerShape(8.dp))
                            .semantics { contentDescription = label },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Card $index")
                    }
                }
            }

            // Filler so the bottom violations sit far outside the first viewport.
            repeat(18) { i ->
                Text(
                    "Filler row ${i + 1} — keep scrolling",
                    modifier = Modifier
                        .testTag("sr_lin_cmp_filler_${i + 1}")
                        .fillMaxWidth()
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(6.dp))
                        .padding(12.dp)
                )
            }

            // BOTTOM violations — only reachable after a full vertical scroll.
            SrCard("BOTTOM: \"Add to Cart Button\" (R6)", "Far off-screen") {
                Button(onClick = {}, modifier = Modifier.testTag("sr_lin_cmp_bottom_r6")) {
                    Text("Add to Cart Button")
                }
            }
            SrCard("BOTTOM: reads \"Buy Now\", speaks otherwise (R7)", "Far off-screen") {
                Button(
                    onClick = {},
                    modifier = Modifier
                        .testTag("sr_lin_cmp_bottom_r7")
                        .semantics { contentDescription = "Complete purchase immediately" }
                ) { Text("Buy Now") }
            }
            SrCard("BOTTOM: image with no name (R4)", "Far off-screen") {
                Image(
                    painter = painterResource(R.drawable.pepsi),
                    contentDescription = "",
                    modifier = Modifier
                        .testTag("sr_lin_cmp_bottom_r4")
                        .size(64.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
        SrScrollArrows(scrollState = scrollState)
    }
}

// =====================================================================
// pHash false-SKIP risk: two states that look nearly identical but differ
// meaningfully. An 8x8 average hash with a >95% similarity skip could treat the
// second state as already-seen and never scan the newly appeared element.
// =====================================================================

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ScreenReaderPHashSimilarScreen(modifier: Modifier = Modifier) {
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .semantics { testTagsAsResourceId = true },
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("pHash — Similar Screens", style = MaterialTheme.typography.titleLarge)
        Text(
            "Tap the button: the screen changes meaningfully (an error appears) while looking " +
                "roughly 95% the same. A pHash that over-skips would MISS the new element.",
            style = MaterialTheme.typography.bodySmall
        )

        TextField(
            value = "",
            onValueChange = {},
            label = { Text("Email") },
            modifier = Modifier.testTag("sr_phash_email_field")
        )

        if (showError) {
            Text(
                "Error: enter a valid email",
                color = Color(0xFFD32F2F),
                modifier = Modifier.testTag("sr_phash_error_label")
            )
        }

        Button(
            onClick = { showError = !showError },
            modifier = Modifier.testTag("sr_phash_toggle_error")
        ) {
            Text(if (showError) "Hide error" else "Show error")
        }
    }
}

// =====================================================================
// pHash false-SCAN risk: pixels change constantly while the accessibility
// content never does. A small spinner barely moves an 8x8 average hash, so the
// stressors below shift large blocks of luminance ACROSS the grid instead.
// =====================================================================

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ScreenReaderPHashAnimatedScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    val transition = rememberInfiniteTransition(label = "phash")

    // A-01: a wide band travelling across the full width — whole columns of the
    // hash grid change as it moves.
    val sweep by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sweep"
    )

    // A-02: a checkerboard inverting light and dark — flips many of the 64 cells
    // at once. This is the maximally adversarial case for an average hash.
    val flip by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flip"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(16.dp)
            .semantics { testTagsAsResourceId = true },
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("pHash — Animated Screen", style = MaterialTheme.typography.titleLarge)
        Text(
            "The accessibility content is static but a large share of the pixels changes every " +
                "frame. A pHash that under-skips would re-scan this same screen repeatedly, " +
                "burning linear-navigation budget and re-reporting the same elements.",
            style = MaterialTheme.typography.bodySmall
        )

        // Sweeping band.
        Box(
            modifier = Modifier
                .testTag("sr_phash_anim_sweep")
                .fillMaxWidth()
                .height(150.dp)
                .background(Color(0xFFECEFF1), RoundedCornerShape(8.dp))
                .semantics { contentDescription = "Loading banner" }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.35f)
                    .height(150.dp)
                    .padding(start = (sweep * 200).dp)
                    .background(Color(0xFF263238), RoundedCornerShape(8.dp))
            )
        }

        // Inverting checkerboard.
        Column(
            modifier = Modifier
                .testTag("sr_phash_anim_tiles")
                .semantics { contentDescription = "Activity grid" },
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            repeat(3) { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    repeat(4) { col ->
                        val evenCell = (row + col) % 2 == 0
                        val dark = if (evenCell) flip > 0.5f else flip <= 0.5f
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (dark) Color.Black else Color.White)
                        )
                    }
                }
            }
        }

        // Static content — must be reported exactly ONCE however many times the
        // animation tempts the engine into re-scanning.
        Button(onClick = {}, modifier = Modifier.testTag("sr_phash_static_button")) {
            Text("Confirm order")
        }

        // Dedup probe: a stable R6 violation on an animated screen. R6 is expected to
        // fire, so it will appear in the report — and it must appear exactly once.
        // A second occurrence means a re-scan slipped past dedup, which is the real
        // user-visible damage of a false scan.
        Button(
            onClick = {},
            modifier = Modifier
                .testTag("sr_phash_anim_r6_button")
                .semantics { contentDescription = "Submit Button" }
        ) { Text("Submit") }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// ---------------------------------------------------------------------
// Shared helpers
// ---------------------------------------------------------------------

@Composable
private fun SrSection(title: String, subtitle: String) {
    // clearAndSetSemantics {} removes this prose from the accessibility tree. On the iOS run the
    // equivalent card text was itself flagged — a note reading 'Label contains "Selected"' fired
    // the duplicate-state rule — so the report filled with findings about the fixture's own
    // captions. Only the elements under test are announced here.
    Column(
        modifier = Modifier
            .padding(top = 8.dp)
            .clearAndSetSemantics { },
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium, color = Color(0xFFC62828))
        Text(subtitle, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun SrCard(title: String, note: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF2F2F7), RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(modifier = Modifier.clearAndSetSemantics { }) {
            Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            Text(note, style = MaterialTheme.typography.bodySmall)
        }
        content()
    }
}

@Composable
private fun SrScrollArrows(
    scrollState: ScrollState,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val stepPx = with(LocalDensity.current) { 600.dp.roundToPx() }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FloatingActionButton(onClick = {
            scope.launch {
                scrollState.animateScrollTo((scrollState.value - stepPx).coerceAtLeast(0))
            }
        }) {
            Icon(
                Icons.Default.KeyboardArrowUp,
                contentDescription = "Scroll up"
            )
        }
        FloatingActionButton(onClick = {
            scope.launch {
                scrollState.animateScrollTo(
                    (scrollState.value + stepPx).coerceAtMost(scrollState.maxValue)
                )
            }
        }) {
            Icon(
                Icons.Default.KeyboardArrowDown,
                contentDescription = "Scroll down"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenReaderComposeRulesScreenPreview() {
    QA_Accessibility_AppTheme { ScreenReaderComposeRulesScreen() }
}
