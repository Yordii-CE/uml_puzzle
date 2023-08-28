package com.example.umlpuzzle.db

import PlayerModel
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcel
import android.os.Parcelable
import android.content.Context;
import android.database.Cursor
import android.widget.Toast
import java.lang.reflect.Executable


class Database(context:Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_NAME = "uml_puzzle";
        private const val DATABASE_VERSION = 2;
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTblPlayer = ("CREATE TABLE PLAYER (" +
                "id integer primary key," +
                "theme varchar(50)," +
                "name varchar(30)," +
                "time time," +
                "score integer);");
        db?.execSQL(createTblPlayer);

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS PLAYER")
        onCreate(db)
    }

    fun insertPlayer(player: PlayerModel):Long{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put("id", player.id)
        contentValues.put("theme", player.theme)
        contentValues.put("name", player.name)
        contentValues.put("time", player.time)
        contentValues.put("score", player.score)

        val success = db.insert("PLAYER",null, contentValues)
        db.close()
        return success
    }

    fun getAllPlayers(theme:String = ""):ArrayList<PlayerModel>{
        val playersList:ArrayList<PlayerModel> = ArrayList()
        val db = this.readableDatabase

        val query = if(theme != "") "SELECT * FROM PLAYER where score > 0 and theme = '$theme' ORDER BY score DESC, time DESC LIMIT 10"
        else "SELECT * FROM PLAYER where score > 0 ORDER BY score DESC, time DESC LIMIT 10"

        val cursor : Cursor?
        try {
            cursor = db.rawQuery(query, null)
        }catch (e: Exception){
            e.printStackTrace()
            db.execSQL(query)

            return ArrayList()
        }

        val idIndex = cursor.getColumnIndex("id")
        val themeIndex = cursor.getColumnIndex("theme")
        val nameIndex = cursor.getColumnIndex("name")
        val timeIndex = cursor.getColumnIndex("time")
        val scoreIndex = cursor.getColumnIndex("score")

        if (idIndex >= 0 && nameIndex >= 0 && scoreIndex >= 0) {

            while (cursor.moveToNext()) {
                val id = cursor.getInt(idIndex)
                val theme = cursor.getString(themeIndex)
                val name = cursor.getString(nameIndex)
                val time = cursor.getString(timeIndex).split(':')

                val milliseconds = (100 - time[1].toInt())
                val seconds = 30 - time[0].toInt() - if (milliseconds >= 1 && milliseconds <= 99) 1 else 0
                val fullTime = String.format("%02d:%02d%s", seconds, if (milliseconds == 100) 0 else milliseconds, "s")

                val score = cursor.getDouble(scoreIndex)
                val player = PlayerModel(id, theme, name, fullTime, score)
                playersList.add(player)
            }

        }

        cursor.close()
        return playersList
    }

    /*fun updateScorePlayer(name:String?):Int{
        var player = this.getAllPlayers().find { player->player.name == name }
        var newScore = player?.score?.plus(1)
        var id = player?.id

        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("score", newScore)

        val success = db.update("PLAYER", contentValues, "id = $id", null)
        db.close()
        return success
    }*/

    fun deleteAllPlayers(theme:String = ""){
        val db = this.writableDatabase
        db.delete("PLAYER",  if(theme != "")"theme = '$theme'" else "", null);
    }

}