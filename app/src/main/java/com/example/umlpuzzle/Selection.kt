package com.example.umlpuzzle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

class Selection : AppCompatActivity() {
    private var name : String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)

        name = intent.getStringExtra("name")
    }

    fun toTheme3(view: View){
        var intent = Intent(this, Theme3::class.java)
        intent.putExtra("name", name)
        startActivity(intent)
    }
    fun toTheme2(view: View){
        var intent = Intent(this, Theme2::class.java)
        intent.putExtra("name", name)
        startActivity(intent)
    }
    fun toTheme1(view: View){
        var intent = Intent(this, Theme1::class.java)
        intent.putExtra("name", name)
        startActivity(intent)
    }

    fun toStatistics(view: View){
        var intent = Intent(this, Statistics::class.java)
        intent.putExtra("theme", "")

        startActivity(intent)
    }
}