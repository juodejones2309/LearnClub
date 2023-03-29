package com.rcappstudios.qualityeducation.fragments.whiteboard

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.google.firebase.database.*
import com.google.firebase.database.annotations.Nullable
import com.rcappstudios.qualityeducation.fragments.whiteboard.model.FingerPath
import com.rcappstudios.qualityeducation.fragments.whiteboard.model.Segment
import kotlin.math.abs


class PaintView : View {
    //App user drawing
    private var paths: MutableList<FingerPath>? = null
    private var fp: FingerPath? = null
    private val TOUCH_TOLERANCE = 5
    private var mX = 0
    private var mY: Int = 0
    private var path: Path? = null
    private var paintScreen: Paint? = null
    var paintLine: Paint? = null
    private var bitmap: Bitmap? = null
    private var blurFlag = false
    private var endTouchFlag = false
    var color = Color.WHITE
    private var myHeight = 0
    private var myWidth = 0
    private var roomId = ""
    private var currentSegment: Segment? = null

    var myTurn = false
    var strokeWidth = 20f
    var brushPropertiesFlag = false
    private lateinit var pathReference :DatabaseReference

    private val pathList: MutableList<FingerPath> = ArrayList()

//    private lateinit var viewModel: ScribbleViewModel
    private lateinit var activity: Activity

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    fun initRoom( roomId: String,activity: Activity) {
        paths = ArrayList()
        paintScreen = Paint()
        paintLine = Paint()
        paintLine!!.isAntiAlias = true
        paintLine!!.color = Color.parseColor("black")
        paintLine!!.strokeWidth = 14f
        paintLine!!.style = Paint.Style.STROKE
        paintLine!!.strokeCap = Paint.Cap.ROUND
        paintLine!!.strokeJoin = Paint.Join.ROUND
        brushPropertiesFlag = false
        blurFlag = false
        endTouchFlag = false
        this.activity = activity
        this.roomId = roomId
        pathReference = FirebaseDatabase.getInstance().getReference("Room/$roomId/whiteBoard")
//        this.viewModel = viewModel
//        this.viewModel.getPathData()
    }

    fun initMentor(subject: String,mentorUserID: String, studentUserId: String , activity: Activity){
        paths = ArrayList()
        paintScreen = Paint()
        paintLine = Paint()
        paintLine!!.isAntiAlias = true
        paintLine!!.color = Color.parseColor("black")
        paintLine!!.strokeWidth = 14f
        paintLine!!.style = Paint.Style.STROKE
        paintLine!!.strokeCap = Paint.Cap.ROUND
        paintLine!!.strokeJoin = Paint.Join.ROUND
        brushPropertiesFlag = false
        blurFlag = false
        endTouchFlag = false
        this.activity = activity

        pathReference = FirebaseDatabase.getInstance().getReference("Mentors/$subject/$mentorUserID/connections/$studentUserId/whiteBoard")
    //        this.viewModel = viewModel
//        this.viewModel.getPathData()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        bitmap!!.eraseColor(Color.WHITE)
        myHeight = h
        myWidth = w
    }

    override fun onDraw(canvas: Canvas) {

        canvas.drawBitmap(bitmap!!, myWidth.toFloat(), myHeight.toFloat(), paintScreen)
        //For Drawing (User's turn)
        if (myTurn) {
            for (path in paths!!) {
                paintLine!!.color = path.color
                paintLine!!.strokeWidth = path.strokeWidth.toFloat()
                canvas.drawPath(path.path, paintLine!!)
            }
            for (fp in pathList) {
                paintLine!!.color = fp.color
                paintLine!!.strokeWidth = fp.strokeWidth.toFloat()
                canvas.drawPath(fp.path, paintLine!!)
            }
        } else {
            //For seeing (Opponent turn)
            for (fp in pathList) {
                paintLine!!.color = fp.color
                paintLine!!.strokeWidth = fp.strokeWidth.toFloat()
                canvas.drawPath(fp.path, paintLine!!)
            }
        }

        invalidate()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (myTurn) {
            performClick()
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    touchStarted(event.x, event.y)
                    invalidate()
                }
                MotionEvent.ACTION_UP -> {
                    touchEnded()
                    invalidate()
                }
                else -> {
                    touchMoved(event.x, event.y)
                    invalidate()
                }
            }
            return true
        }
        return true

    }

    private fun touchStarted(xPos: Float, yPos: Float) {
        if (brushPropertiesFlag) {
            setBrushColor(color)
            setStrokeWidth1(strokeWidth)
        }
        path = Path()
        fp = FingerPath(
            getBrushColor(),
            getStrokeWidth1().toInt(),
            path
        )
        paths!!.add(fp!!)
        path!!.reset()
        path!!.moveTo(xPos, yPos)
        mX = xPos.toInt().toFloat().toInt()
        mY = yPos.toInt().toFloat().toInt()
        currentSegment = Segment(
            getBrushColor(),
            getStrokeWidth1(),
            myWidth,
            myHeight
        )
        currentSegment!!.addPoints(mX, mY)
        invalidate()
    }

    private fun touchMoved(xPos: Float, yPos: Float) {
        val dx = abs(xPos - mX)
        val dy = abs(yPos - mY)
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            path!!.quadTo(mX.toFloat(), mY.toFloat(), (xPos + mX) / 2, (yPos + mY) / 2)
            mX = xPos.toInt()
            mY = yPos.toInt()
            currentSegment!!.addPoints(mX, mY)
        }
        if (currentSegment!!.points.size % 5 == 0 && currentSegment!!.points.size < 125) {
            updatePath()
        } else endTouchFlag =
            true
    }

    private fun touchEnded() {
        path!!.lineTo(mX.toFloat(), mY.toFloat())
        if (endTouchFlag) {
            updatePath()
        }
        endTouchFlag = false
    }

    private fun getStrokeWidth1(): Float {
        return paintLine!!.strokeWidth
    }

    private fun setStrokeWidth1(strokeWidth: Float) {
        paintLine!!.strokeWidth = strokeWidth
    }

    fun setBrushColor(color: Int) {
        paintLine!!.color = color
        invalidate()
    }

    private fun getBrushColor(): Int {
        return paintLine!!.color
    }

    private fun updatePath() {
        val newSegment = Segment(
            currentSegment!!.color, currentSegment!!.strokeWidth, myWidth, myHeight
        )
        for (point in currentSegment!!.points) newSegment.addPoints(point.x, point.y)

        pathReference.push().setValue(newSegment)
//        viewModel.setOrUpdatePathData(newSegment)
    }

    fun pathUpdateListener() {
        if (true) {//->here
            pathReference.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val segment = dataSnapshot.getValue(
                        Segment::class.java
                    )!!
                    drawSegmentPath(
                        segment.points,
                        segment.color,
                        segment.strokeWidth,
                        segment.width,
                        segment.getHeight()
                    )
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
                override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                    clearCanvas(true)
                }

                override fun onChildMoved(dataSnapshot: DataSnapshot, @Nullable s: String?) {}
                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
    }

    private fun drawSegmentPath(
        segmentPoints: List<Point?>,
        color: Int,
        strokeWidth: Float,
        screenWidth: Int,
        screenHeight: Int
    ) {
        if (true) {//->here
            var currentPoint = segmentPoints[0]
            var nextPoint: Point? = null
            val widthScale: Float =
                if (myWidth == screenWidth) 1.0f else myWidth.toFloat() / screenWidth
            val heightScale: Float =
                if (myHeight == screenHeight) 1.0f else myHeight.toFloat() / screenHeight
            var currentX: Int
            var currentY: Int
            var nextX: Int
            var nextY: Int
            currentX = (currentPoint!!.x * widthScale).toInt()
            currentY = (currentPoint.y * heightScale).toInt()
            val path = Path()
            path.reset()
            path.moveTo(currentX.toFloat(), currentY.toFloat())
            val fingerPath = FingerPath(
                color,
                strokeWidth.toInt(),
                path
            )
            pathList.add(fingerPath)
            invalidate()
            for (i in 1 until segmentPoints.size) {
                nextPoint = segmentPoints[i]
                nextX = (nextPoint!!.x * widthScale).toInt()
                nextY = (nextPoint.y * heightScale).toInt()
                path.quadTo(
                    currentX.toFloat(), currentY.toFloat(),
                    (nextX + currentX).toFloat() / 2,
                    (nextY + currentY).toFloat() / 2
                )
                currentPoint = nextPoint
                currentX = (currentPoint.x * widthScale).toInt()
                currentY = (currentPoint.y * heightScale).toInt()
                invalidate()
            }
            if (nextPoint != null) {
                nextX = (nextPoint.x * widthScale).toInt()
                nextY = (nextPoint.y * heightScale).toInt()
                path.lineTo(nextX.toFloat(), nextY.toFloat())
                invalidate()
            }
        }
    }

    fun clearCanvas(clearFlag: Boolean) {
        if (paths != null) {
            paths!!.clear()
            bitmap!!.eraseColor(Color.WHITE)
            if (myTurn) {
                pathReference.removeValue()
            }
            if (clearFlag && pathList != null) {
                pathList.clear()
            }
            invalidate()
        }
    }
}