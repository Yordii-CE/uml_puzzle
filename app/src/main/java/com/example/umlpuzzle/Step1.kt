package com.example.umlpuzzle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class Step1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step1)
    }

    fun toMain(view: View){
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun toRegister(view: View){
        var intent = Intent(this, Register::class.java)
        startActivity(intent)
    }
}