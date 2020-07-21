package com.view.zero.learn.views.draw.fish

import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.Drawable
import android.view.animation.LinearInterpolator
import com.view.zero.learn.LogTag

//🐟🐟动画重做，从鱼中心点出发，按照🐟各处的偏移角度进行计算，算出余下🐟的各个点的坐标位置
//以Drawable的左上角为原点进行后续计算，🐟中心是当前Drawable的中心位置
class FishDrawable2 : Drawable, FishInterface2 {

    private var fishBaseMainAngle = 90f
    private var fishMainAngle =0f
    private var swimFactor = 1f //🐟摆动的角度的比例，按倍数，会加速摆动

    private var Fish_Base_Length = 100f; //基础数值，所有尺寸都是在这个基础上进行计算的
    private val DrawableSize = (8.38 * Fish_Base_Length).toInt()
    private val mCenterX = DrawableSize / 2f //🐟重心的坐标位置x
    private var mCenterY = DrawableSize / 2f //🐟重心的坐标的位置y
    private val mCenterPointF = PointF(mCenterX, mCenterY)

    private val Fish_Head_Radius = Fish_Base_Length //🐟头部的圆的半径大小

    private lateinit var mHeadPointF: PointF //🐟头的中心位置

    private val Fish_Body_Length = 3.2f * Fish_Base_Length //🐟身的长度

    private val Fish_Head_To_Center = 1.6f * Fish_Base_Length //🐟头圆中心点到🐟重心的线长-> 这里设置为鱼身的一般长

    private val Fish_Fin_Length = 1.2f * Fish_Base_Length //🐟鳍的纵向长度
    private val Fish_Fin_Offset_Angle = 110 //🐟鳍相对于角度坐标y方向的偏移值
    private var Fish_Fin_Control_Length = 2.3f * Fish_Base_Length //🐟鳍两点连线的垂直中心线到控制点的距离


    private var Fish_Tail_Big_Raidus = 0.6f * Fish_Base_Length
    private var Fish_Tail_Middle_Radius = 0.42f * Fish_Base_Length
    private var Fish_Tail_Small_Radius = 0.2f * Fish_Base_Length

    private var Fish_Tail_Angle_1 = 0f //🐟尾第一个节肢的角度
    private var Fish_Tail_Angle_2 = 0f //🐟尾第二个节肢的角度


    private val Fish_Tail_Tri_Length = 1f*Fish_Base_Length //鱼尾三角形中线长度
    private var Fish_Tail_Tri_Side = 0.75f*Fish_Base_Length //鱼尾底边中心点到左右的距离


    private var mPaint = Paint()
    private var mPath = Path()
    private lateinit var swimAnim: ValueAnimator
    private var currentValue = 0f
    private val Base_Color = Color.parseColor("#9900aaaa")
    private val coordColor = Color.parseColor("#99ff0000")

    public constructor() {
        innerInit()
    }

    private fun innerInit() {
        mPaint.isDither = true
        mPaint.isAntiAlias = true
        mPaint.color = Base_Color
        mPaint.style = Paint.Style.FILL
        swimAnim = ValueAnimator.ofFloat(0f, 3600f)

        swimAnim.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            override fun onAnimationUpdate(animation: ValueAnimator?) {
                currentValue = (animation!!.getAnimatedValue() as Float) * swimFactor
                val targetAngle =
                    ((Math.toDegrees(Math.sin(Math.toRadians(currentValue.toDouble())))).toInt()).toFloat()
                fishMainAngle = targetAngle * 0.1f +fishBaseMainAngle

                Fish_Tail_Angle_1 = targetAngle * 0.4f

                Fish_Tail_Angle_2 = Math.toDegrees(Math.cos(Math.toRadians(currentValue.toDouble()-90))).toFloat()

                Fish_Tail_Tri_Side = Math.abs(Math.sin(Math.toRadians(currentValue.toDouble()))).toFloat()* 0.75f*Fish_Base_Length

                val tempFishFinControlLength =
                    (-Math.abs(Math.sin(Math.toRadians(currentValue.toDouble()))) * 0.5f * Fish_Base_Length + 2.5f * Fish_Base_Length).toFloat()

                if (swimFactor > 1) {
                    Fish_Fin_Control_Length = tempFishFinControlLength
                } else {
                    if (Fish_Fin_Control_Length != 2.5f * Fish_Base_Length) {
                        Fish_Fin_Control_Length = tempFishFinControlLength
                    }
                }

                invalidateSelf()
            }

        })
        swimAnim.interpolator = LinearInterpolator()
        swimAnim.duration = 10000
        swimAnim.repeatMode = ValueAnimator.RESTART
        swimAnim.repeatCount = ValueAnimator.INFINITE
        swimAnim.start()
    }

    override fun draw(canvas: Canvas) {
        drawHead(canvas)
        drawFins(canvas)
       val tailBeginPointF= drawTails(canvas)
        drawBody(canvas,tailBeginPointF)
        drawCoord(canvas)
    }


    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSPARENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mPaint.colorFilter = colorFilter
    }

    override fun accelSwim(acc: Boolean) {
        if (acc) {
            swimFactor = 3f
        } else {
            swimFactor = 1f
        }
    }



    override fun getFishHeadPointF(): PointF {
        return mHeadPointF!!
    }

    override fun getFishAngle(): Float {
        return fishBaseMainAngle
    }

    override fun getFishBaseLength(): Float {
        return Fish_Base_Length
    }

    override fun setFishAngle(angle: Float) {
        fishBaseMainAngle = angle
        invalidateSelf()
    }



    //画🐟头
    private fun drawHead(canvas: Canvas) {

        mHeadPointF =
            DrawUtils2.calFishTargetPointF(mCenterPointF, fishMainAngle, Fish_Head_To_Center)
        canvas.drawCircle(mHeadPointF.x, mHeadPointF.y, Fish_Head_Radius, mPaint)
    }

    //画🐟鳍，以鱼头中心为起始点
    private fun drawFins(canvas: Canvas) {
        drawFins(canvas, true)
        drawFins(canvas, false)
    }

    private fun drawFins(canvas: Canvas, left: Boolean) {
        val startPointF = mHeadPointF
        val targetPoint = DrawUtils2.calFishTargetPointF(
            startPointF,
            if (left) fishMainAngle + Fish_Fin_Offset_Angle else fishMainAngle - Fish_Fin_Offset_Angle,
            Fish_Head_Radius
        )
        val endPoint =
            DrawUtils2.calFishTargetPointF(targetPoint, fishMainAngle + 180, Fish_Fin_Length)


        val centerX = (targetPoint.x + endPoint.x) / 2
        val centerY = (targetPoint.y + endPoint.y) / 2

        val controlPointF = DrawUtils2.calFishTargetPointF(
            PointF(centerX, centerY),
            if (left) fishMainAngle + 90 else fishMainAngle - 90,
            Fish_Fin_Control_Length
        )
        val controlX = controlPointF.x
        val controlY = controlPointF.y

        mPath.reset()
        mPath.moveTo(targetPoint.x, targetPoint.y)
        mPath.quadTo(controlX, controlY, endPoint.x, endPoint.y)
        mPath.close()
        canvas.drawPath(mPath, mPaint)

//        canvas.drawLine(targetPoint.x,targetPoint.y,endPoint.x,endPoint.y,mPaint)
    }

    private fun drawTails(canvas: Canvas):PointF {
        val bigTailCenter = DrawUtils2.calFishTargetPointF(
            mCenterPointF,
            fishMainAngle + 180,
            Fish_Body_Length / 2f
        )
        val middlePointF = drawTail_Step1(canvas,bigTailCenter)
        drawTail_Step2(canvas, middlePointF)
        return bigTailCenter
    }

    //🐟尾距重心的距离为半个身体长度
    private fun drawTail_Step1(canvas: Canvas,bigTailCenter:PointF): PointF {
        canvas.drawCircle(bigTailCenter.x, bigTailCenter.y, Fish_Tail_Big_Raidus, mPaint)


        val middleAngle = fishMainAngle + Fish_Tail_Angle_1 + 180
        val middleCenter = DrawUtils2.calFishTargetPointF(
            bigTailCenter,
            middleAngle,
            Fish_Tail_Big_Raidus + Fish_Tail_Middle_Radius
        )
        canvas.drawCircle(middleCenter.x, middleCenter.y, Fish_Tail_Middle_Radius, mPaint)


        val tl =
            DrawUtils2.calFishTargetPointF(bigTailCenter, middleAngle + 90, Fish_Tail_Big_Raidus)
        val tr =
            DrawUtils2.calFishTargetPointF(bigTailCenter, middleAngle - 90, Fish_Tail_Big_Raidus)

        val bl =
            DrawUtils2.calFishTargetPointF(middleCenter, middleAngle + 90, Fish_Tail_Middle_Radius)
        val br =
            DrawUtils2.calFishTargetPointF(middleCenter, middleAngle - 90, Fish_Tail_Middle_Radius)

        mPath.reset()
        mPath.moveTo(tl.x, tl.y)
        mPath.lineTo(tr.x, tr.y)
        mPath.lineTo(br.x, br.y)
        mPath.lineTo(bl.x, bl.y)
        mPath.close()
        canvas.drawPath(mPath, mPaint)


        return middleCenter
    }

    private fun drawTail_Step2(canvas: Canvas, middlePointF: PointF){
        val smallAngle = fishMainAngle + Fish_Tail_Angle_2 + 180
        val smallTailPointF = DrawUtils2.calFishTargetPointF(
            middlePointF,
            smallAngle,
            1.3f*Fish_Base_Length
        )

        canvas.drawCircle(smallTailPointF.x,smallTailPointF.y,Fish_Tail_Small_Radius,mPaint)
        val tl =
            DrawUtils2.calFishTargetPointF(middlePointF, smallAngle + 90, Fish_Tail_Middle_Radius)
        val tr =
            DrawUtils2.calFishTargetPointF(middlePointF, smallAngle - 90, Fish_Tail_Middle_Radius)

        val bl =
            DrawUtils2.calFishTargetPointF(smallTailPointF, smallAngle + 90, Fish_Tail_Small_Radius)
        val br =
            DrawUtils2.calFishTargetPointF(smallTailPointF, smallAngle - 90, Fish_Tail_Small_Radius)

        mPath.reset()
        mPath.moveTo(tl.x, tl.y)
        mPath.lineTo(tr.x, tr.y)
        mPath.lineTo(br.x, br.y)
        mPath.lineTo(bl.x, bl.y)
        mPath.close()
        canvas.drawPath(mPath, mPaint)


        drawTail_Trangle(canvas,middlePointF,smallAngle)
    }

    private fun drawTail_Trangle(canvas: Canvas, middlePointF: PointF,angle:Float) {
        val endPoint = DrawUtils2.calFishTargetPointF(
            middlePointF,
            angle,
            Fish_Tail_Tri_Length
        )

        val right = DrawUtils2.calFishTargetPointF(
            endPoint,
            angle-90,
            Fish_Tail_Tri_Side
        )

        val left = DrawUtils2.calFishTargetPointF(
            endPoint,
            angle+90,
            Fish_Tail_Tri_Side
        )


        mPath.reset()
        mPath.moveTo(middlePointF.x,middlePointF.y)
        mPath.lineTo(right.x,right.y)
        mPath.lineTo(left.x,left.y)
        mPath.close()

        canvas.drawPath(mPath,mPaint)

    }

    fun drawBody(canvas: Canvas,tailPointF:PointF){
        val lt = DrawUtils2.calFishTargetPointF(mHeadPointF,fishMainAngle+90,Fish_Head_Radius)
        val rt = DrawUtils2.calFishTargetPointF(mHeadPointF,fishMainAngle-90,Fish_Head_Radius)
        val lb = DrawUtils2.calFishTargetPointF(tailPointF,fishMainAngle+90,Fish_Tail_Big_Raidus)
        val rb = DrawUtils2.calFishTargetPointF(tailPointF,fishMainAngle-90,Fish_Tail_Big_Raidus)

        val controlAngleLeft = fishMainAngle+150
        val cl = DrawUtils2.calFishTargetPointF(lt,controlAngleLeft,Fish_Base_Length*1.1f)

        val controlAngleRight = fishMainAngle-150
        val cr = DrawUtils2.calFishTargetPointF(rt,controlAngleRight,Fish_Base_Length*1.1f)

        mPath.close()
        mPath.moveTo(lt.x,lt.y)
        mPath.quadTo(cl.x,cl.y,lb.x,lb.y)
        mPath.lineTo(rb.x,rb.y)

        mPath.moveTo(lt.x,lt.y)
        mPath.lineTo(rt.x,rt.y)
        mPath.quadTo(cr.x,cr.y,rb.x,rb.y)

        canvas.drawPath(mPath,mPaint)


    }


    fun drawCoord(canvas: Canvas){
        mPaint.color = coordColor

        val right = DrawUtils2.calFishTargetPointF(PointF(mCenterX,mCenterY),fishMainAngle-90,(DrawableSize/2).toFloat())
        val top = DrawUtils2.calFishTargetPointF(PointF(mCenterX,mCenterY),fishMainAngle,(DrawableSize/2).toFloat())
        canvas.drawLine(mCenterX,mCenterY,right.x,right.y,mPaint)
        canvas.drawLine(mCenterX,mCenterY,top.x,top.y,mPaint)

        canvas.drawRect(0f,0f,30f,30f,mPaint)


        mPaint.color = Base_Color
    }

    override fun getIntrinsicHeight(): Int {
        return DrawableSize
    }

    override fun getIntrinsicWidth(): Int {
        return DrawableSize
    }

}
