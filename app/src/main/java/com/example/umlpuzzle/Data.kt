package com.example.umlpuzzle
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

fun readJsonData(inputStream: InputStream): List<Diagram> {
    val gson = Gson()
    val reader = BufferedReader(InputStreamReader(inputStream))
    val jsonContent = reader.use { it.readText() }
    return gson.fromJson(jsonContent, Array<Diagram>::class.java).toList()
}

data class Diagram(
    val theme: String,
    val actors: List<Actor>
)

data class Actor(
    val name : String,
    val cases: List<String>,
)