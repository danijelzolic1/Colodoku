package se.zolda.coloredsudoku.util

import android.content.Context
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Resources
import android.graphics.Rect
import android.graphics.RectF
import android.util.DisplayMetrics
import kotlin.math.roundToInt

private val displayMetrics: DisplayMetrics by lazy { Resources.getSystem().displayMetrics }

val screenRectPx: Rect
    get() = displayMetrics.run { Rect(0, 0, widthPixels, heightPixels) }

val screenRectDp: RectF
    get() = screenRectPx.run { RectF(0f, 0f, right.px2dp, bottom.px2dp) }

val Context.physicalScreenRectPx: Rect
    get() = screenRectPx

val Context.physicalScreenRectDp: RectF
    get() = physicalScreenRectPx.run { RectF(0f, 0f, right.px2dp, bottom.px2dp) }

val Number.px2dp: Float
    get() = this.toFloat() / displayMetrics.density

val Number.dp2px: Int
    get() = (this.toFloat() * displayMetrics.density).roundToInt()

fun shouldShowInterstitialAd(): Boolean{
    return when(val level = AppPreferences.currentLevel){
        0 -> false
        else -> level % 4 == 0
    }
}

fun Context.isDarkThemeOn(): Boolean {
    return resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
}