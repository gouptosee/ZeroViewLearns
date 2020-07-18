package com.view.zero.learn.views.draw.fish

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.view.zero.learn.LogTag

class DrawUtils {

    companion object {
        fun solveSwiming(
            targetView: View,
            startx: Float,
            startY: Float,
            targerx: Float,
            targetY: Float,
            duration: Long
        ) {

            val angle = calcatueAngle(startx, startY, targerx, targetY)
            targetView.rotation = angle
            Log.e(LogTag, "angle ->>> $angle")

            val curTranslateX = targetView.translationX
            val curTransalateY = targetView.translationY
            val swimXProHolder =
                PropertyValuesHolder.ofFloat(
                    "translationX",
                    curTranslateX,
                    curTranslateX + targerx - startx
                )
            val swimYProHolder = PropertyValuesHolder.ofFloat(
                "translationY",
                curTransalateY,
                curTransalateY + targetY - startY
            )
            val swimAnim =
                ObjectAnimator.ofPropertyValuesHolder(targetView, swimXProHolder, swimYProHolder)
            swimAnim.setDuration(duration)
            swimAnim.interpolator = DecelerateInterpolator()
            swimAnim.start()
        }

        //有问题
        fun calcatueAngle(
            startx: Float,
            startY: Float,
            targerx: Float,
            targetY: Float
        ): Float {
            val distanceX = targerx - startx
            val distanceY = targetY - startY
            var angleV = 0f

            if (distanceX == 0f) {
                angleV = if (distanceY > 0) 180f else 0f
                return (angleV * 180 / Math.PI).toFloat()
            }

            val tanV = distanceY / distanceX

            angleV = Math.atan(tanV.toDouble()).toFloat()

            var angle = (angleV * 180 / Math.PI).toFloat()

            //判断象限
            if (distanceX > 0) {
                if (angleV > 0) {//第一象限
                    angle = angle + 90
                } else { //第四象限
                    angle = 90 - Math.abs(angle)
                }
            } else {
                if (angleV > 0) {//第三象限
                    return -(90 - angle)
                } else { //第二象限
                    return -(90 + Math.abs(angle))
                }
            }

            return angle

        }
    }
}