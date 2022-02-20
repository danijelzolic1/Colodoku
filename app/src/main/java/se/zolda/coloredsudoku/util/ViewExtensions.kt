package se.zolda.coloredsudoku.util

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.view.animation.AlphaAnimation
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun createAlphaAnimation(
    start: Float,
    end: Float,
    duration: Long,
    fillAfter: Boolean = true
): AlphaAnimation {
    return AlphaAnimation(start, end).apply {
        this.duration = duration
        this.fillAfter
    }
}

fun Drawable.setDrawableTintColor(@ColorInt color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        this.colorFilter = BlendModeColorFilter(
            color, BlendMode.SRC_ATOP
        )
    } else {
        this.setColorFilter(
            color, PorterDuff.Mode.SRC_ATOP
        )
    }
}

fun View.setCellDrawable(@DrawableRes drawableRes: Int){
    val drawable = ContextCompat.getDrawable(context, drawableRes)
    this.background = drawable
}

fun Drawable.setDrawableTint(context: Context, @ColorRes color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        this.colorFilter = BlendModeColorFilter(
            ContextCompat.getColor(
                context,
                color
            ), BlendMode.SRC_ATOP
        )
    } else {
        this.setColorFilter(
            ContextCompat.getColor(
                context,
                color
            ), PorterDuff.Mode.SRC_ATOP
        )
    }
}