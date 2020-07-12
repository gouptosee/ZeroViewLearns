package com.view.zero.learn.views.nestscroll

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.core.view.NestedScrollingParent2
import androidx.core.view.ViewCompat
import com.view.zero.learn.R
import com.view.zero.learn.utils.LogUtils
import java.lang.Exception

class MNestParent @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    intStyle: Int = 0
) : FrameLayout(context, attributeSet, intStyle), NestedScrollingParent2 {

    private val preListener = ViewTreeObserver.OnPreDrawListener {
        onDenpendViewStateChanged()
        true
    }

    private var preListenerAdded = false
    private val dependRefList: MutableList<DependViewInfo> = ArrayList()
    private var appLayout: MAppLayoutInterface? = null
    private var totalHeight = 0

    data class DependViewInfo(var selfview: View, var dependview: View)


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        prepareChildren()
        ensurePreListener()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }


    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
    }


    //这个方法只是为了在setNestedScrollingParentForType把当前view的父view设置
    // 后续可以通过getNestedScrollingParentForType获取到viewparent
    // 后续的dispatchNestedPreScroll等都会通过这个进行判断，没有就返回false 也就是不处理
    override fun onStartNestedScroll(
        child: View,
        target: View,
        nestedScrollAxes: Int,
        type: Int
    ): Boolean {
        return nestedScrollAxes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }


    override fun onNestedPreScroll(
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray, type: Int
    ) {
        var curScrollY = scrollY

        var afterScrollY = curScrollY + dy

        if (curScrollY == 0 || curScrollY == appLayout!!.getScrollRange()) {
            if(dy <0) {
                for (i in 0 until childCount) {
                    val paraView = getChildAt(i)
                    var paramas = paraView.layoutParams as MLayoutParams
                    val behavior = paramas.behavior
                    if (behavior != null && !behavior.canScrollDown(paraView)) {
                        behavior.tryScrollTo(paraView, 0, dy)
                        return
                    }
                }
            }
        }

//        区间内 全部由父亲接收
        if (afterScrollY > 0 && afterScrollY < appLayout!!.getScrollRange()) {
            if (target.scrollY == 0) {
                consumed[1] = dy
                scrollTo(0, afterScrollY)
            }
        } else {

            if (curScrollY == 0 || curScrollY == appLayout!!.getScrollRange()) return


            if (afterScrollY < 0) {
                afterScrollY = 0
                consumed[1] = dy + afterScrollY
                scrollTo(0, 0)

            } else if (afterScrollY > appLayout!!.getScrollRange()) {
                afterScrollY = appLayout!!.getScrollRange()
                consumed[1] = dy + (afterScrollY - appLayout!!.getScrollRange())
                scrollTo(0, appLayout!!.getScrollRange())

            }
        }

        LogUtils.logE("remain consume ->>>>  ${consumed[1]}")


    }

    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
    }


    override fun onStopNestedScroll(target: View, type: Int) {
    }


    override fun onNestedFling(
        target: View,
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {

        return super.onNestedFling(target, velocityX, velocityY, consumed)
    }


    inner class MLayoutParams : FrameLayout.LayoutParams {

        constructor(width: Int, height: Int) : super(width, height)
        constructor(c: Context, attrs: AttributeSet?) : super(c, attrs)

        var behavior: IBehavior<View>? = null

    }


    fun prepareChildren() {
        dependRefList.clear()
        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            if (childView is MAppLayoutInterface) appLayout = childView
            val params = childView.layoutParams as MLayoutParams
            if (params.behavior == null) continue
            for (j in 0 until childCount) {
                if (i == j) continue
                val dependView = getChildAt(j)
                if (params.behavior!!.dependsOn(this, childView, dependView)) {
                    dependRefList.add(DependViewInfo(childView, dependView))
                    params.behavior!!.onDependViewChanged(this, childView, dependView)
                    break
                }
            }
        }
        if (appLayout != null) {
            totalHeight = appLayout!!.getContentHeight() + measuredHeight
        } else {
            totalHeight = measuredHeight
        }
    }


    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        val parentParams: MLayoutParams = MLayoutParams(context, attrs)
        attrs?.apply {
            val types = context.obtainStyledAttributes(attrs, R.styleable.My_Nest_Parent)
            val behavior = types.getString(R.styleable.My_Nest_Parent_myBehavior)
            LogUtils.logE("behavior ->>>>  $behavior")
            parentParams.behavior = behavior?.run {
                parseBehavior(this)

            }
            LogUtils.logE("behavior after->>>>  ${parentParams.behavior}")

        }

        return parentParams
    }

    fun parseBehavior(clazzName: String): IBehavior<View>? {
        try {
            val constructor = Class.forName(clazzName).getDeclaredConstructor()
            constructor?.apply {
                isAccessible = true
                return constructor.newInstance() as IBehavior<View>
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    fun onDenpendViewStateChanged() {
        if (dependRefList.size == 0) return
        for (i in dependRefList.indices) {
            val dependInfo = dependRefList[i]
            val childParams = dependInfo.selfview.layoutParams as MLayoutParams
            val behavior = childParams.behavior
            behavior!!.onDependViewChanged(this, dependInfo.selfview, dependInfo.dependview)
        }
    }

    fun ensurePreListener() {
        if (preListenerAdded) return
        viewTreeObserver.addOnPreDrawListener(preListener)

        preListenerAdded = true
    }

    override fun onNestedPreFling(target: View, velocityX: Float, velocityY: Float): Boolean {
        for (i in 0 until childCount) {
            val target = getChildAt(i)
            val params = target.layoutParams as MLayoutParams
            params.behavior?.apply {
                params!!.behavior!!.fliping(target, velocityY)
            }
        }
        return false
    }


}