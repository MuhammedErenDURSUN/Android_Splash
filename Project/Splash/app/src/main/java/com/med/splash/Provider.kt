package com.med.splash

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.net.Uri


class Provider: ContentProvider() {


    var AUTHORITY:String="com.med.splash"
    var BASE_PATH:String="user"
    var CONTENT_URI:Uri= Uri.parse("content://$AUTHORITY/$BASE_PATH")

    var User:Int=1
  //  var User_ID:Int=2

    private val uriMatcher:UriMatcher=UriMatcher(UriMatcher.NO_MATCH)

    init{
        uriMatcher.addURI(AUTHORITY,BASE_PATH,User)
       // uriMatcher.addURI(AUTHORITY,BASE_PATH+"/#",User_ID)
    }

    var database: SQLiteDatabase?=null


    override fun query(p0: Uri?, p1: Array<out String>?, p2: String?, p3: Array<out String>?, p4: String?): Cursor {

        var cursor:Cursor?=null
        when(uriMatcher.match(p0))
        {
            User ->{
                cursor=database!!.query(DataBase.UserTable,p1,p2,null,null,null,p4)
               //database.execSQL()
            }
            else->{
            }
        }
        cursor!!.setNotificationUri(context.contentResolver,p0)
        return cursor
    }

    override fun onCreate(): Boolean {
        val databaseclass:DataBase= DataBase(context)

        database=databaseclass.writableDatabase
        return true
    }

    override fun update(p0: Uri?, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        var updCount=0

        when(uriMatcher.match(p0))
        {
            User->{
                updCount=database!!.update(DataBase.UserTable,p1,p2,p3)
            }
            else->{
            }
        }

        context.contentResolver.notifyChange(p0,null)
        return updCount
    }

    override fun delete(p0: Uri?, p1: String?, p2: Array<out String>?): Int {

        var delCount=0

        when(uriMatcher.match(p0))
        {
            User->{
                delCount=database!!.delete(DataBase.UserTable,p1,p2)
            }
            else->{
            }
        }

        context.contentResolver.notifyChange(p0,null)
        return delCount
    }

    override fun getType(p0: Uri?): String? {

        when(uriMatcher.match(p0))
        {
            User->{
                return "vnd.android.cursor.dir/"+BASE_PATH
            }
            else->{
                return null
            }
        }

    }

    override fun insert(p0: Uri?, p1: ContentValues?): Uri? {

        var id:Long=database!!.insert(DataBase.UserTable,null,p1)

        if (id>0)
        {
            var _uri:Uri=ContentUris.withAppendedId(CONTENT_URI,id)
            context.contentResolver.notifyChange(_uri,null)
            return _uri

        }
        else
           return null
    }



}