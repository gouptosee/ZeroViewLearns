package com.view.zero.learn.views.draw.fish

import android.animation.ObjectAnimator
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.PointF
import android.view.View
import android.view.animation.DecelerateInterpolator

class DrawUtils2 {

    companion object {




        fun calFishTargetPointF(startPointF: PointF, angle: Float, distance: Float): PointF {
            val delx = (Math.cos(Math.toRadians(angle.toDouble())) * distance).toFloat()
            val delY = (Math.sin(Math.toRadians((angle - 180).toDouble())) * distance).toFloat()
            return startPointF.offsetAndNew(delx, delY)
        }

        fun PointF.offsetAndNew(dx: Float, dy: Float): PointF {
            return PointF(x + dx, y + dy)
        }


        //已知三角形三边ABC，AB=a,BC=b,AC =c ,角度CAB=α 有 cos(α) = (a*a +c*c - b*b)/2*a*c
        fun calControlPointFs(startPointF: PointF,endPointF: PointF,fishAngle:Float,firstLength:Float):ArrayList<PointF>{
            var array = ArrayList<PointF>()

            val control_1 = calFishTargetPointF(startPointF,fishAngle,firstLength)

            val a = Math.sqrt(Math.pow((startPointF.x - control_1.x).toDouble(),
                2.0)+Math.pow((startPointF.y - control_1.y).toDouble(),2.0))

            val b = Math.sqrt(Math.pow((startPointF.x - endPointF.x).toDouble(),
                2.0)+Math.pow((startPointF.y - endPointF.y).toDouble(),2.0))

            val c =  Math.sqrt(Math.pow((control_1.x - endPointF.x).toDouble(),
                2.0)+Math.pow((control_1.y - endPointF.y).toDouble(),2.0))


            val angleV = (a*a + b*b -c*c)/(2*a*b).toFloat()
            val angle =Math.toDegrees( Math.acos(angleV)).toFloat() //算出余弦角度

            //todo 角度计算不正确

            val control2 = calFishTargetPointF(startPointF,fishAngle+angle/2,firstLength*2)

            array.add(control_1)
            array.add(control2)
            return array

        }

        fun solveSwiming(
            targetView: View,
            startx: Float,
            startY: Float,
            targerx: Float,
            targetY: Float,
            fishAngle: Float,
            lenth:Float,
            duration: Long
        ) :Path{
            val controls = calControlPointFs(PointF(startx,startY),
                PointF(targerx,targetY),fishAngle,lenth
            )
            val control1 = controls[0]
            val control2 = controls[1]

            val path = Path()
            path.moveTo(startx,startY)
            path.cubicTo(control1.x,control1.y,control2.x,control2.y,targerx,targetY)

            val objAnim = ObjectAnimator.ofFloat(targetView,"x","y",path)
            objAnim.duration = duration
            objAnim.interpolator = DecelerateInterpolator()

            objAnim.start()
            return path

        }

    }

}