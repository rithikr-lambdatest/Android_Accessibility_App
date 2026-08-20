# TE-24440 — Android Screen Reader Automation: QA Test Plan

**Feature:** Android (TalkBack) Screen Reader Automation — Auto-Report & Linear Navigation.
**RFC:** `LambdatestIncPrivate/internal-docs` PR #2750 →
`docs/Features-RFC/RFC-TE-24440-android-screen-reader-automation.md` (merged).
**Ticket:** [TE-24440](https://lambdatest.atlassian.net/browse/TE-24440) — status *Ready for QA*, QA = Rithik.
**Sibling:** iOS [TE-22788](https://lambdatest.atlassian.net/browse/TE-22788) (Deployed) — see
`TE-22788-ScreenReader-QA-TestPlan.md` in the iOS sample repo. This plan deliberately re-runs the
defects found there, because the Android design shares the mechanism that caused them.

**Fixture app:** `QA_Accessibility_App` — **Compose and XML kept on separate pages**. Only the two
rules pages sit on the home grid; the sub-fixtures are reached from a footer at the bottom of each
rules page, mirroring the iOS app. Back returns to the rules page that launched them.

```
Home grid
├── SR Rules (Compose) ──footer──┬── SR Linear Nav (Compose)
│                                ├── SR pHash Similar
│                                └── SR pHash Animated
└── SR Rules (XML) ──────footer──┬── SR Linear Nav (XML)
                                 ├── SR pHash Similar
                                 └── SR pHash Animated
```

| Screen | Framework | Source |
|---|---|---|
| `SR Rules (Compose)` | Compose | `ScreenReaderScreens.kt` |
| `SR Rules (XML)` | native `android.widget.*` | `ScreenReaderXmlRulesActivity` → `res/layout/screen_reader_native.xml` |
| `SR Linear Nav (Compose)` | Compose | `ScreenReaderScreens.kt` |
| `SR Linear Nav (XML)` | native | `ScreenReaderLinearNavXmlActivity` → `res/layout/screen_reader_linear_nav.xml` + `screen_reader_carousel.xml` |
| `SR pHash Similar` | Compose | `ScreenReaderScreens.kt` |
| `SR pHash Animated` | Compose | `ScreenReaderScreens.kt` |

**Footer navigation elements are IN the accessibility tree** (unlike the captions) so a manual
TalkBack walk can reach the sub-fixtures. They are expected to appear in every rules-page report as
**PASS** entries, and their names are deliberately clean — if any of them is flagged, that is itself
a finding:

| Case ID | Page | Name | Expected |
|---|---|---|---|
| `sr_nav_linear_compose` | Compose | "Open Linear Navigation (Compose)" | **PASS** |
| `sr_nav_phash_similar_cmp` | Compose | "Open pHash Similar Screens" | **PASS** |
| `sr_nav_phash_animated_cmp` | Compose | "Open pHash Animated Screen" | **PASS** |
| `sr_nav_linear_xml` | XML | "Open Linear Navigation (XML)" | **PASS** |
| `sr_nav_phash_similar` | XML | "Open pHash Similar Screens" | **PASS** |
| `sr_nav_phash_animated` | XML | "Open pHash Animated Screen" | **PASS** |

The XML footer buttons are **native `android.widget.Button`s wired via `findViewById`**, not
Compose, so the XML page stays 100% `android.widget.*` and its report is purely native.

**RCA fixed 2026-08-19 — XML pages are now STANDALONE ACTIVITIES.** The first scans returned zero
findings for the XML elements. Dev RCA: the layouts were inflated inside a Compose `AndroidView`,
so every native view sat underneath an `AndroidComposeView` in the accessibility tree and MAE's
`walkTree` never descended into them. The XML fixtures are now `setContentView` activities
(`ScreenReaderXmlRulesActivity`, `ScreenReaderLinearNavXmlActivity`) — the entire window is
`android.widget.*`, which is also how real View-based apps are shaped. Consequences for testing:
- The home-grid "SR Rules (XML)" button starts the activity directly; its footer starts the
  linear-nav activity; the pHash footer buttons deep-link into `MainActivity` (which finishes back
  to the activity on back).
- The XML activities have **no in-app "Go back" button** — use system back (`driver.back()`, which
  is itself an auto-report trigger command).
- Scroll arrows on the XML pages are native `ImageButton`s with the same "Scroll up"/"Scroll down"
  contentDescriptions as the Compose FABs, so script locators are unchanged.
- **Regression check to add:** re-run the XML scan and confirm the elements are now captured; and
  keep the original hybrid case in mind — an XML view inside a Compose app is a REAL pattern
  (maps, ads, WebViews), so "Compose-hosted native views are invisible to the walker" deserves its
  own ticket even though our fixture no longer depends on it.

**Why separate pages.** Auto-report captures one snapshot per trigger command, so a mixed screen
yields a single report containing both frameworks and every finding has to be traced back by id.
One framework per page makes each report attributable on sight, and lets the two scroll
implementations (Compose scrollable semantics vs a native `ScrollView`) be compared independently
for linear navigation and pHash. The pHash pages stay Compose-only on purpose — they test
screen-change detection, which is pixel-based and framework-independent, so an XML twin would add
cases without adding information.

**Both rules pages carry the same structure** — a heading per rule (name, WCAG criterion,
severity) followed by one card per element giving the case ID and the expected verdict. The XML
page renders these as `TextView`s, the Compose page as `Text`s. Reading either page tells you what
each element is for without cross-referencing this document.

**Fixture chrome is hidden from the accessibility tree** (`clearAndSetSemantics {}` in Compose,
`importantForAccessibility="no"` in XML). On the iOS run the equivalent card captions were
themselves reported as violations — a note reading `Label contains "Selected"` fired the
duplicate-state rule — which buried the real findings. Only the elements under test are announced,
so every element in the report should map to a case ID below. **An element in the report that is
not in this plan is itself worth investigating.**

---

## 0. Ground-truth method

Pass/fail for a case = (scan verdict matches expected verdict) **AND** (`spokenOutput`,
`visibleText`, `boundsInScreen`, `traversalIndex` are all correct).

**Record on every element, not just the failures** — `class`, `contentDescription`, `text`,
`spokenOutput`, `resource-id`. The iOS investigation was only settled by comparing captured
properties, and the two open questions below can only be answered from them.

### Identifiers

| Element source | How the id is set | Where it surfaces |
|---|---|---|
| Compose | `Modifier.testTag("sr_…")` + `semantics { testTagsAsResourceId = true }` on the screen root | `resource-id` |
| Native XML | real `android:id="@+id/sr_…"` | `resource-id` |

If Compose elements arrive with an empty `resource-id`, `testTagsAsResourceId` is not being
honoured — that is a fixture/plumbing problem, not an engine defect. Native ids are unconditional,
so they are the fallback way to identify an element.

---

## Q1. RESOLVED (2026-08-20) — R1/R2 redefined as REACHABILITY rules

The original headline question (does the role leak into `spokenOutput`, making empty-name
detection unsatisfiable, as the trait did on iOS?) is **moot**: dev changed the R1/R2 logic.
The rules no longer test the accessible name at all. New semantics:

> **"Screen Reader Focus Missing for Interactive Element"** — an interactive element exists in
> the accessibility tree but is **not reachable during screen reader linear navigation**.
> HowToFix: *"…On Android, verify importantForAccessibility is set to yes or auto and ensure
> parent containers do not block descendants."*

Trigger conditions (Android): `importantForAccessibility="no"` on the element, or a parent with
`importantForAccessibility="noHideDescendants"`. MAE sees the element in the view tree, routes it
to `skippedInteractive` / `skippedNonInteractive` instead of `traversalOrder`, and R1/R2 fire from
those lists. iOS equivalent: `isAccessibilityElement = false` / parent
`accessibilityElementsHidden = true`.

**Verification consequence:** hidden elements NEVER appear in UiAutomator dumps, so Appium
scripts cannot locate or assert them — the ONLY place they are observable is the scan report
(and MAE's skipped lists). Probes in scripts must always be reachable elements.

## Q2. Does classification survive Compose?

Compose creates no real widgets — it renders a single `AndroidComposeView` and **synthesises** a
`className` from the semantics `Role` (`Role.Button` → `android.widget.Button`; **no role** →
`android.view.View`). The SDK classifies on `android.widget.*` names, so a Compose element and its
native twin can be classified differently while looking identical on screen.

Every rule below therefore has both a Compose case and a native `android.widget.*` case, **on two
separate pages** — `SR Rules (Compose)` and `SR Rules (XML)`. Run them as two separate tests and
diff the reports: any disagreement between a pair isolates the classification map as the cause,
and because each report contains exactly one framework, the comparison needs no id bookkeeping.

This matters beyond our fixture: **customer apps written in Compose are the common case**, so if
classification only works for native widgets, the feature under-reports for most modern apps.

---

## 1. Rule coverage matrix

RFC numbering (1–7; iOS used 1,2,4,5,6,7,8 with reading order excluded — **do not mix the two
numberings when filing defects**).

### R1 — Screen Reader Focus Missing for Interactive Element (2.4.3, critical)

Fixture: `ScreenReaderXmlApp` → SR Rules.

| Case ID | Construction | Expected |
|---|---|---|
| `sr_r1_v01_parent_hidden_button` | Button inside `noHideDescendants` parent (`sr_r1_v01_hiding_parent`) | **FAIL** — in `skippedInteractive` |
| `sr_r1_v02_self_hidden_button` | Button, `importantForAccessibility="no"`, clickable | **FAIL** |
| `sr_r1_v03_self_hidden_edittext` | EditText, `importantForAccessibility="no"` | **FAIL** |
| `sr_r1_v04_parent_hidden_switch` | Switch inside `noHideDescendants` parent | **FAIL** |
| `sr_r1_p01_reachable_button` | ordinary Button, `importantForAccessibility="auto"` | **PASS** — in `traversalOrder` |
| `sr_r1_p02_reachable_edittext` | ordinary EditText with hint | **PASS** |

### R2 — Screen Reader Focus Missing for Non-Interactive Element (2.4.3, serious)

| Case ID | Construction | Expected |
|---|---|---|
| `sr_r2_v01_hidden_image` | ImageView WITH contentDescription "Product image", `importantForAccessibility="no"` | **FAIL** — in `skippedNonInteractive` |
| `sr_r2_v02_parent_hidden_text` | TextView inside `noHideDescendants` parent (`sr_r2_v02_hiding_parent`) | **FAIL** |
| `sr_r2_v03_self_hidden_text` | TextView, `importantForAccessibility="no"` | **FAIL** |
| `sr_r2_p01_reachable_text` | ordinary TextView | **PASS** |
| `sr_r2_p02_reachable_image` | described, reachable ImageView | **PASS** |

Note: the old empty-name / generic-name image cases now belong ONLY to R3/R4 — record which rule
claims each element and flag any double-reporting.

### R3 — Meaningless Spoken Output (1.1.1, serious)

| Case ID | Name under test | Expected |
|---|---|---|
| `sr_r3_v01_generic_button` | "Button" | **FAIL** |
| `sr_r3_v02_generic_image` | "image" | **FAIL** |
| `sr_r3_v05_generic_container` | "container" | **FAIL** |
| `sr_r3_v03_native_generic_view` | "View" | **FAIL** |
| `sr_r3_v04_native_generic_icon` | "icon" | **FAIL** |
| `sr_r3_p01_descriptive` | "Submit order" | **PASS** |

### R4 — Image Missing Spoken Output (1.1.1, serious)

Overlaps R2 by design. **Record which rule claims each element** — on iOS the empty-label image
was claimed by the image rule, not the focus rule.

| Case ID | Name under test | Expected |
|---|---|---|
| `sr_r4_v01_generic_photo` | "photo" | **FAIL** |
| `sr_r4_v02_generic_icon` | "icon" | **FAIL** |
| `sr_r4_v03_native_generic_image` | "image" | **FAIL** |
| `sr_r4_p01_described` | "Nike logo" | **PASS** |
| `sr_r4_p02_native_described` | "Chanel logo" | **PASS** |

### R5 — Duplicate State Info (4.1.2, moderate)

| Case ID | Name under test | Expected |
|---|---|---|
| `sr_r5_v01_switch_on` | Switch "WiFi on" | **FAIL** — "on", toggle-only keyword |
| `sr_r5_v02_switch_off` | Switch "Bluetooth off" | **FAIL** |
| `sr_r5_v03_checkbox_checked` | Checkbox "Task checked" | **FAIL** |
| `sr_r5_v04_selected` | Button "Item selected" | **FAIL** — any element |
| `sr_r5_v05_disabled` | Button "Sound disabled" | **FAIL** |
| `sr_r5_v06_native_switch_on` | native Switch "WiFi on" | **FAIL** |
| `sr_r5_v07_native_checkbox_checked` | native CheckBox "Task checked" | **FAIL** |
| `sr_r5_p01_sony` | "Sony headphones" | **PASS** — word boundary, "Sony" ≠ "on" |
| `sr_r5_p02_shipping` | "Free shipping on orders over $50" | **PASS** — "on" on a non-toggle |

### R6 — Duplicate Type Info (4.1.2, moderate)

| Case ID | Name under test | Expected |
|---|---|---|
| `sr_r6_v01_button_word` | "Submit Button" | **FAIL** |
| `sr_r6_v02_link_word` | "Settings Link" | **FAIL** |
| `sr_r6_v03_switch_word` | "WiFi Switch" | **FAIL** |
| `sr_r6_v04_checkbox_word` | "Terms Checkbox" | **FAIL** |
| `sr_r6_v05_native_button_word` | native "Submit Button" | **FAIL** |
| `sr_r6_v06_native_switch_word` | native "WiFi Switch" | **FAIL** |
| `sr_r6_p01_clean` | "Add to Cart" | **PASS** |
| `sr_r6_p02_onboarding` | "Onboarding" | **PASS** — substring trap |

### R7 — Visible Label Mismatch (2.5.3, serious)

Android reads visible text from `node.getText()`, **not OCR** — so unlike iOS this should be exact,
with no script/rendering excuses. Any miss here is a real logic bug.

All native `Button`s in the fixture set `android:textAllCaps="false"`. The framework Material theme
otherwise renders (and reports to accessibility) button text in ALL CAPS, which would silently
change the ground truth — e.g. R7 P-03 would compare "SEND" against "Send message" and a
case-sensitive containment check would turn a pass case into a false violation. With the transform
off, `getText()` is exactly the literal in the layout. (Real customer apps DO ship all-caps
buttons, so how the engine handles case is still worth a question to the dev — but the fixture
keeps it out of the baseline.)

| Case ID | Visible / spoken | Expected |
|---|---|---|
| `sr_r7_v01_buy_now` | "Buy Now" / "Complete purchase immediately" | **FAIL** |
| `sr_r7_v02_next` | "Next" / "Continue" | **FAIL** |
| `sr_r7_v03_native_mismatch` | native "Buy Now" / "Complete purchase immediately" | **FAIL** |
| `sr_r7_p01_send` | "Send" / "Send message" | **PASS** — visible ⊂ spoken |
| `sr_r7_p02_save` | "Save" / "Save" | **PASS** — identical, skipped |
| `sr_r7_p03_native_contains` | native "Send" / "Send message" | **PASS** |

---

## 2. Linear navigation — `SR Linear Nav`

Content extends well past one viewport and includes two carousels. Auto-report mode should see
only what is on screen; linear-navigation mode must scroll the whole page and swipe both carousels.

Run the two pages as **separate tests** and compare.

### `SR Linear Nav (Compose)`

| Case ID | Position | Expected |
|---|---|---|
| `sr_lin_cmp_top_r1` | on screen at load | found in **both** modes |
| `sr_lin_cmp_carousel_r4` | card 8 of 20 — generic name "image" | **linear mode only**, ~2 swipes in |
| `sr_lin_cmp_carousel_r4b` | card 18 of 20 — no name at all | **linear mode only**, near the far end |
| `sr_lin_cmp_carousel_r6` | card 20 of 20 — "Checkout Button" | **linear mode only**, last card |
| `sr_lin_cmp_bottom_r6` | far below the fold | **linear mode only** |
| `sr_lin_cmp_bottom_r7` | far below the fold | **linear mode only** |
| `sr_lin_cmp_bottom_r4` | far below the fold | **linear mode only** |

### `SR Linear Nav (XML)`

| Case ID | Position | Expected |
|---|---|---|
| `sr_lin_xml_top_input` | on screen at load | **PASS** — reachable input, also the script's restore probe |
| `sr_lin_xml_top_r1` | on screen at load — hidden Button "Flash Sale" (`importantForAccessibility="no"`) | **FAIL (R1)** in **both** modes, via `skippedInteractive` |
| `sr_lin_xml_card_1` … `sr_lin_xml_card_15` | 15 product cards, **every one a different drawable** | **PASS** — all properly described |
| `sr_lin_xml_carousel_r4` | after the 15 cards — generic name "image" | **linear mode only** |
| `sr_lin_xml_carousel_r4b` | next — image with no name at all | **linear mode only** |
| `sr_lin_xml_carousel_r6` | next — "Checkout Button" | **linear mode only** |
| `sr_lin_xml_carousel_r1` | **last item** — HIDDEN ImageButton | **FAIL (R1), linear mode only**; its presence in `skippedInteractive` is the strongest evidence the carousel was swiped to the end |
| `sr_lin_xml_bottom_r6` | far below the fold | **linear mode only** |
| `sr_lin_xml_bottom_r7` | far below the fold | **linear mode only** |
| `sr_lin_xml_bottom_r4` | far below the fold | **linear mode only** |
| `sr_lin_xml_bottom_r1` | very bottom — hidden ImageButton | **FAIL (R1), linear mode only**; hidden elements are invisible to UiAutomator, so only the report can confirm it |

**Vertical scroll path differs between the two pages** and both must work: the Compose page scrolls
via Compose's own scrollable semantics, the XML page via a real `android.widget.ScrollView`
(`sr_lin_xml_scroll_root`). If only one page scrolls to the bottom, the scroll driver — not the
rules — is at fault.

**Carousel detection.** RFC §4.2 matches on class name *and* geometry
(`width ≥ 80% viewport` AND `height > 0` AND `height < 85% viewport`). Two carousels are provided
on purpose:
- `sr_lin_xml_carousel` (XML page) — a real `android.widget.HorizontalScrollView`, the exact class
  the RFC lists. This one **must** be detected.
- `sr_lin_cmp_carousel` (Compose page) — a Compose `Row(horizontalScroll)`. Compose synthesises
  `android.widget.HorizontalScrollView` for horizontally scrollable containers, but **verify it**;
  if only the native one is detected, Compose carousels are invisible to linear navigation, which
  is a significant gap for Compose apps.

**Swipe depth.** Both carousels now plant violations at several depths rather than one, because a
single mid-carousel violation cannot distinguish "swiped once" from "exhausted the carousel".
Compose: cards 8, 18 and 20 of 20. XML: the four violations sit after all 15 product cards. If the
shallow violation is reported and the deep ones are not, the horizontal pass stopped short — record
how many horizontal swipes were issued per carousel and compare against the dynamic timeout split
`total/(containers×2)`, which is the most likely cause of an early stop.

**Unique artwork on the XML carousel.** All 15 cards use different drawables. This makes the
bounding-box overlay checkable against the right artwork, makes a screenshot of any mid-scroll
position unambiguous about how far the swipe got, and avoids feeding near-identical frames to the
pHash comparison — repeated artwork could mask a scroll-exhaustion misfire, which RFC §10 already
flags as a risk for similar-looking list items.

**Also verify:** the `scrollContainers` array from MAE contains both; `scrollToTop` (5 upward ADB
swipes) restores the page; the dynamic timeout split `total/(containers×2)` is respected; the
`linearNavigationTimeout` cap holds.

**Viewport fallback (RFC §4.2).** Android falls back to 1080×2280 when the first element frame is
`<500h` or `<300w`. Our screens open with a small back-arrow `IconButton` as the first element, so
this fixture exercises the fallback path by construction. Confirm swipes are full-viewport and not
tiny — the RFC calls out "invisible 53-pixel swipes" as the failure mode.

---

## 3. pHash screen-change detection

| Screen | Expected | Risk |
|---|---|---|
| `SR pHash Similar` → tap `sr_phash_toggle_error` | the newly appeared `sr_phash_error_label` **is scanned** | **false-skip**: screen looks ~95% the same |
| `SR pHash Animated` | screen scanned **once** | **false-scan**: large pixel churn every frame |

**Animated-screen stressors.** A small spinner barely moves an 8×8 average hash, so this screen
shifts large blocks of luminance across the grid instead:

| Element | Animation | Hash impact |
|---|---|---|
| `sr_phash_anim_sweep` | 150dp band travelling the full width | **high** — whole grid columns change |
| `sr_phash_anim_tiles` | 4×3 checkerboard inverting light↔dark | **maximal** — flips many of the 64 cells |
| `sr_phash_static_button` | none | control |

**Dedup probe.** `sr_phash_anim_r6_button` is a stable R6 violation ("Submit Button") on the
animated screen. It must appear in the report **exactly once**; a second occurrence means a re-scan
slipped past dedup, which is the real user-visible damage of a false scan — duplicate issues plus
burned linear-navigation budget.

**RFC §10 open item:** Android RecyclerView items with similar layouts produce 90–96% similarity,
so the 95% threshold may need a per-platform Amplitude flag. The filler rows on `SR Linear Nav`
are near-identical by design — record the observed similarity between consecutive scroll positions
and whether any distinct screen was wrongly skipped.

---

## 4. Capability, gating and platform routing

| Check | Expected |
|---|---|
| `accessibility: true` only, no screen-reader options | **no** screen reader report; zero behaviour change (RFC: `InitScreenReader` only when `autoReport \|\| linearNavigation`) |
| `screenReaderAutomation.autoReport: true` | report generated, one snapshot per trigger command |
| `screenReaderAutomation.linearNavigation: true` | full scroll traversal |
| `linearNavigationTimeout` | honoured, capped |
| HPS platform gate | Android now **accepted** (was iOS-only); confirm a rejected/ignored capability no longer occurs |
| **iOS regression** | run the iOS fixture unchanged — RFC claims all changes are additive behind `config.OS == "android"`. This is the single highest-risk regression: iOS is already Deployed. |

**MAE broadcast+callback path (Android-only, no iOS equivalent — untested territory):**
- MAE APK installed and the accessibility service enabled (RFC §9 rollback note).
- Broadcast `com.lambdatest.screenReaderTraversal` reaches MAE.
- MAE POSTs to `http://<MB_IP>:<MB_PORT>/v1.0/screenreader/traversal/callback` — note the `/v1.0/`
  prefix, called out in the RFC as a change.
- **Negative paths:** MAE not installed / service disabled / callback times out / callback lands
  after the command completes. Confirm each degrades gracefully rather than hanging the test — the
  RFC does not specify a timeout for the callback wait, which is worth asking about explicitly.

---

## 5. Known limitations to confirm (RFC §10)

| Item | Test |
|---|---|
| `android.view.View` clickable may be missed by R1 | `sr_r1_v02_clickable_box`, `sr_r1_v07_native_clickable_view` |
| No OCR — `node.getText()` only | text rendered inside a custom-drawn view has no `text`; R7 cannot fire there. Confirm it degrades to "no finding" rather than a false positive |
| pHash threshold on similar list items | filler rows on `SR Linear Nav` |
| MAE APK deployment is manual | confirm the device fleet has it before running |

---

## 6. Cross-platform parity

Compare the same logical violation on both fixtures and confirm identical verdicts and severities:

| Logical case | iOS id | Android id |
|---|---|---|
| Unlabelled interactive control | `sr_r1_v02_textfield` | `sr_r1_v05_native_edittext` (XML page) |
| Empty-name image | `sr_r5_v02_empty_logo` | `sr_r4_v03_native_generic_image` |
| State baked into label | `sr_r6_v01_toggle_on` | `sr_r5_v06_native_switch_on` |
| Type baked into label | `sr_r7_v01_button_word` | `sr_r6_v05_native_button_word` |
| Visible ≠ spoken | `sr_r8_v01_buy_now` | `sr_r7_v03_native_mismatch` |

Mind the renumbering: iOS R5/R6/R7/R8 = Android R4/R5/R6/R7.

Also confirm the dashboard renders the Android report through the **same** UI as iOS (RFC §7.1),
and that LAS needed no changes as claimed (RFC §5).
