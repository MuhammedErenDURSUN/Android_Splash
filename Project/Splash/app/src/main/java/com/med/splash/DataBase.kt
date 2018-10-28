package com.med.splash

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import android.util.Log
import android.widget.Toast
import java.util.ArrayList


class DataBase(context: Context) : SQLiteOpenHelper(context, Database_name, null, Version) {

    var context=context

   var   CONTENT_URI = Uri.parse("content://"+context.packageName+"/user")

    private lateinit var Database: SQLiteDatabase

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {

        val usertable_query = "CREATE TABLE " + UserTable + " (" +
                UserID + " INTEGER PRIMARY KEY, " +
                userPassword + " TEXT NOT NULL, " +
                deviceToken + " TEXT NOT NULL, " +
                userName + " TEXT NOT NULL, " +
                userMail + " TEXT NOT NULL, " +
                lastLogin + " TEXT NOT NULL, " +
                loginStatus + " TEXT NOT NULL " +
                ")"
        sqLiteDatabase.execSQL(usertable_query)


       val useradd_query ="INSERT INTO user (_id, userPassword, deviceToken, userName, userMail, lastLogin, loginStatus)"+
               " VALUES (1, ' ', ' ', ' ', ' ' , ' ' , ' ')"

        //Login status servera geçecek
        sqLiteDatabase.execSQL(useradd_query)

    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {


    }

    fun Database_Open() {
        Database = writableDatabase
    }

    fun Database_Close() {
        if (Database != null && Database.isOpen)
            Database.close()
    }
     fun Update_User( userPassword:String="",  deviceToken:String="",userName:String="",userMail:String="",lastLogin:String="",loginStatus:String=""): Int {

            val values = ContentValues()
    

            if (!userPassword.equals(""))
            values.put("userPassword",userPassword)

            if (!deviceToken.equals(""))
            values.put("deviceToken",deviceToken)

            if (!userName.equals(""))
            values.put("userName", userName)

    
            if (!userMail.equals(""))
            values.put("usermail",userMail)
    
            if (!lastLogin.equals(""))
            values.put("lastLogin",lastLogin)
    
            if (!loginStatus.equals(""))
            values.put("loginStatus",loginStatus)


         return context.contentResolver.update(CONTENT_URI, values, "_id=1", null)

        }


    fun Data_Read(table:String,field: String, where:String ): String {

        val field_Column= arrayOf<String>(field)
                if (table.equals("user"))
        {
            val sec = "SELECT $field FROM $table WHERE $where "

            Log.i("hata",CONTENT_URI.toString())
            val c:Cursor=context.contentResolver.query(CONTENT_URI,field_Column,null,null,where)

            c.moveToNext()

            return c.getString(c.getColumnIndex(field))
        }
        else
        {
            val sec = "SELECT $field FROM $table WHERE $where "

            val c:Cursor=Database.rawQuery(sec,null)

            c.moveToNext()

            return c.getString(c.getColumnIndex(field))
        }
    }


    companion object {

        private val Database_name = "database"
        private val Version = 1

        val UserTable = "user"
        val UserID = "_id"
        val userPassword= "userPassword"
        val deviceToken = "deviceToken"
        val userName = "userName"
        val userMail = "userMail"
        val lastLogin = "lastLogin"
        val loginStatus = "loginStatus"
    }
}
