package com.example.qa_accessibility_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ScrollView
import androidx.activity.ComponentActivity

// =====================================================================
// TE-24440 — SR XML fixtures as STANDALONE activities.
//
// RCA (from dev): when the XML layouts were inflated inside a Compose
// AndroidView, the native views sat underneath an AndroidComposeView in the
// accessibility tree and MAE's walkTree never descended into them — the XML
// elements were not scanned and produced no findings. Real View-based apps
// are setContentView activities, so that is what these are now: the entire
// window is android.widget.* with no Compose layer above it.
// =====================================================================

class ScreenReaderXmlRulesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_reader_native)
        wireScrollArrows(R.id.sr_xml_scroll_root)
        findViewById<Button>(R.id.sr_nav_linear_xml).setOnClickListener {
            startActivity(Intent(this, ScreenReaderLinearNavXmlActivity::class.java))
        }
        // The pHash fixtures are Compose screens; MainActivity opens straight onto the
        // requested destination and finishes back to this activity on back.
        findViewById<Button>(R.id.sr_nav_phash_similar).setOnClickListener {
            startComposeDestination(AppDestinations.SCREEN_READER_PHASH_SIMILAR)
        }
        findViewById<Button>(R.id.sr_nav_phash_animated).setOnClickListener {
            startComposeDestination(AppDestinations.SCREEN_READER_PHASH_ANIMATED)
        }
    }
}

class ScreenReaderLinearNavXmlActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_reader_linear_nav)
        wireScrollArrows(R.id.sr_lin_xml_scroll_root)
    }
}

private fun ComponentActivity.wireScrollArrows(scrollId: Int) {
    val scroll = findViewById<ScrollView>(scrollId)
    val step = (resources.displayMetrics.density * 600).toInt()
    findViewById<ImageButton>(R.id.sr_xml_arrow_up).setOnClickListener {
        scroll.smoothScrollBy(0, -step)
    }
    findViewById<ImageButton>(R.id.sr_xml_arrow_down).setOnClickListener {
        scroll.smoothScrollBy(0, step)
    }
}

private fun ComponentActivity.startComposeDestination(destination: AppDestinations) {
    startActivity(
        Intent(this, MainActivity::class.java)
            .putExtra(MainActivity.EXTRA_DESTINATION, destination.name)
    )
}
