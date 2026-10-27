package com.example

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class MainActivity : ComponentActivity() {

  private lateinit var webView: WebView

  @SuppressLint("SetJavaScriptEnabled")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // Keep screen on during recitation and ambient sound playback
    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

    // Android TV immersive full-screen display
    WindowCompat.setDecorFitsSystemWindows(window, false)
    val insetsController = WindowInsetsControllerCompat(window, window.decorView)
    insetsController.hide(WindowInsetsCompat.Type.systemBars())
    insetsController.systemBarsBehavior =
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

    webView = WebView(this).apply {
      isFocusable = true
      isFocusableInTouchMode = true
      isClickable = true
      setBackgroundColor(0xFF070A09.toInt())
    }

    setupWebSettings()
    setupWebViewClients()
    setupBackHandler()

    setContentView(webView)

    // Load local offline HTML mushaf app
    webView.loadUrl("file:///android_asset/index.html")
    webView.requestFocus()
  }

  @SuppressLint("SetJavaScriptEnabled")
  @Suppress("DEPRECATION")
  private fun setupWebSettings() {
    with(webView.settings) {
      javaScriptEnabled = true
      domStorageEnabled = true
      databaseEnabled = true
      mediaPlaybackRequiresUserGesture = false
      allowFileAccess = true
      allowContentAccess = true
      allowFileAccessFromFileURLs = true
      allowUniversalAccessFromFileURLs = true
      cacheMode = WebSettings.LOAD_DEFAULT
      useWideViewPort = true
      loadWithOverviewMode = true
      displayZoomControls = false
      setSupportZoom(false)
    }
  }

  private fun setupWebViewClients() {
    webView.webChromeClient = object : WebChromeClient() {
      override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
        Log.d(
            "AlQuranTV",
            "${consoleMessage?.message()} -- line ${consoleMessage?.lineNumber()}"
        )
        return true
      }
    }

    webView.webViewClient = object : WebViewClient() {
      override fun shouldOverrideUrlLoading(
          view: WebView?,
          request: WebResourceRequest?
      ): Boolean {
        val url = request?.url?.toString() ?: return false
        if (url.startsWith("file:///android_asset/")) {
          return false
        }
        // Direct external links outside the app safely
        return try {
          val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
          startActivity(intent)
          true
        } catch (e: Exception) {
          Log.e("AlQuranTV", "Unable to open external URL $url", e)
          true
        }
      }

      override fun onReceivedError(
          view: WebView?,
          request: WebResourceRequest?,
          error: WebResourceError?
      ) {
        super.onReceivedError(view, request, error)
        Log.w(
            "AlQuranTV",
            "Resource error on ${request?.url}: ${error?.description}"
        )
      }
    }
  }

  private fun setupBackHandler() {
    onBackPressedDispatcher.addCallback(
        this,
        object : OnBackPressedCallback(true) {
          override fun handleOnBackPressed() {
            webView.evaluateJavascript(
                "window.handleAndroidBack ? window.handleAndroidBack() : false"
            ) { result ->
              if (result != "true") {
                isEnabled = false
                onBackPressedDispatcher.onBackPressed()
              }
            }
          }
        }
    )
  }

  override fun dispatchKeyEvent(event: KeyEvent): Boolean {
    if (event.action == KeyEvent.ACTION_DOWN) {
      when (event.keyCode) {
        KeyEvent.KEYCODE_DPAD_CENTER -> {
          // Translate DPAD_CENTER to KEYCODE_ENTER for spatial web navigation
          val enterDown = KeyEvent(
              event.downTime,
              event.eventTime,
              KeyEvent.ACTION_DOWN,
              KeyEvent.KEYCODE_ENTER,
              event.repeatCount,
              event.metaState
          )
          if (webView.dispatchKeyEvent(enterDown)) return true
        }
        KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> {
          webView.evaluateJavascript(
              "document.getElementById('play')?.click();",
              null
          )
          return true
        }
        KeyEvent.KEYCODE_MEDIA_PLAY -> {
          webView.evaluateJavascript(
              "if (!playing) document.getElementById('play')?.click();",
              null
          )
          return true
        }
        KeyEvent.KEYCODE_MEDIA_PAUSE -> {
          webView.evaluateJavascript(
              "if (playing) document.getElementById('play')?.click();",
              null
          )
          return true
        }
        KeyEvent.KEYCODE_MEDIA_NEXT, KeyEvent.KEYCODE_MEDIA_FAST_FORWARD -> {
          webView.evaluateJavascript(
              "document.getElementById('nextAyah')?.click();",
              null
          )
          return true
        }
        KeyEvent.KEYCODE_MEDIA_PREVIOUS, KeyEvent.KEYCODE_MEDIA_REWIND -> {
          webView.evaluateJavascript(
              "document.getElementById('prevAyah')?.click();",
              null
          )
          return true
        }
      }
    } else if (event.action == KeyEvent.ACTION_UP &&
        event.keyCode == KeyEvent.KEYCODE_DPAD_CENTER
    ) {
      val enterUp = KeyEvent(
          event.downTime,
          event.eventTime,
          KeyEvent.ACTION_UP,
          KeyEvent.KEYCODE_ENTER,
          event.repeatCount,
          event.metaState
      )
      if (webView.dispatchKeyEvent(enterUp)) return true
    }
    return super.dispatchKeyEvent(event)
  }

  override fun onPause() {
    super.onPause()
    webView.onPause()
  }

  override fun onResume() {
    super.onResume()
    webView.onResume()
    webView.requestFocus()
  }

  override fun onDestroy() {
    webView.destroy()
    super.onDestroy()
  }
}
