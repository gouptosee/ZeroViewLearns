package com.view.zero.learn.views.draw.fish

import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.animation.Animation
import androidx.core.graphics.plus
import com.view.zero.learn.LogTag
import kotlin.math.cos
import kotlin.math.sin

class FishDrawable : Drawable {

    private var Fish_Base_Radius = 100f; //基础数值，所有尺寸都是在这个基础上进行计算的
    private val DrawableSize = 8.38 * Fish_Base_Radius
    private val Fish_Total_Size = 6.79 * Fish_Base_Radius
    private val Canvas_Init_TranslateY = 1.59 * Fish_Base_Radius

    private val BaseCenterX = (DrawableSize / 2).toFloat()


    private var Fish_Base_Color = Color.parseColor("#9900aaaa")
    private var mPaint = Paint()
    private var mPath = Path()

    private val Fish_Headr_Radius = Fish_Base_Radius
    private val Fish_Fin_Angle = 30f //鱼鳍起始位置相对于鱼头中心的偏移夹角
    private val Fish_Fin_Length = (Fish_Base_Radius * 1.1).toFloat()
    private val Fish_Fin_Control_X_Scale = 2f //鱼鳍控制点x相对于起始位置的偏移比例，以鱼鳍长度作为参照
    private val Fish_Fin_Control_Y_Scale = 0.8f //鱼鳍控制点x相对于起始位置的偏移比例，以鱼鳍长度作为参照

    private val Fish_Tail_StartY = (4.2 * Fish_Base_Radius).toFloat() //鱼尾相对起始位置，不算冒出来的圆半径
    private val Fish_Tail_Circle_1_Radius = (0.7 * Fish_Base_Radius).toFloat() //鱼尾第一个圆半径
    private val Fish_Tail_Circle_2_Radius = (0.42 * Fish_Base_Radius).toFloat() //鱼尾第二个圆半径
    private val Fish_Tail_Circle_3_Radius = (0.168 * Fish_Base_Radius).toFloat() //鱼尾第三个圆半径

    private val Fish_Tail_While_Lenth = 4 //与微商白条宽度
    private val Fish_White_Line_Color = Color.parseColor("#ffffff")

    private val Fish_Tail_Trangle_Angle_1 = 55f //鱼尾大尾巴三角夹角
    private val Fish_Tail_Trangle_Angle_2 = 65f //鱼尾小尾巴三角夹角

    private val Fish_Tail_Trangle_Length_1 = (Fish_Base_Radius * 1.2).toFloat() //大三角鱼尾的斜边长度
    private val Fish_Tail_Trangle_Length_2 = (Fish_Base_Radius * 0.9).toFloat() //小三角鱼尾的斜边长度


    private val Fish_Tail_2_StartY = (5.32 * Fish_Base_Radius).toFloat() //鱼尾第二截相对y方向位置

    private val Fish_Body_Control_X_Scale = 0.5f //鱼身控制点x相对于起始位置的偏移比例
    private val Fish_Body_Control_Y_Scale = 1f //鱼身控制点x相对于起始位置的偏移比例

    private var currentTailDegree = 0f;//当前鱼尾摆动角度
    private var TailMaxDegree = 25f;//鱼尾摆动的最大角度 [-TailMaxDegree,TailMaxDegree]

    private var tailDruation = 500 //鱼尾摆动一次耗时

//    private val handler = object : Handler(Looper.getMainLooper()) {
//        override fun handleMessage(msg: Message) {
//
//            if (degreeAdded) {
//                currentTailDegree += 1
//            } else {
//                currentTailDegree -= 1
//            }
//
//            if (degreeAdded && currentTailDegree == TailMaxDegree) {
//                degreeAdded = false
//            }
//
//            if (!degreeAdded && currentTailDegree == -TailMaxDegree) {
//                degreeAdded = true
//            }
//
//
//            invalidateSelf()
//            sendEmptyMessageAtTime(0, perDegreeeHandler)
//        }
//    }

    public constructor() {
        innerInit()
    }

    private fun initAnims(){
        val anim = ValueAnimator.ofFloat(-TailMaxDegree,TailMaxDegree,-TailMaxDegree)
        anim.addUpdateListener(object :ValueAnimator.AnimatorUpdateListener{
            override fun onAnimationUpdate(animation: ValueAnimator?) {
                currentTailDegree = animation!!.animatedValue as Float
                invalidateSelf()
            }

        })
        anim.repeatCount = Animation.INFINITE
        anim.repeatMode = ValueAnimator.RESTART
        anim.duration = tailDruation.toLong()
        anim.start()
    }


    private fun innerInit() {
        mPaint.style = Paint.Style.FILL
        mPaint.isAntiAlias = true
        mPaint.isDither = true
        mPaint.color = Fish_Base_Color

        initAnims()
    }


    override fun draw(canvas: Canvas) {
        canvas.translate(0f, Canvas_Init_TranslateY.toFloat())
        drawFishHead(canvas)
        drawFishFins(canvas)
        drawTails(canvas)
        drawBody(canvas)
    }

    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT;
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mPaint.colorFilter = colorFilter
    }

    override fun getIntrinsicWidth(): Int {
        return DrawableSize.toInt()
    }

    override fun getIntrinsicHeight(): Int {
        return DrawableSize.toInt()
    }

    //画鱼头
    private fun drawFishHead(canvas: Canvas) {
        canvas.drawCircle(
            (DrawableSize / 2).toFloat(),
            Fish_Headr_Radius,
            Fish_Headr_Radius,
            mPaint
        )
    }

    //画鱼鳍
    private fun drawFishFins(canvas: Canvas) {
        val headerCenter = PointF((DrawableSize / 2).toFloat(), Fish_Headr_Radius)
        var leftFinStartPointF =
            calcuteTargetPoint(headerCenter, Fish_Fin_Angle, Fish_Headr_Radius, true)

        var rightFinStartPointF =
            calcuteTargetPoint(headerCenter, Fish_Fin_Angle, Fish_Headr_Radius, false)

        drawLeftOrRightFishFin(leftFinStartPointF, true, canvas)
        drawLeftOrRightFishFin(rightFinStartPointF, false, canvas)

    }

    private fun drawLeftOrRightFishFin(startPoint: PointF, leftOrRight: Boolean, canvas: Canvas) {
        val startX = startPoint.x
        val startY = startPoint.y

        val endx = startX
        val endy = startY + Fish_Fin_Length

        val controlxOffset =
            if (leftOrRight) -Fish_Fin_Length * Fish_Fin_Control_X_Scale else Fish_Fin_Length * Fish_Fin_Control_X_Scale
        val controlyOffset = (Fish_Fin_Length * Fish_Fin_Control_Y_Scale).toFloat()

        val controlX = startX + controlxOffset
        val controly = startY + controlyOffset

        mPath.reset()
        mPath.moveTo(startX, startY)
        mPath.quadTo(controlX, controly, endx, endy)
        mPath.close()
        canvas.drawPath(mPath, mPaint)

    }


    private fun drawTails(canvas: Canvas) {
        canvas.save()
        canvas.rotate(currentTailDegree.toFloat(), BaseCenterX, Fish_Tail_StartY)
        drawTail_1(canvas)
        drawTail_2(canvas)
        drawTail_3(canvas)
        canvas.restore()
    }

    //画鱼尾的第一个梯形
    private fun drawTail_1(canvas: Canvas) {
        val centerX = (DrawableSize / 2).toFloat()

        val firstCenternY = Fish_Tail_StartY
        val sencondCenterY = Fish_Tail_2_StartY

        canvas.drawCircle(centerX, firstCenternY, Fish_Tail_Circle_1_Radius, mPaint)
        canvas.drawCircle(centerX, sencondCenterY, Fish_Tail_Circle_2_Radius, mPaint)


        val rectFirst = RectF(
            centerX - Fish_Tail_Circle_1_Radius,
            firstCenternY - Fish_Tail_While_Lenth / 2,
            centerX + Fish_Tail_Circle_1_Radius,
            firstCenternY + Fish_Tail_While_Lenth / 2
        )

        val rectSecond = RectF(
            centerX - Fish_Tail_Circle_2_Radius,
            sencondCenterY - Fish_Tail_While_Lenth / 2,
            centerX + Fish_Tail_Circle_2_Radius,
            sencondCenterY + Fish_Tail_While_Lenth / 2
        )
        mPaint.setColor(Fish_White_Line_Color)
        canvas.drawRect(rectFirst, mPaint)
        canvas.drawRect(rectSecond, mPaint)
        mPaint.setColor(Fish_Base_Color)


        mPath.reset()

        mPath.moveTo(centerX - Fish_Tail_Circle_1_Radius, firstCenternY)
        mPath.lineTo(centerX - Fish_Tail_Circle_2_Radius, sencondCenterY)
        mPath.lineTo(centerX + Fish_Tail_Circle_2_Radius, sencondCenterY)
        mPath.lineTo(centerX + Fish_Tail_Circle_1_Radius, firstCenternY)
        mPath.close()
        canvas.drawPath(mPath, mPaint)

    }


    private fun drawTail_2(canvas: Canvas) {
        val centerX = (DrawableSize / 2).toFloat()
        val endCircleY = (Fish_Total_Size - Fish_Tail_Circle_3_Radius).toFloat()
        canvas.drawCircle(centerX, endCircleY, Fish_Tail_Circle_3_Radius, mPaint)

        mPath.reset()
        mPath.moveTo(centerX - Fish_Tail_Circle_2_Radius, Fish_Tail_2_StartY)
        mPath.lineTo(centerX + Fish_Tail_Circle_2_Radius, Fish_Tail_2_StartY)
        mPath.lineTo(centerX + Fish_Tail_Circle_3_Radius, endCircleY)
        mPath.lineTo(centerX - Fish_Tail_Circle_3_Radius, endCircleY)
        mPath.close()
        canvas.drawPath(mPath, mPaint)
    }

    private fun drawTail_3(canvas: Canvas) {
        val startPointF = PointF(BaseCenterX, Fish_Tail_2_StartY)
        val bigTrangleLeftPointF = calcuteTargetPoint(
            startPointF,
            Fish_Tail_Trangle_Angle_1,
            Fish_Tail_Trangle_Length_1,
            true
        )
        val bigTrangleRightPointF = calcuteTargetPoint(
            startPointF,
            Fish_Tail_Trangle_Angle_1,
            Fish_Tail_Trangle_Length_1,
            false
        )

        val smallTrangleLeftPointF = calcuteTargetPoint(
            startPointF,
            Fish_Tail_Trangle_Angle_2,
            Fish_Tail_Trangle_Length_2,
            true
        )
        val smallTrangleRightPointF = calcuteTargetPoint(
            startPointF,
            Fish_Tail_Trangle_Angle_2,
            Fish_Tail_Trangle_Length_2,
            false
        )

        mPath.reset()
        mPath.moveTo(startPointF.x, startPointF.y)
        mPath.lineTo(bigTrangleLeftPointF.x, bigTrangleLeftPointF.y)
        mPath.lineTo(bigTrangleRightPointF.x, bigTrangleRightPointF.y)
        mPath.close()
        canvas.drawPath(mPath, mPaint)

        mPath.reset()
        mPath.moveTo(startPointF.x, startPointF.y)
        mPath.lineTo(smallTrangleLeftPointF.x, smallTrangleLeftPointF.y)
        mPath.lineTo(smallTrangleRightPointF.x, smallTrangleRightPointF.y)
        mPath.close()
        canvas.drawPath(mPath, mPaint)
    }


    private fun drawBody(canvas: Canvas) {
        val TopLeftX = BaseCenterX - Fish_Headr_Radius
        val TopLeftY = Fish_Headr_Radius

        val TopRightX = BaseCenterX + Fish_Headr_Radius
        val TopRightY = TopLeftY

        val BottomLeftX = BaseCenterX - Fish_Tail_Circle_1_Radius
        val BottomLeftY = Fish_Tail_StartY

        val BottomRightX = BaseCenterX + Fish_Tail_Circle_1_Radius
        val BottomRightY = BottomLeftY

        mPath.reset()
        mPath.moveTo(TopLeftX, TopLeftY)
        pathQuadTo(
            mPath,
            TopLeftX,
            TopLeftY,
            BottomLeftX,
            BottomLeftY,
            Fish_Body_Control_X_Scale,
            Fish_Body_Control_Y_Scale,
            true
        )
        mPath.lineTo(BottomRightX, BottomRightY)

        mPath.moveTo(TopLeftX, TopLeftY)

        mPath.lineTo(TopRightX, TopRightY)
        pathQuadTo(
            mPath,
            TopRightX,
            TopRightY,
            BottomRightX,
            BottomRightY,
            Fish_Body_Control_X_Scale,
            Fish_Body_Control_Y_Scale,
            false
        )
        mPath.close()
        canvas.drawPath(mPath, mPaint)

    }

    private fun pathQuadTo(
        path: Path,
        startX: Float,
        startY: Float,
        endX: Float,
        endY: Float,
        scaleX: Float,
        scaleY: Float,
        leftOrRight: Boolean
    ) {
        val offsets = calcuteControlPointF(scaleX, scaleY, leftOrRight)
        val controlX = startX + offsets[0]
        val controlY = startY + offsets[1]
        path.quadTo(controlX, controlY, endX, endY)
    }

    private fun calcuteControlPointF(
        scaleX: Float,
        scaleY: Float,
        leftOrRight: Boolean
    ): FloatArray {
        val controlxOffset =
            if (leftOrRight) -Fish_Fin_Length * scaleX else Fish_Fin_Length * scaleX
        val controlyOffset = (Fish_Fin_Length * scaleY).toFloat()
        return floatArrayOf(controlxOffset, controlyOffset)
    }

    //Math的sin cos方法按照弧度制  角度要先转换成弧度 也就 angle*π/180
    private fun calcuteTargetPoint(
        startPoint: PointF,
        angle: Float,
        distance: Float,
        leftOrRight: Boolean
    ): PointF {
        val targetAngle = Math.PI * angle / 180
        val xOffset = if (leftOrRight) -cos(targetAngle) * distance else
            cos(targetAngle) * distance

        val yOffset = sin(targetAngle) * distance

        return PointF((startPoint.x + xOffset).toFloat(), (startPoint.y + yOffset).toFloat())
    }


}
