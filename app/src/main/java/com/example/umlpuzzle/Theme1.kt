package com.example.umlpuzzle

import PlayerModel
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.umlpuzzle.db.Database
import java.io.InputStream


class Theme1 : AppCompatActivity() {

    //Data extra
    private var theme: String = "Catalogo telefonico"
    private var name: String? = ""

    //Statistics Btn
    private lateinit var statisticsBtn: TextView

    //Play Btn
    private lateinit var playBtn: ImageView

    //Actors
    private lateinit var actor1: ImageView
    private lateinit var actor2: ImageView

    private lateinit var actorName1: TextView
    private lateinit var actorName2: TextView

    //Info Container
    private lateinit var infoContainer: ConstraintLayout

    //Spaces
    private lateinit var spaces: LinearLayout

    private lateinit var space1: FrameLayout
    private lateinit var space2: FrameLayout
    private lateinit var space3: FrameLayout
    private lateinit var space4: FrameLayout
    private lateinit var space5: FrameLayout
    private lateinit var space6: FrameLayout

    //Options
    private lateinit var options: GridLayout

    private lateinit var option1: TextView
    private lateinit var option2: TextView
    private lateinit var option3: TextView
    private lateinit var option4: TextView
    private lateinit var option5: TextView
    private lateinit var option6: TextView

    //Clock
    private lateinit var clock: TextView
    private lateinit var countDownTimer: CountDownTimer

    //Game Over
    private lateinit var overlayView : View
    private lateinit var finishBtn : TextView
    private lateinit var gameOverContainer : ConstraintLayout
    private lateinit var score : TextView

    //Database
    private lateinit var database: Database;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme1)
        initializeComponents()

        name = intent.getStringExtra("name")
        database = Database(this);

        setClock(30)

        //Draw Lines to spaces from actor
        drawLines()

        //set options Text
        setOptionsAndActorsText()
    }

    class Line(context: Context, private val Start : View,  private val  End: View, private val direction : String = "Left") : View(context) {
        private val paint: Paint = Paint()

        init {
            paint.color = Color.parseColor("#FF93A0BA") // Establecer el color de la línea
            paint.strokeWidth = 5f // Establecer el ancho de la línea
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            val endCoordinates = IntArray(2)
            End.getLocationOnScreen(endCoordinates)


            // Obtener las coordenadas del ImageView y del elemento de destino
            val startX = Start.x + Start.width / 2
            val startY = Start.y + Start.height / 2
            val endX = endCoordinates[0] + if (direction == "Right") End.width else 0
            val endY = endCoordinates[1]

            // Dibujar la línea entre los dos puntos
            canvas.drawLine(startX, startY, endX.toFloat(), endY.toFloat(), paint)
        }
    }


    private fun drawLines(){
        // Crear lineas de actores
        val appLayout = findViewById<ConstraintLayout>(R.id.constraintLayoutApp)
        val toSpace1 = Line(this, actor1, space1)
        val toSpace3 = Line(this, actor1, space4)
        val toSpace4 = Line(this, actor1, space5)

        val toSpace2 = Line(this, actor2, space2, "Right")
        val toSpace5 = Line(this, actor2, space3, "Right")
        val toSpace6 = Line(this, actor2, space6, "Right")

        appLayout.addView(toSpace1)
        appLayout.addView(toSpace2)
        appLayout.addView(toSpace3)
        appLayout.addView(toSpace4)
        appLayout.addView(toSpace5)

        appLayout.addView(toSpace6)
    }
    private fun initializeComponents(){

        //Statistics Btn
        statisticsBtn = findViewById(R.id.statisticsBtn);

        //Play Btn
        playBtn = findViewById(R.id.playBtn);

        //Actors
        actor1 = findViewById(R.id.actor1);
        actor2 = findViewById(R.id.actor2);

        actorName1 = findViewById(R.id.actorName1);
        actorName2 = findViewById(R.id.actorName2);

        //Info container
        infoContainer = findViewById(R.id.infoContainer);

        //Init spaces
        spaces = findViewById(R.id.spaces);

        space1 = findViewById(R.id.space1);
        space2 = findViewById(R.id.space2);
        space3 = findViewById(R.id.space3);
        space4 = findViewById(R.id.space4);
        space5 = findViewById(R.id.space5);
        space6 = findViewById(R.id.space6);

        //Init options
        options = findViewById(R.id.options)

        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        option5 = findViewById(R.id.option5);
        option6 = findViewById(R.id.option6);

        //Clock
        clock = findViewById(R.id.clock);

        //Game Over
        overlayView = findViewById(R.id.overlayView)
        gameOverContainer = findViewById(R.id.gameOverContainer)
        score = findViewById(R.id.score)
        finishBtn = findViewById(R.id.finishBtn)
    }

    //Clicks handler
    fun openInfo(view: View){
       overlay(true)
       infoContainer.visibility = View.VISIBLE
    }
    fun closeInfo(view: View){
        onResume()
        overlay(false)
        infoContainer.visibility = View.INVISIBLE
    }
    fun backmenu(view: View){
        var intent = Intent(this, Selection::class.java)
        intent.putExtra("name", name)
        startActivity(intent)
    }

    fun toStatistics(view: View){
        var intent = Intent(this, Statistics::class.java)
        intent.putExtra("theme", theme)
        startActivity(intent)
        recreate()
    }

    fun setOptionsAndActorsText() {
        val inputStream: InputStream = this.assets.open("data.json")
        val diagramList: List<Diagram> = readJsonData(inputStream)
        val diagram: Diagram? = diagramList.find { it.theme == theme }

        actorName1.text = diagram?.actors?.getOrNull(0)?.name
        actorName2.text = diagram?.actors?.getOrNull(1)?.name

        val actor1Cases = diagram?.actors?.getOrNull(0)?.cases
        val actor2Cases = diagram?.actors?.getOrNull(1)?.cases

        option2.text = actor1Cases?.getOrNull(0)
        option3.text = actor1Cases?.getOrNull(1)
        option4.text = actor1Cases?.getOrNull(2)

        option1.text = actor2Cases?.getOrNull(0)
        option5.text = actor2Cases?.getOrNull(1)
        option6.text = actor2Cases?.getOrNull(2)
    }


    //Drag and Drop Handlers
    private fun handlerDragListener():View.OnDragListener {
        return View.OnDragListener { view, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    true
                }

                DragEvent.ACTION_DRAG_ENTERED -> {
                    val target = view as FrameLayout
                    val draggedView = event.localState as? View

                    if (target.childCount == 0) {
                        if(target.contentDescription.toString() == draggedView?.contentDescription.toString()){
                            view.setBackgroundResource(R.drawable.space_success)
                            draggedView?.setBackgroundResource(R.drawable.option_bg_success)
                        }else{
                            view.setBackgroundResource(R.drawable.space_error)
                            draggedView?.setBackgroundResource(R.drawable.option_bg_error)
                        }

                    }
                    true
                }

                DragEvent.ACTION_DRAG_EXITED -> {

                    val target = view as FrameLayout

                    view.setBackgroundResource(R.drawable.space_bg)
                    val draggedView = event.localState as View
                    draggedView.setBackgroundResource(R.drawable.option_bg)

                    var same = false
                    for (i in 0 until target.childCount) {
                        val child = target.getChildAt(i)
                        if (child == draggedView) {
                            same = true
                        }
                    }

                    if (target.childCount == 1 && same) {

                        //Quit option from space
                        target.removeView(draggedView)

                        val layoutDragged = GridLayout.LayoutParams().apply {
                            width = GridLayout.LayoutParams.WRAP_CONTENT
                            height = GridLayout.LayoutParams.WRAP_CONTENT
                            // Especificar el peso de la fila y columna
                            rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                            columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)

                            setMargins(5.dpToPx(), 5.dpToPx(), 5.dpToPx(), 5.dpToPx())
                        }
                        draggedView.layoutParams = layoutDragged

                        //Delete substitute
                        val index = Integer.parseInt(draggedView.getTag().toString())
                        val substitute = options.getChildAt(index)
                        options.removeView(substitute)


                        //Add child again
                        options.addView(draggedView, index)
                    }
                    true
                }

                DragEvent.ACTION_DROP -> {

                    val target = view as FrameLayout

                    if (target.childCount == 0) {
                        val draggedView = event.localState as View

                        val parentView = draggedView?.parent as? ViewGroup
                        parentView?.removeView(draggedView)

                        // Establecer las coordenadas del TextView

                        val layoutParams = FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT,
                        )
                        draggedView.layoutParams = layoutParams

                        target.addView(draggedView)

                        //Create substitute
                        val substitute = TextView(this)
                        val layoutSubstitute = GridLayout.LayoutParams().apply {
                            width = GridLayout.LayoutParams.WRAP_CONTENT
                            height = GridLayout.LayoutParams.WRAP_CONTENT
                            // Especificar el peso de la fila y columna
                            rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                            columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)

                            setMargins(5.dpToPx(), 5.dpToPx(), 5.dpToPx(), 5.dpToPx())
                        }

                        substitute.layoutParams = layoutSubstitute
                        substitute.text = "substitute"
                        substitute.visibility = View.INVISIBLE

                        val index = Integer.parseInt(draggedView.getTag().toString())
                        options.addView(substitute, index)

                    }
                    true
                }

                DragEvent.ACTION_DRAG_ENDED -> {
                    if (spacesCompleted()) finishBtn.visibility = View.VISIBLE

                    else finishBtn.visibility = View.INVISIBLE

                    val target = view as FrameLayout

                    target.setBackgroundResource(R.drawable.space_bg)

                    val draggedView = event.localState as View
                    draggedView.visibility = View.VISIBLE
                    true
                }

                else -> false
            }
        }
    }

    private fun handlerTouchListener():View.OnTouchListener{
        return View.OnTouchListener { view, event ->

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val clipData = ClipData.newPlainText("", "")

                    /*if(startupStyle != null){
                        view.setBackgroundResource(resources.getIdentifier(startupStyle,"drawable", packageName))
                    }*/

                    val shadowBuilder = View.DragShadowBuilder(view)

                    view.startDrag(clipData, shadowBuilder, view, 0)
                    view.visibility = View.INVISIBLE
                }
            }
            true
        }
    }

    private fun setOptionsHandler(handlerTouch:View.OnTouchListener?){
        option1.setOnTouchListener(handlerTouch)
        option2.setOnTouchListener(handlerTouch)
        option3.setOnTouchListener(handlerTouch)
        option4.setOnTouchListener(handlerTouch)
        option5.setOnTouchListener(handlerTouch)
        option6.setOnTouchListener(handlerTouch)
    }

    private fun setSpacesHandler(handlerDrag:View.OnDragListener?){
        space1.setOnDragListener(handlerDrag)
        space2.setOnDragListener(handlerDrag)
        space3.setOnDragListener(handlerDrag)
        space4.setOnDragListener(handlerDrag)
        space5.setOnDragListener(handlerDrag)
        space6.setOnDragListener(handlerDrag)
    }
    //Clock
    private fun setClock(start:Long){

        val tiempoInicial: Long = start * 1000 // 30 segundos en milisegundos

        countDownTimer = object : CountDownTimer(tiempoInicial, 10) { // 10 milisegundos de intervalo
            override fun onTick(millisUntilFinished: Long) {
                val segundosRestantes = millisUntilFinished / 1000
                val milisegundosRestantes = millisUntilFinished % 1000

                val tiempoFormateado = String.format("%02d:%02d", segundosRestantes, milisegundosRestantes / 10)

                clock.setTextColor(Color.parseColor(if(segundosRestantes < 10) "#A52929" else "#000000"))

                clock.text = tiempoFormateado
            }

            override fun onFinish() {
                clock.text = "00:00"
                gameOver()

            }
        }
    }

    override fun onPause() {
        super.onPause()
        countDownTimer.cancel()
    }

    //Game
    fun startGame(view:View){
        countDownTimer.start()
        playBtn.setImageResource(android.R.drawable.ic_popup_sync)
        playBtn.setOnClickListener {view ->
            startGameAgain(view)
        }

        //set handlers
        setSpacesHandler(handlerDragListener())
        setOptionsHandler(handlerTouchListener())
    }

    fun startGameAgain(view:View){
        recreate()
    }

    fun gameOver(){
        overlay(true)
        onPause()

        finishBtn.visibility = View.INVISIBLE
        gameOverContainer.visibility = View.VISIBLE
        score.text = calcScore().toString()

        val player = PlayerModel(name = name.toString(), theme = theme, time= clock.text.toString(), score = calcScore().toString().toDouble());
        val status = database.insertPlayer(player)
    }

    private fun calcScore():Double{

        var score = 0.0

        for (i in 0 until spaces.getChildCount()) {
            val space: FrameLayout = spaces.getChildAt(i) as FrameLayout

            score += if(space.childCount == 1){
                var child = space.getChildAt(0)
                if(space.contentDescription.toString() == child.contentDescription.toString()) 16.66666666666667 else 0.0

            }else  0.0
        }
        return Math.round(score).toDouble()
    }

    fun finishGame(view:View){
        gameOver()
    }

    // Helpers

    private fun overlay(active:Boolean){

        if(active){
            overlayView.visibility = View.VISIBLE

            // Deshabilita los eventos de toque en el contenedor
            overlayView.setOnTouchListener { _, _ -> true }

        }else{
            overlayView.visibility = View.INVISIBLE
        }
    }

    private fun spacesCompleted(): Boolean {

        for (i in 0 until spaces.getChildCount()) {
            val space: FrameLayout = spaces.getChildAt(i) as FrameLayout
            if (space.childCount == 0) {
                return false
            }
        }

        return true
    }


    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}


