package com.example.umlpuzzle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText

class Register : AppCompatActivity() {
    private lateinit var name : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        name = findViewById(R.id.name)
    }

    fun toSelection(view: View){
        if(name.text.toString() != ""){
            var intent = Intent(this, Selection::class.java)
            intent.putExtra("name", name.text.toString())
            startActivity(intent)
        }
    }
}