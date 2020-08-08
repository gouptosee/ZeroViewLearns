package com.view.zero.learn.views.draw.fish

import android.animation.ObjectAnimator
import android.graphics.Path
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
            var angle =Math.toDegrees( Math.acos(angleV)).toFloat() //算出余弦角度

            //todo 角度计算不正确  需要额外计算fishAngle
            //上面的算法算出的角度实际上是鱼重心 鱼头坐标系所需要偏向角，和数学坐标系的夹角不能画等号
            //可以通过 鱼重心 鱼头 连线，然后和点（鱼重心.x+1 ,鱼重心.y) 组成，用上面同样的方法计算出鱼相对于数学坐标系的夹角
            //上面两个夹角加起来，才是所需求的点在数学坐标系的真正的偏角

            // AB连线与X的夹角的tan值 - OB与x轴的夹角的tan值
            val direction: Float = (control_1.y - endPointF.y) / (control_1.x - endPointF.x) - (startPointF.y - endPointF.y) / (startPointF.x - endPointF.x)

                if (direction > 0) {
                    angle = -angle
                } else {
                    angle
                }


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