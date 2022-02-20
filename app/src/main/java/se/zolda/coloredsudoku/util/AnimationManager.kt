package se.zolda.coloredsudoku.util

import android.content.Context
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import se.zolda.coloredsudoku.R

object AnimationManager {

    private lateinit var scaleUp : Animation
    private lateinit var scaleDown : Animation

    private lateinit var reverseHalfAlphaAnimation: AlphaAnimation
    private lateinit var alphaAnimation: AlphaAnimation
    private lateinit var reverseAlphaAnimation: AlphaAnimation

    fun init(context: Context){
        scaleUp = AnimationUtils.loadAnimation(context, R.anim.scale_up)
        scaleDown = AnimationUtils.loadAnimation(context, R.anim.scale_down)
        alphaAnimation = createAlphaAnimation(
            start = 0.0f,
            end = 1.0f,
            duration = 1000
        )
        reverseAlphaAnimation = createAlphaAnimation(
            start = 1.0f,
            end = 0.0f,
            duration = 1000
        )
        reverseHalfAlphaAnimation = createAlphaAnimation(
            start = 1.0f,
            end = 0.5f,
            duration = 1000
        )
    }

    private fun animationListener(onFinish: () -> Unit) = object: Animation.AnimationListener{
        override fun onAnimationStart(animation: Animation?) {

        }

        override fun onAnimationEnd(animation: Animation?) {
            animation?.setAnimationListener(null)
            onFinish.invoke()
        }

        override fun onAnimationRepeat(animation: Animation?) {

        }

    }

    fun alphaAnimation(view: View, onFinish: () -> Unit){
        alphaAnimation.setAnimationListener(animationListener(onFinish))
        view.startAnimation(alphaAnimation)
    }

    fun reverseAlphaAnimation(view: View, onFinish: () -> Unit){
        reverseAlphaAnimation.setAnimationListener(animationListener(onFinish))
        view.startAnimation(reverseAlphaAnimation)
    }

    fun reverseHalfAlphaAnimation(view: View, onFinish: () -> Unit, duration: Long = 1000L){
        reverseHalfAlphaAnimation.setAnimationListener(animationListener(onFinish))
        reverseHalfAlphaAnimation.duration = duration
        view.startAnimation(reverseHalfAlphaAnimation)
    }

    fun scaleUp(view: View, onFinish: () -> Unit, duration: Long = 1000L){
        scaleUp.setAnimationListener(animationListener(onFinish))
        scaleUp.duration = duration
        view.startAnimation(scaleUp)
    }

    fun scaleDown(view: View, onFinish: () -> Unit){
        scaleDown.setAnimationListener(animationListener(onFinish))
        view.startAnimation(scaleDown)
    }
}