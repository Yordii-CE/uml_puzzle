package com.example.umlpuzzle
import PlayerModel
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.example.umlpuzzle.db.Database
import java.lang.String
import kotlin.Int


class Statistics : AppCompatActivity() {
    private lateinit var  listViewItems: ListView
    private lateinit var database: Database
    private var theme: kotlin.String = ""
    private lateinit var statisticsTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        listViewItems = findViewById(R.id.listViewItems)
        statisticsTitle = findViewById(R.id.statisticsTitle)

        theme = intent.getStringExtra("theme").toString()
        database = Database(this)

        fillList()
        statisticsTitle.text = statisticsTitle.text.toString() + if(theme != "")"[" + theme.toString() + "]" else "\uD83C\uDF1F"

        if(theme == ""){
            val headerTheme = findViewById<TextView>(R.id.headerTheme)
            headerTheme.visibility = View.VISIBLE
        }
    }

    private fun fillList(){
        val adapter = ItemAdapter(this, database.getAllPlayers(theme), theme)
        listViewItems.adapter = adapter
    }

    private class ItemAdapter(
        private val context: Context,
        private val itemList: List<PlayerModel>,
        private val theme : kotlin.String) :
        ArrayAdapter<PlayerModel?>(context, 0, itemList) {

        override fun getView(position: Int, @Nullable convertView: View?, parent: ViewGroup): View {

            var convertView = convertView
            if (convertView == null) {
                convertView =
                    LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
            }
            val currentItem = itemList[position]
            val textViewName = convertView!!.findViewById<TextView>(R.id.textViewName)
            val textViewScore = convertView.findViewById<TextView>(R.id.textViewScore)
            val textViewTime = convertView.findViewById<TextView>(R.id.textViewTime)

            textViewName.setText(currentItem.name)
            textViewScore.setText(String.valueOf(currentItem.score))
            textViewTime.setText(String.valueOf(currentItem.time))

            if (theme == "") {
                val textViewTheme = convertView.findViewById<TextView>(R.id.textViewTheme)

                textViewTheme.setText(String.valueOf(currentItem.theme))
                textViewTheme.visibility = View.VISIBLE
            }

            return convertView
        }
    }

    fun deleteAll(view:View){
        database.deleteAllPlayers(theme)
        fillList()

    }
}